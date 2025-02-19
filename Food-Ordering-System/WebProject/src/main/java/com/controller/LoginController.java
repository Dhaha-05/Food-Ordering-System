package com.controller;

import com.google.gson.JsonParser;
import com.util.EncryptDecrypt;
import com.util.JsonUtil;
import com.model.User;
import com.service.UserService;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
//import javax.servlet.http.Cookie;
import java.util.logging.Logger;

public class LoginController extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UserService();

    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        JsonObject jsonResponse = new JsonObject();
        JsonObject jsonObject;

        try {

            jsonObject = (JsonObject) req.getAttribute("param");

        } catch (Exception e) {

            jsonResponse.addProperty("status", "failed");
            jsonResponse.addProperty("message", "Invalid JSON input");
            JsonUtil.writeJson(res, jsonResponse);
            return;
        }

        if (!jsonObject.has("username") || !jsonObject.has("password")) {
            jsonResponse.addProperty("status", "failed");
            jsonResponse.addProperty("message", "Username and password are required");
            JsonUtil.writeJson(res, jsonResponse);
            return;
        }

        String username = jsonObject.get("username").getAsString();
        String password = jsonObject.get("password").getAsString();
        System.out.println(username + " : "+ password + " This is Sensitive");
        User user = userService.login(username, password);
        System.out.println("Login Controller  : " + user);

        if (user != null) {
            req.getSession().setAttribute("userid", EncryptDecrypt.encrypt(String.valueOf(user.getId())));
            req.getSession().setAttribute("name", EncryptDecrypt.encrypt(user.getFullName()));
            req.getSession().setAttribute("password",EncryptDecrypt.encrypt(user.getPassword()));
            req.getSession().setAttribute("role", EncryptDecrypt.encrypt(user.getRole()));
            addCookie(req,res);
            jsonResponse.addProperty("status", "success");
        } else {
            jsonResponse.addProperty("status", "failed");
        }

        JsonUtil.writeJson(res, jsonResponse);
    }

    private void addCookie(HttpServletRequest req, HttpServletResponse res)
    {
        HttpSession session = req.getSession(false);
        Cookie cookie = new Cookie("userid", String.valueOf(session.getAttribute("userid")));
        res.addCookie(cookie);
        cookie = new Cookie("name",String.valueOf(session.getAttribute("name")));
        res.addCookie(cookie);
        cookie = new Cookie("password",String.valueOf(session.getAttribute("password")));
        res.addCookie(cookie);
        cookie = new Cookie("role",String.valueOf(session.getAttribute("role")));
        res.addCookie(cookie);
    }

}
