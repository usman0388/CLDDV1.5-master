package com.example.clddv13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN = 5000;
    Animation RightOne, LeftOne, BottomAnim;
    TextView textCL;
    TextView textDD;
    ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        RightOne = AnimationUtils.loadAnimation(this,R.anim.text_anim_right);
        LeftOne = AnimationUtils.loadAnimation(this,R.anim.text_anim_left);
        BottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_anim);

        textCL = findViewById(R.id.splashCL);
        textDD = findViewById(R.id.splashDD);
        logo = findViewById(R.id.logo);



        textCL.setAnimation(LeftOne);
        textDD.setAnimation(RightOne);

        logo.setAnimation(BottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,Login.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}