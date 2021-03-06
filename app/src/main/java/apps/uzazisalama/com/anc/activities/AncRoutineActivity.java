package apps.uzazisalama.com.anc.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.irozon.alertview.AlertActionStyle;
import com.irozon.alertview.AlertStyle;
import com.irozon.alertview.AlertView;
import com.irozon.alertview.objects.AlertAction;
import com.uniquestudio.library.CircleCheckBox;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import apps.uzazisalama.com.anc.R;
import apps.uzazisalama.com.anc.adapters.PreviousRoutinesAdapter;
import apps.uzazisalama.com.anc.api.Endpoints;
import apps.uzazisalama.com.anc.base.AppDatabase;
import apps.uzazisalama.com.anc.base.BaseActivity;
import apps.uzazisalama.com.anc.database.AncClient;
import apps.uzazisalama.com.anc.database.ClientAppointment;
import apps.uzazisalama.com.anc.database.PostBox;
import apps.uzazisalama.com.anc.database.RoutineVisits;
import apps.uzazisalama.com.anc.objects.RoutineResponse;
import apps.uzazisalama.com.anc.utils.ServiceGenerator;
import apps.uzazisalama.com.anc.viewmodels.RoutineViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static apps.uzazisalama.com.anc.utils.Constants.POST_BOX_DATA_ROUTINE_VISIT;
import static apps.uzazisalama.com.anc.utils.Constants.POST_DATA_UNSYNCED;

/**
 * Created by issy on 16/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project ANC
 */

public class AncRoutineActivity extends BaseActivity {

    Toolbar toolbar;
    CircleCheckBox aYes, aNo, oYes, oNo, pYes, pNo, hYes, hNo, wsYes, wsNo, ahYes, ahNo, suYes, suNo;
    RecyclerView previousRoutinesRecycler;
    TextView clientNames, spauseName, phoneNumber, village, visitCount, newVisitDate, enrollToPncButton, successMessage, clientPara, clientGravida;
    TextView saveRoutinesText;
    LinearLayout routineInputsWrap;
    RelativeLayout enrollToPncContainer, saveRoutinesButton;
    CircularProgressView savingRoutinesProgressView;
    boolean a, o, p, h, ws, ah, su;
    int lastVisit = 0;

    PreviousRoutinesAdapter adapter;
    RoutineViewModel routineViewModel;
    AncClient currentAncClient;
    RoutineVisits currentRoutineVisitsVisit;

    private DatePickerDialog visitDatePickerDialog = new DatePickerDialog();
    private Calendar cal;
    private long visitDate = Calendar.getInstance().getTimeInMillis();

    private Endpoints.RoutineServices routineService;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);
        setupviews();

        if (getIntent().getExtras() != null){
            currentAncClient = (AncClient) getIntent().getSerializableExtra("currentAncClient");
            displayClientInformation(currentAncClient);
        }

        if (toolbar!=null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        routineService = ServiceGenerator.createService(Endpoints.RoutineServices.class,
                session.getUserName(),
                session.getUserPass(),
                session.getKeyHfid());

        setCheckboxListeners();

        /**
         * Load previous visits
         */
        new AsyncTask<Void, Void, Void>(){

            List<RoutineVisits> thisClientsRoutineVisits;
            int mostRecentVisit = 0;

            @Override
            protected Void doInBackground(Void... voids) {

                if (currentAncClient != null){
                    thisClientsRoutineVisits = database.routineModelDao().getClientRoutines(currentAncClient.getHealthFacilityClientId());
                }

                for (RoutineVisits r : thisClientsRoutineVisits){
                    if (r.getVisitNumber() > mostRecentVisit)
                        mostRecentVisit = r.getVisitNumber();
                }

                lastVisit = mostRecentVisit;

                Log.d("midnightstaring", "Size of routines "+ thisClientsRoutineVisits.size());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                int newVisitNumber = mostRecentVisit+1;
                if (newVisitNumber > 5){
                    //Enroll client to PNC
                    enrollToPncContainer.setVisibility(View.VISIBLE);
                    routineInputsWrap.setVisibility(View.GONE);
                    //saveRoutineButton.setVisibility(View.GONE);
                }else {
                    enrollToPncContainer.setVisibility(View.GONE);
                    routineInputsWrap.setVisibility(View.VISIBLE);
                    //saveRoutineButton.setVisibility(View.VISIBLE);
                }

                checkPregnancyPeriodSinceLnmp();

                visitCount.setText(newVisitNumber+"");
                adapter = new PreviousRoutinesAdapter(thisClientsRoutineVisits, AncRoutineActivity.this);
                previousRoutinesRecycler.setAdapter(adapter);
            }
        }.execute();

        saveRoutinesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savingRoutinesProgressView.setVisibility(View.VISIBLE);
                saveRoutinesText.setVisibility(View.GONE);
                if (routineObjectCreated()){
                    saveRoutineData();
                }else {
                    savingRoutinesProgressView.setVisibility(View.GONE);
                    saveRoutinesText.setVisibility(View.VISIBLE);
                }
            }
        });

        enrollToPncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AncRoutineActivity.this, PncClientDetailActivity.class);
                intent.putExtra("currentAncClient", currentAncClient);
                startActivity(intent);
            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    void checkPregnancyPeriodSinceLnmp(){

        long today = new Date().getTime();
        long lnmp = currentAncClient.getLnmp();

        long pregnancyPeriod = today - lnmp;

        long twentyFourHours = 24*60*60*1000;
        long oneWeek = twentyFourHours*7;
        long twentyEightWeeks = oneWeek*28;

        Log.d("JOSH", "Today - "+today);
        Log.d("JOSH", "LNMP - "+lnmp);
        Log.d("JOSH", "Pregnancy Period - "+pregnancyPeriod);
        Log.d("JOSH", "28 Weeks - "+twentyEightWeeks);

        if (pregnancyPeriod > twentyEightWeeks){

            //Restrict Routine Information Inputs
            enrollToPncContainer.setVisibility(View.VISIBLE);
            routineInputsWrap.setVisibility(View.GONE);
            saveRoutinesButton.setVisibility(View.GONE);

            Intent intent = new Intent(AncRoutineActivity.this, PncClientDetailActivity.class);
            intent.putExtra("currentAncClient", currentAncClient);
            startActivity(intent);
        }
    }

    public void selectVisitDate(View view){
        visitDatePickerDialog.show(this.getFragmentManager(),"fromDateRange");
        visitDatePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                newVisitDate.setText((dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "-" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : monthOfYear + 1) + "-" + year);
                cal = Calendar.getInstance();
                cal.set(year, monthOfYear, dayOfMonth);
                visitDate = cal.getTimeInMillis();
            }

        });
    }

    void setupviews(){
        toolbar = findViewById(R.id.routine_toolbar);

        previousRoutinesRecycler = findViewById(R.id.previous_routines_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        previousRoutinesRecycler.setLayoutManager(layoutManager);
        previousRoutinesRecycler.setHasFixedSize(true);

        //Layouts
        routineInputsWrap = findViewById(R.id.routine_inputs_wrap);
        enrollToPncContainer = findViewById(R.id.enroll_to_pnc_container);



        //Progress View
        savingRoutinesProgressView = findViewById(R.id.save_routines_progress_view);
        savingRoutinesProgressView.setVisibility(View.GONE);

        saveRoutinesButton = findViewById(R.id.save_routine_button);

        // Checkboxes
        aYes = findViewById(R.id.a_yes);
        aNo = findViewById(R.id.a_no);
        oYes = findViewById(R.id.o_yes);
        oNo = findViewById(R.id.o_no);
        pYes = findViewById(R.id.p_yes);
        pNo = findViewById(R.id.p_no);
        hYes = findViewById(R.id.h_yes);
        hNo = findViewById(R.id.h_no);
        wsNo = findViewById(R.id.ws_no);
        wsYes = findViewById(R.id.ws_yes);
        ahYes = findViewById(R.id.ah_yes);
        ahNo = findViewById(R.id.ah_no);
        suYes = findViewById(R.id.su_yes);
        suNo = findViewById(R.id.su_no);

        //TextViews
        clientNames = findViewById(R.id.details_client_names);
        spauseName = findViewById(R.id.client_spause_name_value);
        village = findViewById(R.id.client_village_value);
        phoneNumber = findViewById(R.id.client_phone_value);
        visitCount = findViewById(R.id.visit_count);
        newVisitDate = findViewById(R.id.new_visit_date);
        enrollToPncButton = findViewById(R.id.enroll_to_pnc);
        successMessage = findViewById(R.id.success_message);
        successMessage.setVisibility(View.GONE);
        saveRoutinesText = findViewById(R.id.save_routines_text);
        clientPara = findViewById(R.id.client_para);
        clientGravida = findViewById(R.id.client_gravida);

    }

    void setCheckboxListeners(){
        aYes.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    aNo.setChecked(false);
                    a = true;
                }
            }
        });
        aNo.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    aYes.setChecked(false);
                    a = false;
                }
            }
        });
        oYes.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    o = true;
                    oNo.setChecked(false);
                }
            }
        });
        oNo.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    o = false;
                    oYes.setChecked(false);
                }
            }
        });
        pYes.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    p = true;
                    pNo.setChecked(false);
                }
            }
        });
        pNo.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    p = false;
                    pYes.setChecked(false);
                }
            }
        });
        hYes.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    h = true;
                    hNo.setChecked(false);
                }
            }
        });
        hNo.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    h = false;
                    hYes.setChecked(false);
                }
            }
        });
        wsYes.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    ws = true;
                    wsNo.setChecked(false);
                }
            }
        });
        wsNo.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    ws = false;
                    wsYes.setChecked(false);
                }
            }
        });
        ahYes.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    ah = true;
                    ahNo.setChecked(false);
                }
            }
        });
        ahNo.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    ah = false;
                    ahYes.setChecked(false);
                }
            }
        });
        suYes.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    su = true;
                    suNo.setChecked(false);
                }
            }
        });
        suNo.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    su = false;
                    suYes.setChecked(false);
                }
            }
        });
    }

    void displayClientInformation(AncClient ancClient){
        clientNames.setText(ancClient.getFirstName()+" "+ ancClient.getMiddleName()+" "+ ancClient.getSurname());
        village.setText(ancClient.getVillage());
        phoneNumber.setText(ancClient.getPhoneNumber());
        spauseName.setText(ancClient.getSpouseName());
        clientPara.setText(ancClient.getPara()+"");
        clientGravida.setText(ancClient.getGravida()+"");
        newVisitDate.setText(BaseActivity.simpleDateFormat.format(new Date()));
    }

    boolean routineObjectCreated(){

        int visitCount = lastVisit+1;

        RoutineVisits routineVisits = new RoutineVisits();
        //Generate random temporary ID
        long range = 1234567L;
        Random r = new Random();
        long randomID = (long)(r.nextDouble()*range);
        routineVisits.setID(randomID);

        routineVisits.setHealthFacilityClientId(currentAncClient.getHealthFacilityClientId());
        routineVisits.setVisitDate(visitDate);
        routineVisits.setVisitNumber(visitCount);

        routineVisits.setAnaemia(a);
        routineVisits.setOedema(o);
        routineVisits.setProtenuria(p);
        routineVisits.setHighBloodPressure(h);
        routineVisits.setWeightStagnation(ws);
        routineVisits.setAntepartumHaemorrhage(ah);
        routineVisits.setSugarInTheUrine(su);

        currentRoutineVisitsVisit = routineVisits;

        return true;

    }

    @SuppressLint("StaticFieldLeak")
    void saveRoutineData(){
        RoutineVisits routineVisits = currentRoutineVisitsVisit;
        AppDatabase db = database;

        if (isNetworkAvailable()){
            new AsyncTask<Void, Void, Void>(){

                long appointmentId;

                @Override
                protected Void doInBackground(Void... voids) {
                    List<ClientAppointment> list = database.clientAppointmentDao().getAppointmentsByClientId(
                            routineVisits.getHealthFacilityClientId());
                    ClientAppointment visitAppointment = new ClientAppointment();
                    for (ClientAppointment a : list){
                        if (a.getVisitNumber() == routineVisits.getVisitNumber()){
                            visitAppointment = a;
                            routineVisits.setAppointmentID(visitAppointment.getAppointmentID());
                            routineVisits.setAppointmentDate(visitAppointment.getAppointmentDate());
                        }
                    }
                    Log.d("appointment", "Appointment size : "+visitAppointment.getAppointmentID());
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                    Call<RoutineResponse> call = routineService.saveRoutineVisit(getRequestBody(routineVisits));
                    call.enqueue(new Callback<RoutineResponse>() {
                        @Override
                        public void onResponse(Call<RoutineResponse> call, Response<RoutineResponse> response) {

                            try{
                                Log.d("routineVisits", response.body().toString());

                                RoutineResponse response1 = response.body();
                                RoutineVisits visit = response1.getRoutineVisits();
                                visit.setVisitDate(Calendar.getInstance().getTimeInMillis());
                                List<ClientAppointment> appointments = response1.getAppointments();

                                new AsyncTask<RoutineVisits, Void, Void>(){

                                    String nextAppointmentDate = "";

                                    @Override
                                    protected Void doInBackground(RoutineVisits... routineVisits) {
                                        db.routineModelDao().addRoutine(routineVisits[0]);

                                        //Next Client Appointment Date
                                        int currentVisit = routineVisits[0].getVisitNumber();
                                        int nextVisit = currentVisit + 1;
                                        for (ClientAppointment a : appointments){
                                            if (a.getVisitNumber() == nextVisit){
                                                nextAppointmentDate = simpleDateFormat.format(a.getAppointmentDate());
                                            }
                                            db.clientAppointmentDao().addNewAppointment(a);
                                        }
                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        super.onPostExecute(aVoid);

                                        savingRoutinesProgressView.setVisibility(View.GONE);
                                        saveRoutinesText.setVisibility(View.VISIBLE);

                                        Context context = AncRoutineActivity.this;
                                        AlertView alert = new AlertView("Routine Stored Successfully", "Next Appointment : "+nextAppointmentDate, AlertStyle.DIALOG);
                                        alert.addAction(new AlertAction("OK", AlertActionStyle.DEFAULT, action -> {
                                            // Action 2 callback
                                            finish();
                                        }));
                                        alert.show(AncRoutineActivity.this);

                                    }
                                }.execute(visit);
                            }catch (NullPointerException e){
                                e.printStackTrace();
                                savingRoutinesProgressView.setVisibility(View.GONE);
                                saveRoutinesText.setVisibility(View.VISIBLE);
                            }

                        }

                        @Override
                        public void onFailure(Call<RoutineResponse> call, Throwable t) {
                            Log.d("TheyCantHoldUs", t.getMessage());
                            savingRoutinesProgressView.setVisibility(View.GONE);
                            saveRoutinesText.setVisibility(View.VISIBLE);
                        }
                    });

                }
            }.execute();
        }else {
            //Save locally and at to postBox
            new AsyncTask<Void, Void, Void>(){

                String nextAppointmentDate = "";

                @Override
                protected Void doInBackground(Void... voids) {

                    List<ClientAppointment> list = database.clientAppointmentDao().getAppointmentsByClientId(
                            routineVisits.getHealthFacilityClientId());
                    ClientAppointment visitAppointment = new ClientAppointment();
                    for (ClientAppointment a : list){
                        if (a.getVisitNumber() == routineVisits.getVisitNumber()){
                            visitAppointment = a;
                            routineVisits.setAppointmentID(visitAppointment.getAppointmentID());
                            routineVisits.setAppointmentDate(visitAppointment.getAppointmentDate());
                        }
                    }

                    db.routineModelDao().addRoutine(routineVisits);

                    PostBox postBox = new PostBox();
                    postBox.setSyncStatus(POST_DATA_UNSYNCED);
                    postBox.setPostDataType(POST_BOX_DATA_ROUTINE_VISIT);
                    postBox.setPostBoxId(routineVisits.getID()+"");

                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_MONTH, 30);
                    nextAppointmentDate = simpleDateFormat.format(calendar.getTimeInMillis());

                    db.postBoxModelDao().AddNewPost(postBox);

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                    savingRoutinesProgressView.setVisibility(View.GONE);
                    saveRoutinesText.setVisibility(View.VISIBLE);

                    Context context = AncRoutineActivity.this;
                    AlertView alert = new AlertView("Routine Stored Successfully (SYNC NEEDED)", "Next Appointment : "+nextAppointmentDate, AlertStyle.DIALOG);
                    alert.addAction(new AlertAction("OK", AlertActionStyle.DEFAULT, action -> {
                        // Action 2 callback
                        finish();
                    }));
                    alert.show(AncRoutineActivity.this);

                }
            }.execute();

        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
