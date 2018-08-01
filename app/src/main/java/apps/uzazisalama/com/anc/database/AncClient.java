package apps.uzazisalama.com.anc.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by issy on 12/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project ANC
 */

@Entity(tableName = "AncClient")
public class AncClient implements Serializable{

    @PrimaryKey(autoGenerate = false)
    @NonNull
    @SerializedName("healthFacilityClientId")
    private long healthFacilityClientId;

    //Demographic information

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("middleName")
    private String middleName;

    @SerializedName("surname")
    private String surname;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("ward")
    private String ward;

    @SerializedName("village")
    private String village;

    @SerializedName("mapCue")
    private String mapCue;

    @SerializedName("dateOfBirth")
    private long dateOfBirth;

    @SerializedName("pmtctStatus")
    private int pmctcStatus; // 0 = unknown, 1 and 2

    @SerializedName("heightBelowAverage")
    private boolean heightBelowAverage; //true if below 150

    @SerializedName("levelOfEducation")
    private int levelOfEducation; //TODO => Change to ID (int)

    @SerializedName("spouseName")
    private String spouseName;

    //Pregnancy Details
    @SerializedName("gravida")
    private int gravida;

    @SerializedName("para")
    private int para;

    @SerializedName("lmnpDate")
    private long lnmp; //Last Normal Menstrual Period Date

    @SerializedName("edd")
    private long edd; //Expected Date of Delivery

    @SerializedName("gestationalAgeBelow20") //Todo => Refactor to "Gestational Age below 12 weeks*
    private boolean gestationalAgeBelow20;

    @SerializedName("historyOfAbortion")
    private boolean historyOfAbortion;

    @SerializedName("ageBelow20Years")
    private boolean ageBelow20Years;

    @SerializedName("lastPregnancyOver10Years")
    private boolean lastPregnancyOver10yearsAgo;

    @SerializedName("pregnancyAbove35Years")
    private boolean pregnancyAbove35Years;

    @SerializedName("historyOfStillBirth")
    private boolean historyOfStillBirths;

    @SerializedName("historyOfPostmartumHaemorrhage")
    private boolean historyOfPostmartumHaemorrhage;

    @SerializedName("historyOfRetainedPlacenta")
    private boolean historyOfRetainedPlacenta;

    /**
     *  0 -> None
     *  1 -> Condoms
     *  2 -> Pills
     *  3 -> Calendar
     */
    private int familyPlanningMethod;

    /**
     *  0 -> general
     *  1 -> ANC
     *  2 -> PNC
     */
    @SerializedName("clientType")
    private int clientType;

    @SerializedName("cardNumber")
    private String cardNumber;

    /**
     * Year
     */
    @SerializedName("lastChildbirthYear")
    private int lastChildBirthYear;

    /**
     *  0 -> Dead
     *  1 -> Alive
     */
    @SerializedName("lastChildbirthStatus")
    private int lastChildBirthStatus;

    @SerializedName("healthFacilityCode")
    private String healthFacilityCode;

    @SerializedName("createdAt")
    private long createdAt;

    @NonNull
    public long getHealthFacilityClientId() {
        return healthFacilityClientId;
    }

    public void setHealthFacilityClientId(@NonNull long healthFacilityClientId) {
        this.healthFacilityClientId = healthFacilityClientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public boolean isHeightBelowAverage() {
        return heightBelowAverage;
    }

    public void setHeightBelowAverage(boolean heightBelowAverage) {
        this.heightBelowAverage = heightBelowAverage;
    }

    public int getLevelOfEducation() {
        return levelOfEducation;
    }

    public void setLevelOfEducation(int levelOfEducation) {
        this.levelOfEducation = levelOfEducation;
    }

    public int getGravida() {
        return gravida;
    }

    public void setGravida(int gravida) {
        this.gravida = gravida;
    }

    public int getPara() {
        return para;
    }

    public void setPara(int para) {
        this.para = para;
    }

    public String getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }

    public long getLnmp() {
        return lnmp;
    }

    public void setLnmp(long lnmp) {
        this.lnmp = lnmp;
    }

    public long getEdd() {
        return edd;
    }

    public void setEdd(long edd) {
        this.edd = edd;
    }

    public boolean isGestationalAgeBelow20() {
        return gestationalAgeBelow20;
    }

    public void setGestationalAgeBelow20(boolean gestationalAgeBelow20) {
        this.gestationalAgeBelow20 = gestationalAgeBelow20;
    }

    public int getPmctcStatus() {
        return pmctcStatus;
    }

    public void setPmctcStatus(int pmctcStatus) {
        this.pmctcStatus = pmctcStatus;
    }

    public boolean isHistoryOfAbortion() {
        return historyOfAbortion;
    }

    public void setHistoryOfAbortion(boolean historyOfAbortion) {
        this.historyOfAbortion = historyOfAbortion;
    }

    public boolean isAgeBelow20Years() {
        return ageBelow20Years;
    }

    public void setAgeBelow20Years(boolean ageBelow20Years) {
        this.ageBelow20Years = ageBelow20Years;
    }

    public boolean isLastPregnancyOver10yearsAgo() {
        return lastPregnancyOver10yearsAgo;
    }

    public void setLastPregnancyOver10yearsAgo(boolean lastPregnancyOver10yearsAgo) {
        this.lastPregnancyOver10yearsAgo = lastPregnancyOver10yearsAgo;
    }

    public boolean isPregnancyAbove35Years() {
        return pregnancyAbove35Years;
    }

    public void setPregnancyAbove35Years(boolean pregnancyAbove35Years) {
        this.pregnancyAbove35Years = pregnancyAbove35Years;
    }

    public boolean isHistoryOfStillBirths() {
        return historyOfStillBirths;
    }

    public void setHistoryOfStillBirths(boolean historyOfStillBirths) {
        this.historyOfStillBirths = historyOfStillBirths;
    }

    public boolean isHistoryOfPostmartumHaemorrhage() {
        return historyOfPostmartumHaemorrhage;
    }

    public void setHistoryOfPostmartumHaemorrhage(boolean historyOfPostmartumHaemorrhage) {
        this.historyOfPostmartumHaemorrhage = historyOfPostmartumHaemorrhage;
    }

    public boolean isHistoryOfRetainedPlacenta() {
        return historyOfRetainedPlacenta;
    }

    public void setHistoryOfRetainedPlacenta(boolean historyOfRetainedPlacenta) {
        this.historyOfRetainedPlacenta = historyOfRetainedPlacenta;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getMapCue() {
        return mapCue;
    }

    public void setMapCue(String mapCue) {
        this.mapCue = mapCue;
    }

    public int getLastChildBirthYear() {
        return lastChildBirthYear;
    }

    public void setLastChildBirthYear(int lastChildBirthYear) {
        this.lastChildBirthYear = lastChildBirthYear;
    }

    public int getLastChildBirthStatus() {
        return lastChildBirthStatus;
    }

    public void setLastChildBirthStatus(int lastChildBirthStatus) {
        this.lastChildBirthStatus = lastChildBirthStatus;
    }

    public String getHealthFacilityCode() {
        return healthFacilityCode;
    }

    public void setHealthFacilityCode(String healthFacilityCode) {
        this.healthFacilityCode = healthFacilityCode;
    }

    public int getFamilyPlanningMethod() {
        return familyPlanningMethod;
    }

    public void setFamilyPlanningMethod(int familyPlanningMethod) {
        this.familyPlanningMethod = familyPlanningMethod;
    }

    public int getClientType() {
        return clientType;
    }

    public void setClientType(int clientType) {
        this.clientType = clientType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

}
