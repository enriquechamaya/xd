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


  </head>
  <body onload="cargar();">

    <input id="codTitulo" type="hidden" value="<%= menu != null ? menu.getCodigoTitulo() : "0"%>">
    <input id="codModulo" type="hidden" value="<%= menu != null ? menu.getCodigoModulo() : "0"%>">
    <input id="codCategoria" type="hidden" value="<%= menu != null ? menu.getCodigoCategoria() : "0"%>">
    <input id="codSubCategoria" type="hidden" value="<%= menu != null ? menu.getCodigoSubcategoria() : "0"%>">

    <!--navbar - sidebar-->
    <%@include file="templates/header-body.jsp" %>
    <!--navbar - sidebar-->

    <%@include file="templates/page-header.jsp" %>


    <!--contenido-->

    <!-- ------------------------------------------------------------------------------------------------------ -->
    <!-- ------------------------------- Panel de busqueda de fichas pendientes ------------------------------- -->
    <!-- ------------------------------------------------------------------------------------------------------ -->

    <div class="row">
      <div class="col-md-10 col-md-offset-1">
        <div class="panel so-card-2">
          <div class="panel-heading bg-blue-800">
            <h6 class="panel-title text-semibold">
              <i class="fa fa-search"></i>&nbsp;&nbsp;Filtrar por ficha
            </h6>
          </div>
          <div class="panel-body">
            <form action="#" method="post" id="formFiltroFichas" name="formFiltroFichas">
              <div class="row">
                <div class="col-md-3" id="divCriterioFiltroFicha">
                  <div class="form-group">
                    <label for="cboCriterioFiltroFicha" class="text-semibold">Criterio <span class="text-danger">*</span></label>
                    <select class="bootstrap-select" data-width="100%" id="cboCriterioFiltroFicha" name="cboCriterioFiltroFicha">
                      <option value="0">[TODOS]</option>
                      <option value="1">ADMINISTRATIVO</option>
                      <option value="2">DOCENTE SECUNDARIA</option>
                    </select>
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
                  <button type="button" class="btn btn-success btn-sm" id="btnFiltrarFichas"><i class="fa fa-search fa-lg position-left"></i> Filtrar</button>
                  <!--<button type="button" class="btn bg-orange-400 btn-sm" id="btnLimpiarFiltrosFichaPendiente"><i class="fa fa-hand-paper-o fa-lg position-left"></i> Limpiar búsqueda</button>-->
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col-md-12">
        <div class="panel so-card-2">
          <div class="panel-heading bg-blue-800">
            <h6 class="panel-title text-semibold">
              <i class="fa fa-file"></i>&nbsp;&nbsp;Listado de fichas administrativas
              <a class="heading-elements-toggle"><i class="icon-more"></i></a>
            </h6>
            <div class="heading-elements">
              <button type="button" class="btn btn-success btn-sm" id="btnGenerarLote">
                <i class="fa fa-linux fa-lg position-left"></i> Generar lote
              </button>
            </div>
          </div>
          <div class="panel-body">
            <div class="row">
              <table class="table table-bordered table-striped table-framed table-xxs" id="tblFichasPendientes">
                <thead class="text-primary-800" style="background-color: #EEEEEE;" >
                  <tr>
                    <th rowspan="2" style="width: 10px;"><input type="checkbox" class="control-primary checkbox-head"></th>
                    <th rowspan="2">APELLIDOS Y NOMBRES</th>
                    <th rowspan="2">NRO DOCUMENTO</th>
                    <th rowspan="2">SEDE</th>
                    <th rowspan="2">AREA</th>
                    <th rowspan="2">CARGO</th>
                    <th style="text-align: center;" colspan="4">COSTOS</th>
                  </tr>
                  <tr>
                    <th>Mensual</th>
                    <th>Pago A</th>
                    <th>Pago B</th>
                    <th>Pago C</th>
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
    <!--<script type="text/javascript" src="../plantilla/assets/js/plugins/forms/selects/select2.min.js"></script>-->
    <!--checkbox-->
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/styling/switch.min.js"></script>
    <!--checkbox-->

    <script type="text/javascript" src="../plantilla/assets/js/plugins/tables/datatables/extensions/responsive.min.js"></script>
    <!--datatable-->


    <!--select-->
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/selects/bootstrap_select.min.js"></script>

    <!--jqueryconfirm-->
    <script src="../js/lib/jquery-confirm-master/jquery-confirm.min.js" type="text/javascript"></script>
    <!--jqueryconfirm-->

    <!--javascript general-->
    <script src="../js/pages/general.js" type="text/javascript"></script>
    <!--javascript general-->

    <!--javascript propios-->
    <script src="../js/pages/menu.js" type="text/javascript"></script>
    <!--javascript propios-->
    <script src="../js/pages/bandejaPresidencia.js" type="text/javascript"></script>

  </body>
</html>
<%    }
%>
