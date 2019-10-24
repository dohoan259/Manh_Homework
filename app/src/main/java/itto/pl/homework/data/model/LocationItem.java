package itto.pl.homework.data.model;

import androidx.annotation.NonNull;

/**
 * Created by PL_itto-PC on 10/25/2019
 **/
public class LocationItem {
    private Double mLatitude = 0.0;
    private Double mLongitude = 0.0;

    public Double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(Double latitude) {
        mLatitude = latitude;
    }

    public Double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(Double longitude) {
        mLongitude = longitude;
    }

    @NonNull
    @Override
    public String toString() {
        return mLatitude + ":" + mLongitude;
    }
}
