<%@page import="java.util.Enumeration"%>
<%@page import="trismegistoplanilla.beans.PersonaBean"%>
<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject"%>
<%@page import="trismegistoplanilla.beans.TokenFichaBean"%>
<%@include file="response/validarCache.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%    /*Enumeration keys = session.getAttributeNames();
    while (keys.hasMoreElements()) {
        String key = (String) keys.nextElement();
        out.println(key + ": " + session.getValue(key) + "<br>");
    }*/
  TokenFichaBean tf = (TokenFichaBean) session.getAttribute("tokenFicha");
  if (tf == null) {
    String errorTokenFicha = "<div align='center' style=\"font-family: 'Arial', Sans-serif; color:red;\"><br/><h3>Lo sentimos, no hay nada para mostrar :(<h3></div>";
    out.print(errorTokenFicha);
  } else {
    PersonaBean p = (PersonaBean) session.getAttribute("persona");
    if (p == null) {
      String errorTokenFicha = "<div align='center' style=\"font-family: 'Arial', Sans-serif; color:red;\"><br/><h3>Lo sentimos, no hay nada para mostrar :(<h3></div>";
      out.print(errorTokenFicha);
    } else {

%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title> :: FICHA PERSONAL ::</title>

    <!-- Global stylesheets -->
    <link href="https://fonts.googleapis.com/css?family=Roboto:400,300,100,500,700,900" rel="stylesheet" type="text/css">
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous">
    <link href="../plantilla/assets/css/icons/icomoon/styles.css" rel="stylesheet" type="text/css">
    <link href="../plantilla/assets/css/bootstrap.css" rel="stylesheet" type="text/css">
    <link href="../plantilla/assets/css/core.css" rel="stylesheet" type="text/css">
    <link href="../plantilla/assets/css/components.css" rel="stylesheet" type="text/css">
    <link href="../plantilla/assets/css/colors.css" rel="stylesheet" type="text/css">

    <link href="../css/lib/jasny-bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="../js/lib/jquery-confirm-master/jquery-confirm.min.css" rel="stylesheet" type="text/css"/>
    <link href="https://gitcdn.github.io/bootstrap-toggle/2.2.2/css/bootstrap-toggle.min.css" rel="stylesheet">
    <link href="../css/pages/ficha.css" rel="stylesheet" type="text/css"/>
  </head>
  <body class="tp-background-cover">
    <!-- Main navbar -->
    <div class="navbar navbar-inverse so-card-2">
      <!--<div class="navbar-header">-->
      <h5 class="text-center text-semibold text-uppercase">Ficha de registro de personal</h5>
      <!--</div>-->
    </div>
    <!-- /main navbar -->


    <!-- Page container -->
    <div class="page-container">

      <!-- Page content -->
      <div class="page-content">

        <!-- Main content -->
        <div class="content-wrapper">

          <!-- Content area -->
          <div class="content">

            <!--AVISO-->
            <div class="row">
              <div class="col-md-10 col-md-offset-1">
                <div class="alert alert-warning alert-bordered">
                  <span class="text-black"><u>Antes de llenar la ficha, tener en cuenta lo siguiente:</u></span>
                  <ul>
                    <li>
                      Se le pide por favor que los datos solicitados en está ficha sean llenados con total <b>veracidad</b>,
                      ya que estos datos serán tomados en cuenta por la empresa para los posteriores eventos que se puedan
                      realizar.
                    </li>
                    <li>
                      Los campos marcados con <span class="text-danger">(*)</span> hacen referencia a que el campo debe de ser
                      llenado <b>obligatoriamente</b>.
                    </li>
                    <li>
                      Recordar que la ficha tiene un plazo máximo de <b>24 horas</b> desde que se le solicitaron los datos para
                      la generación de su ficha. Pasado el plazo indicado, la ficha pasará a un estado caducado y no podrá acceder.
                    </li>
                  </ul>
                </div>

              </div>
            </div>
            <!--AVISO-->
            <div class="row">
              <div class="col-md-12 text-center">
                <button id="btnPrueba" class="btn btn-primary">REGISTRASH</button>
              </div>
            </div>
            <!--DATOS PERSONALES-->
            <div class="row">
              <div class="col-md-12">
                <form action="#" name="formDatosPersonales" id="formDatosPersonales">
                  <input type="hidden" name="codigoTipoDocumento" id="codigoTipoDocumento" value="<%= p.getCodigoTipoDocumento()%>"/>
                  <div class="row">
                    <div class="col-md-10 col-md-offset-1">
                      <div class="panel so-card-4">
                        <div class="panel-heading bg-blue-800">
                          <h6 class="panel-title text-semibold">
                            <i class="fa fa-user fa-lg"></i>&nbsp;&nbsp;Datos personales
                          </h6>
                        </div>
                        <div class="panel-body">
                          <div class="row">
                            <div class="col-md-9">
                              <div class="row">
                                <div class="col-md-4">
                                  <div class="form-group">
                                    <label class="display-block text-uppercase text-semibold">Tipo documento</label>
                                    <span id="lblTipoDocumento" class="label border-left-blue-800 label-striped text-light fix-label" style="font-size: 13px; font-weight: 400;"><%= p.getNombreTipoDocumento()%></span>
                                  </div>
                                </div>
                                <div class="col-md-4">
                                  <div class="form-group">
                                    <label class="display-block text-uppercase text-semibold">Número documento</label>
                                    <span id="lblNumeroDocumento" class="label border-left-blue-800 label-striped text-light fix-label" style="font-size: 13px; font-weight: 400;"><%= p.getNumeroDocumento()%></span>
                                  </div>
                                </div>
                                <div class="col-md-4" id="divNumeroRUC">
                                  <div class="form-group">
                                    <label class="display-block text-uppercase text-semibold">N° RUC</label>
                                    <% if (p.getCodigoTipoDocumento() == 3) {%>
                                    <span id="txtNumeroRUC" class="label border-left-blue-800 label-striped text-light fix-label" style="font-size: 13px; font-weight: 400;"><%= p.getRuc()%></span>
                                    <%} else {%>
                                    <input id="txtNumeroRUC" name="txtNumeroRUC" type="text" class="form-control" maxlength="11" minlength="11">
                                    <%}%>
                                  </div>
                                </div>
                              </div>
                              <div class="row">
                                <div class="col-md-4" id="divApellidoPaterno">
                                  <div class="form-group">
                                    <label class="display-block text-uppercase text-semibold">Apellido paterno<span class="text-danger"> (*)</span></label>
                                    <% if (p.getApellidoPaterno() != null) {%>
                                    <span id="txtApellidoPaterno" class="label border-left-blue-800 label-striped text-light fix-label" style="font-size: 13px; font-weight: 400;"><%= p.getApellidoPaterno()%></span>
                                    <%} else {%>
                                    <input id="txtApellidoPaterno" name="txtApellidoPaterno" type="text" class="form-control">
                                    <%}%>
                                  </div>
                                </div>
                                <div class="col-md-4" id="divApellidoMaterno">
                                  <div class="form-group">
                                    <label class="display-block text-uppercase text-semibold">Apellido materno<span class="text-danger"> (*)</span></label>
                                    <% if (p.getApellidoMaterno() != null) {%>
                                    <span id="txtApellidoMaterno" class="label border-left-blue-800 label-striped text-light fix-label" style="font-size: 13px; font-weight: 400;"><%= p.getApellidoMaterno()%></span>
                                    <%} else {%>
                                    <input id="txtApellidoMaterno" name="txtApellidoMaterno" type="text" class="form-control">
                                    <%}%>
                                  </div>
                                </div>
                                <div class="col-md-4" id="divNombre">
                                  <div class="form-group">
                                    <label class="display-block text-uppercase text-semibold">Nombre<span class="text-danger"> (*)</span></label>
                                    <% if (p.getNombre() != null) {%>
                                    <span id="txtNombre" class="label border-left-blue-800 label-striped text-light fix-label" style="font-size: 13px; font-weight: 400;"><%= p.getNombre()%></span>
                                    <%} else {%>
                                    <input id="txtNombre" name="txtNombre" type="text" class="form-control">
                                    <%}%>
                                  </div>
                                </div>
                              </div>
                              <div class="row">
                                <div class="col-md-4">
                                  <div class="form-group">
                                    <label class="display-block text-uppercase text-semibold">Sexo<span class="text-danger"> (*)</span></label>
                                    <select class="bootstrap-select" data-width="100%" id="cboSexo" name="cboSexo">
                                      <option value="0">[SELECCIONAR]</option>
                                      <option value="M">MASCULINO</option>
                                      <option value="F">FEMENINO</option>
                                    </select>
                                  </div>
                                </div>
                                <div class="col-md-4">
                                  <div class="form-group">
                                    <label class="display-block text-uppercase text-semibold">Estado civil<span class="text-danger"> (*)</span></label>
                                    <select class="bootstrap-select" data-width="100%" id="cboEstadoCivil" name="cboEstadoCivil">
                                      <option value="0">[SELECCIONAR]</option>
                                    </select>
                                  </div>
                                </div>
                                <div class="col-md-4">
                                  <div class="form-group">
                                    <label class="display-block text-uppercase text-semibold">Fecha nacimiento<span class="text-danger"> (*)</span></label>
                                    <div class="input-group">
                                      <span class="input-group-addon"><i class="icon-calendar"></i></span>
                                      <input type="text" class="form-control" id="dpFechaNacimiento" name="dpFechaNacimiento" placeholder="Fecha de nacimiento &hellip;">
                                    </div>
                                  </div>
                                </div>
                              </div>
                            </div>
                            <div class="col-md-3 text-center">
                              <label class="display-block text-uppercase text-semibold">Foto<span class="text-danger"> (*)</span></label>
                              <!--<div>-->
                              <div class="fileinput fileinput-new" data-provides="fileinput">
                                <div class="fileinput-new thumbnail" style="width: 170px; height: 190px;">
                                  <img data-src="holder.js/100%x100%" alt="100%x100%" src="data:image/png;base64,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" style="height: 100%; width: 100%; display: block;">
                                </div>
                                <div class="fileinput-preview fileinput-exists thumbnail" style="width: 170px; height: 190px;">
                                  <img style="width: 170px !important; height: 180px!important;object-fit: contain !important;" src="">
                                </div>
                                <div>
                                  <span id="spanFoto" class="btn btn-info btn-file">
                                    <span class="fileinput-new">Selecionar imagen</span>
                                    <span id="cambiar" class="fileinput-exists">Cambiar</span>
                                    <input type="file" name="flImage" id="lblFoto" accept="image/png, image/jpeg, image/jpg">
                                  </span>
                                  <a href="#" id="remover" class="btn btn-warning fileinput-exists" data-dismiss="fileinput">Remover</a>
                                  <!--<a href="#" class="btn btn-primary" onclick="dialogoCamara();">Tomar foto</a>-->
                                </div>
                              </div>
                              <!--</div>-->
                            </div>
                          </div>
                          <div class="row">
                            <div class="col-md-3">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold">Nacionalidad<span class="text-danger"> (*)</span></label>
                                <select class="bootstrap-select" data-size="5" data-width="100%" id="cboNacionalidad" name="cboNacionalidad" data-live-search="true">
                                  <option value="0">[SELECCIONAR]</option>
                                </select>
                              </div>
                            </div>
                            <div class="col-md-3">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold">Departamento nacimiento<span class="text-danger"> (*)</span></label>
                                <select id="cboDepartamentoNacimiento" data-size="5" name="cboDepartamentoNacimiento" class="bootstrap-select departamentos" data-width="100%" data-live-search="true">
                                  <option value="0">[SELECCIONAR]</option>
                                </select>
                              </div>
                            </div>
                            <div class="col-md-3">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold">Provincia nacimiento<span class="text-danger"> (*)</span></label>
                                <select id="cboProvinciaNacimiento" data-size="5" name="cboProvinciaNacimiento" class="bootstrap-select" data-width="100%" data-live-search="true">
                                  <option value="0">[SELECCIONAR]</option>
                                </select>
                              </div>
                            </div>
                            <div class="col-md-3">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold">Distrito nacimiento<span class="text-danger"> (*)</span></label>
                                <select id="cboDistritoNacimiento" data-size="5" name="cboDistritoNacimiento" class="bootstrap-select" data-width="100%" data-live-search="true">
                                  <option value="0">[SELECCIONAR]</option>
                                </select>
                              </div>
                            </div>
                          </div>
                          <div class="row">
                            <div class="col-md-6">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold">Dirección del documento de identidad<span class="text-danger"> (*)</span></label>
                                <input maxlength="100" id="txtDireccionDocumento" name="txtDireccionDocumento" type="text" class="form-control">
                              </div>
                            </div>
                            <div class="col-md-3">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold">Teléfono fijo</label>
                                <input id="txtTelefonoFijo" name="txtTelefonoFijo" type="text" class="form-control" maxlength="7">
                              </div>
                            </div>
                            <div class="col-md-3">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold">Teléfono móvil</label>
                                <input id="txtTelefonoMovil" name="txtTelefonoMovil" type="text" class="form-control" maxlength="9">
                              </div>
                            </div>
                          </div>
                          <div class="row">
                            <div class="col-md-6" id="divCorreoElectronico">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold">Correo electrónico<span class="text-danger"> (*)</span></label>
                                <input id="txtCorreoElectronico" name="txtCorreoElectronico" type="text" class="form-control" value="<%= p.getCorreo().toLowerCase()%>">
                              </div>
                            </div>
                            <div class="col-md-3">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold">Departamento residencia<span class="text-danger"> (*)</span></label>
                                <select id="cboDepartamentoResidencia" data-size="5" name="cboDepartamentoResidencia" class="bootstrap-select departamentos" data-width="100%" data-live-search="true">
                                  <option value="0">[SELECCIONAR]</option>
                                </select>
                              </div>
                            </div>
                            <div class="col-md-3">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold">Provincia residencia<span class="text-danger"> (*)</span></label>
                                <select id="cboProvinciaResidencia" data-size="5" name="cboProvinciaResidencia" class="bootstrap-select" data-width="100%" data-live-search="true">
                                  <option value="0">[SELECCIONAR]</option>
                                </select>
                              </div>
                            </div>
                          </div>
                          <div class="row">
                            <div class="col-md-3">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold">Distrito residencia<span class="text-danger"> (*)</span></label>
                                <select id="cboDistritoResidencia"  data-size="5"name="cboDistritoResidencia" class="bootstrap-select" data-width="100%" data-live-search="true">
                                  <option value="0">[SELECCIONAR]</option>
                                </select>
                              </div>
                            </div>
                            <div class="col-md-9">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold">Dirección residencia<span class="text-danger"> (*)</span></label>
                                <input maxlength="100" id="txtDireccionResidencia" name="txtDireccionResidencia" type="text" class="form-control">
                              </div>
                            </div>
                          </div>
                          <div class="row">
                            <div class="col-md-12">
                              <input type="hidden" id="latitudResidencia" name="latitudResidencia" value=""/>
                              <input type="hidden" id="longitudResidencia" name="longitudResidencia" value=""/>
                              <label class="display-block text-uppercase text-semibold">
                                Busque su dirección de residencia arrastrando el marcador o dando clic en el mapa
                                <span class="text-danger"> (*)</span></label>
                              <div class="map-container" id="mapResidencia">
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </form>
              </div>
            </div>
            <!--DATOS PERSONALES-->

            <!--DATOS FAMILIARES-->
            <div class="row">
              <div class="col-md-12">
                <form action="#" method="POST" name="formDatosFamiliares" id="formDatosFamiliares">
                  <div class="row">
                    <div class="col-md-10 col-md-offset-1">
                      <div class="panel so-card-4">
                        <div class="panel-heading bg-blue-800">
                          <h6 class="panel-title text-semibold">
                            <i class="fa fa-users fa-lg"></i>&nbsp;&nbsp;Datos familiares
                          </h6>
                        </div>
                        <div class="panel-body">
                          <div class="row">
                            <div class="col-md-3">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold">Parentesco<span class="text-danger"> (*)</span></label>
                                <select id="cboParentescoFamiliar" name="cboParentescoFamiliar" class="bootstrap-select" data-width="100%">
                                  <option value="0">[SELECCIONAR]</option>
                                </select>
                              </div>
                            </div>
                            <div class="col-md-3">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold">Apellido paterno<span class="text-danger"> (*)</span></label>
                                <input maxlength="50" id="txtApellidoPaternoFamiliar" name="txtApellidoPaternoFamiliar" type="text" class="form-control">
                              </div>
                            </div>
                            <div class="col-md-3">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold">Apellido materno<span class="text-danger"> (*)</span></label>
                                <input maxlength="50" id="txtApellidoMaternoFamiliar" name="txtApellidoMaternoFamiliar" type="text" class="form-control">
                              </div>
                            </div>
                            <div class="col-md-3">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold">Nombre<span class="text-danger"> (*)</span></label>
                                <input maxlength="80" id="txtNombreFamiliar" name="txtNombreFamiliar" type="text" class="form-control">
                              </div>
                            </div>
                          </div>
                          <div class="row">
                            <div class="col-md-3">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold">Fecha nacimiento<span class="text-danger"> (*)</span></label>
                                <div class="input-group">
                                  <span class="input-group-addon"><i class="icon-calendar"></i></span>
                                  <input id="dpFechaNacimientoFamiliar" name="dpFechaNacimientoFamiliar" type="text" class="form-control datepicker_es" placeholder="Fecha de nacimiento &hellip;">
                                </div>
                              </div>
                            </div>
                            <div class="col-md-3">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold">Tipo documento<span class="text-danger"> (*)</span></label>
                                <select id="cbotipoDocumentoFamiliar" name="cbotipoDocumentoFamiliar" class="bootstrap-select" data-width="100%">
                                  <option value="0">[SELECCIONAR]</option>
                                </select>
                              </div>
                            </div>
                            <div class="col-md-3">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold">Número documento<span class="text-danger"> (*)</span></label>
                                <input id="txtNumeroDocumentoFamiliar" name="txtNumeroDocumentoFamiliar" type="text" class="form-control">
                              </div>
                            </div>
                            <div class="col-md-3">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold">Sexo<span class="text-danger"> (*)</span></label>
                                <select class="bootstrap-select" data-width="100%" id="cboSexoFamiliar" name="cboSexoFamiliar">
                                  <option value="0">[SELECCIONAR]</option>
                                  <option value="M">MASCULINO</option>
                                  <option value="F">FEMENINO</option>
                                </select>
                              </div>
                            </div>
                          </div>
                          <div class="row">
                            <div class="col-md-3">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold">Teléfono</label>
                                <input id="txtTelefonoFamiliar" name="txtTelefonoFamiliar" type="text" class="form-control" maxlength="9" minlength="7">
                              </div>
                            </div>
                          </div>
                          <div class="row">
                            <div class="col-md-12 text-center">
                              <button type="button" id="btnAgregarFamiliar" class="btn btn-success ">
                                <i class="icon-checkmark position-left"></i> Agregar
                              </button>
                            </div>
                          </div>
                          <div class="row"  id="rowTblFamiliar">
                            <div class="col-md-12">
                              <div class="table-responsive">
                                <table class="table table-bordered table-striped table-framed table-xxs" id="tblFamiliar">
                                  <thead class="text-primary-800" style="background-color: #EEEEEE;">
                                    <tr>
                                      <th>#</th>
                                      <th>APELLIDOS Y NOMBRES</th>
                                      <th>PARENTESCO</th>
                                      <th>FECHA DE NACIMIENTO</th>
                                      <th>TIPO DE DOCUMENTO</th>
                                      <th>NÚMERO DE DOCUMENTO</th>
                                      <th>SEXO</th>
                                      <th>TELÉFONO</th>
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
                    </div>
                  </div>
                </form>
              </div>
            </div>
            <!--DATOS FAMILIARES-->


            <!--DATOS EDUCACION: ESTUDIOS REALIZADOS O INSTRUCCION-->
            <div class="row">
              <div class="col-md-12">
                <form action="#" name="formFormacionAcademica" id="formFormacionAcademica">
                  <div class="row">
                    <div class="col-md-10 col-md-offset-1">
                      <div class="panel so-card-4">
                        <div class="panel-heading bg-blue-800">
                          <h6 class="panel-title text-semibold">
                            <i class="fa fa-book fa-lg"></i>&nbsp;&nbsp;Formación académica
                          </h6>
                        </div>
                        <div class="panel-body">
                          <div class="row">
                            <div class="col-md-3">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold">Grado estudio<span class="text-danger"> (*)</span></label>
                                <select id="cboGradoEstudioFormacionAcademica" name="cboGradoEstudioFormacionAcademica" class="bootstrap-select" data-width="100%">
                                  <option value="0">[SELECCIONAR]</option>
                                </select>
                              </div>
                            </div>
                            <div class="col-md-3">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold">Estado estudio<span class="text-danger"> (*)</span></label>
                                <select id="cboEstadoEstudioFormacionAcademica" name="cboEstadoEstudioFormacionAcademica" class="bootstrap-select" data-width="100%">
                                  <option value="0">[SELECCIONAR]</option>
                                </select>
                              </div>
                            </div>
                            <div class="col-md-3">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold">Posición de institución<span class="text-danger"> (*)</span></label>
                                <select id="cboPosicionInstitucion" name="cboPosicionInstitucion" class="bootstrap-select" data-width="100%">
                                  <option value="0">[SELECCIONAR]</option>
                                  <option value="1">PÚBLICO</option>
                                  <option value="2">PRIVADO</option>
                                </select>
                              </div>
                            </div>
                            <div class="col-md-3">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold">Centro de estudios<span class="text-danger"> (*)</span></label>
                                <input maxlength="80" id="txtCentroEstudiosFormacionAcademica" name="txtCentroEstudiosFormacionAcademica" type="text" class="form-control">
                              </div>
                            </div>
                          </div>
                          <div class="row">
                            <div class="col-md-3">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold">Número de colegiatura</label>
                                <input maxlength="50" id="txtNumeroColegiatura" name="txtNumeroColegiatura" type="text" class="form-control">
                              </div>
                            </div>
                            <div class="col-md-3">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold">Carrera profesional<span class="text-danger"> (*)</span></label>
                                <input maxlength="50" id="txtCarreraProfesional" name="txtCarreraProfesional" type="text" class="form-control">
                              </div>
                            </div>
                            <div class="col-md-3">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold">Fecha inicio<span class="text-danger"> (*)</span></label>
                                <div class="input-group">
                                  <span class="input-group-addon"><i class="icon-calendar"></i></span>
                                  <input id="dpFechaInicioFormacionAcademica" name="dpFechaInicioFormacionAcademica" type="text" class="form-control datepicker_es" placeholder="Fecha de inicio &hellip;">
                                </div>
                              </div>
                            </div>
                            <div class="col-md-3">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold">Fecha fin<span class="text-danger"> (*)</span></label>
                                <div class="input-group">
                                  <span class="input-group-addon"><i class="icon-calendar"></i></span>
                                  <input id="dpFechaFinFormacionAcademica" name="dpFechaFinFormacionAcademica" type="text" class="form-control datepicker_es" placeholder="Fecha de fin &hellip;">
                                </div>
                              </div>
                            </div>
                          </div>
                          <!--accion agregar-->
                          <div class="row">
                            <div class="col-md-12 text-center">
                              <button type="button" id="btnAgregarFormacionAcademica" class="btn btn-success ">
                                <i class="icon-checkmark position-left"></i> Agregar
                              </button>
                            </div>
                          </div>
                          <!--accion agregar-->
                          <div class="row">
                            <div class="col-md-12">
                              <div class="table-responsive">
                                <table class="table table-bordered table-striped table-framed table-xxs" id="tblFormacionAcademica">
                                  <thead class="text-primary-800" style="background-color: #EEEEEE;">
                                    <tr>
                                      <th>#</th>
                                      <th>GRADO ESTUDIO</th>
                                      <th>ESTADO ESTUDIO</th>
                                      <th>INSTITUCION</th>
                                      <th>CENTRO DE ESTUDIOS</th>
                                      <th>N° COLEGIATURA</th>
                                      <th>CARRERA</th>
                                      <th>FECHA INICIO</th>
                                      <th>FECHA FIN</th>
                                      <th>ACCION</th>
                                    </tr>
                                  </thead>
                                </table>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </form>
              </div>
            </div>
            <!--DATOS EDUCACION: ESTUDIOS REALIZADOS O INSTRUCCION-->

            <!--DATOS EXPERIENCIA LABORAL-->
            <div class="row">
              <div class="col-md-12">
                <div class="row">
                  <form action="#" name="formExperienciaLaboral" id="formExperienciaLaboral">
                    <div class="col-md-10 col-md-offset-1">
                      <div class="panel so-card-4">
                        <div class="panel-heading bg-blue-800">
                          <h6 class="panel-title text-semibold">
                            <i class="fa fa-briefcase fa-lg"></i>&nbsp;&nbsp;Experiencia laboral
                          </h6>
                          <div class="heading-elements">
                            <div class="form-group tp-fix-switch">
                              <label class="checkbox-inline checkbox-switchery checkbox-right">
                                <input type="checkbox" class="switch" id="chkExperienciaLaboral"  name="chkExperienciaLaboral">
                                <span class="text-bold text-uppercase tp-info-experiencia-laboral">¿Tiene experiencia laboral?</span>
                              </label>
                              <span id="textChkExperienciaLaboral" style="margin-left: 8px;" class="text-bold text-uppercase tp-info-experiencia-laboral">NO</span>
                            </div>
                          </div>
                        </div>
                        <div class="panel-body" id="divExperienciaLaboral">

                          <div class="row">
                            <div class="col-md-3">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold tp-text">Empresa&nbsp;<span class="text-danger"> (*)</span></label>
                                <input maxlength="50" id="txtEmpresaExperienciaLaboral" name="txtEmpresaExperienciaLaboral" type="text" class="form-control">
                              </div>
                            </div>
                            <div class="col-md-3">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold tp-text">Cargo&nbsp;<span class="text-danger"> (*)</span></label>
                                <input maxlength="50" id="txtCargoExperienciaLaboral" name="txtCargoExperienciaLaboral" type="text" class="form-control">
                              </div>
                            </div>
                            <div class="col-md-3">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold tp-text">Fecha inicio&nbsp;<span class="text-danger"> (*)</span></label>
                                <div class="input-group">
                                  <span class="input-group-addon"><i class="icon-calendar"></i></span>
                                  <input id="dpFechaInicioExperienciaLaboral" name="dpFechaInicioExperienciaLaboral" type="text" class="form-control datepicker_es" placeholder="Fecha de inicio &hellip;">
                                </div>
                              </div>
                            </div>
                            <div class="col-md-3">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold tp-text">Fecha fin&nbsp;<span class="text-danger"> (*)</span></label>
                                <div class="input-group">
                                  <span class="input-group-addon"><i class="icon-calendar"></i></span>
                                  <input id="dpFechaFinExperienciaLaboral" name="dpFechaFinExperienciaLaboral" type="text" class="form-control datepicker_es" placeholder="Fecha de inicio &hellip;">
                                </div>
                              </div>
                            </div>
                          </div>
                          <div class="row">
                            <div class="col-md-3">
                              <div class="form-group">
                                <label class="display-block text-uppercase text-semibold tp-text">Teléfono&nbsp;</label>
                                <input id="txtTelefonoExperienciaLaboral" name="txtTelefonoExperienciaLaboral" type="text" class="form-control" maxlength="9">
                              </div>
                            </div>
                          </div>
                          <div class="row">
                            <div class="col-md-12 text-center">
                              <button type="button" id="btnAgregarExperienciaLaboral" class="btn btn-success ">
                                <i class="icon-checkmark position-left"></i> Agregar
                              </button>
                            </div>
                          </div>
                          <div class="row">
                            <div class="col-md-12">
                              <div class="table-responsive">
                                <table class="table table-bordered table-striped table-framed table-xxs" id="tblExperienciaLaboral">
                                  <thead class="text-primary-800" style="background-color: #EEEEEE;">
                                    <tr>
                                      <th>#</th>
                                      <th>EMPRESA</th>
                                      <th>CARGO</th>
                                      <th>FECHA INICIO</th>
                                      <th>FECHA FIN</th>
                                      <th>TÉLEFONO</th>
                                      <th>ACCION</th>
                                    </tr>
                                  </thead>
                                </table>
                              </div>
                            </div>
                          </div>

                        </div>
                      </div>
                    </div>
                  </form>
                </div>
              </div>
            </div>
            <!--DATOS EXPERIENCIA LABORAL-->

            <!--DATOS FONDO PENSION-->
            <div class="row">
              <div class="col-md-12">
                <form action="#" name="formFondoPension" id="formFondoPension">
                  <div class="col-md-6 col-md-offset-3">
                    <div class="panel so-card-4">
                      <div class="panel-heading bg-blue-800">
                        <h6 class="panel-title text-semibold">
                          <i class="fa fa-bookmark fa-lg"></i>&nbsp;&nbsp;Régimen pensionario
                        </h6>
                        <div class="heading-elements">
                          <div class="form-group tp-fix-switch">
                            <label class="checkbox-inline checkbox-switchery checkbox-right">
                              <input type="checkbox" class="switch" checked="checked" id="chkFondoPension"  name="chkFondoPension" checked>
                              <span class="text-bold text-uppercase tp-info-experiencia-laboral">¿Está inscrito en un sistema pensionario?</span>
                            </label>
                            <span id="textChkRegimenPensionario" style="margin-left: 8px;" class="text-bold text-uppercase tp-info-experiencia-laboral">SI</span>
                          </div>
                        </div>
                      </div>
                      <div class="panel-body">

                        <div class="row">
                          <div class="col-lg-12 col-md-12 col-sm-12">
                            <label class="display-block text-uppercase">
                              <u class="tp-info-fondo-pension__title text-bold regimenPensionarioTitulo">Seleccione el sistema pensionario en el que se encuentra actualmente</u> <span class="text-danger">(*)</span>&nbsp;
                            </label>
                            <div id="divFondoPension">  
                            </div>
                            <small class="text-info" style="padding-left: 2.3px;">
                              <a href="../img/AFP_VS_ONP.jpg" data-popup="lightbox"><i class="fa fa-info-circle fa-lg"></i> &nbsp;click para más info</a>
                            </small>
                          </div>
                        </div>

                      </div>
                    </div>
                  </div>
                </form>
              </div>
            </div>
            <!--DATOS FONDO PENSION-->

            <div class="row">
              <div class="col-md-12 text-center">
                <button type="button" id="btnRegistrarFicha" class="btn btn-success btn-xlg text-uppercase text-semibold" style="width: 250px">
                  <i class="icon-file-check position-left" style="font-size: 20px;"></i> Registrar ficha
                </button>
              </div>
            </div>



          </div>
          <!-- /content area -->

        </div>
        <!-- /main content -->

      </div>
      <!-- /page content -->

    </div>
    <!-- /page container -->

    <!-- Core JS files -->
    <script type="text/javascript" src="../plantilla/assets/js/plugins/loaders/pace.min.js"></script>
    <script type="text/javascript" src="../plantilla/assets/js/core/libraries/jquery.min.js"></script>
    <script type="text/javascript" src="../plantilla/assets/js/core/libraries/bootstrap.min.js"></script>
    <script type="text/javascript" src="../plantilla/assets/js/plugins/loaders/blockui.min.js"></script>
    <!-- /core JS files -->

    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/styling/switchery.min.js"></script>
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/styling/uniform.min.js"></script>
    <script type="text/javascript" src="../plantilla/assets/js/core/app.js"></script>

    <!--select-->
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/selects/bootstrap_select.min.js"></script>
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/styling/switch.min.js"></script>
    <!--select-->

    <!--datepicker-->
    <script type="text/javascript" src="../plantilla/assets/js/core/libraries/jquery_ui/widgets.min.js"></script>
    <!--<script type="text/javascript" src="../plantilla/assets/js/pages/jqueryui_forms.js"></script>-->

    <!--fancy box (imagenes modal)-->
    <script type="text/javascript" src="../plantilla/assets/js/pages/components_modals.js"></script>
    <script type="text/javascript" src="../plantilla/assets/js/plugins/media/fancybox.min.js"></script>
    <!--fancy box (imagenes modal)-->

    <!--image uploader-->
    <script src="../js/lib/jasny-bootstrap.min.js" type="text/javascript"></script>
    <!--image uploader-->
    <script src="../plantilla/assets/js/plugins/validateInput/validate_inputs.js" type="text/javascript"></script>
    <!--validInput-->
    <script type="text/javascript" src="../plantilla/assets/js/plugins/validateInput/validate_inputs.js"></script>
    <!--validate-->
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/validation/validate.min.js"></script>
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/validation/additional_methods.min.js"></script>
    <!--validate-->
    <!--select 2-->
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/selects/select2.min.js"></script>
    <!--select 2-->

    <!--datatable-->
    <script type="text/javascript" src="../plantilla/assets/js/plugins/tables/datatables/datatables.min.js"></script>
    <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/selects/select2.min.js"></script>
    <script type="text/javascript" src="../plantilla/assets/js/plugins/tables/datatables/extensions/responsive.min.js"></script>
    <!--datatable-->



    <script src="../js/lib/bootbox.4.4.0.min.js" type="text/javascript"></script>
    <script src="../js/lib/jquery-confirm-master/jquery-confirm.min.js" type="text/javascript"></script>
    <script src="../js/lib/underscore.js" type="text/javascript"></script>
    <script src="../js/lib/smooth-scroll.min.js" type="text/javascript"></script>

    <script src="../js/pages/newRulesValidate.js" type="text/javascript"></script>
    <script src="../js/pages/general.js" type="text/javascript"></script>
    <script src="https://gitcdn.github.io/bootstrap-toggle/2.2.2/js/bootstrap-toggle.min.js"></script>

    <!--libreria maps-->
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&key=AIzaSyCgA2sD34iT4HmBrz1Nj3Pxdg1UQisJRLs&libraries=places&region=ES" defer></script>
    <script src="../js/lib/maps.js" type="text/javascript"></script>
    <!--libreria maps-->

    <script src="../js/lib/simple-input-validate.js" type="text/javascript"></script>
    <script src="../js/pages/ficha.js" type="text/javascript"></script>


  </body>
</html>
<%
    }
  }
%>
