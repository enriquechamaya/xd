package trismegistoplanilla.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import trismegistoplanilla.beans.AreaBean;
import trismegistoplanilla.beans.SedeBean;
import trismegistoplanilla.services.SedeAreaService;

public class SedeAreaServlet extends HttpServlet {

  private static final long serialVersionUID = 2450101405568357533L;

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String accion = request.getParameter("accion");
    if (accion.equals("obtenerSedeArea")) {
      obtenerSedeArea(request, response);
    }
  }

  private void obtenerSedeArea(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    JSONObject JOObtenerSedeArea = new JSONObject();
    if (request.getParameter("codigoSede") == null || request.getParameter("codigoArea") == null) {
      JOObtenerSedeArea.put("status", false);
      JOObtenerSedeArea.put("message", "Los datos llegaron nulos");
      out.print(JOObtenerSedeArea);
    } else {
      int codigoSede = Integer.parseInt(request.getParameter("codigoSede"));
      int codigoArea = Integer.parseInt(request.getParameter("codigoArea"));
      SedeBean s = new SedeBean();
      s.setCodigoSede(codigoSede);
      AreaBean a = new AreaBean();
      a.setCodigoArea(codigoArea);
      SedeAreaService service = new SedeAreaService();
      JOObtenerSedeArea = service.obtenerSedeArea(s, a);
      out.print(JOObtenerSedeArea);
    }
  }

}
