package trismegistoplanilla.services;

import org.json.JSONObject;
import trismegistoplanilla.beans.CargaFamiliarBean;
import trismegistoplanilla.beans.PersonaBean;
import trismegistoplanilla.dao.CargaFamiliarDAO;
import trismegistoplanilla.dao.DAOFactory;

public class CargaFamiliarService {

  DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
  CargaFamiliarDAO service = daoFactory.getCargaFamiliarDAO();

  public JSONObject validarExistenciaNumeroDocumento(CargaFamiliarBean cargaFamiliar) {
    JSONObject jsonReturn = null;
    try {
      jsonReturn = service.validarExistenciaNumeroDocumento(cargaFamiliar);
    } catch (Exception e) {
    }
    return jsonReturn;
  }

  public JSONObject obtenerCargaFamiliarPorPersona(PersonaBean persona) {
    JSONObject jsonReturn = null;
    try {
      jsonReturn = service.obtenerCargaFamiliarPorPersona(persona);
    } catch (Exception e) {
    }
    return jsonReturn;
  }

}
