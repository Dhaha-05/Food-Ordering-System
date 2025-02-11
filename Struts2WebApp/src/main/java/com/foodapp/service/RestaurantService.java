package com.foodapp.service;

import com.foodapp.model.Restaurant;
import com.util.connection.DbConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RestaurantService
{
    private Connection con = null;
    private PreparedStatement ps = null;

    public List<Restaurant> getAllRestaurant(){
        List<Restaurant> list = new ArrayList<>();
        try
        {
            con = DbConnection.getConnection();
            String sql = "select id,restaurantname,location,rating,managerid,restaurantstatus from restaurant where restaurantstatus = ?;";
            ps = con.prepareStatement(sql);
            ps.setString(1,"active");
            ResultSet rs = ps.executeQuery();
            if(!rs.isBeforeFirst()){
                return null;
            }
            while(rs.next()){
                Restaurant r = new Restaurant();
                r.setRestaurantid(rs.getInt("id"));
                r.setRestaurantname(rs.getString("restaurantname"));
                r.setLocation(rs.getString("location"));
                r.setRating(rs.getDouble("rating"));
                r.setManagerid(rs.getInt("managerid"));
                r.setRestaurantstatus(rs.getString("restaurantstatus"));
                list.add(r);
            }
            return list;
        }catch (Exception e)
        {
            System.out.println("Exception : "+e.getMessage());
        }
        return list;
    }

    public List<Restaurant> getRestaurantByManagerId(int managerId) {
        List<Restaurant> list = new ArrayList<>();
        try {
            con = DbConnection.getConnection();
            String sql = "SELECT id, restaurantname, location, rating, managerid, restaurantstatus FROM restaurant WHERE managerid = ? and restaurantstatus = ? ;";
            ps = con.prepareStatement(sql);
            ps.setInt(1, managerId);
            ps.setString(2,"active");
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
            }
            return list;
        } catch (Exception e) {
            System.out.println("Exception in getRestaurantByManagerId: " + e.getMessage());
            return null;
        }
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
                 ResultSet rs = ps.getGeneratedKeys();
                 if(!rs.next())
                 {
                     return false;
                 }
                 else
                 {
                     restaurantid = rs.getInt(1);
                     sql = "Insert into manage_restaurant (managerid,restaurantid) values (?,?);";
                     ps =  con.prepareStatement(sql);
                     ps.setInt(1,restaurant.getManagerid());
                     ps.setInt(2,restaurantid);
                     result = ps.executeUpdate();
                     if(result>0)
                     {
                        return true;
                     }
                     return false;
                 }
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
            PreparedStatement ps = con.prepareStatement(sql);
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
            }
            return result>0;
        }catch(Exception e){
            System.out.println("Exception while removing the restaurant : "+e.getMessage());
            return false;
        }
    }
}
