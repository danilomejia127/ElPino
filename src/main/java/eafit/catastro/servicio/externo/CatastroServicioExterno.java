package eafit.catastro.servicio.externo;

import java.math.BigDecimal;
import java.util.List;

import eafit.catastro.servicio.externo.modelo.CertificadoCatastro;

public interface CatastroServicioExterno {

	List<CertificadoCatastro> consultarCertificados(String tipoDocumento, String numeroDocumento);

	String generarCertificado(String municipio, String numeroFicha, String tipoCertificado);

	BigDecimal consultarValorCertificado(String tipoCertificado);

}
