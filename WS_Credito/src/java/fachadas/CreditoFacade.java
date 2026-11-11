/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fachadas;

import entidades.Credito;
import java.math.BigDecimal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

/**
 *
 * @author alefu
 */
@Stateless
public class CreditoFacade extends AbstractFacade<Credito> {

    @PersistenceContext(unitName = "WS_CreditoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CreditoFacade() {
        super(Credito.class);
    }
     public boolean actualizaCredito(int id_clte,BigDecimal bd_monto_requerido) throws entidades.ExcepNoCredito,entidades.ExcepNoExisteClte
    {//recibe id cliente y monto de su pedido
       boolean blnAutorizada = false;
       
       BigDecimal bd_monto_disponible;
        
       Credito credito   = em.find(Credito.class, new Integer(id_clte), LockModeType.PESSIMISTIC_WRITE);
       if(credito == null)
       {
         throw new entidades.ExcepNoExisteClte(id_clte);//no existe cliente
       }
       else//si existe cliente
       {  
         bd_monto_disponible = credito.getCredito();
         /*
         Logger.getLogger(this.getClass().getName()).log(Level.INFO,
                    "PRE: Credito_id:" + credito.getId()  + ", monto disponible:"
                    + bd_monto_disponible.toString() + ", monto requerido:"
                    + bd_monto_requerido.toString());
         */
         if(bd_monto_disponible.compareTo(bd_monto_requerido) >= 0)//si su credito es mayor a lo requeirdo
         {  
           bd_monto_disponible = bd_monto_disponible.subtract(bd_monto_requerido);
           credito.setCredito(bd_monto_disponible);
           this.edit(credito);//actualiza nuevo credito restante
           em.flush();
           /*
           Logger.getLogger(this.getClass().getName()).log(Level.INFO,
                    "POST:Credito_id:" + credito.getId()  + ", monto disponible:"
                    + bd_monto_disponible.toString() + ", monto requerido:"
                    + bd_monto_requerido.toString());
           */
           credito = null;
           blnAutorizada = true;
         }
         else//si no tiene suficiente crredito
         {
           credito = null;  
           throw new entidades.ExcepNoCredito(id_clte);
         }
       }
       
       return blnAutorizada;
    }
   
    // ====================================================================================================

}
