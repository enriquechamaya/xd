package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONObject;
import trismegistoplanilla.beans.TokenFichaBean;
import trismegistoplanilla.dao.TokenFichaDAO;
import trismegistoplanilla.utilities.ResponseHelper;

public class TokenFichaSqlserverDAO implements TokenFichaDAO {

  @Override
  public JSONObject validarTokenURL(TokenFichaBean tf) {
    JSONObject jsonObjValidarTokenURL = null;
    ResponseHelper response = new ResponseHelper();
    int tokenValidoURL = 0;
    String base = "planillabd";
    String sql = ""
            + "select "
            + "count(1) tokenValidoURL "
            + "from token_ficha "
            + "where codigo_token_ficha = ? and "
            + "token = ? ";
    System.out.println("validarTokenURL: " + sql);
    Connection conexion = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      conexion = SqlserverDAOFactory.obtenerConexion(base);
      ps = conexion.prepareStatement(sql);
      ps.setInt(1, tf.getCodigoTokenFicha());
      ps.setString(2, tf.getToken());
      rs = ps.executeQuery();
      rs.next();
      tokenValidoURL = rs.getInt("tokenValidoURL");
      if (tokenValidoURL > 0) {
        response.setStatus(true);
        response.setMessage("El token es válido");
      } else {
        response.setStatus(false);
        // error token no valido
        response.setMessage("El token no existe o ha caducado");
      }
    } catch (SQLException e) {
      System.err.println("validarToken -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
    jsonObjValidarTokenURL = new JSONObject(response);
    return jsonObjValidarTokenURL;
  }

  @Override
  public JSONObject validarToken(TokenFichaBean tf) {
    JSONObject jsonObjValidarToken = null;
    ResponseHelper response = new ResponseHelper();
    int tokenValido = 0;
    String base = "planillabd";
    String sql = ""
            + "select "
            + "count(1) tokenValido "
            + "from token_ficha "
            + "where "
            + "codigo_token_ficha = ? and "
            + "token = ? and "
            + "fecha_expiracion >= getdate() and  "
            + "estado_registro = 1";
    System.out.println("validarToken: " + sql);
    Connection conexion = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      conexion = SqlserverDAOFactory.obtenerConexion(base);
      ps = conexion.prepareStatement(sql);
      ps.setInt(1, tf.getCodigoTokenFicha());
      ps.setString(2, tf.getToken());
      rs = ps.executeQuery();
      rs.next();
      tokenValido = rs.getInt("tokenValido");
      if (tokenValido > 0) {
        response.setStatus(true);
        response.setMessage("El token es válido");
      } else {
        response.setStatus(false);
        response.setMessage("Error, el token no existe o esta inhabilitado");
      }
    } catch (SQLException e) {
      System.err.println("validarToken -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
    jsonObjValidarToken = new JSONObject(response);
    return jsonObjValidarToken;
  }

  @Override
  public JSONObject desactivarToken(TokenFichaBean tf) {
    JSONObject jsonObjDesactivarToken = null;
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = ""
            + "update token_ficha "
            + "set estado_registro = 0 "
            + "where codigo_token_ficha = ? and estado_registro = 1";
    System.out.println("desactivarToken: " + sql);
    Connection conexion = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      conexion = SqlserverDAOFactory.obtenerConexion(base);
      conexion.setAutoCommit(false);
      ps = conexion.prepareStatement(sql);
      ps.setInt(1, tf.getCodigoTokenFicha());
      int desactivarToken = ps.executeUpdate();
      if (desactivarToken > 0) {
        conexion.commit();
        response.setStatus(true);
        response.setMessage("La token se ha desactivado con éxito.");
      } else {
        response.setStatus(false);
        response.setMessage("Error al desactivar el token, no se ha encontrado ningún registro.");
        conexion.rollback();
      }
    } catch (SQLException e) {
      System.err.println("desactivarToken -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
    jsonObjDesactivarToken = new JSONObject(response);
    return jsonObjDesactivarToken;
  }

//    public static void main(String[] args) {
//        TokenFichaSqlserverDAO metodo = new TokenFichaSqlserverDAO();
//        TokenFichaBean tf = new TokenFichaBean();
//        tf.setCodigoTokenFicha(1);
//        tf.setToken("b59c2fa1-8ffd-4a82-ba0d-2871884406e4");
//        System.out.println(metodo.validarTokenURL(tf));
//    }
}
