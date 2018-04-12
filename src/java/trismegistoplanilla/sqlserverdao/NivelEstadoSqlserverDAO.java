package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONObject;
import trismegistoplanilla.beans.NivelEstadoBean;
import trismegistoplanilla.dao.NivelEstadoDAO;
import trismegistoplanilla.utilities.ResponseHelper;

public class NivelEstadoSqlserverDAO implements NivelEstadoDAO {

    @Override
    public JSONObject obtenerNivelEstado(NivelEstadoBean nivelEstado) {
        JSONObject jsonObtenerNivelEstado = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        String base = "planillabd";
        int codigoNivelEstado = 0;
        ResponseHelper response = new ResponseHelper();

        try {
            connection = SqlserverDAOFactory.obtenerConexion(base);
            String sql
                    = "select "
                    + "nivel_estado.codigo_nivel_estado as codigoNivelEstado "
                    + "from nivel_estado "
                    + "where nivel_estado.codigo_nivel_estudio = ? "
                    + "and nivel_estado.codigo_estado_estudio = ? "
                    + "and nivel_estado.estado_registro = 1";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, nivelEstado.getCodigoNivelEstudio());
            ps.setInt(2, nivelEstado.getCodigoEstadoEstudio());
            rs = ps.executeQuery();
            rs.next();
            codigoNivelEstado = rs.getInt("codigoNivelEstado");
            if (codigoNivelEstado > 0) {
                JSONObject getResultedKey = new JSONObject();
                getResultedKey.put("getResultedKey", codigoNivelEstado);
                response.setStatus(true);
                response.setData(getResultedKey);
                response.setMessage("Se obtuvo el código correctamente");
            } else {
                response.setStatus(false);
                response.setMessage("No se pudo obtener el código");
            }

        } catch (SQLException e) {
            System.err.println("obtenerNivelEstado -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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

        jsonObtenerNivelEstado = new JSONObject(response);
        return jsonObtenerNivelEstado;
    }

}
