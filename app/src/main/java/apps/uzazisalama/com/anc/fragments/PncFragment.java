package apps.uzazisalama.com.anc.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import apps.uzazisalama.com.anc.R;
import apps.uzazisalama.com.anc.activities.PncClientsListActivity;

/**
 * Created by issy on 09/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project ANC
 */

public class PncFragment extends Fragment {

    ImageView clientsListIcon;
    CardView pncClientsListCard;

    public PncFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pnc, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupviews(view);

        //Set a high resolution icon to card-icon using glide to optimise phone memory
        Glide.with(view.getContext()).load(R.drawable.ic_list).into(clientsListIcon);

        //Start pnc clients list activity upon clicking of the card
        pncClientsListCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PncFragment.this.getActivity(), PncClientsListActivity.class));
            }
        });

    }

    void setupviews(View v){
        clientsListIcon = v.findViewById(R.id.pnc_clients_list);
        pncClientsListCard = v.findViewById(R.id.pnc_clients_list_card);
    }

}
