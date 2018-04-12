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
import trismegistoplanilla.beans.LoteFichaBean;
import trismegistoplanilla.services.DetalleLoteFichaDocenteService;
import trismegistoplanilla.utilities.DateTimeServer;

public class DetalleLoteFichaDocenteServlet extends HttpServlet {

  private static final long serialVersionUID = 7001760050909270213L;

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String param = request.getParameter("accion");
    switch (param) {
      case "listarDetalleLoteFichaDocenteDT":
        listarDetalleLoteFichaDocenteDT(request, response);
        break;
      case "imprimirLote":
        imprimirLote(request, response);
        break;
      case "registrarSueldosPresidenciaLoteDocente":
        registrarSueldosPresidenciaLoteDocente(request, response);
        break;
      default:
        break;
    }
  }

  private void listarDetalleLoteFichaDocenteDT(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    DetalleLoteFichaDocenteService service = new DetalleLoteFichaDocenteService();
    LoteFichaBean loteFicha = new LoteFichaBean();
    int codigoLote = Integer.parseInt(request.getParameter("codigoFichaLote"));
    System.out.println("CODIGO LOTE >>>>>>>>> " + codigoLote);
    loteFicha.setCodigoFichaLote(codigoLote);
    JSONObject jsonObjListarDetalleLoteFichaDocente = service.listarDetalleLoteFichaDocenteDT(loteFicha);
    out.print(jsonObjListarDetalleLoteFichaDocente);
  }

  private void imprimirLote(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession sessionPDF = request.getSession();
    String mensaje = "";
    if (request.getParameter("numeroLote") == null) {
      mensaje = "Error, numeroLote es null";
      sessionPDF.setAttribute("errorTokenFicha", mensaje);
      response.sendRedirect("vistas/response/ErrorToken.jsp");
    } else if (request.getParameter("numeroFilas") == null) {
      mensaje = "Error, numeroFilas es null";
      sessionPDF.setAttribute("errorTokenFicha", mensaje);
      response.sendRedirect("vistas/response/ErrorToken.jsp");
    } else if (request.getParameter("data") == null) {
      mensaje = "Error, data (json) es null";
      sessionPDF.setAttribute("errorTokenFicha", mensaje);
      response.sendRedirect("vistas/response/ErrorToken.jsp");
    } else {
      String numeroLote = request.getParameter("numeroLote");
      int numeroFilas = Integer.parseInt(request.getParameter("numeroFilas"));
      String json = request.getParameter("data");
      JSONArray jsonResponse = new JSONArray(json);
      String fileName = "sueldoDocentes" + numeroLote + ".pdf";
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
        Paragraph titulo = new Paragraph("COSTO HORA / HABER MENSUAL DOCENTE",
                FontFactory.getFont("arial", 22, Font.UNDERLINE, BaseColor.BLACK));
        titulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(titulo);

        // lote
        Paragraph lote = new Paragraph("N° LOTE " + numeroLote,
                FontFactory.getFont("arial", 22, Font.BOLD, BaseColor.BLACK));
        lote.setAlignment(Element.ALIGN_CENTER);
        documento.add(lote);

        // salto de linea
        // documento.add(Chunk.NEWLINE);
        documento.add(new Phrase(""));

        // cabecera
        PdfPTable tablaData = new PdfPTable(10);
        // primera fila (costos colspan = 3)
        for (int i = 1; i <= 10; i++) {
          switch (i) {
            case 6:
              tablaData.addCell(createCell("COSTOS (S/.)", 0.5f, 4, Element.ALIGN_CENTER, Font.BOLD));
              break;
            case 7:
            case 8:
            case 9:
              break;
            default:
              tablaData.addCell(createCell("", 0, 1, Element.ALIGN_CENTER, Font.BOLD));
              break;
          }
        }
        tablaData.setWidths(new float[]{0.5f, 4, 1.5f, 2.1f, 1.5f, 1.2f, 0.7f, 0.7f, 0.7f, 5});
        tablaData.setWidthPercentage(100);
        tablaData.addCell(createCell("N°", 0.5f, 1, Element.ALIGN_CENTER, Font.BOLD));
        tablaData.addCell(createCell("APELLIDOS Y NOMBRES", 0.5f, 1, Element.ALIGN_LEFT, Font.BOLD));
        tablaData.addCell(createCell("TIPO DOC.", 0.5f, 1, Element.ALIGN_LEFT, Font.BOLD));
        tablaData.addCell(createCell("NRO DOC.", 0.5f, 1, Element.ALIGN_LEFT, Font.BOLD));
        tablaData.addCell(createCell("INICIO", 0.5f, 1, Element.ALIGN_CENTER, Font.BOLD));
        tablaData.addCell(createCell("MENSUAL", 0.5f, 1, Element.ALIGN_CENTER, Font.BOLD));
        tablaData.addCell(createCell("A", 0.5f, 1, Element.ALIGN_CENTER, Font.BOLD));
        tablaData.addCell(createCell("B", 0.5f, 1, Element.ALIGN_CENTER, Font.BOLD));
        tablaData.addCell(createCell("C", 0.5f, 1, Element.ALIGN_CENTER, Font.BOLD));
        tablaData.addCell(createCell("OBSERVACIÓN", 0.5f, 1, Element.ALIGN_CENTER, Font.BOLD));

        // data
        int inicio = 1, limit = 10;
        int fin = limit * numeroFilas, count = 0, multiplicador = 1;
        int countJSON = 0;

        for (int i = inicio; i <= fin; i++) {
          if (countJSON < numeroFilas) {
            JSONObject data = jsonResponse.getJSONObject(countJSON);
            // columns
            tablaData.addCell(createCell("" + i, 0.5f, 1, Element.ALIGN_CENTER, Font.NORMAL));
            tablaData.addCell(createCell(data.getString("apellidoPaterno") + " " + data.getString("apellidoMaterno") + ", " + data.getString("nombre"), 0.5f, 1, Element.ALIGN_LEFT, Font.NORMAL));
            tablaData.addCell(createCell(data.getString("tipoDocumento"), 0.5f, 1, Element.ALIGN_LEFT, Font.NORMAL));
            tablaData.addCell(createCell(data.getString("numeroDocumento"), 0.5f, 1, Element.ALIGN_LEFT, Font.NORMAL));
            tablaData.addCell(createCell(data.getString("mes"), 0.5f, 1, Element.ALIGN_CENTER, Font.NORMAL));
            tablaData.addCell(createCell(data.getString("costoMensual"), 0.5f, 1, Element.ALIGN_CENTER, Font.NORMAL));
            tablaData.addCell(createCell(data.getString("costoa"), 0.5f, 1, Element.ALIGN_CENTER, Font.NORMAL));
            tablaData.addCell(createCell(data.getString("costob"), 0.5f, 1, Element.ALIGN_CENTER, Font.NORMAL));
            tablaData.addCell(createCell(data.getString("costoc"), 0.5f, 1, Element.ALIGN_CENTER, Font.NORMAL));
            tablaData.addCell("");
            countJSON++;
          }

          if (count == limit) {
            multiplicador = multiplicador + 1;
            count = 0;
          }

          count++;
        }

        documento.add(tablaData);

        // salto de linea
        documento.add(Chunk.NEWLINE);
        documento.add(Chunk.NEWLINE);
        documento.add(Chunk.NEWLINE);
        documento.add(Chunk.NEWLINE);
        documento.add(Chunk.NEWLINE);

        // responsables
        String[] responsables = new String[]{"Johan Hilares Piminchumo", "Wilmer Carrasco Beas", "Rosario Piñas Palacios"};

        // cargos
        String[] cargos = new String[]{"Coordinador Académico General", "Presidente de la A.C.E.S.O", "Directora Académica General"};

        PdfPTable tablaAutorizacion = new PdfPTable(responsables.length);
        PdfPCell cellFirma = new PdfPCell(new Phrase("______________________________"));
        cellFirma.setBorder(Rectangle.NO_BORDER);
        PdfPCell cellResponsable = null;
        PdfPCell cellCargo = null;
        tablaLogo.setWidthPercentage(100);

        int countResponsable = 0;
        int countCargo = 0;
        for (int i = 1; i <= responsables.length * responsables.length; i++) {
          // firmas
          if (i <= responsables.length) {
            tablaAutorizacion.addCell(cellFirma);
          }

          // repsonsables
          if (i > responsables.length && i <= responsables.length * 2) {
            cellResponsable = new PdfPCell(new Phrase(responsables[countResponsable]));
            cellResponsable.setBorder(Rectangle.NO_BORDER);
            tablaAutorizacion.addCell(cellResponsable);
            countResponsable++;
          }

          // cargos
          if (i > responsables.length * 2 && i <= responsables.length * 3) {
            cellCargo = new PdfPCell(new Phrase(cargos[countCargo]));
            cellCargo.setBorder(Rectangle.NO_BORDER);
            tablaAutorizacion.addCell(cellCargo);
            countCargo++;
          }
        }
        documento.add(tablaAutorizacion);

        // footer datetime
        documento.close();

      } catch (DocumentException e) {
        throw new IOException(e.getMessage());
      }
    }
  }

  private void registrarSueldosPresidenciaLoteDocente(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    HttpSession sesion = request.getSession();
    DetalleLoteFichaDocenteService service = new DetalleLoteFichaDocenteService();
    String json = request.getParameter("json");
    JSONObject jsonObj = new JSONObject(json);
    UsuarioBean usuario = (UsuarioBean) sesion.getAttribute("usuario");
    jsonObj.put("usuario", usuario.getCodigoUsuario());
    JSONObject respuesta = service.registrarSueldosPresidenciaLoteDocente(jsonObj);
    System.out.println("RESPUESTA DEL SERVIDOR >>>>>>>>>> " + respuesta);
    out.print(respuesta);
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
