package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.json.JSONArray;
import org.json.JSONObject;
import trismegistoplanilla.beans.LoteAprobacionBean;
import trismegistoplanilla.beans.ReporteFichasAprobadasBean;
import trismegistoplanilla.dao.LoteAprobacionDAO;
import trismegistoplanilla.utilities.CurrencyFormat;
import trismegistoplanilla.utilities.ResponseHelper;

public class LoteAprobacionSqlserverDAO implements LoteAprobacionDAO {

  @Override
  public JSONObject rechazarFicha(JSONObject json) {
    JSONObject jsonRechazarFicha = null;
    Connection cnx = null;
    PreparedStatement psDesactivarUltimoEstadoFicha = null,
      psRegistarEstadoFicha = null,
      psRegistrarObservacion = null;
    ResultSet rsRegistarEstadoFicha = null;
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd", sql;
    int resultado;
    try {
      cnx = SqlserverDAOFactory.obtenerConexion(base);
      sql = "update estado_ficha set estado_registro = 0 where codigo_ficha = ? and estado_registro = 1";
      psDesactivarUltimoEstadoFicha = cnx.prepareStatement(sql);
      psDesactivarUltimoEstadoFicha.setInt(1, json.getInt("codigoFicha"));
      resultado = psDesactivarUltimoEstadoFicha.executeUpdate();
      if (resultado > 0) {
        sql = "insert into estado_ficha (codigo_ficha ,codigo_tipo_estado_ficha ,fecha_registro ,codigo_usuario ,estado_registro) "
          + "VALUES (? ,12 ,getdate() ,? ,1)";
        psRegistarEstadoFicha = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        psRegistarEstadoFicha.setInt(1, json.getInt("codigoFicha"));
        psRegistarEstadoFicha.setInt(2, json.getInt("usuario"));
        resultado = psRegistarEstadoFicha.executeUpdate();
        if (resultado > 0) {
          rsRegistarEstadoFicha = psRegistarEstadoFicha.getGeneratedKeys();
          rsRegistarEstadoFicha.next();
          int codigoEstadoFicha = rsRegistarEstadoFicha.getInt(1);
          sql = "insert into observacion_ficha (codigo_estado_ficha ,codigo_ficha ,observacion ,estado_registro) VALUES (? ,? ,? ,1)";
          psRegistrarObservacion = cnx.prepareStatement(sql);
          psRegistrarObservacion.setInt(1, codigoEstadoFicha);
          psRegistrarObservacion.setInt(2, json.getInt("codigoFicha"));
          psRegistrarObservacion.setString(3, json.getString("observacion"));
          resultado = psRegistrarObservacion.executeUpdate();
          if (resultado > 0) {
            cnx.commit();
            response.setStatus(true);
            response.setMessage("Se rechazó la ficha satisfactoriamente.");
          } else {
            cnx.rollback();
            response.setStatus(false);
            response.setMessage("No se pudo insertar observacion");
          }
        } else {
          cnx.rollback();
          response.setStatus(false);
          response.setMessage("No se pudo insertar insertar el estado_ficha");
        }
      } else {
        cnx.rollback();
        response.setStatus(false);
        response.setMessage("No se pudo desactivar el estado de estado_ficha");
      }

    } catch (SQLException e) {
      System.err.println("obtenerCodigoFicha -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
      response.setStatus(false);
      response.setMessage("Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
    } finally {
      try {
        if (rsRegistarEstadoFicha != null) {
          rsRegistarEstadoFicha.close();
        }
        if (psRegistrarObservacion != null) {
          psRegistrarObservacion.close();
        }
        if (psRegistarEstadoFicha != null) {
          psRegistarEstadoFicha.close();
        }
        if (psDesactivarUltimoEstadoFicha != null) {
          psDesactivarUltimoEstadoFicha.close();
        }
        if (cnx != null) {
          cnx.close();
        }
      } catch (SQLException e) {
        System.err.println("Error: ha ocurrido un error al intentar cerrar las conexiones y/o liberacion de recursos -> " + e.getMessage());
      }
    }

    jsonRechazarFicha = new JSONObject(response);
    return jsonRechazarFicha;
  }

  @Override
  public JSONObject aprobarFichaAdministrativa(JSONObject json) {
    JSONObject jsonAprobarFichaAdministrativa = null;
    Connection cnx = null;
    PreparedStatement psDesactivarUltimoEstadoSueldoAdministrativo = null,
      psRegistrarSueldoAdministrativo = null,
      psDesactivarUltimoEstadoFicha = null,
      psRegistrarEstadoFicha = null;
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd", sql;
    int resultado;
    System.out.println(json);
    try {
      cnx = SqlserverDAOFactory.obtenerConexion(base);
      cnx.setAutoCommit(false);
      sql = "update sueldo_administrativo set estado_registro = 0 where codigo_ficha = ? and estado_registro = 1";
      System.out.println("SQL >>>> " + sql);
      psDesactivarUltimoEstadoSueldoAdministrativo = cnx.prepareStatement(sql);
      psDesactivarUltimoEstadoSueldoAdministrativo.setInt(1, json.getInt("codigoFicha"));
      resultado = psDesactivarUltimoEstadoSueldoAdministrativo.executeUpdate();
      System.out.println("resultado >>>> " + resultado);

      if (resultado > 0) {
        sql = "insert into sueldo_administrativo (codigo_ficha, codigo_area_cargo_tipo_pago, sueldo_escalafon, sueldo_mensual, "
          + "sueldo_presidencia, observacion, fecha_registro, estado_registro) "
          + "VALUES (? ,? ,? ,? ,? ,? ,getdate() ,1)";
        System.out.println("SQL >>>> " + sql);
        psRegistrarSueldoAdministrativo = cnx.prepareStatement(sql);
        psRegistrarSueldoAdministrativo.setInt(1, json.getInt("codigoFicha"));
        psRegistrarSueldoAdministrativo.setInt(2, json.getInt("codigoAreaCargoTipoPago"));
        psRegistrarSueldoAdministrativo.setString(3, json.getString("sueldoEscalafon"));
        psRegistrarSueldoAdministrativo.setString(4, json.getString("sueldoMensual"));
        psRegistrarSueldoAdministrativo.setString(5, json.getString("sueldoPresidencia"));
        if (!json.getBoolean("observado")) { // Si no fue observada, este será aprobada
          psRegistrarSueldoAdministrativo.setString(6, json.getString("observacion"));
        } else {
          psRegistrarSueldoAdministrativo.setString(6, json.getString("observacion"));
        }

        resultado = psRegistrarSueldoAdministrativo.executeUpdate();

        System.out.println("resultado >>>> " + resultado);
        if (resultado > 0) {
          sql = "update estado_ficha set estado_registro = 0 where codigo_ficha = ? and estado_registro = 1";
          System.out.println("SQL >>>> " + sql);
          psDesactivarUltimoEstadoFicha = cnx.prepareStatement(sql);
          psDesactivarUltimoEstadoFicha.setInt(1, json.getInt("codigoFicha"));
          resultado = psDesactivarUltimoEstadoFicha.executeUpdate();
          System.out.println("resultado >>>> " + resultado);
          if (resultado > 0) {
            sql = "insert into estado_ficha (codigo_ficha ,codigo_tipo_estado_ficha ,fecha_registro ,codigo_usuario ,estado_registro) "
              + "VALUES (? ,? ,getdate() ,? ,1)";
            System.out.println("SQL >>>> " + sql);
            psRegistrarEstadoFicha = cnx.prepareStatement(sql);
            psRegistrarEstadoFicha.setInt(1, json.getInt("codigoFicha"));
            if (!json.getBoolean("observado")) { // SI NO FUE OBSERVADO (SUELDO NO CAMBIO)
              psRegistrarEstadoFicha.setInt(2, 11); // PERSONAL APROBADO
            } else {
              psRegistrarEstadoFicha.setInt(2, 8); // obser. sueldo presidencia
            }
            psRegistrarEstadoFicha.setInt(3, json.getInt("usuario"));
            resultado = psRegistrarEstadoFicha.executeUpdate();
            System.out.println("resultado >>>> " + resultado);
            if (resultado > 0) {
              cnx.commit();
              response.setStatus(true);
              response.setMessage("Se aprobó al personal satisfactoriamente!");
            } else {
              cnx.rollback();
              response.setStatus(false);
              response.setMessage("No se pudo registrar el estado ficha");
            }
          } else {
            cnx.rollback();
            response.setStatus(false);
            response.setMessage("No se pudo desactivar el ultimo estado ficha");
          }
        } else {
          cnx.rollback();
          response.setStatus(false);
          response.setMessage("No se pudo registrar sueldo administrativo");
        }
      } else {
        cnx.rollback();
        response.setStatus(false);
        response.setMessage("No se pudo desactivar el ultimo estado sueldo administrativo");
      }
    } catch (SQLException e) {
      System.err.println("obtenerCodigoFicha -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
      response.setStatus(false);
      response.setMessage("Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
    } finally {
      try {
        if (psRegistrarEstadoFicha != null) {
          psRegistrarEstadoFicha.close();
        }
        if (psDesactivarUltimoEstadoFicha != null) {
          psDesactivarUltimoEstadoFicha.close();
        }
        if (psRegistrarSueldoAdministrativo != null) {
          psRegistrarSueldoAdministrativo.close();
        }
        if (psDesactivarUltimoEstadoSueldoAdministrativo != null) {
          psDesactivarUltimoEstadoSueldoAdministrativo.close();
        }
        if (cnx != null) {
          cnx.close();
        }
      } catch (SQLException e) {
        System.err.println("Error: ha ocurrido un error al intentar cerrar las conexiones y/o liberacion de recursos -> " + e.getMessage());
      }
    }

    jsonAprobarFichaAdministrativa = new JSONObject(response);
    return jsonAprobarFichaAdministrativa;
  }

  @Override
  public JSONObject aprobarFichaDocente(JSONObject json) {
    JSONObject jsonAprobarFichaDocente = null;
    Connection cnx = null;
    PreparedStatement psDesactivarUltimoEstadoSueldoDocente = null,
      psRegistrarSueldoDocente = null,
      psDesactivarUltimoEstadoFicha = null,
      psRegistrarEstadoFicha = null;
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd", sql;
    int resultado;

    try {
      cnx = SqlserverDAOFactory.obtenerConexion(base);
      cnx.setAutoCommit(false);
      sql = "update sueldo_docente set estado_registro = 0 where codigo_ficha = ? and estado_registro = 1";
      psDesactivarUltimoEstadoSueldoDocente = cnx.prepareStatement(sql);
      psDesactivarUltimoEstadoSueldoDocente.setInt(1, json.getInt("codigoFicha"));
      resultado = psDesactivarUltimoEstadoSueldoDocente.executeUpdate();
      if (resultado > 0) {
        sql = "insert into sueldo_docente (codigo_ficha,codigo_area_cargo_tipo_pago,costo_mensual, "
          + "costo_a,costo_b,costo_c,fecha_registro,estado_registro,observacion) "
          + "VALUES (?,?,?,?,?,?,getdate(),1,?)";
        psRegistrarSueldoDocente = cnx.prepareStatement(sql);
        psRegistrarSueldoDocente.setInt(1, json.getInt("codigoFicha"));
        psRegistrarSueldoDocente.setInt(2, json.getInt("codigoAreaCargoTipoPago"));

        if (json.getString("tipoPago").equals("HORAS")) { // si es horas, el pago mensual es null
          psRegistrarSueldoDocente.setObject(3, null);
        } else {
          psRegistrarSueldoDocente.setString(3, json.getString("costoMensual"));
        }

        if (json.getString("tipoPago").equals("MENSUAL")) { // Si el pago es mensual, el costo por secciones es null
          psRegistrarSueldoDocente.setObject(4, null);
          psRegistrarSueldoDocente.setObject(5, null);
          psRegistrarSueldoDocente.setObject(6, null);
        } else {
          psRegistrarSueldoDocente.setString(4, json.getString("costoa"));
          psRegistrarSueldoDocente.setString(5, json.getString("costob"));
          psRegistrarSueldoDocente.setString(6, json.getString("costoc"));
        }

        psRegistrarSueldoDocente.setString(7, "");

        resultado = psRegistrarSueldoDocente.executeUpdate();

        if (resultado > 0) {
          sql = "update estado_ficha set estado_registro = 0 where codigo_ficha = ? and estado_registro = 1";
          psDesactivarUltimoEstadoFicha = cnx.prepareStatement(sql);
          psDesactivarUltimoEstadoFicha.setInt(1, json.getInt("codigoFicha"));
          resultado = psDesactivarUltimoEstadoFicha.executeUpdate();
          if (resultado > 0) {
            sql = "insert into estado_ficha (codigo_ficha ,codigo_tipo_estado_ficha ,fecha_registro ,codigo_usuario ,estado_registro) "
              + "VALUES (? ,? ,getdate() ,? ,1)";
            psRegistrarEstadoFicha = cnx.prepareStatement(sql);
            psRegistrarEstadoFicha.setInt(1, json.getInt("codigoFicha"));
            psRegistrarEstadoFicha.setInt(2, 11); // personal aprobado
            psRegistrarEstadoFicha.setInt(3, json.getInt("usuario"));
            resultado = psRegistrarEstadoFicha.executeUpdate();
            if (resultado > 0) {
              cnx.commit();
              response.setStatus(true);
              response.setMessage("Se aprobó al personal satisfactoriamente!");
            } else {
              cnx.rollback();
              response.setStatus(false);
              response.setMessage("No se pudo registrar el estado ficha");
            }
          } else {
            cnx.rollback();
            response.setStatus(false);
            response.setMessage("No se pudo desactivar el ultimo estado ficha");
          }
        } else {
          cnx.rollback();
          response.setStatus(false);
          response.setMessage("No se pudo registrar sueldo administrativo");
        }
      } else {
        cnx.rollback();
        response.setStatus(false);
        response.setMessage("No se pudo desactivar el ultimo estado sueldo administrativo");
      }
    } catch (SQLException e) {
      System.err.println("obtenerCodigoFicha -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
      response.setStatus(false);
      response.setMessage("Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
    } finally {
      try {
        if (psRegistrarEstadoFicha != null) {
          psRegistrarEstadoFicha.close();
        }
        if (psDesactivarUltimoEstadoFicha != null) {
          psDesactivarUltimoEstadoFicha.close();
        }
        if (psRegistrarSueldoDocente != null) {
          psRegistrarSueldoDocente.close();
        }
        if (psDesactivarUltimoEstadoSueldoDocente != null) {
          psDesactivarUltimoEstadoSueldoDocente.close();
        }
        if (cnx != null) {
          cnx.close();
        }
      } catch (SQLException e) {
        System.err.println("Error: ha ocurrido un error al intentar cerrar las conexiones y/o liberacion de recursos -> " + e.getMessage());
      }
    }

    jsonAprobarFichaDocente = new JSONObject(response);
    return jsonAprobarFichaDocente;
  }

  @Override
  public JSONObject generarLoteAprobacion(JSONObject json) {
    System.out.println(json);
    JSONObject jsonObjGenerarLote = null;
    Connection cnx = null;
    PreparedStatement psRegistrarLote = null,
      psCantidadLotesAprobados = null,
      psRegistrarDetalleLote = null,
      psRegistrarEstadoLote = null;
    ResultSet rsRegistrarLote = null,
      rsCantidadLotesAprobados = null;
    String base = "planillabd", sql, numeroLote;
    int cantidadLotes = 0, resultado = 0, codigoLoteAprobacion = 0;
    ResponseHelper response = new ResponseHelper();
    try {
      cnx = SqlserverDAOFactory.obtenerConexion(base);
      cnx.setAutoCommit(false);
      sql = "select count(1) cantidadLotes FROM lote_aprobacion";
      System.out.println("SQL >>>> " + sql);
      psCantidadLotesAprobados = cnx.prepareStatement(sql);
      rsCantidadLotesAprobados = psCantidadLotesAprobados.executeQuery();
      rsCantidadLotesAprobados.next();
      cantidadLotes = rsCantidadLotesAprobados.getInt("cantidadLotes");
      System.out.println("cantidadLotes >>>> " + cantidadLotes);
      numeroLote = numeroGenerado(cantidadLotes);
      System.out.println("numeroLote >>>> " + numeroLote);
      sql = "insert into lote_aprobacion (numero_lote,estado_registro) VALUES (?,1)";
      System.out.println("SQL >>>> " + sql);
      psRegistrarLote = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      psRegistrarLote.setString(1, numeroLote);
      resultado = psRegistrarLote.executeUpdate();
      System.out.println("resultado >>> " + resultado);
      if (resultado > 0) {
        rsRegistrarLote = psRegistrarLote.getGeneratedKeys();
        rsRegistrarLote.next();
        codigoLoteAprobacion = rsRegistrarLote.getInt(1);
        sql = "insert into detalle_lote_aprobacion (codigo_lote_aprobacion,codigo_ficha,estado_registro) VALUES (?,?,1)";
        System.out.println("SQL >>>> " + sql);
        psRegistrarDetalleLote = cnx.prepareStatement(sql);
        int longitudJson = json.getInt("count");
        System.out.println("longitud del json >>>> " + longitudJson);
        for (int i = 0; i < longitudJson; i++) {
          psRegistrarDetalleLote.setInt(1, codigoLoteAprobacion);
          psRegistrarDetalleLote.setInt(2, json.getJSONArray("data").getJSONObject(i).getInt("codigoFicha"));
          psRegistrarDetalleLote.addBatch();
        }

        int resultadoInsertarDetalleLote[] = psRegistrarDetalleLote.executeBatch();
        int totalFilasDetalle = resultadoInsertarDetalleLote.length;
        System.out.println("totalFilasDetalle >>>> " + totalFilasDetalle);
        if (totalFilasDetalle != 0) {
          sql = "insert into estado_lote_aprobacion (codigo_lote_aprobacion,fecha_registro,codigo_usuario,estado_registro) VALUES (?,getdate(),?,1)";
          System.out.println("SQL >>>>" + sql);
          psRegistrarEstadoLote = cnx.prepareStatement(sql);
          psRegistrarEstadoLote.setInt(1, codigoLoteAprobacion);
          psRegistrarEstadoLote.setInt(2, json.getInt("usuario"));
          resultado = psRegistrarEstadoLote.executeUpdate();
          System.out.println("resultado >>>> " + resultado);
          if (resultado > 0) {
            cnx.commit();
            JSONObject jsonObjKey = new JSONObject();
            jsonObjKey.put("getResultedKey", numeroLote);
            response.setMessage("Se generó el lote " + numeroLote + " correctamente");
            response.setStatus(true);
            response.setData(jsonObjKey);
          } else {
            cnx.rollback();
            response.setStatus(false);
            response.setMessage("No se pudo insertar el estato del lote de aprobacion");
          }
        } else {
          cnx.rollback();
          response.setStatus(false);
          response.setMessage("No se pudo registrar las fichas al detalle");
        }

      } else {
        cnx.rollback();
        response.setStatus(false);
        response.setMessage("No se pudo registrar lote de aprobacion");
      }

    } catch (SQLException e) {
      System.err.println("obtenerCodigoFicha -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
      response.setStatus(false);
      response.setMessage("Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
    } finally {
      try {
        if (rsCantidadLotesAprobados != null) {
          rsCantidadLotesAprobados.close();
        }
        if (rsRegistrarLote != null) {
          rsRegistrarLote.close();
        }
        if (psRegistrarEstadoLote != null) {
          psRegistrarEstadoLote.close();
        }
        if (psRegistrarDetalleLote != null) {
          psRegistrarDetalleLote.close();
        }
        if (psCantidadLotesAprobados != null) {
          psCantidadLotesAprobados.close();
        }
        if (psRegistrarLote != null) {
          psRegistrarLote.close();
        }
        if (cnx != null) {
          cnx.close();
        }
      } catch (SQLException e) {
        System.err.println("Error: ha ocurrido un error al intentar cerrar las conexiones y/o liberacion de recursos -> " + e.getMessage());
      }
    }
    jsonObjGenerarLote = new JSONObject(response);
    return jsonObjGenerarLote;

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
  public JSONObject listarFichasPresidenciaDT(String draw, String start, String length, JSONObject json) {
    JSONObject jsonObjListarFichas = new JSONObject();
    JSONArray data = new JSONArray();
    String base = "planillabd", sql;
    PreparedStatement ps = null, psCantidad = null;
    ResultSet rs = null, rsCantidad = null;
    Connection cnx = null;
    int cantidadLotes;
    int item = Integer.parseInt(start) + 1;
    try {
      cnx = SqlserverDAOFactory.obtenerConexion(base);
      sql = "select top " + length + " "
        + "lote_aprobacion.codigo_lote_aprobacion codigoLote, "
        + "lote_aprobacion.numero_lote numeroLote, "
        + "format(estado_lote_aprobacion.fecha_registro,'dd/MM/yyyy') fechaRegistro, "
        + "( "
        + "select count(1) "
        + "from detalle_lote_aprobacion "
        + "where detalle_lote_aprobacion.codigo_lote_aprobacion = lote_aprobacion.codigo_lote_aprobacion "
        + ") cantidadFichas "
        + "FROM lote_aprobacion "
        + "inner join estado_lote_aprobacion ON estado_lote_aprobacion.codigo_lote_aprobacion = lote_aprobacion.codigo_lote_aprobacion "
        + "where estado_lote_aprobacion.estado_registro = 1 "
        + "and lote_aprobacion.estado_registro = 1 "
        + "and lote_aprobacion.codigo_lote_aprobacion not in "
        + "( "
        + "select top " + start + " l.codigo_lote_aprobacion from lote_aprobacion l "
        + "inner join estado_lote_aprobacion el ON el.codigo_lote_aprobacion = l.codigo_lote_aprobacion "
        /*+ "where l.codigo_lote_aprobacion = l.codigo_lote_aprobacion "*/
        + "and el.codigo_estado_lote_aprobacion = 1 "
        + listarFichasPresidenciasDTValidacion(json) + " "
        + ") "
        + listarFichasPresidenciasDTValidacion(json);
      ps = cnx.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        LoteAprobacionBean loteAprobacion = new LoteAprobacionBean();
        loteAprobacion.setCodigoLoteAprobacion(rs.getInt("codigoLote"));
        loteAprobacion.setNumeroLote(rs.getString("numeroLote"));
        loteAprobacion.setFechaRegistro(rs.getString("fechaRegistro"));
        loteAprobacion.setCantidadFichas(rs.getString("cantidadFichas"));
        JSONObject jsonObjLoteAprobacion = new JSONObject(loteAprobacion);
        jsonObjLoteAprobacion.put("item", item++);
        data.put(jsonObjLoteAprobacion);
      }

      sql = "select count (1) cantidadLotes FROM lote_aprobacion "
        + "inner join estado_lote_aprobacion ON estado_lote_aprobacion.codigo_lote_aprobacion = lote_aprobacion.codigo_lote_aprobacion "
        + "where estado_lote_aprobacion.estado_registro = 1 "
        + listarFichasPresidenciasDTValidacion(json);
      psCantidad = cnx.prepareStatement(sql);
      rsCantidad = psCantidad.executeQuery();
      rsCantidad.next();
      cantidadLotes = rsCantidad.getInt("cantidadLotes");

      jsonObjListarFichas.put("data", data);
      jsonObjListarFichas.put("recordsFiltered", cantidadLotes);
      jsonObjListarFichas.put("recordsTotal", cantidadLotes);
      jsonObjListarFichas.put("draw", draw);

    } catch (SQLException e) {
      System.err.println("obtenerCodigoFicha -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
      jsonObjListarFichas.put("status", false);
      jsonObjListarFichas.put("message", "Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
    } finally {
      try {
        if (rsCantidad != null) {
          rsCantidad.close();
        }
        if (rs != null) {
          rs.close();
        }
        if (psCantidad != null) {
          psCantidad.close();
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

    return jsonObjListarFichas;
  }

  @Override
  public JSONObject obtenerDetalleLote(JSONObject json) {
    JSONObject jsonObtenerDetalleLote = null;
    JSONArray jsonArrayObtenerDetalleLote = new JSONArray();
    PreparedStatement ps = null;
    ResultSet rs = null;
    Connection cnx = null;
    String base = "planillabd";
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
        + "tipo_documento.descripcion_corta tipoDocumento, "
        + "persona.numero_documento numeroDocumento, "
        + "nacionalidad.gentilicio nacionalidad, "
        + "format(ficha_laboral.fecha_ingreso,'dd/MM/yyyy') fechaIngreso, "
        + "area.nombre area, "
        + "cargo.nombre cargo, "
        + "tipo_estado_ficha.nombre estado, "
        + "tipo_estado_ficha.estilo estilo "
        + "FROM detalle_lote_aprobacion "
        + "inner join ficha_laboral ON ficha_laboral.codigo_ficha = detalle_lote_aprobacion.codigo_ficha "
        + "inner join ficha ON ficha.codigo_ficha = ficha_laboral.codigo_ficha "
        + "inner join estado_ficha ON estado_ficha.codigo_ficha = ficha.codigo_ficha "
        + "inner join tipo_estado_ficha ON tipo_estado_ficha.codigo_tipo_estado_ficha = estado_ficha.codigo_tipo_estado_ficha "
        + "inner join persona ON persona.codigo_persona = ficha.codigo_persona "
        + "inner join tipo_documento ON tipo_documento.codigo_tipo_documento = persona.codigo_tipo_documento "
        + "inner join nacionalidad ON nacionalidad.codigo_nacionalidad = persona.codigo_nacionalidad "
        + "inner join area_cargo ON area_cargo.codigo_area_cargo = ficha_laboral.codigo_area_cargo "
        + "inner join area ON area.codigo_area = area_cargo.codigo_area "
        + "inner join cargo ON cargo.codigo_cargo = area_cargo.codigo_cargo "
        + "where detalle_lote_aprobacion.codigo_lote_aprobacion = ? "
        + "and estado_ficha.estado_registro = 1 "
        + "order by 1 desc";
      ps = cnx.prepareStatement(sql);
      ps.setInt(1, json.getInt("codigoLote"));
      rs = ps.executeQuery();
//      System.out.println("xd");
//      rs.last();
//      System.out.println("last row >>>> " + rs.getRow());
//      rs.beforeFirst();
//      System.out.println("first row >>>> " + rs.getRow());
      int item = 1;
      while (rs.next()) {
        JSONObject jsonObjFicha = new JSONObject();
        jsonObjFicha.put("codigoPersona", rs.getInt("codigoPersona"));
        jsonObjFicha.put("codigoFicha", rs.getInt("codigoFicha"));
        jsonObjFicha.put("apellidoPaterno", rs.getString("apellidoPaterno"));
        jsonObjFicha.put("apellidoMaterno", rs.getString("apellidoMaterno"));
        jsonObjFicha.put("nombre", rs.getString("nombre"));
        jsonObjFicha.put("tipoDocumento", rs.getString("tipoDocumento"));
        jsonObjFicha.put("numeroDocumento", rs.getString("numeroDocumento"));
        jsonObjFicha.put("nacionalidad", rs.getString("nacionalidad"));
        jsonObjFicha.put("fechaIngreso", rs.getString("fechaIngreso"));
        jsonObjFicha.put("area", rs.getString("area"));
        jsonObjFicha.put("cargo", rs.getString("cargo"));
        jsonObjFicha.put("estado", rs.getString("estado"));
        jsonObjFicha.put("estilo", rs.getString("estilo"));
        jsonObjFicha.put("item", item++);
        jsonArrayObtenerDetalleLote.put(jsonObjFicha);
      }

      JSONObject jsonObjFicha = new JSONObject();
      jsonObjFicha.put("fichas", jsonArrayObtenerDetalleLote);

      response.setStatus(true);
      response.setMessage("Se listo correctamente el detalle del lote");
      response.setData(jsonObjFicha);

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
        if (cnx != null) {
          cnx.close();
        }
      } catch (SQLException e) {
        System.err.println("Error: ha ocurrido un error al intentar cerrar las conexiones y/o liberacion de recursos -> " + e.getMessage());
      }
    }

    jsonObtenerDetalleLote = new JSONObject(response);
    return jsonObtenerDetalleLote;
  }

  private String listarFichasPresidenciasDTValidacion(JSONObject json) {
    int criterio = json.getInt("criterio");
    int filtro = json.getInt("filtro");
    String search = json.getString("search");
    String search2 = json.getString("search2");

    if (criterio == 1) {
      return " and lote_aprobacion.numero_lote = '" + search + "' ";
    }

    if (criterio == 2) {
      if (filtro == 1) {
        return " and format(estado_lote_aprobacion.fecha_registro,'dd/MM/yyyy') >= '" + search + "' ";
      }
      if (filtro == 2) {
        return " and format(estado_lote_aprobacion.fecha_registro,'dd/MM/yyyy') <= '" + search + "' ";
      }
      if (filtro == 3) {
        return " and format(estado_lote_aprobacion.fecha_registro,'dd/MM/yyyy') = '" + search + "' ";
      }
      if (filtro == 4) {
        return " and format(estado_lote_aprobacion.fecha_registro,'dd/MM/yyyy') between '" + search + "' and '" + search2 + "' ";
      }
    }

    if (criterio == 3) {
      if (filtro == 1) {
        return " and (select count(1) from detalle_lote_aprobacion where detalle_lote_aprobacion.codigo_lote_aprobacion = lote_aprobacion.codigo_lote_aprobacion) >= " + search + " ";
      }
      if (filtro == 2) {
        return " and (select count(1) from detalle_lote_aprobacion where detalle_lote_aprobacion.codigo_lote_aprobacion = lote_aprobacion.codigo_lote_aprobacion) <=  " + search + " ";
      }
      if (filtro == 3) {
        return " and (select count(1) from detalle_lote_aprobacion where detalle_lote_aprobacion.codigo_lote_aprobacion = lote_aprobacion.codigo_lote_aprobacion) =  " + search + " ";
      }
    }

    return "";
  }

  @Override
  public JSONObject imprimirFichasAprobadas(LoteAprobacionBean la) {
    JSONObject JOimprimirFichasAprobadas = null;
    JSONArray JAimprimirFichasAprobadas = new JSONArray();
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = ""
      + "select "
      + "lote_aprobacion.numero_lote numeroLote, "
      + "ficha.codigo_ficha codigoFicha, "
      + "persona.apellido_paterno apellidoPaterno, "
      + "persona.apellido_materno apellidoMaterno, "
      + "persona.nombre nombres, "
      + "tipo_documento.descripcion_corta tipoDocumento, "
      + "persona.numero_documento numeroDocumento, "
      + "convert(varchar, ficha_laboral.fecha_ingreso, 103) fechaInicio, "
      + "convert(varchar, ficha_laboral.fecha_fin, 103) fechaFin, "
      + "sede.nombre sede, "
      + "area.nombre area, "
      + "cargo.nombre cargo, "
      + "razon_social.abreviatura razonSocial, "
      + "case "
      + "when estado_ficha.codigo_tipo_estado_ficha in (10, 11) then 'APROBADO' "
      + "when estado_ficha.codigo_tipo_estado_ficha = 8 then 'EN OBSERVACIÓN' "
      + "end estado "
      + "from detalle_lote_aprobacion "
      + "inner join dbo.lote_aprobacion ON dbo.lote_aprobacion.codigo_lote_aprobacion = dbo.detalle_lote_aprobacion.codigo_lote_aprobacion "
      + "inner join dbo.ficha_laboral ON dbo.ficha_laboral.codigo_ficha = dbo.detalle_lote_aprobacion.codigo_ficha "
      + "inner join dbo.ficha ON dbo.ficha.codigo_ficha = dbo.ficha_laboral.codigo_ficha "
      + "inner join dbo.estado_ficha ON dbo.estado_ficha.codigo_ficha = dbo.ficha.codigo_ficha "
      + "inner join dbo.persona ON dbo.persona.codigo_persona = dbo.ficha.codigo_persona "
      + "inner join dbo.tipo_documento ON dbo.tipo_documento.codigo_tipo_documento = dbo.persona.codigo_tipo_documento "
      + "inner join dbo.sede_area ON dbo.sede_area.codigo_sede_area = dbo.ficha_laboral.codigo_sede_area "
      + "inner join dbo.sede ON dbo.sede.codigo_sede = dbo.sede_area.codigo_sede "
      + "inner join dbo.razon_social ON dbo.razon_social.codigo_razon_social = dbo.sede.codigo_razon_social "
      + "inner join dbo.area_cargo ON dbo.area_cargo.codigo_area_cargo = dbo.ficha_laboral.codigo_area_cargo "
      + "inner join dbo.area ON dbo.area.codigo_area = dbo.area_cargo.codigo_area "
      + "inner join dbo.cargo ON dbo.cargo.codigo_cargo = dbo.area_cargo.codigo_cargo "
      + "where "
      + "detalle_lote_aprobacion.codigo_lote_aprobacion = ? and "
      + "detalle_lote_aprobacion.estado_registro = 1 and "
      + "estado_ficha.codigo_tipo_estado_ficha in (8, 10, 11) and "
      + "estado_ficha.estado_registro = 1";
    System.out.println("SQL imprimirFichasAprobadas : " + sql);
    Connection conexion = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String numeroLote = "";
    try {
      conexion = SqlserverDAOFactory.obtenerConexion(base);
      ps = conexion.prepareStatement(sql);
      ps.setInt(1, la.getCodigoLoteAprobacion());
      rs = ps.executeQuery();
      while (rs.next()) {
        ReporteFichasAprobadasBean r = new ReporteFichasAprobadasBean();
        r.setCodigoFicha(rs.getString("codigoFicha"));
        r.setApellidoPaterno(rs.getString("apellidoPaterno"));
        r.setApellidoMaterno(rs.getString("apellidoMaterno"));
        r.setNombres(rs.getString("nombres"));
        r.setTipoDocumento(rs.getString("tipoDocumento"));
        r.setNumeroDocumento(rs.getString("numeroDocumento"));
        r.setFechaInicio(rs.getString("fechaInicio"));
        r.setFechaFin(rs.getString("fechaFin"));
        r.setSede(rs.getString("sede"));
        r.setArea(rs.getString("area"));
        r.setCargo(rs.getString("cargo"));
        r.setRazonSocial(rs.getString("razonSocial"));
        numeroLote = rs.getString("numeroLote");
        r.setEstado(rs.getString("estado"));
        JSONObject objReporte = new JSONObject(r);
        JAimprimirFichasAprobadas.put(objReporte);
      }
      JSONObject objReporte = new JSONObject();
      objReporte.put("fichasAprobadas", JAimprimirFichasAprobadas);
      objReporte.put("numeroLote", numeroLote);
      response.setStatus(true);
      response.setMessage("Las fichas aprobadas se listaron correctamente.");
      response.setData(objReporte);
    } catch (SQLException e) {
      System.err.println("imprimirFichasAprobadas -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
    JOimprimirFichasAprobadas = new JSONObject(response);
    return JOimprimirFichasAprobadas;
  }

//  public static void main(String[] args) {
//    LoteAprobacionBean la = new LoteAprobacionBean();
//    la.setCodigoLoteAprobacion(23);
//    System.out.println(new LoteAprobacionSqlserverDAO().imprimirFichasAprobadas(la));
//  }
}
