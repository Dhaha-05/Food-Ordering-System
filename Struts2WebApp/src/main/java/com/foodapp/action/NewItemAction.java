package com.foodapp.action;

import com.foodapp.model.FoodItem;
import com.foodapp.service.FoodItemService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class NewItemAction extends ActionSupport implements SessionAware, ServletRequestAware, ServletResponseAware
{
    private Map<String,Object> jsonResponse = new HashMap<>();
    private Map<String,Object> session;
    private FoodItem item;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public String execute()
    {
        boolean hasPermission =false;
        if(session!=null)
        {
            Cookie[] cookies = request.getCookies();
            if(cookies!=null)
            {
                hasPermission = userAuthentication(session,cookies);
                if(hasPermission)
                {
                    FoodItemService foodItemService = new FoodItemService();
                    hasPermission = foodItemService.createItem(item);
                }
            }
        }
        if(hasPermission){
            jsonResponse.put("status","success");
            jsonResponse.put("message","OK");
            return SUCCESS;
        }
        else {
            jsonResponse.put("status","failed");
            jsonResponse.put("message","Access Denied, 403 Forbidden access");
            return NONE;
        }
    }

    public Map<String, Object> getJsonResponse() {
        return jsonResponse;
    }

    public void setJsonResponse(Map<String, Object> jsonResponse) {
        this.jsonResponse = jsonResponse;
    }

    public FoodItem getItem() {
        return item;
    }

    public void setItem(FoodItem item) {
        this.item = item;
    }

    @Override
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response = httpServletResponse;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }

    private boolean userAuthentication(Map<String,Object> session, Cookie[] cookies)
    {
        boolean isAuthorized = true;
        for(Cookie cookie:cookies)
        {
            if("userid".equals(cookie.getName()) || "role".equals(cookie.getName()) || "password".equals(cookie.getName()) || "name".equals(cookie.getName()))
            {
//                System.out.println("Session => "+cookie.getName()+" : "+session.get(cookie.getName()));
//                System.out.println("Cookie => "+cookie.getName()+" : "+cookie.getValue());
                isAuthorized = session.get(cookie.getName()).equals(cookie.getValue());
                if(!isAuthorized)
                {
                    return isAuthorized;
                }
            }
        }
        return isAuthorized;
    }
}
