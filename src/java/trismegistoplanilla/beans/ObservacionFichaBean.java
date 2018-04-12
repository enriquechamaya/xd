package trismegistoplanilla.beans;

import java.io.Serializable;

public class ObservacionFichaBean implements Serializable {

  private static final long serialVersionUID = 479825527454844805L;

  private int codigoObservacionFicha;
  private int codigoEstadoFicha;
  private int codigoFicha;
  private String observacion;
  private boolean estadoRegistro;

  public int getCodigoObservacionFicha() {
    return codigoObservacionFicha;
  }

  public void setCodigoObservacionFicha(int codigoObservacionFicha) {
    this.codigoObservacionFicha = codigoObservacionFicha;
  }

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

  public String getObservacion() {
    return observacion;
  }

  public void setObservacion(String observacion) {
    this.observacion = observacion;
  }

  public boolean isEstadoRegistro() {
    return estadoRegistro;
  }

  public void setEstadoRegistro(boolean estadoRegistro) {
    this.estadoRegistro = estadoRegistro;
  }

}
