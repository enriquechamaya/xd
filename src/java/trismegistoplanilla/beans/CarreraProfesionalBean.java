package trismegistoplanilla.beans;

import java.io.Serializable;

public class CarreraProfesionalBean implements Serializable {

    private static final long serialVersionUID = -3061163091237095855L;
    private int id;
    private int codigoCarreraProfesional;
    private String nombre;
    private int estado_registro;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCodigoCarreraProfesional() {
        return codigoCarreraProfesional;
    }

    public void setCodigoCarreraProfesional(int codigoCarreraProfesional) {
        this.codigoCarreraProfesional = codigoCarreraProfesional;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEstado_registro() {
        return estado_registro;
    }

    public void setEstado_registro(int estado_registro) {
        this.estado_registro = estado_registro;
    }

}
