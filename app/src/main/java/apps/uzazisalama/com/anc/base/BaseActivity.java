package apps.uzazisalama.com.anc.base;

import android.arch.persistence.room.Database;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;

import apps.uzazisalama.com.anc.database.AncClient;
import apps.uzazisalama.com.anc.database.PncClient;
import apps.uzazisalama.com.anc.database.Referral;
import apps.uzazisalama.com.anc.database.RoutineVisits;
import apps.uzazisalama.com.anc.utils.SessionManager;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by issy on 10/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project ANC
 */

public abstract class BaseActivity extends AppCompatActivity implements InternetConnectivityListener {

    //This is the base activity to be subclassed by all activities created on the application

    public static AppDatabase database;

    public static SessionManager session;

    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd yyy");

    public static boolean networkStatus = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = AppDatabase.getDatabase(this);
        // Session class instance
        session = new SessionManager(getApplicationContext());

        InternetAvailabilityChecker.init(this);

    }

    public RequestBody getRoutineBody(RoutineVisits visits){
        RequestBody body;
        Gson gson  = new Gson();
        String datastream = gson.toJson(visits);
        body = RequestBody.create(MediaType.parse("application/json"), datastream);
        return body;
    }

    public RequestBody getReferralFeedbackBody(Referral referral){
        RequestBody body;
        Gson gson = new Gson();
        String datastream = gson.toJson(referral);
        body = RequestBody.create(MediaType.parse("application/json"), datastream);
        return body;
    }

    public RequestBody getAncClientBody(AncClient client){
        RequestBody body;
        Gson gson = new Gson();
        String datastream = gson.toJson(client);
        body = RequestBody.create(MediaType.parse("application/json"), datastream);
        return body;
    }

    public RequestBody getPncClientBody(PncClient client){
        RequestBody body;
        String datastream = "";
        Gson gson = new Gson();
        datastream = gson.toJson(client);
        body = RequestBody.create(MediaType.parse("application/json"), datastream);
        return body;
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        //do something based on connectivity
        networkStatus = isConnected;
        Log.d("OMUNYA", "Network changed : "+networkStatus);
    }

}
