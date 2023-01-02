package com.example.clddv13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class diseaseDescription extends AppCompatActivity {

    private TextView diseaseTitle, description;
    private ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_description);
        diseaseTitle = findViewById(R.id.txtTitle);
        description = findViewById(R.id.txtDesc);
        img = findViewById(R.id.leaf_thumbnail);

        Intent intent = getIntent();
        String title = intent.getExtras().getString("Title");
        String dec = intent.getExtras().getString("Description");
        int image = intent.getExtras().getInt("Thumbnail");

        diseaseTitle.setText(title);
        description.setText(dec);
        img.setImageResource(image);
    }
}