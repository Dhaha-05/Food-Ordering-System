package com.foodapp.action;

import com.foodapp.model.Restaurant;
import com.foodapp.service.RestaurantService;
import com.opensymphony.xwork2.ActionSupport;
import com.util.cipher.EncryptDecrypt;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class NewRestaurantAction extends ActionSupport implements SessionAware, ServletRequestAware, ServletResponseAware
{
    private Map<String, Object> jsonResponse = new HashMap<>();
    private Map<String,Object> session;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Restaurant restaurant;
    private boolean hasPermission = false;
    public String execute()
    {
        if(session!=null)
        {
            Cookie[] cookies = request.getCookies();
            if(cookies!=null)
            {
                hasPermission = userAuthentication(session,cookies);
                if(hasPermission)
                {
                    RestaurantService restaurantService = new RestaurantService();
                    restaurant.setManagerid(Integer.parseInt(EncryptDecrypt.decrypt(String.valueOf(session.get("userid")))));
                    hasPermission= restaurantService.createRestaurant(restaurant);
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

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
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
                System.out.println("Session => "+cookie.getName()+" : "+session.get(cookie.getName()));
                System.out.println("Cookie => "+cookie.getName()+" : "+cookie.getValue());
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
