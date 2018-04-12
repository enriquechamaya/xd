package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONObject;
import trismegistoplanilla.beans.EstadoFichaBean;
import trismegistoplanilla.dao.EstadoFichaDAO;
import trismegistoplanilla.utilities.ResponseHelper;

public class EstadoFichaSqlserverDAO implements EstadoFichaDAO {

  @Override
  public JSONObject obtenerCodigoEstadoFicha(EstadoFichaBean ef) {
    JSONObject JOObtenerCodigoEstadoFicha = null;
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = ""
            + "select "
            + "codigo_estado_ficha codigoEstadoFicha "
            + "from estado_ficha "
            + "where codigo_ficha = ? and estado_registro = 1";
    System.out.println("SQL obtenerCodigoEstadoFicha: " + sql);
    Connection conexion = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      conexion = SqlserverDAOFactory.obtenerConexion(base);
      ps = conexion.prepareStatement(sql);
      ps.setInt(1, ef.getCodigoFicha());
      rs = ps.executeQuery();
      rs.next();
      int codigoEstadoFicha = rs.getInt("codigoEstadoFicha");
      if (codigoEstadoFicha > 0) {
        JSONObject objEstadoFicha = new JSONObject();
        objEstadoFicha.put("getResultedKey", codigoEstadoFicha);
        response.setStatus(true);
        response.setMessage("Se encontró el codigo de estado de ficha");
        response.setData(objEstadoFicha);
      } else {
        response.setStatus(false);
      }
    } catch (SQLException e) {
      System.err.println("obtenerCodigoEstadoFicha -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
    JOObtenerCodigoEstadoFicha = new JSONObject(response);
    return JOObtenerCodigoEstadoFicha;
  }

  @Override
  public JSONObject desactivarEstadoFicha(EstadoFichaBean ef) {
    JSONObject JODesactivarEstadoFicha = null;
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = ""
            + "update estado_ficha "
            + "set estado_registro = 0 "
            + "where codigo_estado_ficha = ? and estado_registro = 1";
    System.out.println("SQL desactivarEstadoFicha: " + sql);
    Connection conexion = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      conexion = SqlserverDAOFactory.obtenerConexion(base);
      conexion.setAutoCommit(false);
      ps = conexion.prepareStatement(sql);
      ps.setInt(1, ef.getCodigoEstadoFicha());
      int resultadoDesactivar = ps.executeUpdate();
      if (resultadoDesactivar > 0) {
        response.setStatus(true);
        response.setMessage("El estado de ficha [" + ef.getCodigoEstadoFicha() + "] se ha desactivado");
        conexion.commit();
      } else {
        response.setStatus(false);
        response.setMessage("Error!, no se ha podido desactivar el estado de ficha");
        conexion.rollback();
      }
    } catch (SQLException e) {
      System.err.println("desactivarEstadoFicha -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
    JODesactivarEstadoFicha = new JSONObject(response);
    return JODesactivarEstadoFicha;
  }

  @Override
  public JSONObject registrarEstadoFicha(EstadoFichaBean ef) {
    JSONObject JORegistrarEstadoFicha = null;
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = ""
            + "insert into estado_ficha ( "
            + "   codigo_ficha "
            + "  ,codigo_tipo_estado_ficha "
            + "  ,fecha_registro "
            + "  ,codigo_usuario "
            + "  ,estado_registro "
            + ") values ( "
            + "   ? "
            + "  ,? "
            + "  ,getdate() "
            + "  ,? "
            + "  ,1 "
            + ")";
    System.out.println("SQL registrarEstadoFicha: " + sql);
    Connection conexion = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      conexion = SqlserverDAOFactory.obtenerConexion(base);
      conexion.setAutoCommit(false);
      ps = conexion.prepareStatement(sql);
      ps.setInt(1, ef.getCodigoFicha());
      ps.setInt(2, ef.getCodigoTipoEstadoFicha());
      ps.setInt(3, ef.getCodigoUsuario());
      int resultadoRegistro = ps.executeUpdate();
      if (resultadoRegistro > 0) {
        response.setStatus(true);
        response.setMessage("Enhorabuena!, se ha registrado un nuevo estado de ficha");
        conexion.commit();
      } else {
        response.setStatus(false);
        response.setMessage("Error!, no se ha podido regsitrar un nuevo estado de ficha");
        conexion.rollback();
      }
    } catch (SQLException e) {
      System.err.println("registrarEstadoFicha -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
    JORegistrarEstadoFicha = new JSONObject(response);
    return JORegistrarEstadoFicha;
  }

//  public static void main(String[] args) {
//    EstadoFichaSqlServerDAO metodo = new EstadoFichaSqlServerDAO();
//    EstadoFichaBean ef = new EstadoFichaBean();
//    ef.setCodigoFicha(4);
//    ef.setCodigoTipoEstadoFicha(4);
//    ef.setCodigoUsuario(5);
//    System.out.println(metodo.obtenerCodigoEstadoFicha(ef));
//  }
}
