package trismegistoplanilla.beans;

import java.io.Serializable;

public class TokenFichaBean implements Serializable {

    private static final long serialVersionUID = 8272313343449528594L;

    private int codigoTokenFicha;
    private int codigoFicha;
    private String codigoVerificacion;
    private String fechaCreacion;
    private String fechaExpiracion;
    private String token;
    private String estadoRegistro;

    public int getCodigoTokenFicha() {
        return codigoTokenFicha;
    }

    public void setCodigoTokenFicha(int codigoTokenFicha) {
        this.codigoTokenFicha = codigoTokenFicha;
    }

    public int getCodigoFicha() {
        return codigoFicha;
    }

    public void setCodigoFicha(int codigoFicha) {
        this.codigoFicha = codigoFicha;
    }

    public String getCodigoVerificacion() {
        return codigoVerificacion;
    }

    public void setCodigoVerificacion(String codigoVerificacion) {
        this.codigoVerificacion = codigoVerificacion;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(String fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEstadoRegistro() {
        return estadoRegistro;
    }

    public void setEstadoRegistro(String estadoRegistro) {
        this.estadoRegistro = estadoRegistro;
    }

}
