package com.wandaprototype.android.PaaS.information.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.wandaprototype.android.objects.PartidoVersions;

import java.util.List;

/**
 * Interfaz común entre la aplicación y uno o más dispositivos de almacenamiento de datos
 * Contiene establecidad las consultas que serán utilizadas para la consulta de información.
 */
public interface PartidoVersionsDAO {
    @Dao
    /*
      Subclase de PartidoDao. Contiene la información dispuesta al
      usuario mediante consultas que devuelven la información en
      distintos tipos de datos primitivos.
     */ interface PartidoVersionsDao {
        /**
         * Obtiene todos las versiones referentes a los datos de partidos.
         *
         * @return listado de Versiones de partidos
         */
        @Query("SELECT * FROM partidoVersions")
        List<PartidoVersions> getPartidoVersionDao();

        /**
         * Inserta una versión de partidos.
         *
         * @param partidoVersions instancia de objeto partidoVersions
         */
        @Insert(onConflict = OnConflictStrategy.IGNORE)
        void addPartidoVersions(PartidoVersions partidoVersions);

        /**
         * Elimina todos los datos de versiones de partidos
         */
        @Query("DELETE FROM partidoVersions")
        void setPartidoVersionsNuke();
    }
}
