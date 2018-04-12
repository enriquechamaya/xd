

/* global google */

let persona;
let tipoFicha;

let validateData = () => {
  if (window.name === '') {
    if (localStorage.getItem('paginaActual') === 'detalleConsultaLotesGeneral') {
      window.location = 'detalleConsultaLotesGeneral.jsp';
    } else if (localStorage.getItem('paginaActual') === 'detalleLoteAprobacion') {
      window.location = 'detalleLoteAprobacion.jsp';
    } else if (localStorage.getItem('paginaActual') === 'consultaFichas') {
      window.location = 'consultaFichas.jsp'
    }

  } else {
    persona = JSON.parse(window.name);
    tipoFicha = persona.datosAdministrativos.data.tipoFicha;
  }
};
let DOMPersonaComponents = {
  print(persona) {
    this.datosPersonales(persona);
    this.datosFamiliares(persona.cargafamiliar);
    this.formacionAcademica(persona.formacionacademica);
    this.experienciaLaboral(persona.experiencialaboral);
    this.datosAdministrativos(persona.datosAdministrativos.data);
    this.costosPersonal(persona.datosAdministrativos.data);
  },
  datosPersonales(obj) {
    document.querySelector('#lblTipoDocumento').innerHTML = obj.tipoDocumentoDescripcionCorta;
    document.querySelector('#lblNumeroDocumento').innerHTML = obj.numeroDocumento;
    document.querySelector('#lblNumeroRUC').innerHTML = obj.ruc;
    document.querySelector('#lblApellidoPaterno').innerHTML = obj.apellidoPaterno;
    document.querySelector('#lblApellidoMaterno').innerHTML = obj.apellidoMaterno;
    document.querySelector('#lblNombre').innerHTML = obj.nombre;
    document.querySelector('#lblSexo').innerHTML = obj.sexo;
    document.querySelector('#lblEstadoCivil').innerHTML = obj.estadoCivil;
    document.querySelector('#lblFechaNacimiento').innerHTML = obj.fechaNacimiento;
    document.querySelector('#lblNacionalidad').innerHTML = obj.pais;
    document.querySelector('#lblDepartamentoNacimiento').innerHTML = obj.nombreDepartamentoNacimiento;
    document.querySelector('#lblProvinciaNacimiento').innerHTML = obj.nombreProvinciaNacimiento;
    document.querySelector('#lblDistritoNacimiento').innerHTML = obj.nombreDistritoNacimiento;
    document.querySelector('#lblDireccionDocumento').innerHTML = obj.direccionDocumento;
    document.querySelector('#lblTelefonoFijo').innerHTML = obj.telefonoFijo;
    document.querySelector('#lblTelefonoMovil').innerHTML = obj.telefonoMovil;
    document.querySelector('#lblCorreoElectronico').innerHTML = obj.correo;
    document.querySelector('#lblDepartamentoResidencia').innerHTML = obj.nombreDepartamentoResidencia;
    document.querySelector('#lblProvinciaResidencia').innerHTML = obj.nombreProvinciaResidencia;
    document.querySelector('#lblDistritoResidencia').innerHTML = obj.nombreDistritoResidencia;
    document.querySelector('#lblDireccionResidencia').innerHTML = obj.direccionResidencia;
    document.querySelector('#lblFondoPension').innerHTML = obj.fondoPensionDescripcionCorta;
    document.querySelector('#latitudResidencia').innerHTML = obj.latitud;
    document.querySelector('#longitudResidencia').innerHTML = obj.longitud;
    document.querySelector('#lblCantidadDocumentos').innerHTML = obj.expediente.length;
    document.querySelector('#lblEnlaceAlfresco').innerHTML = obj.enlaceAlfresco;
    document.querySelector('#lblFoto').setAttribute('src', 'http:\\\\172.16.2.20/img/' + obj.foto);
  },
  datosFamiliares(obj) {
    let item = 1;
    let tblFamiliar = new SimpleTable({
      element: 'tblFamiliar', //id from the table
      data: obj,
      no_data_text: 'No tiene registros',
      columns: [{
          data: () => item++
        },
        {
          data: data => {
            return `${data.apellidoPaterno} ${data.apellidoMaterno}, ${data.nombre}`;
          }
        },
        {
          data: 'nombreParentesco'
        },
        {
          data: 'fechaNacimiento'
        },
        {
          data: 'nombreTipoDocumentoDescripcionLarga'
        },
        {
          data: 'numeroDocumento'
        },
        {
          data: 'telefono'
        }
      ]
    });
    tblFamiliar.createBody();
  },
  formacionAcademica(obj) {
    let item = 1;
    let tblFormacionAcademica = new SimpleTable({
      element: 'tblFormacionAcademica', //id from the table
      data: obj,
      no_data_text: 'No tiene registros',
      columns: [{
          data: () => item++
        },
        {
          data: 'nivelEstudio'
        },
        {
          data: 'estadoEstudio'
        },
        {
          data: 'nombreCentroEstudios'
        },
        {
          data: 'nombreCarreraProfesional'
        },
        {
          data: 'fechaInicio'
        },
        {
          data: 'fechaFin'
        }
      ]
    });
    tblFormacionAcademica.createBody();
  },
  experienciaLaboral(obj) {
    let item = 1;
    let tblExperienciaLaboral = new SimpleTable({
      element: 'tblExperienciaLaboral', //id from the table
      data: obj,
      no_data_text: 'No tiene registros',
      columns: [{
          data: () => item++
        },
        {
          data: 'nombreEmpresa'
        },
        {
          data: 'nombreCargo'
        },
        {
          data: 'fechaInicio'
        },
        {
          data: 'fechaFin'
        },
        {
          data: 'telefono'
        }
      ]
    });
    tblExperienciaLaboral.createBody();
  },
  datosAdministrativos(obj) {
    document.querySelector('#lblFechaIngreso').innerHTML = obj.fechaIngreso;
    document.querySelector('#lblFechaTermino').innerHTML = obj.fechaTermino;
    document.querySelector('#lblSede').innerHTML = obj.sede;
    document.querySelector('#lblArea').innerHTML = obj.area;
    document.querySelector('#lblCargo').innerHTML = obj.cargo;
    document.querySelector('#lblTipoPago').innerHTML = obj.tipoPago;
    document.querySelector('#lblTituloTabla').innerHTML = `Costo de ${obj.tipoFicha}`;
  },
  costosPersonal(obj) {
    if (localStorage.getItem('paginaActual') === 'consultaFichas') {
      if (obj.tipoFicha === 'ADMINISTRATIVO') {
        let costo = obj.datosAdministrativosCostos[0];
        printCostos.printAdministrativo().then(() => {
          document.querySelector('#lblSueldoEscalafon').innerHTML = `S/. ${costo.sueldoEscalafon}`;
          document.querySelector('#lblSueldoMensualPropuesto').innerHTML = `S/. ${costo.sueldoMensual}`;
        });
      } else if (obj.tipoFicha === 'DOCENTE') {
        if (obj.tipoPago === 'HORAS') {
          printCostosFichaCompletado.printDocenteHoras(obj.datosAdministrativosCostos[0]);
        } else if (obj.tipoPago === 'MENSUAL') {
          printCostosFichaCompletado.printDocentesMensual(obj.datosAdministrativosCostos[0]);
        } else if (obj.tipoPago === 'ADMINISTRATIVO') {
          printCostosFichaCompletado.printDocentesAdministrativo(obj.datosAdministrativosCostos[0]);
        }
      }
    } else {
      if (obj.tipoFicha === 'ADMINISTRATIVO') {
        let costo = obj.datosAdministrativosCostos[0];
        printCostos.printAdministrativo().then(() => {
          document.querySelector('#lblSueldoEscalafon').innerHTML = `S/. ${costo.sueldoEscalafon}`;
          document.querySelector('#lblSueldoMensualPropuesto').innerHTML = `S/. ${costo.sueldoMensual}`;
        });
      } else if (obj.tipoFicha === 'DOCENTE') {
        if (obj.tipoPago === 'HORAS') {
          printCostos.printDocenteHoras(obj);
        } else if (obj.tipoPago === 'MENSUAL') {
          printCostos.printDocenteMensual(obj);
        } else if (obj.tipoPago === 'ADMINISTRATIVO') {
          printCostos.printDocenteAdministrativo(obj);
        }
      }
    }

  }
};
let tablaCostos = '', estiloFila = '', label = '';
let flagCambiarSueldo = false;
let printCostos = {
  printAdministrativo() {
    return new Promise((resolve) => {
      let html = `
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">Escalafón<span class="text-danger"></span></label>
                        <h6 class="text-bold" id="lblSueldoEscalafon"></h6>
                      </div>
                    </div>
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">Sueldo propuesto<span class="text-danger"></span></label>
                        <h6 class="text-bold" id="lblSueldoMensualPropuesto"></h6>
                      </div>
                    </div>
                    `
      resolve(document.querySelector('#divContenidoCostos').innerHTML = html);
    });
  },
  printDocenteHoras(obj) {
    let costos = obj.datosAdministrativosCostos;
    return new Promise((resolve) => {

      tablaCostos = `<thead class="text-primary-800" style="background-color: #EEEEEE;">
                          <tr>
                              <th rowspan="2" class="text-center">#</th>
                              <th colspan="3" class="text-center">COSTOS</th>
                              <th rowspan="2" class="text-center">OBSERVACIÓN</th>
                              <th rowspan="2" class="text-center">ESTADO</th>
                              <th rowspan="2" class="text-center">FECHA DE REGISTRO</th>
                          </tr>
                          <tr>
                              <th class="text-center">A</th>
                              <th class="text-center">B</th>
                              <th class="text-center">C</th>
                          </tr>
                      </thead>
                      <tbody>`;
      for (let i in costos) {
        estiloFila = costos[i].estadoDocente === '0' ? 'danger ' : 'success text-black';
        label = costos[i].estadoDocente === '0' ? 'label label-danger' : 'label label-success';
        tablaCostos += `
                        <tr class="text-center ${estiloFila}">
                            <td>${parseInt(i) + 1}</td>
                            <td>${costos[i].costoA}</td>
                            <td>${costos[i].costoB}</td>
                            <td>${costos[i].costoC}</td>
                            <td>${costos[i].observacionDocente}</td>
                            <td><span class="${label}">${costos[i].descripcionEstadoDocente}</span></td>
                            <td>${costos[i].fechaRegistroDocente}</td>
                        </tr>
                        `;
      }
      resolve(document.querySelector('#tblCostos').innerHTML = tablaCostos);
    });
  },
  printDocenteMensual(obj) {
    let costos = obj.datosAdministrativosCostos;
    return new Promise((resolve) => {
      tablaCostos =
        `
              <thead class="text-primary-800" style="background-color: #EEEEEE;">
                <tr>
                  <th rowspan="1" class="text-center">#</th>
                  <th colspan="1" class="text-center">COSTOS MENSUAL</th>
                  <th rowspan="1" class="text-center">OBSERVACIÓN</th>
                  <th rowspan="1" class="text-center">ESTADO</th>
                  <th rowspan="1" class="text-center">FECHA DE REGISTRO</th>
                </tr>
              </thead>
              <tbody>`;
      for (let i in costos) {
        estiloFila = costos[i].estadoDocente === '0' ? 'danger' : ' success text-black';
        label = costos[i].estadoDocente === '0' ? 'label label-danger' : 'label label-success text-bold';
        tablaCostos += `
                        <tr class="text-center ${estiloFila}">
                          <td>${parseInt(i) + 1}</td>
                          <td>${costos[i].costoMensual}</td>
                          <td>${costos[i].observacionDocente}</td>
                          <td><span class="${label}">${costos[i].descripcionEstadoDocente}</span></td>
                          <td>${costos[i].fechaRegistroDocente}</td>
                        </tr>
                      `;
      }
      resolve(document.querySelector('#tblCostos').innerHTML = tablaCostos);
    });
  },
  printDocenteAdministrativo(obj) {
    let costos = obj.datosAdministrativosCostos;
    return new Promise((resolve) => {
      tablaCostos =
        `
              <thead class="text-primary-800" style="background-color: #EEEEEE;">
                <tr>
                  <th rowspan="2" class="text-center">#</th>
                  <th colspan="4" class="text-center">COSTOS</th>
                  <th rowspan="2" class="text-center">OBSERVACIÓN</th>
                  <th rowspan="2" class="text-center">ESTADO</th>
                  <th rowspan="2" class="text-center">FECHA DE REGISTRO</th>
                </tr>
                <tr>
                  <th class="text-center">MENSUAL</th>
                  <th class="text-center">A</th>
                  <th class="text-center">B</th>
                  <th class="text-center">C</th>
                </tr>
              </thead>
              <tbody>`;
      for (let i in costos) {

        estiloFila = costos[i].estadoDocente === '0' ? 'danger' : ' success text-black';
        label = costos[i].estadoDocente === '0' ? 'label label-danger' : 'label label-success text-bold';
        tablaCostos += `
                                    <tr class="text-center ${estiloFila}">
                                        <td>${parseInt(i) + 1}</td>
                                        <td>${costos[i].costoMensual}</td>
                                        <td>${costos[i].costoA}</td>
                                        <td>${costos[i].costoB}</td>
                                        <td>${costos[i].costoC}</td>
                                        <td>${costos[i].observacionDocente}</td>
                                        <td><span class="${label}">${costos[i].descripcionEstadoDocente}</span></td>
                                        <td>${costos[i].fechaRegistroDocente}</td>
                                    </tr>
                                `;
      }
      resolve(document.querySelector('#tblCostos').innerHTML = tablaCostos);
    });
  }
};
let printCostosFichaCompletado = {
  printDocenteHoras(obj) {
    return new Promise((resolve, reject) => {
      let html = `
                    <div class="col-md-2">
                      <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">COSTO A<span class="text-danger"></span></label>
                        <h6 class="text-bold" id="lblCostoA">S/. ${obj.costoA}</h6>
                      </div>
                    </div>
                    <div class="col-md-2">
                      <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">COSTO B<span class="text-danger"></span></label>
                        <h6 class="text-bold" id="lblCostoA">S/. ${obj.costoB}</h6>
                      </div>
                    </div>
                    <div class="col-md-2">
                      <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">COSTO C<span class="text-danger"></span></label>
                        <h6 class="text-bold" id="lblCostoA">S/. ${obj.costoC}</h6>
                      </div>
                    </div>
                    `
      resolve(document.querySelector('#divContenidoCostos').innerHTML = html);
    })
  },
  printDocentesMensual(obj) {
    return new Promise((resolve, reject) => {
      let html = `
                    <div class="col-md-2">
                      <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">COSTO MENSUAL<span class="text-danger"></span></label>
                        <h6 class="text-bold" id="lblCostoA">S/. ${obj.costoMensual}</h6>
                      </div>
                    </div>
                    `
      resolve(document.querySelector('#divContenidoCostos').innerHTML = html);
    })
  },
  printDocentesAdministrativo(obj) {
    return new Promise((resolve, reject) => {
      let html = `
                    <div class="col-md-2">
                      <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">COSTO A<span class="text-danger"></span></label>
                        <h6 class="text-bold" id="lblCostoA">S/. ${obj.costoA}</h6>
                      </div>
                    </div>
                    <div class="col-md-2">
                      <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">COSTO B<span class="text-danger"></span></label>
                        <h6 class="text-bold" id="lblCostoA">S/. ${obj.costoB}</h6>
                      </div>
                    </div>
                    <div class="col-md-2">
                      <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">COSTO C<span class="text-danger"></span></label>
                        <h6 class="text-bold" id="lblCostoA">S/. ${obj.costoC}</h6>
                      </div>
                    </div>
                    <div class="col-md-2">
                      <div class="form-group">
                        <label class="display-block text-uppercase text-semibold">COSTO MENSUAL<span class="text-danger"></span></label>
                        <h6 class="text-bold" id="lblCostoA">S/. ${obj.costoMensual}</h6>
                      </div>
                    </div>
                    `
      resolve(document.querySelector('#divContenidoCostos').innerHTML = html);
    })
  }
}

let DOMEvents = () => {
  // pintar botones
  if (persona.datosAdministrativos.data.tipoFicha === 'ADMINISTRATIVO') {
    // cambiar sueldo
    document.querySelector('#btnCambiarSueldoPropuesto').addEventListener('click', (e) => {
      flagCambiarSueldo = !flagCambiarSueldo;
      if (flagCambiarSueldo) { // si cambiara sueldo
        $('#btnObservar').css({'display': 'inline-block', 'width': '250px'});
        $('#btnAceptar').css({'display': 'none'});
        $('#btnRechazar').css({'display': 'none'});
        document.querySelector('#txtSueldoMensual').removeAttribute('disabled');
        document.querySelector('#txtSueldoMensual').focus();
        document.querySelector('#btnCambiarSueldoPropuesto').innerHTML = `<i class="fa fa-times fa-lg"></i> Cancelar cambio</a>`;
        $(document.querySelector('#btnCambiarSueldoPropuesto')).addClass('text-danger-800').removeClass('text-blue-800');
        $('#divObservacion').html(`
            <div class="form-group">
              <label for="txtApellidos" class="text-semibold">Observacion <span class="text-danger">*</span></label>
              <input type="text" class="form-control text-uppercase" placeholder="Ingresar observacion" id="txtObservacion" name="txtObservacion" maxlength="100">
            </div>
        `);
      } else { // no cambiara sueldo
        $('#btnObservar').css({'display': 'none'});
        $('#btnAceptar').css({'display': 'inline-block', 'width': '250px'});
        $('#btnRechazar').css({'display': 'inline-block', 'width': '250px'});
        document.querySelector('#txtSueldoMensual').setAttribute('disabled', true);
        document.querySelector('#btnCambiarSueldoPropuesto').innerHTML = `<i class="fa fa-pencil fa-lg"></i> Cambiar sueldo</a>`;
        $(document.querySelector('#btnCambiarSueldoPropuesto')).addClass('text-blue-800').removeClass('text-danger-800');
        $('#divObservacion').html('');
      }
    });

    // observar ficha
    document.querySelector('#btnObservar').addEventListener('click', (e) => {
      if (flagCambiarSueldo) {
        $.confirm({
          icon: 'glyphicon glyphicon-question-sign',
          theme: 'material',
          closeIcon: true,
          animation: 'scale',
          type: 'dark',
          title: 'Confirmar',
          content: '<span class="text-semibold">¿Seguro de aprobar ficha?</span> <br/> <span class="text-danger">Asegúrese de haber ingresado los datos correctamente</span>',
          buttons: {
            Aprobar: {
              btnClass: 'btn-success',
              action: function () {
                if (persona.datosAdministrativos.data.tipoFicha === 'ADMINISTRATIVO') {
                  if (parseFloat(persona.datosAdministrativos.data.datosAdministrativosCostos[0].sueldoMensual) !== parseFloat(document.querySelector('#txtSueldoMensual').value)) {
                    if ($('#txtObservacion').val().trim() !== '') {
                      let obj = {
                        codigoFicha: persona.codigoFicha,
                        sueldoEscalafon: persona.datosAdministrativos.data.datosAdministrativosCostos[0].sueldoEscalafon,
                        sueldoMensual: persona.datosAdministrativos.data.datosAdministrativosCostos[0].sueldoMensual,
                        codigoAreaCargoTipoPago: persona.datosAdministrativos.data.codigoAreaCargoTipoPago,
                        sueldoPresidencia: document.querySelector('#txtSueldoMensual').value,
                        observado: flagCambiarSueldo,
                        observacion: $('#txtObservacion').val().trim()
                      };
                      httpRequest.aprobarFichaAdministrativa(obj).then((data) => {
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
                                window.location = 'aprobacionFichas.jsp';
                              }
                            }
                          }
                        });
                      });
                    } else {
                      warningMessage('Debe ingresar una observación', $('#txtObservacion').focus());
                    }
                  } else {
                    warningMessage('Los sueldos no han cambiado');
                  }
                } else {

                }
              }
            },
            Cancelar: {
              btnClass: 'btn-danger'
            }
          }
        });
      } else {
        errorMessage('Usted no ha cambiado el sueldo');
      }
    });
  }
};

validateData();
DOMPersonaComponents.print(persona);
setTimeout(() => {
  window.name = '';
}, 0)

function setearMapa() {
//  console.log(json.latitud);
//  console.log(json.longitud);
//  

  myLatLng = {
    lat: parseFloat(persona.latitud),
    lng: parseFloat(persona.longitud)
  };

  let map = new google.maps.Map($('#mapResidencia')[0], {
    center: myLatLng,
    zoom: 18,
    disableDefaultUI: false,
    draggable: true
  });

  getMarker(myLatLng, map);

}


function getMarker(myLatLng, map) {
  let marker = new google.maps.Marker({
    position: myLatLng,
    map: map,
    draggable: false,
    animation: google.maps.Animation.DROP,
    anchorPoint: new google.maps.Point(0, -29)
  });
  let infowindow = new google.maps.InfoWindow();

  getAddress(myLatLng, infowindow, map, marker);
}

function getAddress(latlng, infowindow, map, marker) {
  let geocoder = new google.maps.Geocoder;

  geocoder.geocode({'location': latlng}, function (result, status) {
    if (status === 'OK') {
      infowindow.setContent(
        '<div>' +
        '<b class="text-uppercase">' + result[0].address_components[1].long_name + '</b> <br/>' +
        '<span class="text-muted">' + result[0].formatted_address + '</span> <br/>' +
        '</div>'
        );
      infowindow.open(map, marker);

    } else {
      load("Cargando mapa");
      setTimeout(() => unload(), 2000);
    }
  });

}

$(document).ready(() => {
  setearMapa();
});