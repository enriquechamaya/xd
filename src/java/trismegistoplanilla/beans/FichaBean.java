package trismegistoplanilla.beans;

import java.io.Serializable;

public class FichaBean implements Serializable {

    private static final long serialVersionUID = -6298816690611277939L;

    private int codigoFicha;
    private int codigoPersona;
    private int estadoRegistro;

    public int getCodigoFicha() {
        return codigoFicha;
    }

    public void setCodigoFicha(int codigoFicha) {
        this.codigoFicha = codigoFicha;
    }

    public int getCodigoPersona() {
        return codigoPersona;
    }

    public void setCodigoPersona(int codigoPersona) {
        this.codigoPersona = codigoPersona;
    }

    public int getEstadoRegistro() {
        return estadoRegistro;
    }

    public void setEstadoRegistro(int estadoRegistro) {
        this.estadoRegistro = estadoRegistro;
    }

}
