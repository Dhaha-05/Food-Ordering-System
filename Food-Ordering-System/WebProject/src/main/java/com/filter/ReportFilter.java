package com.filter;

import com.google.gson.JsonObject;
import com.util.EncryptDecrypt;
import com.util.JsonUtil;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;

public class ReportFilter implements Filter
{
    HashSet<String> url = new HashSet<>();
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        Collections.addAll(url,"/","/login","/login.html","/js/login.js","/css/login.css","/register",
                "/register.html","/css/register.css","/js/register.js","/home-page.html","/css/home-page.css",
                "/js/home-page.js","/foodapp/restaurants","/foodapp/purchase/","/logout","/foodapp/food-items","/css/add-restaurant.css","/js/add-restaurant.js","/add-restaurant.html");

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String path = request.getRequestURI().substring(request.getContextPath().length());
        System.out.println("request path : "+path);
        HttpSession session = request.getSession(false);
        JsonObject jsonResponse = new JsonObject();
        Cookie[] cookies = request.getCookies();
        JsonObject jsonObject;
        //boolean isValidUrl = true;

//        if(path.equals("/login.html") || path.equals("/register.html"))
//        {
//            if(session!=null)
//            {
//                response.sendRedirect("/home-page.html");
//                return;
//            }
//        }

        if(path.equals("/foodapp/restaurants"))
        {
            String method = request.getMethod();
            if(method.equalsIgnoreCase("POST"))
            {
                if(session==null || cookies==null)
                {
                    sendError(jsonResponse,"error",403,"Access Denied for this user",response);
                    return;
                }
                else
                {
                    if(hasPermission(session,cookies))
                    {
                        chain.doFilter(req,res);
                        return;
                    }
                    else {
                        sendError(jsonResponse,"error",403,"Access Denied for this user",response);
                        return;
                    }
                }
            }
            else {
                chain.doFilter(req,res);
                return;
            }
        }

        if(path.startsWith("/foodapp/restaurants/"))
        {
            String[] paths = request.getPathInfo().split("/");
            if(paths.length==1 || paths.length==0)
            {
                sendError(jsonResponse,"error",400,"Bad Request, Invalid URL",response);
                return;
            }
            try
            {
                int userid = Integer.parseInt(paths[1]);
                String action = paths[2];
                switch(action)
                {
                    case "food-items":
                        chain.doFilter(req,res);
                        return;
                    default:
                        sendError(jsonResponse,"error",400,"Bad Request, Invalid URL",response);
                        return;
                }
            }
            catch (NumberFormatException e)
            {
                sendError(jsonResponse,"error",400,"Bad Request, Invalid URL path parameter",response);
                return;
            }catch (Exception e)
            {
                sendError(jsonResponse,"error",400,"Bad Request, Invalid URL and Exception : "+e.getMessage(),response);
                return;
            }
        }

        else if(path.startsWith("/foodapp/purchase/"))
        {
            String pathInfo = request.getPathInfo();
            if(pathInfo!=null)
            {
                String[] paths = request.getPathInfo().split("/");
                try
                {
                    int userid = Integer.parseInt(paths[1]);
                    String action = paths[2];
                    String method="";
                    switch(action)
                    {
                        case "cart-items":
                            if(session==null)
                            {
                                response.sendRedirect("/WebProject/login.html");
                            }
                            method = request.getMethod();
                            System.out.println(method);
                            if(method.equalsIgnoreCase("POST"))
                            {
                                try {
                                    jsonObject = JsonUtil.readJson(request);
                                } catch (Exception e) {
                                    System.out.println("Inside purchase cart JSON error");
                                    sendError(jsonResponse,"failed",400,"Invalid JSON input",response);
                                    return;
                                }
                                if (!jsonObject.has("itemid") || !jsonObject.has("quantity") || !jsonObject.has("totalprice") || !jsonObject.has("billamount")) {
                                    sendError(jsonResponse,"failed",400,"Input Data missing in JSON request",response);
                                    return;
                                }

                                int itemid = jsonObject.get("itemid").getAsInt();
                                int  quantity = jsonObject.get("quantity").getAsInt();
                                double totalPrice = jsonObject.get("totalprice").getAsDouble();
                                double billamount =jsonObject.get("billamount").getAsDouble();

                                if(itemid==0 && quantity==0 && totalPrice==0 && billamount==0)
                                {
                                    sendError(jsonResponse,"failed",400,"Input Data missing in JSON request",response);
                                    return;
                                }
                                request.setAttribute("param",jsonObject);
                                chain.doFilter(req,res);
                                return;
                            }
                            else
                            {
                                chain.doFilter(req,res);
                                return;
                            }

                        case "orders":
                            if(session==null)
                            {
                                response.sendRedirect("/WebProject/login.html");
                            }
                            method = request.getMethod();
                            System.out.println(method);
                            if(method.equalsIgnoreCase("POST"))
                            {
                                try {
                                    jsonObject = JsonUtil.readJson(request);
                                } catch (Exception e) {
                                    System.out.println("Inside purchase order JSON error");
                                    sendError(jsonResponse,"failed",400,"Invalid JSON input",response);
                                    return;
                                }

                                if (!jsonObject.has("cartid") || !jsonObject.has("userid") || !jsonObject.has("itemid") || !jsonObject.has("quantity") || !jsonObject.has("totalPrice")) {
                                    sendError(jsonResponse,"failed",400,"Input Data missing in JSON request",response);
                                    return;
                                }
                                int cartid = jsonObject.get("cartid").getAsInt();
                                int id = jsonObject.get("userid").getAsInt();
                                int itemid = jsonObject.get("itemid").getAsInt();
                                int  quantity = jsonObject.get("quantity").getAsInt();
                                double totalPrice = jsonObject.get("totalPrice").getAsDouble();

                                if(cartid==0 && id==0 && itemid==0 && quantity==0 && totalPrice==0)
                                {
                                    sendError(jsonResponse,"failed",400,"Input Data missing in JSON request",response);
                                    return;
                                }
                                request.setAttribute("param",jsonObject);
                                chain.doFilter(req,res);

                            }
                            else {
                                chain.doFilter(req,res);
                                return;
                            }

                        default:
                            sendError(jsonResponse,"error",400,"Bad Request, Invalid URL",response);
                            return;
                    }
                }
                catch (NumberFormatException e)
                {
                    sendError(jsonResponse,"failed",400,"Bad Request, Invalid URL path parameter",response);
                    return;
                } catch (Exception e) {
                    sendError(jsonResponse,"failed",400,"Bad Request, Invalid URL and Exception : "+e.getMessage(),response);
                    return;
                }
            }
            else {

                sendError(jsonResponse,"error",400,"Bad Request, Invalid URL",response);
                return;
            }

        }

        else if(path.equals("/login"))
        {
            try {
                jsonObject = JsonUtil.readJson(request);
                System.out.println("Json Object : "+ jsonObject);
            } catch (Exception e) {
                System.out.println("Login failed 1");
                sendError(jsonResponse,"failed",400,"Invalid JSON input",response);
                return;
            }

            if (!jsonObject.has("username") || !jsonObject.has("password")) {
                System.out.println("login failed 2");
                sendError(jsonResponse,"failed",400,"Username and password are required",response);
                return;
            }

            String username = jsonObject.get("username").getAsString();
            String password = jsonObject.get("password").getAsString();

            if(isValidUsername(username))
            {
                System.out.println("Login passed");
                request.setAttribute("param",jsonObject);
                chain.doFilter(req,res);
                System.out.println("After chain req passed");
                return;
            }
            else
            {
                System.out.println("Login failed 3");
                sendError(jsonResponse,"failed",200,"Enter Valid Username",response);
                return;
            }

        }

        else if(path.equals("/register"))
        {
            try {
                jsonObject = JsonUtil.readJson(request);
            } catch (Exception e) {
                System.out.println("Inside register JSON error");
                  sendError(jsonResponse,"failed",400,"Bad Request, Invalid Json input",response);
                return;
            }

            if (!jsonObject.has("username") || !jsonObject.has("password") || !jsonObject.has("fullname") || !jsonObject.has("email") || !jsonObject.has("mobileno") || !jsonObject.has("role")) {
                sendError(jsonResponse,"failed",400,"Missing Input Fields",response);
                return;
            }

            String username = jsonObject.get("username").getAsString();
            String password = jsonObject.get("password").getAsString();
            String fullname = jsonObject.get("fullname").getAsString();
            String email = jsonObject.get("email").getAsString();
            String mobileno = jsonObject.get("mobileno").getAsString();
            String role = jsonObject.get("role").getAsString();
            if(session!=null)
            {
                response.sendRedirect("/WebProject/home-page.html");
                return;
            }

            if(isValidUsername(username) && isValidEmail(email) && isValidMobileno(mobileno) && isValidRole(role) && !password.isEmpty())
            {
                request.setAttribute("param",jsonObject);
                chain.doFilter(req,res);
                return;
            }
            else
            {
                sendError(jsonResponse,"failed",400,"Invalid Inputs",response);
                return;
            }
        }

        else if(url.contains(path))
        {
            chain.doFilter(req,res);
            return;
        }
        else
        {
            sendError(jsonResponse,"error",400,"Bad Request, Invalid URL",response);
            return;
        }


    }
    private boolean isValidUsername(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        return username.matches("^[a-zA-Z0-9]{3,15}$");
    }

    private boolean isValidEmail(String email)
    {
        if(email==null || email.isEmpty())
        {
            return false;
        }
        return email.matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$");
    }

    private boolean isValidMobileno(String mobileno)
    {
        if(mobileno==null || mobileno.isEmpty())
        {
            return false;
        }
        return mobileno.matches("\\d{10}$");
    }

    private boolean isValidRole(String role)
    {
        if(role==null || role.isEmpty())
        {
            return false;
        }
        return role.equals("customer") || role.equals("manager");
    }

    private void sendError(JsonObject jsonResponse,String status,int code, String message,HttpServletResponse response) throws IOException {
        jsonResponse = new JsonObject();
        jsonResponse.addProperty("status",status);
        jsonResponse.addProperty("code",code);
        jsonResponse.addProperty("message",message);
        JsonUtil.writeJson(response,jsonResponse);
    }

    private boolean hasPermission(HttpSession session, Cookie[] cookies)
    {
        String userid = "";
        String password="";
        for(Cookie cookie : cookies)
        {
            if ("userid".equals(cookie.getName())){
                userid = cookie.getValue();
            }
            else if("password".equals(cookie.getName())){
                password = cookie.getValue();
            }
        }
        boolean isAuthorized = false;
        if(userid.equals((String) session.getAttribute("userid")) && password.equals((String) session.getAttribute("password")))
        {
            isAuthorized = "manager".equals(EncryptDecrypt.decrypt((String) session.getAttribute("role")));
        }
        return isAuthorized;
    }
}





