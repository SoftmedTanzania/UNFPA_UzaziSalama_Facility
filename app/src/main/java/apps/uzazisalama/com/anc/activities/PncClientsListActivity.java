package apps.uzazisalama.com.anc.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import apps.uzazisalama.com.anc.R;
import apps.uzazisalama.com.anc.adapters.ClientListAdapter;
import apps.uzazisalama.com.anc.base.BaseActivity;
import apps.uzazisalama.com.anc.database.AncClient;
import apps.uzazisalama.com.anc.viewmodels.ClientsViewModel;

/**
 * Created by issy on 10/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project ANC
 */

public class PncClientsListActivity extends BaseActivity{

    Toolbar pncClientsListToolbar;
    RecyclerView clientsListRecycler;

    ClientListAdapter listAdapter;
    ClientsViewModel clientsViewModel;

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

        listAdapter = new ClientListAdapter(new ArrayList<>(), this);
        clientsViewModel = ViewModelProviders.of(this).get(ClientsViewModel.class);
        clientsViewModel.getAllClientsList().observe(this, new Observer<List<AncClient>>() {
            @Override
            public void onChanged(@Nullable List<AncClient> ancClients) {
                listAdapter.addItems(ancClients);
            }
        });

        clientsListRecycler.setAdapter(listAdapter);

    }

    void setupviews(){
        clientsListRecycler = findViewById(R.id.clients_list_recycler);
    }
}
