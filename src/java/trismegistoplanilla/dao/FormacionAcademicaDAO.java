package trismegistoplanilla.dao;

import org.json.JSONObject;
import trismegistoplanilla.beans.PersonaBean;

public interface FormacionAcademicaDAO {

  public JSONObject obtenerFormacionAcademicaPorPersona(PersonaBean persona);
}
