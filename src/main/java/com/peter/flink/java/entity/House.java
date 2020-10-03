package com.peter.flink.java.entity;

public class House {
    private String ds;
    private String ts;
    private String description;
    private  String name;
    private String size;
    private  String direction;
    private  String decoration;
    private String floor;
    private String category;
    private String total_price;
    private  String avg_price;
    private  String advantage;
    private  String hid;

    public House() {
    }

    public String getDs() {
        return ds;
    }

    public void setDs(String ds) {
        this.ds = ds;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    @Override
    public String toString() {
        return "House{" +
                "ds='" + ds + '\'' +
                ", ts='" + ts + '\'' +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", size='" + size + '\'' +
                ", direction='" + direction + '\'' +
                ", decoration='" + decoration + '\'' +
                ", floor='" + floor + '\'' +
                ", category='" + category + '\'' +
                ", total_price='" + total_price + '\'' +
                ", avg_price='" + avg_price + '\'' +
                ", advantage='" + advantage + '\'' +
                ", hid='" + hid + '\'' +
                '}';
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDecoration() {
        return decoration;
    }

    public void setDecoration(String decoration) {
        this.decoration = decoration;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getAvg_price() {
        return avg_price;
    }

    public void setAvg_price(String avg_price) {
        this.avg_price = avg_price;
    }

    public String getAdvantage() {
        return advantage;
    }

    public void setAdvantage(String advantage) {
        this.advantage = advantage;
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public House(String ds, String ts, String description, String name, String size, String direction, String decoration, String floor, String category, String total_price, String avg_price, String advantage, String hid) {
        this.ds = ds;
        this.ts = ts;
        this.description = description;
        this.name = name;
        this.size = size;
        this.direction = direction;
        this.decoration = decoration;
        this.floor = floor;
        this.category = category;
        this.total_price = total_price;
        this.avg_price = avg_price;
        this.advantage = advantage;
        this.hid = hid;
    }
}
