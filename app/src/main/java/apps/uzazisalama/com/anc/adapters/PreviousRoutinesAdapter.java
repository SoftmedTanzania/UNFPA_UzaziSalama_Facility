package apps.uzazisalama.com.anc.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uniquestudio.library.CircleCheckBox;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import apps.uzazisalama.com.anc.R;
import apps.uzazisalama.com.anc.activities.AncRoutineActivity;
import apps.uzazisalama.com.anc.database.RoutineVisits;

/**
 * Created by issy on 16/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project ANC
 */

public class PreviousRoutinesAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder> {

    List<RoutineVisits> items;
    private Context context;

    final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public PreviousRoutinesAdapter(List<RoutineVisits> mItems, Context _context){
        this.items = mItems;
        this.context = _context;
    }

    public PreviousRoutinesAdapter(){}

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        context         = viewGroup.getContext();
        View itemView   = null;
        itemView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.routine_list_item, viewGroup, false);

        return new PreviousRoutinesAdapter.ListViewItemViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int itemPosition){

        final RoutineVisits routineVisits = getItem(itemPosition);
        PreviousRoutinesAdapter.ListViewItemViewHolder holder = (PreviousRoutinesAdapter.ListViewItemViewHolder) viewHolder;

        holder.visitNumber.setText(routineVisits.getVisitNumber()+"");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(routineVisits.getVisitDate());
        holder.visitDate.setText(simpleDateFormat.format(calendar.getTime()));

        holder.a.setChecked(routineVisits.isAnaemia());
        holder.o.setChecked(routineVisits.isOedema());
        holder.p.setChecked(routineVisits.isProtenuria());
        holder.h.setChecked(routineVisits.isHighBloodPressure());
        holder.ws.setChecked(routineVisits.isWeightStagnation());
        holder.ah.setChecked(routineVisits.isAntepartumHaemorrhage());
        holder.su.setChecked(routineVisits.isSugarInTheUrine());
        holder.fl.setChecked(routineVisits.isFetusLie());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //UpdatePatientInformation and save data
                context.startActivity(new Intent(context, AncRoutineActivity.class));
            }
        });

    }

    public void addItems (List<RoutineVisits> clients){
        this.items = clients;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        return items.size();
    }

    private RoutineVisits getItem(int position){
        return items.get(position);
    }

    private class ListViewItemViewHolder extends RecyclerView.ViewHolder {

        TextView visitNumber, visitDate;
        CircleCheckBox a, o, p, h, ws, ah, su, fl;
        View viewItem;

        public ListViewItemViewHolder(View itemView){
            super(itemView);
            this.viewItem   = itemView;

            visitNumber = itemView.findViewById(R.id.visit_number);
            visitDate = itemView.findViewById(R.id.visit_date);

            a = itemView.findViewById(R.id.a_status);
            o = itemView.findViewById(R.id.o_status);
            p = itemView.findViewById(R.id.p_status);
            h = itemView.findViewById(R.id.h_status);
            ws = itemView.findViewById(R.id.ws_status);
            ah = itemView.findViewById(R.id.ah_status);
            su = itemView.findViewById(R.id.su_status);
            fl = itemView.findViewById(R.id.fl_status);

        }

    }

}
