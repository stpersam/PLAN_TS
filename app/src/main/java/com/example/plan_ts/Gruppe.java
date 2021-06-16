package com.example.plan_ts;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;


public class Gruppe implements Parcelable {

    public Integer GruppenID;

    public String Gruppenname;
    public String Beschreibung;

     public User User;

    public String Username;



    protected Gruppe(Parcel in) {
    }

    public static final Creator<com.example.plan_ts.Gruppe> CREATOR = new Creator<com.example.plan_ts.Gruppe>() {
        @Override
        public com.example.plan_ts.Gruppe createFromParcel(Parcel in) {
            return new Gruppe(in);
        }

        @Override
        public com.example.plan_ts.Gruppe[] newArray(int size) {
            return new Gruppe[size];
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
