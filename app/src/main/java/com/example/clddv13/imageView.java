package com.example.clddv13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clddv13.customClasses.FileInfo;
import com.example.clddv13.remote.APIUtils;
import com.example.clddv13.remote.FileService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.macasaet.fernet.Key;
import com.macasaet.fernet.Token;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class imageView extends AppCompatActivity {
    FileService fileService;
    Uri imageUri;
    String imagePath;
    private TextView txtView;
    CardView cardView;
    ProgressBar progressBar;
    Boolean check = false;
    FloatingActionButton fab;
    String dataCheck;
    FloatingActionButton adClose;
    CardView adCard;
    ImageView adImage;
    String User;
    ///Location Test Display
    TextView loaction;
    FusedLocationProviderClient fusedLocationProviderClient;
    protected final Key key_global = new Key("FxYKToh3BFHavV4wLwMECOKfleABjcqQXEBPo-fekZ0=");

    String log_lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        fileService = APIUtils.getFileService();


        ImageView image = (ImageView) findViewById(R.id.imageShow);
        loaction = findViewById(R.id.location);
        if (getIntent().getExtras() != null) {
            imageUri = Uri.parse(getIntent().getStringExtra("imageUri"));
            imagePath = getIntent().getStringExtra("path");
            dataCheck = getIntent().getStringExtra("check");
            User = getIntent().getStringExtra("UserName");

            image.setImageURI(imageUri);
            progressBar = findViewById(R.id.proBar);
            cardView = findViewById(R.id.displayCard);
            txtView = findViewById(R.id.TxtViewResponse);
        }


        fab = findViewById(R.id.fab2);
        adClose = findViewById(R.id.AdCloseButton);
        adCard = findViewById(R.id.adCard);

        ShowAd();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCardView();
                File file;
                if (dataCheck.equals("camera")) {
                    String newPath = imagePath.substring(5);
                    file = new File(newPath);
                } else {

                    file = new File(imagePath);
                }

                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

                final Token token_logLat = Token.generate(key_global,log_lat);

                Call<FileInfo> call = fileService.upload(body,User,token_logLat.serialise());

                call.enqueue(new Callback<FileInfo>() {
                    @Override
                    public void onResponse(Call<FileInfo> call, Response<FileInfo> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(imageView.this, "Image Uploaded Successfully!", Toast.LENGTH_LONG).show();
                            if (!response.isSuccessful()) {
                                txtView.setText("code: " + response.code());
                                return;
                            }
                            FileInfo posts = response.body();
                            txtView.setText(posts.getName());
                            progressBar.setVisibility(View.GONE);
                            txtView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<FileInfo> call, Throwable t) {
                        Toast.makeText(imageView.this, "Error" + t.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });
            }
        });


    }

    private void ShowAd() {
        fab.setVisibility(View.GONE);
        adCard.setVisibility(View.VISIBLE);
//        adImage = findViewById(R.id.adImage);
        adClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adCard.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
            }
        });


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(imageView.this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(imageView.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    656);
        }
//        adImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Uri uri = Uri.parse("https://www.nestle.com/"); // missing 'http://' will cause crashed
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,  int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 656){
            getLocation();
        }
    }

    private void getLocation() {

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                //Initialization Location

                Location location = task.getResult();
                if (location != null) {
                    try {
                        //Initialize geoCoder
                        Geocoder geocoder = new Geocoder(imageView.this,
                                Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );
                        String logitude;
                        String latitude;
                        String country;
                        String countryCode;
                        String Locality;
                        String addressline;

                        logitude = String.valueOf(addresses.get(0).getLongitude());
                        latitude = String.valueOf(addresses.get(0).getLatitude());
                        country = addresses.get(0).getCountryName();
                        countryCode = String.valueOf(addresses.get(0).getCountryCode());
                        Locality = addresses.get(0).getLocality();
                        addressline = addresses.get(0).getAddressLine(0);

                        log_lat = latitude+","+logitude;
                        loaction.setText(logitude + "\n" +
                                latitude + "\n" +
                                country + "\n" +
                                countryCode + "\n" +
                                Locality + "\n" +
                                addressline);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void setCardView(){
        if(!check){
            cardView.setVisibility(View.VISIBLE);
            check = true;
            fab.setEnabled(false);
        }else {
            cardView.setVisibility(View.GONE);
            check = false;
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

}