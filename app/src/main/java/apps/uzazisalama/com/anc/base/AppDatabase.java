package apps.uzazisalama.com.anc.base;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import apps.uzazisalama.com.anc.database.AncClient;
import apps.uzazisalama.com.anc.database.RoutineVisits;
import apps.uzazisalama.com.anc.database.dao.AncClientModelDao;
import apps.uzazisalama.com.anc.database.dao.RoutineVisitsModelDao;

/**
 * Created by issy on 12/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project ANC
 */

@Database(
        entities = {
                AncClient.class,
                RoutineVisits.class
        },
        version = 1
)
public abstract class AppDatabase extends RoomDatabase{

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "anc_db")
                            .build();
        }
        return INSTANCE;
    }

    public abstract AncClientModelDao clientModel();

    public abstract RoutineVisitsModelDao routineModelDao();

}
