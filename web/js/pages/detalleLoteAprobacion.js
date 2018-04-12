let lote;

let fichas = {
  listarFichas() {
    return new Promise((resolve) => {
      defaultConfigDataTable();
      $(document.querySelector('#tblFichas')).DataTable().destroy()
      $(document.querySelector('#tblFichas')).DataTable({
        bInfo: false,
        paging: false,
        data: lote.fichas,
        columns: [
          {
            data: 'item'
          },
          {
            data: null,
            render: (data, type, row) => {
              return `${data.apellidoPaterno} ${data.apellidoMaterno} ${data.nombre}`
            }
          },
          {
            data: 'tipoDocumento'
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
            render: (data, type, row) => {
              return `<span class="${data.estilo}">${data.estado}</span>`
            }
          },
          {
            data: null,
            render: (data, type, row) => {
              return `
                    <ul class="icons-list">
                      <li class="text-teal-800">
                        <a href="#" onclick="return false" class="verDetalle" data-toggle="tooltip" data-placement="top" title="Ver detalle del lote"><i class="fa fa-file-text-o fa-lg"></i></a>
                      </li>
                    </ul>
                    `
            }
          }
        ],
        initComplete: (settings, data) => {
          resolve(data)
        }
      });
    });
  },
  imprimirFichasAprobadas() {
    document.querySelector('#btnImprimirFichasAprobadas').addEventListener('click', (e) => {
      let codigoLote = document.createElement('input');
      codigoLote.setAttribute("type", "hidden");
      codigoLote.setAttribute("name", "codigoLote");
      codigoLote.setAttribute("id", "codigoLote");
      codigoLote.setAttribute("value", lote.codigoLoteAprobacion);
      document.querySelector('#formFichasAprobadas').appendChild(codigoLote);
      document.querySelector('#formFichasAprobadas').submit();
    });
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

let DOMEvents = {
  accionesTabla() {
    $('#tblFichas tbody').on('click', '.verDetalle', (e) => {
      let rowData = $('#tblFichas').DataTable().row($(e.currentTarget).parents('tr')).data()
      httpRequest.obtenerDetalle(rowData)
              .then(data => {
                window.name = JSON.stringify(data.data.persona)
                localStorage.setItem('paginaActual', 'detalleLoteAprobacion')
                window.location = 'fichadetalle.jsp'
              })
    })
  }
}

let printElements = {
  panelLote() {
    document.querySelector('#lblNumeroLote').innerHTML = lote.numeroLote
    document.querySelector('#lblFechaRegistro').innerHTML = lote.fechaRegistro
  }
}


if (window.name === '') {
  window.location = 'lotepersonal.jsp'
} else {
  lote = JSON.parse(window.name)
}

fichas.listarFichas().then(() => {
  DOMEvents.accionesTabla()
})
printElements.panelLote()
fichas.listarFichas();
fichas.imprimirFichasAprobadas();
