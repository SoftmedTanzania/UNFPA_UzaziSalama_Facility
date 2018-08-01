package apps.uzazisalama.com.anc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;
import com.irozon.alertview.AlertActionStyle;
import com.irozon.alertview.AlertStyle;
import com.irozon.alertview.AlertView;
import com.irozon.alertview.objects.AlertAction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import apps.uzazisalama.com.anc.activities.AncClientsListActivity;
import apps.uzazisalama.com.anc.activities.AncRoutineActivity;
import apps.uzazisalama.com.anc.activities.ClientRegisterActivity;
import apps.uzazisalama.com.anc.api.Endpoints;
import apps.uzazisalama.com.anc.base.BaseActivity;
import apps.uzazisalama.com.anc.customviews.NonSwipeableViewPager;
import apps.uzazisalama.com.anc.database.AncClient;
import apps.uzazisalama.com.anc.database.ClientAppointment;
import apps.uzazisalama.com.anc.database.PncClient;
import apps.uzazisalama.com.anc.database.PostBox;
import apps.uzazisalama.com.anc.database.RoutineVisits;
import apps.uzazisalama.com.anc.fragments.AncFragment;
import apps.uzazisalama.com.anc.fragments.PncFragment;
import apps.uzazisalama.com.anc.fragments.ReportsFragment;
import apps.uzazisalama.com.anc.objects.RegistrationResponse;
import apps.uzazisalama.com.anc.objects.RoutineResponse;
import apps.uzazisalama.com.anc.services.AncToPncDispatcherService;
import apps.uzazisalama.com.anc.utils.ServiceGenerator;
import apps.uzazisalama.com.anc.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static apps.uzazisalama.com.anc.utils.Constants.POST_BOX_DATA_ANC_CLIENT;
import static apps.uzazisalama.com.anc.utils.Constants.POST_BOX_DATA_APPOINTMENT;
import static apps.uzazisalama.com.anc.utils.Constants.POST_BOX_DATA_PNC_CLIENT;
import static apps.uzazisalama.com.anc.utils.Constants.POST_BOX_DATA_ROUTINE_VISIT;

public class MainActivity extends BaseActivity {

    private TabLayout tabLayout;
    public static ViewPager viewPager;
    private Toolbar toolbar;
    private TextView toolbarTitle, unsynced, facilityName;
    private CircularProgressView syncProgressBar;
    private ImageView manualSync;

    private FirebaseJobDispatcher dispatcher;
    private Endpoints.ClientService clientService;
    private Endpoints.RoutineServices routineService;
    private Endpoints.ReferralService referralService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupviews();

        //User Session Manager Initialization
        SessionManager sessionManager = new SessionManager(this);
        if (!sessionManager.isLoggedIn()){
            sessionManager.checkLogin();
            finish();
        }
        if (session.isLoggedIn()){
            TextView userName = findViewById(R.id.toolbar_user_name);
            userName.setText(session.getUserName());
            facilityName.setText(session.getKeyHfid());
        }
        dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        clientService = ServiceGenerator.createService(Endpoints.ClientService.class,
                session.getUserName(),
                session.getUserPass(),
                session.getKeyHfid());
        routineService = ServiceGenerator.createService(Endpoints.RoutineServices.class,
                session.getUserName(),
                session.getUserPass(),
                session.getKeyHfid());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }

        //Postbox contain all data that could not save to the server due to network issues
        checkPostBox();

        //Schedule a background tast to run periodically looking for unsaved data to save once network connectivity is up
        scheduleBackgroundJob();

        //initialize viewpager
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(4);

        //initialize tablayout
        tabLayout = findViewById(R.id.tabs);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
                setupTabIcons();
            }
        });

        manualSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncPostBoxData();
            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    private void checkPostBox(){

        new AsyncTask<Void,Void, Void>(){
            int postBoxSize = 0;
            @Override
            protected Void doInBackground(Void... voids) {

                List<PostBox> postBoxList =  database.postBoxModelDao().getAllPostBoxEntries();
                postBoxSize = postBoxList.size();

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if (postBoxSize > 0){
                    unsynced.setTextColor(getResources().getColor(R.color.red_500));
                    manualSync.setColorFilter(getResources().getColor(R.color.red_500));
                }

            }
        }.execute();

    }

    private void scheduleBackgroundJob(){
        Job myJob = dispatcher.newJobBuilder()
                .setService(AncToPncDispatcherService.class) // the JobService that will be called
                .setTag("10002")        // uniquely identifies the job
                .setRecurring(true)
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                .setTrigger(Trigger.executionWindow(0, 60))
                .setReplaceCurrent(false)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .build();

        dispatcher.mustSchedule(myJob);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout){
            session.logoutUser();
            finish();
        }

        return true;
    }

    void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AncFragment(), "ANC");
        adapter.addFragment(new PncFragment(), "PNC");
        adapter.addFragment(new ReportsFragment(), "Reports");
        viewPager.setAdapter(adapter);
    }

    void setupTabIcons(){

        View ancTabView = getLayoutInflater().inflate(R.layout.custom_tabs, null);
        TextView ancTabTitle = ancTabView.findViewById(R.id.title_text);
        ImageView ancIcon    = ancTabView.findViewById(R.id.icon);
        ancIcon.setColorFilter(getResources().getColor(R.color.card_light_text));
        if (!MainActivity.this.isFinishing())
            Glide.with(this).load(R.mipmap.ic_anc).into(ancIcon);
        ancTabTitle.setText("ANC");
        tabLayout.getTabAt(0).setCustomView(ancTabView);

        View pncTabView = getLayoutInflater().inflate(R.layout.custom_tabs, null);
        TextView pncTabTitle = pncTabView.findViewById(R.id.title_text);
        ImageView pncIcon    = pncTabView.findViewById(R.id.icon);
        pncIcon.setColorFilter(getResources().getColor(R.color.card_light_text));
        if (!MainActivity.this.isFinishing())
            Glide.with(this).load(R.mipmap.ic_pnc).into(pncIcon);
        pncTabTitle.setText("PNC");
        tabLayout.getTabAt(1).setCustomView(pncTabView);


        View reportsTabView = getLayoutInflater().inflate(R.layout.custom_tabs, null);
        TextView reportsTabTitle = reportsTabView.findViewById(R.id.title_text);
        ImageView reportsIcon    = reportsTabView.findViewById(R.id.icon);
        if (!MainActivity.this.isFinishing())
            Glide.with(this).load(R.mipmap.ic_reports).into(reportsIcon);
        reportsIcon.setColorFilter(getResources().getColor(R.color.card_light_text));
        reportsTabTitle.setText("REPORTS");
        tabLayout.getTabAt(2).setCustomView(reportsTabView);

    }

    void setupviews(){

        syncProgressBar = findViewById(R.id.manual_sync_loader);
        syncProgressBar.setVisibility(View.INVISIBLE);

        manualSync = findViewById(R.id.manual_sync);

        facilityName = findViewById(R.id.facility_name);
        toolbarTitle = findViewById(R.id.toolbar_user_name);
        unsynced = findViewById(R.id.unsynced);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position);
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void syncPostBoxData(){
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                syncProgressBar.setVisibility(View.VISIBLE);
                manualSync.setVisibility(View.INVISIBLE);
                unsynced.setText("Syncing...");
            }

            @Override
            protected Void doInBackground(Void... voids) {
                List<PostBox> postBoxList = database.postBoxModelDao().getAllPostBoxEntries();
                /**
                 *  show loader
                 *  loop through all the data and post to server
                 *  Hide loader
                 */
                if (postBoxList.size() > 0){
                    for (PostBox item : postBoxList){
                        int dataType = item.getPostDataType();
                        switch (dataType){
                            case POST_BOX_DATA_ANC_CLIENT:
                                //Anc Client
                                if (isNetworkAvailable()){
                                    /**
                                     *  Post data to server and delete it if successfully stored to server
                                     */
                                    AncClient client = database.clientModel().getClientById(Long.getLong(item.getPostBoxId()));
                                    postAncClient(client, item);
                                }
                                break;
                            case POST_BOX_DATA_PNC_CLIENT:
                                //Pnc Client
                                if (isNetworkAvailable()){
                                    /**
                                     *  Post data to server and delete it if successfully stored to server
                                     */
                                    PncClient client = database.pncClientModelDao().getClientByID(item.getPostBoxId());
                                    postPncClient(client, item);
                                }
                                break;
                            case POST_BOX_DATA_ROUTINE_VISIT:
                                //Routine Visit
                                if (isNetworkAvailable()){
                                    /**
                                     *  Post data to server and delete it if successfully stored to server
                                     */
                                    RoutineVisits routineVisits = database.routineModelDao().getRoutineById(Long.getLong(item.getPostBoxId()));
                                    postRoutineData(routineVisits, item);
                                }
                                break;
                            case POST_BOX_DATA_APPOINTMENT:
                                //Client Appointments
                                break;
                            default:
                                //Nothing
                        }
                    }

                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                syncProgressBar.setVisibility(View.INVISIBLE);
                manualSync.setVisibility(View.VISIBLE);
                unsynced.setText("Sync Data");

                unsynced.setTextColor(getResources().getColor(R.color.white));
                manualSync.setColorFilter(getResources().getColor(R.color.white));

            }
        }.execute();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void postAncClient (AncClient client, PostBox postBoxData){
        //Send to Server
        Call<RegistrationResponse> call = clientService.postAncClient(getAncClientBody(client));
        call.enqueue(new Callback<RegistrationResponse>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {

                if (response != null){
                    Log.d("savingClient", response.body().toString());
                }

                RegistrationResponse registrationResponse = response.body();
                AncClient registeredClient = registrationResponse.getClient();
                //Hack-Alert TODO: Remove once the registered date is implemented on the server side
                registeredClient.setCreatedAt(client.getCreatedAt());

                List<ClientAppointment> appointmentList = registrationResponse.getClientAppointments();

                Gson gson = new Gson();
                String responseString = gson.toJson(registeredClient);
                Log.d("response", "Response "+responseString);

                if (registeredClient != null){
                    new AsyncTask<AncClient, Void, Void>(){
                        @Override
                        protected Void doInBackground(AncClient... ancClients) {
                            AncClient newAncClient = ancClients[0];
                            database.clientModel().addNewClient(newAncClient);
                            for (ClientAppointment appointment : appointmentList){
                                database.clientAppointmentDao().addNewAppointment(appointment);
                                Log.d("appointment", "Appointment ID : "+appointment.getAppointmentID());
                            }

                            List<ClientAppointment> appointmentList1 = database.clientAppointmentDao().getAppointmentsByClientId(newAncClient.getHealthFacilityClientId());
                            Log.d("appointment", "Appointment Size : "+appointmentList1.size());

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                        }
                    }.execute(registeredClient);
                }

            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {

            }
        });
    }

    private void postPncClient(PncClient client, PostBox postBox){

    }

    private void postRoutineData(RoutineVisits routineVisits, PostBox postBox){

        Call<RoutineResponse> call = routineService.saveRoutineVisit(getRoutineBody(routineVisits));
        call.enqueue(new Callback<RoutineResponse>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<RoutineResponse> call, Response<RoutineResponse> response) {

                try{
                    Log.d("routineVisits", response.body().toString());

                    RoutineResponse response1 = response.body();
                    RoutineVisits visit = response1.getRoutineVisits();
                    if (visit.getVisitDate() == 0){
                        visit.setVisitDate(Calendar.getInstance().getTimeInMillis());
                    }

                    List<ClientAppointment> appointments = response1.getAppointments();

                    if (visit != null) {
                        //Delete the old routine entry from the database
                        database.routineModelDao().deleteRoutine(routineVisits);
                    }

                    new AsyncTask<RoutineVisits, Void, Void>(){

                        String nextAppointmentDate = "";

                        @Override
                        protected Void doInBackground(RoutineVisits... routineVisits) {
                            database.routineModelDao().addRoutine(routineVisits[0]);

                            //Next Client Appointment Date
                            int currentVisit = routineVisits[0].getVisitNumber();
                            int nextVisit = currentVisit + 1;
                            for (ClientAppointment a : appointments){
                                if (a.getVisitNumber() == nextVisit){
                                    nextAppointmentDate = simpleDateFormat.format(a.getAppointmentDate());
                                }
                                database.clientAppointmentDao().addNewAppointment(a);
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                        }
                    }.execute(visit);
                }catch (NullPointerException e){
                    e.printStackTrace();

                }

            }

            @Override
            public void onFailure(Call<RoutineResponse> call, Throwable t) {
                Log.d("TheyCantHoldUs", t.getMessage());
            }
        });
    }

}
