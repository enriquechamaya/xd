$(function () {
  llenarDatosFicha();
  rechazarModificacionSueldos();
  aceptarModificacionSueldos();
});


let codigoFicha;
let json;
function llenarDatosFicha() {
  if (window.name === '') {
    window.location = "index.jsp";
  } else {
    json = JSON.parse(window.name);
    configuracionDatosPersonales(json);
  }
}

function configuracionDatosPersonales(json) {
  $('#lblTipoDocumento').text(`${json.tipoDocumentoDescripcionLarga} (${json.tipoDocumentoDescripcionCorta})`);
  $('#lblNumeroDocumento').text(json.numeroDocumento);
  $('#lblNumeroRUC').text(json.ruc);
  $('#lblApellidoPaterno').text(json.apellidoPaterno);
  $('#lblApellidoMaterno').text(json.apellidoMaterno);
  $('#lblNombre').text(json.nombre);
  $('#lblSexo').text(json.sexo);
  $('#lblEstadoCivil').text(json.estadoCivil);
  $('#lblFechaNacimiento').text(json.fechaNacimiento);
  $('#lblNacionalidad').text(json.gentilicio);
  $('#lblDepartamentoNacimiento').text(json.nombreDepartamentoNacimiento);
  $('#lblProvinciaNacimiento').text(json.nombreProvinciaNacimiento);
  $('#lblDistritoNacimiento').text(json.nombreDistritoNacimiento);
  $('#lblDireccionDocumento').text(json.direccionDocumento);
  $('#lblTelefonoFijo').text(json.telefonoFijo);
  $('#lblTelefonoMovil').text(json.telefonoMovil);
  $('#lblCorreoElectronico').text(json.correo);
  $('#lblDepartamentoResidencia').text(json.nombreDepartamentoResidencia);
  $('#lblProvinciaResidencia').text(json.nombreProvinciaResidencia);
  $('#lblDistritoResidencia').text(json.nombreDistritoResidencia);
  $('#lblDireccionResidencia').text(json.direccionResidencia);
  $('#lblFondoPension').text(json.fondoPensionDescripcionCorta);
  $('#latitudResidencia').val(json.latitud);
  $('#longitudResidencia').val(json.longitud);
  $('#lblFoto').attr({
    src: 'http:\\\\172.16.2.20/img/' + json.foto
  });
  configuracionTblFamiliar(json.cargafamiliar);
  configuracionTblFormacionAcademica(json.formacionacademica);
  configuracionTblExperienciaLaboral(json.experiencialaboral);
  codigoFicha = json.codigoFicha;
  configuracionDatosAdministrativos();
}

function configuracionTblFamiliar(cargafamiliar) {
  let tblFamiliar = new SimpleTable({
    element: 'tblFamiliar', //id from the table
    data: cargafamiliar,
    no_data_text: 'No tiene registros',
    columns: [
      {
        data: function (data) {
          return `${data.apellidoPaterno} ${data.apellidoMaterno}, ${data.nombre}`;
        }
      },
      {data: 'nombreParentesco'},
      {data: 'fechaNacimiento'},
      {data: 'nombreTipoDocumentoDescripcionLarga'},
      {data: 'numeroDocumento'},
      {data: 'telefono'}
    ]
  });
  tblFamiliar.createBody();
}

function configuracionTblFormacionAcademica(formacionacademica) {
  let tblFormacionAcademica = new SimpleTable({
    element: 'tblFormacionAcademica', //id from the table
    data: formacionacademica,
    no_data_text: 'No tiene registros',
    columns: [
      {data: 'nivelEstudio'},
      {data: 'estadoEstudio'},
      {data: 'nombreCentroEstudios'},
      {data: 'nombreCarreraProfesional'},
      {data: 'fechaInicio'},
      {data: 'fechaFin'}
    ]
  });
  tblFormacionAcademica.createBody();
}

function configuracionTblExperienciaLaboral(experiencialaboral) {
  let tblExperienciaLaboral = new SimpleTable({
    element: 'tblExperienciaLaboral', //id from the table
    data: experiencialaboral,
    no_data_text: 'No tiene registros',
    columns: [
      {data: 'nombreEmpresa'},
      {data: 'nombreCargo'},
      {data: 'fechaInicio'},
      {data: 'fechaFin'},
      {data: 'telefono'}
    ]
  });
  tblExperienciaLaboral.createBody();
}

function configuracionDatosAdministrativos() {
  let datosAdministrativos = json.datosAdministrativos.data;
  $('#lblFechaIngreso').text(datosAdministrativos.fechaIngreso);
  $('#lblFechaTermino').text(datosAdministrativos.fechaTermino);
  $('#lblSede').text(datosAdministrativos.sede);
  $('#lblArea').text(datosAdministrativos.area);
  $('#lblCargo').text(datosAdministrativos.cargo);
  $('#lblTipoPago').text(datosAdministrativos.tipoPago);

  let tablaCostos = ``, estiloFila = '', label = '';
  // tipo de ficha
  if (datosAdministrativos.tipoFicha === 'DOCENTE') {
    // titulo tabla costos
    $('#lblTituloTabla').text('COSTOS DE ' + datosAdministrativos.tipoFicha);
    // tipo de pago docente
    if (datosAdministrativos.tipoPago === 'HORAS') {
      tablaCostos =
        `
                      <thead class="text-primary-800" style="background-color: #EEEEEE;">
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
      for (let i = 0; i < datosAdministrativos.datosAdministrativosCostos.length; i++) {
        estiloFila = datosAdministrativos.datosAdministrativosCostos[i].estadoDocente === '0' ? 'danger ' : 'success text-black';
        label = datosAdministrativos.datosAdministrativosCostos[i].estadoDocente === '0' ? 'label label-danger' : 'label label-success';
        tablaCostos += `
                                <tr class="text-center ${estiloFila}">
                                    <td>${i + 1}</td>
                                    <td>${datosAdministrativos.datosAdministrativosCostos[i].costoA}</td>
                                    <td>${datosAdministrativos.datosAdministrativosCostos[i].costoB}</td>
                                    <td>${datosAdministrativos.datosAdministrativosCostos[i].costoC}</td>
                                    <td>${datosAdministrativos.datosAdministrativosCostos[i].observacionDocente}</td>
                                    <td><span class="${label}">${datosAdministrativos.datosAdministrativosCostos[i].descripcionEstadoDocente}</span></td>
                                    <td>${datosAdministrativos.datosAdministrativosCostos[i].fechaRegistroDocente}</td>
                                </tr>
                                `;
      }
      `</tbody>`;
      $('#tblCostos').html(tablaCostos);
      $("html, body").animate({scrollTop: $(document).height()}, 1000);
    } else if (datosAdministrativos.tipoPago === 'MENSUAL') {
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
      for (let i = 0; i < datosAdministrativos.datosAdministrativosCostos.length; i++) {
        estiloFila = datosAdministrativos.datosAdministrativosCostos[i].estadoDocente === '0' ? 'danger' : ' success text-black';
        label = datosAdministrativos.datosAdministrativosCostos[i].estadoDocente === '0' ? 'label label-danger' : 'label label-success text-bold';
        tablaCostos += `
                                    <tr class="text-center ${estiloFila}">
                                        <td>${i + 1}</td>
                                        <td>${datosAdministrativos.datosAdministrativosCostos[i].costoMensual}</td>
                                        <td>${datosAdministrativos.datosAdministrativosCostos[i].observacionDocente}</td>
                                        <td><span class="${label}">${datosAdministrativos.datosAdministrativosCostos[i].descripcionEstadoDocente}</span></td>
                                        <td>${datosAdministrativos.datosAdministrativosCostos[i].fechaRegistroDocente}</td>
                                    </tr>
                                `;
      }
      `</tbody>`;
      $('#tblCostos').html(tablaCostos);
      $("html, body").animate({scrollTop: $(document).height()}, 1000);
    } else if (datosAdministrativos.tipoPago === 'ADMINISTRATIVO') {
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
      for (let i = 0; i < datosAdministrativos.datosAdministrativosCostos.length; i++) {
        estiloFila = datosAdministrativos.datosAdministrativosCostos[i].estadoDocente === '0' ? 'danger' : ' success text-black';
        label = datosAdministrativos.datosAdministrativosCostos[i].estadoDocente === '0' ? 'label label-danger' : 'label label-success text-bold';
        tablaCostos += `
                                    <tr class="text-center ${estiloFila}">
                                        <td>${i + 1}</td>
                                        <td>${datosAdministrativos.datosAdministrativosCostos[i].costoMensual}</td>
                                        <td>${datosAdministrativos.datosAdministrativosCostos[i].costoA}</td>
                                        <td>${datosAdministrativos.datosAdministrativosCostos[i].costoB}</td>
                                        <td>${datosAdministrativos.datosAdministrativosCostos[i].costoC}</td>
                                        <td>${datosAdministrativos.datosAdministrativosCostos[i].observacionDocente}</td>
                                        <td><span class="${label}">${datosAdministrativos.datosAdministrativosCostos[i].descripcionEstadoDocente}</span></td>
                                        <td>${datosAdministrativos.datosAdministrativosCostos[i].fechaRegistroDocente}</td>
                                    </tr>
                                `;
      }
      `</tbody>`;
      $('#tblCostos').html(tablaCostos);
      $("html, body").animate({scrollTop: $(document).height()}, 1000);
    }
  } else if (datosAdministrativos.tipoFicha === 'ADMINISTRATIVO') {
    // titulo tabla costos 9NB6T2KBF6
    $('#lblTituloTabla').text('COSTOS DE ' + datosAdministrativos.tipoFicha);
    tablaCostos =
      `
                  <thead class="text-primary-800" style="background-color: #EEEEEE;">
                      <tr>
                          <th class="text-center">#</th>
                          <th class="text-center">SUELDO ESCALAFON</th>
                          <th class="text-center">SUELDO PROPUESTO</th>
                          <th class="text-center">SUELDO PRESIDENCIA</th>
                          <th class="text-center">OBSERVACIÓN</th>
                          <th class="text-center">ESTADO</th>
                          <th class="text-center">FECHA DE REGISTRO</th>
                      </tr>
                  </thead>
                  <tbody>`;
    for (let i = 0; i < datosAdministrativos.datosAdministrativosCostos.length; i++) {
      estiloFila = datosAdministrativos.datosAdministrativosCostos[i].estadoAdministrativo === '0' ? 'danger' : ' success text-black';
      label = datosAdministrativos.datosAdministrativosCostos[i].estadoAdministrativo === '0' ? 'label label-danger' : 'label label-success text-bold';
      tablaCostos += `
                                    <tr class="text-center ${estiloFila}">
                                        <td>${i + 1}</td>
                                        <td>${datosAdministrativos.datosAdministrativosCostos[i].sueldoEscalafon}</td>
                                        <td>${datosAdministrativos.datosAdministrativosCostos[i].sueldoMensual}</td>
                                        <td>${datosAdministrativos.datosAdministrativosCostos[i].sueldoPresidencia}</td>
                                        <td>${datosAdministrativos.datosAdministrativosCostos[i].observacionAdministrativo}</td>
                                        <td><span class="${label}">${datosAdministrativos.datosAdministrativosCostos[i].descripcionEstadoAdministrativo}</span></td>
                                        <td>${datosAdministrativos.datosAdministrativosCostos[i].fechaRegistroAdministrativo}</td>
                                    </tr>
                                `;
    }
    $('#tblCostos').html(tablaCostos);
    $("html, body").animate({scrollTop: $(document).height()}, 1000);
  }

}

/**
 * realiza la accion de rechazar la ficha
 */
function rechazarFichaRequest() {
  let datosAdministrativos = json.datosAdministrativos.data;
  return new Promise((resolve, reject) => {
    $.ajax({
      type: 'POST',
      url: '../FichaServlet',
      data: {accion: 'rechazarFicha', codigoFicha: datosAdministrativos.codigoFicha},
      beforeSend: function (xhr) {
        load('Se esta procediendo a rechazar la ficha, espere un momento');
      },
      success: function (data, textStatus, jqXHR) {
        if (textStatus === 'success') {
          if (data.status) {
            unload();
            resolve(data.message);
          }
        }
      },
      error: function (jqXHR, textStatus, errorThrown) {
        reject("No se pudo procesar la solicitud de rechazar ficha");
      }
    });
  });
}

/**
 * Rechazo de modificacion de sueldos por presidencia
 */
function rechazarModificacionSueldos() {
  $('#btnRechazar').on('click', () => {
    $.confirm({
      icon: 'fa fa-question',
      draggable: false,
      animationBounce: 1.5,
      closeAnimation: 'opacity',
      theme: 'modern',
      closeIcon: false,
      animation: 'scale',
      backgroundDismissAnimation: 'glow',
      type: 'red',
      title: '¿Está seguro de realizar esta acción?',
      content: '<span class="text-danger">La ficha pasará a un estado "RECHAZADO" y no podrá ser <b>revertida</b>.</span>',
      buttons: {
        'Sí, estoy seguro': {
          btnClass: 'btn-danger',
          action: () => {
            rechazarFichaRequest().then((data) => {
              console.log(data);
              $.confirm({
                title: 'Enhorabuena!',
                content: '<b>' + data + '</br>',
                type: 'green',
                typeAnimated: true,
                autoClose: 'Aceptar|1500',
                buttons: {
                  Aceptar: {
                    btnClass: 'btn-success',
                    action: () => {
                      $(location).attr('href', 'index.jsp');
                    }
                  }
                }
              });
            });
          }
        },
        'Cancelar': {
          btnClass: 'btn-default'
        }
      }
    });
  });
}

/**
 * realiza la accion de aceptar la ficha
 */
function aceptarFichaRequest() {
  let datosAdministrativos = json.datosAdministrativos.data;
  return new Promise((resolve, reject) => {
    $.ajax({
      type: 'POST',
      url: '../FichaServlet',
      data: {accion: 'aceptarFicha', codigoFicha: datosAdministrativos.codigoFicha},
      beforeSend: function (xhr) {
        load('Se esta procediendo a rechazar la ficha, espere un momento');
      },
      success: function (data, textStatus, jqXHR) {
        if (textStatus === 'success') {
          if (data.status) {
            unload();
            resolve(data.message);
          }
        }
      },
      error: function (jqXHR, textStatus, errorThrown) {
        reject("No se pudo procesar la solicitud de aceptar ficha");
      }
    });
  });
}

/**
 * Acepta modificacion de sueldos por presidencia
 */
function aceptarModificacionSueldos() {
  $('#btnAceptar').on('click', () => {
    $.confirm({
      icon: 'fa fa-question',
      draggable: false,
      animationBounce: 1.5,
      closeAnimation: 'opacity',
      theme: 'modern',
      closeIcon: false,
      animation: 'scale',
      backgroundDismissAnimation: 'glow',
      type: 'red',
      title: '¿Está seguro de realizar esta acción?',
      content: 'Esta a punto de aceptar el cambio de costo realizado por presidencia',
      buttons: {
        'Sí, estoy seguro': {
          btnClass: 'btn-danger',
          action: () => {
            aceptarFichaRequest().then((data) => {
              console.log(data);
              $.confirm({
                title: 'Enhorabuena!',
                content: '<b>' + data + '</br>',
                type: 'green',
                typeAnimated: true,
                autoClose: 'Aceptar|1500',
                buttons: {
                  Aceptar: {
                    btnClass: 'btn-success',
                    action: () => {
                      $(location).attr('href', 'index.jsp');
                    }
                  }
                }
              });
            });
          }
        },
        'Cancelar': {
          btnClass: 'btn-default'
        }
      }
    });
  });
}


function setearMapa() {
//  console.log(json.latitud);
//  console.log(json.longitud);
//  

  myLatLng = {
    lat: parseFloat(json.latitud),
    lng: parseFloat(json.longitud)
  };

  let map = new google.maps.Map($('#mapResidencia')[0], {
    center: myLatLng,
    zoom: 17,
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