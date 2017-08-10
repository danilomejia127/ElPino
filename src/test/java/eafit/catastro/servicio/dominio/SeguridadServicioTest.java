package eafit.catastro.servicio.dominio;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import eafit.catastro.MockMvcTest;

public class SeguridadServicioTest extends MockMvcTest {

	@Test
	public void debeListarTodosLosUsuarios() throws Exception {
		super.mockMvc.perform(get("/api/seguridad/usuarios").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0].codigo", is("admin")))
			.andExpect(jsonPath("$[0].nombres", is("Admin")))
			.andExpect(jsonPath("$[0].apellidos", is("Admin")))
			.andDo(print());
	}

	@Test
	public void debeBuscarUsuarioPorTipoDocumento() throws Exception {
		super.mockMvc.perform(
				get("/api/seguridad/usuarios")
				.param("tipoDocumento", "CC")
				.param("numeroDocumento", "123123")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.codigo", is("admin")))
			.andExpect(jsonPath("$.nombres", is("Admin")))
			.andExpect(jsonPath("$.apellidos", is("Admin")))
			.andDo(print());
	}

}
