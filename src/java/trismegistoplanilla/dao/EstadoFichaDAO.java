package trismegistoplanilla.dao;

import org.json.JSONObject;
import trismegistoplanilla.beans.EstadoFichaBean;

public interface EstadoFichaDAO {

  public JSONObject obtenerCodigoEstadoFicha(EstadoFichaBean ef);

  public JSONObject desactivarEstadoFicha(EstadoFichaBean ef);

  public JSONObject registrarEstadoFicha(EstadoFichaBean ef);

}
