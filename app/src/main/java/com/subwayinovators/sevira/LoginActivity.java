package com.subwayinovators.sevira;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import org.json.JSONException;
import org.json.JSONObject;

import subwayinovators.sevira.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin;
    private ImageView imgBack;
    private TextView txtEsqueci;
    private EditText edtEmail, edtSenha;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private CallbackManager callbackManager;

    private String emailFB, imagemFB, nameFB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
        progressDialog = new ProgressDialog(this);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();



        auth = FirebaseAuth.getInstance();
        imgBack = (ImageView) findViewById(R.id.imgBack);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtEsqueci = (TextView) findViewById(R.id.txtEsqueci);
        edtEmail = (EditText) findViewById(R.id.edtUsername);
        edtSenha = (EditText) findViewById(R.id.edtSenha);
        callbackManager = CallbackManager.Factory.create();
        databaseReference = FirebaseDatabase.getInstance().getReference();



        if(auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }

        btnLogin.setOnClickListener(this);
        txtEsqueci.setOnClickListener(this);
        imgBack.setOnClickListener(this);


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
        if (v == btnLogin) {
            login();
            return;
        }

        if(v == imgBack){
            startActivity(new Intent(LoginActivity.this, LauncherActivity.class));
        }

        if (v == txtEsqueci) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("  Insira seu e-mail");

            final EditText input = new EditText(this);
            input.setPadding(100, 0, 50, 0);
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
            return;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
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
