package apps.uzazisalama.com.anc.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by issy on 16/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project ANC
 */

@Entity(tableName = "RoutineVisits")
public class RoutineVisits implements Serializable{

    @PrimaryKey(autoGenerate = false)
    @NonNull
    @SerializedName("id")
    private long ID;

    @SerializedName("healthFacilityClientId")
    private long healthFacilityClientId;

    @SerializedName("appointmentId")
    private long appointmentID;

    @SerializedName("visitNumber")
    private int visitNumber;

    @SerializedName("visitDate")
    private long visitDate;

    @SerializedName("appointmentDate")
    private long appointmentDate;

    @SerializedName("anaemia")
    private boolean anaemia;

    @SerializedName("oedema")
    private boolean oedema;

    @SerializedName("protenuria")
    private boolean protenuria;

    @SerializedName("highBloodPressure")
    private boolean highBloodPressure;

    @SerializedName("weightStagnation")
    private boolean weightStagnation;

    @SerializedName("antepartumHaemorrhage")
    private boolean antepartumHaemorrhage;

    @SerializedName("sugarInTheUrine")
    private boolean sugarInTheUrine;

    @SerializedName("fetusLie")
    private boolean fetusLie;

    @NonNull
    public long getID() {
        return ID;
    }

    public void setID(@NonNull long ID) {
        this.ID = ID;
    }

    public void setHealthFacilityClientId(long healthFacilityClientId) {
        this.healthFacilityClientId = healthFacilityClientId;
    }

    public long getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(long appointmentID) {
        this.appointmentID = appointmentID;
    }

    public int getVisitNumber() {
        return visitNumber;
    }

    public void setVisitNumber(int visitNumber) {
        this.visitNumber = visitNumber;
    }

    public long getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(long visitDate) {
        this.visitDate = visitDate;
    }

    public long getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(long appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public boolean isAnaemia() {
        return anaemia;
    }

    public void setAnaemia(boolean anaemia) {
        this.anaemia = anaemia;
    }

    public boolean isOedema() {
        return oedema;
    }

    public void setOedema(boolean oedema) {
        this.oedema = oedema;
    }

    public boolean isProtenuria() {
        return protenuria;
    }

    public void setProtenuria(boolean protenuria) {
        this.protenuria = protenuria;
    }

    public boolean isHighBloodPressure() {
        return highBloodPressure;
    }

    public void setHighBloodPressure(boolean highBloodPressure) {
        this.highBloodPressure = highBloodPressure;
    }

    public boolean isWeightStagnation() {
        return weightStagnation;
    }

    public void setWeightStagnation(boolean weightStagnation) {
        this.weightStagnation = weightStagnation;
    }

    public boolean isAntepartumHaemorrhage() {
        return antepartumHaemorrhage;
    }

    public void setAntepartumHaemorrhage(boolean antepartumHaemorrhage) {
        this.antepartumHaemorrhage = antepartumHaemorrhage;
    }

    public boolean isSugarInTheUrine() {
        return sugarInTheUrine;
    }

    public void setSugarInTheUrine(boolean sugarInTheUrine) {
        this.sugarInTheUrine = sugarInTheUrine;
    }

    public boolean isFetusLie() {
        return fetusLie;
    }

    public void setFetusLie(boolean fetusLie) {
        this.fetusLie = fetusLie;
    }

    public long getHealthFacilityClientId() {
        return healthFacilityClientId;
    }
}
