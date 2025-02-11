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
            String sql = "SELECT i.id id, r.restaurantname name, i.name item, i.price price, i.category category, i.rating rating, i.offer offer, i.itemstatus status FROM item i JOIN restaurant r ON r.id = i.restaurantid where i.itemstatus = ?;";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1,"active");
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
            String sql = "SELECT i.id id, r.restaurantname name, i.name item, i.price price, i.category category, i.rating rating, i.offer offer FROM item i JOIN restaurant r ON r.id = i.restaurantid WHERE r.id = ? and i.itemstatus = ?;";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, restaurantId);
            ps.setString(2,"active");
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
        System.out.println("Food item entry : "+foodItem);
        if(foodItem==null)
        {
            return false;
        }
        try{
            con = DbConnection.getConnection();
            String sql = "Insert into item (restaurantid, name, category, price, rating) values (?,?,?,?,?);";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,foodItem.getRestaurantid());
            ps.setString(2,foodItem.getItemname());
            ps.setString(3, foodItem.getCategory());
            ps.setDouble(4,foodItem.getPrice());
            ps.setDouble(5,foodItem.getRating());
            int result = ps.executeUpdate();
            if(result>0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }catch (Exception e){
            System.out.println("Exception while creating item : "+e.getMessage());
            return false;
        }
    }

    public boolean removeItem(int foodId)
    {
        if(foodId<=0)
        {
            return false;
        }
        try {
            con = DbConnection.getConnection();
            String sql = "Update item set itemstatus = ? where id = ?; ";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1,"inactive");
            ps.setInt(2,foodId);
            int result = ps.executeUpdate();
            if(result>0)
            {
                return true;
            }
            else {
                return false;
            }
        }catch (Exception e){
            System.out.println("Exception while removing an item : "+e.getMessage());
            return false;
        }
    }
}