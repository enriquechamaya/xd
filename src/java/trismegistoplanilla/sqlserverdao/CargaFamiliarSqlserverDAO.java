package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONObject;
import trismegistoplanilla.beans.CargaFamiliarBean;
import trismegistoplanilla.beans.PersonaBean;
import trismegistoplanilla.dao.CargaFamiliarDAO;
import trismegistoplanilla.utilities.ResponseHelper;

public class CargaFamiliarSqlserverDAO implements CargaFamiliarDAO {

  @Override
  public JSONObject validarExistenciaNumeroDocumento(CargaFamiliarBean cargaFamiliar) {
    JSONObject jsonValidarExistenciaNumeroDocumento = null;
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    int existeNumeroDocumentoFamiliar = 0;
    try {
      connection = SqlserverDAOFactory.obtenerConexion(base);
      String sql
              = "select count(1) existeNumeroDocumentoFamiliar "
              + "from carga_familiar "
              + "where numero_documento = ? and estado_registro = 1";
      ps = connection.prepareStatement(sql);
      ps.setString(1, cargaFamiliar.getNumeroDocumento());
      rs = ps.executeQuery();
      rs.next();
      existeNumeroDocumentoFamiliar = rs.getInt("existeNumeroDocumentoFamiliar");
      if (existeNumeroDocumentoFamiliar > 0) {
        response.setStatus(false);
        response.setMessage("Error, el número de documento que acaba de ingresar, ya se encuentra registrado en el sistema.");
      } else if (existeNumeroDocumentoFamiliar == 0) {
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
        if (connection != null) {
          connection.close();
        }
      } catch (SQLException e) {
        System.err.println("Error: ha ocurrido un error al intentar cerrar las conexiones y/o liberacion de recursos -> " + e.getMessage());
      }
    }

    jsonValidarExistenciaNumeroDocumento = new JSONObject(response);
    return jsonValidarExistenciaNumeroDocumento;
  }

  @Override
  public JSONObject obtenerCargaFamiliarPorPersona(PersonaBean persona) {
    JSONObject jsonObjObtenerCargaFamiliarPorPersona = null;
    JSONArray jsonArrayObtenerCargaFamiliarPorPersona = new JSONArray();
    ResponseHelper response = new ResponseHelper();
    PreparedStatement ps = null;
    ResultSet rs = null;
    Connection cnx = null;
    String base = "planillabd";
    try {
      cnx = SqlserverDAOFactory.obtenerConexion(base);
      String sql
              = "SELECT "
              + "cargafamiliar.apellido_paterno apellidoPaterno , "
              + "cargafamiliar.apellido_materno apellidoMaterno , "
              + "cargafamiliar.nombre nombre, "
              + "parentesco.nombre parentesco, "
              + "Format(cargafamiliar.fecha_nacimiento, 'dd/MM/yyyy') fechaNacimiento , "
              + "(cast(datediff(dd,cargafamiliar.fecha_nacimiento,getdate()) / 365.25 as int)) edad, "
              + "tipodocumento.descripcion_larga tipoDocumentoDescripcionLarga, "
              + "tipodocumento.descripcion_corta tipoDocumentoDescripcionCorta, "
              + "cargafamiliar.numero_documento numeroDocumento , "
              + "Isnull(cargafamiliar.telefono, '-') telefono "
              + "FROM carga_familiar cargafamiliar "
              + "INNER JOIN parentesco parentesco ON parentesco.codigo_parentesco = cargafamiliar.codigo_parentesco "
              + "INNER JOIN tipo_documento tipodocumento ON tipodocumento.codigo_tipo_documento = cargafamiliar.codigo_tipo_documento "
              + "WHERE  cargafamiliar.codigo_persona = ?";
      ps = cnx.prepareStatement(sql);
      ps.setInt(1, persona.getCodigoPersona());
      rs = ps.executeQuery();
      while (rs.next()) {
        CargaFamiliarBean cargaFamiliar = new CargaFamiliarBean();
        cargaFamiliar.setApellidoPaterno(rs.getString("apellidoPaterno"));
        cargaFamiliar.setApellidoMaterno(rs.getString("apellidoMaterno"));
        cargaFamiliar.setNombre(rs.getString("nombre"));
        cargaFamiliar.setNombreParentesco(rs.getString("parentesco"));
        cargaFamiliar.setFechaNacimiento(rs.getString("fechaNacimiento"));
        cargaFamiliar.setEdad(rs.getString("edad"));
        cargaFamiliar.setNombreTipoDocumentoDescripcionLarga(rs.getString("tipoDocumentoDescripcionLarga"));
        cargaFamiliar.setNombreTipoDocumentoDescripcionCorta(rs.getString("tipoDocumentoDescripcionCorta"));
        cargaFamiliar.setNumeroDocumento(rs.getString("numeroDocumento"));
        cargaFamiliar.setTelefono(rs.getString("telefono"));
        JSONObject jsonObjCargaFamiliar = new JSONObject(cargaFamiliar);
        jsonArrayObtenerCargaFamiliarPorPersona.put(jsonObjCargaFamiliar);
      }

      JSONObject jsonObjCargaFamiliar = new JSONObject();
      jsonObjCargaFamiliar.put("cargafamiliar", jsonArrayObtenerCargaFamiliarPorPersona);

      response.setStatus(true);
      response.setMessage("Se ha listado la carga familiar correctamente");
      response.setData(jsonObjCargaFamiliar);

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

    jsonObjObtenerCargaFamiliarPorPersona = new JSONObject(response);
    return jsonObjObtenerCargaFamiliarPorPersona;
  }

}
