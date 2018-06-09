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
import apps.uzazisalama.com.anc.activities.ClientRegisterActivity;
import apps.uzazisalama.com.anc.activities.ReferralDetailsView;
import apps.uzazisalama.com.anc.base.AppDatabase;
import apps.uzazisalama.com.anc.base.BaseActivity;
import apps.uzazisalama.com.anc.database.AncClient;
import apps.uzazisalama.com.anc.database.Referral;

/**
 * Created by issy on 20/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project UNFPA_UzaziSalama_Facility
 */

public class ReferralListAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder> {

    List<Referral> items;
    Context context;

    public ReferralListAdapter(Context _context, List<Referral> _items){
        this.items = _items;
        this.context = _context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        context         = viewGroup.getContext();
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.referral_list_item, viewGroup, false);
        return new ListViewItemViewHolder(itemView);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        Referral referral = getItem(position);

        ListViewItemViewHolder holder = (ListViewItemViewHolder) viewHolder;

        ClientNames clientNames = new ClientNames(BaseActivity.database, holder.clientNames, holder.clientPhone, holder.clientVillage);
        clientNames.execute(referral);

        holder.referralReason.setText(referral.getReferralReason());
        switch (referral.getReferralStatus()){
            case 0:
                holder.referralStatus.setText("New");
                break;
            case 1:
                holder.referralStatus.setText("Completed");
                break;
            case -1:
                holder.referralStatus.setText("Rejected");
                break;
            default:
                holder.referralStatus.setText("");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReferralDetailsView.class);
                intent.putExtra("currentReferral", referral);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private Referral getItem(int position){
        return items.get(position);
    }

    private class ListViewItemViewHolder extends RecyclerView.ViewHolder {

        TextView clientNames, clientPhone, clientVillage, referralReason, referralStatus;
        View viewItem;
        Referral referral;

        public ListViewItemViewHolder(View itemView){
            super(itemView);
            this.viewItem   = itemView;

            clientNames = itemView.findViewById(R.id.referral_client_names);
            clientPhone = itemView.findViewById(R.id.referral_client_phone);
            clientVillage = itemView.findViewById(R.id.referral_client_village);
            referralReason = itemView.findViewById(R.id.referral_reason);
            referralStatus = itemView.findViewById(R.id.referral_status);

        }

    }

    private class ClientNames extends AsyncTask<Referral, Void, Void>{

        AppDatabase database;
        TextView name, phone, village;
        String clientNames = "";
        AncClient currentClient;

        public ClientNames (AppDatabase _database, TextView _name, TextView _phone, TextView _village){
            this.database = _database;
            name = _name;
            phone = _phone;
            village = _village;
        }

        @Override
        protected Void doInBackground(Referral... args) {
            List<AncClient> list = database.clientModel().getItemById(args[0].getHealthFacilityClientID());

            if (list.size() > 0){
                AncClient client = list.get(0);
                currentClient = client;
                clientNames = client.getFirstName()+" " +
                        client.getMiddleName()+" " +
                        client.getSurname();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            name.setText(clientNames);
            phone.setText(currentClient.getPhoneNumber());
            village.setText(currentClient.getVillage());
        }
    }

}
