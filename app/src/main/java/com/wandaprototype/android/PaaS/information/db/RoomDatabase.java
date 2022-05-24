package com.wandaprototype.android.PaaS.information.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.wandaprototype.android.objects.Partido;
import com.wandaprototype.android.objects.PartidoVersions;

@Database(entities = {Partido.class, PartidoVersions.class}, version = 3, exportSchema = true)
abstract class PartidoDatabase extends RoomDatabase {
    public abstract PartidoDAO.PartidoDao getPartidoDao();
    public abstract PartidoVersionsDAO.PartidoVersionsDao getPartidoVersionDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
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
