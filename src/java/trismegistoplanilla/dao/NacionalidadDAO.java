package trismegistoplanilla.dao;

import org.json.JSONObject;
import trismegistoplanilla.beans.NacionalidadBean;

public interface NacionalidadDAO {

    public JSONObject listarNacionalidad();

    public JSONObject validarExistenciaNacionalidad(NacionalidadBean nacionalidad);

}
