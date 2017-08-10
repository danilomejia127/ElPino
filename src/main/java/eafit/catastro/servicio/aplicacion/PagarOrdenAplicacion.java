package eafit.catastro.servicio.aplicacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.ImmutableMap;

import eafit.catastro.excepcion.ObjetoNoEncontradoExcepcion;
import eafit.catastro.excepcion.PeticionInvalidadExcepcion;
import eafit.catastro.modelo.Orden;

@RestController
public class PagarOrdenAplicacion extends AplicacionBase {

	@Autowired
	private ConfiguracionServiciosDominio config;

	@Autowired
	private RestTemplate cliente;

	@PostMapping("/ordenes/{id}/pago")
	public ResponseEntity<String> pagarOrden(@PathVariable("id") Long ordenId) {
		// Consultar orden
		Orden orden = buscarOrden(ordenId);

		// Ejecutar pago y retorna el estado del proceso
		String estado = ejecutarPago(orden);

		// Cambiar estado de la orden
		if (estado.equals("PROCESADO")) {
			cambiarEstadoOrden(orden);
			return ResponseEntity.ok("PROCESADO");
		}

		return ResponseEntity.ok("PAGO_INVALIDO");
	}

	/**
	 * Consulta orden
	 */
	private Orden buscarOrden(Long ordenId) {
		Orden orden = null;
		try {
			orden = cliente.getForObject(
					config.getFacturacion().getUrl() + "/ordenes/{id}",
					Orden.class,
					new ImmutableMap.Builder<String, Object>()
						.put("id", ordenId)
						.build());
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
				throw e;
			}
		}

		if (orden == null) {
			throw new ObjetoNoEncontradoExcepcion("Orden No. " + ordenId + " no encontrada");
		}
		if (!orden.getEstado().equals("CERRADA")) {
			throw new PeticionInvalidadExcepcion("La orden No. " + ordenId + " no se encuentra en estado CERRADA");
		}
		return orden;
	}

	/**
	 * Ejecuta el pago
	 */
	private String ejecutarPago(Orden orden) {
		// Obtener referencia pago
		String referenciaPago = cliente.postForObject(config.getPagos().getUrl()
				+ "/parametrizar"
				+ "?tipoDocumento={tipoDocumento}"
				+ "&numeroDocumento={numeroDocumento}"
				+ "&valor={valor}",
				null, String.class,
				new ImmutableMap.Builder<String, Object>()
					.put("tipoDocumento", orden.getUsuario().getTipoDocumento())
					.put("numeroDocumento", orden.getUsuario().getNumeroDocumento())
					.put("valor", orden.getValorTotal().intValue())
					.build());

		// Ejecutar pago
		cliente.postForObject(config.getPagos().getUrl()
				+ "/ejecutarPago?referencia={referencia}",
				null, String.class,
				new ImmutableMap.Builder<String, Object>()
					.put("referencia", referenciaPago)
					.build());

		// Consultar estado del pago
		String estado = cliente.getForObject(config.getPagos().getUrl()
				+ "/consultarEstadoPago?referencia={referencia}",
				String.class,
				new ImmutableMap.Builder<String, Object>()
					.put("referencia", referenciaPago)
					.build());

		return estado;
	}

	/**
	 * Cambiar estado de la orden
	 */
	private void cambiarEstadoOrden(Orden orden) {
		cliente.postForObject(config.getFacturacion().getUrl()
				+ "/ordenes/{id}/pago",
				null, Object.class,
				new ImmutableMap.Builder<String, Object>()
					.put("id", orden.getId())
					.build());
	}

}
