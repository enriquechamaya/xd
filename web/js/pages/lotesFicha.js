let listarLotes = () => {
  defaultConfigDataTable();
  let search = document.querySelector('#txtNumeroLote').value;
  return new Promise((resolve) => {
    $(document.querySelector('#tblLotes')).DataTable().destroy();
    $(document.querySelector('#tblLotes')).DataTable({
      processing: true,
      serverSide: true,
      ajax: {
        url: '../LoteFichaServlet',
        dataType: 'json',
        type: 'post',
        data: {
          accion: 'listarLoteDT',
          search: search
        }
      },
      columns: [{
          data: 'item'
        },
        {
          data: 'numeroLote'
        },
        {
          data: 'fechaRegistro'
        },
        {
          data: 'numeroFichas'
        },
        {
          data: 'estilo'
        },
        {
          data: 'estadoLote',
          render: function (estadoLote) {
            let acciones = ``;
            if (estadoLote === "PENDIENTE") {
              acciones =
                      `
                    <ul class="icons-list">
                        <li class="text-orange-400">
                            <a href="#" onclick="javascript: return false;" class="verDetalleLote" data-toggle="tooltip" data-placement="top" title="Ver detalle lote"><i class="icon-folder-open"></i></a>
                        </li>
                    </ul>
                    `;
            }
            return acciones;
          }
        }
      ],
      fnInitComplete: function (settings, data) {
        $('.verDetalleLote').tooltip({
          template: '<div class="tooltip"><div class="bg-slate-800"><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div></div>'
        });
        resolve(data);
      }
    });
  });
};
let DOMEvents = () => {
  $(document.querySelector('#formFiltroLotes')).validate({
    submitHandler: (form, e) => {
      listarLotes();
      return false;
    }
  });
  document.querySelector('#formFiltroLotes').addEventListener('change', (e) => {
    $(e.currentTarget).valid();
  });
  Ladda.bind('#btnLimpiarFiltroLotes', {
    callback: (instance) => {
      let progress = 0;
      let interval = setInterval(() => {
        progress = Math.min(progress + Math.random(), 2);
        instance.setProgress(progress);
        if (progress === 2) {
          instance.stop();
          clearInterval(interval);
          document.querySelector('#txtNumeroLote').value = '';
          listarLotes();
        }
      }, 200);
    }
  });
  $('#tblLotes tbody').on('click', '.verDetalleLote', (e) => {
    let data = $(document.querySelector('#tblLotes')).DataTable().row($(e.currentTarget).parents('tr')).data();
    obtenerDetalleLote(data.codigoFichaLote)
            .then((obj) => {
              localStorage.setItem('jsonDetalleFichaLoteDocente', JSON.stringify(obj.data.fichadocentes));
              localStorage.setItem('codigoFichaLote', data.codigoFichaLote);
              localStorage.setItem('numeroLote', data.numeroLote);
              localStorage.setItem('lote', JSON.stringify(data));
              window.location = "detalleLoteDocente.jsp";
            });

  });
};
let obtenerDetalleLote = (id) => {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: '../DetalleLoteFichaDocenteServlet',
      dataType: 'json',
      type: 'POST',
      data: {
        codigoFichaLote: id,
        accion: 'listarDetalleLoteFichaDocenteDT'
      },
      success: function (data, textStatus, jqXHR) {
        resolve(data);
      },
      error: function (jqXHR, textStatus, errorThrown) {
        reject("Error al obtener detalle");
      }
    });
  });
};
let initRulesFormValidation = () => {
  $(document.querySelector('#txtNumeroLote')).rules('add', {
    required: true,
    number: true
  });
};

listarLotes();
DOMEvents();
initRulesFormValidation();
localStorage.clear();