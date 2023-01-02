package com.example.clddv13.maps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.example.clddv13.DisplayBoard;
import com.example.clddv13.LogIn.loggingIn;
import com.example.clddv13.R;
import com.example.clddv13.customClasses.dataPoint;
import com.example.clddv13.remote.APIUtils;
import com.example.clddv13.remote.FileService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.macasaet.fernet.Key;
import com.macasaet.fernet.StringValidator;
import com.macasaet.fernet.Token;
import com.macasaet.fernet.Validator;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.MinimapOverlay;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay2;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.views.overlay.simplefastpoint.LabelledGeoPoint;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlay;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlayOptions;
import org.osmdroid.views.overlay.simplefastpoint.SimplePointTheme;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class mapActivity extends AppCompatActivity {

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    private MyLocationNewOverlay mLocationOverlay;
    private RotationGestureOverlay mRotationGestureOverlay;
    private ScaleBarOverlay mScaleBarOverlay;
    private MinimapOverlay mMinimapOverlay;
    private FloatingActionButton floatingActionButton;
    private List<dataPoint> locationPointers;
    protected final Key key_global = new Key("FxYKToh3BFHavV4wLwMECOKfleABjcqQXEBPo-fekZ0=");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getSupportActionBar().hide();

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        floatingActionButton = findViewById(R.id.actionCenter);

        map = (MapView) findViewById(R.id.mapId);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(9.5);
        GeoPoint startPoint = new GeoPoint(48.8583, 2.2944);
        mapController.setCenter(startPoint);
        requestPermissionsIfNecessary(new String[]{
                // if you need to show the current location, uncomment the line below
                // Manifest.permission.ACCESS_FINE_LOCATION,
                // WRITE_EXTERNAL_STORAGE is required in order to show the map
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });

        ///logitude and latitude overlay
//        LatLonGridlineOverlay2 overlay = new LatLonGridlineOverlay2();
//        map.getOverlays().add(overlay);

        //Location overlay
        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getApplicationContext()), map);
        this.mLocationOverlay.enableMyLocation();
        map.getOverlays().add(this.mLocationOverlay);

        //Rotation Gestures
        mRotationGestureOverlay = new RotationGestureOverlay(getApplicationContext(), map);
        mRotationGestureOverlay.setEnabled(true);
        map.setMultiTouchControls(true);
        map.getOverlays().add(this.mRotationGestureOverlay);

        //map scale bar overlay
        final Context context = getApplicationContext();
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        mScaleBarOverlay = new ScaleBarOverlay(map);
        mScaleBarOverlay.setCentred(true);
        //play around with these values to get the location on screen in the right place for your application
        mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        map.getOverlays().add(this.mScaleBarOverlay);


        zoomToMyLocation();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomToMyLocation();
            }
        });
        MapLocRetrofit2Api();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                setGPSmarkers();
            }
        };

        new Handler().postDelayed(runnable,1000);


        //        // create 10k labelled points
//        // in most cases, there will be no problems of displaying >100k points, feel free to try
//        List<IGeoPoint> points = new ArrayList<>();
//        for (int i = 0; i < 10000; i++) {
//            points.add(new LabelledGeoPoint(37 + Math.random() * 5, -8 + Math.random() * 5
//                    , "Point #" + i));
//        }
//
//        // wrap them in a theme
//        SimplePointTheme pt = new SimplePointTheme(points, true);
//
//        // create label style
//        Paint textStyle = new Paint();
//        textStyle.setStyle(Paint.Style.FILL);
//        textStyle.setColor(Color.parseColor("#0000ff"));
//        textStyle.setTextAlign(Paint.Align.CENTER);
//        textStyle.setTextSize(24);
//
//        // set some visual options for the overlay
//        // we use here MAXIMUM_OPTIMIZATION algorithm, which works well with >100k points
//        SimpleFastPointOverlayOptions opt = SimpleFastPointOverlayOptions.getDefaultStyle()
//                .setAlgorithm(SimpleFastPointOverlayOptions.RenderingAlgorithm.MAXIMUM_OPTIMIZATION)
//                .setRadius(7).setIsClickable(true).setCellSize(15).setTextStyle(textStyle);
//
//        // create the overlay with the theme
//        final SimpleFastPointOverlay sfpo = new SimpleFastPointOverlay(pt, opt);
//
//        // onClick callback
//        sfpo.setOnClickListener(new SimpleFastPointOverlay.OnClickListener() {
//            @Override
//            public void onClick(SimpleFastPointOverlay.PointAdapter points, Integer point) {
//                Toast.makeText(map.getContext()
//                        , "You clicked " + ((LabelledGeoPoint) points.get(point)).getLabel()
//                        , Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // add overlay
//        map.getOverlays().add(sfpo);

    }

    private void setGPSmarkers(){
        // create 10k labelled points
        // in most cases, there will be no problems of displaying >100k points, feel free to try
        List<Marker> markers = new ArrayList<>();
        for (int i = 0; i < locationPointers.size(); i++) {
            markers.add(new Marker(map));
            markers.get(i).setPosition(new GeoPoint(locationPointers.get(i).getLongitude(),locationPointers.get(i).getLatitude()));
            markers.get(i).setTitle(locationPointers.get(i).getDataType());
            markers.get(i).setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM);

            markers.get(i).setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    mapView.getController().animateTo(marker.getPosition());
                    marker.showInfoWindow();

                    List<GeoPoint> circle = Polygon.pointsAsCircle(marker.getPosition(),4.3f);
                    Polygon p = new Polygon(mapView);
                    p.setPoints(circle);
                    p.setTitle("Possible Spread Diameter");
                    mapView.getOverlayManager().add(p);
                    mapView.invalidate();
                    return false;
                }
            });

            map.getOverlays().add(markers.get(i));
        }


    }
    private void MapLocRetrofit2Api() {
        FileService fileService = APIUtils.getFileService();
        Call<List<dataPoint>> call = fileService.getLocationPoints();
        call.enqueue(new Callback<List<dataPoint>>() {
            @Override
            public void onResponse(Call<List<dataPoint>> call, Response<List<dataPoint>> response) {
                if (!response.isSuccessful()){
                    //errorText.setText("Code: "+response.code());
                    return;
                }
                List<dataPoint> points = response.body();
                locationPointers = points;
                decrypt();

            }

            @Override
            public void onFailure(Call<List<dataPoint>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Response Failed",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void decrypt() {

        for(int i=0; i< locationPointers.size();i++){
            final Token token_lat = Token.fromString(locationPointers.get(i).getLat());
            final Token token_long = Token.fromString(locationPointers.get(i).getLongs());
            final Token token_type = Token.fromString(locationPointers.get(i).getDataType());
            final Validator<String> validator = new StringValidator() {
            };

            final String payload_lat = token_lat.validateAndDecrypt(key_global, validator);
            final String payload_long = token_long.validateAndDecrypt(key_global, validator);
            final String payload_type = token_type.validateAndDecrypt(key_global,validator);

            locationPointers.get(i).setLatitude(Double.parseDouble(payload_lat));
            locationPointers.get(i).setLongitude(Double.parseDouble(payload_long));
            locationPointers.get(i).setDataType(payload_type);
        }


    }

    private void zoomToMyLocation(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //map.getController().setCenter(new GeoPoint(new GeoPoint(mLocationOverlay.getMyLocation().getLatitude(), mLocationOverlay.getMyLocation().getLongitude())));
                map.getController().animateTo(mLocationOverlay.getMyLocation());
                map.getController().setZoom(18);
            }
        },500);
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }


}