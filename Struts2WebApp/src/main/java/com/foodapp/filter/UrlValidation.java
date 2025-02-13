package com.foodapp.filter;

import com.util.cipher.EncryptDecrypt;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UrlValidation implements Filter
{
    private Set<String> url = new HashSet<>();
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        Collections.addAll(url,"/","/logout","/loginPage","/registerPage","/index.jsp","/login.html","/register.html","/dashboard.html","/manage-restaurant.html","/add-item.html","/remove-item.html","/add-restaurant.html","/remove-restaurant.html","/css/add-item.css","/css/add-restaurant.css","/css/dashboard.css","/css/login.css","/css/manage-restaurant.css","/css/register.css","/css/remove-item.css","/css/remove-restaurant.css","/js/add-item.js","/js/add-restaurant.js","/js/dashboard.js","/js/login.js","/js/manage-restaurant.js","/js/register.js","/js/remove-item.js","/js/remove-restaurant.js");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        Cookie[] cookies = req.getCookies();
        String path = req.getRequestURI().substring(req.getContextPath().length());
        System.out.println("-------------------------------------------------------------Path : "+path+"----------------------------");

        System.out.println("Authenticated : "+AuthFilter.isAuthenticatedUser());
        System.out.println("Has Manager Access : "+AuthFilter.hasManagerAccess());
        System.out.println("Has Admin Access : "+AuthFilter.hasAdminAccess());
        if(path.equals("/login.html") || path.equals("/register.html") || path.equals("/loginPage") || path.equals("/registerPage"))
        {
            System.out.println("Login or Register page called");
            if(AuthFilter.isAuthenticatedUser())
            {
                System.out.println("Some user still logged in and try to login or register operation");
                res.sendRedirect(req.getContextPath()+"/dashboard.html");
                return;
            }
            else
            {
                chain.doFilter(request,response);
            }
        }
        else if(path.equals("/dashboard.html") || path.endsWith(".action"))
        {
            chain.doFilter(request,response);
            return;
        }
        else if((path.startsWith("/js/") || path.startsWith("/css/"))){
            if(url.contains(path))
            {
                chain.doFilter(request, response);
            }
            else {
                res.setStatus(404);
                return;
            }
        }
        else if(path.equals("/manage-restaurant.html") || path.equals("/add-item.html") || path.equals("/remove-item.html"))
        {
            if(AuthFilter.hasManagerAccess()) {
                chain.doFilter(request, response);
                return;
            }
            else {
                res.setStatus(403);
                return;
            }
        }
        else if(path.equals("/add-restaurant.html") || path.equals("/remove-restaurant.html"))
        {
            if(AuthFilter.hasAdminAccess()) {
                chain.doFilter(request, response);
                return;
            }
            else {
                res.setStatus(403);
                return;
            }
        }
        else if(url.contains(path))
        {
            chain.doFilter(request,response);
            return;
        }
        else {
            res.setStatus(404);
            return;
        }

    }
}
