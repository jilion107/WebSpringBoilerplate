package com.xmnjm;

import com.xmnjm.filter.HTTPAuthorizeAttribute;
import com.xmnjm.jwt.Audience;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableConfigurationProperties(Audience.class)
public class WebSpringBoilerplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebSpringBoilerplateApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean jwtFilterRegistrationBean(){
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		HTTPAuthorizeAttribute httpBearerFilter = new HTTPAuthorizeAttribute();
		registrationBean.setFilter(httpBearerFilter);
		List<String> urlPatterns = new ArrayList<String>();
		urlPatterns.add("/api/*");
		registrationBean.setUrlPatterns(urlPatterns);
		return registrationBean;
	}
}
