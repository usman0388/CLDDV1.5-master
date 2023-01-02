package com.example.clddv13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.clddv13.SigningIn.SignUp;
import com.example.clddv13.remote.APIUtils;
import com.example.clddv13.remote.FileService;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class signUp extends AppCompatActivity {

    private EditText username;
    private EditText phone;
    private EditText password;
    private EditText confirmPassword;
    private Button signingIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        username = findViewById(R.id.userSignUp);
        phone = findViewById(R.id.SignUpNumber);
        password = findViewById(R.id.SignUpPassword);
        confirmPassword = findViewById(R.id.SignUpConfirmPassword2);
        signingIn = findViewById(R.id.SignUpServer);

        signingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.length()>0 && phone.length()==11 && password.length()>0 && confirmPassword.length()>0){
                    if(checkNumber(v)){
                        phone.setBackgroundResource(R.drawable.rounded_corner);
                        if(ValidatePassword(v)){
                            SetDefaultDrawable();

                            registerUser(username.getText().toString(),phone.getText().toString(),password.getText().toString(),confirmPassword.getText().toString());

                            Toast.makeText(signUp.this,"Signing in Successful!\n Go to the log in page to log in.", Toast.LENGTH_SHORT).show();

                        }
                    }else {
                        ValidatePassword(v);
                    }

                }else {
                    checkAllAndChange();

                }
            }

            private void checkAllAndChange(){
                if(!(username.length()>0)){
                    username.setBackgroundResource(R.drawable.incorrect_border);
                }else {
                    username.setBackgroundResource(R.drawable.rounded_corner);
                }
                if(!(phone.length()==11)){
                    phone.setBackgroundResource(R.drawable.incorrect_border);
                }else {
                    phone.setBackgroundResource(R.drawable.rounded_corner);
                }
                if(!(password.length()>0)){
                    password.setBackgroundResource(R.drawable.incorrect_border);
                }else{
                    password.setBackgroundResource(R.drawable.rounded_corner);
                }
                if(!(confirmPassword.length()>0)){
                    confirmPassword.setBackgroundResource(R.drawable.incorrect_border);
                }else {
                    confirmPassword.setBackgroundResource(R.drawable.rounded_corner);
                }
            }
            private void SetDefaultDrawable(){
                password.setBackgroundResource(R.drawable.rounded_corner);
                confirmPassword.setBackgroundResource(R.drawable.rounded_corner);
                phone.setBackgroundResource(R.drawable.rounded_corner);
                username.setBackgroundResource(R.drawable.rounded_corner);
            }

            private boolean ValidatePassword(View v){
                if(password.getText().toString().equals(
                        confirmPassword.getText().toString()
                )){
                    return true;
                }else {
                    password.setBackgroundResource(R.drawable.incorrect_border);
                    confirmPassword.setBackgroundResource(R.drawable.incorrect_border);
                    Snackbar.make(v, "Passwords Don't Match!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return false;
                }
            }
            private boolean checkNumber(View v) {
                String regexStr = "(03+[0-9]{9})";
                String number=phone.getText().toString();
                if(number.matches(regexStr)){
                    return true;
                }else {
                    phone.setBackgroundResource(R.drawable.incorrect_border);
                    Snackbar.make(v, "Incorrect number format", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                return false;
            }
        });
    }

    private void registerUser(String userId, String userPhone, String userPass, String userConfirmPass) {
        FileService fileService = APIUtils.getFileService();
        Call<SignUp> call = fileService.register(userId, userPass, userPhone, userConfirmPass);
        call.enqueue(new Callback<SignUp>() {
            @Override
            public void onResponse(Call<SignUp> call, Response<SignUp> response) {
                if(!response.isSuccessful()){
                    return;
                }
                SignUp res = response.body();
                String rez = res.getUsername();
                if(rez.equals("Already Exists")){
                    Toast.makeText(getApplicationContext(),rez,Toast.LENGTH_LONG).show();
                }if(rez.equals("Success")){
                    Toast.makeText(getApplicationContext(),"User Registered",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(),DisplayBoard.class);
                    intent.putExtra("UserName",userId);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<SignUp> call, Throwable t) {

            }
        });

    }
}