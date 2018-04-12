package trismegistoplanilla.dao;

import org.json.JSONObject;
import trismegistoplanilla.beans.AreaBean;
import trismegistoplanilla.beans.CargoBean;

public interface CargoDAO {

  public JSONObject listarCargo(AreaBean a);

  public JSONObject validarExistenciaCargo(AreaBean a, CargoBean c);

}
