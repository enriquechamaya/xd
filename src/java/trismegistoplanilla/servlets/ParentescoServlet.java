package trismegistoplanilla.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import trismegistoplanilla.beans.ParentescoBean;
import trismegistoplanilla.services.ParentescoService;

public class ParentescoServlet extends HttpServlet {
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String param = request.getParameter("accion");
        if ("listarParentesco".equals(param)) {
            listarParentesco(request, response);
        } else if ("validarExistenciaParentesco".equals(param)) {
            validarExistenciaParentesco(request, response);
        }
    }
    
    private void listarParentesco(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        ParentescoService service = new ParentescoService();
        JSONObject jsonListarParentesco = service.listarParentesco();
        out.print(jsonListarParentesco);
    }
    
    private void validarExistenciaParentesco(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        int codigoParentesco = Integer.parseInt(request.getParameter("codigoParentesco"));
        ParentescoService service = new ParentescoService();
        ParentescoBean parentesco = new ParentescoBean();
        parentesco.setCodigoParentesco(codigoParentesco);
        JSONObject jsonValidarExistenciaParentesco = service.validarExistenciaParentesco(parentesco);
        out.print(jsonValidarExistenciaParentesco);
    }
    
}
