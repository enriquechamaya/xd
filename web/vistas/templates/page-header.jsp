<%-- 
    Document   : page-header
    Created on : 29/01/2018, 08:30:29 PM
    Author     : sistem23user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!-- Page header -->
<div class="page-header page-header-default has-cover">
  <div class="page-header-content">
    <div class="page-title">
      <h4 id="title_page_header">
          <i class="icon-mailbox position-left"></i> <span id="nombreAreaPageHeader" class="text-semibold"><%try {%><%=usuario.getNombreArea()%><%} catch (NullPointerException e) {
              e.getMessage();
            }%></span> - Bandeja de entrada
          <small class="display-block"><%try {%><%=usuario.getNombreArea()%> - <%=usuario.getNombreCargo()%><%} catch (NullPointerException e) {
              e.getMessage();
            }%></small>
      </h4>
    </div>
    <div class="heading-elements">
      <%@include file="datetime-server.jsp" %>
    </div>
  </div>
  <div class="breadcrumb-line">
    <ul class="breadcrumb" id="list_page_header">
    </ul>
  </div>
</div>
<!-- /page header -->

<!-- Content area -->
<div class="content">