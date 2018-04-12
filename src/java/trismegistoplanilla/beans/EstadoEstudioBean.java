package trismegistoplanilla.beans;

import java.io.Serializable;

public class EstadoEstudioBean implements Serializable {

    private static final long serialVersionUID = -3061163091237095855L;
    private int codigoEstadoEstudio;
    private int codigoNivelEstudio;
    private String nombre;
    private int estado_registro;

    public int getCodigoEstadoEstudio() {
        return codigoEstadoEstudio;
    }

    public void setCodigoEstadoEstudio(int codigoEstadoEstudio) {
        this.codigoEstadoEstudio = codigoEstadoEstudio;
    }

    public int getCodigoNivelEstudio() {
        return codigoNivelEstudio;
    }

    public void setCodigoNivelEstudio(int codigoNivelEstudio) {
        this.codigoNivelEstudio = codigoNivelEstudio;
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
