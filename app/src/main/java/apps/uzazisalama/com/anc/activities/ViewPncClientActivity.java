package apps.uzazisalama.com.anc.activities;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import apps.uzazisalama.com.anc.R;
import apps.uzazisalama.com.anc.base.AppDatabase;
import apps.uzazisalama.com.anc.base.BaseActivity;
import apps.uzazisalama.com.anc.database.AncClient;
import apps.uzazisalama.com.anc.database.PncClient;

/**
 * Created by issy on 20/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project UNFPA_UzaziSalama_Facility
 */

public class ViewPncClientActivity extends BaseActivity {

    //TextViews
    TextView clientNames, phone, village, age, spouseName, deliveryDate, gravida, para, deliveryMethod, condition, misscarriage, deliveryComplications;
    TextView childGender, childWeight, childApgarScore;
    Button cancelButton;

    PncClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pnc_client);
        setupviews();

        if (getIntent().getExtras() != null){
            client = (PncClient) getIntent().getSerializableExtra("currentPncClient");
            displayClientInformation(client, BaseActivity.database);
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    void displayClientInformation(PncClient client, AppDatabase database){
        new AsyncTask<PncClient, Void, Void>(){

            AncClient ancClient;
            PncClient pncClient;

            @Override
            protected Void doInBackground(PncClient... args) {
                pncClient = args[0];
                Log.d("anc_ops", "Getting client");
                List<AncClient> ancClientsList = database.clientModel().getItemById(pncClient.getAncClientID());
                if (ancClientsList.size() > 0){
                    Log.d("anc_ops", "Got a client");
                    ancClient = ancClientsList.get(0);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (ancClient!=null){
                    clientNames.setText(ancClient.getFirstName()+" "+ancClient.getMiddleName()+" "+ancClient.getSurname());
                    phone.setText(ancClient.getPhoneNumber()+"");
                    village.setText(ancClient.getVillage());
                    spouseName.setText(ancClient.getSpouseName());

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(pncClient.getDateOfDelivery());

                    deliveryDate.setText(BaseActivity.simpleDateFormat.format(calendar.getTime()));
                    gravida.setText(ancClient.getGravida()+"");
                    para.setText(ancClient.getPara()+"");

                    /**
                     *  1 -> Normal Delivery,
                     *  2 -> Vacuum,
                     *  3 -> c-section
                     */
                    String deliveryMethodString = "";
                    switch (pncClient.getDeliveryMethod()){
                        case 1:
                            deliveryMethodString = "Normal Delivery";
                            break;
                        case 2:
                            deliveryMethodString = "Vacuum";
                            break;
                        case 3:
                            deliveryMethodString = "C-section";
                            break;
                        default:
                            deliveryMethodString = "";
                    }

                    deliveryMethod.setText(deliveryMethodString);

                    String motherDischargeConditionString = "";
                    switch (pncClient.getChildDischargeCondition()){
                        case 0:
                            motherDischargeConditionString = "Bad";
                            break;
                        case 1:
                            motherDischargeConditionString = "Good";
                            break;
                        default:
                            motherDischargeConditionString = "";
                    }
                    condition.setText(motherDischargeConditionString);

                    misscarriage.setText(pncClient.isKuharibikaMimba() ? "Yes" : "No");

                    String deliveryComplicationsString = "";
                    switch (pncClient.getDeliveryComplications()){
                        case 1:
                            deliveryComplicationsString = "Postmotum Haemorrhage";
                            break;
                        case 2:
                            deliveryComplicationsString = "Third Degree Tear";
                            break;
                        case 3:
                            deliveryComplicationsString = "Retained Placenta";
                            break;
                        default:
                            deliveryComplicationsString = "";
                    }
                    deliveryComplications.setText(deliveryComplicationsString);

                    childGender.setText(pncClient.getChildGender());
                    childWeight.setText(pncClient.getChildWeight()+"");
                    childApgarScore.setText(pncClient.getApgarScore()+"");

                }
            }
        }.execute(client);
    }

    void setupviews(){

        //TextViews
        clientNames = findViewById(R.id.details_client_names);
        phone = findViewById(R.id.client_phone);
        village = findViewById(R.id.client_village);
        age = findViewById(R.id.client_age_value);
        spouseName = findViewById(R.id.client_spause_name);
        deliveryDate = findViewById(R.id.delivery_date_value);
        gravida = findViewById(R.id.client_gravida);
        para = findViewById(R.id.client_para);
        deliveryMethod = findViewById(R.id.delivery_method_value);
        condition = findViewById(R.id.mother_condition_status_value);
        misscarriage = findViewById(R.id.misscarriage_value);
        deliveryComplications = findViewById(R.id.mother_delivery_complications_value);
        childGender = findViewById(R.id.child_gender_value);
        childWeight = findViewById(R.id.child_weight_value);
        childApgarScore = findViewById(R.id.apgar_score_value);

        //Buttons
        cancelButton = findViewById(R.id.cancel_button);

    }

}
