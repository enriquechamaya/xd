package trismegistoplanilla.dao;

import org.json.JSONObject;
import pe.siso.webservicesseguridad.webservices.UsuarioBean;
import trismegistoplanilla.beans.EstadoFichaBean;
import trismegistoplanilla.beans.FichaBean;
import trismegistoplanilla.beans.ObservacionFichaBean;
import trismegistoplanilla.beans.PersonaBean;
import trismegistoplanilla.beans.TokenFichaBean;

public interface FichaDAO {

  public JSONObject listarFichaDT(String draw, String length, String start, String search, int tipoDocumento, int criterio, UsuarioBean u);

  public JSONObject obtenerCodigoFicha(TokenFichaBean tf);

  public JSONObject registrarFicha(JSONObject data, PersonaBean p, TokenFichaBean tf);

  public JSONObject actualizarFicha(PersonaBean p, TokenFichaBean tf);

  public JSONObject listarDatosFicha(PersonaBean persona);

  public JSONObject consultarFichasPorCriterio(String draw, int start, int length, String search, JSONObject criterioClient, UsuarioBean u);

  public JSONObject listarDetalleEstadoFicha(FichaBean f);

  public JSONObject listarDatosAdministrativos(FichaBean f);

  public JSONObject rechazarFicha(FichaBean f, EstadoFichaBean ef);

  public JSONObject aceptarFicha(FichaBean f, EstadoFichaBean ef);

  public JSONObject listarFichasPresidenciaDT(String draw, String length, String start, JSONObject search);

  public JSONObject anularFicha(EstadoFichaBean ef, ObservacionFichaBean of);

  public JSONObject verObservacion(EstadoFichaBean ef);
  
  public JSONObject listarFichasPresidencia();

  public JSONObject verificarExistenciaFichaAnulada(PersonaBean p);

  public JSONObject obtenerCodigoPersonaPorTipoDocNroDoc(PersonaBean p);

  public JSONObject habilitarFicha(PersonaBean p, UsuarioBean u);

  public JSONObject validarFichaActiva(PersonaBean p);

}
