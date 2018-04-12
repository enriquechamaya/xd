package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONObject;
import trismegistoplanilla.beans.ExperienciaLaboralBean;
import trismegistoplanilla.beans.PersonaBean;
import trismegistoplanilla.dao.ExperienciaLaboralDAO;
import trismegistoplanilla.utilities.ResponseHelper;

public class ExperienciaLaboralSqlserverDAO implements ExperienciaLaboralDAO {

  @Override
  public JSONObject obtenerExperienciaLaboralPorPersona(PersonaBean persona) {
    JSONObject jsonObjObtenerExperienciaLaboralPorPersona = null;
    JSONArray jsonArrayObtenerExperienciaLaboralPorPersona = new JSONArray();
    ResponseHelper response = new ResponseHelper();
    PreparedStatement ps = null;
    ResultSet rs = null;
    Connection cnx = null;
    String base = "planillabd";
    try {
      cnx = SqlserverDAOFactory.obtenerConexion(base);
      String sql
              = "SELECT "
              + "experiencialaboral.nombre_empresa nombreEmpresa, "
              + "experiencialaboral.nombre_cargo nombreCargo, "
              + "Format(experiencialaboral.fecha_inicio, 'dd/MM/yyyy') fechaInicio, "
              + "Format(experiencialaboral.fecha_fin, 'dd/MM/yyyy') fechaFin, "
              + "Isnull(experiencialaboral.telefono, '-') telefono "
              + "FROM experiencia_laboral experiencialaboral "
              + "WHERE experiencialaboral.codigo_persona = ?";
      ps = cnx.prepareStatement(sql);
      ps.setInt(1, persona.getCodigoPersona());
      rs = ps.executeQuery();
      while (rs.next()) {
        ExperienciaLaboralBean experienciaLaboral = new ExperienciaLaboralBean();
        experienciaLaboral.setNombreEmpresa(rs.getString("nombreEmpresa"));
        experienciaLaboral.setNombreCargo(rs.getString("nombreCargo"));
        experienciaLaboral.setFechaInicio(rs.getString("fechaInicio"));
        experienciaLaboral.setFechaFin(rs.getString("fechaFin"));
        experienciaLaboral.setTelefono(rs.getString("telefono"));
        JSONObject jsonObjExperienciaLaboral = new JSONObject(experienciaLaboral);
        jsonArrayObtenerExperienciaLaboralPorPersona.put(jsonObjExperienciaLaboral);
      }

      JSONObject jsonObjExperienciaLaboral = new JSONObject();
      jsonObjExperienciaLaboral.put("experiencialaboral", jsonArrayObtenerExperienciaLaboralPorPersona);

      response.setStatus(true);
      response.setMessage("Se ha listado la experiencia laboral correctamente");
      response.setData(jsonObjExperienciaLaboral);

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
        if (cnx != null) {
          cnx.close();
        }
      } catch (SQLException e) {
        System.err.println("Error: ha ocurrido un error al intentar cerrar las conexiones y/o liberacion de recursos -> " + e.getMessage());
      }
    }

    jsonObjObtenerExperienciaLaboralPorPersona = new JSONObject(response);
    return jsonObjObtenerExperienciaLaboralPorPersona;
  }

}
