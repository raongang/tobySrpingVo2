package com.raon.web.dispatcherServlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* 서블릿 구현 클래스*/
public class SimpleGetServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String name = req.getParameter("name");
		
		resp.getWriter().print("<HTML><BODY>");
		resp.getWriter().print("Hello " + name);
		resp.getWriter().print("</BODY></HTML>");
		
	}
}
