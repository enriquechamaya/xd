package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.json.JSONArray;
import org.json.JSONObject;
import trismegistoplanilla.beans.Correo;
import trismegistoplanilla.beans.EstadoFichaBean;
import trismegistoplanilla.beans.PersonaBean;
import trismegistoplanilla.beans.TokenFichaBean;
import trismegistoplanilla.dao.PersonaDAO;
import trismegistoplanilla.utilities.EmailDesign;
import trismegistoplanilla.utilities.EncryptAction;
import trismegistoplanilla.utilities.GenerateCodeVerification;
import trismegistoplanilla.utilities.ResponseHelper;

public class PersonaSqlserverDAO implements PersonaDAO {

  @Override
  public JSONObject validarExistenciaNumeroDocumento(PersonaBean p) {
    JSONObject jsonObjValidarExistenciaNumeroDocumento = null;
    ResponseHelper response = new ResponseHelper();
    int existeNumeroDocumento = 0;
    String base = "planillabd";
    String sql = ""
            + "select "
            + "count(1) existeNumeroDocumento "
            + "from persona "
            + "where estado_registro = 1 and codigo_tipo_documento = ? and numero_documento = ?";
    System.out.println("validarExistenciaNumeroDocumento: " + sql);
    Connection conexion = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      conexion = SqlserverDAOFactory.obtenerConexion(base);
      ps = conexion.prepareStatement(sql);
      ps.setInt(1, p.getCodigoTipoDocumento());
      ps.setString(2, p.getNumeroDocumento());
      rs = ps.executeQuery();
      rs.next();
      existeNumeroDocumento = rs.getInt("existeNumeroDocumento");
      if (existeNumeroDocumento > 0) {
        response.setStatus(false);
        response.setMessage("Error, el número de documento que acaba de ingresar, ya se encuentra registrado en el sistema.");
      } else if (existeNumeroDocumento == 0) {
        response.setStatus(true);
        response.setMessage("El número de documento puede ser registrado.");
      }
    } catch (SQLException e) {
      System.err.println("validarExistenciaNumeroDocumento -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
    jsonObjValidarExistenciaNumeroDocumento = new JSONObject(response);
    return jsonObjValidarExistenciaNumeroDocumento;
  }

  @Override
  public JSONObject validarExistenciaCorreoElectronico(PersonaBean p) {
    JSONObject jsonObjValidarExistenciaCorreoElectronico = null;
    ResponseHelper response = new ResponseHelper();
    int existeCorreoElectronico = 0;
    String base = "planillabd";
    String sql = ""
            + "select "
            + "count(1) existeCorreoElectronico "
            + "from persona "
            + "where estado_registro = 1 and correo = ?";
    System.out.println("validarExistenciaNumeroDocumento: " + sql);
    Connection conexion = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      conexion = SqlserverDAOFactory.obtenerConexion(base);
      ps = conexion.prepareStatement(sql);
      ps.setString(1, p.getCorreo());
      rs = ps.executeQuery();
      rs.next();
      existeCorreoElectronico = rs.getInt("existeCorreoElectronico");
      if (existeCorreoElectronico > 0) {
        response.setStatus(false);
        response.setMessage("Error, el correo electrónico que acaba de ingresar, ya se encuentra registrado en el sistema.");
      } else if (existeCorreoElectronico == 0) {
        response.setStatus(true);
        response.setMessage("El correo electrónico puede ser registrado.");
      }
    } catch (SQLException e) {
      System.err.println("validarExistenciaCorreoElectronico -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
    jsonObjValidarExistenciaCorreoElectronico = new JSONObject(response);
    return jsonObjValidarExistenciaCorreoElectronico;
  }

  @Override
  public JSONObject registrarPersona(PersonaBean p, EstadoFichaBean ef) {
    JSONObject jsonObjRegistrarPersona = null;
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sqlPersona = ""
            + "insert into persona "
            + "(apellido_paterno, "
            + "apellido_materno, "
            + "nombre, "
            + "codigo_tipo_documento, "
            + "numero_documento, "
            + "correo, "
            + "is_default_mail, "
            + "estado_registro) "
            + "values "
            + "("
            + "upper(rtrim(ltrim(?))), "
            + "upper(rtrim(ltrim(?))), "
            + "upper(rtrim(ltrim(?))), "
            + "upper(rtrim(ltrim(?))), "
            + "upper(rtrim(ltrim(?))), "
            + "upper(rtrim(ltrim(?))), "
            + "upper(rtrim(ltrim(?))), "
            + "1)";
    System.out.println("registrarPersona: " + sqlPersona);
    Connection conexion = null;
    PreparedStatement psPersona = null, psFicha = null, psEstadoFicha = null, psTokenFicha = null, psObtenerToken = null, psObtenerCodigoFicha = null;
    ResultSet rsPersona = null, rsFicha = null, rsEstadoFicha = null, rsTokenFicha = null, rsObtenerToken = null, rsObtenerCodigoFicha = null;
    try {
      conexion = SqlserverDAOFactory.obtenerConexion(base);
      conexion.setAutoCommit(false);
      psPersona = conexion.prepareStatement(sqlPersona, Statement.RETURN_GENERATED_KEYS);
      psPersona.setString(1, p.getApellidoPaterno());
      psPersona.setString(2, p.getApellidoMaterno());
      psPersona.setString(3, p.getNombre());
      psPersona.setInt(4, p.getCodigoTipoDocumento());
      psPersona.setString(5, p.getNumeroDocumento());
      psPersona.setString(6, p.getCorreo());
      psPersona.setInt(7, p.getIsDefaultMail());
      int resultRegistrarPersona = psPersona.executeUpdate();
      System.out.println("resultRegistrarPersona: " + resultRegistrarPersona);
      if (resultRegistrarPersona > 0) {
        rsPersona = psPersona.getGeneratedKeys();
        rsPersona.next();
        int codigoPersona = rsPersona.getInt(1);
        System.out.println("codigoPersona: " + codigoPersona);
        System.out.println("***************************************");
        /*-************ REGISTRAR FICHA **************-*/
        String sqlFicha = ""
                + "insert into ficha ( "
                + "codigo_persona "
                + ",estado_registro "
                + ") "
                + "values ( "
                + "? "
                + ",1 "
                + ")";
        System.out.println("registrarFicha: " + sqlFicha);
        conexion.setAutoCommit(false);
        psFicha = conexion.prepareStatement(sqlFicha, Statement.RETURN_GENERATED_KEYS);
        psFicha.setInt(1, codigoPersona);
        int resultRegistrarFicha = psFicha.executeUpdate();
        System.out.println("resultRegistrarFicha: " + resultRegistrarFicha);
        if (resultRegistrarFicha > 0) {
          rsFicha = psFicha.getGeneratedKeys();
          rsFicha.next();
          int codigoFicha = rsFicha.getInt(1);
          System.out.println("codigoFicha: " + codigoFicha);
          System.out.println("***************************************");
          /*-************ REGISTRAR ESTADO DE FICHA **************-*/
          String sqlEstadoFicha = ""
                  + "insert into estado_ficha ( "
                  + "codigo_ficha "
                  + ",codigo_tipo_estado_ficha "
                  + ",fecha_registro "
                  + ",codigo_usuario "
                  + ",estado_registro "
                  + ") VALUES ( "
                  + "?, "
                  + "1, "
                  + "getdate(), "
                  + "?, "
                  + "1 "
                  + ")";
          System.out.println("registrarEstadoFicha: " + sqlEstadoFicha);
          conexion.setAutoCommit(false);
          psEstadoFicha = conexion.prepareStatement(sqlEstadoFicha);
          psEstadoFicha.setInt(1, codigoFicha);
          psEstadoFicha.setInt(2, ef.getCodigoUsuario());
          int resultRegistrarEstadoFicha = psEstadoFicha.executeUpdate();
          System.out.println("resultRegistrarEstadoFicha: " + resultRegistrarEstadoFicha);
          if (resultRegistrarEstadoFicha > 0) {
            System.out.println("***************************************");
            /*-************ REGISTRAR TOKEN DE FICHA **************-*/
            String sqlTokenFicha = ""
                    + "insert into token_ficha "
                    + " ( "
                    + "codigo_ficha "
                    + ",codigo_verificacion "
                    + ",fecha_creacion "
                    + ",fecha_expiracion "
                    + ",token "
                    + ",estado_registro "
                    + ") "
                    + "values ( "
                    + "? "
                    + ",? "
                    + ",getdate() "
                    + ",dateadd(dd, 1, getdate()) "
                    + ",lower(newid()) "
                    + ",1 "
                    + ")";
            System.out.println("registrarTokenFicha: " + sqlTokenFicha);
            String CodeVerification = GenerateCodeVerification.randomString(6); // almacenar el codigo de verificacion a enviar
            conexion.setAutoCommit(false);
            psTokenFicha = conexion.prepareStatement(sqlTokenFicha, Statement.RETURN_GENERATED_KEYS);
            psTokenFicha.setInt(1, codigoFicha);
            psTokenFicha.setString(2, CodeVerification);
            int resultRegistrarTokenFicha = psTokenFicha.executeUpdate();
            System.out.println("resultRegistrarTokenFicha: " + resultRegistrarTokenFicha);
            if (resultRegistrarTokenFicha > 0) {
              rsTokenFicha = psTokenFicha.getGeneratedKeys();
              rsTokenFicha.next();
              int codigoTokenFicha = rsTokenFicha.getInt(1);
              System.out.println("codigoTokenFicha: " + codigoTokenFicha);
              System.out.println("***************************************");
              /*-************ OBTENER TOKEN FICHA **************-*/
              String sqlGetToken = ""
                      + "select "
                      + "token "
                      + "from token_ficha "
                      + "where codigo_token_ficha = ? and estado_registro = 1";
              System.out.println("obtenerToken: " + sqlGetToken);
              conexion.setAutoCommit(false);
              psObtenerToken = conexion.prepareStatement(sqlGetToken);
              psObtenerToken.setInt(1, codigoTokenFicha);
              rsObtenerToken = psObtenerToken.executeQuery();
              rsObtenerToken.next();
              String token = rsObtenerToken.getString("token");
              System.out.println("token: " + token);
              System.out.println("***************************************");
              /*-************ ENVIAR CORREO ELECT. **************-*/
              System.out.println("aqui");
              Correo c = new Correo();
              c.setDestino(p.getCorreo());
              c.setAsunto("BIENVENIDO A TRISMEGISTO PLANILLA");
              c.setMensaje(EmailDesign.correoDesign(CodeVerification, "http://172.16.2.20:8080/trismegisto-planilla/TokenFichaServlet?accion=" + EncryptAction.Encriptar("validarTokenFicha", "TR1SM3G1ST0-PL4N1LL4") + "&id=" + EncryptAction.Encriptar(String.valueOf(codigoTokenFicha), "TR1SM3G1ST0-ID-PL4N1LL4") + "&token=" + token));
              CorreoSqlserverDAO correo = new CorreoSqlserverDAO();
              if (correo.enviarCorreo(c)) {
                response.setStatus(true);
                response.setMessage("La ficha se ha registrado con éxito. Hemos enviado un correo electrónico el cuál permitirá proceder con el registro, gracias.");
                conexion.commit();
              } else {
                response.setStatus(false);
                response.setMessage("Ha ocurrido un error, no se ha podido registrar la ficha. Por favor contactarse con el área de sistemas, gracias.");
                conexion.rollback();
              }
            } else {
              conexion.rollback();
            }
          } else {
            conexion.rollback();
          }
        } else {
          conexion.rollback();
        }
      } else {
        conexion.rollback();
      }
    } catch (SQLException e) {
      System.err.println("registrarPersona -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
      response.setStatus(false);
      response.setMessage("Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
    } catch (Exception e) {
      System.err.println("registrarPersona -> Error: " + e.getMessage());
      response.setStatus(false);
      response.setMessage("Error: " + e.getMessage());
    } finally {
      try {

        if (rsPersona != null) {
          rsPersona.close();
        }
        if (rsFicha != null) {
          rsFicha.close();
        }
        if (rsEstadoFicha != null) {
          rsEstadoFicha.close();
        }
        if (rsTokenFicha != null) {
          rsTokenFicha.close();
        }
        if (rsObtenerCodigoFicha != null) {
          rsObtenerCodigoFicha.close();
        }

        if (psPersona != null) {
          psPersona.close();
        }
        if (psFicha != null) {
          psFicha.close();
        }
        if (psEstadoFicha != null) {
          psEstadoFicha.close();
        }
        if (psTokenFicha != null) {
          psTokenFicha.close();
        }
        if (psObtenerCodigoFicha != null) {
          psObtenerCodigoFicha.close();
        }

        if (conexion != null) {
          conexion.close();
        }

      } catch (SQLException e) {
        System.err.println("Error: ha ocurrido un error al intentar cerrar las conexiones y/o liberacion de recursos -> " + e.getMessage());
      }
    }
    jsonObjRegistrarPersona = new JSONObject(response);
    return jsonObjRegistrarPersona;
  }

  @Override
  public JSONObject verificarPersona(TokenFichaBean tf, PersonaBean p) {
    JSONObject jsonObjVerificarPersona = null;
    ResponseHelper response = new ResponseHelper();
    int verificacionPersona = 0;
    String base = "planillabd";
    String sql = ""
            + "select "
            + "count(1) verificacionPersona "
            + "from token_ficha "
            + "inner join dbo.ficha ON dbo.ficha.codigo_ficha = dbo.token_ficha.codigo_ficha "
            + "inner join dbo.persona ON dbo.persona.codigo_persona = dbo.ficha.codigo_persona "
            + "where token_ficha.codigo_token_ficha = ? and "
            + "token_ficha.token = ? and "
            + "token_ficha.codigo_verificacion = ? and "
            + "persona.numero_documento = ? and "
            + "persona.estado_registro = 1 and "
            + "ficha.estado_registro = 1 and "
            + "token_ficha.estado_registro = 1";
    Connection conexion = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      conexion = SqlserverDAOFactory.obtenerConexion(base);
      ps = conexion.prepareStatement(sql);
      ps.setInt(1, tf.getCodigoTokenFicha());
      ps.setString(2, tf.getToken());
      ps.setString(3, tf.getCodigoVerificacion());
      ps.setString(4, p.getNumeroDocumento());
      rs = ps.executeQuery();
      rs.next();
      verificacionPersona = rs.getInt("verificacionPersona");
      System.out.println("verificacionPersona: " + verificacionPersona);
      if (verificacionPersona > 0) {
        response.setStatus(true);
        response.setMessage("Enhorabuena!.");
      } else {
        response.setStatus(false);
        response.setMessage("Error, las credenciales ingresadas son incorrectas, asegúrese de haber escrito bien e intentelo de nuevo.");
      }
    } catch (SQLException e) {
      System.err.println("verificarPersona -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
    jsonObjVerificarPersona = new JSONObject(response);
    return jsonObjVerificarPersona;
  }

  @Override
  public JSONObject modificarCorreoFichaPersona(PersonaBean p) {
    JSONObject jsonObjModificarCorreoFichaPersona = null;
    ResponseHelper response = new ResponseHelper();
    int actualizaCorreo = 0;
    String base = "planillabd";
    String sql = ""
            + "update persona "
            + "set correo = ? "
            + "where codigo_persona = ? and estado_registro = 1";
    System.out.println("modificarCorreoFichaPersona: " + sql);
    Connection conexion = null;
    PreparedStatement psUpdatePersona = null;
    ResultSet rsUpdatePersona = null;
    try {
      conexion = SqlserverDAOFactory.obtenerConexion(base);
      conexion.setAutoCommit(false);
      psUpdatePersona = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      psUpdatePersona.setString(1, p.getCorreo());
      psUpdatePersona.setInt(2, p.getCodigoPersona());
      actualizaCorreo = psUpdatePersona.executeUpdate();
      if (actualizaCorreo > 0) {
        response.setStatus(true);
        response.setMessage("Enhorabuena!, los datos fueron actualizados con éxito.");
        conexion.commit();
      } else {
        response.setStatus(false);
        response.setMessage("Error, no se pudo actualizar el correo.");
        conexion.rollback();
      }
    } catch (SQLException e) {
      System.err.println("modificarCorreoFichaPersona -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
      response.setStatus(false);
      response.setMessage("Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
    } finally {
      try {
        if (rsUpdatePersona != null) {
          rsUpdatePersona.close();
        }
        if (psUpdatePersona != null) {
          psUpdatePersona.close();
        }
        if (conexion != null) {
          conexion.close();
        }
      } catch (SQLException e) {
        System.err.println("Error: ha ocurrido un error al intentar cerrar las conexiones y/o liberacion de recursos -> " + e.getMessage());
      }
    }
    jsonObjModificarCorreoFichaPersona = new JSONObject(response);
    return jsonObjModificarCorreoFichaPersona;
  }

  @Override
  public JSONObject obtenerCodigoPersona(TokenFichaBean tf) {
    JSONObject jsonObjObtenerCodigoPersona = null;
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = ""
            + "select "
            + "persona.codigo_persona codigoPersona "
            + "from persona "
            + "inner join dbo.ficha ON dbo.ficha.codigo_persona = dbo.persona.codigo_persona "
            + "inner join dbo.token_ficha ON dbo.token_ficha.codigo_ficha = dbo.ficha.codigo_ficha "
            + "where token_ficha.codigo_token_ficha = ? and token_ficha.token = ? and "
            + "persona.estado_registro = 1 and "
            + "ficha.estado_registro = 1 and "
            + "token_ficha.estado_registro = 1";
    System.out.println("obtenerCodigoPersona: " + sql);
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
      int codigoPersona = rs.getInt("codigoPersona");
      JSONObject objPersona = new JSONObject();
      objPersona.put("getResultedKey", codigoPersona);
      if (codigoPersona > 0) {
        response.setStatus(true);
        response.setMessage("Enhorabuena!, persona identificada");
        response.setData(objPersona);
      } else {
        response.setStatus(false);
        response.setMessage("Error: no se encontró la identificación de la persona");
      }
    } catch (SQLException e) {
      System.err.println("obtenerCodigoPersona -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
    jsonObjObtenerCodigoPersona = new JSONObject(response);
    return jsonObjObtenerCodigoPersona;
  }

  @Override
  public JSONObject obtenerPersonaSession(PersonaBean p) {
    JSONObject jsonObjObtenerPersonaSession = null;
    JSONArray jsonArrObtenerPersonaSession = new JSONArray();
    JSONObject objPersona = null;
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = ""
            + "select "
            + "persona.codigo_persona codigoPersona, "
            + "tipo_documento.codigo_tipo_documento codigoTipoDocumento, "
            + "tipo_documento.descripcion_corta nombreTipoDocumento, "
            + "persona.numero_documento numeroDocumento, "
            + "persona.apellido_paterno apellidoPaterno, "
            + "persona.apellido_materno apellidoMaterno, "
            + "persona.nombre nombre, "
            + "persona.correo "
            + "from persona "
            + "inner join dbo.tipo_documento ON dbo.tipo_documento.codigo_tipo_documento = dbo.persona.codigo_tipo_documento "
            + "where persona.codigo_persona = ? and "
            + "persona.estado_registro = 1  and "
            + "tipo_documento.codigo_tipo_documento = ? and "
            + "tipo_documento.estado_registro = 1";
    System.out.println("SQL obtenerPersonaSession: " + sql);
    Connection conexion = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      conexion = SqlserverDAOFactory.obtenerConexion(base);
      ps = conexion.prepareStatement(sql);
      ps.setInt(1, p.getCodigoPersona());
      ps.setInt(2, p.getCodigoTipoDocumento());
      rs = ps.executeQuery();
      while (rs.next()) {
        p = new PersonaBean();
        p.setCodigoPersona(rs.getInt("codigoPersona"));
        p.setCodigoTipoDocumento(rs.getInt("codigoTipoDocumento"));
        p.setNombreTipoDocumento(rs.getString("nombreTipoDocumento"));
        p.setNumeroDocumento(rs.getString("numeroDocumento"));
        p.setApellidoPaterno(rs.getString("apellidoPaterno"));
        p.setApellidoMaterno(rs.getString("apellidoMaterno"));
        p.setNombre(rs.getString("nombre"));
        p.setCorreo(rs.getString("correo"));
        JSONObject jsonobjPersona = new JSONObject(p);
        jsonArrObtenerPersonaSession.put(jsonobjPersona);
      }
      objPersona = new JSONObject();
      objPersona.put("personas", jsonArrObtenerPersonaSession);
      response.setStatus(true);
      response.setMessage("Enhorabuena!, los datos de la persona se han listado correctamente");
      response.setData(objPersona);
    } catch (SQLException e) {
      System.err.println("obtenerCodigoPersona -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
    jsonObjObtenerPersonaSession = new JSONObject(response);
    return jsonObjObtenerPersonaSession;
  }

//  public static void main(String[] args) {
//    PersonaSqlserverDAO metodo = new PersonaSqlserverDAO();
//    PersonaBean p = new PersonaBean();
//    p.setCodigoTipoDocumento(2);
//    p.setNumeroDocumento("AD64A6SD456A");
//    System.out.println(metodo.validarExistenciaNumeroDocumento(p));
//  }
}
