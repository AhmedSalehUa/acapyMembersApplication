package com.acpay.acapymembers.bottomNavigationFragement.Messages;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.acpay.acapymembers.R;
import com.acpay.acapymembers.Tokens;
import com.acpay.acapymembers.sendNotification.APIService;
import com.acpay.acapymembers.sendNotification.Client;
import com.acpay.acapymembers.sendNotification.Data;
import com.acpay.acapymembers.sendNotification.SendNotification;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
<<<<<<< HEAD:app/src/main/java/com/acpay/acapymembers/bottomNavigationFragement/messages/MessegeFragment.java
=======
import java.util.HashMap;
>>>>>>> e657a51ca46385b1f80c980290f93f6b456eb68a:app/src/main/java/com/acpay/acapymembers/bottomNavigationFragement/Messages/MessegeFragment.java
import java.util.List;
import java.util.Locale;

public class MessegeFragment extends Fragment {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReference;
    private ChildEventListener mChildEventListener;
    ValueEventListener seenListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private static final String TAG = "MessegeFragment";

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;

    private ListView mMessageListView;
    private MessageAdapter mMessageAdapter;

    private ProgressBar mProgressBar;
    private ImageButton mPhotoPickerButton;
    private EditText mMessageEditText;
    private Button mSendButton;

    private String mUsername;
    APIService apiService;

    private static final int RC_PHOTO_PICKER = 2;
    public static final int RC_SIGN_IN = 1;

    String DateNow;
    String TimeNow;
<<<<<<< HEAD:app/src/main/java/com/acpay/acapymembers/bottomNavigationFragement/messages/MessegeFragment.java
=======
    FirebaseUser user;

    String manager1 = "Samaan";
    String manager2 = "Ireny";
    Tokens tok;
>>>>>>> e657a51ca46385b1f80c980290f93f6b456eb68a:app/src/main/java/com/acpay/acapymembers/bottomNavigationFragement/Messages/MessegeFragment.java

    public MessegeFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.message_activity, container, false);
        mUsername = ANONYMOUS;


        mFirebaseAuth = FirebaseAuth.getInstance();
<<<<<<< HEAD:app/src/main/java/com/acpay/acapymembers/bottomNavigationFragement/messages/MessegeFragment.java
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
=======
        user = mFirebaseAuth.getCurrentUser();
>>>>>>> e657a51ca46385b1f80c980290f93f6b456eb68a:app/src/main/java/com/acpay/acapymembers/bottomNavigationFragement/Messages/MessegeFragment.java
        mFirebaseStorage = FirebaseStorage.getInstance();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("messages").child(user.getDisplayName());
<<<<<<< HEAD:app/src/main/java/com/acpay/acapymembers/bottomNavigationFragement/messages/MessegeFragment.java

=======
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
>>>>>>> e657a51ca46385b1f80c980290f93f6b456eb68a:app/src/main/java/com/acpay/acapymembers/bottomNavigationFragement/Messages/MessegeFragment.java
        mChatPhotosStorageReference = mFirebaseStorage.getReference().child("chat_photo");

        // Initialize references to views
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        mMessageListView = (ListView) rootView.findViewById(R.id.messageListView);
        mPhotoPickerButton = (ImageButton) rootView.findViewById(R.id.photoPickerButton);
        mMessageEditText = (EditText) rootView.findViewById(R.id.messageEditText);
        mSendButton = (Button) rootView.findViewById(R.id.sendButton);

        mProgressBar.setVisibility(ProgressBar.INVISIBLE);


        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);

            }
        });


        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
<<<<<<< HEAD:app/src/main/java/com/acpay/acapymembers/bottomNavigationFragement/messages/MessegeFragment.java
                DateNow = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                TimeNow = new SimpleDateFormat("hh:mm").format(new Date());
                Message friendlyMessage = new Message(mMessageEditText.getText().toString(), mUsername, null, DateNow, TimeNow);
=======
                DateNow = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
                TimeNow = new SimpleDateFormat("hh:mm", Locale.ENGLISH).format(new Date());
                Message friendlyMessage = new Message(mMessageEditText.getText().toString(), user.getDisplayName(), null, DateNow, TimeNow, false);
>>>>>>> e657a51ca46385b1f80c980290f93f6b456eb68a:app/src/main/java/com/acpay/acapymembers/bottomNavigationFragement/Messages/MessegeFragment.java
                mDatabaseReference.push().setValue(friendlyMessage);
                Data data = new Data(user.getDisplayName(), mMessageEditText.getText().toString(), "message");
                SendNotification send1 = new SendNotification(getContext(), manager1, data);
                SendNotification send2 = new SendNotification(getContext(), manager2, data);

                mMessageEditText.setText("");
            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    setSiginigInIntilization(user.getDisplayName(), user.getUid());
                } else {
                    clearUpChats();
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
        List<Message> friendlyMessages = new ArrayList<>();
        mMessageAdapter = new MessageAdapter(getContext(), R.layout.message_activity_item, friendlyMessages, user.getDisplayName());
        mMessageListView.setAdapter(mMessageAdapter);
        seenMessage();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("الرسائل");
        return rootView;
    }

    private void seenMessage() {

        seenListener = mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message chat = snapshot.getValue(Message.class);
                    if (!chat.getName().equals(mUsername)) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("seen", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {

                Toast.makeText(getContext(), "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(getContext(), "Sign in canceled", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } else if (requestCode == RC_PHOTO_PICKER && resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();


            StorageReference photoRef = mChatPhotosStorageReference.child(selectedImageUri.getLastPathSegment());


            UploadTask uploadTask = photoRef.putFile(selectedImageUri);
            uploadTask.addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    if (taskSnapshot.getMetadata() != null) {
                        if (taskSnapshot.getMetadata().getReference() != null) {
                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
<<<<<<< HEAD:app/src/main/java/com/acpay/acapymembers/bottomNavigationFragement/messages/MessegeFragment.java
                                    Message friendlyMessage = new Message(null, mUsername, imageUrl, DateNow, TimeNow);
=======
                                    DateNow = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
                                    TimeNow = new SimpleDateFormat("hh:mm", Locale.ENGLISH).format(new Date());
                                    Message friendlyMessage = new Message(null, user.getDisplayName(), imageUrl, DateNow, TimeNow, false);
                                    Data sa=new Data(user.getDisplayName(), "photo","message");
                                    SendNotification send1 = new SendNotification(getContext(), manager1, sa);
                                    SendNotification send2 = new SendNotification(getContext(), manager2, sa);
>>>>>>> e657a51ca46385b1f80c980290f93f6b456eb68a:app/src/main/java/com/acpay/acapymembers/bottomNavigationFragement/Messages/MessegeFragment.java
                                    mDatabaseReference.push().setValue(friendlyMessage);
                                }
                            });
                        }
                    }

                }

            });
            uploadTask.addOnFailureListener(getActivity(), new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("failed", e.getMessage());
                }
            });

        }
    }

    private void setSiginigInIntilization(String displayName, String id) {

        mUsername = displayName;
        attachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Message message = snapshot.getValue(Message.class);
                mMessageAdapter.add(message);
                mMessageAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);

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
        mDatabaseReference.addChildEventListener(mChildEventListener);
    }


    private void clearUpChats() {
        mUsername = ANONYMOUS;
        mMessageAdapter.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

}
