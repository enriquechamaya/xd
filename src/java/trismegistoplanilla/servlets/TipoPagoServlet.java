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
import trismegistoplanilla.services.TipoPagoService;
import trismegistoplanilla.utilities.ParamsValidation;

public class TipoPagoServlet extends HttpServlet {

  private static final long serialVersionUID = 805335261972909791L;

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String accion = request.getParameter("accion");
    if (accion.equals("listarTipoPago")) {
      listarTipoPago(request, response);
    } else if (accion.equals("validarExistenciaTipoPago")) {
      validarExistenciaTipoPago(request, response);
    }
  }

  private void listarTipoPago(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    JSONObject JOListarTipoPago = null;
    if (request.getParameter("codigoAreaCargo") == null) {
      JOListarTipoPago = new JSONObject();
      JOListarTipoPago.put("message", "Los datos llegaron nulos");
      JOListarTipoPago.put("status", false);
      out.print(JOListarTipoPago);
    } else {
      int codigoAreaCargo = Integer.parseInt(request.getParameter("codigoAreaCargo"));
      AreaCargoBean ac = new AreaCargoBean();
      ac.setCodigoAreaCargo(codigoAreaCargo);
      TipoPagoService service = new TipoPagoService();
      JOListarTipoPago = service.listarTipoPago(ac);
      out.print(JOListarTipoPago);
    }
  }

  private void validarExistenciaTipoPago(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    JSONObject JObjectValidarExistenciaTipoPago = null;
    if (request.getParameter("codigoAreaCargo") == null || request.getParameter("codigoTipoPago") == null) {
      JObjectValidarExistenciaTipoPago = new JSONObject();
      JObjectValidarExistenciaTipoPago.put("message", "Los datos llegaron nulos");
      JObjectValidarExistenciaTipoPago.put("status", false);
      out.print(JObjectValidarExistenciaTipoPago);
    } else {

      if (ParamsValidation.validaSoloNumeros(Integer.parseInt(request.getParameter("codigoAreaCargo")))
              && ParamsValidation.validaSoloNumeros(Integer.parseInt(request.getParameter("codigoTipoPago")))) {
        int codigoAreaCargo = Integer.parseInt(request.getParameter("codigoAreaCargo"));
        int codigoTipoPago = Integer.parseInt(request.getParameter("codigoTipoPago"));
        AreaCargoTipoPagoBean actp = new AreaCargoTipoPagoBean();
        actp.setCodigoAreaCargo(codigoAreaCargo);
        actp.setCodigoTipoPago(codigoTipoPago);
        TipoPagoService service = new TipoPagoService();
        JObjectValidarExistenciaTipoPago = service.validarExistenciaTipoPago(actp);
        out.print(JObjectValidarExistenciaTipoPago);
      } else {
        JObjectValidarExistenciaTipoPago = new JSONObject();
        JObjectValidarExistenciaTipoPago.put("message", "codigoAreaCargo o codigoTipoPago solo aceptan numeros");
        JObjectValidarExistenciaTipoPago.put("status", false);
        out.print(JObjectValidarExistenciaTipoPago);
      }
    }
  }

}
