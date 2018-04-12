package trismegistoplanilla.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;
import pe.siso.webservicesseguridad.webservices.MenuBean;
import pe.siso.webservicesseguridad.webservices.UsuarioBean;

public class MenuServlet extends HttpServlet {

  private static final long serialVersionUID = -6329360976061433989L;

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String param = request.getParameter("accion");

    if ("listarMenu".equals(param)) {
      listarMenu(request, response);
    } else if ("paginaMenuSession".equals(param)) {
      paginaMenuSession(request, response);
    }
  }

  private void listarMenu(HttpServletRequest request, HttpServletResponse response) throws IOException {

    int codigoModulo = Integer.parseInt(request.getParameter("codigoModulo"));
    int codigoCategoria = Integer.parseInt(request.getParameter("codigoCategoria"));
    int codigoSubCategoria = Integer.parseInt(request.getParameter("codigoSubCategoria"));

    HttpSession session_actual = request.getSession(true);
    UsuarioBean usuario = (UsuarioBean) session_actual.getAttribute("usuario");

    ArrayList<MenuBean> menuTitulo = (ArrayList<MenuBean>) session_actual.getAttribute("menuSesionTitulo");
    ArrayList<MenuBean> menuModulo = (ArrayList<MenuBean>) session_actual.getAttribute("menuSesionModulo");
    ArrayList<MenuBean> menuCategoria = (ArrayList<MenuBean>) session_actual.getAttribute("menuSesionCategoria");
    ArrayList<MenuBean> menuSubcategoria = (ArrayList<MenuBean>) session_actual.getAttribute("menuSesionSubCategoria");

    response.setContentType("application/json");
    PrintWriter pw = response.getWriter();

    JSONObject jsonReturn = new JSONObject();
    JSONArray arrMenuTitulo = new JSONArray(menuTitulo);
    JSONArray arrMenuModulo = new JSONArray(menuModulo);
    JSONArray arrMenuCategoria = new JSONArray(menuCategoria);
    JSONArray arrMenuSubCategoria = new JSONArray(menuSubcategoria);

    JSONObject jsonMenuActual = new JSONObject();
    jsonMenuActual.put("codigoModulo", codigoModulo);
    jsonMenuActual.put("codigoCategoria", codigoCategoria);
    jsonMenuActual.put("codigoSubCategoria", codigoSubCategoria);

    jsonReturn.put("menuTitulo", arrMenuTitulo);
    jsonReturn.put("menuModulo", arrMenuModulo);
    jsonReturn.put("menuCategoria", arrMenuCategoria);
    jsonReturn.put("menuSubCategoria", arrMenuSubCategoria);
    jsonReturn.put("codigoMenuActual", jsonMenuActual);

    jsonReturn.put("apellidoPaterno", usuario.getApellidoPaternoPersona());
    jsonReturn.put("apellidoMaterno", usuario.getApellidoMaternoPersona());
    jsonReturn.put("nombre", usuario.getNombresPersona());
    jsonReturn.put("tipoUsuario", usuario.getNombreTipoUsuario());

    pw.print(jsonReturn);
  }

  private void paginaMenuSession(HttpServletRequest request, HttpServletResponse response) {

    int codigoTitulo = Integer.parseInt(request.getParameter("codigoTitulo"));
    int codigoModulo = Integer.parseInt(request.getParameter("codigoModulo"));
    int codigoCategoria = Integer.parseInt(request.getParameter("codigoCategoria"));
    int codigoSubCategoria = Integer.parseInt(request.getParameter("codigoSubCategoria"));

//    System.out.println("codTitulo >>>> " + codTitulo);
//    System.out.println("codModulo >>>> " + codModulo);
//    System.out.println("codCategoria >>>> " + codCategoria);
//    System.out.println("codSubCategoria >>>> " + codSubCategoria);
    HttpSession session_actual = request.getSession(true);

    MenuBean menu = new MenuBean();

    menu.setCodigoTitulo(codigoTitulo);
    menu.setCodigoModulo(codigoModulo);
    menu.setCodigoCategoria(codigoCategoria);
    menu.setCodigoSubcategoria(codigoSubCategoria);

    session_actual.setAttribute("menu", menu);
  }

}
