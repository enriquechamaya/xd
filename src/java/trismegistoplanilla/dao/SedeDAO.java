package trismegistoplanilla.dao;

import org.json.JSONObject;
import trismegistoplanilla.beans.SedeBean;

public interface SedeDAO {

  public JSONObject listarSede();

  public JSONObject validarExistenciaSede(SedeBean s);

}
