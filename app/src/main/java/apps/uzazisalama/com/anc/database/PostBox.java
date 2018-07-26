package apps.uzazisalama.com.anc.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity
public class PostBox implements Serializable{

    @NonNull
    @PrimaryKey(autoGenerate = false)
    private String postBoxId;

    private int postDataType;

    private int syncStatus;

    @NonNull
    public String getPostBoxId() {
        return postBoxId;
    }

    public void setPostBoxId(@NonNull String postBoxId) {
        this.postBoxId = postBoxId;
    }

    public int getPostDataType() {
        return postDataType;
    }

    public void setPostDataType(int postDataType) {
        this.postDataType = postDataType;
    }

    public int getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(int syncStatus) {
        this.syncStatus = syncStatus;
    }
}
