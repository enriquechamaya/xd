$(function () {
  inicializarComponentes();
  configuracionComponentes();
  listarFichas().then(() => {
    validarCheckBox()
  })
  configuracionCheckBox();
  generarLote();
  configuracionComboFiltrado();

});

let lotes = [];
let tipoLote = "";
let flagGenerarLote = false;
function listarFichas() {
  return new Promise((resolve, reject) => {
    defaultConfigDataTable();
    $('#tblFichasPendientes').DataTable().destroy()
    $('#tblFichasPendientes').DataTable({
      ajax: {
        url: '../LoteFichaServlet',
        type: 'POST',
        dataType: 'json',
        data: {
          accion: 'listarFichasDT',
          json: JSON.stringify(obtenerDataFiltro())
        }
      },
      processing: true,
      serverSide: true,
      columnDefs: [],
      iDisplayLength: 20,
      columns: [
        {
          data: null,
          render: function () {
            return '<input type="checkbox" class="control-primary checkbox-body">';
          }
        },
        {
          data: null,
          render: function (data) {
            return `${data.apellidoPaterno} ${data.apellidoMaterno}, ${data.nombre}`;
          }
        },
        {
          data: 'numeroDocumento'
        },
        {
          data: 'nombreSede'
        },
        {
          data: 'nombreArea'
        },
        {
          data: 'nombreCargo'
        },
        {
          data: null,
          render: function (data) {
            if (data.tipoFicha === 'A') {
              return `<b> S/. ${data.sueldoMensualAdministrativo} </b>`;
            } else if (data.tipoFicha === 'D') {
              return data.costoMensualDocente !== '0.00' ? `<b> S/. ${data.costoMensualDocente} </b>` : ' - ';
            }
          }
        },
        {
          data: 'costoADocente',
          render: function (data) {
            return data !== '0.00' ? `<b> S/. ${data} </b>` : ' - ';
          }
        },
        {
          data: 'costoBDocente',
          render: function (data) {
            return data !== '0.00' ? `<b> S/. ${data} </b>` : ' - ';
          }
        },
        {
          data: 'costoCDocente',
          render: function (data) {
            return data !== '0.00' ? `<b> S/. ${data} </b>` : ' - ';
          }
        }
      ],
      fnDrawCallback: function (settings) {
        $(".control-primary").uniform({
          radioClass: 'choice',
          wrapperClass: 'border-primary-600 text-primary-800 div-checkbox-body'
        });
        setearChecks();
        if (lotes.length !== $('#tblFichasPendientes').DataTable().page.info().recordsTotal) {
          $(".checkbox-head").prop('checked', false);
          $('.div-checkbox-head span').removeClass('checked');
        }
      },
      createdRow: function (row, data, index) {
        let tipoFicha = data.tipoFicha; // "A" ó "D"
        if (tipoFicha === "A") {
          $(row).addClass('info');
        } else if (tipoFicha === "D") {
          $(row).addClass('warning');
        }
      },
      fnInitComplete: function (settings, data) {
        resolve(data)
      }
    })
  })

}

function setearChecks() {
  let tabla = $('#tblFichasPendientes').DataTable().rows().data();
  for (let i = 0; i < tabla.length; i++) {
    for (let x = 0; x < lotes.length; x++) {
      if (lotes[x].codigoFicha === obtenerDatosFila(i).codigoFicha) {
        let input = $($($('#tblFichasPendientes').DataTable().row(i).node()).children()[0]).find('input');
        input.parent().addClass('checked');
        input.prop('checked', true);
      }
    }
  }
}

let obtenerDatosFila = (fila) => $('#tblFichasPendientes').DataTable().row(fila).data();

function obtenerDataFiltro() {
  let json = {
    criterio: parseInt($('#cboCriterioFiltroFicha').val())
  };
  return json;
}



function generarLote() {
  $('#btnGenerarLote').on('click', function () {
    if (flagGenerarLote) {
      $.confirm({
        icon: 'glyphicon glyphicon-question-sign',
        theme: 'material',
        closeIcon: true,
        animation: 'scale',
        type: 'dark',
        title: 'Confirmar',
        content: '<span class="text-semibold">¿Seguro de generar lote?</span>',
        buttons: {
          Registrar: {
            btnClass: 'btn-success',
            action: function () {
              let json = {};
              let detallelote = [];
              for (let i in lotes) {
                detallelote.push({codigoFicha: lotes[i].codigoFicha, tipoFicha: lotes[i].tipoFicha});
              }
              json.detallelote = detallelote;
              json.tipoLote = tipoLote;
              console.log(json);
              if (detallelote.length !== 0) {
                $.ajax({
                  url: '../LoteFichaServlet',
                  type: 'POST',
                  dataType: 'json',
                  data: {
                    accion: 'registrarLote',
                    json: JSON.stringify(json)
                  },
                  beforeSend: function (xhr) {
                    console.log('Validando ingreso');
                  }, success: function (data, textStatus, jqXHR) {
                    if (data.status) {
                      $.confirm({
                        icon: 'fa fa-check fa-lg',
                        title: 'Registro éxitoso!',
                        content: `<b>Se generó el lote ${data.data.getResultedKey} correctamente</b>`,
                        type: 'green',
                        scrollToPreviousElement: false,
                        scrollToPreviousElementAnimate: false,
                        buttons: {
                          Aceptar: {
                            text: 'Aceptar',
                            btnClass: 'btn-green',
                            action: function () {
                              if (tipoLote === 'D') {
                                window.location = 'lotesFicha.jsp'
                              } else if (tipoLote === 'A') {
                                window.location = 'consultaLotesGeneral.jsp'
                              } else {
                                window.location = 'main.jsp'
                              }
                            }
                          },
//                          detalle: {
//                            text: 'Ir al detalle',
//                            btnClass: 'btn-orange',
//                            action: function () {
//                              $(location).attr('href', 'main.jsp');
//                            }
//                          }
                        }
                      });
                      $('#cboCriterioFiltroFicha').val(0).change();
                      $('#cboCriterioFiltroFicha').selectpicker('refresh');
                      listarFichas();
                    } else {
                      errorMessage(data.message);
                    }
                  }
                });
              } else {
                warningMessage("Debe seleccionar al menos una ficha.");
              }
            }
          },
          Cancelar: {
            btnClass: 'btn-danger'
          }
        }
      });

    } else {
      warningMessage("Acción maliciosa, refresque la página");
    }
  });
}

function configuracionCheckBox() {
  $(".checkbox-head").click(function () {
    if ($(this).prop('checked')) { // le doy check
      $('.div-checkbox-body span').addClass('checked');
      $('.checkbox-body').each((i, el) => {
        if (!$(el).prop('checked')) { // no esta marcado
          let x_data = $('#tblFichasPendientes').DataTable().row(i).data();
          lotes.push(x_data);
        }
      });
    } else { // le quito check
      lotes = [];
      $('.div-checkbox-body span').removeClass('checked');
    }
    $(".checkbox-body").prop('checked', $(this).prop('checked'));
  });

  $('#tblFichasPendientes tbody').on('click', '.checkbox-body', function (e) {
    iterateCheckBox();
    let row = $('#tblFichasPendientes').DataTable().row($(this).parents('tr'));
    let data = row.data();
    let id = data.id;
    if ($(this).prop('checked')) {
      lotes.push(data);
    } else {
      removeByKey(lotes, {key: 'id', value: id});
    }
  });
}

function configuracionComboFiltrado() {
  $('#cboCriterioFiltroFicha').on('change', function (e) {
    lotes = [];
    $(".checkbox-head").prop('checked', false);
    $('.div-checkbox-head span').removeClass('checked');

    let codigoCriteroBusqueda = parseInt($('#cboCriterioFiltroFicha').val());
    if (codigoCriteroBusqueda !== 0) {
      flagGenerarLote = true;
      $('#btnGenerarLote').attr('disabled', false);
      if (codigoCriteroBusqueda === 1) {
        tipoLote = 'A';
      } else if (codigoCriteroBusqueda === 2) {
        tipoLote = 'D';
      }
      listarFichas().then(() => {
        $('.div-checkbox-head').removeClass('disabled')
        $(".checkbox-head").prop('disabled', false)
        $('.div-checkbox-body').removeClass('disabled')
        $('.checkbox-body').prop('disabled', false)
      })
    } else {
      $('#btnGenerarLote').attr('disabled', true);
      flagGenerarLote = false;
      tipoLote = '';
      listarFichas()
        .then(() => {
          validarCheckBox()
        })
    }
  });
}

let iterateCheckBox = () => {
  let count = 0;
  $('.checkbox-body:checked').each((el, i) => {
    count++;
  });
  if ($('.checkbox-body').length === count) {
    $('.div-checkbox-head span').addClass('checked');
    $('.checkbox-head').prop('checked', true);
  } else {
    $('.div-checkbox-head span').removeClass('checked');
    $('.checkbox-head').prop('checked', false);
  }
};

function configuracionComponentes() {
  $('#btnGenerarLote').attr('disabled', true);
}

function inicializarComponentes() {
  $('.bootstrap-select').selectpicker();
  // Primary
  $(".control-primary").uniform({
    radioClass: 'choice',
    wrapperClass: 'border-primary-600 text-primary-800 div-checkbox-head'
  });
}

let validarCheckBox = () => {
  $('.div-checkbox-head').addClass('disabled')
  $(".checkbox-head").prop('disabled', true)
  $('.div-checkbox-body').addClass('disabled')
  $('.checkbox-body').prop('disabled', true)
}