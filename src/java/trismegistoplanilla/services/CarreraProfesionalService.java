package trismegistoplanilla.services;

import org.json.JSONObject;
import trismegistoplanilla.beans.CarreraProfesionalBean;
import trismegistoplanilla.dao.CarreraProfesionalDAO;
import trismegistoplanilla.dao.DAOFactory;

public class CarreraProfesionalService {

    DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
    CarreraProfesionalDAO service = daoFactory.getCarreraProfesionalDAO();

    public JSONObject listarCarreraProfesional(String carrera) {
        JSONObject jsonReturn = null;
        try {
            jsonReturn = service.listarCarreraProfesional(carrera);
        } catch (Exception e) {
        }
        return jsonReturn;
    }

    public JSONObject validarExistenciaCarreraProfesional(CarreraProfesionalBean carreraProfesional) {
        JSONObject jsonReturn = null;
        try {
            jsonReturn = service.validarExistenciaCarreraProfesional(carreraProfesional);
        } catch (Exception e) {
        }
        return jsonReturn;
    }
}
