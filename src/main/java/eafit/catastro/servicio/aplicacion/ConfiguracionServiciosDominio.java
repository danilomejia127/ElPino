package eafit.catastro.servicio.aplicacion;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "configuracion.servicio.dominio")
public class ConfiguracionServiciosDominio {

	private Seguridad seguridad;
	private Notificacion notificacion;
	private Catastro catastro;
	private Facturacion facturacion;
	private Pagos pagos;

	public Seguridad getSeguridad() {
		return seguridad;
	}

	public void setSeguridad(Seguridad seguridad) {
		this.seguridad = seguridad;
	}

	public Notificacion getNotificacion() {
		return notificacion;
	}

	public void setNotificacion(Notificacion notificacion) {
		this.notificacion = notificacion;
	}

	public Catastro getCatastro() {
		return catastro;
	}

	public void setCatastro(Catastro catastro) {
		this.catastro = catastro;
	}

	public Facturacion getFacturacion() {
		return facturacion;
	}

	public void setFacturacion(Facturacion facturacion) {
		this.facturacion = facturacion;
	}

	public Pagos getPagos() {
		return pagos;
	}

	public void setPagos(Pagos pagos) {
		this.pagos = pagos;
	}

	public static class Seguridad {

		private String url;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

	}

	public static class Notificacion {

		private String url;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

	}

	public static class Catastro {

		private String url;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

	}

	public static class Facturacion {

		private String url;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

	}

	public static class Pagos {

		private String url;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

	}

}
