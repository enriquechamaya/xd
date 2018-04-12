package trismegistoplanilla.dao;

import org.json.JSONObject;
import trismegistoplanilla.beans.AreaBean;
import trismegistoplanilla.beans.SedeBean;

public interface AreaDAO {

  public JSONObject listarArea(SedeBean s);

  public JSONObject validarExistenciaArea(SedeBean s, AreaBean a);

}
