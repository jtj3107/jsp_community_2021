<%@ page import="com.jhs.exam.exam2.util.Ut" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%
Object data = request.getAttribute("data");
response.getWriter().print(Ut.toJson(data, ""));
%>