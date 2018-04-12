<%-- 
    Document   : validar
    Created on : 29/01/2018, 04:07:53 PM
    Author     : sistem23user
--%>
<%@page import="java.util.Enumeration"%>
<%@page import="pe.siso.webservicesseguridad.webservices.MenuBean"%>
<%@page import="pe.siso.webservicesseguridad.webservices.UsuarioBean"%>
<%@page import="pe.siso.webservicesseguridad.webservices.ProyectoBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page session="true"%>

<%
  /*  Enumeration keys = session.getAttributeNames();
  while (keys.hasMoreElements()) {
    String key = (String) keys.nextElement();
    out.println(key + ": " + session.getValue(key) + "\n");
  }
   */
  response.setHeader("Cache-Control", "no-cache"); //Forces caches to obtain a new copy of the page from the origin server
  response.setHeader("Cache-Control", "no-store"); //Directs caches not to store the page under any circumstance
  response.setDateHeader("Expires", 0); //Causes the proxy cache to see the page as "stale"
  response.setHeader("Pragma", "no-cache"); //HTTP 1.0 backward compatibility
  HttpSession session_actual = request.getSession(true);
  UsuarioBean usuario = (UsuarioBean) session_actual.getAttribute("usuario");
  List<MenuBean> menuTitulo = (List<MenuBean>) session_actual.getAttribute("menuSesionTitulo");
  List<MenuBean> menuModulo = (List<MenuBean>) session_actual.getAttribute("menuSesionModulo");
  List<MenuBean> menuCategoria = (List<MenuBean>) session_actual.getAttribute("menuSesionCategoria");
  List<MenuBean> menuSubCategoria = (List<MenuBean>) session_actual.getAttribute("menuSesionSubCategoria");
  ArrayList<ProyectoBean> listaProyectos = (ArrayList<ProyectoBean>) session_actual.getAttribute("listaProyectos");
  
  MenuBean menu = (MenuBean) session_actual.getAttribute("menu");
  
  if (usuario == null) {
    response.sendRedirect("login.jsp");
    
  } else {
    

%>