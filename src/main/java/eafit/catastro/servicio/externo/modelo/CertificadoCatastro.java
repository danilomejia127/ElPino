package eafit.catastro.servicio.externo.modelo;

import java.io.Serializable;

public class CertificadoCatastro implements Serializable {

	private static final long serialVersionUID = 1L;

	private String municipio;
	private String numeroPredio;
	private String numeroFicha;
	private String matricula;
	private String direccion;

	public CertificadoCatastro() {
		super();
	}

	public CertificadoCatastro(String municipio, String numeroPredio, String numeroFicha, String matricula,
			String direccion) {
		super();
		this.municipio = municipio;
		this.numeroPredio = numeroPredio;
		this.numeroFicha = numeroFicha;
		this.matricula = matricula;
		this.direccion = direccion;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getNumeroPredio() {
		return numeroPredio;
	}

	public void setNumeroPredio(String numeroPredio) {
		this.numeroPredio = numeroPredio;
	}

	public String getNumeroFicha() {
		return numeroFicha;
	}

	public void setNumeroFicha(String numeroFicha) {
		this.numeroFicha = numeroFicha;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

}
