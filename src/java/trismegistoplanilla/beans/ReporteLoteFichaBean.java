package trismegistoplanilla.beans;

import java.io.Serializable;

public class ReporteLoteFichaBean implements Serializable {

  private static final long serialVersionUID = -2189457634567548108L;
  private int codigoFicha;
  private int codigoPersona;
  private String apellidoPaterno;
  private String apellidoMaterno;
  private String nombre;
  private String tipoFicha;
  private String numeroDocumento;
  private String nombreSede;
  private String nombreArea;
  private String nombreCargo;
  private String sueldoEscalafonAdministrativo;
  private String sueldoMensualAdministrativo;
  private String observacionAdministrativo;
  private String costoMensualDocente;
  private String costoADocente;
  private String costoBDocente;
  private String costoCDocente;
  private String observacionDocente;

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

  public String getTipoFicha() {
    return tipoFicha;
  }

  public void setTipoFicha(String tipoFicha) {
    this.tipoFicha = tipoFicha;
  }

  public String getNumeroDocumento() {
    return numeroDocumento;
  }

  public void setNumeroDocumento(String numeroDocumento) {
    this.numeroDocumento = numeroDocumento;
  }

  public String getNombreSede() {
    return nombreSede;
  }

  public void setNombreSede(String nombreSede) {
    this.nombreSede = nombreSede;
  }

  public String getNombreArea() {
    return nombreArea;
  }

  public void setNombreArea(String nombreArea) {
    this.nombreArea = nombreArea;
  }

  public String getNombreCargo() {
    return nombreCargo;
  }

  public void setNombreCargo(String nombreCargo) {
    this.nombreCargo = nombreCargo;
  }

  public String getSueldoEscalafonAdministrativo() {
    return sueldoEscalafonAdministrativo;
  }

  public void setSueldoEscalafonAdministrativo(String sueldoEscalafonAdministrativo) {
    this.sueldoEscalafonAdministrativo = sueldoEscalafonAdministrativo;
  }

  public String getSueldoMensualAdministrativo() {
    return sueldoMensualAdministrativo;
  }

  public void setSueldoMensualAdministrativo(String sueldoMensualAdministrativo) {
    this.sueldoMensualAdministrativo = sueldoMensualAdministrativo;
  }

  public String getObservacionAdministrativo() {
    return observacionAdministrativo;
  }

  public void setObservacionAdministrativo(String observacionAdministrativo) {
    this.observacionAdministrativo = observacionAdministrativo;
  }

  public String getCostoMensualDocente() {
    return costoMensualDocente;
  }

  public void setCostoMensualDocente(String costoMensualDocente) {
    this.costoMensualDocente = costoMensualDocente;
  }

  public String getCostoADocente() {
    return costoADocente;
  }

  public void setCostoADocente(String costoADocente) {
    this.costoADocente = costoADocente;
  }

  public String getCostoBDocente() {
    return costoBDocente;
  }

  public void setCostoBDocente(String costoBDocente) {
    this.costoBDocente = costoBDocente;
  }

  public String getCostoCDocente() {
    return costoCDocente;
  }

  public void setCostoCDocente(String costoCDocente) {
    this.costoCDocente = costoCDocente;
  }

  public String getObservacionDocente() {
    return observacionDocente;
  }

  public void setObservacionDocente(String observacionDocente) {
    this.observacionDocente = observacionDocente;
  }

}
