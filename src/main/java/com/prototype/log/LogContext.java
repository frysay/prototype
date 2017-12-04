package com.prototype.log;

import org.slf4j.MDC;

public class LogContext {

	public static void setController(String controller) {
		MDC.put("ctrl", controller);
	}
	
	public static void setUser(String userId) {
		MDC.put("usr", userId);
	}

	public static void clearController() {
		MDC.remove("ctrl");
	}
	
	public static void clearUser() {
		MDC.remove("usr");
	}

	public static void clearAll() {
		MDC.remove("ctrl");
		MDC.remove("usr");
	}
	
	public static void setupLogContext(String controller, String userId) {
		LogContext.setController(controller);
		LogContext.setUser(userId);
	}
}

