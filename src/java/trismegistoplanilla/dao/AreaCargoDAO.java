package trismegistoplanilla.dao;

import org.json.JSONObject;
import trismegistoplanilla.beans.AreaBean;
import trismegistoplanilla.beans.CargoBean;

public interface AreaCargoDAO {

  public JSONObject obtenerAreaCargo(AreaBean a, CargoBean c);

}
