package trismegistoplanilla.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import trismegistoplanilla.beans.NivelEstadoBean;
import trismegistoplanilla.services.NivelEstadoService;

public class NivelEstadoServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String param = request.getParameter("accion");
        if ("obtenerNivelEstado".equals(param)) {
            obtenerNivelEstado(request, response);
        }
    }

    private void obtenerNivelEstado(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        int codigoNivelEstudio = Integer.parseInt(request.getParameter("codigoNivelEstudio"));
        int codigoEstadoEstudio = Integer.parseInt(request.getParameter("codigoEstadoEstudio"));
        NivelEstadoService service = new NivelEstadoService();
        NivelEstadoBean nivelEstado = new NivelEstadoBean();
        nivelEstado.setCodigoNivelEstudio(codigoNivelEstudio);
        nivelEstado.setCodigoEstadoEstudio(codigoEstadoEstudio);
        JSONObject jsonObtenerNivelEstado = service.obtenerNivelEstado(nivelEstado);
        out.print(jsonObtenerNivelEstado);
    }

}
