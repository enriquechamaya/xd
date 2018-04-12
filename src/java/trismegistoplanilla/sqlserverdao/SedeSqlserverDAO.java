package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONObject;
import trismegistoplanilla.beans.SedeBean;
import trismegistoplanilla.dao.SedeDAO;
import trismegistoplanilla.utilities.ResponseHelper;

public class SedeSqlserverDAO implements SedeDAO {

  @Override
  public JSONObject listarSede() {
    JSONObject JObjectSede = null;
    JSONArray jArraySede = new JSONArray();
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = ""
            + "select "
            + "sede_area.codigo_sede codigoSede, "
            + "sede.nombre nombre, "
            + "sede.direccion direccion, "
            + "razon_social.codigo_razon_social codigoRazonSocial, "
            + "razon_social.abreviatura nombreRazonSocial "
            + "from sede_area "
            + "inner join dbo.sede ON dbo.sede.codigo_sede = dbo.sede_area.codigo_sede "
            + "inner join dbo.razon_social ON dbo.razon_social.codigo_razon_social = dbo.sede.codigo_razon_social "
            + "where sede_area.estado_registro = 1 and sede.estado_registro = 1 "
            + "group by sede_area.codigo_sede, sede.nombre, sede.direccion, razon_social.codigo_razon_social, razon_social.abreviatura, sede.estado_registro";
    System.out.println("SQL listarSede: " + sql);
    Connection conexion = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      conexion = SqlserverDAOFactory.obtenerConexion(base);
      ps = conexion.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        SedeBean s = new SedeBean();
        s.setCodigoSede(rs.getInt("codigoSede"));
        s.setNombre(rs.getString("nombre"));
        s.setDireccion(rs.getString("direccion"));
        s.setCodigoRazonSocial(rs.getInt("codigoRazonSocial"));
        s.setNombreRazonSocial(rs.getString("nombreRazonSocial"));
        JSONObject objSede = new JSONObject(s);
        jArraySede.put(objSede);
      }
      JSONObject objSede = new JSONObject();
      objSede.put("sedes", jArraySede);

      response.setStatus(true);
      response.setMessage("Las sedes se listaron correctamente");
      response.setData(objSede);
    } catch (SQLException e) {
      System.err.println("listarSede -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
    JObjectSede = new JSONObject(response);
    return JObjectSede;
  }

  @Override
  public JSONObject validarExistenciaSede(SedeBean s) {
    JSONObject JObjectValidarExistenciaSede = null;
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = ""
            + "select "
            + "count(1) existeSede "
            + "from sede_area "
            + "where sede_area.estado_registro = 1 and sede_area.codigo_sede = ?";
    System.out.println("SQL validarExistenciaSede: " + sql);
    Connection conexion = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      conexion = SqlserverDAOFactory.obtenerConexion(base);
      ps = conexion.prepareStatement(sql);
      ps.setInt(1, s.getCodigoSede());
      rs = ps.executeQuery();
      rs.next();
      int existeSede = 0;
      existeSede = rs.getInt("existeSede");
      if (existeSede > 0) {
        response.setStatus(true);
        response.setMessage("La sede seleccionada existe");
      } else {
        response.setStatus(false);
        response.setMessage("Error! La sede seleccionada no existe");
      }
    } catch (SQLException e) {
      System.err.println("validarExistenciaSede -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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

    JObjectValidarExistenciaSede = new JSONObject(response);
    return JObjectValidarExistenciaSede;
  }

//  public static void main(String[] args) {
//    SedeSqlserverDAO metodo = new SedeSqlserverDAO();
//    SedeBean s = new SedeBean();
//    s.setCodigoSede(300);
//    System.out.println(metodo.validarExistenciaSede(s));
//  }
}
