package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONObject;
import trismegistoplanilla.beans.EstadoEstudioBean;
import trismegistoplanilla.dao.EstadoEstudioDAO;
import trismegistoplanilla.utilities.ResponseHelper;

public class EstadoEstudioSqlserverDAO implements EstadoEstudioDAO {

    @Override
    public JSONObject validarExistenciaEstadoEstudio(EstadoEstudioBean estadoEstudio) {
        JSONObject jsonValidarExistenciaEstadoEstudio = null;
        ResponseHelper response = new ResponseHelper();
        int existeEstadoEstudio = 0;
        String base = "planillabd";
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = SqlserverDAOFactory.obtenerConexion(base);
            String sql
                    = "select "
                    + "count (1) existeEstadoEstudio "
                    + "from nivel_estado "
                    + "inner join dbo.nivel_estudio ON dbo.nivel_estudio.codigo_nivel_estudio = dbo.nivel_estado.codigo_nivel_estudio "
                    + "inner join dbo.estado_estudio ON dbo.estado_estudio.codigo_estado_estudio = dbo.nivel_estado.codigo_estado_estudio "
                    + "where nivel_estudio.estado_registro = 1 "
                    + "and nivel_estado.codigo_nivel_estudio = ? "
                    + "and nivel_estado.codigo_estado_estudio = ?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, estadoEstudio.getCodigoNivelEstudio());
            ps.setInt(2, estadoEstudio.getCodigoEstadoEstudio());
            rs = ps.executeQuery();
            rs.next();
            existeEstadoEstudio = rs.getInt("existeEstadoEstudio");
            if (existeEstadoEstudio > 0) {
                response.setStatus(true);
                response.setMessage("El estado estudio seleccionado existe.");
            } else {
                response.setStatus(false);
                response.setMessage("El estado estudio seleccionado no existe.");
            }
        } catch (SQLException e) {
            System.err.println("validarExistenciaEstadoEstudio -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
        jsonValidarExistenciaEstadoEstudio = new JSONObject(response);
        return jsonValidarExistenciaEstadoEstudio;
    }

    @Override
    public JSONObject listarEstadoEstudio(EstadoEstudioBean estadoEstudio) {
        JSONObject jsonListarEstadoEstudio = null;
        JSONArray jsonArrayListarEstadoEstudio = new JSONArray();
        ResponseHelper response = new ResponseHelper();
        String base = "planillabd";
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = SqlserverDAOFactory.obtenerConexion(base);
            String sql
                    = "select "
                    + "estado_estudio.codigo_estado_estudio codigoEstadoEstudio, "
                    + "estado_estudio.nombre nombreEstadoEstudio "
                    + "from nivel_estado "
                    + "inner join dbo.estado_estudio ON dbo.estado_estudio.codigo_estado_estudio = dbo.nivel_estado.codigo_estado_estudio "
                    + "where estado_estudio.estado_registro = 1 and nivel_estado.codigo_nivel_estudio = ?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, estadoEstudio.getCodigoNivelEstudio());
            rs = ps.executeQuery();
            while (rs.next()) {
                estadoEstudio.setCodigoEstadoEstudio(rs.getInt("codigoEstadoEstudio"));
                estadoEstudio.setNombre(rs.getString("nombreEstadoEstudio"));
                JSONObject jsonObjEstadoEstudio = new JSONObject(estadoEstudio);
                jsonArrayListarEstadoEstudio.put(jsonObjEstadoEstudio);
            }

            JSONObject jsonObjEstadoEstudio = new JSONObject();
            jsonObjEstadoEstudio.put("estadoestudio", jsonArrayListarEstadoEstudio);

            response.setStatus(true);
            response.setMessage("Los estados de estudio se han listado correctamente");
            response.setData(jsonObjEstadoEstudio);

        } catch (SQLException e) {
            System.err.println("listarEstadoEstudio -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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

        jsonListarEstadoEstudio = new JSONObject(response);
        return jsonListarEstadoEstudio;
    }
}
