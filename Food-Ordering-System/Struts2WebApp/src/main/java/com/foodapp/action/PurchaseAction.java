package com.foodapp.action;

import com.foodapp.model.Cart;
import com.foodapp.model.Order;
import com.foodapp.service.CartService;
import com.foodapp.service.OrderService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.HashMap;

public class PurchaseAction extends ActionSupport implements ServletResponseAware, ServletRequestAware {
    private Map<String, Object> jsonResponse = new HashMap<>();
    private int userId;
    private String action;
    private Cart cartItem;
    private Order order;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public String execute() {
        if (action == null || action.isEmpty()) {
            jsonResponse.put("status", "failed");
            jsonResponse.put("message", "Invalid action");
            return NONE;
        }

        String method = request.getMethod();

        if(method.equalsIgnoreCase("POST"))
        {
            switch (action)
            {
                case "cart-items":
                    return addCartItem();
                case "orders":
                    System.out.println("Inside POST order request.....");
                    return placeOrder();
                default:
                    jsonResponse.put("status", "error");
                    jsonResponse.put("message", "Invalid action");
                    return NONE;
            }

        }
        else if(method.equalsIgnoreCase("GET"))
        {
            switch (action) {
                case "cart-items":
                    CartService cartService = new CartService();
                    jsonResponse.put("status", "success");
                    jsonResponse.put("items", cartService.getCartItems(userId));
                    return SUCCESS;

                case "orders":
                    OrderService orderService = new OrderService();
                    jsonResponse.put("status", "success");
                    jsonResponse.put("items", orderService.getOrders(userId));
                    return SUCCESS;

                default:
                    jsonResponse.put("status", "error");
                    jsonResponse.put("message", "Invalid action");
                    return NONE;
            }
        }
        return NONE;
    }

    public String addCartItem() {
        CartService cartService = new CartService();
        boolean isCartItemAdded = cartService.addCartItem(cartItem);
        if (isCartItemAdded) {
            jsonResponse.put("status", "success");
            jsonResponse.put("message","Successfully added to the cart");
            return SUCCESS;
        } else {
            jsonResponse.put("status", "failed");
            jsonResponse.put("message","Selected item is not added to the cart");
            return NONE;
        }
    }

    public String placeOrder() {
        System.out.println("Inside placeOrder");
        OrderService orderService = new OrderService();
        boolean isOrderPlaced = orderService.placeOrder(order);
        if (isOrderPlaced) {
            jsonResponse.put("status", "success");
            jsonResponse.put("message","Order placed Successfully");
            return SUCCESS;
        } else {
            jsonResponse.put("status", "failed");
            jsonResponse.put("message","Order failed!!!");
            return NONE;
        }
    }

    public Map<String, Object> getJsonResponse() {
        return jsonResponse;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Cart getCartItem() {
        return cartItem;
    }

    public void setCartItem(Cart cartItem) {
        this.cartItem = cartItem;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response = httpServletResponse;
    }
}