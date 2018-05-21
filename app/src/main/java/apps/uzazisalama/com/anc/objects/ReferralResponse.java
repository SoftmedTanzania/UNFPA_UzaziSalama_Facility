package apps.uzazisalama.com.anc.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import apps.uzazisalama.com.anc.database.AncClient;
import apps.uzazisalama.com.anc.database.Referral;

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
}
