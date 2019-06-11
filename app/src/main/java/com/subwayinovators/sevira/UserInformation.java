package com.subwayinovators.sevira;

public class UserInformation {
    public String email;
    public String username;
    public int linhaFavorita;
    public int pontuacao;
    public int level;
    public String profilepic;

    public UserInformation() {

    }

    public UserInformation(String email, String username, int linhaFavorita, int pontuacao, int level, String profilepic) {
        this.email = email;
        this.username = username;
        this.linhaFavorita = linhaFavorita;
        this.pontuacao = pontuacao;
        this.level = level;
        this.profilepic = profilepic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getLinhaFavorita() {
        return linhaFavorita;
    }

    public void setLinhaFavorita(int linhaFavorita) {
        this.linhaFavorita = linhaFavorita;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }
}
