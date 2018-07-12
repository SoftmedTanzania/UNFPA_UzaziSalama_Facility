package apps.uzazisalama.com.anc.database;

/**
 * Created by issy on 11/07/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project UNFPA_UzaziSalama_Facility
 */

public class ReferralDetailsBinder {

    private Referral referral;

    private AncClient client;

    private String referralDate;

    private String age;

    public Referral getReferral() {
        return referral;
    }

    public void setReferral(Referral referral) {
        this.referral = referral;
    }

    public AncClient getClient() {
        return client;
    }

    public void setClient(AncClient client) {
        this.client = client;
    }

    public String getReferralDate() {
        return referralDate;
    }

    public void setReferralDate(String referralDate) {
        this.referralDate = referralDate;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
