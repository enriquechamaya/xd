package trismegistoplanilla.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import trismegistoplanilla.beans.CarreraProfesionalBean;
import trismegistoplanilla.services.CarreraProfesionalService;

public class CarreraProfesionalServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String param = request.getParameter("accion");
        if ("listarCarreraProfesional".equals(param)) {
            listarCarreraProfesional(request, response);
        } else if ("validarExistenciaCarreraProfesional".equals(param)) {
            validarExistenciaCarreraProfesional(request, response);
        }
    }

    private void listarCarreraProfesional(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String search = request.getParameter("q");
        String page = request.getParameter("page");
        System.out.println("page: " + page);
        CarreraProfesionalService service = new CarreraProfesionalService();
        JSONObject jsonListarCarreraProfesional = service.listarCarreraProfesional(search);
        out.print(jsonListarCarreraProfesional);
    }

    private void validarExistenciaCarreraProfesional(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        CarreraProfesionalService service = new CarreraProfesionalService();
        int codigoCarreraProfesional = Integer.parseInt(request.getParameter("codigoCarreraProfesional"));
        CarreraProfesionalBean carreraProfesional = new CarreraProfesionalBean();
        carreraProfesional.setCodigoCarreraProfesional(codigoCarreraProfesional);
        JSONObject jsonValidarExistenciaCarreraProfesional = service.validarExistenciaCarreraProfesional(carreraProfesional);
        out.print(jsonValidarExistenciaCarreraProfesional);
    }

}
