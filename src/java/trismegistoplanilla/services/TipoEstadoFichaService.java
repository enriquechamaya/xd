package trismegistoplanilla.services;

import org.json.JSONObject;
import trismegistoplanilla.dao.DAOFactory;
import trismegistoplanilla.dao.TipoEstadoFichaDAO;

public class TipoEstadoFichaService {

  DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
  TipoEstadoFichaDAO objTipoEstadoFichaDAO = factory.getTipoEstadoFichaDAO();

  public JSONObject listarTipoEstadoFicha() {
    JSONObject JOlistarTipoEstadoFicha = null;
    try {
      JOlistarTipoEstadoFicha = objTipoEstadoFichaDAO.listarTipoEstadoFicha();
    } catch (Exception e) {
      System.out.println("Error listarTipoEstadoFicha service: " + e.getMessage());
    }
    return JOlistarTipoEstadoFicha;
  }

}
