/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eafit.catastro.servicio.externo.mocks;

import eafit.catastro.servicio.externo.PagoServicioExterno;
import eafit.catastro.servicio.externo.modelo.Pago;
import org.springframework.context.annotation.Bean;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;

/**
 *
 * @author jhon_
 */
public class PagosMocks {

	@Bean
	public PagoServicioExterno getPagoServicioExterno() {
		PagoServicioExterno mock = mock(PagoServicioExterno.class);
		when(mock.consultarEstadoPago(anyString())).thenReturn("PROCESADO");
		when(mock.parametrizarPago(anyString(), anyString(), anyInt())).thenReturn(String.valueOf(Math.random()));
		when(mock.ejecutarPago(anyString())).thenReturn("PROCESADO");
		when(mock.consultarInfoPago(anyString())).thenReturn(new Pago(22334, new java.util.Date(), 445552333,
				"BANCOLOMBIA", "PSE", "123", 50000, "PROCESADO", 1000, "Banca Movil", "127.0.0.1"));
		return (mock);

	}

}
