package com.subwayinovators.sevira;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import java.util.ArrayList;
import java.util.LinkedHashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import subwayinovators.sevira.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton fabReport;
    private CircleImageView imgProfile;
    private FirebaseAuth auth;

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;

    private ArrayList<Linha> linhas;

    private TextView txtHorarioAzul, txtHorarioVerde, txtHorarioVermelha, txtHorarioAmarela;
    private TextView txtSituacaoLinhaAzul, txtSituacaoLinhaVerde, txtSituacaoLinhaVermelha, txtSituacaoLinhaAmarela, txtAzul, txtVerde, txtVermelha, txtAmarela;
    private ImageView imgSituacaoLinhaAzul, imgSituacaoLinhaVerde, imgSituacaoLinhaVermelha, imgSituacaoLinhaAmarela;
    private RelativeLayout layoutLinhaAzul, layoutLinhaVerde, layoutLinhaVermelha, layoutLinhaAmarela;
    private ImageView imgAzul, imgVerde, imgVermelha, imgAmarela;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);

        linhas = new ArrayList<Linha>();

        txtHorarioAzul = findViewById(R.id.txtHorarioAzul);
        txtHorarioVerde = findViewById(R.id.txtHorarioVerde);
        txtHorarioVermelha = findViewById(R.id.txtHorarioVermelha);
        txtHorarioAmarela = findViewById(R.id.txtHorarioAmarela);
        txtSituacaoLinhaAzul = findViewById(R.id.txtSituacaoLinhaAzul);
        txtSituacaoLinhaVerde = findViewById(R.id.txtSituacaoLinhaVerde);
        txtSituacaoLinhaVermelha = findViewById(R.id.txtSituacaoLinhaVermelha);
        txtSituacaoLinhaAmarela = findViewById(R.id.txtSituacaoLinhaAmarela);
        imgSituacaoLinhaAzul = findViewById(R.id.imgSituacaoLinhaAzul);
        imgSituacaoLinhaVerde = findViewById(R.id.imgSituacaoLinhaVerde);
        imgSituacaoLinhaVermelha = findViewById(R.id.imgSituacaoLinhaVermelha);
        imgSituacaoLinhaAmarela = findViewById(R.id.imgSituacaoLinhaAmarela);
        layoutLinhaAzul = findViewById(R.id.layoutLinhaAzul);
        layoutLinhaVerde = findViewById(R.id.layoutLinhaVerde);
        layoutLinhaVermelha = findViewById(R.id.layoutLinhaVermelha);
        layoutLinhaAmarela = findViewById(R.id.layoutLinhaAmarela);
        imgAzul = findViewById(R.id.imgLinhaAzul);
        imgVerde = findViewById(R.id.imgLinhaVerde);
        imgVermelha = findViewById(R.id.imgLinhaVermelha);
        imgAmarela = findViewById(R.id.imgLinhaAmarela);
        txtAzul = findViewById(R.id.txtLinhaAzul);
        txtVerde = findViewById(R.id.txtLinhaVerde);
        txtVermelha = findViewById(R.id.txtLinhaVermelha);
        txtAmarela = findViewById(R.id.txtLinhaAmarela);

        layoutLinhaAzul.setOnClickListener(this);
        layoutLinhaVerde.setOnClickListener(this);
        layoutLinhaVermelha.setOnClickListener(this);
        layoutLinhaAmarela.setOnClickListener(this);


        // FLOATING ACTION BUTTON
        fabReport = (FloatingActionButton) findViewById(R.id.fabReport);
        imgProfile = (CircleImageView) findViewById(R.id.imgSettings);

        if(auth.getCurrentUser() != null){
            final FirebaseUser user = auth.getCurrentUser();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            final UserInformation userInformation = new UserInformation();
            ValueEventListener valueListener = new ValueEventListener() {
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
            databaseReference.addListenerForSingleValueEvent(valueListener);

        }
        fabReport.setOnClickListener(this);
        imgProfile.setOnClickListener(this);




        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(int i = 1; i <= 4; i++){
                    Linha l = new Linha();
                    String index = i + "";
                    l.setCor(dataSnapshot.child("Linhas").child(index).getValue(Linha.class).getCor());
                    l.setEstacaoReport(dataSnapshot.child("Linhas").child(index).getValue(Linha.class).getEstacaoReport());
                    l.setHorario(dataSnapshot.child("Linhas").child(index).getValue(Linha.class).getHorario());
                    l.setHorario(l.horario.replace(" ", "\n"));
                    l.setSituacao(dataSnapshot.child("Linhas").child(index).getValue(Linha.class).getSituacao());
                    linhas.add(l);

                    int j = i - 1;

                    switch(i){
                        case 1:
                            txtHorarioAzul.setText(linhas.get(j).horario);
                            txtSituacaoLinhaAzul.setText(linhas.get(j).situacao);
                            if(!txtSituacaoLinhaAzul.getText().equals("Operando Normalmente")){
                                imgSituacaoLinhaAzul.setVisibility(View.VISIBLE);
                                imgSituacaoLinhaAzul.setImageResource(R.drawable.estado2);
                            } else {
                                imgSituacaoLinhaAzul.setVisibility(View.INVISIBLE);
                            }
                            break;

                        case 2:
                            txtHorarioVerde.setText(linhas.get(j).horario);
                            txtSituacaoLinhaVerde.setText(linhas.get(j).situacao);
                            if(!txtSituacaoLinhaVerde.getText().equals("Operando Normalmente")){
                                imgSituacaoLinhaVerde.setVisibility(View.VISIBLE);
                                imgSituacaoLinhaVerde.setImageResource(R.drawable.estado2);
                            } else {
                                imgSituacaoLinhaVerde.setVisibility(View.INVISIBLE);
                            }
                            break;

                        case 3:
                            txtHorarioVermelha.setText(linhas.get(j).horario);
                            txtSituacaoLinhaVermelha.setText(linhas.get(j).situacao);
                            if(!txtSituacaoLinhaVermelha.getText().equals("Operando Normalmente")){
                                imgSituacaoLinhaVermelha.setVisibility(View.VISIBLE);
                                imgSituacaoLinhaVermelha.setImageResource(R.drawable.estado2);
                            } else {
                                imgSituacaoLinhaVermelha.setVisibility(View.INVISIBLE);
                            }
                            break;

                        case 4:
                            txtHorarioAmarela.setText(linhas.get(j).horario);
                            txtSituacaoLinhaAmarela.setText(linhas.get(j).situacao);
                            if(!txtSituacaoLinhaAmarela.getText().equals("Operando Normalmente")){
                                imgSituacaoLinhaAmarela.setVisibility(View.VISIBLE);
                                imgSituacaoLinhaAmarela.setImageResource(R.drawable.estado2);
                            } else {
                                imgSituacaoLinhaAmarela.setVisibility(View.INVISIBLE);
                            }
                            break;

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(valueEventListener);





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

        Intent i = new Intent(MainActivity.this, ReportsActivity.class);
        if (v == layoutLinhaVermelha || v == imgSituacaoLinhaVermelha || v == txtHorarioVermelha || v == txtSituacaoLinhaVermelha || v == imgVermelha || v == txtVermelha) {
            i.putExtra("linha1", "Vermelha");
            startActivity(i);
        } else if(v == layoutLinhaAmarela || v == imgSituacaoLinhaAmarela || v == txtHorarioAmarela || v == txtSituacaoLinhaAmarela|| v == imgAmarela || v == txtAmarela) {
            i.putExtra("linha1", "Amarela");
            startActivity(i);
        } else if(v == layoutLinhaVerde || v== imgSituacaoLinhaVerde || v == txtHorarioVerde || v == txtSituacaoLinhaVerde || v == imgVerde || v == txtVerde){
            i.putExtra("linha1", "Verde");
            startActivity(i);
        } else if(v == layoutLinhaAzul || v == imgSituacaoLinhaAzul || v == txtHorarioAzul || v == txtSituacaoLinhaAzul || v == imgAzul || v == txtAzul){
            i.putExtra("linha1", "Azul");
            startActivity(i);
        }


    }

}