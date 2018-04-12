package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import pe.siso.webservicesseguridad.webservices.UsuarioBean;
import trismegistoplanilla.beans.Correo;
import trismegistoplanilla.beans.DetalleEstadoFichaBean;
import trismegistoplanilla.beans.FichaBean;
import trismegistoplanilla.beans.ListadoFichaBean;
import trismegistoplanilla.beans.PersonaBean;
import trismegistoplanilla.beans.ReporteBean;
import trismegistoplanilla.beans.TokenFichaBean;
import trismegistoplanilla.beans.DatosAdministrativosBean;
import trismegistoplanilla.beans.DetalleFichaLoteBean;
import trismegistoplanilla.beans.EstadoFichaBean;
import trismegistoplanilla.beans.LoteFichaBean;
import trismegistoplanilla.beans.ObservacionFichaBean;
import trismegistoplanilla.dao.FichaDAO;
import trismegistoplanilla.utilities.CurrencyFormat;
import trismegistoplanilla.utilities.EmailDesign;
import trismegistoplanilla.utilities.EncryptAction;
import trismegistoplanilla.utilities.GenerateCodeVerification;
import trismegistoplanilla.utilities.ResponseHelper;

public class FichaSqlserverDAO implements FichaDAO {

  @Override
  public JSONObject listarFichaDT(String draw, String length, String start, String search, int tipoDocumento, int criterio, UsuarioBean u) {
    JSONObject jsonReturn = new JSONObject();
    JSONArray jsonArrayListarFichaDT = new JSONArray();
    PreparedStatement ps = null;
    ResultSet rs = null;
    Connection conexion = null;
    String base = "planillabd";
    String condicion = "";
    String condicionPorUsuario = "";
    String condicionCantidad = "";
    int numeroFilas = Integer.parseInt(start) + 1;

    switch (criterio) {
      case 1:
        // combo tipo documento
        condicion = "and tipodocumento.codigo_tipo_documento = " + tipoDocumento + " and persona.numero_documento = '" + search + "' ";
        break;
      case 2:
        // input apellido
        condicion = "and persona.apellido_paterno like '" + search + "%' ";
        break;
      case 3:
        // fecha registro
        condicion = "and (select top 1 format(x.fecha_registro,'dd/MM/yyyy') from estado_ficha x where x.codigo_ficha = ficha.codigo_ficha) = '" + search + "' ";
        break;
      default:
        condicion = "";
        break;
    }

    if (u.getCodigoProyectoDetalle() == 5) {
      condicionPorUsuario = " ";
    } else {
//      condicionPorUsuario = " and estadoficha.codigo_usuario in( " + u.getCodigoUsuario() + ", 0)";
      condicionPorUsuario = " and ficha.codigo_ficha in (select ef.codigo_ficha from estado_ficha ef where ef.codigo_usuario = " + u.getCodigoUsuario() + " and ef.codigo_tipo_estado_ficha not in (13))";
    }

    try {
      conexion = SqlserverDAOFactory.obtenerConexion(base);

      String sql = ""
              + "select top " + length + " "
              + "  ficha.codigo_ficha codigoficha, "
              + "  persona.codigo_persona codigopersona, "
              + "  tipodocumento.descripcion_larga tipodocumentodescripcionlarga, "
              + "  tipodocumento.descripcion_corta tipodocumentodescripcioncorta, "
              + "  persona.numero_documento numerodocumentopersona, "
              + "  case "
              + "    when persona.apellido_paterno = '' and "
              + "      persona.apellido_materno = '' and "
              + "      persona.nombre = '' then '-' "
              + "    when persona.apellido_paterno is null or "
              + "      persona.apellido_materno is null or "
              + "      persona.nombre is null then '-' "
              + "    else persona.apellido_paterno + ' ' + persona.apellido_materno + ', ' + persona.nombre "
              + "  end personal, "
              + "  persona.apellido_paterno apellidopaternopersona, "
              + "  persona.apellido_materno apellidomaternopersona, "
              + "  persona.nombre nombrepersona, "
              + "  persona.correo correopersona, "
              + "  format(estadoficha.fecha_registro, 'dd/MM/yyyy') + ' ' + stuff(replace(right(convert(varchar(19), estadoficha.fecha_registro, 0), 7), ' ', '0'), 6, 0, ' ') fecharegistroficha, "
              + "  tipoestadoficha.nombre estadoficha, "
              + "  tipodocumento.codigo_tipo_documento codigotipodocumento, "
              + "  '<span class=\"'+tipoestadoficha.estilo+'\">'+tipoestadoficha.nombre+'</span>' estilo "
              + "from ficha ficha "
              + "inner join estado_ficha estadoficha on ficha.codigo_ficha = estadoficha.codigo_ficha "
              + "inner join tipo_estado_ficha tipoestadoficha on tipoestadoficha.codigo_tipo_estado_ficha = estadoficha.codigo_tipo_estado_ficha "
              + "inner join persona persona on persona.codigo_persona = ficha.codigo_persona "
              + "inner join tipo_documento tipodocumento on tipodocumento.codigo_tipo_documento = persona.codigo_tipo_documento "
              + "where estadoficha.estado_registro = 1 "
              + "and ficha.codigo_ficha not in (select top " + start + " "
              + "  x.codigo_ficha "
              + "from ficha x "
              + "order by 1 desc) "
              + condicion + condicionPorUsuario
              + "order by 1 desc";
      System.out.println("listarFichaDT => " + sql);
      ps = conexion.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        ListadoFichaBean listadoFichaBean = new ListadoFichaBean();
        listadoFichaBean.setItem(numeroFilas++);
        listadoFichaBean.setCodigoFicha(rs.getInt("codigoFicha"));
        listadoFichaBean.setCodigoPersona(rs.getInt("codigoPersona"));
        listadoFichaBean.setDescripcionLargaTipoDocumento(rs.getString("tipoDocumentoDescripcionLarga"));
        listadoFichaBean.setDescripcionCortaTipoDocumento(rs.getString("tipoDocumentoDescripcionCorta"));
        listadoFichaBean.setNumeroDocumento(rs.getString("numeroDocumentoPersona"));
        listadoFichaBean.setApellidoPaterno(rs.getString("apellidoPaternoPersona"));
        listadoFichaBean.setApellidoMaterno(rs.getString("apellidoMaternoPersona"));
        listadoFichaBean.setNombre(rs.getString("nombrePersona"));
        listadoFichaBean.setPersonal(rs.getString("personal"));
        listadoFichaBean.setCorreo(rs.getString("correoPersona"));
        listadoFichaBean.setFechaRegistro(rs.getString("fechaRegistroFicha"));
        listadoFichaBean.setEstadoFicha(rs.getString("estadoFicha"));
        listadoFichaBean.setCodigoTipoDocumento(rs.getInt("codigoTipoDocumento"));
        listadoFichaBean.setEstilo(rs.getString("estilo"));
        JSONObject jsonObjListadoFicha = new JSONObject(listadoFichaBean);
        jsonArrayListarFichaDT.put(jsonObjListadoFicha);
      }
      int cantidadFichas = 0;
      if (!condicion.equals("")) {
        condicionCantidad = "and " + condicion.substring(3);
      }

      String sqlFilas
              = "select count(1) cantidadFichas "
              + "from ficha "
              + "inner join estado_ficha estadoficha on ficha.codigo_ficha = estadoficha.codigo_ficha "
              + "inner join persona persona on persona.codigo_persona = ficha.codigo_persona "
              + "inner join tipo_documento tipodocumento on tipodocumento.codigo_tipo_documento = persona.codigo_tipo_documento "
              + "where estadoficha.estado_registro = 1 " + condicionCantidad + condicionPorUsuario;
      System.out.println("SQL filas => " + sqlFilas);
      PreparedStatement psFilas = conexion.prepareStatement(sqlFilas);
      ResultSet rsFilas = psFilas.executeQuery();
      rsFilas.next();
      cantidadFichas = rsFilas.getInt("cantidadFichas");

      jsonReturn.put("data", jsonArrayListarFichaDT);
      jsonReturn.put("recordsFiltered", cantidadFichas);
      jsonReturn.put("recordsTotal", cantidadFichas);
      jsonReturn.put("draw", draw);
      System.out.println(jsonReturn);

    } catch (SQLException e) {
      jsonReturn.put("Error", "Error inesperado -> " + e.getMessage() + " [" + e.getErrorCode() + "]");
      System.err.println(e.getMessage());
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
        jsonReturn.put("Error", "Error inesperado -> " + e.getMessage() + " [" + e.getErrorCode() + "]");
        System.err.println(e.getMessage());
      }
    }
    return jsonReturn;
  }

  @Override
  public JSONObject obtenerCodigoFicha(TokenFichaBean tf) {
    JSONObject jsonObjObtenerCodigoFicha = null;
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = ""
            + "select "
            + "codigo_ficha codigoFicha "
            + "from token_ficha "
            + "where codigo_token_ficha = ?";
    Connection conexion = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      conexion = SqlserverDAOFactory.obtenerConexion(base);
      ps = conexion.prepareStatement(sql);
      ps.setInt(1, tf.getCodigoTokenFicha());
      rs = ps.executeQuery();
      rs.next();
      int codigoFicha = rs.getInt("codigoFicha");
      JSONObject objFicha = new JSONObject();
      objFicha.put("getResultedKey", codigoFicha);
      if (codigoFicha > 0) {
        response.setStatus(true);
        response.setMessage("Enhorabuena!, el codigo de ficha se ha obtenido con éxito");
        response.setData(objFicha);
      } else {
        response.setStatus(false);
        response.setMessage("Error!, no se pudo obtener el codigo de ficha");
      }
    } catch (SQLException e) {
      System.err.println("obtenerCodigoFicha -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
    jsonObjObtenerCodigoFicha = new JSONObject(response);
    return jsonObjObtenerCodigoFicha;
  }

  @Override
  public JSONObject registrarFicha(JSONObject data, PersonaBean p, TokenFichaBean tf) {
    System.out.println("=== data ===");
    System.out.println(data);
    System.out.println("=== data ===");
    JSONObject jsonObjRegistrarFicha = null;

    ResponseHelper response = new ResponseHelper();

    String base = "planillabd";

    String sql = "";

    Connection conexion = null;
    PreparedStatement psActualizarPersona = null,
            psInsertarCargaFamiliar = null,
            psInsertarFormacionAcademica = null,
            psInsertarExperienciaLaboral = null,
            psObtenerEstadoFicha = null,
            psActualizarUltimoEstadoFichaActivo = null,
            psRegistrarEstadoFicha = null,
            psDesactivarTokenFicha = null;

    ResultSet rsObtenerEstadoFicha = null;
    try {
      // conectarse a la base de datos
      conexion = SqlserverDAOFactory.obtenerConexion(base);
      conexion.setAutoCommit(false);

      // obtener codigo tipo documento
      int codigoTipoDocumento = p.getCodigoTipoDocumento();
      // comparar cuando sea DNI
      if (codigoTipoDocumento == 1) {
        System.out.println("REGISTRAR FICHA - DNI");
        sql = ""
                + "update persona set "
                + "  sexo = upper(?) "
                + "  ,codigo_estado_civil = upper(?) "
                + "  ,fecha_nacimiento = upper(?) "
                + "  ,codigo_nacionalidad = upper(?) "
                + "  ,codigo_ubigeo_nacimiento = upper(?) "
                + "  ,direccion_documento = upper(?) "
                + "  ,telefono_fijo = upper(?) "
                + "  ,telefono_movil = upper(?) "
                + "  ,correo = upper(?) "
                + "  ,codigo_ubigeo_residencia = upper(?) "
                + "  ,direccion_residencia = upper(?) "
                + "  ,latitud_residencia = upper(?) "
                + "  ,longitud_residencia = upper(?) "
                + "  ,foto = upper(?) "
                + "  ,ruc = upper(?) "
                + "  ,fondo_pension_activo = upper(?) "
                + "  ,codigo_fondo_pension = upper(?) "
                + "where codigo_persona = upper(?) ";
        System.out.println("SQL registrarFicha (DNI): " + sql);
        // preparar query
        psActualizarPersona = conexion.prepareStatement(sql);
        // setear paramteros segun query
        psActualizarPersona.setString(1, data.getString("sexo"));
        psActualizarPersona.setInt(2, data.getInt("codigoEstadoCivil"));
        psActualizarPersona.setString(3, data.getString("fechaNacimiento"));
        psActualizarPersona.setInt(4, data.getInt("codigoNacionalidad"));
        if (data.isNull("codigoUbigeoNacimiento")) {
          psActualizarPersona.setNull(5, Types.INTEGER);
        } else {
          psActualizarPersona.setInt(5, data.getInt("codigoUbigeoNacimiento"));
        }
        psActualizarPersona.setString(6, data.getString("direccionDocumento"));
        if (data.getString("telefonoFijo").equals("")) {
          psActualizarPersona.setNull(7, Types.VARCHAR);
        } else {
          psActualizarPersona.setString(7, data.getString("telefonoFijo"));
        }
        psActualizarPersona.setString(8, data.getString("telefonoMovil"));
        psActualizarPersona.setString(9, data.getString("correo"));
        psActualizarPersona.setInt(10, data.getInt("codigoUbigeoResidencia"));
        psActualizarPersona.setString(11, data.getString("direccionResidencia"));
        psActualizarPersona.setString(12, data.getString("latitudResidencia"));
        psActualizarPersona.setString(13, data.getString("longitudResidencia"));
        psActualizarPersona.setString(14, data.getString("foto"));
        if (data.getString("ruc").equals("")) {
          psActualizarPersona.setNull(15, Types.VARCHAR);
        } else {
          psActualizarPersona.setString(15, data.getString("ruc"));
        }
        psActualizarPersona.setBoolean(16, data.getBoolean("fondoPensionActivo"));
        psActualizarPersona.setInt(17, data.getInt("codigoFondoPension"));
        psActualizarPersona.setInt(18, p.getCodigoPersona());
        // ejecutar query
        int resultadoActualizarPersona = psActualizarPersona.executeUpdate();
        System.out.println("resultadoActualizarPersona: " + resultadoActualizarPersona);
        // validar resultado
        if (resultadoActualizarPersona == 0) {
          response.setStatus(false);
          response.setMessage("Lo sentimos, no se pudo actualizar los datos de la persona");
          conexion.rollback();
        }
      } // compaprar cuando RUC
      else if (codigoTipoDocumento == 3) {
        System.out.println("REGISTRAR FICHA - RUC");
        sql = ""
                + "update persona set "
                + "   apellido_paterno = upper(?) "
                + "  ,apellido_materno = upper(?) "
                + "  ,nombre = upper(?) "
                + "  ,sexo = upper(?) "
                + "  ,codigo_estado_civil = upper(?) "
                + "  ,fecha_nacimiento = upper(?) "
                + "  ,codigo_nacionalidad = upper(?) "
                + "  ,codigo_ubigeo_nacimiento = upper(?) "
                + "  ,direccion_documento = upper(?) "
                + "  ,telefono_fijo = upper(?) "
                + "  ,telefono_movil = upper(?) "
                + "  ,correo = upper(?) "
                + "  ,codigo_ubigeo_residencia = upper(?) "
                + "  ,direccion_residencia = upper(?) "
                + "  ,latitud_residencia = upper(?) "
                + "  ,longitud_residencia = upper(?) "
                + "  ,foto = upper(?) "
                + "  ,ruc = upper(?) "
                + "  ,fondo_pension_activo = upper(?) "
                + "  ,codigo_fondo_pension = upper(?) "
                + "where codigo_persona = upper(?) ";
        System.out.println("SQL registrarFicha (RUC): " + sql);
        // preparar query
        psActualizarPersona = conexion.prepareStatement(sql);
        // setear paramteros segun query
        psActualizarPersona.setString(1, data.getString("apellidoPaterno"));
        psActualizarPersona.setString(2, data.getString("apellidoMaterno"));
        psActualizarPersona.setString(3, data.getString("nombre"));
        psActualizarPersona.setString(4, data.getString("sexo"));
        psActualizarPersona.setInt(5, data.getInt("codigoEstadoCivil"));
        psActualizarPersona.setString(6, data.getString("fechaNacimiento"));
        psActualizarPersona.setInt(7, data.getInt("codigoNacionalidad"));
        if (data.isNull("codigoUbigeoNacimiento")) {
          psActualizarPersona.setNull(8, Types.INTEGER);
        } else {
          psActualizarPersona.setInt(8, data.getInt("codigoUbigeoNacimiento"));
        }
        psActualizarPersona.setString(9, data.getString("direccionDocumento"));
        if (data.getString("telefonoFijo").equals("")) {
          psActualizarPersona.setNull(10, Types.VARCHAR);
        } else {
          psActualizarPersona.setString(10, data.getString("telefonoFijo"));
        }
        psActualizarPersona.setString(11, data.getString("telefonoMovil"));
        psActualizarPersona.setString(12, data.getString("correo"));
        psActualizarPersona.setInt(13, data.getInt("codigoUbigeoResidencia"));
        psActualizarPersona.setString(14, data.getString("direccionResidencia"));
        psActualizarPersona.setString(15, data.getString("latitudResidencia"));
        psActualizarPersona.setString(16, data.getString("longitudResidencia"));
        psActualizarPersona.setString(17, data.getString("foto"));
        psActualizarPersona.setString(18, p.getNumeroDocumento());
        psActualizarPersona.setBoolean(19, data.getBoolean("fondoPensionActivo"));
        psActualizarPersona.setInt(20, data.getInt("codigoFondoPension"));
        psActualizarPersona.setInt(21, p.getCodigoPersona());
        // ejecutar query
        int resultadoActualizarPersona = psActualizarPersona.executeUpdate();
        // validar resultado
        if (resultadoActualizarPersona == 0) {
          response.setStatus(false);
          response.setMessage("Lo sentimos, no se pudo actualizar los datos de la persona");
          conexion.rollback();
        }
      } // comparar cuando no sea DNI ni RUC
      else if (codigoTipoDocumento != 1 && codigoTipoDocumento != 3) {
        System.out.println("REGISTRAR FICHA - OTRO DOC");
        sql = ""
                + "update persona set "
                + "   apellido_paterno = upper(?) "
                + "  ,apellido_materno = upper(?) "
                + "  ,nombre = upper(?) "
                + "  ,sexo = upper(?) "
                + "  ,codigo_estado_civil = upper(?) "
                + "  ,fecha_nacimiento = upper(?) "
                + "  ,codigo_nacionalidad = upper(?) "
                + "  ,codigo_ubigeo_nacimiento = upper(?) "
                + "  ,direccion_documento = upper(?) "
                + "  ,telefono_fijo = upper(?) "
                + "  ,telefono_movil = upper(?) "
                + "  ,correo = upper(?) "
                + "  ,codigo_ubigeo_residencia = upper(?) "
                + "  ,direccion_residencia = upper(?) "
                + "  ,latitud_residencia = upper(?) "
                + "  ,longitud_residencia = upper(?) "
                + "  ,foto = upper(?) "
                + "  ,ruc = upper(?) "
                + "  ,fondo_pension_activo = upper(?) "
                + "  ,codigo_fondo_pension = upper(?) "
                + "where codigo_persona = upper(?) ";
        System.out.println("SQL registrarFicha (OTRO DOC): " + sql);
        // preparar query
        psActualizarPersona = conexion.prepareStatement(sql);
        // setear paramteros segun query
        psActualizarPersona.setString(1, data.getString("apellidoPaterno"));
        psActualizarPersona.setString(2, data.getString("apellidoMaterno"));
        psActualizarPersona.setString(3, data.getString("nombre"));
        psActualizarPersona.setString(4, data.getString("sexo"));
        psActualizarPersona.setInt(5, data.getInt("codigoEstadoCivil"));
        psActualizarPersona.setString(6, data.getString("fechaNacimiento"));
        psActualizarPersona.setInt(7, data.getInt("codigoNacionalidad"));
        if (data.isNull("codigoUbigeoNacimiento")) {
          psActualizarPersona.setNull(8, Types.INTEGER);
        } else {
          psActualizarPersona.setInt(8, data.getInt("codigoUbigeoNacimiento"));
        }
        psActualizarPersona.setString(9, data.getString("direccionDocumento"));
        if (data.getString("telefonoFijo").equals("")) {
          psActualizarPersona.setNull(10, Types.VARCHAR);
        } else {
          psActualizarPersona.setString(10, data.getString("telefonoFijo"));
        }
        psActualizarPersona.setString(11, data.getString("telefonoMovil"));
        psActualizarPersona.setString(12, data.getString("correo"));
        psActualizarPersona.setInt(13, data.getInt("codigoUbigeoResidencia"));
        psActualizarPersona.setString(14, data.getString("direccionResidencia"));
        psActualizarPersona.setString(15, data.getString("latitudResidencia"));
        psActualizarPersona.setString(16, data.getString("longitudResidencia"));
        psActualizarPersona.setString(17, data.getString("foto"));
        if (data.getString("ruc").equals("")) {
          psActualizarPersona.setNull(18, Types.VARCHAR);
        } else {
          psActualizarPersona.setString(18, data.getString("ruc"));
        }
        psActualizarPersona.setBoolean(19, data.getBoolean("fondoPensionActivo"));
        psActualizarPersona.setInt(20, data.getInt("codigoFondoPension"));
        psActualizarPersona.setInt(21, p.getCodigoPersona());
        // ejecutar query
        int resultadoActualizarPersona = psActualizarPersona.executeUpdate();
        // validar resultado
        if (resultadoActualizarPersona == 0) {
          response.setStatus(false);
          response.setMessage("Lo sentimos, no se pudo actualizar los datos de la persona");
          conexion.rollback();
        }
      }

      System.out.println("REGISTRAR FICHA - DATOS FAMILIARES");
      // REGISTRAR DATOS FAMILIARES
      sql = "insert into carga_familiar ( "
              + "   codigo_persona "
              + "  ,apellido_paterno "
              + "  ,apellido_materno "
              + "  ,nombre "
              + "  ,codigo_parentesco "
              + "  ,fecha_nacimiento "
              + "  ,codigo_tipo_documento "
              + "  ,numero_documento "
              + "  ,telefono "
              + "  ,sexo"
              + "  ,estado_registro "
              + ") VALUES ( "
              + "   upper(?) "
              + "  ,upper(?) "
              + "  ,upper(?) "
              + "  ,upper(?) "
              + "  ,upper(?) "
              + "  ,upper(?) "
              + "  ,upper(?) "
              + "  ,upper(?) "
              + "  ,upper(?) "
              + "  ,upper(?) "
              + "  ,1 "
              + ")";
      System.out.println("SQL registrarFicha - Carga Familiar: " + sql);
      // preparar query
      psInsertarCargaFamiliar = conexion.prepareStatement(sql);
      // obtener longitud del array carga familiar
      int longitudCargarFamiliar = data.getJSONArray("cargaFamiliar").length();
      // obtener data de array carga familiar
      JSONArray jsonArrayCargaFamiliar = data.getJSONArray("cargaFamiliar");
      for (int i = 0; i < longitudCargarFamiliar; i++) {
        psInsertarCargaFamiliar.setInt(1, p.getCodigoPersona());
        psInsertarCargaFamiliar.setString(2, jsonArrayCargaFamiliar.getJSONObject(i).getString("apellidoPaterno"));
        psInsertarCargaFamiliar.setString(3, jsonArrayCargaFamiliar.getJSONObject(i).getString("apellidoMaterno"));
        psInsertarCargaFamiliar.setString(4, jsonArrayCargaFamiliar.getJSONObject(i).getString("nombre"));
        psInsertarCargaFamiliar.setInt(5, jsonArrayCargaFamiliar.getJSONObject(i).getInt("codigoParentesco"));
        psInsertarCargaFamiliar.setString(6, jsonArrayCargaFamiliar.getJSONObject(i).getString("fechaNacimiento"));
        psInsertarCargaFamiliar.setInt(7, jsonArrayCargaFamiliar.getJSONObject(i).getInt("codigoTipoDocumento"));
        psInsertarCargaFamiliar.setString(8, jsonArrayCargaFamiliar.getJSONObject(i).getString("numeroDocumento"));
        if (jsonArrayCargaFamiliar.getJSONObject(i).getString("telefono").equals("")) {
          psInsertarCargaFamiliar.setNull(9, Types.VARCHAR);
        } else {
          psInsertarCargaFamiliar.setString(9, jsonArrayCargaFamiliar.getJSONObject(i).getString("telefono"));
        }
        psInsertarCargaFamiliar.setString(10, jsonArrayCargaFamiliar.getJSONObject(i).getString("sexo"));
        psInsertarCargaFamiliar.addBatch();
      }
      // ejecutar query
      int resultadoInsertarCargaFamiliar[] = psInsertarCargaFamiliar.executeBatch();
      // obtener total de registros insertados a la bd
      int totalRegistrosCargarFamilia = resultadoInsertarCargaFamiliar.length;
      // validar resultado carga familair
      if (totalRegistrosCargarFamilia == 0) {
        response.setStatus(false);
        response.setMessage("Lo sentimos, no se pudo registrar la carga familiar");
        conexion.rollback();
      }

      System.out.println("REGISTRAR FICHA - FORMACION ACADEMICA");
      // REGISTRAR FORMACION ACADEMICA
      sql = ""
              + "insert into formacion_academica ( "
              + "   codigo_persona "
              + "  ,nombre_centro_estudio "
              + "  ,carrera_profesional "
              + "  ,sector_institucion "
              + "  ,numero_colegiatura "
              + "  ,codigo_carrera_profesional "
              + "  ,codigo_nivel_estado "
              + "  ,fecha_inicio "
              + "  ,fecha_fin "
              + "  ,documento_adjunto "
              + "  ,observacion "
              + "  ,estado_registro "
              + ") values ( "
              + "   upper(?) "
              + "   upper(?) "
              + "  ,upper(?) "
              + "  ,upper(?) "
              + "  ,upper(?) "
              + "  ,upper(?) "
              + "  ,upper(?) "
              + "  ,upper(?) "
              + "  ,upper(?) "
              + "  ,null "
              + "  ,null "
              + "  ,1 "
              + ")";
      System.out.println("SQL registrarFicha - Formacion Academica: " + sql);
      // preparar query
      psInsertarFormacionAcademica = conexion.prepareStatement(sql);
      // obtener longitud del array carga familiar
      int longitudFormacionAcademica = data.getJSONArray("formacionAcademica").length();
      // obtener data de array carga familiar
      JSONArray jsonArrayFormacionAcademica = data.getJSONArray("formacionAcademica");

      for (int i = 0; i < longitudFormacionAcademica; i++) {
        int contador = 1;
        psInsertarFormacionAcademica.setInt(contador++, p.getCodigoPersona());
        psInsertarFormacionAcademica.setString(contador++, jsonArrayFormacionAcademica.getJSONObject(i).getString("centroEstudios"));
        if (jsonArrayFormacionAcademica.getJSONObject(i).getString("carreraProfesional").equals("")) {
          psInsertarFormacionAcademica.setString(contador++, "NINGUNO");
        } else {
          psInsertarFormacionAcademica.setString(contador++, jsonArrayFormacionAcademica.getJSONObject(i).getString("carreraProfesional"));
        }
        psInsertarFormacionAcademica.setString(contador++, jsonArrayFormacionAcademica.getJSONObject(i).getString("sectorInstitucion"));

        if (jsonArrayFormacionAcademica.getJSONObject(i).getInt("codigoCarreraProfesional") == 0) {
          psInsertarFormacionAcademica.setInt(contador++, 404);
        } else {
          psInsertarFormacionAcademica.setInt(contador++, jsonArrayFormacionAcademica.getJSONObject(i).getInt("codigoCarreraProfesional"));
        }
        psInsertarFormacionAcademica.setInt(contador++, jsonArrayFormacionAcademica.getJSONObject(i).getInt("codigoNivelEstado"));
        psInsertarFormacionAcademica.setString(contador++, jsonArrayFormacionAcademica.getJSONObject(i).getString("fechaInicio"));
        psInsertarFormacionAcademica.setString(contador++, jsonArrayFormacionAcademica.getJSONObject(i).getString("fechaFin"));
        psInsertarFormacionAcademica.addBatch();
      }
      // ejecutar query
      int resultadoInsertarFormacionAcademica[] = psInsertarFormacionAcademica.executeBatch();
      // obtener total de registros insertados a la bd
      int totalRegistrosFormacionAcademica = resultadoInsertarFormacionAcademica.length;
      // validar resultado carga familair
      if (totalRegistrosFormacionAcademica == 0) {
        response.setStatus(false);
        response.setMessage("Lo sentimos, no se pudo registrar la formación académica");
        conexion.rollback();
      }

      System.out.println("REGISTRAR FICHA - EXPERIENCIA LABORAL");
      // REGISTRAR EXPERIENCIA LABORAL
      sql = ""
              + "insert into experiencia_laboral ( "
              + "   codigo_persona "
              + "  ,nombre_empresa "
              + "  ,nombre_cargo "
              + "  ,fecha_inicio "
              + "  ,fecha_fin "
              + "  ,telefono "
              + "  ,estado_registro "
              + ") VALUES ( "
              + "   upper(?) "
              + "  ,upper(?) "
              + "  ,upper(?) "
              + "  ,upper(?) "
              + "  ,upper(?) "
              + "  ,upper(?) "
              + "  ,1 "
              + ")";
      System.out.println("SQL registrarFicha - Experiencia Laboral: " + sql);
      // preparar query
      psInsertarExperienciaLaboral = conexion.prepareStatement(sql);

      // validar si hay experiencia laboral
      if (data.getBoolean("experienciaLaboralActivo")) {
        System.out.println("=>>>>> es true");
        // obtener longitud del array carga familiar
        int longitudExperienciaLaboral = data.getJSONArray("experienciaLaboral").length();
        // obtener data de array carga familiar
        JSONArray jsonArrayExperienciaLaboral = data.getJSONArray("experienciaLaboral");
        System.out.println("jsonArrayExperienciaLaboral =>>> " + jsonArrayExperienciaLaboral);
        for (int i = 0; i < longitudExperienciaLaboral; i++) {
          psInsertarExperienciaLaboral.setInt(1, p.getCodigoPersona());
          psInsertarExperienciaLaboral.setString(2, jsonArrayExperienciaLaboral.getJSONObject(i).getString("empresa"));
          psInsertarExperienciaLaboral.setString(3, jsonArrayExperienciaLaboral.getJSONObject(i).getString("cargo"));
          psInsertarExperienciaLaboral.setString(4, jsonArrayExperienciaLaboral.getJSONObject(i).getString("fechaInicio"));
          psInsertarExperienciaLaboral.setString(5, jsonArrayExperienciaLaboral.getJSONObject(i).getString("fechaFin"));
          if (jsonArrayExperienciaLaboral.getJSONObject(i).getString("telefono").equals("")) {
            psInsertarExperienciaLaboral.setNull(6, Types.VARCHAR);
          } else {
            psInsertarExperienciaLaboral.setString(6, jsonArrayExperienciaLaboral.getJSONObject(i).getString("telefono"));
          }
          psInsertarExperienciaLaboral.addBatch();
        }
        // ejecutar query
        int resultadoInsertarExperienciaLaboral[] = psInsertarExperienciaLaboral.executeBatch();
        // obtener total de registros insertados a la bd
        int totalRegistrosExperienciaLaboral = resultadoInsertarExperienciaLaboral.length;
        System.out.println("totalRegistrosExperienciaLaboral: " + totalRegistrosExperienciaLaboral);
        // validar resultado experiencia laboral
        if (totalRegistrosExperienciaLaboral == 0) {
          response.setStatus(false);
          response.setMessage("Lo sentimos, no se pudo registrar la experiencia laboral");
          conexion.rollback();
        }
      } else {
        System.out.println("No tiene experiencia laboral");
      }

      // OBTENER ULTIMO ESTADO DE FICHA ACTIVO
      System.out.println("REGISTRAR FICHA - OBTENER ULTIMO ESTADO DE FICHA ACTIVO");
      sql = ""
              + "select "
              + "codigo_estado_ficha codigoEstadoFicha "
              + "from estado_ficha "
              + "where codigo_ficha = ? and estado_registro = 1";
      System.out.println("SQL registrarFicha - Obtener codigo estado ficha activo: " + sql);
      // preparar query
      psObtenerEstadoFicha = conexion.prepareStatement(sql);
      psObtenerEstadoFicha.setInt(1, tf.getCodigoFicha());
      rsObtenerEstadoFicha = psObtenerEstadoFicha.executeQuery();
      rsObtenerEstadoFicha.next();
      // ejecutar metodo
      int codigoEstadoFicha = rsObtenerEstadoFicha.getInt("codigoEstadoFicha");
      // validar resultado de metodo ejecutado
      if (codigoEstadoFicha > 0) {
        System.out.println("REGISTRAR FICHA - ACTUALIZAR ESTADO DE FICHA A CERO");
        sql = ""
                + "update estado_ficha "
                + "set estado_registro = 0 "
                + "where codigo_estado_ficha = ? and estado_registro = 1";
        System.out.println("SQL registrarFicha - Actualizar ultimo estado de ficha activo: " + sql);
        // preparar query
        psActualizarUltimoEstadoFichaActivo = conexion.prepareStatement(sql);
        psActualizarUltimoEstadoFichaActivo.setInt(1, codigoEstadoFicha);
        int resultadoActualizarEstadoFichaActivo = psActualizarUltimoEstadoFichaActivo.executeUpdate();
        if (resultadoActualizarEstadoFichaActivo > 0) {
          System.out.println("REGISTRAR FICHA - REGISTRAR UN NUEVO ESTADO DE FICHA  (REGISTRADO)");
          sql = ""
                  + "insert into estado_ficha ( "
                  + "   codigo_ficha, "
                  + "  codigo_tipo_estado_ficha, "
                  + "  fecha_registro, "
                  + "  codigo_usuario, "
                  + "  estado_registro "
                  + ") values ( "
                  + "  ?, "
                  + "  ?, "
                  + "  getdate(), "
                  + "  0, "
                  + "  1 "
                  + ")";
          System.out.println("SQL registrarFicha - Registrar nuevo estado de ficha (REGISTRADO): " + sql);
          psRegistrarEstadoFicha = conexion.prepareStatement(sql);
          psRegistrarEstadoFicha.setInt(1, tf.getCodigoFicha());
          psRegistrarEstadoFicha.setInt(2, 3);
//          psRegistrarEstadoFicha.setInt(3, p.getCodigoPersona());
          int resultadoRegistrarEstadoFicha = psRegistrarEstadoFicha.executeUpdate();
          if (resultadoRegistrarEstadoFicha > 0) {
            System.out.println("REGISTRAR FICHA - DESACTIVAR EL TOKEN FICHA");
            sql = ""
                    + "update token_ficha "
                    + "set estado_registro = 0 "
                    + "where codigo_token_ficha = ? and estado_registro = 1";
            System.out.println("SQL registrarFicha - Desactivar token ficha: " + sql);
            psDesactivarTokenFicha = conexion.prepareStatement(sql);
            psDesactivarTokenFicha.setInt(1, tf.getCodigoTokenFicha());
            int resultadoDesactivarTokenFicha = psDesactivarTokenFicha.executeUpdate();
            if (resultadoDesactivarTokenFicha > 0) {
              response.setStatus(true);
              response.setMessage("Enhorabuena!, La ficha ha sido registrado con éxito");
              conexion.commit();
            } else {
              response.setStatus(false);
              response.setMessage("Lo sentimos, no se pudo desactivar el token ficha");
              conexion.rollback();
            }
          } else {
            response.setStatus(false);
            response.setMessage("Lo sentimos, no se pudo registrar el estado de ficha");
            conexion.rollback();
          }
        } else {
          response.setStatus(false);
          response.setMessage("Lo sentimos, no se pudo actualizar el ultimo de estado de ficha");
          conexion.rollback();
        }
      } else {
        response.setStatus(false);
        response.setMessage("Lo sentimos, no se pudo obtener el ultimo codigo de estado de ficha");
        conexion.rollback();
      }
    } catch (SQLException e) {
      System.err.println("registrarFicha -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
      response.setStatus(false);
      response.setMessage("Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
    } finally {
      try {
        if (rsObtenerEstadoFicha != null) {
          rsObtenerEstadoFicha.close();
        }
        if (psActualizarPersona != null) {
          psActualizarPersona.close();
        }
        if (psInsertarCargaFamiliar != null) {
          psInsertarCargaFamiliar.close();
        }
        if (psInsertarFormacionAcademica != null) {
          psInsertarFormacionAcademica.close();
        }
        if (psInsertarExperienciaLaboral != null) {
          psInsertarExperienciaLaboral.close();
        }
        if (psObtenerEstadoFicha != null) {
          psObtenerEstadoFicha.close();
        }
        if (psActualizarUltimoEstadoFichaActivo != null) {
          psActualizarUltimoEstadoFichaActivo.close();
        }
        if (psRegistrarEstadoFicha != null) {
          psRegistrarEstadoFicha.close();
        }
        if (psDesactivarTokenFicha != null) {
          psDesactivarTokenFicha.close();
        }
        if (conexion != null) {
          conexion.close();
        }
      } catch (SQLException e) {
        System.err.println("Error: ha ocurrido un error al intentar cerrar las conexiones y/o liberacion de recursos -> " + e.getMessage());
      }
    }
    jsonObjRegistrarFicha = new JSONObject(response);
    return jsonObjRegistrarFicha;
  }

  @Override
  public JSONObject listarDatosFicha(PersonaBean persona) {
    JSONObject jsonObjListarDatosFicha = null;
//    JSONArray jsonArrayListarDatosFicha = new JSONArray();
    JSONObject jsonReporte = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    Connection cnx = null;
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    try {
      cnx = SqlserverDAOFactory.obtenerConexion(base);
      String sql
              = "SELECT "
              + "ficha.codigo_ficha codigoFicha, "
              //              + "ficha.codigo_persona codigoPersona, "
              + "persona.apellido_paterno apellidoPaterno, "
              + "persona.apellido_materno apellidoMaterno, "
              + "persona.nombre  nombre, "
              + "tipodocumento.descripcion_larga tipoDocumentoDescripcionLarga, "
              + "tipodocumento.descripcion_corta tipoDocumentoDescripcionCorta, "
              + "persona.numero_documento numeroDocumento, "
              + "CASE "
              + " WHEN persona.sexo = 'M' THEN 'MASCULINO' "
              + " WHEN persona.sexo = 'F' THEN 'FEMENINO' "
              + " ELSE 'SIN SEXO' "
              + "END sexo, "
              + "estadocivil.nombre estadoCivil, "
              + "Format(persona.fecha_nacimiento, 'dd/MM/yyyy') fechaNacimiento, "
              + "(cast(datediff(dd,persona.fecha_nacimiento,getdate()) / 365.25 as int)) edad, "
              + "(select count(1) from carga_familiar where carga_familiar.codigo_parentesco = 3 and carga_familiar.codigo_persona = ficha.codigo_persona) nroHijos, "
              + "nacionalidad.pais pais, "
              + "nacionalidad.gentilicio gentilicio, "
              + "nacionalidad.iso iso, "
              + "Isnull(ubigeonacimiento.nombre_departamento,'-') nombreDepartamentoNacimiento, "
              + "Isnull(ubigeonacimiento.nombre_provincia,'-') nombreProvinciaNacimiento, "
              + "Isnull(ubigeonacimiento.nombre_distrito,'-') nombreDistritoNacimiento, "
              + "persona.direccion_documento direccionDocumento, "
              + "Isnull(persona.telefono_fijo,'-') telefonoFijo, "
              + "Isnull(persona.telefono_movil,'-') telefonoMovil, "
              + "persona.correo correo, "
              + "Isnull(persona.ruc, '-') ruc, "
              + "Isnull(persona.foto, 'default.jpg') foto, "
              + "ubigeoresidencia.nombre_departamento nombreDepartamentoResidencia, "
              + "ubigeoresidencia.nombre_provincia nombreProvinciaResidencia, "
              + "ubigeoresidencia.nombre_distrito nombreDistritoResidencia, "
              + "persona.direccion_residencia direccionResidencia, "
              + "persona.latitud_residencia latitudResidencia, "
              + "persona.longitud_residencia longitudResidencia, "
              + "CASE WHEN persona.fondo_pension_activo = 0 THEN 'INACTIVO' "
              + " WHEN persona.fondo_pension_activo = 1 THEN 'ACTIVO' "
              + " ELSE 'ERROR' "
              + "END fondoPensionActivo, "
              + "fondopension.descripcion_larga fondoPensionDescripcionLarga, "
              + "fondopension.descripcion_corta fondoPensionDescripcionCorta, "
              + "persona.enlace_alfresco enlaceAlfresco, "
              + "tipo_estado_ficha.nombre estadoFicha "
              + "FROM ficha ficha "
              + "INNER JOIN persona persona ON ficha.codigo_persona = persona.codigo_persona "
              + "INNER JOIN tipo_documento tipodocumento ON tipodocumento.codigo_tipo_documento = persona.codigo_tipo_documento "
              + "INNER JOIN estado_civil estadocivil ON estadocivil.codigo_estado_civil = persona.codigo_estado_civil "
              + "INNER JOIN nacionalidad nacionalidad ON nacionalidad.codigo_nacionalidad = persona.codigo_nacionalidad "
              + "LEFT JOIN ubigeo ubigeonacimiento ON ubigeonacimiento.codigo_ubigeo = persona.codigo_ubigeo_nacimiento "
              + "INNER JOIN ubigeo ubigeoresidencia ON ubigeoresidencia.codigo_ubigeo = persona.codigo_ubigeo_residencia "
              + "INNER JOIN fondo_pension fondopension ON fondopension.codigo_fondo_pension = persona.codigo_fondo_pension "
              + "INNER JOIN estado_ficha on estado_ficha.codigo_ficha = ficha.codigo_ficha "
              + "INNER JOIN tipo_estado_ficha on tipo_estado_ficha.codigo_tipo_estado_ficha = estado_ficha.codigo_tipo_estado_ficha "
              + "where ficha.estado_registro = 1 and estado_ficha.estado_registro = 1 and ficha.codigo_persona = ?";
      System.out.println("SQL listarDatosFicha : " + sql);
      ps = cnx.prepareStatement(sql);
      ps.setInt(1, persona.getCodigoPersona());
      rs = ps.executeQuery();
      rs.next();
//      if (rs.next()) {
      ReporteBean reporte = new ReporteBean();
      reporte.setCodigoFicha(rs.getInt("codigoFicha"));
//      reporte.setCodigoPersona(rs.getInt("codigoPersona"));
      reporte.setApellidoPaterno(rs.getString("apellidoPaterno"));
      reporte.setApellidoMaterno(rs.getString("apellidoMaterno"));
      reporte.setNombre(rs.getString("nombre"));
      reporte.setTipoDocumentoDescripcionLarga(rs.getString("tipoDocumentoDescripcionLarga"));
      reporte.setTipoDocumentoDescripcionCorta(rs.getString("tipoDocumentoDescripcionCorta"));
      reporte.setNumeroDocumento(rs.getString("numeroDocumento"));
      reporte.setSexo(rs.getString("sexo"));
      reporte.setEstadoCivil(rs.getString("estadoCivil"));
      reporte.setFechaNacimiento(rs.getString("fechaNacimiento"));
      reporte.setEdad(rs.getString("edad"));
      reporte.setNroHijos(rs.getString("nroHijos"));
      reporte.setPais(rs.getString("pais"));
      reporte.setGentilicio(rs.getString("gentilicio"));
      reporte.setIso(rs.getString("iso"));
      reporte.setNombreDepartamentoNacimiento(rs.getString("nombreDepartamentoNacimiento"));
      reporte.setNombreProvinciaNacimiento(rs.getString("nombreProvinciaNacimiento"));
      reporte.setNombreDistritoNacimiento(rs.getString("nombreDistritoNacimiento"));
      reporte.setDireccionDocumento(rs.getString("direccionDocumento"));
      reporte.setTelefonoFijo(rs.getString("telefonoFijo"));
      reporte.setTelefonoMovil(rs.getString("telefonoMovil"));
      reporte.setCorreo(rs.getString("correo"));
      reporte.setRuc(rs.getString("ruc"));
      reporte.setFoto(rs.getString("foto"));
      reporte.setNombreDepartamentoResidencia(rs.getString("nombreDepartamentoResidencia"));
      reporte.setNombreProvinciaResidencia(rs.getString("nombreProvinciaResidencia"));
      reporte.setNombreDistritoResidencia(rs.getString("nombreDistritoResidencia"));
      reporte.setDireccionResidencia(rs.getString("direccionResidencia"));
      reporte.setLatitud(rs.getString("latitudResidencia"));
      reporte.setLongitud(rs.getString("longitudResidencia"));
      reporte.setFondoPensionActivo(rs.getString("fondoPensionActivo"));
      reporte.setFondoPensionDescripcionLarga(rs.getString("fondoPensionDescripcionLarga"));
      reporte.setFondoPensionDescripcionCorta(rs.getString("fondoPensionDescripcionCorta"));
      reporte.setEnlaceAlfresco(rs.getString("enlaceAlfresco"));
      reporte.setEstadoFicha(rs.getString("estadoFicha"));
//        JSONObject jsonReporte = new JSONObject(reporte);
//        jsonArrayListarDatosFicha.put(jsonReporte);
      jsonReporte = new JSONObject(reporte);
//      }

      JSONObject jsonObjListadoPersona = new JSONObject();
      jsonObjListadoPersona.put("persona", jsonReporte);

      response.setStatus(true);
      response.setMessage("Se listo los datos personales correctamente");
      response.setData(jsonObjListadoPersona);

    } catch (SQLException e) {
      System.err.println("registrarFicha -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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

    jsonObjListarDatosFicha = new JSONObject(response);
    return jsonObjListarDatosFicha;
  }

  @Override
  public JSONObject consultarFichasPorCriterio(String draw, int start, int length, String search, JSONObject criterioClient, UsuarioBean u) {
    System.out.println("====== JSON ======");
    System.out.println(criterioClient);
    System.out.println("====== JSON ======");
    JSONObject JOlistarFichas = null;
    JSONArray JAlistarFichas = new JSONArray();
    String base = "planillabd";
    Connection conexion = null;
    PreparedStatement ps = null, psCount = null;
    ResultSet rs = null, rsCount = null;
    String filtroListarFichas = "";
    String condicionPorUsuario = "";
    int cantidad = 0;
    int numeroFilas = start + 1;
    if (criterioClient.isNull("tipoBusqueda")) {
      filtroListarFichas = "";
    } else {
      switch (criterioClient.getString("tipoBusqueda")) {
        case "TIPO_DOCUMENTO":
          filtroListarFichas = "and tipo_documento.codigo_tipo_documento = " + criterioClient.getInt("codigoTipoDocumento") + " and persona.numero_documento = '" + criterioClient.getString("numeroDocumento") + "'";
          break;
        case "APELLIDOS":
          filtroListarFichas = "and persona.apellido_paterno like '" + criterioClient.getString("apellido") + "%'";
          break;
        case "FECHA_REGISTRO":
          filtroListarFichas = "and format(estado_ficha.fecha_registro, 'dd/MM/yyyy') between '" + criterioClient.getString("fechaDesde") + "' and '" + criterioClient.getString("fechaHasta") + "'";

          break;
        case "ESTADO_FICHA":
          filtroListarFichas = "and tipo_estado_ficha.codigo_tipo_estado_ficha = " + criterioClient.getInt("codigoTipoEstadoFicha");
          break;
        default:
          filtroListarFichas = "";
      }
    }

    if (u.getCodigoProyectoDetalle() == 5) {
      condicionPorUsuario = " ";
    } else {
//      condicionPorUsuario = " and estado_ficha.codigo_usuario in (" + u.getCodigoUsuario() + ", 0)";
//      condicionPorUsuario = " and estado_ficha.codigo_usuario in (" + u.getCodigoUsuario() + ", 0)";
      condicionPorUsuario = " and ficha.codigo_ficha in (select ef.codigo_ficha from estado_ficha ef where ef.codigo_usuario = " + u.getCodigoUsuario() + " and ef.codigo_tipo_estado_ficha not in (13))";
    }

    String sql = ""
            + "select top " + length + " "
            + "  ficha.codigo_ficha codigoficha, "
            + "  persona.codigo_persona codigopersona, "
            + "  tipo_documento.codigo_tipo_documento codigotipodocumento, "
            + "  tipo_documento.descripcion_corta tipodocumentodescripcioncorta, "
            + "  persona.numero_documento numerodocumento, "
            + "  persona.apellido_paterno apellidopaterno, "
            + "  persona.apellido_materno apellidomaterno, "
            + "  persona.nombre nombre, "
            + "  case "
            + "    when persona.apellido_paterno = '' and "
            + "      persona.apellido_materno = '' and "
            + "      persona.nombre = '' then '-' "
            + "    when persona.apellido_paterno is null or "
            + "      persona.apellido_materno is null or "
            + "      persona.nombre is null then '-' "
            + "    else persona.apellido_paterno + ' ' + persona.apellido_materno + ', ' + persona.nombre "
            + "  end personal, "
            + "  persona.correo correo, "
            + "  isnull(cargo.nombre, '-') cargo, "
            + "  format(estado_ficha.fecha_registro, 'dd/MM/yyyy') + ' ' + stuff(replace(right(convert(varchar(19), estado_ficha.fecha_registro, 0), 7), ' ', '0'), 6, 0, ' ') fecharegistroficha, "
            + "  tipo_estado_ficha.nombre estadoficha, "
            + "  '<span class=\"'+tipo_estado_ficha.estilo+'\">'+tipo_estado_ficha.nombre+'</span>' estilo "
            + "from ficha "
            + "inner join dbo.persona on dbo.persona.codigo_persona = dbo.ficha.codigo_persona "
            + "inner join dbo.tipo_documento on dbo.tipo_documento.codigo_tipo_documento = dbo.persona.codigo_tipo_documento "
            + "inner join dbo.estado_ficha on dbo.estado_ficha.codigo_ficha = dbo.ficha.codigo_ficha "
            + "inner join dbo.tipo_estado_ficha on dbo.tipo_estado_ficha.codigo_tipo_estado_ficha = dbo.estado_ficha.codigo_tipo_estado_ficha "
            + "left join dbo.ficha_laboral on dbo.ficha_laboral.codigo_ficha = dbo.ficha.codigo_ficha "
            + "left join dbo.area_cargo on dbo.area_cargo.codigo_area_cargo = dbo.ficha_laboral.codigo_area_cargo "
            + "left join dbo.cargo on dbo.cargo.codigo_cargo = dbo.area_cargo.codigo_cargo "
            + "where estado_ficha.estado_registro = 1 and ficha.estado_registro = 1 "
            // se quito estas dos lineas, solo quitare la condicion -> and ficha.estado_registro = 1 en ambas lineas
            //            + "and ficha.estado_registro = 1 "
            //            + "and ficha.codigo_ficha not in (select top " + start + " f.codigo_ficha from ficha f inner join dbo.estado_ficha ef on ef.codigo_ficha = f.codigo_ficha where ef.estado_registro = 1 and f.estado_registro = 1 order by ef.fecha_registro desc) "
            //            + "and ficha.estado_registro = 1 "
            + "and ficha.codigo_ficha not in (select top " + start + " f.codigo_ficha from ficha f inner join dbo.estado_ficha ef on ef.codigo_ficha = f.codigo_ficha where ef.estado_registro = 1 and f.estado_registro = 1 order by ef.fecha_registro desc) "
            + " " + filtroListarFichas + condicionPorUsuario
            + "order by estado_ficha.fecha_registro desc";
    System.out.println("SQL consultarFichasPorCriterio: " + sql);
    try {
      conexion = SqlserverDAOFactory.obtenerConexion(base);
      ps = conexion.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        ListadoFichaBean lf = new ListadoFichaBean();
        lf.setItem(numeroFilas++);
        lf.setCodigoFicha(rs.getInt("codigoFicha"));
        lf.setCodigoPersona(rs.getInt("codigoPersona"));
        lf.setCodigoTipoDocumento(rs.getInt("codigoTipoDocumento"));
        lf.setDescripcionCortaTipoDocumento(rs.getString("tipoDocumentoDescripcionCorta"));
        lf.setNumeroDocumento(rs.getString("numeroDocumento"));
        lf.setApellidoPaterno(rs.getString("apellidoPaterno"));
        lf.setApellidoMaterno(rs.getString("apellidoMaterno"));
        lf.setNombre(rs.getString("nombre"));
        lf.setPersonal(rs.getString("personal"));
        lf.setCorreo(rs.getString("correo"));
        lf.setCargo(rs.getString("cargo"));
        lf.setFechaRegistro(rs.getString("fechaRegistroFicha"));
        lf.setEstadoFicha(rs.getString("estadoFicha"));
        lf.setEstilo(rs.getString("estilo"));
        JSONObject objListadoFicha = new JSONObject(lf);
        JAlistarFichas.put(objListadoFicha);
      }
      System.out.println("==========================================");
      sql = ""
              + "select "
              + "count(1) cantidad "
              + "from ficha "
              + "inner join dbo.persona ON dbo.persona.codigo_persona = dbo.ficha.codigo_persona "
              + "inner join dbo.tipo_documento ON dbo.tipo_documento.codigo_tipo_documento = dbo.persona.codigo_tipo_documento "
              + "inner join dbo.estado_ficha ON dbo.estado_ficha.codigo_ficha = dbo.ficha.codigo_ficha "
              + "inner join dbo.tipo_estado_ficha ON dbo.tipo_estado_ficha.codigo_tipo_estado_ficha = dbo.estado_ficha.codigo_tipo_estado_ficha "
              + "left join dbo.ficha_laboral ON dbo.ficha_laboral.codigo_ficha = dbo.ficha.codigo_ficha "
              + "left join dbo.area_cargo ON dbo.area_cargo.codigo_area_cargo = dbo.ficha_laboral.codigo_area_cargo "
              + "left join dbo.cargo ON dbo.cargo.codigo_cargo = dbo.area_cargo.codigo_cargo "
              + "where estado_ficha.estado_registro = 1 and ficha.estado_registro = 1 " + filtroListarFichas + condicionPorUsuario;
      System.out.println("SQL consultarFichasPorCriterio (COUNT): " + sql);
      psCount = conexion.prepareStatement(sql);
      rsCount = psCount.executeQuery();
      rsCount.next();
      cantidad = rsCount.getInt("cantidad");
      System.out.println("cantidad => " + cantidad);
      JOlistarFichas = new JSONObject();
      JOlistarFichas.put("data", JAlistarFichas);
      JOlistarFichas.put("recordsFiltered", cantidad);
      JOlistarFichas.put("recordsTotal", cantidad);
      JOlistarFichas.put("draw", draw);
    } catch (SQLException e) {
      JOlistarFichas = new JSONObject();
      JOlistarFichas.put("Error", "Error inesperado -> " + e.getMessage() + " [" + e.getErrorCode() + "]");
      System.err.println(e.getMessage());
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
        if (rsCount != null) {
          rsCount.close();
        }
        if (ps != null) {
          ps.close();
        }
        if (psCount != null) {
          psCount.close();
        }
        if (conexion != null) {
          conexion.close();
        }
      } catch (SQLException e) {
        System.err.println("Error: ha ocurrido un error al intentar cerrar las conexiones y/o liberacion de recursos -> " + e.getMessage());
      }
    }
    return JOlistarFichas;
  }

  @Override
  public JSONObject listarDetalleEstadoFicha(FichaBean f) {
    JSONObject JOlistarDetalleEstadoFicha = null;
    JSONArray JAlistarDetalleEstadoFicha = new JSONArray();
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = ""
            + "select "
            + "tipo_estado_ficha.nombre nombreEstado, "
            + "tipo_estado_ficha.descripcion descripcionEstado, "
            + "format(estado_ficha.fecha_registro, 'dd/MM/yyyy') + ' ' + STUFF(REPLACE(RIGHT(CONVERT(varchar(19), estado_ficha.fecha_registro, 0), 7), ' ', '0'), 6, 0, ' ') fechaRegistroEstado, "
            + "estado_ficha.codigo_usuario codigoUsuario, "
            + "case "
            + "when estado_ficha.codigo_usuario = 0 then persona.apellido_paterno + ' ' + persona.apellido_materno + ' ' + persona.nombre "
            + "else trabajador_responsable.apellido_paterno + ' ' + trabajador_responsable.apellido_materno + ' ' +trabajador_responsable.nombre "
            + "end nombreUsuario "
            + "from ficha "
            + "inner join dbo.persona ON dbo.persona.codigo_persona = dbo.ficha.codigo_persona "
            + "inner join dbo.estado_ficha ON dbo.estado_ficha.codigo_ficha = dbo.ficha.codigo_ficha "
            + "inner join dbo.tipo_estado_ficha ON dbo.tipo_estado_ficha.codigo_tipo_estado_ficha = dbo.estado_ficha.codigo_tipo_estado_ficha "
            + "left join trabajador_responsable on trabajador_responsable.codigo_usuario = estado_ficha.codigo_usuario "
            + "where ficha.codigo_ficha = ? "
            + "order by estado_ficha.fecha_registro";
    System.out.println("SQL listarDetalleEstadoFicha: " + sql);
    Connection conexion = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      conexion = SqlserverDAOFactory.obtenerConexion(base);
      ps = conexion.prepareStatement(sql);
      ps.setInt(1, f.getCodigoFicha());
      rs = ps.executeQuery();
      while (rs.next()) {
        DetalleEstadoFichaBean def = new DetalleEstadoFichaBean();
        def.setNombreEstado(rs.getString("nombreEstado"));
        def.setDescripcionEstado(rs.getString("descripcionEstado"));
        def.setFechaRegistroEstado(rs.getString("fechaRegistroEstado"));
        def.setCodigoUsuario(rs.getInt("codigoUsuario"));
        def.setNombreUsuario(rs.getString("nombreUsuario"));
        JSONObject objDetalleEstadoFicha = new JSONObject(def);
        JAlistarDetalleEstadoFicha.put(objDetalleEstadoFicha);
      }
      JSONObject objDetalleEstadoFicha = new JSONObject();
      objDetalleEstadoFicha.put("detallesEstadoFicha", JAlistarDetalleEstadoFicha);
      response.setStatus(true);
      response.setMessage("Los detalle de estado de ficha se listaron correctamente");
      response.setData(objDetalleEstadoFicha);
    } catch (SQLException e) {
      System.err.println("listarDetalleEstadoFicha -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
    JOlistarDetalleEstadoFicha = new JSONObject(response);
    return JOlistarDetalleEstadoFicha;
  }

  @Override
  public JSONObject listarDatosAdministrativos(FichaBean f) {
    JSONObject JOlistarDatosAdministrativos = null;
    JSONObject JOdatosAdministrativos = new JSONObject();
    JSONArray JAlistarDatosAdministrativos = new JSONArray();
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = ""
            + "select "
            + "ficha_laboral.codigo_ficha codigoFicha, "
            + "format(ficha_laboral.fecha_ingreso,'dd/MM/yyyy') fechaIngreso, "
            + "format(ficha_laboral.fecha_fin,'dd/MM/yyyy') fechaTermino, "
            + "sede.nombre sede, "
            + "area.nombre area, "
            + "cargo.nombre cargo, "
            + "case "
            + "when ficha_laboral.tipo_ficha = 'A' then "
            + "("
            + "select codigo_area_cargo_tipo_pago "
            + "from sueldo_administrativo "
            + "where estado_registro = 1 "
            + "and codigo_ficha = " + f.getCodigoFicha() + " "
            + ") "
            + "when ficha_laboral.tipo_ficha = 'D' then "
            + "( "
            + "select codigo_area_cargo_tipo_pago "
            + "from sueldo_docente "
            + "where estado_registro = 1 "
            + "and codigo_ficha = " + f.getCodigoFicha() + " "
            + ") "
            + "end codigoAreaCargoTipoPago, "
            + "case "
            + " when ficha_laboral.tipo_ficha = 'A' then "
            + " ( "
            + "    select tipo_pago.nombre from tipo_pago "
            + "    inner join dbo.area_cargo_tipo_pago ON dbo.area_cargo_tipo_pago.codigo_tipo_pago = dbo.tipo_pago.codigo_tipo_pago "
            + "    inner join dbo.sueldo_administrativo ON dbo.sueldo_administrativo.codigo_area_cargo_tipo_pago = dbo.area_cargo_tipo_pago.codigo_area_cargo_tipo_pago "
            + "    where sueldo_administrativo.codigo_ficha = ficha_laboral.codigo_ficha and sueldo_administrativo.estado_registro = 1 "
            + " ) "
            + " when ficha_laboral.tipo_ficha = 'D' then "
            + " ( "
            + "    select tipo_pago.nombre from tipo_pago "
            + "    inner join dbo.area_cargo_tipo_pago ON dbo.area_cargo_tipo_pago.codigo_tipo_pago = dbo.tipo_pago.codigo_tipo_pago "
            + "    inner join dbo.sueldo_docente ON dbo.sueldo_docente.codigo_area_cargo_tipo_pago = dbo.area_cargo_tipo_pago.codigo_area_cargo_tipo_pago "
            + "    where sueldo_docente.codigo_ficha = ficha_laboral.codigo_ficha and sueldo_docente.estado_registro = 1 "
            + " ) "
            + "end tipoPago, "
            + "case when ficha_laboral.tipo_ficha = 'A' then 'ADMINISTRATIVO' else 'DOCENTE' end tipoFicha "
            + "from ficha_laboral "
            + "inner join dbo.sede_area ON dbo.sede_area.codigo_sede_area = dbo.ficha_laboral.codigo_sede_area "
            + "inner join dbo.sede ON dbo.sede.codigo_sede = dbo.sede_area.codigo_sede "
            + "inner join dbo.area_cargo ON dbo.area_cargo.codigo_area_cargo = dbo.ficha_laboral.codigo_area_cargo "
            + "inner join dbo.area ON dbo.area.codigo_area = dbo.area_cargo.codigo_area "
            + "inner join dbo.cargo ON dbo.cargo.codigo_cargo = dbo.area_cargo.codigo_cargo "
            + "where ficha_laboral.codigo_ficha = ?";
    System.out.println("SQL listarDatosAdministrativos: " + sql);
    Connection conexion = null;
    PreparedStatement ps = null, psCostos = null;
    ResultSet rs = null, rsCostos = null;
    try {
      conexion = SqlserverDAOFactory.obtenerConexion(base);
      ps = conexion.prepareStatement(sql);
      ps.setInt(1, f.getCodigoFicha());
      rs = ps.executeQuery();
      rs.next();
      DatosAdministrativosBean da = new DatosAdministrativosBean();
      da.setCodigoFicha(rs.getInt("codigoFicha"));
      da.setCodigoAreaCargoTipoPago(rs.getInt("codigoAreaCargoTipoPago"));
      da.setFechaIngreso(rs.getString("fechaIngreso"));
      da.setFechaTermino(rs.getString("fechaTermino"));
      da.setSede(rs.getString("sede"));
      da.setArea(rs.getString("area"));
      da.setCargo(rs.getString("cargo"));
      da.setTipoPago(rs.getString("tipoPago"));
      da.setTipoFicha(rs.getString("tipoFicha"));

      JOdatosAdministrativos.put("codigoFicha", da.getCodigoFicha());
      JOdatosAdministrativos.put("fechaIngreso", da.getFechaIngreso());
      JOdatosAdministrativos.put("fechaTermino", da.getFechaTermino());
      JOdatosAdministrativos.put("codigoAreaCargoTipoPago", da.getCodigoAreaCargoTipoPago());
      JOdatosAdministrativos.put("sede", da.getSede());
      JOdatosAdministrativos.put("area", da.getArea());
      JOdatosAdministrativos.put("cargo", da.getCargo());
      JOdatosAdministrativos.put("tipoPago", da.getTipoPago());
      JOdatosAdministrativos.put("tipoFicha", da.getTipoFicha());

      if (da.getTipoFicha().equals("ADMINISTRATIVO")) {
        sql = ""
                + "select "
                + "sueldo_administrativo.sueldo_escalafon sueldoEscalafon, "
                + "sueldo_administrativo.sueldo_mensual sueldoMensual, "
                + "isnull(sueldo_administrativo.sueldo_presidencia, 0.00) sueldoPresidencia, "
                + "sueldo_administrativo.observacion, "
                + "case "
                + " when sueldo_administrativo.estado_registro = 1 then 'COSTOS MODIFICADOS POR PRESIDENCIA' "
                + " when sueldo_administrativo.estado_registro = 0 then 'COSTOS ANTERIORES' "
                + "end descripcionEstado, "
                + "sueldo_administrativo.estado_registro estado , "
                + "format(sueldo_administrativo.fecha_registro, 'dd/MM/yyyy') + ' ' + stuff(replace(right(convert(varchar(19), sueldo_administrativo.fecha_registro, 0), 7), ' ', '0'), 6, 0, ' ') fechaRegistro "
                + "from sueldo_administrativo "
                + "where sueldo_administrativo.codigo_ficha = ?";
        System.out.println("SQL listarDatosAdministrativos COSTOS ADMINISTRATIVOS: " + sql);
        psCostos = conexion.prepareStatement(sql);
        psCostos.setInt(1, f.getCodigoFicha());
        rsCostos = psCostos.executeQuery();
        while (rsCostos.next()) {
          da.setSueldoEscalafon(CurrencyFormat.getCustomCurrency(rsCostos.getDouble("sueldoEscalafon")));
          da.setSueldoMensual(CurrencyFormat.getCustomCurrency(rsCostos.getDouble("sueldoMensual")));
          da.setSueldoPresidencia(CurrencyFormat.getCustomCurrency(rsCostos.getDouble("sueldoPresidencia")));
          da.setObservacionAdministrativo(rsCostos.getString("observacion"));
          da.setDescripcionEstadoAdministrativo(rsCostos.getString("descripcionEstado"));
          da.setEstadoAdministrativo(rsCostos.getString("estado"));
          da.setFechaRegistroAdministrativo(rsCostos.getString("fechaRegistro"));
          JSONObject objCostosAdministrativo = new JSONObject(da);
          JAlistarDatosAdministrativos.put(objCostosAdministrativo);
        }
      } else if (da.getTipoFicha().equals("DOCENTE")) {
        sql = ""
                + "select "
                + "isnull(sueldo_docente.costo_mensual, 0.00) costoMensual, "
                + "isnull(sueldo_docente.costo_a, 0.00) costoA, "
                + "isnull(sueldo_docente.costo_b, 0.00) costoB, "
                + "isnull(sueldo_docente.costo_c, 0.00) costoC, "
                + "sueldo_docente.observacion, "
                + "case "
                + " when sueldo_docente.estado_registro = 1 then 'COSTOS MODIFICADOS POR PRESIDENCIA' "
                + " when sueldo_docente.estado_registro = 0 then 'COSTOS ANTERIORES' "
                + "end descripcionEstado, "
                + "sueldo_docente.estado_registro estado ,"
                + "format(sueldo_docente.fecha_registro, 'dd/MM/yyyy') + ' ' + stuff(replace(right(convert(varchar(19), sueldo_docente.fecha_registro, 0), 7), ' ', '0'), 6, 0, ' ') fechaRegistro "
                + "from sueldo_docente "
                + "where sueldo_docente.codigo_ficha = ?";
        System.out.println("SQL listarDatosAdministrativos COSTOS DOCENTES: " + sql);
        psCostos = conexion.prepareStatement(sql);
        psCostos.setInt(1, f.getCodigoFicha());
        rsCostos = psCostos.executeQuery();
        while (rsCostos.next()) {
          da.setCostoMensual(CurrencyFormat.getCustomCurrency(rsCostos.getDouble("costoMensual")));
          da.setCostoA(CurrencyFormat.getCustomCurrency(rsCostos.getDouble("costoA")));
          da.setCostoB(CurrencyFormat.getCustomCurrency(rsCostos.getDouble("costoB")));
          da.setCostoC(CurrencyFormat.getCustomCurrency(rsCostos.getDouble("costoC")));
          da.setObservacionDocente(rsCostos.getString("observacion"));
          da.setDescripcionEstadoDocente(rsCostos.getString("descripcionEstado"));
          da.setEstadoDocente(rsCostos.getString("estado"));
          da.setFechaRegistroDocente(rsCostos.getString("fechaRegistro"));
          JSONObject objCostosDocente = new JSONObject(da);
          JAlistarDatosAdministrativos.put(objCostosDocente);
        }
      }

      JOdatosAdministrativos.put("datosAdministrativosCostos", JAlistarDatosAdministrativos);

      response.setStatus(true);
      response.setMessage("Los datos administrativos se listraron correctamente!");
      response.setData(JOdatosAdministrativos);

    } catch (SQLException e) {
      System.err.println("listarDatosAdministrativos -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
      System.err.println("listarDetalleEstadoFicha -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
      response.setStatus(false);
      response.setMessage("Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
        if (rsCostos != null) {
          rsCostos.close();
        }
        if (ps != null) {
          ps.close();
        }
        if (psCostos != null) {
          psCostos.close();
        }
        if (conexion != null) {
          conexion.close();
        }
      } catch (SQLException e) {
        System.err.println("Error: ha ocurrido un error al intentar cerrar las conexiones y/o liberacion de recursos -> " + e.getMessage());
      }
    }
    JOlistarDatosAdministrativos = new JSONObject(response);
    return JOlistarDatosAdministrativos;
  }

  @Override
  public JSONObject rechazarFicha(FichaBean f, EstadoFichaBean ef) {
    JSONObject JOrechazarFicha = null;
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = ""
            + "select "
            + "estado_ficha.codigo_estado_ficha codigoEstadoFicha "
            + "from estado_ficha "
            + "where estado_ficha.codigo_ficha = ? and estado_ficha.estado_registro = 1";
    System.out.println("SQL rechazarFicha (obtener codigo estado ficha) : " + sql);
    Connection conexion = SqlserverDAOFactory.obtenerConexion(base);
    PreparedStatement psObtenerCodigoEstadoFicha = null,
            psDesactivarEstadoFicha = null,
            psRegistrarEstadoFicha = null,
            psObtenerCodigoLote = null,
            psObtenerCodigoDetalleFichaLote = null,
            psDesactivarFichaDetalleLote = null;
    ResultSet rsObtenerCodigoEstadoFicha = null,
            rsObtenerCodigoLote = null,
            rsObtenerCodigoDetalleFichaLote = null;
    try {
      conexion.setAutoCommit(false);
      // obtener codigo estado ficha (tabla: "estado_ficha" parametro: codigoFicha)
      psObtenerCodigoEstadoFicha = conexion.prepareStatement(sql);
      psObtenerCodigoEstadoFicha.setInt(1, ef.getCodigoFicha());
      rsObtenerCodigoEstadoFicha = psObtenerCodigoEstadoFicha.executeQuery();
      rsObtenerCodigoEstadoFicha.next();
      ef.setCodigoEstadoFicha(rsObtenerCodigoEstadoFicha.getInt("codigoEstadoFicha"));
      if (ef.getCodigoEstadoFicha() > 0) {
        System.out.println("codigoEstadoFicha = " + ef.getCodigoEstadoFicha());
        // desactivar estado ficha (tabla: "estado_ficha" parametro: codigoEstadoFicha)
        sql = ""
                + "update estado_ficha "
                + "set estado_ficha.estado_registro = 0 "
                + "where estado_ficha.codigo_estado_ficha = ? and estado_ficha.estado_registro = 1";
        System.out.println("SQL rechazarFicha (desactivar estado ficha) : " + sql);
        psDesactivarEstadoFicha = conexion.prepareStatement(sql);
        psDesactivarEstadoFicha.setInt(1, ef.getCodigoEstadoFicha());
        int resultadoDesactivarEstadoFicha = psDesactivarEstadoFicha.executeUpdate();
        if (resultadoDesactivarEstadoFicha > 0) {
          System.out.println("resultadoDesactivarEstadoFicha = " + resultadoDesactivarEstadoFicha);
          // registrar nuevo estado ficha (tabla: "estado_ficha" parametro: codigoFicha, codigoTipoEstadoFicha, codigoUsuario)
          sql = ""
                  + "insert into estado_ficha ( "
                  + "   codigo_ficha "
                  + "  ,codigo_tipo_estado_ficha "
                  + "  ,fecha_registro "
                  + "  ,codigo_usuario "
                  + "  ,estado_registro "
                  + ") VALUES ( "
                  + "   ? "
                  + "  ,9 " // "RECHAZADO"
                  + "  ,getdate() "
                  + "  ,? "
                  + "  ,1 "
                  + ")";
          System.out.println("SQL rechazarFicha (registrar estado ficha) : " + sql);
          psRegistrarEstadoFicha = conexion.prepareStatement(sql);
          psRegistrarEstadoFicha.setInt(1, ef.getCodigoFicha());
          psRegistrarEstadoFicha.setInt(2, ef.getCodigoUsuario());
          int resultadoRegistrarEstadoFicha = psRegistrarEstadoFicha.executeUpdate();
          if (resultadoRegistrarEstadoFicha > 0) {
            System.out.println("resultadoRegistrarEstadoFicha = " + resultadoRegistrarEstadoFicha);
            // obtener codigo de lote (tabla: "detalle_ficha_lote" parametro: codigoFicha)
            sql = ""
                    + "select "
                    + "detalle_ficha_lote.codigo_ficha_lote codigoFichaLote "
                    + "from detalle_ficha_lote "
                    + "where detalle_ficha_lote.codigo_ficha = ? and detalle_ficha_lote.estado_registro = 1";
            System.out.println("SQL rechazarFicha (obtener codigoLote) : " + sql);
            psObtenerCodigoLote = conexion.prepareStatement(sql);
            psObtenerCodigoLote.setInt(1, f.getCodigoFicha());
            rsObtenerCodigoLote = psObtenerCodigoLote.executeQuery();
            rsObtenerCodigoLote.next();
            LoteFichaBean lf = new LoteFichaBean();
            lf.setCodigoFichaLote(rsObtenerCodigoLote.getInt("codigoFichaLote"));
            if (lf.getCodigoFichaLote() > 0) {
              System.out.println("codigoFichaLote = " + lf.getCodigoFichaLote());
              // obtener codigo detalle ficha lote (tabla: "detalle_ficha_lote" parametro: codigo_ficha_lote, codigo_ficha)
              sql = ""
                      + "select "
                      + "detalle_ficha_lote.codigo_detalle_ficha_lote codigoDetalleFichaLote "
                      + "from detalle_ficha_lote "
                      + "where detalle_ficha_lote.codigo_ficha_lote = ? and "
                      + "detalle_ficha_lote.codigo_ficha = ? and "
                      + "detalle_ficha_lote .estado_registro = 1";
              System.out.println("SQL rechazarFicha (obtener codigoDetalleFichaLote) : " + sql);
              psObtenerCodigoDetalleFichaLote = conexion.prepareStatement(sql);
              psObtenerCodigoDetalleFichaLote.setInt(1, lf.getCodigoFichaLote());
              psObtenerCodigoDetalleFichaLote.setInt(2, f.getCodigoFicha());
              rsObtenerCodigoDetalleFichaLote = psObtenerCodigoDetalleFichaLote.executeQuery();
              rsObtenerCodigoDetalleFichaLote.next();
              DetalleFichaLoteBean dfl = new DetalleFichaLoteBean();
              dfl.setCodigoDetalleFichaLote(rsObtenerCodigoDetalleFichaLote.getInt("codigoDetalleFichaLote"));
              if (dfl.getCodigoDetalleFichaLote() > 0) {
                System.out.println("codigoDetalleFichaLote = " + dfl.getCodigoDetalleFichaLote());
                // desactivar ficha del detalle ficha lote (tabla: "detalle_ficha_lote" parametro: codigo_ficha_lote, codigo_ficha)
                sql = ""
                        + "update detalle_ficha_lote "
                        + "set detalle_ficha_lote.estado_registro = 0 "
                        + "where detalle_ficha_lote.codigo_detalle_ficha_lote = ? and "
                        + "detalle_ficha_lote.estado_registro = 1";
                System.out.println("SQL rechazarFicha (desactivar ficha del detalle de ficha lote) : " + sql);
                psDesactivarFichaDetalleLote = conexion.prepareStatement(sql);
                psDesactivarFichaDetalleLote.setInt(1, dfl.getCodigoDetalleFichaLote());
                int resultadoDesactivarFichaDetalleLote = psDesactivarFichaDetalleLote.executeUpdate();
                if (resultadoDesactivarFichaDetalleLote > 0) {
                  System.out.println("resultadoDesactivarFichaDetalleLote = " + resultadoDesactivarFichaDetalleLote);
                  response.setStatus(true);
                  response.setMessage("La ficha ha sido rechazada con éxito");
                  conexion.commit();
                } else {
                  response.setStatus(false);
                  response.setMessage("No se pudo desactivar la ficha del detalle de lote ficha");
                  conexion.rollback();
                }
              } else {
                response.setStatus(false);
                response.setMessage("No se pudo obtener el codigo detalle ficha lote");
                conexion.rollback();
              }
            } else {
              response.setStatus(false);
              response.setMessage("No se pudo obtener el codigo ficha lote");
              conexion.rollback();
            }
          } else {
            response.setStatus(false);
            response.setMessage("No se pudo registrar el estado ficha");
            conexion.rollback();
          }
        } else {
          response.setStatus(false);
          response.setMessage("No se pudo desactivar el estado ficha");
          conexion.rollback();
        }
      } else {
        response.setStatus(false);
        response.setMessage("No se pudo obtener el codigo estado ficha");
        conexion.rollback();
      }
    } catch (SQLException e) {
      try {
        System.err.println("rechazarFicha -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
        response.setStatus(false);
        response.setMessage("Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
        conexion.rollback();
      } catch (SQLException ex) {
        Logger.getLogger(FichaSqlserverDAO.class.getName()).log(Level.SEVERE, null, ex);
      }
    } finally {
      try {
        if (rsObtenerCodigoEstadoFicha != null) {
          rsObtenerCodigoEstadoFicha.close();
        }
        if (rsObtenerCodigoLote != null) {
          rsObtenerCodigoLote.close();
        }
        if (rsObtenerCodigoDetalleFichaLote != null) {
          rsObtenerCodigoDetalleFichaLote.close();
        }
        if (psObtenerCodigoEstadoFicha != null) {
          psObtenerCodigoEstadoFicha.close();
        }
        if (psDesactivarEstadoFicha != null) {
          psDesactivarEstadoFicha.close();
        }
        if (psRegistrarEstadoFicha != null) {
          psRegistrarEstadoFicha.close();
        }
        if (psObtenerCodigoLote != null) {
          psObtenerCodigoLote.close();
        }
        if (psObtenerCodigoDetalleFichaLote != null) {
          psObtenerCodigoDetalleFichaLote.close();
        }
        if (psDesactivarFichaDetalleLote != null) {
          psDesactivarFichaDetalleLote.close();
        }
        if (conexion != null) {
          conexion.close();
        }
      } catch (SQLException e) {
        System.err.println("Error: ha ocurrido un error al intentar cerrar las conexiones y/o liberacion de recursos -> " + e.getMessage());
      }
    }
    JOrechazarFicha = new JSONObject(response);
    return JOrechazarFicha;
  }

  @Override
  public JSONObject aceptarFicha(FichaBean f, EstadoFichaBean ef) {
    JSONObject JOaceptarFicha = null;
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = ""
            + "select "
            + "estado_ficha.codigo_estado_ficha codigoEstadoFicha "
            + "from estado_ficha "
            + "where estado_ficha.codigo_ficha = ? and estado_ficha.estado_registro = 1";
    System.out.println("SQL aceptarFicha (obtener codigo estado ficha) : " + sql);
    Connection conexion = SqlserverDAOFactory.obtenerConexion(base);
    PreparedStatement psObtenerCodigoEstadoFicha = null,
            psDesactivarEstadoFicha = null,
            psRegistrarEstadoFicha = null;
    ResultSet rsObtenerCodigoEstadoFicha = null;
    try {
      conexion.setAutoCommit(false);
      // obtener codigo estado ficha (tabla: "estado_ficha" parametro: codigoFicha)
      psObtenerCodigoEstadoFicha = conexion.prepareStatement(sql);
      psObtenerCodigoEstadoFicha.setInt(1, ef.getCodigoFicha());
      rsObtenerCodigoEstadoFicha = psObtenerCodigoEstadoFicha.executeQuery();
      rsObtenerCodigoEstadoFicha.next();
      ef.setCodigoEstadoFicha(rsObtenerCodigoEstadoFicha.getInt("codigoEstadoFicha"));
      if (ef.getCodigoEstadoFicha() > 0) {
        System.out.println("codigoEstadoFicha = " + ef.getCodigoEstadoFicha());
        // desactivar estado ficha (tabla: "estado_ficha" parametro: codigoEstadoFicha)
        sql = ""
                + "update estado_ficha "
                + "set estado_ficha.estado_registro = 0 "
                + "where estado_ficha.codigo_estado_ficha = ? and estado_ficha.estado_registro = 1";
        System.out.println("SQL aceptarFicha (desactivar estado ficha) : " + sql);
        psDesactivarEstadoFicha = conexion.prepareStatement(sql);
        psDesactivarEstadoFicha.setInt(1, ef.getCodigoEstadoFicha());
        int resultadoDesactivarEstadoFicha = psDesactivarEstadoFicha.executeUpdate();
        if (resultadoDesactivarEstadoFicha > 0) {
          System.out.println("resultadoDesactivarEstadoFicha = " + resultadoDesactivarEstadoFicha);
          // registrar nuevo estado ficha (tabla: "estado_ficha" parametro: codigoFicha, codigoTipoEstadoFicha, codigoUsuario)
          sql = ""
                  + "insert into estado_ficha ( "
                  + "   codigo_ficha "
                  + "  ,codigo_tipo_estado_ficha "
                  + "  ,fecha_registro "
                  + "  ,codigo_usuario "
                  + "  ,estado_registro "
                  + ") VALUES ( "
                  + "   ? "
                  + "  ,10 " // "APROBADO/ACEPTADO"
                  + "  ,getdate() "
                  + "  ,? "
                  + "  ,1 "
                  + ")";
          System.out.println("SQL rechazarFicha (registrar estado ficha) : " + sql);
          psRegistrarEstadoFicha = conexion.prepareStatement(sql);
          psRegistrarEstadoFicha.setInt(1, ef.getCodigoFicha());
          psRegistrarEstadoFicha.setInt(2, ef.getCodigoUsuario());
          int resultadoRegistrarEstadoFicha = psRegistrarEstadoFicha.executeUpdate();
          if (resultadoRegistrarEstadoFicha > 0) {
            System.out.println("resultadoRegistrarEstadoFicha = " + resultadoRegistrarEstadoFicha);
            response.setStatus(true);
            response.setMessage("La ficha ha sido aceptada con éxito");
            conexion.commit();
          } else {
            response.setStatus(false);
            response.setMessage("No se pudo registrar el estado ficha");
            conexion.rollback();
          }
        } else {
          response.setStatus(false);
          response.setMessage("No se pudo desactivar el estado ficha");
          conexion.rollback();
        }
      } else {
        response.setStatus(false);
        response.setMessage("No se pudo obtener el codigo estado ficha");
        conexion.rollback();
      }
    } catch (SQLException e) {
      try {
        System.err.println("aceptarFicha -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
        response.setStatus(false);
        response.setMessage("Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
        conexion.rollback();
      } catch (SQLException ex) {
        Logger.getLogger(FichaSqlserverDAO.class.getName()).log(Level.SEVERE, null, ex);
      }
    } finally {
      try {
        if (rsObtenerCodigoEstadoFicha != null) {
          rsObtenerCodigoEstadoFicha.close();
        }
        if (psObtenerCodigoEstadoFicha != null) {
          psObtenerCodigoEstadoFicha.close();
        }
        if (psDesactivarEstadoFicha != null) {
          psDesactivarEstadoFicha.close();
        }
        if (psRegistrarEstadoFicha != null) {
          psRegistrarEstadoFicha.close();
        }
        if (conexion != null) {
          conexion.close();
        }
      } catch (SQLException e) {
        System.err.println("Error: ha ocurrido un error al intentar cerrar las conexiones y/o liberacion de recursos -> " + e.getMessage());
      }
    }
    JOaceptarFicha = new JSONObject(response);
    return JOaceptarFicha;
  }

  @Override
  public JSONObject actualizarFicha(PersonaBean p, TokenFichaBean tf) {
    JSONObject JOactualizarFicha = null;
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = ""
            + "update persona "
            + "set "
            + "persona.apellido_paterno = ?, "
            + "persona.apellido_materno = ?, "
            + "persona.nombre = ?, "
            + "persona.codigo_tipo_documento = ?, "
            + "persona.numero_documento = ?, "
            + "persona.correo = ? "
            + "where persona.codigo_persona = ?;";
    System.out.println("SQL actualizarFicha (actualizar persona) : " + sql);
    Connection conexion = SqlserverDAOFactory.obtenerConexion(base);
    PreparedStatement psActualizarPersona = null, psDesactivarToken = null, psRegistrarToken = null, psObtenerToken = null;
    ResultSet rsRegistrarToken = null, rsObtenerToken = null;
    try {
      conexion.setAutoCommit(false);
      // Actualizar persona (tabla: "persona" parametro: codigoPersona, codigoTipoDocumento, numeroDocumento, correoElectronico)
      psActualizarPersona = conexion.prepareStatement(sql);
      psActualizarPersona.setString(1, p.getApellidoPaterno());
      psActualizarPersona.setString(2, p.getApellidoMaterno());
      psActualizarPersona.setString(3, p.getNombre());
      psActualizarPersona.setInt(4, p.getCodigoTipoDocumento());
      psActualizarPersona.setString(5, p.getNumeroDocumento());
      psActualizarPersona.setString(6, p.getCorreo());
      psActualizarPersona.setInt(7, p.getCodigoPersona());
      int resultadoActualizarPersona = psActualizarPersona.executeUpdate();
      if (resultadoActualizarPersona > 0) {
        System.out.println("resultadoActualizarPersona = " + resultadoActualizarPersona);
        // Desactivar token (tabla: "token_ficha" parametro: codigoFicha)
        sql = ""
                + "update token_ficha "
                + "set token_ficha.estado_registro = 0 "
                + "where token_ficha.codigo_ficha = ? and token_ficha.estado_registro = 1";
        System.out.println("SQL actualizarFicha (desactivar token) : " + sql);
        psDesactivarToken = conexion.prepareStatement(sql);
        psDesactivarToken.setInt(1, tf.getCodigoFicha());
        int resultadoDesactivarToken = psDesactivarToken.executeUpdate();
        if (resultadoDesactivarToken > 0) {
          System.out.println("resultadoDesactivarToken = " + resultadoDesactivarToken);
          // Registrar token ficha (tabla: "token_ficha" parametro: codigoPersona, codigoVerificacion)
          sql = ""
                  + "insert into token_ficha ( "
                  + "   codigo_ficha "
                  + "  ,codigo_verificacion "
                  + "  ,fecha_creacion "
                  + "  ,fecha_expiracion "
                  + "  ,token "
                  + "  ,estado_registro "
                  + ") values ( "
                  + "   ? "
                  + "  ,? "
                  + "  ,getdate() "
                  + "  ,dateadd(dd, 1, getdate()) "
                  + "  ,lower(newid()) "
                  + "  ,1 "
                  + ")";
          System.out.println("SQL actualizarFicha (registrar token) : " + sql);
          String codigoVerificacion = GenerateCodeVerification.randomString(6); // almacenar el codigo de verificacion a enviar
          psRegistrarToken = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
          psRegistrarToken.setInt(1, tf.getCodigoFicha());
          psRegistrarToken.setString(2, codigoVerificacion);
          int resultadoRegistrarToken = psRegistrarToken.executeUpdate();
          if (resultadoRegistrarToken > 0) {
            rsRegistrarToken = psRegistrarToken.getGeneratedKeys();
            rsRegistrarToken.next();
            int codigoTokenFicha = rsRegistrarToken.getInt(1);
            System.out.println("resultadoRegistrarToken = " + resultadoRegistrarToken + " | codigoVerificacion = " + codigoVerificacion + " | codigoTokenFicha = " + codigoTokenFicha);
            // obtener token (tabla: "token_ficha" parametro: codigoTokenFicha)
            sql = ""
                    + "select token_ficha.token "
                    + "from token_ficha "
                    + "where token_ficha.codigo_token_ficha = ? and "
                    + "token_ficha.estado_registro = 1";
            System.out.println("SQL actualizarFicha (obtener token) : " + sql);
            psObtenerToken = conexion.prepareStatement(sql);
            psObtenerToken.setInt(1, codigoTokenFicha);
            rsObtenerToken = psObtenerToken.executeQuery();
            rsObtenerToken.next();
            String token = rsObtenerToken.getString("token");
            System.out.println("resultadoObtenerToken = " + token);
            // enviar correo con el link encriptado
            String paramUrl = "http://172.16.2.20:8080/trismegisto-planilla/TokenFichaServlet";
            String paramAccion = EncryptAction.Encriptar("validarTokenFicha", "TR1SM3G1ST0-PL4N1LL4");
            String paramId = EncryptAction.Encriptar(String.valueOf(codigoTokenFicha), "TR1SM3G1ST0-ID-PL4N1LL4");
            String paramToken = token;
            Correo c = new Correo();
            c.setDestino(p.getCorreo());
            c.setAsunto("BIENVENIDO A TRISMEGISTO PLANILLA");
            c.setMensaje(EmailDesign.correoDesign(codigoVerificacion, paramUrl + "?accion=" + paramAccion + "&id=" + paramId + "&token=" + paramToken));
            CorreoSqlserverDAO correo = new CorreoSqlserverDAO();
            System.out.println(paramUrl + "?accion=" + paramAccion + "&id=" + paramId + "&token=" + paramToken);
            if (correo.enviarCorreo(c)) {
              response.setStatus(true);
              response.setMessage("La ficha se ha actualizado con éxito. Hemos enviado un correo electrónico el cuál permitirá proceder con el registro, gracias.");
              conexion.commit();
            } else {
              response.setStatus(false);
              response.setMessage("Ha ocurrido un error, no se ha podido actualizar la ficha. Por favor contactarse con el área de sistemas, gracias.");
              conexion.rollback();
            }
          } else {
            response.setStatus(false);
            response.setMessage("No se pudo registrar el token ficha");
            conexion.rollback();
          }
        } else {
          response.setStatus(false);
          response.setMessage("No se pudo desactivar el token ficha");
          conexion.rollback();
        }
      } else {
        response.setStatus(false);
        response.setMessage("No se pudo actualizar la persona");
        conexion.rollback();
      }
    } catch (SQLException e) {
      try {
        System.err.println("actualizarFicha -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
        response.setStatus(false);
        response.setMessage("Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
        conexion.rollback();
      } catch (SQLException ex) {
        Logger.getLogger(FichaSqlserverDAO.class.getName()).log(Level.SEVERE, null, ex);
      }
    } finally {
      try {
        if (rsRegistrarToken != null) {
          rsRegistrarToken.close();
        }
        if (rsObtenerToken != null) {
          rsObtenerToken.close();
        }
        if (psActualizarPersona != null) {
          psActualizarPersona.close();
        }
        if (psDesactivarToken != null) {
          psDesactivarToken.close();
        }
        if (psRegistrarToken != null) {
          psRegistrarToken.close();
        }
        if (psObtenerToken != null) {
          psObtenerToken.close();
        }
        if (conexion != null) {
          conexion.close();
        }
      } catch (SQLException e) {
        System.err.println("Error: ha ocurrido un error al intentar cerrar las conexiones y/o liberacion de recursos -> " + e.getMessage());
      }
    }
    JOactualizarFicha = new JSONObject(response);
    return JOactualizarFicha;
  }

  @Override
  public JSONObject anularFicha(EstadoFichaBean ef, ObservacionFichaBean of) {
    JSONObject JOanularFicha = null;
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = "";
    Connection conexion = SqlserverDAOFactory.obtenerConexion(base);
    PreparedStatement psDesactivarFicha = null,
            psObtenerCodigoEstadoFicha = null,
            psDesactivarEstadoFicha = null,
            psRegistrarEstadoFicha = null,
            psDesactivarTokenFicha = null,
            psRegistrarObservacion = null;
    ResultSet rsObtenerCodigoEstadoFicha = null,
            rsObtenerNuevoCodigoEstadoFicha = null;
    try {
      conexion.setAutoCommit(false);

      /* DESACTIVAR FICHA */
      sql = "update ficha set estado_registro = 0 where codigo_ficha = ?";
      System.out.println("SQL anularFicha (desactivar ficha): " + sql);
      psDesactivarFicha = conexion.prepareStatement(sql);
      psDesactivarFicha.setInt(1, ef.getCodigoFicha());
      int resultadoDesactivarFicha = psDesactivarFicha.executeUpdate();
      if (resultadoDesactivarFicha > 0) {
        System.out.println("resultadoDesactivarFicha = " + resultadoDesactivarFicha);

        /* OBTENER ULTIMO CODIGO DE ESTADO FICHA */
        sql = ""
                + "select "
                + "codigo_estado_ficha codigoEstadoFicha "
                + "from estado_ficha "
                + "where codigo_ficha = ? and estado_registro = 1";
        System.out.println("SQL anularFicha (obtener utlimo codigo estado ficha): " + sql);
        psObtenerCodigoEstadoFicha = conexion.prepareStatement(sql);
        psObtenerCodigoEstadoFicha.setInt(1, ef.getCodigoFicha());
        rsObtenerCodigoEstadoFicha = psObtenerCodigoEstadoFicha.executeQuery();
        rsObtenerCodigoEstadoFicha.next();
        ef.setCodigoEstadoFicha(rsObtenerCodigoEstadoFicha.getInt("codigoEstadoFicha"));
        System.out.println("codigoEstadoFicha = " + ef.getCodigoEstadoFicha());

        /* DESACTIVAR EL ULTIMO ESTADO FICHA */
        sql = ""
                + "update estado_ficha "
                + "set estado_registro = 0 "
                + "where codigo_estado_ficha = ?";
        System.out.println("SQL anularFicha (desactivar ult. estado ficha): " + sql);
        psDesactivarEstadoFicha = conexion.prepareStatement(sql);
        psDesactivarEstadoFicha.setInt(1, ef.getCodigoEstadoFicha());
        int resultadoDesactivarEstadoFicha = psDesactivarEstadoFicha.executeUpdate();
        if (resultadoDesactivarEstadoFicha > 0) {
          System.out.println("resultadoDesactivarEstadoFicha = " + resultadoDesactivarEstadoFicha);
          /* REGISTRAR UN NUEVO ESTADO DE FICHA (ANULADO) */
          sql = ""
                  + "insert into estado_ficha ( "
                  + "   codigo_ficha "
                  + "  ,codigo_tipo_estado_ficha "
                  + "  ,fecha_registro "
                  + "  ,codigo_usuario "
                  + "  ,estado_registro "
                  + ") values ( "
                  + "   ? "
                  + "  ,13 " // Ficha anulada
                  + "  ,getdate() "
                  + "  ,? "
                  + "  ,1 "
                  + ")";
          System.out.println("SQL anularFicha (nuevo estado ficha): " + sql);
          psRegistrarEstadoFicha = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
          psRegistrarEstadoFicha.setInt(1, ef.getCodigoFicha());
          psRegistrarEstadoFicha.setInt(2, ef.getCodigoUsuario());
          int resultadoRegistroEstadoFicha = psRegistrarEstadoFicha.executeUpdate();
          if (resultadoRegistroEstadoFicha > 0) {
            System.out.println("resultadoRegistroEstadoFicha = " + resultadoRegistroEstadoFicha);

            rsObtenerNuevoCodigoEstadoFicha = psRegistrarEstadoFicha.getGeneratedKeys();
            rsObtenerNuevoCodigoEstadoFicha.next();
            int nuevoCodigoEstadoFicha = rsObtenerNuevoCodigoEstadoFicha.getInt(1);

            /* DESACTIVAR TOKEN FICHA */
            sql = ""
                    + "update token_ficha "
                    + "set estado_registro = 0 "
                    + "where codigo_ficha = ? and estado_registro = 1";
            System.out.println("SQL anularFicha (desactivar token ficha): " + sql);
            psDesactivarTokenFicha = conexion.prepareStatement(sql);
            psDesactivarTokenFicha.setInt(1, ef.getCodigoFicha());
            int resultadoDesactivarTokenFicha = psDesactivarTokenFicha.executeUpdate();
            if (resultadoDesactivarTokenFicha > 0) {
              System.out.println("resultadoDesactivarTokenFicha = " + resultadoDesactivarTokenFicha);

              /* REGISTRAR OBSERVACION */
              sql = ""
                      + "insert into observacion_ficha ( "
                      + "   codigo_estado_ficha "
                      + "  ,codigo_ficha "
                      + "  ,observacion "
                      + "  ,estado_registro "
                      + ") VALUES ( "
                      + "   ? "
                      + "  ,? "
                      + "  ,? "
                      + "  ,1 "
                      + ")";
              System.out.println("SQL anularFicha (nuevo estado ficha): " + sql);
              psRegistrarObservacion = conexion.prepareStatement(sql);
              psRegistrarObservacion.setInt(1, nuevoCodigoEstadoFicha);
              psRegistrarObservacion.setInt(2, ef.getCodigoFicha());
              psRegistrarObservacion.setString(3, of.getObservacion());
              int resultadoRegistroObservacion = psRegistrarObservacion.executeUpdate();
              if (resultadoRegistroObservacion > 0) {
                response.setStatus(true);
                response.setMessage("La ficha ha sido anulada.");
                conexion.commit();
              } else {
                response.setStatus(false);
                response.setMessage("No se pudo registrar la observacion");
                conexion.rollback();
              }
            } else {
              response.setStatus(false);
              response.setMessage("No se pudo desactivar el token ficha");
              conexion.rollback();
            }
          } else {
            response.setStatus(false);
            response.setMessage("No se pudo registrar el estado de ficha");
            conexion.rollback();
          }
        } else {
          response.setStatus(false);
          response.setMessage("No se pudo desactivar el estado de ficha");
          conexion.rollback();
        }
      } else {
        response.setStatus(false);
        response.setMessage("No se pudo desactivar la ficha");
        conexion.rollback();
      }
    } catch (SQLException e) {
      try {
        System.err.println("anularFicha -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
        response.setStatus(false);
        response.setMessage("Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
        conexion.rollback();
      } catch (SQLException ex) {
        Logger.getLogger(FichaSqlserverDAO.class.getName()).log(Level.SEVERE, null, ex);
      }
    } finally {
      try {
        if (psDesactivarFicha != null) {
          psDesactivarFicha.close();
        }
        if (psRegistrarEstadoFicha != null) {
          psRegistrarEstadoFicha.close();
        }
        if (psDesactivarTokenFicha != null) {
          psDesactivarTokenFicha.close();
        }
        if (conexion != null) {
          conexion.close();
        }
      } catch (SQLException e) {
        System.err.println("Error: ha ocurrido un error al intentar cerrar las conexiones y/o liberacion de recursos -> " + e.getMessage());
      }
    }
    JOanularFicha = new JSONObject(response);
    return JOanularFicha;
  }

  @Override
  public JSONObject verObservacion(EstadoFichaBean ef) {
    JSONObject JOverObservacion = null;
    JSONArray JAverObservacion = new JSONArray();
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = "";
    Connection conexion = SqlserverDAOFactory.obtenerConexion(base);
    PreparedStatement psObtenerEstadoFicha = null, psVerObservacion = null;
    ResultSet rsObtenerEstadoFicha = null, rsVerObservacion = null;
    try {
      sql = ""
              + "select "
              + "codigo_estado_ficha codigoEstadoFicha "
              + "from estado_ficha "
              + "where codigo_ficha = ? and estado_registro = 1";
      System.out.println("SQL verObservacion (obtener estado ficha) : " + sql);
      psObtenerEstadoFicha = conexion.prepareStatement(sql);
      psObtenerEstadoFicha.setInt(1, ef.getCodigoFicha());
      rsObtenerEstadoFicha = psObtenerEstadoFicha.executeQuery();
      rsObtenerEstadoFicha.next();
      int codigoEstadoFicha = rsObtenerEstadoFicha.getInt("codigoEstadoFicha");
      if (codigoEstadoFicha > 0) {
        System.out.println("codigoEstadoFicha = " + codigoEstadoFicha);

        /* OBTENER OBSERVACIÓN */
        sql = ""
                + "select "
                + "observacion "
                + "from observacion_ficha "
                + "where codigo_estado_ficha = ? and codigo_ficha = ?";
        System.out.println("SQL verObservacion : " + sql);
        psVerObservacion = conexion.prepareStatement(sql);
        psVerObservacion.setInt(1, codigoEstadoFicha);
        psVerObservacion.setInt(2, ef.getCodigoFicha());
        rsVerObservacion = psVerObservacion.executeQuery();
        while (rsVerObservacion.next()) {
          ObservacionFichaBean of = new ObservacionFichaBean();
          of.setObservacion(rsVerObservacion.getString("observacion"));
          JSONObject objObs = new JSONObject(of);
          JAverObservacion.put(objObs);
        }
        JSONObject objObs = new JSONObject();
        objObs.put("observaciones", JAverObservacion);
        response.setData(objObs);
        response.setMessage("las observaciones se listaron correctamente");
        response.setStatus(true);
      } else {
        response.setStatus(false);
        response.setMessage("No se pudo obtener el codigo de estado ficha");
      }
    } catch (SQLException e) {
      System.err.println("verObservacion -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
      response.setStatus(false);
      response.setMessage("Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
    } finally {
      try {
        if (rsObtenerEstadoFicha != null) {
          rsObtenerEstadoFicha.close();
        }
        if (rsVerObservacion != null) {
          rsVerObservacion.close();
        }
        if (psVerObservacion != null) {
          psVerObservacion.close();
        }
        if (psObtenerEstadoFicha != null) {
          psObtenerEstadoFicha.close();
        }
        if (conexion != null) {
          conexion.close();
        }
      } catch (SQLException e) {
        System.err.println("Error: ha ocurrido un error al intentar cerrar las conexiones y/o liberacion de recursos -> " + e.getMessage());
      }
    }
    JOverObservacion = new JSONObject(response);
    return JOverObservacion;
  }

  @Override
  public JSONObject listarFichasPresidenciaDT(String draw, String length, String start, JSONObject search) {
    JSONObject jsonListarFichasPresidenciaDT = new JSONObject();
    JSONArray data = new JSONArray();
    String base = "planillabd";
    Connection cnx = null;
    PreparedStatement ps = null, psCount = null;
    ResultSet rs = null, rsCount = null;
    int item = Integer.parseInt(start) + 1;
    int cantidad = 0;
    try {
      cnx = SqlserverDAOFactory.obtenerConexion(base);
      String sql = ""
              + "select top " + length + " "
              + "persona.codigo_persona codigoPersona, "
              + "ficha.codigo_ficha codigoFicha, "
              + "persona.apellido_paterno apellidoPaterno, "
              + "persona.apellido_materno apellidoMaterno, "
              + "persona.nombre nombre, "
              + "persona.numero_documento numeroDocumento, "
              + "tipo_documento.descripcion_corta descripcionCortaDocumento, "
              + "nacionalidad.gentilicio nacionalidad, "
              + "format(ficha_laboral.fecha_ingreso,'dd/MM/yyyy') fechaIngreso, "
              + "area.nombre area, "
              + "cargo.nombre cargo, "
              + "tipo_estado_ficha.nombre estado, "
              + "tipo_estado_ficha.estilo estilo "
              + "FROM ficha "
              + "inner join estado_ficha ON estado_ficha.codigo_ficha = ficha.codigo_ficha "
              + "inner join ficha_laboral ON ficha_laboral.codigo_ficha = ficha.codigo_ficha "
              + "inner join persona ON persona.codigo_persona = ficha.codigo_persona "
              + "inner join nacionalidad ON nacionalidad.codigo_nacionalidad = persona.codigo_nacionalidad "
              + "inner join tipo_documento ON tipo_documento.codigo_tipo_documento = persona.codigo_tipo_documento "
              + "inner join area_cargo ON area_cargo.codigo_area_cargo = ficha_laboral.codigo_area_cargo "
              + "inner join area ON area.codigo_area = area_cargo.codigo_area "
              + "inner join cargo ON cargo.codigo_cargo = area_cargo.codigo_cargo "
              + "inner join tipo_estado_ficha ON tipo_estado_ficha.codigo_tipo_estado_ficha = estado_ficha.codigo_tipo_estado_ficha "
              + "where estado_ficha.estado_registro = 1 "
              + "and ficha.estado_registro = 1 "
              + "and estado_ficha.codigo_tipo_estado_ficha not in (10) "
              + "and estado_ficha.codigo_tipo_estado_ficha in "
              + "(case when ficha_laboral.tipo_ficha = 'D' then 7 else 6 end, case when ficha_laboral.tipo_ficha = 'A' then 8 end,10,11,12) "
              + "and ficha_laboral.codigo_ficha not in "
              + "( "
              + "select codigo_ficha "
              + "from detalle_lote_aprobacion "
              + "where detalle_lote_aprobacion.codigo_ficha = ficha_laboral.codigo_ficha "
              + ") "
              + "and ficha.codigo_ficha not in "
              + "( "
              + "select top " + start + " f.codigo_ficha from ficha f "
              + "inner join estado_ficha ef ON ef.codigo_ficha = f.codigo_ficha "
              + "inner join ficha_laboral fl ON fl.codigo_ficha = f.codigo_ficha "
              + "where ef.estado_registro = 1 "
              + "and ef.codigo_tipo_estado_ficha not in (10) "
              + "and ef.codigo_tipo_estado_ficha in "
              + "(case when fl.tipo_ficha = 'D' then 7 else 6 end, case when ficha_laboral.tipo_ficha = 'A' then 8 end,10,11,12) "
              + "and fl.codigo_ficha not in "
              + "( "
              + "select codigo_ficha "
              + "from detalle_lote_aprobacion "
              + "where detalle_lote_aprobacion.codigo_ficha = fl.codigo_ficha "
              + ") "
              + "order by 1 desc "
              + ") "
              + "order by 1 desc";
      System.out.println("SQL (listarFichasPresidenciaDT) >>>> " + sql);
      ps = cnx.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        ListadoFichaBean ficha = new ListadoFichaBean();
        ficha.setItem(item++);
        ficha.setCodigoPersona(rs.getInt("codigoPersona"));
        ficha.setCodigoFicha(rs.getInt("codigoFicha"));
        ficha.setApellidoPaterno(rs.getString("apellidoPaterno"));
        ficha.setApellidoMaterno(rs.getString("apellidoMaterno"));
        ficha.setNombre(rs.getString("nombre"));
        ficha.setNumeroDocumento(rs.getString("numeroDocumento"));
        ficha.setDescripcionCortaTipoDocumento(rs.getString("descripcionCortaDocumento"));
        ficha.setNacionalidad(rs.getString("nacionalidad"));
        ficha.setFechaIngreso(rs.getString("fechaIngreso"));
        ficha.setArea(rs.getString("area"));
        ficha.setCargo(rs.getString("cargo"));
        ficha.setEstadoFicha(rs.getString("estado"));
        ficha.setEstilo(rs.getString("estilo"));
        JSONObject jsonObjFicha = new JSONObject(ficha);
        data.put(jsonObjFicha);
      }

      String sqlCount
              = "select "
              + "count(1) as cantidad "
              + "FROM ficha "
              + "inner join ficha_laboral ON ficha_laboral.codigo_ficha = ficha.codigo_ficha "
              + "inner join estado_ficha ON estado_ficha.codigo_ficha = ficha.codigo_ficha "
              + "inner join tipo_estado_ficha ON tipo_estado_ficha.codigo_tipo_estado_ficha = estado_ficha.codigo_tipo_estado_ficha "
              + "where estado_ficha.estado_registro = 1 "
              + "and estado_ficha.codigo_tipo_estado_ficha not in (10) "
              + "and tipo_estado_ficha.codigo_tipo_estado_ficha in (case when ficha_laboral.tipo_ficha = 'D' then 7 else 6 end, case when ficha_laboral.tipo_ficha = 'A' then 8 end,10,11,12) "
              + "and ficha.estado_registro = 1 "
              + "and ficha_laboral.codigo_ficha not in "
              + "( "
              + "select codigo_ficha "
              + "from detalle_lote_aprobacion "
              + "where detalle_lote_aprobacion.codigo_ficha = ficha_laboral.codigo_ficha "
              + ")";
      psCount = cnx.prepareStatement(sqlCount);
      rsCount = psCount.executeQuery();
      rsCount.next();
      cantidad = rsCount.getInt("cantidad");

      jsonListarFichasPresidenciaDT.put("data", data);
      jsonListarFichasPresidenciaDT.put("recordsFiltered", cantidad);
      jsonListarFichasPresidenciaDT.put("recordsTotal", cantidad);
      jsonListarFichasPresidenciaDT.put("draw", draw);

    } catch (SQLException e) {
      jsonListarFichasPresidenciaDT.put("Error", "Error inesperado -> " + e.getMessage() + " [" + e.getErrorCode() + "]");
      System.err.println(e.getMessage());
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
        if (rsCount != null) {
          rsCount.close();
        }
        if (ps != null) {
          ps.close();
        }
        if (psCount != null) {
          psCount.close();
        }
        if (cnx != null) {
          cnx.close();
        }
      } catch (SQLException e) {
        System.err.println("Error: ha ocurrido un error al intentar cerrar las conexiones y/o liberacion de recursos -> " + e.getMessage());
      }
    }

    return jsonListarFichasPresidenciaDT;
  }

  @Override
  public JSONObject listarFichasPresidencia() {
    JSONObject jsonListarFichasPresidencia = null;
    JSONArray data = new JSONArray();
    String base = "planillabd";
    Connection cnx = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    ResponseHelper response = new ResponseHelper();
    try {
      cnx = SqlserverDAOFactory.obtenerConexion(base);
      String sql
              = "select "
              + "persona.codigo_persona codigoPersona, "
              + "ficha.codigo_ficha codigoFicha, "
              + "persona.apellido_paterno apellidoPaterno, "
              + "persona.apellido_materno apellidoMaterno, "
              + "persona.nombre nombre, "
              + "persona.numero_documento numeroDocumento, "
              + "tipo_documento.descripcion_corta descripcionCortaDocumento, "
              + "nacionalidad.gentilicio nacionalidad, "
              + "format(ficha_laboral.fecha_ingreso,'dd/MM/yyyy') fechaIngreso, "
              + "area.nombre area, "
              + "cargo.nombre cargo, "
              + "tipo_estado_ficha.nombre estado, "
              + "tipo_estado_ficha.estilo estilo "
              + "FROM ficha "
              + "inner join estado_ficha ON estado_ficha.codigo_ficha = ficha.codigo_ficha "
              + "inner join ficha_laboral ON ficha_laboral.codigo_ficha = ficha.codigo_ficha "
              + "inner join persona ON persona.codigo_persona = ficha.codigo_persona "
              + "inner join nacionalidad ON nacionalidad.codigo_nacionalidad = persona.codigo_nacionalidad "
              + "inner join tipo_documento ON tipo_documento.codigo_tipo_documento = persona.codigo_tipo_documento "
              + "inner join area_cargo ON area_cargo.codigo_area_cargo = ficha_laboral.codigo_area_cargo "
              + "inner join area ON area.codigo_area = area_cargo.codigo_area "
              + "inner join cargo ON cargo.codigo_cargo = area_cargo.codigo_cargo "
              + "inner join tipo_estado_ficha ON tipo_estado_ficha.codigo_tipo_estado_ficha = estado_ficha.codigo_tipo_estado_ficha "
              + "where "
              + "estado_ficha.estado_registro = 1 "
              + "and ficha.estado_registro = 1 "
              + "and estado_ficha.codigo_tipo_estado_ficha not in (10) "
              + "and estado_ficha.codigo_tipo_estado_ficha in "
              + "(case when ficha_laboral.tipo_ficha = 'D' then 7 else 6 end, case when ficha_laboral.tipo_ficha = 'A' then 8 end,10,11,12) "
              + "and ficha_laboral.codigo_ficha not in "
              + "( "
              + "select codigo_ficha "
              + "from detalle_lote_aprobacion "
              + "where detalle_lote_aprobacion.codigo_ficha = ficha_laboral.codigo_ficha "
              + ")"
              + "order by 1 desc";
      ps = cnx.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        ListadoFichaBean ficha = new ListadoFichaBean();
        ficha.setCodigoPersona(rs.getInt("codigoPersona"));
        ficha.setCodigoFicha(rs.getInt("codigoFicha"));
        ficha.setApellidoPaterno(rs.getString("apellidoPaterno"));
        ficha.setApellidoMaterno(rs.getString("apellidoMaterno"));
        ficha.setNombre(rs.getString("nombre"));
        ficha.setNumeroDocumento(rs.getString("numeroDocumento"));
        ficha.setDescripcionCortaTipoDocumento(rs.getString("descripcionCortaDocumento"));
        ficha.setNacionalidad(rs.getString("nacionalidad"));
        ficha.setFechaIngreso(rs.getString("fechaIngreso"));
        ficha.setArea(rs.getString("area"));
        ficha.setCargo(rs.getString("cargo"));
        ficha.setEstadoFicha(rs.getString("estado"));
        ficha.setEstilo(rs.getString("estilo"));
        JSONObject jsonObjFicha = new JSONObject(ficha);
        data.put(jsonObjFicha);
      }

      JSONObject jsonObjFicha = new JSONObject();
      jsonObjFicha.put("fichas", data);

      response.setStatus(true);
      response.setMessage("Se listo correctamente las fichas para evaluar.");
      response.setData(jsonObjFicha);

    } catch (SQLException e) {
      response.setStatus(false);
      response.setMessage("Error inesperado -> " + e.getMessage() + " [" + e.getErrorCode() + "]");
      System.err.println(e.getMessage());
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

    jsonListarFichasPresidencia = new JSONObject(response);
    return jsonListarFichasPresidencia;
  }

  @Override
  public JSONObject verificarExistenciaFichaAnulada(PersonaBean p) {
    JSONObject JOverificarExistenciaFichaAnulada = null;
    ResponseHelper response = new ResponseHelper();
    String sql = ""
            + "select "
            + "count(1) existeFichaAnulada "
            + "from ficha "
            + "inner join dbo.persona ON dbo.persona.codigo_persona = dbo.ficha.codigo_persona "
            + "inner join dbo.estado_ficha ON dbo.estado_ficha.codigo_ficha = dbo.ficha.codigo_ficha "
            + "where "
            + "persona.codigo_tipo_documento = ? and "
            + "persona.numero_documento = ? and "
            + "persona.estado_registro = 1 and "
            + "ficha.estado_registro = 0 and "
            + "estado_ficha.codigo_tipo_estado_ficha = 13 and "
            + "estado_ficha.estado_registro = 1";
    System.out.println("SQL verificarExistenciaFichaAnulada : " + sql);
    String base = "planillabd";
    Connection conexion = SqlserverDAOFactory.obtenerConexion(base);
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conexion.prepareStatement(sql);
      ps.setInt(1, p.getCodigoTipoDocumento());
      ps.setString(2, p.getNumeroDocumento());
      rs = ps.executeQuery();
      rs.next();
      int existeFichaAnulada = rs.getInt("existeFichaAnulada");
      if (existeFichaAnulada > 0) {
        JSONObject objFicha = new JSONObject();
        objFicha.put("countFichaAnuladas", existeFichaAnulada);
        response.setStatus(true);
        response.setMessage("Se encontro ficha(s) anulada");
      } else {
        response.setStatus(false);
        response.setMessage("No se encontró ninguna ficha anulada");
      }
    } catch (SQLException e) {
      System.err.println("verificarExistenciaFichaAnulada -> Error: " + e.getMessage() + " Error Code: [" + e.getErrorCode() + "]");
      response.setStatus(false);
      response.setMessage("Error: " + e.getMessage() + "  Error Code: [" + e.getErrorCode() + "]");
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
    JOverificarExistenciaFichaAnulada = new JSONObject(response);
    return JOverificarExistenciaFichaAnulada;
  }

  @Override
  public JSONObject obtenerCodigoPersonaPorTipoDocNroDoc(PersonaBean p) {
    JSONObject JOobtenerCodigoPersonaPorTipoDocNroDoc = null;
    ResponseHelper response = new ResponseHelper();
    String sql = ""
            + "select "
            + "codigo_persona codigoPersona "
            + "from persona "
            + "where "
            + "codigo_tipo_documento = ? and "
            + "numero_documento = ? and "
            + "estado_registro = 1";
    System.out.println("SQL obtenerCodigoPersonaPorTipoDocNroDoc : " + sql);
    String base = "planillabd";
    Connection conexion = SqlserverDAOFactory.obtenerConexion(base);
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conexion.prepareStatement(sql);
      ps.setInt(1, p.getCodigoTipoDocumento());
      ps.setString(2, p.getNumeroDocumento());
      rs = ps.executeQuery();
      rs.next();
      int codigoPersona = rs.getInt("codigoPersona");
      if (codigoPersona > 0) {
        JSONObject objPersona = new JSONObject();
        objPersona.put("getResultedKey", codigoPersona);
        response.setStatus(true);
        response.setMessage("Se encontró el código de persona");
        response.setData(objPersona);
      } else {
        response.setStatus(false);
        response.setMessage("No se encontró a ningun persona");
      }
    } catch (SQLException e) {
      System.err.println("obtenerCodigoPersonaPorTipoDocNroDoc -> Error: " + e.getMessage() + " Error Code: [" + e.getErrorCode() + "]");
      response.setStatus(false);
      response.setMessage("Error: " + e.getMessage() + "  Error Code: [" + e.getErrorCode() + "]");
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
    JOobtenerCodigoPersonaPorTipoDocNroDoc = new JSONObject(response);
    return JOobtenerCodigoPersonaPorTipoDocNroDoc;
  }

  @Override
  public JSONObject habilitarFicha(PersonaBean p, UsuarioBean u) {
    JSONObject JOhabilitarFicha = null;
    ResponseHelper response = new ResponseHelper();
    String sql = "";
    String base = "planillabd";
    Connection conexion = SqlserverDAOFactory.obtenerConexion(base);
    PreparedStatement psActualizarCorreoPersona = null,
            psRegistrarFicha = null,
            psRegistrarEstadoFicha = null,
            psRegistrarTokenFicha = null,
            psObtenerToken = null;
    ResultSet rsRegistrarFicha = null,
            rsRegistrarTokenFicha = null,
            rsObtenerToken = null;
    try {
      conexion.setAutoCommit(false);
      /* ACTUALIZAR CORREO DE LA PERSONA */
      sql = ""
              + "update persona "
              + "set correo = ? "
              + "where "
              + "codigo_persona = ? and "
              + "estado_registro = 1";
      System.out.println("SQL habilitarFicha (actualizar persona) : " + sql);
      psActualizarCorreoPersona = conexion.prepareStatement(sql);
      psActualizarCorreoPersona.setString(1, p.getCorreo());
      psActualizarCorreoPersona.setInt(2, p.getCodigoPersona());
      int resultadoActualizarPersona = psActualizarCorreoPersona.executeUpdate();
      if (resultadoActualizarPersona > 0) {
        System.out.println("=> resultadoActualizarPersona = " + resultadoActualizarPersona);

        /* REGISTRAR FICHA */
        sql = ""
                + "insert into ficha ( "
                + "   codigo_persona "
                + "  ,estado_registro "
                + ") values ( "
                + "   ? "
                + "  ,1 "
                + ")";
        System.out.println("SQL habilitarFicha (registrar ficha) : " + sql);
        psRegistrarFicha = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        psRegistrarFicha.setInt(1, p.getCodigoPersona());
        int resultadoRegistrarFicha = psRegistrarFicha.executeUpdate();
        if (resultadoRegistrarFicha > 0) {
          System.out.println("=> resultadoRegistrarFicha = " + resultadoRegistrarFicha);
          rsRegistrarFicha = psRegistrarFicha.getGeneratedKeys();
          rsRegistrarFicha.next();
          int codigoFicha = rsRegistrarFicha.getInt(1);

          /* REGISTRAR ESTADO FICHA */
          sql = ""
                  + "insert into estado_ficha ( "
                  + "   codigo_ficha "
                  + "  ,codigo_tipo_estado_ficha "
                  + "  ,fecha_registro "
                  + "  ,codigo_usuario "
                  + "  ,estado_registro "
                  + ") values ( "
                  + "   ? "
                  + "  ,1 "
                  + "  ,getdate() "
                  + "  ,? "
                  + "  ,1 "
                  + ")";
          System.out.println("SQL habilitarFicha (registrar estado ficha) : " + sql);
          psRegistrarEstadoFicha = conexion.prepareStatement(sql);
          psRegistrarEstadoFicha.setInt(1, codigoFicha);
          psRegistrarEstadoFicha.setInt(2, u.getCodigoUsuario());
          int resultadoRegistrarEstadoFicha = psRegistrarEstadoFicha.executeUpdate();
          if (resultadoRegistrarEstadoFicha > 0) {
            System.out.println("=> resultadoRegistrarEstadoFicha = " + resultadoRegistrarEstadoFicha);

            /*  REGISTRAR TOKEN FICHA */
            sql = ""
                    + "insert into token_ficha ( "
                    + "   codigo_ficha "
                    + "  ,codigo_verificacion "
                    + "  ,fecha_creacion "
                    + "  ,fecha_expiracion "
                    + "  ,token "
                    + "  ,estado_registro "
                    + ") values ( "
                    + "   ? "
                    + "  ,? "
                    + "  ,getdate() "
                    + "  ,dateadd(dd, 1, getdate()) "
                    + "  ,lower(newid()) "
                    + "  ,1 "
                    + ")";
            System.out.println("SQL habilitarFicha (registrar token ficha) : " + sql);
            String codigoVerificacion = GenerateCodeVerification.randomString(6);
            psRegistrarTokenFicha = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            psRegistrarTokenFicha.setInt(1, codigoFicha);
            psRegistrarTokenFicha.setString(2, codigoVerificacion);
            int resultadoRegistrarTokenFicha = psRegistrarTokenFicha.executeUpdate();
            if (resultadoRegistrarTokenFicha > 0) {
              System.out.println("=> resultadoRegistrarTokenFicha = " + resultadoRegistrarTokenFicha);
              rsRegistrarTokenFicha = psRegistrarTokenFicha.getGeneratedKeys();
              rsRegistrarTokenFicha.next();
              int codigoTokenFicha = rsRegistrarTokenFicha.getInt(1);

              /*  OBTENER CAMPO "TOKEN" DE TOKEN FICHA */
              sql = ""
                      + "select "
                      + "token "
                      + "from token_ficha "
                      + "where "
                      + "codigo_token_ficha = ? and "
                      + "estado_registro = 1";
              System.out.println("SQL habilitarFicha (obtener token) : " + sql);
              psObtenerToken = conexion.prepareStatement(sql);
              psObtenerToken.setInt(1, codigoTokenFicha);
              rsObtenerToken = psObtenerToken.executeQuery();
              rsObtenerToken.next();
              String token = rsObtenerToken.getString("token");
              if (token.length() > 0) {
                response.setStatus(true);
                response.setMessage("token = " + token);

                /* ENVIAR CORREO ELECTRÓNICO */
                Correo c = new Correo();
                c.setDestino(p.getCorreo());
                c.setAsunto("BIENVENIDO A TRISMEGISTO PLANILLA");
                c.setMensaje(EmailDesign.correoDesign(codigoVerificacion, "http://172.16.2.20:8080/trismegisto-planilla/TokenFichaServlet?accion=" + EncryptAction.Encriptar("validarTokenFicha", "TR1SM3G1ST0-PL4N1LL4") + "&id=" + EncryptAction.Encriptar(String.valueOf(codigoTokenFicha), "TR1SM3G1ST0-ID-PL4N1LL4") + "&token=" + token));
                CorreoSqlserverDAO correo = new CorreoSqlserverDAO();
                if (correo.enviarCorreo(c)) {
                  response.setStatus(true);
                  response.setMessage("La ficha se ha registrado con éxito. Hemos enviado un correo electrónico el cuál permitirá proceder con el registro, gracias.");
                  conexion.commit();
                } else {
                  response.setStatus(false);
                  response.setMessage("No se pudo registrar la ficha, no se envió el correo electrónico");
                  conexion.rollback();
                }
              } else {
                response.setStatus(false);
                response.setMessage("No se pudo obtener el campo 'token' de token ficha");
                conexion.rollback();
              }
            } else {
              response.setStatus(false);
              response.setMessage("No se pudo registrar token ficha");
              conexion.rollback();
            }
          } else {
            response.setStatus(false);
            response.setMessage("No se pudo registrar estado ficha");
            conexion.rollback();
          }
        } else {
          response.setStatus(false);
          response.setMessage("No se pudo registrar ficha");
          conexion.rollback();
        }
      } else {
        response.setStatus(false);
        response.setMessage("No se pudo actualizar a la persona");
        conexion.rollback();
      }
    } catch (SQLException e) {
      System.err.println("habilitarFicha -> Error: " + e.getMessage() + " Error Code: [" + e.getErrorCode() + "]");
      response.setStatus(false);
      response.setMessage("Error: " + e.getMessage() + "  Error Code: [" + e.getErrorCode() + "]");
    } finally {
      try {
        if (rsRegistrarFicha != null) {
          rsRegistrarFicha.close();
        }
        if (rsRegistrarTokenFicha != null) {
          rsRegistrarTokenFicha.close();
        }
        if (rsObtenerToken != null) {
          rsObtenerToken.close();
        }
        if (psActualizarCorreoPersona != null) {
          psActualizarCorreoPersona.close();
        }
        if (psRegistrarFicha != null) {
          psRegistrarFicha.close();
        }
        if (psRegistrarEstadoFicha != null) {
          psRegistrarEstadoFicha.close();
        }
        if (psRegistrarTokenFicha != null) {
          psRegistrarTokenFicha.close();
        }
        if (psObtenerToken != null) {
          psObtenerToken.close();
        }
        if (conexion != null) {
          conexion.close();
        }

      } catch (SQLException e) {
        System.err.println("Error: ha ocurrido un error al intentar cerrar las conexiones y/o liberacion de recursos -> " + e.getMessage());
      }
    }
    JOhabilitarFicha = new JSONObject(response);
    return JOhabilitarFicha;
  }

  @Override
  public JSONObject validarFichaActiva(PersonaBean p) {
    JSONObject JOvalidarFichaActiva = null;
    ResponseHelper response = new ResponseHelper();
    String sql = ""
            + "select "
            + "count(1) existeFichaActiva "
            + "from ficha "
            + "where "
            + "codigo_persona = ? and "
            + "estado_registro = 1";
    System.out.println("SQL validarFichaActiva : " + sql);
    String base = "planillabd";
    Connection conexion = SqlserverDAOFactory.obtenerConexion(base);
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conexion.prepareStatement(sql);
      ps.setInt(1, p.getCodigoPersona());
      rs = ps.executeQuery();
      rs.next();
      int resultadoExistenciaFichaActiva = rs.getInt("existeFichaActiva");
      if (resultadoExistenciaFichaActiva == 1) {
        response.setStatus(true); // hay una ficha activa
        response.setMessage("Al parecer hay una ficha activa con los datos ingresados. <br/> <span class='text-muted'>para registrar una nueva ficha por favor anule la ficha activa y vuelva a intentarlo</span>");
      } else if (resultadoExistenciaFichaActiva == 0) {
        response.setStatus(false); // no hay fichas activas
        response.setMessage("Al parecener no hay fichas activas");
      }
    } catch (SQLException e) {
      System.err.println("validarFichaActiva -> Error: " + e.getMessage() + " Error Code: [" + e.getErrorCode() + "]");
      response.setStatus(false);
      response.setMessage("Error: " + e.getMessage() + "  Error Code: [" + e.getErrorCode() + "]");
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
    JOvalidarFichaActiva = new JSONObject(response);
    return JOvalidarFichaActiva;
  }

//  public static void main(String[] args) {
//    PersonaBean p = new PersonaBean();
//    p.setCodigoPersona(2);
//    System.out.println(new FichaSqlserverDAO().validarFichaActiva(p));
//  }
}
