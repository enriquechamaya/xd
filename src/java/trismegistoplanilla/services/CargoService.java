package trismegistoplanilla.services;

import org.json.JSONObject;
import trismegistoplanilla.beans.AreaBean;
import trismegistoplanilla.beans.CargoBean;
import trismegistoplanilla.dao.CargoDAO;
import trismegistoplanilla.dao.DAOFactory;

public class CargoService {

  DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
  CargoDAO objCargoDAO = factory.getCargoDAO();

  public JSONObject listarCargo(AreaBean a) {
    JSONObject JOListarCargo = null;
    try {
      JOListarCargo = objCargoDAO.listarCargo(a);
    } catch (Exception e) {
      System.out.println("Error en listarCargo => " + e.getMessage());
    }
    return JOListarCargo;
  }

  public JSONObject validarExistenciaCargo(AreaBean a, CargoBean c) {
    JSONObject JOValidarExistenciaCargo = null;
    try {
      JOValidarExistenciaCargo = objCargoDAO.validarExistenciaCargo(a, c);
    } catch (Exception e) {
      System.out.println("Error en validarExistenciaCargo => " + e.getMessage());
    }
    return JOValidarExistenciaCargo;
  }

}
