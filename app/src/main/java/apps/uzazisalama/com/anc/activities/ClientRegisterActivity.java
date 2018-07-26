package apps.uzazisalama.com.anc.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.uniquestudio.library.CircleCheckBox;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import apps.uzazisalama.com.anc.MainActivity;
import apps.uzazisalama.com.anc.R;
import apps.uzazisalama.com.anc.api.Endpoints;
import apps.uzazisalama.com.anc.base.BaseActivity;
import apps.uzazisalama.com.anc.database.AncClient;
import apps.uzazisalama.com.anc.database.ClientAppointment;
import apps.uzazisalama.com.anc.objects.RegistrationResponse;
import apps.uzazisalama.com.anc.utils.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static apps.uzazisalama.com.anc.utils.Constants.ABOVE_TWELVE_WEEKS;
import static apps.uzazisalama.com.anc.utils.Constants.HEIGHT_ABOVE_ONE_FIFTY;
import static apps.uzazisalama.com.anc.utils.Constants.HEIGHT_BELOW_ONE_FIFTY;
import static apps.uzazisalama.com.anc.utils.Constants.LESS_THAN_TWELVE_WEEKS;

/**
 * Created by issy on 10/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project ANC
 */

public class ClientRegisterActivity extends BaseActivity {

    Toolbar clientsRegisterToolbar;
    MaterialSpinner gestationalAgeSpinner, heightSpinner, levelOfEducationSpinner, pmtctStatusSpinner, lastChildBirthYearSpinner, lastChildBirthStatusSpinner, familyPlanningMethodSpinner;
    EditText firstName, middleName, surname, dateOfBirth, phoneNumber, village, gravidaEt, paraEt, spauseName;
    EditText dateOfLNMP, dateOfDelivery, lastChildBirthYearEt;
    CircleCheckBox historyOfAbortionYes, historyOfAbortionNo, ageBelow20Yes, ageBelow20No, lastPregnancy10YearsYes, lastPregnancy10YearsNo, usesFamilyPlanningNo, usesFamilyPlanningYes;
    CircleCheckBox pregnancyWithMoreThan35YearsYes, pregnancyWithMoreThan35YearsNo, historyOfStillBirthYes, historyOfStillBirthNo, historyOfPostmartumYes, historyOfPostmartumNo;
    CircleCheckBox historyOfRetainedPlacentaYes, historyOfRetainedPlacentaNo;
    Button cancelButton, saveButton;
    LinearLayout familyPlanningMethodContainer;

    //ValueHolders
    String fnameValue, mnameValue, lnameValue, dobValue, phoneValue, villageValue, spauseNameValue;
    int gravidaValue, paraValue, lastChildBirthStatus, lastChildBirthYear;
    boolean historyOfAbortion, ageBelow20, lastPregnancy10Years, pregnancyWithMoreThan35Years, historyOfStillBirths, historyOfPostmartum, historyOfRetainedPlacenta;
    boolean gestationalAgeBelow12Weeks = true;
    boolean heightBelowAverage = true;
    String gestationalAge, height, levelOfEducation, pmCtcStatus, dateOfBirthDisplay, dateOfLNMPDisplay, dateOfDeliveryDisplay;
    long dateOfBirthValue, dateOfLNMPValue, dateOfDeliveryValue;

    Calendar dobCalendar;
    Calendar dolnmpCalendar;

    List<String> gestationalAgeList, heightList, levelOfEducationList, pmctcStatusList, lastChildBirthStatusList, familyPlanningMethods;

    public DatePickerDialog dobDatePicker = new DatePickerDialog();
    public DatePickerDialog lnmpDatePicker = new DatePickerDialog();
    final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Endpoints.ClientService clientService;

    AncClient clientZero;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_clients);
        setupviews();

        if (getIntent().getExtras() != null){
            clientZero = (AncClient) getIntent().getSerializableExtra("clientZero");
            fillInputsWithClientZeroData(clientZero);
        }

        //initialize activity's toolbar
        clientsRegisterToolbar = findViewById(R.id.register_clients_toolbar);
        if (clientsRegisterToolbar!=null){
            setSupportActionBar(clientsRegisterToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        clientService = ServiceGenerator.createService(Endpoints.ClientService.class,
                session.getUserName(),
                session.getUserPass(),
                session.getKeyHfid());

        //initialize the gestational age spinner with values
        gestationalAgeList = new ArrayList<>();
        gestationalAgeList.add(LESS_THAN_TWELVE_WEEKS);
        gestationalAgeList.add(ABOVE_TWELVE_WEEKS);
        gestationalAgeSpinner.setItems(gestationalAgeList);
        gestationalAgeSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if (item.equals(LESS_THAN_TWELVE_WEEKS)){
                    gestationalAgeBelow12Weeks = true;
                }else {
                    gestationalAgeBelow12Weeks = false;
                }
            }
        });

        //initializing the height spinner with values
        heightList = new ArrayList<>();
        heightList.add(HEIGHT_BELOW_ONE_FIFTY);
        heightList.add(HEIGHT_ABOVE_ONE_FIFTY);
        heightSpinner.setItems(heightList);
        heightSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if (item.equals(HEIGHT_BELOW_ONE_FIFTY)){
                    heightBelowAverage = true;
                }else {
                    heightBelowAverage = false;
                }
            }
        });

        //initializing the level of education spinner with values
        levelOfEducationList = new ArrayList<>();
        levelOfEducationList.add("Primary School");
        levelOfEducationList.add("Secondary School");
        levelOfEducationList.add("High School");
        levelOfEducationList.add("University");
        levelOfEducationList.add("None");
        levelOfEducationSpinner.setItems(levelOfEducationList);
        levelOfEducationSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                //Snackbar.make(view, "Clicked " + item +" Position "+position, Snackbar.LENGTH_LONG).show();
                if (position == 0)
                    levelOfEducation = "";
                else levelOfEducation = item;
            }
        });

        familyPlanningMethods = new ArrayList<>();
        familyPlanningMethods.add("Condoms");
        familyPlanningMethods.add("Pills");
        familyPlanningMethods.add("Calendar");
        familyPlanningMethodSpinner.setItems(familyPlanningMethods);
        familyPlanningMethodSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                switch (position){
                    case 0:
                        //Uses Condom;
                        break;
                    case 1:
                        //uses Birth control pills
                        break;
                    case 2:
                        //Uses Calendar
                        break;
                    default:
                        //Nothing
                }
            }
        });


        //initializing the pmtctStatus spinner with values
        pmctcStatusList = new ArrayList<>();
        pmctcStatusList.add("1");
        pmctcStatusList.add("2");
        pmctcStatusList.add("Unknown");
        pmtctStatusSpinner.setItems(pmctcStatusList);
        pmtctStatusSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Selected PMTCT Status "+item, Snackbar.LENGTH_LONG).show();
                if (position == 0)
                    pmCtcStatus = "";
                else pmCtcStatus = item;
            }
        });

        lastChildBirthStatusList = new ArrayList<>();
        lastChildBirthStatusList.add("Alive");
        lastChildBirthStatusList.add("Died");
        lastChildBirthStatusSpinner.setItems(lastChildBirthStatusList);
        lastChildBirthStatusSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (position == 0){
                    lastChildBirthStatus = 1;
                }else if (position == 1){
                    lastChildBirthStatus = 0;
                }
            }
        });

        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dobDatePicker.show(getFragmentManager(),"dateOfBirth");
                dobDatePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

                        dateOfBirth.setText((dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "-" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : monthOfYear + 1) + "-" + year);
                        dobCalendar = Calendar.getInstance();
                        dobCalendar.set(year, monthOfYear, dayOfMonth);
                        dateOfBirthValue = dobCalendar.getTimeInMillis();
                        dateOfBirthDisplay = simpleDateFormat.format(dobCalendar.getTime());
                        dateOfBirth.setText(dateOfBirthDisplay);
                    }

                });
            }
        });

        dateOfLNMP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lnmpDatePicker.show(getFragmentManager(),"dateOfLnmp");
                lnmpDatePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        dolnmpCalendar = Calendar.getInstance();
                        dolnmpCalendar.set(year, monthOfYear, dayOfMonth);
                        dateOfLNMPValue = dolnmpCalendar.getTimeInMillis();
                        dateOfLNMPDisplay = simpleDateFormat.format(dolnmpCalendar.getTime());
                        dateOfLNMP.setText(dateOfLNMPDisplay);

                        dateOfDeliveryValue = calculateEDDFromLNMP(dateOfLNMPValue);
                        Calendar calendar  = Calendar.getInstance();
                        calendar.setTimeInMillis(dateOfDeliveryValue);
                        dateOfDeliveryDisplay = simpleDateFormat.format(calendar.getTime());
                        dateOfDelivery.setText(dateOfDeliveryDisplay);

                    }

                });
            }
        });

        //checkbox listeners -> capture the selected data and store the value at the time the user is selecting
        historyOfAbortionYes.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    historyOfAbortionNo.setChecked(false);
                    historyOfAbortion = true;
                }
            }
        });
        historyOfAbortionNo.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    historyOfAbortionYes.setChecked(false);
                    historyOfAbortion = false;
                }
            }
        });
        ageBelow20Yes.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    ageBelow20Yes.setChecked(true);
                    ageBelow20 = true;
                }
            }
        });
        ageBelow20No.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    ageBelow20Yes.setChecked(false);
                    ageBelow20 = false;
                }
            }
        });
        lastPregnancy10YearsYes.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
            if (isChecked){
                lastPregnancy10YearsNo.setChecked(false);
                lastPregnancy10Years = true;
            }
            }
        });
        lastPregnancy10YearsNo.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    lastPregnancy10YearsYes.setChecked(false);
                    lastPregnancy10Years = false;
                }
            }
        });
        pregnancyWithMoreThan35YearsYes.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked) {
                    pregnancyWithMoreThan35YearsNo.setChecked(false);
                    pregnancyWithMoreThan35Years = true;
                }
            }
        });
        pregnancyWithMoreThan35YearsNo.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    pregnancyWithMoreThan35YearsYes.setChecked(false);
                    pregnancyWithMoreThan35Years = false;
                }
            }
        });
        historyOfStillBirthYes.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    historyOfStillBirthNo.setChecked(false);
                    historyOfStillBirths = true;
                }
            }
        });
        historyOfStillBirthNo.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    historyOfStillBirthYes.setChecked(false);
                    historyOfStillBirths = false;
                }
            }
        });
        historyOfPostmartumYes.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    historyOfPostmartumNo.setChecked(false);
                    historyOfPostmartum = true;
                }
            }
        });
        historyOfPostmartumNo.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    historyOfPostmartumYes.setChecked(false);
                    historyOfPostmartum = false;
                }
            }
        });
        historyOfRetainedPlacentaYes.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    historyOfRetainedPlacentaNo.setChecked(false);
                    historyOfRetainedPlacenta = true;
                }
            }
        });
        historyOfRetainedPlacentaNo.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    historyOfRetainedPlacentaYes.setChecked(false);
                    historyOfRetainedPlacenta = false;
                }
            }
        });


        usesFamilyPlanningYes.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    usesFamilyPlanningNo.setChecked(false);
                    familyPlanningMethodContainer.setVisibility(View.VISIBLE);
                }
            }
        });

        usesFamilyPlanningNo.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    usesFamilyPlanningYes.setChecked(false);
                    familyPlanningMethodContainer.setVisibility(View.INVISIBLE);
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verifyInputs()){
                    createNewUserObject(view);
                }
            }
        });

    }

    public static long calculateEDDFromLNMP(long lnmp) {
        long day = 24 * 60 * 60 * 1000;
        return (day * 281) + lnmp;
    }

    boolean verifyInputs(){
        //From EditTexts
        fnameValue = firstName.getText().toString();
        mnameValue = middleName.getText().toString();
        lnameValue = surname.getText().toString();
        dobValue = dateOfBirth.getText().toString();
        phoneValue = phoneNumber.getText().toString();
        villageValue = village.getText().toString();
        gravidaValue = Integer.parseInt(gravidaEt.getText().toString());
        paraValue = Integer.parseInt(paraEt.getText().toString());
        spauseNameValue = spauseName.getText().toString();
        lastChildBirthYear = lastChildBirthYearEt.getText().toString().isEmpty()? 0 : Integer.parseInt(lastChildBirthYearEt.getText().toString());

        return true;
    }

    @SuppressLint("StaticFieldLeak")
    void createNewUserObject(View view){
        AncClient ancClient = new AncClient();

        ancClient.setFirstName(fnameValue);
        ancClient.setMiddleName(mnameValue);
        ancClient.setSurname(lnameValue);
        ancClient.setDateOfBirth(dateOfBirthValue);
        ancClient.setPhoneNumber(phoneValue);
        ancClient.setVillage(villageValue);
        ancClient.setGravida(gravidaValue);
        ancClient.setPara(paraValue);
        ancClient.setSpouseName(spauseNameValue);
        ancClient.setLnmp(dateOfLNMPValue);
        ancClient.setEdd(dateOfDeliveryValue);
        ancClient.setLastChildBirthStatus(lastChildBirthStatus);
        ancClient.setLastChildBirthYear(lastChildBirthYear);
        ancClient.setClientType(1);
        ancClient.setGestationalAgeBelow20(gestationalAgeBelow12Weeks);
        ancClient.setHeightBelowAverage(heightBelowAverage);
        Date today  = new Date();
        ancClient.setCreatedAt(today.getTime());

        Call<RegistrationResponse> call = clientService.postAncClient(getAncClientBody(ancClient));
        call.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {

                if (response != null){
                    Log.d("savingClient", response.body().toString());
                }

                RegistrationResponse registrationResponse = response.body();

                AncClient registeredClient = registrationResponse.getClient();
                //Hack-Alert TODO: Remove once the registered date is implemented on the server side
                registeredClient.setCreatedAt(ancClient.getCreatedAt());

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
                            Snackbar.make(view,"User created Successfully", Snackbar.LENGTH_LONG).show();

                            Intent intent = new Intent(ClientRegisterActivity.this, AncClientsListActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            ClientRegisterActivity.this.finish();

                        }
                    }.execute(registeredClient);
                }

            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {

            }
        });

    }

    void fillInputsWithClientZeroData(AncClient client){

        /**
         int lastChildBirthStatus, lastChildBirthYear;
         boolean
         String gestationalAge, height, levelOfEducation, pmCtcStatus, dateOfBirthDisplay, dateOfLNMPDisplay, dateOfDeliveryDisplay;
         */

        Calendar calendar = Calendar.getInstance();

        firstName.setText(client.getFirstName());
        fnameValue = client.getFirstName();

        middleName.setText(client.getMiddleName());
        mnameValue = client.getMiddleName();

        surname.setText(client.getSurname());
        lnameValue = client.getSurname();

        calendar.setTimeInMillis(client.getDateOfBirth());
        dateOfBirthValue = client.getDateOfBirth();
        dateOfBirth.setText(simpleDateFormat.format(calendar.getTime()));

        phoneNumber.setText(client.getPhoneNumber());
        phoneValue = client.getPhoneNumber();

        village.setText(client.getVillage());
        villageValue = client.getVillage();

        gravidaEt.setText(client.getGravida()+"");
        gravidaValue = client.getGravida();

        paraEt.setText(client.getPara()+"");
        paraValue = client.getPara();

        spauseName.setText(client.getSpouseName());
        spauseNameValue = client.getSpouseName();

        lastChildBirthYearEt.setText(client.getLastChildBirthYear()+"");
        lastChildBirthYear = client.getLastChildBirthYear();

        calendar.setTimeInMillis(client.getLnmp());
        dateOfLNMPValue = client.getLnmp();
        dateOfLNMP.setText(simpleDateFormat.format(calendar.getTime()));

        calendar.setTimeInMillis(client.getEdd());
        dateOfDeliveryValue = client.getEdd();
        dateOfDelivery.setText(simpleDateFormat.format(calendar.getTime()));

        setCheckbox(historyOfAbortionYes, historyOfAbortionNo, client.isHistoryOfAbortion());
        historyOfAbortion = client.isHistoryOfAbortion();

        setCheckbox(ageBelow20Yes, ageBelow20No, client.isAgeBelow20Years());
        ageBelow20 = client.isAgeBelow20Years();

        setCheckbox(lastPregnancy10YearsYes, lastPregnancy10YearsNo, client.isLastPregnancyOver10yearsAgo());
        lastPregnancy10Years = client.isLastPregnancyOver10yearsAgo();

        setCheckbox(pregnancyWithMoreThan35YearsYes, pregnancyWithMoreThan35YearsNo, client.isPregnancyAbove35Years());
        pregnancyWithMoreThan35Years = client.isPregnancyAbove35Years();

        setCheckbox(historyOfStillBirthYes, historyOfStillBirthNo, client.isHistoryOfStillBirths());
        historyOfStillBirths = client.isHistoryOfStillBirths();

        setCheckbox(historyOfPostmartumYes, historyOfPostmartumNo, client.isHistoryOfPostmartumHaemorrhage());
        historyOfPostmartum = client.isHistoryOfPostmartumHaemorrhage();

        setCheckbox(historyOfRetainedPlacentaYes, historyOfRetainedPlacentaNo, client.isHistoryOfRetainedPlacenta());
        historyOfRetainedPlacenta = client.isHistoryOfRetainedPlacenta();



    }

    void setCheckbox(CircleCheckBox checkBoxYes, CircleCheckBox checkBoxNo, boolean value){
        if (value){
            checkBoxYes.setChecked(true);
            checkBoxNo.setChecked(false);
        }else {
            checkBoxYes.setChecked(false);
            checkBoxNo.setChecked(true);
        }
    }

    void setupviews(){

        //setup date picker dialogue
        dobDatePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));

        //MaterialSpinner
        gestationalAgeSpinner = findViewById(R.id.spinner);
        heightSpinner = findViewById(R.id.height_spinner);
        levelOfEducationSpinner = findViewById(R.id.level_of_education_spinner);
        pmtctStatusSpinner = findViewById(R.id.pmtct_status_spinner);
        lastChildBirthStatusSpinner = findViewById(R.id.last_child_birth_status_spinner);
        familyPlanningMethodSpinner = findViewById(R.id.fp_method_spinner);

        //EditText
        firstName = findViewById(R.id.first_name);
        middleName = findViewById(R.id.middle_name);
        surname = findViewById(R.id.last_name);
        dateOfBirth = findViewById(R.id.date_of_birth);
        dateOfBirth.setFocusableInTouchMode(false); //Prevent Soft keyboard when user clicks and open up a date picker
        phoneNumber = findViewById(R.id.phone);
        village = findViewById(R.id.village);
        gravidaEt = findViewById(R.id.gravida);
        paraEt = findViewById(R.id.para);
        spauseName = findViewById(R.id.spause_name);
        dateOfLNMP = findViewById(R.id.date_of_lnmp);
        dateOfLNMP.setFocusableInTouchMode(false); //Prevent Soft keyboard when user clicks and open up a date picker
        dateOfDelivery = findViewById(R.id.date_of_delivery);
        dateOfDelivery.setFocusableInTouchMode(false); //Prevent Soft keyboard when user clicks and open up a date picker
        lastChildBirthYearEt = findViewById(R.id.last_child_birth_year_et);

        //CircleCheckBox
        historyOfAbortionYes = findViewById(R.id.history_of_abortion_yes_checkbox);
        historyOfAbortionNo = findViewById(R.id.history_of_abortion_no_checkbox);
        ageBelow20Yes = findViewById(R.id.age_below_20_yes_checkbox);
        ageBelow20No = findViewById(R.id.age_below_20_no_checkbox);
        lastPregnancy10YearsYes = findViewById(R.id.last_pregnancy_10_years_yes_checkbox);
        lastPregnancy10YearsNo = findViewById(R.id.last_pregnancy_10_years_no_checkbox);
        pregnancyWithMoreThan35YearsYes = findViewById(R.id.pregnancy_morethan35years_yes_checkbox);
        pregnancyWithMoreThan35YearsNo = findViewById(R.id.pregnancy_morethan35years_no_checkbox);
        historyOfStillBirthYes = findViewById(R.id.history_of_stillbirth_yes_checkbox);
        historyOfStillBirthNo = findViewById(R.id.history_of_stillbirth_no_checkbox);
        historyOfPostmartumYes = findViewById(R.id.postpartum_haemorrhage_yes_checkbox);
        historyOfPostmartumNo = findViewById(R.id.postpartum_haemorrhage_no_checkbox);
        historyOfRetainedPlacentaYes = findViewById(R.id.retained_placenta_yes_checkbox);
        historyOfRetainedPlacentaNo = findViewById(R.id.retained_placenta_no_checkbox);
        usesFamilyPlanningNo = findViewById(R.id.uses_fp_no_checkbox);
        usesFamilyPlanningYes = findViewById(R.id.uses_fp_yes_checkbox);

        //Button
        cancelButton = findViewById(R.id.cancel);
        saveButton = findViewById(R.id.registered_client_save_button);

        //LinearLayouts
        familyPlanningMethodContainer = findViewById(R.id.family_planning_method_container);
    }

}
