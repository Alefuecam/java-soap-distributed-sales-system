/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojo_credito;

import wscredito.ExcepNoCredito_Exception;
import wscredito.ExcepNoExisteClte_Exception;

/**
 *
 * @author axel
 */
public class Pojo_Credito implements interfazservicioestress.InterfazServicioEstress{
    
    long quienSoy;
    String host = null;
    
    @Override
    public boolean inicializa(int quienSoy) {
        this.quienSoy = quienSoy;
        this.host     = host;        // NOTA: no se utiliza
        
        return true;
    }

    @Override
    public long invoca_servicio(int vez) {
        long t0, t1, dt;//*

        t0 = System.currentTimeMillis();

        int VECES = 10;//args.length > 0 ? Integer.parseInt(args[0]) : 10;
        int id_clte;
        double dblMonto = 0.0;
        for (int k = 1; k <= VECES; k++) {
            dblMonto = 100.0 * Math.random();
            id_clte = (int) (10.0 * Math.random());

            try {
                if (autorizaDouble(id_clte, dblMonto)) {
                    System.out.println("Se ha autorizado el credito de " + dblMonto + " para el id_clte " + id_clte);
                } else {
                    System.out.println("No se ha autorizado el credito de " + dblMonto + " para el id_clte " + id_clte);
                }
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }

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
                Class.forName("pojo_credito.Pojo_Credito").newInstance();//pones la clase
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

    private static boolean autorizaDouble(int idClte, double dblMonto) throws ExcepNoExisteClte_Exception, ExcepNoCredito_Exception {
        wscredito.WSCredito_Service service = new wscredito.WSCredito_Service();
        wscredito.WSCredito port = service.getWSCreditoPort();
        return port.autorizaDouble(idClte, dblMonto);
    }
    
}
