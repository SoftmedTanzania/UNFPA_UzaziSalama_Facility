package apps.uzazisalama.com.anc.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import apps.uzazisalama.com.anc.R;
import apps.uzazisalama.com.anc.database.AncClient;

/**
 * Created by issy on 10/07/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project UNFPA_UzaziSalama_Facility
 */

public class ClientsAttendingFirstVisitRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AncClient> items = new ArrayList<>();

    public ClientsAttendingFirstVisitRecyclerAdapter(List<AncClient> itemList){
        this.items = itemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.clients_attending_first_visit_list_item, parent, false);

        return new ClientsAttendingFirstVisitRecyclerAdapter.ListViewItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AncClient client = items.get(position);
        ListViewItemViewHolder viewHolder = (ListViewItemViewHolder) holder;
        viewHolder.bindItem(client);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private class ListViewItemViewHolder extends RecyclerView.ViewHolder {

        TextView clientNames, ancNumber, clientVillage, clientPhoneNumber;
        View viewItem;

        public ListViewItemViewHolder(View itemView){
            super(itemView);
            this.viewItem   = itemView;

            clientNames = itemView.findViewById(R.id.client_name_value);
            ancNumber = itemView.findViewById(R.id.client_anc_number);
            clientVillage = itemView.findViewById(R.id.client_village_value);
            clientPhoneNumber = itemView.findViewById(R.id.client_phone_value);
        }

        void bindItem(AncClient client){
            clientNames.setText(client.getFirstName()+" "+client.getSurname());
            ancNumber.setText(client.getAncNumber() == "" ? "-" : client.getAncNumber());
            clientVillage.setText(client.getVillage());
            clientPhoneNumber.setText(client.getPhoneNumber());
        }

    }
}
