<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="templates/validar.jsp"%>
<!DOCTYPE html>
<html>
  <head>
    <!--Estilos plantilla-->
    <%@include file="templates/header.jsp" %>
    <!--Estilos plantilla-->

    <link href="../js/lib/jquery-confirm-master/jquery-confirm.min.css" rel="stylesheet" type="text/css"/>

  </head>
  <body onload="cargar();">

    <input id="codTitulo" name="codTitulo" type="hidden" value="<%= menu != null ? menu.getCodigoTitulo() : 0%>">
    <input id="codModulo" type="hidden" value="<%= menu != null ? menu.getCodigoModulo() : 0%>">
    <input id="codCategoria" type="hidden" value="<%= menu != null ? menu.getCodigoCategoria() : 0%>">
    <input id="codSubCategoria" type="hidden" value="<%= menu != null ? menu.getCodigoSubcategoria() : 0%>">

    <!--navbar - sidebar-->
    <%@include file="templates/header-body.jsp" %>
    <!--navbar - sidebar-->

    <!--breadcrumb-->
    <%@include file="templates/page-header.jsp" %>
    <!--breadcrumb-->

    <!--
        ************************* PANEL DE LISTADO DE FICHAS ************************* 
    -->
    <div class="row">
      <div class="col-md-12">
        <div class="panel so-card-2">
          <div class="panel-heading bg-blue-800">
            <h6 class="panel-title text-semibold">
              <i class="fa fa-file"></i>&nbsp;&nbsp;Listado de fichas
            </h6>
            <div class="heading-elements">
              <button type="button" style="width: 180px;font-size: 14px;font-weight: bold;" class="btn btn-info text-center" id="btnGenerarLote"><i class="fa fa-folder-open fa-lg position-left"></i> REVISAR</button>
            </div>
          </div>
          <div class="panel-body">
            <div class="row">
              <table class="table table-bordered table-striped table-framed table-xxs" id="tblFichasPendienteAprobacion">
                <thead class="text-primary-800" style="background-color: #EEEEEE;">
                  <tr>
                    <th>#</th>
                    <th>APELLIDOS Y NOMBRES</th>
                    <th>TIPO DOCUMENTO</th>
                    <th>NRO DOCUMENTO</th>
                    <th>NACIONALIDAD</th>
                    <th>FECHA INGRESO</th>
                    <th>AREA</th>
                    <th>CARGO</th>
                    <th>ESTADO</th>
                    <th>ACCIONES</th>
                  </tr>
                </thead>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!--
        ************************* PANEL DE LISTADO DE FICHAS ************************* 
    -->        



    <!--javascript plantilla-->
    <%@include file="templates/footer-body.jsp" %>
    <!--javascript plantilla-->

    <!--datatable-->
    <script type="text/javascript" src="../plantilla/assets/js/plugins/tables/datatables/datatables.min.js"></script>
    <!--datatable-->

    <!--javascript propios-->
    <script type="text/javascript" src="../js/lib/jquery-confirm-master/jquery-confirm.min.js"></script>
    <script src="../js/pages/general.js" type="text/javascript"></script>
    <script src="../js/pages/menu.js" type="text/javascript"></script>
    <script src="../js/pages/aprobacionFichas.js" type="text/javascript"></script>
    <!--javascript propios-->

  </body>
</html>
<%}%>