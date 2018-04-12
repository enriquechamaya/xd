package trismegistoplanilla.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import trismegistoplanilla.beans.TipoExpedienteBean;
import trismegistoplanilla.services.TipoExpedienteService;

public class TipoExpedienteServlet extends HttpServlet {

  private static final long serialVersionUID = -3503795589534606305L;

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String accion = request.getParameter("accion");
    if (accion.equals("listarTipoExpediente")) {
      listarTipoExpediente(request, response);
    } else if (accion.equals("registrarTipoExpediente")) {
      registrarTipoExpediente(request, response);
    }
  }

  private void listarTipoExpediente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    TipoExpedienteService service = new TipoExpedienteService();
    JSONObject JOlistarTipoExpediente = service.listarTipoExpediente();
    out.print(JOlistarTipoExpediente);
  }

  private void registrarTipoExpediente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    JSONObject JOregistrarTipoExpediente = null;
    if (request.getParameter("nombre") == null) {
      JOregistrarTipoExpediente = new JSONObject();
      JOregistrarTipoExpediente.put("status", "false");
      JOregistrarTipoExpediente.put("message", "Error, el parámetro nombre llegó nulo");
      out.print(JOregistrarTipoExpediente);
    } else {
      String nombreTipoExpediente = request.getParameter("nombre");
      TipoExpedienteBean te = new TipoExpedienteBean();
      te.setNombre(nombreTipoExpediente);
      TipoExpedienteService service = new TipoExpedienteService();
      JOregistrarTipoExpediente = service.registrarTipoExpediente(te);
      out.print(JOregistrarTipoExpediente);
    }
  }

}
