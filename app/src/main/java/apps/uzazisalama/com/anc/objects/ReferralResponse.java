package apps.uzazisalama.com.anc.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import apps.uzazisalama.com.anc.database.AncClient;
import apps.uzazisalama.com.anc.database.ClientAppointment;
import apps.uzazisalama.com.anc.database.Referral;
import apps.uzazisalama.com.anc.database.RoutineVisits;

/**
 * Created by issy on 20/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project UNFPA_UzaziSalama_Facility
 */

public class ReferralResponse implements Serializable{

    @SerializedName("ancClientDTO")
    private AncClient ancClient;

    @SerializedName("patientReferralsList")
    private List<Referral> clientReferrals;

    @SerializedName("patientsAppointmentsDTOS")
    private List<ClientAppointment> clientAppointments;

    @SerializedName("routineVisitDTOS")
    private List<RoutineVisits> routineVisits;

    public AncClient getAncClient() {
        return ancClient;
    }

    public void setAncClient(AncClient ancClient) {
        this.ancClient = ancClient;
    }

    public List<Referral> getClientReferrals() {
        return clientReferrals;
    }

    public void setClientReferrals(List<Referral> clientReferrals) {
        this.clientReferrals = clientReferrals;
    }

    public List<ClientAppointment> getClientAppointments() {
        return clientAppointments;
    }

    public void setClientAppointments(List<ClientAppointment> clientAppointments) {
        this.clientAppointments = clientAppointments;
    }

    public List<RoutineVisits> getRoutineVisits() {
        return routineVisits;
    }

    public void setRoutineVisits(List<RoutineVisits> routineVisits) {
        this.routineVisits = routineVisits;
    }
}
