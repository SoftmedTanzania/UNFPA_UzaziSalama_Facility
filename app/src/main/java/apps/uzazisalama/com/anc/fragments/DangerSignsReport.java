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

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import apps.uzazisalama.com.anc.R;
import apps.uzazisalama.com.anc.base.AppDatabase;
import apps.uzazisalama.com.anc.database.AncClient;
import apps.uzazisalama.com.anc.database.RoutineVisits;

/**
 * Created by issy on 14/07/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project UNFPA_UzaziSalama_Facility
 */

public class DangerSignsReport extends DialogFragment {

    private AppDatabase database;
    private TextView totalClientsValue, fromDate, toDate;
    private MaterialSpinner signsSpinner;
    private RecyclerView clientsRecycler;
    private PieChart pieChart;
    private CircularProgressView progressView;

    private DatePickerDialog fromDatePickerDialog = new DatePickerDialog();
    private DatePickerDialog toDatePickerDialog = new DatePickerDialog();
    private Calendar cal;
    private long dateFromInMillis, dateToInMillis;
    private boolean otherDateSelected = false;

    //PieChart Data
    private float[] yData = {};
    private String[] xData = {"anaemia", "oedema" , "protenuria" , "highBloodPressure", "weightStagnation", "antepartumHaemorrhage", "sugarInTheUrine", "fetusLie"};
    private List<RoutineVisits> mVisits = new ArrayList<>();


    public DangerSignsReport(){}

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
        return inflater.inflate(R.layout.fragment_report_danger_signs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView(view);

        Description description = new Description();
        description.setText("Danger Signs Seen in Client Encounters");
        pieChart.setDescription(description);
        pieChart.setRotationEnabled(true);
        //pieChart.setUsePercentValues(true);
        //pieChart.setHoleColor(Color.BLUE);
        //pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setHoleRadius(50f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("Clients With Danger Signs");
        pieChart.setCenterTextSize(10);
        pieChart.setDrawEntryLabels(true);
        pieChart.setEntryLabelTextSize(12);
        //More options just check out the documentation!

        List<String> signs = new ArrayList<>();
        signs.add("All");
        signs.add("Anaemia");
        signs.add("Oedema");
        signs.add("Protenuria");
        signs.add("High Blood Pressure");
        signs.add("Weight Stagnation");
        signs.add("AntepartumHaemorrhage");
        signs.add("Sugar In The Urine");
        signs.add("FetusLie");
        signsSpinner.setItems(signs);
        signsSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Log.d("france", "Position Selected : "+position);
                getFilteredClients(position, mVisits);
            }
        });

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

        LiveData<List<RoutineVisits>> routines = database.routineModelDao().getRoutinesOnDateRange(dateFromInMillis, dateToInMillis);
        routines.observe(this, new Observer<List<RoutineVisits>>() {
            @Override
            public void onChanged(@Nullable List<RoutineVisits> routineVisits) {
                mVisits = routineVisits;
                Log.d("france", "Live data received  : "+routineVisits.size());

                getFilteredClients(0, mVisits);

                int anaemiaCount = 0;
                int oedemaCount = 0;
                int protenuriaCount = 0;
                int highBloodPressureCount = 0;
                int weightStagnationCount = 0;
                int antepartumHaemorrhageCount = 0;
                int sugarInTheUrineCount = 0;
                int fetusLieCount = 0;

                for (RoutineVisits rv : routineVisits){
                    if (rv.isAnaemia()){anaemiaCount = anaemiaCount+1;}
                    if (rv.isOedema()){oedemaCount = oedemaCount+1;}
                    if (rv.isProtenuria()){protenuriaCount = protenuriaCount+1;}
                    if (rv.isHighBloodPressure()){highBloodPressureCount=highBloodPressureCount+1;}
                    if (rv.isWeightStagnation()){weightStagnationCount = weightStagnationCount+1;}
                    if (rv.isAntepartumHaemorrhage()){antepartumHaemorrhageCount = antepartumHaemorrhageCount+1;}
                    if (rv.isSugarInTheUrine()){sugarInTheUrineCount = sugarInTheUrineCount+1;}
                    if (rv.isFetusLie()){fetusLieCount = fetusLieCount+1;}
                }

                ArrayList<PieEntry> yEntrys = new ArrayList<>();
                ArrayList<String> xEntrys = new ArrayList<>();

                //Getting Report Data
                Log.d("", "addDataSet started");
                yEntrys.add(new PieEntry(anaemiaCount , "Anaemia"));
                yEntrys.add(new PieEntry(oedemaCount , "Oedema"));
                yEntrys.add(new PieEntry(protenuriaCount , "Protenuria"));
                yEntrys.add(new PieEntry(highBloodPressureCount , "High Blood Pressure"));
                yEntrys.add(new PieEntry(weightStagnationCount , "Weight Stagnation"));
                yEntrys.add(new PieEntry(antepartumHaemorrhageCount , "Antepartum Haemorrhage"));
                yEntrys.add(new PieEntry(sugarInTheUrineCount , "Sugar in the Urine"));
                yEntrys.add(new PieEntry(fetusLieCount , "Fetuslie"));

                //add colors to dataset
                ArrayList<Integer> colors = new ArrayList<>();
                colors.add(getResources().getColor(R.color.cyan_a400));
                colors.add(getResources().getColor(R.color.orange_a400));
                colors.add(getResources().getColor(R.color.light_blue_a400));
                colors.add(getResources().getColor(R.color.green_a400));
                colors.add(getResources().getColor(R.color.purple_a400));
                colors.add(getResources().getColor(R.color.yellow_400));
                colors.add(getResources().getColor(R.color.teal_a400));

                //create the data set
                PieDataSet pieDataSet = new PieDataSet(yEntrys, "Danger Signs");
                pieDataSet.setSliceSpace(4);
                pieDataSet.setValueTextSize(14);

                pieDataSet.setColors(colors);

                //add legend to chart
                Legend legend = pieChart.getLegend();
                legend.setForm(Legend.LegendForm.CIRCLE);
                legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

                //create pie data object
                PieData pieData = new PieData(pieDataSet);
                pieChart.setData(pieData);
                pieChart.invalidate();

            }
        });

    }

    private void getFilteredClients(int routinePosition, List<RoutineVisits> visits){

        clientsRecycler.setVisibility(View.GONE);
        progressView.setVisibility(View.VISIBLE);

        List<RoutineVisits> filteredRoutines = new ArrayList<>();
        switch (routinePosition){
            case 0: //All
                getClients(visits);
                break;
            case 1: //Anaemia
                for (RoutineVisits v : visits){
                    if (v.isAnaemia())
                        filteredRoutines.add(v);
                }
                getClients(filteredRoutines);
                break;
            case 2: //Oedema
                for (RoutineVisits v : visits){
                    if (v.isOedema())
                        filteredRoutines.add(v);
                }
                getClients(filteredRoutines);
                break;
            case 3: //Protenuria
                for (RoutineVisits v : visits){
                    if (v.isProtenuria())
                        filteredRoutines.add(v);
                }
                getClients(filteredRoutines);
                break;
            case 4: //High Blood Pressure
                for (RoutineVisits v : visits){
                    if (v.isHighBloodPressure())
                        filteredRoutines.add(v);
                }
                getClients(filteredRoutines);
                break;
            case 5: //Weight Stagnation
                for (RoutineVisits v : visits){
                    if (v.isWeightStagnation())
                        filteredRoutines.add(v);
                }
                getClients(filteredRoutines);
                break;
            case 6: //Antepartum Haemorrhage
                for (RoutineVisits v : visits){
                    if (v.isAntepartumHaemorrhage())
                        filteredRoutines.add(v);
                }
                getClients(filteredRoutines);
                break;
            case 7: //Sugar In the Urine
                for (RoutineVisits v : visits){
                    if (v.isSugarInTheUrine())
                        filteredRoutines.add(v);
                }
                getClients(filteredRoutines);
                break;
            case 8: //FetusLie
                for (RoutineVisits v : visits){
                    if (v.isFetusLie())
                        filteredRoutines.add(v);
                }
                getClients(filteredRoutines);
                break;

        }
    }

    @SuppressLint("StaticFieldLeak")
    private void getClients(List<RoutineVisits> visitList){
        new AsyncTask<List<RoutineVisits>, Void, Void>(){
            List<ClientsAndRoutines> results = new ArrayList<>();
            @Override
            protected Void doInBackground(List<RoutineVisits>[] lists) {
                List<RoutineVisits> list = lists[0];
                for (RoutineVisits visit : list){
                    ClientsAndRoutines car = new ClientsAndRoutines();
                    AncClient  client = database.clientModel().getClientById(visit.getHealthFacilityClientId());
                    car.setClientVisit(visit.getVisitNumber());
                    car.setClientAncNumber("");
                    car.setClientPhone(client.getPhoneNumber());
                    car.setClientNames(client.getFirstName()+" "+client.getSurname());

                    results.add(car);
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {

                clientsRecycler.setVisibility(View.VISIBLE);
                progressView.setVisibility(View.GONE);

                ListAdapter adapter =  new ListAdapter(results);
                totalClientsValue.setText(results.size()+"");
                clientsRecycler.setAdapter(adapter);
            }
        }.execute(visitList);

    }

    @Override
    public void onStart() {
        super.onStart();
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
        getDialog().getWindow().setLayout(width, height);
    }

    private void setupView(View view){
        pieChart = view.findViewById(R.id.danger_signs_pie);
        totalClientsValue = view.findViewById(R.id.total_clients_value);
        fromDate = view.findViewById(R.id.from_date);
        toDate = view.findViewById(R.id.to_date);
        signsSpinner = view.findViewById(R.id.signs_spinner);
        progressView = view.findViewById(R.id.progress_view);

        clientsRecycler = view.findViewById(R.id.clients_Recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        clientsRecycler.setLayoutManager(layoutManager);
        clientsRecycler.setHasFixedSize(false);
    }

    class ClientsAndRoutines{

        private int clientVisit;
        private String clientNames;
        private String clientPhone;
        private String clientAncNumber;

        ClientsAndRoutines(){}

        public int getClientVisit() {
            return clientVisit;
        }

        public void setClientVisit(int clientVisit) {
            this.clientVisit = clientVisit;
        }

        public String getClientNames() {
            return clientNames;
        }

        public void setClientNames(String clientNames) {
            this.clientNames = clientNames;
        }

        public String getClientPhone() {
            return clientPhone;
        }

        public void setClientPhone(String clientPhone) {
            this.clientPhone = clientPhone;
        }

        public String getClientAncNumber() {
            return clientAncNumber;
        }

        public void setClientAncNumber(String clientAncNumber) {
            this.clientAncNumber = clientAncNumber;
        }
    }

    class ListAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder> {

        List<ClientsAndRoutines> mItems = new ArrayList<>();

        ListAdapter(List<ClientsAndRoutines> items){
            this.mItems = items;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView   = null;
            itemView = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.danger_sign_report_clients_item_view, parent, false);

            return new ReportViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ClientsAndRoutines clientsAndRoutines = mItems.get(position);
            ReportViewHolder vh = (ReportViewHolder)holder;
            vh.bind(clientsAndRoutines);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        class ReportViewHolder extends RecyclerView.ViewHolder{

            TextView names, phone, ancNumber, visit;

            public ReportViewHolder (View itemView) {
                super(itemView);
                names = itemView.findViewById(R.id.names);
                phone = itemView.findViewById(R.id.phone);
                ancNumber = itemView.findViewById(R.id.anc_number);
                visit = itemView.findViewById(R.id.visit_number);
            }

            void bind(ClientsAndRoutines car){
                names.setText(car.getClientNames());
                phone.setText(car.getClientPhone());
                ancNumber.setText(car.getClientAncNumber());
                visit.setText(car.getClientVisit()+"");
            }

        }

    }

}
