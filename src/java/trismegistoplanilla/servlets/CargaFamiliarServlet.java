package trismegistoplanilla.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import trismegistoplanilla.beans.CargaFamiliarBean;
import trismegistoplanilla.services.CargaFamiliarService;

public class CargaFamiliarServlet extends HttpServlet {

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String param = request.getParameter("accion");
    if ("validarExistenciaNumeroDocumento".equals(param)) {
      validarExistenciaNumeroDocumento(request, response);
    } else if ("obtenerCargaFamiliarPorPersona".equals(param)) {
      obtenerCargaFamiliarPorPersona(request, response);
    }
  }

  private void validarExistenciaNumeroDocumento(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    String numeroDocumento = request.getParameter("numeroDocumento");
    int longitud = Integer.parseInt(request.getParameter("longitud"));
    String tipoEntrada = request.getParameter("tipoEntrada");
    CargaFamiliarService service = new CargaFamiliarService();
    CargaFamiliarBean cargaFamiliar = new CargaFamiliarBean();
    cargaFamiliar.setNumeroDocumento(numeroDocumento);
    JSONObject jsonValidarExistenciaNumeroDocumento = service.validarExistenciaNumeroDocumento(cargaFamiliar);
    out.print(jsonValidarExistenciaNumeroDocumento);
  }

  private void obtenerCargaFamiliarPorPersona(HttpServletRequest request, HttpServletResponse response) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

}
