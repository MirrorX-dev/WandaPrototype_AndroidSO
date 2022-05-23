package com.wandaprototype.android.PaaS.information.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.wandaprototype.android.objects.Partido;

@Database(entities = {Partido.class}, version = 1)
abstract class PartidoDatabase extends RoomDatabase {
    public abstract PartidoDAO.PartidoDao getPartidoDao();
}
