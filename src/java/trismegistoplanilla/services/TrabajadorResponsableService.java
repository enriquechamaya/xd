package trismegistoplanilla.services;

import org.json.JSONObject;
import trismegistoplanilla.beans.TrabajadorResponsableBean;
import trismegistoplanilla.dao.DAOFactory;
import trismegistoplanilla.dao.TrabajadorResponsableDAO;

public class TrabajadorResponsableService {

  DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
  TrabajadorResponsableDAO dao = daoFactory.getTrabajadorResponsableDAO();

  public JSONObject registrarTrabajadorResponsable(TrabajadorResponsableBean trabajadorResponsable) {
    JSONObject jsonReturn = null;
    try {
      jsonReturn = dao.registrarTrabajadorResponsable(trabajadorResponsable);
    } catch (Exception e) {
    }
    return jsonReturn;
  }

  public JSONObject validarExistenciaTrabajadorPorDni(TrabajadorResponsableBean trabajadorResponsable) {
    JSONObject jsonReturn = null;
    try {
      jsonReturn = dao.validarExistenciaTrabajadorPorDni(trabajadorResponsable);
    } catch (Exception e) {
    }
    return jsonReturn;
  }

  public JSONObject obtenerSedeAreaCargoPorCodigoPlanilla(int codigoPlanillaReal) {
    JSONObject jsonReturn = null;
    try {
      jsonReturn = dao.obtenerSedeAreaCargoPorCodigoPlanilla(codigoPlanillaReal);
    } catch (Exception e) {
    }
    return jsonReturn;
  }
}
