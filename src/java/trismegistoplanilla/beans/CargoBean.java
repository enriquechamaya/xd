package trismegistoplanilla.beans;

import java.io.Serializable;

public class CargoBean implements Serializable {

  private static final long serialVersionUID = -5668092524305743080L;

  private int codigoCargo;
  private String nombre;

  public int getCodigoCargo() {
    return codigoCargo;
  }

  public void setCodigoCargo(int codigoCargo) {
    this.codigoCargo = codigoCargo;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

}
