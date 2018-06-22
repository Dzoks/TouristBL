package org.openweathermap.poor.model;

import com.google.gson.annotations.SerializedName;

public class CurrentWeather {

    @SerializedName("dt_txt")
    private String dateTime;

    public String getTime(){
        return dateTime.substring(11,16);
    }


    @SerializedName("weather")
    private Weather[] weather;

    @SerializedName("main")
    private MainParams mainParams;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    public MainParams getMainParams() {
        return mainParams;
    }

    public void setMainParams(MainParams mainParams) {
        this.mainParams = mainParams;
    }
}
