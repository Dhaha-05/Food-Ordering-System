package com.controller;

import com.google.gson.JsonObject;
import com.util.JsonUtil;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;


public class LogoutController extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession(false);
        Cookie[] cookies = req.getCookies();
        if(session!=null)
        {
            session.invalidate();
        }
        if(cookies!=null)
        {
            removeCookies(req, res);
        }
        JsonObject obj = new JsonObject();
        obj.addProperty("status", "success");
        obj.addProperty("message", "Logged out successfully");
        JsonUtil.writeJson(res, obj);
    }
    private void removeCookies(HttpServletRequest req, HttpServletResponse res) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName()) || "name".equals(cookie.getName()) || "role".equals(cookie.getName()) || "password".equals(cookie.getName())) {
                    System.out.println(cookie.getName() +" : "+cookie.getValue());
                    cookie.setValue("");
                    cookie.setMaxAge(-1);
                    res.addCookie(cookie);
                }
            }
        }
    }
}






