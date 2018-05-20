package apps.uzazisalama.com.anc.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import apps.uzazisalama.com.anc.MainActivity;
import apps.uzazisalama.com.anc.R;
import apps.uzazisalama.com.anc.base.BaseActivity;
import apps.uzazisalama.com.anc.fragments.AncFragment;
import apps.uzazisalama.com.anc.fragments.PncFragment;
import apps.uzazisalama.com.anc.fragments.ReferralListFragment;

/**
 * Created by issy on 10/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project ANC
 */

public class ReferralListActivity extends BaseActivity {

    TabLayout referralTab;
    Toolbar referralToolbar;
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referrals_list);
        setupviews();

        if (referralToolbar!=null){
            setSupportActionBar(referralToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //initialize viewpager
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(4);

        //initialize tablayout
        referralTab.post(new Runnable() {
            @Override
            public void run() {
                referralTab.setupWithViewPager(viewPager);
                setupTabIcons();
            }
        });

    }

    void setupViewPager(ViewPager pager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(ReferralListFragment.newInstance(1), "CHW");
        adapter.addFragment(ReferralListFragment.newInstance(2), "Health Facility");
        viewPager.setAdapter(adapter);
    }

    void setupTabIcons(){
        View chwTab = getLayoutInflater().inflate(R.layout.custom_tabs, null);
        TextView chwTitle = chwTab.findViewById(R.id.title_text);
        chwTitle.setTextColor(getResources().getColor(R.color.white));
        ImageView ancIcon    = chwTab.findViewById(R.id.icon);
        ancIcon.setColorFilter(getResources().getColor(R.color.white));
        if (!ReferralListActivity.this.isFinishing())
            Glide.with(this).load(R.mipmap.ic_anc).into(ancIcon);
        chwTitle.setText("Chw");
        referralTab.getTabAt(0).setCustomView(chwTab);

        View hfTab = getLayoutInflater().inflate(R.layout.custom_tabs, null);
        TextView hfTitle = hfTab.findViewById(R.id.title_text);
        hfTitle.setTextColor(getResources().getColor(R.color.white));
        ImageView pncIcon    = hfTab.findViewById(R.id.icon);
        pncIcon.setColorFilter(getResources().getColor(R.color.white));
        if (!ReferralListActivity.this.isFinishing())
            Glide.with(this).load(R.mipmap.ic_pnc).into(pncIcon);
        hfTitle.setText("Health Facility");
        referralTab.getTabAt(1).setCustomView(hfTab);
    }

    void setupviews(){
        referralTab = findViewById(R.id.referral_tab);
        referralToolbar = findViewById(R.id.referral_list_toolbar);
        viewPager = findViewById(R.id.referral_viewpager);
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
