package trismegistoplanilla.services;

import org.json.JSONObject;
import trismegistoplanilla.dao.ConfiguracionFichaDAO;
import trismegistoplanilla.dao.DAOFactory;

public class ConfiguracionFichaService {

  DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
  ConfiguracionFichaDAO objConfiguracionFichaDAO = factory.getConfiguracionFichaDAO();

  public JSONObject getDefaultMail() {
    JSONObject JOmail = null;
    try {
      JOmail = objConfiguracionFichaDAO.getDefaultMail();
    } catch (Exception e) {
      System.out.println("Error en getDefaultMail => " + e.getMessage());
    }
    return JOmail;
  }

}
