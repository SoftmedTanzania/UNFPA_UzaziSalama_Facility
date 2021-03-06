package apps.uzazisalama.com.anc.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.graphics.drawable.ColorDrawable;
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

import java.util.Calendar;
import java.util.List;

import apps.uzazisalama.com.anc.R;
import apps.uzazisalama.com.anc.adapters.ClientsAttendingFirstVisitRecyclerAdapter;
import apps.uzazisalama.com.anc.base.AppDatabase;
import apps.uzazisalama.com.anc.database.AncClient;

import static apps.uzazisalama.com.anc.utils.Constants.LESS_THAN_TWELVE_WEEKS;

/**
 * Created by issy on 10/07/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project UNFPA_UzaziSalama_Facility
 */

public class VisitAtLessThanTwelveWeeksReport extends DialogFragment {

    private RecyclerView clientsListRecycler;
    private TextView totalClientsCount, fromDate, toDate;
    private DatePickerDialog fromDatePickerDialog = new DatePickerDialog();
    private DatePickerDialog toDatePickerDialog = new DatePickerDialog();
    private Calendar cal;
    private long dateFromInMillis, dateToInMillis;
    private boolean otherDateSelected = false;

    private AppDatabase database;

    public VisitAtLessThanTwelveWeeksReport(){}

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
        View rootView = inflater.inflate(R.layout.fragment_report_visits_less_than_twelve_weeks, container, false);
        return rootView;
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
        LiveData<List<AncClient>> clients = database.clientModel().getFirstVisitBelowTwelveWeeksClients(dateFromInMillis, dateToInMillis);
        Log.d("ThriftStore", "Client : Has Routine ");
        clients.observe(this, new Observer<List<AncClient>>() {
            @Override
            public void onChanged(@Nullable List<AncClient> ancClients) {
                int listSize = ancClients.size();
                totalClientsCount.setText(listSize+"");

                ClientsAttendingFirstVisitRecyclerAdapter adapter = new ClientsAttendingFirstVisitRecyclerAdapter(ancClients);
                clientsListRecycler.setAdapter(adapter);
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
        clientsListRecycler = view.findViewById(R.id.below_twelve_client_visits_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        clientsListRecycler.setLayoutManager(layoutManager);
        clientsListRecycler.setHasFixedSize(false);
        totalClientsCount = view.findViewById(R.id.total_clients_value);
        fromDate = view.findViewById(R.id.from_date);
        toDate = view.findViewById(R.id.to_date);
    }

}
