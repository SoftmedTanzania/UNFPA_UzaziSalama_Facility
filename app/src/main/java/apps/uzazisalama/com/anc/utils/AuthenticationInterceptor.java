package apps.uzazisalama.com.anc.utils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by issy on 20/05/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project UNFPA_UzaziSalama_Facility
 */

public class AuthenticationInterceptor implements Interceptor {

    private String authToken;
    private String hfuuid;

    public AuthenticationInterceptor(String token, String hfid) {
        this.authToken = token;
        this.hfuuid   = hfid;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        if (hfuuid!=null){
            Request.Builder builder = original.newBuilder()
                    .addHeader("Authorization", authToken)
                    .addHeader("hfuuid", hfuuid);

            Request request = builder.build();
            return chain.proceed(request);
        }else {
            Request.Builder builder = original.newBuilder()
                    .header("Authorization", authToken);

            Request request = builder.build();
            return chain.proceed(request);
        }
    }
}