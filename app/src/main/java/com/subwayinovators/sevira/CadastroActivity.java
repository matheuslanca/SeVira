package com.subwayinovators.sevira;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import subwayinovators.sevira.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class CadastroActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnRegister;
    private EditText edtEmail;
    private EditText edtUsername;
    private EditText edtPassword, edtConfPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtConfPassword = (EditText) findViewById(R.id.edtConfPassword);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        btnRegister = (Button) findViewById(R.id.btnLogin);
        btnRegister.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void addUser(UserInformation userInformation){
        FirebaseUser user = auth.getCurrentUser();
        assert user != null;
        databaseReference.child("Usuarios").child(user.getUid()).setValue(userInformation)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(CadastroActivity.this, "Cadastro efetuado com sucesso", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CadastroActivity.this, "Não foi possível salvar as informações", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        auth.signOut();
        startActivity(new Intent(CadastroActivity.this, LoginActivity.class));
        finish();
    }

    private void saveUserInformation(String email, String senha, String username) {
        int linhaFavorita = 0;
        final UserInformation userInformation = new UserInformation(email, senha, username, linhaFavorita, 0, 1, 1, "");
        progressDialog.setMessage("Efetuando login...");
        if(auth.getCurrentUser() != null){
            progressDialog.dismiss();
            addUser(userInformation);
        }

    }

    private void registerUser() {
        final String email = edtEmail.getText().toString().trim();
        final String username = edtUsername.getText().toString().trim();
        final String passwordInicial = edtPassword.getText().toString().trim();
        final String confpassword = edtConfPassword.getText().toString().trim();
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(passwordInicial) || TextUtils.isEmpty(confpassword) || TextUtils.isEmpty(username)){
            Toast.makeText(this, R.string.campos_vazios, Toast.LENGTH_SHORT).show();
            return;
        }

        if (passwordInicial.equals(confpassword)) {
            progressDialog.setMessage("Registrando...");
            progressDialog.show();


            auth.createUserWithEmailAndPassword(email, passwordInicial)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful()){
                                verifyEmail(email, passwordInicial);
                                saveUserInformation(email, passwordInicial, username);
                            } else {
                                Toast.makeText(CadastroActivity.this, "Não foi possível efetuar o cadastro", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        } else {
            Toast.makeText(this, R.string.senhas_diferentes, Toast.LENGTH_SHORT).show();
        }
    }

    public void verifyEmail(String email, String password){
        final String mail = email;
        FirebaseUser user;
        if(auth.getCurrentUser() == null){
            auth.signInWithEmailAndPassword(email, password);
        }

        user = auth.getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Uma confirmação de email foi enviada para " + mail, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Não foi possível completar seu cadastro", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == btnRegister) {
            registerUser();
        }

    }


}
