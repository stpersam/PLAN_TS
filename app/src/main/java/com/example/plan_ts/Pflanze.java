package com.example.plan_ts;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Pflanze{

    @SerializedName("PflanzenID")
    public Integer PflanzenID;
    @SerializedName("Pflanzenname")
    public String Pflanzenname;
    @SerializedName("Bild")
    public String Bild;
    @SerializedName("Gegossen")
    public String Gegossen;
    @SerializedName("Groesse")
    public Double Groesse;
    @SerializedName("Username")
    public String Username;
    @SerializedName("Gruppenname")
    public String Gruppenname;
    @SerializedName("Pflanzeartname")
    public String Pflanzeartname;



    @SerializedName("GegossenDate")
    public Date GegossenDate;

    public Date getGegossenDate() {
        return GegossenDate;
    }
    public void setGegossenDate(String gegossen) {
        try {
            GegossenDate = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss.ffffff").parse(gegossen);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public Integer getPflanzenID() {
        return PflanzenID;
    }

    public void setPflanzenID(Integer pflanzenID) {
        PflanzenID = pflanzenID;
    }

    public String getPflanzenname() {
        return Pflanzenname;
    }

    public void setPflanzenname(String pflanzenname) {
        Pflanzenname = pflanzenname;
    }

    public String getBild() {
        return Bild;
    }

    public void setBild(String bild) {
        Bild = bild;
    }

    public String getGegossen() {
        return Gegossen;
    }

    public void setGegossen(String gegossen) {
        Gegossen = gegossen;
    }

    public Double getGroesse() {
        return Groesse;
    }

    public void setGroesse(Double groesse) {
        Groesse = groesse;
    }


    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getGruppenname() {
        return Gruppenname;
    }

    public void setGruppenname(String gruppenname) {
        Gruppenname = gruppenname;
    }

    public String getPflanzeartname() {
        return Pflanzeartname;
    }

    public void setPflanzeartname(String pflanzeartname) {
        Pflanzeartname = pflanzeartname;
    }

}
