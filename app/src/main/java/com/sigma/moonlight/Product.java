package com.sigma.moonlight;


public class Product {

    String title;
    String description;
    double price;
    boolean selected;


    public Product(String title, String description, double price) {
        this.title = title;
        this.description = description;
        this.price = price;
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public boolean isSelected() {
        return selected;
    }

}