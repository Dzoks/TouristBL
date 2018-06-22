package org.openweathermap.poor.util;

import com.google.gson.Gson;

import org.openweathermap.poor.model.CurrentWeather;
import org.openweathermap.poor.model.ForecastWeather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

public class WeatherUtil {

    private String apiKey;
    private String locale;
    public static final String LANG_ENGLISH="en";
    public static final String LANG_SERBIAN="hr";
    private static final String CURRENT_QUERY="http://api.openweathermap.org/data/2.5/weather?q=";
    private static  final String FORECAST_QUERY="http://api.openweathermap.org/data/2.5/forecast?q=";
    public WeatherUtil(String apiKey,String locale ){
        this.apiKey=apiKey;
        this.locale=locale;
    }


    public ForecastWeather getForecastForCity(String cityName){
        String queryURL=FORECAST_QUERY+cityName+"&units=metric&lang="+locale+"&appid="+apiKey;
        Gson gson=new Gson();
        try {
            return gson.fromJson(readJsonFromUrl(queryURL),ForecastWeather.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public CurrentWeather getWeatherForTodayForCity(String cityName){
        String queryURL=CURRENT_QUERY+cityName+"&units=metric&lang="+locale+"&appid="+apiKey;
        Gson gson=new Gson();
        try {
            return gson.fromJson(readJsonFromUrl(queryURL),CurrentWeather.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private static String readJsonFromUrl(String url) throws IOException{
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            return jsonText;
        } finally {
            is.close();
        }
    }

}
