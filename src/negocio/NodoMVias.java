/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HP
 * @param <T>
 */
public class NodoMVias<T>{

    private final List<T> listaDeDatos;
    private final List< NodoMVias<T>> listaDeHijos;

    public NodoMVias(int orden) {
        listaDeDatos = new ArrayList<>();
        listaDeHijos = new ArrayList<>();
        for (int i = 0; i < orden; i++) {
            if (i < (orden - 1)) {
                this.listaDeDatos.add((T) NodoMVias.datoVacio());
            }
            this.listaDeHijos.add(NodoMVias.nodoVacio());
        }
    }

    public NodoMVias(int orden, T primerDato) {
        this(orden);
        this.listaDeDatos.set(0, primerDato);
    }

    /**
     * Retorna el dato de una posicion en la lista de dato. PRE: La posicion es
     * valida
     *
     * @param posicion
     * @return Un dato de la lista de datos
     */
    public T getDato(int posicion) {
        return this.listaDeDatos.get(posicion);
    }

    public void setDato(int posicion, T unDato) {
        this.listaDeDatos.set(posicion, unDato);
    }

    public void setDatoVacio(int posicion) {
        this.listaDeDatos.set(posicion, (T) NodoMVias.datoVacio());
    }

    public boolean esDatoVacio(int posicion) {
        return this.listaDeDatos.get(posicion) == NodoMVias.datoVacio();
    }

    public NodoMVias<T> getHijo(int posicion) {
        return this.listaDeHijos.get(posicion);
    }

    public void setHijo(int posicion, NodoMVias<T> unHijo) {
        this.listaDeHijos.set(posicion, unHijo);
    }

    public boolean esHijoVacio(int posicion) {
        return this.listaDeHijos.get(posicion) == NodoMVias.nodoVacio();
    }

    //metodos compartidos por todas las clases (static) 
    //por lo tanto no pueden ser genericos
    public static NodoMVias nodoVacio() {
        return null;
    }

    public static Object datoVacio() {
        return null;
    }

    public static boolean esNodoVacio(NodoMVias nodoActual){
        return NodoMVias.nodoVacio() == nodoActual;
    }
    
    //** metodos adicionales**//
    public boolean esHoja() {
        for (int i = 0; i < listaDeHijos.size(); i++) {
            if (!esHijoVacio(i)) { //if(this.listaDeHijos.get(i) != NodoMVias.nodoVacio()){
                return false;
            }
        }
        return true;
    }

    public boolean estanLlenosTodosLosDatos() {
        for (int i = 0; i < this.listaDeDatos.size(); i++) {
            if (this.esDatoVacio(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean estanTodosLosDatosVacios() {
        for (int i = 0; i < this.listaDeDatos.size(); i++) {
            if (!this.esDatoVacio(i)) {
                return false;
            }
        }
        return true;
    }

    public int cantidadDeDatosNoVacios() {
        int cantidadDeDatos = 0;
        for (int i = 0; i < this.listaDeDatos.size(); i++) {
            if (!this.esDatoVacio(i)) {
                cantidadDeDatos++;
            }
        }
        return cantidadDeDatos;
    }

    public int cantidadDeHijosNoVacios() {
        int cantidadDeHijos = 0;
        for (int i = 0; i < this.listaDeHijos.size(); i++) {
            if (!this.esHijoVacio(i)) {
                cantidadDeHijos++;
            }
        }
        return cantidadDeHijos;
    }

    @Override
    public String toString() {
        return "NodoMVias{" + "listaDeDatos=" + listaDeDatos + '}';
    }

    
}
