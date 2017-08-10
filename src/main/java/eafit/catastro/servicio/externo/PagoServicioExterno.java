/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eafit.catastro.servicio.externo;

import eafit.catastro.servicio.externo.modelo.Pago;

/**
 *
 * @author jhon_ Esta clase representa una simulación ya que el sistema externo
 * de pagos proveera la funcionalidad requerida para oficinavirtual.
 */
public interface PagoServicioExterno {

    //1. Validar la tarjeta con la que se realizará el pago.
    public boolean validarTarjeta(int numTarjeta, String franquicia, int codSeguridad, String nombrePropietario);

    //2. Parametrizar el pago ingresando la información del pagador y e valor, retorna la referencia para el paso 3.
    public String parametrizarPago(String tipoDocumento, String numDocumento, int valorFatura);
    
    //3. Ejecuta el pago luego de parametrizarlo, retorna estado PENDIENTE, PROCESADO, RECHAZADO
    public String ejecutarPago(String referencia);

    //Consulta estad del pago, sirve cuando el 3 retornó estado pendiente, retorna PENDIENTE, PROCESADO, RECHAZADO
    public String consultarEstadoPago(String referencia);
    
    //Consulta toda la informacion del pago
    public Pago consultarInfoPago(String referencia);
   
}
