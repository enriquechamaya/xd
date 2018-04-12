package trismegistoplanilla.beans;

import java.io.Serializable;

public class SedeBean implements Serializable {

  private static final long serialVersionUID = 9000470645646155674L;

  private int codigoSede;
  private String nombre;
  private String direccion;
  private int codigoRazonSocial;
  private String nombreRazonSocial;

  public int getCodigoSede() {
    return codigoSede;
  }

  public void setCodigoSede(int codigoSede) {
    this.codigoSede = codigoSede;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public int getCodigoRazonSocial() {
    return codigoRazonSocial;
  }

  public void setCodigoRazonSocial(int codigoRazonSocial) {
    this.codigoRazonSocial = codigoRazonSocial;
  }

  public String getNombreRazonSocial() {
    return nombreRazonSocial;
  }

  public void setNombreRazonSocial(String nombreRazonSocial) {
    this.nombreRazonSocial = nombreRazonSocial;
  }

}
