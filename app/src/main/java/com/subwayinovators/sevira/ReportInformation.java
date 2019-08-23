package com.subwayinovators.sevira;

public class ReportInformation {
    // String reportID, String hora, String report, int idlinha, int idestacao, String usuario, String userID, String linha, String estacao
    String id;
    String hora;
    String report;
    int idlinha;
    int idestacao;
    String usuario;
    String linha;
    String estacao;

    public ReportInformation(String id, String hora, String report, int idlinha, int idestacao, String usuario, String linha, String estacao) {
        this.id = id;
        this.hora = hora;
        this.report = report;
        this.idlinha = idlinha;
        this.idestacao = idestacao;
        this.usuario = usuario;
        this.linha = linha;
        this.estacao = estacao;
    }

    public ReportInformation() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public int getIdlinha() {
        return idlinha;
    }

    public void setIdlinha(int idlinha) {
        this.idlinha = idlinha;
    }

    public int getIdestacao() {
        return idestacao;
    }

    public void setIdestacao(int idestacao) {
        this.idestacao = idestacao;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getLinha() {
        return linha;
    }

    public void setLinha(String linha) {
        this.linha = linha;
    }

    public String getEstacao() {
        return estacao;
    }

    public void setEstacao(String estacao) {
        this.estacao = estacao;
    }
}
