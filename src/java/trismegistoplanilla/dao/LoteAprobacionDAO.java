package trismegistoplanilla.dao;

import org.json.JSONObject;
import trismegistoplanilla.beans.LoteAprobacionBean;

public interface LoteAprobacionDAO {

  public JSONObject aprobarFichaAdministrativa(JSONObject json);

  public JSONObject aprobarFichaDocente(JSONObject json);

  public JSONObject rechazarFicha(JSONObject json);

  public JSONObject generarLoteAprobacion(JSONObject json);

  public JSONObject listarFichasPresidenciaDT(String draw, String start, String length, JSONObject json);

  public JSONObject obtenerDetalleLote(JSONObject json);

  public JSONObject imprimirFichasAprobadas(LoteAprobacionBean la);

}
