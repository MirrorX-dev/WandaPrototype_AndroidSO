package com.wandaprototype.android.objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

/**
 * Entidad "Partido"
 */
@Entity(tableName = "partido")
public class Partido {

    public static ArrayList<Partido> partidos = new ArrayList<>();

    @PrimaryKey
    @NonNull
    public String id_partido;
    @ColumnInfo(name = "idpartido")
    @NonNull
    public String competicion;
    @ColumnInfo(name = "elocal")
    @NonNull
    public String equipolocal;
    @ColumnInfo(name = "evisit")
    @NonNull
    public String equipovisitante;
    @ColumnInfo(name = "contenido")
    @NonNull
    public String jornada;
    @ColumnInfo(name = "fpartido")
    @NonNull
    public String fechapartido;
    @ColumnInfo(name = "hpartido")
    @NonNull
    public String horapartido;
    @ColumnInfo(name = "epartido")
    @NonNull
    public String estadiopartido;

    public static ArrayList<Partido> getPartidos() {
        return partidos;
    }

    public static void setPartidos(ArrayList<Partido> partidos) {
        Partido.partidos = partidos;
    }

    @NonNull
    public String getId_partido() {
        return id_partido;
    }

    public void setId_partido(@NonNull String id_partido) {
        this.id_partido = id_partido;
    }

    @NonNull
    public String getCompeticion() {
        return competicion;
    }

    public void setCompeticion(@NonNull String competicion) {
        this.competicion = competicion;
    }

    @NonNull
    public String getEquipolocal() {
        return equipolocal;
    }

    public void setEquipolocal(@NonNull String equipolocal) {
        this.equipolocal = equipolocal;
    }

    @NonNull
    public String getEquipovisitante() {
        return equipovisitante;
    }

    public void setEquipovisitante(@NonNull String equipovisitante) {
        this.equipovisitante = equipovisitante;
    }

    @NonNull
    public String getJornada() {
        return jornada;
    }

    public void setJornada(@NonNull String jornada) {
        this.jornada = jornada;
    }

    @NonNull
    public String getFechapartido() {
        return fechapartido;
    }

    public void setFechapartido(@NonNull String fechapartido) {
        this.fechapartido = fechapartido;
    }

    @NonNull
    public String getHorapartido() {
        return horapartido;
    }

    public void setHorapartido(@NonNull String horapartido) {
        this.horapartido = horapartido;
    }

    @NonNull
    public String getEstadiopartido() {
        return estadiopartido;
    }

    public void setEstadiopartido(@NonNull String estadiopartido) {
        this.estadiopartido = estadiopartido;
    }

    @NonNull
    @Override
    public String toString() {
        return "Partido [id_partido=" + id_partido + ", competicion=" + competicion + ", equipolocal=" + equipolocal
                + ", equipovisitante=" + equipovisitante + ", jornada=" + jornada + ", fechapartido=" + fechapartido
                + ", horapartido=" + horapartido + ", estadiopartido=" + estadiopartido + "]";
    }

    public Partido(String id_partido, String competicion, String equipolocal, String equipovisitante, String jornada,
                   String fechapartido, String horapartido, String estadiopartido) {
        super();
        this.id_partido = id_partido;
        this.competicion = competicion;
        this.equipolocal = equipolocal;
        this.equipovisitante = equipovisitante;
        this.jornada = jornada;
        this.fechapartido = fechapartido;
        this.horapartido = horapartido;
        this.estadiopartido = estadiopartido;
    }

    public Partido() {
        // TODO Auto-generated constructor stub
        id_partido = null;
        competicion = null;
        equipolocal = null;
        estadiopartido = null;
        horapartido = null;
        fechapartido = null;
        equipovisitante = null;
        jornada = null;
    }

}
