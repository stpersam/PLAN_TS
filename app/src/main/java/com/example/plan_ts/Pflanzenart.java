package com.example.plan_ts;

import android.os.Parcel;
import android.os.Parcelable;


public class Pflanzenart implements Parcelable {

    public String Bezeichnung;

    public String Lichtbeduerfnisse;
    public Double Topfgröße;
    public String Erde;
    public Double Wasserzyklus;
    public Integer Luftfeuchtigkeit;




    protected Pflanzenart(Parcel in) {
    }

    public static final Creator<Pflanzenart> CREATOR = new Creator<Pflanzenart>() {
        @Override
        public Pflanzenart createFromParcel(Parcel in) {
            return new Pflanzenart(in);
        }

        @Override
        public Pflanzenart[] newArray(int size) {
            return new Pflanzenart[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
