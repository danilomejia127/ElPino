package eafit.catastro.servicio.dominio;

import java.io.StringWriter;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import eafit.catastro.excepcion.ObjetoNoEncontradoExcepcion;
import eafit.catastro.excepcion.PeticionInvalidadExcepcion;
import eafit.catastro.modelo.Notificacion;
import eafit.catastro.modelo.PlantillaNotificacion;
import eafit.catastro.modelo.Usuario;
import eafit.catastro.repositorio.NotificacionRepositorio;
import eafit.catastro.repositorio.PlantillaNotificacionRepositorio;
import eafit.catastro.repositorio.UsuarioRepositorio;

@RestController
public class NotificacionServicio extends ServicioBase {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;

	@Autowired
	private NotificacionRepositorio notificacionRepositorio;

	@Autowired
	private PlantillaNotificacionRepositorio plantillaRepositorio;

	@Autowired
	private ObjectMapper objectMapper;

	@GetMapping("/notificacion/correo")
	public List<Notificacion> buscarNotificacionesPorCorreo() {
		return notificacionRepositorio.findAll();
	}

	@PostMapping("/notificacion/correo")
	public void notificarPorCorreo(@RequestBody Notificacion notificacion) {
		Usuario usuario = buscarUsuario(notificacion);
		PlantillaNotificacion plantilla = buscarPlantilla(notificacion);

		MimeMessagePreparator preparadorMensaje = mime -> {
			Object mensaje = getMensaje(notificacion);
			String texto = generarTextoMensaje(plantilla, mensaje, usuario);
			MimeMessageHelper mail = new MimeMessageHelper(mime);
			mail.setTo(usuario.getCorreo());
			mail.setSubject(plantilla.getDescripcion());
			mail.setText(texto, true);
		};

		mailSender.send(preparadorMensaje);
		notificacionRepositorio.save(notificacion);
	}

	private Usuario buscarUsuario(Notificacion notificacion) {
		String receptor = notificacion.getReceptor();
		Usuario usuario = usuarioRepositorio.findOne(receptor);
		if (usuario == null) {
			throw new ObjetoNoEncontradoExcepcion("Usuario '" + receptor + "' no encontrado.");
		}
		return usuario;
	}

	private PlantillaNotificacion buscarPlantilla(Notificacion notificacion) {
		String tipoPlantilla = notificacion.getPlantilla();
		PlantillaNotificacion plantilla = plantillaRepositorio.findByTipo(tipoPlantilla);
		if (plantilla == null) {
			throw new ObjetoNoEncontradoExcepcion("Tipo de plantilla '" + tipoPlantilla + "' no encontrado.");
		}
		return plantilla;
	}

	private Object getMensaje(Notificacion notificacion) {
		Object mensaje;
		if (notificacion.getTipoMensaje().equalsIgnoreCase("json")) {
			try {
				mensaje = objectMapper.readValue(notificacion.getMensaje(), Object.class);
			} catch (Exception e) {
				throw new PeticionInvalidadExcepcion(
						"Mensaje invalido para el tipo de dato esperado " + notificacion.getTipoMensaje());
			}
		} else {
			mensaje = notificacion.getMensaje();
		}
		return mensaje;
	}

	private String generarTextoMensaje(PlantillaNotificacion plantilla, Object mensaje, Usuario usuario) {
		StringWriter texto = new StringWriter();
		VelocityContext contexto = new VelocityContext();
		contexto.put("usuario", usuario);
		contexto.put("mensaje", mensaje);
		Velocity.evaluate(contexto, texto, plantilla.getTipo(), plantilla.getPlantilla());
		return texto.toString();
	}

}
