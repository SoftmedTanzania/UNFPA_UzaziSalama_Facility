package apps.uzazisalama.com.anc.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import apps.uzazisalama.com.anc.R;
import apps.uzazisalama.com.anc.base.BaseActivity;

/**
 * Created by issy on 10/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project ANC
 */

public class ReferralListActivity extends BaseActivity {

    Toolbar referralListToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referrals_list);
        setupviews();

        referralListToolbar = findViewById(R.id.referral_list_toolbar);
        if (referralListToolbar!=null){
            setSupportActionBar(referralListToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    void setupviews(){

    }
}
