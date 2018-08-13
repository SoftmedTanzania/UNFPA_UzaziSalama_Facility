package apps.uzazisalama.com.anc.database;

import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.nio.file.SecureDirectoryStream;

@Entity(tableName = "referral_service_indicator")
public class ReferralServiceIndicators implements Serializable{

    @SerializedName("serviceId")
    private long serviceId;

    @SerializedName("serviceName")
    private String serviceName;

    @SerializedName("serviceNameSw")
    private String serviceNameSw;

    @SerializedName("category")
    private String category;

    @SerializedName("isActive")
    private boolean isActive;

    @SerializedName("active")
    private boolean active;

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceNameSw() {
        return serviceNameSw;
    }

    public void setServiceNameSw(String serviceNameSw) {
        this.serviceNameSw = serviceNameSw;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
