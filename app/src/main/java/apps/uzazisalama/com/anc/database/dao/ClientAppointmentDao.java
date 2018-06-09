package apps.uzazisalama.com.anc.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import apps.uzazisalama.com.anc.database.ClientAppointment;
import apps.uzazisalama.com.anc.database.Referral;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by issy on 22/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project UNFPA_UzaziSalama_Facility
 */

@Dao
public interface ClientAppointmentDao {

    @Query("select * from ClientAppointment")
    LiveData<List<ClientAppointment>> getAllAppointments();

    @Query("select * from ClientAppointment where healthFacilityClientId = :healthFacilityClientId " +
            "and visitNumber = :visitNumber")
    List<ClientAppointment> getAppointmentByClientAndVisitNumber(long healthFacilityClientId, int visitNumber);

    @Query("select * from ClientAppointment where healthFacilityClientId = :healthFacilityClientId order by appointmentDate asc")
    List<ClientAppointment> getAppointmentsByClientId(long healthFacilityClientId);

    @Insert(onConflict = REPLACE)
    void addNewAppointment(ClientAppointment appointment);

    @Delete
    void deleteAppointment(ClientAppointment appointment);

    @Update
    void updateAppointment(ClientAppointment appointment);

}
