package com.subwayinovators.sevira;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import subwayinovators.sevira.R;

import java.util.ArrayList;


import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerAdapterReports extends RecyclerView.Adapter<RecyclerAdapterReports.ViewHolderLinha> {

    private ArrayList<String> report;
    private ArrayList<String> horario;
    private ArrayList<String> estacao;
    private int nlinha;
    private int color;


    public RecyclerAdapterReports (ArrayList<String> report, ArrayList<String> horario, ArrayList<String> estacao, int nlinha, int color) {
        this.report = report;
        this.horario = horario;
        this.estacao = estacao;
        this.nlinha = nlinha;
        this.color = color;
    }


    @NonNull
    @Override
    public RecyclerAdapterReports.ViewHolderLinha onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.report_layout, parent, false);
        ViewHolderLinha holderLinha = new ViewHolderLinha(view);
        return holderLinha;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterReports.ViewHolderLinha holder, int position) {
        if(nlinha == 1){
            holder.imgLinha.setImageResource(R.drawable.linha_azul);
        } else if(nlinha == 2){
            holder.imgLinha.setImageResource(R.drawable.linha_verde);
        } else if(nlinha == 3){
            holder.imgLinha.setImageResource(R.drawable.linha_vermelha);
        } else {
            holder.imgLinha.setImageResource(R.drawable.linha_amarela);
        }

        holder.txtEstacao.setText(estacao.get(position).toString());
        holder.txtReport.setText(report.get(position).toString());
        holder.txtHorario.setText(horario.get(position).toString());
        holder.layoutSituacao.setBackgroundColor(color);
    }

    @Override
    public int getItemCount() {
        return report.size();
    }

    public class ViewHolderLinha extends RecyclerView.ViewHolder {

        public TextView txtEstacao;
        public TextView txtHorario;
        public TextView txtReport;
        public ImageView imgLinha;
        public RelativeLayout layoutSituacao;

        public ViewHolderLinha(View itemView) {
            super(itemView);

            txtEstacao = (TextView) itemView.findViewById(R.id.txtEstacao);
            txtHorario = (TextView) itemView.findViewById(R.id.txtHorario);
            txtReport = (TextView) itemView.findViewById(R.id.txtReport);
            imgLinha = (ImageView) itemView.findViewById(R.id.imgLinha);
            layoutSituacao = (RelativeLayout) itemView.findViewById(R.id.layoutSituacao);

        }
    }
}