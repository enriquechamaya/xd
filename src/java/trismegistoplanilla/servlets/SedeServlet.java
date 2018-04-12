package trismegistoplanilla.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import trismegistoplanilla.beans.SedeBean;
import trismegistoplanilla.services.SedeService;
import trismegistoplanilla.utilities.ParamsValidation;

public class SedeServlet extends HttpServlet {

  private static final long serialVersionUID = -339375847638215388L;

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String accion = request.getParameter("accion");

    if (accion.equals("listarSede")) {
      listarSede(request, response);
    } else if (accion.equals("validarExistenciaSede")) {
      validarExistenciaSede(request, response);
    }
  }

  private void listarSede(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    SedeService service = new SedeService();
    JSONObject JObjectSede = service.listarSede();
    out.print(JObjectSede);
  }

  private void validarExistenciaSede(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    JSONObject JObjectValidarExistenciaSede = new JSONObject();
    if (request.getParameter("codigoSede") == null) {
      JObjectValidarExistenciaSede.put("message", "Los datos llegaron nulos");
      JObjectValidarExistenciaSede.put("status", false);
      out.print(JObjectValidarExistenciaSede);
    } else {
      if (ParamsValidation.validaSoloNumeros(Integer.parseInt(request.getParameter("codigoSede")))) {
        int codigoSede = Integer.parseInt(request.getParameter("codigoSede"));
        SedeBean s = new SedeBean();
        s.setCodigoSede(codigoSede);
        SedeService service = new SedeService();
        JObjectValidarExistenciaSede = service.validarExistenciaSede(s);
        out.print(JObjectValidarExistenciaSede);
      } else {
        JObjectValidarExistenciaSede.put("message", "El codigoSede solo aceptar numeros");
        JObjectValidarExistenciaSede.put("status", false);
        out.print(JObjectValidarExistenciaSede);
      }
    }
  }

}
