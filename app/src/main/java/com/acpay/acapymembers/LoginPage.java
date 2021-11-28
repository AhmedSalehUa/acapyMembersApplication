package com.acpay.acapymembers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Arrays;
import java.util.List;

public class LoginPage extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static final int RC_SIGN_IN = 1;
    String token;
    Tokens tokens;
    String name = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("main", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token = task.getResult().getToken();
                        tokens = new Tokens(token);


                        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
                            @Override
                            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                final FirebaseUser user = firebaseAuth.getCurrentUser();
                                if (user != null) {
                                    if (user.getDisplayName().isEmpty()) {

                                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginPage.this);
                                        builder.setTitle("Title");
                                        final EditText input = new EditText(LoginPage.this);
                                        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                        builder.setView(input);
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                name = input.getText().toString();
                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(name).build();

                                                user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            mDatabaseReference = mFirebaseDatabase.getReference().child("users").child(user.getDisplayName());
                                                            mDatabaseReference.removeValue();
                                                            User Fireuser = new User(user.getDisplayName(), user.getUid(), "offline");
                                                            mDatabaseReference.push().setValue(Fireuser);

                                                            DatabaseReference mDatabaseReference = mFirebaseDatabase.getReference().child("tokens").child(user.getDisplayName());
                                                            mDatabaseReference.removeValue();
                                                            mDatabaseReference.push().setValue(tokens);

                                                            Intent intent = new Intent(LoginPage.this, MainActivity.class);

                                                            startActivity(intent);

                                                        }
                                                    }
                                                });
                                                ;
                                            }
                                        });
                                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                                intent.addCategory(Intent.CATEGORY_HOME);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
                                        });

                                        builder.show();
                                    } else {
                                        Log.e("user.getDisplayName()",user.getUid());
                                        mDatabaseReference = mFirebaseDatabase.getReference().child("users").child(user.getDisplayName());
                                        mDatabaseReference.removeValue();
                                        User Fireuser = new User(user.getDisplayName(), user.getUid(), "offline");
                                        mDatabaseReference.push().setValue(Fireuser);
                                        DatabaseReference mDatabaseReference = mFirebaseDatabase.getReference().child("tokens").child(user.getDisplayName());
                                        mDatabaseReference.removeValue();
                                        mDatabaseReference.push().setValue(tokens);
                                        Intent intent = new Intent(LoginPage.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                } else {
                                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.PhoneBuilder().build());
                                    startActivityForResult(
                                            AuthUI.getInstance()
                                                    .createSignInIntentBuilder()
                                                    .setAvailableProviders(providers).setIsSmartLockEnabled(false)
                                                    .build(),
                                            RC_SIGN_IN);
                                }
                            }
                        };

                        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("token", token);
                        // Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


    }

    @Override
    protected void onStart() {
        // mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        super.onStart();
    }

}
