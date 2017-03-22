package com.xmnjm;

import com.xmnjm.filter.HTTPAuthorizeAttribute;
import com.xmnjm.jwt.Audience;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableConfigurationProperties(Audience.class)
@EnableAutoConfiguration(exclude={MultipartAutoConfiguration.class})
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

	@Bean(name="multipartResolver")
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver  resolver=new CommonsMultipartResolver ();
		resolver.setDefaultEncoding("utf-8");
		return resolver;
	}
}
