package com.example.clddv13.remote;

import android.provider.ContactsContract;

import com.example.clddv13.SigningIn.SignUp;
import com.example.clddv13.customClasses.FileInfo;
import com.example.clddv13.LogIn.loggingIn;
import com.example.clddv13.customClasses.dataPoint;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileService {

    @Multipart
    @POST("upload")
    Call<FileInfo> upload(@Part MultipartBody.Part file,
                          @Part("username") String username,
                          @Part("location") String location);

    @FormUrlEncoded
    @POST("login/")
    Call<loggingIn> logCheck(@Field("username") String username,
                             @Field("password") String password);


    @FormUrlEncoded
    @POST("register/")
    Call<SignUp> register(@Field("username") String username,
                          @Field("password") String password,
                          @Field("number") String number,
                          @Field("confirm") String confirm);

    @GET("getdatapoints")
    Call<List<dataPoint>> getLocationPoints();
}
