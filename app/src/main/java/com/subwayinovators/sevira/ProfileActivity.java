package com.subwayinovators.sevira;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;
import subwayinovators.sevira.R;

public class ProfileActivity extends AppCompatActivity {

    private final int levelSize = 250;

    // Criando elementos do XML
    private ImageButton btnBack;
    private CircleImageView btnSettings;
    private ImageView imgProfilePic, imgLinhaFav, imgSituacaoLinhaFav;
    private ProgressBar pbPontuacao;
    private TextView txtLevelPb, txtLevel, txtProgress, txtScore, txtUsername, txtLinhaFav, txtSituacaoLinhaFav, txtHoraLinhaFav;



    // Criando elementos do BD
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;

    // Objeto do usuário para melhor organização
    private UserInformation userInformation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userInformation = new UserInformation();

        // Instanciando elementos do XML
        btnBack = findViewById(R.id.imgBack);
        btnSettings = findViewById(R.id.imgSettings);
        imgProfilePic = findViewById(R.id.imgProfilePic);
        pbPontuacao = findViewById(R.id.pbPontuacao);
        txtLevelPb = findViewById(R.id.txtLevelPb);
        txtLevel = findViewById(R.id.txtLevel);
        txtProgress = findViewById(R.id.txtProgress);
        txtScore = findViewById(R.id.txtScore);
        txtUsername = findViewById(R.id.txtUsername);

        // Elementos de linha favorita
        imgLinhaFav = findViewById(R.id.imgLinhaFav);
        imgSituacaoLinhaFav = findViewById(R.id.imgSituacaoLinhaFav);
        txtLinhaFav = findViewById(R.id.txtLinhaFav);
        txtSituacaoLinhaFav = findViewById(R.id.txtSituacaoLinhaFav);
        txtHoraLinhaFav = findViewById(R.id.txtHoraLinhaFav);
        layoutLinha

        // Inicializando elementos do BD
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Instanciando todos os atributos do usuário
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userInformation.setUsername(dataSnapshot.child("Usuarios").child(user.getUid()).getValue(UserInformation.class).getUsername());
                userInformation.setPontuacao(dataSnapshot.child("Usuarios").child(user.getUid()).getValue(UserInformation.class).getPontuacao());
                userInformation.setEmail(dataSnapshot.child("Usuarios").child(user.getUid()).getValue(UserInformation.class).getEmail());
                userInformation.setLevel(dataSnapshot.child("Usuarios").child(user.getUid()).getValue(UserInformation.class).getLevel());
                userInformation.setLinhaFavorita(dataSnapshot.child("Usuarios").child(user.getUid()).getValue(UserInformation.class).getLinhaFavorita());
                userInformation.setProfilepic(dataSnapshot.child("Usuarios").child(user.getUid()).getValue(UserInformation.class).getProfilepic());


                String level = 1 + userInformation.getPontuacao() / levelSize + "";
                String pontuacao = userInformation.getPontuacao() + "";

                // Atribuindo valores pra todos TextView
                txtLevel.setText(level);
                txtLevelPb.setText(level);
                int myProgress = userInformation.getPontuacao() % levelSize;
                int myLevel = 1 +((userInformation.getPontuacao() - myProgress) / levelSize);
                databaseReference.child("Usuarios").child(user.getUid()).child("level").setValue(myLevel);
                String fullProgress = myProgress + "/" + levelSize;
                txtProgress.setText(fullProgress);
                txtScore.setText(pontuacao);
                txtUsername.setText(userInformation.getUsername());

                pbPontuacao.setMax(levelSize);
                pbPontuacao.setProgress(myProgress);

                int linhaFav = userInformation.getLinhaFavorita();
                setLinhaFavorita(linhaFav);


                if(!userInformation.getProfilepic().equals("")) {
                    Picasso.get().load(userInformation.getProfilepic()).into(imgProfilePic);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addListenerForSingleValueEvent(valueEventListener);


        // Atribuindo listeners para os botões
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });





    }

    public void setLinhaFavorita(int linha){
        switch(linha){
            case 1:
                imgLinhaFav.setImageDrawable(getDrawable(R.drawable.linha_azul));
//                imgSituacaoLinhaFav
                txtLinhaFav.setText(R.string.linha_azul);
//                txtSituacaoLinhaFav
//                txtHoraLinhaFav
                break;

            case 2:
                imgLinhaFav.setImageDrawable(getDrawable(R.drawable.linha_verde));
//                imgSituacaoLinhaFav
                txtLinhaFav.setText(R.string.linha_verde);
//                txtSituacaoLinhaFav
//                txtHoraLinhaFav
                break;

            case 3:
                imgLinhaFav.setImageDrawable(getDrawable(R.drawable.linha_vermelha));
//                imgSituacaoLinhaFav
                txtLinhaFav.setText(R.string.linha_vermelha);
//                txtSituacaoLinhaFav
//                txtHoraLinhaFav
                break;

            case 4:
                imgLinhaFav.setImageDrawable(getDrawable(R.drawable.linha_amarela));
//                imgSituacaoLinhaFav
                txtLinhaFav.setText(R.string.linha_azul);
//                txtSituacaoLinhaFav
//                txtHoraLinhaFav
                break;

            default:
                imgLinhaFav.setImageDrawable(null);
                imgSituacaoLinhaFav.setImageDrawable(null);
                txtLinhaFav.setText("");
                txtSituacaoLinhaFav.setText("");
                txtHoraLinhaFav.setText("");




        }
    }
}