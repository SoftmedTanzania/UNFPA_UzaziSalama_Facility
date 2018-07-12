package apps.uzazisalama.com.anc.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import apps.uzazisalama.com.anc.database.RoutineVisits;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by issy on 16/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project ANC
 */

@Dao
public interface RoutineVisitsModelDao {

    @Query("select * from RoutineVisits")
    LiveData<List<RoutineVisits>> getAllRoutines();

    @Query("select * from RoutineVisits where healthFacilityClientId = :healthFacilityClientID")
    LiveData<List<RoutineVisits>> getRoutinesByClientId(long healthFacilityClientID);

    @Query("select * from RoutineVisits where healthFacilityClientId = :healthFacilityClientID")
    List<RoutineVisits> getClientRoutines(long healthFacilityClientID);

    @Query("select * from RoutineVisits where healthFacilityClientId = :healthFacilityClientID and visitNumber = 1 and visitDate between :fromDate and :dateTo")
    List<RoutineVisits> getVisitOneClientRoutines(long healthFacilityClientID, long fromDate, long dateTo);

    @Query("select * from RoutineVisits where healthFacilityClientId = :healthFacilityClientID and visitNumber >= 4 and visitDate between :fromDate and :dateTo")
    List<RoutineVisits> getVisitFourAndAboveClientRoutines(long healthFacilityClientID, long fromDate, long dateTo);

    @Insert(onConflict = REPLACE)
    void addRoutine(RoutineVisits routineVisits);

    @Delete
    void deleteRoutine(RoutineVisits routineVisits);

}
