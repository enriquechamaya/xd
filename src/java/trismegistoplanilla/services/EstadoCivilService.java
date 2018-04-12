package trismegistoplanilla.services;

import org.json.JSONObject;
import trismegistoplanilla.beans.EstadoCivilBean;
import trismegistoplanilla.dao.DAOFactory;
import trismegistoplanilla.dao.EstadoCivilDAO;

public class EstadoCivilService {

    DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
    EstadoCivilDAO service = daoFactory.getEstadoCivilDAO();

    public JSONObject listarEstadoCivil() {
        JSONObject jsonReturn = null;
        try {
            jsonReturn = service.listarEstadoCivil();
        } catch (Exception e) {
            System.out.println("Error en EstadoCivilService => " + e.getMessage());
        }
        return jsonReturn;
    }

    public JSONObject validarExistenciaEstadoCivil(EstadoCivilBean estadoCivil) {
        JSONObject jsonReturn = null;
        try {
            jsonReturn = service.validarExistenciaEstadoCivil(estadoCivil);
        } catch (Exception e) {
            System.out.println("Error en validarExistenciaEstadoCivil => " + e.getMessage());
        }
        return jsonReturn;
    }

}
