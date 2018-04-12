package trismegistoplanilla.dao;

import org.json.JSONObject;
import trismegistoplanilla.beans.NivelEstudioBean;

public interface NivelEstudioDAO {

    public JSONObject validarExistenciaNivelEstudio(NivelEstudioBean nivelEstudio);

    public JSONObject listarNivelEstudio();

}
