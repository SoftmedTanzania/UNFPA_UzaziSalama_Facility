package apps.uzazisalama.com.anc.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by issy on 18/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project ANC
 */

@Entity(tableName = "Referral")
public class Referral implements Serializable{

    @PrimaryKey(autoGenerate = false)
    @NonNull
    @SerializedName("id")
    private int referralID;

    @SerializedName("healthFacilityClientId")
    private long healthFacilityClientID;

    @SerializedName("ancClientId")
    private String ancClientID;

    @SerializedName("referralUUID")
    private String referralUUID;

    @SerializedName("referralReason")
    private String referralReason;

    @SerializedName("instanceId")
    private String instanceID;

    @SerializedName("facilityId")
    private String facilityID;

    @SerializedName("referralDate")
    private long referralDate;

    @SerializedName("serviceProviderUUID")
    private String serviceProviderUUID;

    @SerializedName("fromFacilityId")
    private String fromFacailityID;

    @SerializedName("referralFeedback")
    private String referralFeedback;

    @SerializedName("otherClinicalInformation")
    private String otherClinicalInformation;

    /**
     *  1 -> Chw referral
     *  2 -> Health Facility Referrals
     *  3 -> Health Facility to CHW
     */
    @SerializedName("referralType")
    private int referralType;

    @SerializedName("referralStatus")
    private int referralStatus;

    @NonNull
    public int getReferralID() {
        return referralID;
    }

    public void setReferralID(@NonNull int referralID) {
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

    public String getReferralFeedback() {
        return referralFeedback;
    }

    public void setReferralFeedback(String referralFeedback) {
        this.referralFeedback = referralFeedback;
    }

    public long getHealthFacilityClientID() {
        return healthFacilityClientID;
    }

    public void setHealthFacilityClientID(long healthFacilityClientID) {
        this.healthFacilityClientID = healthFacilityClientID;
    }
}
