package trismegistoplanilla.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import trismegistoplanilla.beans.EstadoCivilBean;
import trismegistoplanilla.services.EstadoCivilService;

public class EstadoCivilServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String param = request.getParameter("accion");
        if ("listarEstadoCivil".equals(param)) {
            listarEstadoCivil(request, response);
        } else if ("validarExistenciaEstadoCivil".equals(param)) {
            validarExistenciaEstadoCivil(request, response);
        }
    }

    private void listarEstadoCivil(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        EstadoCivilService service = new EstadoCivilService();
        JSONObject jsonReturn = service.listarEstadoCivil();
        out.print(jsonReturn);
    }

    private void validarExistenciaEstadoCivil(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        int codigoEstadoCivil = Integer.parseInt(request.getParameter("codigoEstadoCivil"));
        EstadoCivilService service = new EstadoCivilService();
        EstadoCivilBean estadoCivil = new EstadoCivilBean();
        estadoCivil.setCodigoEstadoCivil(codigoEstadoCivil);
        JSONObject jsonvalidarExistenciaEstadoCivil = service.validarExistenciaEstadoCivil(estadoCivil);
        out.print(jsonvalidarExistenciaEstadoCivil);

    }

}
