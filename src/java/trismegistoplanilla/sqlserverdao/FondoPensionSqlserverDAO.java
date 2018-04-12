package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONObject;
import trismegistoplanilla.beans.FondoPensionBean;
import trismegistoplanilla.dao.FondoPensionDAO;
import trismegistoplanilla.utilities.ResponseHelper;

public class FondoPensionSqlserverDAO implements FondoPensionDAO {

  @Override
  public JSONObject listarFondoPension() {
    JSONObject jsonListarFondoPension = null;
    JSONArray jsonArrayListarFondoPension = new JSONArray();
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String base = "planillabd";
    ResponseHelper response = new ResponseHelper();

    try {
      connection = SqlserverDAOFactory.obtenerConexion(base);
      String sql
              = "select "
              + "codigo_fondo_pension codigoFondoPension, "
              + "descripcion_corta descripcionCorta, "
              + "descripcion_larga descripcionLarga, "
              + "estado_registro estadoRegistro "
              + "FROM fondo_pension "
              + "where estado_registro = 1";
      ps = connection.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        FondoPensionBean fondoPension = new FondoPensionBean();
        fondoPension.setCodigoFondoPension(rs.getInt("codigoFondoPension"));
        fondoPension.setDescripcionCorta(rs.getString("descripcionCorta"));
        fondoPension.setDescripcionLarga(rs.getString("descripcionLarga").trim());
        JSONObject jsonObjFondoPension = new JSONObject(fondoPension);
        jsonArrayListarFondoPension.put(jsonObjFondoPension);
      }
      JSONObject jsonObjFondoPension = new JSONObject();
      jsonObjFondoPension.put("fondopension", jsonArrayListarFondoPension);

      response.setStatus(true);
      response.setMessage("Los fondos de pensiones se han listado correctamente");
      response.setData(jsonObjFondoPension);

    } catch (SQLException e) {
      System.err.println("listarFondoPension -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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

    jsonListarFondoPension = new JSONObject(response);
    return jsonListarFondoPension;
  }

  @Override
  public JSONObject validarExistenciaFondoPension(FondoPensionBean fondoPension) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

}
