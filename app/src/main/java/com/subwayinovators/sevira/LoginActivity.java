package com.subwayinovators.sevira;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import subwayinovators.sevira.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public Button btnLogin;
    public Button btnRegister;
    public TextView txtPular, txtEsqueci;
    public EditText edtEmail, edtSenha;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(this);

        auth = FirebaseAuth.getInstance();
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtPular = (TextView) findViewById(R.id.txtPular);
        txtEsqueci = (TextView) findViewById(R.id.txtEsqueci);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        edtEmail = (EditText) findViewById(R.id.edtUsername);
        edtSenha = (EditText) findViewById(R.id.edtSenha);

        if(auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        txtPular.setOnClickListener(this);
        txtEsqueci.setOnClickListener(this);

    }

    private void login() {
        String email = edtEmail.getText().toString().trim();
        String password = edtSenha.getText().toString().trim();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Efetuando login...");
        progressDialog.show();


        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            if(Objects.requireNonNull(auth.getCurrentUser()).isEmailVerified()) {
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                Toast.makeText(getApplicationContext(), "Login efetuado com sucesso", Toast.LENGTH_SHORT).show();
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Confirme seu e-mail para poder fazer login", Toast.LENGTH_SHORT).show();
                                auth.getCurrentUser().sendEmailVerification();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Não foi possível efetuar o login", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    @Override
    public void onClick(View v) {
        if(v == btnLogin) {
            login();
        }
        if(v == btnRegister){
            startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
        }

        if(v == txtPular) {
            Intent pular = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(pular);
        }

        if(v == txtEsqueci){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("  Insira seu e-mail");

            final EditText input = new EditText(this);
            input.setPadding(30, 0, 30, 0);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);


            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                String enderecoEmail;
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    enderecoEmail = input.getText().toString();
                    esqueciMinhaSenha(enderecoEmail);
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }
    }

    public void esqueciMinhaSenha(String email) {
        final String emailUser = email;
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Foi enviado um e-mail para " + emailUser + " para a alteração de senha", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

}
