<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="templates/validar.jsp"%>
<!DOCTYPE html>
<html>
  <head>
    <!--Estilos plantilla-->
    <%@include file="templates/header.jsp" %>
    <!--Estilos plantilla-->

    <!--Estilos propios-->
    <link href="../js/lib/jquery-confirm-master/jquery-confirm.min.css" rel="stylesheet" type="text/css"/>
    <!--Estilos propios-->
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
        ************************* PANEL DE BUSQUEDA DE FICHAS ************************* 
    -->

    <form action="../FichaServlet" method="post" id="formImprimirFicha" target="_blank">
      <input type="hidden" name="accion" value="imprimirFicha" />
      <input type="hidden" name="params" id="params"/>
    </form>

    <form action="../FichaServlet" method="post" id="formImprimirActividadFicha" target="_blank">
      <input type="hidden" name="accion" value="imprimirActividadFicha" />
      <input type="hidden" name="paramsActividadFicha" id="paramsActividadFicha"/>
    </form>

    <div class="row">
      <div class="col-md-6 col-md-offset-3">
        <div class="panel so-card-2">
          <div class="panel-heading bg-blue-800">
            <h6 class="panel-title text-semibold">
              <i class="fa fa-search"></i>&nbsp;&nbsp;Buscar fichas
            </h6>
            <div class="heading-elements">
              <button type="button" class="btn btn-info btn-ladda btn-ladda-progress" data-style="expand-right" data-spinner-size="20" id="btnLimpiarFiltroFichas"><span class="ladda-label"><i class="fa fa-filter fa-lg"></i> Limpiar filtro</span><span class="ladda-spinner"></span><div class="ladda-progress" style="width: 158px;"></div></button>
            </div>
          </div>
          <div class="panel-body">
            <form action="#" method="post" id="formBuscarFichas" name="formBuscarFichas">
              <div class="row">
                <div class="col-md-4">
                  <div class="form-group">
                    <label for="cboCriterioBusqueda" class="text-semibold">Criterio <span class="text-danger">*</span></label>
                    <select class="select" id="cboCriterioBusqueda" name="cboCriterioBusqueda">
                      <option value="0">[SELECCIONAR]</option>
                      <option value="1">TIPO DE DOCUMENTO</option>
                      <option value="2">APELLIDOS</option>
                      <option value="3">FECHA DE REGISTRO</option>
                      <option value="4">ESTADO</option>
                    </select>
                  </div>
                </div>
                <div id="divRowFiltro1"></div>
                <div id="divRowFiltro2"></div>
              </div>
              <div class="row">
                <div class="col-md-12 text-danger text-right text-semibold">
                  (*) Campos obligatorios
                </div>
              </div>
              <div class="row">
                <div class="col-md-12 text-center">
                  <button type="submit" class="btn btn-success text-center" id="btnBuscarFichas"><i class="fa fa-search fa-lg position-left"></i> Buscar fichas</button>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
    <!--
        ************************* PANEL DE BUSQUEDA DE FICHAS ************************* 
    -->



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
          </div>
          <div class="panel-body">
            <div class="row">
              <table class="table table-bordered table-striped table-framed table-xxs" id="tblFichas">
                <thead class="text-primary-800" style="background-color: #EEEEEE;">
                  <tr>
                    <th>#</th>
                    <th>Tipo doc.</th>
                    <th>Nro doc.</th>
                    <th>Personal</th>
                    <th>Correo</th>
                    <th>Fecha registro</th>
                    <th>Estado actual</th>
                    <th>Accion</th>
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



    <!--
        ************************* PANEL LEYENDA DE FICHAS ************************* 
    -->
    <!--        <div class="row">
                <div class="col-md-6 col-md-offset-3">
                    <div class="panel so-card-2">
                        <div class="panel-heading bg-blue-800">
                            <h6 class="panel-title text-semibold">
                                <i class="fa fa-info-circle fa-lg"></i>&nbsp;&nbsp;Leyenda
                            </h6>
                            <div class="heading-elements">
                                <ul class="icons-list">
                                    <li><a data-action="collapse" class=""></a></li>
                                </ul>
                            </div>
                        </div>
                        <div class="panel-body" style="display: none">
                            
                        </div>
                    </div>
                </div>
            </div>-->
    <!--
        ************************* PANEL LEYENDA DE FICHAS ************************* 
    -->


    <!--
       ************************* MDOAL LISTADO DE DETALLE DE ESTADO FICHA ************************* 
    -->
    <div id="modalListadoDetalleEstadoFicha" class="modal fade">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header bg-blue-800">
            <button type="button" class="close" data-dismiss="modal">&times;</button>
            <h6 class="modal-title"><i class="fa fa-file fa-lg"></i>&nbsp;&nbsp;Actividades de la ficha</h6>
          </div>
          <div class="modal-body">
            <ul class="media-list media-list-bordered" id="listadoActividades">
            </ul>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
            <button type="button" class="btn bg-slate-600 pull-left" id="btnImprimirActividadFicha"><i class="fa fa-print fa-lg"></i> Imprimir</button>
          </div>
        </div>
      </div>
    </div>
    <!--
       ************************* MDOAL LISTADO DE DETALLE DE ESTADO FICHA ************************* 
    -->

    <!--javascript plantilla-->
    <%@include file="templates/footer-body.jsp" %>
    <!--javascript plantilla-->

    <!--select2-->
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/selects/select2.min.js"></script>

    <!--validInput-->
    <script type="text/javascript" src="../plantilla/assets/js/plugins/validateInput/validate_inputs.js"></script>

    <!--datepicker-->
    <script type="text/javascript" src="../plantilla/assets/js/core/libraries/jquery_ui/widgets.min.js"></script>

    <!--validate-->
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/validation/validate.min.js"></script>
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/validation/additional_methods.min.js"></script>
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/validation/localization/messages_es_PE.js"></script>
    <script type="text/javascript" src="../js/pages/newRulesValidate.js"></script>

    <!--datatable-->
    <script type="text/javascript" src="../plantilla/assets/js/plugins/tables/datatables/datatables.min.js"></script>
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/selects/select2.min.js"></script>
    <script type="text/javascript" src="../plantilla/assets/js/plugins/tables/datatables/extensions/responsive.min.js"></script>

    <!--button spinner-->
    <script type="text/javascript" src="../plantilla/assets/js/plugins/buttons/spin.min.js"></script>
    <script type="text/javascript" src="../plantilla/assets/js/plugins/buttons/ladda.min.js"></script>

    <script type="text/javascript" src="../js/lib/jquery-confirm-master/jquery-confirm.min.js"></script>

    <!--javascript propios-->
    <script src="../js/pages/general.js" type="text/javascript"></script>
    <script src="../js/pages/menu.js" type="text/javascript"></script>
    <script src="../js/pages/consultaFichas.js" type="text/javascript"></script>

    <!--javascript propios-->

  </body>
</html>
<%}%>