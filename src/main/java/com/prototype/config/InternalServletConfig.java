package com.prototype.config;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * configures an extra servlet that listens to /internal and redirects to where the actual actuator lies)
 */
@Configuration
public class InternalServletConfig {

	@Value("${server.servlet-path}${management.contextPath}")
	String actuatorPath;

	@Bean
	public ServletRegistrationBean internal(){
		HttpServlet servlet = new HttpServlet() {

			private static final long serialVersionUID = -1247053040636467457L;

			@Override
			protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
				String pathInfo = StringUtils.isEmpty(req.getPathInfo()) ? req.getPathInfo() : "/";
				RequestDispatcher dsp = getServletContext().getRequestDispatcher(actuatorPath+pathInfo);
				dsp.forward(req, resp);
			}
		};

		return new ServletRegistrationBean(servlet, "/internal/*");
	}

}
