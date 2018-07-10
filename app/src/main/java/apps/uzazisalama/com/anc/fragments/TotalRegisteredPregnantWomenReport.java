package apps.uzazisalama.com.anc.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.w3c.dom.Text;

import java.util.Calendar;

import apps.uzazisalama.com.anc.R;
import apps.uzazisalama.com.anc.base.AppDatabase;

/**
 * Created by issy on 10/07/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project UNFPA_UzaziSalama_Facility
 */

public class TotalRegisteredPregnantWomenReport extends DialogFragment {

    private TextView fromDate, toDate, registeredWomenValue;
    private DatePickerDialog fromDatePickerDialog = new DatePickerDialog();
    private DatePickerDialog toDatePickerDialog = new DatePickerDialog();
    private Calendar cal;
    private long dateFromInMillis, dateToInMillis;
    private boolean otherDateSelected = false;
    public TotalRegisteredPregnantWomenReport(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setStyle(STYLE_NO_TITLE, R.style.dialog);
        DatePickerDialog fromDatePickerDialog = new DatePickerDialog();
        DatePickerDialog toDatePickerDialog = new DatePickerDialog();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return inflater.inflate(R.layout.fragment_report_registered_women, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViews(view);
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
                            getRegisteredWomenCount();
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
                            getRegisteredWomenCount();
                        }else {
                            otherDateSelected = true;
                        }
                    }

                });
            }
        });

    }

    private void getRegisteredWomenCount(){
        AppDatabase database = AppDatabase.getDatabase(getContext());
        LiveData<Integer> registeredWomenCount = database.clientModel().getPeriodicRegisteredWomen(dateFromInMillis, dateToInMillis);
        registeredWomenCount.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                registeredWomenValue.setText(integer+"");
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

    private void setUpViews(View view){
        fromDate = view.findViewById(R.id.from_date);
        toDate = view.findViewById(R.id.to_date);
        registeredWomenValue = view.findViewById(R.id.registered_women_value);
    }

}
