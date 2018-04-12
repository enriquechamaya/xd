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
import trismegistoplanilla.beans.EstadoFichaBean;
import trismegistoplanilla.beans.PersonaBean;
import trismegistoplanilla.beans.TokenFichaBean;
import trismegistoplanilla.services.PersonaService;
import trismegistoplanilla.services.TipoDocumentoService;
import trismegistoplanilla.utilities.ParamsValidation;

public class PersonaServlet extends HttpServlet {

  private static final long serialVersionUID = 767323838778604646L;

  HttpSession session = null;
  String mensaje = "";

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    session = request.getSession();

    String accion = request.getParameter("accion");
    System.out.println("accion => " + accion);
    if (accion == null) {

    } else {
      switch (accion) {
        case "validarExistenciaNumeroDocumento":
          validarExistenciaNumeroDocumento(request, response);
          break;
        case "validarExistenciaCorreoElectronico":
          validarExistenciaCorreoElectronico(request, response);
          break;
        case "registrarNuevaFicha":
          registrarNuevaFicha(request, response);
          break;
        case "verificarPersona":
          verificarPersona(request, response);
          break;
        default:
          break;
      }
    }

  }

  private void validarExistenciaNumeroDocumento(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();

    // valida si los parametros son nulos
    if (request.getParameter("codigoTipoDocumento") == null || request.getParameter("numeroDocumento") != null || request.getParameter("longitud") != null || request.getParameter("tipoEntrada") != null) {
      int codigoTipoDocumento = Integer.parseInt(request.getParameter("codigoTipoDocumento"));
      String numeroDocumento = request.getParameter("numeroDocumento");
      int longitud = Integer.parseInt(request.getParameter("longitud"));
      String tipoEntrada = request.getParameter("tipoEntrada");

      boolean validarLongitud = ParamsValidation.validaSoloNumeros(longitud);
      if (validarLongitud) { // valida que la lontiud sea un numero
        boolean validarLongitudNumeroDocumento = ParamsValidation.validaLongitudNumeroDocumento(longitud, numeroDocumento);
        if (validarLongitudNumeroDocumento) { // valida la longitud del numero de documento
          boolean validarTipoEntradaLongitud = ParamsValidation.validaNumeroDocumento(tipoEntrada, numeroDocumento);
          if (validarTipoEntradaLongitud) { // valida el tipo de entrada del numero de documento
            PersonaService service = new PersonaService();
            PersonaBean p = new PersonaBean();
            p.setCodigoTipoDocumento(codigoTipoDocumento);
            p.setNumeroDocumento(numeroDocumento);
            JSONObject jsonObjValidarExistenciaNumeroDocumento = service.validarExistenciaNumeroDocumento(p);
            out.print(jsonObjValidarExistenciaNumeroDocumento);
          } else {
            out.print("Ha sucedido un error, al parecer el número de documento no corresponde al tipo de entrada Alfanumerico o Numerico");
//                        request.setAttribute("error", "Ha sucedido un error, al parecer el número de documento no corresponde al tipo de entrada Alfanumerico o Numerico");
//                        request.getRequestDispatcher("ErrorPage.jsp").forward(request, response);
          }
        } else {
          out.print("Ha sucedido un error, al parecer la longitud del numero de documento no es correcta");
//                    request.setAttribute("error", "Ha sucedido un error, al parecer la longitud del numero de documento no es correcta");
//                    request.getRequestDispatcher("ErrorPage.jsp").forward(request, response);
        }
      } else {
        out.print("Ha sucedido un error, al parecer el dato ingresado no es un dato númerico");
//                request.setAttribute("error", "Ha sucedido un error, al parecer el dato ingresado no es un dato númerico");
//                request.getRequestDispatcher("ErrorPage.jsp").forward(request, response);
      }
    } else {
      out.print("Ha sucedido un error, hay datos nulos al validar la existencia del numero de documento");
//            request.setAttribute("error", "Ha sucedido un error, hay datos nulos al validar la existencia del numero de documento");
//            request.getRequestDispatcher("ErrorPage.jsp").forward(request, response);
    }
  }

  private void validarExistenciaCorreoElectronico(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    JSONObject jsonObjValidarExistenciaCorreoElectronico = null;
    if (request.getParameter("correoElectronico") != null) {
      String correoElectronico = request.getParameter("correoElectronico");
      boolean validarCorreoElectronico = ParamsValidation.validaCorreo(correoElectronico);
      if (validarCorreoElectronico) {
        PersonaService service = new PersonaService();
        PersonaBean p = new PersonaBean();
        p.setCorreo(correoElectronico);
        jsonObjValidarExistenciaCorreoElectronico = service.validarExistenciaCorreoElectronico(p);
        out.print(jsonObjValidarExistenciaCorreoElectronico);
      } else {
        jsonObjValidarExistenciaCorreoElectronico = new JSONObject();
        jsonObjValidarExistenciaCorreoElectronico.put("status", false);
        jsonObjValidarExistenciaCorreoElectronico.put("message", "Ha sucedido un error, el dato ingresado no es un correo electrónico");
        out.print(jsonObjValidarExistenciaCorreoElectronico);
      }
    } else {
      jsonObjValidarExistenciaCorreoElectronico = new JSONObject();
      jsonObjValidarExistenciaCorreoElectronico.put("status", false);
      jsonObjValidarExistenciaCorreoElectronico.put("message", "Ha sucedido un error, el dato llegó nulo");
      out.print(jsonObjValidarExistenciaCorreoElectronico);
    }

  }

  private void registrarNuevaFicha(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    String json = request.getParameter("json");
    JSONObject jsonPersona = new JSONObject(json);
    System.out.println(jsonPersona);
    session = request.getSession();
    UsuarioBean usuario = (UsuarioBean) session.getAttribute("usuario");

    EstadoFichaBean ef = new EstadoFichaBean();
    ef.setCodigoUsuario(usuario.getCodigoUsuario());
    System.out.println("usuario ==========================> " + usuario.getCodigoUsuario());
//    out.println("apellidoPaterno : " + jsonPersona.getString("apellidoPaterno"));
//    out.println("apellidoMaterno : " + jsonPersona.getString("apellidoMaterno"));
//    out.println("nombre : " + jsonPersona.getString("nombre"));
//    out.println("codigoTipoDocumento : " + jsonPersona.getInt("codigoTipoDocumento"));
//    out.println("longitud - numeroDocumento : " + jsonPersona.getInt("longitud") + " - " + jsonPersona.getString("numeroDocumento"));
//    out.println("tipoEntrada-numeroDocumento : " + jsonPersona.getString("tipoEntrada") + " - " + jsonPersona.getString("numeroDocumento"));
//    out.println("correo : " + jsonPersona.getString("correo"));
//    out.println("*******************************************");
//    out.println("apellidoPaterno : " + ParamsValidation.validaSoloLetras(jsonPersona.getString("apellidoPaterno")));
//    out.println("apellidoMaterno : " + ParamsValidation.validaSoloLetras(jsonPersona.getString("apellidoMaterno")));
//    out.println("nombre : " + ParamsValidation.validaSoloLetras(jsonPersona.getString("nombre")));
//    out.println("codigoTipoDocumento : " + ParamsValidation.validaSoloNumeros(jsonPersona.getInt("codigoTipoDocumento")));
//    out.println("longitud - numeroDocumento : " + ParamsValidation.validaLongitudNumeroDocumento(jsonPersona.getInt("longitud"), jsonPersona.getString("numeroDocumento")));
//    out.println("tipoEntrada-numeroDocumento : " + ParamsValidation.validaNumeroDocumento(jsonPersona.getString("tipoEntrada"), jsonPersona.getString("numeroDocumento")));
//    out.println("correo : " + ParamsValidation.validaCorreo(jsonPersona.getString("correo")));
    if (jsonPersona.getBoolean("flagValidacionReniec")) {
      if (ParamsValidation.validaSoloLetras(jsonPersona.getString("apellidoPaterno"))
              && ParamsValidation.validaSoloLetras(jsonPersona.getString("apellidoMaterno"))
              && ParamsValidation.validaSoloLetras(jsonPersona.getString("nombre"))
              && ParamsValidation.validaSoloNumeros(jsonPersona.getInt("codigoTipoDocumento"))
              && ParamsValidation.validaLongitudNumeroDocumento(jsonPersona.getInt("longitud"), jsonPersona.getString("numeroDocumento"))
              && ParamsValidation.validaNumeroDocumento(jsonPersona.getString("tipoEntrada"), jsonPersona.getString("numeroDocumento"))
              && ParamsValidation.validaCorreo(jsonPersona.getString("correo"))) {

        String apellidoPaterno = jsonPersona.getString("apellidoPaterno");
        String apellidoMaterno = jsonPersona.getString("apellidoMaterno");
        String nombre = jsonPersona.getString("nombre");
        int codigoTipoDocumento = jsonPersona.getInt("codigoTipoDocumento");
        String numeroDocumento = jsonPersona.getString("numeroDocumento");
        String correo = jsonPersona.getString("correo");
        int isDefaultMail = jsonPersona.getInt("isDefaultMail");

        PersonaBean p = new PersonaBean();
        p.setApellidoPaterno(apellidoPaterno);
        p.setApellidoMaterno(apellidoMaterno);
        p.setNombre(nombre);
        p.setCodigoTipoDocumento(codigoTipoDocumento);
        p.setNumeroDocumento(numeroDocumento);
        p.setCorreo(correo);
        p.setIsDefaultMail(isDefaultMail);
        PersonaService servicePersona = new PersonaService();
        JSONObject jsonObjRegistrarPersona = servicePersona.registrarPersona(p, ef);
        out.print(jsonObjRegistrarPersona);
      } else {
        out.print("nacional es false");
      }
    } /* EXTRANJERO */ else {
      if (ParamsValidation.validaSoloNumeros(jsonPersona.getInt("codigoTipoDocumento"))
              && ParamsValidation.validaLongitudNumeroDocumento(jsonPersona.getInt("longitud"), jsonPersona.getString("numeroDocumento"))
              && ParamsValidation.validaNumeroDocumento(jsonPersona.getString("tipoEntrada"), jsonPersona.getString("numeroDocumento"))
              && ParamsValidation.validaCorreo(jsonPersona.getString("correo"))) {

        int codigoTipoDocumento = jsonPersona.getInt("codigoTipoDocumento");
        String numeroDocumento = jsonPersona.getString("numeroDocumento");
        String correo = jsonPersona.getString("correo");
        int isDefaultMail = jsonPersona.getInt("isDefaultMail");

        PersonaBean p = new PersonaBean();
        p.setApellidoPaterno(null);
        p.setApellidoMaterno(null);
        p.setNombre(null);
        p.setCodigoTipoDocumento(codigoTipoDocumento);
        p.setNumeroDocumento(numeroDocumento);
        p.setCorreo(correo);
        p.setIsDefaultMail(isDefaultMail);
        PersonaService servicePersona = new PersonaService();
        JSONObject jsonObjRegistrarPersona = servicePersona.registrarPersona(p, ef);
        out.print(jsonObjRegistrarPersona);
      } else {
        out.print("extranjeroes false");
      }
    }

  }

  private void verificarPersona(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    System.out.println("************* SERVLET - VERIFICAR PERSONA *************");
    String codigoVerificacion = request.getParameter("codigoVerificacion");
    String numeroDocumento = request.getParameter("numeroDocumento");
    System.out.println("Obtencion de parametros de verificacion.jsp:");
    System.out.println("1) codigoVerificacion: " + codigoVerificacion);
    System.out.println("2) numeroDocumento: " + numeroDocumento);
    System.out.println("--------------------------------");
    // verificamos que los parametros enviados desde el jsp no sean nulos
    if (codigoVerificacion != null && numeroDocumento != null) {
      // validar que los parametros sean solo alfanumericos
      if (ParamsValidation.validaSoloAlfanumerico(codigoVerificacion) && ParamsValidation.validaSoloAlfanumerico(numeroDocumento)) {
        // obtenemos los valores de session
        TokenFichaBean tf = (TokenFichaBean) session.getAttribute("tokenFicha");
        tf.setCodigoVerificacion(codigoVerificacion);
        System.out.println("Variable de session (TokenFicha):");
        System.out.println("1) tokenFicha: " + tf);
        System.out.println("2) codigoTokenFicha: " + tf.getCodigoTokenFicha());
        System.out.println("3) token: " + tf.getToken());
        System.out.println("4) codigoVerificacion: " + tf.getCodigoVerificacion());
        System.out.println("--------------------------------");
        // instanciar objeto persona y setear valores
        PersonaBean p = new PersonaBean();
        p.setNumeroDocumento(numeroDocumento);
        System.out.println("Instancia personaBean:");
        System.out.println("1) personaBean: " + p);
        System.out.println("2) numeroDocumento: " + p.getNumeroDocumento());
        System.out.println("--------------------------------");
        // instanciar servicio persona
        System.out.println("Verificacion persona:");
        PersonaService service = new PersonaService();
        System.out.println("1) Persona Service: " + service);
        // invocar al metodo de verificacion de persona
        JSONObject jsonObjVerificarPersona = service.verificarPersona(tf, p);
        System.out.println("2) json verificar persona: " + jsonObjVerificarPersona);
        System.out.println("--------------------------------");
        // validar repuesta del metodo verificar persona
        if (jsonObjVerificarPersona.getBoolean("status")) {
          // obtener codigo tipo de documento
          System.out.println("obtener codigo tipo documento:");
          TipoDocumentoService serviceTipoDocumento = new TipoDocumentoService();
          JSONObject jsonObjObtenerCodigoTipoDocumento = serviceTipoDocumento.obtenerCodigoTipoDocumento(tf);
          System.out.println("1) service Tipo Documento: " + serviceTipoDocumento);
          System.out.println("2) json obtener tipo documento: " + jsonObjObtenerCodigoTipoDocumento);
          // validar respuesta del metodo obtener codigo tipo documento
          if (jsonObjObtenerCodigoTipoDocumento.getBoolean("status")) {
            int codigoTipoDocumento = jsonObjObtenerCodigoTipoDocumento.getJSONObject("data").getInt("getResultedKey");
            System.out.println("3) codigoTipoDocumento: " + codigoTipoDocumento);
            System.out.println("--------------------------------");
            System.out.println("obtener codigo persona:");
            JSONObject jsonObjObtenerCodigoPersona = service.obtenerCodigoPersona(tf);
            System.out.println("1) json obtener codigo persona: " + jsonObjObtenerCodigoPersona);
            // validar respuesta del metodo obtener codigo persona
            if (jsonObjObtenerCodigoPersona.getBoolean("status")) {
              int codigoPersona = jsonObjObtenerCodigoPersona.getJSONObject("data").getInt("getResultedKey");
              System.out.println("2) codigoPersona: " + codigoPersona);
              System.out.println("--------------------------------");
              System.out.println("Obtener persona:");
              // instacia de la clase persona
              p.setCodigoPersona(codigoPersona);
              p.setCodigoTipoDocumento(codigoTipoDocumento);
              JSONObject jsonObjObtenerPersonaSession = service.obtenerPersonaSession(p);
              System.out.println("1) Param codigoPersona: " + codigoPersona);
              System.out.println("2) Param codigoTipoDocumento: " + codigoTipoDocumento);
              System.out.println("3) json obtener persona session: " + jsonObjObtenerPersonaSession);
              // validar respuesta del metodo obtener persona session
              if (jsonObjObtenerPersonaSession.getBoolean("status")) {
                System.out.println("--------------------------------");
                System.out.println("variables para el envio de sesion:");
                JSONObject jsonObjPersona = jsonObjObtenerPersonaSession.getJSONObject("data");
                JSONArray jsonArrPersona = jsonObjPersona.getJSONArray("personas");

                if (jsonArrPersona.getJSONObject(0).isNull("apellidoPaterno")) {
                  System.out.println("1) apellidoPaterno: null");
                  p.setApellidoPaterno(null);
                } else {
                  p.setApellidoPaterno(jsonArrPersona.getJSONObject(0).getString("apellidoPaterno"));
                  System.out.println("1) apellidoPaterno: " + jsonArrPersona.getJSONObject(0).getString("apellidoPaterno"));
                }
                if (jsonArrPersona.getJSONObject(0).isNull("apellidoMaterno")) {
                  p.setApellidoMaterno(null);
                  System.out.println("2) apellidoMaterno: null");
                } else {
                  p.setApellidoMaterno(jsonArrPersona.getJSONObject(0).getString("apellidoMaterno"));
                  System.out.println("2) apellidoMaterno: " + jsonArrPersona.getJSONObject(0).getString("apellidoMaterno"));
                }
                if (jsonArrPersona.getJSONObject(0).isNull("nombre")) {
                  p.setNombre(null);
                  System.out.println("3) nombre: null");
                } else {
                  p.setNombre(jsonArrPersona.getJSONObject(0).getString("nombre"));
                  System.out.println("3) nombre: " + jsonArrPersona.getJSONObject(0).getString("nombre"));
                }
                p.setCorreo(jsonArrPersona.getJSONObject(0).getString("correo"));
                System.out.println("4) correo: " + jsonArrPersona.getJSONObject(0).getString("correo"));
                p.setNombreTipoDocumento(jsonArrPersona.getJSONObject(0).getString("nombreTipoDocumento"));
                System.out.println("5) nombreTipoDocumento: " + jsonArrPersona.getJSONObject(0).getString("nombreTipoDocumento"));
                if (codigoTipoDocumento == 3) {
                  p.setRuc(numeroDocumento);
                }
                session.setAttribute("persona", p);
                response.sendRedirect("vistas/ficha.jsp");
              } else {
                mensaje = jsonObjObtenerPersonaSession.getString("message");
                session.setAttribute("errorTokenFicha", mensaje);
                response.sendRedirect("vistas/response/ErrorToken.jsp");
                session.removeAttribute("tokenFicha");
              }
            } else {
              mensaje = jsonObjObtenerCodigoPersona.getString("message");
              session.setAttribute("errorTokenFicha", mensaje);
              response.sendRedirect("vistas/response/ErrorToken.jsp");
              session.removeAttribute("tokenFicha");
            }
          } else {
            mensaje = jsonObjObtenerCodigoTipoDocumento.getString("message");
            session.setAttribute("errorTokenFicha", mensaje);
            response.sendRedirect("vistas/response/ErrorToken.jsp");
            session.removeAttribute("tokenFicha");
          }
        } else {
          mensaje = jsonObjVerificarPersona.getString("message");
          session.setAttribute("errorTokenFicha", mensaje);
          response.sendRedirect("vistas/response/ErrorToken.jsp");
          session.removeAttribute("tokenFicha");
        }
      } else {
        mensaje = "Los valores enviados no son alfanuméricos";
        session.setAttribute("errorTokenFicha", mensaje);
        response.sendRedirect("vistas/response/ErrorToken.jsp");
        session.removeAttribute("tokenFicha");
      }
    } else {
      mensaje = "Los valores son nulos";
      session.setAttribute("errorTokenFicha", mensaje);
      response.sendRedirect("vistas/response/ErrorToken.jsp");
      session.removeAttribute("tokenFicha");
    }

    System.out.println("************* /SERVLET - VERIFICAR PERSONA *************");
  }

}
