package apps.uzazisalama.com.anc.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import apps.uzazisalama.com.anc.base.AppDatabase;
import apps.uzazisalama.com.anc.database.RoutineVisits;

/**
 * Created by issy on 16/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project ANC
 */

public class RoutineViewModel extends AndroidViewModel {

    AppDatabase database;
    String currentClientId = "";

    LiveData<List<RoutineVisits>> thisClientRoutines;

    public RoutineViewModel(Application application){
        super(application);
        database = AppDatabase.getDatabase(application.getApplicationContext());
        thisClientRoutines = database.routineModelDao().getRoutinesByClientId(currentClientId); //Pass AncClient ID
    }

    public void setCurrentClientId(String _id){
        this.currentClientId = _id;
    }

    public LiveData<List<RoutineVisits>> getThisClientRoutines() {
        return thisClientRoutines;
    }
}
