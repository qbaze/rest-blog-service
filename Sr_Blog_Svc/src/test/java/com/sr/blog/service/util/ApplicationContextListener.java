package com.sr.blog.service.util;

import org.springframework.context.ApplicationContext;

public class ApplicationContextListener {
	private static ApplicationContext ctx;
	
	public static void set(ApplicationContext ctx) {
		ApplicationContextListener.ctx = ctx;
	}

	public static ApplicationContext getCtx() {
		return ctx;
	}
}
