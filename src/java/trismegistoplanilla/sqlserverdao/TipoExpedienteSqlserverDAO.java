package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.json.JSONArray;
import org.json.JSONObject;
import trismegistoplanilla.beans.TipoExpedienteBean;
import trismegistoplanilla.dao.TipoExpedienteDAO;
import trismegistoplanilla.utilities.ResponseHelper;

public class TipoExpedienteSqlserverDAO implements TipoExpedienteDAO {

  @Override
  public JSONObject listarTipoExpediente() {
    JSONObject JOlistarTipoExpediente = null;
    JSONArray JArrayArea = new JSONArray();
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = ""
            + "select "
            + "codigo_tipo_expediente codigoTipoExpediente, "
            + "nombre, "
            + "estado_registro estado "
            + "from tipo_expediente";
    System.out.println("SQL listarTipoExpediente: " + sql);
    Connection conexion = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      conexion = SqlserverDAOFactory.obtenerConexion(base);
      ps = conexion.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        TipoExpedienteBean te = new TipoExpedienteBean();
        te.setCodigoTipoExpediente(rs.getInt("codigoTipoExpediente"));
        te.setNombre(rs.getString("nombre"));
        JSONObject obj = new JSONObject(te);
        JArrayArea.put(obj);
      }
      JSONObject obj = new JSONObject();
      obj.put("tipoexpedientes", JArrayArea);
      response.setStatus(true);
      response.setMessage("Los tipo de expediente se listaron correctamente");
      response.setData(obj);
    } catch (SQLException e) {
      System.err.println("listarTipoExpediente -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
    JOlistarTipoExpediente = new JSONObject(response);
    return JOlistarTipoExpediente;
  }

  @Override
  public JSONObject registrarTipoExpediente(TipoExpedienteBean te) {
    JSONObject JOregistrarTipoExpediente = null;
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = "insert into tipo_expediente (nombre, estado_registro) values (upper(?) ,1)";
    System.out.println("SQL registrarTipoExpediente: " + sql);
    Connection conexion = SqlserverDAOFactory.obtenerConexion(base);
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      conexion.setAutoCommit(false);
      // validar existencia de tipo de expediente
      JSONObject validarExistenciaTipoExpediente = validarExistenciaTipoExpediente(te);
      if (validarExistenciaTipoExpediente.getBoolean("status")) {
        response.setStatus(false);
        response.setMessage("El tipo de expediente ingresado, ya existe en el sistema");
        conexion.rollback();
      } else {
        ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, te.getNombre());
        int resultadoRegistroTipoExpediente = ps.executeUpdate();
        if (resultadoRegistroTipoExpediente > 0) {
          rs = ps.getGeneratedKeys();
          rs.next();
          int codigoTipoExpediente = rs.getInt(1);
          JSONObject objTipoExpediente = new JSONObject();
          objTipoExpediente.put("id", codigoTipoExpediente);
          objTipoExpediente.put("data", te.getNombre());
          response.setStatus(true);
          response.setMessage("El tipo de expediente ha sido registrado con exito");
          response.setData(objTipoExpediente);
          conexion.commit();
        } else {
          response.setStatus(false);
          response.setMessage("Error: no se pudo registrar el tipo de expediente ingresado");
          conexion.rollback();
        }
      }
    } catch (SQLException e) {
      System.err.println("listarTipoExpediente -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
      response.setStatus(false);
      response.setMessage("Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
      try {
        conexion.rollback();
      } catch (SQLException ex) {
        System.out.println("Error roolback registrarTipoExpediente: " + ex.getMessage());
      }
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
    JOregistrarTipoExpediente = new JSONObject(response);
    return JOregistrarTipoExpediente;
  }

  @Override
  public JSONObject validarExistenciaTipoExpediente(TipoExpedienteBean te) {
    JSONObject JOvalidarExistenciaTipoExpediente = null;
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = "select count(1) existe from tipo_expediente where nombre = upper(?)";
    System.out.println("SQL validarExistenciaTipoExpediente: " + sql);
    Connection conexion = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      conexion = SqlserverDAOFactory.obtenerConexion(base);
      ps = conexion.prepareStatement(sql);
      ps.setString(1, te.getNombre());
      rs = ps.executeQuery();
      rs.next();
      int existeTipoExpediente = rs.getInt("existe");
      if (existeTipoExpediente > 0) {
        response.setStatus(true);
      } else {
        response.setStatus(false);
      }
    } catch (SQLException e) {
      System.err.println("listarTipoExpediente -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
    JOvalidarExistenciaTipoExpediente = new JSONObject(response);
    return JOvalidarExistenciaTipoExpediente;
  }

  @Override
  public JSONObject validarTipoExpedienteSeleccionadoByID(JSONArray ja) {
    JSONObject JOvalidarExistenciaTipoExpediente = null;
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = ""
            + "select "
            + "count(1) existe "
            + "from tipo_expediente "
            + "where codigo_tipo_expediente = ?";
    System.out.println("SQL validarTipoExpedienteSeleccionadoByID: " + sql);
    Connection conexion = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      conexion = SqlserverDAOFactory.obtenerConexion(base);
      ps = conexion.prepareStatement(sql);
      int existe = 0;
      for (int i = 0; i < ja.length(); i++) {
        JSONObject obj = ja.getJSONObject(i);
        ps.setInt(1, obj.getInt("id"));
        rs = ps.executeQuery();
        rs.next();
        existe = rs.getInt("existe");
        if (existe == 0) {
          break;
        }
      }
      if (existe > 0) {
        response.setStatus(true);
      } else {
        response.setStatus(false);
        response.setMessage("Al parecer ha seleccionado un tipo de expediente que no se encuentra registrardo, por favor seleccione un elemento correcto y vuelva a intentarlo");
      }
    } catch (SQLException e) {
      System.err.println("validarTipoExpedienteSeleccionadoByID -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
    JOvalidarExistenciaTipoExpediente = new JSONObject(response);
    return JOvalidarExistenciaTipoExpediente;
  }

//  public static void main(String[] args) {
//    TipoExpedienteSqlserverDAO metodo = new TipoExpedienteSqlserverDAO();
//    TipoExpedienteBean te = new TipoExpedienteBean();
//    JSONArray ja = new JSONArray();
//    for (int i = 1; i <= 5; i++) {
//      HashMap<String, Integer> x = new HashMap<>();
//      x.put("id", i);
//      JSONObject obj = new JSONObject(x);
//      ja.put(obj);
//    }
//    System.out.println(ja);
//    System.out.println(metodo.validarTipoExpedienteSeleccionadoByID(ja));
//  }
}
