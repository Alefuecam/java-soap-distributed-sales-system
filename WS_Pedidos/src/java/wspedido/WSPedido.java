/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wspedido;

import entidades.Customer;
import entidades.Product;
import fachadas.CustomerFacade;
import fachadas.CustomerOrderFacade;
import fachadas.OrderedProductFacade;
import fachadas.ProductFacade;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import entidades.ExFalataMercancia;
//import javax.persistence.EntityManager;

/**
 *
 * @author alefu
 */
@WebService(serviceName = "WSPedido")
public class WSPedido {
    @EJB
    private OrderedProductFacade orderedProductFacade;
    @EJB
    private CustomerOrderFacade customerOrderFacade;
    @EJB
    private ProductFacade productFacade;
    @EJB
    private CustomerFacade customerFacade;


    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }

    /**
     * Web service operation
     * @param id_clte
     * @param lista_it
     */
    @WebMethod(operationName = "altaPedido")
    public int altaPedido(@WebParam(name = "id_clte") int id_clte, @WebParam(name = "lista_it") List<ClsItem> lista_it) {
        //TODO write your implementation code here:
        //List<entidades.Product> lista_prods_en_pedido = new ArrayList<>();
        List<entidades.OrderedProduct> lista_orderedProducts = new ArrayList<>();//Lista productos ordenados
        entidades.OrderedProduct ordered_product;//un producto ordenado, que tiene producto ordenado, cantidad y customer qu elo ordeno
        entidades.Product prod;// un producto
        int num_conf;//numero de confrimacion
        // para obtener la cantidad posible a surtir
        int cantidad_posible;
        int num_pedido;
        
        BigDecimal bd_monto_pedido = BigDecimal.ZERO;//monto total del pedido
        BigDecimal bd_monto_item;//monto de un item de la lista
        for (ClsItem it : lista_it) //iteracion sobre lista de items
        {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO,
                    "Product_id:" + it.getId_prod() + ", cantidad:"
                    + it.getCantidad());
            // -----------------------
            //regresa la cantidad posible que se puede surtir, si el prodcuto no existe regresa 0
            cantidad_posible = productFacade.actualizaExistencia(it.getId_prod(),it.getCantidad());
            
            if (cantidad_posible > 0) {//Si existe producto y si se puede surtir
                ordered_product = new entidades.OrderedProduct();
                prod = productFacade.find(new Integer(it.getId_prod()));//El producto existe porque salio del catalogo
                ordered_product.setProduct(prod);//se agrega prodcuto a producto ordenado
                
                if( cantidad_posible > 0 )
                {
                  ordered_product.setQuantity((short) cantidad_posible);// se agrgea a producto ordenado cantidad
                  lista_orderedProducts.add(ordered_product);//se agrega product ordenado a lista de productos ordenados
                  //lista_prods_en_pedido.add(prod);
                  Logger.getLogger(this.getClass().getName()).log(Level.INFO,
                        "Product_id:" + prod.getId() + ", "
                        + prod.getName() + ", "
                        + prod.getDescription() + ", "
                        + prod.getCategoryId().getName() + ", "
                        + prod.getPrice());
                  bd_monto_item = prod.getPrice().multiply(BigDecimal.valueOf(cantidad_posible));// monto del item p*q
                  bd_monto_item = bd_monto_item.setScale(2,BigDecimal.ROUND_HALF_UP);//redondeo
                  bd_monto_pedido = bd_monto_pedido.add(bd_monto_item);  //se agrega a monto total un +=
                }
                //--------------------------------------------
            } 
            else //no existe producto, podria poner excepcipion
            {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Clave de producto " + it.getId_prod() + " INEXISTENTE o sin existencias");
                //throw new ExFalataMercancia("falto mercancia");
            }
        }
        //se acaba for 
        if( lista_orderedProducts.size() > 0 ) // si hay items en el pedido
        {
          entidades.Customer clte = customerFacade.find(new Integer(id_clte));//Se encientra a cliente, aqui se supone que existe
          // se obtiene el cliente
          Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
                "Pedido del cliente:" + clte.getId() + ", "
                + clte.getName() + " por " + bd_monto_pedido);
          //
          // Se genera el customer order
          //
          entidades.CustomerOrder customer_order = new entidades.CustomerOrder();
          customer_order.setCustomerId(clte);
          java.util.Date d = new java.util.Date();

          customer_order.setDateCreated(d);
          customer_order.setAmount(bd_monto_pedido);
        
          // se solicita el número de confirmación del pedido
          num_conf = customerOrderFacade.next_conf_number();//err0r aqui, ya no
        
          customer_order.setConfirmationNumber(num_conf);
          customerOrderFacade.create(customer_order);// se crea customer order
          num_pedido = customer_order.getId();//el id se crea solo
          //
          // Van los items
           //EntityManager emop = orderedProductFacade.getEntityManager();
          entidades.OrderedProductPK oppk;
          for (entidades.OrderedProduct op : lista_orderedProducts) //iteramos sobre lista de productos ordneados para agregarlos a la orden del customer
          {
            oppk = new entidades.OrderedProductPK();
            oppk.setCustomerOrderId(customer_order.getId());
            oppk.setProductId(op.getProduct().getId());
            op.setOrderedProductPK(oppk);
            op.setCustomerOrder(customer_order);
            orderedProductFacade.create(op);//actualiza tabla de productos ordenados
            //emop.flush();
          }
           
        }
        else
        {
            num_pedido = 0;  // no hay items a surtir en el pedido
        }
        
        return num_pedido;

        
    }
    @WebMethod(operationName = "restituyeProductosCO")
  public String restituyeProductosCO(@WebParam(name="id_CO") int id_CO){
      String strRes = "";
      entidades.CustomerOrder co = customerOrderFacade.find(id_CO);
      entidades.Product       prod;
      if(co!=null){
        java.util.List<entidades.OrderedProduct> items = (java.util.List<entidades.OrderedProduct>) co.getOrderedProductList();
        strRes += "Colección con " + items.size()+ " items\n";
        for( entidades.OrderedProduct op : items )
        {
            prod = productFacade.find(op.getProduct().getId());
            productFacade.agregaExistencia(prod.getId(), op.getQuantity());
            
            strRes += String.format("%10d",   prod.getId())  + " ... " +
                                String.format("Cantidad en existencia:%5d", op.getQuantity()) + " ... " +
                                String.format("%-20s",  prod.getName()) + " ... " +
                                String.format("%-45s",  prod.getDescription()) + '\n';
        }
        strRes += "Se restituyeron productos por un monto de " + co.getAmount();

      }
      return strRes;
  }
  
    @WebMethod(operationName = "resstitucion")
    public String resstitucion(@WebParam(name = "id_CO") int id_CO) {
        //TODO write your implementation code here:
        String strRes = "";
      entidades.CustomerOrder co = customerOrderFacade.find(id_CO);
      entidades.Product       prod;
      if(co!=null){
        java.util.List<entidades.OrderedProduct> items = (java.util.List<entidades.OrderedProduct>) co.getOrderedProductList();
        java.util.List<entidades.OrderedProduct> lista_op = orderedProductFacade.findByCO(id_CO);
        strRes += "Colección con " + items.size()+ " items\n";
        //for( entidades.OrderedProduct op : items )
        for( entidades.OrderedProduct op : lista_op )
        {
            prod = productFacade.find(op.getProduct().getId());
            productFacade.agregaExistencia(prod.getId(), op.getQuantity());
            
            strRes += String.format("%10d",   prod.getId())  + " ... " +
                                String.format("Cantidad en existencia:%5d", op.getQuantity()) + " ... " +
                                String.format("%-20s",  prod.getName()) + " ... " +
                                String.format("%-45s",  prod.getDescription()) + '\n';
        }
        strRes += "Se restituyeron productos por un monto de " + co.getAmount();

      }
      return strRes;
        
    }
    @WebMethod(operationName = "revisaPedido")
    public String revisaPedido(@WebParam(name = "id_pedido") int id_pedido)
    {
        StringBuffer sb = new StringBuffer("NumPedido:").append(id_pedido);

        entidades.CustomerOrder co = customerOrderFacade.find(id_pedido);
        if(co == null)
        {
        sb.append("\nPedido inexistente");
        }
        else
        {
        sb.append("\nCliente del pedido:").append(co.getCustomerId().getId());
        java.util.List<entidades.OrderedProduct> lista_op = orderedProductFacade.findByCO(id_pedido);
        if( lista_op.isEmpty())
        sb.append("\n ******Pedido sin items******");
        else
        {
        for( entidades.OrderedProduct op : lista_op )
        { sb.append('\n');
        sb.append(op.getProduct().getId()).append(",");
        sb.append(op.getQuantity());
        }
        }
    }

return sb.toString();
}
    
    @WebMethod(operationName = "catalogoCltes")
    public java.util.List<entidades.Customer> catalogoCltes() 
    {
        return customerFacade.findAll();
    }

    /**
     * Web service operation
     * @return 
     */
    @WebMethod(operationName = "catalogoProds")
    public java.util.List<entidades.Product> catalogoProds() 
    {   
        return productFacade.findAll();
    }
    //=============================================================================
    // Nuevos servicios
    // =============================================================================
    /**
    * Web service operation
    */
    @WebMethod(operationName = "montoPedido")
    public BigDecimal montoPedido(@WebParam(name = "num_pedido") int num_pedido)
    {
    java.math.BigDecimal monto_del_pedido;//dado un numero de pedido regresa el monto

    monto_del_pedido = customerOrderFacade.find(num_pedido).getAmount();

    return monto_del_pedido;
    }

    /**
    * Web service operation
    */
    @WebMethod(operationName = "clteDelPedido")
    public Customer clteDelPedido(@WebParam(name = "num_pedido") int num_pedido)
    {
    return customerOrderFacade.find(num_pedido).getCustomerId();// dado el numero de pedido regresa el cliente
    }

    // =============================================================================
    // Fin de Nuevos servicio
    // ===================================

    /**
     * Web service operation
     */
    
}
