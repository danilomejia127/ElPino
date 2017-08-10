/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eafit.catastro.servicio.externo.modelo;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author jhon_
 */
public class Pago implements Serializable {

    private static final long serialVersionUID = 2L;

    /*
• Número de Autorización:
• Fecha y Hora: 2017-04-25 13:54:55
• Cuenta Origen de Fondos: Cuenta de Ahorros N° 1096
• Empresa de servicios: TIGO FACTURACION
• Nombre de Servicio: Tigo Lina
• Número de referencia de pago: 8753546080
• Monto: undefined
• Resultado: Fallido
• Costo de la transacción (Sin IVA): 0
• Canal: Banca Móvil - Smartphones
• IP de Origen: 181.143.52.2"
     */
    private long numAutorizacion;
    private Date fechaPago;
    private int cuentaFondos;
    private String empresaServicios;
    private String nombreServicio;
    private String numeroReferencia;
    private int monto;
    private String estado;
    private int costoTransccion;
    private String canal;
    private String ipOrigen;

    public Pago() {
        super();
    }

    public Pago(long numAutorizacion, Date fechaPago, int cuentaFondos, String empresaServicios,
            String nombreServicio, String numeroReferencia, int monto, String estado, int costoTransaccion, String canal,
            String ipOrigen) {
        super();
        this.numAutorizacion = numAutorizacion;
        this.fechaPago = fechaPago;
        this.cuentaFondos = cuentaFondos;
        this.empresaServicios = empresaServicios;
        this.nombreServicio = nombreServicio;
        this.numeroReferencia = numeroReferencia;
        this.monto = monto;
        this.estado = estado;
        this.costoTransccion = costoTransaccion;
        this.canal = canal;
        this.ipOrigen = ipOrigen;
    }

    public long getNumAutorizacion() {
        return numAutorizacion;
    }

    public void setNumAutorizacion(long numAutorizacion) {
        this.numAutorizacion = numAutorizacion;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public int getCuentaFondos() {
        return cuentaFondos;
    }

    public void setCuentaFondos(int cuentaFondos) {
        this.cuentaFondos = cuentaFondos;
    }

    public String getEmpresaServicios() {
        return empresaServicios;
    }

    public void setEmpresaServicios(String empresaServicios) {
        this.empresaServicios = empresaServicios;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public String getNumeroReferencia() {
        return numeroReferencia;
    }

    public void setNumeroReferencia(String numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getCostoTransccion() {
        return costoTransccion;
    }

    public void setCostoTransccion(int costoTransccion) {
        this.costoTransccion = costoTransccion;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getIpOrigen() {
        return ipOrigen;
    }

    public void setIpOrigen(String ipOrigen) {
        this.ipOrigen = ipOrigen;
    }

}
