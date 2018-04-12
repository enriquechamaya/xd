package trismegistoplanilla.dao;

import org.json.JSONObject;
import trismegistoplanilla.beans.TokenFichaBean;

public interface TokenFichaDAO {

    public JSONObject validarToken(TokenFichaBean tf); // valida si el token existe o no

    public JSONObject validarTokenURL(TokenFichaBean tf); // valida el token en caso de manipulacion de url

    public JSONObject desactivarToken(TokenFichaBean tf); // desactiva el token

}
