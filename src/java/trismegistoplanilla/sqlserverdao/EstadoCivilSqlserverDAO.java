package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONObject;
import trismegistoplanilla.beans.EstadoCivilBean;
import trismegistoplanilla.dao.EstadoCivilDAO;
import trismegistoplanilla.utilities.ResponseHelper;

public class EstadoCivilSqlserverDAO implements EstadoCivilDAO {

    @Override
    public JSONObject listarEstadoCivil() {
        JSONObject jsonListarEstadoCivil = null;
        JSONArray jsonArrayListarEstadoCivil = new JSONArray();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        String base = "planillabd";
        ResponseHelper response = new ResponseHelper();

        try {
            connection = SqlserverDAOFactory.obtenerConexion(base);
            String sql
                    = "select "
                    + "codigo_estado_civil estadoCivil, "
                    + "nombre nombre, "
                    + "estado_registro estadoRegistro "
                    + "FROM estado_civil "
                    + "where estado_registro = 1";
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                EstadoCivilBean estadoCivil = new EstadoCivilBean();
                estadoCivil.setCodigoEstadoCivil(rs.getInt("estadoCivil"));
                estadoCivil.setNombre(rs.getString("nombre"));
                estadoCivil.setEstado_registro(rs.getInt("estadoRegistro"));
                JSONObject jsonObjEstadoCivil = new JSONObject(estadoCivil);
                jsonArrayListarEstadoCivil.put(jsonObjEstadoCivil);
            }
            JSONObject jsonObjEstadoCivil = new JSONObject();
            jsonObjEstadoCivil.put("EstadosCiviles", jsonArrayListarEstadoCivil);
            response.setStatus(true);
            response.setMessage("Los estados civiles se han listado correctamente");
            response.setData(jsonObjEstadoCivil);

        } catch (SQLException e) {
            System.err.println("listarEstadoCivil -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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

        jsonListarEstadoCivil = new JSONObject(response);
        return jsonListarEstadoCivil;
    }

    @Override
    public JSONObject validarExistenciaEstadoCivil(EstadoCivilBean estadoCivil) {
        JSONObject jsonValidarExistenciaEstadoCivil = null;
        ResponseHelper response = new ResponseHelper();
        int existeEstadoCivil = 0;
        String base = "planillabd";
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = SqlserverDAOFactory.obtenerConexion(base);
            String sql
                    = "select "
                    + "count(1) as existeEstadoCivil "
                    + "from estado_civil "
                    + "where codigo_estado_civil = ? and estado_registro = 1";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, estadoCivil.getCodigoEstadoCivil());
            rs = ps.executeQuery();
            rs.next();
            existeEstadoCivil = rs.getInt("existeEstadoCivil");
            if (existeEstadoCivil > 0) {
                response.setStatus(true);
                response.setMessage("El estado civil seleccionado existe.");
            } else {
                response.setStatus(false);
                response.setMessage("El estado civil seleccionado no existe.");
            }
        } catch (SQLException e) {
            System.err.println("validarExistenciaEstadoCivil -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
        jsonValidarExistenciaEstadoCivil = new JSONObject(response);
        return jsonValidarExistenciaEstadoCivil;
    }

}
