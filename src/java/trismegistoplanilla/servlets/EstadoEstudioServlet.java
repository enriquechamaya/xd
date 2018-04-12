package trismegistoplanilla.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import trismegistoplanilla.beans.EstadoEstudioBean;
import trismegistoplanilla.services.EstadoEstudioService;

public class EstadoEstudioServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String param = request.getParameter("accion");
        if ("validarExistenciaEstadoEstudio".equals(param)) {
            validarExistenciaEstadoEstudio(request, response);
        } else if ("listarEstadoEstudio".equals(param)) {
            listarEstadoEstudio(request, response);
        }
    }

    private void validarExistenciaEstadoEstudio(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        EstadoEstudioService service = new EstadoEstudioService();
        int codigoNivelEstudio = Integer.parseInt(request.getParameter("codigoNivelEstudio"));
        int codigoEstadoEstudio = Integer.parseInt(request.getParameter("codigoEstadoEstudio"));
        EstadoEstudioBean estadoEstudio = new EstadoEstudioBean();
        estadoEstudio.setCodigoNivelEstudio(codigoNivelEstudio);
        estadoEstudio.setCodigoEstadoEstudio(codigoEstadoEstudio);
        JSONObject jsonValidarExistenciaEstadoEstudio = service.validarExistenciaEstadoEstudio(estadoEstudio);
        out.print(jsonValidarExistenciaEstadoEstudio);
    }

    private void listarEstadoEstudio(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        EstadoEstudioService service = new EstadoEstudioService();
        int codigoNivelEstudio = Integer.parseInt(request.getParameter("codigoNivelEstudio"));
        EstadoEstudioBean estadoEstudio = new EstadoEstudioBean();
        estadoEstudio.setCodigoNivelEstudio(codigoNivelEstudio);
        JSONObject jsonListarEstadoEstudio = service.listarEstadoEstudio(estadoEstudio);
        out.print(jsonListarEstadoEstudio);
    }

}
