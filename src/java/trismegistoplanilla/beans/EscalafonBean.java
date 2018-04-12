package trismegistoplanilla.beans;

import java.io.Serializable;

public class EscalafonBean implements Serializable {

  private static final long serialVersionUID = -8762010449238102710L;
  private int codigoEscalafon;
  private String sueldo;
  private int codigoAreaCargoTipoPago;
  private int fechaRegistro;
  private String estadoRegistro;

  public int getCodigoEscalafon() {
    return codigoEscalafon;
  }

  public void setCodigoEscalafon(int codigoEscalafon) {
    this.codigoEscalafon = codigoEscalafon;
  }

  public String getSueldo() {
    return sueldo;
  }

  public void setSueldo(String sueldo) {
    this.sueldo = sueldo;
  }

  public int getCodigoAreaCargoTipoPago() {
    return codigoAreaCargoTipoPago;
  }

  public void setCodigoAreaCargoTipoPago(int codigoAreaCargoTipoPago) {
    this.codigoAreaCargoTipoPago = codigoAreaCargoTipoPago;
  }

  public int getFechaRegistro() {
    return fechaRegistro;
  }

  public void setFechaRegistro(int fechaRegistro) {
    this.fechaRegistro = fechaRegistro;
  }

  public String getEstadoRegistro() {
    return estadoRegistro;
  }

  public void setEstadoRegistro(String estadoRegistro) {
    this.estadoRegistro = estadoRegistro;
  }

  
}
