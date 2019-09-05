package com.subwayinovators.sevira;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import subwayinovators.sevira.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnUsername, btnFoto, btnLogout, btnTermos, btnSobre, btnExcluir;
    FirebaseAuth auth;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        auth = FirebaseAuth.getInstance();

        btnUsername = findViewById(R.id.btnUsername);
        btnFoto = findViewById(R.id.btnFoto);
        btnLogout = findViewById(R.id.btnLogout);
        btnTermos = findViewById(R.id.btnTermos);
        btnSobre = findViewById(R.id.btnSobre);
        btnExcluir = findViewById(R.id.btnExcluir);

        btnUsername.setOnClickListener(this);
        btnFoto.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnTermos.setOnClickListener(this);
        btnSobre.setOnClickListener(this);
        btnExcluir.setOnClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference();

    }


    @Override
    public void onClick(View v) {
        if(v == btnUsername){
            // alterar nome de usuário
            return;
        }

        if(v == btnExcluir) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage("Isso excluirá todos os seus dados");
//            builder.setTitle("Você tem certeza?");
//
//            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    databaseReference.child("Usuarios").child(auth.getCurrentUser().getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Toast.makeText(SettingsActivity.this, "Seus dados foram excluídos.", Toast.LENGTH_SHORT).show();
//                            auth.getCurrentUser().delete();
//                            if(auth.getCurrentUser() != null){
//                                auth.signOut();
//                            }
//                            startActivity(new Intent(SettingsActivity.this, LauncherActivity.class));
//                        }
//                    });
//                }
//            });
//            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//
//                }
//            });
//            AlertDialog dialog = builder.create();

            databaseReference.child("Usuarios").child(auth.getCurrentUser().getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(SettingsActivity.this, "Seus dados foram excluídos.", Toast.LENGTH_SHORT).show();
                            auth.getCurrentUser().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void aVoid) {
                                                                                        startActivity(new Intent(SettingsActivity.this, LauncherActivity.class));
                                                                                    }
                                                                                });

                        }
                    });

            return;
        }

        if(v == btnFoto) {
            // alterar foto
            return;
        }

        if(v == btnLogout){
            if(auth.getCurrentUser() != null){
                auth.signOut();

                Toast.makeText(SettingsActivity.this, "Logout efetuado com sucesso", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SettingsActivity.this, LauncherActivity.class));
            }
            return;
        }

        if(v == btnSobre){
            // abrir sobre
            return;
        }

        if(v == btnTermos){
            // abrir termos
            return;
        }

    }
}
