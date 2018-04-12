package trismegistoplanilla.dao;

import org.json.JSONObject;
import pe.siso.webservicesseguridad.webservices.UsuarioBean;

public interface LoteFichaDAO {

  public JSONObject listarFichasDT(String draw, String length, String start, JSONObject json);

  public JSONObject registrarLote(JSONObject json, UsuarioBean usuario);

  public JSONObject listarLoteDT(String draw, String length, String start, String search);
  
  public JSONObject listarLoteGeneralDT(JSONObject json);
  
  public JSONObject listarFichasGeneral(JSONObject json);
  
  public JSONObject listarDetalleFicha(JSONObject json);
}
