package trismegistoplanilla.beans;

import java.io.Serializable;

public class FondoPensionBean implements Serializable {

    private static final long serialVersionUID = -3061163091237095855L;
    private int codigoFondoPension;
    private String descripcionCorta;
    private String descripcionLarga;
    private int estadoRegistro;

    public int getCodigoFondoPension() {
        return codigoFondoPension;
    }

    public void setCodigoFondoPension(int codigoFondoPension) {
        this.codigoFondoPension = codigoFondoPension;
    }

    public String getDescripcionCorta() {
        return descripcionCorta;
    }

    public void setDescripcionCorta(String descripcionCorta) {
        this.descripcionCorta = descripcionCorta;
    }

    public String getDescripcionLarga() {
        return descripcionLarga;
    }

    public void setDescripcionLarga(String descripcionLarga) {
        this.descripcionLarga = descripcionLarga;
    }

    public int getEstadoRegistro() {
        return estadoRegistro;
    }

    public void setEstadoRegistro(int estadoRegistro) {
        this.estadoRegistro = estadoRegistro;
    }

}
