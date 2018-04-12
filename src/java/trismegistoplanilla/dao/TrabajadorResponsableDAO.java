package trismegistoplanilla.dao;

import org.json.JSONObject;
import trismegistoplanilla.beans.TrabajadorResponsableBean;

public interface TrabajadorResponsableDAO {

  public JSONObject registrarTrabajadorResponsable(TrabajadorResponsableBean trabajadorResponsable);

  public JSONObject validarExistenciaTrabajadorPorDni(TrabajadorResponsableBean trabajadorResponsable);

  public JSONObject obtenerSedeAreaCargoPorCodigoPlanilla(int codigoPlanillaReal);

}
