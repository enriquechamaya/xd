package trismegistoplanilla.dao;

import org.json.JSONObject;
import trismegistoplanilla.beans.AreaBean;
import trismegistoplanilla.beans.SedeBean;

public interface SedeAreaDAO {

  public JSONObject obtenerSedeArea(SedeBean s, AreaBean a);

}
