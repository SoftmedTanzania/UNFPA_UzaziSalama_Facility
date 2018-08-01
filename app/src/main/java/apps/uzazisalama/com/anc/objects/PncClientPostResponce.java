package apps.uzazisalama.com.anc.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import apps.uzazisalama.com.anc.database.AncClient;
import apps.uzazisalama.com.anc.database.PncClient;
import apps.uzazisalama.com.anc.database.RoutineVisits;

public class PncClientPostResponce implements Serializable {

    @SerializedName("ancClientDTO")
    private AncClient ancClient;

    @SerializedName("pncClientDTO")
    private PncClient pncClient;

    @SerializedName("routineVisitDTOS")
    private List<RoutineVisits> routineVisits;

    public AncClient getAncClient() {
        return ancClient;
    }

    public void setAncClient(AncClient ancClient) {
        this.ancClient = ancClient;
    }

    public PncClient getPncClient() {
        return pncClient;
    }

    public void setPncClient(PncClient pncClient) {
        this.pncClient = pncClient;
    }

    public List<RoutineVisits> getRoutineVisits() {
        return routineVisits;
    }

    public void setRoutineVisits(List<RoutineVisits> routineVisits) {
        this.routineVisits = routineVisits;
    }
}
