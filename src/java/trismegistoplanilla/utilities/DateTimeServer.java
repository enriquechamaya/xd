package trismegistoplanilla.utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import trismegistoplanilla.sqlserverdao.SqlserverDAOFactory;

public class DateTimeServer {

  public static String getDateTimeServer() {
    String datetime = "";
    String base = "planillabd";
    String sql = "select format(getdate(), 'dd/MM/yyyy HH:mm:ss') + ' ' + right(convert(varchar, getdate(), 0), 2) fechaServer";
    System.out.println("SQL getDateTimeServer: " + sql);
    Connection conexion = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      conexion = SqlserverDAOFactory.obtenerConexion(base);
      ps = conexion.prepareStatement(sql);
      rs = ps.executeQuery();
      rs.next();
      datetime = rs.getString("fechaServer");
    } catch (SQLException e) {
      System.out.println("ERROR AL OBTENER FECHA Y HORA DEL SERVIDOR " + e.getMessage());
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
    return datetime;
  }

//  public static void main(String[] args) {
//    DateTimeServer metodo = new DateTimeServer();
//    System.out.println(metodo.getDateTimeServer());
//  }
}
