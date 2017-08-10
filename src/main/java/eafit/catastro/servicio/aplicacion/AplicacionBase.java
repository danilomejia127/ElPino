package eafit.catastro.servicio.aplicacion;

import java.util.logging.Logger;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

import eafit.catastro.excepcion.ObjetoNoEncontradoExcepcion;
import eafit.catastro.excepcion.PeticionInvalidadExcepcion;

@RequestMapping("/aplicacion")
public abstract class AplicacionBase {

	private Logger logger = Logger.getLogger(AplicacionBase.class.getName());

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ObjetoNoEncontradoExcepcion.class)
	public String manejarObjetoNoEncontradoExcepcion(ObjetoNoEncontradoExcepcion e) {
		logger.warning(e.getClass().getName() + ": " + e.getMessage());
		return e.getMessage();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(PeticionInvalidadExcepcion.class)
	public String manejarPeticionInvalidaExcepcion(PeticionInvalidadExcepcion e) {
		logger.warning(e.getClass().getName() + ": " + e.getMessage());
		return e.getMessage();
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(HttpClientErrorException.class)
	public String manejarHttpClientErrorException(HttpClientErrorException e) {
		logger.warning(e.getClass().getName() + ": " + e.getMessage());
		return "Servicio externo retorno HTTP " + e.getStatusCode() + " " + e.getStatusText() + "\n"
				+ new String(e.getResponseBodyAsByteArray());
	}

	protected ResponseEntity<byte[]> crearRespuestaPdf(byte[] contenido, String nombreArchivo) {
		HttpHeaders cabeceras = new HttpHeaders();
		cabeceras.setContentType(MediaType.parseMediaType("application/pdf"));
		cabeceras.add("Content-Disposition", "inline; filename=\"" + nombreArchivo + "\"");
		cabeceras.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		return new ResponseEntity<byte[]>(contenido, cabeceras, HttpStatus.OK);
	}

}
