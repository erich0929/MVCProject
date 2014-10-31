package com.erich0929.webapp.core.mvc;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

@Target (ElementType.METHOD)
@Retention (RetentionPolicy.RUNTIME)
public @interface RequestMapping {
	String value () default "/";
	String method () default "GET";
}
