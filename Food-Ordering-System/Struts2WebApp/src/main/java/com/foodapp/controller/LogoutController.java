package com.foodapp.controller;

import javax.servlet.http.*;
import java.io.IOException;

public class LogoutController extends HttpServlet
{
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        Cookie[] cookies = request.getCookies();
        if(session!=null)
        {
            session.invalidate();
        }
        if(cookies!=null)
        {
            for(Cookie cookie : cookies)
            {
                cookie.setMaxAge(-1);
                cookie.setValue("");
                response.addCookie(cookie);
            }
        }
    }
}
