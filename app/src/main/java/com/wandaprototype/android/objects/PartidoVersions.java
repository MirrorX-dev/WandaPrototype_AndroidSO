package com.wandaprototype.android.objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "partidoVersions")
public class PartidoVersions {
    public static ArrayList<PartidoVersions> partidosVersions = new ArrayList<>();

    @PrimaryKey
    @ColumnInfo(name = "vnumber")
    public int version_number;

    @NonNull
    @ColumnInfo(name = "vdate")
    public String version_date;

    public PartidoVersions(int version_number, @NonNull String version_date) {
        this.version_number = version_number;
        this.version_date = version_date;
    }

    public PartidoVersions() {
        // TODO Auto-generated constructor stub
        version_date = null;
    }

    public static ArrayList<PartidoVersions> getPartidosVersions() {
        return partidosVersions;
    }

    public static void setPartidosVersions(ArrayList<PartidoVersions> partidosVersions) {
        PartidoVersions.partidosVersions = partidosVersions;
    }

    public int getVersion_number() {
        return version_number;
    }

    public void setVersion_number(int version_number) {
        this.version_number = version_number;
    }

    @NonNull
    public String getVersion_date() {
        return version_date;
    }

    public void setVersion_date(@NonNull String version_date) {
        this.version_date = version_date;
    }

    @NonNull
    @Override
    public String toString() {
        return "PartidoVersions{" +
                "version_number=" + version_number +
                ", version_date='" + version_date + '\'' +
                '}';
    }
}
