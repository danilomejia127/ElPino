/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eafit.catastro.servicio.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eafit.catastro.servicio.externo.PagoServicioExterno;
import eafit.catastro.servicio.externo.modelo.Pago;

/**
 *
 * @author jhon_
 */
@RestController
public class PagoCertificadoServicio extends ServicioBase {

    @Autowired
    private PagoServicioExterno pagoServicioExterno;

    @GetMapping("/pagos/consultarEstadoPago")
    public String consultarEstado(
            @RequestParam("referencia") String referencia) {
        return pagoServicioExterno.consultarEstadoPago(referencia);
    }
    
    @PostMapping("/pagos/parametrizar")
    public String parametrizarPago(
            @RequestParam("tipoDocumento") String tipoDoc, @RequestParam("numeroDocumento") String numDcto,
            @RequestParam("valor") int valorPago) {
        return pagoServicioExterno.parametrizarPago(tipoDoc, numDcto, valorPago);
    }
    
    @PostMapping("/pagos/ejecutarPago")
    public String ejecutar(@RequestParam("referencia") String referencia){
        return pagoServicioExterno.ejecutarPago(referencia);
    }
    
    @GetMapping("/pagos/consultarDetallePago")
    public Pago consultarDetallePago(@RequestParam("referencia") String referencia){
        return pagoServicioExterno.consultarInfoPago(referencia);
    }

}
