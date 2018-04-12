package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONObject;
import trismegistoplanilla.beans.AreaCargoBean;
import trismegistoplanilla.beans.AreaCargoTipoPagoBean;
import trismegistoplanilla.beans.TipoPagoBean;
import trismegistoplanilla.dao.TipoPagoDAO;
import trismegistoplanilla.utilities.ResponseHelper;

public class TipoPagoSqlserverDAO implements TipoPagoDAO {

  @Override
  public JSONObject listarTipoPago(AreaCargoBean ac) {
    JSONObject JOListarTipoPago = null;
    JSONArray JAListarTipoPago = new JSONArray();
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = ""
            + "select "
            + "tipo_pago.codigo_tipo_pago codigoTipoPago, "
            + "tipo_pago.nombre "
            + "from area_cargo_tipo_pago "
            + "inner join dbo.tipo_pago ON dbo.tipo_pago.codigo_tipo_pago = dbo.area_cargo_tipo_pago.codigo_tipo_pago "
            + "where area_cargo_tipo_pago.codigo_area_cargo = ? and area_cargo_tipo_pago.estado_registro = 1";
    System.out.println("SQL listarTipoPago: " + sql);
    Connection conexion = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      conexion = SqlserverDAOFactory.obtenerConexion(base);
      ps = conexion.prepareStatement(sql);
      ps.setInt(1, ac.getCodigoAreaCargo());
      rs = ps.executeQuery();
      while (rs.next()) {
        TipoPagoBean tp = new TipoPagoBean();
        tp.setCodigoTipoPago(rs.getInt("codigoTipoPago"));
        tp.setNombre(rs.getString("nombre"));
        JSONObject objTipoPago = new JSONObject(tp);
        JAListarTipoPago.put(objTipoPago);
      }
      JSONObject objTipoPago = new JSONObject();
      objTipoPago.put("tipoPagos", JAListarTipoPago);
      response.setStatus(true);
      response.setMessage("Los tipos de pagos se listaron correctamente");
      response.setData(objTipoPago);
    } catch (SQLException e) {
      System.err.println("listarTipoPago -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
    JOListarTipoPago = new JSONObject(response);
    return JOListarTipoPago;
  }

  @Override
  public JSONObject validarExistenciaTipoPago(AreaCargoTipoPagoBean actp) {
    JSONObject JObjectValidarExistenciaTipoPago = null;
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = ""
            + "select "
            + "count(1) exiteTipoPago "
            + "from area_cargo_tipo_pago "
            + "inner join dbo.tipo_pago ON dbo.tipo_pago.codigo_tipo_pago = dbo.area_cargo_tipo_pago.codigo_tipo_pago "
            + "where area_cargo_tipo_pago.codigo_area_cargo = ? and area_cargo_tipo_pago.codigo_tipo_pago = ? and area_cargo_tipo_pago.estado_registro = 1;";
    System.out.println("SQL validarExistenciaTipoPago: " + sql);
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
      int exiteTipoPago = 0;
      exiteTipoPago = rs.getInt("exiteTipoPago");
      if (exiteTipoPago > 0) {
        response.setStatus(true);
        response.setMessage("El tipo de pago seleccionada existe");
      } else {
        response.setStatus(false);
        response.setMessage("Error! El tipo de pago seleccionada no existe");
      }
    } catch (SQLException e) {
      System.err.println("validarExistenciaTipoPago -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
    JObjectValidarExistenciaTipoPago = new JSONObject(response);
    return JObjectValidarExistenciaTipoPago;
  }

}
