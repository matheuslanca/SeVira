package com.subwayinovators.sevira;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import subwayinovators.sevira.R;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.android.gms.auth.api.Auth;
import com.google.firebase.database.core.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class CadastroActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnRegister;
    private EditText edtEmail;
    private ImageView imgBack, btnFacebook;
    private EditText edtUsername;
    private SignInButton btnGoogle;
    private EditText edtPassword, edtConfPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private String emailFB, nameFB, imagemFB;
    private CallbackManager callbackManager;
    private LoginManager loginManager;
    private static int RC_SIGN_IN = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);


        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("hash", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("hash", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("hash", "printHashKey()", e);
        }


        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtConfPassword = (EditText) findViewById(R.id.edtConfPassword);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        btnFacebook = (ImageView) findViewById(R.id.btnFacebook);
        btnGoogle = (SignInButton) findViewById(R.id.btnGoogle);
        btnRegister = (Button) findViewById(R.id.btnLogin);
        btnRegister.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        imgBack = (ImageView) findViewById(R.id.imgBack);

        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();






        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {

                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            emailFB = object.getString("email");
                            nameFB = object.getString("name");
                            imagemFB = "https://graph.facebook.com/" + loginResult.getAccessToken().getUserId() + "/picture?type=large";
//                            imagemFB = object.getJSONObject("picture").getJSONObject("data").getString("url");;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "email,name");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
                loginFacebook(loginResult.getAccessToken());

            }


            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        imgBack.setOnClickListener(this);
        btnFacebook.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
    }

    private void loginFacebook (final AccessToken accessToken) {
        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());
        auth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    int linhaFavorita = 0;
                    final UserInformation userInformation = new UserInformation(emailFB, nameFB, linhaFavorita, 0,  1, imagemFB);

                    if(auth.getCurrentUser() != null){
//                        progressDialog.dismiss();
                        // armazenar informações no Firebase

                        databaseReference.child("Usuarios").child(auth.getUid()).setValue(userInformation);
                        startActivity(new Intent(CadastroActivity.this, MainActivity.class));
                        finish();

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Ocorreu algum erro", Toast.LENGTH_SHORT).show();
                }
            }
        });


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

    public void saveUserInformation(String email, String senha, String username) {
        int linhaFavorita = 0;
        final UserInformation userInformation = new UserInformation(email, username, linhaFavorita, 0, 1, "");
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

        if(v == imgBack){
            startActivity(new Intent(CadastroActivity.this, LauncherActivity.class));
        }

        if (v == btnFacebook) {
            loginManager.logInWithReadPermissions(CadastroActivity.this, Arrays.asList("email", "public_profile"));
            return;
        }

        if(v == btnGoogle){
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Toast.makeText(this, "sucesso", Toast.LENGTH_SHORT).show();



        } catch (ApiException e) {

            Toast.makeText(this, "erro", Toast.LENGTH_SHORT).show();
        }
    }

}
