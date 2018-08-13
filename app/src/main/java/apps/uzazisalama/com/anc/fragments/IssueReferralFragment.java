package apps.uzazisalama.com.anc.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import apps.uzazisalama.com.anc.R;
import apps.uzazisalama.com.anc.api.Endpoints;
import apps.uzazisalama.com.anc.base.AppDatabase;
import apps.uzazisalama.com.anc.base.BaseActivity;
import apps.uzazisalama.com.anc.database.AncClient;
import apps.uzazisalama.com.anc.database.PostBox;
import apps.uzazisalama.com.anc.database.Referral;
import apps.uzazisalama.com.anc.utils.ServiceGenerator;
import apps.uzazisalama.com.anc.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static apps.uzazisalama.com.anc.utils.Constants.POST_BOX_DATA_REFERRAL;
import static apps.uzazisalama.com.anc.utils.Constants.POST_DATA_UNSYNCED;

public class IssueReferralFragment extends DialogFragment{

    private MaterialSpinner servicesSpinner;
    private TextView clientName, sendText;
    private RelativeLayout saveButton, cancelButton;
    private CircularProgressView progressView;
    private EditText referralReasonsEt;

    private AncClient mClient;
    private Referral mReferral;

    private static final String CURRENT_CLIENT = "client_client";

    public SessionManager session;
    private Endpoints.ReferralService referralService;

    public static IssueReferralFragment newInstance(AncClient client){
        IssueReferralFragment fragment = new IssueReferralFragment();
        Bundle args = new Bundle();
        args.putSerializable(CURRENT_CLIENT, client);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClient = (AncClient) getArguments().getSerializable(CURRENT_CLIENT);
        session = new SessionManager(IssueReferralFragment.this.getActivity().getApplicationContext());

        referralService = ServiceGenerator.createService(Endpoints.ReferralService.class,
                session.getUserName(),
                session.getUserPass(),
                session.getKeyHfid());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_issue_referral, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onViewCreated(view, savedInstanceState);
        setUpViews(view);

        if (mClient != null){
            clientName.setText(mClient.getFirstName()+" "+mClient.getMiddleName()+" "+mClient.getSurname());
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendText.setVisibility(View.INVISIBLE);
                progressView.setVisibility(View.VISIBLE);

                if (createReferralObject()){
                    sendReferral(mReferral);
                }
            }
        });

    }

    private boolean createReferralObject(){

        boolean created = true;

        Referral referral = new Referral();

        long range = 1234567L;
        Random r = new Random();
        int number = (int)(r.nextDouble()*range);
        referral.setReferralID(number);

        referral.setAncClientID(String.valueOf(mClient.getHealthFacilityClientId()));
        referral.setHealthFacilityClientID(mClient.getHealthFacilityClientId());
        referral.setFacilityID(session.getKeyHfid());
        referral.setReferralUUID(UUID.randomUUID().toString());
        if (referralReasonsEt.getText().toString().isEmpty()){
            created = false;
        }
        referral.setReferralReason(referralReasonsEt.getText().toString());
        referral.setReferralDate(Calendar.getInstance().getTimeInMillis());
        referral.setServiceProviderUUID(session.getServiceProviderUUID());
        referral.setReferralType(3);
        referral.setReferralStatus(0);

        mReferral = referral;

        /*
        Go on and create the referral object here
         */

        return true;
    }

    private void sendReferral(Referral referral){
        if (isNetworkAvailable()){
            sendReferralToServer(referral);
        }else {
            saveReferralLocally(referral);
        }
    }

    private void setUpViews(View v){
        servicesSpinner = v.findViewById(R.id.services_indicator_spinner);
        clientName = v.findViewById(R.id.client_name);
        saveButton = v.findViewById(R.id.btn_send);
        cancelButton = v.findViewById(R.id.btn_cancel);
        progressView = v.findViewById(R.id.progress_view);
        sendText = v.findViewById(R.id.send_text);
        referralReasonsEt = v.findViewById(R.id.referral_reason_et);
    }

    private void sendReferralToServer(Referral referral){
        Call<Referral> sendReferral = referralService.saveFacilityReferral(BaseActivity.getRequestBody(referral));
        sendReferral.enqueue(new Callback<Referral>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<Referral> call, Response<Referral> response) {
                if (response != null){

                    Referral referralRespose = response.body();

                    if (referralRespose != null){
                        /**
                         * Save the received referral locally
                         */
                        new AsyncTask<Referral, Void, Void>(){

                            AppDatabase database = BaseActivity.database;

                            @Override
                            protected Void doInBackground(Referral... referrals) {
                                database.referralModelDao().addNewReferral(referrals[0]);
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                                Toast.makeText(IssueReferralFragment.this.getActivity(), "Referral Sent Successfully", Toast.LENGTH_LONG).show();
                                dismiss();
                            }
                        }.execute(referralRespose);
                    }else {
                        Toast.makeText(IssueReferralFragment.this.getActivity(), "Error! try again later", Toast.LENGTH_LONG).show();
                        sendText.setVisibility(View.VISIBLE);
                        progressView.setVisibility(View.INVISIBLE);
                    }

                    Log.d("Monday", "Referral Response : "+ new Gson().toJson(response.body()));
                }else {
                    Log.d("Monday", "response is null");
                }
            }

            @Override
            public void onFailure(Call<Referral> call, Throwable t) {
                Log.d("Monday", "Failed!");
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void saveReferralLocally(Referral referral){
        new AsyncTask<Referral, Void, Void>(){

            AppDatabase database = BaseActivity.database;

            @Override
            protected Void doInBackground(Referral... referrals) {
                Referral mReferral = referrals[0];
                database.referralModelDao().addNewReferral(mReferral);

                /**
                 * Create a postBox entry of the referral
                 */
                PostBox postBoxEnnty = new PostBox();
                postBoxEnnty.setPostBoxId(String.valueOf(mReferral.getReferralID()));
                postBoxEnnty.setSyncStatus(POST_DATA_UNSYNCED);
                postBoxEnnty.setPostDataType(POST_BOX_DATA_REFERRAL);

                database.postBoxModelDao().AddNewPost(postBoxEnnty);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(IssueReferralFragment.this.getActivity(), "Referral Saved locally, Data sync required", Toast.LENGTH_LONG).show();
                dismiss();
            }
        }.execute(referral);
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) IssueReferralFragment.this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
