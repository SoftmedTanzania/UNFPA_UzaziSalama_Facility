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

import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;

import apps.uzazisalama.com.anc.database.AncClient;
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
        String datastream = "";
        JSONObject object   = new JSONObject();

        try {
            //object.put("clientId", client.getID());

            object.put("healthFacilityClientId", visits.getHealthFacilityClientId());
            object.put("appointmentId", visits.getAppointmentID());
            object.put("visitNumber", visits.getVisitNumber());
            object.put("visitDate", visits.getVisitDate());
            object.put("appointmentDate", visits.getAppointmentDate());
            object.put("anaemia", visits.isAnaemia());
            object.put("oedema", visits.isOedema());
            object.put("protenuria", visits.isProtenuria());
            object.put("highBloodPressure", visits.isHighBloodPressure());
            object.put("weightStagnation", visits.isWeightStagnation());
            object.put("antepartumHaemorrhage", visits.isAntepartumHaemorrhage());
            object.put("sugarInTheUrine", visits.isSugarInTheUrine());
            object.put("fetusLie", visits.isFetusLie());

            datastream = object.toString();

            Log.d("PostOfficeService", datastream);

            body = RequestBody.create(MediaType.parse("application/json"), datastream);

        }catch (Exception e){
            e.printStackTrace();
            body = RequestBody.create(MediaType.parse("application/json"), datastream);
        }

        return body;

    }

    public RequestBody getReferralFeedbackBody(Referral referral){
        RequestBody body;
        String datastream = "";
        JSONObject object   = new JSONObject();
        try{

            object.put("id", referral.getReferralID());
            object.put("ancClientId", referral.getHealthFacilityClientID());
            object.put("referralReason", referral.getReferralReason());
            object.put("instanceId", referral.getInstanceID());
            object.put("referralUUID", referral.getReferralUUID());
            object.put("serviceProviderUUID", referral.getServiceProviderUUID());
            object.put("otherClinicalInformation", referral.getOtherClinicalInformation());
            object.put("referralFeedback", referral.getReferralFeedback());
            object.put("referralType", referral.getReferralType());
            object.put("fromFacilityId", referral.getFromFacailityID());
            object.put("referralDate", referral.getReferralDate());
            object.put("facilityId", referral.getFacilityID());
            object.put("referralStatus", referral.getReferralStatus());

            datastream = object.toString();

            Log.d("PostOfficeService", datastream);

            body = RequestBody.create(MediaType.parse("application/json"), datastream);

        }catch (Exception e){
            e.printStackTrace();
            body = RequestBody.create(MediaType.parse("application/json"), datastream);
        }

        return body;
    }

    public RequestBody getAncClientBody(AncClient client){
        RequestBody body;
        String datastream = "";
        JSONObject object   = new JSONObject();

        try {
            //object.put("clientId", client.getID());
            object.put("firstName", client.getFirstName());
            object.put("middleName", client.getMiddleName());
            object.put("surname", client.getSurname());
            object.put("phoneNumber", client.getPhoneNumber());
            object.put("ward", client.getWard());
            object.put("village", client.getVillage());
            object.put("mapCue", client.getMapCue());
            object.put("dateOfBirth", client.getDateOfBirth());
            object.put("heightBelowAverage", client.isHeightBelowAverage());
            object.put("pmtctStatus", client.getPmctcStatus());
            object.put("levelOfEducation", client.getLevelOfEducation());
            object.put("spouseName", client.getSpouseName());
            object.put("gravida", client.getGravida());
            object.put("para", client.getPara());
            object.put("lmnpDate", client.getLnmp());
            object.put("edd", client.getEdd());
            object.put("clientType", client.getClientType());
            object.put("gestationalAgeBelow20", client.isGestationalAgeBelow20());
            object.put("historyOfAbortion", client.isHistoryOfAbortion());
            object.put("ageBelow20Years", client.isAgeBelow20Years());
            object.put("lastPregnancyOver10Years", client.isLastPregnancyOver10yearsAgo());
            object.put("pregnancyAbove35Years", client.isPregnancyAbove35Years());
            object.put("historyOfStillBirth", client.isHistoryOfStillBirths());
            object.put("historyOfPostmartumHaemorrhage", client.isHistoryOfPostmartumHaemorrhage());
            object.put("historyOfRetainedPlacenta", client.isHistoryOfRetainedPlacenta());
            object.put("clientType", client.getClientType());
            object.put("lastChildbirthYear", client.getLastChildBirthYear());
            object.put("lastChildbirthStatus", client.getLastChildBirthStatus());
            object.put("healthFacilityCode", client.getHealthFacilityCode());


            datastream = object.toString();

            Log.d("PostOfficeService", datastream);

            body = RequestBody.create(MediaType.parse("application/json"), datastream);

        }catch (Exception e){
            e.printStackTrace();
            body = RequestBody.create(MediaType.parse("application/json"), datastream);
        }

        return body;
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        //do something based on connectivity
        networkStatus = isConnected;
        Log.d("OMUNYA", "Network changed : "+networkStatus);
    }

}
