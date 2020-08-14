package com.acpay.acapymembers.bottomNavigationFragement.messages.sendNotification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA3Ycr_N4:APA91bE-1hdkSxMSoG3erDuMGVU_KyEJQ5dng3COK1vq4-anYpixNCZ1gAlU9wjXF0RpXwlKUTccarpycWrc6QKL0iLhXQ_eFXGWxM-N9HvsPwE46uZArksWbiw75cKsdRzqOtB0yYO0"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
