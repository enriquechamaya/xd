package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONObject;
import trismegistoplanilla.beans.FormacionAcademicaBean;
import trismegistoplanilla.beans.PersonaBean;
import trismegistoplanilla.dao.FormacionAcademicaDAO;
import trismegistoplanilla.utilities.ResponseHelper;

public class FormacionAcademicaSqlserverDAO implements FormacionAcademicaDAO {

  @Override
  public JSONObject obtenerFormacionAcademicaPorPersona(PersonaBean persona) {
    JSONObject jsonObjObtenerFormacionAcademicaPorPersona = null;
    JSONArray jsonArrayObtenerFormacionAcademicaPorPersona = new JSONArray();
    ResponseHelper response = new ResponseHelper();
    PreparedStatement ps = null;
    ResultSet rs = null;
    Connection cnx = null;
    String base = "planillabd";
    try {
      cnx = SqlserverDAOFactory.obtenerConexion(base);
      String sql
              = "SELECT "
              + "formacionacademica.nombre_centro_estudio centroEstudios, "
              + "carreraprofesional.nombre carreraProfesional, "
              + "nivelestudio.nombre nivelEstudio, "
              + "estadoestudio.nombre estadoEstudio, "
              + "Format(formacionacademica.fecha_inicio, 'dd/MM/yyyy') fechaInicio, "
              + "Format(formacionacademica.fecha_fin, 'dd/MM/yyyy') fechaFin, "
              + "Isnull(formacionacademica.documento_adjunto, 'NO TIENE DOCUMENTO ADJUNTO') documentoAdjunto,"
              + "formacionacademica.carrera_profesional carreraProfesional, "
              + "formacionacademica.sector_institucion sectorInstitucion, "
              + "Isnull(formacionacademica.numero_colegiatura, '-') numeroColegiatura, "
              + "Isnull(formacionacademica.observacion, '-') observacion "
              + "FROM formacion_academica formacionacademica "
              + "INNER JOIN carrera_profesional carreraprofesional ON carreraprofesional.codigo_carrera_profesional = formacionacademica.codigo_carrera_profesional "
              + "INNER JOIN nivel_estado nivelestado ON nivelestado.codigo_nivel_estado = formacionacademica.codigo_nivel_estado "
              + "INNER JOIN estado_estudio estadoestudio ON estadoestudio.codigo_estado_estudio = nivelestado.codigo_estado_estudio "
              + "INNER JOIN nivel_estudio nivelestudio ON nivelestudio.codigo_nivel_estudio = nivelestado.codigo_nivel_estudio "
              + "where formacionacademica.codigo_persona = ?";
      System.out.println("SQL => " + sql);
      ps = cnx.prepareStatement(sql);
      ps.setInt(1, persona.getCodigoPersona());
      rs = ps.executeQuery();
      while (rs.next()) {
        FormacionAcademicaBean formacionAcademica = new FormacionAcademicaBean();
        formacionAcademica.setNombreCentroEstudios(rs.getString("centroEstudios"));
        formacionAcademica.setNombreCarreraProfesional(rs.getString("carreraProfesional"));
        formacionAcademica.setNivelEstudio(rs.getString("nivelEstudio"));
        formacionAcademica.setEstadoEstudio(rs.getString("estadoEstudio"));
        formacionAcademica.setFechaInicio(rs.getString("fechaInicio"));
        formacionAcademica.setFechaFin(rs.getString("fechaFin"));
        formacionAcademica.setDocumentoAdjunto(rs.getString("documentoAdjunto"));
        formacionAcademica.setCarreraProfesional(rs.getString("carreraProfesional"));
        formacionAcademica.setSectorInstitucion(rs.getString("sectorInstitucion"));
        formacionAcademica.setNumeroColegiatura(rs.getString("numeroColegiatura"));
        formacionAcademica.setObservacion(rs.getString("observacion"));
        JSONObject jsonObjFormacionAcademica = new JSONObject(formacionAcademica);
        jsonArrayObtenerFormacionAcademicaPorPersona.put(jsonObjFormacionAcademica);
      }

      JSONObject jsonObjFormacionAcademica = new JSONObject();
      jsonObjFormacionAcademica.put("formacionacademica", jsonArrayObtenerFormacionAcademicaPorPersona);

      response.setStatus(true);
      response.setMessage("Se ha listado la formacion academica correctamente");
      response.setData(jsonObjFormacionAcademica);

    } catch (SQLException e) {
      System.err.println("obtenerFormacionAcademicaPorPersona -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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

    jsonObjObtenerFormacionAcademicaPorPersona = new JSONObject(response);
    return jsonObjObtenerFormacionAcademicaPorPersona;
  }

}
