package com.wandaprototype.android.PaaS.information.db;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.wandaprototype.android.objects.Partido;
import com.wandaprototype.android.objects.PartidoVersions;

/**
 * Declaramos las entidades "tablas" que actuaran sobre nuestra base de datos
 * La base de datos es titulada "PartidoDatabase" no confundir con "partidos".
 * Se especifica la versión de cambios debido a migraciones por cambios en el
 * schema de la base de datos por cambios de tablas o nuevas adiciones.
 */
@Database(entities = {Partido.class, PartidoVersions.class}, version = 3)
abstract class PartidoDatabase extends RoomDatabase {
    /**
     * Se debe declarar cada uno de los aceso a datos DAO.
     *
     * @return Funciones y acceso a datos.
     */
    public abstract PartidoDAO.PartidoDao getPartidoDao();

    /**
     * Se debe declarar cada uno de los aceso a datos DAO.
     *
     * @return Funciones y acceso a datos.
     */
    public abstract PartidoVersionsDAO.PartidoVersionsDao getPartidoVersionDao();

    /**
     * Por cada migración debemos indicar los cambios sucedidos:
     * Migración por adición de tabla de versiones de partidos.
     */
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
        }
    };

    /**
     * Migración debido a cambio del numero de versión.
     * Implica incremento del mismo y poner clausula con los cambios.
     * Si fuese necesario por cada migración se generan querys.
     */
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Create the new table
            //database.execSQL("CREATE TABLE partidoVersions (vnumber INTEGER, vdate TEXT, PRIMARY KEY(vnumber))");

            // Copy the data
            //database.execSQL("INSERT INTO users_new (userid, username, last_update) SELECT userid, username, last_update FROM users");
            // Remove the old table
            //database.execSQL("DROP TABLE users");
            // Change the table name to the correct one
            //database.execSQL("ALTER TABLE users_new RENAME TO users");
        }
    };

}

/*
// User and Book are classes annotated with  @Entity.
    @Database(version = 1, entities = {User.class, Book.class})
   abstract class AppDatabase extends RoomDatabase {
       // BookDao is a class annotated with  @Dao.
       abstract public BookDao bookDao();
       // UserDao is a class annotated with  @Dao.
       abstract public UserDao userDao();
       // UserBookDao is a class annotated with  @Dao.
       abstract public UserBookDao userBookDao();
   }
 */
