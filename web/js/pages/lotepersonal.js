let inicializarComponentes = {
  bootstrapSelect() {
    $('.bootstrap-select').selectpicker();
  },
  datePicker() {
    $(".datepicker").prop('readonly', true);
    $(".datepicker").datepicker({
      dateFormat: 'dd/mm/yy',
      showButtonPanel: false,
      onSelect: function () {
        console.log(this);
        $(this).valid();
      }
    });
    let date = new Date();
    $(".datepicker").datepicker("setDate", date);
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
  }
};
let lote = {
  listarFichasPresidenciaDT() {
    return new Promise((resolve) => {
      defaultConfigDataTable();
      $(document.querySelector('#tblLotes')).DataTable().destroy();
      $(document.querySelector('#tblLotes')).DataTable({
        serverSide: true,
        processing: true,
        ajax: {
          url: '../LoteAprobacionServlet',
          type: 'POST',
          dataType: 'json',
          data: {
            accion: 'listarFichasPresidenciaDT',
            json: JSON.stringify(getJSONCondition())
          }
        },
        columns: [
          {
            data: 'item'
          },
          {
            data: 'numeroLote'
          },
          {
            data: 'fechaRegistro'
          },
          {
            data: 'cantidadFichas'
          },
          {
            data: null,
            render: (data, type, row) => {
              return `
                    <ul class="icons-list">
                      <li class="text-orange">
                        <a href="#" onclick="return false" class="verDetalle" data-toggle="tooltip" data-placement="top" title="Ver detalle del lote"><i class="icon-folder-open"></i></a>
                      </li>
                    </ul>
                    `;
            }
          }
        ],
        initComplete: (settings, data) => {
          $(document.querySelector('.verDetalle')).tooltip({
            position: {
              my: "center bottom", // the "anchor point" in the tooltip element
              at: "center top" // the position of that anchor point relative to selected element
            },
            hide: {effect: "explode"}
          });
          resolve(data);
        }
      });
    });
  },
  obtenerDetalleLote(obj) {
    return new Promise((resolve, reject) => {
      $.ajax({
        url: '../LoteAprobacionServlet',
        type: 'POST',
        dataType: 'json',
        data: {
          accion: 'obtenerDetalleLote',
          json: JSON.stringify(obj)
        },
        success: (data, textStatus, jqXHR) => {
          if (textStatus === 'success') {
            resolve(data);
          }
        }, error: (jqXHR, textStatus, errorThrown) => {
          reject('Error al obtener detalle de lote');
        }
      });
    });
  }
};
let DOMEvents = () => {
  $('#tblLotes tbody').on('click', '.verDetalle', (e) => {
    let rowData = helpers.getRowData($(e.currentTarget).parents('tr'));
    lote.obtenerDetalleLote({codigoLote: rowData.codigoLoteAprobacion})
      .then(data => {
        if (data.status) {
          return data.data.fichas;
        } else {
          errorMessage(data.message);
        }
      })
      .then(fichas => {
        rowData.fichas = fichas;

        window.name = JSON.stringify(rowData);

        window.location = 'detalleLoteAprobacion.jsp';
      })
      .catch(err => {
        errorMessage(err);
      });
  });
  document.querySelector('#cboCriterio').addEventListener('change', (e) => {
    let criterio = parseInt(document.querySelector('#cboCriterio').value);
    if (criterio === 0) {
      document.querySelector('#divCriterios').innerHTML = '';
      document.querySelector('#divCriterios2').innerHTML = '';
    } else if (criterio === 1) {
      document.querySelector('#divCriterios2').innerHTML = '';
      printInputs.numeroLote().then(() => {
        $('#txtNumeroLote').validCampo('1234567890');
      });
    } else if (criterio === 2) {
      document.querySelector('#divCriterios2').innerHTML = '';
      printInputs.filtroFecha().then(() => {
        $('.bootstrap-select').selectpicker();
        accion.filtroFecha();
      });
    } else if (criterio === 3) {
      document.querySelector('#divCriterios2').innerHTML = '';
      printInputs.filtroFichas().then(() => {
        $('.bootstrap-select').selectpicker();
        accion.filtroFicha();
      });
    }
  });
  $('#formFiltroLotes').validate({
    submitHandler: () => {
      lote.listarFichasPresidenciaDT().then(data => console.log(data));
      return false;
    }
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
          $('#cboCriterio').val(0);
          $('#cboCriterio').selectpicker('refresh');
          $('#divCriterios').html('');
          $('#divCriterios2').html('');
          lote.listarFichasPresidenciaDT();
        }
      }, 200);
    }
  });
};
let accion = {
  filtroFecha() {
    document.querySelector('#cboFiltroFecha').addEventListener('change', (e) => {
      let filtro = parseInt(document.querySelector('#cboFiltroFecha').value);
      if (filtro === 0) {
        document.querySelector('#divCriterios2').innerHTML = '';
      } else {
        printInputs.filtroFechaPor(filtro).then(() => {
          inicializarComponentes.datePicker();
        });
      }
    });
  },
  filtroFicha() {
    document.querySelector('#cboFiltroFicha').addEventListener('change', (e) => {
      let filtro = parseInt(document.querySelector('#cboFiltroFicha').value);
      if (filtro === 0) {
        document.querySelector('#divCriterios2').innerHTML = '';
      } else {
        printInputs.filtroFichasPor().then(() => {
          $('#txtNumeroFicha').validCampo('1234567890');
        });
      }
    });
  }
};
let printInputs = {
  numeroLote() {
    return new Promise((resolve) => {
      let input = `<div class="col-md-3">
                    <div class="form-group">
                      <label for="txtNumeroLote" class="text-semibold">Número de lote <span class="text-danger">*</span></label>
                      <input maxlength="11" type="text" class="form-control text-uppercase" placeholder="Ingresar número de lote" id="txtNumeroLote" name="txtNumeroLote" maxlength="30" autocomplete="off">
                    </div>
                  </div>`;
      resolve(document.querySelector('#divCriterios').innerHTML = input);
    });
  },
  filtroFecha() {
    return new Promise((resolve) => {
      let input = `<div class="col-md-3">
                    <div class="form-group">
                      <label for="cboFiltroFecha" class="text-semibold">Filtro por <span class="text-danger">*</span></label>
                      <select id="cboFiltroFecha" name="cboFiltroFecha" class="bootstrap-select" data-width="100%">
                        <option value="0">[SELECCIONAR]</option>
                        <option value="1">MAYOR A</option>
                        <option value="2">MENOR A</option>
                        <option value="3">IGUAL A</option>
                        <option value="4">ENTRE</option>
                      </select>
                    </div>
                  </div>`;
      resolve(document.querySelector('#divCriterios').innerHTML = input);
    });
  },
  filtroFichas() {
    return new Promise((resolve) => {
      let input = `<div class="col-md-3">
                    <div class="form-group">
                      <label for="cboFiltroFicha" class="text-semibold">Filtro por <span class="text-danger">*</span></label>
                      <select id="cboFiltroFicha" name="cboFiltroFicha" class="bootstrap-select" data-width="100%">
                        <option value="0">[SELECCIONAR]</option>
                        <option value="1">MAYOR A</option>
                        <option value="2">MENOR A</option>
                        <option value="3">IGUAL A</option>
                      </select>
                    </div>
                  </div>`;
      resolve(document.querySelector('#divCriterios').innerHTML = input);
    });
  },
  filtroFechaPor(filtro) {
    return new Promise((resolve) => {
      let input = '';
      if (filtro === 4) {
        input = `<div class="col-md-3">
                       <label for="dpFechaRegistro" class="text-semibold">Fecha desde <span class="text-danger">*</span></label>
                       <div class="input-group">
                         <span class="input-group-addon"><i class="icon-calendar"></i></span>
                         <input type="text" class="form-control datepicker" placeholder="Fecha desde" id="dpFechaRegistro" name="dpFechaRegistro" autocomplete="off">
                       </div>        
                     </div>
                     <div class="col-md-3">
                       <label for="dpFechaRegistro2" class="text-semibold">Fecha hasta <span class="text-danger">*</span></label>
                       <div class="input-group">
                         <span class="input-group-addon"><i class="icon-calendar"></i></span>
                         <input type="text" class="form-control datepicker" placeholder="Fecha hasta" id="dpFechaRegistro2" name="dpFechaRegistro2" autocomplete="off">
                       </div>        
                     </div>
                      `;
      } else {
        input = `<div class="col-md-3">
                     <label for="dpFechaRegistro" class="text-semibold">Fecha de registro <span class="text-danger">*</span></label>
                       <div class="input-group">
                         <span class="input-group-addon"><i class="icon-calendar"></i></span>
                         <input type="text" class="form-control datepicker" placeholder="Fecha de registro de ficha" id="dpFechaRegistro" name="dpFechaRegistro" autocomplete="off">
                       </div>        
                     </div>`;
      }

      resolve(document.querySelector('#divCriterios2').innerHTML = input);
    });
  },
  filtroFichasPor() {
    return new Promise((resolve) => {
      let input = `<div class="col-md-3">
                    <div class="form-group">
                      <label for="txtNumeroFicha" class="text-semibold">Número de fichas<span class="text-danger">*</span></label>
                      <input maxlength="3" type="text" class="form-control text-uppercase" placeholder="Ingresar número de ficha" id="txtNumeroFicha" name="txtNumeroFicha" maxlength="4" autocomplete="off">
                    </div>
                  </div>`;
      resolve(document.querySelector('#divCriterios2').innerHTML = input);
    });
  }
};
let helpers = {
  getRowData(row) {
    return $(document.querySelector('#tblLotes')).DataTable().row(row).data();
  }
};

let getJSONCondition = () => {
  let criterio = parseInt(document.querySelector('#cboCriterio').value), filtro = 0, search = '', search2 = '';

  switch (criterio) {
    case 0:
      break
    case 1:
      search = document.querySelector('#txtNumeroLote').value;
      break
    case 2:
      filtro = parseInt(document.querySelector('#cboFiltroFecha').value);
      switch (filtro) {
        case 0:
          break
        case 1:
        case 2:
        case 3:
          search = document.querySelector('#dpFechaRegistro').value;
          break
        case 4:
          search = document.querySelector('#dpFechaRegistro').value;
          search2 = document.querySelector('#dpFechaRegistro2').value;
          break
        default:
          warningMessage('Método no permitido, al parecer esta manipulando el código. filtro fecha');
//          setTimeout(() => {
//            window.location = 'templates/close.jsp';
//          }, 3000);
      }
      break
    case 3:
      filtro = parseInt(document.querySelector('#cboFiltroFicha').value);
      switch (filtro) {
        case 0:
          break
        case 1:
        case 2:
        case 3:
          search = document.querySelector('#txtNumeroFicha').value;
          break
        default:
          warningMessage('Método no permitido, al parecer esta manipulando el código. filtro ficha');
//          setTimeout(() => {
//            window.location = 'templates/close.jsp';
//          }, 3000);
      }
      break
    default:
      warningMessage('Método no permitido, al parecer esta manipulando el código. criterio');
//      setTimeout(() => {
//        window.location = 'templates/close.jsp';
//      }, 3000);
  }
  return {
    criterio: criterio,
    filtro: filtro,
    search: search,
    search2: search2
  };
};

let formFiltroLotes = {
  init() {
    this.initRulesForm()
    this.initValidateForm()
    this.actionFormChange()
  },
  initRulesForm() {
    $('#cboCriterio').rules('add', {
      valueNotEquals: '0'
    });

    $('#txtNumeroLote').rules('add', {
      required: true,
      number: true
    });

    $('#cboFiltroFecha').rules('add', {
      valueNotEquals: '0'
    });

    $('#dpFechaRegistro').rules('add', {
      required: true,
      dateonly: true
    });

    $('#dpFechaRegistro2').rules('add', {
      required: true,
      dateonly: true
    });

    $('#cboFiltroFicha').rules('add', {
      valueNotEquals: '0'
    });

    $('#txtNumeroFicha').rules('add', {
      required: true,
      number: true
    });
  },
  initValidateForm() {
    $('#formFiltroLotes').validate();
  },
  actionFormChange() {
    $('#formFiltroLotes').on('change', (e) => {
      $(e.currentTarget).valid();
      this.initRulesForm();
    });
  }
};

inicializarComponentes.bootstrapSelect();
lote.listarFichasPresidenciaDT();
DOMEvents();
formFiltroLotes.init()
window.name = '';

