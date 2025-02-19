package com.controller;

import com.util.EncryptDecrypt;
import com.util.JsonUtil;
import com.model.User;
import com.service.UserService;
import com.google.gson.JsonObject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

public class RegisterController extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        JsonObject jsonObject = (JsonObject) req.getAttribute("param");
        User user = new User();
        user.setUsername(jsonObject.get("username").getAsString());
        user.setFullName(jsonObject.get("fullname").getAsString());
        user.setEmail(jsonObject.get("email").getAsString());
        user.setMobileNo(jsonObject.get("mobileno").getAsString());
        user.setPassword(jsonObject.get("password").getAsString());
        user.setRole(jsonObject.get("role").getAsString());

        UserService userService = new UserService();
        boolean isRegistered = userService.register(user);

        JsonObject jsonResponse = new JsonObject();
        if (isRegistered) {
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
