package com.example.clddv13.Tabs;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.clddv13.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class weather_tab3 extends Fragment {
    private String locality;

    //Enter your API_KEY
    private static String API_KEY="";
    //This File will not run if the API_KEY is not entered
    //Get Your Key from openweathermap
    private String URL_Name = "https://api.openweathermap.org/data/2.5/weather?q=";
    private String Api_add = "&appid=";


    private TextView temp_disp;
    private TextView CityCountry_disp;
    private TextView humidity_disp;
    private TextView tempLow_disp;
    private TextView tempMax_disp;
    private TextView weather_cast;
    public weather_tab3(String locality) {
        this.locality = locality;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather_tab3, container, false);
        temp_disp = view.findViewById(R.id.temp);
        CityCountry_disp = view.findViewById(R.id.city_country);
        humidity_disp = view.findViewById(R.id.humidity);
        tempLow_disp = view.findViewById(R.id.Temp_min);
        tempMax_disp = view.findViewById(R.id.Temp_max);
        weather_cast = view.findViewById(R.id.cast);
        if(locality!=null){
            new Weather().execute();
        }

        return view;
    }
    private void getWeather() throws MalformedURLException {
        URL url = new URL(URL_Name+locality+Api_add+API_KEY);
        String output = "";
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String Line;

            while ((Line = bufferedReader.readLine())!=null){
                output = output+Line;
            }

            if(!output.isEmpty()){
                JSONObject jsonObject = new JSONObject(output);
                JSONObject main = jsonObject.getJSONObject("main");
                JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
                JSONObject sys = jsonObject.getJSONObject("sys");

                String city = jsonObject.getString("name");
                String countryName = sys.getString("country");
                String temperature = main.getString("temp");
                String cast = weather.getString("description");
                String humidity = main.getString("humidity");
                String temp_min = main.getString("temp_min");
                String temp_max = main.getString("temp_max");

                temp_disp.setText(temperature);
                CityCountry_disp.setText(city+", "+countryName);
                humidity_disp.setText(humidity);
                tempLow_disp.setText(temp_min);
                tempMax_disp.setText(temp_max);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }


    class Weather extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL(URL_Name+locality+Api_add+API_KEY);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            String output = "";
            HttpURLConnection httpURLConnection = null;
            try {
                httpURLConnection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStream inputStream = null;
            try {
                inputStream = httpURLConnection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String Line = null;

            while (true) {
                try {
                    if (!((Line = bufferedReader.readLine()) != null)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                output = output + Line;
            }
            if (!output.isEmpty()){
                return output;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        private float kelvinToFerinhite(float k){
            float f=0;
            f = (float) (k-273.15);
            float x = (float) 9/5;
            f = f*x;
            f = (float) f+32;
            return f;
        }
        private float kelvinToCelsius(float k){
            float C =0;
            C = (float) (k-273.15);
            return C;
        }
        @Override
        protected void onPostExecute(String output) {

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(output);
                JSONObject main = jsonObject.getJSONObject("main");
                JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
                JSONObject sys = jsonObject.getJSONObject("sys");

                String city = jsonObject.getString("name");
                String countryName = sys.getString("country");
                String temperature = main.getString("temp");
                String cast = weather.getString("description");
                String humidity = main.getString("humidity");
                String temp_min = main.getString("temp_min");
                String temp_max = main.getString("temp_max");

                float f = kelvinToCelsius(Float.parseFloat(temperature));
                temp_disp.setText(Math.round(f)+"°C");
                CityCountry_disp.setText(city+", "+countryName);
                humidity_disp.setText(humidity);
                tempLow_disp.setText(Math.round(kelvinToCelsius(Float.parseFloat(temp_min)))+"°C");
                tempMax_disp.setText(Math.round(kelvinToCelsius(Float.parseFloat(temp_max)))+"°C");
                weather_cast.setText(cast);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
