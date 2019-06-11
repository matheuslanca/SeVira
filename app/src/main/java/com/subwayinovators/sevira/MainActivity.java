package com.subwayinovators.sevira;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import subwayinovators.sevira.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView fabReport;
    private CircleImageView imgProfile;
    private FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();



        // FLOATING ACTION BUTTON
        fabReport = (ImageView) findViewById(R.id.fabReport);
        imgProfile = (CircleImageView) findViewById(R.id.imgSettings);

        if(auth.getCurrentUser() != null){
            final FirebaseUser user = auth.getCurrentUser();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            final UserInformation userInformation = new UserInformation();
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userInformation.setProfilepic(dataSnapshot.child("Usuarios").child(user.getUid()).getValue(UserInformation.class).getProfilepic());

                    if(!userInformation.getProfilepic().equals("")) {
                        Picasso.get().load(userInformation.getProfilepic()).into(imgProfile);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            databaseReference.addListenerForSingleValueEvent(valueEventListener);

        }

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
