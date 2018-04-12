package trismegistoplanilla.services;

import org.json.JSONObject;
import pe.siso.webservicesseguridad.webservices.UsuarioBean;
import trismegistoplanilla.dao.DAOFactory;
import trismegistoplanilla.dao.LoteFichaDAO;

public class LoteFichaService {

  DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
  LoteFichaDAO dao = daoFactory.getLoteFichaDAO();

  public JSONObject listarFichasDT(String draw, String length, String start, JSONObject json) {
    JSONObject jsonReturn = null;
    try {
      jsonReturn = dao.listarFichasDT(draw, length, start, json);
    } catch (Exception e) {
      System.err.println("Error LoteFichaService listarFichasDT >>>> " + e.getMessage());
    }
    return jsonReturn;
  }

  public JSONObject registrarLote(JSONObject data, UsuarioBean usuario) {
    JSONObject jsonReturn = null;
    try {
      jsonReturn = dao.registrarLote(data, usuario);
    } catch (Exception e) {
      System.err.println("Error LoteFichaService registrarLote >>>> " + e.getMessage());
    }
    return jsonReturn;
  }

  public JSONObject listarLoteDT(String draw, String length, String start, String search) {
    JSONObject jsonReturn = null;
    try {
      jsonReturn = dao.listarLoteDT(draw, length, start, search);
    } catch (Exception e) {
      System.err.println("Error LoteFichaService listarLoteDT >>>> " + e.getMessage());
    }
    return jsonReturn;
  }

  public JSONObject listarLoteGeneralDT(JSONObject json) {
    JSONObject jsonReturn = null;
    try {
      jsonReturn = dao.listarLoteGeneralDT(json);
    } catch (Exception e) {
      System.err.println("Error LoteFichaService listarLoteGeneralDT >>>> " + e.getMessage());
    }
    return jsonReturn;
  }

  public JSONObject listarFichasGeneral(JSONObject json) {
    JSONObject jsonReturn = null;
    try {
      jsonReturn = dao.listarFichasGeneral(json);
    } catch (Exception e) {
      System.err.println("Error listarFichasGeneral listarLoteGeneralDT >>>> " + e.getMessage());
    }
    return jsonReturn;
  }
}
