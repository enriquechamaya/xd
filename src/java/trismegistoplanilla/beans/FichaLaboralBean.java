package trismegistoplanilla.beans;

import java.io.Serializable;

public class FichaLaboralBean implements Serializable {

  private static final long serialVersionUID = -8454103990275232023L;

  private int codigoFicha;
  private String fechaIngreso;
  private String fechaFin;
  private int codigoSedeArea;
  private int codigoAreaCargo;
  private String tipoFicha;
  private int estadoRegistro;

  public int getCodigoFicha() {
    return codigoFicha;
  }

  public void setCodigoFicha(int codigoFicha) {
    this.codigoFicha = codigoFicha;
  }

  public String getFechaIngreso() {
    return fechaIngreso;
  }

  public void setFechaIngreso(String fechaIngreso) {
    this.fechaIngreso = fechaIngreso;
  }

  public String getFechaFin() {
    return fechaFin;
  }

  public void setFechaFin(String fechaFin) {
    this.fechaFin = fechaFin;
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

  public String getTipoFicha() {
    return tipoFicha;
  }

  public void setTipoFicha(String tipoFicha) {
    this.tipoFicha = tipoFicha;
  }

  public int getEstadoRegistro() {
    return estadoRegistro;
  }

  public void setEstadoRegistro(int estadoRegistro) {
    this.estadoRegistro = estadoRegistro;
  }

}
