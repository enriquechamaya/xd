package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONObject;
import trismegistoplanilla.beans.TrabajadorResponsableBean;
import trismegistoplanilla.dao.TrabajadorResponsableDAO;
import trismegistoplanilla.utilities.ResponseHelper;

public class TrabajadorResponsableSqlserverDAO implements TrabajadorResponsableDAO {

  @Override
  public JSONObject registrarTrabajadorResponsable(TrabajadorResponsableBean trabajadorResponsable) {
    JSONObject jsonRegistrarTrabajadorResponsable = null;
    PreparedStatement ps = null;
    Connection cnx = null;
    String base = "planillabd";
    ResponseHelper response = new ResponseHelper();
    int resultadoRegistroTrabajadorResponsable;
    try {
      String sql
              = "insert into trabajador_responsable "
              + "(codigo_planilla_real, apellido_paterno, apellido_materno, nombre, dni, codigo_usuario, estado_registro) "
              + "values (?,LTRIM(RTRIM(UPPER(?))),LTRIM(RTRIM(UPPER(?))),LTRIM(RTRIM(UPPER(?))),?,?,1)";
      System.out.println(sql);
      cnx = SqlserverDAOFactory.obtenerConexion(base);
      cnx.setAutoCommit(false);
      ps = cnx.prepareStatement(sql);
      ps.setInt(1, trabajadorResponsable.getCodigoPlanillaReal());
      ps.setString(2, trabajadorResponsable.getApellidoPaterno());
      ps.setString(3, trabajadorResponsable.getApellidoMaterno());
      ps.setString(4, trabajadorResponsable.getNombre());
      ps.setString(5, trabajadorResponsable.getDni());
      ps.setInt(6, trabajadorResponsable.getCodigoUsuario());
      resultadoRegistroTrabajadorResponsable = ps.executeUpdate();
      if (resultadoRegistroTrabajadorResponsable == 0) {
        response.setStatus(false);
        cnx.rollback();
      } else {
        response.setStatus(true);
        cnx.commit();
      }
    } catch (SQLException e) {
      System.err.println("registrarTrabajadorResponsable -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
      response.setStatus(false);
    } finally {
      try {
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

    jsonRegistrarTrabajadorResponsable = new JSONObject(response);
    return jsonRegistrarTrabajadorResponsable;
  }

  @Override
  public JSONObject validarExistenciaTrabajadorPorDni(TrabajadorResponsableBean trabajadorResponsable) {
    JSONObject jsonRegistrarTrabajadorResponsable = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    Connection cnx = null;
    String base = "planillabd";
    ResponseHelper response = new ResponseHelper();

    int existeTrabajador;
    try {
      String sql
              = "select count(1) as existeTrabajador "
              + "from trabajador_responsable "
              + "where trabajador_responsable.dni = ? and estado_registro = 1";
      System.out.println(sql);
      cnx = SqlserverDAOFactory.obtenerConexion(base);
      ps = cnx.prepareStatement(sql);
      ps.setString(1, trabajadorResponsable.getDni());
      rs = ps.executeQuery();
      rs.next();
      existeTrabajador = rs.getInt("existeTrabajador");
      System.out.println("existeTrabajador -> " + existeTrabajador);
      if (existeTrabajador > 0) {
        response.setStatus(true);
        response.setMessage("El trabajador existe");
      } else if (existeTrabajador == 0) {
        response.setStatus(false);
        response.setMessage("El trabajador no existe");
      }

    } catch (SQLException e) {
      System.err.println("validarExistenciaTrabajadorPorDni -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
      response.setStatus(false);
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

    jsonRegistrarTrabajadorResponsable = new JSONObject(response);
    return jsonRegistrarTrabajadorResponsable;
  }

  @Override
  public JSONObject obtenerSedeAreaCargoPorCodigoPlanilla(int codigoPlanillaReal) {
    JSONObject jsonObjObtenerSedeAreaCargoPorCodigoPlanilla = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    Connection cnx = null;
    String base = "planillabd";
    ResponseHelper response = new ResponseHelper();
    try {
      String sql
              = "select "
              + "sede.nombre nombreSede, "
              + "area.nombre nombreArea, "
              + "cargo.nombre nombreCargo "
              + "from trabajador_responsable "
              + "inner join sede_area on trabajador_responsable.codigo_sede_area = sede_area.codigo_sede_area "
              + "inner join sede on sede_area.codigo_sede = sede.codigo_sede "
              + "inner join area on sede_area.codigo_area = area.codigo_area "
              + "inner join area_cargo on trabajador_responsable.codigo_area_cargo = area_cargo.codigo_area_cargo "
              + "inner join cargo on area_cargo.codigo_cargo = cargo.codigo_cargo "
              + "where trabajador_responsable.codigo_planilla_real = ?";
      cnx = SqlserverDAOFactory.obtenerConexion(base);
      ps = cnx.prepareStatement(sql);
      ps.setInt(1, codigoPlanillaReal);
      rs = ps.executeQuery();
      if (rs.next()) {
        TrabajadorResponsableBean tResponsable = new TrabajadorResponsableBean();
        tResponsable.setNombreSede(rs.getString("nombreSede"));
        tResponsable.setNombreArea(rs.getString("nombreArea"));
        tResponsable.setNombreCargo(rs.getString("nombreCargo"));
        JSONObject jsonObjTrabajadorResponsable = new JSONObject(tResponsable);
        response.setStatus(true);
        response.setData(jsonObjTrabajadorResponsable);
      }
    } catch (SQLException e) {
      System.err.println("obtenerSedeAreaCargoPorCodigoPlanilla -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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

    jsonObjObtenerSedeAreaCargoPorCodigoPlanilla = new JSONObject(response);
    return jsonObjObtenerSedeAreaCargoPorCodigoPlanilla;

  }

}
