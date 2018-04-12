package trismegistoplanilla.services;

import org.json.JSONObject;
import trismegistoplanilla.beans.TipoExpedienteBean;
import trismegistoplanilla.dao.DAOFactory;
import trismegistoplanilla.dao.TipoExpedienteDAO;

public class TipoExpedienteService {

  DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
  TipoExpedienteDAO obj = factory.getTipoExpedienteDAO();

  public JSONObject listarTipoExpediente() {
    JSONObject JOlistarTipoExpediente = null;
    try {
      JOlistarTipoExpediente = obj.listarTipoExpediente();
    } catch (Exception e) {
      System.out.println("Error service listarTipoExpediente: " + e.getMessage());
    }
    return JOlistarTipoExpediente;
  }

  public JSONObject registrarTipoExpediente(TipoExpedienteBean te) {
    JSONObject JOregistrarTipoExpediente = null;
    try {
      JOregistrarTipoExpediente = obj.registrarTipoExpediente(te);
    } catch (Exception e) {
      System.out.println("Error service registrarTipoExpediente: " + e.getMessage());
    }
    return JOregistrarTipoExpediente;
  }

}
