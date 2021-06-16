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
        if (in.readByte() == 0) {
            PflanzenID = null;
        } else {
            PflanzenID = in.readInt();
        }
        Pflanzenname = in.readString();
        Bild = in.readString();
        if (in.readByte() == 0) {
            Groesse = null;
        } else {
            Groesse = in.readDouble();
        }
        User = in.readParcelable(com.example.plan_ts.User.class.getClassLoader());
        Username = in.readString();
        Gruppe = in.readParcelable(com.example.plan_ts.Gruppe.class.getClassLoader());
        Gruppenname = in.readString();
        Pflanzenart = in.readParcelable(com.example.plan_ts.Pflanzenart.class.getClassLoader());
        Pflanzeartname = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (PflanzenID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(PflanzenID);
        }
        dest.writeString(Pflanzenname);
        dest.writeString(Bild);
        if (Groesse == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(Groesse);
        }
        dest.writeParcelable(User, flags);
        dest.writeString(Username);
        dest.writeParcelable(Gruppe, flags);
        dest.writeString(Gruppenname);
        dest.writeParcelable(Pflanzenart, flags);
        dest.writeString(Pflanzeartname);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public Date getGegossen() {
        return Gegossen;
    }

    public void setGegossen(Date gegossen) {
        Gegossen = gegossen;
    }

    public Double getGroesse() {
        return Groesse;
    }

    public void setGroesse(Double groesse) {
        Groesse = groesse;
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

    public com.example.plan_ts.Gruppe getGruppe() {
        return Gruppe;
    }

    public void setGruppe(com.example.plan_ts.Gruppe gruppe) {
        Gruppe = gruppe;
    }

    public String getGruppenname() {
        return Gruppenname;
    }

    public void setGruppenname(String gruppenname) {
        Gruppenname = gruppenname;
    }

    public com.example.plan_ts.Pflanzenart getPflanzenart() {
        return Pflanzenart;
    }

    public void setPflanzenart(com.example.plan_ts.Pflanzenart pflanzenart) {
        Pflanzenart = pflanzenart;
    }

    public String getPflanzeartname() {
        return Pflanzeartname;
    }

    public void setPflanzeartname(String pflanzeartname) {
        Pflanzeartname = pflanzeartname;
    }

    public static Creator<Pflanze> getCREATOR() {
        return CREATOR;
    }
}
