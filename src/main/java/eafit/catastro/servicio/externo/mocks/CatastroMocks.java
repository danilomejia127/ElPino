package eafit.catastro.servicio.externo.mocks;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;

import eafit.catastro.servicio.externo.CatastroServicioExterno;
import eafit.catastro.servicio.externo.modelo.CertificadoCatastro;

public class CatastroMocks {

	@Bean
	public CatastroServicioExterno getCertificadoCatastroServicio() {
		CatastroServicioExterno mock = mock(CatastroServicioExterno.class);
		when(mock.consultarCertificados("CC", "123123")).thenReturn(Arrays.asList(
				new CertificadoCatastro("medellin", "001", "001", "001", "Calle 123"),
				new CertificadoCatastro("medellin", "002", "002", "002", "Calle 567")));

		when(mock.generarCertificado("medellin", "001", "catastro1")).thenReturn("certificados/certificado_1.pdf");
		when(mock.generarCertificado("medellin", "002", "catastro1")).thenReturn("certificados/certificado_2.pdf");

		when(mock.consultarValorCertificado("catastro1")).thenReturn(new BigDecimal(10_000));
		when(mock.consultarValorCertificado("catastro2")).thenReturn(new BigDecimal(20_000));
		when(mock.consultarValorCertificado("catastro3")).thenReturn(new BigDecimal(15_000));
		return mock;
	}

}
