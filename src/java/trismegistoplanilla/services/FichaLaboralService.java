package trismegistoplanilla.services;

import org.json.JSONArray;
import org.json.JSONObject;
import pe.siso.webservicesseguridad.webservices.UsuarioBean;
import trismegistoplanilla.beans.FichaLaboralBean;
import trismegistoplanilla.beans.PersonaBean;
import trismegistoplanilla.beans.SueldoAdministrativoBean;
import trismegistoplanilla.beans.SueldoDocenteBean;
import trismegistoplanilla.dao.DAOFactory;
import trismegistoplanilla.dao.FichaLaboralDAO;

public class FichaLaboralService {

  DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
  FichaLaboralDAO objFichaLaboralDAO = factory.getFichaLaboralDAO();

  public JSONObject registrarFichaLaboral(FichaLaboralBean fl, PersonaBean p, JSONArray jaExpediente, SueldoAdministrativoBean sa, SueldoDocenteBean sd, UsuarioBean u) {
    JSONObject JORegistrarFichaLaboral = null;
    try {
      JORegistrarFichaLaboral = objFichaLaboralDAO.registrarFichaLaboral(fl, p, jaExpediente, sa, sd, u);
    } catch (Exception e) {
      System.out.println("Error service registrarFichaLaboral => " + e.getMessage());
    }
    return JORegistrarFichaLaboral;
  }

}
