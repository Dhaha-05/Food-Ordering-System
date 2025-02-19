package com.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class PurchaseFilter implements Filter
{
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        boolean isAuthorized = true;
        if(session!=null)
        {
            String userid = String.valueOf(request.getSession(false).getAttribute("userid"));
            String name = String.valueOf(request.getSession(false).getAttribute("name"));
            String password = String.valueOf(request.getSession(false).getAttribute("password"));
            if (userid == null || userid.isEmpty()) {
             response.sendRedirect("/WebProject/login.html");
             return;
            }
            Cookie[] cookies = request.getCookies();

            if(cookies!=null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("name") && !cookie.getValue().equals(name)) {
                        isAuthorized = false;
                    }
                    if (cookie.getName().equals("userid") && !cookie.getValue().equals(userid)) {
                        isAuthorized = false;
                    }
                    if (cookie.getName().equals("password") && !cookie.getValue().equals(password)) {
                        isAuthorized = false;
                    }
                }
                if (isAuthorized) {
                    chain.doFilter(req, res);
                } else {
                    response.sendRedirect("/WebProject/home-page.html");
                }
            }
        }
        else {
            response.sendRedirect("/WebProject/login.html");
        }
    }
}
