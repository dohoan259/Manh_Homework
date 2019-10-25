package itto.pl.homework.data.model;

import androidx.annotation.NonNull;

public class BatteryEntity {

    private int level;

    public BatteryEntity(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    @NonNull
    @Override
    public String toString() {
        return String.valueOf(level);
    }
}
