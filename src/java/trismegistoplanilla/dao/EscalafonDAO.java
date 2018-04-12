package trismegistoplanilla.dao;

import org.json.JSONObject;
import trismegistoplanilla.beans.AreaCargoTipoPagoBean;

public interface EscalafonDAO {

  public JSONObject obtenerEscalafonAreaCargoTipoPago(AreaCargoTipoPagoBean actp);

}
