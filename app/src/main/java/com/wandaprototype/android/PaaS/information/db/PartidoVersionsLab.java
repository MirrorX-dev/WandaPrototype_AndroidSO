package com.wandaprototype.android.PaaS.information.db;

import static com.wandaprototype.android.PaaS.information.db.PartidoDatabase.MIGRATION_1_2;
import static com.wandaprototype.android.PaaS.information.db.PartidoDatabase.MIGRATION_2_3;

import android.content.Context;

import androidx.room.Room;

import com.wandaprototype.android.objects.Partido;
import com.wandaprototype.android.objects.PartidoVersions;

import java.util.List;

public class PartidoVersionsLab {
    private static PartidoVersionsLab sPartidoVersionsLab;
    private PartidoVersionsDAO.PartidoVersionsDao mPartidoVersionsDao;

    private PartidoVersionsLab(Context context) {
        Context appContext = context.getApplicationContext();
        PartidoDatabase database = Room.databaseBuilder(appContext, PartidoDatabase.class, "partidoVersions")
                //.fallbackToDestructiveMigration()
                .addMigrations(MIGRATION_1_2)
                .addMigrations(MIGRATION_2_3)
                .allowMainThreadQueries().build();
        mPartidoVersionsDao = database.getPartidoVersionDao();
    }

    public static PartidoVersionsLab get(Context context) {
        if (sPartidoVersionsLab == null) {
            sPartidoVersionsLab = new PartidoVersionsLab(context);
        }
        return sPartidoVersionsLab;
    }

    public List<PartidoVersions> getPartidoVersionDao() {
        return mPartidoVersionsDao.getPartidoVersionDao();
    }

    public void addPartidoVersions(PartidoVersions partidoVersions) {
        mPartidoVersionsDao.addPartidoVersions(partidoVersions);
    }

    public void setPartidoVersionsNuke() {
        mPartidoVersionsDao.setPartidoVersionsNuke();
    }

}
