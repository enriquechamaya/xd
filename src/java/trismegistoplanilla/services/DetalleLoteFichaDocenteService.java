package trismegistoplanilla.services;

import org.json.JSONObject;
import trismegistoplanilla.beans.LoteFichaBean;
import trismegistoplanilla.dao.DAOFactory;
import trismegistoplanilla.dao.DetalleLoteFichaDocenteDAO;

public class DetalleLoteFichaDocenteService {

  DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
  DetalleLoteFichaDocenteDAO dao = daoFactory.getDetalleLoteFichaDocenteDAO();

  public JSONObject listarDetalleLoteFichaDocenteDT(LoteFichaBean loteFicha) {
    JSONObject jsonReturn = null;
    try {
      jsonReturn = dao.listarDetalleLoteFichaDocenteDT(loteFicha);
    } catch (Exception e) {
      System.out.println("Error en Service: listarDetalleLoteFichaDocenteDT => " + e.getMessage());
    }
    return jsonReturn;
  }

  public JSONObject registrarSueldosPresidenciaLoteDocente(JSONObject data) {
    JSONObject jsonReturn = null;
    try {
      jsonReturn = dao.registrarSueldosPresidenciaLoteDocente(data);
    } catch (Exception e) {
      System.out.println("Error en Service: registrarSueldosPresidenciaLoteDocente => " + e.getMessage());
    }
    return jsonReturn;
  }

}
