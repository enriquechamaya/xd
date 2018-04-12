package trismegistoplanilla.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import trismegistoplanilla.beans.AreaBean;
import trismegistoplanilla.beans.CargoBean;
import trismegistoplanilla.services.AreaCargoService;

public class AreaCargoServlet extends HttpServlet {

  private static final long serialVersionUID = 5456986003571449483L;

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String accion = request.getParameter("accion");
    if (accion.equals("obtenerAreaCargo")) {
      obtenerAreaCargo(request, response);
    }
  }

  private void obtenerAreaCargo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    JSONObject JOObtenerAreaCargo;
    if (request.getParameter("codigoArea") == null || request.getParameter("codigoCargo") == null) {
      JOObtenerAreaCargo = new JSONObject();
      JOObtenerAreaCargo.put("status", false);
      JOObtenerAreaCargo.put("message", "Los datos llegaron nulos");
      out.print(JOObtenerAreaCargo);
    } else {
      int codigoArea = Integer.parseInt(request.getParameter("codigoArea"));
      int codigoCargo = Integer.parseInt(request.getParameter("codigoCargo"));
      AreaBean a = new AreaBean();
      a.setCodigoArea(codigoArea);
      CargoBean c = new CargoBean();
      c.setCodigoCargo(codigoCargo);
      AreaCargoService service = new AreaCargoService();
      JOObtenerAreaCargo = service.obtenerAreaCargo(a, c);
      out.print(JOObtenerAreaCargo);
    }
  }

}
