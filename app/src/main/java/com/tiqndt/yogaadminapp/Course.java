package com.tiqndt.yogaadminapp;

public class Course {
    private int id;
    private String type;
    private String day;
    private String time;
    private int capacity;
    private double price;
    private int duration;
    private String description;

    public Course() {
    }

    public Course(int id, String type, String day, String time, int capacity, double price, int duration,
            String description) {
        this.id = id;
        this.type = type;
        this.day = day;
        this.time = time;
        this.capacity = capacity;
        this.price = price;
        this.duration = duration;
        this.description = description;
    }

    public Course(String type, String day, String time, int capacity, double price, int duration, String description) {
        this.type = type;
        this.day = day;
        this.time = time;
        this.capacity = capacity;
        this.price = price;
        this.duration = duration;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
