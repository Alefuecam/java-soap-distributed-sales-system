/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fachadas;

import entidades.Product;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

/**
 *
 * @author alefu
 */
@Stateless
public class ProductFacade extends AbstractFacade<Product> {

    @PersistenceContext(unitName = "WS_PedidosPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductFacade() {
        super(Product.class);
    }
    //===============================================================================================
    public int actualizaExistencia(int id_prod,int cant_solicitada)
    {
       int cant_posible = 0;
       int cant_existente;

       Product prod   = em.find(Product.class, new Integer(id_prod), LockModeType.PESSIMISTIC_WRITE);
       //Product prod   = em.find(Product.class, new Integer(id_prod), LockModeType.NONE);
       if(prod == null)
       {
         cant_posible = 0;   
       }
       else
       {
         cant_existente = prod.getExistencia();
         cant_posible   = cant_solicitada < cant_existente ? cant_solicitada : cant_existente;//T, F
         cant_existente -= cant_posible;
         prod.setExistencia(cant_existente);
         this.edit(prod);
         //em.flush();
       }

       prod = null;
       return cant_posible;// si es cero puede ser porque no  existe producto o porque no hay disponibles
    }//la cantidad posible no es la misma a la solicitada siempre, po rejemplo si se solicitan 20 y solo hay 10 de existencia
    //la cantidad posible sera 10 y ya no habra existencias
    public int agregaExistencia(int id_prod,int cant_a_agregar)
    {
       int cant_existente = 0;
       //Product prod   = this.find(new Integer(id_prod));
       //em.getTransaction().begin();
       Product prod   = em.find(Product.class, new Integer(id_prod), LockModeType.PESSIMISTIC_WRITE);
       //Product prod   = em.find(Product.class, new Integer(id_prod), LockModeType.NONE);
       if(prod != null)
       {
         cant_existente = prod.getExistencia();
         cant_existente += cant_a_agregar;
         prod.setExistencia(cant_existente);
         this.edit(prod);
         //em.flush();
       }
       //em.getTransaction().commit();
       prod = null;
        return cant_existente;
    }
}
