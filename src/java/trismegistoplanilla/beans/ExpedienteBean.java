package trismegistoplanilla.beans;

import java.io.Serializable;

public class ExpedienteBean implements Serializable{
  private static final long serialVersionUID = -5668092524305743080L;
  private int codigoExpediente;
  private int codigoPersona;
  private String enlaceAlfresco;
  
  private int codigoTipoExpediente;
  private String nombreExpediente;

  public int getCodigoExpediente() {
    return codigoExpediente;
  }

  public void setCodigoExpediente(int codigoExpediente) {
    this.codigoExpediente = codigoExpediente;
  }

  public int getCodigoPersona() {
    return codigoPersona;
  }

  public void setCodigoPersona(int codigoPersona) {
    this.codigoPersona = codigoPersona;
  }

  public String getEnlaceAlfresco() {
    return enlaceAlfresco;
  }

  public void setEnlaceAlfresco(String enlaceAlfresco) {
    this.enlaceAlfresco = enlaceAlfresco;
  }

  public int getCodigoTipoExpediente() {
    return codigoTipoExpediente;
  }

  public void setCodigoTipoExpediente(int codigoTipoExpediente) {
    this.codigoTipoExpediente = codigoTipoExpediente;
  }

  public String getNombreExpediente() {
    return nombreExpediente;
  }

  public void setNombreExpediente(String nombreExpediente) {
    this.nombreExpediente = nombreExpediente;
  }
  
  
  
  
}
