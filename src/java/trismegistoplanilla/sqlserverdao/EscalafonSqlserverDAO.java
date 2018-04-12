package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONObject;
import trismegistoplanilla.beans.AreaCargoTipoPagoBean;
import trismegistoplanilla.dao.EscalafonDAO;
import static trismegistoplanilla.utilities.CurrencyFormat.getCustomCurrency;
import trismegistoplanilla.utilities.ResponseHelper;

public class EscalafonSqlserverDAO implements EscalafonDAO {
  
  @Override
  public JSONObject obtenerEscalafonAreaCargoTipoPago(AreaCargoTipoPagoBean actp) {
    JSONObject jsonObtenerEscalafonAreaCargo = null;
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = ""
            + "select "
            + "sueldo "
            + "from escalafon "
            + "where codigo_area_cargo_tipo_pago = ? and estado_registro = 1";
    System.out.println("SQL obtenerEscalafonAreaCargo: " + sql);
    Connection cnx = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sueldo = "";
    try {
      cnx = SqlserverDAOFactory.obtenerConexion(base);
      ps = cnx.prepareStatement(sql);
      ps.setInt(1, actp.getCodigoAreaCargoTipoPago());
      rs = ps.executeQuery();
      if (rs.next()) {
        sueldo = getCustomCurrency(rs.getDouble("sueldo"));
        response.setStatus(true);
        JSONObject jsonObjSueldo = new JSONObject();
        jsonObjSueldo.put("sueldo", sueldo);
        response.setData(jsonObjSueldo);
      }
    } catch (SQLException e) {
      System.err.println("obtenerEscalafonAreaCargoTipoPago -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
        if (cnx != null) {
          cnx.close();
        }
      } catch (SQLException e) {
        System.err.println("Error: ha ocurrido un error al intentar cerrar las conexiones y/o liberacion de recursos -> " + e.getMessage());
      }
    }
    
    jsonObtenerEscalafonAreaCargo = new JSONObject(response);
    return jsonObtenerEscalafonAreaCargo;
  }

//  public static void main(String[] args) {
//    EscalafonSqlserverDAO metodo = new EscalafonSqlserverDAO();
//    AreaCargoTipoPagoBean actp = new AreaCargoTipoPagoBean();
//    actp.setCodigoAreaCargoTipoPago(1);
//    System.out.println(metodo.obtenerEscalafonAreaCargoTipoPago(actp));
//  }
}
