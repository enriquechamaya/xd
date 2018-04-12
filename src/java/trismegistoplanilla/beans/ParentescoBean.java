package trismegistoplanilla.beans;

import java.io.Serializable;

public class ParentescoBean implements Serializable {

    private static final long serialVersionUID = -3061163091237095855L;

    private int codigoParentesco;
    private String nombre;
    private int estado_registro;

    public int getCodigoParentesco() {
        return codigoParentesco;
    }

    public void setCodigoParentesco(int codigoParentesco) {
        this.codigoParentesco = codigoParentesco;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEstado_registro() {
        return estado_registro;
    }

    public void setEstado_registro(int estado_registro) {
        this.estado_registro = estado_registro;
    }

}
