package apps.uzazisalama.com.anc.utils;

import java.text.SimpleDateFormat;

/**
 * Created by issy on 11/07/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project UNFPA_UzaziSalama_Facility
 */

public class Util {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyy");

    public static SimpleDateFormat getDateFormat() {
        return dateFormat;
    }
}
