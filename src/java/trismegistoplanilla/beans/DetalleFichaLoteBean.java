package trismegistoplanilla.beans;

import java.io.Serializable;

public class DetalleFichaLoteBean implements Serializable {

  private static final long serialVersionUID = -5435570913005935859L;

  private int codigoDetalleFichaLote;
  private int codigoFichaLote;
  private int codigoFicha;

  public int getCodigoDetalleFichaLote() {
    return codigoDetalleFichaLote;
  }

  public void setCodigoDetalleFichaLote(int codigoDetalleFichaLote) {
    this.codigoDetalleFichaLote = codigoDetalleFichaLote;
  }

  public int getCodigoFichaLote() {
    return codigoFichaLote;
  }

  public void setCodigoFichaLote(int codigoFichaLote) {
    this.codigoFichaLote = codigoFichaLote;
  }

  public int getCodigoFicha() {
    return codigoFicha;
  }

  public void setCodigoFicha(int codigoFicha) {
    this.codigoFicha = codigoFicha;
  }

}
