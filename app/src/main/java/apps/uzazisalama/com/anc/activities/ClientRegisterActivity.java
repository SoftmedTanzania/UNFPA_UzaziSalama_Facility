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
import android.widget.EditText;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.uniquestudio.library.CircleCheckBox;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import apps.uzazisalama.com.anc.MainActivity;
import apps.uzazisalama.com.anc.R;
import apps.uzazisalama.com.anc.api.Endpoints;
import apps.uzazisalama.com.anc.base.BaseActivity;
import apps.uzazisalama.com.anc.database.AncClient;
import apps.uzazisalama.com.anc.utils.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by issy on 10/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project ANC
 */

public class ClientRegisterActivity extends BaseActivity {

    Toolbar clientsRegisterToolbar;
    MaterialSpinner gestationalAgeSpinner, heightSpinner, levelOfEducationSpinner, pmtctStatusSpinner, lastChildBirthYearSpinner, lastChildBirthStatusSpinner;
    EditText firstName, middleName, surname, dateOfBirth, phoneNumber, village, gravidaEt, paraEt, spauseName;
    EditText dateOfLNMP, dateOfDelivery;
    CircleCheckBox historyOfAbortionYes, historyOfAbortionNo, ageBelow20Yes, ageBelow20No, lastPregnancy10YearsYes, lastPregnancy10YearsNo;
    CircleCheckBox pregnancyWithMoreThan35YearsYes, pregnancyWithMoreThan35YearsNo, historyOfStillBirthYes, historyOfStillBirthNo, historyOfPostmartumYes, historyOfPostmartumNo;
    CircleCheckBox historyOfRetainedPlacentaYes, historyOfRetainedPlacentaNo;
    Button cancelButton, saveButton;

    //ValueHolders
    String fnameValue, mnameValue, lnameValue, dobValue, phoneValue, villageValue, spauseNameValue;
    int gravidaValue, paraValue, lastChildBirthStatus, lastChildBirthYear;
    boolean historyOfAbortion, ageBelow20, lastPregnancy10Years, pregnancyWithMoreThan35Years, historyOfStillBirths, historyOfPostmartum, historyOfRetainedPlacenta;
    String gestationalAge, height, levelOfEducation, pmCtcStatus, dateOfBirthDisplay, dateOfLNMPDisplay, dateOfDeliveryDisplay;
    long dateOfBirthValue, dateOfLNMPValue, dateOfDeliveryValue;
    Calendar dobCalendar;

    public DatePickerDialog dobDatePicker = new DatePickerDialog();
    final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Endpoints.ClientService clientService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_clients);
        setupviews();

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
        gestationalAgeSpinner.setItems("Gestational Age", "< 20 Weeks", "20+ Weeks");
        gestationalAgeSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item +" Position "+position, Snackbar.LENGTH_LONG).show();
                if (position == 0){
                    gestationalAge = "";
                }else {
                    gestationalAge = item;
                }
            }
        });

        //initializing the height spinner with values
        heightSpinner.setItems("Height", " <150 ", " >150 ");
        heightSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item +" Position "+position, Snackbar.LENGTH_LONG).show();
                if (position == 0)
                    height = "";
                else height = item;
            }
        });

        //initializing the level of education spinner with values
        levelOfEducationSpinner.setItems("Level Of Education", "Primary School", "Secondary School", "High School", "University","None");
        levelOfEducationSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item +" Position "+position, Snackbar.LENGTH_LONG).show();
                if (position == 0)
                    levelOfEducation = "";
                else levelOfEducation = item;
            }
        });

        //initializing the pmtctStatus spinner with values
        pmtctStatusSpinner.setItems("1", "2", "Unknown");
        pmtctStatusSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Selected PMTCT Status "+item, Snackbar.LENGTH_LONG).show();
                if (position == 0)
                    pmCtcStatus = "";
                else pmCtcStatus = item;
            }
        });

        lastChildBirthYearSpinner.setItems(2017, 2016, 2015, 2014, 2013, 2012, 2011, 2009, 2008, 2007, 2006, 2005, 2004, 2003);
        lastChildBirthYearSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                lastChildBirthYear = (Integer) item;
            }
        });

        lastChildBirthStatusSpinner.setItems("Alive", "Died");
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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verifyInputs()){
                    createNewUserObject(view);
                }
            }
        });

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

        return true;
    }

    @SuppressLint("StaticFieldLeak")
    void createNewUserObject(View view){
        AncClient ancClient = new AncClient();
        String userID = UUID.randomUUID().toString();
        ancClient.setID(userID);
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

        Call<AncClient> call = clientService.postAncClient(getAncClientBody(ancClient));
        call.enqueue(new Callback<AncClient>() {
            @Override
            public void onResponse(Call<AncClient> call, Response<AncClient> response) {

                AncClient registeredClient = response.body();
                if (registeredClient != null){
                    new AsyncTask<AncClient, Void, Void>(){
                        @Override
                        protected Void doInBackground(AncClient... ancClients) {
                            AncClient newAncClient = ancClients[0];
                            database.clientModel().addNewClient(newAncClient);
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
                    }.execute(ancClient);
                }

            }

            @Override
            public void onFailure(Call<AncClient> call, Throwable t) {

            }
        });

    }

    void setupviews(){

        //setup date picker dialogue
        dobDatePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));

        //MaterialSpinner
        gestationalAgeSpinner = findViewById(R.id.spinner);
        heightSpinner = findViewById(R.id.height_spinner);
        levelOfEducationSpinner = findViewById(R.id.level_of_education_spinner);
        pmtctStatusSpinner = findViewById(R.id.pmtct_status_spinner);
        lastChildBirthYearSpinner = findViewById(R.id.last_child_birth_year_spinner);
        lastChildBirthStatusSpinner = findViewById(R.id.last_child_birth_status_spinner);

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

        //Button
        cancelButton = findViewById(R.id.cancel);
        saveButton = findViewById(R.id.registered_client_save_button);

    }

}
