package eafit.catastro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import eafit.catastro.servicio.externo.mocks.CatastroMocks;
import eafit.catastro.servicio.externo.mocks.PagosMocks;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@Import({Configuracion.class, CatastroMocks.class, PagosMocks.class})
public class OficinaVirtualAplicacion {

	public static void main(String[] args) {
		SpringApplication.run(OficinaVirtualAplicacion.class, args);
	}
}
