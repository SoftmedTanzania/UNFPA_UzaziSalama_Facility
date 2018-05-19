package apps.uzazisalama.com.anc.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import apps.uzazisalama.com.anc.base.AppDatabase;
import apps.uzazisalama.com.anc.database.AncClient;

/**
 * Created by issy on 15/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project ANC
 */

public class ClientsViewModel extends AndroidViewModel {

    LiveData<List<AncClient>> allClientsList;
    AppDatabase database;

    public ClientsViewModel(Application application){
        super(application);
        database = AppDatabase.getDatabase(this.getApplication());
        allClientsList = database.clientModel().getAllAncClients();
    }

    public LiveData<List<AncClient>> getAllClientsList() {
        return allClientsList;
    }

    public void deleteItem(AncClient ancClient) {
        new deleteAsyncTask(database).execute(ancClient);
    }

    private static class deleteAsyncTask extends AsyncTask<AncClient, Void, Void> {

        private AppDatabase db;

        deleteAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final AncClient... params) {
            db.clientModel().deleteAClient(params[0]);
            return null;
        }

    }
}
