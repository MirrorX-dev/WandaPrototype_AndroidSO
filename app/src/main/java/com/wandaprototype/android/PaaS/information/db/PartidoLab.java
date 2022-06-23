package com.wandaprototype.android.PaaS.information.db;

import static com.wandaprototype.android.PaaS.information.db.PartidoDatabase.MIGRATION_1_2;
import static com.wandaprototype.android.PaaS.information.db.PartidoDatabase.MIGRATION_2_3;

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

    private final PartidoDAO.PartidoDao mPartidoDao;

    /**
     * Se agregan parametros de migraciones en los casos
     * de introdución de nuevas tablas al schema de la
     * aplicación.
     *
     * @param context instancia del contexto
     * @author Jesús Blanco Antoraz
     */
    private PartidoLab(Context context) {
        Context appContext = context.getApplicationContext();
        PartidoDatabase database = Room.databaseBuilder(appContext, PartidoDatabase.class, "partido")
                //.fallbackToDestructiveMigration()
                .addMigrations(MIGRATION_1_2)
                .addMigrations(MIGRATION_2_3)
                .allowMainThreadQueries().build();
        mPartidoDao = database.getPartidoDao();
    }

    public static PartidoLab get(Context context) {
        if (sPartidoLab == null) {
            sPartidoLab = new PartidoLab(context);
        }
        return sPartidoLab;
    }

    /**
     * Obtiene un listado de todos los partidos almacenados.
     *
     * @return listado partidos
     */
    public List<Partido> getPartidos() {
        return mPartidoDao.getPartidos();
    }

    /**
     * Permite obtener la información de un partido mediante
     * su indetificador
     *
     * @param id identificador del partido
     * @return partido
     */
    public Partido getPartido(String id) {
        return mPartidoDao.getPartido(id);
    }

    /**
     * Comprueba que el partido existe mediante
     * su identificador
     *
     * @param id identificador del partido.
     * @return numerico
     */
    public int isDataExist(String id) {
        return mPartidoDao.isDataExist(id);
    }

    /**
     * Obtiene los partidos por estadio, permite filtrar
     * la información al almacenar distintos partidos.
     *
     * @param estadio Titulo de estadio
     * @return partidos filtrados por estadio
     */
    public Partido getPartidosByStadium(String estadio) {
        return mPartidoDao.getPartidosByStadium(estadio);
    }

    /**
     * Permite obtener el partidos mas reciente
     *
     * @return partido mas reciente.
     */
    public Partido getMoreRecentPartido() {
        return mPartidoDao.getMoreRecentPartido();
    }

    /**
     * Obtiene todos los partidos mas recientes max 3.
     *
     * @return listado partidos 3.
     */
    public List<Partido> getMoreRecentPartidos_limit3() {
        return mPartidoDao.getMoreRecentPartidos_limit3();
    }

    /**
     * Obtiene los partidos mas reciente max 6.
     *
     * @return listado partidos 6.
     */
    public List<Partido> getMoreRecentPartidos_limit6() {
        return mPartidoDao.getMoreRecentPartidos_limit6();
    }

    /**
     * Formatea la tabla de partidos.
     */
    public void setPartidosNuke() {
        mPartidoDao.setPartidosNuke();
    }

    /**
     * Añade un nuevo partido
     *
     * @param partido instancia de objeto Partido partido
     */
    public void addPartido(Partido partido) {
        mPartidoDao.addPartido(partido);
    }

    /**
     * Actualiza la información de un partido.
     *
     * @param partido instancia de objeto Partido partido
     */
    public void updatePartido(Partido partido) {
        mPartidoDao.updatePartido(partido);
    }

    /**
     * Borra un partido
     *
     * @param partido instancia de objeto Partido partido
     */
    public void deletePartido(Partido partido) {
        mPartidoDao.deletePartido(partido);
    }
}
