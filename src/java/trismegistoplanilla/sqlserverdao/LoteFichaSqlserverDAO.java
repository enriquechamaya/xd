package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.json.JSONArray;
import org.json.JSONObject;
import pe.siso.webservicesseguridad.webservices.UsuarioBean;
import trismegistoplanilla.beans.LoteFichaBean;
import trismegistoplanilla.beans.ReporteLoteFichaBean;
import trismegistoplanilla.dao.LoteFichaDAO;
import trismegistoplanilla.utilities.CurrencyFormat;
import trismegistoplanilla.utilities.ResponseHelper;

public class LoteFichaSqlserverDAO implements LoteFichaDAO {

  @Override
  public JSONObject listarFichasDT(String draw, String length, String start, JSONObject json) {
    JSONObject jsonObjListarFichasDT = new JSONObject();
    JSONArray jsonArrayListarFichasDT = new JSONArray();
    Connection cnx = null;
    PreparedStatement ps = null, psCantidadFilas = null;
    ResultSet rs = null, rsCantidadFilas = null;
    String base = "planillabd";
    String condicion = jsonCondicion(json);

    try {
      cnx = SqlserverDAOFactory.obtenerConexion(base);
      String sql
        = "select top " + length + " "
        + "ficha.codigo_ficha codigoFicha, "
        + "ficha.codigo_persona codigoPersona, "
        + "persona.apellido_paterno apellidoPaterno, "
        + "persona.apellido_materno apellidoMaterno, "
        + "persona.nombre nombre, "
        + "persona.numero_documento numeroDocumento, "
        + "ficha_laboral.tipo_ficha tipoFicha, "
        + "sede.nombre nombreSede, "
        + "area.nombre nombreArea, "
        + "cargo.nombre nombreCargo, "
        + "sueldo_administrativo.sueldo_escalafon sueldoEscalafonAdministrativo, "
        + "sueldo_administrativo.sueldo_mensual sueldoMensualAdministrativo, "
        + "isnull(sueldo_administrativo.observacion,'') observacionAdministrativo, "
        + "sueldo_docente.costo_mensual costoMensualDocente, "
        + "sueldo_docente.costo_a costoADocente, "
        + "sueldo_docente.costo_b costoBDocente, "
        + "sueldo_docente.costo_c costoCDocente, "
        + "sueldo_docente.observacion observacionDocente "
        + "FROM ficha "
        + "inner join persona ON persona.codigo_persona = ficha.codigo_persona "
        + "inner join ficha_laboral ON ficha_laboral.codigo_ficha = ficha.codigo_ficha "
        + "inner join sede_area ON sede_area.codigo_sede_area = ficha_laboral.codigo_sede_area "
        + "inner join sede ON sede.codigo_sede = sede_area.codigo_sede "
        + "inner join area ON area.codigo_area = sede_area.codigo_area "
        + "inner join area_cargo ON area_cargo.codigo_area_cargo = ficha_laboral.codigo_area_cargo "
        + "inner join cargo ON cargo.codigo_cargo = area_cargo.codigo_cargo "
        + "left join sueldo_administrativo ON sueldo_administrativo.codigo_ficha = ficha_laboral.codigo_ficha "
        + "left join sueldo_docente ON sueldo_docente.codigo_ficha = ficha_laboral.codigo_ficha "
        + "where ficha.estado_registro = 1 and ficha.codigo_ficha not in "
        + "( "
        + "select top " + start + " "
        + "x.codigo_ficha from ficha x "
        + "inner join estado_ficha ON estado_ficha.codigo_ficha = x.codigo_ficha "
        + "inner join ficha_laboral ON ficha_laboral.codigo_ficha = x.codigo_ficha "
        + "left join sueldo_administrativo ON sueldo_administrativo.codigo_ficha = ficha_laboral.codigo_ficha "
        + "left join sueldo_docente ON sueldo_docente.codigo_ficha = ficha_laboral.codigo_ficha "
        + "where estado_ficha.estado_registro = 1 and estado_ficha.codigo_tipo_estado_ficha = 4 "
        + condicion + " "
        + "order by 1 desc "
        + ") "
        + "and "
        + "( "
        + "select top 1 estado_ficha.codigo_tipo_estado_ficha FROM ficha x "
        + "inner join dbo.estado_ficha ON dbo.estado_ficha.codigo_ficha = x.codigo_ficha "
        + "inner join dbo.tipo_estado_ficha ON dbo.tipo_estado_ficha.codigo_tipo_estado_ficha = dbo.estado_ficha.codigo_tipo_estado_ficha "
        + "where x.codigo_ficha = ficha.codigo_ficha and estado_ficha.estado_registro = 1 "
        + ") = 4 "
        + condicion + " "
        + "order by 1 desc";
      System.out.println("SQL >>> " + sql);
      System.out.println("start >>>> " + start);
      System.out.println("length >>>> " + length);
      ps = cnx.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        ReporteLoteFichaBean reporteLoteFicha = new ReporteLoteFichaBean();
        reporteLoteFicha.setCodigoFicha(rs.getInt("codigoFicha"));
        reporteLoteFicha.setCodigoPersona(rs.getInt("codigoPersona"));
        reporteLoteFicha.setApellidoPaterno(rs.getString("apellidoPaterno"));
        reporteLoteFicha.setApellidoMaterno(rs.getString("apellidoMaterno"));
        reporteLoteFicha.setNombre(rs.getString("nombre"));
        reporteLoteFicha.setNumeroDocumento(rs.getString("numeroDocumento"));
        reporteLoteFicha.setTipoFicha(rs.getString("tipoFicha"));
        reporteLoteFicha.setNombreSede(rs.getString("nombreSede"));
        reporteLoteFicha.setNombreArea(rs.getString("nombreArea"));
        reporteLoteFicha.setNombreCargo(rs.getString("nombreCargo"));
        reporteLoteFicha.setSueldoEscalafonAdministrativo(CurrencyFormat.getCustomCurrency(rs.getDouble("sueldoEscalafonAdministrativo")));
        reporteLoteFicha.setSueldoMensualAdministrativo(CurrencyFormat.getCustomCurrency(rs.getDouble("sueldoMensualAdministrativo")));
        reporteLoteFicha.setObservacionAdministrativo(rs.getString("observacionAdministrativo"));
        reporteLoteFicha.setCostoMensualDocente(CurrencyFormat.getCustomCurrency(rs.getDouble("costoMensualDocente")));
        reporteLoteFicha.setCostoADocente(CurrencyFormat.getCustomCurrency(rs.getDouble("costoADocente")));
        reporteLoteFicha.setCostoBDocente(CurrencyFormat.getCustomCurrency(rs.getDouble("costoBDocente")));
        reporteLoteFicha.setCostoCDocente(CurrencyFormat.getCustomCurrency(rs.getDouble("costoCDocente")));
        reporteLoteFicha.setObservacionDocente(rs.getString("observacionDocente"));
        JSONObject jsonObjReporteLoteFicha = new JSONObject(reporteLoteFicha);
        jsonArrayListarFichasDT.put(jsonObjReporteLoteFicha);
      }
      int cantidadFilas = 0;
      String sqlCantidadFilas
        = "select "
        + "count(1) as cantidadFilas "
        + "FROM ficha "
        + "inner join ficha_laboral ON ficha_laboral.codigo_ficha = ficha.codigo_ficha "
        + "left join sueldo_administrativo ON sueldo_administrativo.codigo_ficha = ficha_laboral.codigo_ficha "
        + "left join sueldo_docente ON sueldo_docente.codigo_ficha = ficha_laboral.codigo_ficha "
        + "where ficha.estado_registro = 1 "
        + "and (select top 1 estado_ficha.codigo_tipo_estado_ficha FROM ficha x "
        + "inner join dbo.estado_ficha ON dbo.estado_ficha.codigo_ficha = x.codigo_ficha "
        + "inner join dbo.tipo_estado_ficha ON dbo.tipo_estado_ficha.codigo_tipo_estado_ficha = dbo.estado_ficha.codigo_tipo_estado_ficha "
        + "where x.codigo_ficha = ficha.codigo_ficha and estado_ficha.estado_registro = 1) = 4 "
        + condicion + " "
        + "order by 1 desc";
//      System.out.println("sqlCantidadFilas >>>> " + sqlCantidadFilas);
      psCantidadFilas = cnx.prepareStatement(sqlCantidadFilas);
      rsCantidadFilas = psCantidadFilas.executeQuery();
      rsCantidadFilas.next();
      cantidadFilas = rsCantidadFilas.getInt("cantidadFilas");
//      System.out.println("Cantidad filas >>>> " + cantidadFilas);
      jsonObjListarFichasDT.put("data", jsonArrayListarFichasDT);
      jsonObjListarFichasDT.put("recordsFiltered", cantidadFilas);
      jsonObjListarFichasDT.put("recordsTotal", cantidadFilas);
      jsonObjListarFichasDT.put("draw", draw);

    } catch (SQLException e) {
      System.err.println("listarFichasDT -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
      jsonObjListarFichasDT.put("status", false);
      jsonObjListarFichasDT.put("Mensaje", "Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
    } finally {
      try {
        if (rsCantidadFilas != null) {
          rsCantidadFilas.close();
        }
        if (psCantidadFilas != null) {
          psCantidadFilas.close();
        }
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
    return jsonObjListarFichasDT;
  }

  private String jsonCondicion(JSONObject json) {

    if (json.getInt("criterio") == 1) { // administrativo
      return " and sueldo_administrativo.codigo_sueldo_administrativo is not null ";
    }

    if (json.getInt("criterio") == 2) { // docente de secundaria
      return " and sueldo_docente.codigo_sueldo_docente is not null ";
    }

    return "";
  }

  @Override
  public JSONObject registrarLote(JSONObject data, UsuarioBean usuario) {
    System.out.println("=========");
    JSONObject jsonObjRegistrarLote = null;
    String base = "planillabd";
    Connection cnx = null;

    PreparedStatement psInsertarLote = null,
      psInsertarEstadoLote = null,
      psCantidadLotes = null,
      psInsertarDetalleLote = null,
      psDesactivarUltimoEstadoFicha = null,
      psRegistrarEstadoFicha = null;

    ResultSet rsInsertarLote = null,
      rsCantidadLotes = null;

    ResponseHelper response = new ResponseHelper();
    int cantidadLotes;
    int codigoLoteGenerado;
    String numeroGenerado;
    try {
      cnx = SqlserverDAOFactory.obtenerConexion(base);
      cnx.setAutoCommit(false);

      String sqlCantidadLote = "select count(1) as cantidad FROM ficha_lote";
      System.out.println("SQL sqlCantidadLote >>>> " + sqlCantidadLote);
      psCantidadLotes = cnx.prepareStatement(sqlCantidadLote);
      rsCantidadLotes = psCantidadLotes.executeQuery();
      rsCantidadLotes.next();
      cantidadLotes = rsCantidadLotes.getInt("cantidad");
      System.out.println("cantidadLotes >>>> " + cantidadLotes);
      numeroGenerado = numeroGenerado(cantidadLotes);
      System.out.println("numeroGenerado >>>> " + numeroGenerado);
      String sql
        = "insert into ficha_lote "
        + "(numero_lote, estado_registro, tipo_lote) "
        + "VALUES (?,1,?)";
      System.out.println("SQL registarLote >>>> " + sql);
      psInsertarLote = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      psInsertarLote.setString(1, numeroGenerado);
      psInsertarLote.setString(2, data.getString("tipoLote"));
      int resultadoInsertarLote = psInsertarLote.executeUpdate();
      System.out.println("resultadoInsertarLote >>>> " + resultadoInsertarLote);

      if (resultadoInsertarLote != 0) {
        rsInsertarLote = psInsertarLote.getGeneratedKeys();
        rsInsertarLote.next();
        codigoLoteGenerado = rsInsertarLote.getInt(1);
        System.out.println("codigoLoteGenerado >>>> " + codigoLoteGenerado);
        String sqlInsertarDetalleLote
          = "insert into detalle_ficha_lote (codigo_ficha_lote,codigo_ficha,estado_registro) "
          + "VALUES (?,?,1)";

        System.out.println("SQL sqlInsertarDetalleLote >>>> " + sqlInsertarDetalleLote);
        psInsertarDetalleLote = cnx.prepareStatement(sqlInsertarDetalleLote);
        JSONArray jsonArrayDetalleLote = data.getJSONArray("detallelote");
        int longitudDetalleLote = data.getJSONArray("detallelote").length();
        System.out.println("jsonArrayDetalleLote >>>> " + jsonArrayDetalleLote);
        System.out.println("longitudDetalleLote >>>> " + longitudDetalleLote);
        for (int i = 0; i < longitudDetalleLote; i++) {
          psInsertarDetalleLote.setInt(1, codigoLoteGenerado);
          psInsertarDetalleLote.setInt(2, jsonArrayDetalleLote.getJSONObject(i).getInt("codigoFicha"));
          psInsertarDetalleLote.addBatch();
        }

        int resultadoInsertarDetalleLote[] = psInsertarDetalleLote.executeBatch();
        int totalFilasDetalle = resultadoInsertarDetalleLote.length;
        System.out.println("total de filas detalle >>>> " + totalFilasDetalle);

        if (totalFilasDetalle != 0) {
          String sqlInsertarEstadoLote
            = "insert into estado_ficha_lote "
            + "(codigo_ficha_lote,codigo_tipo_estado_ficha_lote,fecha_registro,codigo_usuario,estado_registro) "
            + "VALUES (?,1,getdate(),?,1)";
          System.out.println("SQL sqlInsertarEstadoLote >>>> " + sqlInsertarEstadoLote);
          psInsertarEstadoLote = cnx.prepareStatement(sqlInsertarEstadoLote);
          psInsertarEstadoLote.setInt(1, codigoLoteGenerado);
          psInsertarEstadoLote.setInt(2, usuario.getCodigoUsuario());
          int resultadoInsertarEstadoLote = psInsertarEstadoLote.executeUpdate();
          System.out.println("resultadoInsertarEstadoLote >>>> " + resultadoInsertarEstadoLote);
          if (resultadoInsertarEstadoLote != 0) {
            String sqlDesactivarUltimoEstadoFicha
              = "update estado_ficha "
              + "set estado_registro = 0 "
              + "where codigo_ficha = ? and estado_registro = 1 ";
            System.out.println("SQL sqlDesactivarUltimoEstadoFicha >>>> " + sqlDesactivarUltimoEstadoFicha);
            psDesactivarUltimoEstadoFicha = cnx.prepareStatement(sqlDesactivarUltimoEstadoFicha);

            for (int i = 0; i < longitudDetalleLote; i++) {
              psDesactivarUltimoEstadoFicha.setInt(1, jsonArrayDetalleLote.getJSONObject(i).getInt("codigoFicha"));
              psDesactivarUltimoEstadoFicha.addBatch();
            }

            int resultadoDesactivarUltimoEstadoFicha[] = psDesactivarUltimoEstadoFicha.executeBatch();
            int totalFilasDesactivarUltimoEstadoFicha = resultadoDesactivarUltimoEstadoFicha.length;

            System.out.println("total filas desactivar ultimo estado >>>> " + totalFilasDesactivarUltimoEstadoFicha);
            if (totalFilasDesactivarUltimoEstadoFicha != 0) {
              String sqlRegistrarEstadoFicha
                = "insert into estado_ficha "
                + "(codigo_ficha,codigo_tipo_estado_ficha,fecha_registro,codigo_usuario,estado_registro) "
                + "VALUES (?,6 ,getdate() ,?  ,1)";
              System.out.println("SQL sqlRegistrarEstadoFicha >>>> " + sqlRegistrarEstadoFicha);
              psRegistrarEstadoFicha = cnx.prepareStatement(sqlRegistrarEstadoFicha);

              for (int i = 0; i < longitudDetalleLote; i++) {
                psRegistrarEstadoFicha.setInt(1, jsonArrayDetalleLote.getJSONObject(i).getInt("codigoFicha"));
                psRegistrarEstadoFicha.setInt(2, usuario.getCodigoUsuario());
                psRegistrarEstadoFicha.addBatch();
              }
              int resultadoRegistrarEstadoFicha[] = psRegistrarEstadoFicha.executeBatch();
              int totalFilasRegistrarEstadoFicha = resultadoRegistrarEstadoFicha.length;
              System.out.println("totalFilasRegistrarEstadoFicha >>>> " + totalFilasDesactivarUltimoEstadoFicha);
              if (totalFilasRegistrarEstadoFicha != 0) {
                JSONObject jsonObjResultedKey = new JSONObject();
                jsonObjResultedKey.put("getResultedKey", numeroGenerado);
                response.setStatus(true);
                response.setMessage("Se generó lote correctamente!");
                response.setData(jsonObjResultedKey);
                cnx.commit();
              } else {
                response.setStatus(false);
                response.setMessage("No se pudo registrar los estados a la ficha");
                cnx.rollback();
              }
            } else {
              response.setStatus(false);
              response.setMessage("Lo sentimos, no se pudo desactivar los estados de las fichas");
              cnx.rollback();
            }
          } else {
            response.setStatus(false);
            response.setMessage("No se pudo registrar el estado de lote");
            cnx.rollback();
          }
        } else {
          response.setStatus(false);
          response.setMessage("No se pudo registrar el detalle de lote");
          cnx.rollback();
        }
      } else {
        response.setStatus(false);
        response.setMessage("Lo sentimos, no se pudo generar lote");
        cnx.rollback();
      }

    } catch (SQLException e) {
      System.err.println("registrarLote -> Error: " + e.getMessage() + " Error Code: [" + e.getErrorCode() + "]");
      response.setStatus(false);
      response.setMessage("Error: " + e.getMessage() + " Error Code: [" + e.getErrorCode() + "]");
    } finally {
      try {
        if (rsCantidadLotes != null) {
          rsCantidadLotes.close();
        }
        if (rsInsertarLote != null) {
          rsInsertarLote.close();
        }
        if (psRegistrarEstadoFicha != null) {
          psRegistrarEstadoFicha.close();
        }
        if (psDesactivarUltimoEstadoFicha != null) {
          psDesactivarUltimoEstadoFicha.close();
        }
        if (psInsertarDetalleLote != null) {
          psInsertarDetalleLote.close();
        }
        if (psCantidadLotes != null) {
          psCantidadLotes.close();
        }
        if (psInsertarLote != null) {
          psInsertarLote.close();
        }
        if (cnx != null) {
          cnx.close();
        }
      } catch (SQLException e) {
        System.err.println("Error: ha ocurrido un error al intentar cerrar las conexiones y/o liberacion de recursos -> " + e.getMessage());
      }
    }

    jsonObjRegistrarLote = new JSONObject(response);
    return jsonObjRegistrarLote;
  }

  private String numeroGenerado(int cantidad) {
    int numeroLotes = cantidad + 1;
    String x_numeroLotes = String.format("%05d", numeroLotes);
    String fechaActual = CurrencyFormat.getActualDate("dd/MM/yyyy");
    String dia = fechaActual.split("/")[0];
    String mes = fechaActual.split("/")[1];
    String anio = fechaActual.split("/")[2].substring(2);

    return dia + mes + anio + x_numeroLotes;
  }

  @Override
  public JSONObject listarLoteDT(String draw, String length, String start, String search) {
    JSONObject jsonObjListarLoteDT = new JSONObject();
    JSONArray jsonArrayListarLoteDT = new JSONArray();
    Connection cnx = null;
    PreparedStatement ps = null,
      psCantidadLotes = null;
    ResultSet rs = null,
      rsCantidadLotes = null;
    String base = "planillabd";
    int cantidadLotes = 0;
    String condicion = "";

    if (!search.equals("")) {
      condicion = " and ficha_lote.numero_lote = '" + search + "'  ";
    }
    System.out.println("CONDICION =>>>> " + condicion);

    try {
      cnx = SqlserverDAOFactory.obtenerConexion(base);
      String sql
        = "select top " + length + " "
        + "ficha_lote.codigo_ficha_lote codigoLote, "
        + "ficha_lote.numero_lote numeroLote, "
        + "format(estado_ficha_lote.fecha_registro,'dd/MM/yyyy') fechaRegistro, "
        + "upper(datename(mm, estado_ficha_lote.fecha_registro)) mes, "
        + "(select count(1) cantidadFichas "
        + "from detalle_ficha_lote x "
        + "where x.codigo_ficha_lote = ficha_lote.codigo_ficha_lote ) cantidadFichas, "
        + "tipo_estado_ficha_lote.nombre estadoActual, "
        + "'<span class=\"'+tipo_estado_ficha_lote.estilo+'\">'+tipo_estado_ficha_lote.nombre+'</span>' estilo "
        + "FROM ficha_lote "
        + "inner join estado_ficha_lote "
        + "ON estado_ficha_lote.codigo_ficha_lote = ficha_lote.codigo_ficha_lote "
        + "inner join tipo_estado_ficha_lote "
        + "ON tipo_estado_ficha_lote.codigo_tipo_estado_ficha_lote = estado_ficha_lote.codigo_tipo_estado_ficha_lote "
        + "where estado_ficha_lote.estado_registro = 1 "
        + "and ficha_lote.estado_registro = 1 "
        + "and ficha_lote.codigo_ficha_lote not in (select top " + start + " "
        + "x.codigo_ficha_lote "
        + "from ficha_lote x where x.codigo_ficha_lote = ficha_lote.codigo_ficha_lote order by 1 desc) "
        + "and ficha_lote.tipo_lote = 'D' and estado_ficha_lote.codigo_tipo_estado_ficha_lote = 1"
        + condicion
        + "order by 1 desc";
      System.out.println("SQL listarLoteDT >>>> " + sql);
      ps = cnx.prepareStatement(sql);
      rs = ps.executeQuery();

      int numeroFilas = Integer.parseInt(start) + 1;
      while (rs.next()) {
        LoteFichaBean loteFicha = new LoteFichaBean();
        loteFicha.setItem(numeroFilas++);
        loteFicha.setCodigoFichaLote(rs.getInt("codigoLote"));
        loteFicha.setNumeroLote(rs.getString("numeroLote"));
        loteFicha.setFechaRegistro(rs.getString("fechaRegistro"));
        loteFicha.setNumeroFichas(rs.getInt("cantidadFichas"));
        loteFicha.setEstadoLote(rs.getString("estadoActual"));
        loteFicha.setEstilo(rs.getString("estilo"));
        loteFicha.setMes(rs.getString("mes"));
        JSONObject jsonObjLoteFicha = new JSONObject(loteFicha);
        jsonArrayListarLoteDT.put(jsonObjLoteFicha);
      }

      String sqlCantidadLotes
        = "select count(1) cantidadLotes "
        + "FROM ficha_lote "
        + "inner join dbo.estado_ficha_lote ON dbo.estado_ficha_lote.codigo_ficha_lote = dbo.ficha_lote.codigo_ficha_lote "
        + "where ficha_lote.estado_registro = 1 "
        + "and estado_ficha_lote.estado_registro = 1 "
        + "and estado_ficha_lote.codigo_tipo_estado_ficha_lote = 1 "
        + "and ficha_lote.tipo_lote = 'D' "
        + condicion;
      psCantidadLotes = cnx.prepareStatement(sqlCantidadLotes);
      rsCantidadLotes = psCantidadLotes.executeQuery();
      rsCantidadLotes.next();
      cantidadLotes = rsCantidadLotes.getInt("cantidadLotes");

      jsonObjListarLoteDT.put("data", jsonArrayListarLoteDT);
      jsonObjListarLoteDT.put("recordsFiltered", cantidadLotes);
      jsonObjListarLoteDT.put("recordsTotal", cantidadLotes);
      jsonObjListarLoteDT.put("draw", draw);

    } catch (SQLException e) {
      System.err.println("listarLoteDT -> Error: " + e.getMessage() + " Error Code: [" + e.getErrorCode() + "]");
      jsonObjListarLoteDT.put("status", false);
      jsonObjListarLoteDT.put("message", "Error: " + e.getMessage() + " Error Code: [" + e.getErrorCode() + "]");
    } finally {
      try {
        if (rsCantidadLotes != null) {
          rsCantidadLotes.close();
        }
        if (rs != null) {
          rs.close();
        }
        if (psCantidadLotes != null) {
          psCantidadLotes.close();
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

    return jsonObjListarLoteDT;
  }

  @Override
  public JSONObject listarLoteGeneralDT(JSONObject json) {
    JSONObject jsonObjListarLoteGeneral = new JSONObject();
    JSONArray jsonArrayListarLoteGeneral = new JSONArray();
    Connection cnx = null;
    PreparedStatement ps = null, psCount = null;
    ResultSet rs = null, rsCount = null;
    String base = "planillabd";

    try {
      cnx = SqlserverDAOFactory.obtenerConexion(base);
      String sql
        = "select top " + json.getString("length") + " "
        + "ficha_lote.codigo_ficha_lote codigoLote, "
        + "ficha_lote.numero_lote numeroLote, "
        + "format(estado_ficha_lote.fecha_registro,'dd/MM/yyyy') fechaRegistro, "
        + "upper(datename(mm, estado_ficha_lote.fecha_registro)) mes, "
        + "(select count(1) cantidadFichas "
        + "from detalle_ficha_lote x "
        + "where x.codigo_ficha_lote = ficha_lote.codigo_ficha_lote ) cantidadFichas, "
        + "tipo_estado_ficha_lote.nombre estadoActual, "
        + "tipo_estado_ficha_lote.estilo estilo, "
        + "ficha_lote.tipo_lote tipoLote "
        + "FROM ficha_lote "
        + "inner join estado_ficha_lote "
        + "ON estado_ficha_lote.codigo_ficha_lote = ficha_lote.codigo_ficha_lote "
        + "inner join tipo_estado_ficha_lote "
        + "ON tipo_estado_ficha_lote.codigo_tipo_estado_ficha_lote = estado_ficha_lote.codigo_tipo_estado_ficha_lote "
        + "where estado_ficha_lote.estado_registro = 1 "
        + "and ficha_lote.estado_registro = 1 "
        + "and ficha_lote.codigo_ficha_lote not in "
        + "( "
        + "select top " + json.getString("start") + " "
        + "fl.codigo_ficha_lote "
        + "from ficha_lote fl "
        + "inner join estado_ficha_lote efl ON efl.codigo_ficha_lote = fl.codigo_ficha_lote "
        + "where efl.estado_registro = 1 "
        + condicionListarLoteGeneral(json.getJSONObject("data")) + " "
        + "order by 1 desc "
        + ") "
        + condicionListarLoteGeneral(json.getJSONObject("data")) + " "
        + "order by 1 desc;";
      System.out.println("SQL >>>> " + sql);
      ps = cnx.prepareStatement(sql);
      rs = ps.executeQuery();
      int item = Integer.parseInt(json.getString("start")) + 1;
      while (rs.next()) {
        LoteFichaBean fichas = new LoteFichaBean();
        fichas.setCodigoFichaLote(rs.getInt("codigoLote"));
        fichas.setNumeroLote(rs.getString("numeroLote"));
        fichas.setFechaRegistro(rs.getString("fechaRegistro"));
        fichas.setMes(rs.getString("mes"));
        fichas.setNumeroFichas(rs.getInt("cantidadFichas"));
        fichas.setEstadoLote(rs.getString("estadoActual"));
        fichas.setEstilo(rs.getString("estilo"));
        fichas.setTipoLote(rs.getString("tipoLote"));
        fichas.setItem(item++);
        JSONObject jsonObjFichas = new JSONObject(fichas);
        jsonArrayListarLoteGeneral.put(jsonObjFichas);
      }

      String sqlCount
        = "select count(1) cantidad "
        + "FROM ficha_lote "
        + "inner join dbo.estado_ficha_lote ON dbo.estado_ficha_lote.codigo_ficha_lote = dbo.ficha_lote.codigo_ficha_lote "
        + "where ficha_lote.estado_registro = 1 "
        + "and estado_ficha_lote.estado_registro = 1 "
        + condicionListarLoteGeneral(json.getJSONObject("data"));
      int count;
      psCount = cnx.prepareStatement(sqlCount);
      rsCount = psCount.executeQuery();
      rsCount.next();
      count = rsCount.getInt("cantidad");

      jsonObjListarLoteGeneral.put("data", jsonArrayListarLoteGeneral);
      jsonObjListarLoteGeneral.put("recordsFiltered", count);
      jsonObjListarLoteGeneral.put("recordsTotal", count);
      jsonObjListarLoteGeneral.put("draw", json.getString("draw"));
    } catch (SQLException e) {
      System.err.println("listarLoteGeneralDT -> Error: " + e.getMessage() + " Error Code: [" + e.getErrorCode() + "]");
      jsonObjListarLoteGeneral.put("status", false);
      jsonObjListarLoteGeneral.put("message", "Error: " + e.getMessage() + " Error Code: [" + e.getErrorCode() + "]");
    } finally {
      try {
        if (rsCount != null) {
          rsCount.close();
        }
        if (rs != null) {
          rs.close();
        }
        if (psCount != null) {
          psCount.close();
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

    return jsonObjListarLoteGeneral;
  }

  private String condicionListarLoteGeneral(JSONObject json) {
    System.out.println(json);
    int criterio = json.getInt("criterio");
    int filtro = json.getInt("filtro");
    String search = json.getString("search");
    String search2 = json.getString("search2");

    if (criterio == 1) {
      return " and ficha_lote.numero_lote = '" + search + "' ";
    }

    if (criterio == 2) {
      if (filtro == 1) {
        return " and format(estado_ficha_lote.fecha_registro,'dd/MM/yyyy') >= '" + search + "' ";
      }
      if (filtro == 2) {
        return " and format(estado_ficha_lote.fecha_registro,'dd/MM/yyyy') <= '" + search + "' ";
      }
      if (filtro == 3) {
        return " and format(estado_ficha_lote.fecha_registro,'dd/MM/yyyy') = '" + search + "' ";
      }
      if (filtro == 4) {
        return " and format(estado_ficha_lote.fecha_registro,'dd/MM/yyyy') between '" + search + "' and '" + search2 + "' ";
      }
    }

    if (criterio == 3) {
      if (filtro == 1) {
        return " and (select count(1) from detalle_ficha_lote x where x.codigo_ficha_lote = ficha_lote.codigo_ficha_lote) >= " + search + " ";
      }
      if (filtro == 2) {
        return " and (select count(1) from detalle_ficha_lote x where x.codigo_ficha_lote = ficha_lote.codigo_ficha_lote) <=  " + search + " ";
      }
      if (filtro == 3) {
        return " and (select count(1) from detalle_ficha_lote x where x.codigo_ficha_lote = ficha_lote.codigo_ficha_lote) =  " + search + " ";
      }
    }

    return "";
  }

  @Override
  public JSONObject listarFichasGeneral(JSONObject json) {
    JSONObject jsonObjListarFichasGeneral = null;
    JSONArray jsonArrListarFichasGeneral = new JSONArray();
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd", sueldo = "", inner = "", where = "";
    PreparedStatement ps = null;
    ResultSet rs = null;
    Connection cnx = null;

    if (json.getString("tipoLote").equals("A")) {

      sueldo = "sueldo_administrativo.sueldo_escalafon sueldoEscalafon, "
        + "sueldo_administrativo.sueldo_mensual sueldoMensual,"
        + "sueldo_administrativo.sueldo_presidencia sueldoPresidencia ";
      inner = "inner join sueldo_administrativo ON sueldo_administrativo.codigo_ficha = ficha_laboral.codigo_ficha ";
      where = "sueldo_administrativo.estado_registro = 1";

    } else if (json.getString("tipoLote").equals("D")) {

      sueldo = "sueldo_docente.costo_mensual costoMensualDocente, "
        + "sueldo_docente.costo_a costoaDocente, "
        + "sueldo_docente.costo_b costobDocente, "
        + "sueldo_docente.costo_c costocDocente ";
      inner = "inner join sueldo_docente ON sueldo_docente.codigo_ficha = ficha_laboral.codigo_ficha ";
      where = "sueldo_docente.estado_registro = 1 ";

    }

    try {
      cnx = SqlserverDAOFactory.obtenerConexion(base);
      String sql
        = "select "
        + "persona.codigo_persona codigoPersona, "
        + "detalle_ficha_lote.codigo_ficha_lote codigoFichaLote, "
        + "detalle_ficha_lote.codigo_ficha codigoFicha, "
        + "persona.apellido_paterno apellidoPaterno, "
        + "persona.apellido_materno apellidoMaterno, "
        + "persona.nombre nombre, "
        + "tipo_documento.descripcion_corta tipoDocumento, "
        + "persona.numero_documento numeroDocumento, "
        + "format(ficha_laboral.fecha_ingreso,'dd/MM/yyyy') fechaIngreso, "
        + sueldo + " "
        + "FROM detalle_ficha_lote "
        + "inner join ficha_laboral ON ficha_laboral.codigo_ficha = detalle_ficha_lote.codigo_ficha "
        + "inner join ficha ON ficha.codigo_ficha = ficha_laboral.codigo_ficha "
        + "inner join persona ON persona.codigo_persona = ficha.codigo_persona "
        + "inner join tipo_documento ON tipo_documento.codigo_tipo_documento = persona.codigo_tipo_documento "
        + "inner join estado_ficha ON estado_ficha.codigo_ficha = ficha.codigo_ficha "
        + inner + " "
        + "where " + where + " "
        + "and estado_ficha.estado_registro = 1 "
        + "and detalle_ficha_lote.codigo_ficha_lote = ? ";
      System.out.println("JSON >>>> " + json);
      System.out.println("SQL >>>> " + sql);
      ps = cnx.prepareStatement(sql);
      ps.setInt(1, json.getInt("codigoLote"));
      rs = ps.executeQuery();
      while (rs.next()) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("codigoPersona", isNullOrEmpty(rs.getString("codigoPersona")) ? rs.getInt("codigoPersona") : 0);
        jsonObj.put("codigoLote", isNullOrEmpty(rs.getString("codigoFichaLote")) ? rs.getInt("codigoFichaLote") : 0);
        jsonObj.put("codigoFicha", isNullOrEmpty(rs.getString("codigoFicha")) ? rs.getInt("codigoFicha") : 0);
        jsonObj.put("apellidoPaterno", isNullOrEmpty(rs.getString("apellidoPaterno")) ? rs.getString("apellidoPaterno") : "");
        jsonObj.put("apellidoMaterno", isNullOrEmpty(rs.getString("apellidoMaterno")) ? rs.getString("apellidoMaterno") : "");
        jsonObj.put("nombre", isNullOrEmpty(rs.getString("nombre")) ? rs.getString("nombre") : "");
        jsonObj.put("tipoDocumento", isNullOrEmpty(rs.getString("tipoDocumento")) ? rs.getString("tipoDocumento") : "");
        jsonObj.put("numeroDocumento", isNullOrEmpty(rs.getString("numeroDocumento")) ? rs.getString("numeroDocumento") : "");
        jsonObj.put("fechaIngreso", isNullOrEmpty(rs.getString("fechaIngreso")) ? rs.getString("fechaIngreso") : "");
        if (json.getString("tipoLote").equals("A")) {
          jsonObj.put("sueldoEscalafon", isNullOrEmpty(rs.getString("sueldoEscalafon")) ? CurrencyFormat.getCustomCurrency(rs.getDouble("sueldoEscalafon")) : "0.00");
          jsonObj.put("sueldoMensual", isNullOrEmpty(rs.getString("sueldoMensual")) ? CurrencyFormat.getCustomCurrency(rs.getDouble("sueldoMensual")) : "0.00");
          jsonObj.put("sueldoPresidencia", isNullOrEmpty(rs.getString("sueldoPresidencia")) ? CurrencyFormat.getCustomCurrency(rs.getDouble("sueldoPresidencia")) : "0.00");
        } else if (json.getString("tipoLote").equals("D")) {
          jsonObj.put("costoMensualDocente", isNullOrEmpty(rs.getString("costoMensualDocente")) ? CurrencyFormat.getCustomCurrency(rs.getDouble("costoMensualDocente")) : "0.00");
          jsonObj.put("costoaDocente", isNullOrEmpty(rs.getString("costoaDocente")) ? CurrencyFormat.getCustomCurrency(rs.getDouble("costoaDocente")) : "0.00");
          jsonObj.put("costobDocente", isNullOrEmpty(rs.getString("costobDocente")) ? CurrencyFormat.getCustomCurrency(rs.getDouble("costobDocente")) : "0.00");
          jsonObj.put("costocDocente", isNullOrEmpty(rs.getString("costocDocente")) ? CurrencyFormat.getCustomCurrency(rs.getDouble("costocDocente")) : "0.00");
        }

        jsonArrListarFichasGeneral.put(jsonObj);
      }

      JSONObject jsonObj = new JSONObject();
      jsonObj.put("fichas", jsonArrListarFichasGeneral);
      jsonObj.put("count", jsonArrListarFichasGeneral.length());

      response.setStatus(true);
      response.setMessage("Se listo correctamente los datos de la ficha.");
      response.setData(jsonObj);

    } catch (SQLException e) {
      System.err.println("listarFichasGeneral -> Error: " + e.getMessage() + " Error Code: [" + e.getErrorCode() + "]");
      response.setStatus(false);
      response.setMessage("Error: " + e.getMessage() + " Error Code: [" + e.getErrorCode() + "]");
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

    jsonObjListarFichasGeneral = new JSONObject(response);
    return jsonObjListarFichasGeneral;
  }

  private static boolean isNullOrEmpty(String param) {
    if (param == null) {
      return false;
    }

    if (param.trim().length() == 0) {
      return false;
    }

    return true;
  }

  @Override
  public JSONObject listarDetalleFicha(JSONObject json) {
    JSONObject jsonObj = new JSONObject();
    PreparedStatement ps = null;
    ResultSet rs = null;
    Connection cnx = null;
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    try {
      cnx = SqlserverDAOFactory.obtenerConexion(base);
    } catch (Exception e) {
    }
    return jsonObj;
  }

}
