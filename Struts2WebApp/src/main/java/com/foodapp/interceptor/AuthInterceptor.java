package com.foodapp.interceptor;

import com.foodapp.filter.UrlValidation;
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

    private Map<String, Object> jsonResponse = new HashMap<>();;
    private boolean isAuthenticated;
    public Map<String, Object> getJsonResponse() {
        return jsonResponse;
    }

    public void setJsonResponse(Map<String, Object> jsonResponse) {
        this.jsonResponse = jsonResponse;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        isAuthenticated = UrlValidation.isAuthenticatedUser();
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        Map<String, Object> session = invocation.getInvocationContext().getSession();

        String actionName = invocation.getProxy().getActionName();
        System.out.println("Action name : " + actionName);
//        String method = request.getMethod();
        String action = null;
        switch(actionName)
        {
            case "loginPage":
                return invocation.invoke();
            case "registerPage":
                return invocation.invoke();
            case "login":
                return invocation.invoke();
            case "register":
                return invocation.invoke();
            case "logout":
                return invocation.invoke();
            case "restaurantFood":
                return invocation.invoke();
            case "food":
                action = request.getParameter("action");
                if (action != null && (action.equals("all-items") || action.equals("restaurant-items"))) {
                    return invocation.invoke();
                }
                else if (action != null && action.equals("remove-item")) {
                    if (!isAuthenticated) {
                        errorPage(request,response,401,"Unauthorized access");
                        return "unauthorized";
                    } else if (!UrlValidation.hasManagerAccess()) {
                        errorPage(request,response,403,"Access Denied");
                        return "forbidden";
                    }
                    else {
                        invocation.invoke();
                    }
                }
                else
                {
                    errorPage(request,response,404,"Page Not Found");
                    return "notFound";
                }

            case "restaurants":
                action = request.getParameter("action");
                if (action != null && (action.equals("all-restaurant") || action.equals("restaurant"))) {
                    return invocation.invoke();
                }else if (action != null && action.equals("remove-restaurant")) {
                    if (!isAuthenticated) {
                        errorPage(request,response,401,"Unauthorized access");
                        return "unauthorized";
                    } else if (!UrlValidation.hasAdminAccess()) {
                        errorPage(request,response,403,"Access Denied");
                        return "forbidden";
                    }
                    else{
                        return invocation.invoke();
                    }
                }
                else
                {
                    errorPage(request,response,404,"Page Not Found");
                    return "notFound";
                }

            case "purchase":
                if(!isAuthenticated)
                {
                    errorPage(request,response,401,"Unauthorized access");
                    return "unauthorized";
                }
                else
                {
                    return invocation.invoke();
                }
            case "foodItem":
                if (!isAuthenticated) {
                    errorPage(request,response,401,"Unauthorized access");
                    return "unauthorized";
                } else if (!UrlValidation.hasManagerAccess()) {
                    errorPage(request,response,403,"Access Denied");
                    return "forbidden";
                }
                else
                {
                    invocation.invoke();
                }
            case "newRestaurant":
                if (!isAuthenticated) {
                    System.out.println("New Restaurant unauthorized");
                    errorPage(request,response,401,"Unauthorized access");
                    return "unauthorized";
                } else if (!UrlValidation.hasAdminAccess()) {
                    errorPage(request,response,403,"Access Denied");
                    return "forbidden";
                }
            default:
                errorPage(request,response,404,"File Not Found");
                return "notFound";
        }
    }
    private void errorPage(HttpServletRequest request,HttpServletResponse response , int code , String message)
    {
        response.setStatus(code);
        request.setAttribute("errorStatus", code);
        request.setAttribute("errorMessage", message);
    }
//    private void pageError(String status, int statusCode, String message) {
//        jsonResponse.clear();
//        jsonResponse.put("status", status);
//        jsonResponse.put("code", statusCode);
//        jsonResponse.put("message", message);
//        System.out.println("jsonResponse: " + jsonResponse);
//    }
}