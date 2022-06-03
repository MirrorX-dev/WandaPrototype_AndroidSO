package com.wandaprototype.android.PaaS.information.db;

import static com.wandaprototype.android.PaaS.information.db.PartidoDatabase.MIGRATION_1_2;
import static com.wandaprototype.android.PaaS.information.db.PartidoDatabase.MIGRATION_2_3;

import android.content.Context;

import androidx.room.Room;

import com.wandaprototype.android.objects.PartidoVersions;

import java.util.List;

/**
 * Usos de interfaces DAO. Interactua con la base de datos interna.
 *
 * @author Jesús Blanco Antoraz
 */
public class PartidoVersionsLab {
    private static PartidoVersionsLab sPartidoVersionsLab;
    private final PartidoVersionsDAO.PartidoVersionsDao mPartidoVersionsDao;

    /**
     * Se agregan parametros de migraciones en los casos
     * de introdución de nuevas tablas al schema de la
     * aplicación.
     *
     * @param context introduce llamada al contexto de la aplicación.
     * @author Jesús Blanco Antoraz
     */
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

    /**
     * Obtiene un listado de todos las versiones de partidos almacenados.
     *
     * @return listado partidosVersiones
     */
    public List<PartidoVersions> getPartidoVersionDao() {
        return mPartidoVersionsDao.getPartidoVersionDao();
    }

    /**
     * Añade una nueva versión de informacíón de partidos.
     *
     * @param partidoVersions llamda a la instancia del objeto
     */
    public void addPartidoVersions(PartidoVersions partidoVersions) {
        mPartidoVersionsDao.addPartidoVersions(partidoVersions);
    }

    /**
     * Formatea la tabla que contiene las versiones de partidos
     */
    public void setPartidoVersionsNuke() {
        mPartidoVersionsDao.setPartidoVersionsNuke();
    }

}
