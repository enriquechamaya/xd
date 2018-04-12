package trismegistoplanilla.services;

import org.json.JSONObject;
import trismegistoplanilla.beans.AreaBean;
import trismegistoplanilla.beans.SedeBean;
import trismegistoplanilla.dao.AreaDAO;
import trismegistoplanilla.dao.DAOFactory;

public class AreaService {

  DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
  AreaDAO objAreaDAO = factory.getAreaDAO();

  public JSONObject listarArea(SedeBean s) {
    JSONObject JObjectArea = null;
    try {
      JObjectArea = objAreaDAO.listarArea(s);
    } catch (Exception e) {
      System.out.println("Error listarSede service: " + e.getMessage());
    }
    return JObjectArea;
  }

  public JSONObject validarExistenciaArea(SedeBean s, AreaBean a) {
    JSONObject JObjectValidarExistenciaArea = null;
    try {
      JObjectValidarExistenciaArea = objAreaDAO.validarExistenciaArea(s, a);
    } catch (Exception e) {
      System.out.println("Error validarExistenciaArea service: " + e.getMessage());
    }
    return JObjectValidarExistenciaArea;
  }

}
