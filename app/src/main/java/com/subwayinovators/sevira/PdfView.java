package com.subwayinovators.sevira;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

import subwayinovators.sevira.R;

public class PdfView extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);


        String pdf = getIntent().getStringExtra("pdf");
        webView = findViewById(R.id.webview);

        if(pdf.equals("sobre")){
            String urlSobre = "https://drive.google.com/open?id=1prYQ0NSUNZzIGFN8zZLvffCin1wZ49R-";
            webView.loadUrl(urlSobre);
        } else if(pdf.equals("termos")){
            String urlTermos = "https://drive.google.com/open?id=1cgt9f1pGpJWocrU4Zn_5IXN0hmBqAZueaAkbW2WcHU4";
            webView.loadUrl(urlTermos);
        } else {
            Toast.makeText(this, "Ocorreu um erro", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
