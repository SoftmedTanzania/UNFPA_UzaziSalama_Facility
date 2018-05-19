package apps.uzazisalama.com.anc.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import apps.uzazisalama.com.anc.R;
import apps.uzazisalama.com.anc.activities.AncClientsListActivity;
import apps.uzazisalama.com.anc.activities.ClientRegisterActivity;
import apps.uzazisalama.com.anc.activities.ReferralListActivity;

/**
 * Created by issy on 09/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project ANC
 */

public class AncFragment extends Fragment {

    ImageView registrationIcon, referralIcon, clientListIcon;
    CardView referralsCard, clientsListCard, registerClientsCard;

    public AncFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_anc, container, false);
        setupviews(rootview);

        //Set high res cards icon source using glide
        Glide.with(container.getContext()).load(R.drawable.ic_reg_icon).into(registrationIcon);
        Glide.with(container.getContext()).load(R.drawable.ic_referral).into(referralIcon);
        Glide.with(container.getContext()).load(R.drawable.ic_list).into(clientListIcon);

        //Handle cards on click events
        referralsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AncFragment.this.getActivity(), ReferralListActivity.class));
            }
        });

        clientsListCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AncFragment.this.getActivity(), AncClientsListActivity.class));
            }
        });


        registerClientsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AncFragment.this.getActivity(), ClientRegisterActivity.class));
            }
        });

        return rootview;
    }

    void setupviews(View v){
        registrationIcon = v.findViewById(R.id.registration_icon);
        referralIcon = v.findViewById(R.id.referral_icon);
        clientListIcon = v.findViewById(R.id.client_list_icon);

        referralsCard = v.findViewById(R.id.referal_list_card);
        clientsListCard = v.findViewById(R.id.anc_clients_list_card);
        registerClientsCard = v.findViewById(R.id.register_clients_card);

    }

}
