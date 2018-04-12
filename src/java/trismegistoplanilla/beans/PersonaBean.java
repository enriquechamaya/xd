package trismegistoplanilla.beans;

import java.io.Serializable;

public class PersonaBean implements Serializable {

  private static final long serialVersionUID = 6973785449547454777L;

  private int codigoPersona;
  private String apellidoPaterno;
  private String apellidoMaterno;
  private String nombre;
  private int codigoTipoDocumento;
  private String nombreTipoDocumento;
  private String numeroDocumento;
  private String sexo;
  private int codigoEstadoCivil;
  private String fechaNacimiento;
  private int codigoNacionalidad;
  private int codigoUbigeoNacimiento;
  private String direccionDocumento;
  private String telefonoFijo;
  private String telefonoMovil;
  private String correo;
  private int codigoUbigeoResidencia;
  private String direccionResidencia;
  private String latitudResidencia;
  private String longitudResidencia;
  private String foto;
  private String ruc;
  private int fondoPensionActivo;
  private int codigoFondoPension;
  private int isDefaultMail;
  private String estadoRegistro;

  private String enlaceAlfresco;

  public String getEnlaceAlfresco() {
    return enlaceAlfresco;
  }

  public void setEnlaceAlfresco(String enlaceAlfresco) {
    this.enlaceAlfresco = enlaceAlfresco;
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

  public int getCodigoTipoDocumento() {
    return codigoTipoDocumento;
  }

  public void setCodigoTipoDocumento(int codigoTipoDocumento) {
    this.codigoTipoDocumento = codigoTipoDocumento;
  }

  public String getNombreTipoDocumento() {
    return nombreTipoDocumento;
  }

  public void setNombreTipoDocumento(String nombreTipoDocumento) {
    this.nombreTipoDocumento = nombreTipoDocumento;
  }

  public String getNumeroDocumento() {
    return numeroDocumento;
  }

  public void setNumeroDocumento(String numeroDocumento) {
    this.numeroDocumento = numeroDocumento;
  }

  public String getSexo() {
    return sexo;
  }

  public void setSexo(String sexo) {
    this.sexo = sexo;
  }

  public int getCodigoEstadoCivil() {
    return codigoEstadoCivil;
  }

  public void setCodigoEstadoCivil(int codigoEstadoCivil) {
    this.codigoEstadoCivil = codigoEstadoCivil;
  }

  public String getFechaNacimiento() {
    return fechaNacimiento;
  }

  public void setFechaNacimiento(String fechaNacimiento) {
    this.fechaNacimiento = fechaNacimiento;
  }

  public int getCodigoNacionalidad() {
    return codigoNacionalidad;
  }

  public void setCodigoNacionalidad(int codigoNacionalidad) {
    this.codigoNacionalidad = codigoNacionalidad;
  }

  public int getCodigoUbigeoNacimiento() {
    return codigoUbigeoNacimiento;
  }

  public void setCodigoUbigeoNacimiento(int codigoUbigeoNacimiento) {
    this.codigoUbigeoNacimiento = codigoUbigeoNacimiento;
  }

  public String getDireccionDocumento() {
    return direccionDocumento;
  }

  public void setDireccionDocumento(String direccionDocumento) {
    this.direccionDocumento = direccionDocumento;
  }

  public String getTelefonoFijo() {
    return telefonoFijo;
  }

  public void setTelefonoFijo(String telefonoFijo) {
    this.telefonoFijo = telefonoFijo;
  }

  public String getTelefonoMovil() {
    return telefonoMovil;
  }

  public void setTelefonoMovil(String telefonoMovil) {
    this.telefonoMovil = telefonoMovil;
  }

  public String getCorreo() {
    return correo;
  }

  public void setCorreo(String correo) {
    this.correo = correo;
  }

  public int getCodigoUbigeoResidencia() {
    return codigoUbigeoResidencia;
  }

  public void setCodigoUbigeoResidencia(int codigoUbigeoResidencia) {
    this.codigoUbigeoResidencia = codigoUbigeoResidencia;
  }

  public String getDireccionResidencia() {
    return direccionResidencia;
  }

  public void setDireccionResidencia(String direccionResidencia) {
    this.direccionResidencia = direccionResidencia;
  }

  public String getLatitudResidencia() {
    return latitudResidencia;
  }

  public void setLatitudResidencia(String latitudResidencia) {
    this.latitudResidencia = latitudResidencia;
  }

  public String getLongitudResidencia() {
    return longitudResidencia;
  }

  public void setLongitudResidencia(String longitudResidencia) {
    this.longitudResidencia = longitudResidencia;
  }

  public String getFoto() {
    return foto;
  }

  public void setFoto(String foto) {
    this.foto = foto;
  }

  public String getRuc() {
    return ruc;
  }

  public void setRuc(String ruc) {
    this.ruc = ruc;
  }

  public int getFondoPensionActivo() {
    return fondoPensionActivo;
  }

  public void setFondoPensionActivo(int fondoPensionActivo) {
    this.fondoPensionActivo = fondoPensionActivo;
  }

  public int getCodigoFondoPension() {
    return codigoFondoPension;
  }

  public void setCodigoFondoPension(int codigoFondoPension) {
    this.codigoFondoPension = codigoFondoPension;
  }

  public int getIsDefaultMail() {
    return isDefaultMail;
  }

  public void setIsDefaultMail(int isDefaultMail) {
    this.isDefaultMail = isDefaultMail;
  }

  public String getEstadoRegistro() {
    return estadoRegistro;
  }

  public void setEstadoRegistro(String estadoRegistro) {
    this.estadoRegistro = estadoRegistro;
  }

}
