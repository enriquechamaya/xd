package trismegistoplanilla.beans;

import java.io.Serializable;

public class DatosAdministrativosBean implements Serializable {

  private static final long serialVersionUID = 4594333294248399680L;

  // datos administrativos
  private int codigoFicha;
  private String fechaIngreso;
  private String fechaTermino;
  private String sede;
  private String area;
  private String cargo;
  private String tipoPago;
  private String tipoFicha;
  private int codigoAreaCargoTipoPago;

  // sueldo administrativo
  private String sueldoEscalafon;
  private String sueldoMensual;
  private String sueldoPresidencia;
  private String observacionAdministrativo;
  private String descripcionEstadoAdministrativo;
  private String estadoAdministrativo;
  private String fechaRegistroAdministrativo;

  // sueldo docente
  private String costoMensual;
  private String costoA;
  private String costoB;
  private String costoC;
  private String observacionDocente;
  private String descripcionEstadoDocente;
  private String estadoDocente;
  private String fechaRegistroDocente;

  public int getCodigoAreaCargoTipoPago() {
    return codigoAreaCargoTipoPago;
  }

  public void setCodigoAreaCargoTipoPago(int codigoAreaCargoTipoPago) {
    this.codigoAreaCargoTipoPago = codigoAreaCargoTipoPago;
  }

  public int getCodigoFicha() {
    return codigoFicha;
  }

  public void setCodigoFicha(int codigoFicha) {
    this.codigoFicha = codigoFicha;
  }

  public String getFechaIngreso() {
    return fechaIngreso;
  }

  public void setFechaIngreso(String fechaIngreso) {
    this.fechaIngreso = fechaIngreso;
  }

  public String getFechaTermino() {
    return fechaTermino;
  }

  public void setFechaTermino(String fechaTermino) {
    this.fechaTermino = fechaTermino;
  }

  public String getSede() {
    return sede;
  }

  public void setSede(String sede) {
    this.sede = sede;
  }

  public String getArea() {
    return area;
  }

  public void setArea(String area) {
    this.area = area;
  }

  public String getCargo() {
    return cargo;
  }

  public void setCargo(String cargo) {
    this.cargo = cargo;
  }

  public String getTipoPago() {
    return tipoPago;
  }

  public void setTipoPago(String tipoPago) {
    this.tipoPago = tipoPago;
  }

  public String getTipoFicha() {
    return tipoFicha;
  }

  public void setTipoFicha(String tipoFicha) {
    this.tipoFicha = tipoFicha;
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

  public String getObservacionAdministrativo() {
    return observacionAdministrativo;
  }

  public void setObservacionAdministrativo(String observacionAdministrativo) {
    this.observacionAdministrativo = observacionAdministrativo;
  }

  public String getDescripcionEstadoAdministrativo() {
    return descripcionEstadoAdministrativo;
  }

  public void setDescripcionEstadoAdministrativo(String descripcionEstadoAdministrativo) {
    this.descripcionEstadoAdministrativo = descripcionEstadoAdministrativo;
  }

  public String getEstadoAdministrativo() {
    return estadoAdministrativo;
  }

  public void setEstadoAdministrativo(String estadoAdministrativo) {
    this.estadoAdministrativo = estadoAdministrativo;
  }

  public String getCostoMensual() {
    return costoMensual;
  }

  public void setCostoMensual(String costoMensual) {
    this.costoMensual = costoMensual;
  }

  public String getCostoA() {
    return costoA;
  }

  public void setCostoA(String costoA) {
    this.costoA = costoA;
  }

  public String getCostoB() {
    return costoB;
  }

  public void setCostoB(String costoB) {
    this.costoB = costoB;
  }

  public String getCostoC() {
    return costoC;
  }

  public void setCostoC(String costoC) {
    this.costoC = costoC;
  }

  public String getObservacionDocente() {
    return observacionDocente;
  }

  public void setObservacionDocente(String observacionDocente) {
    this.observacionDocente = observacionDocente;
  }

  public String getDescripcionEstadoDocente() {
    return descripcionEstadoDocente;
  }

  public void setDescripcionEstadoDocente(String descripcionEstadoDocente) {
    this.descripcionEstadoDocente = descripcionEstadoDocente;
  }

  public String getEstadoDocente() {
    return estadoDocente;
  }

  public void setEstadoDocente(String estadoDocente) {
    this.estadoDocente = estadoDocente;
  }

  public String getFechaRegistroAdministrativo() {
    return fechaRegistroAdministrativo;
  }

  public void setFechaRegistroAdministrativo(String fechaRegistroAdministrativo) {
    this.fechaRegistroAdministrativo = fechaRegistroAdministrativo;
  }

  public String getFechaRegistroDocente() {
    return fechaRegistroDocente;
  }

  public void setFechaRegistroDocente(String fechaRegistroDocente) {
    this.fechaRegistroDocente = fechaRegistroDocente;
  }

}
