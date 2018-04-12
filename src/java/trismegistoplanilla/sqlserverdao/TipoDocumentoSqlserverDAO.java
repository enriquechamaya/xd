package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONObject;
import trismegistoplanilla.beans.TipoDocumentoBean;
import trismegistoplanilla.beans.TokenFichaBean;
import trismegistoplanilla.dao.TipoDocumentoDAO;
import trismegistoplanilla.utilities.ResponseHelper;

public class TipoDocumentoSqlserverDAO implements TipoDocumentoDAO {

    @Override
    public JSONObject listarTipoDocumento() {
        JSONObject jsonObjListarTipoDocumento = null;
        JSONArray jsonArrListarTipoDocumento = new JSONArray();
        ResponseHelper response = new ResponseHelper();
        String base = "planillabd";
        String sql
                = "select "
                + "tipo_documento.codigo_tipo_documento as codigoTipoDocumento, "
                + "tipo_documento.descripcion_larga as descripcionLarga, "
                + "tipo_documento.descripcion_corta as descripcionCorta, "
                + "tipo_documento.longitud, "
                + "tipo_documento.tipo_entrada as tipoEntrada, "
                + "case "
                + "  when tipo_documento.estado_registro = 1 then 'ACTIVO' "
                + "  when tipo_documento.estado_registro = 0 then 'INACTIVO' "
                + "  else 'ERROR' "
                + "end as nombreEstadoRegistro "
                + "from tipo_documento "
                + "where tipo_documento.estado_registro = 1";
        System.out.println("listarTipoDocumento -> " + sql);
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conexion = SqlserverDAOFactory.obtenerConexion(base);
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                TipoDocumentoBean tipoDocumento = new TipoDocumentoBean();
                tipoDocumento.setCodigoTipoDocumento(rs.getInt("codigoTipoDocumento"));
                tipoDocumento.setDescripcionLarga(rs.getString("descripcionLarga"));
                tipoDocumento.setDescripcionCorta(rs.getString("descripcionCorta"));
                tipoDocumento.setLongitud(rs.getString("longitud"));
                tipoDocumento.setTipoEntrada(rs.getString("tipoEntrada"));
                tipoDocumento.setTipoEntrada(rs.getString("tipoEntrada"));
                JSONObject jsonObjTipoDocumento = new JSONObject(tipoDocumento);
                jsonArrListarTipoDocumento.put(jsonObjTipoDocumento);
            }
            JSONObject jsonObjTipodocumento = new JSONObject();
            jsonObjTipodocumento.put("tipodocumentos", jsonArrListarTipoDocumento);
            response.setStatus(true);
            response.setMessage("Los tipos de documentos se han listado correctamente.");
            response.setData(jsonObjTipodocumento);
        } catch (SQLException e) {
            System.err.println("listarTipoDocumento -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
        jsonObjListarTipoDocumento = new JSONObject(response);
        return jsonObjListarTipoDocumento;
    }

    @Override
    public JSONObject validarExistenciaTipoDocumento(TipoDocumentoBean tipoDocumento) {
        JSONObject jsonObjValidarExistenciaTipoDocumento = null;
        ResponseHelper response = new ResponseHelper();
        int existeTipoDocumento = 0;
        String base = "planillabd";
        String sql = ""
                + "select "
                + "count(1) existeTipoDocumento "
                + "from tipo_documento "
                + "where codigo_tipo_documento = ? and estado_registro = 1";
        System.out.println("validarExistenciaTipoDocumento -> " + sql);
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conexion = SqlserverDAOFactory.obtenerConexion(base);
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, tipoDocumento.getCodigoTipoDocumento());
            rs = ps.executeQuery();
            rs.next();
            existeTipoDocumento = rs.getInt("existeTipoDocumento");
            if (existeTipoDocumento > 0) {
                response.setStatus(true);
                response.setMessage("El tipo documento seleccionado existe.");
            } else if (existeTipoDocumento == 0) {
                response.setStatus(false);
                response.setMessage("El tipo documento seleccionado no existe.");
            }
        } catch (SQLException e) {
            System.err.println("validarExistenciaTipoDocumento -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
        jsonObjValidarExistenciaTipoDocumento = new JSONObject(response);
        return jsonObjValidarExistenciaTipoDocumento;
    }

    @Override
    public JSONObject obtenerLongitudTipoEntrdadaTipoDocumento(TipoDocumentoBean tipoDocumento) {
        JSONObject jsonObjObtenerLongitudTipoEntrdadaTipoDocumento = null;
        JSONArray jsonArrObtenerLongitudTipoEntrdadaTipoDocumento = new JSONArray();
        ResponseHelper response = new ResponseHelper();
        String base = "planillabd";
        String sql = ""
                + "select "
                + "longitud, "
                + "tipo_entrada TipoEntrada "
                + "from tipo_documento "
                + "where codigo_tipo_documento = ? and estado_registro = 1";
        System.out.println("obtenerLongitudTipoEntrdadaTipoDocumento -> " + sql);
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conexion = SqlserverDAOFactory.obtenerConexion(base);
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, tipoDocumento.getCodigoTipoDocumento());
            rs = ps.executeQuery();
            while (rs.next()) {
                tipoDocumento.setLongitud(rs.getString("longitud"));
                tipoDocumento.setTipoEntrada(rs.getString("TipoEntrada"));
                JSONObject jsonObjTipoDocumento = new JSONObject(tipoDocumento);
                jsonArrObtenerLongitudTipoEntrdadaTipoDocumento.put(jsonObjTipoDocumento);
            }
            JSONObject jsonObjTipodocumento = new JSONObject();
            jsonObjTipodocumento.put("tipodocumentos", jsonArrObtenerLongitudTipoEntrdadaTipoDocumento);
            response.setStatus(true);
            response.setMessage("La longitud y tipo de entrada se han listado correctamente.");
            response.setData(jsonObjTipodocumento);
        } catch (SQLException e) {
            System.err.println("obtenerLongitudTipoEntrdadaTipoDocumento -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
        jsonObjObtenerLongitudTipoEntrdadaTipoDocumento = new JSONObject(response);
        return jsonObjObtenerLongitudTipoEntrdadaTipoDocumento;
    }

    @Override
    public JSONObject obtenerCodigoTipoDocumento(TokenFichaBean tf) {
        JSONObject jsonObjObtenerCodigoTipoDocumento = null;
        ResponseHelper response = new ResponseHelper();
        String base = "planillabd";
        String sql = ""
                + "select "
                + "persona.codigo_tipo_documento codigoTipoDocumento "
                + "from persona "
                + "inner join dbo.ficha ON dbo.ficha.codigo_persona = dbo.persona.codigo_persona "
                + "inner join dbo.token_ficha ON dbo.token_ficha.codigo_ficha = dbo.ficha.codigo_ficha "
                + "where token_ficha.codigo_token_ficha = ? and token_ficha.token = ? and "
                + "persona.estado_registro = 1 and "
                + "ficha.estado_registro = 1 and "
                + "token_ficha.estado_registro = 1";
        System.out.println("obtenerCodigoTipoDocumento: " + sql);
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conexion = SqlserverDAOFactory.obtenerConexion(base);
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, tf.getCodigoTokenFicha());
            ps.setString(2, tf.getToken());
            rs = ps.executeQuery();
            rs.next();
            int codigoTipoDocumento = rs.getInt("codigoTipoDocumento");
            JSONObject objTipoDocumento = new JSONObject();
            objTipoDocumento.put("getResultedKey", codigoTipoDocumento);
            if (codigoTipoDocumento > 0) {
                response.setStatus(true);
                response.setMessage("Enhorabuena!, persona y tipo documento identificado");
                response.setData(objTipoDocumento);
            } else {
                response.setStatus(false);
                response.setMessage("Error: no se encontró la identificación de la persona ni el tipo de documento registrado");
            }
        } catch (SQLException e) {
            System.err.println("obtenerCodigoTipoDocumento -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
        jsonObjObtenerCodigoTipoDocumento = new JSONObject(response);
        return jsonObjObtenerCodigoTipoDocumento;
    }
    
//    public static void main(String[] args) {
//        TipoDocumentoSqlserverDAO metodo = new TipoDocumentoSqlserverDAO();
//        TipoDocumentoBean td = new TipoDocumentoBean();
//        td.setCodigoTipoDocumento(1);
//        System.out.println(metodo.obtenerLongitudTipoEntrdadaTipoDocumento(td));
//    }
}
