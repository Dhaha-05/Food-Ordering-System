package com.model;

public class Restaurant {
    private int id;
    private String name;
    private String location;
    private double rating;
    private int managerid;
    private String pancard;
    private String gstno;
    private String bankaccount;
    private String fssailicense;
    private String status;

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", rating=" + rating +
                ", managerid=" + managerid +
                ", pancard='" + pancard + '\'' +
                ", gstno='" + gstno + '\'' +
                ", bankaccount='" + bankaccount + '\'' +
                ", fssailicense='" + fssailicense + '\'' +
                ", status='" + status + '\'' +
                '}';
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getManagerid() {
        return managerid;
    }

    public void setManagerid(int managerid) {
        this.managerid = managerid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}