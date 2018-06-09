package apps.uzazisalama.com.anc.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by issy on 22/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project UNFPA_UzaziSalama_Facility
 */

@Entity
public class ClientAppointment implements Serializable {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    @SerializedName("appointment_id")
    private long appointmentID;

    @SerializedName("healthFacilityClientId")
    private long healthFacilityClientId;

    @SerializedName("appointmentDate")
    private long appointmentDate;

    @SerializedName("isCancelled")
    private boolean isCancelled;

    @SerializedName("status")
    private String status;

    @SerializedName("appointmentType")
    private int appointmentType;

    @SerializedName("visitNumber")
    private int visitNumber;

    public long getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(long appointmentID) {
        this.appointmentID = appointmentID;
    }

    public long getHealthFacilityClientId() {
        return healthFacilityClientId;
    }

    public void setHealthFacilityClientId(long healthFacilityClientId) {
        this.healthFacilityClientId = healthFacilityClientId;
    }

    public long getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(long appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(int appointmentType) {
        this.appointmentType = appointmentType;
    }

    public int getVisitNumber() {
        return visitNumber;
    }

    public void setVisitNumber(int visitNumber) {
        this.visitNumber = visitNumber;
    }
}
