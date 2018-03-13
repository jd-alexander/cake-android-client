package com.waracle.androidtest.data.models;


public class Cake {

    private String title;
    private String description;
    private String imageUrl;

    public Cake() {}

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        //a typo in JSON URL prevented one of the cakes from displaying properly
        this.imageUrl = this.title.equals("victoria sponge") ?
                imageUrl.replace("http", "https") : imageUrl;
    }
}
