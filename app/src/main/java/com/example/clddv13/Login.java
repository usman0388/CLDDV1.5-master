package com.example.clddv13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clddv13.LogIn.loggingIn;
import com.example.clddv13.remote.APIUtils;
import com.example.clddv13.remote.FileService;
import com.google.android.material.snackbar.Snackbar;


import okio.ByteString;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.macasaet.fernet.Key;
import com.macasaet.fernet.Token;
import com.macasaet.fernet.StringValidator;
import com.macasaet.fernet.Validator;

public class Login extends AppCompatActivity {


    private Button login;
    private Button signUp;
    private EditText userName;
    private EditText password;
    private TextView errorText;
    protected final Key key_global = new Key("FxYKToh3BFHavV4wLwMECOKfleABjcqQXEBPo-fekZ0=");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        login = findViewById(R.id.logIn);
        signUp = findViewById(R.id.signUp);
        userName = findViewById(R.id.userLogIn);
        password = findViewById(R.id.userPassword);
        errorText = findViewById(R.id.error_text);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),signUp.class);
                startActivity(intent);
                SetDefaultDrawable();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userName.length()>0 && password.length()>0){
                    if(checkAllAndChange()){
                        final Token token_user = Token.generate(key_global, userName.getText().toString());
                        final Token token_pass = Token.generate(key_global, password.getText().toString());


//                        final Validator<String> validator = new StringValidator() {
//                        };
                        //final String payload = token.validateAndDecrypt(key_global, validator);

//                        loginRetrofit2Api(userName.getText().toString(), password.getText().toString());
                        loginRetrofit2Api(token_user.serialise(), token_pass.serialise());
                    }

                    userName.setText(null);
                    password.setText(null);
                }else{
                    checkAllAndChange();
                    Snackbar.make(v, "FILL ALL FIELDS TO CONTINUE!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });


    }

    private void loginRetrofit2Api(String userid, String pass) {
        final loggingIn logIn = new loggingIn(userid,pass);
        FileService fileService = APIUtils.getFileService();
        Call<loggingIn> call = fileService.logCheck(userid,pass);
        call.enqueue(new Callback<com.example.clddv13.LogIn.loggingIn>() {
            @Override
            public void onResponse(Call<com.example.clddv13.LogIn.loggingIn> call, Response<com.example.clddv13.LogIn.loggingIn> response) {
                if (!response.isSuccessful()){
                    //errorText.setText("Code: "+response.code());
                    return;
                }
                loggingIn res = response.body();
                String rez= res.getUserId();
                if(rez.equals("Incorrect")){
                    Toast.makeText(getApplicationContext(),"Invalid Credentials",Toast.LENGTH_LONG).show();
                }else if(rez.equals("Success")){
                    Toast.makeText(getApplicationContext(),"Response Success",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(),DisplayBoard.class);
                    intent.putExtra("UserName",userid);
                    startActivity(intent);
                    userName.setText(null);
                    password.setText(null);
                }
            }

            @Override
            public void onFailure(Call<com.example.clddv13.LogIn.loggingIn> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Response Failed",Toast.LENGTH_LONG).show();

            }
        });
    }

    private void SetDefaultDrawable() {
        userName.setBackgroundResource(R.drawable.rounded_corner);
        password.setBackgroundResource(R.drawable.rounded_corner);

    }

    private Boolean checkAllAndChange(){
        Boolean flag = false;
        if(!(userName.length()>0)){
            userName.setBackgroundResource(R.drawable.incorrect_border);
            flag = false;
        }else {
            userName.setBackgroundResource(R.drawable.rounded_corner);
            flag = true;

        }
        if(!(password.length()>0)){
            password.setBackgroundResource(R.drawable.incorrect_border);
            flag = false;

        }else{
            password.setBackgroundResource(R.drawable.rounded_corner);
            flag = true;
        }
        return flag;
    }
}