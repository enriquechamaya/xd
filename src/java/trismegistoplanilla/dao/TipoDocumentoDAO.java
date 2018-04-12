package trismegistoplanilla.dao;

import org.json.JSONObject;
import trismegistoplanilla.beans.TipoDocumentoBean;
import trismegistoplanilla.beans.TokenFichaBean;

public interface TipoDocumentoDAO {

    public JSONObject listarTipoDocumento();

    public JSONObject validarExistenciaTipoDocumento(TipoDocumentoBean tipoDocumento);

    public JSONObject obtenerLongitudTipoEntrdadaTipoDocumento(TipoDocumentoBean tipoDocumento);

    public JSONObject obtenerCodigoTipoDocumento(TokenFichaBean tf);

}
