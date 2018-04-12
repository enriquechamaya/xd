package trismegistoplanilla.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;
import trismegistoplanilla.beans.TokenFichaBean;
import trismegistoplanilla.services.FichaServices;
import trismegistoplanilla.services.TokenFichaService;
import trismegistoplanilla.utilities.EncryptAction;

public class TokenFichaServlet extends HttpServlet {

  private static final long serialVersionUID = 8829725917469912298L;

  HttpSession sessionTokenFicha = null;
  String mensaje = "";

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // SESSION: instancia de session de respuestas
    sessionTokenFicha = request.getSession();

    // PARAMTERO ACCION: obtener valor del parametro accion
    String accion = request.getParameter("accion");

    // ACCION: validar que accion no sea nulo
    if (accion != null) {
      try {
        if (accion.equals("validarTokenFichaVerificacion")) {  // ACCION: validar token ficha "verificacion.jsp"
          System.out.println("accion validarTokenFichaVerificacion correcto");
          validarTokenFichaVerificacion(request, response);
        } else if (accion.equals("desactivarToken")) { // ACCION: desactivar token
          desactivarToken(request, response);
        } // ACION: VALDIAR TOKEN FICHA
        else if (EncryptAction.Desencriptar(accion, "TR1SM3G1ST0-PL4N1LL4").equals("validarTokenFicha")) { // ACCION: validar si la desencriptacion de datos es igual a "validarTokenFicha"
          System.out.println("accion correcta");
          validarTokenFicha(request, response);
        } else { // ACCION: ERROR 
          mensaje = "No pudimos determinar a qué URL intentabas acceder. Por favor, verifique para asegurarse de que su URL no esté truncada";
          sessionTokenFicha.setAttribute("errorTokenFicha", mensaje);
          response.sendRedirect("vistas/response/ErrorToken.jsp");
        }
      } catch (Exception ex) {
        System.out.println("Error desencriptacion de datos servlet: " + ex.getMessage());
        mensaje = "Error, no se pudo desencriptar los datos";
        sessionTokenFicha.setAttribute("errorTokenFicha", mensaje);
//                response.sendRedirect("vistas/response/ErrorToken.jsp");
      }
    } else { // ACCION: accion es nulo (redireccionar a la página de error)
      System.out.println("accion es nulo");
      // accion error page
      mensaje = "Error, no se encontró el parámetro 'accion'";
      sessionTokenFicha.setAttribute("errorTokenFicha", mensaje);
      response.sendRedirect("vistas/response/ErrorToken.jsp");
    }
  }

  private void validarTokenFicha(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, Exception {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    // obtener parametro id
    String id = request.getParameter("id");

    // ID: validar que el parametro ID no sea nulo
    if (id != null) {
      System.out.println("id => " + id);
      System.out.println("id d. => " + EncryptAction.Desencriptar(id, "TR1SM3G1ST0-ID-PL4N1LL4"));

      // ID: validar si el ID desencriptado es igual a lo encriptado
      if (id.equals(EncryptAction.Encriptar(EncryptAction.Desencriptar(id, "TR1SM3G1ST0-ID-PL4N1LL4"), "TR1SM3G1ST0-ID-PL4N1LL4"))) { // ID: el id es igual a lo encriptado
        // desencriptar id
        int codigoTokenFicha = Integer.parseInt(EncryptAction.Desencriptar(id, "TR1SM3G1ST0-ID-PL4N1LL4"));
        System.out.println("codigoTokenFicha => " + codigoTokenFicha);

        // obtener parametro token
        String token = request.getParameter("token");

        // TOKEN: validar que el parametro TOKEN no sea nulo
        if (token != null) {
          System.out.println("token => " + token);

          /* VALIDAR QUE EL TOKEN SEA IGUAL A LO DE LA URL */
          // guardar parametros en el objeto token ficha
          TokenFichaBean tf = new TokenFichaBean();
          tf.setCodigoTokenFicha(codigoTokenFicha);
          tf.setToken(token);

          // OBETENE CODIGO FICHA Y SESIONARLO
//                    FichaServices serviceFicha = new FichaServices();
//                    JSONObject jsonObjObtenerCodigoFicha = serviceFicha.obtenerCodigoFicha(tf);
//                    int codigoFicha = jsonObjObtenerCodigoFicha.getJSONObject("data").getInt("getResultedKey");
//                    tf.setCodigoFicha(codigoFicha);
          // instancia del service token ficha
          TokenFichaService services = new TokenFichaService();

          // invocar al método de validacion de token por URL
          JSONObject jsonObjValidarTokenURL = services.validarTokenURL(tf);
          out.print("VALIDAR TOKEN URL => " + jsonObjValidarTokenURL);

          // validar si la url es true (correcta)
          if (jsonObjValidarTokenURL.getBoolean("status")) {

            // OBETENE CODIGO FICHA Y SESIONARLO
            FichaServices serviceFicha = new FichaServices();
            JSONObject jsonObjObtenerCodigoFicha = serviceFicha.obtenerCodigoFicha(tf);
            int codigoFicha = jsonObjObtenerCodigoFicha.getJSONObject("data").getInt("getResultedKey");
            tf.setCodigoFicha(codigoFicha);

            /* VALIDAR TOKEN (CODIGO_TOKEN = [PARAM_CODIGO_TOKEN], TOKEN = [PARAM_TOKEN], FECHA_EXPIRACION >= GETDATE(), ESTADO = 1) */
            // invocar al método de token
            JSONObject jsonObjValidarToken = services.validarToken(tf);
            out.print("VALIDAR TOKEN => " + jsonObjValidarToken);

            // validar que el token cumpla con las condiciones mencionadas anteriormente es true (correcto)
            if (jsonObjValidarToken.getBoolean("status")) {
              System.out.println("************SESION VALID******************");
              if (sessionTokenFicha.isNew()) {
                System.out.println("NUEVA SESION");
              } else {
                System.out.println("ANTIGUA SESION");
                sessionTokenFicha.invalidate();
              }
              System.out.println("************SESION VALID******************");

              // es correcto, redireccionar a la verificacion
              sessionTokenFicha = request.getSession();
              sessionTokenFicha.setAttribute("tokenFicha", tf);
              response.sendRedirect("vistas/verificacion.jsp");
            } else {
              // desactivar token
              JSONObject jsonObjDesactivarToken = services.desactivarToken(tf);

              // validar que el token se haya desactivado con éxito
              if (jsonObjDesactivarToken.getBoolean("status")) {
                // el token no es válido a caducado o a expirado
                mensaje = "El token no existe o ha caducado";
                System.out.println("=>>>>> " + mensaje);
                System.out.println("Error token ha expirado");
                sessionTokenFicha.setAttribute("errorTokenFicha", mensaje);
                response.sendRedirect("vistas/response/ErrorToken.jsp");
                sessionTokenFicha.removeAttribute("tokenFicha");
              } else {
                // el token no es válido a caducado o a expirado - salio un error al desactivar el token
                mensaje = "El token no existe o ha caducado";
                System.out.println("=>>>>> " + mensaje);
                System.out.println("Error al desactivar el token");
                sessionTokenFicha.setAttribute("errorTokenFicha", mensaje);
                response.sendRedirect("vistas/response/ErrorToken.jsp");
                sessionTokenFicha.removeAttribute("tokenFicha");
              }
            }
          } else { // la url es false (incorrecta)
            mensaje = jsonObjValidarTokenURL.getString("message");
            System.out.println("=>>>>> " + mensaje);
            System.out.println("Error el token no es válido error url");
            sessionTokenFicha.setAttribute("errorTokenFicha", mensaje);
            response.sendRedirect("vistas/response/ErrorToken.jsp");
            sessionTokenFicha.removeAttribute("tokenFicha");
          }
        } else { // TOKEN: token es nulo (redireccionar a la página de error)
          System.out.println("token es nulo");
          // accion error page
          mensaje = "Error, no se encontró el parámetro 'token'";
          System.out.println("=>>>>> " + mensaje);
          System.out.println("Error el token es nulo");
          sessionTokenFicha.setAttribute("errorTokenFicha", mensaje);
          response.sendRedirect("vistas/response/ErrorToken.jsp");
          sessionTokenFicha.removeAttribute("tokenFicha");
        }
      } else { // ID: ERROR
        mensaje = "No pudimos determinar a qué URL intentabas acceder. Por favor, verifique para asegurarse de que su URL no esté truncada";
        System.out.println("=>>>>> " + mensaje);
        System.out.println("Error url falsa");
        sessionTokenFicha.setAttribute("errorTokenFicha", mensaje);
        response.sendRedirect("vistas/response/ErrorToken.jsp");
        sessionTokenFicha.removeAttribute("tokenFicha");
      }
    } else { // ID: id es nulo (redireccionar a la página de error)
      System.out.println("id es nulo");
      // accion error page
      mensaje = "Error, no se encontró el parámetro 'id'";
      System.out.println("=>>>>> " + mensaje);
      System.out.println("Error id nulo");
      sessionTokenFicha.setAttribute("errorTokenFicha", mensaje);
      response.sendRedirect("vistas/response/ErrorToken.jsp");
      sessionTokenFicha.removeAttribute("tokenFicha");
    }
  }

  private void validarTokenFichaVerificacion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    // obtener session token ficha verificacion
    TokenFichaBean tf = (TokenFichaBean) sessionTokenFicha.getAttribute("tokenFicha");

    // valida que la session no sea nula
    if (tf != null) {

      // instancia del service token ficha
      TokenFichaService services = new TokenFichaService();

      // invocar al método de token
      JSONObject jsonObjValidarToken = services.validarToken(tf);

      // imprimir resultado metodo
      out.println(jsonObjValidarToken);

    } else { // la sesion es nula
      mensaje = "No se ha encontrado ninguna session activa";
      sessionTokenFicha.setAttribute("errorTokenFicha", mensaje);
      response.sendRedirect("vistas/response/ErrorToken.jsp");
      sessionTokenFicha.removeAttribute("tokenFicha");
    }
  }

  private void desactivarToken(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // obtener session token ficha verificacion
    TokenFichaBean tf = (TokenFichaBean) sessionTokenFicha.getAttribute("tokenFicha");

    if (tf != null) {
      // instancia del service token ficha
      TokenFichaService services = new TokenFichaService();

      // desactivar token
      JSONObject jsonObjDesactivarToken = services.desactivarToken(tf);

      // validar que el token se haya desactivado con éxito
      if (jsonObjDesactivarToken.getBoolean("status") || !jsonObjDesactivarToken.getBoolean("status")) {
        // el token no es válido a caducado o a expirado
        mensaje = "El token no existe o ha caducado";
        sessionTokenFicha.setAttribute("errorTokenFicha", mensaje);
        response.sendRedirect("vistas/response/ErrorToken.jsp");
        sessionTokenFicha.removeAttribute("tokenFicha");
      }
    } else {
      mensaje = "La session ha expirado, por favor vuelva a ingresar";
      sessionTokenFicha.setAttribute("errorTokenFicha", mensaje);
      response.sendRedirect("vistas/response/ErrorToken.jsp");
      sessionTokenFicha.removeAttribute("tokenFicha");
    }
  }

}
