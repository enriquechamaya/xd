package trismegistoplanilla.beans;

import java.io.Serializable;

public class LoteFichaBean implements Serializable {

  private static final long serialVersionUID = -2189457634567548108L;

  private int codigoFichaLote;
  private String numeroLote;
  private String fechaRegistro;
  private String mes;
  private String fechaActualizacion;
  private int numeroFichas;
  private String tipoLote;
  private String estadoLote;
  private int codigoEstadoFichaLote;
  private String estadoRegistro;
  private int item;
  private String estilo;

  public String getEstilo() {
    return estilo;
  }

  public void setEstilo(String estilo) {
    this.estilo = estilo;
  }

  public int getCodigoFichaLote() {
    return codigoFichaLote;
  }

  public void setCodigoFichaLote(int codigoFichaLote) {
    this.codigoFichaLote = codigoFichaLote;
  }

  public String getNumeroLote() {
    return numeroLote;
  }

  public void setNumeroLote(String numeroLote) {
    this.numeroLote = numeroLote;
  }

  public String getFechaRegistro() {
    return fechaRegistro;
  }

  public void setFechaRegistro(String fechaRegistro) {
    this.fechaRegistro = fechaRegistro;
  }

  public String getFechaActualizacion() {
    return fechaActualizacion;
  }

  public void setFechaActualizacion(String fechaActualizacion) {
    this.fechaActualizacion = fechaActualizacion;
  }

  public int getNumeroFichas() {
    return numeroFichas;
  }

  public void setNumeroFichas(int numeroFichas) {
    this.numeroFichas = numeroFichas;
  }

  public String getTipoLote() {
    return tipoLote;
  }

  public void setTipoLote(String tipoLote) {
    this.tipoLote = tipoLote;
  }

  public String getEstadoLote() {
    return estadoLote;
  }

  public void setEstadoLote(String estadoLote) {
    this.estadoLote = estadoLote;
  }

  public int getCodigoEstadoFichaLote() {
    return codigoEstadoFichaLote;
  }

  public void setCodigoEstadoFichaLote(int codigoEstadoFichaLote) {
    this.codigoEstadoFichaLote = codigoEstadoFichaLote;
  }

  public String getEstadoRegistro() {
    return estadoRegistro;
  }

  public void setEstadoRegistro(String estadoRegistro) {
    this.estadoRegistro = estadoRegistro;
  }

  public int getItem() {
    return item;
  }

  public void setItem(int item) {
    this.item = item;
  }

  public String getMes() {
    return mes;
  }

  public void setMes(String mes) {
    this.mes = mes;
  }

}
