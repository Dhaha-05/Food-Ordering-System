//package com.foodapp.interceptor;
//
//import com.foodapp.model.User;
//import com.opensymphony.xwork2.ActionInvocation;
//import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
//import org.apache.struts2.ServletActionContext;
//import org.apache.struts2.dispatcher.HttpParameters;
//import org.apache.struts2.interceptor.SessionAware;
//
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//public class ValidateInterceptor extends AbstractInterceptor implements SessionAware {
//
//    private Map<String, Object> session;
//
//    @Override
//    public String intercept(ActionInvocation invocation) throws Exception {
//        HttpServletRequest request = ServletActionContext.getRequest();
//        HttpServletResponse response = ServletActionContext.getResponse();
//        String actionName = invocation.getProxy().getActionName();
//        HttpParameters parameters = invocation.getInvocationContext().getParameters();
//
//        if (actionName.equals("login") || actionName.equals("register") || actionName.equals("logout")) {
//            return invocation.invoke();
//        }
//
//        if (actionName.equals("food") || actionName.equals("restaurants") || actionName.equals("restaurantFood") || actionName.equals("purchase")) {
//            if (!validateSessionAndCookies()) {
//                return handleUnauthorizedAccess(response, "Unauthorized access: Session or cookies invalid");
//            }
//
//            if (actionName.equals("food")) {
//                String actionParam = String.valueOf(parameters.get("action"));
//                if (actionParam != null && (actionParam.equals("remove-item"))) {
//                    if (!validateAuthorization("manager", "admin")) {
//                        return handleUnauthorizedAccess(response, "Unauthorized access: Role not allowed");
//                    }
//                }
//            }
//
//            if (actionName.equals("restaurants")) {
//                String actionParam = String.valueOf(parameters.get("action"));
//                if (actionParam != null && (actionParam.equals("remove-restaurant"))) {
//                    if (!validateAuthorization("manager", "admin")) {
//                        return handleUnauthorizedAccess(response, "Unauthorized access: Role not allowed");
//                    }
//                }
//            }
//        }
//
//        if (actionName.equals("newRestaurant") || actionName.equals("foodItem")) {
//            if (!validateSessionAndCookies()) {
//                return handleUnauthorizedAccess(response, "Unauthorized access: Session or cookies invalid");
//            }
//            if (!validateAuthorization("manager", "admin")) {
//                return handleUnauthorizedAccess(response, "Unauthorized access: Role not allowed");
//            }
//        }
//
//        return invocation.invoke();
//    }
//
//    private boolean validateSessionAndCookies() {
//
//        User user = (User) session.get("user");
//        if (user == null) {
//            return false;
//        }
//
//        // Validate cookies (e.g., check if cookies match session data)
//        HttpServletRequest request = ServletActionContext.getRequest();
//        Cookie[] cookies = request.getCookies();
//        if (cookies == null) {
//            return false;
//        }
//
//        for (Cookie cookie : cookies) {
//            if (cookie.getName().equals("userid") && !cookie.getValue().equals(session.get("userid"))) {
//                return false;
//            }
//        }
//
//        return true;
//    }
//
//    private boolean validateAuthorization(String... allowedRoles) {
//        User user = (User) session.get("user");
//        if (user == null) {
//            return false;
//        }
//
//        String userRole = user.getRole();
//        for (String role : allowedRoles) {
//            if (userRole.equals(role)) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    private String handleUnauthorizedAccess(HttpServletResponse response, String message) {
//        // Return a JSON response for unauthorized access
//        Map<String, Object> jsonResponse = new HashMap<>();
//        jsonResponse.put("status", "failed");
//        jsonResponse.put("message", message);
//
//        response.setContentType("application/json");
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        return "";
//    }
//
//    @Override
//    public void setSession(Map<String, Object> map) {
//
//    }
//}