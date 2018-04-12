package trismegistoplanilla.dao;

import org.json.JSONObject;
import trismegistoplanilla.beans.CarreraProfesionalBean;

public interface CarreraProfesionalDAO {

    public JSONObject listarCarreraProfesional(String carrera);

    public JSONObject validarExistenciaCarreraProfesional(CarreraProfesionalBean carreraProfesional);

}
