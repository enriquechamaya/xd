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
import trismegistoplanilla.services.CargoService;
import trismegistoplanilla.utilities.ParamsValidation;

public class CargoServlet extends HttpServlet {

  private static final long serialVersionUID = 3418397218958878861L;

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String accion = request.getParameter("accion");
    if (accion.equals("listarCargo")) {
      listarCargo(request, response);
    } else if (accion.equals("validarExistenciaCargo")) {
      validarExistenciaCargo(request, response);
    }
  }

  private void listarCargo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    JSONObject JOListarCargo = null;
    if (request.getParameter("codigoArea") == null) {
      JOListarCargo.put("status", false);
      JOListarCargo.put("message", "Los valores llegaron nulos");
      out.print(JOListarCargo);
    } else {
      int codigoArea = Integer.parseInt(request.getParameter("codigoArea"));
      AreaBean a = new AreaBean();
      a.setCodigoArea(codigoArea);
      CargoService service = new CargoService();
      JOListarCargo = service.listarCargo(a);
      out.print(JOListarCargo);
    }
  }

  private void validarExistenciaCargo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    JSONObject JOValidarExistenciaCargo = null;
    if (request.getParameter("codigoArea") == null || request.getParameter("codigoCargo") == null) {
      JOValidarExistenciaCargo = new JSONObject();
      JOValidarExistenciaCargo.put("status", false);
      JOValidarExistenciaCargo.put("message", "Los valores llegaron nulos");
      out.print(JOValidarExistenciaCargo);
    } else {
      if (ParamsValidation.validaSoloNumeros(Integer.parseInt(request.getParameter("codigoArea")))
              && ParamsValidation.validaSoloNumeros(Integer.parseInt(request.getParameter("codigoCargo")))) {
        int codigoArea = Integer.parseInt(request.getParameter("codigoArea"));
        int codigoCargo = Integer.parseInt(request.getParameter("codigoCargo"));
        AreaBean a = new AreaBean();
        CargoBean c = new CargoBean();
        a.setCodigoArea(codigoArea);
        c.setCodigoCargo(codigoCargo);
        CargoService service = new CargoService();
        JOValidarExistenciaCargo = service.validarExistenciaCargo(a, c);
        out.print(JOValidarExistenciaCargo);
      } else {
        JOValidarExistenciaCargo = new JSONObject();
        JOValidarExistenciaCargo.put("status", false);
        JOValidarExistenciaCargo.put("message", "codigoArea o codigoCargo solo aceptan numeros");
        out.print(JOValidarExistenciaCargo);
      }
    }
  }

}
