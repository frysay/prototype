package com.prototype.utils;

public class ExceptionUtil {

	private ExceptionUtil() {}

	public static String getOriginalClassAndLogLine(Throwable e) {
		String classAndLine = "";
		StackTraceElement[] stackTraces = e.getStackTrace();
		for(StackTraceElement stackTraceElement : stackTraces) {
			if(stackTraceElement.getClassName().contains("com.prototype.")) {
				String className = getSimpleClassName(stackTraceElement.getClassName());
				int line = stackTraceElement.getLineNumber();
				classAndLine = className + " " + line + ": ";
				break;
			}
		}
		return classAndLine;
	}

	private static String getSimpleClassName(String className) {
		String[] classPathParts = className.split("\\.");
		int lastIndex = classPathParts.length -1;
		return lastIndex != -1 ? classPathParts[lastIndex] : className;
	}
}