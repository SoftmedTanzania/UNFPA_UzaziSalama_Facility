package apps.uzazisalama.com.anc.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import apps.uzazisalama.com.anc.database.PostBox;

@Dao
public interface PostBoxModelDao {

    @Query("select * from PostBox")
    List<PostBox> getAllPostBoxEntries();

    @Insert
    void AddNewPost(PostBox postBox);

}
