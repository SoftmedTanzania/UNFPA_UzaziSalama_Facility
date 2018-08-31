package apps.uzazisalama.com.anc.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import apps.uzazisalama.com.anc.R;
import apps.uzazisalama.com.anc.adapters.ClientListAdapter;
import apps.uzazisalama.com.anc.adapters.PncClientsListAdapter;
import apps.uzazisalama.com.anc.base.BaseActivity;
import apps.uzazisalama.com.anc.database.AncClient;
import apps.uzazisalama.com.anc.database.PncClient;
import apps.uzazisalama.com.anc.viewmodels.PncClientsViewModel;

/**
 * Created by issy on 10/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project ANC
 */

public class PncClientsListActivity extends BaseActivity implements ClientListAdapter.ClientSelectedListener {

    Toolbar pncClientsListToolbar;
    RecyclerView clientsListRecycler;

    ClientListAdapter listAdapter;
    PncClientsViewModel pncClientsViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pnc_clients_list);
        setupviews();

        pncClientsListToolbar = findViewById(R.id.pnc_list_toolbar);
        if (pncClientsListToolbar!=null){
            setSupportActionBar(pncClientsListToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        listAdapter = new ClientListAdapter(new ArrayList<>(), this, this);
        pncClientsViewModel = ViewModelProviders.of(this).get(PncClientsViewModel.class);
        pncClientsViewModel.getAllPncClients().observe(this, new Observer<List<PncClient>>() {
            @Override
            public void onChanged(@Nullable List<PncClient> pncClients) {
                PncClientsListAdapter adapter = new PncClientsListAdapter(PncClientsListActivity.this, pncClients);
                clientsListRecycler.setAdapter(adapter);
            }
        });


    }

    void setupviews(){
        clientsListRecycler = findViewById(R.id.pnc_clients_list_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        clientsListRecycler.setLayoutManager(layoutManager);
        clientsListRecycler.setHasFixedSize(true);
    }

    @Override
    public void onClientSelected(AncClient client) {
        //... Do Something
    }
}
