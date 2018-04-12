package trismegistoplanilla.services;

import org.json.JSONObject;
import trismegistoplanilla.beans.PersonaBean;
import trismegistoplanilla.dao.DAOFactory;
import trismegistoplanilla.dao.FormacionAcademicaDAO;

public class FormacionAcademicaService {

  DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
  FormacionAcademicaDAO service = daoFactory.getFormacionAcademicaDAO();

  public JSONObject obtenerFormacionAcademicaPorPersona(PersonaBean persona) {
    JSONObject jsonReturn = null;
    try {
      jsonReturn = service.obtenerFormacionAcademicaPorPersona(persona);
    } catch (Exception e) {
    }
    return jsonReturn;
  }
}
