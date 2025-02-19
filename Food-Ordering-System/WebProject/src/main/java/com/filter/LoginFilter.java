package com.filter;

import com.google.gson.JsonObject;
import com.util.JsonUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter
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
        String password = req.getParameter("password");
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

}
