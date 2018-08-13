package apps.uzazisalama.com.anc.database;

import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "indicators")
public class Indicators implements Serializable{

    @SerializedName("referralServiceIndicatorId")
    private long referralServiceIndicatorId;

    @SerializedName("referralIndicatorId")
    private long referralIndicatorId;

    @SerializedName("indicatorName")
    private String indicatorName;

    @SerializedName("indicatorNameSw")
    private String indicatorNameSw;

    @SerializedName("isActive")
    private boolean isActive;

    @SerializedName("active")
    private boolean active;

    public long getReferralServiceIndicatorId() {
        return referralServiceIndicatorId;
    }

    public void setReferralServiceIndicatorId(long referralServiceIndicatorId) {
        this.referralServiceIndicatorId = referralServiceIndicatorId;
    }

    public long getReferralIndicatorId() {
        return referralIndicatorId;
    }

    public void setReferralIndicatorId(long referralIndicatorId) {
        this.referralIndicatorId = referralIndicatorId;
    }

    public String getIndicatorName() {
        return indicatorName;
    }

    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }

    public String getIndicatorNameSw() {
        return indicatorNameSw;
    }

    public void setIndicatorNameSw(String indicatorNameSw) {
        this.indicatorNameSw = indicatorNameSw;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
