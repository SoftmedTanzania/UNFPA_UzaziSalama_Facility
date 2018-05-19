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

    @Query("select * from RoutineVisits where ancClientID = :clientID")
    LiveData<List<RoutineVisits>> getRoutinesByClientId(String clientID);

    @Query("select * from RoutineVisits where ancClientID = :clientID")
    List<RoutineVisits> getClientRoutines(String clientID);

    @Insert(onConflict = REPLACE)
    void addRoutine(RoutineVisits routineVisits);

    @Delete
    void deleteRoutine(RoutineVisits routineVisits);

}
