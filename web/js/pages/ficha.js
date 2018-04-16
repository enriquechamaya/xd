"use strict";
// globals
let objFamiliar = [], objExperienciaLaboral = [], objFormacionAcademica = []
let codigoUbigeoNacimiento = 0, codigoUbigeoResidencia = 0, codigoGradoEstudio = 0
let flagExperienciaLaboral = false, flagRegimenPensionario = true, statusFoto = false

const getNode = node => document.querySelector(node)
const getNodeAll = nodeList => document.querySelectorAll(nodeList)
const helpers = {
  defaultSelect(el) {
    el.setAttribute('disabled', true)
    el.innerHTML = '<option value="0">[SELECCIONAR]</option>'
    $(el).selectpicker('refresh')
  },
  filteredSelect(el, options) {
    el.removeAttribute('disabled')
    el.innerHTML = options
    $(el).selectpicker('refresh')
  },
  createSelectOptions(obj, valueName, textName) {
    let options = '<option value="0">[SELECCIONAR]</option>'
    obj.forEach((data) => {
      options += '<option value="' + data[valueName] + '">' + data[textName] + '</option>'
    });
    return options
  },
  ajaxRequest(obj) {
    return new Promise((resolve, reject) => {
      $.ajax({
        url: obj.url,
        type: obj.type,
        dataType: obj.dataType,
        data: obj.body,
        beforeSend: (xhr, settings) => {
          load(obj.loadingMessage)
        }, success: (response, textStatus, jqXHR) => {
          unload()
          resolve(response)
        }, error: (jqXHR, textStatus, errorThrown) => {
          reject({
            status: textStatus,
            message: `Error making the request`,
            request: obj
          })
        }
      })
    })
  },
  randomId() {
    let S4 = () => (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1)
    return (S4() + S4() + "-" + S4() + "-" + S4() + "-" + S4() + "-" + S4() + S4() + S4());
  },
  validateValueFromArray(value, array, x_value) {
    for (let i = 0; i < array.length; i++) {
      if (value === array[i][x_value]) {
        return true;
      }
    }
    return false;
  },
  dateLessThan(str, str2) {
    str2 = str2 || new Date()
    if (str2 instanceof Date) {
      let anio = str2.getFullYear()
      let mes = str2.getMonth() + 1
      let dia = str2.getDate()
      str2 = `${dia}/${mes}/${anio}`
    }
    if (helpers.toDate(str) > helpers.toDate(str2)) {
      return false
    }
    return true
  },
  toDate(str) {
    let parts = str.split("/");
    return new Date(parts[2], parts[1] - 1, parts[0]);
  },
  addObjToDataTable(element, obj) {
    $(getNode(element)).DataTable().row.add(obj).draw()
  },
  getRow(el, row) {
    return $(getNode(el)).DataTable().row($(row).parents('tr'))
  },
  setDataTableLocalStorage(objName, table) {
    if (localStorage.getItem(objName)) {
      let obj = JSON.parse(localStorage.getItem(objName))
      for (let o in obj) {
        helpers.addObjToDataTable(table, obj[o])
      }
      return obj
    }
  },
  calculateAge(dateOfBirth) {
    let date = new Date()
    let calculateYear = date.getFullYear();
    let calculateMonth = date.getMonth();
    let calculateDay = date.getDate();

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
}
const DOMComponents = {
  init() {
    this.noEntryInputs()
    this.initializePluginComponents()
    this.configComponents();
  },
  noEntryInputs() {
    // datos personales
    new Input({el: '#txtNumeroRUC', param: 'numbers'}).validate()
    new Input({el: '#txtApellidoPaterno', param: 'letters'}).validate()
    new Input({el: '#txtApellidoMaterno', param: 'letters'}).validate()
    new Input({el: '#txtNombre', param: 'letters'}).validate()
    new Input({el: '#dpFechaNacimiento', param: 'date'}).validate()
    new Input({el: '#txtTelefonoFijo', param: 'numbers'}).validate()
    new Input({el: '#txtTelefonoMovil', param: 'numbers'}).validate()

    // datos familiares
    new Input({el: '#txtApellidoPaternoFamiliar', param: 'letters'}).validate()
    new Input({el: '#txtApellidoMaternoFamiliar', param: 'letters'}).validate()
    new Input({el: '#txtNombreFamiliar', param: 'letters'}).validate()
    new Input({el: '#dpFechaNacimientoFamiliar', param: 'date'}).validate()
    new Input({el: '#txtNumeroDocumentoFamiliar', param: 'alphanumeric'}).validate()
    new Input({el: '#txtTelefonoFamiliar', param: 'numbers'}).validate()

    // formacion academica
    new Input({el: '#txtCentroEstudiosFormacionAcademica', rules: 'abcdefghijklmnñopqrstuvwxyz°,.-1234567890 '}).validate()
    new Input({el: '#txtCarreraProfesional', param: 'letters'}).validate()
    new Input({el: '#dpFechaInicioFormacionAcademica', param: 'date'}).validate()
    new Input({el: '#dpFechaFinFormacionAcademica', param: 'date'}).validate()

    // experiencia laboral
    new Input({el: '#txtEmpresaExperienciaLaboral', param: 'letters'}).validate()
    new Input({el: '#txtCargoExperienciaLaboral', param: 'letters'}).validate()
    new Input({el: '#dpFechaInicioExperienciaLaboral', param: 'date'}).validate()
    new Input({el: '#dpFechaFinExperienciaLaboral', param: 'date'}).validate()
    new Input({el: '#txtTelefonoExperienciaLaboral', param: 'numbers'}).validate()
  },
  initializePluginComponents() {

    // datepicker settings
    let date = new Date();
    $("#dpFechaNacimiento, #dpFechaNacimientoFamiliar, #dpFechaInicioExperienciaLaboral,#dpFechaFinExperienciaLaboral, #dpFechaInicioFormacionAcademica, #dpFechaFinFormacionAcademica").datepicker({
      dateFormat: 'dd/mm/yy',
      showButtonPanel: false,
      changeMonth: true,
      changeYear: true,
      yearRange: '1900:' + date.getFullYear(),
      onselect: function () {
        $(this).valid()
      }
    });

    // datepicker
    $.datepicker.regional['es'] = {
      closeText: 'Cerrar',
      prevText: '<Ant',
      nextText: 'Sig>',
      currentText: 'Hoy',
      monthNames: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
      monthNamesShort: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],
      dayNames: ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'],
      dayNamesShort: ['Dom', 'Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb'],
      dayNamesMin: ['Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sá'],
      weekHeader: 'Sm',
      dateFormat: 'dd/mm/yy',
      firstDay: 1,
      isRTL: false,
      showMonthAfterYear: false,
      yearSuffix: ''
    };

    $.datepicker.setDefaults($.datepicker.regional['es']);

    // bootstrap select
    $('.bootstrap-select').selectpicker();

    // Image lightbox
    $('[data-popup="lightbox"]').fancybox({
      padding: 3
    });

    // jasny bootstrap
    $('div.fileinput-preview').children('img').attr('src', 'data:image/png;base64,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').css({
      "max-height": "180px",
      "width": "160px",
      "height": "180px",
      "object-fit": "cover"
    });
  },
  configComponents() {

    getNode('#cboDepartamentoNacimiento').setAttribute('disabled', true)
    $(getNode('#cboDepartamentoNacimiento')).selectpicker('refresh')
    getNodeAll('#dpFechaNacimiento, #dpFechaNacimientoFamiliar, #dpFechaInicioFormacionAcademica, #dpFechaFinFormacionAcademica, #dpFechaInicioExperienciaLaboral, #dpFechaFinExperienciaLaboral')
      .forEach((el) => {
        el.setAttribute('readonly', true)
        el.style.cursor = 'pointer'
      })
    getNodeAll('#cboProvinciaNacimiento, #cboDistritoNacimiento, #cboProvinciaResidencia, #cboDistritoResidencia, #cboEstadoEstudioFormacionAcademica')
      .forEach((el) => helpers.defaultSelect(el))
    getNodeAll('#txtNumeroDocumentoFamiliar, #txtEmpresaExperienciaLaboral, #txtCargoExperienciaLaboral, #dpFechaInicioExperienciaLaboral, #dpFechaFinExperienciaLaboral, #txtTelefonoExperienciaLaboral, #btnAgregarExperienciaLaboral')
      .forEach((el) => el.setAttribute('disabled', true))
  },
  switchery(node) {
    let switches = Array.prototype.slice.call(getNodeAll(node));
    switches.forEach(function (el) {
      let switchery = new Switchery(el, {color: '#FFE66D', secondaryColor: '#F7FFF7'});
    });

  },
  radioButton() {
    $(".styled, .multiselect-container input").uniform({
      radioClass: 'choice'
    });
  }
}
const httpRequest = {
  datosPersonales: {
    listarEstadoCivil() {
      return helpers.ajaxRequest({
        url: '../EstadoCivilServlet',
        type: 'POST',
        dataType: 'json',
        loadingMessage: 'Listando estados civiles',
        body: {
          accion: 'listarEstadoCivil'
        }
      })
    },
    listarNacionalidad() {
      return helpers.ajaxRequest({
        url: '../NacionalidadServlet',
        type: 'POST',
        dataType: 'json',
        loadingMessage: 'Listando nacionalidades',
        body: {
          accion: 'listarNacionalidad'
        }
      })
    },
    listarDepartamentos() {
      return helpers.ajaxRequest({
        url: '../UbigeoServlet',
        type: 'POST',
        dataType: 'json',
        loadingMessage: 'Listado departamentos',
        body: {
          accion: 'listarDepartamento'
        }
      })
    },
    listarProvincia(codigoDepartamento) {
      return helpers.ajaxRequest({
        url: '../UbigeoServlet',
        type: 'POST',
        dataType: 'json',
        loadingMessage: 'Listando provincias',
        body: {
          accion: 'listarProvincia',
          codigoDepartamento: codigoDepartamento
        }
      })
    },
    listarDistrito(codigoDepartamento, codigoProvincia) {
      return helpers.ajaxRequest({
        url: '../UbigeoServlet',
        type: 'POST',
        dataType: 'json',
        loadingMessage: 'Listando distritos',
        body: {
          accion: 'listarDistrito',
          codigoDepartamento: codigoDepartamento,
          codigoProvincia: codigoProvincia
        }
      })
    },
    obtenerUbigeo(codigoDepartamento, codigoProvincia, codigoDistrito) {
      return helpers.ajaxRequest({
        url: '../UbigeoServlet',
        type: 'POST',
        dataType: 'json',
        loadingMessage: 'Obteniendo ubigeo',
        body: {
          accion: 'obtenerCodigoUbigeo',
          codigoDepartamento: codigoDepartamento,
          codigoProvincia: codigoProvincia,
          codigoDistrito: codigoDistrito
        }
      })
    }
  },
  datosFamiliares: {
    listarParentesco() {
      return helpers.ajaxRequest({
        url: '../ParentescoServlet',
        type: 'POST',
        dataType: 'json',
        loadingMessage: 'Listando parentescos',
        body: {
          accion: 'listarParentesco'
        }
      })
    },
    listarTipoDocumento() {
      return helpers.ajaxRequest({
        url: '../TipoDocumentoServlet',
        type: 'POST',
        dataType: 'json',
        loadingMessage: 'Listado documentos',
        body: {
          accion: 'listarTipoDocumento'
        }
      })
    },
    obtenerConfiguracionTipoDocumento(codigoTipoDocumento) {
      return helpers.ajaxRequest({
        url: '../TipoDocumentoServlet',
        type: 'POST',
        dataType: 'json',
        loadingMessage: 'Obteniendo configuración del documento',
        body: {
          accion: 'obtenerLongitudTipoEntrdadaTipoDocumento',
          codigoTipoDocumento: codigoTipoDocumento
        }
      })
    }
  },
  datosFormacionAcademica: {
    listarGradoEstudio() {
      return helpers.ajaxRequest({
        url: '../NivelEstudioServlet',
        type: 'POST',
        dataType: 'json',
        loadingMessage: 'Listando grado de estudios',
        body: {
          accion: 'listarNivelEstudio'
        }
      })
    },
    listarEstadoEstudio(codigoGradoEstudio) {
      return helpers.ajaxRequest({
        url: '../EstadoEstudioServlet',
        type: 'POST',
        dataType: 'json',
        loadingMessage: 'Listando estado de estudios',
        body: {
          accion: 'listarEstadoEstudio',
          codigoNivelEstudio: codigoGradoEstudio
        }
      })
    },
    obtenerGradoEstadoEstudio(codigoGradoEstudio, codigoEstadoEstudio) {
      return helpers.ajaxRequest({
        url: '../NivelEstadoServlet',
        type: 'POST',
        dataType: 'json',
        loadingMessage: 'Obteniendo grado y estado del estudio',
        body: {
          accion: 'obtenerNivelEstado',
          codigoNivelEstudio: codigoGradoEstudio,
          codigoEstadoEstudio: codigoEstadoEstudio
        }
      })
    }
  },
  datosRegimenPensionario: {
    listarRegimenPensionario() {
      return helpers.ajaxRequest({
        url: '../FondoPensionServlet',
        type: 'POST',
        dataType: 'json',
        loadingMessage: 'Listando regimen pensionario',
        body: {
          accion: 'listarFondoPension'
        }
      })
    }
  },
  registrarFicha(obj) {
    return helpers.ajaxRequest({
      url: '../FichaServlet',
      type: 'POST',
      dataType: 'json',
      loadingMessage: 'Validando datos ingresados, espere un momento por favor',
      body: {
        accion: 'registrarFicha',
        json: JSON.stringify(obj)
      }
    })
  }
}
const DOMEvents = () => {
  // datos personales
  getNode('#cboNacionalidad').addEventListener('change', (e) => {
    let nacionalidad = parseInt(e.currentTarget.value)
    codigoUbigeoNacimiento = 0
    if (nacionalidad === 144) {
      getNode('#cboDepartamentoNacimiento').removeAttribute('disabled')
      $('#cboDepartamentoNacimiento').selectpicker('refresh')
    } else {
      getNode('#cboDepartamentoNacimiento').selectedIndex = 0
      getNode('#cboDepartamentoNacimiento').setAttribute('disabled', true)
      $(getNode('#cboDepartamentoNacimiento')).selectpicker('refresh')
      getNodeAll('#cboProvinciaNacimiento, #cboDistritoNacimiento').forEach((el) => helpers.defaultSelect(el))
    }
  })
  getNode('#cboDepartamentoNacimiento').addEventListener('change', (e) => {
    let departamento = parseInt(e.currentTarget.value)
    codigoUbigeoNacimiento = 0
    if (departamento !== 0) {
      httpRequest.datosPersonales.listarProvincia(departamento)
        .then((data) => {
          let options = helpers.createSelectOptions(data.data.provincias, 'codigoProvincia', 'nombreProvincia')
          helpers.filteredSelect(getNode('#cboProvinciaNacimiento'), options)
          helpers.defaultSelect(getNode('#cboDistritoNacimiento'))
        })
    } else {
      getNodeAll('#cboProvinciaNacimiento, #cboDistritoNacimiento').forEach((el) => helpers.defaultSelect(el))
    }
  })
  getNode('#cboProvinciaNacimiento').addEventListener('change', (e) => {
    let departamento = parseInt(getNode('#cboDepartamentoNacimiento').value)
    let provincia = parseInt(e.currentTarget.value)
    codigoUbigeoNacimiento = 0
    if (provincia !== 0) {
      httpRequest.datosPersonales.listarDistrito(departamento, provincia)
        .then((data) => {
          let options = helpers.createSelectOptions(data.data.distritos, 'codigoDistrito', 'nombreDistrito')
          helpers.filteredSelect(getNode('#cboDistritoNacimiento'), options)
        })
    } else {
      helpers.defaultSelect(getNode('#cboDistritoNacimiento'))
    }
  })
  getNode('#cboDistritoNacimiento').addEventListener('change', (e) => {
    let distrito = parseInt(e.currentTarget.value)
    let provincia = parseInt(getNode('#cboProvinciaNacimiento').value)
    let departamento = parseInt(getNode('#cboDepartamentoNacimiento').value)
    httpRequest.datosPersonales.obtenerUbigeo(departamento, provincia, distrito)
      .then((data) => {
        codigoUbigeoNacimiento = data.data.getResultedKey
      })
  })
  getNode('#cboDepartamentoResidencia').addEventListener('change', (e) => {
    let departamento = parseInt(e.currentTarget.value)
    codigoUbigeoResidencia = 0
    if (departamento !== 0) {
      httpRequest.datosPersonales.listarProvincia(departamento)
        .then((data) => {
          let options = helpers.createSelectOptions(data.data.provincias, 'codigoProvincia', 'nombreProvincia')
          helpers.filteredSelect(getNode('#cboProvinciaResidencia'), options)
          helpers.defaultSelect(getNode('#cboDistritoResidencia'))
        })
    } else {
      getNodeAll('#cboProvinciaResidencia, #cboDistritoResidencia').forEach((el) => helpers.defaultSelect(el))
    }
  })
  getNode('#cboProvinciaResidencia').addEventListener('change', (e) => {
    let departamento = parseInt(getNode('#cboDepartamentoResidencia').value)
    let provincia = parseInt(e.currentTarget.value)
    codigoUbigeoResidencia = 0
    if (provincia !== 0) {
      httpRequest.datosPersonales.listarDistrito(departamento, provincia)
        .then((data) => {
          let options = helpers.createSelectOptions(data.data.distritos, 'codigoDistrito', 'nombreDistrito')
          helpers.filteredSelect(getNode('#cboDistritoResidencia'), options)
        })
    } else {
      helpers.defaultSelect(getNode('#cboDistritoResidencia'))
    }
  })
  getNode('#cboDistritoResidencia').addEventListener('change', (e) => {
    let distrito = parseInt(e.currentTarget.value)
    let provincia = parseInt(getNode('#cboProvinciaResidencia').value)
    let departamento = parseInt(getNode('#cboDepartamentoResidencia').value)
    httpRequest.datosPersonales.obtenerUbigeo(departamento, provincia, distrito)
      .then((data) => {
        codigoUbigeoResidencia = data.data.getResultedKey
      })
  })
  $('#lblFoto').on('change.bs.fileinput', (event) => {
    let imgName = getNode('#lblFoto').value.split('\\');
    let imgExtension = imgName[imgName.length - 1].substring(imgName[imgName.length - 1].indexOf('.'), imgName[imgName.length - 1].length);
    if (imgExtension === '.PNG' || imgExtension === '.png' ||
      imgExtension === '.JPG' || imgExtension === '.jpg' ||
      imgExtension === '.JPEG' || imgExtension === '.jpeg') {
      statusFoto = true;
    } else {
      statusFoto = false;
    }
  });

  // datos familiares
  getNode('#cbotipoDocumentoFamiliar').addEventListener('change', (e) => {
    let tipoDocumento = parseInt(e.currentTarget.value)
    if (tipoDocumento !== 0) {
      getNode('#txtNumeroDocumentoFamiliar').removeAttribute('disabled')
      getNode('#txtNumeroDocumentoFamiliar').value = ''
      httpRequest.datosFamiliares.obtenerConfiguracionTipoDocumento(tipoDocumento)
        .then((data) => {
          if (data.status) {
            return {
              longitud: data.data.tipodocumentos[0].longitud,
              tipoEntrada: data.data.tipodocumentos[0].tipoEntrada
            }
          } else {
            alert('ERROR A OBTENER CONFIGURACION DEL TIPO DE DOCUMENTO')
          }
        })
        .then((configTipoDocumento) => {
          getNode('#txtNumeroDocumentoFamiliar').setAttribute('maxlength', configTipoDocumento.longitud)
          getNode('#txtNumeroDocumentoFamiliar').setAttribute('minlength', configTipoDocumento.longitud)
          getNode('#txtNumeroDocumentoFamiliar').setAttribute('entrada', configTipoDocumento.tipoEntrada)

          if (configTipoDocumento.tipoEntrada === 'N') {
            new Input({el: '#txtNumeroDocumentoFamiliar', param: 'numbers'}).validate()
          } else {
            new Input({el: '#txtNumeroDocumentoFamiliar', param: 'alphanumeric'}).validate()
          }
          getNode('#txtNumeroDocumentoFamiliar').focus()
        })
    } else {
      getNode('#txtNumeroDocumentoFamiliar').setAttribute('disabled', true)
      getNode('#txtNumeroDocumentoFamiliar').value = ''
    }
  })
  getNode('#cboParentescoFamiliar').addEventListener('change', (e) => {
    let parentesco = parseInt(e.currentTarget.value)
    if (parentesco === 1 || parentesco === 2) {
      getNode('#cboSexoFamiliar').value = parentesco === 1 ? 'M' : 'F'
      getNode('#cboSexoFamiliar').setAttribute('disabled', true)
    } else {
      getNode('#cboSexoFamiliar').value = 0
      getNode('#cboSexoFamiliar').removeAttribute('disabled')
    }
    $(getNode('#cboSexoFamiliar')).selectpicker('refresh')
  })

  // formacion academica
  getNode('#cboGradoEstudioFormacionAcademica').addEventListener('change', (e) => {
    codigoGradoEstudio = 0
    let gradoEstudio = parseInt(e.currentTarget.value)
    if (gradoEstudio !== 0) {
      httpRequest.datosFormacionAcademica.listarEstadoEstudio(gradoEstudio)
        .then((data) => {
          let options = helpers.createSelectOptions(data.data.estadoestudio, 'codigoEstadoEstudio', 'nombre')
          helpers.filteredSelect(getNode('#cboEstadoEstudioFormacionAcademica'), options)
        })
      getNode('#txtCarreraProfesional').removeAttribute('disabled')
      if (gradoEstudio === 1 || gradoEstudio === 2) {
        getNode('#txtCarreraProfesional').setAttribute('disabled', true)
        getNode('#txtCarreraProfesional').value = ''
      }
    } else {
      helpers.defaultSelect(getNode('#cboEstadoEstudioFormacionAcademica'))
    }
  })
  getNode('#cboEstadoEstudioFormacionAcademica').addEventListener('change', (e) => {
    let estado = parseInt(e.currentTarget.value)
    let grado = parseInt(getNode('#cboGradoEstudioFormacionAcademica').value)
    if (estado !== 0) {
      httpRequest.datosFormacionAcademica.obtenerGradoEstadoEstudio(grado, estado)
        .then((data) => {
          codigoGradoEstudio = data.data.getResultedKey
        })
    }
  })

  // experiencia laboral
  getNode('#chkExperienciaLaboral').addEventListener('click', (e) => {
    let chkExperienciaLaboral = getNode('#chkExperienciaLaboral')
    if (chkExperienciaLaboral.checked) {
      getNode('#textChkExperienciaLaboral').innerHTML = 'SI&nbsp;&nbsp;'
      getNodeAll('#txtEmpresaExperienciaLaboral, #txtCargoExperienciaLaboral, #dpFechaInicioExperienciaLaboral, #dpFechaFinExperienciaLaboral, #txtTelefonoExperienciaLaboral, #btnAgregarExperienciaLaboral')
        .forEach((el) => {
          el.removeAttribute('disabled')
        })
      flagExperienciaLaboral = true
    } else {
      getNode('#textChkExperienciaLaboral').innerHTML = 'NO'
      getNodeAll('#txtEmpresaExperienciaLaboral, #txtCargoExperienciaLaboral, #dpFechaInicioExperienciaLaboral, #dpFechaFinExperienciaLaboral, #txtTelefonoExperienciaLaboral, #btnAgregarExperienciaLaboral')
        .forEach((el) => {
          el.setAttribute('disabled', true)
        })
      flagExperienciaLaboral = false
    }
  })

  // regimen pensionario
  getNode('#chkFondoPension').addEventListener('change', (e) => {
    let chkRegimenPensionario = getNode('#chkFondoPension')
    if (chkRegimenPensionario.checked) {
      getNode('#textChkRegimenPensionario').innerHTML = 'SI&nbsp;&nbsp;'
      getNode('.regimenPensionarioTitulo').innerText = 'SELECCIONE EL SISTEMA PENSIONARIO EN EL QUE SE ENCUENTRA ACTUALMENTE'
      flagRegimenPensionario = true
    } else {
      getNode('#textChkRegimenPensionario').innerHTML = 'NO'
      getNode('.regimenPensionarioTitulo').innerText = 'SELECCIONE EL SISTEMA PENSIONARIO AL QUE DESEA APORTAR'
      flagRegimenPensionario = false
    }
  })

  // registrar fichas
  getNode('#btnRegistrarFicha').addEventListener('click', (e) => {
    let formsValid = formsValidation.valid()
    if (formsValid.status) {
      let valid = Objetos.datosPersonales.validar()
      if (valid.status) {
        delete valid["status"]
        delete valid["node"]
        delete valid["codigoDepartamentoNacimiento"]
        delete valid["codigoProvinciaNacimiento"]
        delete valid["codigoDistritoNacimiento"]
        $.confirm({
          icon: 'glyphicon glyphicon-question-sign',
          theme: 'material',
          closeIcon: true,
          animation: 'scale',
//                type: 'blue',
          type: 'dark',
          title: 'Confirmar',
          content: '<span class="text-semibold">Asegúrese de haber ingresado los datos con total veracidad, si usted está seguro de haber ingresado los datos correctos, de clic en registrar de lo contrario dar clic en cancelar <br/> ¿Desea registrar la ficha?</span>',
          buttons: {
            Registrar: {
              btnClass: 'btn-success',
              action: function () {
                httpRequest.registrarFicha()
                  .then((data) => {
                    console.log(data)
                  })
                  .catch(err => console.log(err))
              }
            },
            Cancelar: {
              btnClass: 'btn-danger'
            }
          }
        })
      } else {
        console.log(valid)
        warningMessage(valid.message, smoothScrolling(valid.node))
      }
    } else {
      warningMessage(formsValid.message, smoothScrolling(formsValid.node))
    }
  })

}
const initRequest = () => {
  return new Promise((resolve, reject) => {
    try {
      httpRequest.datosPersonales.listarEstadoCivil()
        .then((data) => {
          let options = helpers.createSelectOptions(data.data.EstadosCiviles, 'codigoEstadoCivil', 'nombre')
          helpers.filteredSelect(getNode('#cboEstadoCivil'), options)
        })
      httpRequest.datosPersonales.listarNacionalidad()
        .then((data) => {
          let nacionalidad = data.data.Nacionalidades;
          let cboNacionalidad = `<option value="0">[SELECCIONAR]</option>`;
          for (let i in nacionalidad) {
            cboNacionalidad += `<option value="${nacionalidad[i].codigoNacionalidad}" data-content="${nacionalidad[i].gentilicio} <img class='tp-countrie-icon' src='../img/countries/${nacionalidad[i].iso}.svg'"></option>`;
          }
          helpers.filteredSelect(getNode('#cboNacionalidad'), cboNacionalidad)
        })
      httpRequest.datosPersonales.listarDepartamentos()
        .then((data) => {
          let options = helpers.createSelectOptions(data.data.departamentos, 'codigoDepartamento', 'nombreDepartamento')
          helpers.filteredSelect(getNode('#cboDepartamentoResidencia'), options)
          getNode('#cboDepartamentoNacimiento').innerHTML = options
          getNode('#cboDepartamentoNacimiento').setAttribute('disabled', true)
          $(getNode('#cboDepartamentoNacimiento')).selectpicker('refresh')
        })
      httpRequest.datosFamiliares.listarParentesco()
        .then((data) => {
          let options = helpers.createSelectOptions(data.data.parentescos, 'codigoParentesco', 'nombre')
          helpers.filteredSelect(getNode('#cboParentescoFamiliar'), options)
        })
      httpRequest.datosFamiliares.listarTipoDocumento()
        .then((data) => {
          let options = helpers.createSelectOptions(data.data.tipodocumentos, 'codigoTipoDocumento', 'descripcionCorta')
          helpers.filteredSelect(getNode('#cbotipoDocumentoFamiliar'), options)
        })
      httpRequest.datosFormacionAcademica.listarGradoEstudio().
        then((data) => {
          let options = helpers.createSelectOptions(data.data.nivelestudio, 'codigoNivelEstudio', 'nombre')
          helpers.filteredSelect(getNode('#cboGradoEstudioFormacionAcademica'), options)
        })
      httpRequest.datosRegimenPensionario.listarRegimenPensionario()
        .then((data) => {
          if (data.status) {
            let fondopension = data.data.fondopension;
            let rowFondoPension = ``;
            for (let i in fondopension) {
              rowFondoPension += `
                <div class="radio">
                    <label>
                        <input type="radio" id="rd${fondopension[i].descripcionCorta}" name="rdFondoPension" class="styled" value="${fondopension[i].codigoFondoPension}">
                        ${fondopension[i].descripcionLarga} <span class="text-bold">(${fondopension[i].descripcionCorta})</span>
                    </label>
                </div>        
                `;
            }
            getNode('#divFondoPension').innerHTML = rowFondoPension
            getNode('#rdAFP').setAttribute('required', true)
          }
        })
      resolve('ok')
    } catch (err) {
      reject('Error ' + err.message)
    }
  })
}
const formsValidation = {
  init() {
    this.initValidateForm()
    this.initRulesForms()
    this.initActionFormChange()
  },
  initRulesForms() {
    this.datosPersonales.rules()
    this.datosFamiliares.rules()
    this.datosFormacionAcademica.rules()
    this.datosExperienciaLaboral.rules()
    this.datosRegimenPensionario.rules()
  },
  initActionFormChange() {
    this.datosPersonales.formChangeListener()
    this.datosFamiliares.formChangeListener()
    this.datosFormacionAcademica.formChangeListener()
    this.datosExperienciaLaboral.formChangeListener()
    this.datosRegimenPensionario.formChangeListener()
  },
  datosPersonales: {
    rules() {
      $(getNode('#txtNumeroRUC')).rules('add', {
        number: true,
        minlength: 11,
        maxlength: 11
      })
      $(getNode('#txtApellidoPaterno')).rules('add', {
        required: true,
        lettersonly: true
      })
      $(getNode('#txtApellidoMaterno')).rules('add', {
        required: true,
        lettersonly: true
      })
      $(getNode('#txtNombre')).rules('add', {
        required: true,
        lettersonly: true
      })
      $(getNode('#cboSexo')).rules('add', {
        valueNotEquals: '0'
      })
      $(getNode('#cboEstadoCivil')).rules('add', {
        valueNotEquals: '0'
      })
      $(getNode('#dpFechaNacimiento')).rules('add', {
        required: true,
        dateonly: true
      })
      $(getNode('#cboNacionalidad')).rules('add', {
        valueNotEquals: '0'
      })
      $(getNode('#cboDepartamentoNacimiento')).rules('add', {
        valueNotEquals: '0'
      })
      $(getNode('#cboProvinciaNacimiento')).rules('add', {
        valueNotEquals: '0'
      })
      $(getNode('#cboDistritoNacimiento')).rules('add', {
        valueNotEquals: '0'
      })
      $(getNode('#txtDireccionDocumento')).rules('add', {
        required: true
      })
      $(getNode('#txtTelefonoFijo')).rules('add', {
        number: true
      })
      $(getNode('#txtTelefonoMovil')).rules('add', {
        number: true
      })
      $(getNode('#txtCorreoElectronico')).rules('add', {
        required: true,
        email: true
      })
      $(getNode('#cboDepartamentoResidencia')).rules('add', {
        valueNotEquals: '0'
      })
      $(getNode('#cboProvinciaResidencia')).rules('add', {
        valueNotEquals: '0'
      })
      $(getNode('#cboDistritoResidencia')).rules('add', {
        valueNotEquals: '0'
      })
      $(getNode('#txtDireccionResidencia')).rules('add', {
        required: true
      })
      $(getNode('#flImage')).rules('add', {
        required: true,
        accept: 'image/png, image/jpeg, image/jpg'
      })
    },
    formChangeListener() {
      getNode('#formDatosPersonales').addEventListener('change', (e) => {
        $(e.currentTarget).valid()
        this.rules()
        localStorage.setItem('objDatosPersonales', JSON.stringify({
          ruc: getNode('#txtNumeroRUC').value,
          apellidoPaterno: getNode('#txtApellidoPaterno').value,
          apellidoMaterno: getNode('#txtApellidoMaterno').value,
          nombre: getNode('#txtNombre').value,
          sexo: getNode('#cboSexo').value,
          codigoEstadoCivil: getNode('#cboEstadoCivil').value,
          fechaNacimiento: getNode('#dpFechaNacimiento').value,
          codigoNacionalidad: getNode('#cboNacionalidad').value,
          codigoDepartamentoNacimiento: getNode('#cboDepartamentoNacimiento').value,
          codigoProvinciaNacimiento: getNode('#cboProvinciaNacimiento').value,
          codigoDistritoNacimiento: getNode('#cboDistritoNacimiento').value,
          codigoUbigeoNacimiento: codigoUbigeoNacimiento,
          direccionDocumento: getNode('#txtDireccionDocumento').value,
          telefonoFijo: getNode('#txtTelefonoFijo').value,
          telefonoMovil: getNode('#txtTelefonoMovil').value,
          correo: getNode('#txtCorreoElectronico').value,
          codigoDepartamentoResidencia: getNode('#cboDepartamentoResidencia').value,
          codigoProvinciaResidencia: getNode('#cboProvinciaResidencia').value,
          codigoDistritoResidencia: getNode('#cboDistritoResidencia').value,
          codigoUbigeoResidencia: codigoUbigeoResidencia,
          direccionResidencia: getNode('#txtDireccionResidencia').value,
          latitudResidencia: getNode('#latitudResidencia').value,
          longitudResidencia: getNode('#longitudResidencia').value
        }))
      })
    }
  },
  datosFamiliares: {
    rules() {
      $(getNode('#cboParentescoFamiliar')).rules('add', {
        valueNotEquals: '0'
      })
      $(getNode('#txtApellidoPaternoFamiliar')).rules('add', {
        required: true,
        lettersonly: true
      })
      $(getNode('#txtApellidoMaternoFamiliar')).rules('add', {
        required: true,
        lettersonly: true
      })
      $(getNode('#txtNombreFamiliar')).rules('add', {
        required: true,
        lettersonly: true
      })
      $(getNode('#dpFechaNacimientoFamiliar')).rules('add', {
        required: true,
        dateonly: true
      })
      $(getNode('#cbotipoDocumentoFamiliar')).rules('add', {
        valueNotEquals: '0'
      })
      $(getNode('#txtNumeroDocumentoFamiliar')).rules('add', {
        required: true
      })
      $(getNode('#cboSexoFamiliar')).rules('add', {
        valueNotEquals: '0'
      })
      $(getNode('#txtTelefonoFamiliar')).rules('add', {
        number: true
      })
    },
    formChangeListener() {
      getNode('#formDatosFamiliares').addEventListener('change', (e) => {
        $(e.currentTarget).valid()
        this.rules()
      })
    }
  },
  datosFormacionAcademica: {
    rules() {
      $(getNode('#cboGradoEstudioFormacionAcademica')).rules('add', {
        valueNotEquals: '0'
      })
      $(getNode('#cboEstadoEstudioFormacionAcademica')).rules('add', {
        valueNotEquals: '0'
      })
      $(getNode('#cboPosicionInstitucion')).rules('add', {
        valueNotEquals: '0'
      })
      $(getNode('#txtCentroEstudiosFormacionAcademica')).rules('add', {
        required: true
      })
      $(getNode('#txtCarreraProfesional')).rules('add', {
        required: true,
        lettersonly: true
      })
      $(getNode('#dpFechaInicioFormacionAcademica')).rules('add', {
        required: true,
        dateonly: true
      })
      $(getNode('#dpFechaFinFormacionAcademica')).rules('add', {
        required: true,
        dateonly: true
      })
    },
    formChangeListener() {
      getNode('#formFormacionAcademica').addEventListener('change', (e) => {
        $(e.currentTarget).valid()
        this.rules()
      })
    }
  },
  datosExperienciaLaboral: {
    rules() {
      $(getNode('#txtEmpresaExperienciaLaboral')).rules('add', {
        required: true,
        lettersonly: true
      })
      $(getNode('#txtCargoExperienciaLaboral')).rules('add', {
        required: true,
        lettersonly: true
      })
      $(getNode('#dpFechaInicioExperienciaLaboral')).rules('add', {
        required: true,
        dateonly: true
      })
      $(getNode('#dpFechaFinExperienciaLaboral')).rules('add', {
        required: true,
        dateonly: true
      })
      $(getNode('#txtTelefonoExperienciaLaboral')).rules('add', {
        number: true
      })
    },
    formChangeListener() {
      getNode('#formExperienciaLaboral').addEventListener('change', (e) => {
        $(e.currentTarget).valid()
        this.rules()
      })
    }
  },
  datosRegimenPensionario: {
    rules() {
      $(getNode('#rdFondoPension')).rules('add', {
        required: true
      })
    },
    formChangeListener() {
      getNode('#formFondoPension').addEventListener('change', (e) => {
        $(e.currentTarget).valid()
        this.rules()
        localStorage.setItem('objRegimenPensionario', JSON.stringify({
          regimenPensionario: getNode('input[name=rdFondoPension]:checked') === null ? 0 : parseInt(getNode('input[name=rdFondoPension]:checked').value),
          tieneRegimenPensionario: flagRegimenPensionario,
          tituloRegimenPensionario: getNode('.regimenPensionarioTitulo').innerText
        }))
      })
    }
  },
  initValidateForm() {
    getNodeAll('#formDatosPersonales, #formDatosFamiliares, #formFormacionAcademica, #formExperienciaLaboral, #formFondoPension')
      .forEach((el) => $(el).validate())
  },
  valid() {
    if (!$(getNode('#formDatosPersonales')).valid()) {
      return {
        status: false,
        message: 'Ingrese sus datos personales completos, por favor.',
        node: '#formDatosPersonales'
      }
    }
    if (objFamiliar.length === 0) {
      return {
        status: false,
        message: 'Debe ingresar al menos un familiar, por favor.',
        node: '#formDatosFamiliares'
      }
    }
    if (objFormacionAcademica.length === 0) {
      return {
        status: false,
        message: 'Debe ingresar al menos una formación académica, por favor.',
        node: '#formFormacionAcademica'
      }
    }
    if (flagExperienciaLaboral) {
      if (objExperienciaLaboral.length === 0) {
        return {
          status: false,
          message: 'Debe ingresar una experiencia laboral si tiene marcado la opción.',
          node: '#formExperienciaLaboral'
        }
      }
    }
    if (!$(getNode('#formFondoPension')).valid()) {
      return {
        status: false,
        message: 'Debe escoger un fondo pensionario, por favor.',
        node: '#formFondoPension'
      }
    }

    return {
      status: true
    }
  }
}
const getDataFromLocalStorage = {
  init() {
    this.datosPersonales()
    this.datosFamiliares()
    this.formacionAcademica()
    this.experienciaLaboral()
    this.regimenPensionario()
  },
  datosPersonales() {

  },
  datosFamiliares() {
    if (localStorage.getItem('objFamiliar')) {
      let objJsonFamiliar = JSON.parse(localStorage.getItem('objFamiliar'))
      for (let obj in objJsonFamiliar) {
        helpers.addObjToDataTable('#tblFamiliar', objJsonFamiliar[obj])
        if (objJsonFamiliar[obj].codigoParentesco === 1 || objJsonFamiliar[obj].codigoParentesco === 2) {
          getNode(`#cboParentescoFamiliar option[value='${objJsonFamiliar[obj].codigoParentesco}']`).setAttribute('disabled', true)
          $(getNode('#cboParentescoFamiliar')).selectpicker('refresh')
        }
      }
      objFamiliar = objJsonFamiliar
    }
  },
  formacionAcademica() {
    if (localStorage.getItem('objFormacionAcademica')) {
      let objJsonFormacionAcademica = JSON.parse(localStorage.getItem('objFormacionAcademica'))
      for (let o in objJsonFormacionAcademica) {
        helpers.addObjToDataTable('#tblFormacionAcademica', objJsonFormacionAcademica[o])
      }
      objFormacionAcademica = objJsonFormacionAcademica
    }
  },
  experienciaLaboral() {
    if (localStorage.getItem('objExperienciaLaboral')) {
      let objJsonExperienciaLaboral = JSON.parse(localStorage.getItem('objExperienciaLaboral'))
      if (objJsonExperienciaLaboral.length !== 0) {
        getNode('#textChkExperienciaLaboral').innerHTML = 'SI&nbsp;&nbsp;'
        getNodeAll('#txtEmpresaExperienciaLaboral, #txtCargoExperienciaLaboral, #dpFechaInicioExperienciaLaboral, #dpFechaFinExperienciaLaboral, #txtTelefonoExperienciaLaboral, #btnAgregarExperienciaLaboral')
          .forEach((el) => {
            el.removeAttribute('disabled')
          })
        getNode('#chkExperienciaLaboral').setAttribute('checked', 'checked')
        getNode('#chkExperienciaLaboral').checked = true
        flagExperienciaLaboral = true

      }
      for (let obj in objJsonExperienciaLaboral) {
        helpers.addObjToDataTable('#tblExperienciaLaboral', objJsonExperienciaLaboral[obj])
      }
      objExperienciaLaboral = objJsonExperienciaLaboral
    }
    DOMComponents.switchery('#chkExperienciaLaboral')
  },
  regimenPensionario() {
    if (localStorage.getItem('objRegimenPensionario')) {
      let objJsonRegimenPensionario = JSON.parse(localStorage.getItem('objRegimenPensionario'))
      getNode('.regimenPensionarioTitulo').innerText = objJsonRegimenPensionario.tituloRegimenPensionario
      getNode('#chkFondoPension').checked = objJsonRegimenPensionario.tieneRegimenPensionario
      getNodeAll('input[name=rdFondoPension]').forEach((el) => {
        parseInt(el.value) === objJsonRegimenPensionario.regimenPensionario ? el.checked = true : el.checked = false
      })
      getNode('#textChkRegimenPensionario').innerText = objJsonRegimenPensionario.tieneRegimenPensionario === true ? 'SI' : 'NO'
      flagRegimenPensionario = objJsonRegimenPensionario.tieneRegimenPensionario
    }
    DOMComponents.radioButton()
    DOMComponents.switchery('#chkFondoPension')
  }

}
const Objetos = {
  init() {
    this.datosFamiliares.init()
    this.formacionAcademica.init()
    this.experienciaLaboral.init()
  },
  datosPersonales: {
    init() {

    },
    validar() {
      let obj = {}

      obj = {
        ruc: $('#txtNumeroRUC').val().trim(),
        apellidoPaterno: $('#txtApellidoPaterno').val().trim(),
        apellidoMaterno: $('#txtApellidoMaterno').val().trim(),
        nombre: $('#txtNombre').val().trim(),
        sexo: getNode('#cboSexo').value,
        codigoEstadoCivil: parseInt(getNode('#cboEstadoCivil').value),
        fechaNacimiento: getNode('#dpFechaNacimiento').value.trim(),
        codigoNacionalidad: parseInt(getNode('#cboNacionalidad').value),
        codigoDepartamentoNacimiento: parseInt(getNode('#cboDepartamentoNacimiento').value),
        codigoProvinciaNacimiento: parseInt(getNode('#cboProvinciaNacimiento').value),
        codigoDistritoNacimiento: parseInt(getNode('#cboDistritoNacimiento').value),
        codigoUbigeoNacimiento: codigoUbigeoNacimiento,
        direccionDocumento: getNode('#txtDireccionDocumento').value.trim(),
        telefonoFijo: getNode('#txtTelefonoFijo').value.trim(),
        telefonoMovil: getNode('#txtTelefonoMovil').value.trim(),
        correo: getNode('#txtCorreoElectronico').value.trim(),
        codigoDepartamentoResidencia: parseInt(getNode('#cboDepartamentoResidencia').value),
        codigoProvinciaResidencia: parseInt(getNode('#cboProvinciaResidencia').value),
        codigoDistritoResidencia: parseInt(getNode('#cboDistritoResidencia').value),
        codigoUbigeoResidencia: codigoUbigeoResidencia,
        direccionResidencia: getNode('#txtDireccionResidencia').value.trim(),
        latitudResidencia: getNode('#latitudResidencia').value,
        longitudResidencia: getNode('#longitudResidencia').value,
        fondoPensionActivo: flagRegimenPensionario,
        codigoFondoPension: parseInt(getNode('input[name=rdFondoPension]:checked').value),
        experienciaLaboralActivo: flagExperienciaLaboral,
        codigoTipoDocumento: parseInt(getNode('#codigoTipoDocumento').value),
        cargaFamiliar: objFamiliar,
        formacionAcademica: objFormacionAcademica,
        experienciaLaboral: objExperienciaLaboral,
        cargaFamiliarTotal: objFamiliar.length,
        experienciaLaboralTotal: objExperienciaLaboral.length,
        formacionAcademicaTotal: objFormacionAcademica.length,
        foto: defaultImage,
        extension: '.png',
        statusFoto: statusFoto,
        node: '#formDatosPersonales'
      }

      if (helpers.calculateAge(helpers.toDate(obj.fechaNacimiento)) < 18) {
        return obj = {
          status: false,
          message: 'Verifique la edad ingresada.'
        }
      }

      if (obj.latitudResidencia === '' || obj.longitudResidencia === '') {
        return obj = {
          status: false,
          message: 'Por favor ubique en el mapa su lugar de residencia (dirección referencial) <br/> <span class="text-muted">arrastrar el marcador o dar clic sobre el mapa</span>'
        }
      }

      if (!obj.statusFoto) {
        return obj = {
          status: false,
          message: 'Debe cargar una foto, por favor.'
        }
      }
      obj.status = true
      return obj
    }
  },
  datosFamiliares: {
    init() {
      this.agregar()
      this.listar()
      this.eliminar()
    },
    validar() {
      let obj = {}
      obj = {
        id: helpers.randomId(),
        codigoParentesco: parseInt(getNode('#cboParentescoFamiliar').value),
        nombreParentesco: getNode('#cboParentescoFamiliar').options[getNode('#cboParentescoFamiliar').selectedIndex].text.toUpperCase(),
        apellidoPaterno: getNode('#txtApellidoPaternoFamiliar').value.toUpperCase().trim(),
        apellidoMaterno: getNode('#txtApellidoMaternoFamiliar').value.toUpperCase().trim(),
        nombre: getNode('#txtNombreFamiliar').value.toUpperCase().trim(),
        fechaNacimiento: getNode('#dpFechaNacimientoFamiliar').value.trim(),
        codigoTipoDocumento: parseInt(getNode('#cbotipoDocumentoFamiliar').value),
        nombreTipoDocumento: getNode('#cbotipoDocumentoFamiliar').options[getNode('#cbotipoDocumentoFamiliar').selectedIndex].text.toUpperCase(),
        longitud: parseInt(getNode('#txtNumeroDocumentoFamiliar').getAttribute('maxlength')),
        tipoEntrada: getNode('#txtNumeroDocumentoFamiliar').getAttribute('entrada'),
        numeroDocumento: getNode('#txtNumeroDocumentoFamiliar').value.trim(),
        sexo: getNode('#cboSexoFamiliar').value,
        telefono: getNode('#txtTelefonoFamiliar').value.trim()
      }

      if (helpers.validateValueFromArray(obj.numeroDocumento, objFamiliar, 'numeroDocumento')) {
        return obj = {
          status: false,
          message: 'El número de documento es repetido.'
        }
      }

      if (obj.sexo !== 'M' && obj.sexo !== 'F') {
        return obj = {
          status: false,
          message: 'No se encontró el sexo registrado.'
        }
      }

      if (!helpers.dateLessThan(obj.fechaNacimiento)) {
        return obj = {
          status: false,
          message: 'La fecha de nacimiento no puede ser mayor a la actual.'
        }
      }

      if (obj.telefono.length !== 7 && obj.telefono.length !== 9 && obj.telefono.length !== 0) {
        return obj = {
          status: false,
          message: 'Verifique el número de teléfono ingresado.'
        }
      }

      if (obj.numeroDocumento === getNode('#lblNumeroDocumento').innerText) {
        return obj = {
          status: false,
          message: 'Cuidado con quien agregas como familiar.'
        }
      }

      obj.status = true
      return obj

    },
    agregar() {
      getNode('#btnAgregarFamiliar').addEventListener('click', (e) => {
        let obj = this.validar()
        if ($(getNode('#formDatosFamiliares')).valid()) {
          if (obj.status) {
            delete obj["status"]
            objFamiliar.unshift(obj)
            localStorage.setItem('objFamiliar', JSON.stringify(objFamiliar))
            helpers.addObjToDataTable('#tblFamiliar', obj)
            if (obj.codigoParentesco === 1 || obj.codigoParentesco === 2) {
              getNode(`#cboParentescoFamiliar option[value='${obj.codigoParentesco}']`).setAttribute('disabled', true)
              $(getNode('#cboParentescoFamiliar')).selectpicker('refresh')
            }
            successMessage('Familiar agregado!')
            this.clean()
          } else {
            warningMessage(obj.message)
          }
        }
      })
    },
    listar() {
      defaultConfigDataTable()
      // # , apellidos y nombres, parentesco, fecha de nacimiento, tipo de documento, numero documento,
      // sexo, telefono, accion
      let numeroFilas = 1
      $(getNode('#tblFamiliar')).DataTable({
        bInfo: false,
        bPaginate: false,
        columns: [
          {
            data: null,
            render: (data, type, row) => numeroFilas++
          },
          {
            data: null,
            render: (data, type, row) => `${data.apellidoPaterno} ${data.apellidoMaterno} ${data.nombre}`
          },
          {
            data: 'nombreParentesco'
          },
          {
            data: 'fechaNacimiento'
          },
          {
            data: 'nombreTipoDocumento'
          },
          {
            data: 'numeroDocumento'
          },
          {
            data: 'sexo',
            render: (data, type, row) => data === 'M' ? 'MASCULINO' : 'FEMENINO'
          },
          {
            data: 'telefono',
            render: (data, type, row) => data === '' ? '-' : data
          },
          {
            data: null,
            render: (data, type, row) => {
              return `<ul class="icons-list">
                        <li title="Eliminar" class="text-danger-600">
                            <a href="#" onclick="return false" class="eliminarFamiliar"><i class="fa fa-trash-o fa-lg"></i></a>
                        </li>
                      </ul>`;
            }
          }
        ]
      })
    },
    eliminar() {
      $(getNode('#tblFamiliar tbody')).on('click', '.eliminarFamiliar', (e) => {
        let fila = helpers.getRow('#tblFamiliar', e.currentTarget)
        let data = fila.data()
        $.confirm({
          icon: 'glyphicon glyphicon-question-sign',
          theme: 'material',
          closeIcon: true,
          animation: 'scale',
          type: 'dark',
          title: 'Confirmar',
          content: `<span class="text-semibold">¿Seguro de eliminar a ${data.nombre} ${data.apellidoPaterno}?</span>`,
          buttons: {
            Eliminar: {
              btnClass: 'btn-success',
              action: function () {
                let objJsonFamiliar = removeByKey(objFamiliar, {key: 'id', value: data.id});
                getNode(`#cboParentescoFamiliar option[value='${data.codigoParentesco}']`).removeAttribute('disabled')
                $(getNode('#cboParentescoFamiliar')).selectpicker('refresh')
                if (objJsonFamiliar.length === 0) {
                  localStorage.removeItem('objFamiliar');
                } else {
                  localStorage.setItem("objFamiliar", JSON.stringify(objJsonFamiliar));
                }
                fila.remove().draw();
                successMessage("Familiar eliminado");
              }
            },
            Cancelar: {
              btnClass: 'btn-danger'
            }
          }
        });
      })
    },
    clean() {
      getNodeAll('#txtApellidoPaternoFamiliar, #txtApellidoMaternoFamiliar, #txtNombreFamiliar, #dpFechaNacimientoFamiliar, #txtNumeroDocumentoFamiliar, #txtTelefonoFamiliar').forEach((el) => el.value = '')
      getNode('#txtNumeroDocumentoFamiliar').setAttribute('disabled', true)
      getNodeAll('#cboParentescoFamiliar, #cbotipoDocumentoFamiliar, #cboSexoFamiliar').forEach((el) => {
        el.selectedIndex = 0
        $(el).selectpicker('refresh')
      })

    }
  },
  formacionAcademica: {
    init() {
      this.agregar()
      this.listar()
      this.eliminar()
    },
    validar() {
      let obj = {}
      obj = {
        id: helpers.randomId(),
        codigoGrado: parseInt(getNode('#cboGradoEstudioFormacionAcademica').value),
        codigoEstado: parseInt(getNode('#cboEstadoEstudioFormacionAcademica').value),
        nombreGrado: getNode('#cboGradoEstudioFormacionAcademica').options[getNode('#cboGradoEstudioFormacionAcademica').selectedIndex].text.toUpperCase(),
        nombreEstado: getNode('#cboEstadoEstudioFormacionAcademica').options[getNode('#cboEstadoEstudioFormacionAcademica').selectedIndex].text.toUpperCase(),
        codigoNivelEstado: codigoGradoEstudio,
        sectorInstitucion: getNode('#cboPosicionInstitucion').options[getNode('#cboPosicionInstitucion').selectedIndex].text.toUpperCase(),
        centroEstudios: getNode('#txtCentroEstudiosFormacionAcademica').value.toUpperCase().trim(),
        numeroColegiatura: getNode('#txtNumeroColegiatura').value.toUpperCase().trim(),
        nombreCarreraProfesional: getNode('#txtCarreraProfesional').value.toUpperCase().trim(),
        fechaInicio: getNode('#dpFechaInicioFormacionAcademica').value.trim(),
        fechaFin: getNode('#dpFechaFinFormacionAcademica').value.trim()
      }

      if (helpers.toDate(obj.fechaInicio) > helpers.toDate(obj.fechaFin)) {
        return obj = {
          status: false,
          message: 'La fecha de inicio es mayor a la de fin. Verifique por favor.'
        }
      }

      if (obj.codigoGrado === 1 || obj.codigoGrado === 2) {
        obj.nombreCarreraProfesional = ''
      }

      delete obj["codigoGrado"]
      delete obj["codigoEstado"]

      obj.status = true
      return obj

    },
    agregar() {
      getNode('#btnAgregarFormacionAcademica').addEventListener('click', (e) => {
        let obj = this.validar()
        if ($(getNode('#formFormacionAcademica')).valid()) {
          if (obj.status) {
            delete obj["status"]
            objFormacionAcademica.unshift(obj)
            localStorage.setItem('objFormacionAcademica', JSON.stringify(objFormacionAcademica))
            helpers.addObjToDataTable('#tblFormacionAcademica', obj)
            successMessage('Formacion académica agregada!')
            this.clean()
          } else {
            warningMessage(obj.message)
          }
        }
      })
    },
    listar() {
      defaultConfigDataTable()
      let numeroFilas = 1
      $(getNode('#tblFormacionAcademica')).DataTable({
        bInfo: false,
        bPaginate: false,
        columns: [
          {
            data: null,
            render: (data, type, row) => numeroFilas++
          },
          {
            data: 'nombreGrado'
          },
          {
            data: 'nombreEstado'
          },
          {
            data: 'sectorInstitucion'
          },
          {
            data: 'centroEstudios'
          },
          {
            data: 'numeroColegiatura',
            render: (data, type, row) => data === '' ? '-' : data
          },
          {
            data: 'nombreCarreraProfesional',
            render: (data, type, row) => data === '' ? '-' : data
          },
          {
            data: 'fechaInicio'
          },
          {
            data: 'fechaFin'
          },
          {
            data: null,
            render: (data, type, row) => {
              return `<ul class="icons-list">
                        <li title="Eliminar" class="text-danger-600">
                            <a href="#" onclick="return false" class="eliminarFormacionAcademica"><i class="fa fa-trash-o fa-lg"></i></a>
                        </li>
                      </ul>`;
            }
          }
        ]
      })
    },
    eliminar() {
      $(getNode('#tblFormacionAcademica tbody')).on('click', '.eliminarFormacionAcademica', (e) => {
        let fila = helpers.getRow('#tblFormacionAcademica', e.currentTarget)
        let data = fila.data()
        $.confirm({
          icon: 'glyphicon glyphicon-question-sign',
          theme: 'material',
          closeIcon: true,
          animation: 'scale',
          type: 'dark',
          title: 'Confirmar',
          content: `<span class="text-semibold">¿Seguro de eliminar la formación académica?</span>`,
          buttons: {
            Eliminar: {
              btnClass: 'btn-success',
              action: function () {
                let objJsonFormacionAcademica = removeByKey(objFormacionAcademica, {key: 'id', value: data.id});
                if (objJsonFormacionAcademica.length === 0) {
                  localStorage.removeItem('objFormacionAcademica');
                } else {
                  localStorage.setItem("objFormacionAcademica", JSON.stringify(objFormacionAcademica));
                }
                fila.remove().draw();
                successMessage("Formación académica eliminada");
              }
            },
            Cancelar: {
              btnClass: 'btn-danger'
            }
          }
        });
      })
    },
    clean() {
      getNode('#cboEstadoEstudioFormacionAcademica').setAttribute('disabled', true)
      getNodeAll('#txtCentroEstudiosFormacionAcademica, #txtNumeroColegiatura, #txtCarreraProfesional, #dpFechaInicioFormacionAcademica, #dpFechaFinFormacionAcademica').forEach((el) => el.value = '')
      getNodeAll('#cboGradoEstudioFormacionAcademica, #cboEstadoEstudioFormacionAcademica, #cboPosicionInstitucion').forEach((el) => {
        el.selectedIndex = 0
        $(el).selectpicker('refresh')
      })
      codigoGradoEstudio = 0
    }
  },
  experienciaLaboral: {
    init() {
      this.agregar()
      this.listar()
      this.eliminar()
    },
    validar() {
      let obj = {}
      obj = {
        id: helpers.randomId(),
        empresa: getNode('#txtEmpresaExperienciaLaboral').value.trim(),
        cargo: getNode('#txtCargoExperienciaLaboral').value.trim(),
        fechaInicio: getNode('#dpFechaInicioExperienciaLaboral').value.trim(),
        fechaFin: getNode('#dpFechaFinExperienciaLaboral').value.trim(),
        telefono: getNode('#txtTelefonoExperienciaLaboral').value.trim()
      }

      if (toDate(obj.fechaInicio) > toDate(obj.fechaFin)) {
        return obj = {
          status: false,
          message: 'La fecha de inicio es mayor a la de fin. Verifique por favor.'
        }
      }

      if (obj.telefono.length !== 7 && obj.telefono.length !== 9 && obj.telefono.length !== 0) {
        return obj = {
          status: false,
          message: 'Verifique el número de teléfono ingresado.'
        }
      }

      obj.status = true
      return obj
    },
    agregar() {
      getNode('#btnAgregarExperienciaLaboral').addEventListener('click', (e) => {
        if (flagExperienciaLaboral) {
          let obj = this.validar()
          if ($(getNode('#formExperienciaLaboral')).valid()) {
            if (obj.status) {
              delete obj["status"]
              objExperienciaLaboral.unshift(obj)
              localStorage.setItem('objExperienciaLaboral', JSON.stringify(objExperienciaLaboral))
              helpers.addObjToDataTable('#tblExperienciaLaboral', obj)
              successMessage('Experiencia laboral agregada!')
              this.clean()
            } else {
              warningMessage(obj.message)
            }
          }
        } else {
          errorMessage('No puede agregar experiencia laboral.')
        }
      })
    },
    listar() {
      defaultConfigDataTable()
      let numeroFilas = 1
      $(getNode('#tblExperienciaLaboral')).DataTable({
        bInfo: false,
        bPaginate: false,
        columns: [
          {
            data: null,
            render: (data, type, row) => numeroFilas++
          },
          {
            data: 'empresa'
          },
          {
            data: 'cargo'
          },
          {
            data: 'fechaInicio'
          },
          {
            data: 'fechaFin'
          },
          {
            data: 'telefono',
            render: (data, type, row) => data === '' ? '-' : data
          },
          {
            data: null,
            render: (data, type, row) => {
              return `<ul class="icons-list">
                        <li title="Eliminar" class="text-danger-600">
                            <a href="#" onclick="return false" class="eliminarExperienciaLaboral"><i class="fa fa-trash-o fa-lg"></i></a>
                        </li>
                      </ul>`;
            }
          }
        ]
      })
    },
    eliminar() {
      $(getNode('#tblExperienciaLaboral tbody')).on('click', '.eliminarExperienciaLaboral', (e) => {
        let fila = helpers.getRow('#tblExperienciaLaboral', e.currentTarget)
        let data = fila.data()
        $.confirm({
          icon: 'glyphicon glyphicon-question-sign',
          theme: 'material',
          closeIcon: true,
          animation: 'scale',
          type: 'dark',
          title: 'Confirmar',
          content: `<span class="text-semibold">¿Seguro de eliminar la experiencia laboral?</span>`,
          buttons: {
            Eliminar: {
              btnClass: 'btn-success',
              action: function () {
                let objJsonExperienciaLaboral = removeByKey(objExperienciaLaboral, {key: 'id', value: data.id});
                if (objJsonExperienciaLaboral.length === 0) {
                  localStorage.removeItem('objExperienciaLaboral');
                } else {
                  localStorage.setItem("objExperienciaLaboral", JSON.stringify(objExperienciaLaboral));
                }
                fila.remove().draw();
                successMessage("Experiencia laboral eliminada");
              }
            },
            Cancelar: {
              btnClass: 'btn-danger'
            }
          }
        });
      })
    },
    clean() {
      getNodeAll('#txtEmpresaExperienciaLaboral, #txtCargoExperienciaLaboral, #dpFechaInicioExperienciaLaboral, #dpFechaFinExperienciaLaboral, #txtTelefonoExperienciaLaboral')
        .forEach((el) => el.value = '')
    }
  }
}



DOMComponents.init()
initRequest()
  .then(() => {
    DOMEvents()
  })
formsValidation.init()
Objetos.init()
setTimeout(() => {
  getDataFromLocalStorage.init()
}, 0)

/*
 TODO:
 1) ESTILOS DE LA BANDERA AL LISTAR NACIONALIDAD EN FIREFOX
 2) AL CAMBIAR EL TIPO DE DOCUMENTO DE FAMILIAR, LAS REGLAS DE LOS INPUTS DEBE APLICARSE
 3) CUANDO SE SELECCIONA UN DATEPICKER, DEBE BORRAR EL MENSAJE DE ERROR DE "CAMPO REQUERIDO"
 4) CHECKBOX DEBE VALIDAR: SI TRATA DE QUITAR CHECK Y TIENE EXPERIENCIAS REGISTRADAS, NO LE DEBE DEJAR
 
 
 
 
 
 
 
 
 
 
 
 */
