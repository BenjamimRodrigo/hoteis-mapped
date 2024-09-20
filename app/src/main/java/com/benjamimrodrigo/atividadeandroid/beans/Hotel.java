package com.benjamimrodrigo.atividadeandroid.beans;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Benjamim on 27/05/2017.
 */

public class Hotel implements Serializable{

    String nome;
    String site;
    String telefone;
    double latitude;
    double longitude;
    String endereco;

    public String getNome() {

        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public boolean parseJSON(JSONObject objJSON) {
        try {
            if (objJSON.isNull("latitude") | objJSON.isNull("longitude")) {
                return false;
            } else {
                this.nome = objJSON.getString("nome");
                this.site = objJSON.getString("site");
                this.telefone = objJSON.getString("telefone");
                this.endereco = objJSON.getString("endereco");
                this.latitude = Double.parseDouble(objJSON.getString("latitude"));
                this.longitude = Double.parseDouble(objJSON.getString("longitude"));
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
