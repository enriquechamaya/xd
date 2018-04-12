package trismegistoplanilla.services;

import org.json.JSONObject;
import trismegistoplanilla.beans.AreaBean;
import trismegistoplanilla.beans.CargoBean;
import trismegistoplanilla.dao.AreaCargoDAO;
import trismegistoplanilla.dao.DAOFactory;

public class AreaCargoService {

  DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
  AreaCargoDAO objAreaCargoDAO = factory.getAreaCargoDAO();

  public JSONObject obtenerAreaCargo(AreaBean a, CargoBean c) {
    JSONObject JOObtenerAreaCargo = null;
    try {
      JOObtenerAreaCargo = objAreaCargoDAO.obtenerAreaCargo(a, c);
    } catch (Exception e) {
      System.out.println("Error service obtenerAreaCargo => " + e.getMessage());
    }
    return JOObtenerAreaCargo;
  }

}
