package apps.uzazisalama.com.anc.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import apps.uzazisalama.com.anc.R;

/**
 * Created by issy on 10/07/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project UNFPA_UzaziSalama_Facility
 */

public class ReportsFragment extends Fragment implements View.OnClickListener{

    private CardView registeredPregnantWomenReportCard, attendingFirstAncReportCard, atOrBeforeTwelveWeekReportCard;
    private CardView attendingFourOrMoreTimesReport, diagnosedWithDangerSignsReport, womenDeliveringAtTheFacilityReport;

    public ReportsFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reports, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViews(view);

    }

    private void setUpViews(View rootView){
        registeredPregnantWomenReportCard = rootView.findViewById(R.id.all_pregnant_women);
        registeredPregnantWomenReportCard.setOnClickListener(this);
        attendingFirstAncReportCard = rootView.findViewById(R.id.women_attending_first_anc);
        attendingFirstAncReportCard.setOnClickListener(this);
        atOrBeforeTwelveWeekReportCard = rootView.findViewById(R.id.at_or_before_12_weeks);
        atOrBeforeTwelveWeekReportCard.setOnClickListener(this);
        attendingFourOrMoreTimesReport = rootView.findViewById(R.id.women_attending_four_or_more_times);
        attendingFourOrMoreTimesReport.setOnClickListener(this);
        diagnosedWithDangerSignsReport = rootView.findViewById(R.id.women_diagnosed_with_danger_signs);
        diagnosedWithDangerSignsReport.setOnClickListener(this);
        womenDeliveringAtTheFacilityReport = rootView.findViewById(R.id.pregnant_women_delivering_at_the_facility);
        womenDeliveringAtTheFacilityReport.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.all_pregnant_women:
                registeredPregnantWomenReport();
                break;
            case R.id.women_attending_first_anc:
                attendingFirstAncReport();
                break;
            case R.id.at_or_before_12_weeks:
                atOrBeforeTwelveWeekReport();
                break;
            case R.id.women_attending_four_or_more_times:
                attendingFourOrMoreTimesReport();
                break;
            case R.id.women_diagnosed_with_danger_signs:
                diagnosedWithDangerSignsReport();
                break;
            case R.id.pregnant_women_delivering_at_the_facility:
                womenDeliveringAtTheFacilityReport();
                break;

        }
    }

    public void registeredPregnantWomenReport(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("registeredWomen");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment dialogFragment = new TotalRegisteredPregnantWomenReport();
        dialogFragment.show(ft, "registeredWomen");
    }

    public void attendingFirstAncReport(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("fitstVisitAnc");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment dialogFragment = new WomenAttendingFirstAncReport();
        dialogFragment.show(ft, "fitstVisitAnc");
    }

    public void atOrBeforeTwelveWeekReport(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("visitBeforeTwelve");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment dialogFragment = new VisitAtLessThanTwelveWeeksReport();
        dialogFragment.show(ft, "visitBeforeTwelve");
    }

    public void attendingFourOrMoreTimesReport(){

    }

    public void diagnosedWithDangerSignsReport(){

    }

    public void womenDeliveringAtTheFacilityReport(){

    }

}
