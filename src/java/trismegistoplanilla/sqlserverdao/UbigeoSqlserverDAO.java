package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONObject;
import trismegistoplanilla.beans.UbigeoBean;
import trismegistoplanilla.dao.UbigeoDAO;
import trismegistoplanilla.utilities.ResponseHelper;

public class UbigeoSqlserverDAO implements UbigeoDAO {

    @Override
    public JSONObject listarDepartamento() {
        JSONObject jsonListarDepartamento = null;
        JSONArray jsonArrayListarDepartamento = new JSONArray();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        String base = "planillabd";
        ResponseHelper response = new ResponseHelper();
        try {
            connection = SqlserverDAOFactory.obtenerConexion(base);
            String sql
                    = "select "
                    + "codigo_departamento codigoDepartamento, "
                    + "nombre_departamento nombreDepartamento "
                    + "from ubigeo "
                    + "where estado_registro = 1 "
                    + "group by codigo_departamento, nombre_departamento "
                    + "order by nombre_departamento asc";
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                UbigeoBean ubigeo = new UbigeoBean();
                ubigeo.setCodigoDepartamento(rs.getInt("codigoDepartamento"));
                ubigeo.setNombreDepartamento(rs.getString("nombreDepartamento"));
                JSONObject jsonObjUbigeo = new JSONObject(ubigeo);
                jsonArrayListarDepartamento.put(jsonObjUbigeo);
            }
            JSONObject jsonObjUbigeo = new JSONObject();
            jsonObjUbigeo.put("departamentos", jsonArrayListarDepartamento);
            response.setStatus(true);
            response.setMessage("Los departamentos se han listado correctamente.");
            response.setData(jsonObjUbigeo);

        } catch (SQLException e) {
            System.err.println("listarDepartamento -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
            response.setStatus(false);
            response.setMessage("Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                System.err.println("Error: ha ocurrido un error al intentar cerrar las conexiones y/o liberacion de recursos -> " + e.getMessage());
            }
        }

        jsonListarDepartamento = new JSONObject(response);
        return jsonListarDepartamento;
    }

    @Override
    public JSONObject listarProvincia(UbigeoBean ubigeo) {
        JSONObject jsonListarDepartamento = null;
        JSONArray jsonArrayListarDepartamento = new JSONArray();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        String base = "planillabd";
        ResponseHelper response = new ResponseHelper();
        try {
            connection = SqlserverDAOFactory.obtenerConexion(base);
            String sql
                    = "select "
                    + "codigo_provincia codigoProvincia, "
                    + "nombre_provincia nombreProvincia "
                    + "from ubigeo "
                    + "where codigo_departamento = ? "
                    + "group by codigo_provincia, nombre_provincia "
                    + "order by nombre_provincia asc";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, ubigeo.getCodigoDepartamento());
            rs = ps.executeQuery();
            while (rs.next()) {
                UbigeoBean ubigeoBean = new UbigeoBean();
                ubigeoBean.setCodigoProvincia(rs.getInt("codigoProvincia"));
                ubigeoBean.setNombreProvincia(rs.getString("nombreProvincia"));
                JSONObject jsonObjUbigeo = new JSONObject(ubigeoBean);
                jsonArrayListarDepartamento.put(jsonObjUbigeo);
            }
            JSONObject jsonObjUbigeo = new JSONObject();
            jsonObjUbigeo.put("provincias", jsonArrayListarDepartamento);
            response.setStatus(true);
            response.setMessage("Las provincias se han listado correctamente.");
            response.setData(jsonObjUbigeo);

        } catch (SQLException e) {
            System.err.println("listarProvincia -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
            response.setStatus(false);
            response.setMessage("Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                System.err.println("Error: ha ocurrido un error al intentar cerrar las conexiones y/o liberacion de recursos -> " + e.getMessage());
            }
        }

        jsonListarDepartamento = new JSONObject(response);
        return jsonListarDepartamento;
    }

    @Override
    public JSONObject listarDistrito(UbigeoBean ubigeo) {
        JSONObject jsonListarDepartamento = null;
        JSONArray jsonArrayListarDepartamento = new JSONArray();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        String base = "planillabd";
        ResponseHelper response = new ResponseHelper();
        try {
            connection = SqlserverDAOFactory.obtenerConexion(base);
            String sql
                    = "select "
                    + "codigo_distrito codigoDistrito, "
                    + "nombre_distrito nombreDistrito "
                    + "from ubigeo "
                    + "where codigo_departamento = ? and codigo_provincia = ? "
                    + "group by codigo_distrito, nombre_distrito "
                    + "order by nombre_distrito asc";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, ubigeo.getCodigoDepartamento());
            ps.setInt(2, ubigeo.getCodigoProvincia());
            rs = ps.executeQuery();
            while (rs.next()) {
                UbigeoBean ubigeoBean = new UbigeoBean();
                ubigeoBean.setCodigoDistrito(rs.getInt("codigoDistrito"));
                ubigeoBean.setNombreDistrito(rs.getString("nombreDistrito"));
                JSONObject jsonObjUbigeo = new JSONObject(ubigeoBean);
                jsonArrayListarDepartamento.put(jsonObjUbigeo);
            }
            JSONObject jsonObjUbigeo = new JSONObject();
            jsonObjUbigeo.put("distritos", jsonArrayListarDepartamento);
            response.setStatus(true);
            response.setMessage("Los distritos se han listado correctamente.");
            response.setData(jsonObjUbigeo);

        } catch (SQLException e) {
            System.err.println("listarDistrito -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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

        jsonListarDepartamento = new JSONObject(response);
        return jsonListarDepartamento;
    }

    @Override
    public JSONObject validarExistenciaDepartamento(UbigeoBean ubigeo) {
        JSONObject jsonValidarExistenciaDepartamento = null;
        ResponseHelper response = new ResponseHelper();
        int existeDepartamento = 0;
        String base = "planillabd";
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = SqlserverDAOFactory.obtenerConexion(base);
            String sql
                    = "select "
                    + "count(1) as existeDepartamento "
                    + "from ubigeo "
                    + "where codigo_departamento = ? and estado_registro = 1";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, ubigeo.getCodigoDepartamento());
            rs = ps.executeQuery();
            rs.next();
            existeDepartamento = rs.getInt("existeDepartamento");
            System.out.println(existeDepartamento);
            if (existeDepartamento > 0) {
                response.setStatus(true);
                response.setMessage("El departamento seleccionado existe.");
            } else if (existeDepartamento == 0) {
                response.setStatus(false);
                response.setMessage("El departamento seleccionado no existe.");
            }

        } catch (SQLException e) {
            System.err.println("validarExistenciaDepartamento -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
        jsonValidarExistenciaDepartamento = new JSONObject(response);
        return jsonValidarExistenciaDepartamento;
    }

    @Override
    public JSONObject validarExistenciaProvincia(UbigeoBean ubigeo) {
        JSONObject jsonValidarExistenciaProvincia = null;
        ResponseHelper response = new ResponseHelper();
        int existeProvincia = 0;
        String base = "planillabd";
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = SqlserverDAOFactory.obtenerConexion(base);
            String sql
                    = "select "
                    + "count(1) as existeProvincia "
                    + "from ubigeo "
                    + "where codigo_departamento = ? and codigo_provincia = ? and estado_registro = 1";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, ubigeo.getCodigoDepartamento());
            ps.setInt(2, ubigeo.getCodigoProvincia());
            rs = ps.executeQuery();
            rs.next();
            existeProvincia = rs.getInt("existeProvincia");
            if (existeProvincia > 0) {
                response.setStatus(true);
                response.setMessage("La provincia seleccionada existe.");
            } else if (existeProvincia == 0) {
                response.setStatus(false);
                response.setMessage("La provincia seleccionada no existe.");
            }

        } catch (SQLException e) {
            System.err.println("validarExistenciaProvincia -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
        jsonValidarExistenciaProvincia = new JSONObject(response);
        return jsonValidarExistenciaProvincia;
    }

    @Override
    public JSONObject validarExistenciaDistrito(UbigeoBean ubigeo) {
        JSONObject jsonValidarExistenciaDistrito = null;
        ResponseHelper response = new ResponseHelper();
        int existeDistrito = 0;
        String base = "planillabd";
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = SqlserverDAOFactory.obtenerConexion(base);
            String sql
                    = "select "
                    + "count(codigo_distrito) as existeDistrito "
                    + "from ubigeo "
                    + "where codigo_departamento = ? and codigo_provincia = ? and codigo_distrito = ? and estado_registro = 1";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, ubigeo.getCodigoDepartamento());
            ps.setInt(2, ubigeo.getCodigoProvincia());
            ps.setInt(3, ubigeo.getCodigoDistrito());
            rs = ps.executeQuery();
            rs.next();
            existeDistrito = rs.getInt("existeDistrito");
            if (existeDistrito > 0) {
                response.setStatus(true);
                response.setMessage("El distrito seleccionado existe.");
            } else if (existeDistrito == 0) {
                response.setStatus(false);
                response.setMessage("El distrito seleccionado no existe.");
            }

        } catch (SQLException e) {
            System.err.println("validarExistenciaDistrito -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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
        jsonValidarExistenciaDistrito = new JSONObject(response);
        return jsonValidarExistenciaDistrito;
    }

    @Override
    public JSONObject obtenerCodigoUbigeo(UbigeoBean ubigeo) {
        JSONObject jsonObtenerCodigoUbigeo = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        String base = "planillabd";
        int codigoUbigeo = 0;
        ResponseHelper response = new ResponseHelper();

        try {
            connection = SqlserverDAOFactory.obtenerConexion(base);
            String sql
                    = "select codigo_ubigeo as codigoUbigeo "
                    + "from ubigeo "
                    + "where codigo_departamento = ? "
                    + "and codigo_provincia = ? "
                    + "and codigo_distrito = ? "
                    + "and estado_registro = 1";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, ubigeo.getCodigoDepartamento());
            ps.setInt(2, ubigeo.getCodigoProvincia());
            ps.setInt(3, ubigeo.getCodigoDistrito());
            rs = ps.executeQuery();
            rs.next();
            codigoUbigeo = rs.getInt("codigoUbigeo");
            if (codigoUbigeo > 0) {
                JSONObject getResultedKey = new JSONObject();
                getResultedKey.put("getResultedKey", codigoUbigeo);
                response.setStatus(true);
                response.setData(getResultedKey);
                response.setMessage("Se obtuvo el código correctamente");
            } else {
                response.setStatus(false);
                response.setMessage("No se pudo obtener el código");
            }

        } catch (SQLException e) {
            System.err.println("obtenerCodigoUbigeo -> Error: " + e.getMessage() + " \n Error Code: [" + e.getErrorCode() + "]");
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

        jsonObtenerCodigoUbigeo = new JSONObject(response);
        return jsonObtenerCodigoUbigeo;
    }

}
