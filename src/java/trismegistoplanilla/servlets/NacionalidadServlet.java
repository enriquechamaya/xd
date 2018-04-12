package trismegistoplanilla.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;
import trismegistoplanilla.beans.NacionalidadBean;
import trismegistoplanilla.services.NacionalidadService;
import trismegistoplanilla.utilities.ParamsValidation;

public class NacionalidadServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String param = request.getParameter("accion");
        if ("listarNacionalidad".equals(param)) {
            listarNacionalidad(request, response);
        } else if ("validarExistenciaNacionalidad".equals(param)) {
            validarExistenciaNacionalidad(request, response);
        }
    }

    private void listarNacionalidad(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        NacionalidadService service = new NacionalidadService();
        JSONObject jsonListarNacionalidad = service.listarNacionalidad();
        out.print(jsonListarNacionalidad);
    }

    private void validarExistenciaNacionalidad(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        if (ParamsValidation.validaTipoEntrada("N", request.getParameter("codigoNacionalidad"))) {
            int codigoNacionalidad = Integer.parseInt(request.getParameter("codigoNacionalidad"));
            NacionalidadService service = new NacionalidadService();
            NacionalidadBean nacionalidad = new NacionalidadBean();
            nacionalidad.setCodigoNacionalidad(codigoNacionalidad);
            JSONObject jsonValidarExistenciaNacionalidad = service.validarExistenciaNacionalidad(nacionalidad);
            out.print(jsonValidarExistenciaNacionalidad);
        } else {
            out.println("La nacionalidad seleccionada no existe.");
        }
    }
}
