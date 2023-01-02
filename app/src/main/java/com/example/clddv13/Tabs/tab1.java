package com.example.clddv13.Tabs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.clddv13.R;
import com.example.clddv13.imageView;

import static android.app.Activity.RESULT_OK;

public class tab1 extends Fragment {

    private String User;
    public tab1(String User) {
        this.User = User;
        // Required empty public constructor
    }

    private static final int GALLERY_REQUEST = 789;
    ImageView galImage;
    private Uri fileUri;
    private static final int STORAGE_PERMISSION_CODE = 103;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);
        galImage = view.findViewById(R.id.galleryBtn);

        galImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission()){
                    openGallery();
                }

            }
        });

        return view;
    }

    private boolean checkPermission(){

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(),new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },STORAGE_PERMISSION_CODE);
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }else {
                return false;
            }
        }else {
            return true;
        }

    }
    private void openGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK);
        pickPhoto.setType("image/*");
        startActivityForResult(pickPhoto,GALLERY_REQUEST);

    }


    String imagePath ="";
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == GALLERY_REQUEST){
            if(data == null){
                Toast.makeText(getContext(),"Unable To chose Image!", Toast.LENGTH_LONG).show();
            }
            Uri image = data.getData();
            imagePath = getRealPathFromUri(image);//pass it along
            Intent intent = new Intent(getContext(), imageView.class);
            intent.putExtra("imageUri", image.toString());
            intent.putExtra("path",imagePath);
            intent.putExtra("check","gal");
            intent.putExtra("UserName",User);
            startActivity(intent);
        }
    }
    private String getRealPathFromUri(Uri uri){
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(getContext(),uri,projection,null,null,null);
        Cursor cursor = cursorLoader.loadInBackground();
        int column_idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_idx);
        cursor.close();
        return result;
    }
}