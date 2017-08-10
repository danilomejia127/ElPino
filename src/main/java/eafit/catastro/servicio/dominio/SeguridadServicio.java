package eafit.catastro.servicio.dominio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import eafit.catastro.modelo.Usuario;
import eafit.catastro.repositorio.UsuarioRepositorio;

@RestController
public class SeguridadServicio extends ServicioBase {

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;

	@GetMapping("/seguridad/usuarios")
	public List<Usuario> listarUsuarios() {
		return usuarioRepositorio.findAll();
	}

	@GetMapping(value = "/seguridad/usuarios", params = "tipoDocumento")
	public Usuario buscarUsuario(
			@RequestParam("tipoDocumento") String tipoDocumento,
			@RequestParam("numeroDocumento") String numeroDocumento) {
		return usuarioRepositorio.buscarPorTipoDocumento(tipoDocumento, numeroDocumento);
	}

	@PostMapping("/seguridad/usuarios")
	@ResponseStatus(HttpStatus.CREATED)
	public void registrarUsuario(@RequestBody Usuario usuario) {
		usuarioRepositorio.save(usuario);
	}

}
