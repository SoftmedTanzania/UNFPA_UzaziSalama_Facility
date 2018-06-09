package apps.uzazisalama.com.anc.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import apps.uzazisalama.com.anc.base.AppDatabase;
import apps.uzazisalama.com.anc.database.Referral;

/**
 * Created by issy on 20/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project UNFPA_UzaziSalama_Facility
 */

public class ReferralViewModel extends AndroidViewModel {

    AppDatabase database;
    LiveData<List<Referral>> chwReferrals;
    LiveData<List<Referral>> healthFacilityReferrals;

    public ReferralViewModel(Application application){
        super(application);
        database = AppDatabase.getDatabase(application.getApplicationContext());
        chwReferrals = database.referralModelDao().getAllChwReferrals();
        healthFacilityReferrals = database.referralModelDao().getAllHealthFacilityReferrals();
    }

    public LiveData<List<Referral>> getChwReferrals() {
        return chwReferrals;
    }

    public LiveData<List<Referral>> getHealthFacilityReferrals() {
        return healthFacilityReferrals;
    }
}
