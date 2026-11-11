/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fachadas;

import entidades.OrderedProduct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author alefu
 */
@Stateless
public class OrderedProductFacade extends AbstractFacade<OrderedProduct> {

    @PersistenceContext(unitName = "WS_PedidosPU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public OrderedProductFacade() {
        super(OrderedProduct.class);
    }
    public java.util.List<OrderedProduct> findByCO(int id_co)
    {
    em = getEntityManager();
    TypedQuery<OrderedProduct> queryOrdProdByCO = em.createNamedQuery("OrderedProduct.findByCustomerOrderId", OrderedProduct.class);
    queryOrdProdByCO.setParameter("customerOrderId", id_co);
    java.util.List<OrderedProduct> lista= queryOrdProdByCO.getResultList();

    return lista;
    }
}
