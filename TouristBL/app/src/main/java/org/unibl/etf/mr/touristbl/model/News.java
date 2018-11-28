package org.unibl.etf.mr.touristbl.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class News implements Serializable{
    @SerializedName("vijestID")
    private Integer id;
    @SerializedName("Naslov")
    private String title;
    @SerializedName("Slika")
    private String imageUrl;

    public News(Integer id, String title, String imageUrl) {
        this.id=id;
        this.title=title;
        this.imageUrl=imageUrl;

    }

    public News(){

    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
