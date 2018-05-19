package apps.uzazisalama.com.anc.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by issy on 18/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project ANC
 */

@Entity(tableName = "Referral")
public class Referral {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String referralID;

    private String ancClientID;

    private String referralUUID;

    private String referralReason;

    private String instanceID;

    private String facilityID;

    private long referralDate;

    private String serviceProviderUUID;

    private String fromFacailityID;

    private String otherClinicalInformation;

    /**
     *  1
     *  2
     */
    private int referralType;

    private int referralStatus;

    public String getReferralID() {
        return referralID;
    }

    public void setReferralID(String referralID) {
        this.referralID = referralID;
    }

    public String getAncClientID() {
        return ancClientID;
    }

    public void setAncClientID(String ancClientID) {
        this.ancClientID = ancClientID;
    }

    public String getReferralUUID() {
        return referralUUID;
    }

    public void setReferralUUID(String referralUUID) {
        this.referralUUID = referralUUID;
    }

    public String getReferralReason() {
        return referralReason;
    }

    public void setReferralReason(String referralReason) {
        this.referralReason = referralReason;
    }

    public String getInstanceID() {
        return instanceID;
    }

    public void setInstanceID(String instanceID) {
        this.instanceID = instanceID;
    }

    public String getFacilityID() {
        return facilityID;
    }

    public void setFacilityID(String facilityID) {
        this.facilityID = facilityID;
    }

    public long getReferralDate() {
        return referralDate;
    }

    public void setReferralDate(long referralDate) {
        this.referralDate = referralDate;
    }

    public String getServiceProviderUUID() {
        return serviceProviderUUID;
    }

    public void setServiceProviderUUID(String serviceProviderUUID) {
        this.serviceProviderUUID = serviceProviderUUID;
    }

    public String getFromFacailityID() {
        return fromFacailityID;
    }

    public void setFromFacailityID(String fromFacailityID) {
        this.fromFacailityID = fromFacailityID;
    }

    public String getOtherClinicalInformation() {
        return otherClinicalInformation;
    }

    public void setOtherClinicalInformation(String otherClinicalInformation) {
        this.otherClinicalInformation = otherClinicalInformation;
    }

    public int getReferralType() {
        return referralType;
    }

    public void setReferralType(int referralType) {
        this.referralType = referralType;
    }

    public int getReferralStatus() {
        return referralStatus;
    }

    public void setReferralStatus(int referralStatus) {
        this.referralStatus = referralStatus;
    }
}
