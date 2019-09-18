package com.subwayinovators.sevira;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import subwayinovators.sevira.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnUsername, btnFoto, btnLogout, btnTermos, btnSobre, btnExcluir;
    ImageView imgBack;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    private String newUsername = "";

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
        imgBack = findViewById(R.id.imgBack);

        btnUsername.setOnClickListener(this);
        btnFoto.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnTermos.setOnClickListener(this);
        btnSobre.setOnClickListener(this);
        btnExcluir.setOnClickListener(this);
        imgBack.setOnClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference();

    }


    @Override
    public void onClick(View v) {
        if(v == btnUsername){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Insira um novo nome de usuário: ");

            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            builder.setView(input);

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    newUsername = input.getText().toString();

                    databaseReference.child("Usuarios").child(auth.getUid()).child("username").setValue(newUsername).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SettingsActivity.this, "Nome de usuário alterado com sucesso.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SettingsActivity.this, ProfileActivity.class));
                                finish();
                            } else {
                                Toast.makeText(SettingsActivity.this, "Ocorreu um erro", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();

            return;
        }

        if(v == btnExcluir) {
            final AlertDialog alertDialog = new AlertDialog.Builder(SettingsActivity.this).create();
            alertDialog.setTitle("Você tem certeza?");
            alertDialog.setMessage("Isso excluirá todos os seus dados");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Excluir",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            databaseReference.child("Usuarios").child(auth.getCurrentUser().getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(SettingsActivity.this, "Seus dados foram excluídos.", Toast.LENGTH_SHORT).show();
                                    auth.getCurrentUser().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            startActivity(new Intent(SettingsActivity.this, LauncherActivity.class));
                                            finish();
                                        }
                                    });

                                }
                            });
                            dialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.cancel();
                        }
                    });
                    alertDialog.show();

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
                finish();
            }
            return;
        }

        if(v == btnSobre){
            Intent i = new Intent(SettingsActivity.this, PdfView.class);
            i.putExtra("pdf", "sobre");
            startActivity(i);
            return;
        }

        if(v == btnTermos){
            Intent i = new Intent(SettingsActivity.this, PdfView.class);
            i.putExtra("pdf", "termos");
            startActivity(i);
            return;
        }

        if(v == imgBack){
            startActivity(new Intent(SettingsActivity.this, ProfileActivity.class));
            finish();
        }

    }
}
