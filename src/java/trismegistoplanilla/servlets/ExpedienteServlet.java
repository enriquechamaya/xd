package trismegistoplanilla.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import trismegistoplanilla.services.ExpedienteService;

public class ExpedienteServlet extends HttpServlet {
  
  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String param = request.getParameter("accion");
    switch (param) {
      case "listarTipoExpedientes":
        listarTipoExpedientes(request, response);
        break;
      default:
        break;
    }
  }
  
  private void listarTipoExpedientes(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    ExpedienteService service = new ExpedienteService();
    JSONObject jsonListarTipoExpedientes = service.listarTipoExpedientes();
    out.print(jsonListarTipoExpedientes);
  }
  
}
