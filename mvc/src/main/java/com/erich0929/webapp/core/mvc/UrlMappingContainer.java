package com.erich0929.webapp.core.mvc;

import java.net.URL;
import java.lang.annotation.*;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;
import java.util.Hashtable;
import java.io.File;

public class UrlMappingContainer {
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
		try {
			File [] packageFiles = new File (classRepository + packagePath).listFiles ();
			File repository = new File (classRepository);
			url [0] = repository.toURI().toURL ();
			System.out.println (url[0].toString());
			loader = new URLClassLoader (url, Thread.currentThread().getContextClassLoader());
			for (File iter : packageFiles) {
				String filename = iter.getName ();
				if (filename.endsWith(".class")) {
					//System.out.println (packageName + "." + filename.substring(0, filename.lastIndexOf('.')));
					Class klass = loader.loadClass (packageName + "." + filename.substring(0, filename.lastIndexOf('.')));
					if (klass.getAnnotation(Controller.class) != null) {
						/* TODO : process requestMapping  */
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		classTable = new Hashtable ();
	}
	
}