package com.example.clddv13.remote;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String url){
        if(retrofit == null){
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(340, TimeUnit.SECONDS)
                    .readTimeout(340, TimeUnit.SECONDS)
                    .writeTimeout(340, TimeUnit.SECONDS)
                    .build();
            retrofit = new Retrofit.Builder().baseUrl(url)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
