package com.foodapp.action;

import com.foodapp.model.User;
import com.foodapp.service.UserService;
import com.util.cipher.EncryptDecrypt;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.HashMap;

public class LoginAction extends ActionSupport implements SessionAware, ServletRequestAware, ServletResponseAware {

    private User userBean;
    private Map<String, Object> jsonResponse = new HashMap<>();
    private Map<String, Object> session;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public String execute() {
        UserService userService = new UserService();
        User user = userService.login(userBean);
        if (user != null) {
            session.put("userid", EncryptDecrypt.encrypt(String.valueOf(user.getUserid())));
            session.put("name", EncryptDecrypt.encrypt(user.getFullname()));
            session.put("password", user.getPassword());
            session.put("role", EncryptDecrypt.encrypt(user.getRole()));

            addCookies();

            jsonResponse.put("status", "success");
            jsonResponse.put("message", "User has an account");
            return SUCCESS;
        }

        jsonResponse.put("status", "failed");
        jsonResponse.put("message", "Invalid username and password");
        return NONE;
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

    public User getUserBean() {
        return userBean;
    }

    public void setUserBean(User userBean) {
        this.userBean = userBean;
    }

    public Map<String, Object> getJsonResponse() {
        return jsonResponse;
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
}