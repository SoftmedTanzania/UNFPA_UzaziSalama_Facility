package apps.uzazisalama.com.anc.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import apps.uzazisalama.com.anc.R;
import apps.uzazisalama.com.anc.database.AncClient;
import apps.uzazisalama.com.anc.database.Referral;

public class IssueReferralFragment extends DialogFragment{

    private MaterialSpinner servicesSpinner;
    private TextView clientName, sendText;
    private RelativeLayout saveButton, cancelButton;
    private CircularProgressView progressView;

    private AncClient mClient;
    private Referral mReferral;
    private static final String CURRENT_CLIENT = "client_client";

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

        List<String> servicesList = new ArrayList<>();
        servicesList.add("Family Planning");
        servicesList.add("Malaria");
        servicesList.add("Tuberculosis");
        servicesSpinner.setItems(servicesList);
        servicesSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

            }
        });

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

                /*if (createReferralObject()){
                    sendReferral(mReferral);
                }*/
            }
        });

    }

    private boolean createReferralObject(){
        Referral referral;
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
    }

    private void sendReferralToServer(Referral referral){

    }

    private void saveReferralLocally(Referral referral){

    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) IssueReferralFragment.this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
