package negocio;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import negocio.clases.Contacto;
/**
 *
 * @author HP
 * @param <T>
 */
public class ArbolBinarioBusqueda< T extends Comparable<T> > implements IArbolBusqueda<T> {

    protected NodoBinario<T> raiz;

    // recibe un contacto a insertar, considerando el nombre para ordenar
    @Override
    public boolean insertar(T contactoAInsertar) {
        if (this.esArbolVacio()) {
            this.raiz = new NodoBinario<>(contactoAInsertar);
            return true;
        }
        NodoBinario<T> nodoAnterior = NodoBinario.nodoVacio();
        NodoBinario<T> nodoActual = this.raiz;

        while (!NodoBinario.esNodoVacio(nodoActual)) {
            if (contactoAInsertar.compareTo(nodoActual.getDato()) == 0) {
                return false;
            }
            nodoAnterior = nodoActual;
            if (contactoAInsertar.compareTo(nodoActual.getDato()) > 0) {
                nodoActual = nodoActual.getHijoDerecho();
            } else {
                nodoActual = nodoActual.getHijoIzquierdo();
            }
        }

        NodoBinario<T> nuevoNodo = new NodoBinario<>(contactoAInsertar);
        if (contactoAInsertar.compareTo(nodoAnterior.getDato()) > 0) {
            nodoAnterior.setHijoDerecho(nuevoNodo);
        } else {
            nodoAnterior.setHijoIzquierdo(nuevoNodo);
        }
        return true;
    }
    
    @Override
    public boolean eliminar(T datoAEliminar) {
        System.out.println(datoAEliminar);
        try{
            this.raiz = eliminar(this.raiz,datoAEliminar);
            
            return true;
        }catch(Exception ex){
            return false;
        }
    }
    
    private NodoBinario<T> eliminar(NodoBinario<T> nodoActual, T datoAEliminar)
        throws Exception{
            if(NodoBinario.esNodoVacio(nodoActual)){
                throw new Exception();
            }
            T datoDelNodoActual = nodoActual.getDato();
            if(datoAEliminar.compareTo(datoDelNodoActual) > 0){
                NodoBinario<T> nuevoHijoDerecho = eliminar(nodoActual.getHijoDerecho(),datoAEliminar);
                nodoActual.setHijoDerecho(nuevoHijoDerecho);
                return nodoActual;
            }
            if(datoAEliminar.compareTo(datoDelNodoActual) < 0){
                NodoBinario<T> nuevoHijoIzquierdo = eliminar(nodoActual.getHijoIzquierdo(),datoAEliminar);
                nodoActual.setHijoIzquierdo(nuevoHijoIzquierdo);
                return nodoActual;
            }
            //Caso 1
            if(nodoActual.esHoja()){
                return NodoBinario.nodoVacio();
            }
            //Caso 2
            if(nodoActual.esVacioHijoIzquierdo() &&
                    !nodoActual.esVacioHijoDerecho()){
                //return nodoActual.getHijoDerecho();
                NodoBinario<T> nuevoNodoActual = nodoActual.getHijoDerecho();
                nodoActual.setHijoDerecho(NodoBinario.nodoVacio());
                return (nuevoNodoActual);
            }
            if(!nodoActual.esVacioHijoIzquierdo() &&
                    nodoActual.esVacioHijoDerecho()){
//                return nodoActual.getHijoIzquierdo();
                NodoBinario<T> nuevoNodoActual = nodoActual.getHijoIzquierdo();
                nodoActual.setHijoIzquierdo(NodoBinario.nodoVacio());
                return (nuevoNodoActual);
            }
            //Caso 3
            T datoSucesor = buscarDatosSucesor(nodoActual.getHijoDerecho());
            NodoBinario<T> nuevoHijoDerecho = eliminar(nodoActual.getHijoDerecho(),datoSucesor);
                nodoActual.setHijoDerecho(nuevoHijoDerecho);
                nodoActual.setDato(datoSucesor);
                return nodoActual;       
    }
    
    protected T buscarDatosSucesor (NodoBinario<T> nodoActual){
        NodoBinario<T> nodoAnterior = nodoActual;
        while(!NodoBinario.esNodoVacio(nodoActual)){
            nodoAnterior = nodoActual;
            nodoActual = nodoActual.getHijoIzquierdo();
        }
        return nodoAnterior.getDato();
    }
    
    ////////////////////////
    @Override
    public T buscar(T datoBuscado) {
        NodoBinario<T> nodoAuxiliar = this.raiz;

        while (!NodoBinario.esNodoVacio(nodoAuxiliar)) {
            if (nodoAuxiliar.getDato().compareTo(datoBuscado) == 0) {
                return nodoAuxiliar.getDato();
            }
            if (datoBuscado.compareTo(nodoAuxiliar.getDato()) > 0) {
                nodoAuxiliar = nodoAuxiliar.getHijoDerecho();
            } else {
                nodoAuxiliar = nodoAuxiliar.getHijoIzquierdo();
            }
        }

        return null;
    }

    @Override
    public boolean contiene(T dato) {
        return this.buscar(dato) != null;
    }
//**********************************************////

    @Override
    public List<T> recorridoEnInOrden(){
        List<T> recorrido = new LinkedList();
        recorridoEnInOrden(this.raiz,recorrido);
        return recorrido;
    }
    private void recorridoEnInOrden(NodoBinario<T> nodoActual,List<T> recorrido){
        if(NodoBinario.esNodoVacio(nodoActual)){
            return;
        }
        recorridoEnInOrden(nodoActual.getHijoIzquierdo(),recorrido);
        recorrido.add(nodoActual.getDato()); 
        recorridoEnInOrden(nodoActual.getHijoDerecho(),recorrido);
    }
    
//******************************************///

    @Override  
    public List<T> recorridoEnPreOrden(){
        List<T> recorrido = new LinkedList();
        recorridoEnPreOrden(this.raiz,recorrido);
        return recorrido;
    }
    
    private void recorridoEnPreOrden(NodoBinario<T> nodoActual, List<T> recorrido){
        if(NodoBinario.esNodoVacio(nodoActual)){
            return;
        }
        recorrido.add(nodoActual.getDato());
        recorridoEnPreOrden(nodoActual.getHijoIzquierdo(),recorrido);
        recorridoEnPreOrden(nodoActual.getHijoDerecho(),recorrido);
    }

   ////////////////////////*************************************////////////////////////
    public List<T> recorridoEnPostOrden(){
        List<T> recorrido = new LinkedList();
        recorridoEnPostOrden(this.raiz,recorrido);
        return recorrido;
    }
    private void recorridoEnPostOrden(NodoBinario<T> nodoActual,List<T> recorrido){
        if(NodoBinario.esNodoVacio(nodoActual)){
            return;
        }
        recorridoEnPostOrden(nodoActual.getHijoIzquierdo(),recorrido);
        recorridoEnPostOrden(nodoActual.getHijoDerecho(),recorrido);
        recorrido.add(nodoActual.getDato());
    }

    //***********************************************/////////
    @Override
    public List<T> recorridoPorNiveles() {
        List<T> recorrido = new LinkedList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }

        Queue< NodoBinario<T>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(raiz);

        while (!colaDeNodos.isEmpty()) {
            NodoBinario<T> nodoActual = colaDeNodos.poll();
            recorrido.add(nodoActual.getDato());

            if (!nodoActual.esVacioHijoIzquierdo()) {
                colaDeNodos.offer(nodoActual.getHijoIzquierdo());
            }
            if (!nodoActual.esVacioHijoDerecho()) {
                colaDeNodos.offer(nodoActual.getHijoDerecho());
            }
        }
        return recorrido;
    }

    @Override
    public int size() { //sizeRecursivo
        return size(this.raiz);
    }
    
    private int size(NodoBinario<T> nodoActual){
       
        if(NodoBinario.esNodoVacio(nodoActual)){
            return 0;
        }
        int cantidadPorIzquierda = size(nodoActual.getHijoIzquierdo());
        int cantidadPorDerecha = size(nodoActual.getHijoDerecho());
        return cantidadPorIzquierda + cantidadPorDerecha + 1;
    }

    @Override
    public int altura() {
        return altura(this.raiz);
    }
    protected int altura(NodoBinario<T> nodoActual){
        if(NodoBinario.esNodoVacio(nodoActual)){
            return 0;
        }
        int alturaPorIzquierda = altura(nodoActual.getHijoIzquierdo());
        int alturaPorDerecha = altura(nodoActual.getHijoDerecho());
        if(alturaPorIzquierda > alturaPorDerecha){
            return alturaPorIzquierda + 1;
        }
        return alturaPorDerecha + 1;
    }
    

    @Override
    public final void vaciar() {
        this.raiz = NodoBinario.nodoVacio();
    }

    @Override
    public final boolean esArbolVacio() {
        return NodoBinario.esNodoVacio(raiz);
    }

    @Override
    public int nivel() {
        return nivel(this.raiz);
    }
    
    private int nivel(NodoBinario<T> nodoActual){
        if(NodoBinario.esNodoVacio(nodoActual)){
            return -1;
        }
        int nivelMayorIzquierdo = nivel(nodoActual.getHijoIzquierdo());
        int nivelMayorDerecho = nivel(nodoActual.getHijoDerecho());
        if(nivelMayorIzquierdo > nivelMayorDerecho){
            return nivelMayorIzquierdo + 1;
        }
        return nivelMayorDerecho + 1;
    }
  
   /***
    * metodo de reconstruccion de arboles
    * InOrden -- preOrden:
    * De una lista con el recorrido en inOrden y otra con el recorrido en preOrden de un Arbol
    */
   public void reconstruccionInOrden_PreOrden( List<T> listaInOrden, List<T> listaPreOrden){
       raiz =  reconstruccionInOrden_PreOrdenRecursivo(listaInOrden, listaPreOrden);   
   }
   
   private NodoBinario<T> reconstruccionInOrden_PreOrdenRecursivo( List<T> listaInOrden, List<T> listaPreOrden){
       if(listaInOrden.isEmpty() && listaPreOrden.isEmpty()){
           return NodoBinario.nodoVacio();
       }
       NodoBinario<T> nodoActual = new NodoBinario(listaPreOrden.get(0));
       int posicionRaiz = listaInOrden.indexOf(nodoActual.getDato());
       //por izquierda
       List<T> inOrdenParaIzquierda = subLista(listaInOrden,0, posicionRaiz-1);
       List<T> preOrdenParaIzquierda = subLista(listaPreOrden,1, inOrdenParaIzquierda.size());
       nodoActual.setHijoIzquierdo(reconstruccionInOrden_PreOrdenRecursivo(inOrdenParaIzquierda, preOrdenParaIzquierda) );
       //por derecha
       List<T> inOrdenParaDerecha = subLista(listaInOrden,posicionRaiz+1, listaInOrden.size()-1);
       List<T> preOrdenParaDerecha = subLista(listaPreOrden,preOrdenParaIzquierda.size()+1, listaPreOrden.size()-1);
       nodoActual.setHijoDerecho(reconstruccionInOrden_PreOrdenRecursivo(inOrdenParaDerecha , preOrdenParaDerecha) );
       
       return nodoActual;
   }

   private List<T> subLista(List<T> lista,int fromIndex, int toIndex){
       List<T> SubLista = new LinkedList<>();
        while(fromIndex <= toIndex){
            SubLista.add(lista.get(fromIndex));
            fromIndex++;
        }
       return SubLista;
   }
   
    /***
    * metodo de reconstruccion de arboles
    * InOrden -- postOrden:
    * De una lista con el recorrido en inOrden y otra con el recorrido en postOrden de un Arbol
    */
   public void reconstruccionInOrden_PostOrden(List<T> listaInOrden, List<T> listaPostOrden){
       raiz = reconstruccionInOrden_PostOrdenRecursivo(listaInOrden,listaPostOrden);
   }
   
   public NodoBinario<T> reconstruccionInOrden_PostOrdenRecursivo(List<T> listaInOrden, List<T> listaPostOrden){
       if(listaInOrden.isEmpty() || listaPostOrden.isEmpty()){
           return NodoBinario.nodoVacio();
       }
       NodoBinario nodoActual = new NodoBinario<>(listaPostOrden.get(listaPostOrden.size()-1));
       int posicionRaiz = listaInOrden.indexOf(nodoActual.getDato());
       
       List<T> inOrdenParaIzquierda = subLista(listaInOrden, 0, posicionRaiz-1);
       List<T> postOrdenParaIzquierda = subLista(listaPostOrden,0,inOrdenParaIzquierda.size()-1);
       nodoActual.setHijoIzquierdo(reconstruccionInOrden_PostOrdenRecursivo(inOrdenParaIzquierda,postOrdenParaIzquierda));
       
       List<T> inOrdenParaDerecha = subLista(listaInOrden,posicionRaiz+1,listaInOrden.size()-1);
       List<T> postOrdenParaDerecha = subLista(listaPostOrden,postOrdenParaIzquierda.size(),listaPostOrden.size()-2);
       nodoActual.setHijoDerecho(reconstruccionInOrden_PostOrdenRecursivo(inOrdenParaDerecha,postOrdenParaDerecha));
       
       return nodoActual;
   }
   
   public static void main(String args[]){
       
       ArbolBinarioBusqueda<Contacto> b = new ArbolBinarioBusqueda<>();
       

       b.insertar(new Contacto("Fede", 123,true));
       b.insertar(new Contacto("thor Jose", 567,true));
       b.insertar(new Contacto("Saturnino mamani", 786,true));
       b.insertar(new Contacto("Jacinto ", 7862,true));
       b.insertar(new Contacto("Joaquin chumacero", 456,true));
       b.insertar(new Contacto("Kevin vargas", 123,true));
       b.insertar(new Contacto("Loki asgard", 3565,true));
       b.insertar(new Contacto("thor Jose", 56,true));
       b.insertar(new Contacto("Silvia", 0,true));
       b.insertar(new Contacto("Ayuco", 667,true));

       System.out.println( b.recorridoEnInOrden());
       Contacto nuevo = new Contacto("thor Jose");
//       b.eliminar(nuevo);
//       System.out.println(b.recorridoEnInOrden());
       if(b.contiene(nuevo))
           System.out.println("esta aqui");
       else
           System.out.println("NO esta aqui");
   }
 }
