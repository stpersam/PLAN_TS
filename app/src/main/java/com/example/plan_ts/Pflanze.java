package com.example.plan_ts;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;


public class Pflanze implements Parcelable {

    public Integer PflanzenID;
    public String Pflanzenname;
    public String Bild;
    public Date Gegossen;
    public Double Groesse;
    public User User;
    public String Username;
    public Gruppe Gruppe;
    public String Gruppenname;
    public Pflanzenart Pflanzenart;
    public String Pflanzeartname;


    protected Pflanze(Parcel in) {
    }

    public static final Creator<Pflanze> CREATOR = new Creator<Pflanze>() {
        @Override
        public Pflanze createFromParcel(Parcel in) {
            return new Pflanze(in);
        }

        @Override
        public Pflanze[] newArray(int size) {
            return new Pflanze[size];
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
