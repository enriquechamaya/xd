package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONObject;
import trismegistoplanilla.beans.NivelEstudioBean;
import trismegistoplanilla.dao.NivelEstudioDAO;
import trismegistoplanilla.utilities.ResponseHelper;

public class NivelEstudioSqlserverDAO implements NivelEstudioDAO {

    @Override
    public JSONObject validarExistenciaNivelEstudio(NivelEstudioBean nivelEstudio) {
        JSONObject jsonValidarExistenciaNivelEstudio = null;
        ResponseHelper response = new ResponseHelper();
        int existeNivelEstudio = 0;
        String base = "planillabd";
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = SqlserverDAOFactory.obtenerConexion(base);
            String sql
                    = "select "
                    + "count (1) as existeNivelEstudio "
                    + "from nivel_estado "
                    + "inner join dbo.nivel_estudio ON dbo.nivel_estudio.codigo_nivel_estudio = dbo.nivel_estado.codigo_nivel_estudio "
                    + "where nivel_estudio.estado_registro = 1 and nivel_estado.codigo_nivel_estudio = ?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, nivelEstudio.getCodigoNivelEstudio());
            rs = ps.executeQuery();
            rs.next();
            existeNivelEstudio = rs.getInt("existeNivelEstudio");
            if (existeNivelEstudio > 0) {
                response.setStatus(true);
                response.setMessage("El nivel estudio seleccionado existe.");
            } else {
                response.setStatus(false);
                response.setMessage("El nivel estudio seleccionado no existe.");
            }
        } catch (SQLException e) {
            System.err.println("validarExistenciaNivelEstudio -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
        jsonValidarExistenciaNivelEstudio = new JSONObject(response);
        return jsonValidarExistenciaNivelEstudio;
    }

    @Override
    public JSONObject listarNivelEstudio() {
        JSONObject jsonListarNivelEstudio = null;
        JSONArray jsonArrayListarNivelEstudio = new JSONArray();
        ResponseHelper response = new ResponseHelper();
        String base = "planillabd";
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = SqlserverDAOFactory.obtenerConexion(base);
            String sql
                    = "select "
                    + "nivel_estudio.codigo_nivel_estudio codigoNivelEstudio, "
                    + "nivel_estudio.nombre nombreNivelEstudio "
                    + "from nivel_estado "
                    + "inner join dbo.nivel_estudio ON dbo.nivel_estudio.codigo_nivel_estudio = dbo.nivel_estado.codigo_nivel_estudio "
                    + "where nivel_estudio.estado_registro = 1 "
                    + "group by nivel_estudio.codigo_nivel_estudio, nivel_estudio.nombre";
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                NivelEstudioBean nivelEstudio = new NivelEstudioBean();
                nivelEstudio.setCodigoNivelEstudio(rs.getInt("codigoNivelEstudio"));
                nivelEstudio.setNombre(rs.getString("nombreNivelEstudio"));
                JSONObject jsonObjNivelEstudio = new JSONObject(nivelEstudio);
                jsonArrayListarNivelEstudio.put(jsonObjNivelEstudio);
            }

            JSONObject jsonObjEstadoEstudio = new JSONObject();
            jsonObjEstadoEstudio.put("nivelestudio", jsonArrayListarNivelEstudio);

            response.setStatus(true);
            response.setMessage("Los niveles de estudio se han listado correctamente");
            response.setData(jsonObjEstadoEstudio);

        } catch (SQLException e) {
            System.err.println("listarNivelEstudio -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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

        jsonListarNivelEstudio = new JSONObject(response);
        return jsonListarNivelEstudio;
    }

}
