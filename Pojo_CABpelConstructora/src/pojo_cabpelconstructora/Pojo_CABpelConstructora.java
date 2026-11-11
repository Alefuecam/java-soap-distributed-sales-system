/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojo_cabpelconstructora;

import java.util.logging.Level;
import java.util.logging.Logger;
import wsca.GestionTiendaOperationFault;
import wsca.RespuestaGestionTienda;

/**
 *
 * @author axel_
 */
public class Pojo_CABpelConstructora implements interfazservicioestress.InterfazServicioEstress{

    /**
     * @param args the command line arguments
     */
    long quienSoy;
    String host = null;
    
    java.util.List<wsp.Customer> listaCltes = new java.util.ArrayList<>();
    java.util.List<wsp.Product>  listaProds = new java.util.ArrayList<>();
    
    int num_cltes;
    int num_prods;
 
    @Override
    public boolean inicializa(int quienSoy) {
        this.quienSoy = quienSoy;
        //this.host     = host;        // NOTA: no se utiliza
        
        listaCltes    = catalogoCltes();
        listaProds    = catalogoProds();
        num_cltes     = listaCltes.size();
        num_prods     = listaProds.size();
        
        return true;
    }

    @Override
    public long invoca_servicio(int vez) 
    {
        long t0, t1, dt;//*

        t0 = System.currentTimeMillis();
        
        wsca.GestionTienda datos_pedido = new wsca.GestionTienda();//nuevo var de ida
        wsca.RespuestaGestionTienda res; //var de vuelta, nuevo
        
        java.util.List<wsca.ClsItem>  listaIt = datos_pedido.getListaItem();//nuevo
        
   
        int num_pedido;
        int id_clte;
        int num_it;
        int id_prod,cantidad;

        int queClte,queProd = 0;
       
        wsca.ClsItem item;
        
        queClte = (int) (1 + num_cltes * Math.random());
        id_clte = queClte;//listaCltes.get(queClte).getId();

        num_it = (int) (1.0 + 10.0 * Math.random());
        num_it = num_it <= this.num_prods ? num_it : this.num_prods;
        
        //
        // para controlar que no se repitan los id_prod de los items
        //
        int [] arr_id_prod = new int[num_it];
        boolean ya_esta;
        //
        // Se generan los items para el pedido
        //
        for( int k = 0; k <  num_it; k++)
        {
            ya_esta = true;
            while(ya_esta)
            {
              queProd = (int) (1 + num_prods * Math.random());
              if( k == 0)
                  ya_esta = false;
              else
              {   
                  ya_esta = false;
                  for( int j = 0; j < k; j++)
                      ya_esta = ya_esta | queProd == arr_id_prod[j]; 
              }
            }
            arr_id_prod[k] = queProd;
            
            id_prod  = queProd;//listaProds.get(queProd).getId();
            cantidad = (int)( 5.0 + 100 * Math.random() );
            item = new wsca.ClsItem();
            item.setIdProd(id_prod);
            item.setCantidad(cantidad);
            listaIt.add(item);
        }
        System.out.println("-----------------------------------------------");
        System.out.println("Estresador:" + this.quienSoy + ", vez:" + vez + ", Clte:" + id_clte);
        System.out.println("-----------------------------------------------");
        for(wsca.ClsItem it : listaIt)
            System.out.println("Prod_id:" + it.getIdProd() + ", cantidad:" + it.getCantidad() );
        System.out.println("-----------------------------------------------");
        //
        //   Se solicita registrar el pedido en el WS
        //
        
        datos_pedido.setIdClte(queClte);
        
        
        try {
            res = gestionTiendaOperation(datos_pedido);
            num_pedido = res.getNumPedido();//altaPedido(id_clte,listaIt);
            System.out.println("El número de pedido es:" + num_pedido);
            System.out.println("===============================================");
        } catch (GestionTiendaOperationFault ex) {
            System.out.println("Excepcion: " + ex.getFaultInfo().getMsgEx());
        }
        
        
        t1 = System.currentTimeMillis();//*
        dt = t1 - t0;//*
        return dt;
        
    }

    @Override
    public void cierra() 
    {
         System.out.println("El thread de stress " + this.quienSoy + " ha terminado su trabajo"); 
    }

    // =========================================================================
    //                    main para probar el pojo  TESTER
    // =========================================================================
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception
    {
        
        //instanciar el servicio de estres, emular que ejecuto desde otro lugar con el classforname
        //tengo que referenciarlo del tipo interfaz, invento nombre objSerc, 
        interfazservicioestress.InterfazServicioEstress objServ = 
                (interfazservicioestress.InterfazServicioEstress) //via interfaz no via objeto
                Class.forName("pojo_cabpelconstructora.Pojo_CABpelConstructora").newInstance();//pones la clase
        //class forName es el java class loader
        
        objServ.inicializa(1);
        /*int n_veces = args.length > 0 ? Integer.parseInt(args[0]):5;//NUMERO PEDIDOS
        for( int vez = 1; vez <= n_veces; vez++)
            objServ.invoca_servicio(vez);
        objServ.cierra();*/
        int N_VECES = 5;  // Número de veces que se invocará el servicio
        int vez;
        long dt, dt2;
        long s_dt = 0, s_dt2 = 0;

        for (vez = 1; vez <= N_VECES; vez++) {
            dt = objServ.invoca_servicio(vez);  // Invocar el servicio
            dt2 = dt * dt;
            s_dt += dt;
            s_dt2 += dt2;
            System.out.println("Se proporciono el servicio " + vez + " tardando " + dt + " microSeg");
        }
        
        /*Pojo_CABpelConstructora objServ = new Pojo_CABpelConstructora();
        
        objServ.inicializa(25, null);
        int n_veces = args.length > 0 ? Integer.parseInt(args[0]):4;//NUMERO PEDIDOS
        for( int vez = 1; vez <= n_veces; vez++)
            objServ.invoca_servicio(vez);
        objServ.cierra();*/
        
    }
    

    //=======================
    
    private static RespuestaGestionTienda gestionTiendaOperation(wsca.GestionTienda datosPedido) throws GestionTiendaOperationFault {
        wsca.CAConstructoraService1 service = new wsca.CAConstructoraService1();
        wsca.GestionTiendaPortType port = service.getCasaPort1();
        return port.gestionTiendaOperation(datosPedido);
    }

    private static java.util.List<wsp.Customer> catalogoCltes() {
        wsp.WSPedido_Service service = new wsp.WSPedido_Service();
        wsp.WSPedido port = service.getWSPedidoPort();
        return port.catalogoCltes();
    }

    private static java.util.List<wsp.Product> catalogoProds() {
        wsp.WSPedido_Service service = new wsp.WSPedido_Service();
        wsp.WSPedido port = service.getWSPedidoPort();
        return port.catalogoProds();
    }



    
    
}
