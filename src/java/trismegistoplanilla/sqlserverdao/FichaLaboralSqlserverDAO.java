package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import pe.siso.webservicesseguridad.webservices.UsuarioBean;
import trismegistoplanilla.beans.FichaLaboralBean;
import trismegistoplanilla.beans.PersonaBean;
import trismegistoplanilla.beans.SueldoAdministrativoBean;
import trismegistoplanilla.beans.SueldoDocenteBean;
import trismegistoplanilla.dao.FichaLaboralDAO;
import trismegistoplanilla.utilities.ResponseHelper;

public class FichaLaboralSqlserverDAO implements FichaLaboralDAO {

  @Override
  public JSONObject registrarFichaLaboral(FichaLaboralBean fl, PersonaBean p, JSONArray jaExpediente, SueldoAdministrativoBean sa, SueldoDocenteBean sd, UsuarioBean u) {
    JSONObject JORegistrarFichaLaboral = null;
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = "";
    Connection conexion = SqlserverDAOFactory.obtenerConexion(base);
    PreparedStatement psObtenerCodigoPersona = null,
            psRegistrarEnlace = null,
            psRegistrarExpediente = null,
            psRegistrarFichaLaboral = null,
            psRegistrarSueldoAdministrativo = null,
            psRegistrarSueldoDocente = null,
            psObtenerUltimoEstadoFicha = null,
            psDesactivarEstadoFicha = null,
            psRegistrarEstadoFicha = null;
    ResultSet rsObtenerCodigoPersona = null,
            rsObtenerUltimoEstadoFicha = null;
    try {
      /* ========= ACTIVIDADES ==============
       1- OBTENER CODIGO PERSONA
       2- REGISTRAR ENLACE ALFRESCO
       3- REGISTRAR TIPOS DE EXPEDIENTE SELECCIONADO (ANTES VALIDAR EXISTENCIAS)
       4- REGISTRAR FICHA LABORAL
                     ||=> A = REGISTRAR SUELDO ADMINISTRATIVO
       5- TIPO FICHA:||
                     ||=> B = REGISTRAR SUELDO DOCENTE
       5.1- TIPO FICHA (D):
       - HORAS       ||     - MENSUAL      ||      -ADMINISTRATIVO
      ===============||====================||=======================
       COSTO A       ||     COSTO MENSUAL  ||      COSTO A
       COSTO B       ||                    ||      COSTO B
       COSTO C       ||                    ||      COSTO C
                     ||                    ||      COSTO MENSUAL 
       6- OBTENER ULTIMO CODIGO DE ESTADO FICHA
       7- DESACTIVAR ESTADO FICHA
       8- REGISTRA NUEVO ESTADO FICHA (COMPLETADO)
       */
      conexion.setAutoCommit(false);
      // ACTIVIDAD N° 1
      sql = "select codigo_persona codigoPersona from ficha where codigo_ficha = ? and estado_registro = 1";
      System.out.println("SQL registrarFichaLaboral (obtener codigo persona) - " + sql);
      psObtenerCodigoPersona = conexion.prepareStatement(sql);
      psObtenerCodigoPersona.setInt(1, fl.getCodigoFicha());
      rsObtenerCodigoPersona = psObtenerCodigoPersona.executeQuery();
      rsObtenerCodigoPersona.next();
      int codigoPersona = rsObtenerCodigoPersona.getInt("codigoPersona");

      // ACTIVIDAD N° 2
      sql = "update persona set enlace_alfresco = upper(?) where codigo_persona = ? and estado_registro = 1";
      System.out.println("SQL registrarFichaLaboral (registrar enlace alfresco) - " + sql);
      psRegistrarEnlace = conexion.prepareStatement(sql);
      psRegistrarEnlace.setString(1, p.getEnlaceAlfresco());
      psRegistrarEnlace.setInt(2, codigoPersona);
      int resultadoRegistrarEnlace = psRegistrarEnlace.executeUpdate();
      if (resultadoRegistrarEnlace > 0) {

        // ACTIVIDAD N° 3
        TipoExpedienteSqlserverDAO expediente = new TipoExpedienteSqlserverDAO();
        JSONObject validarExpediente = expediente.validarTipoExpedienteSeleccionadoByID(jaExpediente);
        if (validarExpediente.getBoolean("status")) {
          sql = ""
                  + "insert into expediente ( "
                  + "   codigo_persona "
                  + "  ,codigo_tipo_expediente "
                  + "  ,estado_registro "
                  + ") values ( "
                  + "   ? "
                  + "  ,? "
                  + "  ,1 "
                  + ")";
          System.out.println("SQL registrarFichaLaboral (registrar expedientes) - " + sql);
          psRegistrarExpediente = conexion.prepareStatement(sql);
          for (int i = 0; i < jaExpediente.length(); i++) {
            JSONObject objExpediente = jaExpediente.getJSONObject(i);
            psRegistrarExpediente.setInt(1, codigoPersona);
            psRegistrarExpediente.setInt(2, objExpediente.getInt("id"));
            psRegistrarExpediente.addBatch();
          }
          int executeRegistrarExpediente[] = psRegistrarExpediente.executeBatch();
          int resultadoRegistrarExpediente = executeRegistrarExpediente.length;
          if (resultadoRegistrarExpediente > 0) {

            // ACTIVIDAD N° 4
            sql = ""
                    + "insert into ficha_laboral ( "
                    + "   codigo_ficha "
                    + "  ,fecha_ingreso "
                    + "  ,fecha_fin "
                    + "  ,codigo_sede_area "
                    + "  ,codigo_area_cargo "
                    + "  ,tipo_ficha "
                    + "  ,fecha_registro "
                    + "  ,estado_registro "
                    + ") values ( "
                    + "   ? "
                    + "  ,? "
                    + "  ,? "
                    + "  ,? "
                    + "  ,? "
                    + "  ,? "
                    + "  ,getdate() "
                    + "  ,1 "
                    + ")";
            System.out.println("SQL registrarFichaLaboral (registrar ficha laboral) - " + sql);
            psRegistrarFichaLaboral = conexion.prepareStatement(sql);
            psRegistrarFichaLaboral.setInt(1, fl.getCodigoFicha());
            psRegistrarFichaLaboral.setString(2, fl.getFechaIngreso());
            psRegistrarFichaLaboral.setString(3, fl.getFechaFin());
            psRegistrarFichaLaboral.setInt(4, fl.getCodigoSedeArea());
            psRegistrarFichaLaboral.setInt(5, fl.getCodigoAreaCargo());
            psRegistrarFichaLaboral.setString(6, fl.getTipoFicha());
            int resultadoRegistrarFichaLaboral = psRegistrarFichaLaboral.executeUpdate();
            if (resultadoRegistrarFichaLaboral > 0) {

              // ACTIVIDAD N° 5
              boolean flagSueldos = false;
              if (fl.getTipoFicha().equals("A")) {
                sql = ""
                        + "insert into sueldo_administrativo ( "
                        + "   codigo_ficha "
                        + "  ,codigo_area_cargo_tipo_pago "
                        + "  ,sueldo_escalafon "
                        + "  ,sueldo_mensual "
                        + "  ,sueldo_presidencia "
                        + "  ,observacion "
                        + "  ,fecha_registro "
                        + "  ,estado_registro "
                        + ") values ( "
                        + "   ? "
                        + "  ,? "
                        + "  ,? "
                        + "  ,? "
                        + "  ,? "
                        + "  ,? "
                        + "  ,getdate() "
                        + "  ,1 "
                        + ")";
                System.out.println("SQL registrarFichaLaboral (registrar sueldo administrativo) - " + sql);
                psRegistrarSueldoAdministrativo = conexion.prepareStatement(sql);
                psRegistrarSueldoAdministrativo.setInt(1, sa.getCodigoFicha());
                psRegistrarSueldoAdministrativo.setInt(2, sa.getCodigoAreaCargoTipoPago());
                psRegistrarSueldoAdministrativo.setString(3, sa.getSueldoEscalafon());
                psRegistrarSueldoAdministrativo.setString(4, sa.getSueldoMensual());
                psRegistrarSueldoAdministrativo.setNull(5, Types.DOUBLE);
                psRegistrarSueldoAdministrativo.setString(6, sa.getObservacion());
                int resultadoRegistrarSueldoAdministrativo = psRegistrarSueldoAdministrativo.executeUpdate();
                if (resultadoRegistrarSueldoAdministrativo > 0) {
                  flagSueldos = true;
                } else {
                  response.setStatus(false);
                  response.setMessage("No se pudo registrar el sueldo administrativo");
                  conexion.rollback();
                }
              } else if (fl.getTipoFicha().equals("D")) {
                switch (sd.getCodigoAreaCargoTipoPago()) {
                  case 5: // horas
                    sql = ""
                            + "insert into sueldo_docente (codigo_ficha, codigo_area_cargo_tipo_pago, costo_a, costo_b, costo_c, observacion, fecha_registro, estado_registro) "
                            + "values (?, ?, ?, ?, ?, ?, getdate(), 1)";
                    System.out.println("SQL registrarFichaLaboral (sueldo docente) (HORAS) - " + sql);
                    psRegistrarSueldoDocente = conexion.prepareStatement(sql);
                    psRegistrarSueldoDocente.setInt(1, sd.getCodigoFicha());
                    psRegistrarSueldoDocente.setInt(2, sd.getCodigoAreaCargoTipoPago());
                    psRegistrarSueldoDocente.setString(3, sd.getCostoa());
                    psRegistrarSueldoDocente.setString(4, sd.getCostob());
                    psRegistrarSueldoDocente.setString(5, sd.getCostoc());
                    psRegistrarSueldoDocente.setString(6, sd.getObservacion());
                    break;
                  case 6: // mensual
                    sql = ""
                            + "insert into sueldo_docente (codigo_ficha, codigo_area_cargo_tipo_pago, costo_mensual, observacion, fecha_registro, estado_registro) "
                            + "values (?, ?, ?, ?, getdate(), 1)";
                    System.out.println("SQL registrarFichaLaboral (sueldo docente) (MENSUAL) - " + sql);
                    psRegistrarSueldoDocente = conexion.prepareStatement(sql);
                    psRegistrarSueldoDocente.setInt(1, sd.getCodigoFicha());
                    psRegistrarSueldoDocente.setInt(2, sd.getCodigoAreaCargoTipoPago());
                    psRegistrarSueldoDocente.setString(3, sd.getCostoMensual());
                    psRegistrarSueldoDocente.setString(4, sd.getObservacion());
                    break;
                  case 19: // administrativo
                    sql = ""
                            + "insert into sueldo_docente (codigo_ficha, codigo_area_cargo_tipo_pago, costo_mensual, costo_a, costo_b, costo_c, observacion, fecha_registro, estado_registro) "
                            + "values (?, ?, ?, ?, ?, ?, ?, getdate(), 1)";
                    System.out.println("SQL registrarFichaLaboral (sueldo docente) (ADMINISTRATIVO) - " + sql);
                    psRegistrarSueldoDocente = conexion.prepareStatement(sql);
                    psRegistrarSueldoDocente.setInt(1, sd.getCodigoFicha());
                    psRegistrarSueldoDocente.setInt(2, sd.getCodigoAreaCargoTipoPago());
                    psRegistrarSueldoDocente.setString(3, sd.getCostoMensual());
                    psRegistrarSueldoDocente.setString(4, sd.getCostoa());
                    psRegistrarSueldoDocente.setString(5, sd.getCostob());
                    psRegistrarSueldoDocente.setString(6, sd.getCostoc());
                    psRegistrarSueldoDocente.setString(7, sd.getObservacion());
                    break;
                  default:
                    response.setStatus(false);
                    response.setMessage("No se encontró un tipo de pago para el docente");
                    conexion.rollback();
                }
                int resultadoRegistrarSueldoDocente = psRegistrarSueldoDocente.executeUpdate();
                if (resultadoRegistrarSueldoDocente > 0) {
                  flagSueldos = true;
                } else {
                  response.setStatus(false);
                  response.setMessage("No se pudo registrar el sueldo del docente");
                  conexion.rollback();
                }
              }

              if (flagSueldos) {

                // ACTIVIDAD N° 6
                sql = ""
                        + "select "
                        + "codigo_estado_ficha codigoEstadoFicha "
                        + "from estado_ficha "
                        + "where codigo_ficha = ? and estado_registro = 1";
                System.out.println("SQL registrarFichaLaboral (obtener ultimo estado de ficha): " + sql);
                psObtenerUltimoEstadoFicha = conexion.prepareStatement(sql);
                psObtenerUltimoEstadoFicha.setInt(1, fl.getCodigoFicha());
                rsObtenerUltimoEstadoFicha = psObtenerUltimoEstadoFicha.executeQuery();
                rsObtenerUltimoEstadoFicha.next();
                int codigoEstadoFicha = rsObtenerUltimoEstadoFicha.getInt("codigoEstadoFicha");
                if (codigoEstadoFicha > 0) {

                  // ACTIVIDAD N° 7  
                  sql = ""
                          + "update estado_ficha "
                          + "set estado_registro = 0 "
                          + "where codigo_estado_ficha = ? and estado_registro = 1";
                  System.out.println("SQL registrarFichaLaboral (desactivar estado de ficha obtenida)(ID: " + codigoEstadoFicha + ") - " + sql);
                  psDesactivarEstadoFicha = conexion.prepareStatement(sql);
                  psDesactivarEstadoFicha.setInt(1, codigoEstadoFicha);
                  int resultadoDesactivarEstadoFicha = psDesactivarEstadoFicha.executeUpdate();
                  if (resultadoDesactivarEstadoFicha > 0) {

                    // ACTIVIDAD N° 8
                    sql = ""
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
                    System.out.println("SQL registrarFichaLaboral (registrar nuevo estado de ficha 'COMPLETADO') (ID: " + codigoEstadoFicha + ") - " + sql);
                    psRegistrarEstadoFicha = conexion.prepareStatement(sql);
                    psRegistrarEstadoFicha.setInt(1, fl.getCodigoFicha());
                    psRegistrarEstadoFicha.setInt(2, 4);
                    psRegistrarEstadoFicha.setInt(3, u.getCodigoUsuario());
                    int resultadoRegistrarEstadoFicha = psRegistrarEstadoFicha.executeUpdate();
                    if (resultadoRegistrarEstadoFicha > 0) {
                      response.setStatus(true);
                      response.setMessage("Enhorabuena!, Los datos administrativos han sido registrados correctamente");
                      conexion.commit();
                    } else {
                      response.setStatus(false);
                      response.setMessage("No se pudo registrar el nuevo estado de ficha 'COMPLETADO'");
                      conexion.rollback();
                    }
                  } else {
                    response.setStatus(false);
                    response.setMessage("No se pudo desactivar el ultimo estado de ficha");
                    conexion.rollback();
                  }
                } else {
                  response.setStatus(false);
                  response.setMessage("No se pudo obtener el código de estado de ficha");
                  conexion.rollback();
                }
              } else {
                response.setStatus(false);
                response.setMessage("Error ocurrió un problema al registrar los sueldos del personal, por favor contactarse con el área de sistemas");
                conexion.rollback();
              }
            } else {
              response.setStatus(false);
              response.setMessage("No se pudo registrar la ficha laboral");
              conexion.rollback();
            }
          } else {
            response.setStatus(false);
            response.setMessage("No se pudo registrar los expedientes");
            conexion.rollback();
          }
        } else {
          response.setStatus(false);
          response.setMessage(validarExpediente.getString("message"));
          conexion.rollback();
        }
      } else {
        response.setStatus(false);
        response.setMessage("No se pudo registrar el enlace de alfresco");
        conexion.rollback();
      }
    } catch (SQLException e) {
      System.err.println("registrarFichaLaboral -> Error: " + e.getMessage() + " Error Code: [" + e.getErrorCode() + "]");
      response.setStatus(false);
      response.setMessage("Error: " + e.getMessage() + " Error Code: [" + e.getErrorCode() + "]");
    } finally {
      try {
        if (rsObtenerCodigoPersona != null) {
          rsObtenerCodigoPersona.close();
        }
        if (rsObtenerUltimoEstadoFicha != null) {
          rsObtenerUltimoEstadoFicha.close();
        }
        if (psObtenerCodigoPersona != null) {
          psObtenerCodigoPersona.close();
        }
        if (psRegistrarEnlace != null) {
          psRegistrarEnlace.close();
        }
        if (psRegistrarExpediente != null) {
          psRegistrarExpediente.close();
        }
        if (psRegistrarFichaLaboral != null) {
          psRegistrarFichaLaboral.close();
        }
        if (psRegistrarSueldoAdministrativo != null) {
          psRegistrarSueldoAdministrativo.close();
        }
        if (psRegistrarSueldoDocente != null) {
          psRegistrarSueldoDocente.close();
        }
        if (psObtenerUltimoEstadoFicha != null) {
          psObtenerUltimoEstadoFicha.close();
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
    JORegistrarFichaLaboral = new JSONObject(response);
    return JORegistrarFichaLaboral;
  }

//  public static void main(String[] args) {
//    FichaLaboralBean fl = new FichaLaboralBean();
//    PersonaBean p = new PersonaBean();
//    JSONArray jaExpediente = new JSONArray();
//    SueldoAdministrativoBean sa = new SueldoAdministrativoBean();
//    SueldoDocenteBean sd = new SueldoDocenteBean();
//    UsuarioBean u = new UsuarioBean();
//
//    fl.setCodigoFicha(1);
//    fl.setFechaIngreso("01/01/2018");
//    fl.setFechaFin("01/06/2018");
//    fl.setCodigoSedeArea(1);
//    fl.setCodigoAreaCargo(1);
//    fl.setTipoFicha("D");
//
//    p.setEnlaceAlfresco("http://localhost:8080/alfresco/service/index/all");
//
//    for (int i = 1; i <= 5; i++) {
//      HashMap<String, Integer> x = new HashMap<>();
//      x.put("id", i);
//      JSONObject obj = new JSONObject(x);
//      jaExpediente.put(obj);
//    }
//
//    sa.setCodigoFicha(1);
//    sa.setCodigoAreaCargoTipoPago(1);
//    sa.setSueldoEscalafon("2000");
//    sa.setSueldoMensual("2300");
//    sa.setObservacion("el sueldo cambio we :v");
//
//    sd.setCodigoAreaCargoTipoPago(19);
//    sd.setCodigoFicha(1);
//    sd.setCostoMensual("3500");
//    sd.setCostoa("10");
//    sd.setCostob("15");
//    sd.setCostoc("20");
//    sd.setObservacion("el sueldo del docente cambio we :v");
//
//    u.setCodigoUsuario(6);
//
//    System.out.println(new FichaLaboralSqlserverDAO().registrarFichaLaboral(fl, p, jaExpediente, sa, sd, u));
//  }

}
