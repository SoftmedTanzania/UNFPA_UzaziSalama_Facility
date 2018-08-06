package apps.uzazisalama.com.anc.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
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
import apps.uzazisalama.com.anc.database.ReferralDetailsBinder;
import apps.uzazisalama.com.anc.databinding.ReferralDetailsViewBinding;
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

    ReferralDetailsViewBinding binding;
    private ReferralDetailsBinder data = new ReferralDetailsBinder();

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.referral_details_view);

        setupview();

        if (toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        if (getIntent().getExtras() != null){
            currentReferral = (Referral) getIntent().getSerializableExtra("currentReferral");
            data.setReferral(currentReferral);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentReferral.getReferralDate());
        String referralDate = simpleDateFormat.format(calendar.getTime());

        data.setReferralDate(referralDate);

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
                Calendar now = Calendar.getInstance();
                Calendar dob = Calendar.getInstance();
                dob.setTimeInMillis(client.getDateOfBirth());

                int year1 = now.get(Calendar.YEAR);
                int year2 = dob.get(Calendar.YEAR);
                int age = year1 - year2;

                int month1 = now.get(Calendar.MONTH);
                int month2 = dob.get(Calendar.MONTH);
                if (month2 > month1) {
                    age--;
                } else if (month1 == month2) {
                    int day1 = now.get(Calendar.DAY_OF_MONTH);
                    int day2 = dob.get(Calendar.DAY_OF_MONTH);
                    if (day2 > day1) {
                        age--;
                    }
                }

                data.setClient(client);
                data.setAge(age+"");
                binding.setData(data);
            }
        }.execute(currentReferral);

        referralService = ServiceGenerator.createService(Endpoints.ReferralService.class,
                session.getUserName(),
                session.getUserPass(),
                session.getKeyHfid());

        enrollClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentReferral.setReferralFeedback("Client has been enrolled to the clinic");
                currentReferral.setReferralStatus(1);

                Call<String> call = referralService.postReferralFeedback(getRequestBody(currentReferral));
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
        enrollClient = findViewById(R.id.referral_enroll_button);
    }

    @SuppressLint("StaticFieldLeak")
    void displayInformation(Referral referral){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(referral.getReferralDate());

        referralDate.setText(simpleDateFormat.format(calendar.getTime()));
        referralReasons.setText(referral.getReferralReason());
        otherClinicalInformation.setText(referral.getOtherClinicalInformation());
    }

}
