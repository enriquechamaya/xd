package trismegistoplanilla.services;

import org.json.JSONObject;
import trismegistoplanilla.beans.NivelEstadoBean;
import trismegistoplanilla.dao.DAOFactory;
import trismegistoplanilla.dao.NivelEstadoDAO;

public class NivelEstadoService {

    DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
    NivelEstadoDAO service = daoFactory.getNivelEstadoDAO();

    public JSONObject obtenerNivelEstado(NivelEstadoBean nivelEstado) {
        JSONObject jsonReturn = null;
        try {
            jsonReturn = service.obtenerNivelEstado(nivelEstado);
        } catch (Exception e) {
        }
        return jsonReturn;
    }
}
