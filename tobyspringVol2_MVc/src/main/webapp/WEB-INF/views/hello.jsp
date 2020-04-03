<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="com.raon.controller.test.HelloSpring" %>

<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello Spring!  
</h1>

<P>  The time on the server is ${serverTime}. </P>

ApplicationContext Test. <br>
Spring MVC 외의 웹기술에서 spring application context을 사용하는 방법은 WebApplicationContextUtils 이용.

<% 
	ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
	HelloSpring helloSpring = context.getBean(HelloSpring.class);
	out.println(helloSpring.sayHello("Root Context"));
%>
<br>
Servlet Web Application Context Test <br>
(DispatcherServlet) <br>
${message}
</body>



</html>
