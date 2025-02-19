package com.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        System.out.println("Inside Authentication Filter");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if(httpRequest.getSession(false)!=null)
        {
            String userid = String.valueOf(httpRequest.getSession(false).getAttribute("userid"));
            String name = String.valueOf(httpRequest.getSession(false).getAttribute("name"));
            String role = String.valueOf(httpRequest.getSession(false).getAttribute("role"));
            String password = String.valueOf(httpRequest.getSession(false).getAttribute("password"));
            if (userid == null || userid.isEmpty()) {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.getWriter().write("{\"status\":\"failed\", \"message\":\"Unauthorized access\"}");
                return;
            }
            boolean isAuthorized =true;
            Cookie[] cookies = httpRequest.getCookies();
            if(cookies!=null)
            {
                for(Cookie cookie : cookies)
                {
                    if(cookie.getName().equals("name") && !cookie.getValue().equals(name))
                    {
                        isAuthorized=false;
                    }
                    if(cookie.getName().equals("userid") && !cookie.getValue().equals(userid))
                    {
                        isAuthorized=false;
                    }
                    if(cookie.getName().equals("password") && !cookie.getValue().equals(password))
                    {
                        isAuthorized=false;
                    }
                }
                if(isAuthorized)
                {
                    chain.doFilter(request,response);
                }
                else
                {
                    httpResponse.sendRedirect("/WebProject/login.html");
                    return;
                }
            }
            else
            {
                httpRequest.getSession().invalidate();
                httpResponse.sendRedirect("/WebProject/login.html");
            }

            System.out.println("user id : " + userid + "\nusername : " + name + "\nrole : " + role);

            chain.doFilter(request, response);
        }
        else
        {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.sendRedirect("/login.html");
            return;
        }

    }


//    private String getCookieValue(HttpServletRequest request, String cookieName) {
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookieName.equals(cookie.getName())) {
//                    return URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
//                }
//            }
//        }
//        return null;
//    }



    @Override
    public void destroy() {
    }
}
