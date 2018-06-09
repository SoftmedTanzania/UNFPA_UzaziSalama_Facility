package apps.uzazisalama.com.anc.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import apps.uzazisalama.com.anc.R;
import apps.uzazisalama.com.anc.api.Endpoints;
import apps.uzazisalama.com.anc.base.BaseActivity;
import apps.uzazisalama.com.anc.database.AncClient;
import apps.uzazisalama.com.anc.database.Referral;
import apps.uzazisalama.com.anc.utils.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by issy on 20/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project UNFPA_UzaziSalama_Facility
 */

public class ReferralDetailsView extends BaseActivity {

    Toolbar toolbar;
    TextView names, phone, village, age, spauseName, gravida, para;
    TextView referralDate, referralReasons, otherClinicalInformation;
    Button enrollClient, forwardReferral;

    Referral currentReferral;
    /**
     * ClientZero is a general client with client type 0 that have not been enrolled to ANC or PNC clinics
     * This is the default client type when a new referral is received
     */
    AncClient clientZero;
    Endpoints.ReferralService referralService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.referral_details_view);
        setupview();

        if (toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        if (getIntent().getExtras() != null){
            currentReferral = (Referral) getIntent().getSerializableExtra("currentReferral");
            displayInformation(currentReferral);
        }

        referralService = ServiceGenerator.createService(Endpoints.ReferralService.class,
                session.getUserName(),
                session.getUserPass(),
                session.getKeyHfid());

        enrollClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentReferral.setReferralFeedback("Client has been enrolled to the clinic");
                currentReferral.setReferralStatus(1);

                Call<String> call = referralService.postReferralFeedback(getReferralFeedbackBody(currentReferral));
                call.enqueue(new Callback<String>() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response != null){
                            new AsyncTask<Void, Void, Void>(){
                                @Override
                                protected Void doInBackground(Void... voids) {
                                    BaseActivity.database.referralModelDao().updateReferral(currentReferral);
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    super.onPostExecute(aVoid);
                                    Intent intent = new Intent(ReferralDetailsView.this, ClientRegisterActivity.class);
                                    intent.putExtra("clientZero", clientZero);
                                    startActivity(intent);
                                }
                            }.execute();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });

    }

    void setupview(){
        toolbar = findViewById(R.id.referral_toolbar);
        names = findViewById(R.id.details_client_names);
        phone = findViewById(R.id.client_phone_value);
        village = findViewById(R.id.client_village_value);
        age = findViewById(R.id.client_age);
        spauseName = findViewById(R.id.client_spause_name_value);
        gravida = findViewById(R.id.client_gravida);
        para = findViewById(R.id.client_para);

        referralDate = findViewById(R.id.referral_date_value);
        referralReasons = findViewById(R.id.referral_reasons_value);
        otherClinicalInformation = findViewById(R.id.other_clinical_information_value);

        enrollClient = findViewById(R.id.referral_enroll_button);

    }

    @SuppressLint("StaticFieldLeak")
    void displayInformation(Referral referral){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(referral.getReferralDate());

        referralDate.setText(simpleDateFormat.format(calendar.getTime()));
        referralReasons.setText(referral.getReferralReason());
        otherClinicalInformation.setText(referral.getOtherClinicalInformation());

        new AsyncTask<Referral, Void, Void>(){

            AncClient client;

            @Override
            protected Void doInBackground(Referral... referrals) {
                List<AncClient> list = BaseActivity.database.clientModel().getItemById(referrals[0].getHealthFacilityClientID());
                if (list.size() > 0){
                    client = list.get(0);
                    clientZero = client;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                names.setText(client.getFirstName()+" "+client.getMiddleName()+" "+client.getSurname());
                phone.setText(client.getPhoneNumber());
                village.setText(client.getVillage());
                age.setText("");
                spauseName.setText(client.getSpouseName());
                gravida.setText(client.getGravida()+"");
                para.setText(client.getPara()+"");

            }
        }.execute(referral);
    }

}
