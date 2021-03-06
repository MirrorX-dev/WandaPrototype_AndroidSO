package com.wandaprototype.android.PaaS.information.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.wandaprototype.android.objects.Partido;

import java.util.List;

/**
 * Interfaz común entre la aplicación y uno o más dispositivos de almacenamiento de datos
 * Contiene establecidad las consultas que serán utilizadas para la consulta de información.
 */
public interface PartidoDAO {
    @Dao
    /*
      Subclase de PartidoDao. Contiene la información dispuesta al
      usuario mediante consultas que devuelven la información en
      distintos tipos de datos primitivos.
     */
    interface PartidoDao {
        @Query("SELECT * FROM partido")
        List<Partido> getPartidos();

        @Query("SELECT * FROM partido WHERE id_partido LIKE :idbuscando")
        Partido getPartido(String idbuscando);

        @Query("SELECT * FROM partido WHERE id_partido = :id")
        int isDataExist(String id);

        //@Query()

        @Query("select * from partido where fpartido>=DATE('now','-1 day') and epartido = 'Wanda Metropolitano'  ORDER BY fpartido asc, hpartido LIMIT 1")
        //@Query("select * from partido where fpartido>=DATETIME('now', '-1 day') and hpartido>=TIME() and epartido = 'Wanda Metropolitano'  ORDER BY fpartido asc, hpartido LIMIT 1")
        Partido getMoreRecentPartido();

        @Query("select * from partido where fpartido>=DATE('now','-1 day') and epartido = 'Wanda Metropolitano'  ORDER BY fpartido asc, hpartido LIMIT 3")
        //@Query("select * from partido where fpartido>=DATETIME('now', '-1 day') and hpartido>=TIME() and epartido = 'Wanda Metropolitano'  ORDER BY fpartido asc, hpartido LIMIT 3")
        List<Partido> getMoreRecentPartidos_limit3();

        @Query("select * from partido where fpartido>=DATE('now','-1 day') and epartido = 'Wanda Metropolitano'  ORDER BY fpartido asc, hpartido LIMIT 20")
        //@Query("select * from partido where fpartido>=DATETIME('now', '-1 day') and hpartido>=TIME() and epartido = 'Wanda Metropolitano'  ORDER BY fpartido asc, hpartido LIMIT 20")
        List<Partido> getMoreRecentPartidos_limit6();

        @Query("SELECT * FROM partido WHERE epartido LIKE :estadio")
        Partido getPartidosByStadium(String estadio);

        @Query("DELETE FROM partido")
        void setPartidosNuke();

        //@Insert
        @Insert(onConflict = OnConflictStrategy.IGNORE)
        void addPartido(Partido partido);

        @Delete
        void deletePartido(Partido partido);

        @Update
        void updatePartido(Partido partido);
    }
}
