package org.openweathermap.poor.model;

import com.google.gson.annotations.SerializedName;

public class ForecastWeather {

    @SerializedName("list")
    CurrentWeather[] currentWeathers;

    public CurrentWeather[] getCurrentWeathers() {
        return currentWeathers;
    }

    public void setCurrentWeathers(CurrentWeather[] currentWeathers) {
        this.currentWeathers = currentWeathers;
    }
}
