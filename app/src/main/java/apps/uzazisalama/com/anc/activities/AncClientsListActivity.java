package apps.uzazisalama.com.anc.activities;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import apps.uzazisalama.com.anc.R;
import apps.uzazisalama.com.anc.adapters.ClientListAdapter;
import apps.uzazisalama.com.anc.base.BaseActivity;
import apps.uzazisalama.com.anc.customviews.MyDividerItemDecoration;
import apps.uzazisalama.com.anc.database.AncClient;
import apps.uzazisalama.com.anc.viewmodels.AncClientsViewModel;

/**
 * Created by issy on 10/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project ANC
 */

public class AncClientsListActivity extends BaseActivity implements ClientListAdapter.ClientSelectedListener {

    final String TAG = "AncClientsListActivity";

    Toolbar ancToolbar;
    RecyclerView clientsListRecycler;

    ClientListAdapter listAdapter;
    AncClientsViewModel ancClientsViewModel;
    private SearchView searchView;

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

        // white background notification bar
        whiteNotificationBar(clientsListRecycler);

        //create and initialize clients list recyclerview
        listAdapter = new ClientListAdapter(new ArrayList<AncClient>(), this, this);
        clientsListRecycler.setAdapter(listAdapter);

        //creting the viewmodel observer for the client object
        ancClientsViewModel = ViewModelProviders.of(this).get(AncClientsViewModel.class);
        Log.d(TAG, "Initialized the observer");
        ancClientsViewModel.getAllClientsList().observe(this, new Observer<List<AncClient>>() {
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
        clientsListRecycler.setItemAnimator(new DefaultItemAnimator());
        clientsListRecycler.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.client_list_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                Log.d("filter", "OnQueryTextSubmit  "+query);
                listAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                Log.d("filter", "OnQuertTextChange  "+query);
                listAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //Close searchView on backPressed
        if (!searchView.isIconified()){
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @Override
    public void onClientSelected(AncClient client) {
        //This is the selected Client
    }
}
