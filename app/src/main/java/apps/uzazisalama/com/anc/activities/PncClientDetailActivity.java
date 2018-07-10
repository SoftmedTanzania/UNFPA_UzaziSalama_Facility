package apps.uzazisalama.com.anc.activities;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import apps.uzazisalama.com.anc.R;
import apps.uzazisalama.com.anc.base.AppDatabase;
import apps.uzazisalama.com.anc.base.BaseActivity;
import apps.uzazisalama.com.anc.database.AncClient;
import apps.uzazisalama.com.anc.database.PncClient;

/**
 * Created by issy on 18/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project ANC
 */

public class PncClientDetailActivity extends BaseActivity {

    TextView clientNames, clientPhone, clientVillage, clientAge, clientSpauseName, clientGravida, clientPara;
    EditText dateOfAdmissionEt, dateOfDeliveryEt, childWeightEt, apgarScoreEt;
    MaterialSpinner missCarriageSpinner, deliveryMethodSpinner, deliveryComplicationsSpinner, motherDischargeConditionSpinner, childGenderSpinner;
    Button savePncData;

    public DatePickerDialog doaDatePicker = new DatePickerDialog();
    public DatePickerDialog dodDatePicker = new DatePickerDialog();
    Calendar calendar;
    final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    //Values
    long dateOfAdmissionValue = 0;
    long dateOfDeliveryValue = 0;
    String admissionDateDisplay, deliveryDateDisplay, childGenderValue;
    boolean misscarriageValue;
    int deliveryMethodValue, deliveryComplicationsValue, motherDischargedConditionValue;

    AncClient currentAncClient;
    PncClient createdPncClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pnc_details);
        setupviews();

        if (getIntent().getExtras() != null){
            currentAncClient = (AncClient) getIntent().getSerializableExtra("currentAncClient");
            displayClientInformation(currentAncClient);
        }

        dateOfDeliveryEt.setFocusableInTouchMode(false);
        dateOfAdmissionEt.setFocusableInTouchMode(false);

        dateOfAdmissionEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doaDatePicker.show(getFragmentManager(),"dateOfBirth");
                doaDatePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        dateOfAdmissionValue = calendar.getTimeInMillis();
                        admissionDateDisplay = simpleDateFormat.format(calendar.getTime());
                        dateOfAdmissionEt.setText(admissionDateDisplay);
                    }

                });
            }
        });

        dateOfDeliveryEt.setFocusableInTouchMode(false);
        dateOfDeliveryEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dodDatePicker.show(getFragmentManager(),"dateOfBirth");
                dodDatePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        dateOfDeliveryValue = calendar.getTimeInMillis();
                        deliveryDateDisplay = simpleDateFormat.format(calendar.getTime());
                        dateOfDeliveryEt.setText(deliveryDateDisplay);
                    }

                });
            }
        });

        missCarriageSpinner.setItems("Yes", "No");
        missCarriageSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                misscarriageValue = position == 0;
            }
        });

        /**
         *  1 -> Normal Delivery,
         *  2 -> Vacuum,
         *  3 -> c-section
         */
        deliveryMethodSpinner.setItems("Normal Delivery", "Vacuum", "C-section");
        deliveryMethodSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                switch (position){
                    case 0:
                        deliveryMethodValue = 1;
                        break;
                    case 1:
                        deliveryMethodValue = 2;
                        break;
                    case 2:
                        deliveryMethodValue = 3;
                        break;
                    default:
                        deliveryMethodValue = 0;
                }
            }
        });

        /**
         *  1 -> Postmotum Haemorrhage,
         *  2 -> Third degree tear ,
         *  3 -> Retained Placenta
         */
        deliveryComplicationsSpinner.setItems("Postmotum Haemorrhage", "Third degree tear", "Retained Placenta");
        deliveryComplicationsSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                switch (position){
                    case 0:
                        deliveryComplicationsValue = 1;
                        break;
                    case 1:
                        deliveryComplicationsValue = 2;
                        break;
                    case 2:
                        deliveryComplicationsValue = 3;
                        break;
                    default:
                        deliveryComplicationsValue = 0;
                }
            }
        });

        /**
         *  0 -> Bad
         *  1 -> Good
         */
        motherDischargeConditionSpinner.setItems("Good", "Bad");
        motherDischargeConditionSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (position == 0){
                    motherDischargedConditionValue = 1; //1 => Good
                }else {
                    motherDischargedConditionValue = 0; //2 => Bad
                }
            }
        });

        childGenderSpinner.setItems("Male", "Female");
        childGenderSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                childGenderValue = (String) item;
                Log.d("selectedGender", childGenderValue);
            }
        });

        savePncData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (createPncClientObject()){
                    if (createdPncClient != null){
                        savePncClientObject(createdPncClient);
                    }else {
                        toastMessage("Error Creating Client Object");
                    }
                }
            }
        });

    }

    boolean createPncClientObject(){

        createdPncClient = new PncClient();
        createdPncClient.setHealthFacilityClientID(currentAncClient.getHealthFacilityClientId());
        createdPncClient.setPncClientID(UUID.randomUUID().toString());
        if (dateOfAdmissionValue == 0){
            Toast.makeText(this, "Fill in the date of Admission", Toast.LENGTH_LONG).show();
            return false;
        }else {
            createdPncClient.setDateOfAdmission(dateOfAdmissionValue);
        }

        if (dateOfDeliveryValue == 0){
            Toast.makeText(this, "Fill in the date of Admission", Toast.LENGTH_LONG).show();
            return false;
        }else {
            createdPncClient.setDateOfDelivery(dateOfDeliveryValue);
        }

        createdPncClient.setKuharibikaMimba(misscarriageValue);
        createdPncClient.setDeliveryMethod(deliveryMethodValue);
        createdPncClient.setDeliveryComplications(deliveryComplicationsValue);
        createdPncClient.setMotherDischargeCondition(motherDischargedConditionValue);
        createdPncClient.setChildGender(childGenderValue);
        createdPncClient.setChildWeight(Double.parseDouble(childWeightEt.getText().toString()));
        createdPncClient.setApgarScore(Integer.parseInt(apgarScoreEt.getText().toString()));

        return true;

    }

    @SuppressLint("StaticFieldLeak")
    void savePncClientObject(PncClient client){
        new AsyncTask<Void, Void, Void>(){

            AppDatabase db = database;

            @Override
            protected Void doInBackground(Void... voids) {
                //Saving the newly created PNC client to the database
                db.pncClientModelDao().addNewClient(client);

                //Updating the ANC client type to 2 = PNC Client
                currentAncClient.setClientType(2);
                db.clientModel().updateClient(currentAncClient);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                toastMessage("Client Created Successfully");
                finish();
            }
        }.execute();
    }

    void setupviews(){

        //TextViews
        clientNames = findViewById(R.id.details_client_names);
        clientPhone = findViewById(R.id.client_phone);
        clientVillage = findViewById(R.id.client_village);
        clientAge = findViewById(R.id.client_age);
        clientSpauseName = findViewById(R.id.client_spause_name);
        clientGravida = findViewById(R.id.client_gravida);
        clientPara = findViewById(R.id.client_para);

        //EditTexts
        dateOfAdmissionEt = findViewById(R.id.date_of_admission);
        dateOfDeliveryEt = findViewById(R.id.date_of_delivery);
        childWeightEt = findViewById(R.id.child_weight);
        apgarScoreEt = findViewById(R.id.apgar_score);

        //Material Spinners
        missCarriageSpinner = findViewById(R.id.misscarriage_spinner);
        deliveryMethodSpinner = findViewById(R.id.delivery_method_spinner);
        deliveryComplicationsSpinner = findViewById(R.id.delivery_complications_spinner);
        motherDischargeConditionSpinner = findViewById(R.id.mother_discharge_condition_spinner);
        childGenderSpinner = findViewById(R.id.child_gender_spinner);

        //Buttons
        savePncData = findViewById(R.id.save_pnc_data_button);


    }

    void displayClientInformation(AncClient ancClient){

        clientNames.setText(ancClient.getFirstName()+" "+ancClient.getMiddleName()+" "+ancClient.getSurname());
        clientPhone.setText(ancClient.getPhoneNumber());
        clientVillage.setText(ancClient.getVillage());
        clientAge.setText("");
        clientSpauseName.setText(ancClient.getSpouseName());
        clientGravida.setText(ancClient.getGravida()+"");
        clientPara.setText(ancClient.getPara()+"");

    }

    void toastMessage(String mess){
        Toast.makeText(this, mess, Toast.LENGTH_LONG).show();
    }

}
