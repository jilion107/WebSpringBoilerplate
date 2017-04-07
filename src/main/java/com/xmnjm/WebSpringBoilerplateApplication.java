package com.xmnjm;

import com.xmnjm.filter.HTTPAuthorizeAttribute;
import com.xmnjm.jwt.Audience;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableConfigurationProperties(Audience.class)
@EnableAutoConfiguration(exclude={MultipartAutoConfiguration.class})
public class WebSpringBoilerplateApplication {
/*@EnableAutoConfiguration(exclude={MultipartAutoConfiguration.class, ErrorMvcAutoConfiguration.class})
public class WebSpringBoilerplateApplication extends SpringBootServletInitializer{*/

/*
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(WebSpringBoilerplateApplication.class);
	}
*/

	public static void main(String[] args) {
		SpringApplication.run(WebSpringBoilerplateApplication.class, args);
	}

	@Component
	public static class CustomServletContainer implements EmbeddedServletContainerCustomizer {

		@Override
		public void customize(ConfigurableEmbeddedServletContainer container) {
			container.setPort(8080);
			container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/index.html"));
			container.setSessionTimeout(10, TimeUnit.MINUTES);
		}

	}

	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {

		return (container -> {
			ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/index.html");
			ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/index.html");
			ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/index.html");

			container.addErrorPages(error401Page, error404Page, error500Page);
		});
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
