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
    @SerializedName("clientID")
    private String ID;

    //Demographic information

    private String firstName;

    private String middleName;

    private String surname;

    private String phoneNumber;

    private String ward;

    private String village;

    private String mapCue;

    private long dateOfBirth;

    private boolean pmctcStatus; // 0 = unknown, 1 and 2

    private boolean heightBelowAverage; //true if below 150

    private String levelOfEducation; //TODO => Change to ID (int)

    private String spouseName;

    //Pregnancy Details
    private int gravida;

    private int para;

    private long lnmp; //Last Normal Menstrual Period Date

    private long edd; //Expected Date of Delivery

    private boolean gestationalAgeBelow20;

    private boolean historyOfAbortion;

    private boolean ageBelow20Years;

    private boolean lastPregnancyOver10yearsAgo;

    private boolean pregnancyAbove35Years;

    private boolean historyOfStillBirths;

    private boolean historyOfPostmartumHaemorrhage;

    private boolean historyOfRetainedPlacenta;

    /**
     *  0 -> false
     *  1 -> true
     */
    private int pncStatus;

    /**
     * Year
     */
    private int lastChildBirthYear;

    /**
     *  0 -> Dead
     *  1 -> Alive
     */
    private int lastChildBirthStatus;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

    public String getLevelOfEducation() {
        return levelOfEducation;
    }

    public void setLevelOfEducation(String levelOfEducation) {
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

    public boolean isPmctcStatus() {
        return pmctcStatus;
    }

    public void setPmctcStatus(boolean pmctcStatus) {
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

    public int getPncStatus() {
        return pncStatus;
    }

    public void setPncStatus(int pncStatus) {
        this.pncStatus = pncStatus;
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
}
