package trismegistoplanilla.beans;

import java.io.Serializable;

public class CargaFamiliarBean implements Serializable {

  private static final long serialVersionUID = -3061163091237095855L;
  private int codigoCargaFamiliar;
  private int codigoPersona;
  private String apellidoPaterno;
  private String apellidoMaterno;
  private String nombre;
  private int codigoParentesco;
  private String nombreParentesco;
  private String fechaNacimiento;
  private int codigoTipoDocumento;
  private String nombreTipoDocumentoDescripcionLarga;
  private String nombreTipoDocumentoDescripcionCorta;
  private String numeroDocumento;
  private String telefono;
  private String edad;
  private int estado_registro;

  public int getCodigoCargaFamiliar() {
    return codigoCargaFamiliar;
  }

  public void setCodigoCargaFamiliar(int codigoCargaFamiliar) {
    this.codigoCargaFamiliar = codigoCargaFamiliar;
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

  public int getCodigoParentesco() {
    return codigoParentesco;
  }

  public void setCodigoParentesco(int codigoParentesco) {
    this.codigoParentesco = codigoParentesco;
  }

  public String getNombreParentesco() {
    return nombreParentesco;
  }

  public void setNombreParentesco(String nombreParentesco) {
    this.nombreParentesco = nombreParentesco;
  }

  public String getFechaNacimiento() {
    return fechaNacimiento;
  }

  public void setFechaNacimiento(String fechaNacimiento) {
    this.fechaNacimiento = fechaNacimiento;
  }

  public int getCodigoTipoDocumento() {
    return codigoTipoDocumento;
  }

  public void setCodigoTipoDocumento(int codigoTipoDocumento) {
    this.codigoTipoDocumento = codigoTipoDocumento;
  }

  public String getNumeroDocumento() {
    return numeroDocumento;
  }

  public String getNombreTipoDocumentoDescripcionLarga() {
    return nombreTipoDocumentoDescripcionLarga;
  }

  public void setNombreTipoDocumentoDescripcionLarga(String nombreTipoDocumentoDescripcionLarga) {
    this.nombreTipoDocumentoDescripcionLarga = nombreTipoDocumentoDescripcionLarga;
  }

  public String getNombreTipoDocumentoDescripcionCorta() {
    return nombreTipoDocumentoDescripcionCorta;
  }

  public void setNombreTipoDocumentoDescripcionCorta(String nombreTipoDocumentoDescripcionCorta) {
    this.nombreTipoDocumentoDescripcionCorta = nombreTipoDocumentoDescripcionCorta;
  }

  public void setNumeroDocumento(String numeroDocumento) {
    this.numeroDocumento = numeroDocumento;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public int getEstado_registro() {
    return estado_registro;
  }

  public void setEstado_registro(int estado_registro) {
    this.estado_registro = estado_registro;
  }

  public String getEdad() {
    return edad;
  }

  public void setEdad(String edad) {
    this.edad = edad;
  }

}
