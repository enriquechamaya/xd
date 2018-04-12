package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONObject;
import trismegistoplanilla.beans.TipoEstadoFichaBean;
import trismegistoplanilla.dao.TipoEstadoFichaDAO;
import trismegistoplanilla.utilities.ResponseHelper;

public class TipoEstadoFichaSqlserverDAO implements TipoEstadoFichaDAO {

  @Override
  public JSONObject listarTipoEstadoFicha() {
    JSONObject JOlistarTipoEstadoFicha = null;
    JSONArray JAlistarTipoEstadoFicha = new JSONArray();
    ResponseHelper response = new ResponseHelper();
    String base = "planillabd";
    String sql = ""
            + "select "
            + "codigo_tipo_estado_ficha codigoTipoEstadoFicha, "
            + "nombre nombre, "
            + "descripcion descripcion "
            + "from tipo_estado_ficha "
            + "where estado_registro = 1 "
            + "order by 1";
    System.out.println("SQL listarTipoEstadoFicha: " + sql);
    Connection conexion = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      conexion = SqlserverDAOFactory.obtenerConexion(base);
      ps = conexion.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        TipoEstadoFichaBean tef = new TipoEstadoFichaBean();
        tef.setCodigoTipoEstadoFicha(rs.getInt("codigoTipoEstadoFicha"));
        tef.setNombre(rs.getString("nombre"));
        tef.setDescripcion(rs.getString("descripcion"));
        JSONObject objTipoEstadoFicha = new JSONObject(tef);
        JAlistarTipoEstadoFicha.put(objTipoEstadoFicha);
      }
      JSONObject objTipoEstadoFicha = new JSONObject();
      objTipoEstadoFicha.put("tiposEstadoFicha", JAlistarTipoEstadoFicha);
      response.setStatus(true);
      response.setMessage("Los tipos de estado de ficha se listaron correctamente");
      response.setData(objTipoEstadoFicha);
    } catch (SQLException e) {
      System.err.println("listarTipoEstadoFicha -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
    JOlistarTipoEstadoFicha = new JSONObject(response);
    return JOlistarTipoEstadoFicha;
  }

//  public static void main(String[] args) {
//    TipoEstadoFichaSqlserverDAO metodo = new TipoEstadoFichaSqlserverDAO();
//    System.out.println(metodo.listarTipoEstadoFicha());
//  }
}
