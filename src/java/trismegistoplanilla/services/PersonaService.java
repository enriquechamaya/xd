package trismegistoplanilla.services;

import org.json.JSONObject;
import trismegistoplanilla.beans.EstadoFichaBean;
import trismegistoplanilla.beans.PersonaBean;
import trismegistoplanilla.beans.TokenFichaBean;
import trismegistoplanilla.dao.DAOFactory;
import trismegistoplanilla.dao.PersonaDAO;

public class PersonaService {

  DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
  PersonaDAO objPersonaDAO = factory.getPersonaDAO();

  public JSONObject validarExistenciaNumeroDocumento(PersonaBean p) {
    JSONObject jsonObjValidarExistenciaNumeroDocumento = null;
    try {
      jsonObjValidarExistenciaNumeroDocumento = objPersonaDAO.validarExistenciaNumeroDocumento(p);
    } catch (Exception e) {
      System.out.println("Error validarExistenciaNumeroDocumento service: " + e.getMessage());
    }
    return jsonObjValidarExistenciaNumeroDocumento;
  }

  public JSONObject validarExistenciaCorreoElectronico(PersonaBean p) {
    JSONObject jsonObjValidarExistenciaCorreoElectronico = null;
    try {
      jsonObjValidarExistenciaCorreoElectronico = objPersonaDAO.validarExistenciaCorreoElectronico(p);
    } catch (Exception e) {
      System.out.println("Error validarExistenciaCorreoElectronico service: " + e.getMessage());
    }
    return jsonObjValidarExistenciaCorreoElectronico;
  }

  public JSONObject registrarPersona(PersonaBean p, EstadoFichaBean ef) {
    JSONObject jsonObjRegistrarPersona = null;
    try {
      jsonObjRegistrarPersona = objPersonaDAO.registrarPersona(p, ef);
    } catch (Exception e) {
      System.out.println("Error registrarPersona service: " + e.getMessage());
    }
    return jsonObjRegistrarPersona;
  }

  public JSONObject verificarPersona(TokenFichaBean tf, PersonaBean p) {
    JSONObject jsonObjVerificarPersona = null;
    try {
      jsonObjVerificarPersona = objPersonaDAO.verificarPersona(tf, p);
    } catch (Exception e) {
      System.out.println("Error verificarPersona service: " + e.getMessage());
    }
    return jsonObjVerificarPersona;
  }

  public JSONObject obtenerCodigoPersona(TokenFichaBean tf) {
    JSONObject jsonObjObtenerCodigoPersona = null;
    try {
      jsonObjObtenerCodigoPersona = objPersonaDAO.obtenerCodigoPersona(tf);
    } catch (Exception e) {
      System.out.println("Error obtenerCodigoPersona service: " + e.getMessage());
    }
    return jsonObjObtenerCodigoPersona;
  }

  public JSONObject obtenerPersonaSession(PersonaBean p) {
    JSONObject jsonObjObtenerPersonaSession = null;
    try {
      jsonObjObtenerPersonaSession = objPersonaDAO.obtenerPersonaSession(p);
    } catch (Exception e) {
      System.out.println("Error obtenerPersonaSession service: " + e.getMessage());
    }
    return jsonObjObtenerPersonaSession;
  }

}
