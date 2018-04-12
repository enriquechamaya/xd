package trismegistoplanilla.dao;

import org.json.JSONArray;
import org.json.JSONObject;
import pe.siso.webservicesseguridad.webservices.UsuarioBean;
import trismegistoplanilla.beans.FichaLaboralBean;
import trismegistoplanilla.beans.PersonaBean;
import trismegistoplanilla.beans.SueldoAdministrativoBean;
import trismegistoplanilla.beans.SueldoDocenteBean;

public interface FichaLaboralDAO {

  public JSONObject registrarFichaLaboral(FichaLaboralBean fl, PersonaBean p, JSONArray jaExpediente, SueldoAdministrativoBean sa, SueldoDocenteBean sd, UsuarioBean u);

}
