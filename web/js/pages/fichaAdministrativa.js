/* global google */

$(function () {
  inicializarComponentes();
  llenarDatosFicha();
  listarSede();
  validarExistenciaSede();
  validarExistenciaArea();
  validarExistenciaCargo();
  validarExistenciaTipoPago();
  regresarBandejaFicha();
  registrarDatosAdministrativos();
  setearMapa();
  registrarTipoExpediente();
  listarTipoExpediente();
  validarObservacion();
});

let codigoFicha;
let json;
function llenarDatosFicha() {
  if (window.name === '') {
    window.location = "index.jsp";
  } else {
    json = JSON.parse(window.name);
    configuracionDatosPersonales(json);
  }
}

function configuracionDatosPersonales(json) {
  $('#lblTipoDocumento').text(`${json.tipoDocumentoDescripcionLarga} (${json.tipoDocumentoDescripcionCorta})`);
  $('#lblNumeroDocumento').text(json.numeroDocumento);
  $('#lblNumeroRUC').text(json.ruc);
  $('#lblApellidoPaterno').text(json.apellidoPaterno);
  $('#lblApellidoMaterno').text(json.apellidoMaterno);
  $('#lblNombre').text(json.nombre);
  $('#lblSexo').text(json.sexo);
  $('#lblEstadoCivil').text(json.estadoCivil);
  $('#lblFechaNacimiento').text(json.fechaNacimiento);
  $('#lblNacionalidad').text(json.gentilicio);
  $('#lblDepartamentoNacimiento').text(json.nombreDepartamentoNacimiento);
  $('#lblProvinciaNacimiento').text(json.nombreProvinciaNacimiento);
  $('#lblDistritoNacimiento').text(json.nombreDistritoNacimiento);
  $('#lblDireccionDocumento').text(json.direccionDocumento);
  $('#lblTelefonoFijo').text(json.telefonoFijo);
  $('#lblTelefonoMovil').text(json.telefonoMovil);
  $('#lblCorreoElectronico').text(json.correo);
  $('#lblDepartamentoResidencia').text(json.nombreDepartamentoResidencia);
  $('#lblProvinciaResidencia').text(json.nombreProvinciaResidencia);
  $('#lblDistritoResidencia').text(json.nombreDistritoResidencia);
  $('#lblDireccionResidencia').text(json.direccionResidencia);
  $('#lblFondoPension').text(json.fondoPensionDescripcionCorta);
  $('#latitudResidencia').val(json.latitud);
  $('#longitudResidencia').val(json.longitud);
  configuracionTblFamiliar(json.cargafamiliar);
  configuracionTblFormacionAcademica(json.formacionacademica);
  configuracionTblExperienciaLaboral(json.experiencialaboral);
  codigoFicha = json.codigoFicha;
  $('#lblFoto').attr({
    src: 'http:\\\\172.16.2.20/img/' + json.foto
  });
}

function configuracionTblFamiliar(cargafamiliar) {
  let tblFamiliar = new SimpleTable({
    element: 'tblFamiliar', //id from the table
    data: cargafamiliar,
    no_data_text: 'No tiene registros',
    columns: [
      {
        data: function (data) {
          return `${data.apellidoPaterno} ${data.apellidoMaterno}, ${data.nombre}`;
        }
      },
      {data: 'nombreParentesco'},
      {data: 'fechaNacimiento'},
      {data: 'nombreTipoDocumentoDescripcionLarga'},
      {data: 'numeroDocumento'},
      {data: 'telefono'}
    ]
  });
  tblFamiliar.createBody();
}

function configuracionTblFormacionAcademica(formacionacademica) {
  let tblFormacionAcademica = new SimpleTable({
    element: 'tblFormacionAcademica', //id from the table
    data: formacionacademica,
    no_data_text: 'No tiene registros',
    columns: [
      {data: 'nivelEstudio'},
      {data: 'estadoEstudio'},
      {data: 'nombreCentroEstudios'},
      {data: 'nombreCarreraProfesional'},
      {data: 'fechaInicio'},
      {data: 'fechaFin'}
    ]
  });
  tblFormacionAcademica.createBody();
}

function configuracionTblExperienciaLaboral(experiencialaboral) {
  let tblExperienciaLaboral = new SimpleTable({
    element: 'tblExperienciaLaboral', //id from the table
    data: experiencialaboral,
    no_data_text: 'No tiene registros',
    columns: [
      {data: 'nombreEmpresa'},
      {data: 'nombreCargo'},
      {data: 'fechaInicio'},
      {data: 'fechaFin'},
      {data: 'telefono'}
    ]
  });
  tblExperienciaLaboral.createBody();
}

function regresarBandejaFicha() {
  $('#btnRegresarBandeja').on('click', function (e) {
    window.name = "";
    window.location = "index.jsp";
  });
}

function inicializarComponentes() {
  $('.bootstrap-select').selectpicker();

  $('#dpFechaIngreso, #dpFechaTermino').attr('readonly', true);

  $('#dpFechaIngreso, #dpFechaTermino').datepicker({
    onSelect: function (date) {
//            console.log(date);
    },
    dateFormat: 'dd/mm/yy',
    showButtonPanel: false
  });

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

  $('.js-example-basic-multiple').select2({
    multiple: true,
    placeholder: 'Ingrese los expedientes que corresponden al personal'
  });
}

// listar sede
function listarSede() {
  $('#cboArea, #cboCargo, #cboTipoPago').attr('disabled', true);
  $('#cboArea, #cboCargo, #cboTipoPago').selectpicker('refresh');
  $.ajax({
    type: 'POST',
    url: '../SedeServlet',
    data: {accion: 'listarSede'},
    beforeSend: function () {
      load('Listando sedes');
    },
    success: function (data, textStatus, jqXHR) {
      if (textStatus === 'success') {
        unload();
        let sede = data.data.sedes;
        let cboSede = '<option value="0">[SELECCIONAR]</option>';
        for (let i in sede) {
          cboSede += '<option value="' + sede[i].codigoSede + '">' + sede[i].nombre + '</option>';
        }
        $('#cboSede').html(cboSede);
        $('#cboSede').selectpicker('refresh');
      }
    }
  });
}

// validar existencia sede 
function validarExistenciaSede() {
  $('#cboSede').on('change', function () {
    datosAdministrativosDocente(0);
    let codigoSede = parseInt($('#cboSede').val());

    if (isNaN(codigoSede)) {
      $(location).attr('href', 'templates/close.jsp');
    }

    $('#cboCargo, #cboTipoPago').html('<option value="0">[SELECCIONAR]</option>');
    $('#cboCargo, #cboTipoPago').attr('disabled', true);
    $('#cboCargo, #cboTipoPago').selectpicker('refresh');
    $('#sueldoEscalafon').text('S/. 0.00');
    $('#txtSueldoMensual').val('0.00');
    if (codigoSede === 0) {
      $('#cboArea, #cboCargo, #cboTipoPago').html('<option value="0">[SELECCIONAR]</option>');
      $('#cboArea, #cboCargo, #cboTipoPago').attr('disabled', true);
      $('#cboArea, #cboCargo, #cboTipoPago').selectpicker('refresh');
    } else {
      // cuando sea diferente a cero validar existencia de sede
      $.ajax({
        type: 'POST',
        url: '../SedeServlet',
        data: {accion: 'validarExistenciaSede', codigoSede: codigoSede},
        beforeSend: function () {
//                    load('validando existencia de sede');
        },
        success: function (data, textStatus, jqXHR) {
          if (textStatus === 'success') {
            $('#cboArea').attr('disabled', false);
            $('#cboArea').selectpicker('refresh');
            unload();
//                        si el resultado de existencia es 'false', redireccion al login
            if (!data.status) {
              $(location).attr('href', 'templates/close.jsp');
            } else {
              listarArea(codigoSede);
            }
          }
        }
      });
    }
  });
}


// listar area
function listarArea(codigoSede) {
  $.ajax({
    type: 'POST',
    url: '../AreaServlet',
    data: {accion: 'listarArea', codigoSede: codigoSede},
    beforeSend: function () {
      load('Listando áreas');
    },
    success: function (data, textStatus, jqXHR) {
      if (textStatus === 'success') {
        unload();
        let area = data.data.areas;
        let cboArea = '<option value="0">[SELECCIONAR]</option>';
        for (let i in area) {
          cboArea += '<option value="' + area[i].codigoArea + '">' + area[i].nombre + '</option>';
        }
        $('#cboArea').html(cboArea);
        $('#cboArea').selectpicker('refresh');
      }
    }
  });
}

// validar existencia area
function validarExistenciaArea() {
  $('#cboArea').on('change', function () {
    datosAdministrativosDocente(0);
    let codigoSede = parseInt($('#cboSede').val());
    let codigoArea = parseInt($('#cboArea').val());

    if (isNaN(codigoSede) || isNaN(codigoArea)) {
      $(location).attr('href', 'templates/close.jsp');
    }


    $('#cboTipoPago').html('<option value="0">[SELECCIONAR]</option>');
    $('#cboTipoPago').attr('disabled', true);
    $('#cboTipoPago').selectpicker('refresh');
    $('#sueldoEscalafon').text('S/. 0.00');
    $('#txtSueldoMensual').val('0.00');
    if (codigoArea === 0) {
      // cuando es cero hacer algo
      $('#cboCargo').html('<option value="0">[SELECCIONAR]</option>');
      $('#cboCargo').attr('disabled', true);
      $('#cboCargo').selectpicker('refresh');
    } else {
      $.ajax({
        type: 'POST',
        url: '../AreaServlet',
        data: {accion: 'validarExistenciaArea', codigoSede: codigoSede, codigoArea: codigoArea},
        beforeSend: function () {
//                    load('Validando existencia de área');
        },
        success: function (data, textStatus, jqXHR) {
          if (textStatus === 'success') {
            $('#cboCargo').attr('disabled', false);
            $('#cboCargo').selectpicker('refresh');
//                        unload();
            // si el resultado de existencia es 'false', redireccion al login
            if (!data.status) {
              $(location).attr('href', 'templates/close.jsp');
            } else {
              obtenerSedeArea(codigoSede, codigoArea);
              listarCargo(codigoArea);
            }
          }
        }
      });
    }
  });
}

// obtener codigo de sede area
let codigoSedeArea = 0;
function obtenerSedeArea(codigoSede, codigoArea) {
  $.ajax({
    type: 'POST',
    url: '../SedeAreaServlet',
    data: {accion: 'obtenerSedeArea', codigoSede: codigoSede, codigoArea: codigoArea},
    beforeSend: function () {
      load('Obteniendo resultados - sede/area');
    },
    success: function (data, textStatus, jqXHR) {
      if (textStatus === 'success') {
        unload();
        if (data.status) {
          codigoSedeArea = data.data.getResultedKey;
        }
      }
    }
  });
}

// listar cargo
function listarCargo(codigoArea) {
  $.ajax({
    type: 'POST',
    url: '../CargoServlet',
    data: {accion: 'listarCargo', codigoArea: codigoArea},
    beforeSend: function (xhr) {
      load('Listando cargos');
    },
    success: function (data, textStatus, jqXHR) {
      if (textStatus === 'success') {
        unload();
        let cargo = data.data.cargos;
        let cboCargo = '<option value="0">[SELECCIONAR]</option>';
        for (let i in cargo) {
          cboCargo += '<option value="' + cargo[i].codigoCargo + '">' + cargo[i].nombre + '</option>';
        }
        $('#cboCargo').html(cboCargo);
        $('#cboCargo').selectpicker('refresh');
      }
    }
  });
}

// validar existencia cargo
function validarExistenciaCargo() {
  $('#cboCargo').on('change', function () {
    datosAdministrativosDocente(0);
    let codigoArea = parseInt($('#cboArea').val());
    let codigoCargo = parseInt($('#cboCargo').val());

    if (isNaN(codigoArea) || isNaN(codigoCargo)) {
      $(location).attr('href', 'templates/close.jsp');
    }

    $('#sueldoEscalafon').text('S/. 0.00');
    $('#txtSueldoMensual').val('0.00');
    if (codigoCargo === 0) {
      $('#cboTipoPago').html('<option value="0">[SELECCIONAR]</option>');
      $('#cboTipoPago').attr('disabled', true);
      $('#cboTipoPago').selectpicker('refresh');
    } else {
      $.ajax({
        type: 'POST',
        url: '../CargoServlet',
        data: {accion: 'validarExistenciaCargo',
          codigoArea: codigoArea,
          codigoCargo: codigoCargo},
        beforeSend: function () {
//                    load('Validando existencia de cargo');
        }, success: function (data, textStatus, jqXHR) {
          if (textStatus === 'success') {
            $('#cboTipoPago').attr('disabled', false);
            $('#cboTipoPago').selectpicker('refresh');
//                        unload();
            // si el resultado de existencia es 'false', redireccion al login
            if (!data.status) {
              $(location).attr('href', 'templates/close.jsp');
            } else {
              obtenerAreaCargo(codigoArea, codigoCargo);
            }
          }
        }
      });
    }
  });
}

// obtener area cargo
let codigoAreaCargo = 0;
let tipoFicha = '';
function obtenerAreaCargo(codigoArea, codigoCargo) {
  $.ajax({
    type: 'POST',
    url: '../AreaCargoServlet',
    data: {accion: 'obtenerAreaCargo', codigoArea: codigoArea, codigoCargo: codigoCargo},
    beforeSend: function () {
      load('Obteniendo resultados - area/cargo');
    },
    success: function (data, textStatus, jqXHR) {
      if (textStatus === 'success') {
        unload();
        if (data.status) {
          codigoAreaCargo = data.data.getResultedKey;
          listarTipoPago(codigoAreaCargo);
          if (codigoAreaCargo === 5) {
            tipoFicha = 'D';
          } else {
            tipoFicha = 'A';
          }
        }
      }
    }
  });
}


// listar tipo de pago
// dependiento del cargo seleccionado
function listarTipoPago(codigoAreaCargo) {
  $.ajax({
    type: 'POST',
    url: '../TipoPagoServlet',
    data: {accion: 'listarTipoPago', codigoAreaCargo: codigoAreaCargo},
    beforeSend: function () {
      load('Listando tipo de pagos');
    },
    success: function (data, textStatus, jqXHR) {
      if (textStatus === 'success') {
        unload();
        let tipoPago = data.data.tipoPagos;
        let cboTipoPago = '<option value="0">[SELECCIONAR]</option>';
        for (let i in tipoPago) {
          cboTipoPago += '<option value="' + tipoPago[i].codigoTipoPago + '">' + tipoPago[i].nombre + '</option>';
        }
        $('#cboTipoPago').html(cboTipoPago);
        $('#cboTipoPago').selectpicker('refresh');
      }
    }
  });
}

// validar existencia de cargo seleccionado
function validarExistenciaTipoPago() {
  $('#cboTipoPago').on('change', function () {
    datosAdministrativosDocente(0);
    let codigoTipoPago = parseInt($('#cboTipoPago').val());

    if (isNaN(codigoTipoPago)) {
      $(location).attr('href', 'templates/close.jsp');
    }

    $('#sueldoEscalafon').text('S/. 0.00');
    $('#txtSueldoMensual').val('0.00');
    if (codigoTipoPago === 0) {
      $('#sueldoEscalafon').text('S/. 0.00');
      $('#txtSueldoMensual').val('0.00');
    } else {
      $.ajax({
        type: 'POST',
        url: '../TipoPagoServlet',
        data: {accion: 'validarExistenciaTipoPago', codigoAreaCargo: codigoAreaCargo, codigoTipoPago: codigoTipoPago},
        beforeSend: function () {
//                    load('Validando existencia de tipo pago');
        },
        success: function (data, textStatus, jqXHR) {
          if (textStatus === 'success') {
            unload();
            // si el resultado de existencia es 'false', redireccion al login
            if (!data.status) {
              $(location).attr('href', 'templates/close.jsp');
            } else {
              obtenerAreaCargoTipoPago(codigoAreaCargo, codigoTipoPago);
            }
          }
        }
      });
    }
  });
}

// obtener area cargo tipo pago
let codigoAreaCargoTipoPago = 0;
function obtenerAreaCargoTipoPago(codigoAreaCargo, codigoTipoPago) {
  $.ajax({
    type: 'POST',
    url: '../AreaCargoTipoPagoServlet',
    data: {accion: 'obtenerAreaCargoTipoPago', codigoAreaCargo: codigoAreaCargo, codigoTipoPago: codigoTipoPago},
    beforeSend: function (xhr) {
      load('Obteniendo resultados - areacargo/tipopago');
    },
    success: function (data, textStatus, jqXHR) {
      if (textStatus === 'success') {
        unload();
        if (data.status) {
          codigoAreaCargoTipoPago = data.data.getResultedKey;
          obtenerEscalafonAreaCargoTipoPago(codigoAreaCargoTipoPago);
          datosAdministrativosDocente(codigoAreaCargoTipoPago);
        }
      }
    }
  });
}

// obtener sueldo escalafon
let sueldoEscalafon = 0;
function obtenerEscalafonAreaCargoTipoPago(codigoAreaCargoTipoPago) {
  $.ajax({
    type: 'POST',
    url: '../EscalafonServlet',
    data: {accion: 'obtenerEscalafonAreaCargoTipoPago', codigoAreaCargoTipoPago: codigoAreaCargoTipoPago},
    beforeSend: function () {
      load('Cargando escalafon');
    },
    success: function (data, textStatus, jqXHR) {
      if (textStatus === 'success') {
        unload();
        if (data.status) {
          $('#sueldoEscalafon').text('S/. ' + data.data.sueldo);
          $('#txtSueldoMensual').val(data.data.sueldo);
          validarObservacion();
          sueldoEscalafon = data.data.sueldo;
        } else {
          $('#sueldoEscalafon').text('S/. -.-');
        }
      }
    }
  });
}

// construir datos administrativos de docente
// segun area cargo tipo pago seleccionado
function datosAdministrativosDocente(codigoAreaCargoTipoPago) {

  if (codigoAreaCargoTipoPago === 5) {
    $('#divContenidoDatoAdministrativo').html(`
        <div class="col-md-6">
            <div class="row">
                <div class="col-md-4">
                    <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">COSTO  A<span class="text-danger"> (*)</span></label>
                        <div class="input-group">
                            <span class="input-group-addon">S/. </span>
                            <input type="text" class="form-control" placeholder="0.00" maxlength="10" id="txtCostoA" name="txtCostoA">
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">COSTO  B<span class="text-danger"> (*)</span></label>
                        <div class="input-group">
                            <span class="input-group-addon">S/. </span>
                            <input type="text" class="form-control" placeholder="0.00" maxlength="10" id="txtCostoB" name="txtCostoB">
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">COSTO  C<span class="text-danger"> (*)</span></label>
                        <div class="input-group">
                            <span class="input-group-addon">S/. </span>
                            <input type="text" class="form-control" placeholder="0.00" maxlength="10" id="txtCostoC" name="txtCostoC">
                        </div>
                    </div>
                </div>
            </div>
        </div>`);
  } else if (codigoAreaCargoTipoPago === 6) {
    $('#divContenidoDatoAdministrativo').html(`
        <div class="col-md-3">
            <div class="form-group">
                <label class="display-block text-uppercase text-semibold">Sueldo Mensual<span class="text-danger"> (*)</span></label>
                <div class="input-group">
                    <span class="input-group-addon">S/. </span>
                    <input type="text" class="form-control" placeholder="0.00" maxlength="10" id="txtSueldoMensual" name="txtSueldoMensual">
                </div>
            </div>
        </div>`);
  } else if (codigoAreaCargoTipoPago === 19) {
    $('#divContenidoDatoAdministrativo').html(`
        <div class="col-md-6">
            <div class="row">
                <div class="col-md-2">
                    <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">COSTO  A<span class="text-danger"> (*)</span></label>
                        <div class="input-group">
                            <span class="input-group-addon">S/. </span>
                            <input type="text" class="form-control" placeholder="0.00" maxlength="10" id="txtCostoA" name="txtCostoA">
                        </div>
                    </div>
                </div>
                <div class="col-md-2">
                    <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">COSTO  B<span class="text-danger"> (*)</span></label>
                        <div class="input-group">
                            <span class="input-group-addon">S/. </span>
                            <input type="text" class="form-control" placeholder="0.00" maxlength="10" id="txtCostoB" name="txtCostoB">
                        </div>
                    </div>
                </div>
                <div class="col-md-2">
                    <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">COSTO  C<span class="text-danger"> (*)</span></label>
                        <div class="input-group">
                            <span class="input-group-addon">S/. </span>
                            <input type="text" class="form-control" placeholder="0.00" maxlength="10" id="txtCostoC" name="txtCostoC">
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">Sueldo Mensual<span class="text-danger"> (*)</span></label>
                        <div class="input-group">
                            <span class="input-group-addon">S/. </span>
                            <input class="form-control" placeholder="0.00" maxlength="10" id="txtSueldoMensual" name="txtSueldoMensual" type="text">
                        </div>
                    </div>
                </div>
            </div>
        </div>`);
  } else {
    $('#divContenidoDatoAdministrativo').html(`
        <div class="col-md-3">
            <div class="form-group">
                <label class="display-block text-uppercase text-semibold">Escalafón<span class="text-danger"></span></label>
                <h6 class="text-bold" id="sueldoEscalafon">S/. 0.00</h6>
            </div>
            </div>
            <div class="col-md-3">
            <div class="form-group">
                <label class="display-block text-uppercase text-semibold">Sueldo Mensual<span class="text-danger"> (*)</span></label>
                <div class="input-group">
                    <span class="input-group-addon">S/. </span>
                    <input type="text" class="form-control" placeholder="0.00" maxlength="10" id="txtSueldoMensual" name="txtSueldoMensual">
                </div>
            </div>
        </div>`);
  }
}

// listar expedientes request
let listarExpedientesRequest = () => {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: '../ExpedienteServlet',
      type: 'POST',
      dataType: 'json',
      data: {
        accion: 'listarTipoExpedientes'
      },
      success: function (data, textStatus, jqXHR) {
        resolve(data);
      }, error: function (jqXHR, textStatus, errorThrown) {
        reject('Error al listar expedientes');
      }
    });
  });
};
// listar expedientes response
let listarExpedientesResponse = () => {

};
// listar options
let createSelectOptions = (obj, valueName, textName) => {
  let options = '';
  obj.forEach((data) => {
    options += `<option value="${data[valueName]}">${data[textName]}</option>`;
  });
  return options;
};


/* *************************** */
/* **** VALIDAR FORMULARIO *** */
/* *************************** */
function validarFormularioDatosAdministrativos() {
  let formDatosAdministrativos = $('#formDatosAdministrativos');
  formDatosAdministrativos.validate({
    rules: {
      dpFechaIngreso: {
        required: true,
        dateonly: true
      },
      dpFechaTermino: {
        required: true,
        dateonly: true
      },
      cboSede: {
        required: true,
        valueNotEquals: "0"
      },
      cboArea: {
        required: true,
        valueNotEquals: "0"
      },
      cboCargo: {
        required: true,
        valueNotEquals: "0"
      },
      cboTipoPago: {
        required: true,
        valueNotEquals: "0"
      },
      txtSueldoMensual: {
        required: true,
        number: true
      },
      txtCostoA: {
        required: true,
        number: true
      },
      txtCostoB: {
        required: true,
        number: true
      },
      txtCostoC: {
        required: true,
        number: true
      },
      txtEnlaceAlfresco: {
        required: true,
        url: true
      }

//            txtObservacion: {
//                required: true
//            }
    },
    messages: {
      dpFechaIngreso: {
        required: 'El campo es obligatorio',
        dateonly: 'Ingrese una fecha válida (dd/mm/yyyy)'
      },
      dpFechaTermino: {
        required: 'El campo es obligatorio',
        dateonly: 'Ingrese una fecha válida (dd/mm/yyyy)'
      },
      cboSede: {
        required: 'El campo es obligatorio',
        valueNotEquals: 'Debe seleccionar una sede'
      },
      cboArea: {
        required: 'El campo es obligatorio',
        valueNotEquals: 'Debe seleccionar una área'
      },
      cboCargo: {
        required: 'El campo es obligatorio',
        valueNotEquals: 'Debe seleccionar una cargo'
      },
      cboTipoPago: {
        required: 'El campo es obligatorio',
        valueNotEquals: 'Debe seleccionar un tipo de pago'
      },
      txtSueldoMensual: {
        required: 'El campo es obligatorio',
        number: 'Ingrese un monto válido'
      },
      txtCostoA: {
        required: 'El campo es obligatorio',
        number: 'Ingrese un monto válido'
      },
      txtCostoB: {
        required: 'El campo es obligatorio',
        number: 'Ingrese un monto válido'
      },
      txtCostoC: {
        required: 'El campo es obligatorio',
        number: 'Ingrese un monto válido'
      },
      txtEnlaceAlfresco: {
        required: 'El campo es requerido',
        url: 'Ingrese un enlace correcto'
      }
//            txtObservacion: {
//                required: 'El campo es obligatorio'
//            }
    }
  });

  return formDatosAdministrativos.valid();
}

/* *************************** */
/* *** /VALIDAR FORMULARIO *** */
/* *************************** */

/**
 * registrar datos administrativos
 * envia los datos al servlet
 * @argument {JSON Object} json 
 */
function registrarDatosAdministrativosRequest(json) {
  return new Promise((resolve, reject) => {
    // ajax registar datos administrativos
    $.ajax({
      type: 'POST',
      url: '../FichaLaboralServlet',
      data: {accion: 'registrarFichaLaboral', json: JSON.stringify(json)},
      beforeSend: function () {
        load('Registrando datos administrativos, por favor espere');
      },
      success: function (data, textStatus, jqXHR) {
        if (textStatus === 'success') {
          unload();
          console.log(data);
          if (data.status) {
            resolve(data.message);
          } else {
            errorMessage(data.message);
          }
        }
      },
      error: function (jqXHR, textStatus, errorThrown) {
        rejecet('No se pudo procesar la solicitud registrarDatosAdministrativosRequest : ' + errorThrown);
      }
    });
  });
}


// Registrar datos administrativa
function registrarDatosAdministrativos() {
  $('#btnRegistrarFicha').on('click', function () {
    let expediente = $('.js-example-basic-multiple').select2("val");
    if (expediente === null) {
      warningMessage('Error, debe seleccionar al menos un expediente adjunto del personal');
    } else {
      let arrExpediente = [];
      expediente.map((obj, i, array) => {
        arrExpediente.push({id: obj});
      });
      let enlaceAlfresco = $('#txtEnlaceAlfresco').val();
      if (isUrlValid(enlaceAlfresco)) {
        if (validarFormularioDatosAdministrativos()) {

          let jsonRequest = {
            codigoFicha: codigoFicha,
            enlaceAlfresco: enlaceAlfresco,
            expediente: arrExpediente,
            fechaIngreso: $('#dpFechaIngreso').val(),
            fechaTermino: $('#dpFechaTermino').val(),
            codigoSedeArea: codigoSedeArea,
            codigoAreaCargo: codigoAreaCargo,
            tipoFicha: tipoFicha,
            codigoAreaCargoTipoPago: codigoAreaCargoTipoPago,
            observacion: $('#txtObservacion').val() === '' ? '-' : $('#txtObservacion').val()
          };

          if (tipoFicha === 'A') {
            jsonRequest.sueldoEscalafon = sueldoEscalafon;
            jsonRequest.sueldoMensual = $('#txtSueldoMensual').val() === '' ? '0.00' : $('#txtSueldoMensual').val();
          } else if (tipoFicha === 'D') {
            if (codigoAreaCargo === 5) {
              if (codigoAreaCargoTipoPago === 5) {
                jsonRequest.costoA = $('#txtCostoA').val() === '' ? '0.00' : $('#txtCostoA').val();
                jsonRequest.costoB = $('#txtCostoB').val() === '' ? '0.00' : $('#txtCostoB').val();
                jsonRequest.costoC = $('#txtCostoC').val() === '' ? '0.00' : $('#txtCostoC').val();
              } else if (codigoAreaCargoTipoPago === 6) {
                jsonRequest.sueldoMensual = $('#txtSueldoMensual').val() === '' ? '0.00' : $('#txtSueldoMensual').val();
              } else if (codigoAreaCargoTipoPago === 19) {
                jsonRequest.costoA = $('#txtCostoA').val() === '' ? '0.00' : $('#txtCostoA').val();
                jsonRequest.costoB = $('#txtCostoB').val() === '' ? '0.00' : $('#txtCostoB').val();
                jsonRequest.costoC = $('#txtCostoC').val() === '' ? '0.00' : $('#txtCostoC').val();
                jsonRequest.sueldoMensual = $('#txtSueldoMensual').val() === '' ? '0.00' : $('#txtSueldoMensual').val();
              }
            }
          }

          $.confirm({
            icon: 'glyphicon glyphicon-question-sign',
            theme: 'material',
            closeIcon: true,
            animation: 'scale',
            type: 'dark',
            title: 'Confirmar',
            content: '<span class="text-semibold">¿Desea registrar los datos administrativos a la ficha del personal ' + json.apellidoPaterno + ' ' + json.apellidoMaterno + ', ' + json.nombre + '?</span>',
            buttons: {
              Registrar: {
                btnClass: 'btn-success',
                action: function () {
                  registrarDatosAdministrativosRequest(jsonRequest).then((data) => {

                    $.confirm({
                      icon: 'fa fa-check fa-lg',
                      title: 'Registro éxitoso!',
                      content: `<b>${data}</b>`,
                      type: 'green',
                      scrollToPreviousElement: false,
                      scrollToPreviousElementAnimate: false,
                      buttons: {
                        Aceptar: {
                          text: 'Aceptar',
                          btnClass: 'btn-green',
                          action: function () {
                            $(location).attr('href', 'index.jsp');
                          }
                        }
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
        } else {
          warningMessage('Error, al parecer hay datos ingresados incorrectamente, por favor revisar');
        }
      } else {
        warningMessage('Error, debe ingresar un enlace correcto');
      }
    }
  });
}

/**
 * valida que el dato ingresado sea una URL valida
 * @argument {String} url
 */
function isUrlValid(url) {
  return /^(https?|s?ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i.test(url);
}

/**
 * setear mapa
 */
function setearMapa() {
//  console.log(json.latitud);
//  console.log(json.longitud);
//  
  myLatLng = {
    lat: parseFloat(json.latitud),
    lng: parseFloat(json.longitud)
  };

  let map = new google.maps.Map($('#mapResidencia')[0], {
    center: myLatLng,
    zoom: 18,
    disableDefaultUI: false,
    draggable: true
  });

  getMarker(myLatLng, map);

}


function getMarker(myLatLng, map) {
  let marker = new google.maps.Marker({
    position: myLatLng,
    map: map,
    draggable: false,
    animation: google.maps.Animation.DROP,
    anchorPoint: new google.maps.Point(0, -29)
  });
  let infowindow = new google.maps.InfoWindow();

  getAddress(myLatLng, infowindow, map, marker);
}

function getAddress(latlng, infowindow, map, marker) {
  let geocoder = new google.maps.Geocoder;

  geocoder.geocode({'location': latlng}, function (result, status) {
    if (status === 'OK') {
      infowindow.setContent(
        '<div>' +
        '<b class="text-uppercase">' + result[0].address_components[1].long_name + '</b> <br/>' +
        '<span class="text-muted">' + result[0].formatted_address + '</span> <br/>' +
        '</div>'
        );
      infowindow.open(map, marker);

    } else {
      load("Cargando mapa");
      setTimeout(() => unload(), 2000);
    }
  });

}

/**
 * LISTAR TIPOS DE EXPEDIENTES
 */
function listarTipoExpediente() {
  $.ajax({
    type: 'POST',
    url: '../TipoExpedienteServlet',
    data: {'accion': 'listarTipoExpediente'},
    beforeSend: function (xhr) {
      load('Listando tipos de expedientes..');
    },
    success: function (data, textStatus, jqXHR) {
      if (textStatus === 'success') {
        unload();
        let tipoExpediente = data.data.tipoexpedientes;
        let cboTipoExpediente = '';
        for (let i in tipoExpediente) {
          cboTipoExpediente += '<option value="' + tipoExpediente[i].codigoTipoExpediente + '">' + tipoExpediente[i].nombre + '</option>';
        }
        $('.js-example-basic-multiple').html(cboTipoExpediente);
      }
    }
  });
}


/**
 * REGISTRAR TIPO DE EXPEDIENTE
 * @argument {String} nombre 
 * @returns {String}
 */
function registrarTipoExpedienterequest(nombre) {
  return new Promise((resolve, rejecet) => {
    $.ajax({
      type: 'POST',
      url: '../TipoExpedienteServlet',
      data: {accion: 'registrarTipoExpediente', nombre: nombre},
      beforeSend: function (xhr) {
        load('Registrando tipo expediente, por favor espere');
      },
      success: function (data, textStatus, jqXHR) {
        if (textStatus === 'success') {
          unload();
          if (data.status) {
            resolve(data);
          } else {
            errorMessage(data.message);
          }
        }
      },
      error: function (jqXHR, textStatus, errorThrown) {
        rejecet('No se pudo procesarr la solicitud registrarTipoExpedienterequest : ' + errorThrown);
      }
    });
  });
}

/**
 * AGREGAR NUEVO TIPO DE EXPEDIENTE
 */
function registrarTipoExpediente() {
  $('#btnAgregarTipoExpediente').on('click', () => {
    $.confirm({
      icon: 'fa fa-question',
      draggable: false,
      animationBounce: 1.5,
      closeAnimation: 'opacity',
      theme: 'modern',
      closeIcon: false,
      animation: 'scale',
      backgroundDismissAnimation: 'glow',
      type: 'blue',
      title: 'Ingrese un nuevo tipo de expediente',
      content: `
                
                  <div class="form-group">
                    <label class="text-semibold">Tipo expediente:</label>
                    <input type="text" id="txtTipoExpediente" class="form-control text-uppercase"/>
                  </div>
                
                    `,
      buttons: {
        'Registrar': {
          btnClass: 'btn-success',
          action: () => {
            let nombre = $('#txtTipoExpediente').val().trim();
            if (!nombre) {
              warningMessage('Debe ingresar un tipo de expediente');
              return false;
            }


            registrarTipoExpedienterequest(nombre).then((response) => {
              // add new option
              var data = {
                id: response.data.id,
                text: response.data.data
              };
              var newOption = new Option(data.text.toUpperCase(), data.id, false, false);
              $('.js-example-basic-multiple').append(newOption).trigger('change');
              successMessage(response.message);
            });

          }
        },
        'Cancelar': {
          btnClass: 'btn-danger'
        }
      }
    });
  });
}

/**
 * validar observacion solo cuando el campo
 * sueldo propuesto sea diferente a escalafon
 */
let validObservacion = false;
function validarObservacion() {
  $('#txtSueldoMensual').on('keyup', () => {
    let sueldoPropuesto = $('#txtSueldoMensual').val().trim();
    console.log(validarFormularioDatosAdministrativos());
    if (validarFormularioDatosAdministrativos()) {
      console.log('cambio');
      console.log(sueldoEscalafon);
      console.log(sueldoPropuesto);

      if (sueldoPropuesto !== sueldoEscalafon) {
        console.log('son diferentes');
        $('#txtObservacion').rules('add', {
          required: true,
          messages: {
            required: "Este campo es requerido, por favor ingrese una observación"
          }
        });
      } else {
        console.log('son iguales');
        $('#txtObservacion').rules('remove', 'required');
      }

    } else {
      $('#txtObservacion').rules('remove', 'required');
    }
  });
}