package trismegistoplanilla.beans;

import java.io.Serializable;

public class AreaCargoTipoPagoBean implements Serializable {

  private static final long serialVersionUID = -2054303887097264028L;
  private int codigoAreaCargoTipoPago;
  private int codigoAreaCargo;
  private int codigoTipoPago;

  public int getCodigoAreaCargoTipoPago() {
    return codigoAreaCargoTipoPago;
  }

  public void setCodigoAreaCargoTipoPago(int codigoAreaCargoTipoPago) {
    this.codigoAreaCargoTipoPago = codigoAreaCargoTipoPago;
  }

  public int getCodigoAreaCargo() {
    return codigoAreaCargo;
  }

  public void setCodigoAreaCargo(int codigoAreaCargo) {
    this.codigoAreaCargo = codigoAreaCargo;
  }

  public int getCodigoTipoPago() {
    return codigoTipoPago;
  }

  public void setCodigoTipoPago(int codigoTipoPago) {
    this.codigoTipoPago = codigoTipoPago;
  }

}
