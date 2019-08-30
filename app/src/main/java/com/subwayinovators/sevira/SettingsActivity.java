package com.subwayinovators.sevira;

import android.content.Intent;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import subwayinovators.sevira.R;

public class SettingsActivity extends AppCompatPreferenceActivity {

    EditTextPreference prefUsername;
    Preference prefTermos;
    Preference prefSobre;
    Preference prefImage;
    Preference prefLogout;
    Preference prefExcluir;
    ListView listView;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();

        // procurar outro metodo para achar preferencias
        prefUsername = (EditTextPreference) findPreference(this.getResources().getString(R.string.pref_username));
        prefTermos = (Preference) findPreference(this.getResources().getString(R.string.pref_termos));
        prefSobre = (Preference) findPreference(this.getResources().getString(R.string.pref_sobre));
        prefImage = (Preference) findPreference(this.getResources().getString(R.string.pref_imagem));
        prefLogout = (Preference) findPreference(this.getResources().getString(R.string.pref_logout));
        prefExcluir = (Preference) findPreference(this.getResources().getString(R.string.pref_excluir));

        auth = FirebaseAuth.getInstance();


//        prefUsername.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object newValue) {
//                // Mudança de username
//
//                return false;
//            }
//        });
//
//
//        prefExcluir.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//                // Excluir conta
//
//                return false;
//            }
//        });
//
//        prefLogout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//                auth.signOut();
//                if(auth.getCurrentUser() == null) {
//                    Toast.makeText(SettingsActivity.this, "Logout efetuado com sucesso", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(SettingsActivity.this, LauncherActivity.class));
//                }
//
//                return false;
//            }
//        });
//
//        prefImage.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//                // Alterar imagem de perfil
//
//                return false;
//            }
//        });
//
//        prefTermos.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//                // Abrir termos de uso
//
//                return false;
//            }
//        });
//
//        prefSobre.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//                // Abrir sobre
//
//                return false;
//            }
//        });



    }


    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
    public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);
        }
    }








}
