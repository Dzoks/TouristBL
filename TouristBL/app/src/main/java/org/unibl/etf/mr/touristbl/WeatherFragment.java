package org.unibl.etf.mr.touristbl;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import org.openweathermap.poor.model.CurrentWeather;
import org.openweathermap.poor.model.ForecastWeather;
import org.openweathermap.poor.util.WeatherUtil;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.unibl.etf.mr.touristbl.R;

public class WeatherFragment extends Fragment {
    private final String API_KEY="43ba0420a582f6918f407f8df3ccb2b7";
    private ImageView todayPhoto;
    private TextView todayTemperature;
    private TextView todayDescription;

    private TextView oneDate;
    private TextView oneTemp;
    private ImageView onePhoto;

    private TextView twoDate;
    private TextView twoTemp;
    private ImageView twoPhoto;

    private TextView threeDate;
    private TextView threeTemp;
    private ImageView threePhoto;
    private ProgressDialog dialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_weather,container,false);
        todayTemperature=(TextView)view.findViewById(R.id.weather_main_temp);
        todayDescription=(TextView)view.findViewById(R.id.weather_main_desc);
        todayPhoto=(ImageView)view.findViewById(R.id.weather_main_photo);
        oneDate=(TextView)view.findViewById(R.id.weather_one_date);
        onePhoto=(ImageView)view.findViewById(R.id.weather_one_photo);
        oneTemp=(TextView)view.findViewById(R.id.weather_one_temp);
        twoDate=(TextView)view.findViewById(R.id.weather_two_date);
        twoPhoto=(ImageView)view.findViewById(R.id.weather_two_photo);
        twoTemp=(TextView)view.findViewById(R.id.weather_two_temp);
        threeDate=(TextView)view.findViewById(R.id.weather_three_date);
        threePhoto=(ImageView)view.findViewById(R.id.weather_three_photo);
        threeTemp=(TextView)view.findViewById(R.id.weather_three_temp);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Učitava se...");
        dialog.show();
        new WeatherTask().execute();

        return view;
        }



    public class WeatherTask extends AsyncTask<Void,Void,ForecastWeather>{
        @Override
        protected void onPostExecute(ForecastWeather forecast) {
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy. HH:mm");

            CurrentWeather weather=forecast.getCurrentWeathers()[0];
            todayTemperature.setText(weather.getMainParams().getTemp()+"°C");
            todayDescription.setText(weather.getWeather()[0].getDescription());
            Picasso.get().load(weather.getWeather()[0].getImageURL()).into(todayPhoto);
            //First day
            weather=forecast.getCurrentWeathers()[1];
            oneDate.setText(weather.getTime());
            oneTemp.setText(weather.getMainParams().getTemp()+"°C");
            Picasso.get().load(weather.getWeather()[0].getImageURL()).into(onePhoto);
            //Second day
            weather=forecast.getCurrentWeathers()[2];
            twoDate.setText(weather.getTime());
            twoTemp.setText(weather.getMainParams().getTemp()+"°C");
            Picasso.get().load(weather.getWeather()[0].getImageURL()).into(twoPhoto);
            //Third day
            weather=forecast.getCurrentWeathers()[3];
            threeDate.setText(weather.getTime());
            threeTemp.setText(weather.getMainParams().getTemp()+"°C");
            Picasso.get().load(weather.getWeather()[0].getImageURL()).into(threePhoto);
            hideDialog();

        }

        @Override
        protected ForecastWeather doInBackground(Void... voids) {
            WeatherUtil weatherUtil=new WeatherUtil(API_KEY,WeatherUtil.LANG_SERBIAN);

            ForecastWeather forecast=weatherUtil.getForecastForCity(getString(R.string.city_name).replace(" ","%20"));

         return forecast;
        }
    }
    public void hideDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
