package trismegistoplanilla.services;

import org.json.JSONObject;
import trismegistoplanilla.beans.NacionalidadBean;
import trismegistoplanilla.dao.DAOFactory;
import trismegistoplanilla.dao.NacionalidadDAO;

public class NacionalidadService {

    DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
    NacionalidadDAO service = daoFactory.getNacionalidadDAO();

    public JSONObject listarNacionalidad() {
        JSONObject jsonReturn = null;
        try {
            jsonReturn = service.listarNacionalidad();
        } catch (Exception e) {
            System.out.println("Error service listarNacionalidad => " + e.getMessage());
        }
        return jsonReturn;
    }

    public JSONObject validarExistenciaNacionalidad(NacionalidadBean nacionalidad) {
        JSONObject jsonReturn = null;
        try {
            jsonReturn = service.validarExistenciaNacionalidad(nacionalidad);
        } catch (Exception e) {
            System.out.println("Error service validarExistenciaNacionalidad => " + e.getMessage());
        }
        return jsonReturn;
    }
}
