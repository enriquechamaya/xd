package trismegistoplanilla.beans;

import java.io.Serializable;

public class TipoEstadoFichaBean implements Serializable {

  private static final long serialVersionUID = -1195809955845286573L;

  private int codigoTipoEstadoFicha;
  private String nombre;
  private String descripcion;

  public int getCodigoTipoEstadoFicha() {
    return codigoTipoEstadoFicha;
  }

  public void setCodigoTipoEstadoFicha(int codigoTipoEstadoFicha) {
    this.codigoTipoEstadoFicha = codigoTipoEstadoFicha;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

}
