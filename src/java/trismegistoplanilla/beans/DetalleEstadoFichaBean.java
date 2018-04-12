package trismegistoplanilla.beans;

import java.io.Serializable;

public class DetalleEstadoFichaBean implements Serializable {

  private static final long serialVersionUID = -43168006290587140L;

  private String nombreEstado;
  private String descripcionEstado;
  private String fechaRegistroEstado;
  private int codigoUsuario;
  private String nombreUsuario;

  public String getNombreEstado() {
    return nombreEstado;
  }

  public void setNombreEstado(String nombreEstado) {
    this.nombreEstado = nombreEstado;
  }

  public String getDescripcionEstado() {
    return descripcionEstado;
  }

  public void setDescripcionEstado(String descripcionEstado) {
    this.descripcionEstado = descripcionEstado;
  }

  public String getFechaRegistroEstado() {
    return fechaRegistroEstado;
  }

  public void setFechaRegistroEstado(String fechaRegistroEstado) {
    this.fechaRegistroEstado = fechaRegistroEstado;
  }

  public int getCodigoUsuario() {
    return codigoUsuario;
  }

  public void setCodigoUsuario(int codigoUsuario) {
    this.codigoUsuario = codigoUsuario;
  }

  public String getNombreUsuario() {
    return nombreUsuario;
  }

  public void setNombreUsuario(String nombreUsuario) {
    this.nombreUsuario = nombreUsuario;
  }

}
