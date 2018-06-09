package apps.uzazisalama.com.anc.api;

import java.util.List;

import apps.uzazisalama.com.anc.database.AncClient;
import apps.uzazisalama.com.anc.objects.LoginResponse;
import apps.uzazisalama.com.anc.objects.ReferralResponse;
import apps.uzazisalama.com.anc.objects.RegistrationResponse;
import apps.uzazisalama.com.anc.objects.RoutineResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by issy on 20/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project UNFPA_UzaziSalama_Facility
 */

public class Endpoints {

    public interface LoginService {
        @POST("security/authenticate/")
        Call<LoginResponse> basicLogin();
    }

    public interface ClientService{

        @POST("save-anc-client")
        Call<RegistrationResponse> postAncClient(@Body RequestBody p );

    }

    public interface ReferralService{
        @GET("get-facility-referrals/{facilityUUID}")
        Call<List<ReferralResponse>> getHealthFacilityReferrals(@Path("facilityUUID") String facilityUUID );

        @POST("receive-feedback")
        Call<String> postReferralFeedback(@Body RequestBody b);

    }

    public interface NotificationServices{
        @POST("save-push-notification-token")
        Call<String> registerDevice(@Body RequestBody u);
    }

    public interface RoutineServices{
        @POST("save-visit")
        Call<RoutineResponse> saveRoutineVisit(@Body RequestBody b);
    }

}
