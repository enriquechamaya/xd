package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import trismegistoplanilla.beans.AreaBean;
import trismegistoplanilla.beans.SedeBean;
import trismegistoplanilla.dao.AreaDAO;
import trismegistoplanilla.utilities.ResponseHelper;

public class AreaSqlserverDAO implements AreaDAO {

  @Override
  public JSONObject listarArea(SedeBean s) {
    JSONObject JObjectArea = null;
    JSONArray JArrayArea = new JSONArray();
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = ""
      + "select "
      + "sede_area.codigo_area codigoArea, "
      + "area.nombre nombre "
      + "from sede_area "
      + "inner join dbo.area ON dbo.area.codigo_area = dbo.sede_area.codigo_area "
      + "where sede_area.estado_registro = 1 and area.estado_registro = 1 and sede_area.codigo_sede = ? "
      + "group by sede_area.codigo_area, area.nombre";
    System.out.println("SQL listarArea: " + sql);
    Connection conexion = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      conexion = SqlserverDAOFactory.obtenerConexion(base);
      ps = conexion.prepareStatement(sql);
      ps.setInt(1, s.getCodigoSede());
      rs = ps.executeQuery();
      while (rs.next()) {
        AreaBean a = new AreaBean();
        a.setCodigoArea(rs.getInt("codigoArea"));
        a.setNombre(rs.getString("nombre"));
        JSONObject objArea = new JSONObject(a);
        JArrayArea.put(objArea);
      }
      JSONObject objArea = new JSONObject();
      objArea.put("areas", JArrayArea);
      response.setStatus(true);
      response.setMessage("Las áreas se listaron correctamente");
      response.setData(objArea);
    } catch (SQLException e) {
      System.err.println("listarArea -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
    JObjectArea = new JSONObject(response);
    return JObjectArea;
  }

  @Override
  public JSONObject validarExistenciaArea(SedeBean s, AreaBean a) {
    JSONObject JObjectValidarExistenciaArea = null;
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = ""
      + "select "
      + "count(1) existeArea "
      + "from sede_area "
      + "where sede_area.estado_registro = 1 and sede_area.codigo_sede = ? and sede_area.codigo_area = ?";
    System.out.println("SQL validarExistenciaArea: " + sql);
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
      int existeArea = 0;
      existeArea = rs.getInt("existeArea");
      if (existeArea > 0) {
        response.setStatus(true);
        response.setMessage("La área seleccionada existe");
      } else {
        response.setStatus(false);
        response.setMessage("Error! La área seleccionada no existe");
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
    JObjectValidarExistenciaArea = new JSONObject(response);
    return JObjectValidarExistenciaArea;
  }

//  public static void main(String[] args) {
//    AreaSqlServerDAO metodo = new AreaSqlServerDAO();
//    SedeBean s = new SedeBean();
//    s.setCodigoSede(30);
//    AreaBean a = new AreaBean();
//    a.setCodigoArea(2);
//    System.out.println(metodo.validarExistenciaArea(s, a));
//    JSONObject obj = new JSONObject();
//    obj.put("message", "no existe");
//    obj.put("status", false);
//    System.out.println(obj);
//  }
}
