/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

/**
 *
 * @author HP
 */
public class ExceptionArbolOrdenInvalido extends Exception {

    /**
     * Creates a new instance of <code>ExceptionArbolOrdenInvalido</code>
     * without detail message.
     */
    public ExceptionArbolOrdenInvalido() {
    }

    /**
     * Constructs an instance of <code>ExceptionArbolOrdenInvalido</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public ExceptionArbolOrdenInvalido(String msg) {
        super(msg);
    }
}
