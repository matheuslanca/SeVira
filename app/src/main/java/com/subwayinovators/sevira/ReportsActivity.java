package com.subwayinovators.sevira;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;


import subwayinovators.sevira.R;

public class ReportsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> report;
    ArrayList<String> horario;
    ArrayList<String> estacao;
    FloatingActionButton fabBack;
    int nlinha;
    String linha;
    RecyclerAdapterReports adapter;
    ImageView imgBack;
    RelativeLayout corLinha;
    TextView txtLinha;
    int color = 0;

    // Elementos do firebase
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        linha = getIntent().getExtras().getString("linha1");
        corLinha = findViewById(R.id.corLinha);
        txtLinha = findViewById(R.id.txtLinha);
        txtLinha.setText("Linha " + linha);
        nlinha = getIntLinha(linha);

        estacao = new ArrayList<>();
        report = new ArrayList<>();
        horario = new ArrayList<>();

        //
        // Definir valores para report, horario, estacao, de acordo com as info do BD
        //

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Linhas").child(nlinha + "");
        databaseReference.keepSynced(true);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot childSnapshot : dataSnapshot.child("reports").getChildren()) {
//                    r.setEstacao(childSnapshot.getValue(ReportInformation.class).getEstacao());
//                    r.setHora(childSnapshot.getValue(ReportInformation.class).getHora());
//                    r.setReport(childSnapshot.getValue(ReportInformation.class).getReport());

                    report.add(childSnapshot.getValue(ReportInformation.class).getReport());
                    horario.add(childSnapshot.getValue(ReportInformation.class).getHora());
                    estacao.add(childSnapshot.getValue(ReportInformation.class).getEstacao());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        databaseReference.addListenerForSingleValueEvent(valueEventListener);


        recyclerView = findViewById(R.id.listaEstacoes);
        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapterReports(report, horario, estacao, nlinha, color);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);




        fabBack = (FloatingActionButton)findViewById(R.id.fabReport);
        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportsActivity.this, SecondReport.class);
                intent.putExtra("linha1", linha);
                startActivity(intent);
                finish();
            }
        });

        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReportsActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private int getIntLinha(String nlinha) {
        switch(nlinha){
            case "Azul":
                color = getResources().getColor(R.color.linha_azul);
                corLinha.setBackgroundColor(color);
                return 1;

            case "Verde":
                color = getResources().getColor(R.color.linha_verde);
                corLinha.setBackgroundColor(color);
                return 2;

            case "Vermelha":
                color = getResources().getColor(R.color.linha_vermelha);
                corLinha.setBackgroundColor(color);
                return 3;

            case "Amarela":
                color = getResources().getColor(R.color.linha_amarela);
                corLinha.setBackgroundColor(color);
                return 4;

            default:
                color = getResources().getColor(R.color.linha_azul);
                corLinha.setBackgroundColor(color);
                return 1;
        }

    }

}