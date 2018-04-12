package trismegistoplanilla.dao;

import org.json.JSONObject;
import trismegistoplanilla.beans.LoteFichaBean;

public interface DetalleLoteFichaDocenteDAO {

  public JSONObject listarDetalleLoteFichaDocenteDT(LoteFichaBean loteFicha);

  public JSONObject registrarSueldosPresidenciaLoteDocente(JSONObject json);

}
