package com.foodapp.service;

import com.foodapp.model.FoodItem;
import com.util.connection.DbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FoodItemService {
    private Connection con;
    public List<FoodItem> getAllFoodItems() {
        List<FoodItem> foodItems = new ArrayList<>();
        try{
            con = DbConnection.getConnection();
            String sql = "SELECT i.id id, r.restaurantname name, i.name item, i.price price, i.category category, i.rating rating, i.offer offer, i.itemstatus status FROM item i JOIN restaurant r ON r.id = i.restaurantid;";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                FoodItem item = new FoodItem();
                item.setItemid(rs.getInt("id"));
                item.setRestaurantname(rs.getString("name"));
                item.setItemname(rs.getString("item"));
                item.setPrice(rs.getDouble("price"));
                item.setCategory(rs.getString("category"));
                item.setRating(rs.getDouble("rating"));
                item.setOffer(rs.getInt("offer"));
                item.setItemstatus(rs.getString("status"));
                foodItems.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return foodItems;
    }

    public List<FoodItem> getFoodItemsByRestaurant(int restaurantId) {
        List<FoodItem> foodItems = new ArrayList<>();
        try{
            con = DbConnection.getConnection();
            String sql = "SELECT i.id id, r.restaurantname name, i.name item, i.price price, i.category category, i.rating rating, i.offer offer FROM item i JOIN restaurant r ON r.id = i.restaurantid WHERE r.id = ?;";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, restaurantId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                FoodItem item = new FoodItem();
                item.setItemid(rs.getInt("id"));
                item.setRestaurantname(rs.getString("name"));
                item.setItemname(rs.getString("item"));
                item.setPrice(rs.getDouble("price"));
                item.setCategory(rs.getString("category"));
                item.setRating(rs.getDouble("rating"));
                item.setOffer(rs.getInt("offer"));
                foodItems.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return foodItems;
    }

    public boolean createItem(FoodItem foodItem)
    {
        return false;
    }
}