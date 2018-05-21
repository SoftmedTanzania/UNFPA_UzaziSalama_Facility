package apps.uzazisalama.com.anc.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by issy on 11/23/17.
 */

public class User implements Serializable {

    @SerializedName("username")
    private String username;

    @SerializedName("status")
    private String status;

    @SerializedName("roles")
    private ArrayList<String> roles;

    @SerializedName("permissions")
    private ArrayList<String> permissions;

    @SerializedName("preferredName")
    private String preferedName;

    @SerializedName("baseEntityId")
    private String baseEntityId;

    @SerializedName("attributes")
    private Attributes attributes;

    @SerializedName("voided")
    private String voided;


    public User(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<String> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<String> roles) {
        this.roles = roles;
    }

    public ArrayList<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<String> permissions) {
        this.permissions = permissions;
    }

    public String getPreferedName() {
        return preferedName;
    }

    public void setPreferedName(String preferedName) {
        this.preferedName = preferedName;
    }

    public String getBaseEntityId() {
        return baseEntityId;
    }

    public void setBaseEntityId(String baseEntityId) {
        this.baseEntityId = baseEntityId;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public String getVoided() {
        return voided;
    }

    public void setVoided(String voided) {
        this.voided = voided;
    }

    public class Attributes implements Serializable {

        @SerializedName("_PERSON_UUID")
        private String personUUID;

        public Attributes(){}

        public String getPersonUUID() {
            return personUUID;
        }

        public void setPersonUUID(String personUUID) {
            this.personUUID = personUUID;
        }
    }

}
