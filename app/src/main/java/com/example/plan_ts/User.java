package com.example.plan_ts;

import android.os.Parcel;
import android.os.Parcelable;


public class User implements Parcelable {

    public String Username;
    public String EMail;
    public String Passwort;
    public String Session;


    protected User(Parcel in) {
        Username = in.readString();
        EMail = in.readString();
        Passwort = in.readString();
        Session = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Username);
        dest.writeString(EMail);
        dest.writeString(Passwort);
        dest.writeString(Session);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEMail() {
        return EMail;
    }

    public void setEMail(String EMail) {
        this.EMail = EMail;
    }

    public String getPasswort() {
        return Passwort;
    }

    public void setPasswort(String passwort) {
        Passwort = passwort;
    }

    public String getSession() {
        return Session;
    }

    public void setSession(String session) {
        Session = session;
    }

    public static Creator<User> getCREATOR() {
        return CREATOR;
    }
}
