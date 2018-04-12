package trismegistoplanilla.services;

import org.json.JSONObject;
import trismegistoplanilla.beans.TokenFichaBean;
import trismegistoplanilla.dao.DAOFactory;
import trismegistoplanilla.dao.TokenFichaDAO;

public class TokenFichaService {

    DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
    TokenFichaDAO objTokenFichaDAO = factory.getTokenFichaDAO();

    public JSONObject validarTokenURL(TokenFichaBean tf) {
        JSONObject jsonObjValidarTokenURL = null;
        try {
            jsonObjValidarTokenURL = objTokenFichaDAO.validarTokenURL(tf);
        } catch (Exception e) {
            System.out.println("Error validarToken service: " + e.getMessage());
        }
        return jsonObjValidarTokenURL;
    }

    public JSONObject validarToken(TokenFichaBean tf) {
        JSONObject jsonObjValidarExistenciaToken = null;
        try {
            jsonObjValidarExistenciaToken = objTokenFichaDAO.validarToken(tf);
        } catch (Exception e) {
            System.out.println("Error validarToken service: " + e.getMessage());
        }
        return jsonObjValidarExistenciaToken;
    }

    public JSONObject desactivarToken(TokenFichaBean tf) {
        JSONObject jsonObjDesactivarToken = null;
        try {
            jsonObjDesactivarToken = objTokenFichaDAO.desactivarToken(tf);
        } catch (Exception e) {
            System.out.println("Error desactivarToken service: " + e.getMessage());
        }
        return jsonObjDesactivarToken;
    }
}
