package trismegistoplanilla.services;

import org.json.JSONObject;
import trismegistoplanilla.beans.AreaCargoTipoPagoBean;
import trismegistoplanilla.dao.DAOFactory;
import trismegistoplanilla.dao.EscalafonDAO;

public class EscalafonService {

  DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
  EscalafonDAO dao = daoFactory.getEscalafonDAO();

  public JSONObject obtenerEscalafonAreaCargoTipoPago(AreaCargoTipoPagoBean actp) {
    JSONObject jsonObtenerEscalafonAreaCargo = null;
    try {
      jsonObtenerEscalafonAreaCargo = dao.obtenerEscalafonAreaCargoTipoPago(actp);
    } catch (Exception e) {
    }
    return jsonObtenerEscalafonAreaCargo;
  }

}
