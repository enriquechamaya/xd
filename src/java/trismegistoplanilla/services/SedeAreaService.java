package trismegistoplanilla.services;

import org.json.JSONObject;
import trismegistoplanilla.beans.AreaBean;
import trismegistoplanilla.beans.SedeBean;
import trismegistoplanilla.dao.DAOFactory;
import trismegistoplanilla.dao.SedeAreaDAO;

public class SedeAreaService {

  DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
  SedeAreaDAO objSedeAreaDAO = factory.getSedeAreaDAO();

  public JSONObject obtenerSedeArea(SedeBean s, AreaBean a) {
    JSONObject JOObtenerSedeArea = null;
    try {
      JOObtenerSedeArea = objSedeAreaDAO.obtenerSedeArea(s, a);
    } catch (Exception e) {
      System.out.println("Error obtenerSedeArea service: " + e.getMessage());
    }
    return JOObtenerSedeArea;
  }

}
