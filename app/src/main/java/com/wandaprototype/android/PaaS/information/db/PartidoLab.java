package com.wandaprototype.android.PaaS.information.db;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.room.Room;

import com.wandaprototype.android.objects.Partido;

import java.util.List;

/**
 * Esta clase hace uso de la interfaz NotaDao e interactúa con la base de datos.
 *
 * @author Miguel Callejón Berenguer
 * @version 2018.07
 */
public class PartidoLab {
    @SuppressLint("StaticFieldLeak")
    private static PartidoLab sPartidoLab;

    private PartidoDAO.PartidoDao mPartidoDao;

    private PartidoLab(Context context) {
        Context appContext = context.getApplicationContext();
        PartidoDatabase database = Room.databaseBuilder(appContext, PartidoDatabase.class, "partido")
                .allowMainThreadQueries().build();
        mPartidoDao = database.getPartidoDao();
    }

    public static PartidoLab get(Context context) {
        if (sPartidoLab == null) {
            sPartidoLab = new PartidoLab(context);
        }
        return sPartidoLab;
    }

    public List<Partido> getPartidos() {
        return mPartidoDao.getPartidos();
    }

    public Partido getPartido(String id) {
        return mPartidoDao.getPartido(id);
    }

    public Partido getPartidosByStadium(String estadio) {
        return mPartidoDao.getPartidosByStadium(estadio);
    }

    public Partido getMoreRecentPartido() {
        return mPartidoDao.getMoreRecentPartido();
    }

    public List<Partido> getMoreRecentPartidos_limit3() {
        return mPartidoDao.getMoreRecentPartidos_limit3();
    }

    public List<Partido> getMoreRecentPartidos_limit6() {
        return mPartidoDao.getMoreRecentPartidos_limit6();
    }

    public void setPartidosNuke() {
        mPartidoDao.setPartidosNuke();
    }

    public void addPartido(Partido partido) {
        mPartidoDao.addPartido(partido);
    }

    public void updatePartido(Partido partido) {
        mPartidoDao.updatePartido(partido);
    }

    public void deletePartido(Partido partido) {
        mPartidoDao.deletePartido(partido);
    }
}
