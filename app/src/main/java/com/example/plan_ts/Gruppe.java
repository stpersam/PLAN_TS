package com.example.plan_ts;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Gruppe{

    @SerializedName("GruppenID")
    public Integer GruppenID;
    @SerializedName("Gruppenname")
    public String Gruppenname;
    @SerializedName("Beschreibung")
    public String Beschreibung;
    @SerializedName("Username")
    public String Username;


    public Integer getGruppenID() {
        return GruppenID;
    }

    public void setGruppenID(Integer gruppenID) {
        GruppenID = gruppenID;
    }

    public String getGruppenname() {
        return Gruppenname;
    }

    public void setGruppenname(String gruppenname) {
        Gruppenname = gruppenname;
    }

    public String getBeschreibung() {
        return Beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        Beschreibung = beschreibung;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

}
