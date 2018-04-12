package trismegistoplanilla.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import trismegistoplanilla.services.FondoPensionService;

public class FondoPensionServlet extends HttpServlet {
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String param = request.getParameter("accion");
        
        if ("listarFondoPension".equals(param)) {
            listarFondoPension(request, response);
        } else if ("validarExistenciaFondoPension".equals(param)) {
            validarExistenciaFondoPension(request, response);
        }
    }
    
    private void listarFondoPension(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        FondoPensionService service = new FondoPensionService();
        JSONObject jsonListarFondoPension = service.listarFondoPension();
        out.print(jsonListarFondoPension);
        
    }
    
    private void validarExistenciaFondoPension(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
