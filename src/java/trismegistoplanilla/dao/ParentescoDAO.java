package trismegistoplanilla.dao;

import org.json.JSONObject;
import trismegistoplanilla.beans.ParentescoBean;

public interface ParentescoDAO {

    public JSONObject listarParentesco();

    public JSONObject validarExistenciaParentesco(ParentescoBean parentesco);
}
