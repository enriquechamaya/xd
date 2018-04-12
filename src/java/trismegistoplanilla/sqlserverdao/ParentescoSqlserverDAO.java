package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONObject;
import trismegistoplanilla.beans.ParentescoBean;
import trismegistoplanilla.dao.ParentescoDAO;
import trismegistoplanilla.utilities.ResponseHelper;

public class ParentescoSqlserverDAO implements ParentescoDAO {

    @Override
    public JSONObject listarParentesco() {
        JSONObject jsonListarParentesco = null;
        JSONArray jsonArrayListarParentesco = new JSONArray();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        String base = "planillabd";
        ResponseHelper response = new ResponseHelper();

        try {
            connection = SqlserverDAOFactory.obtenerConexion(base);
            String sql
                    = "select "
                    + "codigo_parentesco codigoParentesco, "
                    + "nombre nombre, "
                    + "estado_registro estadoRegistro "
                    + "FROM parentesco "
                    + "where estado_registro = 1";
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                ParentescoBean parentesco = new ParentescoBean();
                parentesco.setCodigoParentesco(rs.getInt("codigoParentesco"));
                parentesco.setNombre(rs.getString("nombre"));
                parentesco.setEstado_registro(rs.getInt("estadoRegistro"));
                JSONObject jsonObjParentesco = new JSONObject(parentesco);
                jsonArrayListarParentesco.put(jsonObjParentesco);
            }
            JSONObject jsonObjParentesco = new JSONObject();
            jsonObjParentesco.put("parentescos", jsonArrayListarParentesco);
            response.setStatus(true);
            response.setMessage("Los parentescos se han listado correctamente");
            response.setData(jsonObjParentesco);

        } catch (SQLException e) {
            System.err.println("listarParentesco -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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

        jsonListarParentesco = new JSONObject(response);
        return jsonListarParentesco;
    }

    @Override
    public JSONObject validarExistenciaParentesco(ParentescoBean parentesco) {
        JSONObject jsonValidarExistenciaParentesco = null;
        ResponseHelper response = new ResponseHelper();
        int existeParentesco = 0;
        String base = "planillabd";
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = SqlserverDAOFactory.obtenerConexion(base);
            String sql
                    = "select "
                    + "count(1) as existeParentesco "
                    + "from parentesco "
                    + "where codigo_parentesco = ? and estado_registro = 1";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, parentesco.getCodigoParentesco());
            rs = ps.executeQuery();
            rs.next();
            existeParentesco = rs.getInt("existeParentesco");
            if (existeParentesco > 0) {
                response.setStatus(true);
                response.setMessage("El parentesco seleccionado existe.");
            } else {
                response.setStatus(false);
                response.setMessage("El parentesco seleccionado no existe.");
            }
        } catch (SQLException e) {
            System.err.println("validarExistenciaParentesco -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
        jsonValidarExistenciaParentesco = new JSONObject(response);
        return jsonValidarExistenciaParentesco;
    }

}
