package trismegistoplanilla.beans;

import java.io.Serializable;

public class ListadoFichaBean implements Serializable {

  private static final long serialVersionUID = -6298816690611277939L;

  private int codigoFicha;
  private int codigoPersona;
  private int item;
  private String descripcionLargaTipoDocumento;
  private String descripcionCortaTipoDocumento;
  private String numeroDocumento;
  private String nacionalidad; // listado presidencia
  private String fechaIngreso;// listado presidencia
  private String apellidoPaterno;
  private String apellidoMaterno;
  private String nombre;
  private String personal;
  private String correo;
  private String area;// listado presidencia
  private String cargo;
  private String fechaRegistro;
  private String estilo;
  private String estadoFicha;
  private int codigoTipoDocumento;

  public int getCodigoFicha() {
    return codigoFicha;
  }

  public String getNacionalidad() {
    return nacionalidad;
  }

  public void setNacionalidad(String nacionalidad) {
    this.nacionalidad = nacionalidad;
  }

  public String getFechaIngreso() {
    return fechaIngreso;
  }

  public void setFechaIngreso(String fechaIngreso) {
    this.fechaIngreso = fechaIngreso;
  }

  public String getArea() {
    return area;
  }

  public void setArea(String area) {
    this.area = area;
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

  public int getItem() {
    return item;
  }

  public void setItem(int item) {
    this.item = item;
  }

  public String getDescripcionLargaTipoDocumento() {
    return descripcionLargaTipoDocumento;
  }

  public void setDescripcionLargaTipoDocumento(String descripcionLargaTipoDocumento) {
    this.descripcionLargaTipoDocumento = descripcionLargaTipoDocumento;
  }

  public String getDescripcionCortaTipoDocumento() {
    return descripcionCortaTipoDocumento;
  }

  public void setDescripcionCortaTipoDocumento(String descripcionCortaTipoDocumento) {
    this.descripcionCortaTipoDocumento = descripcionCortaTipoDocumento;
  }

  public String getNumeroDocumento() {
    return numeroDocumento;
  }

  public void setNumeroDocumento(String numeroDocumento) {
    this.numeroDocumento = numeroDocumento;
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

  public String getPersonal() {
    return personal;
  }

  public void setPersonal(String personal) {
    this.personal = personal;
  }

  public String getCorreo() {
    return correo;
  }

  public void setCorreo(String correo) {
    this.correo = correo;
  }

  public String getCargo() {
    return cargo;
  }

  public void setCargo(String cargo) {
    this.cargo = cargo;
  }

  public String getFechaRegistro() {
    return fechaRegistro;
  }

  public void setFechaRegistro(String fechaRegistro) {
    this.fechaRegistro = fechaRegistro;
  }

  public String getEstadoFicha() {
    return estadoFicha;
  }

  public void setEstadoFicha(String estadoFicha) {
    this.estadoFicha = estadoFicha;
  }

  public String getEstilo() {
    return estilo;
  }

  public void setEstilo(String estilo) {
    this.estilo = estilo;
  }

  public int getCodigoTipoDocumento() {
    return codigoTipoDocumento;
  }

  public void setCodigoTipoDocumento(int codigoTipoDocumento) {
    this.codigoTipoDocumento = codigoTipoDocumento;
  }

}
