package trismegistoplanilla.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import trismegistoplanilla.beans.TipoDocumentoBean;
import trismegistoplanilla.services.TipoDocumentoService;

public class TipoDocumentoServlet extends HttpServlet {

  private static final long serialVersionUID = 2735704424902061897L;

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String accion = request.getParameter("accion");
    switch (accion) {
      case "listarTipoDocumento":
        listarTipoDocumento(request, response);
        break;
      case "validarExistenciaTipoDocumento":
        validarExistenciaTipoDocumento(request, response);
        break;
      case "obtenerLongitudTipoEntrdadaTipoDocumento":
        obtenerLongitudTipoEntrdadaTipoDocumento(request, response);
        break;
      default:
        break;
    }
  }

  private void listarTipoDocumento(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    TipoDocumentoService service = new TipoDocumentoService();
    JSONObject jsonObjListarTipoDocumento = service.listarTipoDocumento();
    out.print(jsonObjListarTipoDocumento);
  }

  private void validarExistenciaTipoDocumento(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    JSONObject jsonObjValidarExistenciaTipoDocumento = null;
    if (request.getParameter("codigoTipoDocumento") == null) {
      jsonObjValidarExistenciaTipoDocumento = new JSONObject();
      jsonObjValidarExistenciaTipoDocumento.put("status", false);
      jsonObjValidarExistenciaTipoDocumento.put("message", "Los datos llegaron nulos");
      out.print(jsonObjValidarExistenciaTipoDocumento);
    } else {
      int codigoTipoDocumento = Integer.parseInt(request.getParameter("codigoTipoDocumento"));
      TipoDocumentoService service = new TipoDocumentoService();
      TipoDocumentoBean tipoDocumento = new TipoDocumentoBean();
      tipoDocumento.setCodigoTipoDocumento(codigoTipoDocumento);
      jsonObjValidarExistenciaTipoDocumento = service.validarExistenciaTipoDocumento(tipoDocumento);
      out.print(jsonObjValidarExistenciaTipoDocumento);
    }
  }

  private void obtenerLongitudTipoEntrdadaTipoDocumento(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    int codigoTipoDocumento = Integer.parseInt(request.getParameter("codigoTipoDocumento"));
    TipoDocumentoService service = new TipoDocumentoService();
    TipoDocumentoBean tipoDocumento = new TipoDocumentoBean();
    tipoDocumento.setCodigoTipoDocumento(codigoTipoDocumento);
    JSONObject jsonObjObtenerLongitudTipoEntrdadaTipoDocumento = service.obtenerLongitudTipoEntrdadaTipoDocumento(tipoDocumento);
    out.print(jsonObjObtenerLongitudTipoEntrdadaTipoDocumento);
  }

}
