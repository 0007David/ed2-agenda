/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.clases;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author JJTN
 */
public class AccesoAleatorio {

    private static RandomAccessFile flujo;
    private static int cantidadDeContactos;
    private static final int tamañoDeCadaContacto = 179;

    public static void crearFileContactos(File archivo) throws IOException {
        if (archivo.exists() && !archivo.isFile()) {
            throw new IOException(archivo.getName() + " no es un archivo");
        }
        flujo = new RandomAccessFile(archivo, "rw");
        cantidadDeContactos = (int) Math.ceil(
                (double) flujo.length() / (double) tamañoDeCadaContacto);
    }

    public static void cerrar() throws IOException {
        flujo.close();
    }

    public static boolean setContacto(int i, Contacto contacto) throws IOException {
        if (i >= 0 && i <= cantidadDeContactos) {
            if (contacto.getCantidadDeBytes() > tamañoDeCadaContacto) {
                System.out.println("\nTamaño de registro excedido.");
            } else {
                flujo.seek(i * tamañoDeCadaContacto);
                flujo.writeUTF(contacto.getNombre());
                flujo.writeInt(contacto.getNroTelefono());
                flujo.writeUTF(contacto.getCorreo());
                flujo.writeUTF(contacto.getDireccion());
                flujo.writeBoolean(contacto.isActivo());
                return true;
            }
        } else {
            System.out.println("\nNúmero de registro fuera de límites.");
        }
        return false;
    }

    private static int buscarContactoApagado() throws IOException {

        for (int i = 0; i < cantidadDeContactos; i++) {
            flujo.seek(i * tamañoDeCadaContacto);
            if (!getContacto(i).isActivo()) {
                return i;
            }
        }
        return -1;
    }

    public static boolean eliminarContacto(String aEliminar) throws IOException {
        int pos = buscarContacto(aEliminar);
        if (pos == -1) {
            return false;
        }
        Contacto personaEliminada = getContacto(pos);
        personaEliminada.setActivo(false);
        setContacto(pos, personaEliminada);
        return true;
    }

    //metodo para eliminar los contatos en el archivo
    public static void compactarArchivo(File archivo) throws IOException {
        crearFileContactos(archivo); // Abrimos el flujo.
        Contacto[] listado = new Contacto[cantidadDeContactos];
        for (int i = 0; i < cantidadDeContactos; ++i) {
            listado[i] = getContacto(i);
        }
        cerrar(); // Cerramos el flujo.
        archivo.delete(); // Borramos el archivo.

        File tempo = new File("temporal.txt");
        crearFileContactos(tempo); // Como no existe se crea.
        for (Contacto p : listado) {
            if (p.isActivo()) {
                añadirContacto(p);
            }
        }
        cerrar();

        tempo.renameTo(archivo); // Renombramos.
    }

    public static void eliminarDatosArchivo(File f) throws IOException {
        crearFileContactos(f); // Abrimos el flujo.
        Contacto[] listado = new Contacto[cantidadDeContactos];
        for (int i = 0; i < cantidadDeContactos; ++i) {
            listado[i] = getContacto(i);
            listado[i].setActivo(false);
        }
        cerrar(); // Cerramos el flujo.
        crearFileContactos(f);
        for (Contacto c : listado) {
            añadirContacto(c);
        }
        compactarArchivo(f);
    }

    public static void moverUnArchivoAOtro(File origen, File destino) throws IOException {
        crearFileContactos(origen); // Abrimos el flujo.
        Contacto[] listado = new Contacto[cantidadDeContactos];
        for (int i = 0; i < cantidadDeContactos; ++i) {
            listado[i] = getContacto(i);
        }
        cerrar(); // Cerramos el flujo.
        origen.delete(); // Borramos el archivo.

        //File tempo = new File("temporal.dat");
        crearFileContactos(destino); // Como no existe se crea.
        for (Contacto p : listado) {
            añadirContacto(p);
        }
        cerrar();

    }

    public static void añadirContacto(Contacto contacto) throws IOException {
        int indiceApagado = buscarContactoApagado();
        if (indiceApagado == -1) { // llego a la ultima pos
            if (setContacto(cantidadDeContactos, contacto)) {
                cantidadDeContactos++;
            }
            return;
        }
        //aqui añade sobre un contacto Eliminado
        if (setContacto(indiceApagado, contacto)) {
            cantidadDeContactos++;
        }
    }

    public static int getCantidadDeContactos(File f) {
        try {
            crearFileContactos(f);

        } catch (Exception ex) {
            System.out.println("error al obtener cantidadContacto");
            System.out.println(ex.getMessage());
        }
        return cantidadDeContactos;
    }

    //devuelve un contacto de un posicion 
    public static Contacto getContacto(int posicion) throws IOException {

        if (posicion >= 0 && posicion <= cantidadDeContactos) {
            // apunta el flujo en la direccion correcta 
            flujo.seek(posicion * tamañoDeCadaContacto);
            Contacto c = new Contacto(flujo.readUTF(), flujo.readInt(), flujo.readUTF(), flujo.readUTF(), flujo.readBoolean());
            return c;
        } else {
            System.out.println("\nNúmero de registro fuera de límites.");
            return null;
        }
    }

    public static int buscarContacto(String nombreBuscado) {

        try {
            if (nombreBuscado == null) {
                return -1;
            }
            for (int i = 0; i < cantidadDeContactos; i++) {
                flujo.seek(i * tamañoDeCadaContacto);
                Contacto contacto = getContacto(i);
                if (contacto.getNombre().equals(nombreBuscado) && contacto.isActivo()) {
                    return i;
                }
            }
        } catch (IOException ex) {
            System.out.println("Error al buscar Contactos");
            System.out.println(ex.getMessage());
        }
        return -1;

    }

    public static List<Contacto> listaDeContactosEnArchivo(File f) {
        List<Contacto> list = new LinkedList<>();

        if (getCantidadDeContactos(f) > 0) {
            //hay contactos 
            try {
                Contacto aux;
                for (int i = 0; i < getCantidadDeContactos(f); i++) {
                    flujo.seek(i * tamañoDeCadaContacto);
                    aux = getContacto(i);
                    if (aux.isActivo()) {
                        list.add(aux);
                    }
                }

            } catch (Exception ex) {
                System.out.println("Error en Obtener la lista de contactos");
                System.out.println(ex.getMessage());
            }
        }

        return list;
    }

}
