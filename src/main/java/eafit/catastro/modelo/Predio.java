package eafit.catastro.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Predio implements Serializable {

	private static final long serialVersionUID = 5135894917226649134L;

	@Id
	@Column(name = "numero_predio")
	private String numeroPredio;	
	private String municipio;	
	@Column(name = "numero_ficha")
	private String numeroFicha;
	private String matricula;
	private String direccion;
	@Column(name = "tipo_documento_usuario")	
	private String tipoDocUsuario;
	@Column(name = "id_propietario")	
	private String idPropietario;
	public String getNumeroPredio() {
		return numeroPredio;
	}
	public void setNumeroPredio(String numeroPredio) {
		this.numeroPredio = numeroPredio;
	}
	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
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
	public String getTipoDocUsuario() {
		return tipoDocUsuario;
	}
	public void setTipoDocUsuario(String tipoDocUsuario) {
		this.tipoDocUsuario = tipoDocUsuario;
	}
	public String getIdPropietario() {
		return idPropietario;
	}
	public void setIdPropietario(String idPropietario) {
		this.idPropietario = idPropietario;
	}
	
	
		
}
