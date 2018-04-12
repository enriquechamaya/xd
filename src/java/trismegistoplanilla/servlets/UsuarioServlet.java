package trismegistoplanilla.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceRef;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;
import pe.siso.webservicesseguridad.webservices.MenuBean;
import pe.siso.webservicesseguridad.webservices.MenuWebService_Service;
import pe.siso.webservicesseguridad.webservices.ProyectoBean;
import pe.siso.webservicesseguridad.webservices.ProyectoWebService_Service;
import pe.siso.webservicesseguridad.webservices.UsuarioBean;
import pe.siso.webservicesseguridad.webservices.UsuarioWebService_Service;
import trismegistoplanilla.beans.TrabajadorResponsableBean;
import trismegistoplanilla.services.TrabajadorResponsableService;

public class UsuarioServlet extends HttpServlet {

  private static final long serialVersionUID = 6883195532697593722L;

  @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/app8.sacooliveros.edu.pe_8080/WebservicesSeguridad/ProyectoWebService.wsdl")
  private ProyectoWebService_Service proyectoservice;

  @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/app8.sacooliveros.edu.pe_8080/WebservicesSeguridad/MenuWebService.wsdl")
  private MenuWebService_Service menuservice;

  @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/app8.sacooliveros.edu.pe_8080/WebservicesSeguridad/UsuarioWebService.wsdl")
  private UsuarioWebService_Service usuarioservice;

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String param = request.getParameter("accion");
    if (null != param) switch (param) {
      case "validarUsuario":
        validarUsuario(request, response);
        break;
      case "redireccion":
        redireccion(request, response);
        break;
      case "redireccionUsuario":
        redireccionUsuario(request, response);
        break;
      default:
        break;
    }
  }

  private void validarUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException {
    int dato = 0;

    String jsonValidarUsuario = request.getParameter("json");
    JSONObject jsonObjValidarUsuario = new JSONObject(jsonValidarUsuario);
    String txtUsuario = jsonObjValidarUsuario.getString("txtUsuario");
    String txtContrasenia = jsonObjValidarUsuario.getString("txtContrasenia");

    String passwordEncriptado = DigestUtils.md5Hex(txtContrasenia);

    response.setContentType("application/json");
    PrintWriter pw = response.getWriter();

    int proyecto = 5; // SISTEMA TRIMEGISTO PLANILLA

    dato = validarUsuarioWebservice(txtUsuario, passwordEncriptado, proyecto);

    JSONObject responseJson = new JSONObject();

    if (dato == 0) {
      responseJson.put("status", false);
      responseJson.put("message", "Usuario y/o contraseña incorrecta.");

    } else {
      HttpSession session_actual = request.getSession(true);
      UsuarioBean usuario = consultarDatosWebservice(txtUsuario, passwordEncriptado, proyecto);

      TrabajadorResponsableService service = new TrabajadorResponsableService();
      TrabajadorResponsableBean trabajadorResponsable = new TrabajadorResponsableBean();
      trabajadorResponsable.setDni(usuario.getDni());
      JSONObject jsonObjValidarExistenciaTrabajador = service.validarExistenciaTrabajadorPorDni(trabajadorResponsable);
      System.out.println("jsonObjValidarExistenciaTrabajador -> " + jsonObjValidarExistenciaTrabajador);
      if (!jsonObjValidarExistenciaTrabajador.getBoolean("status")) {
        trabajadorResponsable.setCodigoPlanillaReal(usuario.getCodigoTrabajador());
        trabajadorResponsable.setApellidoPaterno(usuario.getApellidoPaternoPersona());
        trabajadorResponsable.setApellidoMaterno(usuario.getApellidoMaternoPersona());
        trabajadorResponsable.setNombre(usuario.getNombresPersona());
        trabajadorResponsable.setCodigoUsuario(usuario.getCodigoUsuario());
        JSONObject jsonObjRegistrarTrabajador = service.registrarTrabajadorResponsable(trabajadorResponsable);
        System.out.println("No existe el personal.. Se registrará");
        if (jsonObjRegistrarTrabajador.getBoolean("status")) {
          System.out.println("Se registro el trabajador responsable");
        } else {
          System.out.println("Error al registrar trabajador responsable");
        }
      } else {
        System.out.println("Existe personal en la base");
      }

      int codigoProyectoDetalle = usuario.getCodigoProyectoDetalle();

      List<ProyectoBean> listaProyectos = listarProyectoUsuario(usuario.getCodigoUsuario());
      session_actual.setAttribute("listaProyectos", listaProyectos);

      List<MenuBean> menuTitulo = listarTituloWebservice(codigoProyectoDetalle);
      session_actual.setAttribute("menuSesionTitulo", menuTitulo);

      List<MenuBean> menuModulo = listarModuloWebservice(codigoProyectoDetalle);
      session_actual.setAttribute("menuSesionModulo", menuModulo);

      List<MenuBean> menuCategoria = listarCategoriaWebservice(codigoProyectoDetalle);
      session_actual.setAttribute("menuSesionCategoria", menuCategoria);

      List<MenuBean> menuSubCategoria = listarSubCategoriaWebservice(codigoProyectoDetalle);
      session_actual.setAttribute("menuSesionSubCategoria", menuSubCategoria);

      JSONObject jsonObjSedeAreaCargo = service.obtenerSedeAreaCargoPorCodigoPlanilla(usuario.getCodigoTrabajador());
      
      if (jsonObjSedeAreaCargo.getBoolean("status")) {
        usuario.setNombreSede(jsonObjSedeAreaCargo.getJSONObject("data").getString("nombreSede"));
        usuario.setNombreArea(jsonObjSedeAreaCargo.getJSONObject("data").getString("nombreArea"));
        usuario.setNombreCargo(jsonObjSedeAreaCargo.getJSONObject("data").getString("nombreCargo"));
      }

      session_actual.setAttribute("usuario", usuario);
      session_actual.setMaxInactiveInterval(10 * 60 * 60);// 10 horas

      
      responseJson.put("status", true);
    }

    pw.print(responseJson);

  }

  private void redireccion(HttpServletRequest request, HttpServletResponse response) throws IOException {
    HttpSession session_actual = request.getSession(true);
    UsuarioBean usuarioSession = (UsuarioBean) session_actual.getAttribute("usuario");
    String usuario = usuarioSession.getUsuario();
    String clave = usuarioSession.getContrasena();

    int p = Integer.parseInt(request.getParameter("p"));

    ProyectoBean proyecto = consultarProyecto(p);
    response.sendRedirect(proyecto.getUrlProyecto() + "UsuarioServlet?accion=redireccionUsuario&txtUsuario=" + usuario + "&txtContrasena=" + clave + "");
  }

  private void redireccionUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException {
    int valor = 0;
    String dato = "";

    String usuario = request.getParameter("txtUsuario");
    String passwordEncriptado = request.getParameter("txtContrasena");
    int proyecto = 5;

    valor = validarUsuarioWebservice(usuario, passwordEncriptado, proyecto);
    PrintWriter pw = response.getWriter();

    if (valor == 0) {
      response.sendRedirect("vistas/login.jsp");
      //pw.println("0");
    } else {
      HttpSession session_actual = request.getSession(true);
      UsuarioBean usu = consultarDatosWebservice(usuario, passwordEncriptado, proyecto);

      int codigoProyectoDetalle = usu.getCodigoProyectoDetalle();

      List<ProyectoBean> listaProyectos = listarProyectoUsuario(usu.getCodigoUsuario());
      session_actual.setAttribute("listaProyectos", listaProyectos);

      List<MenuBean> menuTitulo = listarTituloWebservice(codigoProyectoDetalle);
      session_actual.setAttribute("menuSesionTitulo", menuTitulo);

      List<MenuBean> menuModulo = listarModuloWebservice(codigoProyectoDetalle);
      session_actual.setAttribute("menuSesionModulo", menuModulo);

      List<MenuBean> menuCategoria = listarCategoriaWebservice(codigoProyectoDetalle);
      session_actual.setAttribute("menuSesionCategoria", menuCategoria);

      List<MenuBean> menuSubCategoria = listarSubCategoriaWebservice(codigoProyectoDetalle);
      session_actual.setAttribute("menuSesionSubCategoria", menuSubCategoria);

      session_actual.setAttribute("usuario", usu);
      session_actual.setMaxInactiveInterval(10 * 60 * 60);// 10 horas
      response.sendRedirect("vistas/main.jsp");

    }

  }

  private UsuarioBean consultarDatosWebservice(java.lang.String usuario, java.lang.String clave, int proyecto) {
    // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
    // If the calling of port operations may lead to race condition some synchronization is required.
    pe.siso.webservicesseguridad.webservices.UsuarioWebService port = usuarioservice.getUsuarioWebServicePort();
    return port.consultarDatosWebservice(usuario, clave, proyecto);
  }

  private Integer validarUsuarioWebservice(java.lang.String usuario, java.lang.String clave, int proyecto) {
    // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
    // If the calling of port operations may lead to race condition some synchronization is required.
    pe.siso.webservicesseguridad.webservices.UsuarioWebService port = usuarioservice.getUsuarioWebServicePort();
    return port.validarUsuarioWebservice(usuario, clave, proyecto);
  }

  private java.util.List<pe.siso.webservicesseguridad.webservices.MenuBean> listarCategoriaWebservice(int codigoProyectoDetalle) {
    // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
    // If the calling of port operations may lead to race condition some synchronization is required.
    pe.siso.webservicesseguridad.webservices.MenuWebService port = menuservice.getMenuWebServicePort();
    return port.listarCategoriaWebservice(codigoProyectoDetalle);
  }

  private java.util.List<pe.siso.webservicesseguridad.webservices.MenuBean> listarModuloWebservice(int codigoProyectoDetalle) {
    // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
    // If the calling of port operations may lead to race condition some synchronization is required.
    pe.siso.webservicesseguridad.webservices.MenuWebService port = menuservice.getMenuWebServicePort();
    return port.listarModuloWebservice(codigoProyectoDetalle);
  }

  private java.util.List<pe.siso.webservicesseguridad.webservices.MenuBean> listarSubCategoriaWebservice(int codigoProyectoDetalle) {
    // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
    // If the calling of port operations may lead to race condition some synchronization is required.
    pe.siso.webservicesseguridad.webservices.MenuWebService port = menuservice.getMenuWebServicePort();
    return port.listarSubCategoriaWebservice(codigoProyectoDetalle);
  }

  private java.util.List<pe.siso.webservicesseguridad.webservices.MenuBean> listarTituloWebservice(int codigoProyectoDetalle) {
    // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
    // If the calling of port operations may lead to race condition some synchronization is required.
    pe.siso.webservicesseguridad.webservices.MenuWebService port = menuservice.getMenuWebServicePort();
    return port.listarTituloWebservice(codigoProyectoDetalle);
  }

  private ProyectoBean consultarProyecto(java.lang.Integer name) {
    // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
    // If the calling of port operations may lead to race condition some synchronization is required.
    pe.siso.webservicesseguridad.webservices.ProyectoWebService port = proyectoservice.getProyectoWebServicePort();
    return port.consultarProyecto(name);
  }

  private java.util.List<pe.siso.webservicesseguridad.webservices.ProyectoBean> listarProyectoUsuario(int codigoUsuario) {
    // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
    // If the calling of port operations may lead to race condition some synchronization is required.
    pe.siso.webservicesseguridad.webservices.ProyectoWebService port = proyectoservice.getProyectoWebServicePort();
    return port.listarProyectoUsuario(codigoUsuario);
  }

}
