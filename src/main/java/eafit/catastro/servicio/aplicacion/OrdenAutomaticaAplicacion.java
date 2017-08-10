package eafit.catastro.servicio.aplicacion;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMethodCall;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

import java.net.URI;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;

import eafit.catastro.controlador.PagoControlador;
import eafit.catastro.excepcion.ObjetoNoEncontradoExcepcion;
import eafit.catastro.modelo.Item;
import eafit.catastro.modelo.Notificacion;
import eafit.catastro.modelo.Orden;
import eafit.catastro.modelo.Usuario;
import eafit.catastro.servicio.externo.modelo.CertificadoCatastro;

@RestController
public class OrdenAutomaticaAplicacion extends AplicacionBase {

	private Logger logger = Logger.getLogger(OrdenAutomaticaAplicacion.class.getName());

	@Autowired
	private ConfiguracionServiciosDominio config;

	@Autowired
	private RestTemplate cliente;

	@Autowired
	private ObjectMapper objectMapper;

	@PostMapping("/ordenes")
	public void generarOrden(
			@RequestParam(value = "tipoDocumento", required = true) String tipoDocumento,
			@RequestParam(value = "numeroDocumento", required = true) String numeroDocumento) {
		// Validar usuario existente
		Usuario usuario = validarUsuario(tipoDocumento, numeroDocumento);

		// Consultar certificados
		CertificadoCatastro[] certificados = consultarCertificados(tipoDocumento, numeroDocumento);

		// Generar orden
		URI ordenCreada = generarOrden(tipoDocumento, numeroDocumento, certificados);

		// Notificar
		notificarUsuario(usuario, ordenCreada);
	}

	/**
	 * Validar Usuario
	 */
	private Usuario validarUsuario(String tipoDocumento, String numeroDocumento) {
		Usuario usuario = cliente.getForObject(
				config.getSeguridad().getUrl() + "/usuarios?tipoDocumento={tipoDocumento}&numeroDocumento={numeroDocumento}",
				Usuario.class,
				new ImmutableMap.Builder<String, Object>()
					.put("tipoDocumento", tipoDocumento)
					.put("numeroDocumento", numeroDocumento)
					.build());

		if (usuario == null) {
			throw new ObjetoNoEncontradoExcepcion(String.format(
					"Usuario tipoDocumento: '%s' numeroDocumento: '%s' no encontrado", tipoDocumento, numeroDocumento));
		}
		return usuario;
	}

	/**
	 * Consultar certificados
	 */
	private CertificadoCatastro[] consultarCertificados(String tipoDocumento, String numeroDocumento) {
		CertificadoCatastro[] certificados = cliente.getForObject(
				config.getCatastro().getUrl() + "/certificados?tipoDocumento={tipoDocumento}&numeroDocumento={numeroDocumento}",
				CertificadoCatastro[].class,
				new ImmutableMap.Builder<String, Object>()
					.put("tipoDocumento", tipoDocumento)
					.put("numeroDocumento", numeroDocumento)
					.build());

		if (certificados.length == 0) {
			throw new ObjetoNoEncontradoExcepcion(String.format(
					"No se encontraron certificados para el usuario tipoDocumento: '%s' numeroDocumento: '%s'",
					tipoDocumento, numeroDocumento));
		}
		return certificados;
	}

	/**
	 * Crear orden, agregar items, cerrar orden
	 */
	private URI generarOrden(String tipoDocumento, String numeroDocumento, CertificadoCatastro[] certificados) {
		// Abrir orden
		URI ordenCreada = cliente.postForLocation(
				config.getFacturacion().getUrl() + "/ordenes?tipoDocumento={tipoDocumento}&numeroDocumento={numeroDocumento}",
				null, new ImmutableMap.Builder<String, Object>()
					.put("tipoDocumento", tipoDocumento)
					.put("numeroDocumento", numeroDocumento)
					.build());

		// Agregar items
		for (CertificadoCatastro certificado : certificados) {
			Item item = new Item();
			item.setMunicipio(certificado.getMunicipio());
			item.setNumeroFicha(certificado.getNumeroFicha());
			item.setTipoCertificado("catastro1");
			item.setCantidad(1);
			cliente.postForEntity(ordenCreada.toString() + "/items", item, Object.class);
		}

		// Cerrar orden
		cliente.postForEntity(ordenCreada.toString() + "/cierre", null, Object.class);
		return ordenCreada;
	}

	/**
	 * Notificar usuario por correo
	 */
	private void notificarUsuario(Usuario usuario, URI ordenCreada) {
		Orden orden = cliente.getForObject(ordenCreada.toString(), Orden.class);

		String mensaje;
		try {
			String ordenMensaje = objectMapper.writeValueAsString(orden);
			StringBuilder mensajeBuilder = new StringBuilder();
			mensajeBuilder.append("{");
			mensajeBuilder.append("\"orden\":").append(ordenMensaje).append(",");
			mensajeBuilder.append("\"pagoUri\":\"").append(getMostrarPaginaPagoUri(orden)).append("\"");
			mensajeBuilder.append("}");
			mensaje = mensajeBuilder.toString();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		logger.info("Notificando usuario con mensaje: " + mensaje);
		Notificacion notificacion = new Notificacion();
		notificacion.setReceptor(usuario.getCodigo());
		notificacion.setPlantilla("orden_generada");
		notificacion.setMensaje(mensaje);
		notificacion.setTipoMensaje("json");
		cliente.postForEntity(config.getNotificacion().getUrl() + "/correo", notificacion, Object.class);
	}

	private String getMostrarPaginaPagoUri(Orden orden) {
		return fromMethodCall(on(PagoControlador.class).mostrarPaginaPago(orden.getId())).build().toString();
	}

}
