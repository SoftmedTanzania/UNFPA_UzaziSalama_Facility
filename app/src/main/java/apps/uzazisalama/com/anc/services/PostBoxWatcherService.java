package apps.uzazisalama.com.anc.services;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LifecycleService;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import apps.uzazisalama.com.anc.MainActivity;
import apps.uzazisalama.com.anc.api.Endpoints;
import apps.uzazisalama.com.anc.base.AppDatabase;
import apps.uzazisalama.com.anc.base.BaseActivity;
import apps.uzazisalama.com.anc.database.AncClient;
import apps.uzazisalama.com.anc.database.ClientAppointment;
import apps.uzazisalama.com.anc.database.PncClient;
import apps.uzazisalama.com.anc.database.PostBox;
import apps.uzazisalama.com.anc.database.Referral;
import apps.uzazisalama.com.anc.database.RoutineVisits;
import apps.uzazisalama.com.anc.objects.PncClientPostResponce;
import apps.uzazisalama.com.anc.objects.RegistrationResponse;
import apps.uzazisalama.com.anc.utils.ServiceGenerator;
import apps.uzazisalama.com.anc.utils.SessionManager;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.support.annotation.Nullable;

import static apps.uzazisalama.com.anc.base.BaseActivity.session;
import static apps.uzazisalama.com.anc.utils.Constants.POST_BOX_DATA_ANC_CLIENT;
import static apps.uzazisalama.com.anc.utils.Constants.POST_BOX_DATA_APPOINTMENT;
import static apps.uzazisalama.com.anc.utils.Constants.POST_BOX_DATA_PNC_CLIENT;
import static apps.uzazisalama.com.anc.utils.Constants.POST_BOX_DATA_ROUTINE_VISIT;

/**
 * Created by issy on 09/07/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project UNFPA_UzaziSalama_Facility
 */

public class PostBoxWatcherService extends JobService {

    private Endpoints.ClientService clientService;
    private Endpoints.RoutineServices routineService;
    private Endpoints.ReferralService referralService;

    private SessionManager mSession;

    private LifecycleService lifecycleService;

    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onStartJob(JobParameters job) {

        Log.d("POSTBOXSERVICE", "Background postbox watcher");

        AppDatabase database = AppDatabase.getDatabase(this);
        lifecycleService = new LifecycleService();
        mSession = new SessionManager(getApplicationContext());
        clientService = ServiceGenerator.createService(Endpoints.ClientService.class,
                mSession.getUserName(),
                mSession.getUserPass(),
                mSession.getKeyHfid());
        routineService = ServiceGenerator.createService(Endpoints.RoutineServices.class,
                mSession.getUserName(),
                mSession.getUserPass(),
                mSession.getKeyHfid());
        /**
         if internet exist
         Scan through the postBox database
         If exist any data post it to server, clear the postBox entry
         */

        if (isNetworkAvailable()){ //Internet connection is available
            Log.d("POSTBOXSERVICE", "Network available");
            new AsyncTask<Void, Void, Void>(){

                List<PostBox> postBoxes = new ArrayList<PostBox>();

                @Override
                protected Void doInBackground(Void... voids) {
                    postBoxes = database.postBoxModelDao().getAllPostBoxEntries();
                    Log.d("POSTBOXSERVICE", "PostBox Size "+postBoxes.size());

                    if (postBoxes.size() > 0){
                        for (PostBox box : postBoxes){
                            //Get box datatype
                            int dataType = box.getPostDataType();
                            Log.d("POSTBOXSERVICE", "Data type "+dataType);
                            switch (dataType){
                                case POST_BOX_DATA_ANC_CLIENT:
                                    //Anc Client
                                    if (isNetworkAvailable()){
                                        /**
                                         *  Post data to server and delete it if successfully stored to server
                                         */
                                        AncClient mClient = database.clientModel().getClientById(Long.parseLong(box.getPostBoxId()));
                                        if (mClient != null){
                                            postAncClient(mClient, box, database);
                                        }

                                    }
                                    break;
                                case POST_BOX_DATA_PNC_CLIENT:
                                    //Pnc Client
                                    if (isNetworkAvailable()){
                                        /**
                                         *  Post data to server and delete it if successfully stored to server
                                         */
                                        PncClient client = database.pncClientModelDao().getClientByID(box.getPostBoxId());
                                        if (client != null){
                                            postPncClient(client, box, database);
                                        }
                                    }
                                    break;
                                case POST_BOX_DATA_ROUTINE_VISIT:
                                    //Routine Visit
                                    if (isNetworkAvailable()){
                                        /**
                                         *  Post data to server and delete it if successfully stored to server
                                         */
                                        RoutineVisits routineVisits = database.routineModelDao().getRoutineById(Long.parseLong(box.getPostBoxId()));
                                        if (routineVisits != null){
                                            postRoutineData(routineVisits, box, database);
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
                    }else {
                        Log.d("POSTBOXSERVICE", "Postbox Empty");
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                }

            }.execute();

        }


        return true;
    }

    private void postAncClient(AncClient client, PostBox box, AppDatabase database){
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
                    wrapper.setBoxItem(box);
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

                            //Add the client that has been received from the server
                            database.clientModel().addNewClient(registeredClient);
                            for (ClientAppointment appointment : appointmentList){
                                database.clientAppointmentDao().addNewAppointment(appointment);
                            }

                            //Delete the postBox Entry
                            database.postBoxModelDao().deletePostItem(postAncWrappers[0].boxItem);

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
                }

            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {

            }
        });
    }

    private void postPncClient(PncClient client, PostBox box, AppDatabase database){
        Call<PncClientPostResponce> call = clientService.postPncClient(getPncClientBody(client));
        call.enqueue(new Callback<PncClientPostResponce>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<PncClientPostResponce> call, Response<PncClientPostResponce> response) {
                if (response != null){

                    PncClientPostResponce pncClientPostResponce = response.body();

                    /**
                     * Wrap response and postBox data together to pass to the background class
                     */
                    PostPncWrapper postPncWrapper = new PostPncWrapper();
                    postPncWrapper.setPostBox(box);
                    postPncWrapper.setResponce(pncClientPostResponce);
                    postPncWrapper.setOldPncClient(client);

                    new AsyncTask<PostPncWrapper, Void, Void>(){
                        @Override
                        protected Void doInBackground(PostPncWrapper... wrappers) {

                            //Unpack individual objects from the wrapper
                            PostPncWrapper mWrapper = wrappers[0];
                            PncClientPostResponce response = mWrapper.getResponce();
                            PncClient receivedClient = response.getPncClient();
                            AncClient receivedAncClient = response.getAncClient();
                            List<RoutineVisits> visits = response.getRoutineVisits();

                            //Add the newly registered client to the local database
                            database.pncClientModelDao().addNewClient(receivedClient);

                            //Add the corresponding AncClient to the database
                            database.clientModel().addNewClient(receivedAncClient);

                            //Add all the received Routine Visits
                            for (RoutineVisits v : visits){
                                database.routineModelDao().addRoutine(v);
                            }

                            //Get PostBox From the wrapper
                            PostBox boxItem = mWrapper.getPostBox();
                            database.postBoxModelDao().deletePostItem(boxItem);

                            //Delete the old PNC object registered with temporary ID
                            database.pncClientModelDao().deleteAClient(mWrapper.oldPncClient);

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

    private void postRoutineData(RoutineVisits visits, PostBox box, AppDatabase database){

    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
        Log.d("POSTBOXSERVICE", "Anc Client Body === "+datastream);
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

}
