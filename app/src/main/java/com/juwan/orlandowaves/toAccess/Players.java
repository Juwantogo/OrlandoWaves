package com.juwan.orlandowaves.toAccess;

/**
 * Created by Juwan on 12/4/2017.
 */

public class Players {
    String experience, height,image_url, name, position;
    long number;

    public Players(String experience, String height, String image_url, String name, String position, long number) {
        this.experience = experience;
        this.height = height;
        this.image_url = image_url;
        this.name = name;
        this.position = position;
        this.number = number;
    }

    public Players() {
    }


    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }


    @Override
    public String toString() {
        return "Players{" +
                "experience='" + experience + '\'' +
                ", height='" + height + '\'' +
                ", image_url='" + image_url + '\'' +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
