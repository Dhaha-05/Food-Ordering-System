package com.filter;

import com.google.gson.JsonObject;
import com.util.JsonUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RegisterFilter implements Filter
{
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        if(session!=null)
        {
            response.sendRedirect("/WebProject/home-page.html");
            return;
        }

        String username = req.getParameter("username");
        String name = req.getParameter("fullname");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String mobileno = req.getParameter("mobileno");
        String role = req.getParameter("role");
        boolean regexExp = false;
        if(isValidUsername(username))
        {
            regexExp = true;
        }
        if(regexExp)
        {
            chain.doFilter(req,res);
        }
        else
        {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("status","failed");
            JsonUtil.writeJson(response,jsonObject);
        }
    }
    private boolean isValidUsername(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        return username.matches("^[a-zA-Z0-9]{3,15}$");
    }

    private boolean isValidEmail(String email)
    {
        if(email==null || email.isEmpty())
        {
            return false;
        }
        return email.matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$");
    }

    private boolean isValidMobileno(String mobileno)
    {
        if(mobileno==null || mobileno.isEmpty())
        {
            return false;
        }
        return mobileno.matches("\\d{10}$");
    }

    private boolean isValidRole(String role)
    {
        if(role==null || role.isEmpty())
        {
            return false;
        }
        return role.equals("customer") || role.equals("manager");
    }

}
