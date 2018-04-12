package trismegistoplanilla.beans;

import java.io.Serializable;

public class NacionalidadBean implements Serializable {

    private static final long serialVersionUID = -3061163091237095855L;

    private int codigoNacionalidad;
    private String pais;
    private String gentilicio;
    private String iso;
    private int estadoRegistro;

    public int getCodigoNacionalidad() {
        return codigoNacionalidad;
    }

    public void setCodigoNacionalidad(int codigoNacionalidad) {
        this.codigoNacionalidad = codigoNacionalidad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getGentilicio() {
        return gentilicio;
    }

    public void setGentilicio(String gentilicio) {
        this.gentilicio = gentilicio;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public int getEstadoRegistro() {
        return estadoRegistro;
    }

    public void setEstadoRegistro(int estadoRegistro) {
        this.estadoRegistro = estadoRegistro;
    }


}
