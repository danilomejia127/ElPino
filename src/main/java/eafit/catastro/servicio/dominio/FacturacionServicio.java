package eafit.catastro.servicio.dominio;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMethodCall;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;

import eafit.catastro.excepcion.ObjetoNoEncontradoExcepcion;
import eafit.catastro.excepcion.PeticionInvalidadExcepcion;
import eafit.catastro.modelo.Item;
import eafit.catastro.modelo.Orden;
import eafit.catastro.modelo.Usuario;
import eafit.catastro.repositorio.ItemRepositorio;
import eafit.catastro.repositorio.OrdenRepositorio;
import eafit.catastro.repositorio.UsuarioRepositorio;
import eafit.catastro.servicio.externo.CatastroServicioExterno;

@RestController
@Transactional
public class FacturacionServicio extends ServicioBase {

	private static final String ORDEN_ABIERTA = "ABIERTA";

	private static final String ORDEN_CERRADA = "CERRADA";

	private static final String ORDEN_PAGADA = "PAGADA";

	@Autowired
	private OrdenRepositorio ordenRepositorio;

	@Autowired
	private ItemRepositorio itemRepositorio;

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;

	@Autowired
	private CatastroServicioExterno catastroServicioExterno;

	@GetMapping("/facturacion/ordenes")
	public List<Orden> buscarOrdenes() {
		return ordenRepositorio.findAll();
	}

	@GetMapping("/facturacion/ordenes/{id}")
	public Orden buscarOrden(@PathVariable("id") Long ordenId) {
		Orden orden = ordenRepositorio.findOne(ordenId);
		if (orden == null) {
			throw new ObjetoNoEncontradoExcepcion("Orden No. " + ordenId + " no encontrada");
		}
		return orden;
	}

	@PostMapping("/facturacion/ordenes")
	public ResponseEntity<?> crearOrden(String tipoDocumento, String numeroDocumento) {
		Usuario usuario = buscarUsuario(tipoDocumento, numeroDocumento);
		Orden orden = new Orden();
		orden.setUsuario(usuario);
		orden.setValorTotal(BigDecimal.ZERO);
		orden.setEstado(ORDEN_ABIERTA);
		orden = ordenRepositorio.save(orden);

		UriComponents uriComponents = uri(orden);
		return ResponseEntity.created(uriComponents.toUri()).build();
	}

	@PostMapping("/facturacion/ordenes/{id}/cierre")
	public void cerrarOrden(@PathVariable("id") Long ordenId) {
		Orden orden = buscarOrden(ordenId);
		validarEstadoOrden(orden, ORDEN_ABIERTA);
		orden.setEstado(ORDEN_CERRADA);
		ordenRepositorio.save(orden);
	}

	@PostMapping("/facturacion/ordenes/{id}/pago")
	public void pagarOrden(@PathVariable("id") Long ordenId) {
		Orden orden = buscarOrden(ordenId);
		validarEstadoOrden(orden, ORDEN_CERRADA);
		orden.setEstado(ORDEN_PAGADA);
		ordenRepositorio.save(orden);
	}

	@DeleteMapping("/facturacion/ordenes/{id}")
	public String eliminarOrden(@PathVariable("id") Long ordenId) {
		Orden orden = buscarOrden(ordenId);
		validarEstadoOrden(orden, ORDEN_ABIERTA);
		ordenRepositorio.delete(orden);
		return "Orden eliminada";
	}

	@GetMapping("/facturacion/items/{id}")
	public Item buscarItem(@PathVariable("id") Long itemId) {
		Item item = itemRepositorio.findOne(itemId);
		if (item == null) {
			throw new ObjetoNoEncontradoExcepcion("Item '" + itemId + "' no encontrado");
		}
		return item;
	}

	@PostMapping("/facturacion/ordenes/{id}/items")
	@ResponseStatus(HttpStatus.CREATED)
	public void crearItem(@PathVariable("id") Long ordenId, @RequestBody Item item) {
		Orden orden = buscarOrden(ordenId);
		validarEstadoOrden(orden, ORDEN_ABIERTA);
		BigDecimal valorCertificado = consultarValorCertificado(item);
		item.setValorUnidad(valorCertificado);
		item.setValorTotal(valorCertificado.multiply(new BigDecimal(item.getCantidad())));
		orden.addItem(item);
		ordenRepositorio.save(orden);
		ordenRepositorio.recalcularValorTotal(ordenId);
	}

	@DeleteMapping("/facturacion/items/{id}")
	public String eliminarItem(@PathVariable("id") Long itemId) {
		Item item = buscarItem(itemId);
		Orden orden = item.getOrden();
		validarEstadoOrden(orden, ORDEN_ABIERTA);
		orden.removeItem(item);
		ordenRepositorio.save(orden);
		ordenRepositorio.recalcularValorTotal(orden.getId());
		return "Item eliminado";
	}

	private Orden validarEstadoOrden(Orden orden, String estado) {
		if (!orden.getEstado().equals(estado)) {
			throw new PeticionInvalidadExcepcion("La orden '" + orden.getId() + "' se encuentra en estado invalido '"
					+ orden.getEstado() + "'. Se esperaba estado '" + estado + "'");
		}
		return orden;
	}

	private Usuario buscarUsuario(String tipoDocumento, String numeroDocumento) {
		Usuario usuario = usuarioRepositorio.buscarPorTipoDocumento(tipoDocumento, numeroDocumento);
		if (usuario == null) {
			throw new ObjetoNoEncontradoExcepcion(String.format(
					"Usuario tipoDocumento: '%s' numeroDocumento: '%s' no encontrado", tipoDocumento, numeroDocumento));
		}
		return usuario;
	}

	private BigDecimal consultarValorCertificado(Item item) {
		BigDecimal valorCertificado = catastroServicioExterno.consultarValorCertificado(item.getTipoCertificado());
		if (valorCertificado == null) {
			throw new ObjetoNoEncontradoExcepcion(
					"Tipo de certificado '" + item.getTipoCertificado() + "' no registrado");
		}
		return valorCertificado;
	}

	private UriComponents uri(Orden orden) {
		return fromMethodCall(on(FacturacionServicio.class).buscarOrden(orden.getId())).build();
	}

}
