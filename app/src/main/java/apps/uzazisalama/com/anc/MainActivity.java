package apps.uzazisalama.com.anc;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.util.ArrayList;
import java.util.List;

import apps.uzazisalama.com.anc.base.BaseActivity;
import apps.uzazisalama.com.anc.customviews.NonSwipeableViewPager;
import apps.uzazisalama.com.anc.fragments.AncFragment;
import apps.uzazisalama.com.anc.fragments.PncFragment;
import apps.uzazisalama.com.anc.fragments.ReportsFragment;
import apps.uzazisalama.com.anc.services.AncToPncDispatcherService;
import apps.uzazisalama.com.anc.utils.SessionManager;

public class MainActivity extends BaseActivity {

    TabLayout tabLayout;
    public static ViewPager viewPager;
    Toolbar toolbar;
    TextView toolbarTitle, unsynced;
    CircularProgressView syncProgressBar;
    ImageView manualSync;

    FirebaseJobDispatcher dispatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupviews();

        dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));

        scheduleBackgroundJob();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }

        //User Session Manager Initialization
        SessionManager sessionManager = new SessionManager(this);
        if (!sessionManager.isLoggedIn()){
            sessionManager.checkLogin();
            finish();
        }

        if (session.isLoggedIn()){
            TextView userName = findViewById(R.id.toolbar_user_name);
            userName.setText(session.getUserName());
        }

        //initialize viewpager
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(4);

        //initialize tablayout
        tabLayout = findViewById(R.id.tabs);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
                setupTabIcons();
            }
        });

    }

    private void scheduleBackgroundJob(){
        Job myJob = dispatcher.newJobBuilder()
                .setService(AncToPncDispatcherService.class) // the JobService that will be called
                .setTag("10002")        // uniquely identifies the job
                .setRecurring(true)
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                .setTrigger(Trigger.executionWindow(0, 60))
                .setReplaceCurrent(false)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .build();

        dispatcher.mustSchedule(myJob);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout){
            session.logoutUser();
            finish();
        }

        return true;
    }

    void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AncFragment(), "ANC");
        adapter.addFragment(new PncFragment(), "PNC");
        adapter.addFragment(new ReportsFragment(), "Reports");
        viewPager.setAdapter(adapter);
    }

    void setupTabIcons(){

        View ancTabView = getLayoutInflater().inflate(R.layout.custom_tabs, null);
        TextView ancTabTitle = ancTabView.findViewById(R.id.title_text);
        ImageView ancIcon    = ancTabView.findViewById(R.id.icon);
        ancIcon.setColorFilter(getResources().getColor(R.color.card_light_text));
        if (!MainActivity.this.isFinishing())
            Glide.with(this).load(R.mipmap.ic_anc).into(ancIcon);
        ancTabTitle.setText("ANC");
        tabLayout.getTabAt(0).setCustomView(ancTabView);

        View pncTabView = getLayoutInflater().inflate(R.layout.custom_tabs, null);
        TextView pncTabTitle = pncTabView.findViewById(R.id.title_text);
        ImageView pncIcon    = pncTabView.findViewById(R.id.icon);
        pncIcon.setColorFilter(getResources().getColor(R.color.card_light_text));
        if (!MainActivity.this.isFinishing())
            Glide.with(this).load(R.mipmap.ic_pnc).into(pncIcon);
        pncTabTitle.setText("PNC");
        tabLayout.getTabAt(1).setCustomView(pncTabView);


        View reportsTabView = getLayoutInflater().inflate(R.layout.custom_tabs, null);
        TextView reportsTabTitle = reportsTabView.findViewById(R.id.title_text);
        ImageView reportsIcon    = reportsTabView.findViewById(R.id.icon);
        if (!MainActivity.this.isFinishing())
            Glide.with(this).load(R.mipmap.ic_reports).into(reportsIcon);
        reportsIcon.setColorFilter(getResources().getColor(R.color.card_light_text));
        reportsTabTitle.setText("REPORTS");
        tabLayout.getTabAt(2).setCustomView(reportsTabView);

    }

    void setupviews(){

        syncProgressBar = findViewById(R.id.manual_sync_loader);
        syncProgressBar.setVisibility(View.INVISIBLE);

        manualSync = findViewById(R.id.manual_sync);

        toolbarTitle = findViewById(R.id.toolbar_user_name);
        unsynced = findViewById(R.id.unsynced);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position);
            return null;
        }
    }

}
