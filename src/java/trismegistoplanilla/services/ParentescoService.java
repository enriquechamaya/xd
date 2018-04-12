package trismegistoplanilla.services;

import org.json.JSONObject;
import trismegistoplanilla.beans.ParentescoBean;
import trismegistoplanilla.dao.DAOFactory;
import trismegistoplanilla.dao.ParentescoDAO;

public class ParentescoService {

    DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
    ParentescoDAO service = daoFactory.getParentescoDAO();

    public JSONObject listarParentesco() {
        JSONObject jsonReturn = null;
        try {
            jsonReturn = service.listarParentesco();
        } catch (Exception e) {
        }
        return jsonReturn;
    }

    public JSONObject validarExistenciaParentesco(ParentescoBean parentesco) {
        JSONObject jsonReturn = null;
        try {
            jsonReturn = service.validarExistenciaParentesco(parentesco);
        } catch (Exception e) {
        }
        return jsonReturn;
    }
}
