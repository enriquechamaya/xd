package trismegistoplanilla.beans;

import java.io.Serializable;

public class TipoExpedienteBean implements Serializable {

  private static final long serialVersionUID = 7527411502256916503L;

  private int codigoTipoExpediente;
  private String nombre;
  private boolean estado;

  public int getCodigoTipoExpediente() {
    return codigoTipoExpediente;
  }

  public void setCodigoTipoExpediente(int codigoTipoExpediente) {
    this.codigoTipoExpediente = codigoTipoExpediente;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public boolean isEstado() {
    return estado;
  }

  public void setEstado(boolean estado) {
    this.estado = estado;
  }

}
