/* global Ladda */

"use strict";
$(document).ready(() => {
  initSelect2();
  criterioBusqueda();
  limpiarFiltro();
  validarFormBuscarFichas();
  reglasFormBuscarFichas();
  listarFichas();
  verDetalleEstadoFicha();
  verDetalleFichaCompletado();
  anularFicha();
  verObservacion();
  imprimirFicha();
  imprimirActividadFicha();
});
// INICIALIZACION
//========================================


let JOCriterioBusqueda = {};
/** inicializa el select */
function initSelect2() {
  $('.select').select2({
    minimumResultsForSearch: Infinity
  });
}

/** inicializa el datepicker */
function initSelectDatepicker() {
  $(".datepicker").prop('readonly', true);
  $(".datepicker").datepicker({
    dateFormat: 'dd/mm/yy',
    showButtonPanel: false,
    onSelect: function () {
      $(this).valid();
    }
  });
  let date = new Date();
  $(".datepicker").datepicker("setDate", date);
  $.datepicker.regional['es'] = {
    closeText: 'Cerrar',
    prevText: '<Ant',
    nextText: 'Sig>',
    currentText: 'Hoy',
    monthNames: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
    monthNamesShort: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],
    dayNames: ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'],
    dayNamesShort: ['Dom', 'Lun', 'Mar', 'Mié', 'Juv', 'Vie', 'Sáb'],
    dayNamesMin: ['Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sá'],
    weekHeader: 'Sm',
    dateFormat: 'dd/mm/yy',
    firstDay: 1,
    isRTL: false,
    showMonthAfterYear: false,
    yearSuffix: ''
  };
  $.datepicker.setDefaults($.datepicker.regional['es']);
}

/**
 * Listar tipo de documento - peticion
 * @returns {JSONObject} JsonObject
 * */
function listarTipoDocumentoRequest() {
  return new Promise((resolve, reject) => {
    $.ajax({
      type: 'POST',
      url: '../TipoDocumentoServlet?accion=listarTipoDocumento',
      beforeSend: function () {
        load("Listando tipo de documentos");
      },
      success: function (data, textStatus, jqXHR) {
        if (textStatus === 'success') {
          unload();
          resolve(data);
        }
      },
      error: function () {
        reject("No se pudo procesar la solicitud de listar tipo de documento");
      }
    });
  });
}

/**
 * Listar tipo estado de fichas - peticion
 * @returns {JSONObject} 
 */
function listarTipoEstadoFichaRequest() {
  return new Promise((resolve, reject) => {
    $.ajax({
      type: 'POST',
      url: '../TipoEstadoFichaServlet',
      data: {accion: 'listarTipoEstadoFicha'},
      beforeSend: function (xhr) {
        load('Listando tipo de estados de ficha');
      },
      success: function (data, textStatus, jqXHR) {
        if (textStatus === 'success') {
          unload();
          resolve(data);
        }
      },
      error: function (jqXHR, textStatus, errorThrown) {
        reject("No se pudo procesar la solicitud de listar tipos de estados de ficha");
      }
    });
  });
}

/**
 * Listar tipo de estados de ficha
 * @argument {int} cboID 
 * */
function listarTipoEstadoFichaResponse(cboID) {
  listarTipoEstadoFichaRequest().then((data) => {
    $('#' + cboID).html(createSelectOptions(data.data.tiposEstadoFicha, 'codigoTipoEstadoFicha', 'nombre'));
  });
}

/**
 * Crea los elementos option del array que se pase como parametro
 * @method createSelectOptions
 * @param {JSONArray} obj array de objetos
 * @param {String} valueName la key del json que se usará como value del elemento option
 * @param {String} textName la key del json que se usará como text del elemento option
 * @returns {Promise} Promise retorna status, msg
 */
let createSelectOptions = (obj, valueName, textName) => {
  let options = '<option value="0">[SELECCIONAR]</option>';
  obj.forEach((data) => {
    options += '<option value="' + data[valueName] + '">' + data[textName] + '</option>';
  });
  return options;
};
/*
 * Lista el tipo de documento
 * @argument {int} cboID 
 * */
function listarTipoDocumentoResponse(cboID) {
  listarTipoDocumentoRequest().then((data) => {
    $('#' + cboID).html(createSelectOptions(data.data.tipodocumentos, 'codigoTipoDocumento', 'descripcionCorta'));
  });
}

/**
 * valida la existencia del tipo de documento <br/>
 * seleccionado
 * @argument {int} codigoTipoDocumento
 * @return {boolean} verdadero|falso 
 * */
function validarExistenciaTipoDocumentoRequest(codigoTipoDocumento) {
  return new Promise((resolve, reject) => {
    $.ajax({
      type: 'POST',
      url: '../TipoDocumentoServlet',
      data: {accion: 'validarExistenciaTipoDocumento', codigoTipoDocumento: codigoTipoDocumento},
      beforeSend: function () {
        load("Validando datos");
      },
      success: function (data, textStatus, jqXHR) {
        if (textStatus === 'success') {
          unload();
          if (data.status) {
            resolve(data.status);
          } else {
            $(location).attr('href', 'templates/close.jsp');
          }
        }
      }, error: function (jqXHR, textStatus, errorThrown) {
        reject("No se pudo procesar la solicitud de validar existencia tipo de documento");
      }
    });
  });
}

/**
 * obtiene la longitud y tipo de entrada <br/>
 * segun tipo de documento seleccionado
 * @argument {int} codigoTipoDocumento 
 * @returns {JSONArray} 
 * */
function obtenerLongitudTipoEntrdadaTipoDocumentoRequest(codigoTipoDocumento) {
  return new Promise((resolve, reject) => {
    $.ajax({
      type: 'POST',
      url: '../TipoDocumentoServlet',
      data: {accion: 'obtenerLongitudTipoEntrdadaTipoDocumento', codigoTipoDocumento: codigoTipoDocumento},
      beforeSend: function () {
        load("Validando datos");
      },
      success: function (data, textStatus, jqXHR) {
        if (textStatus === 'success') {
          unload();
          if (data.status) {
            resolve(data.data.tipodocumentos);
          } else {
            $(location).attr('href', 'templates/close.jsp');
          }
        }
      }, error: function (jqXHR, textStatus, errorThrown) {
        reject("No se pudo procesar la solicitud de obtener longitud y tipo entrada del tipo de documento");
      }
    });
  });
}

// PANEL CRITERIO DE BUSQUEDA
//========================================

/**
 * configuracion de criterio de busqueda <br/>
 * FILTROS: <br/>
 * 1- Tipo de documento <br/>
 * 2- Apellidos <br/>
 * 3- Fecha de registro <br/>
 * */
function criterioBusqueda() {

  $('#cboCriterioBusqueda').change((e) => {

    $(e.currentTarget).valid();
    let codigoCriterioBusqueda = parseInt($('#cboCriterioBusqueda').val());
    if (isNaN(codigoCriterioBusqueda)) {
      $(location).attr('href', 'templates/close.jsp');
    }
    if (codigoCriterioBusqueda === 0) { // [SELECCIONAR]
      $('#divRowFiltro1').html('');
      $('#divRowFiltro2').html('');
    } else if (codigoCriterioBusqueda === 1) { // TIPO DE DOCUMENTO
      HTMLtipoDocumento();
      validarTipoDocumento();
      reglasFormBuscarFichas();
    } else if (codigoCriterioBusqueda === 2) { // APELLIDOS
      HTMLapellidos();
      reglasFormBuscarFichas();
    } else if (codigoCriterioBusqueda === 3) { // FECHA DE REGISTRO
      HTMLfechaRegistro();
      reglasFormBuscarFichas();
    } else if (codigoCriterioBusqueda === 4) { // ESTADO
      HTMLtipoEstadoFicha();
      reglasFormBuscarFichas();
    }
  });
}

/** Dibujar el select tipo de documento */
function HTMLtipoDocumento() {
  let cboTipoDocumento =
    `
        <div class="col-md-4">
            <div class="form-group">
                <label for="cboTipoDocumento" class="text-semibold">Tipo documento <span class="text-danger">*</span></label>
                <select class="select" id="cboTipoDocumento" name="cboTipoDocumento">
                    ${listarTipoDocumentoResponse('cboTipoDocumento')}
                </select>
            </div>
        </div>
            `;
  $('#divRowFiltro1').html(cboTipoDocumento);
  $('#divRowFiltro2').html('');
  $('#cboTipoDocumento').select2({
    minimumResultsForSearch: Infinity
  });
}

/** Dibujar el input de apellidos */
function HTMLapellidos() {
  let txtApellidos =
    `
    <div class="col-md-4">
        <div class="form-group">
            <label for="txtApellidos" class="text-semibold">Apellido paterno <span class="text-danger">*</span></label>
            <input type="text" class="form-control text-uppercase" placeholder="Ingresar apellido paterno" id="txtApellidos" name="txtApellidos" maxlength="30" autocomplete="off">
        </div>
    </div>
            `;
  $('#divRowFiltro1').html(txtApellidos);
  $('#txtApellidos').validCampo('abcdefghijklmnñopqrstuvwxyzáéíóú ');
  $('#divRowFiltro2').html('');
}

/** Dibujar la fecha "desde" y "hasta" */
function HTMLfechaRegistro() {
  let dpFechaRegistroDesde =
    `
    <div class="col-md-4">
        <label for="dpFechaRegistroDesde" class="text-semibold">Desde <span class="text-danger">*</span></label>
        <div class="input-group">
            <span class="input-group-addon"><i class="icon-calendar"></i></span>
            <input type="text" class="form-control datepicker" placeholder="Fecha de registro de ficha" id="dpFechaRegistroDesde" name="dpFechaRegistroDesde" autocomplete="off">
        </div>        
    </div>
            `;
  let dpFechaRegistroHasta =
    `
    <div class="col-md-4">
        <label for="dpFechaRegistroHasta" class="text-semibold">Hasta <span class="text-danger">*</span></label>
        <div class="input-group">
            <span class="input-group-addon"><i class="icon-calendar"></i></span>
            <input type="text" class="form-control datepicker" placeholder="Fecha de registro de ficha" id="dpFechaRegistroHasta" name="dpFechaRegistroHasta" autocomplete="off">
        </div>        
    </div>
            `;
  $('#divRowFiltro1').html(dpFechaRegistroDesde);
  $('#divRowFiltro2').html(dpFechaRegistroHasta);
  initSelectDatepicker();
}

/** Dibujar el select de tipo de estados */
function HTMLtipoEstadoFicha() {
  let cboTipoEstado =
    `
        <div class="col-md-4">
            <div class="form-group">
                <label for="cboTipoEstadoFicha" class="text-semibold">Estado ficha <span class="text-danger">*</span></label>
                <select class="select" id="cboTipoEstadoFicha" name="cboTipoEstadoFicha">
                    ${listarTipoEstadoFichaResponse('cboTipoEstadoFicha')}
                </select>
            </div>
        </div>
            `;
  $('#divRowFiltro1').html(cboTipoEstado);
  $('#divRowFiltro2').html('');
  $('#cboTipoEstadoFicha').select2({
    minimumResultsForSearch: Infinity
  });
  $('#cboTipoEstadoFicha').change((e) => {
    $(e.currentTarget).valid();
  });
}

/** Validar tipo de documento <br/>
 * - existencia de tipo documento <br/>
 * - obtener longitud y tipo entrada */
function validarTipoDocumento() {
  $('#cboTipoDocumento').change((e) => {
    $(e.currentTarget).valid();
    let codigoTipoDocumento = parseInt($('#cboTipoDocumento').val());
    if (isNaN(codigoTipoDocumento)) {
      $(location).attr('href', 'templates/close.jsp');
    }
    if (codigoTipoDocumento === 0) {
      $('#divRowFiltro2').html('');
    } else {
      validarExistenciaTipoDocumentoRequest(codigoTipoDocumento).then(() => {
        obtenerLongitudTipoEntrdadaTipoDocumentoRequest(codigoTipoDocumento).then((tipoDocumento) => {
          HTMLnumeroDocumento(tipoDocumento);
          reglasFormBuscarFichas();
        });
      });
    }
  });
}

/** Dibujar input numero de documento <br> 
 * y configurar segun tipo doc seleccionado 
 * @argument {JSONObject} tipoDocumento 
 * */
let tipoEntrada = '';
function HTMLnumeroDocumento(tipoDocumento) {
  let txtNumeroDocumento =
    `
    <div class="col-md-4">
        <div class="form-group">
            <label for="txtNumeroDocumento" class="text-semibold">N° Documento <span class="text-danger">*</span></label>
            <input type="text" class="form-control text-uppercase" placeholder="Ingresar nro documento" id="txtNumeroDocumento" name="txtNumeroDocumento" minlength="${tipoDocumento[0].longitud}" maxlength="${tipoDocumento[0].longitud}" autocomplete="off">            
        </div>
    </div>
            `;
  $('#divRowFiltro2').html(txtNumeroDocumento);
  tipoEntrada = tipoDocumento[0].tipoEntrada;
  if (tipoDocumento[0].tipoEntrada === 'N') {
    $('#txtNumeroDocumento').validCampo('0123456789');
  } else if (tipoDocumento[0].tipoEntrada === 'A') {
    $('#txtNumeroDocumento').validCampo('abcdefghijklmnopqrstuvwxyz0123456789');
  }
}

/**
 * inicializar reglas del form buscar fichas
 * */
function reglasFormBuscarFichas() {
  $('#cboCriterioBusqueda').rules('add', {
    valueNotEquals: '0'
  });
  $('#cboTipoDocumento').rules('add', {
    valueNotEquals: '0'
  });
  if (tipoEntrada === 'N') {
    $('#txtNumeroDocumento').rules('remove', 'alphanumeric');
    $('#txtNumeroDocumento').rules('add', {
      required: true,
      number: true
    });
  } else if (tipoEntrada === 'A') {
    $('#txtNumeroDocumento').rules('remove', 'number');
    $('#txtNumeroDocumento').rules('add', {
      required: true,
      alphanumeric: true
    });
  }

  $('#txtApellidos').rules('add', {
    required: true,
    lettersonly: true
  });
  $('#dpFechaRegistroDesde').rules('add', {
    required: true,
    dateonly: true
  });
  $('#dpFechaRegistroHasta').rules('add', {
    required: true,
    dateonly: true,
    greaterThan: "#dpFechaRegistroDesde"
  });
  $('#cboTipoEstadoFicha').rules('add', {
    valueNotEquals: '0'
  });
}

/**
 * validar el formulario antes de enviar peticion
 * */
function validarFormBuscarFichas() {
  $('#formBuscarFichas').validate({
    submitHandler: function () {
      listarFichas();
      return false;
    }
  });
}

/**
 * obtener valores criterios de busqueda
 * */
function criterioBusquedaParams() {
  let codigoCriterioBusqueda = parseInt($('#cboCriterioBusqueda').val());
  if (codigoCriterioBusqueda === 1) {
    let codigoTipoDocumento = parseInt($('#cboTipoDocumento').val());
    let numeroDocumento = $('#txtNumeroDocumento').val();
    JOCriterioBusqueda.tipoBusqueda = 'TIPO_DOCUMENTO';
    JOCriterioBusqueda.codigoTipoDocumento = codigoTipoDocumento;
    JOCriterioBusqueda.numeroDocumento = numeroDocumento;
  } else if (codigoCriterioBusqueda === 2) {
    let apellido = $('#txtApellidos').val();
    JOCriterioBusqueda.tipoBusqueda = 'APELLIDOS';
    JOCriterioBusqueda.apellido = apellido;
  } else if (codigoCriterioBusqueda === 3) {
    let fechaDesde = $('#dpFechaRegistroDesde').val();
    let fechaHasta = $('#dpFechaRegistroHasta').val();
    JOCriterioBusqueda.tipoBusqueda = 'FECHA_REGISTRO';
    JOCriterioBusqueda.fechaDesde = fechaDesde;
    JOCriterioBusqueda.fechaHasta = fechaHasta;
  } else if (codigoCriterioBusqueda === 4) {
    let codigoTipoEstadoFicha = parseInt($('#cboTipoEstadoFicha').val());
    JOCriterioBusqueda.tipoBusqueda = 'ESTADO_FICHA';
    JOCriterioBusqueda.codigoTipoEstadoFicha = codigoTipoEstadoFicha;
  } else {
    JOCriterioBusqueda = {};
  }
  return JOCriterioBusqueda;
}

/**
 * Limpiar el filtro realizado en el panel de busqueda por criterio
 */
function limpiarFiltro() {
  Ladda.bind('#btnLimpiarFiltroFichas', {
    callback: function (instance) {
      var progress = 0;
      var interval = setInterval(function () {
        progress = Math.min(progress + Math.random(), 2);
        instance.setProgress(progress);
        if (progress === 2) {
          instance.stop();
          clearInterval(interval);
          $('#divRowFiltro1').html('');
          $('#divRowFiltro2').html('');
          $('#cboCriterioBusqueda').val(0).trigger('change');
          let formBuscarFichas = $('#formBuscarFichas').validate();
          formBuscarFichas.resetForm();
          listarFichas();
        }
      }, 200);
    }
  });
}

// PANEL BUSQUEDA DE FICHAS
//========================================

/**
 * Envia los parametros al servidor <br/>
 * dependiendo los criterios enviados <br/>
 * se listaran las fichas
 * */
function listarFichas() {
  let json = {};
  json = criterioBusquedaParams();
  defaultConfigDataTable();
  $('#tblFichas').dataTable().fnDestroy();
  $('#tblFichas').DataTable({
    ajax: {
      type: 'POST',
      url: '../FichaServlet',
      dataType: 'json',
      data: {
        accion: 'consultarFichasPorCriterio',
        json: JSON.stringify(json)
      },
      error: function (data) {
        errorMessage(data.responseText);
      }
    },
    processing: true,
    serverSide: true,
    responsive: true,
    iDisplayLength: 20,
    columns: [
      {data: "item", className: 'text-bold'},
      {data: "descripcionCortaTipoDocumento"},
      {data: "numeroDocumento"},
      {data: "personal"},
      {data: "correo"},
      {data: "fechaRegistro", className: 'text-center'},
      {data: "estilo", className: 'text-center'},
      {
        data: "estadoFicha",
        className: 'text-center',
        render: (data) => {
          let accion;
          if (data === 'PENDIENTE') {
            accion =
              `
                    <ul class="icons-list">
                        <li class="text-primary-800">
                            <a href="#" onclick="return false" class="verDetalleFicha" data-toggle="tooltip" data-placement="top" title="VER DETALLE"><i class="icon-eye8"></i></a>
                        </li>
                        <li class="text-danger-800">
                            <a href="#" onclick="return false" class="anularFicha" data-toggle="tooltip" title="ANULAR FICHA"><i class="icon-trash"></i></a>
                        </li>
                    </ul>
                                    `;
          } else if (data === 'REGISTRADO') {
            accion =
              `
                    <ul class="icons-list">
                        <li class="text-primary-800">
                            <a href="#" onclick="return false" class="verDetalleFicha" data-toggle="tooltip" data-placement="top" title="VER DETALLE"><i class="icon-eye8"></i></a>
                        </li>
                        <li class="text-slate-800">
                            <a href="#" onclick="return false" class="imprimirFicha" data-toggle="tooltip" title="IMPRIMIR FICHA"><i class="icon-printer2"></i></a>
                        </li>
                    </ul>
                                    `;
          } else if (data === 'COMPLETADO') {
            accion =
              `
                    <ul class="icons-list">
                        <li class="text-primary-800">
                            <a href="#" onclick="return false" class="verDetalleFicha" data-toggle="tooltip" data-placement="top" title="VER DETALLE"><i class="icon-eye8"></i></a>
                        </li>
                        <li class="text-slate-800">
                            <a href="#" onclick="return false" class="imprimirFicha" data-toggle="tooltip" title="IMPRIMIR FICHA"><i class="icon-printer2"></i></a>
                        </li>
                        <li class="text-slate-800">
                            <a href="#" onclick="return false" class="verDetalleFichaCompletado" data-toggle="tooltip" title="VER DETALLE FICHA"><i class="fa fa-file-text-o fa-lg"></i></a>
                        </li>
                    </ul>
                                    `;
          } else if (data === 'FICHA ANULADA') {
            accion =
              `
                    <ul class="icons-list">
                        <li class="text-primary-800">
                          <a href="#" onclick="return false" class="verDetalleFicha" data-toggle="tooltip" data-placement="top" title="VER DETALLE"><i class="icon-eye8"></i></a>
                        </li>
                        <li class="text-blue-800">
                            <a href="#" onclick="return false" class="verObservacion" data-toggle="tooltip" data-placement="top" title="VER OBSERVACION"><i class="icon-info22"></i></a>
                        </li>
                    </ul>
                    `;
          } else {
            accion =
              `
                    <ul class="icons-list">
                        <li class="text-primary-800">
                            <a href="#" onclick="return false" class="verDetalleFicha" data-toggle="tooltip" data-placement="top" title="VER DETALLE"><i class="icon-eye8"></i></a>
                        </li>
                    </ul>
                    `;
          }
          return accion;
        }
      }
    ],
    initComplete: () => {
      $('.verDetalleFicha, .anularFicha, .verObservacion, .imprimirFicha, .verDetalleFichaCompletado').tooltip();
    }
  });
}

/**
 * Realiza la peticion para <br/>
 * listar los detalles de estado <br/>
 * por las que está pasando la ficha <br/>
 * @param {int} codigoFicha
 */
function listarDetalleEstadoFichaRequest(codigoFicha) {
  return new Promise((resolve, reject) => {
    $.ajax({
      type: 'POST',
      url: '../FichaServlet',
      data: {accion: 'listarDetalleEstadoFicha', codigoFicha: codigoFicha},
      beforeSend: function () {
        load("Listando los detalles de estados de la ficha");
      },
      success: function (data, textStatus, jqXHR) {
        if (textStatus === 'success') {
          unload();
          if (data.status) {
            resolve(data.data.detallesEstadoFicha);
          }
        }
      },
      error: function (jqXHR, textStatus, errorThrown) {
        reject("No se pudo procesar la solicitud de listar detalles de estado de ficha");
      }
    });
  });
}

/**
 * permite ver el detalle de los estados <br/>
 * por el cual esta pasando la ficha
 */
let paramCodigoFicha = 0;
function verDetalleEstadoFicha() {
  $('#tblFichas tbody').on('click', '.verDetalleFicha', function (e) {
    $('#modalListadoDetalleEstadoFicha').modal({
      show: true,
      backdrop: 'static',
      keyboard: false
    });
    let data = $('#tblFichas').DataTable().row($(this).parents('tr')).data();
    let codigoFicha = data.codigoFicha;
    paramCodigoFicha = codigoFicha;
    listarDetalleEstadoFichaRequest(codigoFicha).then((data) => {
      let actividades = '';
      let icon = 'icon-checkmark3';
      let style = 'border-success text-success';
      let iconUser = '';
      for (let i in data) {
//                if (data[i].nombreEstado === 'PENDIENTE') {
//                    style = 'border-slate text-slate';
//                    icon = 'icon-loop3';
//                } else if (data[i].nombreEstado === 'REGISTRADO' || data[i].nombreEstado === 'COMPLETADO') {
//                    style = 'border-success text-success';
//                    icon = 'icon-checkmark3';
//                }
        if (parseInt(data[i].codigoUsuario) === 0) {
          iconUser = 'icon-user';
        } else {
          iconUser = 'icon-laptop';
        }
        actividades +=
          `
                            <li class="media">
                                <div class="media-left">
                                    <a href="#" onclick="return false;" class="btn ${style} btn-flat btn-rounded btn-icon btn-xs"><i class="${icon}"></i></a>
                                </div>
                                <div class="media-body">
                                    <u><span class="text-semibold"><i class="${iconUser}"></i> ${data[i].nombreUsuario}</span></u><br/>
                                    <span>${data[i].descripcionEstado}</span><br/>
                                    <span class="text-uppercase">Estado:</span> <span class="text-semibold text-primary-800">${data[i].nombreEstado}</span><br/>
                                    <div class="media-annotation"><i class="fa fa-clock-o"></i> ${data[i].fechaRegistroEstado}</div>
                                </div>
                            </li>
                        `;
      }
      $('#listadoActividades').html(actividades);
    });
  });
}

/**
 * 
 * Permite ver el detalle de la ficha <br/>
 * cuando su estado se encuentra en completado
 */
function verDetalleFichaCompletado() {
  $('#tblFichas tbody').on('click', '.verDetalleFichaCompletado', (e) => {
    let rowData = $('#tblFichas').DataTable().row($(e.currentTarget).parents('tr')).data()
    verDetalleFichaCompletadoRequest(rowData)
      .then((data) => {
        window.name = JSON.stringify(data.data.persona)
        localStorage.setItem('paginaActual', 'consultaFichas')
        window.location = 'fichadetalle.jsp'
      })
  })
}

function verDetalleFichaCompletadoRequest(obj) {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: '../FichaServlet',
      dataType: 'json',
      type: 'POST',
      data: {
        accion: 'listarDetalleFichaPresidencia',
        codigoPersona: obj.codigoPersona,
        codigoFicha: obj.codigoFicha
      }, beforeSend: function (xhr) {

      }, success: function (data, textStatus, jqXHR) {
        if (textStatus === 'success') {
          resolve(data)
        }
      }, error: function (jqXHR, textStatus, errorThrown) {
        reject('Error al obtener detalle ficha completado')
      }
    })

  })
}

/**
 * Accion: anularFicha <br/>
 * Realiza la peticion <br/>
 * y deveuleve la respuesta <br/>
 * @argument {int} codigoFicha 
 * @argument {String} observacion 
 * @returns {boolean} 
 */
function anularFichaRequest(codigoFicha, observacion) {
  return new Promise((resolve, reject) => {
    $.ajax({
      type: 'POST',
      url: '../FichaServlet',
      data: {accion: 'anularFicha', codigoFicha: codigoFicha, observacion: observacion},
      beforeSend: function () {
        load('Anulando Ficha, por favor espere');
      },
      success: function (data, textStatus, jqXHR) {
        if (textStatus === 'success') {
          unload();
          if (data.status) {
            resolve(data.message);
          } else {
            errorMessage(data.message);
          }
        }
      },
      error: function (jqXHR, textStatus, errorThrown) {
        reject("No se pudo procesar la solicitud de anular ficha");
      }
    });
  });
}

/**
 * Anular ficha
 */
function anularFicha() {
  $('#tblFichas tbody').on('click', '.anularFicha', function () {
    let codigoFicha = $('#tblFichas').DataTable().row($(this).parents('tr')).data().codigoFicha;
    $.confirm({
      icon: 'fa fa-question',
      draggable: false,
      animationBounce: 1.5,
      closeAnimation: 'opacity',
      theme: 'modern',
      closeIcon: false,
      animation: 'scale',
      backgroundDismissAnimation: 'glow',
      type: 'red',
      title: '¿Está seguro de anular la ficha?',
      content: `
                    <form action="" class="formName">
                    <div class="form-group">
                    <label class="text-semibold">Ingrese el motivo</label>
                    <textarea id="txtObservacion" class="form-control text-uppercase" rows="5" cols="30" style="resize:none;"></textarea>
                    </div>
                    </form>
                    `,
      buttons: {
        'Sí, estoy seguro': {
          btnClass: 'btn-danger',
          action: () => {
            let observacion = $('#txtObservacion').val().trim();
            if (!observacion) {
              warningMessage('Debe ingresar un motivo por el cual esta anulando la ficha');
              return false;
            }

            anularFichaRequest(codigoFicha, observacion.toUpperCase()).then((data) => {
              successMessage(data);
              listarFichas();
            });
          }
        },
        'Cancelar': {
          btnClass: 'bg-slate'
        }
      }
    });
  });
}

/**
 * ver observacion request <br/>
 * realiza una peticion y trae la observacion <br/>
 * segun la ficha seleccionada (solo ficha ANULADA)
 * @argument {int} codigoFicha 
 * @returns {String} 
 */
function verObservacionRequest(codigoFicha) {
  return new Promise((resolve, reject) => {
    $.ajax({
      type: 'POST',
      url: '../FichaServlet',
      data: {accion: 'verObservacion', codigoFicha: codigoFicha},
      beforeSend: function (xhr) {
        load('Cargando datos, por favor espere');
      },
      success: function (data, textStatus, jqXHR) {
        if (textStatus === 'success') {
          unload();
          let observaciones = data.data.observaciones;
          if (data.status) {
            for (var i in observaciones) {
              resolve(observaciones[i].observacion);
            }
          }
        }
      },
      error: function (jqXHR, textStatus, errorThrown) {
        reject("No se pudo procesar la solicitud de ver observacion");
      }
    });
  });
}

/**
 * funcion ver observacion <br/>
 * abre el modal y visualiza <br/>
 * la descripcion que se le puso a la ficha
 */
function verObservacion() {
  $('#tblFichas tbody').on('click', '.verObservacion', function () {
    let codigoFicha = $('#tblFichas').DataTable().row($(this).parents('tr')).data().codigoFicha;
    verObservacionRequest(codigoFicha).then((data) => {
      let obs = data.split('\n');
      $.alert({
        title: 'Observación',
        icon: 'fa fa-info fa-lg',
        type: 'blue',
        content:
          `<h6>
                  ${obs.map((val) => {
            return '<p>' + val + '</p>';
          }).join('\n')}
                  <h6>
                `
      });
    });
  });
}

/**
 * imprimir fichas
 */
function imprimirFicha() {
  $('#tblFichas tbody').on('click', '.imprimirFicha', function () {
    let codigoPersona = parseInt($('#tblFichas').DataTable().row($(this).parents('tr')).data().codigoPersona);
    let codigoFicha = parseInt($('#tblFichas').DataTable().row($(this).parents('tr')).data().codigoFicha);
    let json = {
      codigoPersona: codigoPersona,
      codigoFicha: codigoFicha
    };
    $('#params').val(JSON.stringify(json));
    $('#formImprimirFicha').submit();
  });
}

/**
 * Imprimir actividade de ficha - (flujo)
 */
function imprimirActividadFicha() {
  $('#btnImprimirActividadFicha').on('click', () => {
    listarDetalleEstadoFichaRequest(paramCodigoFicha).then((data) => {

      let jsonData = {
        arrayActividadFicha: data,
        codigoFicha: paramCodigoFicha
      };

      $('#paramsActividadFicha').val(JSON.stringify(jsonData));

      $('#formImprimirActividadFicha').submit();
    });
  });
}