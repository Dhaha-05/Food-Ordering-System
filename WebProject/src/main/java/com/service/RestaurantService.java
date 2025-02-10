package com.service;

import com.model.User;
import com.model.Restaurant;
import com.connection.DbConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RestaurantService {
    private Connection con = null;
    private PreparedStatement ps = null;
    int result=0;
    ResultSet rs = null;
    public List<Restaurant> getAllRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();
        try (Connection con = DbConnection.getConnection()) {
            String sql = "SELECT id, name, location, rating,user_id FROM restaurant;";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Restaurant restaurant = new Restaurant();
                restaurant.setId(rs.getInt("id"));
                restaurant.setName(rs.getString("name"));
                restaurant.setLocation(rs.getString("location"));
                restaurant.setRating(rs.getDouble("rating"));
                restaurant.setManagerid(rs.getInt("user_id"));
                restaurants.add(restaurant);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return restaurants;
    }

    public List<Restaurant> getSpecificRestaurant(int userId)
    {
        List<Restaurant> restaurantList = getAllRestaurants();
        List<Restaurant> bindRestaurant = new ArrayList<>();
        for(Restaurant restaurant : restaurantList)
        {
            if(userId == restaurant.getManagerid())
            {
                bindRestaurant.add(restaurant);
            }
        }
        return bindRestaurant;
    }

    public boolean createRestaurant(Restaurant restaurant) throws SQLException {
        if (restaurant == null) {
            System.out.println("Restaurant object is null");
            return false;
        }

        String sql;
        try (Connection con = DbConnection.getConnection()) {
            sql = "INSERT INTO restaurant(name, location, rating, user_id, pan_card, gst_no, bank_account, fssai_license) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, restaurant.getName());
                ps.setString(2, restaurant.getLocation());
                ps.setDouble(3, restaurant.getRating());
                ps.setInt(4, restaurant.getManagerid());
                ps.setString(5, restaurant.getPancard());
                ps.setString(6, restaurant.getGstno());
                ps.setString(7, restaurant.getBankaccount());
                ps.setString(8, restaurant.getFssailicense());

                result = ps.executeUpdate();
            }

            if (result > 0) {
                sql = "SELECT id FROM restaurant WHERE gst_no = ? AND user_id = ?";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1, restaurant.getGstno());
                    ps.setInt(2, restaurant.getManagerid());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            return false;
                        }
                        restaurant.setId(rs.getInt("id"));
                    }
                }

                sql = "INSERT INTO manage_restaurant(user_id, restaurant_id) VALUES (?, ?);";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, restaurant.getManagerid());
                    ps.setInt(2, restaurant.getId());
                    result = ps.executeUpdate();
                }
                return result > 0;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return false;
        }
    }

}
