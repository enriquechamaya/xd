package trismegistoplanilla.dao;

import org.json.JSONObject;
import trismegistoplanilla.beans.EstadoCivilBean;

public interface EstadoCivilDAO {

    public JSONObject listarEstadoCivil();

    public JSONObject validarExistenciaEstadoCivil(EstadoCivilBean estadoCivil);
}
