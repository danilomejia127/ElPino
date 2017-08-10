package eafit.catastro;

import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

import eafit.catastro.servicio.aplicacion.ConfiguracionServiciosDominio;

public class ConfigTest {

	@Bean
	public ApplicationListener<EmbeddedServletContainerInitializedEvent> createConfigSetter(
			final ConfiguracionServiciosDominio config) {
		return new ApplicationListener<EmbeddedServletContainerInitializedEvent>() {

			@Override
			public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
				int serverPort = event.getEmbeddedServletContainer().getPort();
				config.getSeguridad().setUrl(String.format("http://localhost:%d/api/seguridad", serverPort));
				config.getNotificacion().setUrl(String.format("http://localhost:%d/api/notificacion", serverPort));
				config.getCatastro().setUrl(String.format("http://localhost:%d/api/catastro", serverPort));
				config.getFacturacion().setUrl(String.format("http://localhost:%d/api/facturacion", serverPort));
				config.getPagos().setUrl(String.format("http://localhost:%d/api/pagos", serverPort));
			}
		};
	}

}
