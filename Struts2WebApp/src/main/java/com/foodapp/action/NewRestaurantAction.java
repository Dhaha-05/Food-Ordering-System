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
        RestaurantService restaurantService = new RestaurantService();
        hasPermission=restaurantService.verifyManager(restaurant.getManagerid());
        if(hasPermission)
        {
           hasPermission = restaurantService.createRestaurant(restaurant);
        }
        if(hasPermission){
            jsonResponse.put("status","success");
            jsonResponse.put("message","OK");
            return SUCCESS;
        }
        else {
            jsonResponse.put("status","failed");
            jsonResponse.put("message","Access Denied, 403 Forbidden access");
            jsonResponse.put("managerError","Enter valid Manager id");
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
}
