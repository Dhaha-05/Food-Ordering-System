package com.foodapp.action;

import com.foodapp.service.UserService;
import com.opensymphony.xwork2.ActionSupport;
import com.foodapp.model.User;
import com.util.cipher.EncryptDecrypt;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class RegisterAction extends ActionSupport implements SessionAware, ServletRequestAware, ServletResponseAware
{
    private User userBean;
    private Map<String,Object> jsonResponse = new HashMap<>();
    private Map<String, Object> session;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public String execute() throws SQLException {

        UserService userService = new UserService();
        System.out.println(userBean);
        boolean isValid = userService.dataValidation(userBean, jsonResponse);
        if(isValid)
        {
            boolean isRegistered = userService.register(getUserBean());
            if(isRegistered)
            {
                session.put("userid", EncryptDecrypt.encrypt(String.valueOf(userBean.getUserid())));
                session.put("name", EncryptDecrypt.encrypt(userBean.getFullname()));
                session.put("password", userBean.getPassword());
                session.put("role", EncryptDecrypt.encrypt(userBean.getRole()));
                addCookies();
                jsonResponse.put("status","success");
                jsonResponse.put("message","Data inserted");
                return SUCCESS;
            }
            else {
                jsonResponse.put("status","error");
                jsonResponse.put("message","Insertion failed");
                return NONE;
            }
        }
        return NONE;
    }

    public Map<String, Object> getJsonResponse() {
        return jsonResponse;
    }

    public  void setUserBean(User userBean)
    {
        this.userBean = userBean;
    }

    public User getUserBean()
    {
        return userBean;
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

    private void addCookies() {
        Cookie cookie = new Cookie("userid", String.valueOf(session.get("userid")));
        response.addCookie(cookie);

        cookie = new Cookie("name", String.valueOf(session.get("name")));
        response.addCookie(cookie);

        cookie = new Cookie("password", String.valueOf(session.get("password")));
        response.addCookie(cookie);

        cookie = new Cookie("role", String.valueOf(session.get("role")));
        response.addCookie(cookie);
    }
}
