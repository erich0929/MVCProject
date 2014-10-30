package com.erich0929.webapp.core.mvc;

import javax.servlet.http.*;
import java.lang.Exception.*;

public class ViewResolver {
	private String rootPath;
	public ViewResolver (String rootPath) {
		if (!rootPath.startsWith("/")) 
			throw new IllegalArgumentException (
					"Invalid rootPath : " + rootPath + 
					"\nrootPath must be started with '/'");
		this.rootPath = rootPath;
		
	}
	public ViewResolver () {
		this.rootPath = "/";
	}
	public void resolve (String view, HttpServletRequest req, HttpServletResponse res) {
		try {	
			req.getRequestDispatcher(rootPath + view).forward(req, res);
		} catch (Exception e) {
			e.printStackTrace ();
		}
	}
}
