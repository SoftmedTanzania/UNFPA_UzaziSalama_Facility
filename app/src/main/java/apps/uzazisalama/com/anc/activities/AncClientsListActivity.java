package apps.uzazisalama.com.anc.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

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

public class AncClientsListActivity extends BaseActivity {

    final String TAG = "AncClientsListActivity";

    Toolbar ancToolbar;
    RecyclerView clientsListRecycler;

    ClientListAdapter listAdapter;
    ClientsViewModel clientsViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anc_clients_list);
        setupviews();

        ancToolbar = findViewById(R.id.anc_list_toolbar);
        if (ancToolbar!=null){
            setSupportActionBar(ancToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        //create and initialize clients list recyclerview
        listAdapter = new ClientListAdapter(new ArrayList<AncClient>(), this);
        clientsListRecycler.setAdapter(listAdapter);

        //creting the viewmodel observer for the client object
        clientsViewModel = ViewModelProviders.of(this).get(ClientsViewModel.class);
        Log.d(TAG, "Initialized the observer");
        clientsViewModel.getAllClientsList().observe(this, new Observer<List<AncClient>>() {
            @Override
            public void onChanged(@Nullable List<AncClient> ancClients) {
                listAdapter.addItems(ancClients);
                listAdapter.notifyDataSetChanged();
            }
        });

    }

    void setupviews(){
        clientsListRecycler = findViewById(R.id.clients_list_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        clientsListRecycler.setLayoutManager(layoutManager);
        clientsListRecycler.setHasFixedSize(true);
    }
}
