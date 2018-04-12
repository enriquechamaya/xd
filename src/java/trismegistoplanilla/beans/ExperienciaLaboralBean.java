package trismegistoplanilla.beans;

import java.io.Serializable;

public class ExperienciaLaboralBean implements Serializable {

    private static final long serialVersionUID = -3206160023607581842L;

    private int codigoExperienciaLaboral;
    private int codigoPersona;
    private String nombreEmpresa;
    private String nombreCargo;
    private String fechaInicio;
    private String fechaFin;
    private String telefono;
    private String estadoRegistro;

    public int getCodigoExperienciaLaboral() {
        return codigoExperienciaLaboral;
    }

    public void setCodigoExperienciaLaboral(int codigoExperienciaLaboral) {
        this.codigoExperienciaLaboral = codigoExperienciaLaboral;
    }

    public int getCodigoPersona() {
        return codigoPersona;
    }

    public void setCodigoPersona(int codigoPersona) {
        this.codigoPersona = codigoPersona;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getNombreCargo() {
        return nombreCargo;
    }

    public void setNombreCargo(String nombreCargo) {
        this.nombreCargo = nombreCargo;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEstadoRegistro() {
        return estadoRegistro;
    }

    public void setEstadoRegistro(String estadoRegistro) {
        this.estadoRegistro = estadoRegistro;
    }

}
