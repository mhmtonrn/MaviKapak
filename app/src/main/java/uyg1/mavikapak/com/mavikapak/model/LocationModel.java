package uyg1.mavikapak.com.mavikapak.model;


import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class LocationModel {
    public String active;
    public String latitu;
    public String longttitu;

    public LocationModel() {
    }

    public LocationModel(String active, String latitu, String longttitu) {
        this.active = active;
        this.latitu = latitu;
        this.longttitu = longttitu;
    }

    @Override
    public String toString() {
        return "LocationModel{" +
                "active='" + active + '\'' +
                ", latitu='" + latitu + '\'' +
                ", longttitu='" + longttitu + '\'' +
                '}';
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getLatitu() {
        return latitu;
    }

    public void setLatitu(String latitu) {
        this.latitu = latitu;
    }

    public String getLongttitu() {
        return longttitu;
    }

    public void setLongttitu(String longttitu) {
        this.longttitu = longttitu;
    }
}
