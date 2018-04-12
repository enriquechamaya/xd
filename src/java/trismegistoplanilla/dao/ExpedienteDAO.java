package trismegistoplanilla.dao;

import org.json.JSONObject;
import trismegistoplanilla.beans.PersonaBean;

public interface ExpedienteDAO {

  public JSONObject listarTipoExpedientes();

  public JSONObject obtenerExpedientesPorPersona(PersonaBean persona);

}
