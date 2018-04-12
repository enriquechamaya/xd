<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="templates/validar.jsp"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <!--Estilos plantilla-->
    <%@include file="templates/header.jsp" %>
    <!--Estilos plantilla-->

    <!--Estilos propios-->
    <link href="../js/lib/jquery-confirm-master/jquery-confirm.min.css" rel="stylesheet" type="text/css"/>
    <!--Estilos propios-->
    <link href="../css/pages/general.css" rel="stylesheet" type="text/css"/>
    <style>
      a.editable.editable-click::before {
        content: 'S/. ';
        /* border-bottom: 2px solid; */
        color: black;
        font-weight: bold;
      }

      .tp-error-input-observacion{
        border: 1px solid red;
      }

      .tp-success-input-observacion{
        border: 1px solid green;
      }

    </style>
  </head>

  <body onload="cargar();">

    <input id="codTitulo" type="hidden" value="<%= menu != null ? menu.getCodigoTitulo() : 0%>">
    <input id="codModulo" type="hidden" value="<%= menu != null ? menu.getCodigoModulo() : 0%>">
    <input id="codCategoria" type="hidden" value="<%= menu != null ? menu.getCodigoCategoria() : 0%>">
    <input id="codSubCategoria" type="hidden" value="<%= menu != null ? menu.getCodigoSubcategoria() : 0%>">

    <!--navbar - sidebar-->
    <%@include file="templates/header-body.jsp" %>
    <!--navbar - sidebar-->

    <%@include file="templates/page-header.jsp" %>


    <!--contenido-->
    <form action="../DetalleLoteFichaDocenteServlet" method="post" id="formPDF" target="_blank">
      <input type="hidden" name="accion" value="imprimirLote" />
      <input type="hidden" name="numeroLote" id="numeroLote" value="" />
      <input type="hidden" name="numeroFilas" id="numeroFilas" value="" />
      <input type="hidden" name="data" id="data" value="" />
    </form>

    <div class="row">
      <div class="col-md-4 col-md-offset-4">
        <div class="panel-heading bg-blue-800">
          <h6 class="panel-title text-semibold">
            <i class="icon-folder-open"></i>&nbsp;&nbsp;Lote
          </h6>
        </div>
        <div class="panel panel-body no-border-top no-border-radius-top">
          <div class="form-group mt-5">
            <label class="text-semibold">Número de lote:</label>
            <span id="lblNumeroLote" class="pull-right-sm text-bold"></span>
          </div>

          <div class="form-group">
            <label class="text-semibold">Fecha de registro:</label>
            <span id="lblFechaRegistro" class="pull-right-sm text-bold"></span>
          </div>
          <div id="divLote">
          </div>
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col-md-12">
        <div class="panel so-card-2">
          <div class="panel-heading bg-blue-800">
            <h6 class="panel-title text-semibold">
              <i class="fa fa-file"></i>&nbsp;&nbsp;Listado de fichas
              <a class="heading-elements-toggle"><i class="icon-more"></i></a>
            </h6>
            <div class="heading-elements">
              <!--              <button type="button" class="btn btn-success btn-lg" id="btnImprimir">
                              <i class="fa fa-print fa-lg position-left"></i> Imprimir
                            </button>-->
            </div>
          </div>
          <div class="panel-body">
            <div class="row" id="divTblFicha">
              <table class="table table-bordered table-striped table-framed table-xxs" id="tblFichas">

              </table>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!--contenido-->


    <!--javascript plantilla-->
    <%@include file="templates/footer-body.jsp" %>

    <!--editable form-->
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/editable/editable.min.js"></script>
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/inputs/touchspin.min.js"></script>
    <!--editable form-->

    <!--datatable-->
    <script type="text/javascript" src="../plantilla/assets/js/plugins/tables/datatables/datatables.min.js"></script>
    <script type="text/javascript" src="../plantilla/assets/js/plugins/tables/datatables/extensions/responsive.min.js"></script>
    <!--datatable-->

    <script src="../js/lib/jquery-confirm-master/jquery-confirm.min.js" type="text/javascript"></script>

    <!--javascript general-->
    <script src="../js/pages/general.js" type="text/javascript"></script>
    <!--javascript general-->

    <!--javascript propios-->
    <script src="../js/pages/menu.js" type="text/javascript"></script>
    <!--javascript propios-->

    <script src="../js/pages/detalleConsultaLotesGeneral.js" type="text/javascript"></script>

  </body>
</html>
<%    }
%>
