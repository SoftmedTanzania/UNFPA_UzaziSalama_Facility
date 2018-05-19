package apps.uzazisalama.com.anc.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import apps.uzazisalama.com.anc.R;
import apps.uzazisalama.com.anc.activities.AncRoutineActivity;
import apps.uzazisalama.com.anc.database.AncClient;

/**
 * Created by issy on 15/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project ANC
 */

public class ClientListAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder> {

        List<AncClient> items;
        private Context context;

        public ClientListAdapter(List<AncClient> mItems, Context _context){
            this.items = mItems;
            this.context = _context;
        }

        public ClientListAdapter(){}

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
            context         = viewGroup.getContext();
            View itemView   = null;
            itemView = LayoutInflater
                    .from(viewGroup.getContext())
                    .inflate(R.layout.clients_list_items, viewGroup, false);

            return new ClientListAdapter.ListViewItemViewHolder(itemView);

        }


        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int itemPosition){

            final AncClient ancClient = getItem(itemPosition);
            ClientListAdapter.ListViewItemViewHolder holder = (ClientListAdapter.ListViewItemViewHolder) viewHolder;

            holder.clientNames.setText(ancClient.getFirstName()+" "+ ancClient.getMiddleName()+" "+ ancClient.getSurname());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //UpdatePatientInformation and save data
                    Intent intent = new Intent(context, AncRoutineActivity.class);
                    intent.putExtra("currentAncClient", ancClient);
                    context.startActivity(intent);
                }
            });

        }

        public void addItems (List<AncClient> ancClients){
            this.items = ancClients;
            this.notifyDataSetChanged();
        }

        @Override
        public int getItemCount(){
            return items.size();
        }

        private AncClient getItem(int position){
            return items.get(position);
        }

        private class ListViewItemViewHolder extends RecyclerView.ViewHolder {

            TextView clientNames;
            View viewItem;
            AncClient ancClient;

            public ListViewItemViewHolder(View itemView){
                super(itemView);
                this.viewItem   = itemView;

                clientNames = itemView.findViewById(R.id.client_list_names);

            }

        }

}
