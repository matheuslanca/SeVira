package com.subwayinovators.sevira;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import subwayinovators.sevira.R;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnUsername, btnFoto, btnLogout, btnTermos, btnSobre, btnExcluir;
    ImageView imgBack;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    StorageReference storageRef;
    private Uri mMediaUri;
    private String newUsername = "";

    public static final int TAKE_PIC_REQUEST_CODE = 0;
    public static final int CHOOSE_PIC_REQUEST_CODE = 1;
    public static final int MEDIA_TYPE_IMAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        auth = FirebaseAuth.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

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
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
            builder.setTitle("Escolha ou tire uma foto");
            builder.setPositiveButton("Escolher da galeria", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // open gallery

                    Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    getIntent.setType("image/*");

                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("image/*");

                    Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                    startActivityForResult(chooserIntent, CHOOSE_PIC_REQUEST_CODE);

                }
            });
            builder.setNegativeButton("Tirar foto da câmera", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // open camera

                    File file = new File("/SeVira");
                    Uri outputFileUri = Uri.fromFile(file);
                    Intent intent = new Intent(
                            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                    startActivityForResult(intent, TAKE_PIC_REQUEST_CODE);

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CHOOSE_PIC_REQUEST_CODE) {
            Uri pic = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(pic,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] foto = stream.toByteArray();
            bitmap.recycle();

            UploadTask uploadTask = storageRef.child("images").child(auth.getUid()).putBytes(foto);
            uploadTask.addOnFailureListener(new OnFailureListener() {

                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(SettingsActivity.this, "Ocorreu um erro", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(SettingsActivity.this, "Imagem de perfil alterada com sucesso!", Toast.LENGTH_SHORT).show();
                }
            });

        }


        if (requestCode == TAKE_PIC_REQUEST_CODE) {
            // tratar upload de imagem pela câmera
        }
    }

    private Uri getOutputMediaFileUri(int mediaTypeImage) {

        if (isExternalStorageAvailable()) {
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "UPLOADIMAGES");
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return null;
                }
            }


            File mediaFile = null;
            Date now = new Date();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(now);

            String path = mediaStorageDir.getPath() + File.separator;
            if (mediaTypeImage == MEDIA_TYPE_IMAGE) {
                mediaFile = new File(path + "IMG_" + timestamp + ".jpg");
            }

            return Uri.fromFile(mediaFile);
        } else {

            return null;
        }
    }

    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

}
