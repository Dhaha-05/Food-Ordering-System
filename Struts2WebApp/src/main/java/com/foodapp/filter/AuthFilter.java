package com.foodapp.filter;

import com.util.cipher.EncryptDecrypt;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthFilter implements Filter
{
    private static ThreadLocal<Boolean> isAuthenticated = ThreadLocal.withInitial(()->false);
    private static ThreadLocal<Boolean> isManager = ThreadLocal.withInitial(()->false);
    private static ThreadLocal<Boolean> isAdmin = ThreadLocal.withInitial(()->false);
    private String username = null;
    private String password = null;
    private String role = null;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        Cookie[] cookies = req.getCookies();
        isAuthenticated.set(authUser(session,cookies));
        if(isAuthenticated.get())
        {
            isManager.set(hasManagerPermission());
            isAdmin.set(hasAdminPermission());
        }
        else {
            isManager.set(false);
            isAdmin.set(false);
        }
        System.out.println("Inside auth Filter");
        System.out.println("Authentication : "+isAuthenticated.get());
        System.out.println("manager : "+isManager.get());
        System.out.println("admin : "+isAdmin.get());
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
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
        if(username==null || password==null || role==null)
            return false;
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

    public static boolean isAuthenticatedUser()
    {
        return isAuthenticated.get();
    }

    public static boolean hasManagerAccess()
    {
        return isManager.get();
    }
    public static boolean hasAdminAccess()
    {
        return isAdmin.get();
    }
}
