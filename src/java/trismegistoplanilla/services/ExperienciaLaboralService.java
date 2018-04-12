package trismegistoplanilla.services;

import org.json.JSONObject;
import trismegistoplanilla.beans.PersonaBean;
import trismegistoplanilla.dao.DAOFactory;
import trismegistoplanilla.dao.ExperienciaLaboralDAO;

public class ExperienciaLaboralService {

  DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
  ExperienciaLaboralDAO service = daoFactory.getExperienciaLaboralDAO();

  public JSONObject obtenerExperienciaLaboralPorPersona(PersonaBean persona) {
    JSONObject jsonReturn = null;
    try {
      jsonReturn = service.obtenerExperienciaLaboralPorPersona(persona);
    } catch (Exception e) {
      System.out.println("Error ExperienciaLaboralService > obtenerExperienciaLaboralPorPersona => " + e.getMessage());
    }
    return jsonReturn;
  }
}
