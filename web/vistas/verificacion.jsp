<%@page import="java.util.Enumeration"%>
<%@page import="trismegistoplanilla.beans.TokenFichaBean"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="response/validarCache.jsp"%>
<%    /*Enumeration keys = session.getAttributeNames();
    while (keys.hasMoreElements()) {
        String key = (String) keys.nextElement();
        out.println(key + ": " + session.getValue(key) + "<br>");
    }*/

    TokenFichaBean tf = (TokenFichaBean) session.getAttribute("tokenFicha");
    if (tf == null) {
        String errorTokenFicha = "<div align='center' style=\"font-family: 'Arial', Sans-serif; color:red;\"><br/><h3>Lo sentimos, no hay nada para mostrar :(<h3></div>";
        out.print(errorTokenFicha);
    } else {
        session.removeAttribute("errorTokenFicha");
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <!--Estilos plantilla-->
        <%@include file="templates/header.jsp" %>
        <!--Estilos plantilla-->

        <!--Estilos propios-->
        <link href="../css/pages/verificacion.css" rel="stylesheet" type="text/css"/>
        <link href="../js/lib/jquery-confirm-master/jquery-confirm.min.css" rel="stylesheet" type="text/css"/>
        <!--Estilos propios-->
    </head>

    <body class="login-container pace-done tp-background-cover">

        <!-- Main navbar -->
        <div class="navbar navbar-inverse so-card-2">
            <!--<div class="navbar-header">-->
            <h5 class="text-center text-semibold text-uppercase">Verificación de personal</h5>
            <!--</div>-->
        </div>
        <!-- /main navbar -->

        <!-- Page container -->
        <div class="page-container">

            <!-- Page content -->
            <div class="page-content">

                <!-- Main content -->
                <div class="content-wrapper">

                    <!-- Content area -->
                    <div class="content">


                        <!-- ------------------------------------------------------------------------------------------------------ -->
                        <!-- -------------------------------------- PANEL DE LOGIN DE USUARIO ------------------------------------- -->
                        <!-- ------------------------------------------------------------------------------------------------------ -->

                        <!-- Simple login form -->
                        <form action="#" method="post" id="formValidarIngresoRegistroFicha" name="formValidarIngresoRegistroFicha">
                            <div class="panel panel-body login-form so-card-2">
                                <div class="text-center">
                                    <div class="icon-object border-blue-800 text-blue-800"><i class="fa fa-lock fa-lg"></i></div>
                                    <h5 class="content-group">Verificacion de Personal <small class="display-block">Ingrese sus credenciales</small></h5>
                                </div>

                                <div class="form-group">
                                    <label class="text-semibold" for="txtCodigoVerificacion">Ingrese su código de verificación</label>
                                    <div class="input-group">
                                        <span  class="input-group-addon bg-blue-800" data-popup="tooltip" data-original-title="Ingrese el código de registro que se le envió al correo"><i class="fa fa-key fa-lg"></i></span>
                                        <input id="txtCodigoVerificacion" name="txtCodigoVerificacion" type="text" class="form-control input-lg text-uppercase" placeholder="Ingresar código de registro">
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="text-semibold" for="txtNumeroDocumento">Ingrese su número de documento</label>
                                    <div class="input-group">
                                        <span class="input-group-addon bg-blue-800" data-popup="tooltip" data-original-title="Ingrese el número de documento el cuál proporcionó para su registro"><i class="icon-vcard"></i></span>
                                        <input  id="txtNumeroDocumento" name="txtNumeroDocumento" type="text" class="form-control input-lg text-uppercase" placeholder="Ingresar número documento">
                                    </div>
                                </div>

                                <div class="form-group">
                                    <button id="btnVerificarPersona" type="button" class="btn btn-success btn-block btn-lg text-uppercase"  data-animation="shake">Verificar<i class="icon-arrow-right14 position-right"></i></button>    
                                </div>
                                <div class="content-divider text-muted form-group"><span>Recomendamos usar </span></div>
                                <ul class="list-inline form-group list-inline-condensed text-center">
                                    <li><img class="browser-img" src="../img/browsers/chrome.svg" alt="chrome"></li>
                                    <li><img class="browser-img" src="../img/browsers/firefox.svg" alt="firefox"></li>
                                    <li><img class="browser-img" src="../img/browsers/opera.svg" alt="opera"></li>
                                    <li><img class="browser-img" src="../img/browsers/edge.svg" alt="edge"></li>

                                </ul>
                            </div>
                        </form>
                        <!-- ------------------------------------------------------------------------------------------------------ -->
                        <!-- -------------------------------------- PANEL DE LOGIN DE USUARIO ------------------------------------- -->
                        <!-- ------------------------------------------------------------------------------------------------------ -->



                        <!-- ------------------------------------------------------------------------------------------------------ -->
                        <!-- -------------------------------------- FORMULARIO VALIDAR TOKEN -------------------------------------- -->
                        <!-- ------------------------------------------------------------------------------------------------------ -->
                        <form action="../TokenFichaServlet" id="formTokenFicha" method="post"></form>
                        <form action="../PersonaServlet" id="formPersona" method="post"></form>
                        <!-- ------------------------------------------------------------------------------------------------------ -->
                        <!-- -------------------------------------- FORMULARIO VALIDAR TOKEN -------------------------------------- -->
                        <!-- ------------------------------------------------------------------------------------------------------ -->


                        <div class="footer text-muted text-center text-blue-800">
                            &copy; 2018. <a href="#" class="text-blue-800">Saco Oliveros - Trismegisto Planilla</a> por <a href="#" class="text-blue-800">Área Sistemas TIC</a>
                        </div>

                    </div>
                    <!-- /content area -->

                </div>
                <!-- /main content -->

            </div>
            <!-- /page content -->

        </div>
        <!-- /page container -->



        <!-- Core JS files -->
        <script type="text/javascript" src="../plantilla/assets/js/plugins/loaders/pace.min.js"></script>
        <script type="text/javascript" src="../plantilla/assets/js/core/libraries/jquery.min.js"></script>
        <script type="text/javascript" src="../plantilla/assets/js/core/libraries/bootstrap.min.js"></script>
        <script type="text/javascript" src="../plantilla/assets/js/plugins/loaders/blockui.min.js"></script>
        <!-- /core JS files -->
        <script type="text/javascript" src="../plantilla/assets/js/core/app.js"></script>

        <!--validInput-->
        <script type="text/javascript" src="../plantilla/assets/js/plugins/validateInput/validate_inputs.js"></script>
        <!--validate-->
        <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/validation/validate.min.js"></script>
        <script type="text/javascript" src="../plantilla/assets/js/plugins/forms/validation/additional_methods.min.js"></script>
        <script type="text/javascript" src="../js/pages/newRulesValidate.js"></script>

        <!--javascript general-->
        <script src="../js/pages/general.js" type="text/javascript"></script>
        <!--javascript general-->

        <!--javascript propios-->
        <script src="../js/pages/verificacion.js" type="text/javascript"></script>
        <script type="text/javascript" src="../js/lib/jquery-confirm-master/jquery-confirm.min.js"></script>
        <!--javascript propios-->

    </body>
</html>
<%}%>