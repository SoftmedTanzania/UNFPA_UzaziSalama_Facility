package apps.uzazisalama.com.anc.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import apps.uzazisalama.com.anc.database.PostBox;
import retrofit2.http.POST;

@Dao
public interface PostBoxModelDao {

    @Query("select * from PostBox")
    List<PostBox> getAllPostBoxEntries();

    @Query("select count(*) from PostBox")
    LiveData<Integer> getBoxSize();

    @Insert
    void AddNewPost(PostBox postBox);

    @Delete
    void deletePostItem(PostBox postBox);
}
