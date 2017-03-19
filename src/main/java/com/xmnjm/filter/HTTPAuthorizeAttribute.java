package com.xmnjm.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmnjm.jwt.Audience;
import com.xmnjm.jwt.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jilion.chen on 3/18/2017.
 */
public class HTTPAuthorizeAttribute implements Filter{
    @Autowired
    private Audience audienceEntity;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                filterConfig.getServletContext());

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Map<String, Object> result = new HashMap<>();
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestUri = httpRequest.getRequestURI();
        String excludeUri = audienceEntity.getExcludeUri();
        if (requestUri.indexOf(excludeUri) > -1) {
            chain.doFilter(request, response);
            return;
        }
        if ("OPTIONS".equals(httpRequest.getMethod())) {
            chain.doFilter(request, response);
            return;
        }
        String auth = httpRequest.getHeader("Authorization");
        if (auth != null) {
            if (JwtHelper.parseJWT(auth, audienceEntity.getBase64Secret()) != null) {
                chain.doFilter(request, response);
                return;
            }
        }


        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json; charset=utf-8");
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ObjectMapper mapper = new ObjectMapper();

        result.put("token", null);
        httpResponse.getWriter().write(mapper.writeValueAsString(result));
        return;
    }

    @Override
    public void destroy() {
    }
}
