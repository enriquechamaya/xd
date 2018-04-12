let DOMEvents = {
  accionesTabla() {
    $('#tblFichas tbody').on('click', '.verDetalle', (e) => {
      let rowData = $('#tblFichas').DataTable().row($(e.currentTarget).parents('tr')).data()
      httpRequest.obtenerDetalle(rowData)
        .then(data => {
          window.name = JSON.stringify(data.data.persona)
          localStorage.setItem('paginaActual', 'detalleConsultaLotesGeneral')
          window.location = 'fichadetalle.jsp'
        })
    })
  }

};
let inicializarComponentes = {

};
let printElements = {
  pintarLote() {
    document.querySelector('#lblNumeroLote').innerHTML = lote.numeroLote;
    document.querySelector('#lblFechaRegistro').innerHTML = lote.fechaRegistro;
    let estiloTipoLote = lote.tipoLote === 'A' ? 'bg-blue' : 'bg-green';
    let tipoLote = lote.tipoLote === 'A' ? 'ADMINISTRATIVO' : 'DOCENTE';
    document.querySelector('#divLote').innerHTML = `
            <div class="form-group">
              <label class="text-semibold">Tipo de lote:</label>
              <span class="pull-right-sm label ${estiloTipoLote}">${tipoLote}</span>
            </div>
            <div class="form-group">
              <label class="text-semibold">Estado de lote:</label>
              <span class="pull-right-sm ${lote.estilo}">${lote.estadoLote}</span>
            </div>
    `;
  },
  cabezeraTabla() {
    return new Promise((resolve) => {
      let header = '';
      if (lote.tipoLote === 'A') {
        header = `
                <thead class="text-primary-800" style="background-color: #EEEEEE;" >
                  <tr>
                    <th rowspan="2">#</th>
                    <th rowspan="2">APELLIDOS Y NOMBRES</th>
                    <th rowspan="2">TIPO DOCUMENTO</th>
                    <th rowspan="2">N° DOCUMENTO</th>
                    <th rowspan="2">FECHA INGRESO</th>
                    <th colspan="2" style="text-align: center;">COSTOS</th>
                    <th rowspan="2">ACCIONES</th>
                  </tr>
                  <tr>
                    <th>SUELDO ESCALAFON</th>
                    <th>SUELDO PROPUESTO</th>
                  </tr>
                </thead>
                `;
      } else if (lote.tipoLote === 'D') {
        header = `
                <thead class="text-primary-800" style="background-color: #EEEEEE;" >
                  <tr>
                    <th rowspan="2">#</th>
                    <th rowspan="2">APELLIDOS Y NOMBRES</th>
                    <th rowspan="2">TIPO DOCUMENTO</th>
                    <th rowspan="2">N° DOCUMENTO</th>
                    <th rowspan="2">FECHA INGRESO</th>
                    <th colspan="4" style="text-align: center;">COSTOS</th>
                    <th rowspan="2">ACCIONES</th>
                  </tr>
                  <tr>
                    <th>COSTO MENSUAL</th>
                    <th>A</th>
                    <th>B</th>
                    <th>C</th>
                  </tr>
                </thead>
                `;
      } else {
        window.location = 'consultaLotesGeneral.jsp'
      }

      resolve(document.querySelector('#tblFichas').innerHTML = header);
    });
  },
  cuerpoTabla(tipoLote) {
    defaultConfigDataTable();
    let count = 0;
    if (tipoLote === 'A') {
      $(document.querySelector('#tblFichas')).DataTable({
        bInfo: false,
        paging: false,
        data: lote.fichas,
        columns: [
          {
            data: null,
            render: () => ++count
          },
          {
            data: null,
            render: (data) => `${data.apellidoPaterno} ${data.apellidoMaterno} ${data.nombre}`
          },
          {
            data: 'tipoDocumento'

          },
          {
            data: 'numeroDocumento'

          },
          {
            data: 'fechaIngreso'

          },
          {
            data: 'sueldoEscalafon',
            render: (data) => `<b> S/. ${data} </b>`
          },
          {
            data: 'sueldoMensual',
            render: (data) => `<b> S/. ${data} </b>`
          },
          {
            data: null,
            render: () => `
                          <ul class="icons-list">
                              <li class="text-blue-800">
                                  <a href="#" onclick="javascript:return false;" class="verDetalle" data-toggle="tooltip" data-placement="top" title="VER DETALLE">
                                    <i class="fa fa-file-text-o fa-lg"></i>
                                  </a>
                              </li>
                          </ul>
                          `
          }
        ],
        initComplete: (settings, data) => {
          $('.verDetalle').tooltip({
            position: {
              my: "center bottom", // the "anchor point" in the tooltip element
              at: "center top" // the position of that anchor point relative to selected element
            },
            hide: {effect: "explode"}
          });
        },
        fnRowCallback: (row, data, index, iDisplayIndexFull) => {
          if (parseFloat(data.sueldoMensual) > parseFloat(data.sueldoEscalafon)) {
            $(row).css('background-color', '#ffeee2');
          }
        }
      });
    } else if (tipoLote === 'D') {
      $(document.querySelector('#tblFichas')).DataTable({
        bInfo: false,
        paging: false,
        data: lote.fichas,
        columns: [
          {
            data: null,
            render: () => ++count
          },
          {
            data: null,
            render: (data) => `${data.apellidoPaterno} ${data.apellidoMaterno} ${data.nombre}`
          },
          {
            data: 'tipoDocumento'

          },
          {
            data: 'numeroDocumento'

          },
          {
            data: 'fechaIngreso'

          },
          {
            data: 'costoMensualDocente',
            render: (data) => data !== '0.00' ? `<b> S/. ${data} </b>` : ' - '
          },
          {
            data: 'costoaDocente',
            render: (data) => data !== '0.00' ? `<b> S/. ${data} </b>` : ' - '
          },
          {
            data: 'costobDocente',
            render: (data) => data !== '0.00' ? `<b> S/. ${data} </b>` : ' - '
          },
          {
            data: 'costocDocente',
            render: (data) => data !== '0.00' ? `<b> S/. ${data} </b>` : ' - '
          },
          {
            data: null,
            render: () => `
                          <ul class="icons-list">
                              <li class="text-blue-800">
                                  <a href="#" onclick="javascript: return false;" class="verDetalle" data-toggle="tooltip" data-placement="top" title="VER DETALLE">
                                    <i class="fa fa-file-text-o fa-lg"></i>
                                  </a>
                              </li>
                          </ul>
                          `
          }
        ],
        initComplete: (settings, data) => {

        },
        fnRowCallback: (row, data, index, iDisplayIndexFull) => {
          if ($(row).hasClass('odd')) {
            $(row).css('background-color', '#e8f4f4');
          }
        }
      });
    }
  }
};
let httpRequest = {
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
  }
}

let lote;
if (window.name === '') {
  window.location = 'consultaLotesGeneral.jsp';
} else {
  lote = JSON.parse(window.name);
}


printElements.pintarLote();
printElements.cabezeraTabla().then(() => {
  printElements.cuerpoTabla(lote.tipoLote)
  DOMEvents.accionesTabla();
});
