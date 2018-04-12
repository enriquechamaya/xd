package trismegistoplanilla.dao;

import org.json.JSONObject;
import trismegistoplanilla.beans.PersonaBean;

public interface ExperienciaLaboralDAO {

  public JSONObject obtenerExperienciaLaboralPorPersona(PersonaBean persona);
}
