/* global Infinity, Ladda */

$(document).ready(function () {

  initComponents();

  configuracionBusquedaFichaPendiente();
  validarFormBusquedaFichaPendientes();
  initRulesFormBuscarFichaPendiente();
  listarFichasPendientes();
  verDetalleFichaAdministrativa();
  verDetalleFichaAdministrativaObservadaPorPresidencia();
  buscarFichaPendiente();
  limpiarFiltro();
  // =================================== //
  modalRegistroFicha();
  validarFormModalNuevaFicha();
  initRulesFormModalNuevaFicha();
  registrarNuevaFicha();
  cancelarRegistroFicha();
  // =================================== //
  editarFicha();
  validarConsultaReniec();
  validarActualizarFicha();
  cancelarActualizarFicha();
  // =================================== //
  anularFicha();
  // =================================== //
  verObservacion();
  // =================================== //
  setDefaultMail();
});
function initComponents() {
  initSelect2();
//  initSwitchery();
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
// inicializa el input 'select2'
function initSelect2() {
  $('.select').select2({
    minimumResultsForSearch: Infinity
  });
}

// inicializa el input 'datepicker'
function initSelectDatepicker() {
  $(".datepicker").prop('readonly', true);
  $(".datepicker").datepicker({
    dateFormat: 'dd/mm/yy',
    showButtonPanel: false
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

// =================================================== //

// configuracion dinámico para la busqueda de fichas pendientes
function configuracionBusquedaFichaPendiente() {
  $('#cboCriterioBusqueda').on('change', function () {
    let cboCriterioBusqueda = parseInt($('#cboCriterioBusqueda').val());
    if (isNaN(cboCriterioBusqueda)) {
      $(location).attr('href', 'templates/close.jsp');
    }
    if (cboCriterioBusqueda === 0) {
      $('#divRowFiltro1').html('');
      $('#divRowFiltro2').html('');
    } else if (cboCriterioBusqueda === 1) {
      HTMLtipoDocumento();
      validarTipoDocumento();
      initRulesFormBuscarFichaPendiente();
    } else if (cboCriterioBusqueda === 2) {
      HTMLapellidos();
      initRulesFormBuscarFichaPendiente();
      validarTxtApellido()
    } else if (cboCriterioBusqueda === 3) {
      HTMLfechaRegistro();
      initRulesFormBuscarFichaPendiente();
    } else {
      $('#divRowFiltro1').html('');
      $('#divRowFiltro2').html('');
    }
  });
}

// buscar por tipo de documento
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

// buscar por apellidos
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

// buscar por fecha de registro de ficha
function HTMLfechaRegistro() {
  let dpFechaRegistro =
    `
    <div class="col-md-4">
        <label for="dpFechaRegistro" class="text-semibold">Fecha de registro <span class="text-danger">*</span></label>
        <div class="input-group">
            <span class="input-group-addon"><i class="icon-calendar"></i></span>
            <input type="text" class="form-control datepicker" placeholder="Fecha de registro de ficha" id="dpFechaRegistro" name="dpFechaRegistro" autocomplete="off">
        </div>        
    </div>
            `;
  $('#divRowFiltro1').html(dpFechaRegistro);
  $('#divRowFiltro2').html('');
  initSelectDatepicker();
}

// buscar por numero de documento
function validarTipoDocumento() {
  $('#cboTipoDocumento').on('change', function () {
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
          initRulesFormBuscarFichaPendiente();
          $('#txtNumeroDocumentoNuevaFicha').attr({
            autofocus: true
          });
        });
      });
    }
  });
}

// buscar por numero de documento segun tipo de documento seleccionado
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

// listar tipo de documento - request
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

/*
 * Lista el tipo de documento
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

/**
 * parametros para la busqueda de fichas pendientes
 * */
function criterioBusquedaFichaPendiente() {
  let codigoCriterioBusqueda = 0;
  let codigoTipoDocumento = 0;
  let busqueda = '';
  codigoCriterioBusqueda = parseInt($('#cboCriterioBusqueda').val());
  if (codigoCriterioBusqueda === 1) {
    codigoTipoDocumento = parseInt($('#cboTipoDocumento').val());
    busqueda = $('#txtNumeroDocumento').val().trim();
  } else if (codigoCriterioBusqueda === 2) {
    busqueda = $('#txtApellidos').val().trim();
  } else if (codigoCriterioBusqueda === 3) {
    busqueda = $('#dpFechaRegistro').val().trim();
  }
  let JSONCriterioBusqueda = {
    codigoCriterioBusqueda: codigoCriterioBusqueda,
    codigoTipoDocumento: codigoTipoDocumento,
    busqueda: busqueda
  };
  return JSONCriterioBusqueda;
}


/**
 * accion Listar fichas pendientes
 * */
function listarFichasPendientes() {
  let JSONCriterioBusqueda = criterioBusquedaFichaPendiente();
  defaultConfigDataTable();
  $('#tblListadoFichaPendiente').DataTable({
    processing: true,
    serverSide: true,
    responsive: {
      details: {
        type: 'column'
      }
    },
    ajax: {
      url: '../FichaServlet?accion=listarFichaDT',
      type: 'POST',
      dataType: 'json',
      data: {
        criterio: JSONCriterioBusqueda.codigoCriterioBusqueda,
        tipoDocumento: JSONCriterioBusqueda.codigoTipoDocumento,
        search: JSONCriterioBusqueda.busqueda
      }, error: function (data) {
        errorMessage(data.responseText);
      }
    },
    iDisplayLength: 20,
    columns: [
      {
        data: "item",
        className: "text-bold"
      },
      {
        data: "descripcionCortaTipoDocumento"
      },
      {
        data: "numeroDocumento"
      },
      {
        data: "personal"
      },
      {data: "fechaRegistro"},
      {data: "correo"},
      {
        data: "estilo",
        className: "text-center"
      },
      {
        data: "estadoFicha",
        render: function (data, type, row) {
          let acciones = ``;
          if (data === "PENDIENTE") {
            acciones =
              `
                    <ul class="icons-list">
                        <li class="text-primary-800">
                            <a href="#" onclick="return false" class ="editarFicha" data-toggle="tooltip" title="EDITAR FICHA"><i class="icon-pencil5"></i></a>
                        </li>
                        <li class="text-danger-800">
                            <a href="#" onclick="return false" class="anularFicha" data-toggle="tooltip" title="ANULAR FICHA"><i class="icon-trash"></i></a>
                        </li>
                    </ul>
                    `;
          } else if (data === "REGISTRADO") {
            acciones =
              `
                    <ul class="icons-list">
                        <li class="text-slate-800">
                            <a href="#" onclick="return false" class="verDetalleFichaAdministrativa" data-toggle="tooltip" data-placement="right" title="Ver detalle ficha"><i class="fa fa-file-text-o fa-lg"></i></a>
                        </li>
                    </ul>
                    `;
          } else if (data === "OBSER. SUELDO PRESIDENCIA" || data === "OBSERVADO POR PRESIDENCIA") {
            acciones =
              `
                    <ul class="icons-list">
                        <li class="text-danger-800">
                            <a href="#" onclick="return false" class="verDetalleFichaAdministrativaObservadaPorPresidencia" data-toggle="tooltip" data-placement="top" title="Ver detalle ficha observada por presidencia"><i class="fa fa-file-text-o fa-lg"></i></a>
                        </li>
                    </ul>
                    `;
          } else if (data === 'FICHA ANULADA') {
            acciones =
              `
                    <ul class="icons-list">
                        <li class="text-blue-800">
                            <a href="#" onclick="return false" class="verObservacion" data-toggle="tooltip" data-placement="top" title="VER OBSERVACION"><i class="icon-info22"></i></a>
                        </li>
                    </ul>
                    `;
          }
          return acciones;
        }
      }
    ],
    "fnRowCallback": function (nRow, aData, iDisplayLength) {

      if ($(nRow).hasClass('odd')) {
        $(nRow).css('background-color', '#e8f4f4');
      }
      if (aData.estadoFicha === 'FICHA ANULADA') {
        $(nRow).addClass('text-danger');
      } else {
        $(nRow).removeClass('text-danger');
      }
    },
    initComplete: function (settings, data) {
      $('.editarFicha, .anularFicha, .verDetalleFichaAdministrativa, .verDetalleFichaAdministrativaObservadaPorPresidencia, .verObservacion').tooltip({
        position: {
          my: "center bottom", // the "anchor point" in the tooltip element
          at: "center top" // the position of that anchor point relative to selected element
        },
        hide: {effect: "explode"}
      });
    }
  });
}

/**
 * refrescar tabla de listado de fichas pendientes
 * */
function listarFichasPendientesRefresh() {
  $('#tblListadoFichaPendiente').DataTable().destroy();
  listarFichasPendientes();
}

/**
 * permite ver el detalle de la ficha y asignar datos administrativos
 * */
function verDetalleFichaAdministrativa() {
  $('#tblListadoFichaPendiente tbody').on('click', '.verDetalleFichaAdministrativa', function () {
    let codigoPersona = $('#tblListadoFichaPendiente').DataTable().row($(this).parents('tr')).data().codigoPersona;
    $.ajax({
      url: '../FichaServlet',
      type: 'POST',
      dataType: 'json',
      data: {
        accion: 'listarDatosFicha',
        codigoPersona: codigoPersona
      }, success: function (data, textStatus, jqXHR) {
        if (data.status) {
          window.location = 'fichaAdministrativa.jsp';
          window.name = JSON.stringify(data.data.persona);
        }
      }
    });
  });
}

/**
 * permite ver el detalle de la ficha y asignar datos administrativos
 * */
function verDetalleFichaAdministrativaObservadaPorPresidencia() {
  $('#tblListadoFichaPendiente tbody').on('click', '.verDetalleFichaAdministrativaObservadaPorPresidencia', function () {
    let codigoPersona = $('#tblListadoFichaPendiente').DataTable().row($(this).parents('tr')).data().codigoPersona;
    let codigoFicha = $('#tblListadoFichaPendiente').DataTable().row($(this).parents('tr')).data().codigoFicha;
    $.ajax({
      url: '../FichaServlet',
      type: 'POST',
      dataType: 'json',
      data: {
        accion: 'listarDatosAdministrativosFicha',
        codigoPersona: codigoPersona,
        codigoFicha: codigoFicha
      }, success: function (data, textStatus, jqXHR) {
        if (data.status) {
          window.location = 'fichaObservadaPorPresidencia.jsp';
          window.name = JSON.stringify(data.data.persona);
        }
      }
    });
  });
}

/**
 * inicia las reglas para el formuarlio de busqueda <br/>
 * */
function initRulesFormBuscarFichaPendiente() {
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
  $('#dpFechaRegistro').rules('add', {
    required: true,
    dateonly: true
  });
}

// validar panel de busqueda de fichas pendientes (jquery validate)
function validarFormBusquedaFichaPendientes() {
  let formBusquedaFichaPendiente = $('#formBusquedaFichaPendiente');
  return formBusquedaFichaPendiente.valid();
}

/**
 * valida el form - success y lista
 * */
function buscarFichaPendiente() {
  $('#btnBusquedaFichaPendiente').on('click', function () {
    if (validarFormBusquedaFichaPendientes()) {

      listarFichasPendientesRefresh();
    }
  });
}

function validarTxtApellido() {

}

/**
 * limpar el filtro que actualmente esta en uso
 * */
function limpiarFiltro() {
//    Ladda.bind('#btnLimpiarFiltroFichaPendiente', {
//        dataSpinnerSize: 16,
//        timeout: 1000
//    });
  Ladda.bind('#btnLimpiarFiltroFichaPendiente', {
    callback: function (instance) {
      let progress = 0;
      let interval = setInterval(function () {
//                progress = Math.min(progress + Math.random() * 0.1, 1);
        progress = Math.min(progress + Math.random(), 2);
        instance.setProgress(progress);
        if (progress === 2) {
          instance.stop();
          clearInterval(interval);
          $('#divRowFiltro1').html('');
          $('#divRowFiltro2').html('');
          $('#cboCriterioBusqueda').val(0).trigger('change');
          let formBusquedaFichaPendiente = $('#formBusquedaFichaPendiente').validate();
          formBusquedaFichaPendiente.resetForm();
          listarFichasPendientesRefresh();
        }
      }, 200);
    }
  });
}

// ======================================================= //
//******************************************************//
//******************* NUEVA FICHA *********************//
//******************************************************//

/**
 * abre el modal e inicializa los metodos que se encuentran en él
 */
function modalRegistroFicha() {
  $('#btnAbrirModalRegistrarNuevaFicha').on('click', function () {
    $('#modalRegistrarNuevaFicha').modal({
      show: true,
      backdrop: 'static',
      keyboard: false
    });
  });

  listarTipoDocumentoResponse('cboTipoDocumentoNuevaFicha');
  validarInputs('bloquearInputs');
  validarTipoDocumentoSeleccionadoModalNuevaFicha();

  $('#modalRegistrarNuevaFicha').on('show.bs.modal', function () {
    initSwitchery();
  });
}

/**
 * Permite realizar accion a los inputs del modal segun parametro a enviar 
 * @argument {String} accion 
 * */
function validarInputs(accion) {
// limpieza de inputs
  if (accion === 'limpiarInputs') { // limpia los inputs del modal
    $('#txtNumeroDocumentoNuevaFicha').val('');
    $('#txtCorreoElectronicoNuevaFicha').val('');
  } else if (accion === 'limpiarNumeroDocumento') { // limpia solo numero documento
    $('#txtNumeroDocumentoNuevaFicha').val('');
  } else if (accion === 'limpiarCorreo') { // limpiar solo correo
    $('#txtCorreoElectronicoNuevaFicha').val('');
  }
// bloque de inputs
  else if (accion === 'bloquearInputs') { // bloquea los inputs del modal
    $('#txtNumeroDocumentoNuevaFicha').attr('disabled', true);
    $('#txtCorreoElectronicoNuevaFicha').attr('disabled', true);
  } else if (accion === 'bloquearNumeroDocumento') { // bloquea solo numero documento
    $('#txtNumeroDocumentoNuevaFicha').attr('disabled', true);
  } else if (accion === 'bloquearCorreo') { // bloquea solo correo
    $('#txtCorreoElectronicoNuevaFicha').attr('disabled', true);
  } else if (accion === 'bloquearBotonRENIEC') { // bloque el boton de consulta reniec
    $('#btnValidarConReniec').attr('disabled', true);
  }
// deshabilitar inputs
  else if (accion === 'deshabilitarInputs') { // deshabilita los inputs
    $('#txtNumeroDocumentoNuevaFicha').attr('disabled', false);
    $('#txtCorreoElectronicoNuevaFicha').attr('disabled', false);
  } else if (accion === 'deshabilitarNumeroDocumento') { // deshabilita solo numero documento
    $('#txtNumeroDocumentoNuevaFicha').attr('disabled', false);
  } else if (accion === 'deshabilitarCorreo') { // deshabilita solo correo
    $('#txtCorreoElectronicoNuevaFicha').attr('disabled', false);
  } else if (accion === 'bloquearBotonRENIEC') { // bloquear el boton de consulta reniec
    $('#btnValidarConReniec').attr('disabled', true);
  } else if (accion === 'deshabilitarBotonRENIEC') { // deshabilita el boton de consulta reniec
    $('#btnValidarConReniec').attr('disabled', false);
  }
}

/**
 * validar el tipo de documento segun opcion seleccionada
 */
function validarTipoDocumentoSeleccionadoModalNuevaFicha() {
  $('#cboTipoDocumentoNuevaFicha').on('change', function () {
    let codigoTipoDocumento = parseInt($('#cboTipoDocumentoNuevaFicha').val());
    if (isNaN(codigoTipoDocumento)) {
      $(location).attr('href', 'templates/close.jsp');
    }
    construirInputNumeroDocumentoDNI(codigoTipoDocumento);
    $('#divRespuestaReniec').html('');
    flagValidacionReniec = false;
  });
}

/**
 * valida el tipo de entrada según tipo de documento seleccionado
 * @argument {Json Array} tipoDocumento 
 * */
let tipoEntradaModalNuevaFicha = '';
function validarTipoEntradaSeleccionadoModalNuevaFicha(tipoDocumento) {
  validarInputs('deshabilitarNumeroDocumento');
  validarInputs('limpiarCorreo');
  validarInputs('bloquearCorreo');
  $('#txtNumeroDocumentoNuevaFicha').attr({
    minlength: tipoDocumento[0].longitud,
    maxlength: tipoDocumento[0].longitud
  });
  if (tipoDocumento[0].tipoEntrada === 'N') { // numérico
    tipoEntradaModalNuevaFicha = 'N';
    $('#txtNumeroDocumentoNuevaFicha').val('');
    $('#txtNumeroDocumentoNuevaFicha').validCampo('0123456789');
    initRulesFormModalNuevaFicha();
  } else if (tipoDocumento[0].tipoEntrada === 'A') { // alfanumérico
    tipoEntradaModalNuevaFicha = 'A';
    $('#txtNumeroDocumentoNuevaFicha').val('');
    $('#txtNumeroDocumentoNuevaFicha').validCampo('abcdefghijklmnopqrstuvwxyz0123456789');
    initRulesFormModalNuevaFicha();
  }
}

/**
 * construye el input cuando el tipo de documento sea 1 (L.E/DNI) <br/>
 * si no es 1 entonces construira un input normal para todos
 * @argument {int} codigoTipoDocumento
 * */
function construirInputNumeroDocumentoDNI(codigoTipoDocumento) {
  let inputNumeroDocumento;
  if (codigoTipoDocumento === 0) {
    inputNumeroDocumento =
      `
                    <label for="txtNumeroDocumentoNuevaFicha" class="text-semibold">Número de documento <span class="text-danger">*</span></label>
                    <input type="text" class="form-control text-uppercase" placeholder="Número de documento" id="txtNumeroDocumentoNuevaFicha" name="txtNumeroDocumentoNuevaFicha" autocomplete="off">
                `;
    $('#divNumeroDocumentoModalNuevaFicha').html(inputNumeroDocumento);
    validarInputs('limpiarInputs');
    validarInputs('bloquearInputs');
    validarFormModalNuevaFicha();
  } else {
    validarExistenciaTipoDocumentoRequest(codigoTipoDocumento).then(() => {
      obtenerLongitudTipoEntrdadaTipoDocumentoRequest(codigoTipoDocumento).then((tipoDocumento) => {
        if (tipoDocumento[0].codigoTipoDocumento === 1) {
          inputNumeroDocumento =
            `
                    <label for="txtNumeroDocumentoNuevaFicha" class="text-semibold">Número de documento <span class="text-danger">*</span></label>
                    <div class="input-group">
                        <input type="text" class="form-control text-uppercase" placeholder="Ingrese su L.E/DNI" id="txtNumeroDocumentoNuevaFicha" name="txtNumeroDocumentoNuevaFicha" autocomplete="off">
                        <span class="input-group-btn" data-popup="tooltip" title="" data-placement="left" data-original-title="Left tooltip">
                            <button class="btn bg-blue-800" type="button" id="btnValidarConReniec"><i class="fa fa-search fa-lg position-left"></i>VALIDAR</button>
                        </span>
                    </div>
                `;
        } else {
          inputNumeroDocumento =
            `
                    <label for="txtNumeroDocumentoNuevaFicha" class="text-semibold">Número de documento <span class="text-danger">*</span></label>
                    <input type="text" class="form-control text-uppercase" placeholder="Número de documento" id="txtNumeroDocumentoNuevaFicha" name="txtNumeroDocumentoNuevaFicha" autocomplete="off">
                `;
        }
        $('#divNumeroDocumentoModalNuevaFicha').html(inputNumeroDocumento);
        validarTipoEntradaSeleccionadoModalNuevaFicha(tipoDocumento);
        validarConsultaRENIEC(tipoDocumento);
        validarFormModalNuevaFicha();
      });
    });
  }
}

/**
 * valida la longitud del numero de documento <br/>
 * y activa el boton para realizar la consulta con RENIEC
 * @argument {JSON Array} tipoDocumento 
 * */
function validarConsultaRENIEC(tipoDocumento) {
  if (tipoDocumento[0].codigoTipoDocumento === 1) {
    validarInputs('bloquearBotonRENIEC');
    $('#txtNumeroDocumentoNuevaFicha').on('keyup', function () {
      let longitud = $('#txtNumeroDocumentoNuevaFicha').val().length;
      if (longitud === 8 && validarFormModalNuevaFicha()) {
        validarInputs('deshabilitarBotonRENIEC');
        validarConsultaRENIECAccion();
      } else {
        validarInputs('bloquearBotonRENIEC');
        validarInputs('bloquearCorreo');
        $('#divRespuestaReniec').html('');
      }
    });
  } else {
    $('#txtNumeroDocumentoNuevaFicha').on('keyup', function () {
      let longitud = $('#txtNumeroDocumentoNuevaFicha').val().length;
      if (longitud === parseInt(tipoDocumento[0].longitud)) {
        validarInputs('deshabilitarCorreo');
      } else {
        validarInputs('bloquearCorreo');
      }
    });
  }
}

/**
 * valida la consulta de reniec <br/>
 * si ya ha hecho una consulta hara lo siguiente: <br/>
 * 1- bloquear numero documento <br/>
 * 2- cambiar el nombre del boton "VALIDAR" a "VALIDAR OTRO" <br/>
 * 3- cambiar el estilo del boton "VALIDAR"
 * */
function validarConsultaRENIECAccion() {
  let flag = true;
  $('#btnValidarConReniec').off('click');
  $('#btnValidarConReniec').on('click', function () {
    if ($('#txtNumeroDocumentoNuevaFicha').val().length === 8 && validarFormModalNuevaFicha()) {
      if (flag) {
        flag = false;
        validarInputs('bloquearNumeroDocumento');
        $('#btnValidarConReniec').removeClass('bg-blue-800').addClass('bg-slate-600');
        $('#btnValidarConReniec').html('<i class="fa fa-repeat fa-lg position-left"></i>VALIDAR OTRO');
        ConsultarRENIEC($('#txtNumeroDocumentoNuevaFicha').val());
      } else {
        flag = true;
        validarInputs('deshabilitarNumeroDocumento');
        validarInputs('limpiarNumeroDocumento');
        validarInputs('bloquearBotonRENIEC');
        $('#btnValidarConReniec').removeClass('bg-slate-600').addClass('bg-blue-800');
        $('#btnValidarConReniec').html('<i class="fa fa-search fa-lg position-left"></i>VALIDAR');
        $('#divRespuestaReniec').html('');
        flagValidacionReniec = false;
      }
    }
  });
}

/**
 * @description realiza una petición al servidor de la reniec <br/>
 * y valida que el dni le pertenezca a una persona +18
 * @param {JSONArray} dni
 * @returns {boolean} 
 */
let apellidoPaterno, apellidoMaterno, nombres, flagValidacionReniec = false;
function ConsultarRENIEC(dni) {
//  let token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjRmNTZiOWI1ODczYjM0NTI3MjEzNjA5YzcwMWMwM2Y0MDlmMjQ1MTA1MmQyYjdhYWM0ZDYyMWM3ODkzMWZjMzk1MjY0NmUwODdkOGFiOTA5In0.eyJhdWQiOiIxIiwianRpIjoiNGY1NmI5YjU4NzNiMzQ1MjcyMTM2MDljNzAxYzAzZjQwOWYyNDUxMDUyZDJiN2FhYzRkNjIxYzc4OTMxZmMzOTUyNjQ2ZTA4N2Q4YWI5MDkiLCJpYXQiOjE1MjI5NTk3OTksIm5iZiI6MTUyMjk1OTc5OSwiZXhwIjoxNTU0NDk1Nzk5LCJzdWIiOiIxNzI1Iiwic2NvcGVzIjpbInVzZS1yZW5pZWMiXX0.czq534cPCIyTD5Ne4k7ReBWvgOf98UD328w_wYJI411k8p5xPo9NXvKxaylNTd_VvBYxFTRfd4qYxZvRTq95Va8Ylb6lKDkGvpnDeJVfaaRbZqopT0VzH6xhDhTGze71XnHCZ9NKjGve9oS_AVyMCPv-PFphSae4o-F_WoirTidQAQi97Os8uzFdbq0e2AzdfV9I1G3edwcipGE0EJuIKEEUqPaNupWvZ6fADcXon5eNeVSHVcB7RqfO6E4xQYGpFXxlW7ooQ_QekdP4eIv93g1TwU1Ku86pCJtOsHZ6Libv7Tb62qODn7A693x_3h0DcfglaNutDfiZwcmutx_NiuafYhj92gKzVqEEO3_CmClN9oZgyG6SFTXfO75HxcDAulgTujLFXeVWhc8pFO59f-O3KDcbp0QRaRT_Hx_x7Y6NwCGyol67fPrNAzGoGvxidRkILHwQfqesbayA2oe-W8it_qOIGNKt3u6cRQbvtu0S5Hf8LUs789fVszHxAQphw3g-xjta3L-j-uPkr73dyeHgZCJY9N0vl2dxGj0_M1TVBKySaH6qmt1vcBW8iZpp_flb3PdECs9E69CiAtuS1gastt2LaArxfUCqKwUt14JN8lS1Wjdp3Hv5au2PXjTKXXmf9yFZJQQ2zSmpvD5ygZdw_DVFQGqhxrj_gwZ2ay4";
  let respuesta = ``;
  load('Conectándose con RENIEC');

  const url = "https://tecactus.com/api/reniec/dni";

  const json = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjRmNTZiOWI1ODczYjM0NTI3MjEzNjA5YzcwMWMwM2Y0MDlmMjQ1MTA1MmQyYjdhYWM0ZDYyMWM3ODkzMWZjMzk1MjY0NmUwODdkOGFiOTA5In0.eyJhdWQiOiIxIiwianRpIjoiNGY1NmI5YjU4NzNiMzQ1MjcyMTM2MDljNzAxYzAzZjQwOWYyNDUxMDUyZDJiN2FhYzRkNjIxYzc4OTMxZmMzOTUyNjQ2ZTA4N2Q4YWI5MDkiLCJpYXQiOjE1MjI5NTk3OTksIm5iZiI6MTUyMjk1OTc5OSwiZXhwIjoxNTU0NDk1Nzk5LCJzdWIiOiIxNzI1Iiwic2NvcGVzIjpbInVzZS1yZW5pZWMiXX0.czq534cPCIyTD5Ne4k7ReBWvgOf98UD328w_wYJI411k8p5xPo9NXvKxaylNTd_VvBYxFTRfd4qYxZvRTq95Va8Ylb6lKDkGvpnDeJVfaaRbZqopT0VzH6xhDhTGze71XnHCZ9NKjGve9oS_AVyMCPv-PFphSae4o-F_WoirTidQAQi97Os8uzFdbq0e2AzdfV9I1G3edwcipGE0EJuIKEEUqPaNupWvZ6fADcXon5eNeVSHVcB7RqfO6E4xQYGpFXxlW7ooQ_QekdP4eIv93g1TwU1Ku86pCJtOsHZ6Libv7Tb62qODn7A693x_3h0DcfglaNutDfiZwcmutx_NiuafYhj92gKzVqEEO3_CmClN9oZgyG6SFTXfO75HxcDAulgTujLFXeVWhc8pFO59f-O3KDcbp0QRaRT_Hx_x7Y6NwCGyol67fPrNAzGoGvxidRkILHwQfqesbayA2oe-W8it_qOIGNKt3u6cRQbvtu0S5Hf8LUs789fVszHxAQphw3g-xjta3L-j-uPkr73dyeHgZCJY9N0vl2dxGj0_M1TVBKySaH6qmt1vcBW8iZpp_flb3PdECs9E69CiAtuS1gastt2LaArxfUCqKwUt14JN8lS1Wjdp3Hv5au2PXjTKXXmf9yFZJQQ2zSmpvD5ygZdw_DVFQGqhxrj_gwZ2ay4'
    },
    body: JSON.stringify({dni: dni})
  }

  fetch(url, json)
    .then((response) => {
      return response.json();
    })
    .then((data) => {
      if (data === 'DNI no existe.') {
        respuesta =
          `
                                    <div class="alert alert-danger alert-styled-left alert-arrow-left alert-component">
                                        <h6 class="alert-heading text-semibold text-uppercase">¡Oh, oh - EL DNI NO EXISTE!</h6>
                                        ${data} <br>
                                        <span class="text-bold">Por favor, ingrese un DNI válido e inténtelo de nuevo</span>
                                    </div>
                                `;
        $('#divRespuestaReniec').html(respuesta);
        validarInputs('bloquearCorreo');
        flagValidacionReniec = false;
      } else if (data === "El DNI pertenece a un menor de edad.") {
        respuesta =
          `
                                    <div class="alert alert-warning alert-styled-left alert-arrow-left alert-component">
                                        <h6 class="alert-heading text-semibold text-uppercase">¡Oh, oh - EL DNI LE PERTENECE A UN MENOR!</h6>
                                        ${data} <br/>
                                        <span class="text-bold">Por favor, ingrese un DNI válido (+18) e inténtelo de nuevo</span>
                                    </div>
                                `;
        $('#divRespuestaReniec').html(respuesta);
        validarInputs('bloquearCorreo');
        flagValidacionReniec = false;
      } else {
        respuesta =
          `
                                    <div class="alert alert-success alert-styled-left alert-arrow-left alert-component">
                                        <h6 class="alert-heading text-uppercase">¡Enhorabuena!, la persona ha sido reconocida por la reniec</h6>
                                        <span class="text-semibold">${data.nombres} ${data.apellido_paterno} ${data.apellido_materno}</span>
                                        <br/>
                                    </div>
                                `;
        $('#divRespuestaReniec').html(respuesta);
        validarInputs('deshabilitarCorreo');
        apellidoPaterno = data.apellido_paterno;
        apellidoMaterno = data.apellido_materno;
        nombres = data.nombres;
        flagValidacionReniec = true;

      }
      unload();
    })
    .catch((err) => {
      errorMessage(`
                            ERROR PETICIÓN RENIEC <br/>
                            mensaje: ${err} <br/>
                            <hr/>
                            asegúrese de haber ingresado un dni correcto e inténtelo de nuevo. <br/>
                            refrescar la página (F5)
                         `);
      flagValidacionReniec = false;

    });
}

/**
 * definicion de reglas para los inputs del modal
 * */
function initRulesFormModalNuevaFicha() {
  $('#cboTipoDocumentoNuevaFicha').rules('add', {
    valueNotEquals: '0'
  });
  if (tipoEntradaModalNuevaFicha === 'N') {
    $('#txtNumeroDocumentoNuevaFicha').rules('remove', 'alphanumeric');
    $('#txtNumeroDocumentoNuevaFicha').rules('add', {
      required: true,
      number: true
    });
  } else if (tipoEntradaModalNuevaFicha === 'A') {
    $('#txtNumeroDocumentoNuevaFicha').rules('remove', 'number');
    $('#txtNumeroDocumentoNuevaFicha').rules('add', {
      required: true,
      alphanumeric: true
    });
  }
  $('#txtCorreoElectronicoNuevaFicha').rules('add', {
    required: true,
    email: true
  });
}

/**
 * valida que el formuario sea correcto
 * @returns {boolean} 
 * */
function validarFormModalNuevaFicha() {
  let formNuevaFicha = $('#formNuevaFicha');
  return formNuevaFicha.valid();
}

/**
 * cancelar actualizacion de ficha
 */
function cancelarRegistroFicha() {
  $('#btnCancelarRegistarNuevaFicha').on('click', () => {
    $('#divRespuestaReniec').html('');
    $('#cboTipoDocumentoNuevaFicha').val('0').trigger('change');
    validarInputs('limpiarInputs');
    validarInputs('bloquearInputs');
    let formValidate = $('#formNuevaFicha').validate();
    formValidate.resetForm();
  });
}

/**
 * verificar existencia de fichas anuladas
 * @argument {int} codigoTipoDocumento 
 * @argument {int} numeroDocumento
 */
function verificarExistenciaFichaAnuladaRequest(codigoTipoDocumento, numeroDocumento) {
  return new Promise((resolve, reject) => {
    $.ajax({
      type: 'POST',
      url: '../FichaServlet',
      data: {
        accion: 'verificarExistenciaFichaAnulada',
        codigoTipoDocumento: codigoTipoDocumento,
        numeroDocumento: numeroDocumento
      },
      beforeSend: function (xhr) {
        load('validando ficha, por favor espere');
      },
      success: function (data, textStatus, jqXHR) {
        if (textStatus === 'success') {
          unload();
          resolve(data.status);
        }
      },
      error: function (jqXHR, textStatus, errorThrown) {
        reject("No se pudo procesar la solicitud de verificacion de existencia de fichas anuladas => " + textStatus);
      }
    });
  });
}

/**
 * obtiene el codigo de persona
 * @argument {int} codigoTipoDocumento segun tipo de documento seleccionado
 * @argument {int} numeroDocumento numero de documento ingresado
 */
function obtenerCodigoPersonaPorTipoDocNroDoc(codigoTipoDocumento, numeroDocumento) {
  return new Promise((resolve, reject) => {
    $.ajax({
      type: 'POST',
      url: '../FichaServlet',
      data: {
        accion: 'obtenerCodigoPersonaPorTipoDocNroDoc',
        codigoTipoDocumento: codigoTipoDocumento,
        numeroDocumento: numeroDocumento
      },
      success: function (data, textStatus, jqXHR) {
        if (textStatus === 'success') {
          if (data.status) {
            resolve(data.data.getResultedKey);
          } else {
            errorMessage(data.message);
          }
        }
      },
      error: function (jqXHR, textStatus, errorThrown) {
        reject("No se pudo procesar la solicitud de obtener codigo de persona por tipoDoc y nroDoc => " + textStatus);
      }
    });
  });
}

/**
 * habilitar ficha
 * @argument {int} codigoPersona
 * @argument {String} correoElectronico
 */
function habilitarFichaRequest(codigoPersona, correoElectronico) {
  return new Promise((resolve, reject) => {
    $.ajax({
      type: 'POST',
      url: '../FichaServlet',
      data: {
        accion: 'habilitarFicha',
        codigoPersona: codigoPersona,
        correoElectronico: correoElectronico
      },
      beforeSend: function (xhr) {
        load('Registrando ficha, por favor espere');
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
        reject("No se pudo procesar la solicitud de habilitar ficha => " + textStatus);
      }
    });
  });
}

/**
 * valida si hay una ficha activa
 * SI = no permitir registrar una nueva ficha
 * NO = permitir registrar una nueva ficha
 * @argument {int} codigoPersona 
 * @returns {boolean} 
 */
function validarFichaActivaRequest(codigoPersona) {
  return new Promise((resolve, reject) => {
    $.ajax({
      type: 'POST',
      url: '../FichaServlet',
      data: {accion: 'validarFichaActiva', codigoPersona: codigoPersona},
      success: function (data, textStatus, jqXHR) {
        if (textStatus === 'success') {
          unload();
          resolve(data);
        }
      },
      error: function (jqXHR, textStatus, errorThrown) {
        reject("No se pudo procesar la solicitud de validar ficha activa => " + textStatus);
      }
    });
  });
}

/**
 * valida que el numero de documento ingresado  <br/>
 * no haya sido registrado anteriormente
 * @argument {int} codigoTipoDocumento 
 * @argument {String} numeroDocumento 
 * * @argument {int} longitud 
 * * @argument {String} tipoEntrada 
 * @returns {boolean} 
 * */
function validarExistenciaNumeroDocumentoRequest(codigoTipoDocumento, numeroDocumento, longitud, tipoEntrada) {
  return new Promise((resolve, reject) => {
    $.ajax({
      type: 'POST',
      url: '../PersonaServlet?accion=validarExistenciaNumeroDocumento',
      data: {codigoTipoDocumento: codigoTipoDocumento, numeroDocumento: numeroDocumento, longitud: longitud, tipoEntrada: tipoEntrada},
      dataType: 'json',
      beforeSend: function () {
        load("Validando número de documento ingresado");
      },
      success: function (data, textStatus, jqXHR) {
        if (textStatus === 'success') {
          unload();
          if (!data.status) {
            errorMessage(data.message);
          } else {
            resolve(data.status);
          }
        }
      },
      error: function () {
        reject("No se pudo procesar la solicitud de validar numero de documento");
      }
    });
  });
}

/**
 * valida que el correo electrónico ingresado
 * no haya sido registrado anteriormente
 * @argument {String} correoElectronico 
 * * @returns {boolean} 
 * */
function validarExistenciaCorreoElectronicoRequest(correoElectronico) {
//    let correoElectronico = $('#txtCorreoElectronicoNuevaFicha').val().trim();
  return new Promise((resolve, reject) => {
    $.ajax({
      type: 'POST',
      url: '../PersonaServlet?accion=validarExistenciaCorreoElectronico',
      data: {correoElectronico: correoElectronico},
      dataType: 'json',
      beforeSend: function () {
        load("Validando correo electrónico ingresado");
      },
      success: function (data, textStatus, jqXHR) {
        if (textStatus === 'success') {
          unload();
          if (!data.status) {
            errorMessage(data.message);
          } else {
            resolve(data.status);
          }
        }
      },
      error: function () {
        reject("No se pudo procesar la solicitud de validar el correo eletronico");
      }
    });
  });
}

/**
 * registra una nueva ficha con estado pendiente
 * asimimo envia un correo electronico con el cual se registro
 * @argument {JSONArray} json 
 * */
function registrarNuevaFichaRequest(json) {
  return new Promise((resolve, reject) => {
    $.ajax({
      type: 'POST',
      url: '../PersonaServlet?accion=registrarNuevaFicha',
      data: {json: JSON.stringify(json)},
      dataType: 'json',
      beforeSend: function () {
        load("Registrando nueva ficha, espere por favor");
      },
      success: function (data, textStatus, jqXHR) {
        unload();
        if (!data.status) {
          errorMessage(data.message);
        } else {
          resolve(data);
        }
      },
      error: function () {
        reject("No se pudo procesar la solicitud de registrar la ficha");
      }
    });
  });
}

/**
 * permite resear todo el modal cuando <br/>
 * el registro haya sido satisfactorio
 * */
function resetFormModalFichaNueva() {
  $('#modalRegistrarNuevaFicha').modal('hide');
  $('#divRespuestaReniec').html('');
  flagValidacionReniec = false;
  apellidoPaterno = '';
  apellidoPaterno = '';
  nombres = '';
  $('#cboTipoDocumentoNuevaFicha').val('0').trigger('change');
  validarInputs('limpiarInputs');
  validarInputs('bloquearInputs');


  let formValidate = $('#formNuevaFicha').validate();
  formValidate.resetForm();
}

/**
 * registrar nueva ficha
 * */
function registrarNuevaFicha() {
  $('#btnRegistrarNuevaFicha').on('click', function () {

    if (validarFormModalNuevaFicha()) {
      let codigoTipoDocumento = parseInt($('#cboTipoDocumentoNuevaFicha').val());
      let numeroDocumento = $('#txtNumeroDocumentoNuevaFicha').val().trim();
      let correoElectronico = $('#txtCorreoElectronicoNuevaFicha').val().trim();
      let isDefaultMail = parseInt($('#statusCorreoValue').val());
      if (codigoTipoDocumento === 1) {
        if (flagValidacionReniec) {
          // verifica si hay una ficha anulada con los datos ingresados
          verificarExistenciaFichaAnuladaRequest(codigoTipoDocumento, numeroDocumento).then((fichaAnuladaData) => {
            if (fichaAnuladaData) { // encontro una ficha anulada
              $.confirm({
                icon: 'fa fa-warning',
                theme: 'material',
                closeIcon: true,
                animation: 'scale',
                type: 'dark',
                draggable: false,
                backgroundDismissAnimation: 'glow',
                title: 'Confirmar',
                content:
                  `
                      <span class="text-muted">A este personal anteriormente se le anuló una ficha</span> <br/> 
                      <span class="text-semibold">¿Desea registrar una nueva ficha?</span> <br/> 
                      <span class="text-danger">Asegúrese de haber ingresado los datos correctamente</span>
                    `,
                buttons: {
                  Registrar: {
                    btnClass: 'btn-success',
                    action: function () {
                      /* registrar ficha aun asi tenga una ficha anulada anteriormente (implementar logica)  */
                      obtenerCodigoPersonaPorTipoDocNroDoc(codigoTipoDocumento, numeroDocumento).then((codigoPersona) => {
                        validarFichaActivaRequest(codigoPersona).then((data) => {
                          if (data.status) { // hay ficha activa y no registrar ficha
                            errorMessage(data.message);
                          } else { // no hay fichas activas
                            habilitarFichaRequest(codigoPersona, correoElectronico).then((data) => {
                              listarFichasPendientesRefresh();
                              successMessage(data);
                              resetFormModalFichaNueva();
                            });
                          }
                        });
                      });
                    }
                  },
                  Cancelar: {
                    btnClass: 'btn-danger'
                  }
                }
              });
            } else { // no encontro ninguna ficha anulada
              // validar exitencia del tipo de docuemtno ingresado
              validarExistenciaTipoDocumentoRequest(codigoTipoDocumento).then(() => {
                // obtener longitud y correo electronico
                obtenerLongitudTipoEntrdadaTipoDocumentoRequest(codigoTipoDocumento).then((tipoDocumento) => {
                  let longitud = parseInt(tipoDocumento[0].longitud);
                  let tipoEntrada = tipoDocumento[0].tipoEntrada;
                  // validar existencia numero documento
                  validarExistenciaNumeroDocumentoRequest(codigoTipoDocumento, numeroDocumento, longitud, tipoEntrada).then(() => {

                    if (isDefaultMail === 1) { // no tiene correo e ingresara un correo comun - quitar validar correo
                      // registrar nueva ficha
                      let json;
                      if (flagValidacionReniec) {

                        json = {
                          flagValidacionReniec: flagValidacionReniec,
                          apellidoPaterno: apellidoPaterno,
                          apellidoMaterno: apellidoMaterno,
                          nombre: nombres,
                          codigoTipoDocumento: codigoTipoDocumento,
                          longitud: longitud,
                          tipoEntrada: tipoEntrada,
                          numeroDocumento: numeroDocumento,
//                          correo: $('#txtCorreoElectronicoNuevaFicha').val()
                          correo: correoElectronico,
                          isDefaultMail: isDefaultMail
                        };
                      } else {

                        json = {
                          flagValidacionReniec: flagValidacionReniec,
                          codigoTipoDocumento: codigoTipoDocumento,
                          longitud: longitud,
                          tipoEntrada: tipoEntrada,
                          numeroDocumento: numeroDocumento,
                          correo: correoElectronico,
                          isDefaultMail: isDefaultMail
                        };
                      }

                      $.confirm({
                        icon: 'glyphicon glyphicon-question-sign',
                        theme: 'material',
                        closeIcon: true,
                        animation: 'scale',
                        type: 'dark',
                        title: 'Confirmar',
                        content: '<span class="text-semibold">¿Desea registrar una nueva ficha?</span> <br/> <span class="text-danger">Asegúrese de haber ingresado los datos correctamente</span>',
                        buttons: {
                          Registrar: {
                            btnClass: 'btn-success',
                            action: function () {

                              registrarNuevaFichaRequest(json).then((data) => {
                                if (data.status) {
                                  listarFichasPendientesRefresh();
                                  successMessage(data.message);
                                  resetFormModalFichaNueva();
                                }
                              });
                            }
                          },
                          Cancelar: {
                            btnClass: 'btn-danger'
                          }
                        }
                      });
                    } else if (isDefaultMail === 0) { // tiene un correo, entonces validar si hay correo existentes
                      // validar exitencia de correo electronico
                      validarExistenciaCorreoElectronicoRequest(correoElectronico).then(() => {
                        // registrar nueva ficha
                        let json;
                        if (flagValidacionReniec) {

                          json = {
                            flagValidacionReniec: flagValidacionReniec,
                            apellidoPaterno: apellidoPaterno,
                            apellidoMaterno: apellidoMaterno,
                            nombre: nombres,
                            codigoTipoDocumento: codigoTipoDocumento,
                            longitud: longitud,
                            tipoEntrada: tipoEntrada,
                            numeroDocumento: numeroDocumento,
//                          correo: $('#txtCorreoElectronicoNuevaFicha').val()
                            correo: correoElectronico,
                            isDefaultMail: isDefaultMail
                          };
                        } else {

                          json = {
                            flagValidacionReniec: flagValidacionReniec,
                            codigoTipoDocumento: codigoTipoDocumento,
                            longitud: longitud,
                            tipoEntrada: tipoEntrada,
                            numeroDocumento: numeroDocumento,
                            correo: correoElectronico,
                            isDefaultMail: isDefaultMail
                          };
                        }

                        $.confirm({
                          icon: 'glyphicon glyphicon-question-sign',
                          theme: 'material',
                          closeIcon: true,
                          animation: 'scale',
                          type: 'dark',
                          title: 'Confirmar',
                          content: '<span class="text-semibold">¿Desea registrar una nueva ficha?</span> <br/> <span class="text-danger">Asegúrese de haber ingresado los datos correctamente</span>',
                          buttons: {
                            Registrar: {
                              btnClass: 'btn-success',
                              action: function () {

                                registrarNuevaFichaRequest(json).then((data) => {
                                  if (data.status) {
                                    listarFichasPendientesRefresh();
                                    successMessage(data.message);
                                    resetFormModalFichaNueva();
                                  }
                                });
                              }
                            },
                            Cancelar: {
                              btnClass: 'btn-danger'
                            }
                          }
                        });
                      });
                    }
                  });
                });
              });
            }
          });
        } else {
          warningMessage('Para poder generar la ficha, primero debe de validar con reniec el numero de documento ingresado, por favor de clic en "validar dni"');
        }
      } else {
        // verifica si hay una ficha anulada con los datos ingresados
        verificarExistenciaFichaAnuladaRequest(codigoTipoDocumento, numeroDocumento).then((fichaAnuladaData) => {
          if (fichaAnuladaData) { // encontro una ficha anulada
            $.confirm({
              icon: 'fa fa-warning',
              theme: 'material',
              closeIcon: true,
              animation: 'scale',
              type: 'dark',
              draggable: false,
              backgroundDismissAnimation: 'glow',
              title: 'Confirmar',
              content:
                `
                      <span class="text-muted">A este personal anteriormente se le anuló una ficha</span> <br/> 
                      <span class="text-semibold">¿Desea registrar una nueva ficha?</span> <br/> 
                      <span class="text-danger">Asegúrese de haber ingresado los datos correctamente</span>
                    `,
              buttons: {
                Registrar: {
                  btnClass: 'btn-success',
                  action: function () {
                    /* registrar ficha aun asi tenga una ficha anulada anteriormente (implementar logica)  */
                    obtenerCodigoPersonaPorTipoDocNroDoc(codigoTipoDocumento, numeroDocumento).then((codigoPersona) => {
                      validarFichaActivaRequest(codigoPersona).then((data) => {
                        if (data.status) { // hay ficha activa y no registrar ficha
                          errorMessage(data.message);
                        } else { // no hay fichas activas
                          habilitarFichaRequest(codigoPersona, correoElectronico).then((data) => {
                            listarFichasPendientesRefresh();
                            successMessage(data);
                            resetFormModalFichaNueva();
                          });
                        }
                      });
                    });
                  }
                },
                Cancelar: {
                  btnClass: 'btn-danger'
                }
              }
            });
          } else { // no se encontro ninguna ficha anulada
            // validar exitencia del tipo de docuemtno ingresado
            validarExistenciaTipoDocumentoRequest(codigoTipoDocumento).then(() => {
              // obtener longitud y correo electronico
              obtenerLongitudTipoEntrdadaTipoDocumentoRequest(codigoTipoDocumento).then((tipoDocumento) => {
                let longitud = parseInt(tipoDocumento[0].longitud);
                let tipoEntrada = tipoDocumento[0].tipoEntrada;
                // validar existencia numero documento
                validarExistenciaNumeroDocumentoRequest(codigoTipoDocumento, numeroDocumento, longitud, tipoEntrada).then(() => {

                  if (isDefaultMail === 1) { // no tiene correo e ingresara un correo comun - quitar validar correo
                    // registrar nueva ficha
                    let json;
                    if (flagValidacionReniec) {
                      json = {
                        flagValidacionReniec: flagValidacionReniec,
                        apellidoPaterno: apellidoPaterno,
                        apellidoMaterno: apellidoMaterno,
                        nombre: nombres,
                        codigoTipoDocumento: codigoTipoDocumento,
                        longitud: longitud,
                        tipoEntrada: tipoEntrada,
                        numeroDocumento: numeroDocumento,
//                          correo: $('#txtCorreoElectronicoNuevaFicha').val()
                        correo: correoElectronico,
                        isDefaultMail: isDefaultMail
                      };
                    } else {
                      json = {
                        flagValidacionReniec: flagValidacionReniec,
                        codigoTipoDocumento: codigoTipoDocumento,
                        longitud: longitud,
                        tipoEntrada: tipoEntrada,
                        numeroDocumento: numeroDocumento,
                        correo: correoElectronico,
                        isDefaultMail: isDefaultMail
                      };
                    }

                    $.confirm({
                      icon: 'glyphicon glyphicon-question-sign',
                      theme: 'material',
                      closeIcon: true,
                      animation: 'scale',
                      type: 'dark',
                      title: 'Confirmar',
                      content: '<span class="text-semibold">¿Desea registrar una nueva ficha?</span> <br/> <span class="text-danger">Asegúrese de haber ingresado los datos correctamente</span>',
                      buttons: {
                        Registrar: {
                          btnClass: 'btn-success',
                          action: function () {
                            registrarNuevaFichaRequest(json).then((data) => {
                              if (data.status) {
                                listarFichasPendientesRefresh();
                                successMessage(data.message);
                                resetFormModalFichaNueva();
                              }
                            });
                          }
                        },
                        Cancelar: {
                          btnClass: 'btn-danger'
                        }
                      }
                    });
                  } else if (isDefaultMail === 0) { // tiene un correo, entonces validar si hay correo existentes
                    // validar exitencia de correo electronico
                    validarExistenciaCorreoElectronicoRequest(correoElectronico).then(() => {
                      // registrar nueva ficha
                      let json;
                      if (flagValidacionReniec) {
                        json = {
                          flagValidacionReniec: flagValidacionReniec,
                          apellidoPaterno: apellidoPaterno,
                          apellidoMaterno: apellidoMaterno,
                          nombre: nombres,
                          codigoTipoDocumento: codigoTipoDocumento,
                          longitud: longitud,
                          tipoEntrada: tipoEntrada,
                          numeroDocumento: numeroDocumento,
//                          correo: $('#txtCorreoElectronicoNuevaFicha').val()
                          correo: correoElectronico,
                          isDefaultMail: isDefaultMail
                        };
                      } else {
                        json = {
                          flagValidacionReniec: flagValidacionReniec,
                          codigoTipoDocumento: codigoTipoDocumento,
                          longitud: longitud,
                          tipoEntrada: tipoEntrada,
                          numeroDocumento: numeroDocumento,
                          correo: correoElectronico,
                          isDefaultMail: isDefaultMail
                        };
                      }

                      $.confirm({
                        icon: 'glyphicon glyphicon-question-sign',
                        theme: 'material',
                        closeIcon: true,
                        animation: 'scale',
                        type: 'dark',
                        title: 'Confirmar',
                        content: '<span class="text-semibold">¿Desea registrar una nueva ficha?</span> <br/> <span class="text-danger">Asegúrese de haber ingresado los datos correctamente</span>',
                        buttons: {
                          Registrar: {
                            btnClass: 'btn-success',
                            action: function () {
                              registrarNuevaFichaRequest(json).then((data) => {
                                if (data.status) {
                                  listarFichasPendientesRefresh();
                                  successMessage(data.message);
                                  resetFormModalFichaNueva();
                                }
                              });
                            }
                          },
                          Cancelar: {
                            btnClass: 'btn-danger'
                          }
                        }
                      });
                    });
                  }
                });
              });
            });
          }
        });
      }
    } else {
      errorMessage('Se han detectado que hay campos vacíos, por favor ingresar los datos correspondientes');
    }
  });
}

//******************************************************//
//******************* /NUEVA FICHA *********************//
//******************************************************//
//
// ======================================================= //

//******************************************************//
//******************* EDITAR FICHA *********************//
//******************************************************//

/**
 * Editar ficha
 */
let data = {};
function editarFicha() {
  $('#tblListadoFichaPendiente tbody').on('click', '.editarFicha', function () {
    data = $('#tblListadoFichaPendiente').DataTable().row($(this).parents('tr')).data();
    $("#modalEditarFicha").modal({
      show: true,
      backdrop: 'static',
      keyboard: false
    });
    setearDatosEditarFicha(data);
  });
}

/**
 * setear los datos para editar ficha
 * (editar ficha)
 * @argument {JSON} dataResponse
 * */
function setearDatosEditarFicha(dataResponse) {
// setear tipo de documento
  listarTipoDocumentoRequest().then((data) => {
    $('#cboTipoDocumentoEditarFicha').html(createSelectOptions(data.data.tipodocumentos, 'codigoTipoDocumento', 'descripcionCorta'));
    $('#cboTipoDocumentoEditarFicha').val(dataResponse.codigoTipoDocumento).trigger('change');
  });
  // setear numero documento
  $('#txtNumeroDocumentoEditarFicha').val(dataResponse.numeroDocumento);
  // setear correo electronico
  $('#txtCorreoElectronicoEditarFicha').val(dataResponse.correo.toLowerCase());
}

/**
 * Permite realizar accion a los inputs del modal segun parametro a enviar 
 * @argument {String} accion 
 * */
function validarInputsEditarFicha(accion) {
// limpieza de inputs
  if (accion === 'limpiarInputs') { // limpia los inputs del modal
    $('#txtNumeroDocumentoEditarFicha').val('');
    $('#txtCorreoElectronicoEditarFicha').val('');
  } else if (accion === 'limpiarNumeroDocumento') { // limpia solo numero documento
    $('#txtNumeroDocumentoEditarFicha').val('');
  } else if (accion === 'limpiarCorreo') { // limpiar solo correo
    $('#txtCorreoElectronicoEditarFicha').val('');
  }
// bloque de inputs
  else if (accion === 'bloquearInputs') { // bloquea los inputs del modal
    $('#txtNumeroDocumentoEditarFicha').attr('disabled', true);
    $('#txtCorreoElectronicoEditarFicha').attr('disabled', true);
  } else if (accion === 'bloquearNumeroDocumento') { // bloquea solo numero documento
    $('#txtNumeroDocumentoEditarFicha').attr('disabled', true);
  } else if (accion === 'bloquearCorreo') { // bloquea solo correo
    $('#txtCorreoElectronicoEditarFicha').attr('disabled', true);
  } else if (accion === 'bloquearBotonRENIEC') { // bloque el boton de consulta reniec
    $('#btnValidarConReniecEditarFicha').attr('disabled', true);
  } else if (accion === 'bloquearBotonActualizar') {
    $('#btnActualizarFicha').attr('disabled', true);
  }

// deshabilitar inputs
  else if (accion === 'deshabilitarInputs') { // deshabilita los inputs
    $('#txtNumeroDocumentoEditarFicha').attr('disabled', false);
    $('#txtCorreoElectronicoEditarFicha').attr('disabled', false);
  } else if (accion === 'deshabilitarNumeroDocumento') { // deshabilita solo numero documento
    $('#txtNumeroDocumentoEditarFicha').attr('disabled', false);
  } else if (accion === 'deshabilitarCorreo') { // deshabilita solo correo
    $('#txtCorreoElectronicoEditarFicha').attr('disabled', false);
  } else if (accion === 'bloquearBotonRENIEC') { // bloquear el boton de consulta reniec
    $('#btnValidarConReniecEditarFicha').attr('disabled', true);
  } else if (accion === 'deshabilitarBotonRENIEC') { // deshabilita el boton de consulta reniec
    $('#btnValidarConReniecEditarFicha').attr('disabled', false);
  } else if (accion === 'deshabilitarBotonActualizar') {
    $('#btnActualizarFicha').attr('disabled', false);
  }
}


/**
 * valida que cuando el tipo de documento sea <br/>
 * DNI, se mostrara el boton de "validar con reniec" <br/>
 * de lo contrario, el boton se ocultará
 */
function validarConsultaReniec() {
  $('#cboTipoDocumentoEditarFicha').on('change', () => {
    let codigoTipoDocumento = parseInt($('#cboTipoDocumentoEditarFicha').val());
    if (isNaN(codigoTipoDocumento)) {
      $(location).attr('href', 'templates/close.jsp');
    }

    if (codigoTipoDocumento === 0) {
      validarInputsEditarFicha('bloquearBotonActualizar');
      validarInputsEditarFicha('bloquearInputs');
    } else {
      validarInputsEditarFicha('deshabilitarBotonActualizar');
      validarInputsEditarFicha('deshabilitarInputs');
      validarTipoDocumentoSeleccionado(codigoTipoDocumento);
    }

    $('#divRespuestaReniecEditarFicha').html('');
    flagConsultaReniecEditarFicha = false;
    iniciarValidacionFormularioEditarFicha();
    iniciarReglasFormEditarFicha();
  });
}

/**
 * - validar tipo de documento seleccionado <br/>
 * - obtener longitud y tipo de entrada
 * @argument {int}  codigoTipoDocumento
 */
function validarTipoDocumentoSeleccionado(codigoTipoDocumento) {
  validarExistenciaTipoDocumentoRequest(codigoTipoDocumento).then(() => {
    obtenerLongitudTipoEntrdadaTipoDocumentoRequest(codigoTipoDocumento).then((tipoDocumento) => {
      validarLongitudTipoEntrada(tipoDocumento);
      if (codigoTipoDocumento === 1) {
        let validarDNIHTML = `<label for="txtNumeroDocumentoEditarFicha" class="text-semibold">Número de documento <span class="text-danger">*</span></label>
                                <a href="#" onclick="return false;" class="pull-right text-blue-800" id="btnValidarDNI"><i class="icon-search4"></i>validar dni</a>`;
        $('#divValidarDNI').html(validarDNIHTML);
        consultarReniecEditarFicha(codigoTipoDocumento);
      } else {
        let validarDNIHTML = `<label for="txtNumeroDocumentoEditarFicha" class="text-semibold">Número de documento <span class="text-danger">*</span></label>`;
        $('#divValidarDNI').html(validarDNIHTML);
      }
    });
  });
}

/**
 * realizar la peticion con el servicio de reniec <br/>
 * y devuelve como respuesta la los datos personales
 * @argument {String} dni 
 * */
let flagConsultaReniecEditarFicha = false;
let jsonReniec = {};
function ConsultarRENIECRequest(dni) {
//  let token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjRmNTZiOWI1ODczYjM0NTI3MjEzNjA5YzcwMWMwM2Y0MDlmMjQ1MTA1MmQyYjdhYWM0ZDYyMWM3ODkzMWZjMzk1MjY0NmUwODdkOGFiOTA5In0.eyJhdWQiOiIxIiwianRpIjoiNGY1NmI5YjU4NzNiMzQ1MjcyMTM2MDljNzAxYzAzZjQwOWYyNDUxMDUyZDJiN2FhYzRkNjIxYzc4OTMxZmMzOTUyNjQ2ZTA4N2Q4YWI5MDkiLCJpYXQiOjE1MjI5NTk3OTksIm5iZiI6MTUyMjk1OTc5OSwiZXhwIjoxNTU0NDk1Nzk5LCJzdWIiOiIxNzI1Iiwic2NvcGVzIjpbInVzZS1yZW5pZWMiXX0.czq534cPCIyTD5Ne4k7ReBWvgOf98UD328w_wYJI411k8p5xPo9NXvKxaylNTd_VvBYxFTRfd4qYxZvRTq95Va8Ylb6lKDkGvpnDeJVfaaRbZqopT0VzH6xhDhTGze71XnHCZ9NKjGve9oS_AVyMCPv-PFphSae4o-F_WoirTidQAQi97Os8uzFdbq0e2AzdfV9I1G3edwcipGE0EJuIKEEUqPaNupWvZ6fADcXon5eNeVSHVcB7RqfO6E4xQYGpFXxlW7ooQ_QekdP4eIv93g1TwU1Ku86pCJtOsHZ6Libv7Tb62qODn7A693x_3h0DcfglaNutDfiZwcmutx_NiuafYhj92gKzVqEEO3_CmClN9oZgyG6SFTXfO75HxcDAulgTujLFXeVWhc8pFO59f-O3KDcbp0QRaRT_Hx_x7Y6NwCGyol67fPrNAzGoGvxidRkILHwQfqesbayA2oe-W8it_qOIGNKt3u6cRQbvtu0S5Hf8LUs789fVszHxAQphw3g-xjta3L-j-uPkr73dyeHgZCJY9N0vl2dxGj0_M1TVBKySaH6qmt1vcBW8iZpp_flb3PdECs9E69CiAtuS1gastt2LaArxfUCqKwUt14JN8lS1Wjdp3Hv5au2PXjTKXXmf9yFZJQQ2zSmpvD5ygZdw_DVFQGqhxrj_gwZ2ay4";
  let respuesta = ``;
  load('Conectándose con RENIEC');
  const url = "https://tecactus.com/api/reniec/dni";

  const json = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjRmNTZiOWI1ODczYjM0NTI3MjEzNjA5YzcwMWMwM2Y0MDlmMjQ1MTA1MmQyYjdhYWM0ZDYyMWM3ODkzMWZjMzk1MjY0NmUwODdkOGFiOTA5In0.eyJhdWQiOiIxIiwianRpIjoiNGY1NmI5YjU4NzNiMzQ1MjcyMTM2MDljNzAxYzAzZjQwOWYyNDUxMDUyZDJiN2FhYzRkNjIxYzc4OTMxZmMzOTUyNjQ2ZTA4N2Q4YWI5MDkiLCJpYXQiOjE1MjI5NTk3OTksIm5iZiI6MTUyMjk1OTc5OSwiZXhwIjoxNTU0NDk1Nzk5LCJzdWIiOiIxNzI1Iiwic2NvcGVzIjpbInVzZS1yZW5pZWMiXX0.czq534cPCIyTD5Ne4k7ReBWvgOf98UD328w_wYJI411k8p5xPo9NXvKxaylNTd_VvBYxFTRfd4qYxZvRTq95Va8Ylb6lKDkGvpnDeJVfaaRbZqopT0VzH6xhDhTGze71XnHCZ9NKjGve9oS_AVyMCPv-PFphSae4o-F_WoirTidQAQi97Os8uzFdbq0e2AzdfV9I1G3edwcipGE0EJuIKEEUqPaNupWvZ6fADcXon5eNeVSHVcB7RqfO6E4xQYGpFXxlW7ooQ_QekdP4eIv93g1TwU1Ku86pCJtOsHZ6Libv7Tb62qODn7A693x_3h0DcfglaNutDfiZwcmutx_NiuafYhj92gKzVqEEO3_CmClN9oZgyG6SFTXfO75HxcDAulgTujLFXeVWhc8pFO59f-O3KDcbp0QRaRT_Hx_x7Y6NwCGyol67fPrNAzGoGvxidRkILHwQfqesbayA2oe-W8it_qOIGNKt3u6cRQbvtu0S5Hf8LUs789fVszHxAQphw3g-xjta3L-j-uPkr73dyeHgZCJY9N0vl2dxGj0_M1TVBKySaH6qmt1vcBW8iZpp_flb3PdECs9E69CiAtuS1gastt2LaArxfUCqKwUt14JN8lS1Wjdp3Hv5au2PXjTKXXmf9yFZJQQ2zSmpvD5ygZdw_DVFQGqhxrj_gwZ2ay4'
    },
    body: JSON.stringify({dni: dni})
  };

  fetch(url, json)
    .then((response) => {
      return response.json();
    })
    .then((data) => {
      if (data === 'DNI no existe.') {
        respuesta =
          `
                                    <div class="alert alert-danger alert-styled-left alert-arrow-left alert-component">
                                        <h6 class="alert-heading text-semibold text-uppercase">¡Oh, oh - EL DNI NO EXISTE!</h6>
                                        ${data} <br>
                                        <span class="text-bold">Por favor, ingrese un DNI válido e inténtelo de nuevo</span>
                                    </div>
                                `;
        $('#divRespuestaReniecEditarFicha').html(respuesta);
        flagConsultaReniecEditarFicha = false;
      } else if (data === "El DNI pertenece a un menor de edad.") {
        respuesta =
          `
                                    <div class="alert alert-warning alert-styled-left alert-arrow-left alert-component">
                                        <h6 class="alert-heading text-semibold text-uppercase">¡Oh, oh - EL DNI LE PERTENECE A UN MENOR!</h6>
                                        ${data} <br/>
                                        <span class="text-bold">Por favor, ingrese un DNI válido (+18) e inténtelo de nuevo</span>
                                    </div>
                                `;
        $('#divRespuestaReniecEditarFicha').html(respuesta);
        flagConsultaReniecEditarFicha = false;
      } else {
        respuesta =
          `
                                    <div class="alert alert-success alert-styled-left alert-arrow-left alert-component">
                                        <h6 class="alert-heading text-uppercase">¡Enhorabuena!, la persona ha sido reconocida por la reniec</h6>
                                        <span class="text-semibold">${data.nombres} ${data.apellido_paterno} ${data.apellido_materno}</span>
                                        <br/>
                                    </div>
                                `;
        $('#divRespuestaReniecEditarFicha').html(respuesta);
        flagConsultaReniecEditarFicha = true;
      }
      unload();
      jsonReniec = {
        dni: data.dni,
        nombres: data.nombres,
        apellidoPaterno: data.apellido_paterno,
        apellidoMaterno: data.apellido_materno
      };

    })
    .catch((err) => {
      errorMessage(`
                    ERROR PETICIÓN RENIEC <br/>
                    código error: ${err} <br/>
                    respuesta servidor: ${err} <br/>
                    mensaje: ${err} <br/>
                    <hr/>
                    asegúrese de haber ingresado un dni correcto e inténtelo de nuevo. <br/>
                    refrescar la página (F5)
                         `);
      flagConsultaReniecEditarFicha = false;
    });
}

/**
 * inicializar validacion en formulario editar ficha
 */
function iniciarValidacionFormularioEditarFicha() {
  let formNuevaFicha = $('#formEditarFicha');
  return formNuevaFicha.valid();
}

/**
 * definir reglas para el formulario editar dicha
 */
function iniciarReglasFormEditarFicha() {
  $('#cboTipoDocumentoEditarFicha').rules('add', {
    valueNotEquals: '0'
  });
  $('#txtNumeroDocumentoEditarFicha').rules('add', {
    required: true
  });
  $('#txtCorreoElectronicoEditarFicha').rules('add', {
    required: true,
    email: true
  });
}

/**
 * validar longitud y tipo de entrada
 * @argument {JSON Object} tipoDocumento
 */
function validarLongitudTipoEntrada(tipoDocumento) {
  let tipoEntrada = tipoDocumento[0].tipoEntrada;
  let longitud = parseInt(tipoDocumento[0].longitud);
  $('#txtNumeroDocumentoEditarFicha').attr({
    minlength: longitud,
    maxlength: longitud
  });
  if (tipoEntrada === 'N') { // numerico
    $('#txtNumeroDocumentoEditarFicha').rules('remove', 'alphanumeric');
    $('#txtNumeroDocumentoEditarFicha').rules('add', {
      required: true,
      number: true
    });
  } else if (tipoEntrada === 'A') { // alfanumerico
    $('#txtNumeroDocumentoEditarFicha').rules('remove', 'number');
    $('#txtNumeroDocumentoEditarFicha').rules('add', {
      required: true,
      alphanumeric: true
    });
  }

  iniciarValidacionFormularioEditarFicha();
}

/**
 * consultar con reniec
 * @argument {int} codigoTipoDocumento 
 */
function consultarReniecEditarFicha(codigoTipoDocumento) {
  if (codigoTipoDocumento === 1) {
    $('#btnValidarDNI').off('click');
    $('#btnValidarDNI').on('click', () => {
      let longitud = $('#txtNumeroDocumentoEditarFicha').val().length;
      if (longitud === 8 && iniciarValidacionFormularioEditarFicha()) {
        ConsultarRENIECRequest($('#txtNumeroDocumentoEditarFicha').val().trim());
      }
    });
    $('#txtNumeroDocumentoEditarFicha').on('keyup', () => {
      flagConsultaReniecEditarFicha = false;
      $('#divRespuestaReniecEditarFicha').html('');
      let longitud = $('#txtNumeroDocumentoEditarFicha').val().length;
      if (longitud === 8 && iniciarValidacionFormularioEditarFicha()) {
        $('#btnValidarDNI').off('click');
        $('#btnValidarDNI').on('click', () => {
          let longitud = $('#txtNumeroDocumentoEditarFicha').val().length;
          if (longitud === 8 && iniciarValidacionFormularioEditarFicha()) {
            ConsultarRENIECRequest($('#txtNumeroDocumentoEditarFicha').val().trim());
          }
        });
      }
    });
  }
}

/**
 * permite resear todo el modal cuando <br/>
 * el registro haya sido satisfactorio
 * */
function resetFormModalEditarNueva() {
  $('#modalEditarFicha').modal('hide');
  $('#divRespuestaReniecEditarFicha').html('');
  apellidoPaterno = '';
  apellidoPaterno = '';
  nombres = '';
  flagConsultaReniecEditarFicha = false;
}

/**
 * obtenerDatoEditarFicha obtiene los datos segun tipo de doc selecc.
 * realiza la peticion al servidor
 * @argument {int} codigoTipoDocumento 
 * @argument {int} longitud 
 * @argument {int} tipoEntrada 
 * @argument {String} numeroDocumento 
 * @argument {String} correo
 * @returns {JSON Object} 
 */
function actualizarFicha(codigoTipoDocumento, longitud, tipoEntrada, numeroDocumento, correo) {
  let getJsonReniec = jsonReniec;
  jsonFicha = {};
  if (codigoTipoDocumento === 1) {
    if (flagConsultaReniecEditarFicha) { // ha consultado con reniec
      $.confirm({
        icon: 'glyphicon glyphicon-question-sign',
        theme: 'material',
        closeIcon: true,
        animation: 'scale',
        type: 'dark',
        title: 'Confirmar',
        content: '<span class="text-semibold">¿Desea actualizar la ficha?</span> <br/> <span class="text-danger">Asegúrese de haber ingresado los datos correctamente</span>',
        buttons: {
          'Actualizar ficha': {
            btnClass: 'btn-success',
            action: function () {
              jsonFicha = {
                flagValidacionReniec: flagConsultaReniecEditarFicha,
                apellidoPaterno: getJsonReniec.apellidoPaterno,
                apellidoMaterno: getJsonReniec.apellidoMaterno,
                nombres: getJsonReniec.nombres,
                codigoTipoDocumento: codigoTipoDocumento,
                numeroDocumento: getJsonReniec.dni,
                longitud: longitud,
                tipoEntrada: tipoEntrada,
                correo: correo,
                codigoFicha: data.codigoFicha,
                codigoPersona: data.codigoPersona
              };
              actualizarFichaRequest(jsonFicha).then((data) => {
                if (data.status) {
                  listarFichasPendientesRefresh();
                  successMessage(data.message);
                  resetFormModalEditarNueva();
                }
              });
            }
          },
          Cancelar: {
            btnClass: 'btn-danger'
          }
        }
      });
    } else { // no ha consultado con reniec
      warningMessage('Para poder actualizar la ficha, primero debe de validar con reniec el numero de documento ingresado, por favor de clic en "validar dni"');
    }
  } else {
    $.confirm({
      icon: 'glyphicon glyphicon-question-sign',
      theme: 'material',
      closeIcon: true,
      animation: 'scale',
      type: 'dark',
      title: 'Confirmar',
      content: '<span class="text-semibold">¿Desea actualizar la ficha?</span> <br/> <span class="text-danger">Asegúrese de haber ingresado los datos correctamente</span>',
      buttons: {
        'Actualizar ficha': {
          btnClass: 'btn-success',
          action: function () {
            jsonFicha = {
              flagValidacionReniec: flagConsultaReniecEditarFicha,
              codigoTipoDocumento: codigoTipoDocumento,
              numeroDocumento: numeroDocumento,
              longitud: longitud,
              tipoEntrada: tipoEntrada,
              correo: correo,
              codigoFicha: data.codigoFicha,
              codigoPersona: data.codigoPersona
            };
            actualizarFichaRequest(jsonFicha).then((data) => {
              if (data.status) {
                listarFichasPendientesRefresh();
                successMessage(data.message);
                resetFormModalEditarNueva();
              }
            });
          }
        },
        Cancelar: {
          btnClass: 'btn-danger'
        }
      }
    });
  }
}

/**
 * actualizarFichaRequest
 * @argument {JSON Object} json 
 */
function actualizarFichaRequest(json) {
  return new Promise((resolve, reject) => {
    $.ajax({
      type: 'POST',
      url: '../FichaServlet',
      data: {accion: 'actualizarFicha', json: JSON.stringify(json)},
      dataType: 'json',
      beforeSend: function () {
        load('Actualizando ficha, por favor espere');
      },
      success: function (data, textStatus, jqXHR) {
        if (textStatus === 'success') {
          unload();
          if (!data) {
            errorMessage(data.message);
          } else {
            resolve(data);
          }
        }
      },
      error: function (jqXHR, textStatus, errorThrown) {
        reject("No se pudo procesar la solicitud de actualizar la ficha");
      }
    });
  });
}

/**
 * actualizar ficha
 */
function validarActualizarFicha() {
  $('#btnActualizarFicha').on('click', () => {
    iniciarValidacionFormularioEditarFicha();
    iniciarReglasFormEditarFicha();
    if (iniciarValidacionFormularioEditarFicha()) {
      let codigoTipoDocumento = parseInt($('#cboTipoDocumentoEditarFicha').val());
      validarExistenciaTipoDocumentoRequest(codigoTipoDocumento).then(() => {
        obtenerLongitudTipoEntrdadaTipoDocumentoRequest(codigoTipoDocumento).then((tipoDocumento) => {
          let numeroDocumento = $('#txtNumeroDocumentoEditarFicha').val().toUpperCase();
          let correoElectronico = $('#txtCorreoElectronicoEditarFicha').val().toUpperCase();
          let longitud = parseInt(tipoDocumento[0].longitud);
          let tipoEntrada = tipoDocumento[0].tipoEntrada;
          if (codigoTipoDocumento === data.codigoTipoDocumento && numeroDocumento === data.numeroDocumento && correoElectronico === data.correo) {
            warningMessage('El sistema ha detectado que no ha realizado ningun cambio por lo tanto no se podrá actualizar la ficha');
          } else if (codigoTipoDocumento !== data.codigoTipoDocumento && correoElectronico === data.correo) {
//                        alert('modificar tipo de documento y numero documento');
            validarExistenciaNumeroDocumentoRequest(codigoTipoDocumento, numeroDocumento, longitud, tipoEntrada).then(() => {
//                            window.alert('el numero documento no pertenece a nadie y es posible registrarlo/actualizarlo');
              actualizarFicha(codigoTipoDocumento, longitud, tipoEntrada, numeroDocumento, correoElectronico);
            });
          } else if (numeroDocumento !== data.numeroDocumento && codigoTipoDocumento === data.codigoTipoDocumento && correoElectronico === data.correo) {
//                        alert('modificar numero documento');
            validarExistenciaNumeroDocumentoRequest(codigoTipoDocumento, numeroDocumento, longitud, tipoEntrada).then(() => {
//                            window.alert('el numero documento no pertenece a nadie y es posible registrarlo/actualizarlo');
              actualizarFicha(codigoTipoDocumento, longitud, tipoEntrada, numeroDocumento, correoElectronico);
            });
          } else if (correoElectronico !== data.correo && codigoTipoDocumento === data.codigoTipoDocumento && numeroDocumento === data.numeroDocumento) {
//                        alert('modificar correo');
            validarExistenciaCorreoElectronicoRequest(correoElectronico).then((data) => {
//                            console.log(data);
//                            window.alert('el correo no pertenece a nadie y es posible registrarlo/actualizarlo');
              actualizarFicha(codigoTipoDocumento, longitud, tipoEntrada, numeroDocumento, correoElectronico);
            });
          } else if (codigoTipoDocumento !== data.codigoTipoDocumento && correoElectronico !== data.correo) {
//                        alert('modificar tipo de documento, numero documento y correo');
            validarExistenciaNumeroDocumentoRequest(codigoTipoDocumento, numeroDocumento, longitud, tipoEntrada).then(() => {
//                            window.alert('el numero documento no pertenece a nadie y es posible registrarlo/actualizarlo');
              validarExistenciaCorreoElectronicoRequest(correoElectronico).then(() => {
//                                window.alert('el correo no pertenece a nadie y es posible registrarlo/actualizarlo');
                actualizarFicha(codigoTipoDocumento, longitud, tipoEntrada, numeroDocumento, correoElectronico);
              });
            });
          } else if (numeroDocumento !== data.numeroDocumento && codigoTipoDocumento === data.codigoTipoDocumento && correoElectronico !== data.correo) {
//                        alert('modificar numero documento y correo');
            validarExistenciaNumeroDocumentoRequest(codigoTipoDocumento, numeroDocumento, longitud, tipoEntrada).then(() => {
//                            window.alert('el numero documento no pertenece a nadie y es posible registrarlo/actualizarlo');
              validarExistenciaCorreoElectronicoRequest(correoElectronico).then(() => {
//                                window.alert('el correo no pertenece a nadie y es posible registrarlo/actualizarlo');
                actualizarFicha(codigoTipoDocumento, longitud, tipoEntrada, numeroDocumento, correoElectronico);
              });
            });
          }
        });
      });
    } else {
      warningMessage('Al parecer hay datos vacíos o ingresados incorrectamente');
    }
  });
}

/**
 * cancelar actualizacion de ficha
 */
function cancelarActualizarFicha() {
  $('#btnCancelarEditarFicha').on('click', () => {
    let formValidate = $('#formEditarFicha').validate();
    formValidate.resetForm();
    $('#divRespuestaReniecEditarFicha').html('');
  });
}

// =================================================== //

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
  $('#tblListadoFichaPendiente tbody').on('click', '.anularFicha', function () {
    let codigoFicha = $('#tblListadoFichaPendiente').DataTable().row($(this).parents('tr')).data().codigoFicha;
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
              listarFichasPendientesRefresh();
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

// =================================================== //

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
            for (let i in observaciones) {
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
  $('#tblListadoFichaPendiente tbody').on('click', '.verObservacion', function () {
    let codigoFicha = $('#tblListadoFichaPendiente').DataTable().row($(this).parents('tr')).data().codigoFicha;
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
 * inicializar switch
 */
function initSwitchery() {
  var elem = document.querySelector('.switchery');
  let switchery = new Switchery(elem, {color: '#4CAF50', secondaryColor: '#EF5350'});

  elem.onclick = () => {
    let statusSwitch = elem.checked;
    if (statusSwitch) {
      document.querySelector('#statusCorreoText').innerHTML = 'SI';
      document.querySelector('#statusCorreoValue').value = 0; // tiene correo
      document.querySelector('#helpText').innerHTML = '';
      $('#txtCorreoElectronicoNuevaFicha').val(null);
//      validarInputs('deshabilitarCorreo');
    } else {
      document.querySelector('#statusCorreoText').innerHTML = 'NO';
      document.querySelector('#statusCorreoValue').value = 1; // no tiene correo
      document.querySelector('#helpText').innerHTML = '<i class="fa fa-circle"></i> Se activó el correo predeterminado';
      $('#txtCorreoElectronicoNuevaFicha').val($('#defaultMail').val());
      validarInputs('bloquearCorreo');
    }
    validarFormModalNuevaFicha();
  };
}

/**
 * obtener el correo predeterminado
 * que se seteara en un input cuando el usuaio
 * ponga que no tiene correo
 */
function defaultMailRequest() {
  return new Promise((resolve, reject) => {
    let client = new XMLHttpRequest();
    client.open('POST', '../ConfiguracionFichaServlet?accion=getDefaultMail');
    client.onreadystatechange = handler;
    client.responseType = 'json';
    client.setRequestHeader('Accept', 'application/json');
    client.send();
    function handler() {
      if (this.readyState === this.DONE) {
        if (this.status === 200) {
          resolve(this.response);
        } else {
          reject(this);
        }
      }
    }
  });
}

/**
 * setea el correo en un input para su posterior uso
 */
function setDefaultMail() {
  defaultMailRequest().then((data) => {
    if (data.status) {
      $('#defaultMail').val(data.data.mail);
    } else {
      warningMessage('No se pudo obtener el correo predeterminado.');
    }
  });
}