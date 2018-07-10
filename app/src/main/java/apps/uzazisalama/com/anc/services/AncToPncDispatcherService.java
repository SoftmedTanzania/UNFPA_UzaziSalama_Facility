package apps.uzazisalama.com.anc.services;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import apps.uzazisalama.com.anc.base.AppDatabase;
import apps.uzazisalama.com.anc.database.AncClient;

/**
 * Created by issy on 09/07/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project UNFPA_UzaziSalama_Facility
 */

public class AncToPncDispatcherService extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {

        Log.d("PresenceOfAngels", "Starting the ANC->PNC Dispatcher Service "+job.getTag());
        /*
         Scan through the database of Anc Clients,
         Calculate their period if it has exceeded 24 weeks since LNMP and transfer them to PNC
         Simple!
         */

        AppDatabase database = AppDatabase.getDatabase(this);
        LiveData<List<AncClient>> clientsLiveData = database.clientModel().getAllAncClients();
        List<AncClient> clientsList = clientsLiveData.getValue();

        if (clientsList != null){
            for (AncClient client : clientsList){
                long today = new Date().getTime();
                long lnmp = client.getLnmp();

                long pregnancyPeriod = today - lnmp;
                long twentyFourWeeks = 24*7*24*60*60*1000;
                if (pregnancyPeriod > twentyFourWeeks){
                    client.setClientType(2);
                    Log.d("PresenceOfAngels", "Client needs to be changed to PNC "+job.getTag());
                    //database.clientModel().updateClient(client);
                }
            }
        }

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}
