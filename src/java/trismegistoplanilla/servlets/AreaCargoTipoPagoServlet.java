package trismegistoplanilla.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import trismegistoplanilla.beans.AreaCargoTipoPagoBean;
import trismegistoplanilla.services.AreaCargoTipoPagoService;

public class AreaCargoTipoPagoServlet extends HttpServlet {

  private static final long serialVersionUID = -6509552355496191036L;

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String accion = request.getParameter("accion");
    if (accion.equals("obtenerAreaCargoTipoPago")) {
      obtenerAreaCargoTipoPago(request, response);
    }
  }

  private void obtenerAreaCargoTipoPago(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    JSONObject JOObtenerAreaCargoTipoPago = null;
    if (request.getParameter("codigoAreaCargo") == null || request.getParameter("codigoTipoPago") == null) {
      JOObtenerAreaCargoTipoPago = new JSONObject();
      JOObtenerAreaCargoTipoPago.put("status", false);
      JOObtenerAreaCargoTipoPago.put("message", "Los datos llegaron nulos");
      out.print(JOObtenerAreaCargoTipoPago);
    } else {
      int codigoAreaCargo = Integer.parseInt(request.getParameter("codigoAreaCargo"));
      int codigoTipoPago = Integer.parseInt(request.getParameter("codigoTipoPago"));
      AreaCargoTipoPagoBean actp = new AreaCargoTipoPagoBean();
      actp.setCodigoAreaCargo(codigoAreaCargo);
      actp.setCodigoTipoPago(codigoTipoPago);
      AreaCargoTipoPagoService service = new AreaCargoTipoPagoService();
      JOObtenerAreaCargoTipoPago = service.obtenerAreaCargoTipoPago(actp);
      out.print(JOObtenerAreaCargoTipoPago);
    }
  }

}
