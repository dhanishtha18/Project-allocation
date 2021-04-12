package org.projectapp.APIs;


import org.projectapp.Notifications.MyResponse;
import org.projectapp.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAIRuOngQ:APA91bEXkeZd1scSF3DvlMkx9uR01o7z8we0dYRng02k0NpRtbUxcoSGVB45XpAOh51zSQUHF18j6Lc6718VYP77IYv7O-HXV1fJu2vgDsevrhWMhXJwFljhexpnuVusM3Os3ypeMN-z"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
