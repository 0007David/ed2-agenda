/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import java.util.Stack;

/**
 * Arbol B Caracteristica de un arbol B: -Todas la hojas estan en un mismo nivel
 * -El max y el min valor de un arbol B siempre estan en una hoja
 *  Ventajas
        - Cada vez que se insertar un dato el arbol crece literalmente para arriba
        - Con cada insercio y eliminacio el arbol queda perfectamente valanceado
        -Se tiene un coste de busqueda logaritmica
     Desventajas
        - Con cada insercion y un nodo lleno se crea otros dos nodos con un min de datos
         lo que implica que habla mucho mas nodos que en un arbol M Vias no valanceado
        - Al crearce nodos con los datos minimos de datos el arbol al tener mas nodos
          estara separando memorio que no se ocupa
 * @author Juan Torrez
 * @param <T>
 */
public class ArbolB<T extends Comparable<T>> extends ArbolBusquedaMVias<T> {

    private int exceso;
    private int nroMinDeDatos;
    private int nroMinDeHijos;
    private int nroMaxDeDatos;

    /**Constructor por defecto          */
    public ArbolB() {
        super(); //orden =3
        this.exceso = 4;
        this.nroMaxDeDatos = 2; // orden - 1
        this.nroMinDeDatos = 1; // orden / 2
        this.nroMinDeHijos = 2; //nroMinDeDatos +1
    }

    /**
     * Constructor para el arbol B donde se pasa el orden por parametro
     *
     * @param orden
     * @throws negocio.ExceptionArbolOrdenInvalido
     */
    public ArbolB(int orden) throws ExceptionArbolOrdenInvalido {
        super(orden);
        this.exceso = orden + 1;
        this.nroMaxDeDatos = orden - 1;
        this.nroMinDeDatos = this.nroMaxDeDatos / 2;
        this.nroMinDeHijos = nroMinDeDatos + 1;
        /* maxHijos = orden;
         maxDatos = orden-1;
        minHijos = (orden%2==0) ?  oden/2: (orden/2) +1 ;
        minDatos = minHijos -1;   
         */
    }

    @Override
    public boolean insertar(T datoAInsertar) {

        if (NodoMVias.esNodoVacio(raiz)) {
            raiz = new NodoMVias<>(exceso, datoAInsertar);
            return true;
        }
        Stack<NodoMVias<T>> pilaDePadres = new Stack<>();
        NodoMVias<T> nodoActual = raiz;

        while (!NodoMVias.esNodoVacio(nodoActual)) {
            if (nodoActual.esHoja()) {
                if (this.estaDatoEnElNodo(nodoActual, datoAInsertar)) {
                    return false; // valor de dato ya existe
                }
                //debe insertar orden + 1
                insertarDatoEnElNodoB(nodoActual, datoAInsertar);//modificar
                //verifica si no romple regla de maximo de datos
                if (getNroDeDatosEnNodo(nodoActual) <= nroMaxDeDatos) {
                    break; // Arbol -OK
                } else {//necesita dividirse
                    //esta aqui por que rompe regla maxDeDatos
                    dividir(nodoActual, pilaDePadres);//metodo no implemetado
                    break;
                }
            } else { // cuando no es hoja el nodoActual

                if (this.estaDatoEnElNodo(nodoActual, datoAInsertar)) {
                    return false; // valor de dato ya existe
                }
                NodoMVias<T> nodoAux = nodoActual;
                int nroDeDatosNodoActual = getNroDeDatosEnNodo(nodoActual);

                for (int i = 0; ((i < nroDeDatosNodoActual) && (nodoAux == nodoActual)); i++) {
                    T datoEnTurno = nodoActual.getDato(i);
                    if (datoAInsertar.compareTo(datoEnTurno) < 0) {
                        nodoAux = nodoActual.getHijo(i);
                    }
                }// End For
                if (nodoAux == nodoActual) {
                    nodoAux = nodoActual.getHijo(nroDeDatosNodoActual);
                }
                //si cambio de nodo el nodo actual es padre del nuevo nodo actual 
                pilaDePadres.push(nodoActual);
                nodoActual = nodoAux;
            } // Fin else (nodoActual.esHoja())
        }// fin while
        return true;
    }

    // metodos para insertar un dato
    protected int insertarDatoEnElNodoB(NodoMVias<T> nodoActual, T dato) { // |10|20|30| | |
        // en el nodo actual hay campo por lo menos para un dato
        //modificar que el permita insertar hasta la (posicion orden)
        int pos = -1;
        for (int i = 0; ((i < exceso - 1) && (pos == -1)); i++) {
            if (!nodoActual.esDatoVacio(i)) {
                T datoActual = nodoActual.getDato(i);
                if (dato.compareTo(datoActual) < 0) {
                    pos = i;
                }
            } else {
                pos = i;
            }
        }// end for
        for (int i = exceso - 2; i > pos; i--) { // hace espacio para el dato
            nodoActual.setDato(i, nodoActual.getDato(i - 1));
            nodoActual.setHijo(i + 1, nodoActual.getHijo(i));
        }
        nodoActual.setDato(pos, dato);
        return pos;
    }

    protected int getNroDeDatosEnNodo(NodoMVias<T> nodoActual) {
        return nodoActual.cantidadDeDatosNoVacios();

    }

    /**
     * Divide el nodo actual y si es necesario la parte de mas arriba
     *
     * @param nodoActual
     * @param pilaDePadres
     */
    private void dividir(NodoMVias<T> nodoActual, Stack<NodoMVias<T>> pilaDePadres) {
        //pos = posicion donde debo dividir     
        while ((!pilaDePadres.isEmpty()
                || getNroDeDatosEnNodo(nodoActual) > nroMaxDeDatos)) {
            T datoDeMedio = nodoActual.getDato(this.nroMinDeDatos);
            NodoMVias<T> nuevoNodo = new NodoMVias<>(exceso);
            divideNodo(nodoActual, nuevoNodo);
            if ((nodoActual == raiz)) {//creo una nueva raiz
                //creo nueva raiz
                NodoMVias<T> nuevoRaiz = new NodoMVias<>(exceso, datoDeMedio);
                nuevoRaiz.setHijo(0, nodoActual);
                nuevoRaiz.setHijo(1, nuevoNodo);
                raiz = nuevoRaiz; // modifica todo el arbol
                break;
            }
            NodoMVias<T> nodoPadre = new NodoMVias<>(exceso);
            nodoPadre = pilaDePadres.pop();
            int posDeInsertado = this.insertarDatoEnElNodoB(nodoPadre, datoDeMedio);
            nodoPadre.setHijo(posDeInsertado + 1, nuevoNodo);
            nodoActual = nodoPadre;
            if (getNroDeDatosEnNodo(nodoActual) <= nroMaxDeDatos) {
                break;
            }
        }

    }

    /**
     * Entra un nodoAct y lo modifica los dato e hijos si los hay nuevo nodo
     * entra vacio y lo devuelve con datos e hijos si los hay
     *
     * @param nodoAct
     * @param nuevoNodo
     */
    private void divideNodo(NodoMVias<T> nodoAct, NodoMVias<T> nuevoNodo) {
        nodoAct.setDato(this.nroMinDeDatos, (T) NodoMVias.datoVacio());
        int pos = 0;
        for (int i = nroMinDeDatos + 1; i < exceso - 1; i++) {
            nuevoNodo.setDato(pos, nodoAct.getDato(i));
            nodoAct.setDato(i, (T) NodoMVias.datoVacio());
            nuevoNodo.setHijo(pos, nodoAct.getHijo(i));
            nodoAct.setHijo(i, NodoMVias.nodoVacio());
            pos++;
        }
        nuevoNodo.setHijo(pos, nodoAct.getHijo(exceso - 1));
        nodoAct.setHijo(exceso - 1, NodoMVias.nodoVacio());
    }

    @Override
    public boolean eliminar(T datoAEliminar) {
        Stack<NodoMVias<T>> pilaDePadres = new Stack<>();
        NodoMVias<T> nodoAct = buscarNodoDeDato(datoAEliminar, pilaDePadres);
        //si el nododevuelto es nulo quiere decir que el datoAEliminar no existe en el arbol 
        if (NodoMVias.esNodoVacio(nodoAct)) {
            return false;
        }
        //obtener el indice donde esta el datoAElimnar en el nodoActual
        int indice = getPosicionDeDato(nodoAct, datoAEliminar);
        if (nodoAct.esHoja()) {// es hoja nodoActual
            this.eliminarDatoDeNodo(nodoAct, indice);
            NodoMVias<T> padre = pilaDePadres.isEmpty() ? NodoMVias.nodoVacio() : pilaDePadres.peek();
            if (!NodoMVias.esNodoVacio(padre)
                    && getNroDeDatosEnNodo(nodoAct) < nroMinDeDatos) {
                //realiza la operacion de Prestamo o Fusion considerando los casos de una hoja visto  
                //en el ejemplo de clases. Ademas de prestarse o fucionar llaves, este metodo
                //tambien van en el prestamo los hijos en caso de no ser hoja
                prestarseOFusionar(nodoAct, pilaDePadres); 
            } else if (NodoMVias.esNodoVacio(padre)
                    && getNroDeDatosEnNodo(nodoAct) == 0) {
                //si la raiz deja de tener datos dejar el arbol vacio
                this.vaciar();
            }
        } else { // caso de nodoAct no sea hoja
            // el nodo que no es hoja, hay que buscar quien reemplace al datoAEliminar.
            //El reemplazo estara en alguna hoja siguiente la rama del hijo
            // en la misma posicion de la llave que se elimina
            pilaDePadres.push(nodoAct);
            NodoMVias<T> hijoDeLadoIzqDeDatoEliminado = nodoAct.getHijo(indice);
            NodoMVias<T> nodoDelPredecesor = this.buscarNodoDelPredecesor(hijoDeLadoIzqDeDatoEliminado, pilaDePadres);
            //el nodo del predecesor sera hoja.
            T valorDeReemplazo = this.elimnarDatoMayorDelNodo(nodoDelPredecesor);
            nodoAct.setDato(indice, valorDeReemplazo);
            //si el nodo del q se saco el reeplazo queda con pocos datos, hay que ver si presta o fusionar
            NodoMVias<T> padre = pilaDePadres.isEmpty() ? NodoMVias.nodoVacio() : pilaDePadres.peek();
            if (!NodoMVias.esNodoVacio(padre)
                    && this.getNroDeDatosEnNodo(nodoDelPredecesor) < nroMinDeDatos) {
                this.prestarseOFusionar(nodoDelPredecesor, pilaDePadres);
            }

        }// fin del else (nodoAct no es hoja()
        return true;
    }

    //++++++++++++++++++Metodos auxiliares para eliminar un dato ++++++++++++++++++++++++++++++++++\\

    /**
     * metodo que busca el nodo de el dato a eliminar, y va poniendo en la pila
     * cada nodo superior al nodo buscado.
     *
     * @param datoAEliminar referencia de busqueda del nodo
     * @param pilaDePadres pila que almacena los nodo superiores al nodo buscado
     * @return el nodo buscado
     */
    private NodoMVias<T> buscarNodoDeDato(T datoAEliminar, Stack<NodoMVias<T>> pilaDePadres) {
        //si esta devuelve null
        NodoMVias<T> nodoAct = raiz;
        int posPorDondeBajar;
        while (!NodoMVias.esNodoVacio(nodoAct)) {
            posPorDondeBajar = this.obtenerPosicionPorDondeBajar(nodoAct, datoAEliminar);
            if (this.estaDatoEnElNodo(nodoAct, datoAEliminar)) {
                return nodoAct;
            }
            pilaDePadres.push(nodoAct);
            nodoAct = nodoAct.getHijo(posPorDondeBajar);
        }
        return NodoMVias.nodoVacio();
    }

   
    
    /** metodo mas realiza un prestamo o una fucion el proceso de eliminacion de un dato en un arbol B.
     * @param nodoAct nodo inicial a realizar un prestamo o fusion
     * @param pilaDePadres pila de padres que hay que ver si hay necesidad de un fusion o prestamo
     */
    private void prestarseOFusionar(NodoMVias<T> nodoAct, Stack<NodoMVias<T>> pilaDePadres) {
        //entra por que rompe la regla de nroMinDatos
        while (!pilaDePadres.isEmpty()) {
            NodoMVias<T> nodoPadre = pilaDePadres.pop();
            int prestamoOFusion = esPrestamoOFusion(nodoPadre, nodoAct);
            int posicion = this.posicionDeHijo(nodoPadre, nodoAct);
            if (prestamoOFusion > 0) {//prestamo
                if (prestamoOFusion == 1) {//prestamo por Izquierda
                    this.prestamoPorIzquierda(nodoAct, nodoPadre, posicion - 1);
                } else {//prestamo por Derecha
                    this.prestamoPorDerecha(nodoAct, nodoPadre, posicion + 1);
                }
            } else if (prestamoOFusion == -1) {//fusion por Izquierda
                this.fusionPorIzquierda(nodoAct, nodoPadre, posicion - 1);
            } else {//fusion por Derecha
                this.fusionPorDerecha(nodoAct, nodoPadre, posicion + 1);
            }
            if (nodoPadre == raiz) {
                if ((nodoPadre.cantidadDeDatosNoVacios() != 0)) {
                    //caso que la raiz tenga almenos un dato
                    raiz = nodoPadre;
                    break;
                } else if (prestamoOFusion == -1) {//por izquierda
                    //caso que raiz este con datos vacio hay una nueva raiz = nodoHerIz;
                    raiz = nodoPadre.getHijo(posicion - 1);
                    break;
                } else {//por derecha
                    //caso que raiz este con datos vacio hay una nueva raiz = nodoHerDer;
                    raiz = nodoPadre.getHijo(posicion + 1);
                    break;
                }
            }
            if (this.getNroDeDatosEnNodo(nodoPadre) >= nroMinDeDatos) {
                break;
            }
            nodoAct = nodoPadre;
        } //fin de While
    }
    
    /** metodo que resliza un prestamo por izquierda con su hermano vecino que tiene datos demas
     * @param nodoAct nodo que recibe el prestamo
     * @param padre padre que interactua el el prestamo
     * @param posHerIzq posicion del hermano vecino izquierdo que va relizar el prestamo
     */
    private void prestamoPorIzquierda(NodoMVias<T> nodoAct, NodoMVias<T> padre, int posHerIzq) {
        NodoMVias<T> nodoHerIzq = padre.getHijo(posHerIzq);
        T datoHerIz = nodoHerIzq.getDato(nodoHerIzq.cantidadDeDatosNoVacios() - 1);
        T datoDePadre = padre.getDato(posHerIzq);
        hacerEspacioLaPosicion0(nodoAct);
        nodoAct.setDato(0, datoDePadre);
        padre.setDato(posHerIzq, datoHerIz);
        nodoAct.setHijo(0, nodoHerIzq.getHijo(nodoHerIzq.cantidadDeDatosNoVacios()));
        nodoHerIzq.setHijo(nodoHerIzq.cantidadDeDatosNoVacios(), NodoMVias.nodoVacio());
        nodoHerIzq.setDato(nodoHerIzq.cantidadDeDatosNoVacios() - 1, (T) NodoMVias.datoVacio());
    }
    
    /** metodo que hace  espacio la posicion 0 en nodoActual para recibir un prestamo por Izquierda
     * @param nodoAct 
     */
    private void hacerEspacioLaPosicion0(NodoMVias<T> nodoAct) {
        int n = nodoAct.cantidadDeDatosNoVacios();
        for (int i = 0; i < n; i++) {
            nodoAct.setDato(n, nodoAct.getDato(n - 1));
            nodoAct.setHijo(n + 1, nodoAct.getHijo(n));
        }
        nodoAct.setHijo(1, nodoAct.getHijo(0));
    }

    /** metodo que resliza un prestamo por Derecha con su hermano vecino que tiene datos demas
     * @param nodoAct nodo que recibe el prestamo 
     * @param padre nodo padre que es intermediador del prestamo
     * @param posHerDer posicion del hermano derecho que va relizar el prestamo 
     */
    private void prestamoPorDerecha(NodoMVias<T> nodoAct, NodoMVias<T> padre, int posHerDer) {
        //solo funcion con nodos hojas
        NodoMVias<T> nodoHerDer = padre.getHijo(posHerDer);
        T datoDePadre = padre.getDato(posHerDer - 1);
        nodoAct.setDato(nodoAct.cantidadDeDatosNoVacios(), datoDePadre);
        padre.setDato(posHerDer - 1, nodoHerDer.getDato(0));
        nodoAct.setHijo(nodoAct.cantidadDeDatosNoVacios(), nodoHerDer.getHijo(0));
        contraer(nodoHerDer, 0);
    }
    /** metodo que contrae un nodo (reduce el nodo)
     * @param nodoAct nodo a sufrir cambios
     * @param pos posicion que se eliminara
     */
    private void contraer(NodoMVias<T> nodoAct, int pos) {
        int n = nodoAct.cantidadDeDatosNoVacios();
        if (n != 0) {
            for (int i = pos; i < n; i++) {
                nodoAct.setDato(i, nodoAct.getDato(i + 1));
                nodoAct.setHijo(i, nodoAct.getHijo(i + 1));
            }
            nodoAct.setHijo(n - 1, nodoAct.getHijo(n));
            nodoAct.setHijo(n, NodoMVias.nodoVacio());
            nodoAct.setDato(n - 1, (T) NodoMVias.datoVacio());
        }
    }
    /** metodo que raliza un fusion por izquierda
     * @param nodoAct nodo que necesita una fusion
     * @param nodopadre nodo padre que ayuda en la fusion
     * @param pos posicio de hermano que se fusionara con nodoActual
     */
    private void fusionPorIzquierda(NodoMVias<T> nodoAct, NodoMVias<T> nodopadre, int pos) {
        NodoMVias<T> nodoHerIz = nodopadre.getHijo(pos);
        T datoMedio = nodopadre.getDato(pos);
        this.contraer(nodopadre, pos);
        unirNodoPorIzq(nodoHerIz, nodoAct, datoMedio);
        nodopadre.setHijo(pos, nodoHerIz);
    }
    
    /**
     * metodo que une dos nodos donde en dato Medio es del nodo Padre
     * @param nodoHer 
     * @param nodoAct
     * @param datoMedio 
     */
    private void unirNodoPorIzq(NodoMVias<T> nodoHer, NodoMVias<T> nodoAct, T datoMedio) {
        int pos = nodoHer.cantidadDeDatosNoVacios();
        nodoHer.setDato(pos, datoMedio);
        int n = nodoAct.cantidadDeDatosNoVacios();
        for (int i = 0; i < n; i++) {
            pos++;
            nodoHer.setDato(pos, nodoAct.getDato(i));
            nodoAct.setDato(i, (T) NodoMVias.datoVacio());
        }
        pos = nodoHer.cantidadDeHijosNoVacios();
        n = nodoAct.cantidadDeHijosNoVacios(); // union de hijos si los hay
        for (int i = 0; i < n; i++) {
            nodoHer.setHijo(pos, nodoAct.getHijo(i));
            nodoAct.setHijo(i, NodoMVias.nodoVacio());
            pos++;
        }

    }
    
    /** metodo que realiza una fusion pro derecha 
     * @param nodoAct
     * @param nodopadre
     * @param pos posicion de el hermano derecho 
     */
    private void fusionPorDerecha(NodoMVias<T> nodoAct, NodoMVias<T> nodopadre, int pos) {
        NodoMVias<T> nodoHer = nodopadre.getHijo(pos);
        T datoMedio = nodopadre.getDato(pos - 1);
        this.contraer(nodopadre, pos - 1);
        unirPorDer(nodoHer, nodoAct, datoMedio);
    }
    
    /**metodo que reliza un union de nodos
     * @param nodoHerDer
     * @param nodoAct
     * @param datoMedio 
     */
    private void unirPorDer(NodoMVias<T> nodoHerDer, NodoMVias<T> nodoAct, T datoMedio) {
        this.hacerEspacioLaPosicion0(nodoHerDer);
        nodoHerDer.setDato(0, datoMedio);
        nodoHerDer.setHijo(0, nodoAct.getHijo(0));
    }

    /** metodo que determina si hay pestamo o fusion Los valores de retorno van a
     * ser (-2,-1,1,2) si es 1 hay prestamo por el hermano vecino izquierdo, si
     * es 2 hay prestamo por el hermano vecino derecho, -1 hay fusion por el
     * hermano vecino izquierdo, si es -2 hay prestamo por el hermano vecino Derecho
     * @param padre
     * @param nodoAct
     * @return                                                                  */
    private int esPrestamoOFusion(NodoMVias<T> padre, NodoMVias<T> nodoAct) {
        int posicion = posicionDeHijo(padre, nodoAct);//obteniendo posicion de nodoAct
        if (padre.cantidadDeDatosNoVacios() == posicion) {
            NodoMVias<T> hermano = padre.getHijo(posicion - 1);
            if (hermano.cantidadDeDatosNoVacios() > this.nroMinDeDatos) {
                return 1;
            } else {
                return -1;
            }
        } else if (posicion == 0) {
            NodoMVias<T> hermano = padre.getHijo(posicion + 1);
            if (hermano.cantidadDeDatosNoVacios() > this.nroMinDeDatos) {
                return 2;
            } else {
                return -2;
            }
        } else {
            NodoMVias<T> hermanoIz = padre.getHijo(posicion - 1);
            NodoMVias<T> hermanoDer = padre.getHijo(posicion + 1);
            if (hermanoIz.cantidadDeDatosNoVacios() > this.nroMinDeDatos) {
                return 1;
            }
            if (hermanoDer.cantidadDeDatosNoVacios() > this.nroMinDeDatos) {
                return 2;
            }
        }
        return -1; //fusionar  
    }
    
    /** metodo que devuelve la posicion que corresponde a un hijo 
     * @param nodoPadre
     * @param nodo
     * @return 
     */
    private int posicionDeHijo(NodoMVias<T> nodoPadre, NodoMVias<T> nodo) {
        for (int i = 0; i < nodoPadre.cantidadDeHijosNoVacios(); i++) {
            if (nodo == nodoPadre.getHijo(i)) {
                return i;
            }
        }
        return 0;
    }
    
    /** metodo que busca nodo predecesor donde se encuentra el predecesor de dato a eliminar.
     * @param nodoAct
     * @param pilaDePadres
     * @return nodo
     */
    private NodoMVias<T> buscarNodoDelPredecesor(NodoMVias<T> nodoAct, Stack<NodoMVias<T>> pilaDePadres) {
        // si o si hay nodo predecesor
        NodoMVias<T> nodo = nodoAct;
        while (!nodo.esHoja()) {
            pilaDePadres.push(nodo);
            nodo = nodo.getHijo(nodo.cantidadDeDatosNoVacios());
        }
        return nodo;
    }
    
    /** metodo que elimina el dato mayor en el nodo y devuelve el mismo
     * @param nodo
     * @return 
     */
    private T elimnarDatoMayorDelNodo(NodoMVias<T> nodo) {
        T datoPredecesor = nodo.getDato(nodo.cantidadDeDatosNoVacios() - 1);
        nodo.setDato(nodo.cantidadDeDatosNoVacios() - 1, (T) NodoMVias.datoVacio());
        return datoPredecesor;
    }
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\\
    //Ejercicios de Examen

    public T devuelveDatoMayor() {
        //devuelve dato mayor hasta el nivel n
        return this.devuelveDatoMayor(raiz);
    }

    private T devuelveDatoMayor(NodoMVias<T> nodoAct) {
        if (NodoMVias.esNodoVacio(nodoAct)) {
            return null;
        }
        int posicionDeDatoMayor = nodoAct.cantidadDeDatosNoVacios();
        if (!nodoAct.esHijoVacio(posicionDeDatoMayor)) {// && nivel > 0){
            return this.devuelveDatoMayor(nodoAct.getHijo(posicionDeDatoMayor));//nivel-1
        }
        return nodoAct.getDato(posicionDeDatoMayor - 1);
    }

    public NodoMVias<T> devuelveNodoDeDatoMayor() {//int nivel){
        //devuelve Nodo dato mayor hasta el nivel n
        return this.devuelveNodoDeDatoMayor(raiz);//, nivel);
    }

    private NodoMVias<T> devuelveNodoDeDatoMayor(NodoMVias<T> nodoAct) {//,int nivel){
        if (NodoMVias.esNodoVacio(nodoAct)) {
            return null;
        }
        NodoMVias<T> nodoM = NodoMVias.nodoVacio();
        int posicionDeDatoMayor = nodoAct.cantidadDeDatosNoVacios();
        if (!nodoAct.esHijoVacio(posicionDeDatoMayor)) {//&& nivel > 0){
            nodoM = devuelveNodoDeDatoMayor(nodoAct.getHijo(posicionDeDatoMayor));//,nivel-1);
            return nodoM;//nivel-1
        } else {
            return nodoAct;
        }

    }

    public int devuelveNivelDeDatoMayor() {
        //devuelve dato mayor hasta el nivel n
        return this.esArbolVacio() ? -1
                : this.devuelveNivelDeDatoMayor(raiz);
    }

    private int devuelveNivelDeDatoMayor(NodoMVias<T> nodoAct) {
        if ((nodoAct.esHoja())) {
            return 0;
        }
        int posicionDeDatoMayor = nodoAct.cantidadDeDatosNoVacios();
        if (!nodoAct.esHijoVacio(posicionDeDatoMayor)) {// && nivel > 0){
            int nivel = 1;
            return nivel + this.devuelveNivelDeDatoMayor(nodoAct.getHijo(posicionDeDatoMayor));//nivel-1
        }
        return -1;
    }

    public static void main(String arg[]) {
        System.out.println("---------ARBOL B-----------");
        Integer[] v = {39, 13, 20, 30, 49, 15, 16, 22, 25, 29, 33, 39, 41, 42, 45, 47, 52, 57, 63, 73};
        Integer[] v3 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 40, 39, 38, 37, 36, 35, 34, 33, 32, 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21};
        Integer[] v4 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
        ArbolB<Integer> b = null; 
        ArbolBusquedaMVias<Integer> b1 = null;
        try {
            b = new ArbolB<>(4);
            b1 = new ArbolBusquedaMVias<>(4);
        } catch (ExceptionArbolOrdenInvalido ex) {
            System.out.println(ex);
        }
        for (Integer dato : v4) {
            boolean bv = b.insertar(dato);
            b1.insertar(dato);
            //System.out.println(bv);
        }
        System.out.println(b.recorridoEnInOrden()+"\n" +"Altura: "+b.altura()+"size : "+ b.size());
        System.out.println(b1.recorridoEnInOrden()+"\n"+"Altura: "+b1.altura()+"\n"+"size : "+ b1.size());
        
    }

}
