package trismegistoplanilla.beans;

import java.io.Serializable;

public class AreaBean implements Serializable {

  private static final long serialVersionUID = -8762010449238102710L;

  private int codigoArea;
  private String nombre;

  public int getCodigoArea() {
    return codigoArea;
  }

  public void setCodigoArea(int codigoArea) {
    this.codigoArea = codigoArea;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

}
