package apps.uzazisalama.com.anc.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

import apps.uzazisalama.com.anc.MainActivity;
import apps.uzazisalama.com.anc.R;
import apps.uzazisalama.com.anc.api.Endpoints;
import apps.uzazisalama.com.anc.base.BaseActivity;
import apps.uzazisalama.com.anc.database.AncClient;
import apps.uzazisalama.com.anc.database.ClientAppointment;
import apps.uzazisalama.com.anc.database.PncClient;
import apps.uzazisalama.com.anc.database.Referral;
import apps.uzazisalama.com.anc.database.RoutineVisits;
import apps.uzazisalama.com.anc.objects.LoginResponse;
import apps.uzazisalama.com.anc.objects.ReferralResponse;
import apps.uzazisalama.com.anc.utils.Config;
import apps.uzazisalama.com.anc.utils.ServiceGenerator;
import apps.uzazisalama.com.anc.utils.SessionManager;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by issy on 20/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project UNFPA_UzaziSalama_Facility
 */

public class LoginActivity extends BaseActivity {

    RelativeLayout loginButton;
    EditText usernameEt, passwordEt;
    TextView messages, loginButtonText;
    ImageView unfpaLogo, ttcihLogo, background;
    CircularProgressView progressView;

    Endpoints.ReferralService referalService;
    Endpoints.ClientService clientService;

    // Session Manager Class
    private SessionManager session;
    int flag = 0;
    boolean justInitializing = false;
    String usernameValue, passwordValue;
    private String deviceRegistrationId = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupviews();

        session = new SessionManager(getApplicationContext());

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButtonText.setVisibility(View.GONE);
                progressView.setVisibility(View.VISIBLE);
                if (getAuthenticationCredentials()){
                    loginUser();
                }else {
                    loginButtonText.setVisibility(View.VISIBLE);
                    progressView.setVisibility(View.GONE);
                }
            }
        });

    }

    boolean getAuthenticationCredentials(){

        if (!isDeviceRegistered()){
            messages.setText("Device is Not Registered for Notifications, please Register");
            return false;
        }

        if (usernameEt.getText().length() <= 0){
            Toast.makeText(this, getResources().getString(R.string.username_required), Toast.LENGTH_SHORT).show();
            return false;
        }else if (passwordEt.getText().length() <= 0){
            Toast.makeText(this, getResources().getString(R.string.password_required), Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            usernameValue = usernameEt.getText().toString();
            passwordValue = passwordEt.getText().toString();

            return true;

        }
    }

    void loginUser(){
        if (isNetworkAvailable()){
            Endpoints.LoginService loginService =
                    ServiceGenerator.createService(Endpoints.LoginService.class, usernameValue, passwordValue, null);
            Call<LoginResponse> call = loginService.basicLogin();
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                    if (response == null){
                        Log.d("tag", "response is null");
                    }

                    loginButtonText.setVisibility(View.VISIBLE);
                    progressView.setVisibility(View.GONE);
                    if (response.isSuccessful()){
                        LoginResponse loginResponse = response.body();

                        String userName = loginResponse.getUser().getUsername();
                        String userUUID = loginResponse.getUser().getAttributes().getPersonUUID();
                        String facilityUUID = loginResponse.getTeam().getTeam().getLocation().getUuid();

                        Log.d("facilityID", facilityUUID);

                        session.createLoginSession(
                                userName,
                                userUUID,
                                passwordValue,
                                facilityUUID);

                        referalService = ServiceGenerator.createService(Endpoints.ReferralService.class, session.getUserName(), session.getUserPass(), session.getKeyHfid());
                        clientService = ServiceGenerator.createService(Endpoints.ClientService.class, session.getUserName(), session.getUserPass(), session.getKeyHfid());

                        sendRegistrationToServer(deviceRegistrationId,
                                loginResponse.getUser().getAttributes().getPersonUUID(),
                                loginResponse.getTeam().getTeam().getLocation().getUuid());

                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    t.printStackTrace();
                    loginButtonText.setVisibility(View.VISIBLE);
                    progressView.setVisibility(View.GONE);
                }
            });
        }
    }

    void callReferralList(String facilityUUID){
        if (session.isLoggedIn()){
            //Call referral list from the server

            Call<List<ReferralResponse>> call = referalService.getHealthFacilityReferrals(facilityUUID);
            call.enqueue(new Callback<List<ReferralResponse>>() {
                @SuppressLint("StaticFieldLeak")
                @Override
                public void onResponse(Call<List<ReferralResponse>> call, Response<List<ReferralResponse>> response) {
                    Log.d("ReferralCheck", "Response Body => "+response.body());
                    if (response!=null){

                        List<ReferralResponse> responses = response.body();

                        new AsyncTask<List<ReferralResponse>, Void, Void>(){
                            @Override
                            protected Void doInBackground(List<ReferralResponse>[] lists) {

                                List<ReferralResponse> list = lists[0];
                                if (list != null){
                                    for (ReferralResponse referralResponse : list){
                                        AncClient ancClient = referralResponse.getAncClient();
                                        List<Referral> referrals = referralResponse.getClientReferrals();
                                        List<ClientAppointment> appointments = referralResponse.getClientAppointments();
                                        List<RoutineVisits> routines = referralResponse.getRoutineVisits();
                                        database.clientModel().addNewClient(ancClient);
                                        Log.d("ReferralCheck", "Added anc client");
                                        for (Referral referral : referrals){
                                            database.referralModelDao().addNewReferral(referral);
                                        }

                                        for (ClientAppointment appointment : appointments){
                                            database.clientAppointmentDao().addNewAppointment(appointment);
                                        }

                                        for (RoutineVisits r : routines){
                                            database.routineModelDao().addRoutine(r);
                                        }

                                    }
                                }

                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                                callPncClients(session.getKeyHfid());
                            }
                        }.execute(responses);
                    }
                }

                @Override
                public void onFailure(Call<List<ReferralResponse>> call, Throwable t) {

                }
            });


        }
    }

    void callPncClients(String facilityUUID){
        if (session.isLoggedIn()){
            Call<List<PncClient>> call = clientService.getPncClients(facilityUUID);
            call.enqueue(new Callback<List<PncClient>>() {
                @SuppressLint("StaticFieldLeak")
                @Override
                public void onResponse(Call<List<PncClient>> call, Response<List<PncClient>> response) {
                    if (response!=null){
                        List<PncClient> pncClients = response.body();
                        new AsyncTask<List<PncClient>, Void, Void>(){
                            @Override
                            protected Void doInBackground(List<PncClient>... lists) {
                                List<PncClient> clientList = lists[0];
                                if (clientList != null){
                                    for (PncClient client : clientList){
                                        database.pncClientModelDao().addNewClient(client);
                                    }
                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                LoginActivity.this.finish();

                            }
                        }.execute(pncClients);
                    }
                }

                @Override
                public void onFailure(Call<List<PncClient>> call, Throwable t) {

                }
            });
        }
    }

    void setupviews(){

        progressView = findViewById(R.id.progress_view);

        loginButtonText = findViewById(R.id.login_button_text);
        loginButton = findViewById(R.id.login_button);
        usernameEt = findViewById(R.id.username_et);
        passwordEt = findViewById(R.id.password_et);
        messages = findViewById(R.id.messages);

        background = findViewById(R.id.background);
        Glide.with(this).load(R.drawable.bg).into(background);

        unfpaLogo = findViewById(R.id.unfpa_logo);
        Glide.with(this).load(R.drawable.unfpa_image).into(unfpaLogo);
        ttcihLogo = findViewById(R.id.ttcih_logo);
        Glide.with(this).load(R.drawable.ttcih_image).into(ttcihLogo);
    }

    private boolean isDeviceRegistered(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        deviceRegistrationId = pref.getString("regId", null);
        if (deviceRegistrationId == null || deviceRegistrationId.isEmpty()){
            return false;
        }else {
            return true;
        }
    }

    private void sendRegistrationToServer(String token, String userUiid, String hfid){

        SessionManager sess = new SessionManager(getApplicationContext());

        String datastream = "";
        JSONObject object   = new JSONObject();
        RequestBody body;

        try {
            object.put("userUiid", userUiid);
            object.put("googlePushNotificationToken", token);
            object.put("facilityUiid", hfid);
            object.put("userType", 1);

            datastream = object.toString();
            Log.d("FCMService", "data "+datastream);

            body = RequestBody.create(MediaType.parse("application/json"), datastream);

        }catch (Exception e){
            e.printStackTrace();
            body = RequestBody.create(MediaType.parse("application/json"), datastream);
        }

        Endpoints.NotificationServices notificationServices = ServiceGenerator.createService(Endpoints.NotificationServices.class, session.getUserName(), session.getUserPass(), null);
        retrofit2.Call call = notificationServices.registerDevice(body);
        call.enqueue(new retrofit2.Callback() {
            @Override
            public void onResponse(retrofit2.Call call, Response response) {
                //new AddUserData(baseDatabase).execute(userData);
                messages.setTextColor(getResources().getColor(R.color.green_400));
                messages.setText("Device registered Successfully");

                //Registered Successfully, call referrals list
                callReferralList(sess.getKeyHfid());
            }

            @Override
            public void onFailure(retrofit2.Call call, Throwable t) {
                //new AddUserData(baseDatabase).execute(userData);
                messages.setTextColor(getResources().getColor(R.color.orange_400));
                messages.setText("Device might have not been Registered");
                messages.setTextColor(getResources().getColor(R.color.red_600));
            }
        });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
