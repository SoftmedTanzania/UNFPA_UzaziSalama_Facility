package apps.uzazisalama.com.anc.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import apps.uzazisalama.com.anc.R;
import apps.uzazisalama.com.anc.activities.PncClientDetailActivity;
import apps.uzazisalama.com.anc.activities.ViewPncClientActivity;
import apps.uzazisalama.com.anc.base.AppDatabase;
import apps.uzazisalama.com.anc.base.BaseActivity;
import apps.uzazisalama.com.anc.database.AncClient;
import apps.uzazisalama.com.anc.database.PncClient;

/**
 * Created by issy on 19/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project UNFPA_UzaziSalama_Facility
 */

public class PncClientsListAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder> {

    List<PncClient> items;
    Context context;

    public PncClientsListAdapter(Context _context, List<PncClient> _items){
        this.items = _items;
        this.context = _context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pnc_client_view_item, parent, false);
        return new PncClientsListAdapter.ListViewItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        final PncClient client = getItem(position);

        PncClientsListAdapter.ListViewItemViewHolder holder = (PncClientsListAdapter.ListViewItemViewHolder) viewHolder;

        holder.viewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewPncClientActivity.class);
                intent.putExtra("currentPncClient", client);
                context.startActivity(intent);
            }
        });

        AncClientDetails clientDetails = new AncClientDetails(BaseActivity.database, holder);
        clientDetails.execute(client.getAncClientID());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private PncClient getItem(int position){
        return items.get(position);
    }

    public void updateAdapterItems(List<PncClient> updatedItems){
        this.items = items;
        this.notifyDataSetChanged();
    }

    private class ListViewItemViewHolder extends RecyclerView.ViewHolder {

        TextView clientNames;
        View viewItem;
        AncClient pncClient;

        public ListViewItemViewHolder(View itemView){
            super(itemView);
            this.viewItem   = itemView;

            clientNames = itemView.findViewById(R.id.client_list_names);

        }

    }

    private class AncClientDetails extends AsyncTask<String, Void, Void>{

        ListViewItemViewHolder holder;
        AppDatabase database;
        AncClient ancClient;

        AncClientDetails(AppDatabase db, ListViewItemViewHolder _holder){
            this.holder = _holder;
            this.database = db;
        }

        @Override
        protected Void doInBackground(String... args) {
            List<AncClient> ancClientList = BaseActivity.database.clientModel().getItemById(args[0]);
            if (ancClientList.size() > 0){
                ancClient = ancClientList.get(0);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            holder.clientNames.setText(ancClient.getFirstName()+" "+ancClient.getMiddleName()+" "+ancClient.getSurname());
            //Other Details
        }
    }

}
