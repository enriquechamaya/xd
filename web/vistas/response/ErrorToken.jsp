<%@page import="trismegistoplanilla.beans.TokenFichaBean"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../response/validarCache.jsp"%>
<%  // SESION: Obtener session respuestas del servidor (servletTokenFicha)
    String errorTokenFicha = (String) session.getAttribute("errorTokenFicha");
    if (errorTokenFicha == null) {
        errorTokenFicha = "<div align='center' style=\"font-family: 'Arial', Sans-serif; color:red;\"><br/><h3>Lo sentimos, no hay nada para mostrar :(<h3></div>";
        out.print(errorTokenFicha);
    } else {
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>:: Trismegistro Planilla ::</title>

        <!-- Global stylesheets -->
        <link href="https://fonts.googleapis.com/css?family=Roboto:400,300,100,500,700,900" rel="stylesheet" type="text/css">
        <link href="../../plantilla/assets/css/icons/icomoon/styles.css" rel="stylesheet" type="text/css">
        <link href="../../plantilla/assets/css/bootstrap.css" rel="stylesheet" type="text/css">
        <link href="../../plantilla/assets/css/core.css" rel="stylesheet" type="text/css">
        <link href="../../plantilla/assets/css/components.css" rel="stylesheet" type="text/css">
        <link href="../../plantilla/assets/css/colors.css" rel="stylesheet" type="text/css">
        <!-- /global stylesheets -->

        <!-- Core JS files -->
        <script type="text/javascript" src="../../plantilla/assets/js/plugins/loaders/pace.min.js"></script>
        <script type="text/javascript" src="../../plantilla/assets/js/core/libraries/jquery.min.js"></script>
        <script type="text/javascript" src="../../plantilla/assets/js/core/libraries/bootstrap.min.js"></script>
        <script type="text/javascript" src="../../plantilla/assets/js/plugins/loaders/blockui.min.js"></script>
        <!-- /core JS files -->


        <!-- Theme JS files -->
        <script type="text/javascript" src="../../plantilla/assets/js/core/app.js"></script>
        <!-- /theme JS files -->
    </head>
    <body class="login-container">
        <!-- Main navbar -->
        <div class="navbar navbar-inverse">
            <div class="navbar-header">
                <a class="navbar-brand" href="index.html"><img src="../../plantilla/assets/images/logo_light.png" alt=""></a>
            </div>
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

                        <form action="index.html">
                            <div class="panel panel-body login-form">
                                <div class="text-center">
                                    <div class="icon-object border-danger text-danger"><i class="icon-cross2"></i></div>
                                </div>
                                <div class="text-center text-black">
                                    <h1>
                                        ${errorTokenFicha}
                                    </h1>
                                </div>
                                <hr/>
                                <div class="text-semibold text-center text-muted">
                                    Si cree que este es un problema, por favor comunicarse con la persona quien realizó su registro.
                                </div>
                            </div>
                        </form>

                    </div>
                    <!-- /content area -->

                </div>
                <!-- /main content -->

            </div>
            <!-- /page content -->

        </div>
        <!-- /page container -->
    </body>
</html>
<% }%>