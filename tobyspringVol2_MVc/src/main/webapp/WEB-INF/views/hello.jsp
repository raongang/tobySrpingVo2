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



<% 
	ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
	HelloSpring helloSpring = context.getBean(HelloSpring.class);
	out.println(helloSpring.sayHello("Root Context"));
%>

</body>
</html>
