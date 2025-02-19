package com.controller;

import com.util.JsonUtil;
import com.model.FoodItem;
import com.service.FoodItemService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class FoodItemController extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        FoodItemService foodItemService = new FoodItemService();
        List<FoodItem> foodItems = foodItemService.getAllFoodItems();
        JsonObject jsonResponse = new JsonObject();
        JsonArray foodItemsArray = new JsonArray();
        for (FoodItem item : foodItems) {
            JsonObject itemJson = new JsonObject();
            itemJson.addProperty("id", item.getId());
            itemJson.addProperty("name", item.getName());
            itemJson.addProperty("item",item.getItem());
            itemJson.addProperty("price", item.getPrice());
            itemJson.addProperty("category", item.getCategory());
            itemJson.addProperty("rating", item.getRating());
            itemJson.addProperty("offer", item.getOffer());
            foodItemsArray.add(itemJson);
        }

        jsonResponse.add("foodItems", foodItemsArray);
        JsonUtil.writeJson(res, jsonResponse);
    }
}
