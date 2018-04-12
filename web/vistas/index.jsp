<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="templates/validar.jsp"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <!--Estilos plantilla-->
    <%@include file="templates/header.jsp" %>
    <!--Estilos plantilla-->

    <!--Estilos propios-->
    <link href="../css/pages/index.css" rel="stylesheet" type="text/css"/>
    <link href="../js/lib/jquery-confirm-master/jquery-confirm.min.css" rel="stylesheet" type="text/css"/>
    <!--Estilos propios-->


  </head>

  <body onload="cargar();">

    <input id="codTitulo" name="codTitulo" type="hidden" value="<%= menu != null ? menu.getCodigoTitulo() : 0%>">
    <input id="codModulo" type="hidden" value="<%= menu != null ? menu.getCodigoModulo() : 0%>">
    <input id="codCategoria" type="hidden" value="<%= menu != null ? menu.getCodigoCategoria() : 0%>">
    <input id="codSubCategoria" type="hidden" value="<%= menu != null ? menu.getCodigoSubcategoria() : 0%>">

    <input type="hidden" id="defaultMail" name="defaultMail" value=""/>


    <!--navbar - sidebar-->
    <%@include file="templates/header-body.jsp" %>
    <!--navbar - sidebar-->

    <%@include file="templates/page-header.jsp" %>


    <!--contenido-->

    <!-- ------------------------------------------------------------------------------------------------------ -->
    <!-- ------------------------------- Panel de busqueda de fichas pendientes ------------------------------- -->
    <!-- ------------------------------------------------------------------------------------------------------ -->

    <div class="row">
      <div class="col-md-6 col-md-offset-3">
        <div class="panel so-card-2">
          <div class="panel-heading bg-blue-800">
            <h6 class="panel-title text-semibold">
              <i class="fa fa-search"></i>&nbsp;&nbsp;Buscar fichas pendientes
            </h6>
            <div class="heading-elements">
              <!--<button type="button" class="btn btn-info btn-ladda-progress"  id="btnLimpiarFiltroFichaPendiente"><i class="fa fa-refresh fa-lg position-left"></i> Limpiar filtro</button>-->
              <button type="button" class="btn btn-info btn-ladda btn-ladda-progress" data-style="expand-right" data-spinner-size="20" id="btnLimpiarFiltroFichaPendiente"><span class="ladda-label">Limpiar filtro</span><span class="ladda-spinner"></span><div class="ladda-progress" style="width: 158px;"></div></button>
            </div>
          </div>
          <div class="panel-body">
            <form action="#" method="post" id="formBusquedaFichaPendiente" name="formBusquedaFichaPendiente">
              <div class="row">
                <div class="col-md-4">
                  <div class="form-group">
                    <label for="cboCriterioBusqueda" class="text-semibold">Criterio <span class="text-danger">*</span></label>
                    <select class="select" id="cboCriterioBusqueda" name="cboCriterioBusqueda">
                      <option value="0">[SELECCIONAR]</option>
                      <option value="1">TIPO DE DOCUMENTO</option>
                      <option value="2">APELLIDOS</option>
                      <option value="3">FECHA DE REGISTRO</option>
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
                  <button type="button" class="btn btn-success text-center" id="btnBusquedaFichaPendiente"><i class="fa fa-search fa-lg position-left"></i> Buscar</button>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>

    <!-- ------------------------------------------------------------------------------------------------------ -->
    <!-- ------------------------------- Panel de busqueda de fichas pendientes ------------------------------- -->
    <!-- ------------------------------------------------------------------------------------------------------ -->




















    <!-- ------------------------------------------------------------------------------------------------------ -->
    <!-- ------------------------------- Panel de listado de fichas pendientes -------------------------------- -->
    <!-- ------------------------------------------------------------------------------------------------------ -->
    <div class="row">
      <div class="col-md-12">
        <div class="panel so-card-2">
          <div class="panel-heading bg-blue-800">
            <h6 class="panel-title text-semibold">
              <i class="fa fa-file"></i>&nbsp;&nbsp;Listado de fichas pendientes
              <a class="heading-elements-toggle"><i class="icon-more"></i></a>
            </h6>
            <div class="heading-elements">
              <button type="button" class="btn btn-success btn-sm" id="btnAbrirModalRegistrarNuevaFicha">
                <i class="fa fa-plus fa-lg position-left"></i> Nueva ficha
              </button>
            </div>
          </div>
          <div class="panel-body">
            <div class="row">
              <table class="table table-bordered table-striped table-framed table-xxs" id="tblListadoFichaPendiente">
                <thead class="text-primary-800" style="background-color: #EEEEEE;">
                  <tr>
                    <th>#</th>
                    <th>TIPO DOCUMENTO</th>
                    <th>NRO DOCUMENTO</th>
                    <th>APELLIDOS Y NOMBRES</th>
                    <th>FECHA DE REGISTRO</th>
                    <th>CORREO ELECTRÓNICO</th>
                    <th>ESTADO</th>
                    <th>ACCIÓN</th>
                  </tr>
                </thead>
                <tbody>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- ------------------------------------------------------------------------------------------------------ -->
    <!-- ------------------------------- Panel de listado de fichas pendientes -------------------------------- -->
    <!-- ------------------------------------------------------------------------------------------------------ -->




















    <!-- ------------------------------------------------------------------------------------------------------ -->
    <!-- ------------------------------------ Modal de registro de nueva ficha -------------------------------- -->
    <!-- ------------------------------------------------------------------------------------------------------ -->

    <!--Modal nueva ficha-->
    <div id="modalRegistrarNuevaFicha" class="modal fade">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header bg-blue-800">
            <h6 class="modal-title"><i class="fa fa-file fa-lg"></i>&nbsp;&nbsp;Nueva ficha</h6>
          </div>
          <div class="modal-body">
            <form action="#" method="post" id="formNuevaFicha" name="formNuevaFicha">
              <div class="row">
                <div class="col-md-6">
                  <div class="form-group">
                    <label for="cboTipoDocumentoNuevaFicha" class="text-semibold">Tipo de documento <span class="text-danger">*</span></label>
                    <select class="select" id="cboTipoDocumentoNuevaFicha" name="cboTipoDocumentoNuevaFicha">
                      <option value="0">[SELECCIONAR]</option>
                    </select>
                  </div>
                </div>
                <div class="col-md-6">
                  <div class="form-group" id="divNumeroDocumentoModalNuevaFicha">
                    <label for="txtNumeroDocumentoNuevaFicha" class="text-semibold">Número de documento <span class="text-danger">*</span></label>
                    <input type="text" class="form-control text-uppercase" placeholder="Número de documento" id="txtNumeroDocumentoNuevaFicha" name="txtNumeroDocumentoNuevaFicha">
                  </div>
                </div>
              </div>
              <div class="row" id="divRespuestaReniec"></div>
              <div class="row">
                <div class="col-md-12">
                  <div class="form-group">
                    <div class="row">
                      <div class="col-md-6">
                        <label for="txtCorreoElectronicoNuevaFicha" class="text-semibold">Correo electrónico <span class="text-danger">*</span></label>
                      </div>
                      <div class="col-md-6 text-right">
                        <div class="checkbox-right checkbox-switchery switchery-xs">
                          <label>
                            <input type="checkbox" class="switchery" checked="checked">
                            <span class="text-semibold">¿Tiene correo?</span>
                          </label>
                          <span class="text-black" id="statusCorreoText">SI</span>
                          <input type="hidden" name="statusCorreoValue" id="statusCorreoValue" value="0" />
                        </div>
                      </div>
                    </div>
                    <input type="text" class="form-control" placeholder="Correo electrónico" id="txtCorreoElectronicoNuevaFicha" name="txtCorreoElectronicoNuevaFicha" maxlength="50">
                    <span class="help-block text-orange" id="helpText"></span>
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="col-md-12 text-danger text-right text-semibold">
                  (*) Campos obligatorios
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-success" id="btnRegistrarNuevaFicha">Registrar</button>
            <button type="button" class="btn btn-danger" data-dismiss="modal" id="btnCancelarRegistarNuevaFicha">Cerrar</button>
          </div>
        </div>
      </div>
    </div>
    <!-- /Modal nueva ficha-->
    <!-- ------------------------------------------------------------------------------------------------------ -->
    <!-- ------------------------------------ Modal de registro de nueva ficha -------------------------------- -->
    <!-- ------------------------------------------------------------------------------------------------------ -->



    <!-- ------------------------------------------------------------------------------------------------------ -->
    <!-- ------------------------------------ Modal de editar ficha ------------------------------------------- -->
    <!-- ------------------------------------------------------------------------------------------------------ -->
    <!--Modal editar ficha-->
    <div id="modalEditarFicha" class="modal fade">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header bg-blue-800">
            <h6 class="modal-title"><i class="fa fa-pencil fa-lg"></i>&nbsp;&nbsp;Editar ficha</h6>
          </div>
          <div class="modal-body">
            <form action="#" method="post" id="formEditarFicha" name="formEditarFicha">
              <div class="row">
                <div class="col-md-12">
                  <div class="alert alert-warning alert-styled-left">
                    <span class="text-semibold">¡ADVERTENCIA!</span>
                    <p class="text-size-mini">Al actualizar pasará lo siguiente:</p>
                    <ul>
                      <li class="text-size-mini">Se desactivará el código de verificación de ficha enviado anteriormente.</li>
                      <li class="text-size-mini">Se enviará un nuevo código de verificación al correo indicado.</li>
                    </ul>
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="col-md-6">
                  <div class="form-group">
                    <label for="cboTipoDocumentoEditarFicha" class="text-semibold">Tipo de documento <span class="text-danger">*</span></label>
                    <select class="select" id="cboTipoDocumentoEditarFicha" name="cboTipoDocumentoEditarFicha">
                      <option value="0">[SELECCIONAR]</option>
                    </select>
                  </div>
                </div>
                <div class="col-md-6">
                  <div class="form-group" id="divNumeroDocumentoModalEditarFicha">
                    <div id="divValidarDNI">
                      <label for="txtNumeroDocumentoEditarFicha" class="text-semibold">Número de documento <span class="text-danger">*</span></label>
                    </div>
                    <input type="text" class="form-control text-uppercase" placeholder="Número de documento" id="txtNumeroDocumentoEditarFicha" name="txtNumeroDocumentoEditarFicha" autocomplete="off" required="">
                  </div>
                </div>
              </div>
              <div class="row" id="divRespuestaReniecEditarFicha"></div>
              <div class="row">
                <div class="col-md-12">
                  <div class="form-group">
                    <label for="txtCorreoElectronicoEditarFicha" class="text-semibold">Correo electrónico <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" placeholder="Correo electrónico" id="txtCorreoElectronicoEditarFicha" name="txtCorreoElectronicoEditarFicha" maxlength="50" autocomplete="off">
                  </div>                                        
                </div>
              </div>
              <div class="row">
                <div class="col-md-12 text-danger text-right text-semibold">
                  (*) Campos obligatorios
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="submit" class="btn btn-success" id="btnActualizarFicha">Actualizar</button>
            <button type="button" class="btn btn-danger" id="btnCancelarEditarFicha" data-dismiss="modal">Cancelar</button>
          </div>

        </div>
      </div>
    </div>
    <!--/Modal editar ficha-->
    <!-- ------------------------------------------------------------------------------------------------------ -->
    <!-- ------------------------------------/Modal de editar ficha ------------------------------------------- -->
    <!-- ------------------------------------------------------------------------------------------------------ -->




    <!--contenido-->


    <!--javascript plantilla-->
    <%@include file="templates/footer-body.jsp" %>
    <!--select-->
    <!--<script type="text/javascript" src="../plantilla/assets/js/plugins/forms/selects/bootstrap_select.min.js"></script>-->
    <!--select2-->
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/selects/select2.min.js"></script>
    <!--datatable-->
    <script type="text/javascript" src="../plantilla/assets/js/plugins/tables/datatables/datatables.min.js"></script>
    <!--datepicker-->
    <script type="text/javascript" src="../plantilla/assets/js/core/libraries/jquery_ui/widgets.min.js"></script>
    <!--validInput-->
    <script type="text/javascript" src="../plantilla/assets/js/plugins/validateInput/validate_inputs.js"></script>
    <!--validate-->
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/validation/validate.min.js"></script>
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/validation/additional_methods.min.js"></script>
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/validation/localization/messages_es_PE.js"></script>
    <script type="text/javascript" src="../js/pages/newRulesValidate.js"></script>
    <!--javascript plantilla-->

    <!--datatable-->
    <script type="text/javascript" src="../plantilla/assets/js/plugins/tables/datatables/datatables.min.js"></script>
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/selects/select2.min.js"></script>
    <script type="text/javascript" src="../plantilla/assets/js/plugins/tables/datatables/extensions/responsive.min.js"></script>
    <!--datatable-->


    <!--javascript lib api-reniec--> 
    <script src="../js/lib/reniec-sunat-js.min.js" type="text/javascript"></script>
    <!--javascript lib api-reniec--> 

    <!--switchery-->
    <!--    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/styling/uniform.min.js"></script>
        <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/styling/switchery.min.js"></script>
        <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/inputs/touchspin.min.js"></script>-->
    <!--switchery-->

    <!--button spinner-->
    <script type="text/javascript" src="../plantilla/assets/js/plugins/buttons/spin.min.js"></script>
    <script type="text/javascript" src="../plantilla/assets/js/plugins/buttons/ladda.min.js"></script>

    <!--switchery-->
    <!--    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/styling/uniform.min.js"></script>
        <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/styling/switchery.min.js"></script>
        <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/styling/switch.min.js"></script>-->


    <!--javascript general-->
    <script src="../js/pages/general.js" type="text/javascript"></script>
    <!--javascript general-->


    <!--javascript propios-->
    <script src="../js/pages/menu.js" type="text/javascript"></script>
    <script type="text/javascript" src="../js/pages/index.js"></script>
    <script type="text/javascript" src="../js/lib/jquery-confirm-master/jquery-confirm.min.js"></script>
    <!--javascript propios-->

  </body>
</html>
<%    }
%>