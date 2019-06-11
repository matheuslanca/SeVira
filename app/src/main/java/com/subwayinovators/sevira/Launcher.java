package com.subwayinovators.sevira;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import subwayinovators.sevira.R;

public class Launcher extends AppCompatActivity {

    private ImageView btnLogin, btnRegister;
    private TextView txtPular;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null){
            startActivity(new Intent(Launcher.this, MainActivity.class));
        }

        btnLogin = (ImageView) findViewById(R.id.btnLogin);
        btnRegister = (ImageView) findViewById(R.id.btnRegister);
        txtPular = (TextView) findViewById(R.id.txtPular);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Launcher.this, LoginActivity.class));
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Launcher.this, CadastroActivity.class));
                finish();
            }
        });

        txtPular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Launcher.this, MainActivity.class));
                finish();
            }
        });
    }
}
