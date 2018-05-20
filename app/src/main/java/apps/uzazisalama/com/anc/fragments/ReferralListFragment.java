package apps.uzazisalama.com.anc.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import apps.uzazisalama.com.anc.R;
import apps.uzazisalama.com.anc.adapters.ReferralListAdapter;
import apps.uzazisalama.com.anc.database.Referral;
import apps.uzazisalama.com.anc.viewmodels.ReferralViewModel;

/**
 * Created by issy on 20/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project UNFPA_UzaziSalama_Facility
 */

public class ReferralListFragment extends Fragment {

    RecyclerView referralsRecycler;
    TextView title, newCount;

    ReferralListAdapter adapter;
    ReferralViewModel referralViewModel;

    int referralSource;

    /**
     *
     * @param referralSource of the referral either CHW or Other Facility
     * @return Fragment instance
     */
    public static ReferralListFragment newInstance(int referralSource) {
        Bundle args = new Bundle();
        ReferralListFragment fragment = new ReferralListFragment();
        args.putInt("source", referralSource);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_referral_list, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupviews(view);

        referralSource = getArguments().getInt("source");

        referralViewModel = ViewModelProviders.of(this).get(ReferralViewModel.class);
        if (referralSource == 1){ //Chw Referrals
            title.setText("CHW referrals");
            referralViewModel.getChwReferrals().observe(this, new Observer<List<Referral>>() {
                @Override
                public void onChanged(@Nullable List<Referral> referrals) {
                    adapter = new ReferralListAdapter(ReferralListFragment.this.getContext(), referrals);
                    referralsRecycler.setAdapter(adapter);
                }
            });
        }else if (referralSource == 2){ //Health Facility Referrals
            title.setText("Health Facility referrals");
            referralViewModel.getHealthFacilityReferrals().observe(this, new Observer<List<Referral>>() {
                @Override
                public void onChanged(@Nullable List<Referral> referrals) {
                    adapter = new ReferralListAdapter(ReferralListFragment.this.getContext(), referrals);
                    referralsRecycler.setAdapter(adapter);
                }
            });
        }

    }

    void setupviews(View view){
        referralsRecycler = view.findViewById(R.id.referral_list_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ReferralListFragment.this.getActivity());
        referralsRecycler.setLayoutManager(layoutManager);
        referralsRecycler.setHasFixedSize(true);

        title = view.findViewById(R.id.summary_title);
        newCount = view.findViewById(R.id.new_referrals_count);

    }


}
