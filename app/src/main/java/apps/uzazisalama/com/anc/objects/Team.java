package apps.uzazisalama.com.anc.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by issy on 11/23/17.
 */

public class Team implements Serializable {

    @SerializedName("identifier")
    private String identifier;

    @SerializedName("display")
    private String display;

    @SerializedName("patients")
    private ArrayList<String> patients;

    @SerializedName("uuid")
    private String UUID;

    @SerializedName("team")
    private InnerTeam team;

    public Team() {}

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public ArrayList<String> getPatients() {
        return patients;
    }

    public void setPatients(ArrayList<String> patients) {
        this.patients = patients;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public InnerTeam getTeam() {
        return team;
    }

    public void setTeam(InnerTeam team) {
        this.team = team;
    }

    public class InnerTeam {

        @SerializedName("location")
        private InnerLocation location;

        public InnerTeam(){

        }

        public InnerLocation getLocation() {
            return location;
        }

        public void setLocation(InnerLocation location) {
            this.location = location;
        }
    }

    public class InnerLocation {

        @SerializedName("name")
        private String name;

        @SerializedName("display")
        private String display;

        @SerializedName("uuid")
        private String uuid;

        public InnerLocation(){

        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDisplay() {
            return display;
        }

        public void setDisplay(String display) {
            this.display = display;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }
    }

}
