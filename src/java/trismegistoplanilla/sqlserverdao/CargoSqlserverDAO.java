package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONObject;
import trismegistoplanilla.beans.AreaBean;
import trismegistoplanilla.beans.CargoBean;
import trismegistoplanilla.dao.CargoDAO;
import trismegistoplanilla.utilities.ResponseHelper;

public class CargoSqlserverDAO implements CargoDAO {

  @Override
  public JSONObject listarCargo(AreaBean a) {
    JSONObject JOListarCargo = null;
    JSONArray JArrayCargo = new JSONArray();
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = ""
      + "select "
      + "area_cargo.codigo_cargo codigoCargo, "
      + "cargo.nombre "
      + "from area_cargo "
      + "inner join dbo.cargo ON dbo.cargo.codigo_cargo = dbo.area_cargo.codigo_cargo "
      + "where area_cargo.estado_registro = 1 and area_cargo.codigo_area = ?";
    System.out.println("SQL listarCargo: " + sql);
    Connection conexion = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      conexion = SqlserverDAOFactory.obtenerConexion(base);
      ps = conexion.prepareStatement(sql);
      ps.setInt(1, a.getCodigoArea());
      rs = ps.executeQuery();
      while (rs.next()) {
        CargoBean c = new CargoBean();
        c.setCodigoCargo(rs.getInt("codigoCargo"));
        c.setNombre(rs.getString("nombre"));
        JSONObject objCargo = new JSONObject(c);
        JArrayCargo.put(objCargo);
      }
      JSONObject objCargo = new JSONObject();
      objCargo.put("cargos", JArrayCargo);
      response.setStatus(true);
      response.setMessage("Los cargos se listaron correctamente");
      response.setData(objCargo);
    } catch (SQLException e) {
      System.err.println("listarCargo -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
    JOListarCargo = new JSONObject(response);
    return JOListarCargo;
  }

  @Override
  public JSONObject validarExistenciaCargo(AreaBean a, CargoBean c) {
    JSONObject JOValidarExistenciaCargo = null;
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = ""
      + "select "
      + "count(1) existeCargo "
      + "from area_cargo "
      + "where area_cargo.estado_registro = 1 and area_cargo.codigo_area = ? and area_cargo.codigo_cargo = ?";
    System.out.println("SQL validarExistenciaCargo: " + sql);
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
      int existeCargo = 0;
      existeCargo = rs.getInt("existeCargo");
      if (existeCargo > 0) {
        response.setStatus(true);
        response.setMessage("El cargo seleccionada existe");
      } else {
        response.setStatus(false);
        response.setMessage("Error! El cargo seleccionada no existe");
      }
    } catch (SQLException e) {
      System.err.println("validarExistenciaCargo -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
    JOValidarExistenciaCargo = new JSONObject(response);
    return JOValidarExistenciaCargo;
  }

}
