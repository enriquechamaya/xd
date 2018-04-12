package trismegistoplanilla.services;

import org.json.JSONObject;
import trismegistoplanilla.beans.LoteAprobacionBean;
import trismegistoplanilla.dao.DAOFactory;
import trismegistoplanilla.dao.LoteAprobacionDAO;

public class LoteAprobacionService {

  DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
  LoteAprobacionDAO dao = daoFactory.getLoteAprobacionDAO();

  public JSONObject aprobarFichaAdministrativa(JSONObject json) {
    JSONObject jsonReturn = null;
    try {
      jsonReturn = dao.aprobarFichaAdministrativa(json);
    } catch (Exception e) {
      System.out.println("Error LoteAprobacionService > aprobarFichaAdministrativa => " + e.getMessage());
    }
    return jsonReturn;
  }

  public JSONObject aprobarFichaDocente(JSONObject json) {
    JSONObject jsonReturn = null;
    try {
      jsonReturn = dao.aprobarFichaDocente(json);
    } catch (Exception e) {
      System.out.println("Error LoteAprobacionService > aprobarFichaDocente => " + e.getMessage());
    }
    return jsonReturn;
  }

  public JSONObject rechazarFicha(JSONObject json) {
    JSONObject jsonReturn = null;
    try {
      jsonReturn = dao.rechazarFicha(json);
    } catch (Exception e) {
      System.out.println("Error LoteAprobacionService > rechazarFicha => " + e.getMessage());
    }
    return jsonReturn;
  }

  public JSONObject generarLoteAprobacion(JSONObject json) {
    JSONObject jsonReturn = null;
    try {
      jsonReturn = dao.generarLoteAprobacion(json);
    } catch (Exception e) {
      System.out.println("Error LoteAprobacionService > generarLoteAprobacion => " + e.getMessage());
    }
    return jsonReturn;
  }

  public JSONObject listarFichasPresidenciaDT(String draw, String start, String length, JSONObject json) {
    JSONObject jsonReturn = null;
    try {
      jsonReturn = dao.listarFichasPresidenciaDT(draw, start, length, json);
    } catch (Exception e) {
      System.out.println("Error LoteAprobacionService > listarFichasPresidenciaDT => " + e.getMessage());
    }
    return jsonReturn;
  }

  public JSONObject obtenerDetalleLote(JSONObject json) {
    JSONObject jsonReturn = null;
    try {
      jsonReturn = dao.obtenerDetalleLote(json);
    } catch (Exception e) {
      System.out.println("Error LoteAprobacionService > obtenerDetalleLote => " + e.getMessage());
    }
    return jsonReturn;
  }

  public JSONObject imprimirFichasAprobadas(LoteAprobacionBean la) {
    JSONObject JOimprimirFichasAprobadas = null;
    try {
      JOimprimirFichasAprobadas = dao.imprimirFichasAprobadas(la);
    } catch (Exception e) {
      System.out.println("Error LoteAprobacionService > imprimirFichasAprobadas => " + e.getMessage());
    }
    return JOimprimirFichasAprobadas;
  }
}
