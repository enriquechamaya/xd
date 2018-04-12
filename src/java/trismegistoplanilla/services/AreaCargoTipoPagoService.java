package trismegistoplanilla.services;

import org.json.JSONObject;
import trismegistoplanilla.beans.AreaCargoTipoPagoBean;
import trismegistoplanilla.dao.AreaCargoTipoPagoDAO;
import trismegistoplanilla.dao.DAOFactory;

public class AreaCargoTipoPagoService {

  DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
  AreaCargoTipoPagoDAO objAreaCargoTipoPagoDAO = factory.getAreaCargoTipoPagoDAO();

  public JSONObject obtenerAreaCargoTipoPago(AreaCargoTipoPagoBean actp) {
    JSONObject JOObtenerAreaCargoTipoPago = null;
    try {
      JOObtenerAreaCargoTipoPago = objAreaCargoTipoPagoDAO.obtenerAreaCargoTipoPago(actp);
    } catch (Exception e) {
      System.out.println("Error service obtenerAreaCargoTipoPago => " + e.getMessage());
    }
    return JOObtenerAreaCargoTipoPago;
  }
}
