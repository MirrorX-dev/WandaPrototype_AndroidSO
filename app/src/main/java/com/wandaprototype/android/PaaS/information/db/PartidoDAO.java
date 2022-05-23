package com.wandaprototype.android.PaaS.information.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.wandaprototype.android.objects.Partido;

import java.util.List;

public interface PartidoDAO {
    @Dao
    public interface PartidoDao {

        @Query("SELECT * FROM partido")
        List<Partido> getPartidos();

        @Query("SELECT * FROM partido WHERE id_partido LIKE :idbuscando")
        Partido getPartido(String idbuscando);

        @Query("select * from partido where fpartido>=DATE() and epartido = 'Wanda Metropolitano'  ORDER BY fpartido LIMIT 1")
        Partido getMoreRecentPartido();

        @Query("select * from partido where fpartido>=DATE() and epartido = 'Wanda Metropolitano'  ORDER BY fpartido LIMIT 3")
        List<Partido>  getMoreRecentPartidos_limit3();

        @Query("select * from partido where fpartido>=DATE() and epartido = 'Wanda Metropolitano'  ORDER BY fpartido LIMIT 6")
        List<Partido>  getMoreRecentPartidos_limit6();

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
