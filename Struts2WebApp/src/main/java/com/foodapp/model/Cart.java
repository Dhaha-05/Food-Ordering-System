package com.foodapp.model;

public class Cart
{
    private int cartid;
    private int userid;
    private int itemid;
    private String itemname;
    private int quantity;
    private double price;
    private double totalprice;
    private String couponcode;
    private double couponamount;

    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public int getCartid() {
        return cartid;
    }

    public void setCartid(int cartid) {
        this.cartid = cartid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(double totalprice) {
        this.totalprice = totalprice;
    }

    public String getCouponcode() {
        return couponcode;
    }

    public void setCouponcode(String couponcode) {
        this.couponcode = couponcode;
    }

    public double getCouponamount() {
        return couponamount;
    }

    public void setCouponamount(double couponamount) {
        this.couponamount = couponamount;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartid=" + cartid +
                ", userid=" + userid +
                ", quantity=" + quantity +
                ", totalprice=" + totalprice +
                ", couponcode='" + couponcode + '\'' +
                ", couponamount=" + couponamount +
                '}';
    }
}
