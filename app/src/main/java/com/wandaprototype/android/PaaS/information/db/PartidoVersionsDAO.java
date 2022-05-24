package com.wandaprototype.android.PaaS.information.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.wandaprototype.android.objects.Partido;
import com.wandaprototype.android.objects.PartidoVersions;

import java.util.List;

public interface PartidoVersionsDAO {
    @Dao
    public interface PartidoVersionsDao {
        @Query("SELECT * FROM partidoVersions")
        List<PartidoVersions> getPartidoVersionDao();

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        void addPartidoVersions(PartidoVersions partidoVersions);
    }
}
