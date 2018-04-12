package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONObject;
import trismegistoplanilla.beans.ExpedienteBean;
import trismegistoplanilla.beans.PersonaBean;
import trismegistoplanilla.dao.ExpedienteDAO;
import trismegistoplanilla.utilities.ResponseHelper;

public class ExpedienteSqlserverDAO implements ExpedienteDAO {

  @Override
  public JSONObject listarTipoExpedientes() {
    JSONObject jsonObjListarTipoExpediente = null;
    JSONArray jsonArrayListarTipoExpediente = new JSONArray();
    Connection cnx = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String base = "planillabd";
    ResponseHelper response = new ResponseHelper();
    try {
      cnx = SqlserverDAOFactory.obtenerConexion(base);
      String sql
              = "select "
              + "codigo_tipo_expediente codigoTipoExpediente, "
              + "nombre nombreExpediente "
              + "FROM tipo_expediente "
              + "where tipo_expediente.estado_registro = 1";
      ps = cnx.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        ExpedienteBean expediente = new ExpedienteBean();
        expediente.setCodigoTipoExpediente(rs.getInt("codigoTipoExpediente"));
        expediente.setNombreExpediente(rs.getString("nombreExpediente"));
        JSONObject jsonObjExpediente = new JSONObject(expediente);
        jsonArrayListarTipoExpediente.put(jsonObjExpediente);
      }
      JSONObject jsonObjExpediente = new JSONObject();
      jsonObjExpediente.put("expediente", jsonArrayListarTipoExpediente);
      response.setStatus(true);
      response.setMessage("Se ha listado los tipos de expediente correctamente.");
      response.setData(jsonObjExpediente);

    } catch (SQLException e) {
      System.err.println("listarTipoExpedientes -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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

    jsonObjListarTipoExpediente = new JSONObject(response);
    return jsonObjListarTipoExpediente;
  }

  @Override
  public JSONObject obtenerExpedientesPorPersona(PersonaBean persona) {
    JSONObject jsonObjObtenerExpedientesPorPersona = null;
    JSONArray jsonArrayObtenerExpedientesPorPersona = new JSONArray();
    Connection cnx = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String base = "planillabd";
    ResponseHelper response = new ResponseHelper();
    try {
      cnx = SqlserverDAOFactory.obtenerConexion(base);
      String sql
              = "select "
              + "tipo_expediente.nombre nombreExpediente "
              + "FROM expediente "
              + "inner join tipo_expediente "
              + "ON tipo_expediente.codigo_tipo_expediente = expediente.codigo_tipo_expediente "
              + "where expediente.estado_registro = 1 "
              + "and expediente.codigo_persona = ? ";
      ps = cnx.prepareStatement(sql);
      ps.setInt(1, persona.getCodigoPersona());
      rs = ps.executeQuery();
      while (rs.next()) {
        ExpedienteBean expediente = new ExpedienteBean();
        expediente.setNombreExpediente(rs.getString("nombreExpediente"));
        JSONObject jsonObjExpediente = new JSONObject(expediente);
        jsonArrayObtenerExpedientesPorPersona.put(jsonObjExpediente);
      }
      JSONObject jsonObjExpediente = new JSONObject();
      jsonObjExpediente.put("expediente", jsonArrayObtenerExpedientesPorPersona);
      response.setStatus(true);
      response.setMessage("Se obtuvo los expedientes correctamente.");
      response.setData(jsonObjExpediente);

    } catch (SQLException e) {
      System.err.println("obtenerExpedientesPorPersona -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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

    jsonObjObtenerExpedientesPorPersona = new JSONObject(response);
    return jsonObjObtenerExpedientesPorPersona;
  }

}
