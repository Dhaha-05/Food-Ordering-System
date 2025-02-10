package com.foodapp.model;

public class Restaurant
{
    private int restaurantid;
    private String restaurantname;
    private String location;
    private double rating;
    private int managerid;
    private String pancard;
    private String gstno;
    private String bankaccount;
    private String fssailicense;
    private String restaurantstatus;

    public int getRestaurantid() {
        return restaurantid;
    }

    public void setRestaurantid(int restaurantid) {
        this.restaurantid = restaurantid;
    }

    public String getRestaurantname() {
        return restaurantname;
    }

    public void setRestaurantname(String restaurantname) {
        this.restaurantname = restaurantname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getPancard() {
        return pancard;
    }

    public void setPancard(String pancard) {
        this.pancard = pancard;
    }

    public String getGstno() {
        return gstno;
    }

    public void setGstno(String gstno) {
        this.gstno = gstno;
    }

    public String getBankaccount() {
        return bankaccount;
    }

    public void setBankaccount(String bankaccount) {
        this.bankaccount = bankaccount;
    }

    public String getFssailicense() {
        return fssailicense;
    }

    public void setFssailicense(String fssailicense) {
        this.fssailicense = fssailicense;
    }

    public String getRestaurantstatus() {
        return restaurantstatus;
    }

    public void setRestaurantstatus(String restaurantstatus) {
        this.restaurantstatus = restaurantstatus;
    }

    public int getManagerid() {
        return managerid;
    }

    public void setManagerid(int managerid) {
        this.managerid = managerid;
    }


    @Override
    public String toString() {
        return "Restaurant{" +
                "restaurantid=" + restaurantid +
                ", restaurantname='" + restaurantname + '\'' +
                ", location='" + location + '\'' +
                ", rating=" + rating +
                ", managerid=" + managerid +
                ", pancard='" + pancard + '\'' +
                ", gstno='" + gstno + '\'' +
                ", bankaccount='" + bankaccount + '\'' +
                ", fssailicense='" + fssailicense + '\'' +
                ", restaurantstatus='" + restaurantstatus + '\'' +
                '}';
    }
}
