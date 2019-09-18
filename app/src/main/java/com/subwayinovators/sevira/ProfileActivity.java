package com.subwayinovators.sevira;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import subwayinovators.sevira.R;

public class ProfileActivity extends AppCompatActivity {

    private final int levelSize = 250;

    // Criando elementos do XML
    private ImageView btnBack;
    private CircleImageView btnSettings;
    private ImageView imgProfilePic;
    private ProgressBar pbPontuacao;
    private TextView txtLevelPb, txtLevel, txtProgress, txtScore, txtUsername;

    // Criando elementos do BD
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;

    // Objeto do usuário para melhor organização
    private UserInformation userInformation;

    ValueEventListener valueEventListener1;
    DatabaseReference databaseReference1;
    ArrayList<String> report;
    ArrayList<String> horario;
    ArrayList<Integer> cor;
    ArrayList<Integer> linhas;
    ArrayList<String> estacao;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerAdapterReports adapter;


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
        recyclerView = findViewById(R.id.recyclerView);

        // Inicializando elementos do BD
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Instanciando todos os atributos do usuário
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.child("Usuarios").child(auth.getUid()).child("Denuncias").getChildren()) {
                    report.add(childSnapshot.getValue(ReportInformation.class).getReport());
                    horario.add(childSnapshot.getValue(ReportInformation.class).getHora());
                    estacao.add(childSnapshot.getValue(ReportInformation.class).getEstacao());
                    String id = (childSnapshot.getValue(ReportInformation.class).getId());
                    linhas.add(Character.getNumericValue(id.charAt(17)));
                    cor.add(getIntLinha(Character.getNumericValue(id.charAt(17))));
                }

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


                if(!userInformation.getProfilepic().equals("")) {
                    Picasso.get().load(userInformation.getProfilepic()).into(imgProfilePic);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addListenerForSingleValueEvent(valueEventListener);

        report = new ArrayList<String>();
        horario = new ArrayList<String>();
        estacao = new ArrayList<String>();
        cor = new ArrayList<Integer>();
        linhas = new ArrayList<Integer>();


        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapterReports(report, horario, estacao, cor, linhas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);



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

    private int getIntLinha(int nlinha) {
        switch(nlinha){
            case 1:
                int x = getResources().getColor(R.color.linha_azul);
                return x;

            case 2:
                int y = getResources().getColor(R.color.linha_verde);
                return y;

            case 3:
                int z = getResources().getColor(R.color.linha_vermelha);
                return z;

            case 4:
                int a = getResources().getColor(R.color.linha_amarela);
                return a;
        }

        return 0;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProfileActivity.this, FirstReport.class));
        finish();
    }

}