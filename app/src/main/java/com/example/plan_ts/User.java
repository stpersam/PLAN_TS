package com.example.plan_ts;

import android.os.Parcel;
import android.os.Parcelable;


public class User implements Parcelable {

    public String Username;
    public String EMail;
    public String Passwort;
    public String Session;




    protected User(Parcel in) {
    }

    public static final Creator<com.example.plan_ts.User> CREATOR = new Creator<com.example.plan_ts.User>() {
        @Override
        public com.example.plan_ts.User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public com.example.plan_ts.User[] newArray(int size) {
            return new User[size];
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
