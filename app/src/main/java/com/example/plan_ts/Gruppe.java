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
        if (in.readByte() == 0) {
            GruppenID = null;
        } else {
            GruppenID = in.readInt();
        }
        Gruppenname = in.readString();
        Beschreibung = in.readString();
        User = in.readParcelable(com.example.plan_ts.User.class.getClassLoader());
        Username = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (GruppenID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(GruppenID);
        }
        dest.writeString(Gruppenname);
        dest.writeString(Beschreibung);
        dest.writeParcelable(User, flags);
        dest.writeString(Username);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Gruppe> CREATOR = new Creator<Gruppe>() {
        @Override
        public Gruppe createFromParcel(Parcel in) {
            return new Gruppe(in);
        }

        @Override
        public Gruppe[] newArray(int size) {
            return new Gruppe[size];
        }
    };

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

    public com.example.plan_ts.User getUser() {
        return User;
    }

    public void setUser(com.example.plan_ts.User user) {
        User = user;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public static Creator<Gruppe> getCREATOR() {
        return CREATOR;
    }
}
