package com.juwan.orlandowaves.toAccess;

/**
 * Created by Juwan on 12/1/2017.
 */

public class Items {
    private String name;
    private String description;
    private String price;
    private String type;
    private String location;
    private long quantity;

    public Items(String name, String description, String price, long quantity, String type,String location) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.type = type;
        this.location = location;
    }

    public Items() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    @Override
    public String toString() {
        return "Items{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", type='" + type + '\'' +
                ", location='" + location + '\'' +
                ", quantity='" + quantity +
                '}';
    }
}
