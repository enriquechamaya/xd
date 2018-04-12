// FUNCION PARA EL SIDEBAR
function despliegue() {

  // ========================================
  //
  // Navbar
  //
  // ========================================


  // Navbar navigation
  // -------------------------

  // Prevent dropdown from closing on click
  $(document).on('click', '.dropdown-content', function (e) {
    e.stopPropagation();
  });

  // Disabled links
  $('.navbar-nav .disabled a').on('click', function (e) {
    e.preventDefault();
    e.stopPropagation();
  });

  // Show tabs inside dropdowns
  $('.dropdown-content a[data-toggle="tab"]').on('click', function (e) {
    $(this).tab('show');
  });
  // ========================================
  //
  // Main navigation
  //
  // ========================================
  // Main navigation
  // -------------------------

  // Add 'active' class to parent list item in all levels
  $('.navigation').find('li.active').parents('li').addClass('active');

  // Hide all nested lists
  $('.navigation').find('li').not('.active, .category-title').has('ul').children('ul').addClass('hidden-ul');

  // Highlight children links
  $('.navigation').find('li').has('ul').children('a').addClass('has-ul');

  // Add active state to all dropdown parent levels
  $('.dropdown-menu:not(.dropdown-content), .dropdown-menu:not(.dropdown-content) .dropdown-submenu').has('li.active').addClass('active').parents('.navbar-nav .dropdown:not(.language-switch), .navbar-nav .dropup:not(.language-switch)').addClass('active');

  // Collapsible functionality
  // -------------------------

  // Main navigation
  $('.navigation-main').find('li').has('ul').children('a').on('click', function (e) {
    e.preventDefault();

    // Collapsible
    $(this).parent('li').not('.disabled').not($('.sidebar-xs').not('.sidebar-xs-indicator').find('.navigation-main').children('li')).toggleClass('active').children('ul').slideToggle(250);

    // Accordion
    if ($('.navigation-main').hasClass('navigation-accordion')) {
      $(this).parent('li').not('.disabled').not($('.sidebar-xs').not('.sidebar-xs-indicator').find('.navigation-main').children('li')).siblings(':has(.has-ul)').removeClass('active').children('ul').slideUp(250);
    }
  });
  // Alternate navigation
  $('.navigation-alt').find('li').has('ul').children('a').on('click', function (e) {
    e.preventDefault();

    // Collapsible
    $(this).parent('li').not('.disabled').toggleClass('active').children('ul').slideToggle(200);

    // Accordion
    if ($('.navigation-alt').hasClass('navigation-accordion')) {
      $(this).parent('li').not('.disabled').siblings(':has(.has-ul)').removeClass('active').children('ul').slideUp(200);
    }
  });
}


// TRANSFORMAR LA PRIMERA LETRA EN MAYUSCULA Y LAS DEMAS EN MINUSCULA
// EJM: AlOnSo moreNO pOSTIGO ========> capitalizeWords =======> Alonso Moreno Postigo
let capitalizeWords = str => {
  const new_words = [];
  str.split(' ').map((word, i, arr) => new_words.push(word.charAt(0).toUpperCase() + word.slice(1).toLowerCase()));
  return new_words.join(' ');
};

//CARGAR EL MENU 
function cargar() {
  $.ajax({
    type: 'POST',
    url: '../MenuServlet?accion=listarMenu',
    dataType: 'json',
    data: {
      codigoTitulo: $("#codTitulo").val(),
      codigoModulo: $("#codModulo").val(),
      codigoCategoria: $('#codCategoria').val(),
      codigoSubCategoria: $('#codSubCategoria').val()
    },
    success: function (data) {
      let codigoModuloActual = data.codigoMenuActual.codigoModulo;
      let codigoCategoriaActual = data.codigoMenuActual.codigoCategoria;
      let codigoSubCategoriaActual = data.codigoMenuActual.codigoSubCategoria;
      let menuTitulo = data.menuTitulo;
      let menuModulo = data.menuModulo;
      let menuCategoria = data.menuCategoria;
      let menuSubCategoria = data.menuSubCategoria;

      let active = '';
      let menu = ``;
      let list_page_header = ``;

      for (let titulo in menuTitulo) { // titulo de inicio
        let codigoTitulo = menuTitulo[titulo].codigoTitulo; // codigoTitulo
        menu +=
          `
                <li class="navigation-header"> 
                    <span><i class="fa ${menuTitulo[titulo].icono} fa-sm position-left"></i>${capitalizeWords(menuTitulo[titulo].nombre)}</span>
                </li>
                `;
        for (let modulo in menuModulo) {
          if (menuModulo[modulo].codigoTitulo === codigoTitulo) {
            let codigoModulo = menuModulo[modulo].codigoModulo;
            let tipoModulo = menuModulo[modulo].tipo;
            active = '';
            //${capitalizeWords(menuTitulo[i].nombre)}
            if (codigoModulo === codigoModuloActual) {
              active = 'active';
              list_page_header += `<li><i class="fa ${menuTitulo[titulo].icono} position-left"></i>${capitalizeWords(menuTitulo[titulo].nombre)}</li>
                                   <li><i class="fa ${menuModulo[modulo].icono} position-left"></i>${capitalizeWords(menuModulo[modulo].nombre)}</li>`;
            }

            if (tipoModulo === 1) { // 1 : No tiene categoria
              menu +=
                `
                      <li class='${active}'> 
                          <a href='javascript:paginaMenuSession(${codigoTitulo},${codigoModulo},0,0,"${menuModulo[modulo].url}")'> 
                              <i class='fa ${menuModulo[modulo].icono}'></i> 
                              <span>${capitalizeWords(menuModulo[modulo].nombre)}</span> 
                          </a> 
                      </li>
                      `;
            } else { // 2. Si tiene categoria
              menu +=
                `
                      <li class='${active}'> 
                          <a  href='${menuModulo[modulo].url}'> 
                              <i class='fa ${menuModulo[modulo].icono}'></i>  
                              <span class='nav-label'>${capitalizeWords(menuModulo[modulo].nombre)}</span>
                          </a> 
                          <ul class='nav nav-second-level'>
                      `;
              for (let categoria in menuCategoria) {
                if (menuCategoria[categoria].codigoModulo === codigoModulo) {
                  let codigoCategoria = menuCategoria[categoria].codigoCategoria;
                  let tipoCategoria = menuCategoria[categoria].tipo;
                  active = '';

                  if (codigoCategoria === codigoCategoriaActual) {
                    active = 'active';
                    list_page_header += `<li class="active">${capitalizeWords(menuCategoria[categoria].nombre)}</li>`;
                  }

                  if (tipoCategoria === 1) { // 1. SIN SUBCATEGORIA
                    menu +=
                      `
                            <li class='${active}'>
                                <a href='javascript:paginaMenuSession(${codigoTitulo},${codigoModulo},${codigoCategoria},0,"${menuCategoria[categoria].url}")'>${capitalizeWords(menuCategoria[categoria].nombre)}</a>
                            </li>
                            `;
                  } else { // 2. CON SUBCATEGORIA
                    menu +=
                      `
                            <li class='${active}'>
                                <a href='${menuCategoria[categoria].url}'>${capitalizeWords(menuCategoria[categoria].nombre)}<span class='fa arrow'></span></a>
                                <ul class='nav nav-third-level'>
                            `;

                    for (let subcategoria in menuSubCategoria) {
                      if (menuSubCategoria[subcategoria].codigoCategoria === codigoCategoria) {
                        let codigoSubCategoria = menuSubCategoria[subcategoria].codigoSubcategoria;
                        active = '';
                        if (codigoSubCategoria === codigoSubCategoriaActual) {
                          active = 'active';
                        }

                        menu +=
                          `
                                <li class='${active}'>
                                    <a href='javascript:paginaMenuSession(${codigoTitulo},${codigoModulo},${codigoCategoria},${codigoSubCategoria},"${menuSubCategoria[subcategoria].url}")'>${capitalizeWords(menuSubCategoria[subcategoria].nombre)}</a>
                                </li>
                                `;
                      }
                    }
                    menu += `</ul></li>`;
                  }
                }
              }
              menu += `</ul></li>`;
            }

          }
        }
      }
      $('#lista_menu').html(menu);
      if (codigoModuloActual === 0 && codigoCategoriaActual === 0 && codigoSubCategoriaActual === 0) {
        $('#list_page_header').html(`<li><i class="icon-home2 position-left"></i>Menú Principal</li>`);
      } else {
        $('#list_page_header').html(list_page_header);
      }
      $('.tp-avatar').attr('avatar', data.nombre.substr(0, 1) + " " + data.apellidoPaterno.substring(0, 1));
//      $('.nombre-usuario').text(capitalizeWords(data.nombre));
      $('.nombre-usuario').text(data.nombre.split(" ")[0] + ' ' + data.apellidoPaterno + " " + data.apellidoMaterno);
//      $('.tipo-usuario').text(capitalizeWords(data.tipoUsuario));
      $('.tipo-usuario').html('<i class="icon-shield-check text-info"></i> ' + data.tipoUsuario);
      generarLetraCircular();
    }, complete: function () {
      despliegue();
//      init();
    }
  });
}

// OBTENER EL MENU EN LA SESION ACTUAL
function paginaMenuSession(codigoTitulo, codigoModulo, codigoCategoria, codigoSubCategoria, pagina) {
  $.ajax({
    type: 'POST',
    url: '../MenuServlet?accion=paginaMenuSession',
    dataType: 'json',
    data: {
      codigoTitulo: codigoTitulo,
      codigoModulo: codigoModulo,
      codigoCategoria: codigoCategoria,
      codigoSubCategoria: codigoSubCategoria
    },
    success: function (resultado) {
    }, complete: function () {
      window.location = "" + pagina + "";
    }
  });
}

// TRANSFORMAR LA PRIMERA LETRA EN MAYUSCULA Y LAS DEMAS EN MINUSCULA POR DATOS OBTENIDOS EN LA SESION (SOLO POR EL ID)
// EJM: AlOnSo moreNO pOSTIGO ========> capitalizeWordsSesion =======> Alonso Moreno Postigo
let capitalizeWordsSesion = () => {
  $('#nombreUsuarioSideBar').text(capitalizeWords($('#nombreUsuarioSideBar').text()));
  $('#rolUsuarioSideBar').html('<i class="icon-user text-size-small"></i>' + capitalizeWords($('#rolUsuarioSideBar').text()));

};
capitalizeWordsSesion();

let invalidarSesion = () => {
  load(`Cerrando sesión`);
  window.location.href = 'templates/close.jsp';
};
