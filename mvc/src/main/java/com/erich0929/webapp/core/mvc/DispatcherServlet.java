package com.erich0929.webapp.core.mvc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;



import java.io.PrintWriter;
/**
 * Servlet implementation class TestController
 */
public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DispatcherServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
    public void init () {
    	ServletContext context = getServletContext ();
    	String webappname = context.getContextPath();
    	webappname = webappname.substring(1, webappname.length());
    	System.out.println ("Webappname : " + webappname);
    	
    	String controllerPackage = context.getInitParameter("Controller-package");
    	System.out.println ("Controller-package : " + controllerPackage);
    	UrlMappingContainer container = new UrlMappingContainer (webappname, controllerPackage);
		ViewResolver resolver = new ViewResolver ();

    	context.setAttribute("container", container);
    	context.setAttribute("resolver", resolver);
    	
    	
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ServletContext context = getServletContext ();
		UrlMappingContainer container = (UrlMappingContainer) context.getAttribute("container");
		ViewResolver resolver = (ViewResolver) context.getAttribute("resolver");
		String view = container.dispatch (request.getRequestURI(), request, response);
		if (view == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		} else {
			resolver.resolve(view, request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
