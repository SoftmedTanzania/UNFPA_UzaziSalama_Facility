package apps.uzazisalama.com.anc.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import apps.uzazisalama.com.anc.database.ClientAppointment;
import apps.uzazisalama.com.anc.database.RoutineVisits;

/**
 * Created by issy on 21/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project UNFPA_UzaziSalama_Facility
 */

public class RoutineResponse implements Serializable {

    @SerializedName("routineVisitDTO")
    RoutineVisits routineVisits;

    @SerializedName("patientsAppointmentsDTOS")
    List<ClientAppointment> appointments;

    public RoutineVisits getRoutineVisits() {
        return routineVisits;
    }

    public void setRoutineVisits(RoutineVisits routineVisits) {
        this.routineVisits = routineVisits;
    }

    public List<ClientAppointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<ClientAppointment> appointments) {
        this.appointments = appointments;
    }
}
