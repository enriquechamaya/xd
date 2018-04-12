package trismegistoplanilla.beans;

import java.io.Serializable;

public class AreaCargoBean implements Serializable {

  private static final long serialVersionUID = -2189457634567548108L;
  private int codigoAreaCargo;
  private int codigoArea;
  private int codigoCargo;

  public int getCodigoAreaCargo() {
    return codigoAreaCargo;
  }

  public void setCodigoAreaCargo(int codigoAreaCargo) {
    this.codigoAreaCargo = codigoAreaCargo;
  }

  public int getCodigoArea() {
    return codigoArea;
  }

  public void setCodigoArea(int codigoArea) {
    this.codigoArea = codigoArea;
  }

  public int getCodigoCargo() {
    return codigoCargo;
  }

  public void setCodigoCargo(int codigoCargo) {
    this.codigoCargo = codigoCargo;
  }

}
