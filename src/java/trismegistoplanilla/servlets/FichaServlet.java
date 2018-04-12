package trismegistoplanilla.servlets;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;
import pe.siso.webservicesseguridad.webservices.UsuarioBean;
import sun.misc.BASE64Decoder;
import trismegistoplanilla.beans.Correo;
import trismegistoplanilla.beans.EstadoFichaBean;
import trismegistoplanilla.beans.FichaBean;
import trismegistoplanilla.beans.ObservacionFichaBean;
import trismegistoplanilla.beans.PersonaBean;
import trismegistoplanilla.beans.TokenFichaBean;
import trismegistoplanilla.services.CargaFamiliarService;
import trismegistoplanilla.services.ExpedienteService;
import trismegistoplanilla.services.ExperienciaLaboralService;
import trismegistoplanilla.services.FichaServices;
import trismegistoplanilla.services.FormacionAcademicaService;
import trismegistoplanilla.services.TokenFichaService;
import trismegistoplanilla.sqlserverdao.CorreoSqlserverDAO;
import trismegistoplanilla.utilities.EmailDesign;
import trismegistoplanilla.utilities.ParamsValidation;

public class FichaServlet extends HttpServlet {

  private static final long serialVersionUID = 767323838778604646L;

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String accion = request.getParameter("accion");
    switch (accion) {
      case "listarFichaDT":
        listarFichaDT(request, response);
        break;
      case "registrarFicha":
        registrarFicha(request, response);
        break;
      case "listarDatosFicha":
        listarDatosFicha(request, response);
        break;
      case "consultarFichasPorCriterio":
        consultarFichasPorCriterio(request, response);
        break;
      case "listarDetalleEstadoFicha":
        listarDetalleEstadoFicha(request, response);
        break;
      case "listarDatosAdministrativosFicha":
        listarDatosAdministrativosFicha(request, response);
        break;
      case "rechazarFicha":
        rechazarFicha(request, response);
        break;
      case "aceptarFicha":
        aceptarFicha(request, response);
        break;
      case "actualizarFicha":
        actualizarFicha(request, response);
        break;
      case "anularFicha":
        anularFicha(request, response);
        break;
      case "verObservacion":
        verObservacion(request, response);
        break;
      case "listarFichasPresidenciaDT":
        listarFichasPresidenciaDT(request, response);
        break;
      case "listarDetalleFichaPresidencia":
        listarDetalleFichaPresidencia(request, response);
        break;
      case "verificarExistenciaFichaAnulada":
        verificarExistenciaFichaAnulada(request, response);
        break;
      case "obtenerCodigoPersonaPorTipoDocNroDoc":
        obtenerCodigoPersonaPorTipoDocNroDoc(request, response);
        break;
      case "habilitarFicha":
        habilitarFicha(request, response);
        break;
      case "validarFichaActiva":
        validarFichaActiva(request, response);
        break;
      case "listarFichasPresidencia":
        listarFichasPresidencia(request, response);
        break;
      case "imprimirFicha":
        imprimirFicha(request, response);
        break;
      case "imprimirActividadFicha":
        imprimirActividadFicha(request, response);
        break;
      default:
        break;
    }
  }

  private void listarFichaDT(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();

    FichaServices service = new FichaServices();

    String draw = request.getParameter("draw");
    String start = request.getParameter("start");
    String length = request.getParameter("length");
    String search = request.getParameter("search");
    int criterio = Integer.parseInt(request.getParameter("criterio"));
    int tipoDocumento = Integer.parseInt(request.getParameter("tipoDocumento"));
    System.out.println("criterio => " + criterio);
    // criterio 1 = tipo documento
    // criterio 2 = apellido
    // criterio 3 = fecha registro
    if (request.getParameter("criterio") != null || request.getParameter("search") != null) {
      if (ParamsValidation.validaSoloNumeros(criterio)) {
        HttpSession session = request.getSession();
        UsuarioBean u = (UsuarioBean) session.getAttribute("usuario");
        switch (criterio) {
          case 1:
            // criterio documento
            if (ParamsValidation.validaSoloNumeros(tipoDocumento)) { // tipo documento
              if (ParamsValidation.validaSoloAlfanumerico(search)) { // numero documento
                JSONObject json = service.listarFichaDT(draw, length, start, search, tipoDocumento, criterio, u);
                out.print(json);
              } else {
                out.print("Ha ocurrido un error. Al parecer esta infringiendo las leyes de los values en los inputs.");
              }
            } else {
              out.print("Ha ocurrido un error. Al parecer esta infringiendo las leyes de los values en los inputs.");
            }
            break;
          case 2:
            // criterio apellido
            if (ParamsValidation.validaSoloLetras(search)) {
              JSONObject json = service.listarFichaDT(draw, length, start, search, tipoDocumento, criterio, u);
              out.print(json);
            } else {
              out.print("Ha ocurrido un error. Al parecer esta infringiendo las leyes de los values en los inputs.");
            }
            break;
          case 3:
            // criterio fecha
            if (ParamsValidation.validaSoloFecha(search)) {
              JSONObject json = service.listarFichaDT(draw, length, start, search, tipoDocumento, criterio, u);
              out.print(json);
            }
            break;
          case 0:
            // sin filtros
            JSONObject json = service.listarFichaDT(draw, length, start, search, tipoDocumento, criterio, u);
            out.print(json);
            break;
          default:
            out.print("Ha ocurrido un error. Al parecer esta infringiendo las leyes de los values en los inputs.");
            break;
        }
      } else {
        out.print("Ha ocurrido un error. Al parecer esta infringiendo las leyes de los values en los inputs.");
      }
    } else {
      out.print("Ha ocurrido un error. Al parecer esta infringiendo las leyes de los values en los inputs.");
    }
  }

  private void registrarFicha(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();

    // validar la session
    HttpSession session = request.getSession();
//    Enumeration keys = session.getAttributeNames();
//    while (keys.hasMoreElements()) {
//      String key = (String) keys.nextElement();
//      out.println(key + ": " + session.getValue(key));
//    }

    // obtener variables de session
    PersonaBean p = (PersonaBean) session.getAttribute("persona");
    TokenFichaBean tf = (TokenFichaBean) session.getAttribute("tokenFicha");

    String mensaje = "";

    // validar que las variables obtenidas no sean nulas
    if (p == null || tf == null) {
      mensaje = "Los valores son nulos";
      session.setAttribute("errorTokenFicha", mensaje);
      response.sendRedirect("vistas/response/ErrorToken.jsp");
      session.removeAttribute("tokenFicha");
      session.removeAttribute("persona");
    } else {
      // validar que el token aún esté activo
      TokenFichaService serviceTokenFicha = new TokenFichaService();
      JSONObject JOValidarTokenFicha = serviceTokenFicha.validarToken(tf);

      JSONObject JObjectResponse = new JSONObject();

      if (JOValidarTokenFicha.getBoolean("status")) { // token activo
        // --------------> registrar ficha <-------------- //
        // validar el parámetro JSON enviado desde el formulario de ficha
        if (request.getParameter("json") == null) {
          out.println("El parámetro JSON es NULL");
        } else {
          // obtener parámetro JSON enviado desde el formulario de ficha
          String paramJSON = request.getParameter("json");
          JSONObject json = new JSONObject(paramJSON);

          JSONObject JOvalidarDatosPersonales = validarDatosPersonales(json, p, request, response);

          if (JOvalidarDatosPersonales.getBoolean("status")) {
            JSONObject JOvalidarDatosFamiliares = validarDatosFamiliares(json, p, request, response);
            if (JOvalidarDatosFamiliares.getBoolean("status")) {
              JSONObject JOvalidarFormacionAcademica = validarFormacionAcademica(json, p, request, response);
              if (JOvalidarFormacionAcademica.getBoolean("status")) {
                JSONObject JOvalidarExperienciaLaboral = validarExperienciaLaboral(json, p, request, response);
                if (JOvalidarExperienciaLaboral.getBoolean("status")) {
                  JSONObject JOvalidarRegimenPensionario = validarRegimenPensionario(json, p, request, response);
                  if (JOvalidarRegimenPensionario.getBoolean("status")) {
                    // HASTA AQUÍ ESTA TODO CORRECTO CON LA VALIDACIÓN , SOLO QUEDA REGISTRAR LA FICHA
                    FichaServices serviceFicha = new FichaServices();
                    if (json.getString("extension").equals("")) {
                      json.put("extension", ".png");
                    }
                    // convert foto
                    if (json.getString("foto").contains(",")) {
                      String foto = json.getString("foto").substring(json.getString("foto").indexOf(",") + 1, json.getString("foto").length());
                      BASE64Decoder decoder = new BASE64Decoder();
                      byte[] decodedBytes = decoder.decodeBuffer(foto);
                      String uploadFile = "C:\\AppServ\\www\\img\\" + p.getNumeroDocumento() + json.getString("extension");
                      System.out.println("photo saved in path : " + uploadFile);
                      BufferedImage image = ImageIO.read(new ByteArrayInputStream(decodedBytes));
                      if (image == null) {
                        System.out.println("ERROR SAVE IMAGE : Buffered Image is null");
                      }
                      File f = new File(uploadFile);
                      ImageIO.write(image, "png", f);
                      json.put("foto", p.getNumeroDocumento() + json.getString("extension"));
                    }

                    JSONObject jsonObjRegistrarFicha = serviceFicha.registrarFicha(json, p, tf);
                    if (jsonObjRegistrarFicha.getBoolean("status")) {
                      // llenar datos segun tipo de documento
                      if (p.getCodigoTipoDocumento() != 1) {
                        p.setApellidoPaterno(json.getString("apellidoPaterno").toUpperCase());
                        p.setApellidoMaterno(json.getString("apellidoMaterno").toUpperCase());
                        p.setNombre(json.getString("nombre").toUpperCase());
                      }
                      // enviar correo se satisfaccion
                      Correo c = new Correo();
                      c.setDestino(p.getCorreo());
                      c.setAsunto("TRISMEGISTO - PLANILLA");
                      c.setMensaje(EmailDesign.correoSuccess(p.getApellidoPaterno(), p.getApellidoMaterno(), p.getNombre(), p.getNombreTipoDocumento(), p.getNumeroDocumento()));
                      CorreoSqlserverDAO enviarCorreo = new CorreoSqlserverDAO();
                      if (enviarCorreo.enviarCorreo(c)) {
                        session.removeAttribute("tokenFicha");
                        session.setAttribute("fichaCompletada", "ok");
                        JObjectResponse.put("page", "response/success.jsp");
                        out.print(JObjectResponse);
                      } else {
                        mensaje = "No se pudo enviar el correo de satisfaccion";
                        session.setAttribute("errorTokenFicha", mensaje);
                        session.removeAttribute("tokenFicha");
                        session.removeAttribute("persona");
                        JObjectResponse.put("page", "response/ErrorToken.jsp");
                        out.print(JObjectResponse);
                      }
                    } else {
                      mensaje = jsonObjRegistrarFicha.getString("message");
                      session.setAttribute("errorTokenFicha", mensaje);
                      session.removeAttribute("tokenFicha");
                      session.removeAttribute("persona");
                      JObjectResponse.put("page", "response/ErrorToken.jsp");
                      out.print(JObjectResponse);
                    }
                  } else {
                    out.print("RÉGIMEN PENSIONARIO - " + JOvalidarRegimenPensionario.getString("message"));
                  }
                } else {
                  out.print("EXPERIENCIA LABORAL - " + JOvalidarExperienciaLaboral.getString("message"));
                }
              } else {
                out.print("FORMACIÓN ACADÉMICA - " + JOvalidarFormacionAcademica.getString("message"));
              }
            } else {
              out.print("DATOS FAMILIARES - " + JOvalidarDatosFamiliares.getString("message"));
            }
          } else {
            out.print("DATOS PERSONALES - " + JOvalidarDatosPersonales.getString("message"));
          }

        }
      } else { // token inactivo
        session.setAttribute("errorTokenFicha", JOValidarTokenFicha.getString("message"));
        session.removeAttribute("tokenFicha");
        session.removeAttribute("persona");
        JOValidarTokenFicha.put("page", "response/ErrorToken.jsp");
        out.print(JOValidarTokenFicha);
      }
    }
  }

  private JSONObject validarDatosPersonales(JSONObject json, PersonaBean p, HttpServletRequest request, HttpServletResponse response) throws IOException {
    JSONObject JOResponse = new JSONObject();
    JOResponse.put("status", true);

    boolean statusTipoDocumento = false; // indica que los campos ruc - ape pat - ape mat - nombres hayan sido validados correctamente
    boolean statusUbigeoNacimiento = false; // indica que el ubigeo de nacimiento esta correcto o no, esto depende de la nacionalidad
    boolean statusDireccionIdentidad = false;

    switch (p.getCodigoTipoDocumento()) {
      case 1: // dni
        // ruc
        if (json.getString("ruc") != null) {
          System.out.println(json.getString("ruc"));
          if (!json.getString("ruc").trim().equals("")) {
            if (!ParamsValidation.validaLongitudNumeroDocumento(11, json.getString("ruc").trim())) {
              JOResponse.put("status", false);
              JOResponse.put("message", "El parámetro [RUC] debe tener 11 dígitos");
            } else if (!ParamsValidation.validaNumeroDocumento("N", json.getString("ruc"))) {
              JOResponse.put("status", false);
              JOResponse.put("message", "El parámetro [RUC] solo acepta números");
            } else {
              statusTipoDocumento = true;
            }
          } else {
            statusTipoDocumento = true;
          }
        } else {
          JOResponse.put("status", false);
          JOResponse.put("message", "El parámetro [RUC] es nulo");
        }
        break;
      case 3: // ruc
        // apellido paterno
        if (json.getString("apellidoPaterno") != null) {
          if (!json.getString("apellidoPaterno").trim().equals("")) {
            if (!ParamsValidation.validaSoloLetras(json.getString("apellidoPaterno").trim())) {
              JOResponse.put("status", false);
              JOResponse.put("message", "El parámetro [APELLIDO PATERNO] solo acepta letras");
            } else {
              // apellido materno
              if (json.getString("apellidoMaterno") != null) {
                if (!json.getString("apellidoMaterno").trim().equals("")) {
                  if (!ParamsValidation.validaSoloLetras(json.getString("apellidoMaterno").trim())) {
                    JOResponse.put("status", false);
                    JOResponse.put("message", "El parámetro [APELLIDO MATERNO] solo acepta letras");
                  } else {
                    // nombres
                    if (json.getString("nombre") != null) {
                      if (!json.getString("nombre").trim().equals("")) {
                        if (!ParamsValidation.validaSoloLetras(json.getString("nombre").trim())) {
                          JOResponse.put("status", false);
                          JOResponse.put("message", "El parámetro [NOMBRE] solo acepta letras");
                        } else {
                          statusTipoDocumento = true;
                        }
                      } else {
                        JOResponse.put("status", false);
                        JOResponse.put("message", "El parámetro [NOMBRE] está vacío");
                      }
                    } else {
                      JOResponse.put("status", false);
                      JOResponse.put("message", "El parámetro [NOMBRE] es nulo");
                    }
                  }
                } else {
                  JOResponse.put("status", false);
                  JOResponse.put("message", "El parámetro [APELLIDO MATERNO] está vacío");
                }
              } else {
                JOResponse.put("status", false);
                JOResponse.put("message", "El parámetro [APELLIDO MATERNO] es nulo");
              }
            }
          } else {
            JOResponse.put("status", false);
            JOResponse.put("message", "El parámetro [APELLIDO PATERNO] está vacío");
          }
        } else {
          JOResponse.put("status", false);
          JOResponse.put("message", "El parámetro [APELLIDO PATERNO] es nulo");
        }
        break;
      default: // otro doc
        // ruc
        if (json.getString("ruc") != null) {
          if (!json.getString("ruc").trim().equals("")) {
            if (!ParamsValidation.validaLongitudNumeroDocumento(11, json.getString("ruc").trim())) {
              JOResponse.put("status", false);
              JOResponse.put("message", "El parámetro [RUC] debe tener 11 dígitos");
            } else if (!ParamsValidation.validaNumeroDocumento("N", json.getString("ruc").trim())) {
              JOResponse.put("status", false);
              JOResponse.put("message", "El parámetro [RUC] solo acepta números");
            }
          }
        } else {
          JOResponse.put("status", false);
          JOResponse.put("message", "El parámetro [RUC] es nulo");
        }

        // apellido paterno
        if (json.getString("apellidoPaterno") != null) {
          if (!json.getString("apellidoPaterno").trim().equals("")) {
            if (!ParamsValidation.validaSoloLetras(json.getString("apellidoPaterno").trim())) {
              JOResponse.put("status", false);
              JOResponse.put("message", "El parámetro [APELLIDO PATERNO] solo acepta letras");
            } else {
              // apellido materno
              if (json.getString("apellidoMaterno") != null) {
                if (!json.getString("apellidoMaterno").trim().equals("")) {
                  if (!ParamsValidation.validaSoloLetras(json.getString("apellidoMaterno").trim())) {
                    JOResponse.put("status", false);
                    JOResponse.put("message", "El parámetro [APELLIDO MATERNO] solo acepta letras");
                  } else {
                    // nombres
                    if (json.getString("nombre") != null) {
                      if (!json.getString("nombre").trim().equals("")) {
                        if (!ParamsValidation.validaSoloLetras(json.getString("nombre").trim())) {
                          JOResponse.put("status", false);
                          JOResponse.put("message", "El parámetro [NOMBRE] solo acepta letras");
                        } else {
                          statusTipoDocumento = true;
                        }
                      } else {
                        JOResponse.put("status", false);
                        JOResponse.put("message", "El parámetro [NOMBRE] está vacío");
                      }
                    } else {
                      JOResponse.put("status", false);
                      JOResponse.put("message", "El parámetro [NOMBRE] es nulo");
                    }
                  }
                } else {
                  JOResponse.put("status", false);
                  JOResponse.put("message", "El parámetro [APELLIDO MATERNO] está vacío");
                }
              } else {
                JOResponse.put("status", false);
                JOResponse.put("message", "El parámetro [APELLIDO MATERNO] es nulo");
              }
            }
          } else {
            JOResponse.put("status", false);
            JOResponse.put("message", "El parámetro [APELLIDO PATERNO] está vacío");
          }
        } else {
          JOResponse.put("status", false);
          JOResponse.put("message", "El parámetro [APELLIDO PATERNO] es nulo");
        }
        break;
    }

    if (statusTipoDocumento) {
      // sexo
      if (json.getString("sexo") != null) {
        if (!json.getString("sexo").trim().equals("")) {
          if (!json.getString("sexo").trim().equals("0")) {
            if (!ParamsValidation.validaSoloLetras(json.getString("sexo").trim())) {
              JOResponse.put("status", false);
              JOResponse.put("message", "El parámetro [SEXO] solo acepta letras");
            } else {
              if (!ParamsValidation.validaLongitudCadena(1, json.getString("sexo").trim())) {
                JOResponse.put("status", false);
                JOResponse.put("message", "El parámetro [SEXO] solo acepta una letra");
              } else {
                if (!(json.getString("sexo").trim().equals("F") || json.getString("sexo").trim().equals("M"))) {
                  JOResponse.put("status", false);
                  JOResponse.put("message", "El parámetro [SEXO] tiene un valor inválido " + json.getString("sexo"));
                } else {
                  // estado civil
                  if (json.getString("codigoEstadoCivil") != null) {
                    if (!json.getString("codigoEstadoCivil").trim().equals("")) {
                      if (!ParamsValidation.validaTipoEntrada("N", json.getString("codigoEstadoCivil").trim())) {
                        JOResponse.put("status", false);
                        JOResponse.put("message", "El parámetro [ESTADO CIVIL] solo acepta números");
                      } else if (json.getString("codigoEstadoCivil").trim().equals("0")) {
                        JOResponse.put("status", false);
                        JOResponse.put("message", "El parámetro [ESTADO CIVIL] tiene un valor inválido");
                      } else {
                        // fecha nacimiento
                        if (json.getString("fechaNacimiento") != null) {
                          if (!json.getString("fechaNacimiento").trim().equals("")) {
                            if (!ParamsValidation.validaSoloFecha(json.getString("fechaNacimiento").trim())) {
                              JOResponse.put("status", false);
                              JOResponse.put("message", "El parámetro [FECHA DE NACIMIENTO] tiene un valor inválido");
                            } else {
                              // nacionalidad
                              if (json.getString("codigoNacionalidad") != null) {
                                if (!json.getString("codigoNacionalidad").trim().equals("")) {
                                  if (!ParamsValidation.validaTipoEntrada("N", json.getString("codigoNacionalidad").trim())) {
                                    JOResponse.put("status", false);
                                    JOResponse.put("message", "El parámetro [NACIONALIDAD] solo acepta números");
                                  } else if (json.getString("codigoNacionalidad").trim().equals("0")) {
                                    JOResponse.put("status", false);
                                    JOResponse.put("message", "El parámetro [NACIONALIDAD] tiene un valor inválido");
                                  } else {
                                    System.out.println(json);
                                    if (json.getString("codigoNacionalidad").trim().equals("144")) { // codigo nacionalidad peruana
                                      // ubigeo nacimiento
                                      if (!json.isNull("codigoUbigeoNacimiento")) {
                                        if (json.getInt("codigoUbigeoNacimiento") == 0) {
                                          JOResponse.put("status", false);
                                          JOResponse.put("message", "El parámetro [UBIGEO NACIMIENTO] no se le pudo asignar un valor");
                                        } else {
                                          statusUbigeoNacimiento = true;
                                        }
                                      } else {
                                        JOResponse.put("status", false);
                                        JOResponse.put("message", "El parámetro [UBIGEO NACIMIENTO] es nulo");
                                      }
                                    } else { // otro codigo de nacionalidad diferente al codigo de nacionaldiad peruana
                                      // ubigeo nacimiento
                                      if (!json.isNull("codigoUbigeoNacimiento")) {
                                        JOResponse.put("status", false);
                                        JOResponse.put("message", "El parámetro [UBIGEO NACIMIENTO] tiene un valor inválido");
                                      } else {
                                        statusUbigeoNacimiento = true;
                                      }
                                    }
                                  }
                                } else {
                                  JOResponse.put("status", false);
                                  JOResponse.put("message", "El parámetro [NACIONALIDAD] está vacío");
                                }
                              } else {
                                JOResponse.put("status", false);
                                JOResponse.put("message", "El parámetro [NACIONALIDAD] es nulo");
                              }
                            }
                          } else {
                            JOResponse.put("status", false);
                            JOResponse.put("message", "El parámetro [FECHA DE NACIMIENTO] está vacío");
                          }
                        } else {
                          JOResponse.put("status", false);
                          JOResponse.put("message", "El parámetro [FECHA DE NACIMIENTO] es nulo");
                        }
                      }
                    } else {
                      JOResponse.put("status", false);
                      JOResponse.put("message", "El parámetro [ESTADO CIVIL] está vacío");
                    }
                  } else {
                    JOResponse.put("status", false);
                    JOResponse.put("message", "El parámetro [ESTADO CIVIL] está vacío");
                  }
                }
              }
            }
          } else {
            JOResponse.put("status", false);
            JOResponse.put("message", "El parámetro [SEXO] tiene un valor inválido");
          }
        } else {
          JOResponse.put("status", false);
          JOResponse.put("message", "El parámetro [SEXO] está vacío");
        }
      } else {
        JOResponse.put("status", false);
        JOResponse.put("message", "El parámetro [SEXO] es nulo");
      }

      if (statusUbigeoNacimiento) {
        if (p.getCodigoTipoDocumento() == 1 && p.getCodigoTipoDocumento() == 3 && p.getCodigoTipoDocumento() == 5) {
          // direccion de documento
          if (json.getString("direccionDocumento") != null) {
            if (json.getString("direccionDocumento").trim().equals("")) {
              JOResponse.put("status", false);
              JOResponse.put("message", "El parámetro [DIRECCION DE DOCUMENTO] está vacío");
            } else {
              statusDireccionIdentidad = true;
            }
          } else {
            JOResponse.put("status", false);
            JOResponse.put("message", "El parámetro [DIRECCION DE DOCUMENTO] es nulo");
          }
        } else {
          // direccion de documento
          if (json.getString("direccionDocumento") != null) {
            statusDireccionIdentidad = true;
          } else {
            JOResponse.put("status", false);
            JOResponse.put("message", "El parámetro [DIRECCION DE DOCUMENTO] es nulo");
          }
        }

        if (statusDireccionIdentidad) {
          // telefono fijo
          if (json.getString("telefonoFijo") != null) {
            if (!json.getString("telefonoFijo").trim().equals("")) {
              if (!ParamsValidation.validaLongitudCadena(7, json.getString("telefonoFijo").trim())) {
                JOResponse.put("status", false);
                JOResponse.put("message", "El parámetro [TELEFONO FIJO] debe tener al 7 dígitos");
              } else if (!ParamsValidation.validaTipoEntrada("N", json.getString("telefonoFijo").trim())) {
                JOResponse.put("status", false);
                JOResponse.put("message", "El parámetro [TELEFONO FIJO] solo acepta números");
              }
            }
          } else {
            JOResponse.put("status", false);
            JOResponse.put("message", "El parámetro [TELEFONO FIJO] es nulo");
          }

          // telefono movil
          if (json.getString("telefonoMovil") != null) {
            if (!json.getString("telefonoMovil").trim().equals("")) {
              if (!ParamsValidation.validaLongitudCadena(9, json.getString("telefonoMovil").trim())) {
                JOResponse.put("status", false);
                JOResponse.put("message", "El parámetro [TELEFONO MÓVIL] debe tener al 9 dígitos");
              } else if (!ParamsValidation.validaTipoEntrada("N", json.getString("telefonoMovil").trim())) {
                JOResponse.put("status", false);
                JOResponse.put("message", "El parámetro [TELEFONO MÓVIL] solo acepta números");
              }
            }
          } else {
            JOResponse.put("status", false);
            JOResponse.put("message", "El parámetro [TELEFONO MÓVIL] es nulo");
          }

          // correo electronico
          if (json.getString("correo") != null) {
            if (json.getString("correo").trim().equals("")) {
              JOResponse.put("status", false);
              JOResponse.put("message", "El parámetro [CORREO ELECTRÓNICO] está vacío");
            } else if (!ParamsValidation.validaCorreo(json.getString("correo"))) {
              JOResponse.put("status", false);
              JOResponse.put("message", "El parámetro [CORREO ELECTRÓNICO] tiene un valor inválido");
            } else {
              // ubigeo residencia
              if (!json.isNull("codigoUbigeoResidencia")) {
                if (json.getInt("codigoUbigeoResidencia") == 0) {
                  JOResponse.put("status", false);
                  JOResponse.put("message", "El parámetro [UBIGEO RESIDENCIA] no se le pudo asignar un valor");
                } else {
                  // direccion de residencia
                  if (json.getString("direccionResidencia") != null) {
                    if (json.getString("direccionResidencia").trim().equals("")) {
                      JOResponse.put("status", false);
                      JOResponse.put("message", "El parámetro [DIRECCIÓN DE RESIDENCIA] está vacío");
                    } else {
                      // latitud
                      if (json.getString("latitudResidencia") != null) {
                        if (json.getString("latitudResidencia").trim().equals("")) {
                          JOResponse.put("status", false);
                          JOResponse.put("message", "El parámetro [LATITUD] está vacío");
                        } else if (!ParamsValidation.validaLatitud(json.getString("latitudResidencia"))) {
                          JOResponse.put("status", false);
                          JOResponse.put("message", "El parámetro [LATITUD] tiene un valor inválido");
                        } else {
                          // longitud
                          if (json.getString("longitudResidencia") != null) {
                            if (json.getString("longitudResidencia").trim().equals("")) {
                              JOResponse.put("status", false);
                              JOResponse.put("message", "El parámetro [LONGITUD] está vacío");
                            } else if (!ParamsValidation.validaLatitud(json.getString("longitudResidencia"))) {
                              JOResponse.put("status", false);
                              JOResponse.put("message", "El parámetro [LONGITUD] tiene un valor inválido");
                            }
                          } else {
                            JOResponse.put("status", false);
                            JOResponse.put("message", "El parámetro [LONGITUD] es nulo");
                          }
                        }
                      } else {
                        JOResponse.put("status", false);
                        JOResponse.put("message", "El parámetro [LATITUD] es nulo");
                      }
                    }
                  } else {
                    JOResponse.put("status", false);
                    JOResponse.put("message", "El parámetro [DIRECCIÓN DE RESIDENCIA] es nulo");
                  }
                }
              } else {
                JOResponse.put("status", false);
                JOResponse.put("message", "El parámetro [UBIGEO RESIDENCIA] es nulo");
              }
            }
          } else {
            JOResponse.put("status", false);
            JOResponse.put("message", "El parámetro [CORREO ELECTRÓNICO] es nulo");
          }
        }
      }
    }

    return JOResponse;
  }

  private JSONObject validarDatosFamiliares(JSONObject json, PersonaBean p, HttpServletRequest request, HttpServletResponse response) throws IOException {
    JSONObject JOResponse = new JSONObject();
    JOResponse.put("status", true);

    if (json.getJSONArray("cargaFamiliar") != null) {
      int longitudCargaFamiliar = json.getJSONArray("cargaFamiliar").length();
      if (longitudCargaFamiliar != 0) {
        for (int i = 0; i < longitudCargaFamiliar; i++) {
          JSONObject cargaFamiliar = json.getJSONArray("cargaFamiliar").getJSONObject(i);
          // parentesco
          if (!ParamsValidation.validaSoloNumeros(cargaFamiliar.getInt("codigoParentesco"))) {
            JOResponse.put("status", false);
            JOResponse.put("message", "El parámetro [CARGA FAMILIAR - PARENTESCO] solo acepta numeros");
          } else if (cargaFamiliar.getInt("codigoParentesco") == 0) {
            JOResponse.put("status", false);
            JOResponse.put("message", "El parámetro [CARGA FAMILIAR - PARENTESCO] tiene un valor inválido");
          } else {
            // apellido paterno
            if (cargaFamiliar.getString("apellidoPaterno") != null) {
              if (!cargaFamiliar.getString("apellidoPaterno").trim().equals("")) {
                if (!ParamsValidation.validaSoloLetras(cargaFamiliar.getString("apellidoPaterno").trim())) {
                  JOResponse.put("status", false);
                  JOResponse.put("message", "El parámetro [CARGA FAMILIAR - APELLIDO PATERNO] solo acepta letras");
                } else {
                  // apellido materno
                  if (cargaFamiliar.getString("apellidoMaterno") != null) {
                    if (!cargaFamiliar.getString("apellidoMaterno").trim().equals("")) {
                      if (!ParamsValidation.validaSoloLetras(cargaFamiliar.getString("apellidoMaterno").trim())) {
                        JOResponse.put("status", false);
                        JOResponse.put("message", "El parámetro [CARGA FAMILIAR - APELLIDO MATERNO] solo acepta letras");
                      } else {
                        // nombres
                        if (cargaFamiliar.getString("nombre") != null) {
                          if (!cargaFamiliar.getString("nombre").trim().equals("")) {
                            if (!ParamsValidation.validaSoloLetras(cargaFamiliar.getString("nombre").trim())) {
                              JOResponse.put("status", false);
                              JOResponse.put("message", "El parámetro [CARGA FAMILIAR - NOMBRE] solo acepta letras");
                            } else {
                              // fecha de nacimiento
                              if (cargaFamiliar.getString("fechaNacimiento") != null) {
                                if (!cargaFamiliar.getString("fechaNacimiento").trim().equals("")) {
                                  if (!ParamsValidation.validaSoloFecha(cargaFamiliar.getString("fechaNacimiento").trim())) {
                                    JOResponse.put("status", false);
                                    JOResponse.put("message", "El parámetro [CARGA FAMILIAR - FECHA DE NACIMIENTO] tiene un valor inválido");
                                  } else {
                                    // tipo de documento
                                    if (!cargaFamiliar.isNull("codigoTipoDocumento")) {
                                      if (cargaFamiliar.getInt("codigoTipoDocumento") != 0) {
                                        // numero de documento
                                        if (cargaFamiliar.getString("numeroDocumento") != null) {
                                          if (!cargaFamiliar.getString("numeroDocumento").trim().equals("")) {
                                            if (!ParamsValidation.validaTipoEntrada(cargaFamiliar.getString("tipoEntrada").trim(), cargaFamiliar.getString("numeroDocumento").trim())) {
                                              JOResponse.put("status", false);
                                              JOResponse.put("message", "El parámetro [CARGA FAMILIAR - NÚMERO DE DOCUMENTO] tiene un valor inválido");
                                            } else if (!ParamsValidation.validaLongitudNumeroDocumento(Integer.parseInt(cargaFamiliar.getString("longitud").trim()), cargaFamiliar.getString("numeroDocumento").trim())) {
                                              JOResponse.put("status", false);
                                              JOResponse.put("message", "El parámetro [CARGA FAMILIAR - NÚMERO DE DOCUMENTO] tiene una longitud inválida");
                                            } else {
                                              // sexo
                                              if (cargaFamiliar.getString("sexo") != null) {
                                                if (!cargaFamiliar.getString("sexo").trim().equals("")) {
                                                  if (!cargaFamiliar.getString("sexo").trim().equals("0")) {
                                                    if (!ParamsValidation.validaSoloLetras(cargaFamiliar.getString("sexo").trim())) {
                                                      JOResponse.put("status", false);
                                                      JOResponse.put("message", "El parámetro [SEXO] solo acepta letras");
                                                    } else {
                                                      if (!(cargaFamiliar.getString("sexo").trim().equals("F") || cargaFamiliar.getString("sexo").trim().equals("M"))) {
                                                        JOResponse.put("status", false);
                                                        JOResponse.put("message", "El parámetro [SEXO] tiene un valor inválido " + cargaFamiliar.getString("sexo"));
                                                      } else {
                                                        // telefono
                                                        if (cargaFamiliar.getString("telefono") != null) {
                                                          if (!cargaFamiliar.getString("telefono").trim().equals("")) {
                                                            if (!ParamsValidation.validaTipoEntrada("N", cargaFamiliar.getString("telefono").trim())) {
                                                              JOResponse.put("status", false);
                                                              JOResponse.put("message", "El parámetro [CARGA FAMILIAR - TELÉFONO] solo acepta números");
                                                            } else if (!(cargaFamiliar.getString("telefono").length() == 7 || cargaFamiliar.getString("telefono").trim().length() == 9)) {
                                                              JOResponse.put("status", false);
                                                              JOResponse.put("message", "El parámetro [CARGA FAMILIAR - TELÉFONO] debe ingresar un número de telefono/celular válido");
                                                            }
                                                          }
                                                        } else {
                                                          JOResponse.put("status", false);
                                                          JOResponse.put("message", "El parámetro [CARGA FAMILIAR - TELÉFONO] es nulo");
                                                        }
                                                      }
                                                    }
                                                  } else {
                                                    JOResponse.put("status", false);
                                                    JOResponse.put("message", "El parámetro [SEXO] tiene un valor inválido");
                                                  }
                                                } else {
                                                  JOResponse.put("status", false);
                                                  JOResponse.put("message", "El parámetro [SEXO] está vacío");
                                                }
                                              } else {
                                                JOResponse.put("status", false);
                                                JOResponse.put("message", "El parámetro [SEXO] es nulo");
                                              }
                                            }
                                          } else {
                                            JOResponse.put("status", false);
                                            JOResponse.put("message", "El parámetro [CARGA FAMILIAR - NÚMERO DE DOCUMENTO] está vacío");
                                          }
                                        } else {
                                          JOResponse.put("status", false);
                                          JOResponse.put("message", "El parámetro [CARGA FAMILIAR - NÚMERO DE DOCUMENTO] es nulo");
                                        }
                                      } else {
                                        JOResponse.put("status", false);
                                        JOResponse.put("message", "El parámetro [CARGA FAMILIAR - TIPO DE DOCUMENTO] tiene un valor inválido");
                                      }
                                    } else {
                                      JOResponse.put("status", false);
                                      JOResponse.put("message", "El parámetro [CARGA FAMILIAR - TIPO DE DOCUMENTO] es nulo");
                                    }
                                  }
                                } else {
                                  JOResponse.put("status", false);
                                  JOResponse.put("message", "El parámetro [CARGA FAMILIAR - FECHA DE NACIMIENTO] está vacío");
                                }
                              } else {
                                JOResponse.put("status", false);
                                JOResponse.put("message", "El parámetro [CARGA FAMILIAR - FECHA DE NACIMIENTO] es nulo");
                              }
                            }
                          } else {
                            JOResponse.put("status", false);
                            JOResponse.put("message", "El parámetro [CARGA FAMILIAR - NOMBRE] está vacío");
                          }
                        } else {
                          JOResponse.put("status", false);
                          JOResponse.put("message", "El parámetro [CARGA FAMILIAR - NOMBRE] es nulo");
                        }
                      }
                    } else {
                      JOResponse.put("status", false);
                      JOResponse.put("message", "El parámetro [CARGA FAMILIAR - APELLIDO MATERNO] está vacío");
                    }
                  } else {
                    JOResponse.put("status", false);
                    JOResponse.put("message", "El parámetro [CARGA FAMILIAR - APELLIDO MATERNO] es nulo");
                  }
                }
              } else {
                JOResponse.put("status", false);
                JOResponse.put("message", "El parámetro [CARGA FAMILIAR - APELLIDO PATERNO] está vacío");
              }
            } else {
              JOResponse.put("status", false);
              JOResponse.put("message", "El parámetro [CARGA FAMILIAR - APELLIDO PATERNO] es nulo");
            }
          }
        }
      } else {
        JOResponse.put("status", false);
        JOResponse.put("message", "El parámetro [CARGA FAMILIAR] debe tener al menos un registro");
      }
    } else {
      JOResponse.put("status", false);
      JOResponse.put("message", "El parámetro [CARGA FAMILIAR] es nulo");
    }

    return JOResponse;
  }

  private JSONObject validarFormacionAcademica(JSONObject json, PersonaBean p, HttpServletRequest request, HttpServletResponse response) throws IOException {
    JSONObject JOResponse = new JSONObject();
    JOResponse.put("status", true);

    if (json.getJSONArray("formacionAcademica") != null) {
      int longitudFormacionAcademica = json.getJSONArray("formacionAcademica").length();
      if (longitudFormacionAcademica != 0) {
        for (int j = 0; j < longitudFormacionAcademica; j++) {
          JSONObject formacionAcademica = json.getJSONArray("formacionAcademica").getJSONObject(j);
          // grado estudio y estado estudio
          if (!formacionAcademica.isNull("codigoNivelEstado")) {
            if (!ParamsValidation.validaSoloNumeros(formacionAcademica.getInt("codigoNivelEstado"))) {
              JOResponse.put("status", false);
              JOResponse.put("message", "El parámetro [FORMACION ACADÉMICA - GRADO ESTUDIO Y ESTADO ESTUDIO] solo acepta numeros");
            } else if (formacionAcademica.getInt("codigoNivelEstado") != 0) {
              // centro de estudios
              if (formacionAcademica.getString("centroEstudios") != null) {
                if (formacionAcademica.getString("centroEstudios").trim().equals("")) {
                  JOResponse.put("status", false);
                  JOResponse.put("message", "El parámetro [FORMACION ACADÉMICA - CENTRO DE ESTUDIOS] está vacío");
                } else {
                  // carrera profesional
                  if (!formacionAcademica.isNull("codigoCarreraProfesional")) {
                    if (!ParamsValidation.validaSoloNumeros(formacionAcademica.getInt("codigoCarreraProfesional"))) {
                      JOResponse.put("status", false);
                      JOResponse.put("message", "El parámetro [FORMACION ACADÉMICA - CARRERA PROFESIONAL] solo acepta número");
                    } else {
                      // fecha de inicio
                      if (formacionAcademica.getString("fechaInicio") != null) {
                        if (!formacionAcademica.getString("fechaInicio").trim().equals("")) {
                          if (!ParamsValidation.validaSoloFecha(formacionAcademica.getString("fechaInicio").trim())) {
                            JOResponse.put("status", false);
                            JOResponse.put("message", "El parámetro [FORMACION ACADÉMICA - FECHA DE INICIO] tiene un valor inválido");
                          } else {
                            // fecha de termino(fin)
                            if (formacionAcademica.getString("fechaFin") != null) {
                              if (!formacionAcademica.getString("fechaFin").trim().equals("")) {
                                if (!ParamsValidation.validaSoloFecha(formacionAcademica.getString("fechaFin").trim())) {
                                  JOResponse.put("status", false);
                                  JOResponse.put("message", "El parámetro [FORMACION ACADÉMICA - FECHA DE FIN] tiene un valor inválido");
                                }
                              } else {
                                JOResponse.put("status", false);
                                JOResponse.put("message", "El parámetro [FORMACION ACADÉMICA - FECHA DE FIN] está vacío");
                              }
                            } else {
                              JOResponse.put("status", false);
                              JOResponse.put("message", "El parámetro [FORMACION ACADÉMICA - FECHA DE FIN] es nulo");
                            }
                          }
                        } else {
                          JOResponse.put("status", false);
                          JOResponse.put("message", "El parámetro [FORMACION ACADÉMICA - FECHA DE INICIO] está vacío");
                        }
                      } else {
                        JOResponse.put("status", false);
                        JOResponse.put("message", "El parámetro [FORMACION ACADÉMICA - FECHA DE INICIO] es nulo");
                      }
                    }
                  } else {
                    JOResponse.put("status", false);
                    JOResponse.put("message", "El parámetro [FORMACION ACADÉMICA - CARRERA PROFESIONAL] es nulo");
                  }
                }
              } else {
                JOResponse.put("status", false);
                JOResponse.put("message", "El parámetro [FORMACION ACADÉMICA - CENTRO DE ESTUDIOS] es nulo");
              }
            } else {
              JOResponse.put("status", false);
              JOResponse.put("message", "El parámetro [FORMACION ACADÉMICA - GRADO ESTUDIO Y ESTADO ESTUDIO] tienen valores inválidos");
            }
          } else {
            JOResponse.put("status", false);
            JOResponse.put("message", "El parámetro [FORMACION ACADÉMICA - GRADO ESTUDIO Y ESTADO ESTUDIO] es nulo");
          }
        }
      } else {
        JOResponse.put("status", false);
        JOResponse.put("message", "El parámetro [FORMACIÓN ACADÉMICA] debe tener al menos un registro");
      }
    } else {
      JOResponse.put("status", false);
      JOResponse.put("message", "El parámetro [FORMACIÓN ACADÉMICA] es nulo");
    }

    return JOResponse;
  }

  private JSONObject validarExperienciaLaboral(JSONObject json, PersonaBean p, HttpServletRequest request, HttpServletResponse response) throws IOException {
    JSONObject JOResponse = new JSONObject();
    JOResponse.put("status", true);

    if (json.getJSONArray("experienciaLaboral") != null) {
      int longitudExperienciaLaboral = json.getJSONArray("experienciaLaboral").length();
      if (longitudExperienciaLaboral != 0) {
        for (int k = 0; k < longitudExperienciaLaboral; k++) {
          JSONObject experienciaLaboral = json.getJSONArray("experienciaLaboral").getJSONObject(k);
          // empresa
          if (experienciaLaboral.getString("empresa") != null) {
            if (!experienciaLaboral.getString("empresa").equals("")) {
              // cargo
              if (experienciaLaboral.getString("cargo") != null) {
                if (!experienciaLaboral.getString("cargo").equals("")) {
                  // fecha inicio
                  if (experienciaLaboral.getString("fechaInicio") != null) {
                    if (!experienciaLaboral.getString("fechaInicio").trim().equals("")) {
                      if (!ParamsValidation.validaSoloFecha(experienciaLaboral.getString("fechaInicio").trim())) {
                        JOResponse.put("status", false);
                        JOResponse.put("message", "El parámetro [EXPERIENCIA LABORAL - FECHA DE INICIO] tienen un valor inválido");
                      } else {
                        if (experienciaLaboral.getString("fechaFin") != null) {
                          if (!experienciaLaboral.getString("fechaFin").trim().equals("")) {
                            if (!ParamsValidation.validaSoloFecha(experienciaLaboral.getString("fechaFin").trim())) {
                              JOResponse.put("status", false);
                              JOResponse.put("message", "El parámetro [EXPERIENCIA LABORAL - FECHA FIN] tienen un valor inválido");
                            } else {
                              // telefono
                              if (experienciaLaboral.getString("telefono") != null) {
                                if (!experienciaLaboral.getString("telefono").trim().equals("")) {
                                  if (!(experienciaLaboral.getString("telefono").length() == 7 || experienciaLaboral.getString("telefono").trim().length() == 9)) {
                                    JOResponse.put("status", false);
                                    JOResponse.put("message", "El parámetro [EXPERIENCIA LABORAL - TELÉFONO] debe ingresar un número de telefono/celular válido");
                                  } else {
                                    JOResponse.put("status", "true");
                                  }
                                }
                              } else {
                                JOResponse.put("status", false);
                                JOResponse.put("message", "El parámetro [EXPERIENCIA LABORAL - TELEFONO] es nulo");
                              }
                            }
                          } else {
                            JOResponse.put("status", false);
                            JOResponse.put("message", "El parámetro [EXPERIENCIA LABORAL - FECHA FIN] está vacío");
                          }
                        } else {
                          JOResponse.put("status", false);
                          JOResponse.put("message", "El parámetro [EXPERIENCIA LABORAL - FECHA FIN] es nulo");
                        }
                      }
                    } else {
                      JOResponse.put("status", false);
                      JOResponse.put("message", "El parámetro [EXPERIENCIA LABORAL - FECHA DE INICIO] está vacío");
                    }
                  } else {
                    JOResponse.put("status", false);
                    JOResponse.put("message", "El parámetro [EXPERIENCIA LABORAL - FECHA DE INICIO] es nulo");
                  }
                } else {
                  JOResponse.put("status", false);
                  JOResponse.put("message", "El parámetro [EXPERIENCIA LABORAL - CARGO] está vacío");
                }
              } else {
                JOResponse.put("status", false);
                JOResponse.put("message", "El parámetro [EXPERIENCIA LABORAL - CARGO] es nulo");
              }
            } else {
              JOResponse.put("status", false);
              JOResponse.put("message", "El parámetro [EXPERIENCIA LABORAL - EMPRESA] está vacío");
            }
          } else {
            JOResponse.put("status", false);
            JOResponse.put("message", "El parámetro [EXPERIENCIA LABORAL - EMPRESA] es nulo");
          }
        }
      }
    } else {
      JOResponse.put("status", false);
      JOResponse.put("message", "El parámetro [EXPERIENCIA LABORAL] es nulo");
    }

    return JOResponse;
  }

  private JSONObject validarRegimenPensionario(JSONObject json, PersonaBean p, HttpServletRequest request, HttpServletResponse response) throws IOException {
    JSONObject JOResponse = new JSONObject();
    JOResponse.put("status", true);

    if (json.isNull("codigoFondoPension")) {
      JOResponse.put("status", false);
      JOResponse.put("message", "El parámetro [FONDO DE PENSIÓN] es nulo");
    } else {
      if (!ParamsValidation.validaSoloNumeros(json.getInt(("codigoFondoPension")))) {
        JOResponse.put("status", "false");
        JOResponse.put("message", "El parámetro [FONDO DE PENSIÓN] solo acepta números");
      } else if (json.getInt("codigoFondoPension") == 0) {
        JOResponse.put("status", "false");
        JOResponse.put("message", "El parámetro [FONDO DE PENSIÓN] tiene un valor inválido");
      }
    }

    return JOResponse;
  }

  private void listarDatosFicha(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    int codigoPersona = Integer.parseInt(request.getParameter("codigoPersona"));
    FichaServices serviceFicha = new FichaServices();
    CargaFamiliarService serviceCargaFamiliar = new CargaFamiliarService();
    ExperienciaLaboralService serviceExperienciaLaboral = new ExperienciaLaboralService();
    FormacionAcademicaService serviceFormacionAcademica = new FormacionAcademicaService();
    PersonaBean persona = new PersonaBean();
    persona.setCodigoPersona(codigoPersona);

    JSONObject jsonObjDatosPersonales = serviceFicha.listarDatosFicha(persona);
    JSONObject jsonObjDatosFamiliares = serviceCargaFamiliar.obtenerCargaFamiliarPorPersona(persona);
    JSONObject jsonObjExperienciaLaboral = serviceExperienciaLaboral.obtenerExperienciaLaboralPorPersona(persona);
    JSONObject jsonObjFormacionAcademica = serviceFormacionAcademica.obtenerFormacionAcademicaPorPersona(persona);

    JSONArray jsonArrayDatosFamiliares = jsonObjDatosFamiliares.getJSONObject("data").getJSONArray("cargafamiliar");
    JSONArray jsonArrayExperienciaLaboral = jsonObjExperienciaLaboral.getJSONObject("data").getJSONArray("experiencialaboral");
    JSONArray jsonArrayFormacionAcademica = jsonObjFormacionAcademica.getJSONObject("data").getJSONArray("formacionacademica");
    System.out.println(jsonObjDatosPersonales);
    JSONObject jsonReturn = jsonObjDatosPersonales.getJSONObject("data").getJSONObject("persona");
    jsonReturn.put("cargafamiliar", jsonArrayDatosFamiliares);
    jsonReturn.put("experiencialaboral", jsonArrayExperienciaLaboral);
    jsonReturn.put("formacionacademica", jsonArrayFormacionAcademica);

    out.print(jsonObjDatosPersonales);

  }

  private void consultarFichasPorCriterio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    JSONObject JOlistarFichas = null;
    if (request.getParameter("json") == null) {
      JOlistarFichas = new JSONObject();
      JOlistarFichas.put("message", "Los datos llegaron nulos");
      JOlistarFichas.put("status", false);
      out.print(JOlistarFichas);
    } else {
      HttpSession session = request.getSession();
      UsuarioBean u = (UsuarioBean) session.getAttribute("usuario");
      String json = request.getParameter("json");
      String draw = request.getParameter("draw");
      int start = Integer.parseInt(request.getParameter("start"));
      int length = Integer.parseInt(request.getParameter("length"));
      System.out.println(json);
      System.out.println("draw: " + draw);
      System.out.println("start: " + start);
      System.out.println("length: " + length);
      JSONObject criterioClient = new JSONObject(json);
      FichaServices services = new FichaServices();
      JOlistarFichas = services.consultarFichasPorCriterio(draw, start, length, "", criterioClient, u);
      out.print(JOlistarFichas);
    }
  }

  private void listarDetalleEstadoFicha(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    JSONObject JOlistarDetalleEstadoFicha = null;
    if (request.getParameter("codigoFicha") == null) {
      JOlistarDetalleEstadoFicha = new JSONObject();
      JOlistarDetalleEstadoFicha.put("message", "Los datos llegaron nulos");
      JOlistarDetalleEstadoFicha.put("status", false);
      out.print(JOlistarDetalleEstadoFicha);
    } else {
      if (ParamsValidation.validaTipoEntrada("N", request.getParameter("codigoFicha"))) {
        int codigoFicha = Integer.parseInt(request.getParameter("codigoFicha"));
        FichaBean f = new FichaBean();
        f.setCodigoFicha(codigoFicha);
        FichaServices services = new FichaServices();
        JOlistarDetalleEstadoFicha = services.listarDetalleEstadoFicha(f);
        out.print(JOlistarDetalleEstadoFicha);
      }
    }
  }

  private void listarDatosAdministrativosFicha(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    JSONObject JOlistarDatosAdministrativosFichas = null;
    if (request.getParameter("codigoFicha") == null || request.getParameter("codigoPersona") == null) {
      JOlistarDatosAdministrativosFichas = new JSONObject();
      JOlistarDatosAdministrativosFichas.put("message", "Los datos llegaron nulos");
      JOlistarDatosAdministrativosFichas.put("status", false);
      out.print(JOlistarDatosAdministrativosFichas);
    } else {
      int codigoPersona = Integer.parseInt(request.getParameter("codigoPersona"));
      int codigoFicha = Integer.parseInt(request.getParameter("codigoFicha"));
      FichaServices serviceFicha = new FichaServices();
      CargaFamiliarService serviceCargaFamiliar = new CargaFamiliarService();
      ExperienciaLaboralService serviceExperienciaLaboral = new ExperienciaLaboralService();
      FormacionAcademicaService serviceFormacionAcademica = new FormacionAcademicaService();
      PersonaBean persona = new PersonaBean();
      persona.setCodigoPersona(codigoPersona);
      FichaBean f = new FichaBean();
      f.setCodigoFicha(codigoFicha);

      JSONObject jsonObjDatosPersonales = serviceFicha.listarDatosFicha(persona);
      JSONObject jsonObjDatosFamiliares = serviceCargaFamiliar.obtenerCargaFamiliarPorPersona(persona);
      JSONObject jsonObjExperienciaLaboral = serviceExperienciaLaboral.obtenerExperienciaLaboralPorPersona(persona);
      JSONObject jsonObjFormacionAcademica = serviceFormacionAcademica.obtenerFormacionAcademicaPorPersona(persona);
      JOlistarDatosAdministrativosFichas = serviceFicha.listarDatosAdministrativos(f);

      JSONArray jsonArrayDatosFamiliares = jsonObjDatosFamiliares.getJSONObject("data").getJSONArray("cargafamiliar");
      JSONArray jsonArrayExperienciaLaboral = jsonObjExperienciaLaboral.getJSONObject("data").getJSONArray("experiencialaboral");
      JSONArray jsonArrayFormacionAcademica = jsonObjFormacionAcademica.getJSONObject("data").getJSONArray("formacionacademica");

      JSONObject jsonReturn = jsonObjDatosPersonales.getJSONObject("data").getJSONObject("persona");
      jsonReturn.put("cargafamiliar", jsonArrayDatosFamiliares);
      jsonReturn.put("experiencialaboral", jsonArrayExperienciaLaboral);
      jsonReturn.put("formacionacademica", jsonArrayFormacionAcademica);
      jsonReturn.put("datosAdministrativos", JOlistarDatosAdministrativosFichas);

      out.print(jsonObjDatosPersonales);
    }
  }

  private void rechazarFicha(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    JSONObject JOrechazarFicha = null;
    if (request.getParameter("codigoFicha") == null) {
      JOrechazarFicha = new JSONObject();
      JOrechazarFicha.put("status", false);
      JOrechazarFicha.put("message", "Los datos llegaronn nulos");
    } else {
      HttpSession session = request.getSession();
      UsuarioBean usuario = (UsuarioBean) session.getAttribute("usuario");
      int codigoFicha = Integer.parseInt(request.getParameter("codigoFicha"));
      int codigoUsuario = usuario.getCodigoUsuario();

      // obtener codigo estado ficha / desactivar estado ficha / registrar nuevo estado ficha ("RECHAZADO")
      EstadoFichaBean ef = new EstadoFichaBean();
      ef.setCodigoFicha(codigoFicha);
      ef.setCodigoUsuario(codigoUsuario);

      // desactivar ficha del detalle de ficha lotes
      FichaBean f = new FichaBean();
      f.setCodigoFicha(codigoFicha);

      FichaServices services = new FichaServices();
      JOrechazarFicha = services.rechazarFicha(f, ef);

      out.print(JOrechazarFicha);
    }
  }

  private void aceptarFicha(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    JSONObject JOaceptarFicha = null;
    if (request.getParameter("codigoFicha") == null) {
      JOaceptarFicha = new JSONObject();
      JOaceptarFicha.put("status", false);
      JOaceptarFicha.put("message", "Los datos llegaronn nulos");
    } else {
      HttpSession session = request.getSession();
      UsuarioBean usuario = (UsuarioBean) session.getAttribute("usuario");
      int codigoFicha = Integer.parseInt(request.getParameter("codigoFicha"));
      int codigoUsuario = usuario.getCodigoUsuario();

      // obtener codigo estado ficha / desactivar estado ficha / registrar nuevo estado ficha ("RECHAZADO")
      EstadoFichaBean ef = new EstadoFichaBean();
      ef.setCodigoFicha(codigoFicha);
      ef.setCodigoUsuario(codigoUsuario);

      // desactivar ficha del detalle de ficha lotes
      FichaBean f = new FichaBean();
      f.setCodigoFicha(codigoFicha);

      FichaServices services = new FichaServices();
      JOaceptarFicha = services.aceptarFicha(f, ef);

      out.print(JOaceptarFicha);
    }
  }

  private void actualizarFicha(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    JSONObject JOactualizarFicha = null;
    if (request.getParameter("json") == null) {
      JOactualizarFicha = new JSONObject();
      JOactualizarFicha.put("status", false);
      JOactualizarFicha.put("message", "Los datos llegaronn nulos");
      out.print(JOactualizarFicha);
    } else {
      String json = request.getParameter("json");
      JSONObject data = new JSONObject(json);
      System.out.println("**********");
      System.out.println(data);
      System.out.println("**********");
      if (validarDatosEditarFicha(data, data.getBoolean("flagValidacionReniec"))) {
        int codigoTipoDocumento, codigoFicha, codigoPersona;
        String apellidoPaterno, apellidoMaterno, nombres, numeroDocumento, correo;
        PersonaBean p = new PersonaBean();
        TokenFichaBean tf = new TokenFichaBean();
        if (data.getBoolean("flagValidacionReniec")) {
          codigoFicha = data.getInt("codigoFicha");
          codigoPersona = data.getInt("codigoPersona");
          codigoTipoDocumento = data.getInt("codigoTipoDocumento");
          apellidoPaterno = data.getString("apellidoPaterno");
          apellidoMaterno = data.getString("apellidoMaterno");
          nombres = data.getString("nombres");
          numeroDocumento = data.getString("numeroDocumento");
          correo = data.getString("correo");
          p.setCodigoPersona(codigoPersona);
          p.setCodigoTipoDocumento(codigoTipoDocumento);
          p.setApellidoPaterno(apellidoPaterno);
          p.setApellidoMaterno(apellidoMaterno);
          p.setNombre(nombres);
          p.setNumeroDocumento(numeroDocumento);
          p.setCorreo(correo);
          tf.setCodigoFicha(codigoFicha);
        } else {
          codigoFicha = data.getInt("codigoFicha");
          codigoPersona = data.getInt("codigoPersona");
          codigoTipoDocumento = data.getInt("codigoTipoDocumento");
          numeroDocumento = data.getString("numeroDocumento");
          correo = data.getString("correo");
          p.setCodigoPersona(codigoPersona);
          p.setCodigoTipoDocumento(codigoTipoDocumento);
          p.setApellidoPaterno(null);
          p.setApellidoMaterno(null);
          p.setNombre(null);
          p.setNumeroDocumento(numeroDocumento);
          p.setCorreo(correo);
          tf.setCodigoFicha(codigoFicha);
        }
        FichaServices services = new FichaServices();
        JOactualizarFicha = services.actualizarFicha(p, tf);
        out.print(JOactualizarFicha);
      } else {
        JOactualizarFicha = new JSONObject();
        JOactualizarFicha.put("status", false);
        JOactualizarFicha.put("message", "Al parecer ha intentado realizar una acción maliciosa");
        out.print(JOactualizarFicha);
      }
    }
  }

  private boolean validarDatosEditarFicha(JSONObject data, boolean flagValidacionReniec) {
    if (flagValidacionReniec) {
      if (!ParamsValidation.validaSoloNumeros(data.getInt("codigoFicha"))) {
        return false;
      }
      if (!ParamsValidation.validaSoloNumeros(data.getInt("codigoPersona"))) {
        return false;
      }
      if (!ParamsValidation.validaSoloNumeros(data.getInt("codigoTipoDocumento"))) {
        return false;
      }
      if (!ParamsValidation.validaSoloLetras(data.getString("apellidoPaterno"))) {
        return false;
      }
      if (!ParamsValidation.validaSoloLetras(data.getString("apellidoMaterno"))) {
        return false;
      }
      if (!ParamsValidation.validaSoloLetras(data.getString("nombres"))) {
        return false;
      }
      if (!ParamsValidation.validaLongitudNumeroDocumento(data.getInt("longitud"), data.getString("numeroDocumento"))) {
        return false;
      }
      if (!ParamsValidation.validaNumeroDocumento(data.getString("tipoEntrada"), data.getString("numeroDocumento"))) {
        return false;
      }
      if (!ParamsValidation.validaCorreo(data.getString("correo"))) {
        return false;
      }
    } else {
      if (!ParamsValidation.validaSoloNumeros(data.getInt("codigoFicha"))) {
        return false;
      }
      if (!ParamsValidation.validaSoloNumeros(data.getInt("codigoPersona"))) {
        return false;
      }
      if (!ParamsValidation.validaSoloNumeros(data.getInt("codigoTipoDocumento"))) {
        return false;
      }
      if (!ParamsValidation.validaLongitudNumeroDocumento(data.getInt("longitud"), data.getString("numeroDocumento"))) {
        return false;
      }
      if (!ParamsValidation.validaNumeroDocumento(data.getString("tipoEntrada"), data.getString("numeroDocumento"))) {
        return false;
      }
      if (!ParamsValidation.validaCorreo(data.getString("correo"))) {
        return false;
      }
    }
    return true;
  }

  private void anularFicha(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    JSONObject JOanularFicha = null;
    if (request.getParameter("codigoFicha") == null || request.getParameter("observacion") == null) {
      JOanularFicha = new JSONObject();
      JOanularFicha.put("status", "false");
      JOanularFicha.put("message", "Los datos llegaron nulos");
      out.print(JOanularFicha);
    } else {
      HttpSession session = request.getSession();
      UsuarioBean usuario = (UsuarioBean) session.getAttribute("usuario");

      int codigoFicha = Integer.parseInt(request.getParameter("codigoFicha"));
      int codigoUsuario = usuario.getCodigoUsuario();
      String observacion = request.getParameter("observacion");

      EstadoFichaBean ef = new EstadoFichaBean();
      ef.setCodigoFicha(codigoFicha);
      ef.setCodigoUsuario(codigoUsuario);
      ObservacionFichaBean of = new ObservacionFichaBean();
      of.setObservacion(observacion);

      FichaServices services = new FichaServices();
      JOanularFicha = services.anularFicha(ef, of);
      out.print(JOanularFicha);
    }
  }

  private void verObservacion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    JSONObject JOverObservacion = new JSONObject();
    if (request.getParameter("codigoFicha") == null) {
      JOverObservacion.put("status", "false");
      JOverObservacion.put("message", "Los datos llegaron nulos");
      out.print(JOverObservacion);
    } else {
      int codigoFicha = Integer.parseInt(request.getParameter("codigoFicha"));
      EstadoFichaBean ef = new EstadoFichaBean();
      ef.setCodigoFicha(codigoFicha);
      FichaServices services = new FichaServices();
      JOverObservacion = services.verObservacion(ef);
      out.print(JOverObservacion);
    }
  }

  private void listarFichasPresidenciaDT(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    FichaServices service = new FichaServices();

    String draw = request.getParameter("draw");
    String start = request.getParameter("start");
    String length = request.getParameter("length");

    JSONObject respuesta = service.listarFichasPresidenciaDT(draw, length, start, new JSONObject());

    out.print(respuesta);
  }

  private void listarDetalleFichaPresidencia(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    JSONObject JOlistarDatosAdministrativosFichas = null;
    if (request.getParameter("codigoFicha") == null || request.getParameter("codigoPersona") == null) {
      JOlistarDatosAdministrativosFichas = new JSONObject();
      JOlistarDatosAdministrativosFichas.put("message", "Los datos llegaron nulos");
      JOlistarDatosAdministrativosFichas.put("status", false);
      out.print(JOlistarDatosAdministrativosFichas);
    } else {
      int codigoPersona = Integer.parseInt(request.getParameter("codigoPersona"));
      int codigoFicha = Integer.parseInt(request.getParameter("codigoFicha"));
      FichaServices serviceFicha = new FichaServices();
      CargaFamiliarService serviceCargaFamiliar = new CargaFamiliarService();
      ExperienciaLaboralService serviceExperienciaLaboral = new ExperienciaLaboralService();
      FormacionAcademicaService serviceFormacionAcademica = new FormacionAcademicaService();
      ExpedienteService serviceExpediente = new ExpedienteService();
      PersonaBean persona = new PersonaBean();
      persona.setCodigoPersona(codigoPersona);
      FichaBean f = new FichaBean();
      f.setCodigoFicha(codigoFicha);

      JSONObject jsonObjDatosPersonales = serviceFicha.listarDatosFicha(persona);
      JSONObject jsonObjDatosFamiliares = serviceCargaFamiliar.obtenerCargaFamiliarPorPersona(persona);
      JSONObject jsonObjExperienciaLaboral = serviceExperienciaLaboral.obtenerExperienciaLaboralPorPersona(persona);
      JSONObject jsonObjFormacionAcademica = serviceFormacionAcademica.obtenerFormacionAcademicaPorPersona(persona);
      JSONObject jsonObjExpedientes = serviceExpediente.obtenerExpedientesPorPersona(persona);
      JOlistarDatosAdministrativosFichas = serviceFicha.listarDatosAdministrativos(f);

      JSONArray jsonArrayDatosFamiliares = jsonObjDatosFamiliares.getJSONObject("data").getJSONArray("cargafamiliar");
      JSONArray jsonArrayExperienciaLaboral = jsonObjExperienciaLaboral.getJSONObject("data").getJSONArray("experiencialaboral");
      JSONArray jsonArrayFormacionAcademica = jsonObjFormacionAcademica.getJSONObject("data").getJSONArray("formacionacademica");
      JSONArray jsonArrayExpediente = jsonObjExpedientes.getJSONObject("data").getJSONArray("expediente");

      JSONObject jsonReturn = jsonObjDatosPersonales.getJSONObject("data").getJSONObject("persona");
      jsonReturn.put("cargafamiliar", jsonArrayDatosFamiliares);
      jsonReturn.put("experiencialaboral", jsonArrayExperienciaLaboral);
      jsonReturn.put("formacionacademica", jsonArrayFormacionAcademica);
      jsonReturn.put("expediente", jsonArrayExpediente);
      jsonReturn.put("datosAdministrativos", JOlistarDatosAdministrativosFichas);

      out.print(jsonObjDatosPersonales);
    }

  }

  private void listarFichasPresidencia(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    FichaServices service = new FichaServices();
    JSONObject respuesta = service.listarFichasPresidencia();
    out.print(respuesta);
  }

  private void verificarExistenciaFichaAnulada(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    JSONObject JOverificarExistenciaFichaAnulada = null;
    if (request.getParameter("codigoTipoDocumento") == null || request.getParameter("numeroDocumento") == null) {
      JOverificarExistenciaFichaAnulada = new JSONObject();
      JOverificarExistenciaFichaAnulada.put("status", false);
      JOverificarExistenciaFichaAnulada.put("message", "Los parámetros llegaron nulos");
    } else {
      int codigoTipoDocumento = Integer.parseInt(request.getParameter("codigoTipoDocumento"));
      String numeroDocumento = request.getParameter("numeroDocumento");
      PersonaBean p = new PersonaBean();
      p.setCodigoTipoDocumento(codigoTipoDocumento);
      p.setNumeroDocumento(numeroDocumento);
      FichaServices services = new FichaServices();
      JOverificarExistenciaFichaAnulada = services.verificarExistenciaFichaAnulada(p);
      out.print(JOverificarExistenciaFichaAnulada);
    }
  }

  private void obtenerCodigoPersonaPorTipoDocNroDoc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    JSONObject JOobtenerCodigoPersonaPorTipoDocNroDoc = null;
    if (request.getParameter("codigoTipoDocumento") == null && request.getParameter("numeroDocumento") == null) {
      JOobtenerCodigoPersonaPorTipoDocNroDoc = new JSONObject();
      JOobtenerCodigoPersonaPorTipoDocNroDoc.put("status", "false");
      JOobtenerCodigoPersonaPorTipoDocNroDoc.put("message", "Los parámetros llegaron nulos");
      out.print(JOobtenerCodigoPersonaPorTipoDocNroDoc);
    } else {
      int codigoTipoDocumento = Integer.parseInt(request.getParameter("codigoTipoDocumento"));
      String numeroDocumento = request.getParameter("numeroDocumento");
      PersonaBean p = new PersonaBean();
      p.setCodigoTipoDocumento(codigoTipoDocumento);
      p.setNumeroDocumento(numeroDocumento);
      FichaServices services = new FichaServices();
      JOobtenerCodigoPersonaPorTipoDocNroDoc = services.obtenerCodigoPersonaPorTipoDocNroDoc(p);
      out.print(JOobtenerCodigoPersonaPorTipoDocNroDoc);
    }
  }

  private void habilitarFicha(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    JSONObject JOhabilitarFicha = null;
    if (request.getParameter("codigoPersona") == null && request.getParameter("correoElectronico") == null) {
      JOhabilitarFicha = new JSONObject();
      JOhabilitarFicha.put("status", "false");
      JOhabilitarFicha.put("message", "Los parámetros llegaron nulos");
      out.print(JOhabilitarFicha);
    } else {
      HttpSession session = request.getSession();
      UsuarioBean u = (UsuarioBean) session.getAttribute("usuario");
      int codigoPersona = Integer.parseInt(request.getParameter("codigoPersona"));
      String correoElectronico = request.getParameter("correoElectronico");
      PersonaBean p = new PersonaBean();
      p.setCodigoPersona(codigoPersona);
      p.setCorreo(correoElectronico);
      FichaServices services = new FichaServices();
      JOhabilitarFicha = services.habilitarFicha(p, u);
      out.print(JOhabilitarFicha);
    }
  }

  private void validarFichaActiva(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    JSONObject JOvalidarFichaActiva = null;
    if (request.getParameter("codigoPersona") == null) {
      JOvalidarFichaActiva = new JSONObject();
      JOvalidarFichaActiva.put("status", "false");
      JOvalidarFichaActiva.put("message", "Los parámetros llegaron nulos");
      out.print(JOvalidarFichaActiva);
    } else {
      int codigoPersona = Integer.parseInt(request.getParameter("codigoPersona"));
      PersonaBean p = new PersonaBean();
      p.setCodigoPersona(codigoPersona);
      FichaServices services = new FichaServices();
      JOvalidarFichaActiva = services.validarFichaActiva(p);
      out.print(JOvalidarFichaActiva);
    }
  }

  private void imprimirFicha(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession sessionFicha = request.getSession();
    String mensaje = "";
    if (request.getParameter("params") == null) {
      mensaje = "El parámetro [params] llego nulo";
      sessionFicha.setAttribute("errorTokenFicha", mensaje);
      response.sendRedirect("vistas/response/ErrorToken.jsp");
    } else {
      JSONObject JOgetParam = new JSONObject(request.getParameter("params"));
      int codigoPersona = JOgetParam.getInt("codigoPersona");
      int codigoFicha = JOgetParam.getInt("codigoFicha");
      FichaServices serviceFicha = new FichaServices();
      CargaFamiliarService serviceCargaFamiliar = new CargaFamiliarService();
      FormacionAcademicaService serviceFormacionAcademica = new FormacionAcademicaService();
      ExperienciaLaboralService serviceExperienciaLaboral = new ExperienciaLaboralService();
      PersonaBean p = new PersonaBean();
      p.setCodigoPersona(codigoPersona);
      FichaBean f = new FichaBean();
      f.setCodigoFicha(codigoFicha);
//      PrintWriter out = response.getWriter();

      //obtener datos personales
      JSONObject JOdatosPersonales = serviceFicha.listarDatosFicha(p);
      System.out.println(JOdatosPersonales);

      // obtener carga familiar
      JSONObject JOdatosFamiliares = serviceCargaFamiliar.obtenerCargaFamiliarPorPersona(p);

      // obtener formacion academica
      JSONObject JOformacionAcademica = serviceFormacionAcademica.obtenerFormacionAcademicaPorPersona(p);

      // obtener experiencia laboral
      JSONObject JOexperienciaLaboral = serviceExperienciaLaboral.obtenerExperienciaLaboralPorPersona(p);
//      out.print(JOexperienciaLaboral);

      // imprimir
      crearPDFFicha(request, response, JOdatosPersonales, JOdatosFamiliares, JOformacionAcademica, JOexperienciaLaboral);
    }
  }

  private void crearPDFFicha(HttpServletRequest request, HttpServletResponse response, JSONObject JOdatosPersonales, JSONObject JOdatosFamiliares, JSONObject JOFormacionAcademica, JSONObject JOexperienciaLaboral) throws ServletException, IOException {
    String fileName = "ficha" + JOdatosPersonales.getJSONObject("data").getJSONObject("persona").getString("numeroDocumento") + ".pdf";
    response.setContentType("application/pdf");
    response.setHeader("Expires", "0");
    response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
    response.setHeader("Content-Disposition", "inline; filename=" + fileName);
    response.setHeader("Accept-Ranges", "bytes");
    try {

      int contentTitleSize = 9;
      int contentBodySize = 8;

      Document documento = new Document(PageSize.A4, 5, 5, 5, 5);
      PdfWriter.getInstance(documento, response.getOutputStream());
      documento.open();

      // logo saco oliveros y sistema helicoidal
      Image logoSOSH = Image.getInstance("C:\\AppServ\\www\\img\\so_sh.png");
      logoSOSH.scaleToFit(100, 100);
      logoSOSH.setAlignment(Chunk.ALIGN_LEFT);

      // logo apeiron
      Image logoAPE = Image.getInstance("C:\\AppServ\\www\\img\\ap.png");
      logoAPE.scaleToFit(100, 100);
      logoAPE.setAlignment(Chunk.ALIGN_RIGHT);

      // tabla logo
      PdfPTable tablaLogo = new PdfPTable(2);
      tablaLogo.setWidthPercentage(100);
      PdfPCell celda1 = new PdfPCell();
      PdfPCell celda2 = new PdfPCell();
      celda1.setBorder(Rectangle.NO_BORDER);
      celda2.setBorder(Rectangle.NO_BORDER);
      celda1.addElement(logoSOSH);
      celda2.addElement(logoAPE);
      tablaLogo.addCell(celda1);
      tablaLogo.addCell(celda2);
      documento.add(tablaLogo);

      // titulo principal
      Paragraph titulo = new Paragraph("FICHA DE DATOS PERSONALES",
              FontFactory.getFont("arial", 15, Font.BOLD, BaseColor.BLACK));
      titulo.setAlignment(Element.ALIGN_CENTER);
      documento.add(titulo);

      documento.add(Chunk.NEWLINE);

      // 1- DATOS PERSONALES
      JSONObject objPersona = JOdatosPersonales.getJSONObject("data").getJSONObject("persona");
      Paragraph tituloDatosPersonales = new Paragraph("1. DATOS PERSONALES", FontFactory.getFont("arial", 10, Font.BOLD, BaseColor.BLACK));
      titulo.setAlignment(Element.ALIGN_LEFT);
      documento.add(tituloDatosPersonales);

      documento.add(new Phrase(""));

      PdfPTable tablaDatosPersonales = new PdfPTable(6);
      tablaDatosPersonales.setWidthPercentage(100);
      tablaDatosPersonales.setWidths(new float[]{5, 5, 5, 5, 5, 5});
      // primara fila
      tablaDatosPersonales.addCell(createCell("TIPO DOC", 0.5f, 1, 1, Element.ALIGN_LEFT, Font.BOLD, contentTitleSize, Rectangle.NO_BORDER, 3));
      tablaDatosPersonales.addCell(createCell("NÚMERO DE DOC.", 0.5f, 1, 1, Element.ALIGN_LEFT, Font.BOLD, contentTitleSize, Rectangle.NO_BORDER, 3));
      tablaDatosPersonales.addCell(createCell("APELLIDOS Y NOMBRES", 0.5f, 2, 1, Element.ALIGN_LEFT, Font.BOLD, contentTitleSize, Rectangle.NO_BORDER, 3));
      tablaDatosPersonales.addCell(createCell("NACIONALIDAD", 0.5f, 1, 1, Element.ALIGN_LEFT, Font.BOLD, contentTitleSize, Rectangle.NO_BORDER, 3));
      tablaDatosPersonales.addCell(createCell("FOTO", 0.5f, 1, 1, Element.ALIGN_CENTER, Font.BOLD, contentTitleSize, Rectangle.NO_BORDER, 3));
      tablaDatosPersonales.addCell(createCell(objPersona.getString("tipoDocumentoDescripcionCorta"), 0.5f, 1, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
      tablaDatosPersonales.addCell(createCell(objPersona.getString("numeroDocumento"), 0.5f, 1, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
      tablaDatosPersonales.addCell(createCell(objPersona.getString("apellidoPaterno") + " " + objPersona.getString("apellidoMaterno") + ", " + objPersona.getString("nombre"), 0.5f, 2, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
      tablaDatosPersonales.addCell(createCell(objPersona.getString("gentilicio"), 0.5f, 1, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
      // foto
      if (objPersona.getString("foto").equals("")) {
        String foto = "C:\\AppServ\\www\\img\\default.png"; // 128 x 128
        Image image = Image.getInstance(foto);
        PdfPCell cellImage = new PdfPCell();
        cellImage.addElement(image);
        cellImage.setBorderWidth(0.5f);
        cellImage.setColspan(1);
        cellImage.setRowspan(7);
        cellImage.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellImage.setBorder(Rectangle.BOX);
        cellImage.setPaddingBottom(5);
        tablaDatosPersonales.addCell(cellImage);
      } else {
        String foto = "C:\\AppServ\\www\\img\\" + objPersona.getString("foto"); // 128 x 128
        Image image = Image.getInstance(foto);
        PdfPCell cellImage = new PdfPCell();
        cellImage.addElement(image);
        cellImage.setBorderWidth(0.5f);
        cellImage.setColspan(1);
        cellImage.setRowspan(7);
        cellImage.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellImage.setBorder(Rectangle.BOX);
        cellImage.setPaddingBottom(5);
        tablaDatosPersonales.addCell(cellImage);
      }

      // segunda fila
      tablaDatosPersonales.addCell(createCell("DPTO - PROV - DIST DE NACIMIENTO", 0.5f, 2, 1, Element.ALIGN_LEFT, Font.BOLD, contentTitleSize, Rectangle.NO_BORDER, 3));
      tablaDatosPersonales.addCell(createCell("DIRECCIÓN DE DOCUMENTO", 0.5f, 2, 1, Element.ALIGN_LEFT, Font.BOLD, contentTitleSize, Rectangle.NO_BORDER, 3));
      tablaDatosPersonales.addCell(createCell("AFP / ONP", 0.5f, 1, 1, Element.ALIGN_LEFT, Font.BOLD, contentTitleSize, Rectangle.NO_BORDER, 3));
      if (objPersona.getString("nombreDepartamentoNacimiento").equals("-") || objPersona.getString("nombreProvinciaNacimiento").equals("-") || objPersona.getString("nombreDistritoNacimiento").equals("-")) {
        tablaDatosPersonales.addCell(createCell("(ES EXTRANJERO)", 0.5f, 2, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
      } else {
        tablaDatosPersonales.addCell(createCell(objPersona.getString("nombreDepartamentoNacimiento") + " - " + objPersona.getString("nombreProvinciaNacimiento") + " - " + objPersona.getString("nombreDistritoNacimiento"), 0.5f, 2, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
      }
      tablaDatosPersonales.addCell(createCell(objPersona.getString("direccionDocumento").equals("") ? "-" : objPersona.getString("direccionDocumento"), 0.5f, 2, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
      tablaDatosPersonales.addCell(createCell(objPersona.getString("fondoPensionDescripcionCorta"), 0.5f, 1, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
      // tercera fila
      tablaDatosPersonales.addCell(createCell("DPTO - PROV - DIST DE RESIDENCIA", 0.5f, 2, 1, Element.ALIGN_LEFT, Font.BOLD, contentTitleSize, Rectangle.NO_BORDER, 3));
      tablaDatosPersonales.addCell(createCell("DIRECCIÓN DE RESIDENCIA", 0.5f, 2, 1, Element.ALIGN_LEFT, Font.BOLD, contentTitleSize, Rectangle.NO_BORDER, 3));
      tablaDatosPersonales.addCell(createCell("RUC", 0.5f, 1, 1, Element.ALIGN_LEFT, Font.BOLD, contentTitleSize, Rectangle.NO_BORDER, 3));
      tablaDatosPersonales.addCell(createCell(objPersona.getString("nombreDepartamentoResidencia") + " - " + objPersona.getString("nombreProvinciaResidencia") + " - " + objPersona.getString("nombreDistritoResidencia"), 0.5f, 2, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
      tablaDatosPersonales.addCell(createCell(objPersona.getString("direccionResidencia"), 0.5f, 2, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
      tablaDatosPersonales.addCell(createCell(objPersona.getString("ruc"), 0.5f, 1, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
      // cuarta fila
      tablaDatosPersonales.addCell(createCell("FECHA NACIMIENTO", 0.5f, 1, 1, Element.ALIGN_LEFT, Font.BOLD, contentTitleSize, Rectangle.NO_BORDER, 3));
      tablaDatosPersonales.addCell(createCell("EDAD", 0.5f, 1, 1, Element.ALIGN_LEFT, Font.BOLD, contentTitleSize, Rectangle.NO_BORDER, 3));
      tablaDatosPersonales.addCell(createCell("SEXO", 0.5f, 1, 1, Element.ALIGN_LEFT, Font.BOLD, contentTitleSize, Rectangle.NO_BORDER, 3));
      tablaDatosPersonales.addCell(createCell("ESTADO CIVIL", 0.5f, 1, 1, Element.ALIGN_LEFT, Font.BOLD, contentTitleSize, Rectangle.NO_BORDER, 3));
      tablaDatosPersonales.addCell(createCell("NÚMERO DE HIJOS", 0.5f, 1, 1, Element.ALIGN_LEFT, Font.BOLD, contentTitleSize, Rectangle.NO_BORDER, 3));
      tablaDatosPersonales.addCell(createCell(objPersona.getString("fechaNacimiento"), 0.5f, 1, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
      tablaDatosPersonales.addCell(createCell(objPersona.getString("edad"), 0.5f, 1, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
      tablaDatosPersonales.addCell(createCell(objPersona.getString("sexo"), 0.5f, 1, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
      tablaDatosPersonales.addCell(createCell(objPersona.getString("estadoCivil"), 0.5f, 1, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
      tablaDatosPersonales.addCell(createCell(objPersona.getString("nroHijos"), 0.5f, 1, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
      // quinta fila
      tablaDatosPersonales.addCell(createCell("TELÉFONO FIJO", 0.5f, 1, 1, Element.ALIGN_LEFT, Font.BOLD, contentTitleSize, Rectangle.NO_BORDER, 3));
      tablaDatosPersonales.addCell(createCell("TELÉFONO MÓVIL", 0.5f, 1, 1, Element.ALIGN_LEFT, Font.BOLD, contentTitleSize, Rectangle.NO_BORDER, 3));
      tablaDatosPersonales.addCell(createCell("CORREO ELECTRÓNICO", 0.5f, 4, 1, Element.ALIGN_LEFT, Font.BOLD, contentTitleSize, Rectangle.NO_BORDER, 3));
      tablaDatosPersonales.addCell(createCell(objPersona.getString("telefonoFijo"), 0.5f, 1, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
      tablaDatosPersonales.addCell(createCell(objPersona.getString("telefonoMovil"), 0.5f, 1, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
      tablaDatosPersonales.addCell(createCell(objPersona.getString("correo"), 0.5f, 4, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
      tablaDatosPersonales.addCell(createCell("UBICACIÓN GEOGRÁFICA DE LA DIRECCIÓN DE RESIDENCIA:", 0.5f, 7, 1, Element.ALIGN_LEFT, Font.BOLD, contentTitleSize, Rectangle.BOX, 5));
      // sexta fila - croquis
      String croquis = "https://maps.googleapis.com/maps/api/staticmap?center=" + objPersona.getString("latitud") + "," + objPersona.getString("longitud") + "&zoom=16&size=800x200&markers=color:red|" + objPersona.getString("latitud") + "," + objPersona.getString("longitud") + "&key=AIzaSyCgA2sD34iT4HmBrz1Nj3Pxdg1UQisJRLs"; // 128 x 128
      Image imageCroquis = Image.getInstance(new URL(croquis));
      PdfPCell cellImageCroquis = new PdfPCell();
      cellImageCroquis.addElement(imageCroquis);
      cellImageCroquis.setBorderWidth(0.5f);
      cellImageCroquis.setColspan(6);
      cellImageCroquis.setRowspan(1);
      cellImageCroquis.setHorizontalAlignment(Element.ALIGN_CENTER);
      cellImageCroquis.setBorder(Rectangle.BOX);
      cellImageCroquis.setPaddingBottom(5);
      tablaDatosPersonales.addCell(cellImageCroquis);
      documento.add(tablaDatosPersonales);

      documento.add(Chunk.NEWLINE);

      // 2- DATOS FAMILIARES
      JSONArray arrFamiliar = JOdatosFamiliares.getJSONObject("data").getJSONArray("cargafamiliar");
      Paragraph tituloDatosFamiliares = new Paragraph("2. DATOS FAMILIARES", FontFactory.getFont("arial", 10, Font.BOLD, BaseColor.BLACK));
      titulo.setAlignment(Element.ALIGN_LEFT);
      documento.add(tituloDatosFamiliares);
      documento.add(new Phrase(""));

      // cabeceras
      String[] cabecerasDatosFamiliares = {"#", "APELLIDOS Y NOMBRES", "TIPO DOC", "NRO DOC", "EDAD", "PARENTESCO", "TELEF/CEL"};
      PdfPTable tablaDatosFamiliares = new PdfPTable(7);
      tablaDatosFamiliares.setWidthPercentage(100);
      tablaDatosFamiliares.setWidths(new float[]{0.3f, 2.5f, 1, 1, 0.5f, 1, 1});
      for (String dataFamiliar : cabecerasDatosFamiliares) {
        tablaDatosFamiliares.addCell(createCell(dataFamiliar, 0.5f, 1, 1, Element.ALIGN_CENTER, Font.BOLD, contentTitleSize, Rectangle.NO_BORDER, 3));
      }
      for (int i = 0; i < arrFamiliar.length(); i++) {
        JSONObject objFamiliar = arrFamiliar.getJSONObject(i);
        tablaDatosFamiliares.addCell(createCell("" + (i + 1), 0.5f, 1, 1, Element.ALIGN_CENTER, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
        tablaDatosFamiliares.addCell(createCell(objFamiliar.getString("apellidoPaterno") + " " + objFamiliar.getString("apellidoMaterno") + ", " + objFamiliar.getString("nombre"), 0.5f, 1, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
        tablaDatosFamiliares.addCell(createCell(objFamiliar.getString("nombreTipoDocumentoDescripcionCorta"), 0.5f, 1, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
        tablaDatosFamiliares.addCell(createCell(objFamiliar.getString("numeroDocumento"), 0.5f, 1, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
        tablaDatosFamiliares.addCell(createCell(objFamiliar.getString("edad"), 0.5f, 1, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
        tablaDatosFamiliares.addCell(createCell(objFamiliar.getString("nombreParentesco"), 0.5f, 1, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
        tablaDatosFamiliares.addCell(createCell(objFamiliar.getString("telefono"), 0.5f, 1, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
      }
      documento.add(tablaDatosFamiliares);

      documento.add(Chunk.NEWLINE);

      // 3. FORMACIÓN ACADÉMICA
      JSONArray arrFormacionAcademica = JOFormacionAcademica.getJSONObject("data").getJSONArray("formacionacademica");
      Paragraph tituloDatosFormacionAcademica = new Paragraph("3. FORMACIÓN ACADÉMICA", FontFactory.getFont("arial", 10, Font.BOLD, BaseColor.BLACK));
      tituloDatosFormacionAcademica.setAlignment(Element.ALIGN_LEFT);
      documento.add(tituloDatosFormacionAcademica);
      documento.add(new Phrase(""));
      // cabeceras
      String[] cabecerasDatosFormacionAcademica = {"#", "CENTRO DE ESTUDIOS", "GRADO", "ESTADO", "CARRERA", "INICIO", "FIN"};
      PdfPTable tablaDatosFormacionAcademica = new PdfPTable(7);
      tablaDatosFormacionAcademica.setWidthPercentage(100);
      tablaDatosFormacionAcademica.setWidths(new float[]{0.3f, 2.5f, 1, 1, 1.5f, 0.6f, 0.6f});
      for (String dataFormacionAcademica : cabecerasDatosFormacionAcademica) {
        tablaDatosFormacionAcademica.addCell(createCell(dataFormacionAcademica, 0.5f, 1, 1, Element.ALIGN_CENTER, Font.BOLD, contentTitleSize, Rectangle.NO_BORDER, 3));
      }
      for (int i = 0; i < arrFormacionAcademica.length(); i++) { // arrFormacionAcademica.length()
        JSONObject objFormacionAcademica = arrFormacionAcademica.getJSONObject(i);
        tablaDatosFormacionAcademica.addCell(createCell("" + (i + 1), 0.5f, 1, 1, Element.ALIGN_CENTER, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
        tablaDatosFormacionAcademica.addCell(createCell(objFormacionAcademica.getString("nombreCentroEstudios"), 0.5f, 1, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
        tablaDatosFormacionAcademica.addCell(createCell(objFormacionAcademica.getString("nivelEstudio"), 0.5f, 1, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
        tablaDatosFormacionAcademica.addCell(createCell(objFormacionAcademica.getString("estadoEstudio"), 0.5f, 1, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
        tablaDatosFormacionAcademica.addCell(createCell(objFormacionAcademica.getString("nombreCarreraProfesional"), 0.5f, 1, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
        tablaDatosFormacionAcademica.addCell(createCell(objFormacionAcademica.getString("fechaInicio"), 0.5f, 1, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
        tablaDatosFormacionAcademica.addCell(createCell(objFormacionAcademica.getString("fechaFin"), 0.5f, 1, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
      }
      documento.add(tablaDatosFormacionAcademica);

      documento.add(Chunk.NEWLINE);

      // 4. EXPERIENCIA LABORAL
      JSONArray arrExperienciaLaboral = JOexperienciaLaboral.getJSONObject("data").getJSONArray("experiencialaboral");
      Paragraph tituloDatoExperienciaLaboral = new Paragraph("4. EXPERIENCIA LABORAL", FontFactory.getFont("arial", 10, Font.BOLD, BaseColor.BLACK));
      tituloDatoExperienciaLaboral.setAlignment(Element.ALIGN_LEFT);
      documento.add(tituloDatoExperienciaLaboral);
      documento.add(new Phrase(""));

      if (arrExperienciaLaboral.length() == 0) {
        Paragraph tituloDatoExperienciaLaboralEmpty = new Paragraph("Sin experiencia laboral", FontFactory.getFont("arial", contentBodySize, Font.NORMAL, BaseColor.BLACK));
        tituloDatoExperienciaLaboralEmpty.setAlignment(Element.ALIGN_LEFT);
        documento.add(tituloDatoExperienciaLaboralEmpty);
      } else {
        String[] cabecerasDatosExperienciaLaboral = {"#", "EMPRESA", "CARGO", "INICIO", "FIN", "TELEFONO"};
        PdfPTable tablaDatosExperienciaLaboral = new PdfPTable(6);
        tablaDatosExperienciaLaboral.setWidthPercentage(100);
        tablaDatosExperienciaLaboral.setWidths(new float[]{0.3f, 2, 2, 1, 1, 1});
        for (String dataExperienciaLaboral : cabecerasDatosExperienciaLaboral) {
          tablaDatosExperienciaLaboral.addCell(createCell(dataExperienciaLaboral, 0.5f, 1, 1, Element.ALIGN_CENTER, Font.BOLD, contentTitleSize, Rectangle.NO_BORDER, 3));
        }
        for (int i = 0; i < arrExperienciaLaboral.length(); i++) {
          JSONObject objExperienciaLaboral = arrExperienciaLaboral.getJSONObject(i);
          tablaDatosExperienciaLaboral.addCell(createCell("" + (i + 1), 0.5f, 1, 1, Element.ALIGN_CENTER, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
          tablaDatosExperienciaLaboral.addCell(createCell(objExperienciaLaboral.getString("nombreEmpresa"), 0.5f, 1, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
          tablaDatosExperienciaLaboral.addCell(createCell(objExperienciaLaboral.getString("nombreCargo"), 0.5f, 1, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
          tablaDatosExperienciaLaboral.addCell(createCell(objExperienciaLaboral.getString("fechaInicio"), 0.5f, 1, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
          tablaDatosExperienciaLaboral.addCell(createCell(objExperienciaLaboral.getString("fechaFin"), 0.5f, 1, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
          tablaDatosExperienciaLaboral.addCell(createCell(objExperienciaLaboral.getString("telefono"), 0.5f, 1, 1, Element.ALIGN_LEFT, Font.NORMAL, contentBodySize, Rectangle.NO_BORDER, 5));
        }
        documento.add(tablaDatosExperienciaLaboral);
      }

      documento.close();
    } catch (DocumentException e) {
      throw new IOException(e.getMessage());
    }
  }

  public PdfPCell createCell(String content, float borderWidth, int colspan, int rowspan, int alignment, int textStyle, int size, int border, int paddingBotom) {
    Font f = new Font(Font.FontFamily.HELVETICA, size, textStyle, GrayColor.BLACK);
    PdfPCell cell = new PdfPCell(new Phrase(content, f));
//    cell.setBorder(border);
    cell.setBorder(Rectangle.BOX);
    cell.setBorderWidth(borderWidth);
    cell.setColspan(colspan);
    cell.setRowspan(rowspan);
    cell.setHorizontalAlignment(alignment);
    cell.setPaddingBottom(paddingBotom);
    return cell;
  }

  private void imprimirActividadFicha(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//    PrintWriter out = response.getWriter();

    String data = request.getParameter("paramsActividadFicha");

    byte[] bytes = data.getBytes(StandardCharsets.ISO_8859_1);
    data = new String(bytes, StandardCharsets.UTF_8);

    JSONObject JOdata = new JSONObject(data);

    System.out.println(JOdata);

    crearPDFActividadFicha(JOdata, request, response);
  }

  private void crearPDFActividadFicha(JSONObject JOdata, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/pdf");
    response.setHeader("Expires", "0");
    response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
    response.setHeader("Content-Disposition", "inline; filename=actividadFichaNro" + JOdata.getInt("codigoFicha"));
    response.setHeader("Accept-Ranges", "bytes");

    try {
      Document documento = new Document(PageSize.A4, 5, 5, 5, 5);
      PdfWriter.getInstance(documento, response.getOutputStream());
      documento.open();

      // logo saco oliveros y sistema helicoidal
      Image logoSOSH = Image.getInstance("C:\\AppServ\\www\\img\\so_sh.png");
      logoSOSH.scaleToFit(100, 100);
      logoSOSH.setAlignment(Chunk.ALIGN_LEFT);

      // logo apeiron
      Image logoAPE = Image.getInstance("C:\\AppServ\\www\\img\\ap.png");
      logoAPE.scaleToFit(100, 100);
      logoAPE.setAlignment(Chunk.ALIGN_RIGHT);

      // tabla logo
      PdfPTable tablaLogo = new PdfPTable(2);
      tablaLogo.setWidthPercentage(100);
      PdfPCell celda1 = new PdfPCell();
      PdfPCell celda2 = new PdfPCell();
      celda1.setBorder(Rectangle.NO_BORDER);
      celda2.setBorder(Rectangle.NO_BORDER);
      celda1.addElement(logoSOSH);
      celda2.addElement(logoAPE);
      tablaLogo.addCell(celda1);
      tablaLogo.addCell(celda2);
      documento.add(tablaLogo);

      // titulo principal
      Paragraph titulo = new Paragraph("ACTIVIDADES DE LA FICHA",
              FontFactory.getFont("arial", 22, Font.BOLD, BaseColor.BLACK));
      titulo.setAlignment(Element.ALIGN_CENTER);
      documento.add(titulo);

      Paragraph codigoFicha = new Paragraph("CÓDIGO DE FICHA: " + JOdata.getInt("codigoFicha"),
              FontFactory.getFont("arial", 15, Font.NORMAL, BaseColor.BLACK));
      codigoFicha.setAlignment(Element.ALIGN_CENTER);
      documento.add(codigoFicha);

      documento.add(new Paragraph("\n\n"));

      // mostrar actividades
      int longitudActividades = JOdata.getJSONArray("arrayActividadFicha").length();
      JSONArray JAdata = JOdata.getJSONArray("arrayActividadFicha");

      for (int i = 0; i < longitudActividades; i++) {
        JSONObject data = JAdata.getJSONObject(i);

        PdfPTable actividades = new PdfPTable(2);
        actividades.setWidths(new int[]{1, 5});

        String isUser = data.getInt("codigoUsuario") == 0 ? "*" : "";

        PdfPCell cellUsuario = new PdfPCell(new Phrase(data.getString("nombreUsuario") + " " + isUser, FontFactory.getFont("arial", 10, Font.BOLD, BaseColor.BLACK)));
        cellUsuario.setColspan(2);
        actividades.addCell(cellUsuario);

        PdfPCell tituloAccion = new PdfPCell(new Phrase("ACCIÓN: ", FontFactory.getFont("arial", 8, Font.BOLD, BaseColor.BLACK)));
        tituloAccion.setPadding(3);
        actividades.addCell(tituloAccion);
        PdfPCell accion = new PdfPCell(new Phrase(data.getString("descripcionEstado"), FontFactory.getFont("arial", 8, Font.NORMAL, BaseColor.BLACK)));
        accion.setPadding(3);
        actividades.addCell(accion);

        PdfPCell tituloEstado = new PdfPCell(new Phrase("ESTADO: ", FontFactory.getFont("arial", 8, Font.BOLD, BaseColor.BLACK)));
        tituloEstado.setPadding(3);
        actividades.addCell(tituloEstado);
        PdfPCell estado = new PdfPCell(new Phrase(data.getString("nombreEstado"), FontFactory.getFont("arial", 8, Font.NORMAL, BaseColor.BLACK)));
        estado.setPadding(3);
        actividades.addCell(estado);

        PdfPCell tituloRegistro = new PdfPCell(new Phrase("REGISTRO: ", FontFactory.getFont("arial", 8, Font.BOLD, BaseColor.BLACK)));
        tituloRegistro.setPadding(3);
        actividades.addCell(tituloRegistro);
        PdfPCell registro = new PdfPCell(new Phrase(data.getString("fechaRegistroEstado"), FontFactory.getFont("arial", 8, Font.NORMAL, BaseColor.BLACK)));
        registro.setPadding(3);
        actividades.addCell(registro);

        documento.add(actividades);

        documento.add(new Paragraph("\n\n"));

      }

      PdfPTable actividades = new PdfPTable(1);
      PdfPCell nroActividades = new PdfPCell(new Phrase("Número de actividades: " + longitudActividades, FontFactory.getFont("arial", 8, Font.NORMAL, BaseColor.BLACK)));
      nroActividades.setPadding(3);
      nroActividades.setBorder(Rectangle.NO_BORDER);
      actividades.addCell(nroActividades);

      documento.add(actividades);

      documento.close();
    } catch (DocumentException e) {
      throw new IOException(e.getMessage());
    }
  }

}
