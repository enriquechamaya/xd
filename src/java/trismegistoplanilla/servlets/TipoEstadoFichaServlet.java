package trismegistoplanilla.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import trismegistoplanilla.services.TipoEstadoFichaService;

public class TipoEstadoFichaServlet extends HttpServlet {

  private static final long serialVersionUID = 7749245119398017493L;

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String accion = request.getParameter("accion");
    if (accion.equals("listarTipoEstadoFicha")) {
      listarTipoEstadoFicha(request, response);
    }
  }

  private void listarTipoEstadoFicha(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    TipoEstadoFichaService service = new TipoEstadoFichaService();
    JSONObject JOlistarTipoEstadoFicha = service.listarTipoEstadoFicha();
    out.print(JOlistarTipoEstadoFicha);
  }

}
