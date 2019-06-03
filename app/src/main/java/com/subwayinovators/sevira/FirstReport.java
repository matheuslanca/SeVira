package com.subwayinovators.sevira;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import subwayinovators.sevira.R;

public class FirstReport extends AppCompatActivity {

    Button btnAzul;
    Button btnVerde;
    Button btnAmarelo;
    Button btnVermelho;
    FloatingActionButton fabBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_report);

        final Intent intent = new Intent(FirstReport.this, SecondReport.class);

        btnAzul = (Button)findViewById(R.id.btnAzul);
        btnAzul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("linha1", "Azul");
                startActivity(intent);
                finish();
            }
        });

        btnVerde = (Button)findViewById(R.id.btnVerde);
        btnVerde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("linha1", "Verde");
                startActivity(intent);
                finish();

            }
        });

        btnVermelho = (Button)findViewById(R.id.btnVermelho);
        btnVermelho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("linha1", "Vermelho");
                startActivity(intent);
                finish();
            }
        });

        btnAmarelo = (Button)findViewById(R.id.btnAmarelo);
        btnAmarelo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("linha1", "Amarelo");
                startActivity(intent);
                finish();
            }
        });

        fabBack = (FloatingActionButton)findViewById(R.id.fabBack);
        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstReport.this, MainActivity.class));
                finish();
            }
        });
    }


}
