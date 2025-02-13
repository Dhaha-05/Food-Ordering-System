package com.foodapp.interceptor;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.struts2.ServletActionContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class FormValidationInterceptor extends AbstractInterceptor {

    private Map<String, Object> jsonResponse = new HashMap<>();

    public Map<String, Object> getJsonResponse() {
        return jsonResponse;
    }

    public void setJsonResponse(Map<String, Object> jsonResponse) {
        this.jsonResponse = jsonResponse;
    }

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();

        String actionName = invocation.getProxy().getActionName();
        System.out.println("Action name : " + actionName);
        boolean isValid = true;
        if(actionName.equals("login")){
            String username = request.getParameter("userBean.username");
            String password = request.getParameter("userBean.password");
            if(username == null || username.isEmpty())
            {
                isValid = false;
                jsonResponse.put("usernameError", "Username is required");
            }
            if(password==null || password.isEmpty())
            {
                isValid = false;
                jsonResponse.put("passwordError", "Password must required");
            }
        }
        else if (actionName.equals("register")) {
            String username = request.getParameter("userBean.username");
            String email = request.getParameter("userBean.email");
            String mobileNo = request.getParameter("userBean.mobileno");

            if (username == null || username.isEmpty()) {
                isValid =false;
                jsonResponse.put("usernameError", "Username is required");
            }

            if (email == null || email.isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                isValid = false;
                jsonResponse.put("emailError", "Invalid email format");
            }

            if (mobileNo == null || mobileNo.isEmpty() || !mobileNo.matches("\\d{10}")) {
                isValid = false;
                jsonResponse.put("mobileError", "Invalid mobile number");
            }
        } else if (actionName.equals("newRestaurant")) {
            String restaurantName = request.getParameter("restaurant.restaurantname");
            String location = request.getParameter("restaurant.location");
            String rating = request.getParameter("restaurant.rating");

            if (restaurantName == null || restaurantName.isEmpty()) {
                isValid = false;
                jsonResponse.put("restaurantError", "Restaurant name is required");
            }

            if (location == null || location.isEmpty()) {
                isValid = false;
                jsonResponse.put("locationError", "Location is required");
            }

            if (rating == null || rating.isEmpty() || !rating.matches("\\d+(\\.\\d+)?")) {
                isValid = false;
                jsonResponse.put("ratingError", "Invalid rating");
            }
        }
        
        if(isValid)
        {
            return invocation.invoke();
        }
        else
        {
            jsonResponse.put("status", "failed");
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new Gson().toJson(jsonResponse));
            response.getWriter().flush();
            return null;
        }
    }
}