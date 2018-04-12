package trismegistoplanilla.services;

import org.json.JSONObject;
import trismegistoplanilla.beans.NivelEstudioBean;
import trismegistoplanilla.dao.DAOFactory;
import trismegistoplanilla.dao.NivelEstudioDAO;

public class NivelEstudioService {

    DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
    NivelEstudioDAO service = daoFactory.getNivelEstudioDAO();

    public JSONObject validarExistenciaNivelEstudio(NivelEstudioBean nivelEstudio) {
        JSONObject jsonReturn = null;
        try {
            jsonReturn = service.validarExistenciaNivelEstudio(nivelEstudio);
        } catch (Exception e) {
        }
        return jsonReturn;
    }

    public JSONObject listarNivelEstudio() {
        JSONObject jsonReturn = null;
        try {
            jsonReturn = service.listarNivelEstudio();
        } catch (Exception e) {
        }
        return jsonReturn;
    }
}
