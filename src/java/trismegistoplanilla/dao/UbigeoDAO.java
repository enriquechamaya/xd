package trismegistoplanilla.dao;

import org.json.JSONObject;
import trismegistoplanilla.beans.UbigeoBean;

public interface UbigeoDAO {

    public JSONObject listarDepartamento();

    public JSONObject listarProvincia(UbigeoBean ubigeo);

    public JSONObject listarDistrito(UbigeoBean ubigeo);

    public JSONObject validarExistenciaDepartamento(UbigeoBean ubigeo);

    public JSONObject validarExistenciaProvincia(UbigeoBean ubigeo);

    public JSONObject validarExistenciaDistrito(UbigeoBean ubigeo);

    public JSONObject obtenerCodigoUbigeo(UbigeoBean ubigeo);
}
