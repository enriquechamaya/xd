<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<div class="" style="display: inline-block; position: relative; padding-left: 21px; line-height: 1;">
    <%
        Date hoy = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(hoy);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String meses[] = {"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"};
        String mes = meses[month];

    %>
    <i id='dia' style='display: inline-block; font-size: 28px;font-weight: normal;  font-style: normal;letter-spacing: -0.015em;'><%=day%></i> 
    <b style='display: inline-block; margin-left: 4px;font-weight: 400;'>
        <i id='mes' style='font-size: 11px;display: block;line-height: 12px; text-transform: uppercase;font-style: normal; font-weight: 400;'><%=mes%></i>
        <i id='anio' style='font-size: 11px;display: block;line-height: 12px;text-transform: uppercase; font-style: normal; font-weight: 400;'><%=year%></i>
    </b>
</div>