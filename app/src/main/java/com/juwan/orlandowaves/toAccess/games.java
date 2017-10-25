package com.juwan.orlandowaves.toAccess;

/**
 * Created by Juwan on 10/25/2017.
 */

public class games {
    private long game_id;
    private String date;
    private String day;
    private String doors_open;
    private String event;
    private String opponent;
    private String location;
    private String image_url;
    private String time;
    private String adult_price;
    private String youth_price;

    public games(long game_id, String date, String day, String doors_open, String event, String opponent, String location, String image_url, String time, String adult_price, String youth_price) {
        this.game_id = game_id;
        this.date = date;
        this.day = day;
        this.doors_open = doors_open;
        this.event = event;
        this.opponent = opponent;
        this.location = location;
        this.image_url = image_url;
        this.time = time;
        this.adult_price = adult_price;
        this.youth_price = youth_price;
    }

    public games() {

    }

    public String getYouth_price() {
        return youth_price;
    }

    public void setYouth_price(String youth_price) {
        this.youth_price = youth_price;
    }

    public void setGame_id(long game_id) {
        this.game_id = game_id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setDoors_open(String doors_open) {
        this.doors_open = doors_open;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAdult_price() {
        return adult_price;
    }

    public void setAdult_price(String adult_price) {
        this.adult_price = adult_price;
    }
}
