package trismegistoplanilla.beans;

import java.io.Serializable;

public class SedeAreaBean implements Serializable {

  private static final long serialVersionUID = -2189457634567548108L;

  private int codigoSedeArea;
  private int codigoSede;
  private int codigoArea;

  public int getCodigoSedeArea() {
    return codigoSedeArea;
  }

  public void setCodigoSedeArea(int codigoSedeArea) {
    this.codigoSedeArea = codigoSedeArea;
  }

  public int getCodigoSede() {
    return codigoSede;
  }

  public void setCodigoSede(int codigoSede) {
    this.codigoSede = codigoSede;
  }

  public int getCodigoArea() {
    return codigoArea;
  }

  public void setCodigoArea(int codigoArea) {
    this.codigoArea = codigoArea;
  }

}
