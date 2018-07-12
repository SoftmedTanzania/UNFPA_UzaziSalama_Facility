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
import java.util.Calendar;
import java.util.List;

import apps.uzazisalama.com.anc.R;
import apps.uzazisalama.com.anc.adapters.ClientsAttendingFirstVisitRecyclerAdapter;
import apps.uzazisalama.com.anc.base.AppDatabase;
import apps.uzazisalama.com.anc.database.AncClient;
import apps.uzazisalama.com.anc.database.RoutineVisits;

/**
 * Created by issy on 12/07/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project UNFPA_UzaziSalama_Facility
 */

public class WomenAttendingFourOrMoreVisitsReport extends DialogFragment {

    private AppDatabase database;
    private RecyclerView clientListRecycler;
    private TextView totalClientsValue, fromDate, toDate;

    private DatePickerDialog fromDatePickerDialog = new DatePickerDialog();
    private DatePickerDialog toDatePickerDialog = new DatePickerDialog();
    private Calendar cal;
    private long dateFromInMillis, dateToInMillis;
    private boolean otherDateSelected = false;

    public WomenAttendingFourOrMoreVisitsReport(){}

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
        return inflater.inflate(R.layout.fragment_report_more_than_four_visits, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews(view);

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null){
                    fromDatePickerDialog.show(getActivity().getFragmentManager(),"fromDateRange");
                }
                fromDatePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        fromDate.setText((dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "-" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : monthOfYear + 1) + "-" + year);
                        cal = Calendar.getInstance();
                        cal.set(year, monthOfYear, dayOfMonth);
                        dateFromInMillis = cal.getTimeInMillis();

                        if (otherDateSelected){
                            getReportData();
                        }else {
                            otherDateSelected = true;
                        }

                    }

                });
            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null){
                    toDatePickerDialog.show(getActivity().getFragmentManager(),"fromDateRange");
                }
                toDatePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        toDate.setText((dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "-" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : monthOfYear + 1) + "-" + year);
                        cal = Calendar.getInstance();
                        cal.set(year, monthOfYear, dayOfMonth);
                        dateToInMillis = cal.getTimeInMillis();

                        if (otherDateSelected){
                            getReportData();
                        }else {
                            otherDateSelected = true;
                        }
                    }

                });
            }
        });

    }

    private void getReportData(){
        LiveData<List<AncClient>> clients = database.clientModel().getAllAncClients();
        clients.observe(this, new Observer<List<AncClient>>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onChanged(@Nullable List<AncClient> ancClients) {

                new AsyncTask<Void, Void, Void>(){

                    List<AncClient> fourOrMoreVisitsClients = new ArrayList<>();
                    @Override
                    protected Void doInBackground(Void... voids) {
                        for (AncClient client : ancClients){
                            List<RoutineVisits> routines = database.routineModelDao().getVisitFourAndAboveClientRoutines(client.getHealthFacilityClientId(), dateFromInMillis, dateToInMillis);
                            if (routines.size() > 0){
                                fourOrMoreVisitsClients.add(client);
                                Log.d("ThriftStore", "Client : Added!");
                            }
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        int listSize = fourOrMoreVisitsClients.size();
                        totalClientsValue.setText(listSize+"");
                        ClientsAttendingFirstVisitRecyclerAdapter adapter = new ClientsAttendingFirstVisitRecyclerAdapter(fourOrMoreVisitsClients);
                        clientListRecycler.setAdapter(adapter);
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
        fromDate = view.findViewById(R.id.from_date);
        toDate = view.findViewById(R.id.to_date);
        clientListRecycler = view.findViewById(R.id.four_or_more_visits_clients_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        clientListRecycler.setLayoutManager(layoutManager);
        clientListRecycler.setHasFixedSize(false);
    }

}
