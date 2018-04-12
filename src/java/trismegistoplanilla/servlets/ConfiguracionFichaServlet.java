package trismegistoplanilla.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import trismegistoplanilla.services.ConfiguracionFichaService;

public class ConfiguracionFichaServlet extends HttpServlet {

  private static final long serialVersionUID = 2913760089019246444L;

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String accion = request.getParameter("accion");

    if (accion.equals("getDefaultMail")) {
      getDefaultMail(request, response);
    }

  }

  private void getDefaultMail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    ConfiguracionFichaService service = new ConfiguracionFichaService();
    JSONObject JOmail = service.getDefaultMail();
    out.print(JOmail);
  }

}
