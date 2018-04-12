<%-- 
    Document   : close
    Created on : 29/01/2018, 04:00:48 PM
    Author     : sistem23user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page session="true" %>
<%
    HttpSession session_actual = request.getSession(true);
    session_actual.removeAttribute("usuario");
    session_actual.invalidate();
    response.sendRedirect("../login.jsp");
%>
