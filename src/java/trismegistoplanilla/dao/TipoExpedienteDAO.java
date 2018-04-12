package trismegistoplanilla.dao;

import org.json.JSONArray;
import org.json.JSONObject;
import trismegistoplanilla.beans.TipoExpedienteBean;

public interface TipoExpedienteDAO {

  public JSONObject listarTipoExpediente();

  public JSONObject validarExistenciaTipoExpediente(TipoExpedienteBean te);

  public JSONObject registrarTipoExpediente(TipoExpedienteBean te);

  public JSONObject validarTipoExpedienteSeleccionadoByID(JSONArray ja);

}
