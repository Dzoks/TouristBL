package org.openweathermap.poor.model;

import com.google.gson.annotations.SerializedName;

public class Weather {


    private final static String IMG_URL="http://openweathermap.org/img/w/";

    @SerializedName("main")
    private String main;
    @SerializedName("description")
    private String description;
    @SerializedName("icon")
    private String icon;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMain() {

        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getImageURL(){
        return IMG_URL+icon+".png";
    }
}
