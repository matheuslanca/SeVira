package com.subwayinovators.sevira;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import subwayinovators.sevira.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolderLinha> {

    private List<String> linhas;
    private int nlinha;


    public RecyclerAdapter (List<String> linhas, int nlinha) {
        this.linhas = linhas;
        this.nlinha = nlinha;
    }


    @NonNull
    @Override
    public RecyclerAdapter.ViewHolderLinha onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.text_view_linha, parent, false);
        ViewHolderLinha holderLinha = new ViewHolderLinha(view);
        return holderLinha;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolderLinha holder, int position) {

        if(nlinha == 1){
            holder.imgLinha.setImageResource(R.drawable.linha_azul);
        } else if(nlinha == 2){
            holder.imgLinha.setImageResource(R.drawable.linha_verde);
        } else if(nlinha == 3){
            holder.imgLinha.setImageResource(R.drawable.linha_vermelha);
        } else {
            holder.imgLinha.setImageResource(R.drawable.linha_amarela);
        }

        holder.txtLinha.setText(linhas.get(position).toString());


    }

    @Override
    public int getItemCount() {
        return linhas.size();
    }

    public class ViewHolderLinha extends RecyclerView.ViewHolder {

        public TextView txtLinha;
        public CircleImageView imgLinha;

        public ViewHolderLinha(View itemView) {
            super(itemView);

            txtLinha = (TextView) itemView.findViewById(R.id.txtLinha);
            imgLinha = (CircleImageView) itemView.findViewById(R.id.imgLinha);

        }
    }
}
