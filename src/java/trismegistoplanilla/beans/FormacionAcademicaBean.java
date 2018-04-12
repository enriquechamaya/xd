package trismegistoplanilla.beans;

import java.io.Serializable;

public class FormacionAcademicaBean implements Serializable {

  private static final long serialVersionUID = -3061163091237095855L;

  private int codigoFormacionAcademica;
  private int codigoPersona;
  private String nombreCentroEstudios;
  private int codigoCarreraProfesional;
  private String nombreCarreraProfesional;
  private int codigoNivelEstado;
  private String nivelEstudio;
  private String estadoEstudio;
  private String fechaInicio;
  private String fechaFin;
  private String documentoAdjunto;
  private String observacion;
  private String sectorInstitucion;
  private String carreraProfesional;
  private String numeroColegiatura;
  private int estado_registro;

  public int getCodigoFormacionAcademica() {
    return codigoFormacionAcademica;
  }

  public String getNivelEstudio() {
    return nivelEstudio;
  }

  public void setNivelEstudio(String nivelEstudio) {
    this.nivelEstudio = nivelEstudio;
  }

  public String getEstadoEstudio() {
    return estadoEstudio;
  }

  public void setEstadoEstudio(String estadoEstudio) {
    this.estadoEstudio = estadoEstudio;
  }

  public String getNumeroColegiatura() {
    return numeroColegiatura;
  }

  public void setNumeroColegiatura(String numeroColegiatura) {
    this.numeroColegiatura = numeroColegiatura;
  }

  public String getNombreCarreraProfesional() {
    return nombreCarreraProfesional;
  }

  public void setNombreCarreraProfesional(String nombreCarreraProfesional) {
    this.nombreCarreraProfesional = nombreCarreraProfesional;
  }

  public void setCodigoFormacionAcademica(int codigoFormacionAcademica) {
    this.codigoFormacionAcademica = codigoFormacionAcademica;
  }

  public int getCodigoPersona() {
    return codigoPersona;
  }

  public void setCodigoPersona(int codigoPersona) {
    this.codigoPersona = codigoPersona;
  }

  public String getNombreCentroEstudios() {
    return nombreCentroEstudios;
  }

  public void setNombreCentroEstudios(String nombreCentroEstudios) {
    this.nombreCentroEstudios = nombreCentroEstudios;
  }

  public int getCodigoCarreraProfesional() {
    return codigoCarreraProfesional;
  }

  public void setCodigoCarreraProfesional(int codigoCarreraProfesional) {
    this.codigoCarreraProfesional = codigoCarreraProfesional;
  }

  public int getCodigoNivelEstado() {
    return codigoNivelEstado;
  }

  public void setCodigoNivelEstado(int codigoNivelEstado) {
    this.codigoNivelEstado = codigoNivelEstado;
  }

  public String getFechaInicio() {
    return fechaInicio;
  }

  public void setFechaInicio(String fechaInicio) {
    this.fechaInicio = fechaInicio;
  }

  public String getFechaFin() {
    return fechaFin;
  }

  public void setFechaFin(String fechaFin) {
    this.fechaFin = fechaFin;
  }

  public String getDocumentoAdjunto() {
    return documentoAdjunto;
  }

  public void setDocumentoAdjunto(String documentoAdjunto) {
    this.documentoAdjunto = documentoAdjunto;
  }

  public String getObservacion() {
    return observacion;
  }

  public void setObservacion(String observacion) {
    this.observacion = observacion;
  }

  public String getSectorInstitucion() {
    return sectorInstitucion;
  }

  public void setSectorInstitucion(String sectorInstitucion) {
    this.sectorInstitucion = sectorInstitucion;
  }

  public String getCarreraProfesional() {
    return carreraProfesional;
  }

  public void setCarreraProfesional(String carreraProfesional) {
    this.carreraProfesional = carreraProfesional;
  }

  public int getEstado_registro() {
    return estado_registro;
  }

  public void setEstado_registro(int estado_registro) {
    this.estado_registro = estado_registro;
  }

}
