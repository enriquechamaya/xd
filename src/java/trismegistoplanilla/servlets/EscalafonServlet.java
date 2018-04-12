package trismegistoplanilla.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import trismegistoplanilla.beans.AreaCargoBean;
import trismegistoplanilla.beans.AreaCargoTipoPagoBean;
import trismegistoplanilla.services.EscalafonService;
import trismegistoplanilla.utilities.ParamsValidation;

public class EscalafonServlet extends HttpServlet {

  private static final long serialVersionUID = 8147939769580376491L;

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String param = request.getParameter("accion");
    if ("obtenerEscalafonAreaCargoTipoPago".equals(param)) {
      obtenerEscalafonAreaCargoTipoPago(request, response);
    }
  }

  private void obtenerEscalafonAreaCargoTipoPago(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    if (request.getParameter("codigoAreaCargoTipoPago") != null) {
      System.out.println("codigoAreaCargoTipoPago => " + request.getParameter("codigoAreaCargoTipoPago"));
      int codigoAreaCargoTipoPago = Integer.parseInt(request.getParameter("codigoAreaCargoTipoPago"));
      if (ParamsValidation.validaSoloNumeros(codigoAreaCargoTipoPago)) {
        EscalafonService service = new EscalafonService();
        AreaCargoTipoPagoBean actp = new AreaCargoTipoPagoBean();
        actp.setCodigoAreaCargoTipoPago(codigoAreaCargoTipoPago);
        JSONObject JOAreaCargoTipoPago = service.obtenerEscalafonAreaCargoTipoPago(actp);
        out.print(JOAreaCargoTipoPago);
      } else {
        out.print("Al parecer ha ocurrido un error");
      }
    } else {
      out.print("Al parecer ha ocurrido un error");
    }
  }
}
