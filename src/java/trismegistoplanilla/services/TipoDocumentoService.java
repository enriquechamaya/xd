package trismegistoplanilla.services;

import org.json.JSONObject;
import trismegistoplanilla.beans.TipoDocumentoBean;
import trismegistoplanilla.beans.TokenFichaBean;
import trismegistoplanilla.dao.DAOFactory;
import trismegistoplanilla.dao.TipoDocumentoDAO;

public class TipoDocumentoService {

    DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
    TipoDocumentoDAO objTipoDocumentoDAO = factory.getTipoDocumentoDAO();

    public JSONObject listarTipoDocumento() {
        JSONObject jsonObjListarTipoDocumento = null;
        try {
            jsonObjListarTipoDocumento = objTipoDocumentoDAO.listarTipoDocumento();
        } catch (Exception e) {
            System.out.println("Error listarTipoDocumento service: " + e.getMessage());
        }
        return jsonObjListarTipoDocumento;
    }

    public JSONObject validarExistenciaTipoDocumento(TipoDocumentoBean tipoDocumento) {
        JSONObject jsonObjValidarExistenciaTipoDocumento = null;
        try {
            jsonObjValidarExistenciaTipoDocumento = objTipoDocumentoDAO.validarExistenciaTipoDocumento(tipoDocumento);
        } catch (Exception e) {
            System.out.println("Error validarExistenciaTipoDocumento service: " + e.getMessage());
        }
        return jsonObjValidarExistenciaTipoDocumento;
    }

    public JSONObject obtenerLongitudTipoEntrdadaTipoDocumento(TipoDocumentoBean tipoDocumento) {
        JSONObject jsonObjObtenerLongitudTipoEntrdadaTipoDocumento = null;
        try {
            jsonObjObtenerLongitudTipoEntrdadaTipoDocumento = objTipoDocumentoDAO.obtenerLongitudTipoEntrdadaTipoDocumento(tipoDocumento);
        } catch (Exception e) {
            System.out.println("Error obtenerLongitudTipoEntrdadaTipoDocumento service: " + e.getMessage());
        }
        return jsonObjObtenerLongitudTipoEntrdadaTipoDocumento;
    }

    public JSONObject obtenerCodigoTipoDocumento(TokenFichaBean tf) {
        JSONObject jsonObjObtenerCodigoTipoDocumento = null;
        try {
            jsonObjObtenerCodigoTipoDocumento = objTipoDocumentoDAO.obtenerCodigoTipoDocumento(tf);
        } catch (Exception e) {
            System.out.println("Error obtenerCodigoTipoDocumento service: " + e.getMessage());
        }
        return jsonObjObtenerCodigoTipoDocumento;
    }

}
