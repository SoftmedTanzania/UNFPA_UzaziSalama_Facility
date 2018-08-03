package apps.uzazisalama.com.anc.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import apps.uzazisalama.com.anc.database.AncClient;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by issy on 12/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project ANC
 */

@Dao
public interface AncClientModelDao {

    @Query("SELECT * from AncClient")
    LiveData<List<AncClient>> getAllClients();

    @Query("select * from AncClient where healthFacilityClientId = :clientId")
    AncClient getClientById(long clientId );

    @Query("select * from AncClient where healthFacilityClientId = :clientId")
    LiveData<AncClient> getLiveAncClient(long clientId);

    @Query("select * from AncClient where clientType = 1 ") //Room maps true to 1 false to 0
    LiveData<List<AncClient>> getAllAncClients();

    @Query("select * from AncClient where healthFacilityClientId = :clientID")
    List<AncClient> getItemById(long clientID);

    //Report Helpers
    @Query("select count(*) from AncClient where createdAt >= :fromDate and createdAt <= :toDate ")
    LiveData<Integer> getPeriodicRegisteredWomen(long fromDate, long toDate);

    @Query("select * from AncClient where clientType = 1 and gestationalAgeBelow20 = 1 and createdAt between :fromDate and :toDate") //Room maps true to 1 false to 0
    LiveData<List<AncClient>> getFirstVisitBelowTwelveWeeksClients(long fromDate, long toDate);

    @Insert(onConflict = REPLACE)
    void addNewClient(AncClient ancClient);

    @Delete
    void deleteAClient(AncClient ancClient);

    @Update
    void updateClient(AncClient ancClient);

}
