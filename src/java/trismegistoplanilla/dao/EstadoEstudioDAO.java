package trismegistoplanilla.dao;

import org.json.JSONObject;
import trismegistoplanilla.beans.EstadoEstudioBean;

public interface EstadoEstudioDAO {

    public JSONObject validarExistenciaEstadoEstudio(EstadoEstudioBean estadoEstudio);

    public JSONObject listarEstadoEstudio(EstadoEstudioBean estadoEstudio);

}
