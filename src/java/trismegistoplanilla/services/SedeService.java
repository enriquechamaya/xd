package trismegistoplanilla.services;

import org.json.JSONObject;
import trismegistoplanilla.beans.SedeBean;
import trismegistoplanilla.dao.DAOFactory;
import trismegistoplanilla.dao.SedeDAO;

public class SedeService {

  DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
  SedeDAO objSedeDAO = factory.getSedeDAO();

  public JSONObject listarSede() {
    JSONObject JObjectSede = null;
    try {
      JObjectSede = objSedeDAO.listarSede();
    } catch (Exception e) {
      System.out.println("Error listarSede service: " + e.getMessage());
    }
    return JObjectSede;
  }

  public JSONObject validarExistenciaSede(SedeBean s) {
    JSONObject JObjectValidarExistenciaSede = null;
    try {
      JObjectValidarExistenciaSede = objSedeDAO.validarExistenciaSede(s);
    } catch (Exception e) {
      System.out.println("Error validarExistenciaSede service: " + e.getMessage());
    }
    return JObjectValidarExistenciaSede;
  }

}
