/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package example.hello;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author RGGH
 */
public class Distribuidor 
{
    public static void main( String args[])
    {

        System.out.println("Distribuidor, inciando con:");
        if( args.length == 0)
            System.out.println("Sin argumentos");
        else
            for(int i = 0; i < args.length; i++)
                System.out.println("args[" + i + "]:" + args[i]);
        System.out.println("------------------------------------------");

        String[] argsPar = new String[0];
        
        if( args.length > 1 )//SI TRAE MAS DE UN ARGUMENTO, ENTONCES APLICA CHIF, ARGUMENTOS-1, PORQUE ME QUEDO CON EL PRIMERO, AHI LOS RECORRE 2.
        {
           argsPar = new String[args.length-1];
           for(int i=1;i<args.length;i++)
             argsPar[i-1] = args[i];  
        }
        if( args.length == 0 ) 
        {
            System.out.println("uso:\n" + 
                               "Distribuidor ServidorDeDisparo ip_host_localhost\n" +
                               "Distribuidor Master            ip_host_localhost reset deltaTEnSegs\n" +
                               "Distribuidor Master            ip_host_localhost [reporta]\n"            +
                               "Distribuidor Cliente           ip_host_localhost");
        }
        else
        /*
        {
            if( args[0].compareToIgnoreCase("Master")  == 0) Master.main( argsPar );
            if( args[0].compareToIgnoreCase("Cliente") == 0) Cliente.main( argsPar );
            if( args[0].compareToIgnoreCase("Server") == 0) Server.main( argsPar );
            if( args[0].compareToIgnoreCase("ServidorDeDisparo") == 0) ServidorDeDisparo.main( argsPar );
        }
        */
        {
            Class[] cArg = new Class[1];//clase
            cArg[0] = String[].class;//clase de los argumentos, busca metodo con firma
            System.out.println("Distribuidor redirigiendo a args[0]:" + args[0]);
            try 
            {
              Class cl = Class.forName("example.hello." + args[0]);//master, etc clientedes
              Method m = cl.getMethod("main", cArg);
              m.invoke(cl, (Object) argsPar);//inova de esa clase ese metodo pasado argspar, ARGSPAR EJECUTA EL MAIN DE ESA CLASE, MAIN ES ESTATICO, SE EJECUTA EL MAIN 1.
              System.out.println("Distribuidor...");
            
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }

        }
        
        
    }
    
    
    
}
