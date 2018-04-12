<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="templates/validar.jsp"%>
<!DOCTYPE html>
<html lang="en">

  <head>
    <!--Estilos plantilla-->
    <%@include file="templates/header.jsp" %>
    <!--Estilos plantilla-->
    <link href="../css/lib/jasny-bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="../css/pages/fichaAdministrativa.css" rel="stylesheet" type="text/css"/>
    <!--Estilos propios-->
    <style>

      .fix-label{
          width: 100%;
          text-align: left;
          font-size: 13px; 
          font-weight: 400;
      }

      .form-horizontal .form-group{
          margin-left: 0px;
          margin-right: 0px;
      }

      .fix-escalafon{
          font-size: 18px; 
          text-align: left;
          width: 100%;
      }

    </style>
    <link href="../js/lib/jquery-confirm-master/jquery-confirm.min.css" rel="stylesheet" type="text/css"/>
    <!--Estilos propios-->


  </head>

  <body onload="cargar();">

    <input id="codTitulo" type="hidden" value="<%= menu != null ? menu.getCodigoTitulo() : 0%>">
    <input id="codModulo" type="hidden" value="<%= menu != null ? menu.getCodigoModulo() : 0%>">
    <input id="codCategoria" type="hidden" value="<%= menu != null ? menu.getCodigoCategoria() : 0%>">
    <input id="codSubCategoria" type="hidden" value="<%= menu != null ? menu.getCodigoSubcategoria() : 0%>">

    <input type="hidden" id="latitudResidencia" name="latitudResidencia" value=""/>
    <input type="hidden" id="longitudResidencia" name="longitudResidencia" value=""/>

    <!--navbar - sidebar-->
    <%@include file="templates/header-body.jsp" %>
    <!--navbar - sidebar-->

    <%@include file="templates/page-header.jsp" %>


    <!--contenido-->

    <div class="row">
      <div class="panel so-card-4">
        <div class="panel-heading bg-blue-800">
          <h6 class="panel-title text-semibold">
            <i class="fa fa-file fa-lg"></i>&nbsp;&nbsp;Datos ficha personal
          </h6>
          <div class="heading-elements">
            <!--            <ul class="icons-list">
                          <li><a data-action="collapse"></a></li>
                        </ul>-->
          </div>
        </div>

        <div class="panel-body">
          <fieldset>
            <legend class="text-semibold">
              <i class="fa fa-user fa-lg position-left"></i>
              Datos personales
              <a class="control-arrow" data-toggle="collapse" data-target="#demo1">
                <i class="icon-circle-down2"></i>
              </a>
            </legend>

            <div class="collapse in" id="demo1">
              <div class="row">
                <div class="col-md-9">
                  <div class="row">
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">Tipo documento</label>
                        <span id="lblTipoDocumento" class="label border-left-blue-800 label-striped text-light fix-label"></span>
                      </div>
                    </div>
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">Número documento</label>
                        <span id="lblNumeroDocumento" class="label border-left-blue-800 label-striped text-light fix-label"></span>
                      </div>
                    </div>
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">N° RUC</label>
                        <span id="lblNumeroRUC" class="label border-left-blue-800 label-striped text-light fix-label"></span>
                      </div>
                    </div>
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">Apellido paterno</label>
                        <span id="lblApellidoPaterno" class="label border-left-blue-800 label-striped text-light fix-label"></span>
                      </div>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">Apellido materno</label>
                        <span id="lblApellidoMaterno" class="label border-left-blue-800 label-striped text-light fix-label"></span>
                      </div>
                    </div>
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">Nombre</label>
                        <span id="lblNombre" class="label border-left-blue-800 label-striped text-light fix-label"></span>
                      </div>
                    </div>
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">Sexo</label>
                        <span id="lblSexo" class="label border-left-blue-800 label-striped text-light fix-label"></span>
                      </div>
                    </div>
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">Estado civil</label>
                        <span id="lblEstadoCivil" class="label border-left-blue-800 label-striped text-light fix-label"></span>
                      </div>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">Fecha de nacimiento</label>
                        <span id="lblFechaNacimiento" class="label border-left-blue-800 label-striped text-light fix-label"></span>
                      </div>
                    </div>
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">Nacionalidad</label>
                        <span id="lblNacionalidad" class="label border-left-blue-800 label-striped text-light fix-label"></span>
                      </div>
                    </div>
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">Departamento nacimiento</label>
                        <span id="lblDepartamentoNacimiento" class="label border-left-blue-800 label-striped text-light fix-label"></span>
                      </div>
                    </div>
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">Provincia nacimiento</label>
                        <span id="lblProvinciaNacimiento" class="label border-left-blue-800 label-striped text-light fix-label"></span>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="col-md-3 text-center">
                  <label class="display-block text-uppercase text-semibold">Foto</label>
                  <!--<div>-->
                  <div class="fileinput fileinput-new" data-provides="fileinput">
                    <div class="fileinput-new thumbnail" style="width: 170px; height: 190px;">
                      <img id="lblFoto" data-src="holder.js/100%x100%" alt="100%x100%" src="../img/user.svg" style="height: 100%; width: 100%; display: block;">
                    </div>
                    <div class="fileinput-preview fileinput-exists thumbnail" style="width: 170px; height: 190px;">
                      <img id="foto" style="width: 170px !important; height: 180px!important;object-fit: contain !important;">
                    </div>
                  </div>
                  <!--</div>-->
                </div>
              </div>
              <div class="row">
                <div class="col-md-9">
                  <div class="row">
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">Distrito nacimiento</label>
                        <span id="lblDistritoNacimiento" class="label border-left-blue-800 label-striped text-light fix-label"></span>
                      </div>
                    </div>
                    <div class="col-md-9">
                      <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">Dirección del documento</label>
                        <span id="lblDireccionDocumento" class="label border-left-blue-800 label-striped text-light fix-label"></span>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="row">
                    <div class="col-md-12">
                      <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">Teléfono fijo</label>
                        <span id="lblTelefonoFijo" class="label border-left-blue-800 label-striped text-light fix-label"></span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="col-md-9">
                  <div class="row">
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">Teléfono móvil</label>
                        <span id="lblTelefonoMovil" class="label border-left-blue-800 label-striped text-light fix-label"></span>
                      </div>
                    </div>
                    <div class="col-md-6">
                      <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">Correo electrónico</label>
                        <span id="lblCorreoElectronico" class="label border-left-blue-800 label-striped text-light fix-label"></span>
                      </div>
                    </div>
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">Departamento residencia</label>
                        <span id="lblDepartamentoResidencia" class="label border-left-blue-800 label-striped text-light fix-label"></span>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="row">
                    <div class="col-md-12">
                      <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">Provincia residencia</label>
                        <span id="lblProvinciaResidencia" class="label border-left-blue-800 label-striped text-light fix-label"></span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="col-md-9">
                  <div class="row">
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">Distrito residencia</label>
                        <span id="lblDistritoResidencia" class="label border-left-blue-800 label-striped text-light fix-label"></span>
                      </div>
                    </div>
                    <div class="col-md-6">
                      <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">Dirección residencia</label>
                        <span id="lblDireccionResidencia" class="label border-left-blue-800 label-striped text-light fix-label"></span>
                      </div>
                    </div>
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">Fondo de pensión</label>
                        <span id="lblFondoPension" class="label border-left-blue-800 label-striped text-light fix-label"></span>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="form-group">
                    <label class="display-block text-uppercase text-semibold">Ubicación de residencia</label>
                    <button type="button" class="btn btn-success" data-toggle="modal" data-target="#modalMapaResidencia"><b><i class="icon-pin-alt"></i></b> Ver en el mapa</button>
                  </div>
                </div>
              </div>
          </fieldset>
          <br>
          <fieldset>
            <legend class="text-semibold">
              <i class="fa fa-users fa-lg position-left"></i>
              Datos familiares
              <a class="control-arrow" data-toggle="collapse" data-target="#demo2">
                <i class="icon-circle-down2"></i>
              </a>
            </legend>

            <div class="collapse in" id="demo2">
              <div class="row">
                <div class="col-md-12">
                  <div class="table-responsive">
                    <table class="table table-bordered table-striped table-framed table-xxs" id="tblFamiliar">
                      <thead class="text-primary-800" style="background-color: #EEEEEE;">
                        <tr>
                          <th>APELLIDOS Y NOMBRES</th>
                          <th>PARENTESCO</th>
                          <th>FECHA DE NACIMIENTO</th>
                          <th>TIPO DE DOCUMENTO</th>
                          <th>NÚMERO DE DOCUMENTO</th>
                          <th>TELÉFONO</th>
                        </tr>
                      </thead>
                    </table>
                  </div>
                </div>
              </div>
            </div>
          </fieldset>
          <br>
          <fieldset>
            <legend class="text-semibold">
              <i class="fa fa-book fa-lg position-left"></i>
              Formación académica
              <a class="control-arrow" data-toggle="collapse" data-target="#demo3">
                <i class="icon-circle-down2"></i>
              </a>
            </legend>

            <div class="collapse in" id="demo3">
              <div class="row">
                <div class="col-md-12">
                  <div class="table-responsive">
                    <table class="table table-bordered table-striped table-framed table-xxs" id="tblFormacionAcademica">
                      <thead class="text-primary-800" style="background-color: #EEEEEE;">
                        <tr>
                          <th>GRADO ESTUDIO</th>
                          <th>ESTADO ESTUDIO</th>
                          <th>CENTRO DE ESTUDIO</th>
                          <th>CARRERA</th>
                          <th>FECHA INICIO</th>
                          <th>FECHA FIN</th>
                        </tr>
                      </thead>
                    </table>
                  </div>
                </div>
              </div>
            </div>
          </fieldset>
          <br>
          <fieldset>
            <legend class="text-semibold">
              <i class="fa fa-book fa-lg position-left"></i>
              Experiencia laboral
              <a class="control-arrow" data-toggle="collapse" data-target="#demo4">
                <i class="icon-circle-down2"></i>
              </a>
            </legend>

            <div class="collapse in" id="demo4">
              <div class="row">
                <div class="col-md-12">
                  <div class="table-responsive">
                    <table class="table table-bordered table-striped table-framed table-xxs" id="tblExperienciaLaboral">
                      <thead class="text-primary-800" style="background-color: #EEEEEE;">
                        <tr>
                          <th>EMPRESA</th>
                          <th>CARGO</th>
                          <th>FECHA INICIO</th>
                          <th>FECHA FIN</th>
                          <th>TÉLEFONO</th>
                        </tr>
                      </thead>
                    </table>
                  </div>
                </div>
              </div>
            </div>
          </fieldset>
          <br>
          <fieldset>
            <legend class="text-semibold">
              <i class="fa fa-book fa-lg position-left"></i>
              Expedientes
            </legend>

            <div class="collapse in" id="demo5">
              <div class="row">
                <form action="action" id="formExpedientesPersona">
                  <div class="col-md-12">
                    <div class="form-group">
                      <div class="row">
                        <div class="col-md-6">
                          <label class="display-block text-uppercase text-semibold">Expedientes adjuntados <span class="text-danger"> (*)</span></label>
                        </div>
                        <div class="col-md-6">
                          <a class="pull-right text-success" id="btnAgregarTipoExpediente"><i class="icon-plus-circle2 position-left"></i> Nuevo tipo de expediente</a>
                        </div>
                      </div>
                      <select name="states[]" id="" class="js-example-basic-multiple" id="cboTipoExpediente"></select>
                    </div>
                  </div>
                </form>
              </div>
              <div class="row">
                <div class="col-md-12">
                  <div class="form-group">
                    <label class="display-block text-uppercase text-semibold tp-text">Enlace de alfresco&nbsp;<span class="text-danger"> (*)</span></label>
                    <div class="input-group">
                      <span class="input-group-addon"><i class="icon-link"></i></span>
                      <input type="url" class="form-control" placeholder="Ingrese el enlace del repositorio de expedientes asociado a la persona" name="txtEnlaceAlfresco" id="txtEnlaceAlfresco" required=""/>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </fieldset>
        </div>
      </div>
    </div>

    <!--datos administrativos-->
    <div class="row">
      <div class="col-md-12">
        <div class="panel so-card-4">
          <div class="panel-heading bg-blue-800">
            <h6 class="panel-title text-semibold">
              <i class="fa fa-file fa-lg"></i>&nbsp;&nbsp;Datos administrativos
            </h6>
          </div>
          <div class="panel-body">
            <form action="#" method="post" id="formDatosAdministrativos">
              <div class="row">
                <div class="col-md-3">
                  <div class="form-group">
                    <label class="display-block text-uppercase text-semibold tp-text">Fecha de ingreso&nbsp;<span class="text-danger"> (*)</span></label>
                    <div class="input-group">
                      <span class="input-group-addon"><i class="icon-calendar"></i></span>
                      <input id="dpFechaIngreso" name="dpFechaIngreso" type="text" class="form-control datepicker_es" placeholder="Fecha de inicio &hellip;">
                    </div>
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="form-group">
                    <label class="display-block text-uppercase text-semibold tp-text">Fecha de término&nbsp;<span class="text-danger"> (*)</span></label>
                    <div class="input-group">
                      <span class="input-group-addon"><i class="icon-calendar"></i></span>
                      <input id="dpFechaTermino" name="dpFechaTermino" type="text" class="form-control datepicker_es" placeholder="Fecha de inicio &hellip;">
                    </div>
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="form-group">
                    <label class="display-block text-uppercase text-semibold">Sede<span class="text-danger"> (*)</span></label>
                    <select class="bootstrap-select" data-size="5" data-width="100%" id="cboSede" name="cboSede">
                      <option value="0">[SELECCIONAR]</option>
                    </select>
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="form-group">
                    <label class="display-block text-uppercase text-semibold">Área<span class="text-danger"> (*)</span></label>
                    <select class="bootstrap-select" data-size="5" data-width="100%" id="cboArea" name="cboArea">
                      <option value="0">[SELECCIONAR]</option>
                    </select>
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="col-md-3">
                  <div class="form-group">
                    <label class="display-block text-uppercase text-semibold">Cargo<span class="text-danger"> (*)</span></label>
                    <select class="bootstrap-select" data-size="5" data-width="100%" id="cboCargo" name="cboCargo">
                      <option value="0">[SELECCIONAR]</option>
                    </select>
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="form-group">
                    <label class="display-block text-uppercase text-semibold">Tipo Pago<span class="text-danger"> (*)</span></label>
                    <select class="bootstrap-select" data-size="5" data-width="100%" id="cboTipoPago" name="cboTipoPago">
                      <option value="0">[SELECCIONAR]</option>
                    </select>
                  </div>
                </div>
                <div id="divContenidoDatoAdministrativo">
                  <div class="col-md-3">
                    <div class="form-group">
                      <label class="display-block text-uppercase text-semibold">Escalafón<span class="text-danger"></span></label>
                      <h6 class="text-bold" id="sueldoEscalafon">S/. 0.00</h6>
                    </div>
                  </div>
                  <div class="col-md-3">
                    <div class="form-group">
                      <label class="display-block text-uppercase text-semibold">Sueldo Propuesto<span class="text-danger"> (*)</span></label>
                      <div class="input-group">
                        <span class="input-group-addon">S/. </span>
                        <input type="text" class="form-control" placeholder="0.00" maxlength="10" id="txtSueldoMensual" name="txtSueldoMensual">
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="col-md-6">
                  <div class="form-group">
                    <label class="display-block text-uppercase text-semibold">Observación</label>
                    <textarea id="txtObservacion" name="txtObservacion" rows="5" cols="5" class="form-control" placeholder="Agregue una observación..."></textarea>
                  </div>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col-md-12 text-center">
        <button type="button" id="btnRegresarBandeja" class="btn bg-danger btn-xlg text-uppercase text-semibold" style="width: 250px">
          <i class="icon-x position-left" style="font-size: 20px;"></i> Cancelar
        </button>
        <button type="button" id="btnRegistrarFicha" class="btn btn-success btn-xlg text-uppercase text-semibold" style="width: 250px">
          <i class="icon-checkmark position-left" style="font-size: 20px;"></i> Registrar ficha
        </button>
      </div>
    </div>
    <!--datos administrativos-->


    <!-- Primary modal -->
    <div id="modalMapaResidencia" class="modal fade">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header bg-success-600">
            <button type="button" class="close" data-dismiss="modal">&times;</button>
            <h6 class="modal-title">Ubicación geográfica de la residencia del personal</h6>
          </div>
          <div class="modal-body">
            <div class="row">
              <div class="col-md-12">
                <div class="map-container" id="mapResidencia">
                  <div  id="mapResidencia"></div>
                </div>
              </div>
            </div>
          </div>
          <div class="modal-footer text-center">
            <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar ventana</button>
          </div>
        </div>
      </div>
    </div>
    <!-- /primary modal -->

    <!--contenido-->

    <!--javascript plantilla-->
    <%@include file="templates/footer-body.jsp" %>

    <script type="text/javascript" src="../plantilla/assets/js/core/libraries/jquery_ui/interactions.min.js"></script>
    <!--select-->
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/selects/bootstrap_select.min.js"></script>

    <!--datepicker-->
    <script type="text/javascript" src="../plantilla/assets/js/plugins/pickers/pickadate/picker.js"></script>
    <script type="text/javascript" src="../plantilla/assets/js/plugins/pickers/pickadate/picker.date.js"></script>
    <!--validInput-->
    <script type="text/javascript" src="../plantilla/assets/js/plugins/validateInput/validate_inputs.js"></script>
    <!--validate-->
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/validation/validate.min.js"></script>
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/validation/additional_methods.min.js"></script>
    <script type="text/javascript" src="../js/pages/newRulesValidate.js"></script>
    <!--javascript plantilla-->

    <!--datatable-->
    <script type="text/javascript" src="../plantilla/assets/js/plugins/tables/datatables/datatables.min.js"></script>
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/selects/select2.min.js"></script>
    <script type="text/javascript" src="../plantilla/assets/js/plugins/tables/datatables/extensions/responsive.min.js"></script>
    <!--datatable-->

    <!--switchery-->
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/styling/uniform.min.js"></script>
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/styling/switchery.min.js"></script>
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/inputs/touchspin.min.js"></script>
    <!--switchery-->


    <!--javascript general-->
    <script src="../js/pages/general.js" type="text/javascript"></script>
    <!--javascript general-->
    <script src="../js/lib/simple-table.min.js" type="text/javascript"></script>

    <!--javascript propios-->
    <script src="../js/pages/menu.js" type="text/javascript"></script>
    <script type="text/javascript" src="../js/lib/jquery-confirm-master/jquery-confirm.min.js"></script>
    <script src="../js/pages/fichaAdministrativa.js" type="text/javascript"></script>
    <!--datepicker-->
    <script type="text/javascript" src="../plantilla/assets/js/core/libraries/jquery_ui/widgets.min.js"></script>
    <!--new rules validate-->
    <script src="../js/pages/newRulesValidate.js" type="text/javascript"></script>
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&key=AIzaSyCgA2sD34iT4HmBrz1Nj3Pxdg1UQisJRLs&libraries=places&region=ES" defer></script>
    <!--javascript propios-->

  </body>

</html>
<%    }
%>
