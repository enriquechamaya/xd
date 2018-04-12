package trismegistoplanilla.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;
import pe.siso.webservicesseguridad.webservices.UsuarioBean;
import trismegistoplanilla.services.LoteFichaService;
import trismegistoplanilla.utilities.ParamsValidation;

public class LoteFichaServlet extends HttpServlet {
  
  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String param = request.getParameter("accion");
    switch (param) {
      case "listarFichasDT":
        listarFichasDT(request, response);
        break;
      case "registrarLote":
        registrarLote(request, response);
        break;
      case "listarLoteDT":
        listarLoteDT(request, response);
        break;
      case "listarLoteGeneralDT":
        listarLoteGeneralDT(request, response);
        break;
      case "listarFichasGeneral":
        listarFichasGeneral(request, response);
        break;
      default:
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONObject respuesta = new JSONObject();
        respuesta.put("status", false)
                .put("message", "No existe la url solicitada")
                .put("data", "");
        out.print(respuesta);
        break;
    }
  }
  
  private void listarFichasDT(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    LoteFichaService service = new LoteFichaService();
    String objJson = request.getParameter("json");
    JSONObject json = new JSONObject(objJson);
    String draw = request.getParameter("draw");
    String length = request.getParameter("length");
    String start = request.getParameter("start");
    
    System.out.println("draw" + draw + "   length " + length + "   start " + start);
    JSONObject jsonObjListarFichasDT = service.listarFichasDT(draw, length, start, json);
    System.out.println("SERVLET jsonObjListarFichasDT >>>> " + jsonObjListarFichasDT);
    out.print(jsonObjListarFichasDT);
  }
  
  private void registrarLote(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    LoteFichaService service = new LoteFichaService();
    String jsonObj = request.getParameter("json");
    
    if (jsonObj != null) {
      System.out.println(jsonObj);
      HttpSession sesion = request.getSession();
      UsuarioBean usuario = (UsuarioBean) sesion.getAttribute("usuario");
      System.out.println("CODIGO USUARIO >>>> " + usuario.getCodigoUsuario());
      JSONObject jsonObjRegistrarLote = new JSONObject(jsonObj);
      if (validarRegistroLoteJson(jsonObjRegistrarLote)) {
        JSONObject jsonReturn = service.registrarLote(jsonObjRegistrarLote, usuario);
        out.print(jsonReturn);
//        out.print(jsonObjRegistrarLote);
      } else {
        out.print("Ha ocurrido un error. Al parecer esta infringiendo las leyes de los values en los inputs.");
      }
    } else {
      out.print("Ha ocurrido un error. Al parecer esta infringiendo las leyes de los values en los inputs.");
    }
    
  }
  
  private void listarLoteDT(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    LoteFichaService service = new LoteFichaService();
    
    String draw = request.getParameter("draw");
    String length = request.getParameter("length");
    String start = request.getParameter("start");
    String search = request.getParameter("search");
    System.out.println("search ====> " + search);
    if (search != null) {
      System.out.println("draw" + draw + "   length " + length + "   start " + start);
      JSONObject jsonObjListarLoteDT = service.listarLoteDT(draw, length, start, search);
      System.out.println("SERVLET jsonObjListarLoteDT >>>> " + jsonObjListarLoteDT);
      out.print(jsonObjListarLoteDT);
      
    } else {
      out.print("Los datos llegaron nulos");
    }
  }
  
  private boolean validarRegistroLoteJson(JSONObject json) {
    
    JSONArray detallelote = json.getJSONArray("detallelote");
    
    if (detallelote.length() == 0) {
      return false;
    }
    
    if (!ParamsValidation.validaSoloLetras(json.getString("tipoLote"))) {
      return false;
    }
    
    for (int i = 0; i < detallelote.length(); i++) {
      if (!detallelote.getJSONObject(i).getString("tipoFicha").equals(json.getString("tipoLote"))) {
        return false;
      }
    }
    
    return true;
  }
  
  private void listarLoteGeneralDT(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    String draw = request.getParameter("draw");
    String length = request.getParameter("length");
    String start = request.getParameter("start");
    JSONObject data = new JSONObject(request.getParameter("json"));
    
    JSONObject req = new JSONObject();
    req.put("draw", draw);
    req.put("length", length);
    req.put("start", start);
    req.put("data", data);
    
    LoteFichaService service = new LoteFichaService();
    JSONObject respuesta = service.listarLoteGeneralDT(req);
    out.print(respuesta);
    
  }
  
  private void listarFichasGeneral(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    LoteFichaService service = new LoteFichaService();
    JSONObject json = new JSONObject(request.getParameter("json"));
    JSONObject respuesta = service.listarFichasGeneral(json);
    out.print(respuesta);
  }
  
}
