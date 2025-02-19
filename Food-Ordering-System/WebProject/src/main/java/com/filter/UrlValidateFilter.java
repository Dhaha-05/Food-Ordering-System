package com.filter;

import com.google.gson.JsonObject;
import com.util.JsonUtil;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UrlValidateFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("Inside Authentication Filter");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI().substring(request.getContextPath().length());
        System.out.println(path);
        if(path.startsWith("/css") || path.startsWith("/js"))
        {
            chain.doFilter(req,res);
            return;
        }
        if (path.equals("/login") || path.equals("/register") || path.equals("/") || path.equals("/login.html") || path.equals("/register.html") || path.equals("/home-page.html") || path.equals("/logout")) {
            System.out.println("Hi there, this is before login");
            chain.doFilter(req, res);
            return;
        }

        System.out.println("No login or register page found");
        if (path.startsWith("/foodapp/")) {
            if(path.equals("/foodapp/food-items"))
            {
                chain.doFilter(req,res);
                return;
            }
            else if(path.equals("/foodapp/restaurants"))
            {
                chain.doFilter(req,res);
                return;
            }
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("userid") != null) {

                if (path.startsWith("/foodapp/purchase/")) {
                    String pathInfo = request.getPathInfo();
                    String[] pathParts = pathInfo.split("/");
                    if (pathParts.length == 3) {
                        try {
                            int userid = Integer.parseInt(pathParts[1]);
                            String action = pathParts[2].trim();
                            if ("cart-items".equals(action) || "orders".equals(action)) {
                                chain.doFilter(req, res);
                            } else {
                                sendErrorPage(request, response,400, "Bad Request.");
                                return;
                            }
                        } catch (NumberFormatException e) {
                            sendErrorPage(request, response,400, "Bad Request.");
                            return;
                        }
                    } else {
                        sendErrorPage(request, response, 400, "Bad Request");
                        return;
                    }
                    return;
                }

                if(path.startsWith("/foodapp/restaurants"))
                {
                    String pathInfo = request.getPathInfo();
                    String[] pathParts = pathInfo.split("/");
                    if (pathParts.length == 3) {
                        try {
                            int userid = Integer.parseInt(pathParts[1]);
                            String action = pathParts[2].trim();
                            if ("food-items".equals(action)) {
                                chain.doFilter(req, res);
                                return;
                            } else {
                                sendErrorPage(request, response,400, "Bad Request.");
                                return;
                            }
                        } catch (NumberFormatException e) {
                            sendErrorPage(request, response,400, "Bad Request.");
                            return;
                        }
                    } else {
                        sendErrorPage(request, response, 400, "Bad Request");
                        return;
                    }
                }
            } else {
                response.sendRedirect("/WebProject/login.html");
                return;
            }
        }

    }

    public void sendErrorPage(HttpServletRequest req, HttpServletResponse res, int code, String message) throws ServletException, IOException {
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("status","error");
        jsonResponse.addProperty("errorCode",code);
        jsonResponse.addProperty("message",message);
        JsonUtil.writeJson(res,jsonResponse);

    }

}
