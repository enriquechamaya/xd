package trismegistoplanilla.beans;

import java.io.Serializable;

public class EstadoFichaBean implements Serializable {

    private static final long serialVersionUID = -5886633619752526490L;

    private int codigoEstadoFicha;
    private int codigoFicha;
    private int codigoTipoEstadoFicha;
    private String fechaRegistro;
    private int codigoUsuario;
    private String estadoRegistro;

    public int getCodigoEstadoFicha() {
        return codigoEstadoFicha;
    }

    public void setCodigoEstadoFicha(int codigoEstadoFicha) {
        this.codigoEstadoFicha = codigoEstadoFicha;
    }

    public int getCodigoFicha() {
        return codigoFicha;
    }

    public void setCodigoFicha(int codigoFicha) {
        this.codigoFicha = codigoFicha;
    }

    public int getCodigoTipoEstadoFicha() {
        return codigoTipoEstadoFicha;
    }

    public void setCodigoTipoEstadoFicha(int codigoTipoEstadoFicha) {
        this.codigoTipoEstadoFicha = codigoTipoEstadoFicha;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

  public int getCodigoUsuario() {
    return codigoUsuario;
  }

  public void setCodigoUsuario(int codigoUsuario) {
    this.codigoUsuario = codigoUsuario;
  }

    public String getEstadoRegistro() {
        return estadoRegistro;
    }

    public void setEstadoRegistro(String estadoRegistro) {
        this.estadoRegistro = estadoRegistro;
    }

}
