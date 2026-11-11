/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//================================================================
// Código del Cliente:
//================================================================
package example.hello;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import interfazservicioestress.InterfazServicioEstress;

public class Clte_des 
{
    //private Cliente() {}
    public static void main(String[] args) 
    {
       interfazservicioestress.InterfazServicioEstress objServ;//objeto de servicio de tipo interfaz
        
       long lngQuienSoy;
       long sumDeltaT, sumDeltaT2, dtMin = 0,dtMax = 0;
       long lngCuantosMilisFaltan;
       
       String host = (args.length < 1) ? null : args[0];//HOST AGARRA EL ARGS DE 0
       long i,n,t0,t1,dt;
       String response;
       
       //n = 2000;//ESE DEBE SER EL ARGS DE 1
       // Convertir args[1] a long y asignarlo a n
       n = (args.length > 1) ? Long.parseLong(args[1]) : 2000;
       
       
       try 
        {//args[2]
            objServ = (interfazservicioestress.InterfazServicioEstress)//nstanciar el objeto de servicio
                    Class.forName(args[2]).newInstance();//se pone objeto de servicio pone " clienteestresadorws_t1.ClienteEstresadorWS_T1" 
                                                        // en terminal paquete.clase
            //Class.forName(args[2]).newInstance();
             Registry registry = LocateRegistry.getRegistry(host);
             IServDisparo servDisparo = (IServDisparo) registry.lookup("ServidorDeDisparo");
             lngQuienSoy = servDisparo.quienSoy();
             
             objServ.inicializa((int) lngQuienSoy);
             
             lngCuantosMilisFaltan = servDisparo.deltaTEnMilis();
             System.out.println("Cliente " + lngQuienSoy + " faltan " + lngCuantosMilisFaltan  + " milisegundos");
             sumDeltaT  = 0;
             sumDeltaT2 = 0;
             //Hello stub = (Hello) registry.lookup("Hello");
             Thread.currentThread().sleep(lngCuantosMilisFaltan);
             
             for(i= 0; i < n; i++ )
             {
               t0 = System.currentTimeMillis();  
               //response = stub.sayHello();
               
               dt = objServ.invoca_servicio((int)i);
               
               //t1 = System.currentTimeMillis();
               //dt = t1 - t0;
               sumDeltaT  += dt;
               sumDeltaT2 += dt * dt;
               if( i == 0 )
               {
                   dtMin = dt;
                   dtMax = dt;
               }
               else
               {
                   if( dt < dtMin ) dtMin = dt;
                   if( dt > dtMax ) dtMax = dt;
               }
               //System.out.println("Clte " + lngQuienSoy + ": " + response);
             }
             servDisparo.acumula(sumDeltaT, sumDeltaT2, n, dtMax, dtMin);
             
             objServ.cierra();
          } 
          catch (Exception e)
          {
              System.err.println("Client exception: " + e.toString());
               e.printStackTrace();
           }
    }
}
//================================================================

