package com.foodapp.action;

import com.foodapp.model.FoodItem;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
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
        return NONE;
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
}
