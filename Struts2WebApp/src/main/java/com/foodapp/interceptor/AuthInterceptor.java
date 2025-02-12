package com.foodapp.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.util.cipher.EncryptDecrypt;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AuthInterceptor extends AbstractInterceptor {

    private Map<String, Object> jsonResponse;

    public Map<String, Object> getJsonResponse() {
        return jsonResponse;
    }

    public void setJsonResponse(Map<String, Object> jsonResponse) {
        this.jsonResponse = jsonResponse;
    }

    @Override
    public void init() {
        jsonResponse = new HashMap<>();
        super.init();
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        Map<String, Object> session = invocation.getInvocationContext().getSession();

        String actionName = invocation.getProxy().getActionName();
        System.out.println("Action name : " + actionName);
//        String method = request.getMethod();

        if (actionName.equals("loginPage") || actionName.equals("registerPage") || actionName.equals("login") || actionName.equals("register") || actionName.equals("logout") || actionName.equals("restaurantFood")) {
            return invocation.invoke();
        }
        if (actionName.equals("food")) {
            String action = request.getParameter("action");
            if (action != null && (action.equals("all-items") || action.equals("restaurant-items"))) {
                return invocation.invoke();
            }
        }
        if (actionName.equals("restaurants")) {
            String action = request.getParameter("action");
            if (action != null && (action.equals("all-restaurant") || action.equals("restaurant"))) {
                return invocation.invoke();
            }
        }

        if (session == null || session.isEmpty()) {
            System.out.println("Session is Empty");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            request.setAttribute("errorStatus", 401);
            request.setAttribute("errorMessage", "Unauthorized access");
            return "unauthorized";
        }

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            System.out.println("Cookies null unauthorized");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            request.setAttribute("errorStatus", 401);
            request.setAttribute("errorMessage", "Unauthorized access");
            return "unauthorized";
        }

        String username = null;
        String role = null;
        String password = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("name")) {
                username = EncryptDecrypt.decrypt(cookie.getValue());
            }
            if (cookie.getName().equals("role")) {
                role = EncryptDecrypt.decrypt(cookie.getValue());
            }
            if (cookie.getName().equals("password")) {
                password = cookie.getValue();
            }
        }

        if (username == null || role == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            request.setAttribute("errorStatus", 401);
            request.setAttribute("errorMessage", "Unauthorized access");
            return "unauthorized";
        }

        if (actionName.equals("food")) {
            String action = request.getParameter("action");
            if (action != null && action.equals("remove-item")) {
                if (!username.equals(EncryptDecrypt.decrypt(String.valueOf(session.get("name")))) && !Objects.equals(password, String.valueOf(session.get("password")))) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    request.setAttribute("errorStatus", 401);
                    request.setAttribute("errorMessage", "Unauthorized access");
                    return "unauthorized";
                } else if (!role.equals("admin") && !role.equals("manager")) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    request.setAttribute("errorStatus", 403);
                    request.setAttribute("errorMessage", "Access Denied");
                    return "forbidden";
                }
            }
        } else if (actionName.equals("restaurants")) {
            String action = request.getParameter("action");
            if (action != null && action.equals("remove-restaurant")) {
                if (!username.equals(EncryptDecrypt.decrypt(String.valueOf(session.get("name")))) && !Objects.equals(password, session.get("password"))) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    request.setAttribute("errorStatus", 401);
                    request.setAttribute("errorMessage", "Unauthorized access");
                    return "unauthorized";
                } else if (!role.equals("admin")) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    request.setAttribute("errorStatus", 403);
                    request.setAttribute("errorMessage", "Access Denied");
                    return "forbidden";
                }
            }
        } else if (actionName.equals("foodItem")) {
            if (!username.equals(EncryptDecrypt.decrypt(String.valueOf(session.get("name")))) && !Objects.equals(password, String.valueOf(session.get("password")))) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                request.setAttribute("errorStatus", 401);
                request.setAttribute("errorMessage", "Unauthorized access");
                return "unauthorized";
            } else if (!role.equals("admin") && !role.equals("manager")) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                request.setAttribute("errorStatus", 403);
                request.setAttribute("errorMessage", "Access Denied");
                return "forbidden";
            }
        } else if (actionName.equals("newRestaurant")) {
            if (!username.equals(EncryptDecrypt.decrypt(String.valueOf(session.get("name")))) && !Objects.equals(password, String.valueOf(session.get("password")))) {
                System.out.println("New Restaurant unauthorized");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                request.setAttribute("errorStatus", 401);
                request.setAttribute("errorMessage", "Unauthorized access");
                return "unauthorized";
            } else if (!role.equals("admin")) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                request.setAttribute("errorStatus", 403);
                request.setAttribute("errorMessage", "Access Denied");
                return "forbidden";
            }
        }

        return invocation.invoke();
    }

//    private void pageError(String status, int statusCode, String message) {
//        jsonResponse.clear();
//        jsonResponse.put("status", status);
//        jsonResponse.put("code", statusCode);
//        jsonResponse.put("message", message);
//        System.out.println("jsonResponse: " + jsonResponse);
//    }
}