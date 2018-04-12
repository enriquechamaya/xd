<%@page import="pe.siso.webservicesseguridad.webservices.UsuarioBean"%>
<%
  response.setHeader("Cache-Control", "no-cache"); //Forces caches to obtain a new copy of the page from the origin server
  response.setHeader("Cache-Control", "no-store"); //Directs caches not to store the page under any circumstance
  response.setDateHeader("Expires", 0); //Causes the proxy cache to see the page as "stale"
  response.setHeader("Pragma", "no-cache"); //HTTP 1.0 backward compatibility

  HttpSession sesion_actual = request.getSession(true);
  UsuarioBean usuario = (UsuarioBean) sesion_actual.getAttribute("usuario");
  if (usuario != null) {
    response.sendRedirect("index.jsp");
  }
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <!--Estilos plantilla-->
        <%@include file="templates/header.jsp" %>
        <!--Estilos plantilla-->

        <!--Estilos propios-->
        <link href="../css/pages/login.css" rel="stylesheet" type="text/css"/>
        <!--Estilos propios-->

    </head>
    <body class="login-container">

        <div class="page-container login-cover">

            <div class="page-content">

                <div class="content-wrapper">

                    <div class="content">

                        <div class="login-form">

                            <div class="text-center">
                                <div class="icon-object border-white text-white"><i class="icon-user-check"></i></div>
                                <h1 class="text-uppercase text-white text-bold">trismegisto planilla 
                                    <small class="display-block text-muted">Ingrese sus credenciales</small>
                                </h1>
                            </div>

                            <div class="form-group has-feedback">
                                <div class="input-group input-group-xlg">
                                    <span class="input-group-addon"><i class="fa fa-user fa-lg"></i></span>
                                    <input id="txtUsuario" type="text" class="form-control" value="" placeholder="Ingresar usuario">
                                </div>
                            </div>

                            <div class="form-group has-feedback">
                                <div class="input-group input-group-xlg">
                                    <span class="input-group-addon"><i class="fa fa-lock fa-lg"></i></span>
                                    <input id="txtContrasena" type="password" class="form-control" value="" placeholder="&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;">
                                </div>
                            </div>

                            <div id="alertMessageError" class="alert alert-danger no-border" style="display: none;">
                                <span class="text-semibold">El usuario y/o contraseña ingresado no existe.</span> Pruebe nuevamente.
                            </div>

                            <button id="btnIngresar" class="btn bg-pink-800 btn-block btn-xlg text-uppercase text-bold"  data-animation="shake">Ingresar<i class="fa fa-chevron-right fa-lg position-right"></i></button>                            

                        </div>

                    </div>

                </div>

            </div>

        </div>


        <!-- Core JS files -->
        <script type="text/javascript" src="../plantilla/assets/js/plugins/loaders/pace.min.js"></script>
        <script type="text/javascript" src="../plantilla/assets/js/core/libraries/jquery.min.js"></script>
        <script type="text/javascript" src="../plantilla/assets/js/core/libraries/bootstrap.min.js"></script>
        <script type="text/javascript" src="../plantilla/assets/js/plugins/loaders/blockui.min.js"></script>
        <!-- /core JS files -->
        <script type="text/javascript" src="../plantilla/assets/js/core/app.js"></script>
        <!--velocity-->
        <script type="text/javascript" src="../plantilla/assets/js/plugins/velocity/velocity.min.js"></script>
        <script type="text/javascript" src="../plantilla/assets/js/plugins/velocity/velocity.ui.min.js"></script>

        <!--javascript propios-->
        <script src="../js/pages/login.js" type="text/javascript"></script>
        <!--javascript propios-->
    </body>
</html>
