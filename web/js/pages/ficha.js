$(function () {
  inicializarComponentes();
  configuracionComponentes();
  init();
  dontLeaveMe()
});

// init
function init() {

  initCargaFamiliar();
  initFormacionAcademica();
  initDatosPersonales();
  initFondoPension();

  validarRegistroFicha();

  listarEstadoCivil();
  listarNacionalidadResponse();
  listarParentescoResponse('cboParentescoFamiliar');
  listarCarreraProfesional_v2();

  listarTipoDocumentoResponse('cbotipoDocumentoFamiliar');
  listarNivelEstudioResponse('cboGradoEstudioFormacionAcademica', 0);
  listarFondoPensionResponse();

  initExperienciaLaboral();

  validarFoto();

}

let dontLeaveMe = () => {
  // Get page title
  const pageTitle = document.querySelector('title').innerText

  // Change page title on blur
  window.addEventListener('blur', (event) => {
    document.querySelector('title').innerText = ' ¡Hey! ¡No olvides la ficha!'
  })

  // Change page title back on focus
  window.addEventListener('focus', (event) => {
    document.querySelector('title').innerText = pageTitle
  })
}

// configuracon de compoenents
function configuracionComponentes() {

  // datos personales
  $('#cboDepartamentoNacimiento').prop('disabled', true);
  $('#cboProvinciaNacimiento').prop('disabled', true);
  $('#cboDistritoNacimiento').prop('disabled', true);
  $('#cboProvinciaResidencia').prop('disabled', true);
  $('#cboDistritoResidencia').prop('disabled', true);
  $('#cboDepartamentoNacimiento').selectpicker('refresh');
  $('#cboProvinciaNacimiento').selectpicker('refresh');
  $('#cboDistritoNacimiento').selectpicker('refresh');
  $('#cboProvinciaResidencia').selectpicker('refresh');
  $('#cboDistritoResidencia').selectpicker('refresh');

  //datos familiares
  $('#txtNumeroDocumentoFamiliar').prop('disabled', true);

  // estudios academicos
  $('#cboEstadoEstudioFormacionAcademica').prop('disabled', true);
  $('#cboEstadoEstudioFormacionAcademica').selectpicker('refresh');

  // fondo de pension

  soloNumeros('txtNumeroRUC');
  soloLetras('txtApellidoPaterno');
  soloLetras('txtApellidoMaterno');
  soloLetras('txtNombre');
  soloNumeros('txtTelefonoFijo');
  soloNumeros('txtTelefonoMovil');
  soloLetras('txtApellidoPaternoFamiliar');
  soloLetras('txtApellidoMaternoFamiliar');
  soloLetras('txtNombreFamiliar');
  soloNumeros('txtTelefonoFamiliar');
  $("#txtCentroEstudiosFormacionAcademica").validCampo('abcdefghijklmnñopqrstuvwxyzáéíóú 1234567890');
  soloLetras('txtCargoExperienciaLaboral');
  soloNumeros('txtTelefonoExperienciaLaboral');


  configuracionComboNacionalidad();
  configuracionCombosUbigeoNacimiento();
  configuracionCombosUbigeoResidencia();

  configuracionComboTipoDocumentoFamiliar();
  configuracionComboParentesco();

  configuracionComboNivelEstadoEstudio();

  $(".tp-background-cover").find("input").addClass('text-uppercase');

}

let jsonObjFamiliar = [];
let jsonObjExperienciaLaboral = [];
let jsonObjFormacionAcademica = [];

let codigoUbigeoNacimiento = 0;
let codigoUbigeoResidencia = 0;

let flagTieneExperienciaLaboral = false;
let flagTieneFondoPension = true;

let idFamiliar = "";
let idExperienciaLaboral = "";
let idFormacionAcademica = "";
function validarRegistroFicha() {
  $('#btnRegistrarFicha').on('click', function (e) {
    if (validacionFormulariosRegistroFicha()) {
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
              let json = registrarFicha();
              let edad = calculateAge(toDate(json.fechaNacimiento), new Date());
              if (edad >= 18) {
                if (json.latitudResidencia !== '' || json.longitudResidencia !== '') {

                  if ($('div.fileinput-preview').children('img').attr('src') === '') {
                    json.foto = $('div.fileinput-preview').children('img').attr('src', 'data:image/png;base64,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');
                    json.extension = '.png';
                  } else {
                    json.foto = $('div.fileinput-preview').children('img').attr('src');
                    // get extension photo
                    let lblFoto = $('#lblFoto').val().split('\\');
                    let imgExtension = lblFoto[lblFoto.length - 1].substring(lblFoto[lblFoto.length - 1].indexOf('.'), lblFoto[lblFoto.length - 1].length);
                    json.extension = imgExtension;
                  }

                  if (!statusFoto) {
                    warningMessage('Por favor debe cargar una foto');
                  } else {
                    $.ajax({
                      type: 'POST',
                      url: "../FichaServlet",
                      dataType: 'json',
                      data: {
                        accion: 'registrarFicha',
                        json: JSON.stringify(json)
                      },
                      beforeSend: function (xhr) {
                        load('Validando datos ingresados');
                      },
                      success: function (data, textStatus, jqXHR) {
                        if (textStatus === 'success') {
                          localStorage.clear();
                          $(location).attr('href', data.page);
                        }
                      }
                    });
                  }
                } else {
                  warningMessage(`Por favor ubique en el mapa su lugar de residencia (dirección referencial) <br/> <span class="text-muted">arrastrar el marcador o dar clic sobre el mapa</span>`);
                  smoothScrolling('#mapResidencia');
                }
              } else {
                warningMessage(`Verifique la edad ingresada`);
                smoothScrolling('#formDatosPersonales');
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

}

/**
 * validar foto
 * que solo se permite imagenes
 * png jpg jpeg entre otros
 */
let statusFoto = false;
function validarFoto() {
  $('#lblFoto').on('change.bs.fileinput', (event) => {
    let imgName = $('#lblFoto').val().split('\\');
    let imgExtension = imgName[imgName.length - 1].substring(imgName[imgName.length - 1].indexOf('.'), imgName[imgName.length - 1].length);
    if (imgExtension === '.PNG' || imgExtension === '.png' ||
      imgExtension === '.JPG' || imgExtension === '.jpg' ||
      imgExtension === '.JPEG' || imgExtension === '.jpeg') {
      statusFoto = true;
    } else {
      statusFoto = false;
    }
  });
}

function validacionFormulariosRegistroFicha() {
  if (!validarFormDatosPersonales()) {
    warningMessage("Ingrese sus datos personales completos, por favor.", smoothScrolling('#formDatosPersonales'));
    return false;
  }

  if (jsonObjFamiliar.length === 0) {
    warningMessage("Ingrese al menos un familiar, por favor.", smoothScrolling('#formDatosFamiliares'));
    return false;
  }

  if (jsonObjFormacionAcademica.length === 0) {
    warningMessage("Ingrese al menos una formación académica, por favor.", smoothScrolling('#formFormacionAcademica'));
    return false;
  }

  if (flagTieneExperienciaLaboral) {
    if (jsonObjExperienciaLaboral.length === 0) {
      warningMessage("Ingrese al menos una experiencia laboral, por favor.", smoothScrolling('#formExperienciaLaboral'));
      return false;
    }
  }

  if (!validarFormFondoPension()) {
    warningMessage("Escoge tu fondo de pensión, por favor.", smoothScrolling('#formFondoPension'));
    return false;
  }

  return true;

}

function registrarFicha() {
  let codigoFondoPension = parseInt($('input[name="rdFondoPension"]:checked', '#formFondoPension').val());
  let codigoTipoDocumento = parseInt($('#codigoTipoDocumento').val());
  let json = obtenerDatosPersonales(codigoTipoDocumento);
  json.codigoFondoPension = codigoFondoPension;
  json.experienciaLaboralActivo = flagTieneExperienciaLaboral;
  json.cargaFamiliar = jsonObjFamiliar;
  json.experienciaLaboral = jsonObjExperienciaLaboral;
  json.formacionAcademica = jsonObjFormacionAcademica;
  json.cargaFamiliarTotal = jsonObjFamiliar.length;
  json.experienciaLaboralTotal = jsonObjExperienciaLaboral.length;
  json.formacionAcademicaTotal = jsonObjFormacionAcademica.length;

  return json;
}

///////////////////////////////////////////////////////////////////////////
///////////////////////DATOS FAMILIARES ACCIONES///////////////////////////
///////////////////////////////////////////////////////////////////////////
function initDatosPersonales() {
  llenarDatosPersonalesLocalStorage();
  setTimeout(() => {
    obtenerDatosPersonalesLocalStorage();
  }, 0);
}

function obtenerDatosPersonales(codigoTipoDocumento) {
  let codigoNacionalidad = parseInt($('#cboNacionalidad').val());

  let json = {
    sexo: $('#cboSexo').val(),
    codigoEstadoCivil: $('#cboEstadoCivil').val(),
    fechaNacimiento: $('#dpFechaNacimiento').val(),
    codigoNacionalidad: $('#cboNacionalidad').val(),
    direccionDocumento: $('#txtDireccionDocumento').val().trim(),
    telefonoFijo: $('#txtTelefonoFijo').val().trim(),
    telefonoMovil: $('#txtTelefonoMovil').val().trim(),
    correo: $('#txtCorreoElectronico').val().trim(),
    codigoUbigeoResidencia: codigoUbigeoResidencia,
    direccionResidencia: $('#txtDireccionResidencia').val().trim(),
    latitudResidencia: $('#latitudResidencia').val(),
    longitudResidencia: $('#longitudResidencia').val(),
    fondoPensionActivo: flagTieneFondoPension
  };



  if (codigoNacionalidad === 144) {
    json.codigoUbigeoNacimiento = codigoUbigeoNacimiento;
  }

  if (codigoTipoDocumento === 1) {
    json.ruc = $('#txtNumeroRUC').val().trim();
  } else if (codigoTipoDocumento === 3) {
    json.apellidoPaterno = $('#txtApellidoPaterno').val().trim();
    json.apellidoMaterno = $('#txtApellidoMaterno').val().trim();
    json.nombre = $('#txtNombre').val().trim();
  } else {
    json.ruc = $('#txtNumeroRUC').val().trim();
    json.apellidoPaterno = $('#txtApellidoPaterno').val().trim();
    json.apellidoMaterno = $('#txtApellidoMaterno').val().trim();
    json.nombre = $('#txtNombre').val().trim();
  }

  return json;

}

// obtiene la informacion del localStorage y llena los inputs
function obtenerDatosPersonalesLocalStorage() {
  if (localStorage.getItem('jsonFormDatosPersonales')) {
    let jsonFormDatosPersonales = JSON.parse(localStorage.getItem('jsonFormDatosPersonales'));
    $('#txtNumeroRUC').val(jsonFormDatosPersonales.ruc);
    $('#cboSexo').val(jsonFormDatosPersonales.sexo);
    $('#cboEstadoCivil').val(jsonFormDatosPersonales.codigoEstadoCivil);
    $('#dpFechaNacimiento').val(jsonFormDatosPersonales.fechaNacimiento);
//        $('#cboNacionalidad').val(jsonFormDatosPersonales.codigoNacionalidad);
//        $('#cboDepartamentoNacimiento').val(jsonFormDatosPersonales.codigoDepartamentoNacimiento);
//        $('#cboProvinciaNacimiento').val(jsonFormDatosPersonales.codigoProvinciaNacimiento);
//        $('#cboDistritoNacimiento').val(jsonFormDatosPersonales.codigoDistritoNacimiento);

    $('#txtDireccionDocumento').val(jsonFormDatosPersonales.direccionDocumento);
    $('#txtTelefonoFijo').val(jsonFormDatosPersonales.telefonoFijo);
    $('#txtTelefonoMovil').val(jsonFormDatosPersonales.telefonoMovil);
//        $('#txtCorreoElectronico').val(jsonFormDatosPersonales.correo);
//        $('#cboDepartamentoResidencia').val(jsonFormDatosPersonales.codigoDepartamentoResidencia);
//        $('#cboProvinciaResidencia').val(jsonFormDatosPersonales.codigoProvinciaResidencia);
//        $('#cboDistritoResidencia').val(jsonFormDatosPersonales.codigoDistritoResidencia);

    $('#txtDireccionResidencia').val(jsonFormDatosPersonales.direccionResidencia);
    $('#latitudResidencia').val(jsonFormDatosPersonales.latitudResidencia);
    $('#longitudResidencia').val(jsonFormDatosPersonales.longitudResidencia);

    $('#cboSexo').selectpicker('refresh');
    $('#cboEstadoCivil').selectpicker('refresh');
    $('#cboNacionalidad').selectpicker('refresh');
    $('#cboDepartamentoNacimiento').selectpicker('refresh');
    $('#cboProvinciaNacimiento').selectpicker('refresh');
    $('#cboDistritoNacimiento').selectpicker('refresh');
    $('#cboDepartamentoResidencia').selectpicker('refresh');
    $('#cboProvinciaResidencia').selectpicker('refresh');
    $('#cboDistritoResidencia').selectpicker('refresh');
  }
}

// llena los ampos del formulario de datos personales al LocalStorage
function llenarDatosPersonalesLocalStorage() {
  $('#formDatosPersonales').on('change', function (e) {
    let jsonFormDatosPersonales = {
      ruc: $('#txtNumeroRUC').val().trim(),
      sexo: $('#cboSexo').val(),
      codigoEstadoCivil: $('#cboEstadoCivil').val(),
      fechaNacimiento: $('#dpFechaNacimiento').val().trim(),
      codigoNacionalidad: $('#cboNacionalidad').val(),
      codigoDepartamentoNacimiento: $('#cboDepartamentoNacimiento').val(),
      codigoProvinciaNacimiento: $('#cboProvinciaNacimiento').val(),
      codigoDistritoNacimiento: $('#cboDistritoNacimiento').val(),
      direccionDocumento: $('#txtDireccionDocumento').val().trim(),
      telefonoFijo: $('#txtTelefonoFijo').val().trim(),
      telefonoMovil: $('#txtTelefonoMovil').val().trim(),
      correo: $('#txtCorreoElectronico').val().trim(),
      codigoDepartamentoResidencia: $('#cboDepartamentoResidencia').val(),
      codigoProvinciaResidencia: $('#cboProvinciaResidencia').val(),
      codigoDistritoResidencia: $('#cboDistritoResidencia').val(),
      direccionResidencia: $('#txtDireccionResidencia').val().trim(),
      latitudResidencia: $('#latitudResidencia').val(),
      longitudResidencia: $('#longitudResidencia').val()
    };

    localStorage.setItem('jsonFormDatosPersonales', JSON.stringify(jsonFormDatosPersonales));
  });
}


///////////////////////////////////////////////////////////////////////////
///////////////////////DATOS FAMILIARES ACCIONES///////////////////////////
///////////////////////////////////////////////////////////////////////////


///////////////////////////////////////////////////////////////////////////
////////////////////////TABLA FAMILIAR ACCIONES////////////////////////////
///////////////////////////////////////////////////////////////////////////

function initCargaFamiliar() {
  agregarFamiliar();
  eliminarFamiliar();
  listadoFamiliar();
  setTimeout(function () {
    llenarTblFamiliarLocalStorage();
  }, 0);
}


// accion para agregar familiar
function agregarFamiliar() {
  $('#btnAgregarFamiliar').on('click', function (e) {
    if (validarFormDatosFamiliares()) {
      $.confirm({
        icon: 'glyphicon glyphicon-question-sign',
        theme: 'material',
        closeIcon: true,
        animation: 'scale',
        type: 'dark',
        title: 'Confirmar',
        content: '<span class="text-semibold">¿Desea agregar un nuevo familiar?</span>',
        buttons: {
          Registrar: {
            btnClass: 'btn-success',
            action: function () {
              let tblFamiliar = $('#tblFamiliar').DataTable();
              obtenerDataFamiliar().then((data) => {
                console.log(data);
                if (!validarExistenciaValorDeArray(data.numeroDocumento, jsonObjFamiliar, "numeroDocumento")) {
                  if ($('#txtNumeroDocumentoFamiliar').val() !== $('#lblNumeroDocumento').text()) {
                    if (dateLessThanActualDate(data.fechaNacimiento)) {
                      jsonObjFamiliar.push(data);
                      localStorage.setItem("jsonObjFamiliar", JSON.stringify(jsonObjFamiliar));
                      tblFamiliar.row.add(data).draw();
                      if (data.codigoParentesco === 1 || data.codigoParentesco === 2) {
                        $('#cboParentescoFamiliar option[value="' + data.codigoParentesco + '"]').attr('disabled', true);
                        $('#cboParentescoFamiliar').selectpicker('refresh');
                      }
                      successMessage("Familiar agregado");
                      limpiarFormFamilia();
                    } else {
                      warningMessage(`Verifique la fecha ingresada.`);
                    }
                  } else {
                    warningMessage("No puedes registrar tu dni");
                  }
                } else {
                  warningMessage("El número de documento ingresado ya existe.");
                }
              });
            }
          },
          Cancelar: {
            btnClass: 'btn-danger'
          }
        }
      });
    }
  });
}

// Valida si la fecha es menor que la fecha actual
function dateLessThanActualDate(str, str2) {

  str2 = str2 || new Date();
  if (str2 instanceof Date) {
    let mes = str2.getMonth() + 1;
    let dia = str2.getDay();
    let anio = str2.getFullYear();
    str2 = `${dia}/${mes}/${anio}`;
  }

  if (toDate(str) > toDate(str2)) {
    return false;
  }

  return true;
}

// agregar informacion tblFamiliar localstorage
function llenarTblFamiliarLocalStorage() {

  if (localStorage.getItem('jsonObjFamiliar')) {
    let jsonFamiliar = JSON.parse(localStorage.getItem('jsonObjFamiliar'));
    let tblFamiliar = $('#tblFamiliar').DataTable();
    for (let i = 0; i < jsonFamiliar.length; i++) {
      tblFamiliar.row.add(jsonFamiliar[i]).draw();
      if (jsonFamiliar[i].codigoParentesco === 1 || jsonFamiliar[i].codigoParentesco === 2) {
        $('#cboParentescoFamiliar option[value="' + jsonFamiliar[i].codigoParentesco + '"]').attr('disabled', true);
        $('#cboParentescoFamiliar').selectpicker('refresh');
      }
    }

    jsonObjFamiliar = jsonFamiliar;
  }
}

// limpiar formulario de familair
function limpiarFormFamilia() {
  $('#txtApellidoPaternoFamiliar').val('');
  $('#txtApellidoMaternoFamiliar').val('');
  $('#txtNombreFamiliar').val('');
  $('#cboParentescoFamiliar').val(0).change();
  $('#cboParentescoFamiliar').selectpicker('refresh');
  $('#dpFechaNacimientoFamiliar').val('').change();
  $('#cbotipoDocumentoFamiliar').val(0).change();
  $('#cbotipoDocumentoFamiliar').selectpicker('refresh');
  $('#txtNumeroDocumentoFamiliar').val('');
  $('#txtNumeroDocumentoFamiliar').attr('disabled', true);
  $('#txtTelefonoFamiliar').val('');
  $('#txtApellidoPaternoFamiliar').focus();
}

//obtener data familiar
function obtenerDataFamiliar() {
  return obtenerLongitudTipoEntradaTipoDocumento(parseInt($('#cbotipoDocumentoFamiliar').val()))
    .then((data) => {
      console.log(data);
      return {
        id: guidGenerator(),
        apellidoPaterno: $('#txtApellidoPaternoFamiliar').val().trim(),
        apellidoMaterno: $('#txtApellidoMaternoFamiliar').val().trim(),
        nombre: $('#txtNombreFamiliar').val().trim(),
        codigoParentesco: parseInt($('#cboParentescoFamiliar').val()),
        nombreParentesco: $('#cboParentescoFamiliar option:selected').text(),
        fechaNacimiento: $('#dpFechaNacimientoFamiliar').val().trim(),
        codigoTipoDocumento: parseInt($('#cbotipoDocumentoFamiliar').val()),
        nombreTipoDocumento: $('#cbotipoDocumentoFamiliar option:selected').text(),
        longitud: data.data.tipodocumentos[0].longitud,
        tipoEntrada: data.data.tipodocumentos[0].tipoEntrada,
        numeroDocumento: $('#txtNumeroDocumentoFamiliar').val().trim(),
        sexo: $('#cboSexoFamiliar').val(),
        telefono: $('#txtTelefonoFamiliar').val().trim()
      };
    });

}

// eliminar fila de familiar
function eliminarFamiliar() {
  $('#tblFamiliar tbody').on('click', '.eliminarFamiliar', function () {
    let fila = $('#tblFamiliar').DataTable().row($(this).parents('tr'));
    let id = fila.data().id;
    $.confirm({
      icon: 'glyphicon glyphicon-question-sign',
      theme: 'material',
      closeIcon: true,
      animation: 'scale',
      type: 'dark',
      title: 'Confirmar',
      content: '<span class="text-semibold">¿Desea eliminar familia?</span>',
      buttons: {
        Eliminar: {
          btnClass: 'btn-success',
          action: function () {
            let objJsonFamiliar = removeByKey(jsonObjFamiliar, {key: 'id', value: id});
            $('#cboParentescoFamiliar option[value="' + fila.data().codigoParentesco + '"]').attr('disabled', false);
            $('#cboParentescoFamiliar').selectpicker('refresh');
            if (objJsonFamiliar.length === 0) {
              localStorage.removeItem('jsonObjFamiliar');
            } else {
              localStorage.setItem("jsonObjFamiliar", JSON.stringify(objJsonFamiliar));
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
  });
}

// listado de familiar
function listadoFamiliar() {
  defaultConfigDataTable();
  let numeroFilas = 0;
  $('#tblFamiliar').DataTable({
    bInfo: false,
    bPaginate: false,
    columns: [
      {
        data: null,
        render: function (data, type, row) {
          return ++numeroFilas;
        }
      },
      {
        data: null,
        render: function (data, type, row) {
          return `${data.apellidoPaterno} ${data.apellidoMaterno} ${data.nombre}`;
        }
      },
      {data: "nombreParentesco"},
      {data: "fechaNacimiento"},
      {data: "nombreTipoDocumento"},
      {data: "numeroDocumento"},
      {
        data: "sexo",
        render: function (data, type, row) {
          return data === 'M' ? 'MASCULINO' : 'FEMENINO';
        }
      },
      {
        data: "telefono",
        render: function (data, type, row) {
          return data === '' ? '-' : data;
        }
      },
      {
        data: null,
        className: 'text-center',
        render: function (data, type, row) {
          return `<ul class="icons-list">
                                
                                <li title="Eliminar" class="text-danger-600">
                                    <a href="#" onclick="return false" class="eliminarFamiliar"><i class="fa fa-trash-o fa-lg"></i></a>
                                </li>
                            </ul>`;
        }
      }
    ],
    createdRow: function (row, data, index) {
      $(this).addClass('text-uppercase');
    }

  });
}

///////////////////////////////////////////////////////////////////////////
////////////////////////TABLA FAMILIAR ACCIONES////////////////////////////
///////////////////////////////////////////////////////////////////////////


///////////////////////////////////////////////////////////////////////////
//////////////////TABLA EXPERIENCIA LABORAL ACCIONES///////////////////////
///////////////////////////////////////////////////////////////////////////

function initExperienciaLaboral() {
  deshabilitarCamposExperienciaLaboral();
  configuracionChkExperienciaLaboral();
  listarExperienciaLaboral();
  agregarExperienciaLaboral();
  setTimeout(() => {
    llenarTblExperienciaLaboral();
  }, 0);
  eliminarExperienciaLaboral();
}



// agregar experiencia laboral
function agregarExperienciaLaboral() {
  $('#btnAgregarExperienciaLaboral').on('click', function (e) {
    if (validarFormExperienciaLaboral()) {
      $.confirm({
        icon: 'glyphicon glyphicon-question-sign',
        theme: 'material',
        closeIcon: true,
        animation: 'scale',
        type: 'dark',
        title: 'Confirmar',
        content: '<span class="text-semibold">¿Desea agregar experiencia laboral?</span>',
        buttons: {
          Agregar: {
            btnClass: 'btn-success',
            action: function () {
              let tblExperienciaLaboral = $('#tblExperienciaLaboral').DataTable();
              let json = obtenerDataExperienciaLaboral();
              if (toDate(json.fechaFin) > toDate(json.fechaInicio)) {
                jsonObjExperienciaLaboral.push(json);
                localStorage.setItem('jsonObjExperienciaLaboral', JSON.stringify(jsonObjExperienciaLaboral));
                tblExperienciaLaboral.row.add(json).draw();
                limpiarFormExperienciaLaboral();
                successMessage("Experiencia laboral agregada");
              } else {
                warningMessage("La fecha de fin es menor a la del comienzo. Verifique las fechas ingresadas");
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
}

// eliminar registro de familia
function eliminarExperienciaLaboral() {
  $('#tblExperienciaLaboral tbody').on('click', '.eliminarExperienciaLaboral', function () {
    let fila = $('#tblExperienciaLaboral').DataTable().row($(this).parents('tr'));
    let id = fila.data().id;

    $.confirm({
      icon: 'glyphicon glyphicon-question-sign',
      theme: 'material',
      closeIcon: true,
      animation: 'scale',
      type: 'dark',
      title: 'Confirmar',
      content: '<span class="text-semibold">¿Desea eliminar experiencia laboral?</span>',
      buttons: {
        Eliminar: {
          btnClass: 'btn-success',
          action: function () {
            let objJsonExperienciaLaboral = removeByKey(jsonObjExperienciaLaboral, {key: 'id', value: id});
            if (objJsonExperienciaLaboral.length === 0) {
              localStorage.removeItem("jsonObjExperienciaLaboral");
            } else {
              localStorage.setItem("jsonObjExperienciaLaboral", JSON.stringify(objJsonExperienciaLaboral));
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
  });
}

// llenar tabla experiencia laboral del localStorage
function llenarTblExperienciaLaboral() {
  if (localStorage.getItem('jsonObjExperienciaLaboral')) {
    let jsonExperienciaLaboral = JSON.parse(localStorage.getItem('jsonObjExperienciaLaboral'));
    let jsonExperienciaLaboralLength = jsonExperienciaLaboral.length;
    if (jsonExperienciaLaboralLength === 0) {
      deshabilitarCamposExperienciaLaboral();
      configChkExperienciaLaboral()
    } else {
      habilitarCamposExperienciaLaboral();
      flagTieneExperienciaLaboral = true;
      $('.tp-info-experiencia-laboral').text('¿Tiene experiencia laboral?');
      $('#chkExperienciaLaboral').prop('checked', true);
      $('.div-chk-experiencia-laboral .off').removeClass('off').removeClass('btn-default').addClass('btn-success')
      configChkExperienciaLaboral()

    }
    let tblExperienciaLaboral = $('#tblExperienciaLaboral').DataTable();
    for (let i = 0; i < jsonExperienciaLaboralLength; i++) {
      tblExperienciaLaboral.row.add(jsonExperienciaLaboral[i]).draw();
    }
    jsonObjExperienciaLaboral = jsonExperienciaLaboral;
  } else {
    configChkExperienciaLaboral()
  }
}

// obtiene los valores para agregar una fila de experiencia laboral
function obtenerDataExperienciaLaboral() {
  return {
    id: guidGenerator(),
    empresa: $('#txtEmpresaExperienciaLaboral').val().trim(),
    cargo: $('#txtCargoExperienciaLaboral').val().trim(),
    fechaInicio: $('#dpFechaInicioExperienciaLaboral').val().trim(),
    fechaFin: $('#dpFechaFinExperienciaLaboral').val().trim(),
    telefono: $('#txtTelefonoExperienciaLaboral').val().trim()
  };
}

// listadoExperienciaLaboral
function listarExperienciaLaboral() {
  defaultConfigDataTable();
  $('#tblExperienciaLaboral').DataTable({
    bInfo: false,
    bPaginate: false,
    columns: [
      {data: "empresa"},
      {data: "cargo"},
      {data: "fechaInicio"},
      {data: "fechaFin"},
      {
        data: "telefono",
        render: function (data, type, row) {
          return data === '' ? ' - ' : data;
        }
      },
      {
        data: null,
        className: 'text-center',
        render: function (data, type, row) {
          return `<ul class="icons-list">
                                
                                <li title="Eliminar" class="text-danger-600">
                                    <a href="#" onclick="return false" class="eliminarExperienciaLaboral"><i class="fa fa-trash-o fa-lg"></i></a>
                                </li>
                            </ul>`;
        }
      }
    ],
    createdRow: function (row, data, index) {
      $(this).addClass('text-uppercase');
    }

  });
}

// limpia los inputs despues de agregar
function limpiarFormExperienciaLaboral() {
  $('#txtEmpresaExperienciaLaboral').val('');
  $('#txtCargoExperienciaLaboral').val('');
  $('#dpFechaInicioExperienciaLaboral').val('').change();
  $('#dpFechaFinExperienciaLaboral').val('').change();
  $('#txtTelefonoExperienciaLaboral').val('');
}

// deshabilita todos los campos de epxeriencia laboral
function deshabilitarCamposExperienciaLaboral() {
  $('#divExperienciaLaboral').find('.text-danger').css('display', 'none');
  $('#divExperienciaLaboral').find('.tp-text').addClass('text-muted');
  $('#txtEmpresaExperienciaLaboral').attr('disabled', true);
  $('#txtCargoExperienciaLaboral').attr('disabled', true);
  $('#txtCargoExperienciaLaboral').attr('disabled', true);
  $('#dpFechaInicioExperienciaLaboral').attr('disabled', true);
  $('#dpFechaFinExperienciaLaboral').attr('disabled', true);
  $('#txtTelefonoExperienciaLaboral').attr('disabled', true);
  $('#btnAgregarExperienciaLaboral').attr('disabled', true);
  $('#divExperienciaLaboral').find('th').addClass('text-muted');
//  $('.tp-info-experiencia-laboral').text('TENGO EXPERIENCIA LABORAL');

}

// habilita todos los campos de experiencia laboral
function habilitarCamposExperienciaLaboral() {
  $('#divExperienciaLaboral').find('.text-danger').css('display', 'inline-block');
  $('#divExperienciaLaboral').find('.tp-text').removeClass('text-muted');
  $('#txtEmpresaExperienciaLaboral').attr('disabled', false);
  $('#txtCargoExperienciaLaboral').attr('disabled', false);
  $('#txtCargoExperienciaLaboral').attr('disabled', false);
  $('#dpFechaInicioExperienciaLaboral').attr('disabled', false);
  $('#dpFechaFinExperienciaLaboral').attr('disabled', false);
  $('#txtTelefonoExperienciaLaboral').attr('disabled', false);
  $('#btnAgregarExperienciaLaboral').attr('disabled', false);
  $('#divExperienciaLaboral').find('th').removeClass('text-muted');
//  $('.tp-info-experiencia-laboral').text('NO TENGO EXPERIENCIA LABORAL');
}


// configuracion para habilitar/deshabilitar los campos de experiencia laboral
function configuracionChkExperienciaLaboral() {
  let chkExperienciaLaboral = document.querySelector('#chkExperienciaLaboral');
  $('#chkExperienciaLaboral').on('change', function (e) {
    if (chkExperienciaLaboral.checked) {
      habilitarCamposExperienciaLaboral();
      flagTieneExperienciaLaboral = true;
    } else {
      deshabilitarCamposExperienciaLaboral();
      $('#divExperienciaLaboral').find('.form-group').removeClass('has-error');
      $('.help-block').css('display', 'none');
      flagTieneExperienciaLaboral = false;
    }
    console.log(flagTieneExperienciaLaboral);
  });
}

///////////////////////////////////////////////////////////////////////////
//////////////////TABLA EXPERIENCIA LABORAL ACCIONES///////////////////////
///////////////////////////////////////////////////////////////////////////



///////////////////////////////////////////////////////////////////////////
//////////////////TABLA FORMACION ACADEMICA ACCIONES///////////////////////
///////////////////////////////////////////////////////////////////////////

// inicializa funciones de formacion academica 
function initFormacionAcademica() {
  agregarFormacionAcademica();
  listadoFormacionAcademica();
  eliminarFormacionAcademica();
  setTimeout(() => {
    llenarTblFormacionAcademica();
  }, 0);
}


//accion para agregar formacion academica
function agregarFormacionAcademica() {
  $('#btnAgregarFormacionAcademica').on('click', function (e) {
    if (validarFormFormacionAcademica()) {
      $.confirm({
        icon: 'glyphicon glyphicon-question-sign',
        theme: 'material',
        closeIcon: true,
        animation: 'scale',
        type: 'dark',
        title: 'Confirmar',
        content: '<span class="text-semibold">¿Desea agregar una nueva formación académica?</span>',
        buttons: {
          Registrar: {
            btnClass: 'btn-success',
            action: function () {
              let tblFormacionAcademica = $('#tblFormacionAcademica').DataTable();
              let obtenerData = obtenerFormacionAcademica();
              obtenerData.then((data) => {
                if (toDate(data.fechaFin) > toDate(data.fechaInicio)) {
                  jsonObjFormacionAcademica.push(data);
                  localStorage.setItem('jsonObjFormacionAcademica', JSON.stringify(jsonObjFormacionAcademica));
                  tblFormacionAcademica.row.add(data).draw();
                  successMessage("Formación académica agregada correctamente.");
                  limpiarFormFormacionAcademica();
                } else {
                  warningMessage("La fecha de fin es menor a la del comienzo. Verifique las fechas ingresadas");
                }
              });
            }
          },
          Cancelar: {
            btnClass: 'btn-danger'
          }
        }
      });

    } else {
      $('.tp-error-move-to-last').append($('#cboCarreraProfesionalFormacionAcademica-error'));
    }
  });
}

// eliminar formacion academica
function eliminarFormacionAcademica() {
  $('#tblFormacionAcademica tbody').on('click', '.eliminarFormacionAcademica', function () {
    let fila = $('#tblFormacionAcademica').DataTable().row($(this).parents('tr'));
    let id = fila.data().id;

    $.confirm({
      icon: 'glyphicon glyphicon-question-sign',
      theme: 'material',
      closeIcon: true,
      animation: 'scale',
      type: 'dark',
      title: 'Confirmar',
      content: '<span class="text-semibold">¿Desea eliminar formación académica?</span>',
      buttons: {
        Eliminar: {
          btnClass: 'btn-success',
          action: function () {
            let objJsonFormacionAcademica = removeByKey(jsonObjFormacionAcademica, {key: 'id', value: id});
            if (objJsonFormacionAcademica.length === 0) {
              localStorage.removeItem("jsonObjFormacionAcademica");
            } else {
              localStorage.setItem("jsonObjFormacionAcademica", JSON.stringify(objJsonFormacionAcademica));
            }
            fila.remove().draw();
            successMessage("Formación académica eliminada.");
          }
        },
        Cancelar: {
          btnClass: 'btn-danger'
        }
      }
    });
  });
}

// llenar tabla formacion academica del localStorage
function llenarTblFormacionAcademica() {
  if (localStorage.getItem('jsonObjFormacionAcademica')) {
    let jsonFormacionAcademica = JSON.parse(localStorage.getItem('jsonObjFormacionAcademica'));
    let tblFormacionAcademica = $('#tblFormacionAcademica').DataTable();
    for (let i = 0; i < jsonFormacionAcademica.length; i++) {
      tblFormacionAcademica.row.add(jsonFormacionAcademica[i]).draw();
    }
    jsonObjFormacionAcademica = jsonFormacionAcademica;
  }
}

// obtener data formacion academica
function obtenerFormacionAcademica() {
  let codigoNivelEstudio = parseInt($('#cboGradoEstudioFormacionAcademica').val());
  let codigoEstadoEstudio = parseInt($('#cboEstadoEstudioFormacionAcademica').val());

  let json = {
    id: guidGenerator(),
    nombreGrado: $('#cboGradoEstudioFormacionAcademica option:selected').text(),
    nombreEstado: $('#cboEstadoEstudioFormacionAcademica option:selected').text(),
    centroEstudios: $('#txtCentroEstudiosFormacionAcademica').val().trim(),
    codigoCarreraProfesional: parseInt($('#cboCarreraProfesionalFormacionAcademica').val()),
    nombreCarreraProfesional: $('#select2-cboCarreraProfesionalFormacionAcademica-container').text().slice(1),
    fechaInicio: $('#dpFechaInicioFormacionAcademica').val().trim(),
    fechaFin: $('#dpFechaFinFormacionAcademica').val().trim()
  };

  if (codigoNivelEstudio === 1 || codigoNivelEstudio === 2) {
    json.codigoCarreraProfesional = 0;
    json.nombreCarreraProfesional = '';
  }

  return obtenerNivelEstado(codigoNivelEstudio, codigoEstadoEstudio)
    .then((data) => {
      if (data.status) {
        return data.data.getResultedKey;
      }
    })
    .then((codigoNivelEstado) => {
      json.codigoNivelEstado = codigoNivelEstado;
      return json;
    });

}

// limpiar campos de formacion academica
function limpiarFormFormacionAcademica() {
  $('#cboGradoEstudioFormacionAcademica').val(0).change();
  $('#cboGradoEstudioFormacionAcademica').selectpicker('refresh');
  $('#cboEstadoEstudioFormacionAcademica').val(0).change();
  $('#cboEstadoEstudioFormacionAcademica').selectpicker('refresh');
  $('#txtCentroEstudiosFormacionAcademica').val('');
//    $('#cboCarreraProfesionalFormacionAcademica').val(0).change();
  $('#cboCarreraProfesionalFormacionAcademica').attr('disabled', true);
  $('#select2-cboCarreraProfesionalFormacionAcademica-container').text('');
  $('#dpFechaInicioFormacionAcademica').val('');
  $('#dpFechaFinFormacionAcademica').val('');
}

// listado para la tabla formaicon academica
function listadoFormacionAcademica() {
  defaultConfigDataTable();
  $('#tblFormacionAcademica').DataTable({
    bInfo: false,
    bPaginate: false,
    columns: [
      {data: "nombreGrado"},
      {data: "nombreEstado"},
      {data: "centroEstudios"},
      {
        data: "nombreCarreraProfesional",
        render: function (data, type, row) {
          return data === '' ? " - " : data;

        }
      },
      {data: "fechaInicio"},
      {data: "fechaFin"},
      {
        data: null,
        className: 'text-center',
        render: function (data, type, row) {
          return `<ul class="icons-list">
                                
                                <li title="Eliminar" class="text-danger-600">
                                    <a href="#" onclick="return false" class="eliminarFormacionAcademica"><i class="fa fa-trash-o fa-lg"></i></a>
                                </li>
                            </ul>`;
        }
      }

    ],
    createdRow: function (row, data, index) {
      $(this).addClass('text-uppercase');
    }
  });
}


// obtener el grado(nivel) y estado
function obtenerNivelEstado(codigoNivelEstudio, codigoEstadoEstudio) {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: '../NivelEstadoServlet?accion=obtenerNivelEstado',
      type: 'POST',
      dataType: 'json',
      data: {codigoNivelEstudio: codigoNivelEstudio, codigoEstadoEstudio: codigoEstadoEstudio},
      beforeSend: function (xhr) {
        load("Obteniendo el nivel y estado");
      }, success: function (data, textStatus, jqXHR) {
        unload();
        resolve(data);
      }, error: function (jqXHR, textStatus, errorThrown) {
        reject("Error al obtener el nivel y estado");
      }
    });
  });
}

///////////////////////////////////////////////////////////////////////////
//////////////////TABLA formacion academica ACCIONES///////////////////////
///////////////////////////////////////////////////////////////////////////




// validacion formulario datos personales
function validarFormDatosPersonales() {
  let formDatosPersonales = $('#formDatosPersonales');
  formDatosPersonales.validate({
    rules: {
      txtNumeroRUC: {
        number: true,
        minlength: 11,
        maxlength: 11
      },
      txtApellidoPaterno: {
        required: true,
        lettersonly: true
      },
      txtApellidoMaterno: {
        required: true,
        lettersonly: true
      },
      txtNombre: {
        required: true,
        lettersonly: true
      },
      cboSexo: {
        valueNotEquals: "0"
      },
      cboEstadoCivil: {
        valueNotEquals: "0"
      },
      dpFechaNacimiento: {
        required: true,
        dateonly: true
      },
      cboNacionalidad: {
        valueNotEquals: "0"
      },
      cboDepartamentoNacimiento: {
        valueNotEquals: "0"
      },
      cboProvinciaNacimiento: {
        valueNotEquals: "0"
      },
      cboDistritoNacimiento: {
        valueNotEquals: "0"
      },
      txtDireccionDocumento: {
        required: true
      },
      txtTelefonoFijo: {
        number: true
      },
      txtTelefonoMovil: {
        number: true
      },
      txtCorreoElectronico: {
        required: true,
        email: true
      },
      cboDepartamentoResidencia: {
        valueNotEquals: "0"
      },
      cboProvinciaResidencia: {
        valueNotEquals: "0"
      },
      cboDistritoResidencia: {
        valueNotEquals: "0"
      },
      txtDireccionResidencia: {
        required: true
      },
      flImage: {
        required: true,
        accept: "image/png, image/jpeg, image/jpg"
      }
    },
    messages: {
      txtNumeroRUC: {
        number: "Sólo se admite números",
        minlength: "El campo debe tener 11 dígitos",
        maxlength: "El campo debe tener 11 dígitos"
      },
      txtApellidoPaterno: {
        required: "Ingrese su apellido paterno",
        lettersonly: "Sólo se admite letras"
      },
      txtApellidoMaterno: {
        required: "Ingrese su apellido materno",
        lettersonly: "Sólo se admite letras"
      },
      txtNombre: {
        required: "Ingrese su nombre",
        lettersonly: "Sólo se admite letras"
      },
      cboSexo: {
        valueNotEquals: "Seleccione un sexo"
      },
      cboEstadoCivil: {
        valueNotEquals: "Seleccione un estado civil"
      },
      dpFechaNacimiento: {
        required: "Ingrese una fecha de nacimiento",
        dateonly: "Ingrese una fecha válida"
      },
      cboNacionalidad: {
        valueNotEquals: "Seleccione un país"
      },
      cboDepartamentoNacimiento: {
        valueNotEquals: "Seleccione un departamento"
      },
      cboProvinciaNacimiento: {
        valueNotEquals: "Seleccione una provincia"
      },
      cboDistritoNacimiento: {
        valueNotEquals: "Seleccione un distrito"
      },
      txtDireccionDocumento: {
        required: "Ingrese una dirección"
      },
      txtTelefonoFijo: {
        number: "Sólo se admite números"
      },
      txtTelefonoMovil: {
        number: "Sólo se admite números"
      },
      txtCorreoElectronico: {
        required: "Ingresé un correo electrónico",
        email: "Ingrese un correo válido"
      },
      cboDepartamentoResidencia: {
        valueNotEquals: "Seleccione un departamento"
      },
      cboProvinciaResidencia: {
        valueNotEquals: "Seleccione una provincia"
      },
      cboDistritoResidencia: {
        valueNotEquals: "Seleccione un distrito"
      },
      txtDireccionResidencia: {
        required: "Ingrese su dirección"
      },
      flImage: {
        required: 'Debe ingresar una fotografía de usted (solo rostro)',
        accept: "Solo se acemptar los siguientes formatos (png, jpg, jpeg)"
      }
    }
  });
  return formDatosPersonales.valid();
}

// validacion formulario datos familiares
function validarFormDatosFamiliares() {
  let formDatosFamiliares = $('#formDatosFamiliares');
  formDatosFamiliares.validate({
    rules: {
      txtApellidoPaternoFamiliar: {
        required: true,
        lettersonly: true
      },
      txtApellidoMaternoFamiliar: {
        required: true,
        lettersonly: true
      },
      txtNombreFamiliar: {
        required: true,
        lettersonly: true
      },
      cboParentescoFamiliar: {
        valueNotEquals: "0"
      },
      dpFechaNacimientoFamiliar: {
        required: true,
        dateonly: true
      },
      cbotipoDocumentoFamiliar: {
        valueNotEquals: "0"
      },
      cboSexoFamiliar: {
        valueNotEquals: "0"
      },
      txtNumeroDocumentoFamiliar: {
        required: true
      },
      txtTelefonoFamiliar: {
        number: true
      }
    },
    messages: {
      txtApellidoPaternoFamiliar: {
        required: "Ingrese su apellido paterno",
        lettersonly: "Sólo se admite letras"
      },
      txtApellidoMaternoFamiliar: {
        required: "Ingrese su apellido paterno",
        lettersonly: "Sólo se admite letras"
      },
      txtNombreFamiliar: {
        required: "Ingrese su nombre",
        lettersonly: "Sólo se admite letras"
      },
      cboParentescoFamiliar: {
        valueNotEquals: "Seleccione un parentesco"
      },
      dpFechaNacimientoFamiliar: {
        required: "Ingrese una fecha de nacimiento",
        dateonly: "Ingrese una fecha válida"
      },
      cbotipoDocumentoFamiliar: {
        valueNotEquals: "Seleccione un tipo de documento"
      },
      cboSexoFamiliar: {
        valueNotEquals: "Seleccione un sexo"
      },
      txtNumeroDocumentoFamiliar: {
        required: "Ingrese el número de documento",
        number: "Sólo se admite números",
        alphanumeric: "Sólo se admite dígitos alfanuméricos",
        minlength: "El campo debe tener {0} dígitos",
        maxlength: "El campo debe tener {0} dígitos"
      },
      txtTelefonoFamiliar: {
        number: "Sólo se admite números"
      }

    }
  });
  return formDatosFamiliares.valid();
}

// validacion formulario datos academicos
function validarFormFormacionAcademica() {
  let formFormacionAcademica = $('#formFormacionAcademica');
  formFormacionAcademica.validate({
    rules: {
      cboGradoEstudioFormacionAcademica: {
        valueNotEquals: "0"
      },
      cboEstadoEstudioFormacionAcademica: {
        valueNotEquals: "0"
      },
      txtCentroEstudiosFormacionAcademica: {
        required: true
      },
      cboCarreraProfesionalFormacionAcademica: {
        valueNotEquals: "0"
      },
      dpFechaInicioFormacionAcademica: {
        required: true,
        dateonly: true
      },
      dpFechaFinFormacionAcademica: {
        required: true,
        dateonly: true
      }
    },
    messages: {
      cboGradoEstudioFormacionAcademica: {
        valueNotEquals: "Seleccione un grado"
      },
      cboEstadoEstudioFormacionAcademica: {
        valueNotEquals: "Seleccione un nivel"
      },
      txtCentroEstudiosFormacionAcademica: {
        required: "Ingrese su institución",
        lettersonly: "Sólo se admite letras"
      },
      cboCarreraProfesionalFormacionAcademica: {
        valueNotEquals: "Seleccione una carrera"
      },
      dpFechaInicioFormacionAcademica: {
        required: "Ingrese una fecha de inicio",
        dateonly: "Ingrese una fecha válida"
      },
      dpFechaFinFormacionAcademica: {
        required: "Ingrese una fecha de fin",
        dateonly: "Ingrese una fecha válida"
      }
    }
  });

  return formFormacionAcademica.valid();
}

// validacion formulario datos experiencia laboral
function validarFormExperienciaLaboral() {
  let formExperienciaLaboral = $('#formExperienciaLaboral');
  formExperienciaLaboral.validate({
    rules: {
      txtEmpresaExperienciaLaboral: {
        required: true
      },
      txtCargoExperienciaLaboral: {
        required: true,
        lettersonly: true
      },
      dpFechaInicioExperienciaLaboral: {
        required: true,
        dateonly: true
      },
      dpFechaFinExperienciaLaboral: {
        required: true,
        dateonly: true
      },
      txtTelefonoExperienciaLaboral: {
        number: true
      }
    },
    messages: {
      txtEmpresaExperienciaLaboral: {
        required: "Ingrese una empresa"
      },
      txtCargoExperienciaLaboral: {
        required: "Ingrese su cargo",
        lettersonly: "Sólo se admite letras"
      },
      dpFechaInicioExperienciaLaboral: {
        required: "Ingrese una fecha de inicio",
        dateonly: "Ingrese una fecha válida"
      },
      dpFechaFinExperienciaLaboral: {
        required: "Ingrese una fecha de fin",
        dateonly: "Ingrese una fecha válida"
      },
      txtTelefonoExperienciaLaboral: {
        number: "Sólo se admite números",
        minlength: "El campo debe tener {0} dígitos",
        maxlength: "El campo debe tener {0} dígitos"
      }
    }
  });
  return formExperienciaLaboral.valid();


}

// validacion formulario datos regimen pensionario
function validarFormFondoPension() {
  let formFondoPension = $('#formFondoPension');
  formFondoPension.validate({
    rules: {
      rdFondoPension: {
        required: true
      }
    },
    messages: {
      rdFondoPension: {
        required: "Seleccione al menos un régimen pensionario"
      }
    },
    errorPlacement: function (error, element) {
      if (element.parents('div').hasClass("checker") || element.parents('div').hasClass("choice") || element.parent().hasClass('bootstrap-switch-container')) {
        if (element.parents('label').hasClass('checkbox-inline') || element.parents('label').hasClass('radio-inline')) {
          error.appendTo(element.parent().parent().parent().parent());
        } else {
          error.appendTo(element.parent().parent().parent().parent().parent());
        }
      }
    }
  });
  return formFondoPension.valid();
}




///////////////////////////////////////////////////////////////////////////
/////////////////////////////DATOS PERSONALES//////////////////////////////
///////////////////////////////////////////////////////////////////////////

//listar estado civil
function listarEstadoCivil() {
  $.ajax({
    url: '../EstadoCivilServlet?accion=listarEstadoCivil',
    type: 'POST',
    data: {},
    dataType: 'json',
    beforeSend: function (xhr) {
      load("Listando estados civiles");
    },
    success: function (data, textStatus, jqXHR) {
      if (textStatus === 'success') {
        unload();
        let estadoCivil = data.data.EstadosCiviles;
        let cboEstadoCivil = `<option value="0">[SELECCIONAR]</option>`;
        for (let i in estadoCivil) {
          cboEstadoCivil += `<option value="${estadoCivil[i].codigoEstadoCivil}">${estadoCivil[i].nombre}</option>`;
        }
        $('#cboEstadoCivil').html(cboEstadoCivil);
        $('#cboEstadoCivil').selectpicker('refresh');

      }
    }
  });
}

// Validar la existencia del codigo de estado civil
function validarExistenciaEstadoCivil(codigoEstadoCivil) {
  return new Promise((resolve, reject) => {
    $.ajax({
      type: 'POST',
      url: '../EstadoCivilServlet?accion=validarExistenciaEstadoCivil',
      data: {codigoEstadoCivil: codigoEstadoCivil},
      dataType: 'json',
      beforeSend: function () {
        load("Validando existencia de estado civil...");
      },
      success: function (data, textStatus, jqXHR) {
        resolve(data);
      }, error: function (jqXHR, textStatus, errorThrown) {
        reject(new Error('Error al validar existencia estado civi'));
      }
    });
  });
}

// listar nacionalidad
function listarNacionalidadRequest() {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: '../NacionalidadServlet?accion=listarNacionalidad',
      type: 'POST',
      beforeSend: function (xhr) {
        load("Listando nacionalidades");
      },
      success: function (data, textStatus, jqXHR) {
        unload();
        resolve(data);
      }, error: function (jqXHR, textStatus, errorThrow) {
        reject("Error al listar nacionalidad");
      }
    });
  });
}

// pintar nacionalidad
function listarNacionalidadResponse() {
  listarNacionalidadRequest()
    .then((data) => {
      let nacionalidad = data.data.Nacionalidades;
      let cboNacionalidad = `<option value="0">[SELECCIONAR]</option>`;
      for (let i in nacionalidad) {
        cboNacionalidad += `<option value="${nacionalidad[i].codigoNacionalidad}" data-content="${nacionalidad[i].gentilicio} <img class='tp-countrie-icon' src='../img/countries/${nacionalidad[i].iso}.svg'"></option>`;
      }
      $('#cboNacionalidad').html(cboNacionalidad);
      $('#cboNacionalidad').selectpicker('refresh');
    });
}

//listar departamentos
function listarDepartamentosRequest() {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: '../UbigeoServlet?accion=listarDepartamento',
      type: 'POST',
      beforeSend: function (xhr) {
        load("Listando departamentos");
      },
      success: function (data, textStatus, jqXHR) {
        unload();
        resolve(data);
      }, error: function (jqXHR, textStatus, errorThrown) {
        reject("Error al listar departamento");
      }
    });
  });
}

// pintar departamentos
function listarDepartamentoResponse(identifier) {
  listarDepartamentosRequest()
    .then((data) => {
      if (data.status) {
        let departamentos = data.data.departamentos;
        let cboDepartamento = `<option value="0">[SELECCIONAR]</option>`;
        for (let i in departamentos) {
          cboDepartamento += `<option value="${departamentos[i].codigoDepartamento}">${departamentos[i].nombreDepartamento}</option>`;
        }
        $(`#${identifier}`).html(cboDepartamento);
        $(`#${identifier}`).selectpicker('refresh');
      }
    });
}

// listar provincias
function listarProvinciasRequest(codigoDepartamento) {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: '../UbigeoServlet?accion=listarProvincia',
      type: 'POST',
      data: {codigoDepartamento: codigoDepartamento},
      dataType: 'json',
      beforeSend: function (xhr) {
        load("Listando provincias");
      },
      success: function (data, textStatus, jqXHR) {
        unload();
        resolve(data);
      }, error: function (jqXHR, textStatus, errorThrown) {
        reject("Error al listar provincia");
      }
    });
  });

}

// pintar provincia 
function listarProvinciaResponse(codigoDepartamento, identifier) {
  listarProvinciasRequest(codigoDepartamento)
    .then((data) => {
      if (data.status) {
        let provincias = data.data.provincias;
        let cboProvincia = `<option value="0">[SELECCIONAR]</option>`;
        for (let i in provincias) {
          cboProvincia += `<option value="${provincias[i].codigoProvincia}">${provincias[i].nombreProvincia}</option>`;
        }
        $(`#${identifier}`).html(cboProvincia);
        $(`#${identifier}`).selectpicker('refresh');
      }
    });
}

// listar distritos
function listarDistritoRequest(codigoDepartamento, codigoProvincia) {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: '../UbigeoServlet?accion=listarDistrito',
      type: 'POST',
      data: {
        codigoDepartamento: codigoDepartamento,
        codigoProvincia: codigoProvincia
      },
      dataType: 'json',
      beforeSend: function (xhr) {
        load("Listando distritos");
      },
      success: function (data, textStatus, jqXHR) {
        unload();
        resolve(data);
      }, error: function (jqXHR, textStatus, errorThrown) {
        reject("Error al listar distrito");
      }
    });
  });
}

// pintar distritos 
function listarDistritoResponse(codigoDepartamento, codigoProvincia, identifier) {
  listarDistritoRequest(codigoDepartamento, codigoProvincia)
    .then((data) => {
      if (data.status) {
        let distritos = data.data.distritos;
        let cboDistrito = `<option value="0">[SELECCIONAR]</option>`;
        for (let i in distritos) {
          cboDistrito += `<option value="${distritos[i].codigoDistrito}">${distritos[i].nombreDistrito}</option>`;
        }
        $(`#${identifier}`).html(cboDistrito);
        $(`#${identifier}`).selectpicker('refresh');
      }
    });
}

// validar existencia nacionalidad
function validarExistenciaNacionalidad(codigoNacionalidad) {
  return new Promise((resolve, reject) => {
    $.ajax({
      type: 'POST',
      url: '../NacionalidadServlet?accion=validarExistenciaNacionalidad',
      data: {codigoNacionalidad: codigoNacionalidad},
      dataType: 'json',
      beforeSend: function (xhr) {
        load("Validando existencia nacionalidad...");
      },
      success: function (data, textStatus, jqXHR) {
        unload();
        resolve(data);
      }, error: function (jqXHR, textStatus, errorThrown) {
        reject("Error al validar existencia nacionalidad");
      }
    });
  });
}

// validar existencia de departamento
function validarExistenciaDepartamento(codigoDepartamento) {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: '../UbigeoServlet?accion=validarExistenciaDepartamento',
      type: 'POST',
      data: {
        codigoDepartamento: codigoDepartamento
      },
      dataType: 'json',
      success: function (data, textStatus, jqXHR) {
        resolve(data);
      }, error: function (jqXHR, textStatus, errorThrown) {
        console.log("Error al validardepartamento");
        reject('Error al validarExistenciaDepartamento');
      }
    });
  });
}

// validar existencia de provincia
function validarExistenciaProvincia(codigoDepartamento, codigoProvincia) {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: '../UbigeoServlet?accion=validarExistenciaProvincia',
      type: 'POST',
      data: {
        codigoDepartamento: codigoDepartamento,
        codigoProvincia: codigoProvincia
      },
      dataType: 'json',
      success: function (data, textStatus, jqXHR) {
        resolve(data);
      }, error: function (jqXHR, textStatus, errorThrown) {
        reject('Error al validarExistenciaDepartamento');
      }
    });
  });
}

// validar existencia de distrito
function validarExistenciaDistrito(codigoDepartamento, codigoProvincia, codigoDistrito) {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: '../UbigeoServlet?accion=validarExistenciaDistrito',
      type: 'POST',
      data: {
        codigoDepartamento: codigoDepartamento,
        codigoProvincia: codigoProvincia,
        codigoDistrito: codigoDistrito
      },
      dataType: 'json',
      success: function (data, textStatus, jqXHR) {
        resolve(data);
      }, error: function (jqXHR, textStatus, errorThrown) {
        reject('Error al validarExistenciaDepartamento');
      }
    });
  });
}

// obtener codigo Ubigeo
function obtenerCodigoUbigeoRequest(codigoDepartamento, codigoProvincia, codigoDistrito) {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: '../UbigeoServlet?accion=obtenerCodigoUbigeo',
      type: 'POST',
      dataType: 'json',
      data: {
        codigoDepartamento: codigoDepartamento,
        codigoProvincia: codigoProvincia,
        codigoDistrito: codigoDistrito
      },
      beforeSend: function (xhr) {
        load("Obteniendo codigo ubigeo");
      }, success: function (data, textStatus, jqXHR) {
        unload();
        resolve(data);
      }, error: function (jqXHR, textStatus, errorThrown) {
        reject("Error al obtener el codigo de ubigeo");
      }
    });
  });
}

// configuracion del combo nacionalidad dinamico
function configuracionComboNacionalidad() {
  $('#cboNacionalidad').on('change', function (e) {
    let codigoNacionalidad = parseInt($('#cboNacionalidad').val());
    validarExistenciaNacionalidad(codigoNacionalidad)
      .then((data) => {
        if (data.status) {
          if (codigoNacionalidad === 144) {
            $('#cboDepartamentoNacimiento').prop('disabled', false);
            $('#cboDepartamentoNacimiento').selectpicker('refresh');
            $('#cboProvinciaNacimiento').prop('disabled', true);
            $('#cboDistritoNacimiento').prop('disabled', true);
            $('#cboProvinciaNacimiento').selectpicker('refresh');
            $('#cboDistritoNacimiento').selectpicker('refresh');
            listarDepartamentoResponse('cboDepartamentoNacimiento');
          } else {
            $('#cboDepartamentoNacimiento').prop('disabled', true);
            $('#cboProvinciaNacimiento').prop('disabled', true);
            $('#cboDistritoNacimiento').prop('disabled', true);
            $('#cboDepartamentoNacimiento').html(`<option value="0">[SELECCIONAR]</option>`);
            $('#cboProvinciaNacimiento').html(`<option value="0">[SELECCIONAR]</option>`);
            $('#cboDistritoNacimiento').html(`<option value="0">[SELECCIONAR]</option>`);
            $('#cboDepartamentoNacimiento').selectpicker('refresh');
            $('#cboProvinciaNacimiento').selectpicker('refresh');
            $('#cboDistritoNacimiento').selectpicker('refresh');
          }
        }
      });
  });
}

// configuracion del combo de ubigeo nacimiento
function configuracionCombosUbigeoNacimiento() {
  $('#cboDepartamentoNacimiento').on('change', function (e) {
    let codigoDepartamento = $('#cboDepartamentoNacimiento').val();
    validarExistenciaDepartamento(codigoDepartamento)
      .then((data) => {
        if (data.status) {
          $('#cboProvinciaNacimiento').prop('disabled', false);
          $('#cboProvinciaNacimiento').selectpicker('refresh');
          $('#cboDistritoNacimiento').prop('disabled', true);
          $('#cboDistritoNacimiento').html(`<option value="0">[SELECCIONAR]</option>`);
          $('#cboDistritoNacimiento').selectpicker('refresh');
          listarProvinciaResponse(codigoDepartamento, 'cboProvinciaNacimiento');
        } else {
          $('#cboProvinciaNacimiento').prop('disabled', true);
          $('#cboDistritoNacimiento').prop('disabled', true);
          $('#cboProvinciaNacimiento').html(`<option value="0">[SELECCIONAR]</option>`);
          $('#cboDistritoNacimiento').html(`<option value="0">[SELECCIONAR]</option>`);
          $('#cboProvinciaNacimiento').selectpicker('refresh');
          $('#cboDistritoNacimiento').selectpicker('refresh');
        }
      });
  });

  $('#cboProvinciaNacimiento').on('change', function (e) {
    let codigoDepartamento = parseInt($('#cboDepartamentoNacimiento').val());
    let codigoProvincia = parseInt($('#cboProvinciaNacimiento').val());
    validarExistenciaProvincia(codigoDepartamento, codigoProvincia)
      .then((data) => {
        if (data.status) {
          $('#cboDistritoNacimiento').prop('disabled', false);
          $('#cboDistritoNacimiento').selectpicker('refresh');
          listarDistritoResponse(codigoDepartamento, codigoProvincia, 'cboDistritoNacimiento');
        } else {
          $('#cboDistritoNacimiento').prop('disabled', true);
          $('#cboDistritoNacimiento').html(`<option value="0">[SELECCIONAR]</option>`);
          $('#cboDistritoNacimiento').selectpicker('refresh');
        }
      });
  });

  $('#cboDistritoNacimiento').on('change', function (e) {
    let codigoDepartamento = $('#cboDepartamentoNacimiento').val();
    let codigoProvincia = $('#cboProvinciaNacimiento').val();
    let codigoDistrito = $('#cboDistritoNacimiento').val();
    obtenerCodigoUbigeoRequest(codigoDepartamento, codigoProvincia, codigoDistrito)
      .then((data) => {
        if (data.status) {
          codigoUbigeoNacimiento = data.data.getResultedKey;
        } else {
          console.log('no existe');
        }

      });
  });
}


function configuracionCombosUbigeoResidencia() {
  listarDepartamentoResponse('cboDepartamentoResidencia');
  $('#cboDepartamentoResidencia').on('change', function (e) {
    let codigoDepartamento = parseInt($('#cboDepartamentoResidencia').val());
    validarExistenciaDepartamento(codigoDepartamento)
      .then((data) => {
        if (data.status) {
          $('#cboProvinciaResidencia').prop('disabled', false);
          $('#cboProvinciaResidencia').selectpicker('refresh');
          $('#cboDistritoResidencia').prop('disabled', true);
          $('#cboDistritoResidencia').html(`<option value="0">[SELECCIONAR]</option>`);
          $('#cboDistritoResidencia').selectpicker('refresh');
          listarProvinciaResponse(codigoDepartamento, 'cboProvinciaResidencia');
        } else {
          $('#cboProvinciaResidencia').prop('disabled', true);
          $('#cboDistritoResidencia').prop('disabled', true);
          $('#cboProvinciaResidencia').html(`<option value="0">[SELECCIONAR]</option>`);
          $('#cboDistritoResidencia').html(`<option value="0">[SELECCIONAR]</option>`);
          $('#cboProvinciaResidencia').selectpicker('refresh');
          $('#cboDistritoResidencia').selectpicker('refresh');
        }
      });
  });

  $('#cboProvinciaResidencia').on('change', function (e) {
    let codigoDepartamento = parseInt($('#cboDepartamentoResidencia').val());
    let codigoProvincia = parseInt($('#cboProvinciaResidencia').val());
    validarExistenciaProvincia(codigoDepartamento, codigoProvincia)
      .then((data) => {
        if (data.status) {
          $('#cboDistritoResidencia').prop('disabled', false);
          $('#cboDistritoResidencia').selectpicker('refresh');
          listarDistritoResponse(codigoDepartamento, codigoProvincia, 'cboDistritoResidencia');
        } else {
          $('#cboDistritoResidencia').prop('disabled', true);
          $('#cboDistritoResidencia').html(`<option value="0">[SELECCIONAR]</option>`);
          $('#cboDistritoResidencia').selectpicker('refresh');
        }
      });
  });

  $('#cboDistritoResidencia').on('change', function (e) {
    let codigoDepartamento = $('#cboDepartamentoResidencia').val();
    let codigoProvincia = $('#cboProvinciaResidencia').val();
    let codigoDistrito = $('#cboDistritoResidencia').val();
    obtenerCodigoUbigeoRequest(codigoDepartamento, codigoProvincia, codigoDistrito)
      .then((data) => {
        if (data.status) {
          codigoUbigeoResidencia = data.data.getResultedKey;
        } else {
          console.log('no existe');
        }

      });
  });
}

///////////////////////////////////////////////////////////////////////////
/////////////////////////////DATOS PERSONALES//////////////////////////////
///////////////////////////////////////////////////////////////////////////






///////////////////////////////////////////////////////////////////////////
/////////////////////////////DATOS FAMILIARES//////////////////////////////
///////////////////////////////////////////////////////////////////////////

// Listar parentesco request
function listarParentescoRequest() {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: '../ParentescoServlet?accion=listarParentesco',
      type: 'POST',
      beforeSend: function (xhr) {
        load("Cargando parentescos");
      },
      success: function (data, textStatus, jqXHR) {
        unload();
        resolve(data);
      }, error: function (jqXHR, textStatus, errorThrown) {
        reject("Error al listar parentescos");
      }
    });
  });
}

// Pintar parentesco
function listarParentescoResponse(identifier, codigoParentesco) {
  listarParentescoRequest().
    then((data) => {
      if (data.status) {
        let parentescos = data.data.parentescos;
        let cboParentescoFamiliar = `<option value="0">[SELECCIONAR]</option>`;
        for (let i in parentescos) {
          cboParentescoFamiliar += `<option value="${parentescos[i].codigoParentesco}">${parentescos[i].nombre}</option>`;
        }
        $(`#${identifier}`).html(cboParentescoFamiliar);
        $(`#${identifier}`).val(codigoParentesco);
        $(`#${identifier}`).selectpicker('refresh');
      }
    });
}

// validar existencia parentesco
function validarExistenciaParentesco(codigoParentesco) {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: '../ParentescoServlet?accion=validarExistenciaParentesco',
      type: 'POST',
      data: {codigoParentesco: codigoParentesco},
      dataType: 'json',
      beforeSend: function (xhr) {
        load("Validando parentesco");
      }, success: function (data, textStatus, jqXHR) {
        unload();
        resolve(data);
      }, error: function (jqXHR, textStatus, errorThrown) {
        reject("Error al validarExistenciaParentesco");
      }
    });
  });
}

// configuracion
function configuracionComboParentesco() {
  $('#cboParentescoFamiliar').on('click', function (e) {

  });
}

// listar tipo documento request
function listarTipoDocumentoRequest() {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: '../TipoDocumentoServlet?accion=listarTipoDocumento',
      type: 'POST',
      beforeSend: function (xhr) {
        load("Cargando tipos de documentos");
      },
      success: function (data, textStatus, jqXHR) {
        unload();
        resolve(data);
      }, error: function (jqXHR, textStatus, errorThrown) {
        reject("Error al listar parentescos");
      }
    });
  });
}

// pintar tipo de documento
function listarTipoDocumentoResponse(identifier, codigoTipoDocumento) {
  listarTipoDocumentoRequest()
    .then((data) => {
      if (data.status) {
        let tipodocumentos = data.data.tipodocumentos;
        let cboTipoDocumentoFamiliar = `<option value="0">[SELECCIONAR]</option>`;
        for (let i in tipodocumentos) {
          cboTipoDocumentoFamiliar += `<option value="${tipodocumentos[i].codigoTipoDocumento}">${tipodocumentos[i].descripcionCorta}</option>`;
        }
        $(`#${identifier}`).html(cboTipoDocumentoFamiliar);
        $(`#${identifier}`).val(codigoTipoDocumento);
        $(`#${identifier}`).selectpicker('refresh');
      }
    });
}

// validar existencia tipo documento
function validarExistenciaTipoDocumento(codigoTipoDocumento) {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: '../TipoDocumentoServlet?accion=validarExistenciaTipoDocumento',
      type: 'POST',
      dataType: 'json',
      data: {codigoTipoDocumento: codigoTipoDocumento},
      beforeSend: function (xhr) {
        load("Validando existencia tipo de documento");
      }, success: function (data, textStatus, jqXHR) {
        unload();
        resolve(data);
      }, error: function (jqXHR, textStatus, errorThrown) {
        reject("Error al validar tipo de documento");
      }
    });
  });
}

//validar existencia numeroDocumentoFamilair
function validarExistenciaNumeroDocumentoFamiliar(numeroDocumento, longitud, tipoEntrada) {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: '../CargaFamiliarServlet?accion=validarExistenciaNumeroDocumento',
      type: 'POST',
      dataType: 'json',
      data: {
        numeroDocumento: numeroDocumento,
        longitud: longitud,
        tipoEntrada: tipoEntrada
      },
      beforeSend: function (xhr) {
        load("Validando numero de documento");
      }, success: function (data, textStatus, jqXHR) {
        unload();
        resolve(data);
      }, error: function (jqXHR, textStatus, errorThrown) {
        reject("Error al validarExistenciaNumeroDocumentoFamiliar");
      }
    });
  });
}

// Obtener la longitud y el tipo de entrada del tipo de documento
function obtenerLongitudTipoEntradaTipoDocumento(codigoTipoDocumento) {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: '../TipoDocumentoServlet?accion=obtenerLongitudTipoEntrdadaTipoDocumento',
      type: 'POST',
      dataType: 'json',
      data: {codigoTipoDocumento: codigoTipoDocumento},
      beforeSend: function (xhr) {
        load("Obteniendo longitud y tipo de entrada del documento");
      }, success: function (data, textStatus, jqXHR) {
        unload();
        resolve(data);
      }, error: function (jqXHR, textStatus, errorThrown) {
        reject("Error al obtenerLongitudTipoEntradaTipoDocumento");
      }
    });
  });
}

// configuracion del combo de tipo de documento
function configuracionComboTipoDocumentoFamiliar() {
  $('#cbotipoDocumentoFamiliar').on('change', function (e) {
    let codigoTipoDocumento = parseInt($('#cbotipoDocumentoFamiliar').val());
    $('#txtNumeroDocumentoFamiliar').val('');
    if (codigoTipoDocumento === 0) {
      $('#txtNumeroDocumentoFamiliar').prop('disabled', true);
      $('#txtNumeroDocumentoFamiliar').val('');
    } else {
      validarExistenciaTipoDocumento(codigoTipoDocumento)
        .then((data) => {
          if (data.status) {
            $('#txtNumeroDocumentoFamiliar').prop('disabled', false);
            obtenerLongitudTipoEntradaTipoDocumento(codigoTipoDocumento)
              .then((data) => {
                validarIngresoTxtNumeroDocumento(data);
              });
          }
        });
    }


  });
}

// validar el ingreso del txt Numero de documento
function validarIngresoTxtNumeroDocumento(data) {
  if (data.status) {
    let tipodocumentos = data.data.tipodocumentos;
    let longitud = tipodocumentos[0].longitud;
    let tipoEntrada = tipodocumentos[0].tipoEntrada;
    configuracionTxtNumeroDocumentoFamiliar(longitud, tipoEntrada);
  } else {
    console.log(data.message);
  }
}

// configuracion del numero de documento con el tipo de documento escogio
function configuracionTxtNumeroDocumentoFamiliar(longitud, tipoEntrada) {
  $('#txtNumeroDocumentoFamiliar').attr('maxlength', longitud);
  $('#txtNumeroDocumentoFamiliar').focus();
  if (tipoEntrada === 'N') {
    $('#txtNumeroDocumentoFamiliar').off('keypress keyup blur');
    soloNumeros('txtNumeroDocumentoFamiliar');
  } else {
    $('#txtNumeroDocumentoFamiliar').off('keypress keyup blur');
    soloAlfanumerico('txtNumeroDocumentoFamiliar');
  }
}




///////////////////////////////////////////////////////////////////////////
////////////////////////DATOS FORMACION ACADEMICA//////////////////////////
///////////////////////////////////////////////////////////////////////////


// listar grado(nivel) estudio
function listarNivelEstudioRequest() {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: '../NivelEstudioServlet?accion=listarNivelEstudio',
      type: 'POST',
      beforeSend: function (xhr) {
        load("Listando niveles de estudio");
      }, success: function (data, textStatus, jqXHR) {
        unload();
        resolve(data);
      }, error: function (jqXHR, textStatus, errorThrown) {
        reject("Error al listar niveles de estudio");
      }
    });
  });
}

// pintar grado(nivel) estudio
function listarNivelEstudioResponse(identifier, codigoNivelEstudio) {
  listarNivelEstudioRequest()
    .then((data) => {
      if (data.status) {
        let nivelEstudios = data.data.nivelestudio;
        let cboGradoEstudio = `<option value="0">[SELECCIONAR]</option>`;
        for (let i in nivelEstudios) {
          cboGradoEstudio += `<option value="${nivelEstudios[i].codigoNivelEstudio}">${nivelEstudios[i].nombre}</option>`;
        }
        $(`#${identifier}`).html(cboGradoEstudio);
        $(`#${identifier}`).val(codigoNivelEstudio);
        $(`#${identifier}`).selectpicker('refresh');
      }
    });
}

// validar existencia de grado(nivel) estudio
function validarExistenciaNivelEstudio(codigoNivelEstudio) {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: '../NivelEstudioServlet?accion=validarExistenciaNivelEstudio',
      type: 'POST',
      dataType: 'json',
      data: {codigoNivelEstudio: codigoNivelEstudio},
      beforeSend: function (xhr) {
        load("Validando nivel estudio");
      }, success: function (data, textStatus, jqXHR) {
        unload();
        resolve(data);
      }, error: function (jqXHR, textStatus, errorThrown) {
        reject("Error al validar existencia nivel estudio");
      }
    });
  });
}

// listar estado estudio
function listarEstadoEstudioRequest(codigoNivelEstudio) {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: '../EstadoEstudioServlet?accion=listarEstadoEstudio',
      type: 'POST',
      dataType: 'json',
      data: {
        codigoNivelEstudio: codigoNivelEstudio
      },
      beforeSend: function (xhr) {
        load("Listando estados de estudio");
      }, success: function (data, textStatus, jqXHR) {
        unload();
        resolve(data);
      }, error: function (jqXHR, textStatus, errorThrown) {
        reject("Error al listar estado de estudios");
      }
    });
  });
}

// pintar estado de estudios
function listarEstadoEstudioResponse(identifier, codigoNivelEstudio, codigoEstadoEstudio) {
  listarEstadoEstudioRequest(codigoNivelEstudio)
    .then((data) => {
      if (data.status) {
        let estadoEstudios = data.data.estadoestudio;
        let cboEstadoEstudio = `<option value="0">[SELECCIONAR]</option>`;
        for (let i in estadoEstudios) {
          cboEstadoEstudio += `<option value="${estadoEstudios[i].codigoEstadoEstudio}">${estadoEstudios[i].nombre}</option>`;
        }
        $(`#${identifier}`).html(cboEstadoEstudio);
        $(`#${identifier}`).val(codigoEstadoEstudio);
        $(`#${identifier}`).selectpicker('refresh');
      }
    });
}

// validar existencia del estado del estudio
function validarExistenciaEstadoEstudio(codigoNivelEstudio, codigoEstadoEstudio) {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: '../EstadoEstudioServlet?accion=validarExistenciaEstadoEstudio',
      type: 'POST',
      dataType: 'json',
      data: {
        codigoNivelEstudio: codigoNivelEstudio,
        codigoEstadoEstudio: codigoEstadoEstudio
      },
      beforeSend: function (xhr) {
        load("Validando estado estudio");
      }, success: function (data, textStatus, jqXHR) {
        unload();
        resolve(data);
      }, error: function (jqXHR, textStatus, errorThrown) {
        reject("Error al validarExistenciaEstadoEstudio");
      }
    });
  });
}

// configuracion del combo grado(nivel) estudio y estado estudio
function configuracionComboNivelEstadoEstudio() {
  $('#cboGradoEstudioFormacionAcademica').on('change', function (e) {
    let codigoNivelEstudio = parseInt($('#cboGradoEstudioFormacionAcademica').val());
    $('#txtCentroEstudiosFormacionAcademica').val('');
    validarExistenciaNivelEstudio(codigoNivelEstudio)
      .then((data) => {
        if (data.status) {
          $('#cboEstadoEstudioFormacionAcademica').attr('disabled', false);
          configuracionValidacionEstudiosColegiales(codigoNivelEstudio);
          listarEstadoEstudioResponse('cboEstadoEstudioFormacionAcademica', codigoNivelEstudio);
        }
      });
    if (codigoNivelEstudio === 0) {
      $('#cboEstadoEstudioFormacionAcademica').attr('disabled', true);
      $('#cboEstadoEstudioFormacionAcademica').html('<option value="0">[SELECCIONAR]</option>');
      $('#cboEstadoEstudioFormacionAcademica').selectpicker('refresh');
    }
  });

  $('#cboEstadoEstudioFormacionAcademica').on('change', function (e) {
    let codigoEstadoEstudio = parseInt($('#cboEstadoEstudioFormacionAcademica').val());
    let codigoNivelEstudio = parseInt($('#cboGradoEstudioFormacionAcademica').val());
    validarExistenciaEstadoEstudio(codigoNivelEstudio, codigoEstadoEstudio)
      .then((data) => {
        if (data.status) {
          $('#txtCentroEstudiosFormacionAcademica').focus();
        }
      });
  });
}

function configuracionValidacionEstudiosColegiales(codigoNivelEstudio) {
  if (codigoNivelEstudio === 1 || codigoNivelEstudio === 2) {
    $('#cboCarreraProfesionalFormacionAcademica').attr('disabled', true);
    $('#cboCarreraProfesionalFormacionAcademica').val(0);
    $('#select2-cboCarreraProfesionalFormacionAcademica-container').text('');
//        $("#cboCarreraProfesionalFormacionAcademica").rules("remove", "valueNotEquals");
  } else {
    $('#cboCarreraProfesionalFormacionAcademica').attr('disabled', false);
    $('#select2-cboCarreraProfesionalFormacionAcademica-container').text('');
//        $('#cboCarreraProfesionalFormacionAcademica').rules('add', {
//            valueNotEquals: "0"
//        });
  }
}


// lista las carreras profesionales
function listarCarreraProfesional_v2() {
  $('#cboCarreraProfesionalFormacionAcademica').select2({
    placeholder: "Select a state",
    allowClear: true,
    language: {
      noResults: function () {
        return "No hay resultado";
      },
      searching: function () {
        return "Buscando..";
      },
      inputTooShort: function (limit) {
        return 'Digite al menos ' + limit.minimum + ' caracter para buscar';
      }
    },
    ajax: {
      url: '../CarreraProfesionalServlet?accion=listarCarreraProfesional',
      dataType: 'json',
      delay: 250,
      contentType: "application/json; charset=utf-8",
      success: function (data) {
      },
      data: function (params) {
        return {
          q: params.term
        };
      },
      processResults: function (data, params) {
        return {
          results: data.items
        };
      },
      cache: true
    },
    escapeMarkup: function (markup) {
      return markup;
    },
    minimumInputLength: 2,
    templateResult: formatRepo,
    templateSelection: formatRepoSelection
  });
}

function formatRepo(repo) {
  if (repo.loading) {
    return repo.text;
  }

  var markup = '<option value="' + repo.codigoCarreraProfesional + '">' + repo.nombre + '</option>';

  return markup;
}

function formatRepoSelection(repo) {
  return repo.nombre;
}


// validar existencia carrera profesional
function validarExistenciaCarreraProfesional(codigoCarreraProfesional) {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: '../CarreraProfesionalServlet?accion=validarExistenciaCarreraProfesional',
      type: 'POST',
      dataType: 'json',
      data: {
        codigoCarreraProfesional: codigoCarreraProfesional
      },
      beforeSend: function (xhr) {
        load("Validando carrera profesional");
      }, success: function (data, textStatus, jqXHR) {
        unload();
        resolve(data);
      }, error: function (jqXHR, textStatus, errorThrown) {
        reject("Error al validad existencia de carrera profesional");
      }
    });
  });
}


// inicializar 
function inicializarComponentes() {
  var elemSwitch = Array.prototype.slice.call(document.querySelectorAll(".js-switch"));
  elemSwitch.forEach(function (html) {
    var switchery = new Switchery(html, {
      color: '#F4E04D',
      disabledOpacity: 0.75
    });

  });

  $(".styled, .multiselect-container input").uniform({
    radioClass: 'choice'
  });

  // solo fecha de nacimiento
  let date = new Date();
  $("#dpFechaNacimiento, #dpFechaNacimientoFamiliar, #dpFechaInicioExperienciaLaboral,#dpFechaFinExperienciaLaboral").datepicker({
    dateFormat: 'dd/mm/yy',
    showButtonPanel: false,
    changeMonth: true,
    changeYear: true,
    yearRange: '1900:' + date.getFullYear()
  });

  $('#dpFechaInicioFormacionAcademica').datepicker({
    dateFormat: 'dd/mm/yy',
    showButtonPanel: false,
    changeMonth: true,
    changeYear: true,
    yearRange: '1900:' + (parseInt(date.getFullYear()) + 5)
  });

  $('#dpFechaFinFormacionAcademica').datepicker({
    dateFormat: 'dd/mm/yy',
    showButtonPanel: false,
    changeMonth: true,
    changeYear: true,
    yearRange: '1900:' + (parseInt(date.getFullYear()) + 15)
  });


  $("#dpFechaNacimiento, #dpFechaNacimientoFamiliar, #dpFechaInicioFormacionAcademica, #dpFechaFinFormacionAcademica,#dpFechaInicioExperienciaLaboral, #dpFechaFinExperienciaLaboral").attr('readonly', true);

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

  $('.bootstrap-select').selectpicker();

  // Image lightbox
  $('[data-popup="lightbox"]').fancybox({
    padding: 3
  });

  $('div.fileinput-preview').children('img').attr('src', 'data:image/png;base64,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').css({
    "max-height": "180px",
    "width": "160px",
    "height": "180px",
    "object-fit": "cover"
  });
}

///////////////////////////////////////////////////////////////////////////
////////////////////////////DATOS FONDO PENSION////////////////////////////
///////////////////////////////////////////////////////////////////////////

function initFondoPension() {
  configuracionChkFondoPension()
}
// listar fondo de pension
function listarFondoPensionRequest() {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: '../FondoPensionServlet?accion=listarFondoPension',
      type: 'POST',
      beforeSend: function (xhr) {
        load("Listando fondo de pensiones");
      }, success: function (data, textStatus, jqXHR) {
        unload();
        resolve(data);
      }, error: function (jqXHR, textStatus, errorThrown) {
        reject("Error al listar fondo de pension");
      }
    });
  });
}

// pintar fondo pension
function listarFondoPensionResponse() {
  listarFondoPensionRequest()
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

        $('#divFondoPension').html(rowFondoPension);
        $('#rdAFP').attr('required', true);
        $(".styled, .multiselect-container input").uniform({
          radioClass: 'choice'
        });
      }
    });
}



// configuracion del checkbox de fondo pension
function configuracionChkFondoPension() {
  let chkFondoPension = document.querySelector('#chkFondoPension');
  $('#chkFondoPension').on('change', (e) => {
    if (chkFondoPension.checked) {
      $('.tp-info-fondo-pension__title').text('SELECCIONE EL SISTEMA PENSIONARIO EN EL QUE SE ENCUENTRE ACTUALMENTE');
      flagTieneFondoPension = true;
    } else {
      $('.tp-info-fondo-pension__title').text('SELECCIONE EL SISTEMA PENSIONARIO AL QUE DESEA APORTAR');
      flagTieneFondoPension = false;
    }

    console.log(flagTieneFondoPension)
  });
}

let configChkFondoPension = () => {
  $('#chkFondoPension').bootstrapToggle({
    on: 'SI',
    off: 'NO'
  });
}

let configChkExperienciaLaboral = () => {
  $('#chkExperienciaLaboral').bootstrapToggle({
    on: 'SI',
    off: 'NO'
  });
}

setTimeout(() => {
  $('#chkFondoPension').prop('checked', true);
  configChkFondoPension()
  configChkExperienciaLaboral()
}, 0);


///////////////////////////////////////////////////////////////////////////
////////////////////////////DATOS FONDO PENSION////////////////////////////
///////////////////////////////////////////////////////////////////////////
