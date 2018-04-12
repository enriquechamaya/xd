<%@page import="trismegistoplanilla.beans.PersonaBean"%>
<%@page import="java.util.Enumeration"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="validarCache.jsp" %>
<%    /*Enumeration keys = session.getAttributeNames();
    while (keys.hasMoreElements()) {
        String key = (String) keys.nextElement();
        out.println(key + ": " + session.getValue(key) + "<br>");
    }*/
  PersonaBean p = (PersonaBean) session.getAttribute("persona");
  System.out.println("p =>>>>>>>>> " + p);
  if (p == null) {
    String errorTokenFicha = "<div align='center' style=\"font-family: 'Arial', Sans-serif; color:red;\"><br/><h3>Lo sentimos, no hay nada para mostrar :(<h3></div>";
    out.print(errorTokenFicha);
  } else {

    String fichaCompletada = (String) session.getAttribute("fichaCompletada");
    System.out.println("ficha completada =>>>>>>>>>>>>>>>>>> " + fichaCompletada);
    if (fichaCompletada == null) {
      String errorTokenFicha = "<div align='center' style=\"font-family: 'Arial', Sans-serif; color:red;\"><br/><h3>Lo sentimos, no hay nada para mostrar :(<h3></div>";
      out.print(errorTokenFicha);
    } else {
      if (!fichaCompletada.equals("ok")) {
        String errorTokenFicha = "<div align='center' style=\"font-family: 'Arial', Sans-serif; color:red;\"><br/><h3>Lo sentimos, no hay nada para mostrar :(<h3></div>";
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
                  <div class="icon-object border-success text-success"><i class="icon-checkmark3"></i></div>
                </div>
                <div class="text-center">
                  <h1 class="text-black">Enhorabuena!</h1>
                  <hr/>
                  <h1 class="text-semibold">
                    <%=p.getApellidoPaterno() + " "
                            + p.getApellidoMaterno() + " "
                            + p.getNombre()%>
                    , tu ficha ha sido registrada con éxito.
                  </h1>
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
    <script type="text/javascript">
      $(function () {
        setTimeout((e) => {
          window.close();
        }, 5000);
      });
    </script>
  </body>
</html>
<%}
    }
  }%>