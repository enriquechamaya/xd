package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONObject;
import trismegistoplanilla.beans.NacionalidadBean;
import trismegistoplanilla.dao.NacionalidadDAO;
import trismegistoplanilla.utilities.ResponseHelper;

public class NacionalidadSqlserverDAO implements NacionalidadDAO {

    @Override
    public JSONObject listarNacionalidad() {
        JSONObject jsonListarNacionalidad = null;
        JSONArray jsonArrayListarNacionalidad = new JSONArray();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String base = "planillabd";
        ResponseHelper response = new ResponseHelper();
        try {
            connection = SqlserverDAOFactory.obtenerConexion(base);
            String sql
                    = "select codigo_nacionalidad codigoNacionalidad, "
                    + "pais pais, "
                    + "gentilicio gentilicio, "
                    + "iso iso, "
                    + "estado_registro estadoRegistro "
                    + "FROM nacionalidad where estado_registro = 1";
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                NacionalidadBean nacionalidad = new NacionalidadBean();
                nacionalidad.setCodigoNacionalidad(rs.getInt("codigoNacionalidad"));
                nacionalidad.setPais(rs.getString("pais"));
                nacionalidad.setGentilicio(rs.getString("gentilicio"));
                nacionalidad.setIso(rs.getString("iso"));
                nacionalidad.setEstadoRegistro(rs.getInt("estadoRegistro"));
                JSONObject jsonObjListarNacionalidad = new JSONObject(nacionalidad);
                jsonArrayListarNacionalidad.put(jsonObjListarNacionalidad);
            }

            JSONObject jsonObjListarNacionalidad = new JSONObject();
            jsonObjListarNacionalidad.put("Nacionalidades", jsonArrayListarNacionalidad);
            response.setStatus(true);
            response.setMessage("Las nacionalidades se han listado correctamente");
            response.setData(jsonObjListarNacionalidad);

        } catch (SQLException e) {
            System.err.println("listarNacionalidad -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
        jsonListarNacionalidad = new JSONObject(response);
        return jsonListarNacionalidad;
    }

    @Override
    public JSONObject validarExistenciaNacionalidad(NacionalidadBean nacionalidad) {
        JSONObject jsonvalidadExistenciaNacionalidad = null;
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
                    + "count(1) as existeNacionalidad "
                    + "from nacionalidad "
                    + "where codigo_nacionalidad = ? and estado_registro = 1";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, nacionalidad.getCodigoNacionalidad());
            rs = ps.executeQuery();
            rs.next();
            existeEstadoCivil = rs.getInt("existeNacionalidad");
            if (existeEstadoCivil > 0) {
                response.setStatus(true);
                response.setMessage("La nacionalidad seleccionada existe.");
            } else {
                response.setStatus(false);
                response.setMessage("La nacionalidad seleccionada no existe.");
            }
        } catch (SQLException e) {
            System.err.println("validadExistenciaNacionalidad -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
        jsonvalidadExistenciaNacionalidad = new JSONObject(response);
        return jsonvalidadExistenciaNacionalidad;
    }

}
