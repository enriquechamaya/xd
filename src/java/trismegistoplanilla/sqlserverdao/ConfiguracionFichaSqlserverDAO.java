package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONObject;
import trismegistoplanilla.dao.ConfiguracionFichaDAO;
import trismegistoplanilla.utilities.ResponseHelper;

public class ConfiguracionFichaSqlserverDAO implements ConfiguracionFichaDAO {

  @Override
  public JSONObject getDefaultMail() {
    JSONObject JOmail = null;
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = ""
            + "select "
            + "correo "
            + "from configuracion_ficha "
            + "where estado = 1";
    System.out.println("SQL getDefaultMail: " + sql);
    Connection conexion = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      conexion = SqlserverDAOFactory.obtenerConexion(base);
      ps = conexion.prepareStatement(sql);
      rs = ps.executeQuery();
      rs.next();
      JSONObject obj = new JSONObject();
      obj.put("mail", rs.getString("correo"));
      response.setData(obj);
      response.setStatus(true);
    } catch (SQLException e) {
      System.err.println("getDefaultMail -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
        if (conexion != null) {
          conexion.close();
        }
      } catch (SQLException e) {
        System.err.println("Error: ha ocurrido un error al intentar cerrar las conexiones y/o liberacion de recursos -> " + e.getMessage());
      }
    }
    JOmail = new JSONObject(response);
    return JOmail;
  }

//  public static void main(String[] args) {
//    System.out.println(new ConfiguracionFichaSqlserverDAO().getDefaultMail());
//  }
}
