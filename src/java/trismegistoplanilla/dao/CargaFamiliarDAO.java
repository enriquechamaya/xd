package trismegistoplanilla.dao;

import org.json.JSONObject;
import trismegistoplanilla.beans.CargaFamiliarBean;
import trismegistoplanilla.beans.PersonaBean;

public interface CargaFamiliarDAO {

  public JSONObject validarExistenciaNumeroDocumento(CargaFamiliarBean cargaFamiliar);

  public JSONObject obtenerCargaFamiliarPorPersona(PersonaBean persona);

}
