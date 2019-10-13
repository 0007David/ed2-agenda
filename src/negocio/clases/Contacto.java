package negocio.clases;

import java.util.List;
import negocio.*;

/**
 *
 * @author HP
 */
public class Contacto implements Comparable< Contacto>, java.io.Serializable {

    private String nombre;      //cada caracter 2 bytes max de caracter == 28
    private Integer nroTelefono; // 4 bytes
    private String correo;    //cada caracter 2 bytes max de caracter == 28
    private String direccion;  //cada caracter 2 bytes max de caracter == 28 B/ Juan Azurduy
    private boolean activo;      // 1 byte

    public Contacto() {
        nombre = "";
        nroTelefono = 0;
        this.correo = "";
        this.direccion = "";
        activo = false;
    }

   
    public Contacto(String nombre, int numTelefono, boolean activo) {
        this.nombre = nombre;
        this.nroTelefono = numTelefono;
        this.activo = activo;
    }

    public Contacto(String nombre, int numTelefono, String correo, String direccion, boolean activo) {
        this.nombre = nombre;
        this.nroTelefono = numTelefono;
        this.correo = correo;
        this.direccion = direccion;
        this.activo = activo;
    }

    public String getCorreo() {
        if (correo.equals("")) {
            return Contacto.datosVacio();
        }
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        if (direccion.equals("")) {
            return Contacto.datosVacio();
        }
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Contacto(String nombre) {
        this.nombre = nombre;
        this.nroTelefono = Contacto.nroVacio();
    }

    public Contacto(int nroTelefono) {
        this.nroTelefono = nroTelefono;
        this.nombre = "";
    }

    public Integer getNroTelefono() {
        return nroTelefono;
    }

    public void setNroTelefono(Integer nroTelefono) {
        this.nroTelefono = nroTelefono;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    private static Integer nroVacio() {
        return null;
    }

    public static Contacto contactoVacio() {
        return null;
    }

    public static boolean esContactoVacio(Contacto c) {
        return (c == contactoVacio());
    }

    public static boolean esCorreoValido(String correo) {
        String extension = "@gmail.com";// davidtn52@gmail.com
        return correo.endsWith(extension);
    }

    public static String datosVacio() {
        return "...............";
    }

    @Override
    public String toString() {
        return nombre + " - " + nroTelefono;
    }

    /**
     * metodo que devuelve la cantidad de bytes del objeto
     *
     * @return
     */
    public int getCantidadDeBytes() {
        return this.getNombre().length() * 2 + this.getCorreo().length() * 2 + this.getDireccion().length() * 2 + 6 + 4 + 1;
    }

    /// no puede encontrar solo por el nombre porque podria haber varios con el mismo nombre y dif. num.
    // ni viseversa
    @Override
    public int compareTo(Contacto contactoAComparar) {
        int comparacion = this.nombre.compareTo(contactoAComparar.nombre);

        if (comparacion > 0) {
            return 1;
        } else if (comparacion < 0) {
            return -1;
        }
        if (this.nroTelefono != null && contactoAComparar.nroTelefono != null) {
            comparacion = this.nroTelefono.compareTo(contactoAComparar.nroTelefono);

            if (comparacion > 0) {
                return 1;
            } else if (comparacion < 0) {
                return -1;
            }
        }

        return 0;
    }

    public static void main(String arg[]) {
        Contacto c = new Contacto("DAVID");
        // System.out.println(c.getNombre());
        c.setCorreo("davidtn52@gmail.com");
        System.out.println(Contacto.esCorreoValido(c.getCorreo()));
        c.setCorreo("davidtn52@gmail.com");
        System.out.println(Contacto.esCorreoValido(c.getCorreo()));
    }
}
