package com.example.clddv13.remote;

public class APIUtils {

    private APIUtils(){

    }

    //public static final String API_URL = "http://41aeeacb550c.ngrok.io";
    //public static final String API_URL = "https://testcase-2.herokuapp.com/";
    //public static final String API_URL = "http://ec2-13-126-238-223.ap-south-1.compute.amazonaws.com:1122/";
    //public static final String API_URL = "http://ec2-13-126-238-223.ap-south-1.compute.amazonaws.com:4555/";
    public static final String API_URL = "http://ec2-13-126-238-223.ap-south-1.compute.amazonaws.com:2233/";

    public static FileService getFileService(){
        return RetrofitClient.getClient(API_URL).create(FileService.class);

    }
}
