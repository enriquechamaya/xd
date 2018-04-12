package trismegistoplanilla.services;

import org.json.JSONObject;
import trismegistoplanilla.beans.PersonaBean;
import trismegistoplanilla.dao.DAOFactory;
import trismegistoplanilla.dao.ExpedienteDAO;

public class ExpedienteService {

  DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
  ExpedienteDAO dao = daoFactory.getExpedienteDAO();

  public JSONObject listarTipoExpedientes() {
    JSONObject jsonReturn = null;
    try {
      jsonReturn = dao.listarTipoExpedientes();
    } catch (Exception e) {
      System.err.println("Error service listarTipoExpedientes >>>> " + e.getMessage());
    }
    return jsonReturn;
  }

  public JSONObject obtenerExpedientesPorPersona(PersonaBean persona) {
    JSONObject jsonReturn = null;
    try {
      jsonReturn = dao.obtenerExpedientesPorPersona(persona);
    } catch (Exception e) {
      System.err.println("Error service obtenerExpedientesPorPersona >>>> " + e.getMessage());
    }
    return jsonReturn;
  }

}
