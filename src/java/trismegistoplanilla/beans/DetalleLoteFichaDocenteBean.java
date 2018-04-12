package trismegistoplanilla.beans;

import java.io.Serializable;

public class DetalleLoteFichaDocenteBean implements Serializable {

  private static final long serialVersionUID = -8762010449238102710L;
  private int codigoFicha;
  private int codigoPago;
  private String apellidoPaterno;
  private String apellidoMaterno;
  private String nombre;
  private String numeroDocumento;
  private String tipoDocumento;
  private String fechaInicio;
  private String mes;
  private String costoa;
  private String costob;
  private String costoc;
  private String costoMensual;
  private String observacion;

  public int getCodigoFicha() {
    return codigoFicha;
  }

  public int getCodigoPago() {
    return codigoPago;
  }

  public void setCodigoPago(int codigoPago) {
    this.codigoPago = codigoPago;
  }

  public String getTipoDocumento() {
    return tipoDocumento;
  }

  public void setTipoDocumento(String tipoDocumento) {
    this.tipoDocumento = tipoDocumento;
  }

  public void setCodigoFicha(int codigoFicha) {
    this.codigoFicha = codigoFicha;
  }

  public String getApellidoPaterno() {
    return apellidoPaterno;
  }

  public void setApellidoPaterno(String apellidoPaterno) {
    this.apellidoPaterno = apellidoPaterno;
  }

  public String getApellidoMaterno() {
    return apellidoMaterno;
  }

  public void setApellidoMaterno(String apellidoMaterno) {
    this.apellidoMaterno = apellidoMaterno;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNumeroDocumento() {
    return numeroDocumento;
  }

  public void setNumeroDocumento(String numeroDocumento) {
    this.numeroDocumento = numeroDocumento;
  }

  public String getFechaInicio() {
    return fechaInicio;
  }

  public void setFechaInicio(String fechaInicio) {
    this.fechaInicio = fechaInicio;
  }

  public String getCostoa() {
    return costoa;
  }

  public void setCostoa(String costoa) {
    this.costoa = costoa;
  }

  public String getCostob() {
    return costob;
  }

  public void setCostob(String costob) {
    this.costob = costob;
  }

  public String getCostoc() {
    return costoc;
  }

  public void setCostoc(String costoc) {
    this.costoc = costoc;
  }

  public String getCostoMensual() {
    return costoMensual;
  }

  public void setCostoMensual(String costoMensual) {
    this.costoMensual = costoMensual;
  }

  public String getObservacion() {
    return observacion;
  }

  public void setObservacion(String observacion) {
    this.observacion = observacion;
  }

  public String getMes() {
    return mes;
  }

  public void setMes(String mes) {
    this.mes = mes;
  }

}
