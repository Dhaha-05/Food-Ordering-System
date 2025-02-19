package com.filter;

import javax.servlet.*;
import java.io.IOException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutFilter implements Filter
{
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        if ("/logout".equals(path)) {
            HttpSession session = httpRequest.getSession(false);

            if (session != null) {
                session.invalidate();
                Cookie[] cookies = httpRequest.getCookies();
                if(cookies!=null)
                    removeCookies(httpRequest,httpResponse,cookies);
            }

            httpResponse.sendRedirect("/WebProject/home-page.html");
        } else {
            chain.doFilter(request, response);
        }
    }
    private void removeCookies(HttpServletRequest req, HttpServletResponse res, Cookie[] cookies) {

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName()) || "name".equals(cookie.getName()) || "role".equals(cookie.getName()) || "password".equals(cookie.getName())) {
                    System.out.println(cookie.getName() +" : "+cookie.getValue());
                    cookie.setValue("");
                    cookie.setMaxAge(-1);
                    res.addCookie(cookie);
                    System.out.println(cookie.getName() +" : "+cookie.getValue() + "--> after clearing!!!");
                }
            }
        }
    }
}

