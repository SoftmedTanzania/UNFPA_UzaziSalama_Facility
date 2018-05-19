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

@Entity(tableName = "PncClient")
public class PncClient {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String pncClientID;

    private String ancClientID;

    // Date that the mother was admitted to the hospital
    private long dateOfAdmission;

    private long dateOfDelivery;

    private boolean kuharibikaMimba;

    /**
     *  1 -> Normal Delivery,
     *  2 -> Vacuum,
     *  3 -> c-section
     */
    private int deliveryMethod;

    /**
     *  1 -> Postmotum Haemorrhage,
     *  2 -> Third degree tear ,
     *  3 -> Retained Placenta
     */
    private int deliveryComplications;


    /**
     *  0 -> Bad
     *  1 -> Good
     */
    private int motherDischargeCondition;

    //Child's Condition
    private String childGender;

    private double childWeight;

    /**
     *  Child's five senses, each sense is 2 score
     *  1 - 10
     */
    private int apgarScore;

    /**
     *  True if present
     */
    private boolean childAbnomalities;

    /**
     *  0 -> Bad
     *  1 -> Good
     */
    private int childDischargeCondition;

    /**
     * True if child has died within the past 24 hours
     */
    private boolean diedWithin24Hours;

    /**
     * 1 -> FBS
     * 2 -> MSB
     */
    private int stillBirthTypes;

    public String getPncClientID() {
        return pncClientID;
    }

    public void setPncClientID(String pncClientID) {
        this.pncClientID = pncClientID;
    }

    public String getAncClientID() {
        return ancClientID;
    }

    public void setAncClientID(String ancClientID) {
        this.ancClientID = ancClientID;
    }

    public long getDateOfAdmission() {
        return dateOfAdmission;
    }

    public void setDateOfAdmission(long dateOfAdmission) {
        this.dateOfAdmission = dateOfAdmission;
    }

    public long getDateOfDelivery() {
        return dateOfDelivery;
    }

    public void setDateOfDelivery(long dateOfDelivery) {
        this.dateOfDelivery = dateOfDelivery;
    }

    public boolean isKuharibikaMimba() {
        return kuharibikaMimba;
    }

    public void setKuharibikaMimba(boolean kuharibikaMimba) {
        this.kuharibikaMimba = kuharibikaMimba;
    }

    public int getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(int deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public int getDeliveryComplications() {
        return deliveryComplications;
    }

    public void setDeliveryComplications(int deliveryComplications) {
        this.deliveryComplications = deliveryComplications;
    }

    public int getMotherDischargeCondition() {
        return motherDischargeCondition;
    }

    public void setMotherDischargeCondition(int motherDischargeCondition) {
        this.motherDischargeCondition = motherDischargeCondition;
    }

    public String getChildGender() {
        return childGender;
    }

    public void setChildGender(String childGender) {
        this.childGender = childGender;
    }

    public double getChildWeight() {
        return childWeight;
    }

    public void setChildWeight(double childWeight) {
        this.childWeight = childWeight;
    }

    public int getApgarScore() {
        return apgarScore;
    }

    public void setApgarScore(int apgarScore) {
        this.apgarScore = apgarScore;
    }

    public boolean isChildAbnomalities() {
        return childAbnomalities;
    }

    public void setChildAbnomalities(boolean childAbnomalities) {
        this.childAbnomalities = childAbnomalities;
    }

    public int getChildDischargeCondition() {
        return childDischargeCondition;
    }

    public void setChildDischargeCondition(int childDischargeCondition) {
        this.childDischargeCondition = childDischargeCondition;
    }

    public boolean isDiedWithin24Hours() {
        return diedWithin24Hours;
    }

    public void setDiedWithin24Hours(boolean diedWithin24Hours) {
        this.diedWithin24Hours = diedWithin24Hours;
    }

    public int getStillBirthTypes() {
        return stillBirthTypes;
    }

    public void setStillBirthTypes(int stillBirthTypes) {
        this.stillBirthTypes = stillBirthTypes;
    }

}
