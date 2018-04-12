// // ---------------------------------------------------------------------------//
// ---------------------------------  INPUT VALIDACION -----------------------//
// ---------------------------------------------------------------------------//
function soloNumeros(input) {
  $('#' + input).on('keypress keyup blur', function (event) {
    $(this).val($(this).val().replace(/[^\d].+/, ""));
    if ((event.which < 48 || event.which > 57)) {
      event.preventDefault();
    }
  });
}

function soloLetras(input) {
  $('#' + input).on('keypress keyup blur', function (e) {
    var regex = new RegExp(/^[ña-zÑA-Z\s]*$/);
    var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
    if (regex.test(str)) {
      return true;
    }
    e.preventDefault();
    return false;
  });
}

function soloAlfanumerico(input) {
  $('#' + input).on('keypress keyup blur', function (e) {
    let regex = new RegExp("^[a-zA-Z0-9]+$");
    let str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
    if (regex.test(str)) {
      return true;
    }
    e.preventDefault();
    return false;
  });
}

// ---------------------------------------------------------------------------//
// --------------------------------- /INPUT VALIDACION -----------------------//
// ---------------------------------------------------------------------------//

function popup(obj) {
  bootbox.dialog({
    title: obj.title,
    size: obj.size,
    message: obj.mensaje,
    className: obj.className,
    buttons: {
      confirm: {
        label: 'Yes',
        className: 'btn-success'
      },
      cancel: {
        label: 'No',
        className: 'btn-danger'
      }
    }
  });
}

// ---------------------------------------------------------------------------//
// --------------------------------- EFECTO CARGAR ---------------------------//
// ---------------------------------------------------------------------------//

// cierra el efecto cargar
function unload() {
  $.unblockUI();
}

// efecto cargar
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

// ----------------MENSAJES DE CONFIRMACION
function successMessage(msg, callback) {
  $.confirm({
    icon: 'fa fa-check fa-lg',
    title: 'Registro correcto!',
    content: `<b>${msg}</b>`,
    type: 'green',
    scrollToPreviousElement: false,
    scrollToPreviousElementAnimate: false,
    buttons: {
      Aceptar: {
        text: 'Aceptar',
        btnClass: 'btn-green',
        action: function () {
          callback;
        }
      }
    }
  });
}

function errorMessage(msg, callback) {
  $.confirm({
    icon: "fa fa-warning fa-lg",
    title: '¡Error!',
    content: `<b>${msg}</b>`,
    type: 'red',
    scrollToPreviousElement: false,
    scrollToPreviousElementAnimate: false,
    typeAnimated: true,
    buttons: {
      Cerrar: {
        text: 'Cerrar',
        btnClass: 'btn-red',
        action: function () {
          callback;
        }
      }
    }
  });
}

function warningMessage(msg, callback) {
  $.confirm({
    icon: "fa fa-warning fa-lg",
    title: '¡Alerta!',
    content: `<b>${msg}</b>`,
    type: 'orange',
    scrollToPreviousElement: false,
    scrollToPreviousElementAnimate: false,
    typeAnimated: true,
    buttons: {
      Cerrar: {
        text: 'Cerrar',
        btnClass: 'btn-orange',
        action: function () {
          callback;
        }
      }
    }
  });
}
// ----------------MENSAJES DE CONFIRMACION


// ----------------MENSAJES DE CONFIRMACION (PROMISE)



//configuracion por defecto datatable 
function defaultConfigDataTable() {
  $.extend($.fn.dataTable.defaults, {
    autoWidth: false,
    searching: false,
    lengthChange: false,
    responsive: true,
    bSort: false,
    dom: '<"datatable-header"fl><"datatable-scroll"t><"datatable-footer"ip>',
    language: {
      "paginate": {'first': 'First', 'last': 'Last', 'next': '&rarr;', 'previous': '&larr;'},
      "sProcessing": "Procesando...",
      "sLengthMenu": "Mostrar _MENU_ registros",
      "sZeroRecords": "No se encontraron resultados",
      "sEmptyTable": "Ningún dato disponible en esta tabla",
      "sInfo": "Mostrando registros del _START_ al _END_ de un total de _TOTAL_ registros",
      "sInfoEmpty": "Mostrando registros del 0 al 0 de un total de 0 registros",
      "sInfoFiltered": "(filtrado de un total de _MAX_ registros)",
      "sInfoPostFix": "",
      "sSearch": "Buscar:",
      "sUrl": "",
      "sInfoThousands": ",",
      "sLoadingRecords": "Cargando...",
      "oPaginate": {
        "sFirst": "Primero",
        "sLast": "Último",
        "sNext": "Siguiente",
        "sPrevious": "Anterior"
      },
      "oAria": {
        "sSortAscending": ": Activar para ordenar la columna de manera ascendente",
        "sSortDescending": ": Activar para ordenar la columna de manera descendente"
      }
    },
    drawCallback: function () {
      $(this).find('tbody tr').slice(-3).find('.dropdown, .btn-group').addClass('dropup');
    },
    preDrawCallback: function () {
      $(this).find('tbody tr').slice(-3).find('.dropdown, .btn-group').removeClass('dropup');
    },
    rowCallback: function (row, data, index) {
      if ($(row).hasClass('odd')) {
        $(row).css('background-color', '#e8f4f4');
      }
    }
  });
}

// smoothScrolling
function smoothScrolling(nodeIdentifier) {
  var scroll = new SmoothScroll();
  var anchor = document.querySelector(nodeIdentifier);
  scroll.animateScroll(anchor);
}



// eliminar un elemento del array por llave
function removeByKey(array, params) {
  array.some(function (item, index) {
    return (array[index][params.key] === params.value) ? !!(array.splice(index, 1)) : false;
  });
  return array;
}


// validar existencia del valor en un array
function validarExistenciaValorDeArray(value, array, x_value) {
  for (let i = 0; i < array.length; i++) {
    if (value === array[i][x_value]) {
      return true;
    }
  }
  return false;
}

// generar identificador unico
function guidGenerator() {
  let S4 = function () {
    return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
  };
  return (S4() + S4() + "-" + S4() + "-" + S4() + "-" + S4() + "-" + S4() + S4() + S4());
}

// FUNCION FECHA QUE FORMATEA LA FECHA
function toDate(dateStr) {
  var parts = dateStr.split("/");
  return new Date(parts[2], parts[1] - 1, parts[0]);
}

function getActualDate() {
  return new Date();
}

// FUNCION PARA OBTENER LA FECHA ACTUAL
//is valid date format
function calculateAge(dateOfBirth, dateToCalculate) {
  let calculateYear = dateToCalculate.getFullYear();
  let calculateMonth = dateToCalculate.getMonth();
  let calculateDay = dateToCalculate.getDate();

  let birthYear = dateOfBirth.getFullYear();
  let birthMonth = dateOfBirth.getMonth();
  let birthDay = dateOfBirth.getDate();

  let age = calculateYear - birthYear;
  let ageMonth = calculateMonth - birthMonth;
  let ageDay = calculateDay - birthDay;

  if (ageMonth < 0 || (ageMonth === 0 && ageDay < 0)) {
    age = parseInt(age) - 1;
  }
  return age;
}

let getLocal = (keyName) => JSON.parse(window.localStorage.getItem(keyName));
let setLocal = (keyName, obj) => window.localStorage.setItem(keyName, JSON.stringify(obj));
let removeLocal = (keyName) => window.localStorage.removeItem(keyName);
let clearLocal = () => localStorage.clear();



(() => {
//  console.log('%c(╯°□°）╯︵ ┻━┻!', 'color: red; font-size: 100px; font-weight: bold;');
//  console.log('%cTodos los cambios son registrados en nuestra base de datos.', 'background: #222; font-size: 25px; color: #bada55');
//  console.log('%cobservando...', 'background: #222; font-size: 15px; color: #bada55');
//  console.log('%cMejor cierra la consola prro o valdrás ( ͡° ͜ʖ ͡°) ...', 'background: #222; font-size: 18px; color: #bada55;font-weight: bold;');
//
//  Object.getOwnPropertyNames(console).filter(function (property) {
//    return typeof console[property] === 'function';
//  }).forEach(function (verb) {
//    console[verb] = function () {
//      return 'Te estoy mirando!';
//    };
//  });
})();
