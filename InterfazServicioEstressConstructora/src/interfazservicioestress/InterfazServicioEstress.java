/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfazservicioestress;

/**
 *
 * @author axel_
 */
public interface InterfazServicioEstress {
    
    boolean inicializa(int quienSoy);
    
    long invoca_servicio(int vez);
    
    void cierra();
    
}
