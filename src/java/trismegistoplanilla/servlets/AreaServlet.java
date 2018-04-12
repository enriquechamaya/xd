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
import trismegistoplanilla.services.AreaService;
import trismegistoplanilla.utilities.ParamsValidation;

public class AreaServlet extends HttpServlet {

  private static final long serialVersionUID = 5009288243009651896L;

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String accion = request.getParameter("accion");
    if (accion.equals("listarArea")) {
      listarArea(request, response);
    } else if (accion.equals("validarExistenciaArea")) {
      validarExistenciaArea(request, response);
    }
  }

  private void listarArea(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();

    JSONObject JOListarArea;

    if (request.getParameter("codigoSede") == null) {
      JOListarArea = new JSONObject();
      JOListarArea.put("message", "Los datos llegaron nulos");
      JOListarArea.put("status", false);
      out.print(JOListarArea);
    } else {
      int codigoSede = Integer.parseInt(request.getParameter("codigoSede"));
      SedeBean s = new SedeBean();
      s.setCodigoSede(codigoSede);
      AreaService service = new AreaService();
      JOListarArea = service.listarArea(s);
      out.print(JOListarArea);
    }

  }

  private void validarExistenciaArea(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();

    JSONObject JOValidarExistenciaArea = null;

    if (request.getParameter("codigoSede") == null || request.getParameter("codigoArea") == null) {
      JOValidarExistenciaArea = new JSONObject();
      JOValidarExistenciaArea.put("message", "Los datos llegaron nulos");
      JOValidarExistenciaArea.put("status", false);
      out.print(JOValidarExistenciaArea);
    } else {
      if (ParamsValidation.validaSoloNumeros(Integer.parseInt(request.getParameter("codigoSede")))
              && ParamsValidation.validaSoloNumeros(Integer.parseInt(request.getParameter("codigoArea")))) {
        int codigoSede = Integer.parseInt(request.getParameter("codigoSede"));
        int codigoArea = Integer.parseInt(request.getParameter("codigoArea"));
        SedeBean s = new SedeBean();
        s.setCodigoSede(codigoSede);
        AreaBean a = new AreaBean();
        a.setCodigoArea(codigoArea);
        AreaService service = new AreaService();
        JOValidarExistenciaArea = service.validarExistenciaArea(s, a);
        out.print(JOValidarExistenciaArea);
      } else {
        JOValidarExistenciaArea = new JSONObject();
        JOValidarExistenciaArea.put("message", "codigoSede o codigoArea solo aceptan numeros");
        JOValidarExistenciaArea.put("status", false);
        out.print(JOValidarExistenciaArea);
      }
    }
  }

}
