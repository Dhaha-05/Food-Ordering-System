package com.foodapp.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.util.redis.RedisUtil;
import com.foodapp.model.FoodItem;
import com.util.connection.DbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class FoodItemService {
    private Connection con;

    public List<FoodItem> getAllFoodItems() {
        List<FoodItem> foodItems = new ArrayList<>();
        try {
            String key = "restaurant_fooditems";
            Map<String, String> restaurantFoods = RedisUtil.getAll(key, new TypeToken<Map<String, String>>() {}.getType());

            if (restaurantFoods == null || restaurantFoods.isEmpty()) {
                System.out.println("No active food items in cache, fetching from DB...");
                con = DbConnection.getConnection();
                String sql = "SELECT i.id id, r.id restaurantid, r.restaurantname name, i.name item, i.price price, i.category category, i.rating rating, i.offer offer, i.itemstatus status " +
                        "FROM item i JOIN restaurant r ON r.id = i.restaurantid WHERE i.itemstatus = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, "active");
                ResultSet rs = ps.executeQuery();

                Map<String, List<FoodItem>> restaurantFoodItems = new HashMap<>();
                while (rs.next()) {
                    FoodItem item = new FoodItem();
                    item.setItemid(rs.getInt("id"));
                    item.setRestaurantid(rs.getInt("restaurantid"));
                    item.setRestaurantname(rs.getString("name"));
                    item.setItemname(rs.getString("item"));
                    item.setPrice(rs.getDouble("price"));
                    item.setCategory(rs.getString("category"));
                    item.setRating(rs.getDouble("rating"));
                    item.setOffer(rs.getInt("offer"));
                    item.setItemstatus(rs.getString("status"));
                    foodItems.add(item);

                    String restaurantId = String.valueOf(item.getRestaurantid());
                    if (!restaurantFoodItems.containsKey(restaurantId)) {
                        restaurantFoodItems.put(restaurantId, new ArrayList<>());
                    }
                    restaurantFoodItems.get(restaurantId).add(item);
                }

                for (Map.Entry<String, List<FoodItem>> entry : restaurantFoodItems.entrySet()) {
                    RedisUtil.set(key, entry.getKey(), entry.getValue());
                }
                RedisUtil.expire(key, 3600);
            } else {
                System.out.println("Fetching food items from Redis cache...");
                for (Map.Entry<String, String> entry : restaurantFoods.entrySet()) {
                    List<FoodItem> items = new Gson().fromJson(entry.getValue(), new TypeToken<List<FoodItem>>() {}.getType());
                    foodItems.addAll(items);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }

        return foodItems;
    }

    public List<FoodItem> getFoodItemsByRestaurant(int restaurantId) {
        List<FoodItem> foodItems = null;
        String key = "restaurant_fooditems";

        try {
            if (!RedisUtil.exists(key)) {
                getAllFoodItems();
            }

            foodItems = RedisUtil.get(key, String.valueOf(restaurantId), new TypeToken<List<FoodItem>>() {}.getType());

            if (foodItems != null) {
                System.out.println("Fetching from Redis cache");
            } else {
                System.out.println("No food items found for restaurant ID: " + restaurantId);
            }

        } catch (Exception e) {
            System.out.println("Exception while fetching food items from Redis: " + e.getMessage());
        }

        return foodItems;
    }
//    public List<FoodItem> getAllFoodItems() {
//        List<FoodItem> foodItems = new ArrayList<>();
//        try{
//            con = DbConnection.getConnection();
//            String sql = "SELECT i.id id, r.restaurantname name, i.name item, i.price price, i.category category, i.rating rating, i.offer offer, i.itemstatus status FROM item i JOIN restaurant r ON r.id = i.restaurantid where i.itemstatus = ?;";
//            PreparedStatement ps = con.prepareStatement(sql);
//            ps.setString(1,"active");
//            ResultSet rs = ps.executeQuery();
//
//            while (rs.next()) {
//                FoodItem item = new FoodItem();
//                item.setItemid(rs.getInt("id"));
//                item.setRestaurantname(rs.getString("name"));
//                item.setItemname(rs.getString("item"));
//                item.setPrice(rs.getDouble("price"));
//                item.setCategory(rs.getString("category"));
//                item.setRating(rs.getDouble("rating"));
//                item.setOffer(rs.getInt("offer"));
//                item.setItemstatus(rs.getString("status"));
//                foodItems.add(item);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return foodItems;
//    }


//    public List<FoodItem> getFoodItemsByRestaurant(int restaurantId) {
//        List<FoodItem> foodItems = new ArrayList<>();
//        try{
//            con = DbConnection.getConnection();
//            String sql = "SELECT i.id id, r.restaurantname name, i.name item, i.price price, i.category category, i.rating rating, i.offer offer FROM item i JOIN restaurant r ON r.id = i.restaurantid WHERE r.id = ? and i.itemstatus = ?;";
//            PreparedStatement ps = con.prepareStatement(sql);
//            ps.setInt(1, restaurantId);
//            ps.setString(2,"active");
//            ResultSet rs = ps.executeQuery();
//
//            while (rs.next()) {
//                FoodItem item = new FoodItem();
//                item.setItemid(rs.getInt("id"));
//                item.setRestaurantname(rs.getString("name"));
//                item.setItemname(rs.getString("item"));
//                item.setPrice(rs.getDouble("price"));
//                item.setCategory(rs.getString("category"));
//                item.setRating(rs.getDouble("rating"));
//                item.setOffer(rs.getInt("offer"));
//                foodItems.add(item);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return foodItems;
//    }

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
                System.out.println("Deleting FoodItems in the redis cache because of new item creation...");
                RedisUtil.delete("restaurant_fooditems");
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
                System.out.println("Deleting FoodItems in the redis cache because of an item is removed...");
                RedisUtil.delete("restaurant_fooditems");
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