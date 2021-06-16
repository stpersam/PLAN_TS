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
        Bezeichnung = in.readString();
        Lichtbeduerfnisse = in.readString();
        if (in.readByte() == 0) {
            Topfgröße = null;
        } else {
            Topfgröße = in.readDouble();
        }
        Erde = in.readString();
        if (in.readByte() == 0) {
            Wasserzyklus = null;
        } else {
            Wasserzyklus = in.readDouble();
        }
        if (in.readByte() == 0) {
            Luftfeuchtigkeit = null;
        } else {
            Luftfeuchtigkeit = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Bezeichnung);
        dest.writeString(Lichtbeduerfnisse);
        if (Topfgröße == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(Topfgröße);
        }
        dest.writeString(Erde);
        if (Wasserzyklus == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(Wasserzyklus);
        }
        if (Luftfeuchtigkeit == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(Luftfeuchtigkeit);
        }
    }

    @Override
    public int describeContents() {
        return 0;
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

    public Double getTopfgröße() {
        return Topfgröße;
    }

    public void setTopfgröße(Double topfgröße) {
        Topfgröße = topfgröße;
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

    public static Creator<Pflanzenart> getCREATOR() {
        return CREATOR;
    }
}
