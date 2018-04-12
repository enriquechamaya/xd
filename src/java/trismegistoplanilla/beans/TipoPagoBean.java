package trismegistoplanilla.beans;

import java.io.Serializable;

public class TipoPagoBean implements Serializable {

  private static final long serialVersionUID = 4131273473397472088L;

  private int codigoTipoPago;
  private String nombre;

  public int getCodigoTipoPago() {
    return codigoTipoPago;
  }

  public void setCodigoTipoPago(int codigoTipoPago) {
    this.codigoTipoPago = codigoTipoPago;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

}
