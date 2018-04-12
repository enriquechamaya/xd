package trismegistoplanilla.dao;

import org.json.JSONObject;
import trismegistoplanilla.beans.AreaCargoBean;
import trismegistoplanilla.beans.AreaCargoTipoPagoBean;

public interface TipoPagoDAO {

  public JSONObject listarTipoPago(AreaCargoBean ac);

  public JSONObject validarExistenciaTipoPago(AreaCargoTipoPagoBean actp);

}
