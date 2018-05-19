package apps.uzazisalama.com.anc.viewmodels;

import android.app.Application;
import android.app.ListActivity;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import apps.uzazisalama.com.anc.base.AppDatabase;
import apps.uzazisalama.com.anc.database.PncClient;

/**
 * Created by issy on 19/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project UNFPA_UzaziSalama_Facility
 */

public class PncClientsViewModel extends AndroidViewModel{

    AppDatabase database;
    LiveData<List<PncClient>> allPncClients;

    public PncClientsViewModel(Application application){
        super(application);
        database = AppDatabase.getDatabase(application.getApplicationContext());
        allPncClients = database.pncClientModelDao().getAllClients();
    }

    public LiveData<List<PncClient>> getAllPncClients() {
        return allPncClients;
    }
}
