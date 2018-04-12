package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONObject;
import trismegistoplanilla.beans.DetalleLoteFichaDocenteBean;
import trismegistoplanilla.beans.LoteFichaBean;
import trismegistoplanilla.dao.DetalleLoteFichaDocenteDAO;
import trismegistoplanilla.utilities.CurrencyFormat;
import trismegistoplanilla.utilities.ResponseHelper;

public class DetalleLoteFichaDocenteSqlserverDAO implements DetalleLoteFichaDocenteDAO {

  @Override
  public JSONObject listarDetalleLoteFichaDocenteDT(LoteFichaBean loteFicha) {
    JSONObject jsonObjListar = null;
    JSONArray jsonArrayListar = new JSONArray();
    Connection cnx = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String base = "planillabd";
    ResponseHelper response = new ResponseHelper();
    try {
      String sql
              = "select "
              + "ficha.codigo_ficha codigoFicha, "
              + "persona.apellido_paterno apellidoPaterno, "
              + "persona.apellido_materno apellidoMaterno, "
              + "persona.nombre nombre, "
              + "persona.numero_documento numeroDocumento, "
              + "tipo_documento.descripcion_corta tipodocumento, "
              + "format(ficha_laboral.fecha_ingreso,'dd/MM/yyyy') fechaInicio, "
              + "upper(datename(mm, ficha_laboral.fecha_ingreso)) mes, "
              + "sueldo_docente.codigo_area_cargo_tipo_pago as tipoPago, "
              + "isnull(sueldo_docente.costo_a,'') costoADocente, "
              + "isnull(sueldo_docente.costo_b,'') costoBDocente, "
              + "isnull(sueldo_docente.costo_c,'') costoCDocente, "
              + "isnull(sueldo_docente.costo_mensual,'') costoMensualDocente "
              + "FROM detalle_ficha_lote "
              + "inner join ficha_laboral ON ficha_laboral.codigo_ficha = detalle_ficha_lote.codigo_ficha "
              + "inner join ficha ON ficha.codigo_ficha = ficha_laboral.codigo_ficha "
              + "inner join persona ON persona.codigo_persona = ficha.codigo_persona "
              + "inner join dbo.tipo_documento ON dbo.tipo_documento.codigo_tipo_documento = dbo.persona.codigo_tipo_documento "
              + "left join sueldo_docente ON sueldo_docente.codigo_ficha = ficha_laboral.codigo_ficha "
              + "where detalle_ficha_lote.estado_registro = 1 "
              + "and detalle_ficha_lote.codigo_ficha_lote = ?";
      cnx = SqlserverDAOFactory.obtenerConexion(base);
      ps = cnx.prepareStatement(sql);
      ps.setInt(1, loteFicha.getCodigoFichaLote());
      rs = ps.executeQuery();
      while (rs.next()) {
        DetalleLoteFichaDocenteBean detalleLoteFichaDocente = new DetalleLoteFichaDocenteBean();
        detalleLoteFichaDocente.setCodigoFicha(rs.getInt("codigoFicha"));
        detalleLoteFichaDocente.setApellidoPaterno(rs.getString("apellidoPaterno"));
        detalleLoteFichaDocente.setApellidoMaterno(rs.getString("apellidoMaterno"));
        detalleLoteFichaDocente.setNombre(rs.getString("nombre"));
        detalleLoteFichaDocente.setNumeroDocumento(rs.getString("numeroDocumento"));
        detalleLoteFichaDocente.setTipoDocumento(rs.getString("tipodocumento"));
        detalleLoteFichaDocente.setFechaInicio(rs.getString("fechaInicio"));
        detalleLoteFichaDocente.setCodigoPago(rs.getInt("tipoPago"));
        detalleLoteFichaDocente.setMes(rs.getString("mes"));
        detalleLoteFichaDocente.setCostoa(CurrencyFormat.getCustomCurrency(rs.getDouble("costoADocente")));
        detalleLoteFichaDocente.setCostob(CurrencyFormat.getCustomCurrency(rs.getDouble("costoBDocente")));
        detalleLoteFichaDocente.setCostoc(CurrencyFormat.getCustomCurrency(rs.getDouble("costoCDocente")));
        detalleLoteFichaDocente.setCostoMensual(CurrencyFormat.getCustomCurrency(rs.getDouble("costoMensualDocente")));
        JSONObject jsonObjDetalleLoteFichaDocente = new JSONObject(detalleLoteFichaDocente);
        jsonArrayListar.put(jsonObjDetalleLoteFichaDocente);
      }

      JSONObject jsonObjDetalleLoteFichaDocente = new JSONObject();
      jsonObjDetalleLoteFichaDocente.put("fichadocentes", jsonArrayListar);

      response.setStatus(true);
      response.setMessage("Se ha listado las fichas de docentes correctamente!");
      response.setData(jsonObjDetalleLoteFichaDocente);

    } catch (SQLException e) {
      System.err.println("listarDetalleLoteFichaDocenteDT -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
    jsonObjListar = new JSONObject(response);
    return jsonObjListar;
  }

  @Override
  public JSONObject registrarSueldosPresidenciaLoteDocente(JSONObject data) {
    JSONObject jsonObjRegistrarSueldoPresidencia = null;
    Connection cnx = null;
    PreparedStatement psDesactivarEstadoSueldoDocente = null,
            psInsertarSueldoDocente = null,
            psDesactivarEstadoLote = null,
            psInsertarEstadoLote = null,
            psDesactivarEstadoFicha = null,
            psInsertarEstadoFicha = null;
    String base = "planillabd";
    ResponseHelper response = new ResponseHelper();
    String sql = "";
    JSONArray jsonArrayFichas = data.getJSONArray("fichas");
    int longitudJsonArrayFichas = jsonArrayFichas.length();
//    System.out.println("jsonArrayFichas >>>> " + jsonArrayFichas);
//    System.out.println("longitudFicha >>>> " + longitudJsonArrayFichas);
    try {
      cnx = SqlserverDAOFactory.obtenerConexion(base);
      cnx.setAutoCommit(false);
      sql
              = "update sueldo_docente "
              + "set estado_registro = 0 "
              + "where sueldo_docente.estado_registro = 1 "
              + "and codigo_ficha = ?";
      psDesactivarEstadoSueldoDocente = cnx.prepareStatement(sql);
      System.out.println("SQL desactivar estado sueldo_docente >>>> " + sql);

      for (int i = 0; i < longitudJsonArrayFichas; i++) {
        psDesactivarEstadoSueldoDocente.setInt(1, jsonArrayFichas.getJSONObject(i).getInt("codigoFicha"));
        psDesactivarEstadoSueldoDocente.addBatch();
      }
      int resultadoDesactivarEstadoSueldoDocente[] = psDesactivarEstadoSueldoDocente.executeBatch();
      int totalFilasDesactivarEstadoSueldoDocente = resultadoDesactivarEstadoSueldoDocente.length;
      System.out.println("totalFilasDesactivarEstadoSueldoDocente >>>> " + totalFilasDesactivarEstadoSueldoDocente);
      if (totalFilasDesactivarEstadoSueldoDocente != 0) {
        sql
                = "insert into sueldo_docente "
                + "(codigo_ficha, codigo_area_cargo_tipo_pago, "
                + "costo_mensual, costo_a, costo_b, costo_c, "
                + "observacion, fecha_registro, estado_registro) "
                + "values (?, ?, ?, ?, ?, ?, LTRIM(RTRIM(UPPER(?))), getdate(), 1)";
        psInsertarSueldoDocente = cnx.prepareStatement(sql);
        System.out.println("SQL insertar sueldo_docente >>>> " + sql);

        for (int i = 0; i < longitudJsonArrayFichas; i++) {
          int tipoPago = jsonArrayFichas.getJSONObject(i).getInt("codigoPago");
          psInsertarSueldoDocente.setInt(1, jsonArrayFichas.getJSONObject(i).getInt("codigoFicha"));
          psInsertarSueldoDocente.setInt(2, tipoPago);
          if (tipoPago == 5) { // Si el pago es horas, costo mensual es null
            psInsertarSueldoDocente.setObject(3, null);
          } else {
            psInsertarSueldoDocente.setString(3, jsonArrayFichas.getJSONObject(i).getString("costoMensual"));
          }
          if (tipoPago == 6) { // Si el pago es mensual, costo por secciones es null
            psInsertarSueldoDocente.setObject(4, null);
            psInsertarSueldoDocente.setObject(5, null);
            psInsertarSueldoDocente.setObject(6, null);
          } else {
            psInsertarSueldoDocente.setString(4, jsonArrayFichas.getJSONObject(i).getString("costoa"));
            psInsertarSueldoDocente.setString(5, jsonArrayFichas.getJSONObject(i).getString("costob"));
            psInsertarSueldoDocente.setString(6, jsonArrayFichas.getJSONObject(i).getString("costoc"));
          }
          psInsertarSueldoDocente.setString(7, jsonArrayFichas.getJSONObject(i).getString("observacion"));
          psInsertarSueldoDocente.addBatch();
        }

        int resultadoInsertarSueldoDocente[] = psInsertarSueldoDocente.executeBatch();
        int totalFilasInsetarSueldoDocente = resultadoInsertarSueldoDocente.length;
        System.out.println("totalFilasInsetarSueldoDocente >>>> " + totalFilasInsetarSueldoDocente);
        if (totalFilasInsetarSueldoDocente != 0) {

          sql
                  = "update estado_ficha_lote "
                  + "set estado_registro = 0 "
                  + "where codigo_ficha_lote = ? "
                  + "and estado_ficha_lote.estado_registro = 1";
          psDesactivarEstadoLote = cnx.prepareStatement(sql);
          System.out.println("SQL desactivar estado_ficha_lote >>>> " + sql);
          psDesactivarEstadoLote.setInt(1, data.getInt("codigoLote"));
          int resultadoDesactivarEstadoLote = psDesactivarEstadoLote.executeUpdate();
          System.out.println("resultadoDesactivarEstadoLote >>>> " + resultadoDesactivarEstadoLote);
          if (resultadoDesactivarEstadoLote != 0) {
            sql
                    = "insert into estado_ficha_lote "
                    + "(codigo_ficha_lote, "
                    + "codigo_tipo_estado_ficha_lote, "
                    + "fecha_registro, "
                    + "codigo_usuario, "
                    + "estado_registro) "
                    + "VALUES (?,?,getdate(),?,1)";
            psInsertarEstadoLote = cnx.prepareStatement(sql);
            System.out.println("SQL insertar estado_ficha_lote >>>> " + sql);
            psInsertarEstadoLote.setInt(1, data.getInt("codigoLote"));
            if (!data.getBoolean("estadoLote")) { // No cambio los sueldos
              psInsertarEstadoLote.setInt(2, 2); // Estado completado!
            } else { // Si cambio los sueldos
              psInsertarEstadoLote.setInt(2, 3); // Completado con obs
            }

            psInsertarEstadoLote.setInt(3, data.getInt("usuario"));
            int resultadoInsertarEstadoLote = psInsertarEstadoLote.executeUpdate();
            System.out.println("resultadoInsertarEstadoLote >>>> " + resultadoInsertarEstadoLote);
            if (resultadoInsertarEstadoLote != 0) {
              sql
                      = "update estado_ficha "
                      + "set estado_registro = 0 "
                      + "where estado_ficha.estado_registro = 1 "
                      + "and estado_ficha.codigo_ficha = ?";
              psDesactivarEstadoFicha = cnx.prepareStatement(sql);
              System.out.println("SQL desactivar estado_ficha >>>> " + sql);
              for (int i = 0; i < longitudJsonArrayFichas; i++) {
                psDesactivarEstadoFicha.setInt(1, jsonArrayFichas.getJSONObject(i).getInt("codigoFicha"));
                psDesactivarEstadoFicha.addBatch();
              }
              int resultadoDesactivarEstadoFicha[] = psDesactivarEstadoFicha.executeBatch();
              int totalFilasDesactivarEstadoFicha = resultadoDesactivarEstadoFicha.length;
              System.out.println("totalFilasDesactivarEstadoFicha >>>> " + totalFilasDesactivarEstadoFicha);
              if (totalFilasDesactivarEstadoFicha != 0) {
                sql
                        = "insert into estado_ficha "
                        + "(codigo_ficha, "
                        + "codigo_tipo_estado_ficha, "
                        + "fecha_registro, "
                        + "codigo_usuario, "
                        + "estado_registro) "
                        + "VALUES (?, ? ,getdate() ,?  ,1)";
                psInsertarEstadoFicha = cnx.prepareStatement(sql);
                System.out.println("SQL insertar estado_ficha >>>> " + sql);
                for (int i = 0; i < longitudJsonArrayFichas; i++) {
                  psInsertarEstadoFicha.setInt(1, jsonArrayFichas.getJSONObject(i).getInt("codigoFicha"));
                  if (!jsonArrayFichas.getJSONObject(i).getBoolean("estadoFicha")) { // NO cambio el sueldo de la ficha
                    psInsertarEstadoFicha.setInt(2, 7); // Estado: Sueldo aprobado por presidencia
                  } else { // Si cambio el sueldo de la ficha
                    psInsertarEstadoFicha.setInt(2, 8); // Estado: Observado por presidencia: Por evaluar.
                  }

                  psInsertarEstadoFicha.setInt(3, data.getInt("usuario"));
                  psInsertarEstadoFicha.addBatch();
                }
                int resultadoInsertarEstadoFicha[] = psInsertarEstadoFicha.executeBatch();
                int totalFilasInsertarEstadoFicha = resultadoInsertarEstadoFicha.length;
                System.out.println("totalFilasInsertarEstadoFicha >>>> " + totalFilasInsertarEstadoFicha);
                if (totalFilasInsertarEstadoFicha != 0) {
                  response.setStatus(true);
                  response.setMessage("Se actualizó correctamente el lote!");
                  cnx.commit();
                } else {
                  response.setStatus(false);
                  response.setMessage("Lo sentimos, no se pudo insertar el estado de la ficha");
                  cnx.rollback();
                }
              } else {
                response.setStatus(false);
                response.setMessage("Lo sentimos, no se pudo desactivar los estados de la ficha");
                cnx.rollback();
              }

            } else {
              response.setStatus(false);
              response.setMessage("Lo sentimos, no se pudo insertar el estado del lote");
              cnx.rollback();
            }

          } else {
            response.setStatus(false);
            response.setMessage("Lo sentimos, no se pudo desactivar el estado del lote");
            cnx.rollback();
          }

        } else {
          response.setStatus(false);
          response.setMessage("Lo sentimos, no se pudo insertar los estados de los sueldos");
          cnx.rollback();
        }

      } else {
        response.setStatus(false);
        response.setMessage("Lo sentimos, no se pudo desactivar los estados de los sueldos");
        cnx.rollback();
      }

    } catch (SQLException e) {
      System.err.println("registrarSueldosPresidenciaLoteDocente -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
      response.setStatus(false);
      response.setMessage("Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
    } finally {
      try {
        if (psInsertarEstadoFicha != null) {
          psInsertarEstadoFicha.close();
        }
        if (psDesactivarEstadoFicha != null) {
          psDesactivarEstadoFicha.close();
        }
        if (psInsertarEstadoLote != null) {
          psInsertarEstadoLote.close();
        }
        if (psDesactivarEstadoLote != null) {
          psDesactivarEstadoLote.close();
        }
        if (psInsertarSueldoDocente != null) {
          psInsertarSueldoDocente.close();
        }
        if (psDesactivarEstadoSueldoDocente != null) {
          psDesactivarEstadoSueldoDocente.close();
        }
        if (cnx != null) {
          cnx.close();
        }
      } catch (SQLException e) {
        System.err.println("Error: ha ocurrido un error al intentar cerrar las conexiones y/o liberacion de recursos -> " + e.getMessage());
      }
    }

    jsonObjRegistrarSueldoPresidencia = new JSONObject(response);
    return jsonObjRegistrarSueldoPresidencia;

  }

}
