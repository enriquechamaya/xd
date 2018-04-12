/* global google */

$(document).ready(function () {
  initMap();
});


let map, myLatLng, infowindow;

function initMap() {
  myLatLng = {
    lat: -12.0262676,
    lng: -77.1278633
  };
  map = new google.maps.Map($('#mapResidencia')[0], {
    center: myLatLng,
    zoom: 10
  });

  google.maps.event.addListener(map, 'click', function (event) {
    marker.setMap(null);
    getMarker(event.latLng);
    getAddress(event.latLng);
    getCordinates();
    $('#latitudResidencia').val(event.latLng.lat());
    $('#longitudResidencia').val(event.latLng.lng());
  });

  // mostrar marcador (marker)
  getMarker(myLatLng);

  // obtener coordenadas
  getCordinates();
}

let marker;
function getMarker(myLatLng) {
  marker = new google.maps.Marker({
    position: myLatLng,
    map: map,
    draggable: true,
    animation: google.maps.Animation.DROP,
    anchorPoint: new google.maps.Point(0, -29)
  });
  infowindow = new google.maps.InfoWindow();
}

function getCordinates() {
  google.maps.event.addListener(marker, 'dragend', function () {

    let lat = this.getPosition().lat();
    let lng = this.getPosition().lng();

    console.log(lat);
    console.log(lng);
    console.log("------------------------");

    let latlng = {lat: lat, lng: lng};

    getAddress(latlng);

    $('#latitudResidencia').val(lat);
    $('#longitudResidencia').val(lng);
  });
}

function getAddress(latlng) {
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
