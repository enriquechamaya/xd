package trismegistoplanilla.beans;

import java.io.Serializable;

public class NivelEstadoBean implements Serializable {

    private static final long serialVersionUID = -3061163091237095855L;

    private int codigoNivelEstado;
    private int codigoNivelEstudio;
    private int codigoEstadoEstudio;
    private int estado_registro;

    public int getCodigoNivelEstado() {
        return codigoNivelEstado;
    }

    public void setCodigoNivelEstado(int codigoNivelEstado) {
        this.codigoNivelEstado = codigoNivelEstado;
    }

    public int getCodigoNivelEstudio() {
        return codigoNivelEstudio;
    }

    public void setCodigoNivelEstudio(int codigoNivelEstudio) {
        this.codigoNivelEstudio = codigoNivelEstudio;
    }

    public int getCodigoEstadoEstudio() {
        return codigoEstadoEstudio;
    }

    public void setCodigoEstadoEstudio(int codigoEstadoEstudio) {
        this.codigoEstadoEstudio = codigoEstadoEstudio;
    }

    public int getEstado_registro() {
        return estado_registro;
    }

    public void setEstado_registro(int estado_registro) {
        this.estado_registro = estado_registro;
    }

    

}
