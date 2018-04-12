package trismegistoplanilla.beans;

import java.io.Serializable;

public class SueldoAdministrativoBean implements Serializable {

  private static final long serialVersionUID = 4415085478243509927L;

  private int codigoSueldoAdministrativo;
  private int codigoFicha;
  private int codigoAreaCargoTipoPago;
  private String sueldoEscalafon;
  private String sueldoMensual;
  private String sueldoPresidencia;
  private String fechaRegistro;
  private String observacion;

  public int getCodigoSueldoAdministrativo() {
    return codigoSueldoAdministrativo;
  }

  public void setCodigoSueldoAdministrativo(int codigoSueldoAdministrativo) {
    this.codigoSueldoAdministrativo = codigoSueldoAdministrativo;
  }

  public int getCodigoFicha() {
    return codigoFicha;
  }

  public int getCodigoAreaCargoTipoPago() {
    return codigoAreaCargoTipoPago;
  }

  public void setCodigoAreaCargoTipoPago(int codigoAreaCargoTipoPago) {
    this.codigoAreaCargoTipoPago = codigoAreaCargoTipoPago;
  }

  public void setCodigoFicha(int codigoFicha) {
    this.codigoFicha = codigoFicha;
  }

  public String getSueldoEscalafon() {
    return sueldoEscalafon;
  }

  public void setSueldoEscalafon(String sueldoEscalafon) {
    this.sueldoEscalafon = sueldoEscalafon;
  }

  public String getSueldoMensual() {
    return sueldoMensual;
  }

  public void setSueldoMensual(String sueldoMensual) {
    this.sueldoMensual = sueldoMensual;
  }

  public String getSueldoPresidencia() {
    return sueldoPresidencia;
  }

  public void setSueldoPresidencia(String sueldoPresidencia) {
    this.sueldoPresidencia = sueldoPresidencia;
  }

  public String getObservacion() {
    return observacion;
  }

  public void setObservacion(String observacion) {
    this.observacion = observacion;
  }

  public String getFechaRegistro() {
    return fechaRegistro;
  }

  public void setFechaRegistro(String fechaRegistro) {
    this.fechaRegistro = fechaRegistro;
  }

}
