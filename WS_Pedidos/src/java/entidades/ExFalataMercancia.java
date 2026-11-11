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
public class ExFalataMercancia extends Exception {

    /**
     * Creates a new instance of <code>ExFalataMercancia</code> without detail
     * message.
     */
    public ExFalataMercancia() {
    }

    /**
     * Constructs an instance of <code>ExFalataMercancia</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ExFalataMercancia(String msg) {
        super(msg);
    }
}
