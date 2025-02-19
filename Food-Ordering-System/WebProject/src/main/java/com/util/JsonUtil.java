package com.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class JsonUtil {
    public static JsonObject readJson(HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        String body = stringBuilder.toString();
        return JsonParser.parseString(body).getAsJsonObject();
    }


    public static void writeJson(HttpServletResponse res, JsonObject jsonObject) throws IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write(jsonObject.toString());
    }

//    public static void writeJsonArray(HttpServletResponse res,JsonArray jsonArray)throws IOException
//    {
//        res.setContentType("application/json");
//        res.setCharacterEncoding("UTF-8");
//        res.getWriter().write(jsonArray.toString());
//    }

    public static JsonArray toJsonArray(List<?> list) {
        Gson gson = new Gson();
        return gson.toJsonTree(list).getAsJsonArray();
    }
}