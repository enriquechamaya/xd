package trismegistoplanilla.beans;

import java.io.Serializable;

public class TrabajadorResponsableBean implements Serializable {

  private static final long serialVersionUID = -3061163091237095855L;

  private int codigoTrabajadorResponsable;
  private int codigoPlanillaReal;
  private String apellidoPaterno;
  private String apellidoMaterno;
  private String nombre;
  private String dni;
  private int codigoSedeArea;
  private int codigoAreaCargo;
  private String nombreSede;
  private String nombreArea;
  private String nombreCargo;
  private int codigoUsuario;
  private String estadoRegistro;

  public int getCodigoTrabajadorResponsable() {
    return codigoTrabajadorResponsable;
  }

  public void setCodigoTrabajadorResponsable(int codigoTrabajadorResponsable) {
    this.codigoTrabajadorResponsable = codigoTrabajadorResponsable;
  }

  public int getCodigoPlanillaReal() {
    return codigoPlanillaReal;
  }

  public void setCodigoPlanillaReal(int codigoPlanillaReal) {
    this.codigoPlanillaReal = codigoPlanillaReal;
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

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getDni() {
    return dni;
  }

  public void setDni(String dni) {
    this.dni = dni;
  }

  public int getCodigoSedeArea() {
    return codigoSedeArea;
  }

  public void setCodigoSedeArea(int codigoSedeArea) {
    this.codigoSedeArea = codigoSedeArea;
  }

  public int getCodigoAreaCargo() {
    return codigoAreaCargo;
  }

  public void setCodigoAreaCargo(int codigoAreaCargo) {
    this.codigoAreaCargo = codigoAreaCargo;
  }

  public int getCodigoUsuario() {
    return codigoUsuario;
  }

  public void setCodigoUsuario(int codigoUsuario) {
    this.codigoUsuario = codigoUsuario;
  }

  public String getEstadoRegistro() {
    return estadoRegistro;
  }

  public void setEstadoRegistro(String estadoRegistro) {
    this.estadoRegistro = estadoRegistro;
  }

}
