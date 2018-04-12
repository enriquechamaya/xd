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

    <style>
      .validation-error-label, .validation-valid-label{
        display: inline-block;
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

    <!-- ------------------------------------------------------------------------------------------------------ -->
    <!-- ------------------------------- Panel de busqueda de fichas pendientes ------------------------------- -->
    <!-- ------------------------------------------------------------------------------------------------------ -->

    <div class="row">
      <div class="col-md-4 col-md-offset-4">
        <div class="panel so-card-2">
          <div class="panel-heading bg-blue-800">
            <h6 class="panel-title text-semibold">
              <i class="fa fa-search"></i>&nbsp;&nbsp;Filtro de lotes
            </h6>
            <div class="heading-elements">
              <button type="button" class="btn btn-info btn-ladda btn-ladda-progress" data-style="expand-right" data-spinner-size="20" id="btnLimpiarFiltroLotes"><span class="ladda-label">Limpiar filtro</span><span class="ladda-spinner"></span><div class="ladda-progress" style="width: 158px;"></div></button>
            </div>
          </div>
          <div class="panel-body">
            <form action="#" method="post" id="formFiltroLotes" name="formFiltroLotes">
              <div class="row">
                <div class="col-md-12">
                  <div class="form-group">
                    <label for="txtNumeroLote" class="text-semibold">Número de lote <span class="text-danger">*</span></label>
                    <input type="text" class="form-control text-uppercase" placeholder="Ingresar número de lote" id="txtNumeroLote" name="txtNumeroLote" maxlength="15" autocomplete="off">
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="col-md-12 text-danger text-right text-semibold">
                  (*) Campos obligatorios
                </div>
              </div>
              <div class="row">
                <div class="col-md-4 col-md-offset-4 text-center">
                  <button type="submit" class="btn btn-success btn-sm"><i class="fa fa-search fa-lg position-left"></i> Filtrar</button>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>

      <div class="col-md-8 col-md-offset-2">
        <div class="panel so-card-2">
          <div class="panel-heading bg-blue-800">
            <h6 class="panel-title text-semibold">
              <i class="fa fa-file"></i>&nbsp;&nbsp;Listado de lotes
              <a class="heading-elements-toggle"><i class="icon-more"></i></a>
            </h6>
            <div class="heading-elements">
              <!--              <button type="button" class="btn btn-success btn-sm" id="btnGenerarLote">
                              <i class="fa fa-linux fa-lg position-left"></i> Generar lote
                            </button>-->
            </div>
          </div>
          <div class="panel-body">
            <div class="row">
              <table class="table table-bordered table-striped table-framed table-xxs" id="tblLotes">
                <thead class="text-primary-800" style="background-color: #EEEEEE;" >
                  <tr>
                    <th>#</th>
                    <th>NUMERO DE LOTE</th>
                    <th>FECHA REGISTRO</th>
                    <th>N° DE FICHAS</th>
                    <th>ESTADO DE LOTE</th>
                    <th>ACCIONES</th>
                  </tr>
                </thead>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!--contenido-->


    <!--javascript plantilla-->
    <%@include file="templates/footer-body.jsp" %>

    <!--datatable-->
    <script type="text/javascript" src="../plantilla/assets/js/plugins/tables/datatables/datatables.min.js"></script>
    <script type="text/javascript" src="../plantilla/assets/js/plugins/tables/datatables/extensions/responsive.min.js"></script>
    <!--datatable-->

    <!--checkbox-->
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/styling/switch.min.js"></script>
    <!--checkbox-->

    <!--boostrap select-->
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/selects/bootstrap_select.min.js"></script>
    <!--boostrap select-->

    <!--validate-->
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/validation/validate.min.js"></script>
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/validation/additional_methods.min.js"></script>
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/validation/localization/messages_es_PE.js"></script>
    <script type="text/javascript" src="../js/pages/newRulesValidate.js"></script>
    <!--validate-->

    <!--jqueryconfirm-->
    <script src="../js/lib/jquery-confirm-master/jquery-confirm.min.js" type="text/javascript"></script>
    <!--jqueryconfirm-->

    <script type="text/javascript" src="../plantilla/assets/js/plugins/buttons/spin.min.js"></script>
    <script type="text/javascript" src="../plantilla/assets/js/plugins/buttons/ladda.min.js"></script>

    <!--datepicker-->
    <script type="text/javascript" src="../plantilla/assets/js/core/libraries/jquery_ui/widgets.min.js"></script>
    <!--datepicker-->

    <!--javascript general-->
    <script src="../js/pages/general.js" type="text/javascript"></script>
    <!--javascript general-->

    <!--javascript propios-->
    <script src="../js/pages/menu.js" type="text/javascript"></script>
    <!--javascript propios-->

    <script src="../js/pages/lotesFicha.js" type="text/javascript"></script>

  </body>
</html>
<%    }
%>
