package apps.uzazisalama.com.anc.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by issy on 18/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project ANC
 */

@Entity(tableName = "PncClient")
public class PncClient {

    @PrimaryKey(autoGenerate = false)
    private String pncClientID;

    private String ancClientID;

    // Date that the mother was admitted to the hospital
    private long dateOfAdmission;

    private long dateOfDelivery;


    private boolean kuharibikaMimba;

    /**
     *  1 -> Normal Delivery,
     *  2 -> Vacuum,
     *  3 -> c-section
     */
    private int deliveryMethod;

    /**
     *  1 -> Postmotum Haemorrhage,
     *  2 -> Third degree tear ,
     *  3 -> Retained Placenta
     */
    private int deliveryComplications;


    /**
     *  0 -> Bad
     *  1 -> Good
     */
    private int motherDischargeCondition;

    //Child's Condition
    private String childGender;

    private double childWeight;

    /**
     *  Child's five senses, each sense is 2 score
     *  1 - 10
     */
    private int apgarScore;

    /**
     *  True if present
     */
    private boolean childAbnomalities;

    /**
     *  0 -> Bad
     *  1 -> Good
     */
    private int childDischargeCondition;

    /**
     * True if child has died within the past 24 hours
     */
    private boolean diedWithin24Hours;

    /**
     * 1 -> FBS
     * 2 -> MSB
     */
    private int stillBirthTypes;

}
