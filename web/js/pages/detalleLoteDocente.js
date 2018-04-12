$(function () {
  getDataFromLocalStorage();
  listarFichasDocente();
  inicializarComponentes();
  DOMEvents();
  imprimirLote();
  printComponents.panelLote();
});

let lote;
let data;
let codigoFichaLote;
let numeroLote = localStorage.getItem('numeroLote');
let numeroFilas;
let json = {};
let getDataFromLocalStorage = () => {
  if (localStorage.getItem('codigoFichaLote')) {
    codigoFichaLote = localStorage.getItem('codigoFichaLote');
  } else {
    window.location = 'lotesFicha.jsp';
  }
  if (localStorage.getItem('jsonDetalleFichaLoteDocente')) {
    data = JSON.parse(localStorage.getItem('jsonDetalleFichaLoteDocente'));
    numeroFilas = data.length;
  } else {
    window.location = 'lotesFicha.jsp';
  }

  if (localStorage.getItem('lote')) {
    lote = JSON.parse(localStorage.getItem('lote'));
  } else {
    window.location = "lotesFicha.jsp";
  }
};

let listarFichasDocente = () => {
  defaultConfigDataTable();
  let contador = 0;
  return new Promise((resolve) => {
    $(document.querySelector('#tblFichasDocente')).DataTable().destroy();
    $(document.querySelector('#tblFichasDocente')).DataTable({
      data: data,
      bInfo: false,
      paging: false,
      columnDefs: [{
          targets: 0,
          orderable: false,
          width: '2%',
          className: 'text-center'
        },
        {
          targets: 1,
          orderable: false,
          width: '20%'
        },
        {
          targets: 2,
          orderable: false,
          width: '10%'
        },
        {
          targets: 3,
          orderable: false,
          width: '10%',
          className: 'text-center'
        },
        {
          targets: 4,
          orderable: false,
          width: '7%',
          className: 'text-center'
        },
        {
          targets: 5,
          orderable: false,
          width: '6%',
          className: 'text-center'
        },
        {
          targets: 6,
          orderable: false,
          width: '6%',
          className: 'text-center'
        },
        {
          targets: 7,
          orderable: false,
          width: '6%',
          className: 'text-center'
        }
      ],
      columns: [{
          data: null,
          render: () => ++contador
        },
        {
          data: null,
          render: (data) => `${data.apellidoPaterno} ${data.apellidoMaterno} ${data.nombre}`
        },
        {
          data: 'numeroDocumento'
        },
        {
          data: 'fechaInicio'
        },
        {
          data: 'costoMensual',
          render: (data) => {
            return data === '0.00' ? data : `<a href="#" class="costoMensual"  data-prop="" data-type="text" data-inputclass="form-control" data-pk="1" data-value="${data}" data-title="Costo mensual"></a>`;
          }
        },
        {
          data: 'costoa',
          render: (data) => {
            return data === '0.00' ? data : `<a href="#" class="costoa" data-type="text" data-inputclass="form-control" data-pk="1" data-value="${data}" data-title="Costo mensual"></a>`;
          }
        },
        {
          data: 'costob',
          render: (data) => {
            return data === '0.00' ? data : `<a href="#" class="costob" data-type="text" data-inputclass="form-control" data-pk="1" data-value="${data}" data-title="Costo mensual"></a>`;
          }
        },
        {
          data: 'costoc',
          render: (data) => {
            return data === '0.00' ? data : `<a href="#" class="costoc" data-type="text" data-inputclass="form-control" data-pk="1" data-value="${data}" data-title="Costo mensual"></a>`;
          }
        },
        {
          data: null,
          render: () => `<input type="text" class="form-control observacion" value="" placeholder="Ingrese observación"/>`
        }
      ],
      fnCreatedRow: function (row, data, index) {

      }
    });
  });
};

let obtenerDatosTabla = () => $(document.querySelector('#tblFichasDocente')).DataTable().rows().data();

let DOMEvents = () => {
  document.querySelector('#btnActualizarLote').addEventListener('click', (e) => {
    $.confirm({
      icon: 'glyphicon glyphicon-question-sign',
      theme: 'material',
      closeIcon: true,
      animation: 'scale',
      type: 'dark',
      title: 'Confirmar',
      content: '<span class="text-semibold">Asegúrese de revisar bien la información de los sueldos de los docentes. </span>',
      buttons: {
        Actualizar: {
          btnClass: 'btn-success',
          action: function () {
            validarCambios()
              .then((obj) => {
                if (obj.estadoLote) {
                  let fichas = obj.fichas;
                  for (let i in fichas) {
                    if (fichas[i].estadoFicha) {
                      if (fichas[i].observacion === "") {
                        let input = $($($('#tblFichasDocente tbody').find('tr')[i]).find('td')[8]).children()
                        input.addClass('tp-error-input-observacion')
                        input.after('<span style="color: #F44336;">Debe ingresar una observación</span>')
                      } else {
                        registrarSueldoDocente(obj)
                          .then((data) => {
                            if (data.status) {
                              $.confirm({
                                icon: 'fa fa-check fa-lg',
                                title: 'Registro éxitoso!',
                                content: `<b>${data.message}</b>`,
                                type: 'green',
                                scrollToPreviousElement: false,
                                scrollToPreviousElementAnimate: false,
                                buttons: {
                                  Aceptar: {
                                    text: 'Aceptar',
                                    btnClass: 'btn-green',
                                    action: function () {
                                      window.location = 'main.jsp';
                                    }
                                  }
                                }
                              });
                            } else {
                              errorMessage(data.message);
                            }
                          });
                      }
                    } else {
                      $($($('#tblFichasDocente tbody').find('tr')[i]).find('td')[8]).children().removeClass('tp-error-input-observacion');
                    }
                  }
                } else {
                  $('#tblFichasDocente tbody tr').each((i, el) => {
                    $($(el).find('td')[8]).children().removeClass('tp-error-input-observacion');
                  });
                  registrarSueldoDocente(obj)
                    .then((data) => {
                      if (data.status) {
                        $.confirm({
                          icon: 'fa fa-check fa-lg',
                          title: 'Registro éxitoso!',
                          content: `<b>${data.message}</b>`,
                          type: 'green',
                          scrollToPreviousElement: false,
                          scrollToPreviousElementAnimate: false,
                          buttons: {
                            Aceptar: {
                              text: 'Aceptar',
                              btnClass: 'btn-green',
                              action: function () {
                                window.location = 'main.jsp';
                              }
                            }
                          }
                        });
                      } else {
                        errorMessage(data.message);
                      }
                    });
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

  $('.observacion').on('change', (e) => {
    if ($(e.currentTarget).val() !== '') {
      $(e.currentTarget).removeClass('tp-error-input-observacion');
      $(e.currentTarget).next().remove()
    }
  });
};

let registrarSueldoDocente = (obj) => {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: '../DetalleLoteFichaDocenteServlet',
      dataType: 'json',
      type: 'POST',
      data: {
        accion: 'registrarSueldosPresidenciaLoteDocente',
        json: JSON.stringify(obj)
      },
      success: function (data, textStatus, jqXHR) {
        resolve(data);
      },
      error: function (jqXHR, textStatus, errorThrown) {
        reject(errorThrown);
      }
    });
  });
};

let validarCambios = () => {
  return new Promise((resolve) => {
    let new_data = getDataFromTableAsJson();
    let flag = false;
    for (let i = 0; i < data.length; i++) {
      if (data[i].costoMensual === new_data[i].costoMensual &&
        data[i].costoa === new_data[i].costoa &&
        data[i].costob === new_data[i].costob &&
        data[i].costoc === new_data[i].costoc) {
        new_data[i].estadoFicha = false;
      } else {
        new_data[i].estadoFicha = true;
        flag = true;
      }
    }
    json.fichas = new_data;
    json.codigoLote = parseInt(codigoFichaLote);
    json.estadoLote = flag;
    resolve(json);
  });
};

let getDataFromTableAsJson = () => {
  let table = $(document.querySelector('#tblFichasDocente tbody'));
  let jsonData = [];
  table.find('tr').each(function (i, el) {
    let row = $(this).find('td');
    let codigoFicha = $('#tblFichasDocente').DataTable().row(i).data().codigoFicha;
    let codigoPago = $('#tblFichasDocente').DataTable().row(i).data().codigoPago;
    let costoMensual = row.eq(4).text();
    let costoa = row.eq(5).text();
    let costob = row.eq(6).text();
    let costoc = row.eq(7).text();
    let observacion = row.eq(8).find('input').val();
    let json = `
                {
                  "codigoFicha" : ${codigoFicha},
                  "codigoPago" : ${codigoPago},
                  "costoMensual": "${costoMensual}",
                  "costoa":"${costoa}",
                  "costob":"${costob}",
                  "costoc":"${costoc}",
                  "observacion":"${observacion}"
                }
                `;
    jsonData.push(JSON.parse(json));
  });
  return jsonData;
};

let inicializarComponentes = () => {
  pluginComponents.inputTouchSpin();
};

let pluginComponents = {
  inputTouchSpin() {
    // Basic
    $('.costoMensual, .costoa, .costob, .costoc').editable({
      clear: false
    });
    $('.costoMensual').on('shown', function (e, editable) {
      editable.input.$input.TouchSpin({
        min: 0,
        max: 10000,
        step: 0.1,
        decimals: 2
      }).parent().parent().addClass('editable-touchspin');
    });
    $('.costoa, .costob, .costoc').on('shown', function (e, editable) {
      editable.input.$input.TouchSpin({
        min: 0,
        max: 30,
        step: 0.1,
        decimals: 2
      }).parent().parent().addClass('editable-touchspin');
    });
  }
};

let printComponents = {
  panelLote() {
    document.querySelector('#lblNumeroLote').innerHTML = numeroLote;
    document.querySelector('#lblFechaRegistro').innerHTML = lote.fechaRegistro;
    document.querySelector('#divEstadoLote').innerHTML = `
                <div class="form-group">
                  <label class="text-semibold">Estado de lote:</label>
                  <span class="pull-right-sm label bg-warning-600"">${lote.estadoLote}</span>
                </div>
    `
  }

};

/**
 * Imprime sueldos de los docentes
 * para posteriormente el presidente
 * evalue o rechace algunas fichas
 * por "x" motivos que se indicaran
 * ahi mismo
 */
function imprimirLote() {
  $('#btnImprimir').on('click', () => {
    $('#numeroLote').val(numeroLote);
    $('#numeroFilas').val(numeroFilas);
    $('#data').val(JSON.stringify(data));
    $('#formPDF').submit();
  });
}