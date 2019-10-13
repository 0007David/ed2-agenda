package negocio;

/**
 *
 * @author HP
 */
public class NodoBinario<T> {
    private T dato;
    private NodoBinario<T> hijoIzquierdo;
    private NodoBinario<T> hijoDerecho;
    
    public NodoBinario(){
    }

    public NodoBinario(T dato) {
        this.dato = dato;
    }

    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public NodoBinario<T> getHijoIzquierdo() {
        return hijoIzquierdo;
    }

    public void setHijoIzquierdo(NodoBinario<T> hijoIzquierdo) {
        this.hijoIzquierdo = hijoIzquierdo;
    }

    public NodoBinario<T> getHijoDerecho() {
        return hijoDerecho;
    }

    public void setHijoDerecho(NodoBinario<T> hijoDerecho) {
        this.hijoDerecho = hijoDerecho;
    }
    
    public boolean esVacioHijoIzquierdo(){
        return ( this.hijoIzquierdo == null);
    }
    
    public boolean esVacioHijoDerecho(){
        return ( this.hijoDerecho == null);
    }
    
    public boolean esHoja(){
        return ( this.esVacioHijoIzquierdo() && this.esVacioHijoDerecho() );
    }
    
    public static NodoBinario nodoVacio(){
        return null;
    }
    
    public static boolean esNodoVacio(NodoBinario nodo){
        return ( nodo == NodoBinario.nodoVacio());
    }
        
}
