package apps.uzazisalama.com.anc.base;

import android.arch.persistence.room.Database;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.text.SimpleDateFormat;

/**
 * Created by issy on 10/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project ANC
 */

public abstract class BaseActivity extends AppCompatActivity {

    //This is the base activity to be subclassed by all activities created on the application

    public static AppDatabase database;

    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd yyy");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = AppDatabase.getDatabase(this);
    }
}
