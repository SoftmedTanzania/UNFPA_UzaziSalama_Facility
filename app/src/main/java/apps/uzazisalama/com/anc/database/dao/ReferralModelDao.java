package apps.uzazisalama.com.anc.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import apps.uzazisalama.com.anc.database.PncClient;
import apps.uzazisalama.com.anc.database.Referral;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by issy on 20/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project UNFPA_UzaziSalama_Facility
 */

@Dao
public interface ReferralModelDao  {

    @Query("SELECT * from Referral")
    LiveData<List<Referral>> getAllReferrals();

    @Query("SELECT * from Referral where referralID =:referralId")
    Referral getReferalByID(int referralId);

    @Query("select * from Referral where referralType = 1 and referralStatus = 0")
    LiveData<List<Referral>> getAllChwReferrals();

    @Query("select * from Referral where referralType = 2 and referralStatus = 0")
    LiveData<List<Referral>> getAllHealthFacilityReferrals();

    @Insert(onConflict = REPLACE)
    void addNewReferral(Referral referral);

    @Delete
    void deleteAReferral(Referral referral);

    @Update
    void updateReferral(Referral referral);

}
