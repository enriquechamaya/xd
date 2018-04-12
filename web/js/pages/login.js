$(function () {
  init();
});

function init() {
  ingresarTrismegistoPlanilla();
}

//INGRESAR A TRISMEGISTO PLANILLA
function ingresarTrismegistoPlanilla() {
  // CLICK AL BOTON
  const btnIngresar = $('#btnIngresar');
  btnIngresar.on('click', function (e) {
    validarUsuario();
  });

  // CUANDO PRESIONE ENTER
  document.addEventListener('keydown', function (e) {
    if (e.keyCode === 13) {
      validarUsuario();
    }
  });
}

//VALIDAR USUARIO
function validarUsuario() {

  const json = {
    txtUsuario: $('#txtUsuario').val(),
    txtContrasenia: $('#txtContrasena').val()
  };

  $.ajax({
    url: '../UsuarioServlet?accion=validarUsuario',
    type: 'POST',
    data: {
      json: JSON.stringify(json)
    },
    dataType: 'json',
    beforeSend: function (xhr) {
      load('Verificando usuario ingresado');
    },
    success: function (data, textStatus, jqXHR) {
      const status = data.status;
      if (status) {
        dismissAlertMessage();
        window.location.href = 'main.jsp';

      } else {
//                shakeError();
        showAlertMessage();
      }
    },
    complete: function () {
      unload();
    }
  });
}

// ---------------------------------------------------------------------------//
// --------------------------------- EFECTO CARGAR ---------------------------//
// ---------------------------------------------------------------------------//

/* cierra el efecto cargar */
function unload() {
  $.unblockUI();
}

/* efecto cargar */
function load(msg) {
  $.blockUI({
    message: '<span class="text-semibold"><i class="icon-hour-glass2 spinner position-left"></i>&nbsp; ' + msg + '...</span>',
    timeout: 20000 * 100, //unblock after 2 seconds
    overlayCSS: {
      backgroundColor: '#1b2024',
      opacity: 0.8,
      zIndex: 1200,
      cursor: 'wait'
    },
    css: {
      border: 0,
      color: '#fff',
      padding: 0,
      zIndex: 1201,
      backgroundColor: 'transparent'
    }
  });
}

// ---------------------------------------------------------------------------//
// --------------------------------- /EFECTO CARGAR --------------------------//
// ---------------------------------------------------------------------------//


// ---------------------------------------------------------------------------//
// --------------------------- /MOSTRAR MENSAJE DE ALERTA --------------------//
// ---------------------------------------------------------------------------//

function showAlertMessage() {
  $('#alertMessageError').fadeIn();
}

function dismissAlertMessage() {
  $('#alertMessageError').fadeOut();
}
// ---------------------------------------------------------------------------//
// --------------------------- /MOSTRAR MENSAJE DE ALERTA --------------------//
// ---------------------------------------------------------------------------//

localStorage.clear()