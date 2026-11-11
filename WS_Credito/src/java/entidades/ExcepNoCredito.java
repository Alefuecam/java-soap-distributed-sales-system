/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

/**
 *
 * @author alefu
 */
public class ExcepNoCredito extends Exception {
     String msg;
    /**
     * Creates a new instance of <code>ExcepNoCredito</code> without detail
     * message.
     */
    int id_clte;
    
    public ExcepNoCredito(int id_clte) {
        
        this.id_clte = id_clte;
        this.msg= " no tiene credito el cliente "+id_clte;
        
        
    }
    public String getMsg() 
    { 
    return this.msg; 
    } 
    @Override
    public String toString() {
        return "Excepción de servicio:" + this.msg; 
        //return "El Cliente " + this.id_clte + " no tiene crèdito";
    }

    
}
