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
    private String username = null;
    private String password = null;
    private String role = null;
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
        System.out.println("Path : "+path);
        if(path.equals("/login.html") || path.equals("/register.html"))
        {
            System.out.println("Login or Register page called");
            if(session!=null && cookies!=null)
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
            authUser(session,cookies);
            if(hasManagerPermission())
                chain.doFilter(request,response);
            else
                res.setStatus(403);
        }
        else if(path.equals("/add-restaurant.html") || path.equals("/remove-restaurant.html"))
        {
            authUser(session,cookies);
            if(hasAdminPermission())
                chain.doFilter(request, response);
            else
                res.setStatus(403);
        }
        else if(url.contains(path))
        {
            chain.doFilter(request,response);
        }
        else {
            res.setStatus(404);
        }
    }

    private boolean authUser(HttpSession session, Cookie[] cookies)
    {
        if(session==null || cookies==null)
            return false;
        for (Cookie cookie : cookies)
        {
            if(cookie.getName().equals("name"))
            {
                username = cookie.getValue();
            }
            else if(cookie.getName().equals("password")){
                password = cookie.getValue();
            }
            else if(cookie.getName().equals("role"))
            {
                role = cookie.getValue();
            }
        }
        if(username.equals(session.getAttribute("name")) && password.equals(session.getAttribute("password")) && role.equals(session.getAttribute("role")))
        {
            return true;
        }
        return false;
    }

    private boolean hasManagerPermission()
    {
        return role.equals(EncryptDecrypt.encrypt("manager")) || role.equals(EncryptDecrypt.encrypt("admin"));
    }

    private boolean hasAdminPermission()
    {
        return role.equals(EncryptDecrypt.encrypt("admin"));
    }
}
