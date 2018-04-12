package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONObject;
import trismegistoplanilla.beans.AreaBean;
import trismegistoplanilla.beans.CargoBean;
import trismegistoplanilla.dao.AreaCargoDAO;
import trismegistoplanilla.utilities.ResponseHelper;

public class AreaCargoSqlserverDAO implements AreaCargoDAO {

  @Override
  public JSONObject obtenerAreaCargo(AreaBean a, CargoBean c) {
    JSONObject JOObtenerAreaCargo = null;
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = ""
            + "select "
            + "codigo_area_cargo codigoAreaCargo "
            + "from area_cargo "
            + "where codigo_area = ? and codigo_cargo = ? and estado_registro = 1";
    System.out.println("SQL obtenerAreaCargo: " + sql);
    Connection conexion = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      conexion = SqlserverDAOFactory.obtenerConexion(base);
      ps = conexion.prepareStatement(sql);
      ps.setInt(1, a.getCodigoArea());
      ps.setInt(2, c.getCodigoCargo());
      rs = ps.executeQuery();
      rs.next();
      int codigoAreaCargo = rs.getInt("codigoAreaCargo");
      if (codigoAreaCargo > 0) {
        JSONObject objAreaCargo = new JSONObject();
        objAreaCargo.put("getResultedKey", codigoAreaCargo);
        response.setStatus(true);
        response.setMessage("Se encontró el código");
        response.setData(objAreaCargo);
      } else {
        response.setStatus(false);
        response.setMessage("Error! no se ha podido encontrar el código area cargo");
      }
    } catch (SQLException e) {
      System.err.println("obtenerAreaCargo -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
    JOObtenerAreaCargo = new JSONObject(response);
    return JOObtenerAreaCargo;

  }

}
