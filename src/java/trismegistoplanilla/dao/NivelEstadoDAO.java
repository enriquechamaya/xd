package trismegistoplanilla.dao;

import org.json.JSONObject;
import trismegistoplanilla.beans.NivelEstadoBean;

public interface NivelEstadoDAO {

    public JSONObject obtenerNivelEstado(NivelEstadoBean nivelEstado);

}
