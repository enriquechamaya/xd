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
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
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
import trismegistoplanilla.beans.LoteAprobacionBean;
import trismegistoplanilla.services.LoteAprobacionService;
import trismegistoplanilla.utilities.DateTimeServer;
import trismegistoplanilla.utilities.ParamsValidation;

public class LoteAprobacionServlet extends HttpServlet {

  private static final long serialVersionUID = -1613959268000536244L;

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String param = request.getParameter("accion");
    switch (param) {
      case "aprobarFichaAdministrativa":
        aprobarFichaAdministrativa(request, response);
        break;
      case "aprobarFichaDocente":
        aprobarFichaDocente(request, response);
        break;
      case "rechazarFicha":
        rechazarFicha(request, response);
        break;
      case "generarLoteAprobacion":
        generarLoteAprobacion(request, response);
        break;
      case "listarFichasPresidenciaDT":
        listarFichasPresidenciaDT(request, response);
        break;
      case "obtenerDetalleLote":
        obtenerDetalleLote(request, response);
        break;
      case "imprimirFichasAprobadas":
        imprimirFichasAprobadas(request, response);
        break;
      default:
        break;
    }
  }

  private void aprobarFichaAdministrativa(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    LoteAprobacionService service = new LoteAprobacionService();
    HttpSession sesion = request.getSession();
    UsuarioBean usuario = (UsuarioBean) sesion.getAttribute("usuario");
    int codigoUsuario = usuario.getCodigoUsuario();

    JSONObject json = new JSONObject(request.getParameter("json"));
    json.put("usuario", codigoUsuario);

    JSONObject respuesta = service.aprobarFichaAdministrativa(json);
    out.print(respuesta);
  }

  private void aprobarFichaDocente(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    LoteAprobacionService service = new LoteAprobacionService();
    HttpSession sesion = request.getSession();
    UsuarioBean usuario = (UsuarioBean) sesion.getAttribute("usuario");
    int codigoUsuario = usuario.getCodigoUsuario();

    JSONObject json = new JSONObject(request.getParameter("json"));
    json.put("usuario", codigoUsuario);

    JSONObject respuesta = service.aprobarFichaDocente(json);

    out.print(respuesta);
  }

  private void rechazarFicha(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    LoteAprobacionService service = new LoteAprobacionService();
    HttpSession sesion = request.getSession();
    UsuarioBean usuario = (UsuarioBean) sesion.getAttribute("usuario");
    int codigoUsuario = usuario.getCodigoUsuario();

    JSONObject json = new JSONObject(request.getParameter("json"));
    json.put("usuario", codigoUsuario);

    JSONObject respuesta = service.rechazarFicha(json);

    out.print(respuesta);
  }

  private void generarLoteAprobacion(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();

    LoteAprobacionService service = new LoteAprobacionService();
    String jsonObj = request.getParameter("json");
    System.out.println(jsonObj);
    JSONObject data = new JSONObject(jsonObj);
    HttpSession sesion = request.getSession();
    UsuarioBean usuario = (UsuarioBean) sesion.getAttribute("usuario");
    data.put("usuario", 5);

    JSONObject respuesta = service.generarLoteAprobacion(data);
    out.print(respuesta);

  }

  private void listarFichasPresidenciaDT(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    LoteAprobacionService service = new LoteAprobacionService();
    String draw = request.getParameter("draw");
    String start = request.getParameter("start");
    String length = request.getParameter("length");
    JSONObject json = new JSONObject(request.getParameter("json"));
    System.out.println(json);

    if (validarFiltro(json).getBoolean("status")) {
      JSONObject respuesta = service.listarFichasPresidenciaDT(draw, start, length, json);
      out.print(respuesta);
    } else {
      System.out.println(validarFiltro(json).getString("message"));
      out.print(validarFiltro(json));
    }

  }

  private void obtenerDetalleLote(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    JSONObject json = new JSONObject(request.getParameter("json"));
    LoteAprobacionService service = new LoteAprobacionService();

    JSONObject respuesta = service.obtenerDetalleLote(json);
    out.print(respuesta);
  }

  private JSONObject validarFiltro(JSONObject json) {
    int criterio = json.getInt("criterio");
    int filtro = json.getInt("filtro");
    String search = json.getString("search");
    String search2 = json.getString("search2");
    JSONObject validar = new JSONObject();

    if (!ParamsValidation.validaSoloNumeros(criterio)) {
      validar.put("status", false);
      validar.put("message", "El critério de búsqueda debe ser un número.");
      return validar;
    }

    if (criterio != 1 && criterio != 2 && criterio != 3 && criterio != 0) {
      validar.put("status", false);
      validar.put("message", "No existe criterio indicado.");
      return validar;
    }

    if (!ParamsValidation.validaSoloNumeros(filtro)) {
      validar.put("status", false);
      validar.put("message", "El filtro debe ser un número.");
      return validar;
    }

    if (criterio == 1) {
      if (search.equals("")) {
        validar.put("status", false);
        validar.put("message", "El número de lote es vacío.");
        return validar;
      }
//      if (!ParamsValidation.validaSoloNumeros(Integer.parseInt(search))) {
//        validar.put("status", false);
//        validar.put("message", "El lote debe ser un número.");
//        return validar;
//      }
    }

    if (criterio == 2) {
      if (filtro == 0) {
        validar.put("status", false);
        validar.put("message", "Debe escoger un filtro.");
        return validar;
      }

      if (filtro != 1 && filtro != 2 && filtro != 3 && filtro != 4) {
        validar.put("status", false);
        validar.put("message", "No existe filtro indicado.");
        return validar;
      }

      if (search.equals("")) {
        validar.put("status", false);
        validar.put("message", "Debe indicar una fecha.");
        return validar;
      }

      if (!ParamsValidation.validaSoloFecha(search)) {
        validar.put("status", false);
        validar.put("message", "La fecha recibida no tiene el formato correcto.");
        return validar;
      }

      if (filtro == 4) {
        if (search2.equals("")) {
          validar.put("status", false);
          validar.put("message", "Debe indicar una fecha.");
          return validar;
        }
        if (!ParamsValidation.validaSoloFecha(search2)) {
          validar.put("status", false);
          validar.put("message", "La fecha recibida no tiene el formato correcto.");
          return validar;
        }
      }
    }

    if (criterio == 3) {
      if (filtro == 0) {
        validar.put("status", false);
        validar.put("message", "Debe escoger un filtro.");
        return validar;
      }

      if (filtro != 1 && filtro != 2 && filtro != 3) {
        validar.put("status", false);
        validar.put("message", "No existe filtro indicado.");
        return validar;
      }

      if (search.equals("")) {
        validar.put("status", false);
        validar.put("message", "Debe indicar un número de lote.");
        return validar;
      }

      if (!ParamsValidation.validaSoloNumeros(Integer.parseInt(search))) {
        validar.put("status", false);
        validar.put("message", "Debe indicar un número.");
        return validar;
      }
    }

    validar.put("status", true);
    validar.put("message", "Paso el filtro correctamente.");
    return validar;

  }

  private void imprimirFichasAprobadas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    JSONObject JOimprimirFichasAprobadas = null;
    System.out.println(request.getParameter("codigoLote"));
    if (request.getParameter("codigoLote") == null) {
      PrintWriter out = response.getWriter();
      JOimprimirFichasAprobadas = new JSONObject();
      JOimprimirFichasAprobadas.put("status", "false");
      JOimprimirFichasAprobadas.put("message", "El parámetro llegó nulo");
      out.print(JOimprimirFichasAprobadas);
    } else {
      int codigoLote = Integer.parseInt(request.getParameter("codigoLote"));
      LoteAprobacionBean la = new LoteAprobacionBean();
      la.setCodigoLoteAprobacion(codigoLote);
      LoteAprobacionService service = new LoteAprobacionService();
      JOimprimirFichasAprobadas = service.imprimirFichasAprobadas(la);
      createPDFFichasAprobadas(request, response, JOimprimirFichasAprobadas);
    }
  }

  private void createPDFFichasAprobadas(HttpServletRequest request, HttpServletResponse response, JSONObject JOimprimirFichasAprobadas) throws ServletException, IOException {
    String fileName = "loteAprobacion" + JOimprimirFichasAprobadas.getJSONObject("data").getString("numeroLote") + ".pdf";
    response.setContentType("application/pdf");
    response.setHeader("Expires", "0");
    response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
    response.setHeader("Content-Disposition", "inline; filename=" + fileName);
    response.setHeader("Accept-Ranges", "bytes");
    try {
      Document documento = new Document(PageSize.A4.rotate(), 5, 5, 5, 5);
      PdfWriter writer = PdfWriter.getInstance(documento, response.getOutputStream());
      MyFooter event = new MyFooter();
      writer.setPageEvent(event);
      documento.open();

      // logo saco oliveros y sistema helicoidal
      Image logoSOSH = Image.getInstance("C:\\AppServ\\www\\img\\so_sh.png");
      logoSOSH.scaleToFit(200, 200);
      logoSOSH.setAlignment(Chunk.ALIGN_LEFT);

      Image logoAPE = Image.getInstance("C:\\AppServ\\www\\img\\ap.png");
      logoAPE.scaleToFit(200, 200);
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
      Paragraph titulo = new Paragraph("CONSOLIDADO FICHAS APROBADAS POR PRESIDENCIA",
              FontFactory.getFont("arial", 22, Font.UNDERLINE, BaseColor.BLACK));
      titulo.setAlignment(Element.ALIGN_CENTER);
      documento.add(titulo);

      // lote
      String numeroLote = JOimprimirFichasAprobadas.getJSONObject("data").getString("numeroLote");
      Paragraph lote = new Paragraph("N° LOTE " + numeroLote,
              FontFactory.getFont("arial", 22, Font.BOLD, BaseColor.BLACK));
      lote.setAlignment(Element.ALIGN_CENTER);
      documento.add(lote);

      documento.add(new Phrase(""));

      /* cabecera */
      String[] cabecera = {"#", "PERSONAL", "TIPO DOC", "NRO DOC", "INGRESO", "TÉRMINO", "SEDE", "ÁREA", "CARGO", "RAZÓN", "ESTADO"};
      PdfPTable tablaData = new PdfPTable(cabecera.length);
      tablaData.setWidthPercentage(100);
      tablaData.setWidths(new float[]{0.2f, 2f, 0.7f, 1f, 0.5f, 0.5f, 1, 1, 1.3f, 0.5f, 0.8f});
      for (String cabeceraData : cabecera) {
        tablaData.addCell(createCell(cabeceraData, 1, 1, Element.ALIGN_CENTER, Font.BOLD));
      }

      /* data */
      int longitudArray = JOimprimirFichasAprobadas.getJSONObject("data").getJSONArray("fichasAprobadas").length();
      JSONArray data = JOimprimirFichasAprobadas.getJSONObject("data").getJSONArray("fichasAprobadas");
      for (int i = 0; i < longitudArray; i++) {
        JSONObject obj = data.getJSONObject(i);
        tablaData.addCell(createCell("" + (i + 1), 1, 1, Element.ALIGN_CENTER, Font.BOLD));
        tablaData.addCell(createCell(obj.getString("apellidoPaterno") + " " + obj.getString("apellidoMaterno") + ", " + obj.getString("nombres"), 1, 1, Element.ALIGN_LEFT, Font.NORMAL));
        tablaData.addCell(createCell(obj.getString("tipoDocumento"), 1, 1, Element.ALIGN_LEFT, Font.NORMAL));
        tablaData.addCell(createCell(obj.getString("numeroDocumento"), 1, 1, Element.ALIGN_LEFT, Font.NORMAL));
        tablaData.addCell(createCell(obj.getString("fechaInicio"), 1, 1, Element.ALIGN_CENTER, Font.NORMAL));
        tablaData.addCell(createCell(obj.getString("fechaFin"), 1, 1, Element.ALIGN_CENTER, Font.NORMAL));
        tablaData.addCell(createCell(obj.getString("sede"), 1, 1, Element.ALIGN_LEFT, Font.NORMAL));
        tablaData.addCell(createCell(obj.getString("area"), 1, 1, Element.ALIGN_LEFT, Font.NORMAL));
        tablaData.addCell(createCell(obj.getString("cargo"), 1, 1, Element.ALIGN_LEFT, Font.NORMAL));
        tablaData.addCell(createCell(obj.getString("razonSocial"), 1, 1, Element.ALIGN_LEFT, Font.NORMAL));
        tablaData.addCell(createCell(obj.getString("estado"), 1, 1, Element.ALIGN_LEFT, Font.NORMAL));
      }
      documento.add(tablaData);

      // salto de linea
      documento.add(Chunk.NEWLINE);
      documento.add(Chunk.NEWLINE);
      documento.add(Chunk.NEWLINE);
      documento.add(Chunk.NEWLINE);
      documento.add(Chunk.NEWLINE);

      // responsables
      String[] responsables = new String[]{"Fabianna Janeht Gonzales Saavedra"};

      // cargos
      String[] cargos = new String[]{"Dirección General"};

      PdfPTable tablaAutorizacion = new PdfPTable(responsables.length);
      PdfPCell cellFirma = new PdfPCell(new Phrase("_________________________________"));
      cellFirma.setBorder(Rectangle.NO_BORDER);
      PdfPCell cellResponsable = null;
      PdfPCell cellCargo = null;

      for (int i = 0; i < responsables.length; i++) {
        // firma
        tablaAutorizacion.addCell(cellFirma);

        // responsables
        cellResponsable = new PdfPCell(new Phrase(responsables[i], new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        cellResponsable.setBorder(Rectangle.NO_BORDER);
        tablaAutorizacion.addCell(cellResponsable);

        // cargos
        cellCargo = new PdfPCell(new Phrase(cargos[i], new Font(Font.FontFamily.HELVETICA, 12)));
        cellCargo.setBorder(Rectangle.NO_BORDER);
        tablaAutorizacion.addCell(cellCargo);
      }

      documento.add(tablaAutorizacion);

      documento.close();

    } catch (DocumentException e) {
      throw new IOException(e.getMessage());
    }
  }

  public PdfPCell createCell(String content, float borderWidth, int colspan, int alignment, int textStyle) {
    Font f = new Font(Font.FontFamily.HELVETICA, 7, textStyle, GrayColor.BLACK);
    PdfPCell cell = new PdfPCell(new Phrase(content, f));
    cell.setBorderWidth(borderWidth);
    cell.setColspan(colspan);
    cell.setHorizontalAlignment(alignment);
    return cell;
  }

  class MyFooter extends PdfPageEventHelper {

    Font ffont = new Font(Font.FontFamily.COURIER, 5, Font.BOLD);

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
//      String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
      String datetime = DateTimeServer.getDateTimeServer();
      PdfContentByte cb = writer.getDirectContent();
      Phrase footer = new Phrase(datetime, ffont);
      ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
              footer,
              (document.right() - document.left()) / 2 + document.leftMargin(),
              document.bottom() - 1, 0);
    }
  }
}
