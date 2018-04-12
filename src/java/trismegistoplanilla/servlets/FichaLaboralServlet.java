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
import trismegistoplanilla.beans.FichaLaboralBean;
import trismegistoplanilla.beans.PersonaBean;
import trismegistoplanilla.beans.SueldoAdministrativoBean;
import trismegistoplanilla.beans.SueldoDocenteBean;
import trismegistoplanilla.services.FichaLaboralService;
import trismegistoplanilla.utilities.ParamsValidation;

public class FichaLaboralServlet extends HttpServlet {

  private static final long serialVersionUID = -7238924361724961239L;

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String accion = request.getParameter("accion");
    if (accion.equals("registrarFichaLaboral")) {
      registrarFichaLaboral(request, response);
    }
  }

  private void registrarFichaLaboral(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();

    JSONObject JORegistrarFichaLaboralRequest = null;

    if (request.getParameter("json") == null) {
      JORegistrarFichaLaboralRequest = new JSONObject();
      JORegistrarFichaLaboralRequest.put("status", false);
      JORegistrarFichaLaboralRequest.put("message", "El parámetro 'json' llegó nulo");
      out.print(JORegistrarFichaLaboralRequest);
    } else {
      HttpSession session = request.getSession();
      UsuarioBean u = (UsuarioBean) session.getAttribute("usuario");

      String data = request.getParameter("json");
      JSONObject json = new JSONObject(data);

      JSONObject jsonValid = validarJson(request, response, json);

      if (jsonValid.getBoolean("status")) {

        JSONArray jaExpediente = json.getJSONArray("expediente");

        PersonaBean p = new PersonaBean();
        p.setEnlaceAlfresco(json.getString("enlaceAlfresco"));

        FichaLaboralBean fl = new FichaLaboralBean();
        fl.setCodigoFicha(json.getInt("codigoFicha"));
        fl.setFechaIngreso(json.getString("fechaIngreso"));
        fl.setFechaFin(json.getString("fechaTermino"));
        fl.setCodigoSedeArea(json.getInt("codigoSedeArea"));
        fl.setCodigoAreaCargo(json.getInt("codigoAreaCargo"));
        fl.setTipoFicha(json.getString("tipoFicha"));

        SueldoAdministrativoBean sa = new SueldoAdministrativoBean();
        SueldoDocenteBean sd = new SueldoDocenteBean();
        if (json.getString("tipoFicha").equals("A")) { // administrativo
          sa.setCodigoFicha(json.getInt("codigoFicha"));
          sa.setCodigoAreaCargoTipoPago(json.getInt("codigoAreaCargoTipoPago"));
          sa.setSueldoEscalafon(json.getString("sueldoEscalafon"));
          sa.setSueldoMensual(json.getString("sueldoMensual"));
          sa.setObservacion(json.getString("observacion"));
        } else if (json.getString("tipoFicha").equals("D")) { // docente
          sd.setCodigoFicha(json.getInt("codigoFicha"));
          sd.setCodigoAreaCargoTipoPago(json.getInt("codigoAreaCargoTipoPago"));
          sd.setObservacion(json.getString("observacion"));
          switch (json.getInt("codigoAreaCargoTipoPago")) {
            case 5:
              sd.setCostoa(json.getString("costoA"));
              sd.setCostob(json.getString("costoB"));
              sd.setCostoc(json.getString("costoC"));
              break;
            case 6:
              sd.setCostoMensual(json.getString("sueldoMensual"));
              break;
            case 19:
              sd.setCostoa(json.getString("costoA"));
              sd.setCostob(json.getString("costoB"));
              sd.setCostoc(json.getString("costoC"));
              sd.setCostoMensual(json.getString("sueldoMensual"));
              break;
            default:
              break;
          }
        }

        FichaLaboralService service = new FichaLaboralService();

        JORegistrarFichaLaboralRequest = service.registrarFichaLaboral(fl, p, jaExpediente, sa, sd, u);

        out.print(JORegistrarFichaLaboralRequest);

      } else {
        out.print(jsonValid);
      }

    }

  }

  private JSONObject validarJson(HttpServletRequest request, HttpServletResponse response, JSONObject json) throws ServletException, IOException {
    JSONObject jsonResponse = new JSONObject();

    jsonResponse.put("status", "true");

    if (!ParamsValidation.validaSoloNumeros(json.getInt("codigoFicha"))) {
      jsonResponse.put("status", "false");
      jsonResponse.put("message", "Ocurrió un error con el parámetro código ficha");
    }

    if (json.getJSONArray("expediente").length() == 0) {
      jsonResponse.put("status", "false");
      jsonResponse.put("message", "Ocurrió un error, los expedientes están vacíos");
    }

    if (!ParamsValidation.validaSoloFecha(json.getString("fechaIngreso"))) {
      jsonResponse.put("status", "false");
      jsonResponse.put("message", "Ocurrió un error con la fecha de ingreso");
    }

    if (!ParamsValidation.validaSoloFecha(json.getString("fechaTermino"))) {
      jsonResponse.put("status", "false");
      jsonResponse.put("message", "Ocurrió un error con la fecha de termino");
    }

    if (!ParamsValidation.validaSoloNumeros(json.getInt("codigoSedeArea"))) {
      jsonResponse.put("status", "false");
      jsonResponse.put("message", "Ocurrió un error con el parámetro codigoSedeArea");
    }

    if (!ParamsValidation.validaSoloNumeros(json.getInt("codigoAreaCargo"))) {
      jsonResponse.put("status", "false");
      jsonResponse.put("message", "Ocurrió un error con el parámetro codigoAreaCargo");
    }

    if (!ParamsValidation.validaLongitudCadena(1, json.getString("tipoFicha"))) {
      jsonResponse.put("status", "false");
      jsonResponse.put("message", "Ocurrió un error con el parámetro tipoFicha");
    }

    if (json.getString("tipoFicha").equals("A")) { // administrativo

      if (!ParamsValidation.validaSoloDecimal(json.getString("sueldoEscalafon"))) {
        jsonResponse.put("status", "false");
        jsonResponse.put("message", "Ocurrió un error con el parámetro sueldoEscalafon");
      }

      if (!ParamsValidation.validaSoloDecimal(json.getString("sueldoMensual"))) {
        jsonResponse.put("status", "false");
        jsonResponse.put("message", "Ocurrió un error con el parámetro sueldoMensual");
      }

    } else if (json.getString("tipoFicha").equals("D")) { // docente

      if (!ParamsValidation.validaSoloNumeros(json.getInt("codigoAreaCargoTipoPago"))) {
        jsonResponse.put("status", "false");
        jsonResponse.put("message", "Ocurrió un error con el parámetro codigoAreaCargoTipoPago");
      }

      switch (json.getInt("codigoAreaCargoTipoPago")) {
        case 5:
          if (!ParamsValidation.validaSoloDecimal(json.getString("costoA"))) {
            jsonResponse.put("status", "false");
            jsonResponse.put("message", "Ocurrió un error con el parámetro costoA");
          }
          if (!ParamsValidation.validaSoloDecimal(json.getString("costoB"))) {
            jsonResponse.put("status", "false");
            jsonResponse.put("message", "Ocurrió un error con el parámetro costoB");
          }
          if (!ParamsValidation.validaSoloDecimal(json.getString("costoC"))) {
            jsonResponse.put("status", "false");
            jsonResponse.put("message", "Ocurrió un error con el parámetro costoC");
          }
          break;
        case 6:
          if (!ParamsValidation.validaSoloDecimal(json.getString("sueldoMensual"))) {
            jsonResponse.put("status", "false");
            jsonResponse.put("message", "Ocurrió un error con el parámetro sueldoMensual");
          }
          break;
        case 19:
          if (!ParamsValidation.validaSoloDecimal(json.getString("costoA"))) {
            jsonResponse.put("status", "false");
            jsonResponse.put("message", "Ocurrió un error con el parámetro costoA");
          }
          if (!ParamsValidation.validaSoloDecimal(json.getString("costoB"))) {
            jsonResponse.put("status", "false");
            jsonResponse.put("message", "Ocurrió un error con el parámetro costoB");
          }
          if (!ParamsValidation.validaSoloDecimal(json.getString("costoC"))) {
            jsonResponse.put("status", "false");
            jsonResponse.put("message", "Ocurrió un error con el parámetro costoC");
          }
          if (!ParamsValidation.validaSoloDecimal(json.getString("sueldoMensual"))) {
            jsonResponse.put("status", "false");
            jsonResponse.put("message", "Ocurrió un error con el parámetro sueldoMensual");
          }
          break;
        default:
          break;
      }

    }

    return jsonResponse;
  }

}
