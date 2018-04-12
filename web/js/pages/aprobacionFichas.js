'use strict'
let tableData = [];
let flagLote = false;
let httpRequest = {
  listar() {
    defaultConfigDataTable();
    return new Promise((resolve, reject) => {
      $(document.querySelector('#tblFichasPendienteAprobacion')).DataTable().destroy();
      $(document.querySelector('#tblFichasPendienteAprobacion')).DataTable({
        processing: true,
        serverSide: true,
        iDisplayLength: 20,
        ajax: {
          url: '../FichaServlet',
          type: 'POST',
          dataType: 'json',
          data: {
            accion: 'listarFichasPresidenciaDT'
          }
        },
        columnDefs: [
          {
            targets: '0',
            checkboxes: {
              selectRow: true
            }
          }
        ],
        select: {
          style: 'multi'
        },
        order: [[1, 'asc']],
        columns: [
          {
            data: 'item'
          },
          {
            data: null,
            render: (data) => {
              return `${data.apellidoPaterno} ${data.apellidoMaterno} ${data.nombre}`;
            }
          },
          {
            data: 'descripcionCortaTipoDocumento'
          },
          {
            data: 'numeroDocumento'
          },
          {
            data: 'nacionalidad'
          },
          {
            data: 'fechaIngreso'
          },
          {
            data: 'area'
          },
          {
            data: 'cargo'
          },
          {
            data: null,
            className: 'text-center',
            render: (data) => {
              if (data.estadoFicha === 'PERSONAL DESAPROBADO' || data.estadoFicha === 'OBSERVADO POR PRESIDENCIA' || data.estadoFicha === 'FICHA APROBADA POR PRESID.') {
                return `<span class="${data.estilo}">${data.estadoFicha}</span>`;
              } else {
                return `<span class="label bg-orange-600">PENDIENTE</span>`;
              }
//              return `<span class="${data.estilo}">${data.estadoFicha}</span>`;

            }
          },
          {
            data: 'estadoFicha',
            render: (data) => {
              if (data === 'PERSONAL DESAPROBADO') {
                return '';
              } else if (data === 'FICHA APROBADA POR PRESID.') {
                return '';
              } else if (data === 'OBSERVADO POR PRESIDENCIA') {
                return '';
              } else {
                return `<ul class="icons-list">
                        <li class="text-primary-800">
                            <a href="#" onclick="return false" class="verDetalleFicha" data-toggle="tooltip" data-placement="top" title="Ver detalle ficha"><i class="fa fa-file-text-o fa-lg"></i></a>
                        </li>
                      </ul>
                    `;
              }
            }
          }
        ],
        initComplete: (setting, data) => {
          $('.verDetalleFicha').tooltip({
            template: '<div class="tooltip"><div class="bg-slate-800"><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div></div>'
          });
          resolve(data);
        }
      });
    });
  },
  obtenerDetalle(obj) {
    return new Promise((resolve, reject) => {
      $.ajax({
        url: '../FichaServlet',
        type: 'POST',
        dataType: 'json',
        data: {
          accion: 'listarDetalleFichaPresidencia',
          codigoPersona: obj.codigoPersona,
          codigoFicha: obj.codigoFicha
        },
        success: function (data, textStatus, jqXHR) {
          resolve(data);
        }, error: function (jqXHR, textStatus, errorThrown) {
          reject('Error al obtener detalle');
        }
      });
    });
  },
  listarFichasHelper() {
    return new Promise((resolve, reject) => {
      $.ajax({
        url: '../FichaServlet',
        type: 'POST',
        dataType: 'json',
        data: {
          accion: 'listarFichasPresidencia'
        },
        success: function (data, textStatus, jqXHR) {
          if (textStatus === 'success') {
            resolve(data.data.fichas);
          }
        }, error: function (jqXHR, textStatus, errorThrown) {
          reject("Error al listas fichas");
        }
      });
    });
  },
  generarLoteAprobacion(obj) {
    return new Promise((resolve, reject) => {
      $.ajax({
        url: '../LoteAprobacionServlet',
        type: 'POST',
        dataType: 'json',
        data: {
          accion: 'generarLoteAprobacion',
          json: JSON.stringify(obj)
        }, success: function (data, textStatus, jqXHR) {
          if (textStatus === 'success') {
            resolve(data);
          }
        }, error: function (jqXHR, textStatus, errorThrown) {
          reject('Error al generar lote de aprobaicon');
        }
      });
    });
  }
};

let DOMEvents = () => {
  $('#tblFichasPendienteAprobacion tbody').on('click', '.verDetalleFicha', (e) => {
    let data = $(document.querySelector('#tblFichasPendienteAprobacion')).DataTable().row($(e.currentTarget).parents('tr')).data();
    let obj = {codigoFicha: data.codigoFicha, codigoPersona: data.codigoPersona};
    httpRequest.obtenerDetalle(obj)
      .then((data) => {
        if (data.status) {
          window.name = JSON.stringify(data.data.persona);
          window.location = "aprobacionFichasDetalle.jsp";
        } else {
          errorMessage(data.message);
        }

      })
      .catch((err) => {
        console.error(err);
      });
  });

  document.querySelector('#btnGenerarLote').addEventListener('click', (e) => {
    if (flagLote) {
      $.confirm({
        icon: 'glyphicon glyphicon-question-sign',
        theme: 'material',
        closeIcon: true,
        animation: 'scale',
        type: 'dark',
        title: 'Confirmar',
        content: '<span class="text-semibold">¿Seguro generas el lote?</span> <br/> <span class="text-danger">Asegúrese de haber ingresado los datos correctamente</span>',
        buttons: {
          Aceptar: {
            btnClass: 'btn-success',
            action: function () {

              let obj = {
                data: tableData,
                count: tableData.length
              };
              if (obj.count !== 0) {
                httpRequest.generarLoteAprobacion(obj).then((data) => {
                  $.confirm({
                    icon: 'fa fa-check fa-lg',
                    title: 'Registro correcto!',
                    content: `<b>${data.message}</b>`,
                    type: 'green',
                    scrollToPreviousElement: false,
                    scrollToPreviousElementAnimate: false,
                    buttons: {
                      Aceptar: {
                        text: 'Aceptar',
                        btnClass: 'btn-green',
                        action: function () {
                          window.name = '';
                          httpRequest.listar();
                          httpRequest.listarFichasHelper().then((fichas) => {
                            fichas.map((ficha, indice, fichas) => {
                              if (ficha.estadoFicha === 'OBSERVADO POR PRESIDENCIA' || ficha.estadoFicha === 'PERSONAL DESAPROBADO' || ficha.estadoFicha === 'FICHA APROBADA POR PRESID.') {
                                tableData.push({codigoFicha: ficha.codigoFicha});
                              }
                            });
                            if (fichas.length === 0) {
                              document.querySelector('#btnGenerarLote').setAttribute('disabled', true);
                            } else {
                              flagLote = true;
                            }
                          });
                          window.location = 'lotepersonal.jsp'
                        }
                      },
//                      'Ir al detalle': {
//                        btnClass: 'btn-orange',
//                        action: () => {
//                          console.log(data.data.getResultedKey);
//                        }
//                      }
                    }
                  });
                });
              } else {
                warningMessage('Debe haber una ficha evaluada como mínimo.');
              }
            }
          },
          Cancelar: {
            btnClass: 'btn-danger'
          }
        }
      });
    }
  });
};
httpRequest.listar()
DOMEvents();
window.name = ''
httpRequest.listarFichasHelper()
  .then((fichas) => {
    if (fichas.length !== 0) {
      fichas.map((ficha, indice, fichas) => {
        if (ficha.estadoFicha === 'OBSERVADO POR PRESIDENCIA' || ficha.estadoFicha === 'PERSONAL DESAPROBADO' || ficha.estadoFicha === 'PERSONAL APROBADO') {
          tableData.push({codigoFicha: ficha.codigoFicha})
        }
      });
      if (fichas.length === 0) {
        document.querySelector('#btnGenerarLote').setAttribute('disabled', true)
      } else {
        flagLote = true
      }
    }
  })
  .catch(err => console.log(err))

