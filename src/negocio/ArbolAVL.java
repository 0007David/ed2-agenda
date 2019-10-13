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
public class ArbolAVL< T extends Comparable<T> > extends ArbolBinarioBusqueda<T> {
    
    private final int RANGO_PERMITIDO = 1;
    
    /**
     * metodo que inserta un dato T segun las reglas del Arbol de busqueda binario
     * retorna true si se hizo la insercion
     * retorna false si no se inserta(en caso que el dato ya este en el arbol)
     * @param datoAInsertar
     * @return 
     */
    @Override
    public boolean insertar( T datoAInsertar){
         try{
             super.raiz =  insertar(super.raiz,datoAInsertar);
             return true;
         }catch(Exception ex){
             return false;
         }
         
     }
    /**
     * A partir del nodoActual se busca la posicion correcta del dato a insertar en el arbolBinario
     * retorna el nodo de menor Nivel(raiz) actualizado(con la modificacion de insercion)
     * Ademas se encarga de que el arbol este balanceado, balanceando cada nodo que se recorra desde la insercion..
     * hasta el nodo de menor Nivel(raiz)
     * Genera una exepcion si el dato ya se encontraba en el arbol
     * @param nodoActual
     * @param datoAInsertar
     * @return
     * @throws Exception 
     */
    private NodoBinario<T> insertar(NodoBinario<T> nodoActual , T datoAInsertar)throws Exception{
        if (NodoBinario.esNodoVacio(nodoActual)) {
            nodoActual = new NodoBinario<>(datoAInsertar);
            return nodoActual;
        }
        
        if(datoAInsertar.compareTo(nodoActual.getDato()) > 0){
            nodoActual.setHijoDerecho( insertar(nodoActual.getHijoDerecho(), datoAInsertar));
            return balancear(nodoActual);
            
        }if(datoAInsertar.compareTo(nodoActual.getDato()) < 0){
            nodoActual.setHijoIzquierdo(insertar(nodoActual.getHijoIzquierdo(), datoAInsertar)); 
            return balancear(nodoActual);
        }else{
            throw new Exception(); 
        }
    }
    
    /**
     * elimina un dato T si esta en el Arbol, retorna True si se realiza correctamente
     * caso contrario retorna false(si el dato no esta en el Arbol)
     * @param datoAEliminar
     * @return 
     */
    @Override
    public boolean eliminar(T datoAEliminar) {
        try{
            super.raiz = eliminar(super.raiz,datoAEliminar);
            return true;
        }catch(Exception ex){
            return false;
        }
    }
    
    /**
     * A partir de un nodoActual busca el dato a eliminar 
     * * considera todos los casos de eliminacion de un nodo(1,2,3)
     * Ademas se encarga de balancear cada Nodo que se recorra desde el Padre del nodoEliminado 
     * *hasta el nodo de menor Nivel(Raiz)
     * genera una exepcion si no se encuentra el datoAEliminar(o el Nodo con el datoAEliminar)
     * @param nodoActual
     * @param datoAEliminar
     * @return
     * @throws Exception 
     */
    private NodoBinario<T> eliminar(NodoBinario<T> nodoActual, T datoAEliminar)
        throws Exception{
            if(NodoBinario.esNodoVacio(nodoActual)){
                throw new Exception();
            }
//            T datoDelNodoActual = nodoActual.getDato();
            if(datoAEliminar.compareTo(nodoActual.getDato()) > 0){
                NodoBinario<T> nuevoHijoDerecho = eliminar(nodoActual.getHijoDerecho(),datoAEliminar);
                nodoActual.setHijoDerecho(nuevoHijoDerecho);
                return balancear(nodoActual);
            }
            if(datoAEliminar.compareTo(nodoActual.getDato()) < 0){
                NodoBinario<T> nuevoHijoIzquierdo = eliminar(nodoActual.getHijoIzquierdo(),datoAEliminar);
                nodoActual.setHijoIzquierdo(nuevoHijoIzquierdo);
                return balancear(nodoActual);
            }
            //Caso 1
            if(nodoActual.esHoja()){
                return NodoBinario.nodoVacio();
            }
            //Caso 2
            if(nodoActual.esVacioHijoIzquierdo() &&
                    !nodoActual.esVacioHijoDerecho()){
                //algo mas
                NodoBinario<T> nuevoNodoActual = nodoActual.getHijoDerecho();
                nodoActual.setHijoDerecho(NodoBinario.nodoVacio());
                return balancear(nuevoNodoActual);
            }
            if(!nodoActual.esVacioHijoIzquierdo() &&
                    nodoActual.esVacioHijoDerecho()){
                NodoBinario<T> nuevoNodoActual = nodoActual.getHijoIzquierdo();
                nodoActual.setHijoIzquierdo(NodoBinario.nodoVacio());
                return balancear(nuevoNodoActual);
            }
            //Caso 3
            T datoSucesor = buscarDatosSucesor(nodoActual.getHijoDerecho());
            NodoBinario<T> nuevoHijoDerecho = eliminar(nodoActual.getHijoDerecho(),datoSucesor);
                nodoActual.setHijoDerecho(nuevoHijoDerecho);
                nodoActual.setDato(datoSucesor);
                return balancear(nodoActual);       
    }
    /**
     * balancea un nodoActual identificando los casos de rotacion (simple o doble : Izquierda o Derecha)
     * retorna el nuevoNodo que reemplazara al nodoActual en el Arbol
     * @param nodoActual
     * @return 
     */
    private NodoBinario<T> balancear(NodoBinario<T> nodoActual){
        
        int alturaRamaIzquierda = altura(nodoActual.getHijoIzquierdo());
        int alturaRamaDerecha = altura(nodoActual.getHijoDerecho());
        int diferenciaDeAlturas = alturaRamaIzquierda - alturaRamaDerecha;
        
        if(diferenciaDeAlturas > this.RANGO_PERMITIDO){
           NodoBinario<T> hijoIzquierdo = nodoActual.getHijoIzquierdo();
           alturaRamaIzquierda = altura(hijoIzquierdo.getHijoIzquierdo());
           alturaRamaDerecha = altura(hijoIzquierdo.getHijoDerecho());
           if(alturaRamaDerecha > alturaRamaIzquierda){
               return rotacionDobleDerecha(nodoActual);
           }
           return rotacionSimpleDerecha(nodoActual);
           
        }else if(diferenciaDeAlturas < -this.RANGO_PERMITIDO){
           NodoBinario<T> hijoDerecho = nodoActual.getHijoDerecho();
           alturaRamaIzquierda = altura(hijoDerecho.getHijoIzquierdo());
           alturaRamaDerecha = altura(hijoDerecho.getHijoDerecho());
           if(alturaRamaIzquierda > alturaRamaDerecha){
               return rotacionDobleIzquierda(nodoActual);
           }
           return rotacionSimpleIzquierda(nodoActual);
        }
        return nodoActual;
    }
    /**
     * De un NodoActual, el hijoIzquierdo rota a la Derecha
     * de modo que el arbolBinario siga siendo de Busqueda
     * @param nodoActual
     * @return 
     */
    private NodoBinario<T> rotacionSimpleDerecha(NodoBinario<T> nodoActual) {
        NodoBinario<T> hijoIzquierdoDelNodoActual = nodoActual.getHijoIzquierdo();
        NodoBinario<T> hijoDerechoDelHijoIzquierdoActual = hijoIzquierdoDelNodoActual.getHijoDerecho();
        nodoActual.setHijoIzquierdo(hijoDerechoDelHijoIzquierdoActual);
        hijoIzquierdoDelNodoActual.setHijoDerecho(nodoActual);
        return hijoIzquierdoDelNodoActual;
    }
    /**
     * De un nodoActual , realiza RotacionSimpleIzquierda y luego rotacionSimpleDerecha
     * @param nodoActual
     * @return 
     */
    private NodoBinario<T> rotacionDobleDerecha(NodoBinario<T> nodoActual) {
        
        NodoBinario<T> nuevoHijoIzquierdo = rotacionSimpleIzquierda(nodoActual.getHijoIzquierdo());
        nodoActual.setHijoIzquierdo(nuevoHijoIzquierdo);
       return  rotacionSimpleDerecha(nodoActual);
    }
    
     /**
     * De un NodoActual, el hijoDerecho rota a la Izquierda
     * de modo que el arbolBinario siga siendo de Busqueda
     * @param nodoActual
     * @return 
     */
    private NodoBinario<T> rotacionSimpleIzquierda(NodoBinario<T> nodoActual) {
        NodoBinario<T> hijoDerechoDelNodoActual = nodoActual.getHijoDerecho();
        NodoBinario<T> hijoIzquierdoDelhijoDerechoActual = hijoDerechoDelNodoActual.getHijoIzquierdo();
        nodoActual.setHijoDerecho(hijoIzquierdoDelhijoDerechoActual);
        hijoDerechoDelNodoActual.setHijoIzquierdo(nodoActual);
        return hijoDerechoDelNodoActual;
    }
    /**
     * De un nodoActual , realiza rotacionSimpleDerecha y luego RotacionSimpleIzquierda
     * @param nodoActual
     * @return 
     */
    private NodoBinario<T> rotacionDobleIzquierda(NodoBinario<T> nodoActual) {
       
       NodoBinario<T> nuevoHijoDerecho = rotacionSimpleDerecha(nodoActual.getHijoDerecho());
       nodoActual.setHijoDerecho(nuevoHijoDerecho);
       return  rotacionSimpleIzquierda(nodoActual);
    }

    // OPERACIÓN AUXILIAR
    
    //metodo que retorne en que nivel está el mayor dato del arbol.
    //El metodo debe estar optimizado aprovechando las caracteristicas
    //del Arbol B y no realizar operaciones excesivas
}
