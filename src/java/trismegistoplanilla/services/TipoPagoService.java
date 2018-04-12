package trismegistoplanilla.services;

import org.json.JSONObject;
import trismegistoplanilla.beans.AreaCargoBean;
import trismegistoplanilla.beans.AreaCargoTipoPagoBean;
import trismegistoplanilla.dao.DAOFactory;
import trismegistoplanilla.dao.TipoPagoDAO;

public class TipoPagoService {

  DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
  TipoPagoDAO objTipoPago = factory.getTipoPagoDAO();

  public JSONObject listarTipoPago(AreaCargoBean ac) {
    JSONObject JOListarTipoPago = null;
    try {
      JOListarTipoPago = objTipoPago.listarTipoPago(ac);
    } catch (Exception e) {
      System.out.println("Error listarTipoPago service: " + e.getMessage());
    }
    return JOListarTipoPago;
  }

  public JSONObject validarExistenciaTipoPago(AreaCargoTipoPagoBean actp) {
    JSONObject JObjectValidarExistenciaTipoPago = null;
    try {
      JObjectValidarExistenciaTipoPago = objTipoPago.validarExistenciaTipoPago(actp);
    } catch (Exception e) {
      System.out.println("Error listarTipoPago service: " + e.getMessage());
    }
    return JObjectValidarExistenciaTipoPago;
  }

}
