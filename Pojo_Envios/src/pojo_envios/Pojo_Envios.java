package pojo_envios;

/**
 *
 * @author RGAMBOAH
 */
public class Pojo_Envios implements interfazservicioestress.InterfazServicioEstress{
    
    long quienSoy;
    String host = null;
    
    @Override
    public boolean inicializa(int quienSoy) {
        this.quienSoy = quienSoy;
        this.host     = host;        // NOTA: no se utiliza
        
        return true;
    }
    
    @Override
    public long invoca_servicio(int vez) 
    {
        long t0, t1, dt;//*

        t0 = System.currentTimeMillis();
        
        Clte.carga_lista_Cltes();
        Clte clte = null;
        
        boolean blnSolEnv = false; 
        
        for( int k = 1;k <= 5; k++)
        {
          clte = Clte.selClte();
          clte.setId_pedido(k);
          
          blnSolEnv = solicitudEnvio(clte.getId_tda(),
                                     clte.getId_pedido(),
                                     clte.getName(),
                                     clte.getEmail(),
                                     clte.getPhone(),
                                     clte.getAddress(),
                                     clte.getCity_region());
          System.out.println("Se ha solicitado el envio del pedido " + clte.getId_pedido() + " para el clte " + clte.getName() +
                             " con email "  + clte.getEmail() + "con status " + blnSolEnv); 
          
        }
        
        System.out.println("-----------------------------------------------");
        System.out.println("Estresador:" + this.quienSoy + ", vez:" + vez);
        System.out.println("-----------------------------------------------");
        
        
        t1 = System.currentTimeMillis();//*
        dt = t1 - t0;//*
        return dt;
    }
    
    @Override
    public void cierra() 
    {
         System.out.println("El thread de stress " + this.quienSoy + " ha terminado su trabajo"); 
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception 
    {
        //instanciar el servicio de estres, emular que ejecuto desde otro lugar con el classforname
        //tengo que referenciarlo del tipo interfaz, invento nombre objSerc, 
        interfazservicioestress.InterfazServicioEstress objServ = 
                (interfazservicioestress.InterfazServicioEstress) //via interfaz no via objeto
                Class.forName("pojo_envios.Pojo_Envios").newInstance();//pones la clase
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
        /*Pojo_Parcial_02 objServ = new Pojo_Parcial_02();
        
        objServ.prepara(25, null);
        int n_veces = args.length > 0 ? Integer.parseInt(args[0]):5;//NUMERO PEDIDOS
        for( int vez = 1; vez <= n_veces; vez++)
            objServ.solicitaServicio(vez);
        objServ.cierra();*/
        
    }

    private static boolean solicitudEnvio(int idTda, int idPedido, java.lang.String name, java.lang.String email, java.lang.String phone, java.lang.String address, java.lang.String cityRegion) {
        wsenvios.WSEnvios_Service service = new wsenvios.WSEnvios_Service();
        wsenvios.WSEnvios port = service.getWSEnviosPort();
        return port.solicitudEnvio(idTda, idPedido, name, email, phone, address, cityRegion);
    }
    
}
