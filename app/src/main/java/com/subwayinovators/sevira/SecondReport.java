package com.subwayinovators.sevira;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import subwayinovators.sevira.R;


public class SecondReport extends AppCompatActivity {

    FloatingActionButton fabBack, fabNext;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<String> list;
    RecyclerAdapter adapter;
    int nlinha = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_report);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listaEstacoes);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final String linha = getIntent().getExtras().getString("linha1");

        if("Azul".equals(linha)){
            list = Arrays.asList(getResources().getStringArray(R.array.estacoes_linhaazul));
            nlinha = 1;
        } else if("Verde".equals(linha)){
            list = Arrays.asList(getResources().getStringArray(R.array.estacoes_linhaverde));
            nlinha = 2;
        } else if("Vermelho".equals(linha)){
            list = Arrays.asList(getResources().getStringArray(R.array.estacoes_linhavermelha));
            nlinha = 3;
        } else if("Amarelo".equals(linha)){
            list = Arrays.asList(getResources().getStringArray(R.array.estacoes_linhaamarela));
            nlinha = 4;
        } else {
            list = null;
            Toast.makeText(this, "Ocorreu um erro", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SecondReport.this, MainActivity.class));
        }

        adapter = new RecyclerAdapter(list, nlinha);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(SecondReport.this, ThirdReport.class);
                i.putExtra("linha", linha + "");
                i.putExtra("idestacao", position + "");
                Toast.makeText(getApplicationContext(), position + "", Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
        }));


        fabBack = (FloatingActionButton)findViewById(R.id.fabBack);
        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondReport.this, FirstReport.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SecondReport.this, FirstReport.class));
    }
}
