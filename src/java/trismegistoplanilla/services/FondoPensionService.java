/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trismegistoplanilla.services;

import org.json.JSONObject;
import trismegistoplanilla.beans.FondoPensionBean;
import trismegistoplanilla.dao.DAOFactory;
import trismegistoplanilla.dao.FondoPensionDAO;

/**
 *
 * @author sistem23user
 */
public class FondoPensionService {

    DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
    FondoPensionDAO service = daoFactory.getFondoPensionDAO();

    public JSONObject listarFondoPension() {
        JSONObject jsonReturn = null;
        try {
            jsonReturn = service.listarFondoPension();
        } catch (Exception e) {
        }
        return jsonReturn;
    }

    public JSONObject validarExistenciaFondoPension(FondoPensionBean fondoPension) {
        JSONObject jsonReturn = null;
        try {
            jsonReturn = service.validarExistenciaFondoPension(fondoPension);
        } catch (Exception e) {
        }
        return jsonReturn;
    }
}
