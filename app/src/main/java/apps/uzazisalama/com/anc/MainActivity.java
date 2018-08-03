package apps.uzazisalama.com.anc;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import apps.uzazisalama.com.anc.api.Endpoints;
import apps.uzazisalama.com.anc.base.BaseActivity;
import apps.uzazisalama.com.anc.database.AncClient;
import apps.uzazisalama.com.anc.database.ClientAppointment;
import apps.uzazisalama.com.anc.database.PncClient;
import apps.uzazisalama.com.anc.database.PostBox;
import apps.uzazisalama.com.anc.database.RoutineVisits;
import apps.uzazisalama.com.anc.fragments.AncFragment;
import apps.uzazisalama.com.anc.fragments.PncFragment;
import apps.uzazisalama.com.anc.fragments.ReportsFragment;
import apps.uzazisalama.com.anc.objects.PncClientPostResponce;
import apps.uzazisalama.com.anc.objects.RegistrationResponse;
import apps.uzazisalama.com.anc.objects.RoutineResponse;
import apps.uzazisalama.com.anc.services.PostBoxWatcherService;
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

        LiveData<Integer> postBoxSize = database.postBoxModelDao().getBoxSize();
        postBoxSize.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                Log.d("heavy", "Post Box Size = "+integer);
            }
        });

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

    private void checkPostBox(){

        LiveData<Integer> boxSize = database.postBoxModelDao().getBoxSize();
        boxSize.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                int size = integer.intValue();
                if (size > 0){
                    unsynced.setTextColor(getResources().getColor(R.color.red_500));
                    manualSync.setColorFilter(getResources().getColor(R.color.red_500));
                }else {
                    unsynced.setTextColor(getResources().getColor(R.color.white));
                    manualSync.setColorFilter(getResources().getColor(R.color.white));
                }
            }
        });

    }

    private void scheduleBackgroundJob(){
        Job myJob = dispatcher.newJobBuilder()
                .setService(PostBoxWatcherService.class) // the JobService that will be called
                .setTag("10002")        // uniquely identifies the job
                .setRecurring(true)
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                .setTrigger(Trigger.executionWindow(0, 10)) //60 seconds changed to 10 seconds for development purposes
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
                                    AncClient client = database.clientModel().getClientById(Long.parseLong(item.getPostBoxId()));
                                    if (client != null){
                                        postAncClient(client, item);
                                    }
                                }
                                break;
                            case POST_BOX_DATA_PNC_CLIENT:
                                //Pnc Client
                                if (isNetworkAvailable()){
                                    /**
                                     *  Post data to server and delete it if successfully stored to server
                                     */
                                    PncClient client = database.pncClientModelDao().getClientByID(item.getPostBoxId());
                                    if (client != null){
                                        postPncClient(client, item);
                                    }
                                }
                                break;
                            case POST_BOX_DATA_ROUTINE_VISIT:
                                //Routine Visit
                                if (isNetworkAvailable()){
                                    /**
                                     *  Post data to server and delete it if successfully stored to server
                                     */
                                    RoutineVisits routineVisits = database.routineModelDao().getRoutineById(Long.parseLong(item.getPostBoxId()));
                                    if (routineVisits != null){
                                        postRoutineData(routineVisits, item);
                                    }
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
                    RegistrationResponse registrationResponse = response.body();

                    PostAncWrapper wrapper = new PostAncWrapper();
                    wrapper.setResponse(registrationResponse);
                    wrapper.setBoxItem(postBoxData);
                    wrapper.setClient(client);

                    new AsyncTask<PostAncWrapper, Void, Void>(){
                        @Override
                        protected Void doInBackground(PostAncWrapper... postAncWrappers) {

                            //Get response object from the wrapper
                            PostAncWrapper mWrapper = postAncWrappers[0];
                            RegistrationResponse mResponse = mWrapper.getResponse();
                            AncClient oldClientObject = mWrapper.getClient();
                            AncClient registeredClient = mResponse.getClient();
                            //Hack-Alert TODO: Remove once the registered date is implemented on the server side
                            registeredClient.setCreatedAt(oldClientObject.getCreatedAt());
                            List<ClientAppointment> appointmentList = mResponse.getClientAppointments();

                            //Save the newly registered client
                            database.clientModel().addNewClient(registeredClient);

                            //Save client appointments received from the server
                            for (ClientAppointment appointment : appointmentList){
                                database.clientAppointmentDao().addNewAppointment(appointment);
                            }

                            //Delete postBox entry
                            database.postBoxModelDao().deletePostItem(wrapper.boxItem);

                            //Delete the old Client registered with a temporary ID
                            database.clientModel().deleteAClient(oldClientObject);

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            /**
                             * Client already saved
                             */
                        }
                    }.execute(wrapper);
                }else {
                    Log.d("POSTBOXSERVICE", "Response is null ");
                }

            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {

            }
        });
    }

    private void postPncClient(PncClient client, PostBox postBox){
        Call<PncClientPostResponce> call = clientService.postPncClient(getPncClientBody(client));
        call.enqueue(new Callback<PncClientPostResponce>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<PncClientPostResponce> call, Response<PncClientPostResponce> response) {
                if (response != null){

                    PncClientPostResponce pncClientPostResponce = response.body();


                    //Wrap response and postBox data together to pass to the background class
                    PostPncWrapper postPncWrapper = new PostPncWrapper();
                    postPncWrapper.setPostBox(postBox);
                    postPncWrapper.setResponce(pncClientPostResponce);
                    postPncWrapper.setOldPncClient(client);

                    new AsyncTask<PostPncWrapper, Void, Void>(){
                        @Override
                        protected Void doInBackground(PostPncWrapper... wrappers) {
                            PostPncWrapper wrapper = wrappers[0];

                            //Unpack the objects from the wrapper
                            PncClientPostResponce response = wrapper.getResponce();
                            PncClient receivedClient = response.getPncClient();
                            AncClient receivedAncClient = response.getAncClient();
                            List<RoutineVisits> visits = response.getRoutineVisits();
                            PncClient oldPncClient = wrapper.getOldPncClient();
                            PostBox postBoxItem = wrapper.getPostBox();

                            //Save the newly registered client in the database
                            database.pncClientModelDao().addNewClient(receivedClient);

                            //Save the corresponding ANC client to the database (CONTRACT - Replace if exists)
                            database.clientModel().addNewClient(receivedAncClient);

                            //Save received client routines (CONTRACT -> Replace if exists)
                            for (RoutineVisits v : visits){
                                database.routineModelDao().addRoutine(v);
                            }

                            //Delete the postBox entry after it has been saved to the server
                            database.postBoxModelDao().deletePostItem(postBoxItem);

                            //Delete the old PncClient registered with a temporary ID
                            database.pncClientModelDao().deleteAClient(oldPncClient);

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                        }
                    }.execute(postPncWrapper);

                }
            }

            @Override
            public void onFailure(Call<PncClientPostResponce> call, Throwable t) {

            }
        });
    }

    private void postRoutineData(RoutineVisits routineVisits, PostBox postBox){

        Call<RoutineResponse> call = routineService.saveRoutineVisit(getRoutineBody(routineVisits));
        call.enqueue(new Callback<RoutineResponse>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<RoutineResponse> call, Response<RoutineResponse> response) {

                RoutineResponse routineResponse = response.body();
                if (routineResponse != null){
                    //Wrapp the response and postBox object together
                    PostRoutineWrapper wrapper = new PostRoutineWrapper();
                    wrapper.setResponse(routineResponse);
                    wrapper.setPostBox(postBox);
                    wrapper.setOldRoutineVisit(routineVisits);

                    new AsyncTask<PostRoutineWrapper, Void, Void>(){
                        @Override
                        protected Void doInBackground(PostRoutineWrapper... postRoutineWrappers) {
                            PostRoutineWrapper mWrapper = postRoutineWrappers[0];

                            //Unpack the objects from the wrapper
                            RoutineResponse mResponse = mWrapper.getResponse();
                            RoutineVisits oldRoutineVisit = mWrapper.getOldRoutineVisit();
                            PostBox postBoxItem = mWrapper.getPostBox();
                            RoutineVisits visit = mResponse.getRoutineVisits();
                            List<ClientAppointment> appointments = mResponse.getAppointments();

                            //Save the newly received Routine visit to the databse
                            if (visit.getVisitDate() == 0){
                                visit.setVisitDate(Calendar.getInstance().getTimeInMillis());
                            }
                            database.routineModelDao().addRoutine(visit);

                            //Save client appointments received with the client object
                            for (ClientAppointment a : appointments){
                                database.clientAppointmentDao().addNewAppointment(a);
                            }

                            //Delete the old routine registered with a temporary ID
                            database.routineModelDao().deleteRoutine(oldRoutineVisit);

                            //Delete the PostBox entry after the object have been successfully saved to the server
                            database.postBoxModelDao().deletePostItem(postBoxItem);

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                        }
                    }.execute(wrapper);
                }

            }

            @Override
            public void onFailure(Call<RoutineResponse> call, Throwable t) {
                Log.d("TheyCantHoldUs", t.getMessage());
            }
        });
    }

    class PostPncWrapper{

        PncClientPostResponce responce;

        PostBox postBox;

        PncClient oldPncClient;

        public PostPncWrapper(){}

        public PncClientPostResponce getResponce() {
            return responce;
        }

        public void setResponce(PncClientPostResponce responce) {
            this.responce = responce;
        }

        public PostBox getPostBox() {
            return postBox;
        }

        public void setPostBox(PostBox postBox) {
            this.postBox = postBox;
        }

        public PncClient getOldPncClient() {
            return oldPncClient;
        }

        public void setOldPncClient(PncClient oldPncClient) {
            this.oldPncClient = oldPncClient;
        }
    }

    class PostAncWrapper{
        private RegistrationResponse response;
        private PostBox boxItem;
        private AncClient client;
        public PostAncWrapper(){ }
        public RegistrationResponse getResponse() {
            return response;
        }
        public void setResponse(RegistrationResponse response) {
            this.response = response;
        }
        public PostBox getBoxItem() {
            return boxItem;
        }
        public void setBoxItem(PostBox boxItem) {
            this.boxItem = boxItem;
        }
        public AncClient getClient() {
            return client;
        }
        public void setClient(AncClient client) {
            this.client = client;
        }
    }

    class PostRoutineWrapper{
        private RoutineResponse response;
        private PostBox postBox;
        private RoutineVisits oldRoutineVisit;
        public PostRoutineWrapper(){}

        public RoutineResponse getResponse() {
            return response;
        }

        public void setResponse(RoutineResponse response) {
            this.response = response;
        }

        public PostBox getPostBox() {
            return postBox;
        }

        public void setPostBox(PostBox postBox) {
            this.postBox = postBox;
        }

        public RoutineVisits getOldRoutineVisit() {
            return oldRoutineVisit;
        }

        public void setOldRoutineVisit(RoutineVisits oldRoutineVisit) {
            this.oldRoutineVisit = oldRoutineVisit;
        }
    }

}
