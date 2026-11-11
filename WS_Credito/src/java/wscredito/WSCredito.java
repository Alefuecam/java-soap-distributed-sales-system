/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wscredito;

import entidades.Credito;
import entidades.ExcepNoCredito;
import entidades.ExcepNoExisteClte;
import fachadas.CreditoFacade;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * @author alefu
 */
@WebService(serviceName = "WSCredito")
public class WSCredito {

    @EJB
    private CreditoFacade ejbRef;// Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Web Service Operation")

//    @WebMethod(operationName = "create")
//    @Oneway
//    public void create(@WebParam(name = "entity") Credito entity) {
//        ejbRef.create(entity);
//    }
//
//    @WebMethod(operationName = "edit")
//    @Oneway
//    public void edit(@WebParam(name = "entity") Credito entity) {
//        ejbRef.edit(entity);
//    }
//
//    @WebMethod(operationName = "remove")
//    @Oneway
//    public void remove(@WebParam(name = "entity") Credito entity) {
//        ejbRef.remove(entity);
//    }
//
//    @WebMethod(operationName = "find")
//    public Credito find(@WebParam(name = "id") Object id) {
//        return ejbRef.find(id);
//    }
//
//    @WebMethod(operationName = "findAll")
//    public List<Credito> findAll() {
//        return ejbRef.findAll();
//    }
//
//    @WebMethod(operationName = "findRange")
//    public List<Credito> findRange(@WebParam(name = "range") int[] range) {
//        return ejbRef.findRange(range);
//    }
//
//    @WebMethod(operationName = "count")
//    public int count() {
//        return ejbRef.count();
//    }
//    @WebMethod(operationName = "autoriza")
//    public boolean autoriza(@WebParam(name = "id_clte") final int id_clte, @WebParam(name = "monto") final BigDecimal monto) throws ExcepNoCredito, ExcepNoExisteClte {
//        
//        boolean blnAutorizado = ejbRef.actualizaCredito(id_clte, monto);
//        
//        return blnAutorizado;
//    }
    @WebMethod(operationName = "autoriza_double")
    public boolean autoriza_double(@WebParam(name = "id_clte") final int id_clte, @WebParam(name = "dbl_monto") final double dbl_monto) throws ExcepNoCredito, ExcepNoExisteClte {
        
        BigDecimal monto = BigDecimal.valueOf(dbl_monto);

        monto.setScale(2,BigDecimal.ROUND_HALF_UP);

        boolean blnAutorizado = ejbRef.actualizaCredito(id_clte, monto);
        
        return blnAutorizado;
    }
    
    
    

//    /**
//     * Web service operation
//     */
//    @WebMethod(operationName = "aumentarCredito")
//    @Oneway
//    public void aumentarCredito(@WebParam(name = "monto") double monto,@WebParam(name = "quien") int quien) {
//        BigDecimal aux,monto1;
//        //monto1=(BigDecimal)monto;
//        
//        monto1 = BigDecimal.valueOf(2000);
//        Credito credito= ejbRef.find(quien);
//        aux=credito.getCredito();
//        aux.add(monto1);
//        credito.setCredito(aux);
//        ejbRef.edit(credito);
//    }
}
