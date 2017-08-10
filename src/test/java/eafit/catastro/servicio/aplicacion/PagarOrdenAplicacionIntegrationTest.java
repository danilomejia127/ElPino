package eafit.catastro.servicio.aplicacion;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eafit.catastro.MockMvcTest;
import eafit.catastro.modelo.Orden;
import eafit.catastro.modelo.Usuario;
import eafit.catastro.repositorio.OrdenRepositorio;
import eafit.catastro.repositorio.UsuarioRepositorio;

public class PagarOrdenAplicacionIntegrationTest extends MockMvcTest {

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;

	@Autowired
	private OrdenRepositorio ordenRepositorio;

	@Test
	public void debePagarOrdenCerrada() throws Exception {
		Usuario usuario = usuarioRepositorio.findOne("admin");
		Orden orden = new Orden();
		orden.setEstado("CERRADA");
		orden.setUsuario(usuario);
		orden.setValorTotal(new BigDecimal(10));
		orden = ordenRepositorio.save(orden);

		super.mockMvc.perform(post("/aplicacion/ordenes/" + orden.getId() + "/pago"))
				.andExpect(status().isOk())
				.andExpect(content().string("PROCESADO"))
				.andDo(print());

		orden = ordenRepositorio.findOne(orden.getId());
		assertEquals("PAGADA", orden.getEstado());
		ordenRepositorio.delete(orden.getId());
	}

	@Test
	public void debeRecharOrdenNoCerrada() throws Exception {
		Usuario usuario = usuarioRepositorio.findOne("admin");
		Orden orden = new Orden();
		orden.setEstado("ABIERTA");
		orden.setUsuario(usuario);
		orden.setValorTotal(new BigDecimal(10));
		orden = ordenRepositorio.save(orden);

		super.mockMvc.perform(post("/aplicacion/ordenes/" + orden.getId() + "/pago"))
				.andExpect(status().isBadRequest())
				.andDo(print());

		orden = ordenRepositorio.findOne(orden.getId());
		assertEquals("ABIERTA", orden.getEstado());
		ordenRepositorio.delete(orden.getId());
	}

}
