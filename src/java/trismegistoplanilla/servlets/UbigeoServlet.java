package trismegistoplanilla.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import trismegistoplanilla.beans.UbigeoBean;
import trismegistoplanilla.services.UbigeoService;

public class UbigeoServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String param = request.getParameter("accion");
        if ("listarDepartamento".equals(param)) {
            listarDepartamento(request, response);
        } else if ("listarProvincia".equals(param)) {
            listarProvincia(request, response);
        } else if ("listarDistrito".equals(param)) {
            listarDistrito(request, response);
        } else if ("validarExistenciaDepartamento".equals(param)) {
            validarExistenciaDepartamento(request, response);
        } else if ("validarExistenciaProvincia".equals(param)) {
            validarExistenciaProvincia(request, response);
        } else if ("validarExistenciaDistrito".equals(param)) {
            validarExistenciaDistrito(request, response);
        } else if ("obtenerCodigoUbigeo".equals(param)) {
            obtenerCodigoUbigeo(request, response);
        }
    }

    private void listarDepartamento(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        UbigeoService service = new UbigeoService();
        JSONObject jsonListarDepartamento = service.listarDepartamento();
        out.print(jsonListarDepartamento);
    }

    private void listarProvincia(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        UbigeoService service = new UbigeoService();
        int codigoDepartamento = Integer.parseInt(request.getParameter("codigoDepartamento"));
        UbigeoBean ubigeo = new UbigeoBean();
        ubigeo.setCodigoDepartamento(codigoDepartamento);
        JSONObject jsonListarProvincia = service.listarProvincia(ubigeo);
        out.print(jsonListarProvincia);
    }

    private void listarDistrito(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        UbigeoService service = new UbigeoService();
        int codigoDepartamento = Integer.parseInt(request.getParameter("codigoDepartamento"));
        int codigoProvincia = Integer.parseInt(request.getParameter("codigoProvincia"));
        UbigeoBean ubigeo = new UbigeoBean();
        ubigeo.setCodigoDepartamento(codigoDepartamento);
        ubigeo.setCodigoProvincia(codigoProvincia);
        JSONObject jsonListarDistrito = service.listarDistrito(ubigeo);
        out.print(jsonListarDistrito);
    }

    private void validarExistenciaDepartamento(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        UbigeoService service = new UbigeoService();
        int codigoDepartamento = Integer.parseInt(request.getParameter("codigoDepartamento"));
        UbigeoBean ubigeo = new UbigeoBean();
        ubigeo.setCodigoDepartamento(codigoDepartamento);
        JSONObject jsonValidarExistenciaDepartamento = service.validarExistenciaDepartamento(ubigeo);
        out.print(jsonValidarExistenciaDepartamento);
    }

    private void validarExistenciaProvincia(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        UbigeoService service = new UbigeoService();
        int codigoDepartamento = Integer.parseInt(request.getParameter("codigoDepartamento"));
        int codigoProvincia = Integer.parseInt(request.getParameter("codigoProvincia"));
        UbigeoBean ubigeo = new UbigeoBean();
        ubigeo.setCodigoDepartamento(codigoDepartamento);
        ubigeo.setCodigoProvincia(codigoProvincia);
        JSONObject jsonValidarExistenciaProvincia = service.validarExistenciaProvincia(ubigeo);
        out.print(jsonValidarExistenciaProvincia);
    }

    private void validarExistenciaDistrito(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        UbigeoService service = new UbigeoService();
        int codigoDepartamento = Integer.parseInt(request.getParameter("codigoDepartamento"));
        int codigoProvincia = Integer.parseInt(request.getParameter("codigoProvincia"));
        int codigoDistrito = Integer.parseInt(request.getParameter("codigoDistrito"));
        UbigeoBean ubigeo = new UbigeoBean();
        ubigeo.setCodigoDepartamento(codigoDepartamento);
        ubigeo.setCodigoProvincia(codigoProvincia);
        ubigeo.setCodigoDistrito(codigoDistrito);
        JSONObject jsonValidarExistenciaDistrito = service.validarExistenciaDistrito(ubigeo);
        out.print(jsonValidarExistenciaDistrito);
    }

    private void obtenerCodigoUbigeo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        UbigeoService service = new UbigeoService();
        int codigoDepartamento = Integer.parseInt(request.getParameter("codigoDepartamento"));
        int codigoProvincia = Integer.parseInt(request.getParameter("codigoProvincia"));
        int codigoDistrito = Integer.parseInt(request.getParameter("codigoDistrito"));
        UbigeoBean ubigeo = new UbigeoBean();
        ubigeo.setCodigoDepartamento(codigoDepartamento);
        ubigeo.setCodigoProvincia(codigoProvincia);
        ubigeo.setCodigoDistrito(codigoDistrito);
        JSONObject jsonObtenerCodigoUbigeo = service.obtenerCodigoUbigeo(ubigeo);
        out.print(jsonObtenerCodigoUbigeo);
    }

}
