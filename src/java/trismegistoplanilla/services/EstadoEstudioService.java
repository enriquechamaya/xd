package trismegistoplanilla.services;

import org.json.JSONObject;
import trismegistoplanilla.beans.EstadoEstudioBean;
import trismegistoplanilla.dao.DAOFactory;
import trismegistoplanilla.dao.EstadoEstudioDAO;

public class EstadoEstudioService {

    DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
    EstadoEstudioDAO service = daoFactory.getEstadoEstudioDAO();

    public JSONObject validarExistenciaEstadoEstudio(EstadoEstudioBean estadoEstudio) {
        JSONObject jsonReturn = null;
        try {
            jsonReturn = service.validarExistenciaEstadoEstudio(estadoEstudio);
        } catch (Exception e) {
        }
        return jsonReturn;
    }

    public JSONObject listarEstadoEstudio(EstadoEstudioBean estadoEstudio) {
        JSONObject jsonReturn = null;
        try {
            jsonReturn = service.listarEstadoEstudio(estadoEstudio);
        } catch (Exception e) {
        }
        return jsonReturn;
    }
}
