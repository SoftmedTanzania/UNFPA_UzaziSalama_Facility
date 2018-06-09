package apps.uzazisalama.com.anc.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import apps.uzazisalama.com.anc.database.AncClient;
import apps.uzazisalama.com.anc.database.ClientAppointment;
import apps.uzazisalama.com.anc.database.Referral;

/**
 * Created by issy on 22/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project UNFPA_UzaziSalama_Facility
 */

public class RegistrationResponse implements Serializable {

    @SerializedName("ancClientDTO")
    private AncClient client;

    @SerializedName("patientReferralsList")
    private List<Referral> clientReferrals;

    @SerializedName("patientsAppointmentsDTOS")
    private List<ClientAppointment> clientAppointments;

    public AncClient getClient() {
        return client;
    }

    public void setClient(AncClient client) {
        this.client = client;
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
}
