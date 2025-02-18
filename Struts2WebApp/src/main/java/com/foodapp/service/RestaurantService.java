package com.foodapp.service;

import com.foodapp.model.Restaurant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.util.connection.DbConnection;
import com.util.redis.RedisUtil;
import java.sql.*;
import java.util.*;

public class RestaurantService
{
    private Connection con = null;
    private PreparedStatement ps = null;

    public List<Restaurant> getAllRestaurant() {
        List<Restaurant> list = new ArrayList<>();
        try {
            String key = "restaurants";
            Map<String, String> restaurantMap = RedisUtil.getAll(key, new TypeToken<Map<String, String>>() {}.getType());

            if (restaurantMap == null || restaurantMap.isEmpty()) {
                System.out.println("No cache found, fetching data from DB...");
                con = DbConnection.getConnection();
                String sql = "SELECT id, restaurantname, location, rating, managerid, restaurantstatus FROM restaurant WHERE restaurantstatus = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, "active");
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    Restaurant r = new Restaurant();
                    r.setRestaurantid(rs.getInt("id"));
                    r.setRestaurantname(rs.getString("restaurantname"));
                    r.setLocation(rs.getString("location"));
                    r.setRating(rs.getDouble("rating"));
                    r.setManagerid(rs.getInt("managerid"));
                    r.setRestaurantstatus(rs.getString("restaurantstatus"));
                    list.add(r);

                    Map<String, String> restaurantData = new HashMap<>();
                    restaurantData.put("restaurantname", r.getRestaurantname());
                    restaurantData.put("location", r.getLocation());
                    restaurantData.put("rating", String.valueOf(r.getRating()));
                    restaurantData.put("managerid", String.valueOf(r.getManagerid()));
                    restaurantData.put("restaurantstatus", r.getRestaurantstatus());

                    RedisUtil.set(key, String.valueOf(r.getRestaurantid()), restaurantData);
                    RedisUtil.expire(key, 3600);
                }
            } else {
                System.out.println("Fetched from Redis cache!!!");
                for (Map.Entry<String, String> entry : restaurantMap.entrySet()) {
                    Restaurant r = new Restaurant();
                    Map<String, String> restaurantData = new Gson().fromJson(entry.getValue(), new TypeToken<Map<String, String>>() {}.getType());
                    r.setRestaurantid(Integer.parseInt(entry.getKey()));
                    r.setRestaurantname(restaurantData.get("restaurantname"));
                    r.setLocation(restaurantData.get("location"));
                    r.setRating(Double.parseDouble(restaurantData.get("rating")));
                    r.setManagerid(Integer.parseInt(restaurantData.get("managerid")));
                    r.setRestaurantstatus(restaurantData.get("restaurantstatus"));
                    list.add(r);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        return list;
    }

//    public List<Restaurant> getAllRestaurant(){
//        List<Restaurant> list = new ArrayList<>();
//        try
//        {
//            con = DbConnection.getConnection();
//            String sql = "select id,restaurantname,location,rating,managerid,restaurantstatus from restaurant where restaurantstatus = ?;";
//            ps = con.prepareStatement(sql);
//            ps.setString(1,"active");
//            ResultSet rs = ps.executeQuery();
//            if(!rs.isBeforeFirst()){
//                return null;
//            }
//            while(rs.next()){
//                Restaurant r = new Restaurant();
//                r.setRestaurantid(rs.getInt("id"));
//                r.setRestaurantname(rs.getString("restaurantname"));
//                r.setLocation(rs.getString("location"));
//                r.setRating(rs.getDouble("rating"));
//                r.setManagerid(rs.getInt("managerid"));
//                r.setRestaurantstatus(rs.getString("restaurantstatus"));
//                list.add(r);
//            }
//            return list;
//        }catch (Exception e)
//        {
//            System.out.println("Exception : "+e.getMessage());
//        }
//        return list;
//    }

    public List<Restaurant> getRestaurantByManagerId(int managerId) {
        List<Restaurant> list = new ArrayList<>();
        try {
            String key = "restaurants";
            Map<String, String> restaurantMap = RedisUtil.getAll(key, new TypeToken<Map<String, String>>() {}.getType());

            if (restaurantMap == null || restaurantMap.isEmpty()) {
                list = getAllRestaurant();
            } else {
                for (Map.Entry<String, String> entry : restaurantMap.entrySet()) {
                    Map<String, String> restaurantData = new Gson().fromJson(entry.getValue(), new TypeToken<Map<String, String>>() {}.getType());
                    if (Integer.parseInt(restaurantData.get("managerid")) == managerId) {
                        Restaurant r = new Restaurant();
                        r.setRestaurantid(Integer.parseInt(entry.getKey()));
                        r.setRestaurantname(restaurantData.get("restaurantname"));
                        r.setLocation(restaurantData.get("location"));
                        r.setRating(Double.parseDouble(restaurantData.get("rating")));
                        r.setManagerid(managerId);
                        r.setRestaurantstatus(restaurantData.get("restaurantstatus"));
                        list.add(r);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        return list;
    }

    public boolean createRestaurant(Restaurant restaurant)
    {
        if(restaurant == null)
            return false;
        try{
            System.out.println("Restaurant : "+restaurant);
            con = DbConnection.getConnection();
            String sql = "Insert into restaurant (restaurantname,location,rating,managerid,pancard,gstno,bankaccount,fssailicense)values(?,?,?,?,?,?,?,?)";
            ps = con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1,restaurant.getRestaurantname());
            ps.setString(2,restaurant.getLocation());
            ps.setDouble(3,restaurant.getRating());
            ps.setInt(4,restaurant.getManagerid());
            ps.setString(5, restaurant.getPancard());
            ps.setString(6,restaurant.getGstno());
            ps.setString(7,restaurant.getBankaccount());
            ps.setString(8,restaurant.getFssailicense());
            int result = ps.executeUpdate();
            int restaurantid=0;
            if(result>0)
            {
                RedisUtil.delete("restaurants");
                return true;
//                 ResultSet rs = ps.getGeneratedKeys();
//                 if(!rs.next())
//                 {
//                     return false;
//                 }
//                 else
//                 {
//                     restaurantid = rs.getInt(1);
//                     sql = "Insert into manage_restaurant (managerid,restaurantid) values (?,?);";
//                     ps =  con.prepareStatement(sql);
//                     ps.setInt(1,restaurant.getManagerid());
//                     ps.setInt(2,restaurantid);
//                     result = ps.executeUpdate();
//                     if(result>0)
//                     {
//                        return true;
//                     }
//                     return false;
//                 }
            }
            return false;
        }catch (Exception e)
        {
            System.out.println("Exception in Restaurant Service : "+e.getMessage());
            return false;
        }
    }

    public boolean removeRestaurant(int restaurantId){
        if(restaurantId<=0)
            return false;
        try
        {
            con = DbConnection.getConnection();
            String sql = "Update restaurant set restaurantstatus = ? where id = ?;";
            ps = con.prepareStatement(sql);
            ps.setString(1,"inactive");
            ps.setInt(2,restaurantId);
            int result = ps.executeUpdate();
            if(result>0)
            {
                sql="Update item set itemstatus = ? where restaurantid = ? ;";
                ps = con.prepareStatement(sql);
                ps.setString(1,"inactive");
                ps.setInt(2,restaurantId);
                result = ps.executeUpdate();
                System.out.println("Clearing restaurant and foodItem data in the cache!!!");
                RedisUtil.delete("restaurants");
                RedisUtil.delete("restaurant_fooditems");
            }
            return result>0;
        }catch(Exception e){
            System.out.println("Exception while removing the restaurant : "+e.getMessage());
            return false;
        }
    }

//    public boolean verifyManager(int managerId)
//    {
//        try {
//            con = DbConnection.getConnection();
//            String sql = "select id from users where id = ? and (role = ? or role = ?); ";
//            PreparedStatement ps = con.prepareStatement(sql);
//            ps.setInt(1,managerId);
//            ps.setString(2,"manager");
//            ps.setString(3,"admin");
//            ResultSet rs = ps.executeQuery();
//            if(!rs.next())
//            {
//                return false;
//            }
//            else
//                return true;
//        }catch (Exception e)
//        {
//            return false;
//        }
//    }
}
