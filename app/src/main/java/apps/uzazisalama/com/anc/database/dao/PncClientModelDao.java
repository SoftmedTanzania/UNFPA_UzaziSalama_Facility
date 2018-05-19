package apps.uzazisalama.com.anc.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import apps.uzazisalama.com.anc.database.AncClient;
import apps.uzazisalama.com.anc.database.PncClient;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by issy on 19/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project UNFPA_UzaziSalama_Facility
 */

@Dao
public interface PncClientModelDao {

    @Query("SELECT * from PncClient")
    LiveData<List<PncClient>> getAllClients();

    @Insert(onConflict = REPLACE)
    void addNewClient(PncClient pncClient);

    @Delete
    void deleteAClient(PncClient pncClient);

    @Update
    void updateClient(PncClient pncClient);

}
