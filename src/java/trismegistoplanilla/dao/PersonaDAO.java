package trismegistoplanilla.dao;

import org.json.JSONObject;
import trismegistoplanilla.beans.EstadoFichaBean;
import trismegistoplanilla.beans.PersonaBean;
import trismegistoplanilla.beans.TokenFichaBean;

public interface PersonaDAO {

    public JSONObject validarExistenciaNumeroDocumento(PersonaBean p);

    public JSONObject validarExistenciaCorreoElectronico(PersonaBean p);

    public JSONObject registrarPersona(PersonaBean p, EstadoFichaBean ef);

    public JSONObject verificarPersona(TokenFichaBean tf, PersonaBean p);

    public JSONObject modificarCorreoFichaPersona(PersonaBean p);

    public JSONObject obtenerCodigoPersona(TokenFichaBean tf);

    public JSONObject obtenerPersonaSession(PersonaBean p);

}
