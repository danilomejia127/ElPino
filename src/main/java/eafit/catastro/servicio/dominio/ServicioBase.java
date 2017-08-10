package eafit.catastro.servicio.dominio;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.common.io.ByteStreams;

import eafit.catastro.excepcion.ObjetoNoEncontradoExcepcion;
import eafit.catastro.excepcion.PeticionInvalidadExcepcion;

@RequestMapping("/api")
public abstract class ServicioBase {

	private Logger logger = Logger.getLogger(ServicioBase.class.getName());

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

	protected ResponseEntity<byte[]> crearRespuestaPdf(byte[] contenido, String nombreArchivo) {
		HttpHeaders cabeceras = new HttpHeaders();
		cabeceras.setContentType(MediaType.parseMediaType("application/pdf"));
		cabeceras.add("Content-Disposition", "inline; filename=\"" + nombreArchivo + "\"");
		cabeceras.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		return new ResponseEntity<byte[]>(contenido, cabeceras, HttpStatus.OK);
	}

	protected byte[] leerArchivo(String rutaArchivo) {
		try (InputStream in = getClass().getClassLoader().getResourceAsStream(rutaArchivo)) {
			return ByteStreams.toByteArray(in);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
