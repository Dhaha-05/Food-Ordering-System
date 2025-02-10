package com.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.util.JsonUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UserInfoController extends HttpServlet
{
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession(false);
        JsonObject status = new JsonObject();
        if(session!=null)
        {
            String userId = String.valueOf(session.getAttribute("userid"));
            String fullname = (String) session.getAttribute("fullname");

            status.addProperty("status","success");
            status.addProperty("userid",userId);
            status.addProperty("fullname",fullname);
            JsonUtil.writeJson(res,status);
        }
        else {
            status.addProperty("status","failed");
            JsonUtil.writeJson(res,status);
        }
    }
}
