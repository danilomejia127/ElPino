package eafit.catastro.servicio.aplicacion;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.ImmutableMap;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;

import eafit.catastro.excepcion.ObjetoNoEncontradoExcepcion;
import eafit.catastro.excepcion.PeticionInvalidadExcepcion;
import eafit.catastro.modelo.Item;
import eafit.catastro.modelo.Orden;

@RestController
public class DescargarCertificadosAplicacion extends AplicacionBase {

	@Autowired
	private ConfiguracionServiciosDominio config;

	@Autowired
	private RestTemplate cliente;

	@RequestMapping(value = "/ordenes/{id}/certificados/pdf", method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseEntity<byte[]> descargarOrden(@PathVariable("id") Long ordenId) {
		// Consultar orden
		Orden orden = buscarOrden(ordenId);

		// Generar certificados
		List<byte[]> certificados = generarCertificados(orden);

		// Unir PDFs
		byte[] contenido = unirPdfs(certificados);

		// Generar respuesta
		return crearRespuestaPdf(contenido, "certificados_orden_" + ordenId + "_" + System.currentTimeMillis() + ".pdf");
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
		if (!orden.getEstado().equals("PAGADA")) {
			throw new PeticionInvalidadExcepcion("La orden No. " + ordenId + " no se encuentra en estado PAGADA");
		}
		return orden;
	}

	/**
	 * Genera certificado por item
	 */
	private List<byte[]> generarCertificados(Orden orden) {
		List<byte[]> certificados = new ArrayList<>();
		List<Item> items = orden.getItems();
		for (Item item : items) {
			byte[] certificado = cliente.postForObject(
					config.getCatastro().getUrl()
							+ "/certificados/pdf"
							+ "?municipio={municipio}"
							+ "&numeroFicha={numeroFicha}"
							+ "&tipoCertificado={tipoCertificado}",
					null, byte[].class,
					new ImmutableMap.Builder<String, Object>()
						.put("municipio", item.getMunicipio())
						.put("numeroFicha", item.getNumeroFicha())
						.put("tipoCertificado", item.getTipoCertificado())
						.build());
			
			certificados.add(certificado);
		}
		return certificados;
	}

	/**
	 * Une los certificados en un solo documento
	 */
	private byte[] unirPdfs(List<byte[]> originales) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Document document = new Document();
			PdfCopy pdf = new PdfCopy(document, out);
			document.open();

			for (byte[] original : originales) {
				PdfReader reader = new PdfReader(original);
				for (int i = 0; i < reader.getNumberOfPages(); i++) {
					pdf.addPage(pdf.getImportedPage(reader, i + 1));
				}
				pdf.freeReader(reader);
				reader.close();
			}

			document.close();
			return out.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
