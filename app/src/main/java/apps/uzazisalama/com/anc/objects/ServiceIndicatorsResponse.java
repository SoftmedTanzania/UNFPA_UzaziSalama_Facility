package apps.uzazisalama.com.anc.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import apps.uzazisalama.com.anc.database.Indicators;

public class ServiceIndicatorsResponse implements Serializable
{

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

    @SerializedName("indicators")
    private List<Indicators> indicators;

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

    public List<Indicators> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<Indicators> indicators) {
        this.indicators = indicators;
    }
}
