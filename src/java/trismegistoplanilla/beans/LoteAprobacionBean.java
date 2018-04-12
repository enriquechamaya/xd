package trismegistoplanilla.beans;

import java.io.Serializable;

public class LoteAprobacionBean implements Serializable {

  private static final long serialVersionUID = 4594333294248399680L;

  // lote aprobacion
  private Integer codigoLoteAprobacion;
  private String numeroLote;

  //detalle lote aprobacion
  private Integer codigoDetalleLoteAprobacion;
  private Integer codigoFicha;
  private String cantidadFichas;

  //estado lote aprobacion
  private String fechaRegistro;

  public String getCantidadFichas() {
    return cantidadFichas;
  }

  public void setCantidadFichas(String cantidadFichas) {
    this.cantidadFichas = cantidadFichas;
  }

  
  
  public int getCodigoLoteAprobacion() {
    return codigoLoteAprobacion;
  }

  public void setCodigoLoteAprobacion(int codigoLoteAprobacion) {
    this.codigoLoteAprobacion = codigoLoteAprobacion;
  }

  public String getNumeroLote() {
    return numeroLote;
  }

  public void setNumeroLote(String numeroLote) {
    this.numeroLote = numeroLote;
  }

  public int getCodigoDetalleLoteAprobacion() {
    return codigoDetalleLoteAprobacion;
  }

  public void setCodigoDetalleLoteAprobacion(int codigoDetalleLoteAprobacion) {
    this.codigoDetalleLoteAprobacion = codigoDetalleLoteAprobacion;
  }

  public int getCodigoFicha() {
    return codigoFicha;
  }

  public void setCodigoFicha(int codigoFicha) {
    this.codigoFicha = codigoFicha;
  }

  public String getFechaRegistro() {
    return fechaRegistro;
  }

  public void setFechaRegistro(String fechaRegistro) {
    this.fechaRegistro = fechaRegistro;
  }

}
