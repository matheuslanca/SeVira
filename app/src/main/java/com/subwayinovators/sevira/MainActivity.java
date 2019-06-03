package com.subwayinovators.sevira;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;
import subwayinovators.sevira.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton fabReport;
    private CircleImageView imgProfile;
    private FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null){

        }


        // FLOATING ACTION BUTTON
        fabReport = (FloatingActionButton) findViewById(R.id.fabReport);
        imgProfile = (CircleImageView) findViewById(R.id.imgSettings);

        fabReport.setOnClickListener(this);
        imgProfile.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == fabReport){
            startActivity(new Intent(MainActivity.this, FirstReport.class));
        }

        if(v == imgProfile){
            if(auth.getCurrentUser() != null){
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            } else {
                Toast.makeText(this, "Faça login para acessar o seu perfil", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        }

    }
}
