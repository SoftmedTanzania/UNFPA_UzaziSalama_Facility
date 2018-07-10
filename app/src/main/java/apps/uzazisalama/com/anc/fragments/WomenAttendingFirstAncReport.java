package apps.uzazisalama.com.anc.fragments;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.List;

import apps.uzazisalama.com.anc.R;
import apps.uzazisalama.com.anc.adapters.ClientsAttendingFirstVisitRecyclerAdapter;
import apps.uzazisalama.com.anc.base.AppDatabase;
import apps.uzazisalama.com.anc.database.AncClient;
import apps.uzazisalama.com.anc.database.RoutineVisits;

/**
 * Created by issy on 10/07/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project UNFPA_UzaziSalama_Facility
 */

public class WomenAttendingFirstAncReport extends DialogFragment {

    private TextView totalClientsValue;
    private RecyclerView clientsListRecycler;

    private AppDatabase database;

    public WomenAttendingFirstAncReport(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setStyle(STYLE_NO_TITLE, R.style.dialog);
        super.onCreate(savedInstanceState);

        database = AppDatabase.getDatabase(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View rootView = inflater.inflate(R.layout.fragment_report_women_attending_first_anc, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews(view);

        LiveData<List<AncClient>> clients = database.clientModel().getAllAncClients();
        clients.observe(this, new Observer<List<AncClient>>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onChanged(@Nullable List<AncClient> ancClients) {


                new AsyncTask<Void, Void, Void>(){

                    List<AncClient> firstVisitClients = new ArrayList<>();
                    @Override
                    protected Void doInBackground(Void... voids) {

                        for (AncClient client : ancClients){
                            Log.d("ThriftStore", "Client : "+client.getFirstName());
                            List<RoutineVisits> routines = database.routineModelDao().getClientRoutines(client.getHealthFacilityClientId());
                            boolean firstVisitRoutineOnly = true;
                            boolean hasRoutines = false;
                            for (RoutineVisits visits : routines){
                                hasRoutines = true;
                                Log.d("ThriftStore", "Client : Has Routine "+visits.getVisitNumber());
                                if (visits.getVisitNumber() > 1){
                                    firstVisitRoutineOnly = false;
                                    Log.d("ThriftStore", "Client : Routine "+visits.getVisitNumber());
                                }
                            }
                            if (hasRoutines && firstVisitRoutineOnly){
                                firstVisitClients.add(client);
                                Log.d("ThriftStore", "Client : Added!");
                            }
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);

                        int listSize = firstVisitClients.size();
                        totalClientsValue.setText(listSize+"");

                        ClientsAttendingFirstVisitRecyclerAdapter adapter = new ClientsAttendingFirstVisitRecyclerAdapter(firstVisitClients);
                        clientsListRecycler.setAdapter(adapter);

                    }

                }.execute();

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.80);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.80);
        getDialog().getWindow().setLayout(width, height);
    }

    private void setupViews(View view){
        totalClientsValue = view.findViewById(R.id.total_clients_value);
        clientsListRecycler = view.findViewById(R.id.first_anc_visit_clients_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        clientsListRecycler.setLayoutManager(layoutManager);
        clientsListRecycler.setHasFixedSize(false);
    }

}
