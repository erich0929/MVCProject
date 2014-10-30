package com.erich0929.webapp.core.mvc;

import java.net.URL;
import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;
import java.util.Hashtable;

import java.util.ArrayList;
import java.io.File;

public class UrlMappingContainer {
	public class Pair {
		public Class  klass;
		public Method function;
		public Pair (Class klass, Method function) {
			this.klass = klass;
			this.function = function;
		}
	}
	
	private String webappPath;
	private String packagePath;
	private String classRepository;
	//private String libRepository;
	
	private URLClassLoader loader;
	private Hashtable classTable;
	private final String WEB_ROOT = System.getProperty("catalina.base") + File.separator + "wtpwebapps" + File.separator ;
	
	public UrlMappingContainer (String webappName, String packageName) {
		
		this.webappPath = webappName + File.separator;
		this.packagePath = packageName.replace('.', File.separatorChar) + File.separator;
		this.classRepository = WEB_ROOT + webappPath + "WEB-INF" + File.separator +"classes" + File.separator;
		
		/* debug */
		System.out.println (this.webappPath);
		System.out.println (this.packagePath);
		System.out.println (this.classRepository);
		System.out.println (this.classRepository + this.packagePath);
		System.out.println ("packageName : " + packageName );
		//this.libRepository = WEB_ROOT + webappPath + "lib" + File.separator; 
		URL url [] = new URL [1]; 
		classTable = new Hashtable ();
		try {
			File [] packageFiles = new File (classRepository + packagePath).listFiles ();
			File repository = new File (classRepository);
			url [0] = repository.toURI().toURL ();
			System.out.println (url[0].toString());
			loader = new URLClassLoader (url, Thread.currentThread().getContextClassLoader());
			for (File iter : packageFiles) {
				String filename = iter.getName ();
				if (filename.endsWith(".class")) {
					System.out.println (packageName + "." + filename.substring(0, filename.lastIndexOf('.')));
					Class klass = loader.loadClass (packageName + "." + filename.substring(0, filename.lastIndexOf('.')));
					Controller controllerAnno;
					RequestMapping methodAnno;
					if ((controllerAnno = (Controller) klass.getAnnotation(Controller.class)) != null) {
						/* TODO : process requestMapping  */
						System.out.println ("Controller class found : "+ klass.getName());
						String controllerUrl = controllerAnno.url ();
						Method [] methods = klass.getMethods();
						for (Method function : methods) {
							//System.out.println ("method name : " + function.getName ());
							//System.out.println ("has anno : " + (function.getAnnotation(RequestMapping.class) != null));
							if ((methodAnno = function.getAnnotation(RequestMapping.class)) != null) {
								System.out.println ("RequestMapping method found : " + function.getName());
								String methodUrl = methodAnno.url ();
								if (methodUrl.equals("")) {
									// THROW EXCEPTION.
								} else {
									String fullUrl = "/" + webappPath.substring (0, webappPath.lastIndexOf("/")) + controllerUrl + methodUrl;
									classTable.put(fullUrl, new Pair (klass, function));
									System.out.println ("URL : " + fullUrl + ", " + "klass : " + klass.getName() + 
											", "  + "method : " +  function.getName());
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void dispatche (String url) {
		Pair pair = (Pair) classTable.get(url);
		Class klass = pair.klass;
		Method function = pair.function;
		System.out.println ("class : " + klass.getName () + ",  " + "method : " + function.getName ());
	}
}