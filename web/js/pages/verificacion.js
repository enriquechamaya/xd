$(function () {
  init();
});

function init() {
  configuracionFormularioIngresoRegistroFicha();
  validarTokenFichaVerificacion();
  verificarPersona();
}


// VALIDAR INPUTS 
function configuracionFormularioIngresoRegistroFicha() {
  $('#txtCodigoVerificacion').attr({
    maxlength: 6,
    minlength: 6
  });

  soloAlfanumerico('txtNumeroDocumento');
  soloAlfanumerico('txtCodigoVerificacion');
}

// VALIDAR EL FORMULARIO QUE SEA VALIDO
function validarFormularioIngresoRegistroFicha() {
  let formBusquedaFichaPendiente = $('#formValidarIngresoRegistroFicha');
  formBusquedaFichaPendiente.validate({
    rules: {
      txtNumeroDocumento: {
        alphanumeric: true,
        required: true
      },
      txtCodigoVerificacion: {
        alphanumeric: true,
        required: true,
        maxlength: 6,
        minlength: 6
      }
    },
    messages: {
      txtNumeroDocumento: {
        alphanumeric: "Solo se admite dígitos alfanuméricos",
        required: "Ingrese el número de documento"
      },
      txtCodigoVerificacion: {
        alphanumeric: "Solo se admite dígitos alfanuméricos",
        required: "Ingrese el número de documento",
        maxlength: "El código de verificación es de 6 dígitos",
        minlength: "El código de verificación es de 6 dígitos"
      }
    }
  });

  return formBusquedaFichaPendiente.valid();
}




//// VERIFICAR TOKEN FICHA VERIFICACION
function validarTokenFichaVerificacion() {
  $.ajax({
    type: 'POST',
    url: '../TokenFichaServlet',
    data: {accion: 'validarTokenFichaVerificacion'},
    dataType: 'json',
    beforeSend: function (xhr) {
      load('validando token');
    },
    success: function (data, textStatus, jqXHR) {
      if (textStatus === 'success') {
        unload();
        if (data.status) {
        } else {
          $('<input>').attr({
            type: 'hidden',
            id: 'accion',
            name: 'accion',
            value: 'desactivarToken'
          }).appendTo('#formTokenFicha');
          $('#formTokenFicha').submit();
        }
      }
    }
  });
}

// VERIFICAR PERSONA Y VERIFICAR TOKEN FICHA VERIFICACION (EVENT CLICK)
function verificarPersona() {
  $('#btnVerificarPersona').on('click', function () {
    if (validarFormularioIngresoRegistroFicha()) {
      $.ajax({
        type: 'POST',
        url: '../TokenFichaServlet',
        data: {accion: 'validarTokenFichaVerificacion'},
        dataType: 'json',
        beforeSend: function (xhr) {
          load('validando token');
        },
        success: function (data, textStatus, jqXHR) {
          if (textStatus === 'success') {
            unload();
            if (data.status) {
              // verificar persona
              // accion
              $('<input>').attr({
                type: 'hidden',
                name: 'accion',
                value: 'verificarPersona'
              }).appendTo('#formPersona');
              // codigo verificacion
              $('<input>').attr({
                type: 'hidden',
                name: 'codigoVerificacion',
                value: $('#txtCodigoVerificacion').val()
              }).appendTo('#formPersona');
              // numero documento
              $('<input>').attr({
                type: 'hidden',
                name: 'numeroDocumento',
                value: $('#txtNumeroDocumento').val()
              }).appendTo('#formPersona');
              $('#formPersona').submit();
            } else {
              $('<input>').attr({
                type: 'hidden',
                id: 'accion',
                name: 'accion',
                value: 'desactivarToken'
              }).appendTo('#formTokenFicha');
              $('#formTokenFicha').submit();
            }
          }
        }
      });
    } else {
    }
  });
}