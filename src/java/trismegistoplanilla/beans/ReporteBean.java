package trismegistoplanilla.beans;

public class ReporteBean implements java.io.Serializable {

  private static final long serialVersionUID = -3061163091237095855L;

  // datos persona
  private Integer codigoFicha;
  private Integer codigoPersona;
  private String apellidoPaterno;
  private String apellidoMaterno;
  private String nombre;
  private String tipoDocumentoDescripcionLarga;
  private String tipoDocumentoDescripcionCorta;
  private String numeroDocumento;
  private String sexo;
  private String estadoCivil;
  private String fechaNacimiento;
  private String edad;
  private String nroHijos;
  private String pais;
  private String gentilicio;
  private String iso;
  private String nombreDepartamentoNacimiento;
  private String nombreProvinciaNacimiento;
  private String nombreDistritoNacimiento;
  private String direccionDocumento;
  private String telefonoFijo;
  private String telefonoMovil;
  private String correo;
  private String ruc;
  private String foto;
  private String nombreDepartamentoResidencia;
  private String nombreProvinciaResidencia;
  private String nombreDistritoResidencia;
  private String direccionResidencia;
  private String fondoPensionActivo;
  private String fondoPensionDescripcionLarga;
  private String fondoPensionDescripcionCorta;
  private String latitud;
  private String longitud;
  private String enlaceAlfresco;
  private String estadoFicha;

  public String getEstadoFicha() {
    return estadoFicha;
  }

  public void setEstadoFicha(String estadoFicha) {
    this.estadoFicha = estadoFicha;
  }

  public Integer getCodigoFicha() {
    return codigoFicha;
  }

  public String getEnlaceAlfresco() {
    return enlaceAlfresco;
  }

  public void setEnlaceAlfresco(String enlaceAlfresco) {
    this.enlaceAlfresco = enlaceAlfresco;
  }

  public void setCodigoFicha(Integer codigoFicha) {
    this.codigoFicha = codigoFicha;
  }

  public Integer getCodigoPersona() {
    return codigoPersona;
  }

  public void setCodigoPersona(Integer codigoPersona) {
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

  public String getTipoDocumentoDescripcionLarga() {
    return tipoDocumentoDescripcionLarga;
  }

  public void setTipoDocumentoDescripcionLarga(String tipoDocumentoDescripcionLarga) {
    this.tipoDocumentoDescripcionLarga = tipoDocumentoDescripcionLarga;
  }

  public String getTipoDocumentoDescripcionCorta() {
    return tipoDocumentoDescripcionCorta;
  }

  public void setTipoDocumentoDescripcionCorta(String tipoDocumentoDescripcionCorta) {
    this.tipoDocumentoDescripcionCorta = tipoDocumentoDescripcionCorta;
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

  public String getEstadoCivil() {
    return estadoCivil;
  }

  public void setEstadoCivil(String estadoCivil) {
    this.estadoCivil = estadoCivil;
  }

  public String getFechaNacimiento() {
    return fechaNacimiento;
  }

  public void setFechaNacimiento(String fechaNacimiento) {
    this.fechaNacimiento = fechaNacimiento;
  }

  public String getPais() {
    return pais;
  }

  public void setPais(String pais) {
    this.pais = pais;
  }

  public String getGentilicio() {
    return gentilicio;
  }

  public void setGentilicio(String gentilicio) {
    this.gentilicio = gentilicio;
  }

  public String getIso() {
    return iso;
  }

  public void setIso(String iso) {
    this.iso = iso;
  }

  public String getNombreDepartamentoNacimiento() {
    return nombreDepartamentoNacimiento;
  }

  public void setNombreDepartamentoNacimiento(String nombreDepartamentoNacimiento) {
    this.nombreDepartamentoNacimiento = nombreDepartamentoNacimiento;
  }

  public String getNombreProvinciaNacimiento() {
    return nombreProvinciaNacimiento;
  }

  public void setNombreProvinciaNacimiento(String nombreProvinciaNacimiento) {
    this.nombreProvinciaNacimiento = nombreProvinciaNacimiento;
  }

  public String getNombreDistritoNacimiento() {
    return nombreDistritoNacimiento;
  }

  public void setNombreDistritoNacimiento(String nombreDistritoNacimiento) {
    this.nombreDistritoNacimiento = nombreDistritoNacimiento;
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

  public String getRuc() {
    return ruc;
  }

  public void setRuc(String ruc) {
    this.ruc = ruc;
  }

  public String getFoto() {
    return foto;
  }

  public void setFoto(String foto) {
    this.foto = foto;
  }

  public String getNombreDepartamentoResidencia() {
    return nombreDepartamentoResidencia;
  }

  public void setNombreDepartamentoResidencia(String nombreDepartamentoResidencia) {
    this.nombreDepartamentoResidencia = nombreDepartamentoResidencia;
  }

  public String getNombreProvinciaResidencia() {
    return nombreProvinciaResidencia;
  }

  public void setNombreProvinciaResidencia(String nombreProvinciaResidencia) {
    this.nombreProvinciaResidencia = nombreProvinciaResidencia;
  }

  public String getNombreDistritoResidencia() {
    return nombreDistritoResidencia;
  }

  public void setNombreDistritoResidencia(String nombreDistritoResidencia) {
    this.nombreDistritoResidencia = nombreDistritoResidencia;
  }

  public String getDireccionResidencia() {
    return direccionResidencia;
  }

  public void setDireccionResidencia(String direccionResidencia) {
    this.direccionResidencia = direccionResidencia;
  }

  public String getFondoPensionActivo() {
    return fondoPensionActivo;
  }

  public void setFondoPensionActivo(String fondoPensionActivo) {
    this.fondoPensionActivo = fondoPensionActivo;
  }

  public String getFondoPensionDescripcionLarga() {
    return fondoPensionDescripcionLarga;
  }

  public void setFondoPensionDescripcionLarga(String fondoPensionDescripcionLarga) {
    this.fondoPensionDescripcionLarga = fondoPensionDescripcionLarga;
  }

  public String getFondoPensionDescripcionCorta() {
    return fondoPensionDescripcionCorta;
  }

  public void setFondoPensionDescripcionCorta(String fondoPensionDescripcionCorta) {
    this.fondoPensionDescripcionCorta = fondoPensionDescripcionCorta;
  }

  public String getLatitud() {
    return latitud;
  }

  public void setLatitud(String latitud) {
    this.latitud = latitud;
  }

  public String getLongitud() {
    return longitud;
  }

  public void setLongitud(String longitud) {
    this.longitud = longitud;
  }

  public String getEdad() {
    return edad;
  }

  public void setEdad(String edad) {
    this.edad = edad;
  }

  public String getNroHijos() {
    return nroHijos;
  }

  public void setNroHijos(String nroHijos) {
    this.nroHijos = nroHijos;
  }

}
