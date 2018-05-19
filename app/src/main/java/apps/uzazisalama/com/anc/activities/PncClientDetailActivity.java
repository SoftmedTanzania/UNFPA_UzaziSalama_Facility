package apps.uzazisalama.com.anc.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import apps.uzazisalama.com.anc.R;
import apps.uzazisalama.com.anc.base.BaseActivity;

/**
 * Created by issy on 18/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project ANC
 */

public class PncClientDetailActivity extends BaseActivity {

    TextView clientNames;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pnc_details);
        setupviews();

        displayClientInformation();

    }

    void setupviews(){

    }

    void displayClientInformation(){

    }

}
