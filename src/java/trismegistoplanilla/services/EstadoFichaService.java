package trismegistoplanilla.services;

import org.json.JSONObject;
import trismegistoplanilla.beans.EstadoFichaBean;
import trismegistoplanilla.dao.DAOFactory;
import trismegistoplanilla.dao.EstadoFichaDAO;

public class EstadoFichaService {

  DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
  EstadoFichaDAO objEstadoFichaDAO = factory.getEstadoFichaDAO();

  public JSONObject obtenerCodigoEstadoFicha(EstadoFichaBean ef) {
    JSONObject JOObtenerCodigoEstadoFicha = null;
    try {
      JOObtenerCodigoEstadoFicha = objEstadoFichaDAO.obtenerCodigoEstadoFicha(ef);
    } catch (Exception e) {
      System.out.println("Error en obtenerCodigoEstadoFicha => " + e.getMessage());
    }
    return JOObtenerCodigoEstadoFicha;
  }

  public JSONObject desactivarEstadoFicha(EstadoFichaBean ef) {
    JSONObject JODesactivarEstadoFicha = null;
    try {
      JODesactivarEstadoFicha = objEstadoFichaDAO.desactivarEstadoFicha(ef);
    } catch (Exception e) {
      System.out.println("Error en desactivarEstadoFicha => " + e.getMessage());
    }
    return JODesactivarEstadoFicha;
  }

  public JSONObject registrarEstadoFicha(EstadoFichaBean ef) {
    JSONObject JORegistrarEstadoFicha = null;
    try {
      JORegistrarEstadoFicha = objEstadoFichaDAO.registrarEstadoFicha(ef);
    } catch (Exception e) {
      System.out.println("Error en registrarEstadoFicha => " + e.getMessage());
    }
    return JORegistrarEstadoFicha;
  }

}
