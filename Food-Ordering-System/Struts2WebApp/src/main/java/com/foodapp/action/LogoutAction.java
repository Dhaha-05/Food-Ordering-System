package com.foodapp.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class LogoutAction extends ActionSupport implements SessionAware, ServletRequestAware, ServletResponseAware
{
    private Map<String, Object> jsonResponse = new HashMap<>();
    private Map<String, Object> session;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public String execute()
    {
        if(session!=null)
        {
            SessionMap session = (SessionMap) ActionContext.getContext().getSession();
            session.invalidate();
            removeCookie(request,response);
            jsonResponse.put("status","success");
            jsonResponse.put("message","Logout success");
            return SUCCESS;
        }
        jsonResponse.put("status","failed");
        jsonResponse.put("message","Something went wrong while logout");
        return NONE;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    public Map<String, Object> getJsonResponse() {
        return jsonResponse;
    }

    public void setJsonResponse(Map<String, Object> jsonResponse) {
        this.jsonResponse = jsonResponse;
    }

    private void removeCookie(HttpServletRequest req, HttpServletResponse res)
    {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName()) || "name".equals(cookie.getName()) || "role".equals(cookie.getName()) || "password".equals(cookie.getName())) {
                    System.out.println(cookie.getName() +" : "+cookie.getValue());
                    cookie.setValue("");
                    cookie.setMaxAge(-1);
                    res.addCookie(cookie);
                }
            }
        }
    }
}
