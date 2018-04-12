package trismegistoplanilla.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import trismegistoplanilla.beans.NivelEstudioBean;
import trismegistoplanilla.services.NivelEstudioService;

public class NivelEstudioServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String param = request.getParameter("accion");
        if ("validarExistenciaNivelEstudio".equals(param)) {
            validarExistenciaNivelEstudio(request, response);
        } else if ("listarNivelEstudio".equals(param)) {
            listarNivelEstudio(request, response);
        }
    }

    private void validarExistenciaNivelEstudio(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        NivelEstudioService service = new NivelEstudioService();
        int codigoNivelEstudio = Integer.parseInt(request.getParameter("codigoNivelEstudio"));
        NivelEstudioBean nivelEstudio = new NivelEstudioBean();
        nivelEstudio.setCodigoNivelEstudio(codigoNivelEstudio);
        JSONObject validarExistenciaNivelEstudio = service.validarExistenciaNivelEstudio(nivelEstudio);
        out.print(validarExistenciaNivelEstudio);

    }

    private void listarNivelEstudio(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        NivelEstudioService service = new NivelEstudioService();
        JSONObject jsonListarNivelEstudio = service.listarNivelEstudio();
        out.print(jsonListarNivelEstudio);
    }

}
