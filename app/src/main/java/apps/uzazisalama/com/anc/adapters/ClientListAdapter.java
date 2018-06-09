package apps.uzazisalama.com.anc.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import apps.uzazisalama.com.anc.R;
import apps.uzazisalama.com.anc.activities.AncRoutineActivity;
import apps.uzazisalama.com.anc.base.AppDatabase;
import apps.uzazisalama.com.anc.base.BaseActivity;
import apps.uzazisalama.com.anc.database.AncClient;
import apps.uzazisalama.com.anc.database.ClientAppointment;

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
            holder.bindItem(ancClient);
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

            TextView clientNames, clientAge, clientVillage, clientPara, clientGravida, clientAppointmentDate;
            View viewItem;
            AncClient ancClient;

            public ListViewItemViewHolder(View itemView){
                super(itemView);
                this.viewItem   = itemView;

                clientNames = itemView.findViewById(R.id.client_list_names);
                clientAge = itemView.findViewById(R.id.client_age);
                clientVillage = itemView.findViewById(R.id.client_village);
                clientPara = itemView.findViewById(R.id.client_para);
                clientGravida = itemView.findViewById(R.id.client_gravida);
                clientAppointmentDate = itemView.findViewById(R.id.client_appointment_date);

            }

            public void bindItem(AncClient client){
                clientNames.setText(client.getFirstName()+" "+client.getMiddleName()+" "+client.getSurname());

                Calendar dobCalendar = Calendar.getInstance();
                dobCalendar.setTimeInMillis(client.getDateOfBirth());

                clientAge.setText(getAge(dobCalendar.getTime())+"");
                clientVillage.setText(client.getVillage());
                clientPara.setText(client.getPara()+"");
                clientGravida.setText(client.getGravida()+"");

                DisplayNextAppointment displayNextAppointment = new DisplayNextAppointment(BaseActivity.database, clientAppointmentDate, client.getHealthFacilityClientId());
                displayNextAppointment.execute();

            }

            public int getAge(Date dateOfBirth) {
                Calendar today = Calendar.getInstance();
                Calendar birthDate = Calendar.getInstance();
                birthDate.setTime(dateOfBirth);
                if (birthDate.after(today)) {
                    //throw new IllegalArgumentException("You don't exist yet");
                }
                int todayYear = today.get(Calendar.YEAR);
                int birthDateYear = birthDate.get(Calendar.YEAR);
                int todayDayOfYear = today.get(Calendar.DAY_OF_YEAR);
                int birthDateDayOfYear = birthDate.get(Calendar.DAY_OF_YEAR);
                int todayMonth = today.get(Calendar.MONTH);
                int birthDateMonth = birthDate.get(Calendar.MONTH);
                int todayDayOfMonth = today.get(Calendar.DAY_OF_MONTH);
                int birthDateDayOfMonth = birthDate.get(Calendar.DAY_OF_MONTH);
                int age = todayYear - birthDateYear;

                // If birth date is greater than todays date (after 2 days adjustment of leap year) then decrement age one year
                if ((birthDateDayOfYear - todayDayOfYear > 3) || (birthDateMonth > todayMonth)){
                    age--;

                    // If birth date and todays date are of same month and birth day of month is greater than todays day of month then decrement age
                } else if ((birthDateMonth == todayMonth) && (birthDateDayOfMonth > todayDayOfMonth)){
                    age--;
                }
                return age;
            }

        }

        private class DisplayNextAppointment extends AsyncTask<Void, Void, Void>{

            AppDatabase database;
            TextView textView;
            long clientId;
            long nextAppointmentDate = 0;
            boolean hasNextAppointment = true;
            boolean nextAppointmentFound = false;

            public DisplayNextAppointment(AppDatabase _database, TextView tv, long _clientId){
                this.database = _database;
                this.textView = tv;
                this.clientId = _clientId;
            }

            @Override
            protected Void doInBackground(Void... voids) {

                Calendar calendar = Calendar.getInstance();


                List<ClientAppointment> appointments = database.clientAppointmentDao().getAppointmentsByClientId(clientId);
                for (int i=0; i<appointments.size(); i++){
                    if (appointments.get(i).getAppointmentDate() > Calendar.getInstance().getTimeInMillis()){
                        if (!nextAppointmentFound){
                            nextAppointmentFound = true;
                            nextAppointmentDate = appointments.get(i).getAppointmentDate();
                        }
                    }else {
                        hasNextAppointment = false;
                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (hasNextAppointment && (nextAppointmentDate!=0)){
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(nextAppointmentDate);
                    String displayDate = BaseActivity.simpleDateFormat.format(calendar.getTime());
                    textView.setText(displayDate);
                }else {
                    textView.setText("-");
                }
            }
        }

}
