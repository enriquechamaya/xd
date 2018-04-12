package trismegistoplanilla.services;

import org.json.JSONObject;
import trismegistoplanilla.beans.UbigeoBean;
import trismegistoplanilla.dao.DAOFactory;
import trismegistoplanilla.dao.UbigeoDAO;

public class UbigeoService {

    DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
    UbigeoDAO service = daoFactory.getUbigeoDAO();

    public JSONObject listarDepartamento() {
        JSONObject jsonReturn = null;
        try {
            jsonReturn = service.listarDepartamento();
        } catch (Exception e) {
        }
        return jsonReturn;
    }

    public JSONObject listarProvincia(UbigeoBean ubigeo) {
        JSONObject jsonReturn = null;
        try {
            jsonReturn = service.listarProvincia(ubigeo);
        } catch (Exception e) {
        }
        return jsonReturn;
    }

    public JSONObject listarDistrito(UbigeoBean ubigeo) {
        JSONObject jsonReturn = null;
        try {
            jsonReturn = service.listarDistrito(ubigeo);
        } catch (Exception e) {
        }
        return jsonReturn;
    }

    public JSONObject validarExistenciaDepartamento(UbigeoBean ubigeo) {
        JSONObject jsonReturn = null;
        try {
            jsonReturn = service.validarExistenciaDepartamento(ubigeo);
        } catch (Exception e) {
        }
        return jsonReturn;
    }

    public JSONObject validarExistenciaProvincia(UbigeoBean ubigeo) {
        JSONObject jsonReturn = null;
        try {
            jsonReturn = service.validarExistenciaProvincia(ubigeo);
        } catch (Exception e) {
        }
        return jsonReturn;
    }

    public JSONObject validarExistenciaDistrito(UbigeoBean ubigeo) {
        JSONObject jsonReturn = null;
        try {
            jsonReturn = service.validarExistenciaDistrito(ubigeo);
        } catch (Exception e) {
        }
        return jsonReturn;
    }

    public JSONObject obtenerCodigoUbigeo(UbigeoBean ubigeo) {
        JSONObject jsonReturn = null;
        try {
            jsonReturn = service.obtenerCodigoUbigeo(ubigeo);
        } catch (Exception e) {
        }
        return jsonReturn;
    }

}
