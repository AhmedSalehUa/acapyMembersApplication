package com.acpay.acapymembers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acpay.acapymembers.bottomNavigationFragement.messages.sendNotification.APIService;
import com.acpay.acapymembers.bottomNavigationFragement.messages.sendNotification.Client;
import com.acpay.acapymembers.bottomNavigationFragement.messages.sendNotification.Data;
import com.acpay.acapymembers.bottomNavigationFragement.messages.sendNotification.MyResponse;
import com.acpay.acapymembers.bottomNavigationFragement.messages.sendNotification.Sender;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public  class SendNotification {
    Tokens tok;
    APIService apiService;
    Context mContext;
    public SendNotification(Context mContext, String receiver, final String username, final String message) {
        this.mContext=mContext;
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        sendNotifiaction(receiver,username, message);
    }
    private void sendNotifiaction(String receiver, final String username, final String message) {

        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("tokens").child(receiver);
        ChildEventListener mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                tok = snapshot.getValue(Tokens.class);
                if (tok.getToken() != null) {
                    Log.e("b", tok.getToken());
                    Data data = new Data(username, message);

                    Sender sender = new Sender(data, tok.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        if (response.body().success != 1) {
                                            Toast.makeText(mContext, "Failed!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Log.e("b", "ok");
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                } else {
                    Log.e("notification", "no token");
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                tok = snapshot.getValue(Tokens.class);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        tokens.addChildEventListener(mChildEventListener);
    }
}
