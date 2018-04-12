package trismegistoplanilla.beans;

import java.io.Serializable;

public class ConfiguracionFichaBean implements Serializable {

  private static final long serialVersionUID = 1395041488319712181L;

  private int id;
  private String correo;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getCorreo() {
    return correo;
  }

  public void setCorreo(String correo) {
    this.correo = correo;
  }

}
