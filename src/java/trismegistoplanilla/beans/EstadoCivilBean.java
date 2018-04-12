package trismegistoplanilla.beans;

import java.io.Serializable;

public class EstadoCivilBean implements Serializable {

    private static final long serialVersionUID = -3061163091237095855L;

    private int codigoEstadoCivil;
    private String nombre;
    private int estado_registro;

    public int getCodigoEstadoCivil() {
        return codigoEstadoCivil;
    }

    public void setCodigoEstadoCivil(int codigoEstadoCivil) {
        this.codigoEstadoCivil = codigoEstadoCivil;
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
