package com.example.plan_ts;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class Pflanzenart{

    @SerializedName("Bezeichnung")
    public String Bezeichnung;
    @SerializedName("Lichtbeduerfnisse")
    public String Lichtbeduerfnisse;
    @SerializedName("Topfgroesse")
    public Double Topfgroesse;
    @SerializedName("Erde")
    public String Erde;
    @SerializedName("Wasserzyklus")
    public Double Wasserzyklus;
    @SerializedName("Luftfeuchtigkeit")
    public Integer Luftfeuchtigkeit;


    public String getBezeichnung() {
        return Bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        Bezeichnung = bezeichnung;
    }

    public String getLichtbeduerfnisse() {
        return Lichtbeduerfnisse;
    }

    public void setLichtbeduerfnisse(String lichtbeduerfnisse) {
        Lichtbeduerfnisse = lichtbeduerfnisse;
    }

    public Double getTopfgroesse() {
        return Topfgroesse;
    }

    public void setTopfgroesse(Double topfgroesse) {
        Topfgroesse = topfgroesse;
    }

    public String getErde() {
        return Erde;
    }

    public void setErde(String erde) {
        Erde = erde;
    }

    public Double getWasserzyklus() {
        return Wasserzyklus;
    }

    public void setWasserzyklus(Double wasserzyklus) {
        Wasserzyklus = wasserzyklus;
    }

    public Integer getLuftfeuchtigkeit() {
        return Luftfeuchtigkeit;
    }

    public void setLuftfeuchtigkeit(Integer luftfeuchtigkeit) {
        Luftfeuchtigkeit = luftfeuchtigkeit;
    }
}
