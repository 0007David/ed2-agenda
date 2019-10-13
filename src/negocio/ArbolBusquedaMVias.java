/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author Juan Torrez
 * @param <T>
 */
public class ArbolBusquedaMVias<T extends Comparable<T>> implements IArbolBusqueda<T> {

    protected NodoMVias<T> raiz;
    protected int orden;

    public ArbolBusquedaMVias() {
        orden = 3;
    }

    public ArbolBusquedaMVias(int orden) throws ExceptionArbolOrdenInvalido{
          if(orden<3){
              throw new ExceptionArbolOrdenInvalido("Orden debe ser mayor a 3");
          }
        this.orden = orden;
    }

    @Override
    public boolean insertar(T dato) {
        if (this.esArbolVacio()) {
            this.raiz = new NodoMVias<>(this.orden, dato);
            return true;
        }
        NodoMVias<T> nodoActual = raiz;
        while (!NodoMVias.esNodoVacio(nodoActual)) {
            if (nodoActual.esHoja()) {
                if (this.estaDatoEnElNodo(nodoActual, dato)) {
                    return false;
                }
                //llegó aquí por que el dato no existe en el nodo actual
                if (nodoActual.estanLlenosTodosLosDatos()) {
                    int posicion = obtenerPosicionPorDondeBajar(nodoActual, dato);
                    NodoMVias<T> nuevoHijo = new NodoMVias<>(orden, dato);
                    nodoActual.setHijo(posicion, nuevoHijo);
                } else {
                    // llego aqui porque el nodo es hoja y tiene espacio por lo menos para un dato
                    insertarDatoEnElNodo(nodoActual, dato);
                }
                break;
            } else { // llega aquí por el nodo no es hoja
                if (this.estaDatoEnElNodo(nodoActual, dato)) {
                    return false;
                }
                int posicion = obtenerPosicionPorDondeBajar(nodoActual, dato);
                if (!nodoActual.esHijoVacio(posicion)) {
                    nodoActual = nodoActual.getHijo(posicion);
                } else {
                    NodoMVias<T> nuevoHijo = new NodoMVias<>(orden, dato);
                    nodoActual.setHijo(posicion, nuevoHijo);
                    break;
                }
            }
        } // end while
        return true;
    }

    /**
     * metodo que verifica si existe un dato en el nodo
     *
     * @param nodoActual
     * @param dato
     * @return un valor booleano true o false
     */
    protected boolean estaDatoEnElNodo(NodoMVias<T> nodoActual, T dato) {
        int n = nodoActual.cantidadDeDatosNoVacios();//para ser usado en cualquier clases de Arbol B
        for (int i = 0; i < n; i++) {
            T datoActual = nodoActual.getDato(i);
            if (datoActual.compareTo(dato) == 0) {
                return true;
            }
        } // end for
        return false;
    }

    /**
     * metodo que devuelve la posicion de un hijo por donde debe bajar con
     * respecto a un dato
     *
     * @param nodoActual
     * @param dato
     * @return devuelve un valor entero una posicion
     */
    protected int obtenerPosicionPorDondeBajar(NodoMVias<T> nodoActual, T dato) {// |10|20|30|40|50|
        // el nodoactual esta entrando con todos loa datos llenos no(preguntar por datoVacio(i))
        //para el uso de un arbol B importa los datos vacio (si pregunta por datoVacio(i))
        for (int i = 0; i < orden - 1; i++) {
            if (!nodoActual.esDatoVacio(i)) {
                T datoActual = nodoActual.getDato(i);
                if (dato.compareTo(datoActual) < 0) {
                    return i;
                }
            } else {
                return i;
            }
        } // end for
        return orden - 1;
    }

    /**
     * metodo que inserta un dato en el nodo (que un nodo hoja)
     *
     * @param nodoActual
     * @param dato
     */
    protected void insertarDatoEnElNodo(NodoMVias<T> nodoActual, T dato) { // |10|20|30| | |
        // en el nodo actual hay campo por lo menos para un dato
        int pos = -1;
        for (int i = 0; ((i < orden - 1) && (pos == -1)); i++) {
            if (!nodoActual.esDatoVacio(i)) {
                T datoActual = nodoActual.getDato(i);
                if (dato.compareTo(datoActual) < 0) {
                    pos = i;
                }
            } else {
                pos = i;
            }
        }// end for
        for (int i = orden - 2; i > pos; i--) { // hace espacio para el dato
            nodoActual.setDato(i, nodoActual.getDato(i - 1));
        }
        nodoActual.setDato(pos, dato);
    }

    @Override
    public boolean eliminar(T dato) {
        try {
            this.raiz = eliminar(this.raiz, dato);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private NodoMVias<T> eliminar(NodoMVias<T> nodoActual, T datoAEliminar) throws Exception {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            throw new Exception();
        }
        for (int i = 0; i < this.orden - 1; i++) {
            if (NodoMVias.esNodoVacio(nodoActual)) {
                throw new Exception();//si llego aca el dato no esta en el arbol
            }
            // aqui el dato de la posicion i no es vacio 
            T datoDePos = nodoActual.getDato(i);
            if (datoAEliminar.compareTo(datoDePos) == 0) {
                if (nodoActual.esHoja()) { // caso 1
                    eliminarDatoDeNodo(nodoActual, i);
                    if (nodoActual.estanTodosLosDatosVacios()) {
                        return NodoMVias.nodoVacio();
                    }
                    return nodoActual;
                }
                //si llego aca puede ser caso 2 o 3  
                T datoSucesor = buscarDatoSucesor(nodoActual, i + 1);//i+1 para buscar al sucesor del siguiente hijo
                if (datoSucesor != NodoMVias.datoVacio()) { // caso 2
                    nodoActual = eliminar(nodoActual, datoSucesor);
                    nodoActual.setDato(i, datoSucesor);
                    return nodoActual;
                }// estoy en el caso 3
                T datoPredecesor = buscarDatoPredecesor(nodoActual, i);
                nodoActual = eliminar(nodoActual, datoPredecesor);
                nodoActual.setDato(i, datoPredecesor);
                return nodoActual;
            } else if (datoAEliminar.compareTo(datoDePos) < 0) {
                NodoMVias<T> supuestoNuevoHijo = eliminar(nodoActual.getHijo(i), datoAEliminar);
                nodoActual.setHijo(i, supuestoNuevoHijo);
                return nodoActual;
            }
        }// End For
        NodoMVias<T> supuestoNuevoHijo = eliminar(nodoActual.getHijo(orden - 1), datoAEliminar);
        nodoActual.setHijo(orden - 1, supuestoNuevoHijo);
        return nodoActual;

    }

    /**
     * metodo que elimina un dato que esta en la posicion pos en el nodo
     *
     * @param nodoActual en donde se va eliminar dato
     * @param pos de dato a eliminar
     */
    protected void eliminarDatoDeNodo(NodoMVias<T> nodoActual, int pos) { // |10|20|30|40|50|
        int posEliminar = pos;
        //no entra si dato a eliminar es el ultimo dato del nodo
        for (int i = pos; i < orden - 2; i++) {
            posEliminar = i + 1;
            nodoActual.setDato(i, nodoActual.getDato(posEliminar));
        }//fin For
        //pone dato vacio despues de eliminar al dato 
        nodoActual.setDato(posEliminar, (T) NodoMVias.datoVacio());
    }

    /**
     * metodo q busca el dato sucesor de otro dato apartir de la posicion pos,
     * se utiliza para el eliminar dato para el caso 2
     *
     * @param nodoActual
     * @param pos
     * @return un T dato que es generico
     */
    private T buscarDatoSucesor(NodoMVias<T> nodoActual, int pos) { // |10|20|30|40|50|
        T datoSucesor = (T) NodoMVias.datoVacio();
        if (hayHijosApartirDeEstaPosicion(nodoActual, pos)) {// verifica si dato sucesor
            if (!nodoActual.esHijoVacio(pos)) {
                //si entra aqui es porq hay sucesor por el hijo del posicion (pos)
                NodoMVias<T> aux = nodoActual.getHijo(pos);
                while (datoSucesor == NodoMVias.datoVacio()) {
                    if (!aux.esHijoVacio(0)) {
                        //llego aqui por esta el dato sucesor mas abajo
                        aux = aux.getHijo(0);
                    } else {//dato sucesor es el primero de nodo aux
                        datoSucesor = aux.getDato(0);
                    }
                }
            } else { //llego aqui porq su sucesor es el siguiente()
                datoSucesor = nodoActual.getDato(pos);
            }
        }
        return datoSucesor;
    }

    /**
     * metodo auxiliar para buscar el dato sucesor, permite saber si existe al
     * menos un hijo Apartir de la posicion pos
     *
     * @param nodo
     * @param pos
     * @return devuelve true si hay hay hijos, false caso contrario
     */
    private boolean hayHijosApartirDeEstaPosicion(NodoMVias<T> nodo, int pos) {
        for (int i = pos; i < orden; i++) {
            if (!nodo.esHijoVacio(i)) {
                return true;
            }
        }//End For
        return false;
    }

    /**
     * metodo q busca el dato predecesor de otro dato apartir de la posicion
     * pos, se utiliza para el eliminar dato en el caso 3
     *
     * @param nodoActual
     * @param pos
     * @return T dato generico
     */
    private T buscarDatoPredecesor(NodoMVias<T> nodoActual, int pos) { // |10|20|30|40|50|
        T datoPredecesor = (T) NodoMVias.datoVacio();
        if (hayHijosAntesDeEstaPosicion(nodoActual, pos)) { //verifica si hay dato predecesor
            if (!nodoActual.esHijoVacio(pos)) {
                //hay hijo predecesor mas abajo
                NodoMVias<T> aux = nodoActual.getHijo(pos);
                while (datoPredecesor == NodoMVias.datoVacio()) {
                    if (!aux.esHijoVacio(orden - 1)) {
                        //dato predecesor esta mas abajo
                        aux = aux.getHijo(orden - 1);
                    } else {//hay por lo menos un dato 
                        for (int i = 0; ((i < orden - 1) && (!aux.esDatoVacio(i))); i++) {
                            datoPredecesor = aux.getDato(i);
                        }//End For
                    }
                }//End While
            } else {
                //dato predecesor es el de atras
                datoPredecesor = nodoActual.getDato(pos - 1);
            }
        }
        return datoPredecesor;
    }

    /**
     * metodo auxiliar para buscar el dato predecesor, permite saber si existe
     * al menos un hijo antes de la posicion pos
     *
     * @param nodoActual
     * @param pos
     * @return un valor booleano true si hay hijos false caso contrario
     */
    private boolean hayHijosAntesDeEstaPosicion(NodoMVias<T> nodoActual, int pos) {// |10|20|30|40|50| | |
        for (int i = 0; i < (pos + 1); i++) {
            if (!nodoActual.esHijoVacio(i)) {
                return true;
            }
        }
        return false;
    }

    /** metodo que retorna la posicion de un dato en un nodo cualquiera.
     * @param nodoAct
     * @param datoAEliminar
     * @return
     */
    protected int getPosicionDeDato(NodoMVias<T> nodoAct, T datoAEliminar) {
        // aqui entra por que en el nodoActual esta el dato a eliminar, por tanto posision nunca va devolver -1
        int posicion = -1;
        for (int i = 0; i < orden - 1 && posicion == -1; i++) {
            if (!nodoAct.esDatoVacio(i)) {
                T datoAct = nodoAct.getDato(i);
                if (datoAct.compareTo(datoAEliminar) == 0) {
                    posicion = i;
                }
            }
        }//end for
        return posicion;
    }

    @Override
    public T buscar(T dato) {
        return buscar(this.raiz, dato);
    }

    /**
     *
     * @param nodoActual
     * @param datoBuscado
     * @return
     */
    private T buscar(NodoMVias<T> nodoActual, T datoBuscado) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return (T) NodoMVias.datoVacio();
        }
        for (int i = 0; i < orden - 1; i++) {
            if (nodoActual.esDatoVacio(i)) {
                return (T) NodoMVias.datoVacio();
            }
            T datoDePos = nodoActual.getDato(i);
            if (datoBuscado.compareTo(datoDePos) == 0) {
                return datoDePos;
            }
            if (datoBuscado.compareTo(datoDePos) < 0) {
                return this.buscar(nodoActual.getHijo(i), datoBuscado);
            }
        }//End del For
        return this.buscar(nodoActual.getHijo(this.orden - 1), datoBuscado);
    }

    @Override
    public boolean contiene(T dato) {
        //    System.out.println(dato);
        T datoB = this.buscar(dato);
        if (datoB == NodoMVias.nodoVacio()) {
            return false;
        }
        return dato.compareTo(datoB) == 0;
    }

    @Override
    public List<T> recorridoEnInOrden() {
        List<T> recorrido = new LinkedList<>();
        recorridoEnInOrden(this.raiz, recorrido);
        return recorrido;
    }

    private void recorridoEnInOrden(NodoMVias<T> nodoAct, List<T> recorrido) {
        if (NodoMVias.esNodoVacio(nodoAct)) {
            return;
        }
        
        for (int i = 0; i < (orden - 1); i++) {
            recorridoEnInOrden(nodoAct.getHijo(i), recorrido);
            if (!nodoAct.esDatoVacio(i)) {
                recorrido.add(nodoAct.getDato(i));
            }
        }
        recorridoEnInOrden(nodoAct.getHijo(orden - 1), recorrido);
    }

    @Override
    public List<T> recorridoEnPreOrden() {
        List<T> recorrido = new LinkedList<>();
        recorridoEnPreOrden(this.raiz, recorrido);
        return recorrido;
    }

    private void recorridoEnPreOrden(NodoMVias<T> nodoAct, List<T> recorrido) {
        if (NodoMVias.esNodoVacio(nodoAct)) {
            return;
        }
        for (int i = 0; i < (orden - 1); i++) {
            if (!nodoAct.esDatoVacio(i)) {
                recorrido.add(nodoAct.getDato(i));
            }
            recorridoEnPreOrden(nodoAct.getHijo(i), recorrido);
        }
        recorridoEnPreOrden(nodoAct.getHijo(orden - 1), recorrido);
    }

    @Override
    public List<T> recorridoEnPostOrden() {
        List<T> recorrido = new LinkedList<>();
        recorridoEnPostOrden(this.raiz, recorrido);
        return recorrido;
    }

    private void recorridoEnPostOrden(NodoMVias<T> nodoAct, List<T> recorrido) {
        if (NodoMVias.esNodoVacio(nodoAct)) {
            return;
        }
        recorridoEnPostOrden(nodoAct.getHijo(0), recorrido);
        // funciona para todo los caso de los recorrido
        for (int i = 0; ((i < (orden - 1)) && (!nodoAct.esDatoVacio(i))); i++) {
            recorridoEnPostOrden(nodoAct.getHijo(i + 1), recorrido);
            // if (!nodoAct.esDatoVacio(i)) {
            recorrido.add(nodoAct.getDato(i));
            // }
        }
    }

    @Override
    public List<T> recorridoPorNiveles() {
        List<T> recorrido = new LinkedList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }
        Queue<NodoMVias<T>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(raiz);
        while (!colaDeNodos.isEmpty()) {
            NodoMVias<T> nodoActual = colaDeNodos.poll();
            for (int i = 0; i < this.orden; i++) {
                if (i < (orden - 1) && !nodoActual.esDatoVacio(i)) {
                    recorrido.add(nodoActual.getDato(i));
                }
                if (!nodoActual.esHijoVacio(i)) {
                    colaDeNodos.offer(nodoActual.getHijo(i));
                }
            }
        } //fin del while
        return recorrido;
    }

    @Override
    public int size() {
        return size(raiz);
    }

    private int size(NodoMVias<T> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        int sizeMayor = 0;
        for (int i = 0; i < orden; i++) {
            int sizeDeHijo = size(nodoActual.getHijo(i));
            sizeMayor = sizeMayor + sizeDeHijo;
        }//End For
        return sizeMayor + 1;
    }

    @Override
    public int altura() {
        return altura(raiz);
    }

    private int altura(NodoMVias<T> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        int alturaMayor = 0;
        for (int i = 0; i < orden; i++) {
            int alturaDeHijo = altura(nodoActual.getHijo(i));
            if (alturaDeHijo > alturaMayor) {
                alturaMayor = alturaDeHijo;
            }
        }// End For
        return alturaMayor + 1;
    }

    @Override
    public int nivel() {
        return nivel(this.raiz);
    }

    private int nivel(NodoMVias<T> nodoAct) {
        if (NodoMVias.esNodoVacio(nodoAct)) {
            return -1;
        }
        int nivelMayor = -1;
        for (int i = 0; i < orden; i++) {
            int nivelHijo = nivel(nodoAct.getHijo(i));
            if (nivelHijo > nivelMayor) {
                nivelMayor = nivelHijo;
            }
        }//End For
        return nivelMayor + 1;
    }

    @Override
    public void vaciar() {
        this.raiz = NodoMVias.nodoVacio();
    }

    @Override
    public boolean esArbolVacio() {
        return this.raiz == NodoMVias.nodoVacio();
    }

    public int cantidadDeDatosVacios() {
        return cantidadDeDatosVacios(raiz);
    }

    /**
     * devuelve la cantidad de datos vacios (uso recorrido en PostOrden)
     *
     * @param nodoActual
     * @return
     */
    private int cantidadDeDatosVacios(NodoMVias<T> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        int cantDatoVacio = 0;
        int cantidad1erLlamada = cantidadDeDatosVacios(nodoActual.getHijo(0));
        int cantidad2daLlamada = 0;
        for (int i = 0; i < (orden - 1); i++) {
            if (!nodoActual.esHoja()) {
                cantidad2daLlamada = cantidadDeDatosVacios(nodoActual.getHijo(i + 1));
            } else if (nodoActual.esDatoVacio(i)) {
                cantDatoVacio++;
            }
            cantDatoVacio = cantDatoVacio + cantidad2daLlamada;
        }//End For
        return cantidad1erLlamada + cantDatoVacio;
    }

    /**
     * devuelve la cantidad de datos vacios (uso recorrido en PreOrden)
     *
     * @param nodoActual
     * @return
     */
    private int cantidadDeDatosVacios1(NodoMVias<T> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        int cantDatoVacio = 0;
        int cantidad1eraLlamada = 0;
        for (int i = 0; i < (orden - 1); i++) {
            if (nodoActual.esDatoVacio(i)) {
                cantDatoVacio++;
            }
            if (!nodoActual.esHoja()) {
                cantidad1eraLlamada = cantidadDeDatosVacios1(nodoActual.getHijo(i));
            }
            cantDatoVacio = cantDatoVacio + cantidad1eraLlamada;
        }//End For
        return cantDatoVacio + cantidadDeDatosVacios1(nodoActual.getHijo(orden - 1));
    }

    public int cantidadDeDatos() {
        return cantidadDeDatos(this.raiz);
    }

    private int cantidadDeDatos(NodoMVias<T> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        int cantidadDeDatos = nodoActual.cantidadDeDatosNoVacios();
        for (int i = 0; i < (orden)&& !nodoActual.esHijoVacio(i); i++) {
            cantidadDeDatos = cantidadDeDatos+ cantidadDeDatos(nodoActual.getHijo(i));
        }
        return cantidadDeDatos;
    }

    //otra version de cantidad de datos
    //tarea
    public int cantidadDeHojas() {
        return cantidadDeHojas(this.raiz);
    }

    private int cantidadDeHojas(NodoMVias<T> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) 
            return 0;
        
        int cantidadDeHojas = 0;
        if (nodoActual.esHoja()) {
            cantidadDeHojas++;
        }
        for (int i = 0; i < orden; i++) {
            cantidadDeHojas = cantidadDeHojas + cantidadDeHojas(nodoActual.getHijo(i));
        }// End For
        return cantidadDeHojas;
    }

    //tarea
    /**
     * Devuelve la cantidad de nodo Incompletos hasta el nivel n
     *
     * @param n
     * @return
     */
    public int cantidadDeNodosIncompletosHastaElNivelN(int n) {
        return cantidadDeNodosIncompletosHastaElNivelN(raiz, n);
    }

    private int cantidadDeNodosIncompletosHastaElNivelN(NodoMVias<T> nodoActual, int nivel) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        int cantNodoIncompletos = 0;
        if (nodoActual.cantidadDeHijosNoVacios() != orden) {
            cantNodoIncompletos++;
        }
        for (int i = 0; i < orden; i++) {
            if (nivel > 0) {
                cantNodoIncompletos = cantNodoIncompletos + this.cantidadDeNodosIncompletosHastaElNivelN(nodoActual.getHijo(i), nivel - 1);
            }
        }//Fin For
        return cantNodoIncompletos;
    }

    /**
     * Devuelve la cantidad de nodos Incompletos desde el nivel n
     *
     * @param n
     * @return
     */
    public int cantidadDeNodosIncompletosDesdeElNivelN(int n) {
        return cantidadDeNodosIncompletosDesdeElNivelN(raiz, n);
    }

    private int cantidadDeNodosIncompletosDesdeElNivelN(NodoMVias<T> nodoActual, int nivel) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        int nodoIm = 0;
        if (nodoActual.cantidadDeHijosNoVacios() != orden) {
            nodoIm++;
        }
        int cantNodoIncompletos = 0;
        for (int i = 0; i < orden; i++) {
            cantNodoIncompletos = cantNodoIncompletos + cantidadDeNodosIncompletosDesdeElNivelN(nodoActual.getHijo(i), nivel - 1);
        }//Fin For
        if (nivel < 1) {
            cantNodoIncompletos = cantNodoIncompletos + nodoIm;
        }
        return cantNodoIncompletos;
    }

    /*Para un Arbol B imp. un metodo que devuelva en que nivel esta el mayor dato del arbol.Metodo optimizado
        no operaciones execivas(33)*/
    public int nivelDeDatoMayor() {
        if (!this.esArbolVacio()) {
            return nivelDeDatoMayor(this.raiz, raiz.getDato(0), 0);
        }
        return -1;
    }

    protected int nivelDeDatoMayor(NodoMVias<T> nodoAct, T datoMay, int nivel) {
        if (NodoMVias.esNodoVacio(nodoAct)) {
            return -1;
        }
        int nivelresultado = -1;
        T datoMayorDeNodo = this.datoMayorEnNodo(nodoAct);
        if (datoMayorDeNodo.compareTo(datoMay) > 0) {
            datoMay = datoMayorDeNodo;
            nivelresultado = nivel;
        }
        if (!nodoAct.esHoja()) {
            for (int i = orden - 1; i > 0; i--) {
                if (!nodoAct.esHijoVacio(i)) {
                    int llamadaRecursiva = this.nivelDeDatoMayor(nodoAct.getHijo(i), datoMay, nivel + 1);
                    if (llamadaRecursiva != -1) {
                        nivelresultado = llamadaRecursiva;
                        i = 0;
                    }
                }
            }//End For
        }
        return nivelresultado;
    }

    private T datoMayorEnNodo(NodoMVias<T> nodoAct) {
        T datoMayor = nodoAct.getDato(0);
        for (int i = 0; i < orden - 1; i++) {
            if (!nodoAct.esDatoVacio(i)) {
                T datoAct = nodoAct.getDato(i);
                if (datoAct.compareTo(datoMayor) > 0) {
                    datoMayor = datoAct;
                }
            }
        }//Fin For
        return datoMayor;
    }

    public NodoMVias<T> nodoDeStringDeMayorRepDeA() {
        if (esArbolVacio()) {
            return NodoMVias.nodoVacio();
        }
        return nodoDeStringDeMayorRepDeA(raiz, raiz);

    }

    private NodoMVias<T> nodoDeStringDeMayorRepDeA(NodoMVias<T> nodoAct, NodoMVias<T> nodoMay) {
        if (NodoMVias.esNodoVacio(nodoAct)) {
            return nodoMay;
        }
        int cant = cantidadDeLetraAEnElNodo(nodoAct);

        for (int i = 0; i < orden; i++) {
            NodoMVias<T> llamada = this.nodoDeStringDeMayorRepDeA(nodoAct.getHijo(i), nodoMay);
            int cantLlamda = cantidadDeLetraAEnElNodo(llamada);
            if (cantLlamda > cant) {
                nodoMay = llamada;
            }
            if (nodoAct.esHoja()) {
                i = orden - 1;
            }
        }
        System.out.println(cantidadDeLetraAEnElNodo(nodoAct));
        return nodoMay;
    }

    private int cantidadDeLetraAEnElNodo(NodoMVias<T> nodoAct) {
        int cantidad = 0;
        for (int i = 0; i < orden - 1; i++) {
            if (!nodoAct.esDatoVacio(i)) {
                String cadena = (String) nodoAct.getDato(i);
                for (int j = 0; j < cadena.length(); j++) {
                    Character c = cadena.charAt(j);
                    if (c=='a' || c=='A') {
                        cantidad++;
                    }
                }
            }
        }

        return cantidad;
    }
    //examen tipo A
    public boolean sonTodosHojasApartirDeNivelN(int nivel){
           return sonTodosHojasApartirDeNivelN(raiz,nivel);
    }
     private boolean sonTodosHojasApartirDeNivelN(NodoMVias<T> nodoAct,int nivel){
           if(NodoMVias.esNodoVacio(nodoAct))
               return false;
           if(nivel ==0){
              return sonHojasTodoSusHijos(nodoAct); 
           }
           boolean llamada=true;
           for (int i = 0; i < orden && llamada; i++) {
             llamada = sonTodosHojasApartirDeNivelN(nodoAct.getHijo(i), nivel-1);
         }
           return llamada;
     }
     private boolean sonHojasTodoSusHijos( NodoMVias<T> nodoAct){
          if(nodoAct.esHoja())
              return true;
          for (int i = 0; i < orden; i++) {
              if(!nodoAct.esHijoVacio(i)){
                  if(!nodoAct.getHijo(i).esHoja())
                      return false;
          }
         }
          return true;
     }
     // dato un nivel devolver la cantidad de nodos Completos (tiene todos los datos e hijos)
    public static void main(String arg[]) {
        ArbolBusquedaMVias<String> c = null;
         try{
         c = new ArbolBusquedaMVias<>(4);
         }catch(ExceptionArbolOrdenInvalido ex){
             System.out.println(ex);
         }
        c.insertar("mama");
        c.insertar("casa");
        c.insertar("papa");
        c.insertar("ana");
        c.insertar("abel");
        c.insertar("cesar");
        c.insertar("julio");
        c.insertar("maria");
        c.insertar("marta");
        c.insertar("profe");

        System.out.println(c.recorridoPorNiveles());
        NodoMVias<String> nodo = c.nodoDeStringDeMayorRepDeA();
        System.out.println("Nodo = " + nodo);

    }
}
