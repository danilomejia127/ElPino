package eafit.catastro.controlador;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMethodCall;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.ImmutableMap;

import eafit.catastro.modelo.Orden;
import eafit.catastro.servicio.aplicacion.ConfiguracionServiciosDominio;
import eafit.catastro.servicio.aplicacion.DescargarCertificadosAplicacion;
import eafit.catastro.servicio.aplicacion.PagarOrdenAplicacion;

@Controller
public class PagoControlador {

	@Autowired
	private ConfiguracionServiciosDominio config;

	@Autowired
	private RestTemplate cliente;

	@GetMapping("/ordenes/{id}/pago")
	public ModelAndView mostrarPaginaPago(@PathVariable("id") Long ordenId) {
		ModelAndView modelo = new ModelAndView("pago");
		Orden orden = buscarOrden(ordenId);
		if (orden == null) {
			modelo.addObject("error", "Orden No. " + ordenId + " no encontrada");
			return modelo;
		}
		if (!orden.getEstado().equals("CERRADA")) {
			modelo.addObject("error", "La orden No. " + ordenId + " debe estar en estado CERRADA. El estado actual es "
					+ orden.getEstado());
			return modelo;
		}

		modelo.addObject("orden", orden);
		modelo.addObject("pagoUrl", getPaginaRealizarPagoUri(orden));
		return modelo;
	}

	@PostMapping("/ordenes/{id}/pago")
	public ModelAndView realizarPago(@PathVariable("id") Long ordenId) {
		ModelAndView modelo = new ModelAndView("resultado-pago");
		Orden orden = buscarOrden(ordenId);
		if (orden == null) {
			modelo.addObject("error", "Orden No. " + ordenId + " no encontrada");
			return modelo;
		}
		if (!orden.getEstado().equals("CERRADA")) {
			modelo.addObject("error", "La orden No. " + ordenId + " debe estar en estado CERRADA. El estado actual es "
					+ orden.getEstado());
			return modelo;
		}

		String estado = cliente.postForObject(getServicioPagosUri(orden), null, String.class);
		if (!estado.equals("PROCESADO")) {
			modelo.addObject("error", "Error procesando pago");
		}

		modelo.addObject("mensaje", "Pago procesado correctamente");
		modelo.addObject("pdfUrl", getServicioDocumentosOrdenUri(orden));
		return modelo;
	}

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
		return orden;
	}

	private String getPaginaRealizarPagoUri(Orden orden) {
		return fromMethodCall(on(PagoControlador.class).realizarPago(orden.getId())).build().toString();
	}

	private String getServicioPagosUri(Orden orden) {
		return fromMethodCall(on(PagarOrdenAplicacion.class).pagarOrden(orden.getId())).build().toString();
	}

	private String getServicioDocumentosOrdenUri(Orden orden) {
		return fromMethodCall(on(DescargarCertificadosAplicacion.class).descargarOrden(orden.getId())).build()
				.toString();
	}

}
