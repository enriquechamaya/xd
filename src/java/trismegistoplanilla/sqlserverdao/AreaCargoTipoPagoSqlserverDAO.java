package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONObject;
import trismegistoplanilla.beans.AreaCargoTipoPagoBean;
import trismegistoplanilla.dao.AreaCargoTipoPagoDAO;
import trismegistoplanilla.utilities.ResponseHelper;

public class AreaCargoTipoPagoSqlserverDAO implements AreaCargoTipoPagoDAO {

  @Override
  public JSONObject obtenerAreaCargoTipoPago(AreaCargoTipoPagoBean actp) {
    JSONObject JOObtenerAreaCargoTipoPago = null;
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = ""
            + "select "
            + "codigo_area_cargo_tipo_pago codigoAreaCargoTipoPago "
            + "from area_cargo_tipo_pago "
            + "where codigo_area_cargo = ? and codigo_tipo_pago = ? and estado_registro = 1";
    System.out.println("SQL obtenerAreaCargoTipoPago: " + sql);
    Connection conexion = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      conexion = SqlserverDAOFactory.obtenerConexion(base);
      ps = conexion.prepareStatement(sql);
      ps.setInt(1, actp.getCodigoAreaCargo());
      ps.setInt(2, actp.getCodigoTipoPago());
      rs = ps.executeQuery();
      rs.next();
      int codigoAreaCargoTipoPago = rs.getInt("codigoAreaCargoTipoPago");
      if (codigoAreaCargoTipoPago > 0) {
        JSONObject objAreaCargo = new JSONObject();
        objAreaCargo.put("getResultedKey", codigoAreaCargoTipoPago);
        response.setStatus(true);
        response.setMessage("Se encontró el código");
        response.setData(objAreaCargo);
      } else {
        response.setStatus(false);
        response.setMessage("Error! no se ha podido encontrar el código area cargo");
      }
    } catch (SQLException e) {
      System.err.println("obtenerAreaCargoTipoPago -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
    JOObtenerAreaCargoTipoPago = new JSONObject(response);
    return JOObtenerAreaCargoTipoPago;
  }

}
