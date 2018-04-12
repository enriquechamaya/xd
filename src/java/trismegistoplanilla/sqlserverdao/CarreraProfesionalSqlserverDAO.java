package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONObject;
import trismegistoplanilla.beans.CarreraProfesionalBean;
import trismegistoplanilla.dao.CarreraProfesionalDAO;
import trismegistoplanilla.utilities.ResponseHelper;

public class CarreraProfesionalSqlserverDAO implements CarreraProfesionalDAO {

    @Override
    public JSONObject listarCarreraProfesional(String carrera) {
        JSONObject jsonListarCarreraProfesional = new JSONObject();
        JSONArray jsonArrayListarCarreraProfesional = new JSONArray();
        PreparedStatement ps = null, psCount = null;
        ResultSet rs = null, rsCount = null;
        Connection connection = null;
        int total_count = 0;
        String base = "planillabd";
        try {
            connection = SqlserverDAOFactory.obtenerConexion(base);
            String sql
                    = "select "
                    + "codigo_carrera_profesional codigoCarreraProfesional, "
                    + "nombre nombre, "
                    + "estado_registro estadoRegistro "
                    + "FROM carrera_profesional "
                    + "where estado_registro = 1 and codigo_carrera_profesional != 404 and nombre like ?";
            System.out.println("listarCarreraProfesional: " + sql);
            ps = connection.prepareStatement(sql);
            ps.setString(1, carrera + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                CarreraProfesionalBean carreraProfesional = new CarreraProfesionalBean();
                carreraProfesional.setId(rs.getInt("codigoCarreraProfesional"));
                carreraProfesional.setCodigoCarreraProfesional(rs.getInt("codigoCarreraProfesional"));
                carreraProfesional.setNombre(rs.getString("nombre"));
                carreraProfesional.setEstado_registro(rs.getInt("estadoRegistro"));
                JSONObject jsonObjCarreraProfesional = new JSONObject(carreraProfesional);
                jsonArrayListarCarreraProfesional.put(jsonObjCarreraProfesional);
            }

            // count result search
            String sqlCount = ""
                    + "select "
                    + "count(1) total_count "
                    + "from carrera_profesional "
                    + "where estado_registro = 1 and nombre like ?";
            System.out.println("listarCarreraProfesional count: " + sqlCount);
            psCount = connection.prepareStatement(sqlCount);
            psCount.setString(1, carrera + "%");
            rsCount = psCount.executeQuery();
            rsCount.next();
            total_count = rsCount.getInt("total_count");
        } catch (SQLException e) {
            System.err.println("listarCarreraProfesional -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
            jsonListarCarreraProfesional.put("status", false);
            jsonListarCarreraProfesional.put("message", "Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (rsCount != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (psCount != null) {
                    psCount.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error: ha ocurrido un error al intentar cerrar las conexiones y/o liberacion de recursos -> " + e.getMessage());
            }
        }

        jsonListarCarreraProfesional.put("total_count", total_count);
        jsonListarCarreraProfesional.put("incomplete_results", false);
        jsonListarCarreraProfesional.put("items", jsonArrayListarCarreraProfesional);
        return jsonListarCarreraProfesional;
    }

    @Override
    public JSONObject validarExistenciaCarreraProfesional(CarreraProfesionalBean carreraProfesional) {
        JSONObject jsonValidarExistenciaCarreraProfesional = null;
        ResponseHelper response = new ResponseHelper();
        int existeCarreraProfesional = 0;
        String base = "planillabd";
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = SqlserverDAOFactory.obtenerConexion(base);
            String sql
                    = "select "
                    + "count(1) as existeCarreraProfesional "
                    + "from carrera_profesional "
                    + "where codigo_carrera_profesional = ? and estado_registro = 1";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, carreraProfesional.getCodigoCarreraProfesional());
            rs = ps.executeQuery();
            rs.next();
            existeCarreraProfesional = rs.getInt("existeCarreraProfesional");
            if (existeCarreraProfesional > 0) {
                response.setStatus(true);
                response.setMessage("La carrera profesional seleccionada existe.");
            } else {
                response.setStatus(false);
                response.setMessage("La carrera profesional seleccionada no existe.");
            }
        } catch (SQLException e) {
            System.err.println("validarExistenciaCarreraProfesional -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
            response.setStatus(false);
            response.setMessage("Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error: ha ocurrido un error al intentar cerrar las conexiones y/o liberacion de recursos -> " + e.getMessage());
            }
        }
        jsonValidarExistenciaCarreraProfesional = new JSONObject(response);
        return jsonValidarExistenciaCarreraProfesional;
    }

//    public static void main(String[] args) {
//        CarreraProfesionalSqlserverDAO metodo = new CarreraProfesionalSqlserverDAO();
//        String carrera = "a";
//        System.out.println(metodo.listarCarreraProfesional(carrera));
//    }
}
