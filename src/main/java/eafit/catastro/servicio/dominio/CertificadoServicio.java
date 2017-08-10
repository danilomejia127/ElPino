package eafit.catastro.servicio.dominio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;

import eafit.catastro.excepcion.ObjetoNoEncontradoExcepcion;
import eafit.catastro.modelo.Predio;
import eafit.catastro.repositorio.PredioRepositorio;
import eafit.catastro.servicio.externo.CatastroServicioExterno;
import eafit.catastro.servicio.externo.modelo.CertificadoCatastro;

@RestController
public class CertificadoServicio extends ServicioBase {

	@Autowired
	private CatastroServicioExterno catastroServicioExterno;

	@Autowired
	private PredioRepositorio predioRepositorio;

	@GetMapping("/catastro/certificados")
	public List<CertificadoCatastro> buscarCertificados(
			@RequestParam("tipoDocumento") String tipoDocumento,
			@RequestParam("numeroDocumento") String numeroDocumento) {
		return catastroServicioExterno.consultarCertificados(tipoDocumento, numeroDocumento);
	}

	@RequestMapping(value = "/catastro/certificados/pdf", method = {RequestMethod.POST, RequestMethod.GET})
	public ResponseEntity<byte[]> generarCertificado(
			@RequestParam("municipio") String municipio,
			@RequestParam("numeroFicha") String numeroFicha,
			@RequestParam("tipoCertificado") String tipoCertificado) {
		String rutaArchivo = generarArchivoExterno(municipio, numeroFicha, tipoCertificado);
		byte[] contenido = leerArchivo(rutaArchivo);
		return crearRespuestaPdf(contenido, municipio + "-" + tipoCertificado + "-" + numeroFicha + ".pdf");
	}

	private String generarArchivoExterno(String municipio, String numeroFicha, String tipoCertificado) {
		String rutaArchivo = catastroServicioExterno.generarCertificado(municipio, numeroFicha, tipoCertificado);
		if (Strings.isNullOrEmpty(rutaArchivo)) {
			throw new ObjetoNoEncontradoExcepcion(String.format(
					"Certificado no encontrado para municipio: '%s' numeroFicha: '%s' tipoCertificado: '%s'",
					municipio, numeroFicha, tipoCertificado));
		}
		return rutaArchivo;
	}

	@GetMapping("/catastro/predios")
	public List<Predio> listarPredios() {
		return predioRepositorio.findAll();
	}

	@GetMapping(value = "/catastro/predios", params = "tipoDocUsuario")
	public List<Predio> buscarPredios(
			@RequestParam("tipoDocUsuario") String tipoDocUsuario,
			@RequestParam("idPropietario") String idPropietario) {
		return predioRepositorio.buscarPredioPorPropietario(tipoDocUsuario, idPropietario);
	}

}
