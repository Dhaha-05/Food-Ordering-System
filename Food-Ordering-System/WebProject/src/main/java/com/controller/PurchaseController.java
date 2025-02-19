package com.controller;

import com.util.JsonUtil;
import com.model.CartItem;
import com.model.Order;
import com.service.CartService;
import com.service.OrderService;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PurchaseController extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            JsonObject error = new JsonObject();
            error.addProperty("status", "failed");
            error.addProperty("message", "Invalid URL");
            JsonUtil.writeJson(res, error);
            return;
        }

        String[] pathParts = pathInfo.split("/");
        int userId = Integer.parseInt(pathParts[1]);
        String action = pathParts[2].trim();

        JsonObject jsonResponse = new JsonObject();
        switch (action) {
            case "cart-items":
                CartService cartService = new CartService();
                JsonObject cartResponse = new JsonObject();
                cartResponse.addProperty("status", "success");
                cartResponse.add("items", JsonUtil.toJsonArray(cartService.getCartItems(userId)));
                jsonResponse = cartResponse;
                break;

            case "orders":
                OrderService orderService = new OrderService();
                JsonObject orderResponse = new JsonObject();
                orderResponse.addProperty("status", "success");
                orderResponse.add("items", JsonUtil.toJsonArray(orderService.getOrders(userId)));
                jsonResponse = orderResponse;
                break;

            default:
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Invalid action");
                res.setStatus(404);
                break;
        }

        JsonUtil.writeJson(res, jsonResponse);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            JsonObject error = new JsonObject();
            error.addProperty("status", "failed");
            error.addProperty("message", "Invalid URL");
            JsonUtil.writeJson(res, error);
            return;
        }

        String[] pathParts = pathInfo.split("/");
        int userId = Integer.parseInt(pathParts[1]);
        String action = pathParts[2].trim();

        JsonObject jsonObject = (JsonObject) req.getAttribute("param");
        JsonObject jsonResponse = new JsonObject();

        switch (action) {
            case "cart-items":
                CartItem cartItem = new CartItem();
                cartItem.setUserId(userId);
                cartItem.setItemId(jsonObject.get("itemid").getAsInt());
                cartItem.setQuantity(jsonObject.get("quantity").getAsInt());
                cartItem.setTotalPrice(jsonObject.get("totalprice").getAsDouble());
                cartItem.setBillamount(jsonObject.get("billamount").getAsDouble());

                CartService cartService = new CartService();
                boolean isCartItemAdded = cartService.addCartItem(cartItem);

                if (isCartItemAdded) {
                    jsonResponse.addProperty("status", "success");
                } else {
                    jsonResponse.addProperty("status", "failed");
                }
                break;

            case "orders":
                Order order = new Order();
                order.setUserId(userId);
                order.setItemId(jsonObject.get("itemid").getAsInt());
                order.setQuantity(jsonObject.get("quantity").getAsInt());
                order.setTotalPrice(jsonObject.get("totalPrice").getAsDouble());
                order.setCartId(jsonObject.get("cartid").getAsInt());

                OrderService orderService = new OrderService();
                boolean isOrderPlaced = orderService.placeOrder(order);

                if (isOrderPlaced) {
                    jsonResponse.addProperty("status", "success");
                } else {
                    jsonResponse.addProperty("status", "failed");
                }
                break;

            default:
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Invalid action");
                res.setStatus(404);
                break;
        }

        JsonUtil.writeJson(res, jsonResponse);
    }
}