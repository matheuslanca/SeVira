package com.subwayinovators.sevira;

public class Linha {
    public String cor;
    public String horario;
    public String situacao;
    public String estacaoReport;

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getEstacaoReport() {
        return estacaoReport;
    }

    public void setEstacaoReport(String estacaoReport) {
        this.estacaoReport = estacaoReport;
    }

    public Linha() {
    }

    public Linha(String cor, String horario, String situacao, String estacaoReport) {
        this.cor = cor;
        this.horario = horario;
        this.situacao = situacao;
        this.estacaoReport = estacaoReport;
    }
}
