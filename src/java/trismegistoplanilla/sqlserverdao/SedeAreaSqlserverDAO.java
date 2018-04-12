package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONObject;
import trismegistoplanilla.beans.AreaBean;
import trismegistoplanilla.beans.SedeBean;
import trismegistoplanilla.dao.SedeAreaDAO;
import trismegistoplanilla.utilities.ResponseHelper;

public class SedeAreaSqlserverDAO implements SedeAreaDAO {

  @Override
  public JSONObject obtenerSedeArea(SedeBean s, AreaBean a) {
    JSONObject JOObtenerSedeArea = null;
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = ""
            + "select "
            + "sede_area.codigo_sede_area codigoSedeArea "
            + "from sede_area "
            + "where sede_area.codigo_sede = ? and sede_area.codigo_area = ? and sede_area.estado_registro = 1";
    System.out.println("SQL obtenerSedeArea: " + sql);
    Connection conexion = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      conexion = SqlserverDAOFactory.obtenerConexion(base);
      ps = conexion.prepareStatement(sql);
      ps.setInt(1, s.getCodigoSede());
      ps.setInt(2, a.getCodigoArea());
      rs = ps.executeQuery();
      rs.next();
      int codigoSedeArea = rs.getInt("codigoSedeArea");
      if (codigoSedeArea > 0) {
        JSONObject objSedeArea = new JSONObject();
        objSedeArea.put("getResultedKey", codigoSedeArea);
        response.setStatus(true);
        response.setMessage("Se encontró el código");
        response.setData(objSedeArea);
      } else {
        response.setStatus(false);
        response.setMessage("Error! no se ha podido encontrar el código sede area");
      }
    } catch (SQLException e) {
      System.err.println("validarExistenciaArea -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
    JOObtenerSedeArea = new JSONObject(response);
    return JOObtenerSedeArea;
  }

//  public static void main(String[] args) {
//    SedeAreaSqlServerDAO metodo = new SedeAreaSqlServerDAO();
//    SedeBean s = new SedeBean();
//    AreaBean a = new AreaBean();
//    s.setCodigoSede(30);
//    a.setCodigoArea(1);
//    System.out.println(metodo.obtenerSedeArea(s, a));
//  }
}
