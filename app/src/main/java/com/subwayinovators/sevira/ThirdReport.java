package com.subwayinovators.sevira;

import android.app.ProgressDialog;
import android.content.Intent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import subwayinovators.sevira.R;

import java.util.Locale;


public class ThirdReport extends AppCompatActivity implements View.OnClickListener {

    // Botões flutuantes
    FloatingActionButton fabBack, fabNext;

    // Valores inteiros
    int numEstacao, numLinha, idreport, pontUser;

    // Binário para definir situações de login
    boolean login;

    // Textos
    String linhaFinal, estacaoFinal, username, report, idFinal;

    // Caixa de texto
    EditText edtOutro;

    // Formatação de data
    DateFormat dataFormat, horarioFormat;

    // Caixa de progresso para criação do report
    ProgressDialog progressDialog;

    // Elementos do banco de dados
    // Referências
    DatabaseReference databaseReference, userRef;
    ValueEventListener valueEventListener;

    // User
    FirebaseUser user;
    UserInformation userInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_report);

        // Inicialização de variáveis
        // Recepção do valor do intent e trigger do método para receber o nome da estação
        linhaFinal = getIntent().getStringExtra("linha");
        numEstacao = Integer.parseInt(getIntent().getStringExtra("idestacao"));
        estacaoFinal = checkStation(numEstacao, linhaFinal);

        // Inicialiação do variáveis de report
        // Inicializando tudo em 0 e null para comparação de validez
        idreport = 0;
        report = null;
        username = null;

        // Inicialização da formatação de datas em HHmmss e yyyyMMdd para criação de id do report
        horarioFormat = new SimpleDateFormat("HHmmss", Locale.getDefault());
        dataFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());


        // Localização dos elementos do XML
        edtOutro = (EditText) findViewById(R.id.edtOutro);
        fabNext = (FloatingActionButton)findViewById(R.id.fabNext);
        fabBack = (FloatingActionButton)findViewById(R.id.fabBack);

        // Estabelecendo listeners nos botões flutuantes
        fabBack.setOnClickListener(this);
        fabNext.setOnClickListener(this);

        // Inicialização do idFinal em null para conferir validez
        idFinal = null;

        // Inicialização de elementos do FB
        user = FirebaseAuth.getInstance().getCurrentUser();
        login = user != null;
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Get do username para armazenamento do report
        // Get da pontuação para alteração de valor
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInformation userInf = new UserInformation();
                userInf.setUsername(dataSnapshot.child("Usuarios").child(user.getUid()).getValue(UserInformation.class).getUsername());
                userInf.setPontuacao(dataSnapshot.child("Usuarios").child(user.getUid()).getValue(UserInformation.class).getPontuacao());
                username = userInf.getUsername();
                pontUser = userInf.getPontuacao();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addListenerForSingleValueEvent(valueEventListener);

        // Inicialização do ProgressDialog
        progressDialog = new ProgressDialog(this);

    }

    @Override
    public void onClick(View v) {
        // Botão de voltar
        if(v == fabBack){
            Intent i = new Intent(ThirdReport.this, SecondReport.class);
            i.putExtra("linha1", linhaFinal + "");
            startActivity(i);
            finish();
            return;
        }

        // Botão de avançar
        if(v == fabNext){
            // Evita que o usuário faça uma denúncia vazia
            if(idreport == 0){
                Toast.makeText(getApplicationContext(), "Selecione alguma opção", Toast.LENGTH_SHORT).show();
                return;
            }

            // Checa se o usuário escolheu a opção "Outro" e recolhe o valor do input
            if(idreport == 9){
                report = getReport();
            }

            // Evita que o usuário não preencha o campo de texto em "Outro" e faz uma segunda checagem do primeiro If
            if(report == null) {
                Toast.makeText(getApplicationContext(), "Preencha o campo vazio", Toast.LENGTH_SHORT).show();
                return;
            }


            progressDialog.setMessage("Fazendo denúncia...");
            progressDialog.show();

            // Após checar tudo, se recebe o horário do report e se cria um id para armazenamento no BD
            Date hora = Calendar.getInstance().getTime();
            Date dia = Calendar.getInstance().getTime();
            String dataFinal = dataFormat.format(dia) + "_" + horarioFormat.format(hora);

            // Se armazena um valor de horário
            DateFormat horareport = new SimpleDateFormat("HH:mm:ss, dd/MM/yyyy", Locale.getDefault());
            String horafinal = horareport.format(Calendar.getInstance().getTime()).toString();
            String est = (numEstacao < 10) ? ("0" + "" + numEstacao) : (numEstacao + "");
            idFinal = dataFinal + "_0" + numLinha + "_" + est + "_0" + idreport;
            String userid = "";
            if(login) {
                userid = user.getUid();
            }

            createReport(idFinal, horafinal, report, numLinha, numEstacao, username, userid, linhaFinal, estacaoFinal);


        }
    }

    public void onButtonClicked(View v){
        // Checa se algum radiobutton foi selecionado
        boolean check = ((RadioButton) v).isChecked();

        if(check){
            switch(v.getId()){
                case R.id.rbassedio:
                    report = "Assédio";
                    idreport = 1;
                    break;

                case R.id.rbcatraca:
                    report = "Catraca Quebrada";
                    idreport = 2;
                    break;

                case R.id.rbcomercio:
                    report = "Comércio Ambulante";
                    idreport = 3;
                    break;

                case R.id.rbescada:
                    report = "Escada rolante quebrada";
                    idreport = 4;
                    break;

                case R.id.rbfurto:
                    report = "Furto";
                    idreport = 5;
                    break;

                case R.id.rblotacao:
                    report = "Lotação";
                    idreport = 6;
                    break;

                case R.id.rbparalisacao:
                    report = "Paralisação";
                    idreport = 7;
                    break;

                case R.id.rbvelocidade:
                    report = "Velocidade Reduzida";
                    idreport = 8;
                    break;

                case R.id.rboutro:
                    idreport = 9;
                    break;
            }
        }
    }

    public String getReport(){
        // Checa se o campo de texto "Outro" está preenchido
        // Caso não esteja, pede pra que o usuário preencha
        // Caso esteja, retorna o texto preenchido
        String n = edtOutro.getText().toString().trim();
        if(TextUtils.isEmpty(n)){
            edtOutro.setError("Preencha o campo");
            return null;
        } else {
            return n;
        }
    }

    public String checkStation(int estacao, String linha){
        // Recebe como parâmetro o id da estação e o nome da linha
        // Retorna o nome da estação como String

        List<String> linhas;
        if(linha.equals("Azul")){
            linhas = Arrays.asList(getResources().getStringArray(R.array.estacoes_linhaazul));
            numLinha = 1;
        } else if (linha.equals("Verde")) {
            linhas = Arrays.asList(getResources().getStringArray(R.array.estacoes_linhaverde));
            numLinha = 2;
        } else if(linha.equals("Vermelho")) {
            linhas = Arrays.asList(getResources().getStringArray(R.array.estacoes_linhavermelha));
            numLinha = 3;
        } else if(linha.equals("Amarelo")) {
            linhas = Arrays.asList(getResources().getStringArray(R.array.estacoes_linhaamarela));
            numLinha = 4;
        } else {
            finish();
            return null;
        }

        return linhas.get(estacao);
    }

    public void createReport(String reportID, String hora, String report, int idlinha, int idestacao, String usuario, String userID, String linha, String estacao){

        ReportInformation reportInformation = new ReportInformation(reportID, hora, report, idlinha, idestacao, usuario, linha, estacao);
        if(hora != null && report != null){
            // Report sem login
            if(!userID.equals("")){
                reportInformation.usuario = "Usuário não cadastrado";
            }

            databaseReference.child("Reports").child(reportID).setValue(reportInformation)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Denúncia realizada com sucesso!", Toast.LENGTH_SHORT).show();
                                if(user != null) {
                                    int pontuacaoFinal = pontUser + 60;
                                    databaseReference.child("Usuarios").child(user.getUid()).child("pontuacao").setValue(pontuacaoFinal);
                                    Toast.makeText(ThirdReport.this, "Você ganhou 60 pontos!\nSua pontuação: " + pontuacaoFinal, Toast.LENGTH_SHORT).show();
                                }
                                databaseReference.removeEventListener(valueEventListener);
                                startActivity(new Intent(ThirdReport.this, MainActivity.class));
                            } else {
                                databaseReference.removeEventListener(valueEventListener);
                                Toast.makeText(ThirdReport.this, "Ocorreu um erro", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
        }

    }


}
