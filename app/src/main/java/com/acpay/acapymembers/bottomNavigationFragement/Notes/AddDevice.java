package com.acpay.acapymembers.bottomNavigationFragement.Notes;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.acpay.acapymembers.R;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AddDevice extends AppCompatActivity {
    private String TokenList;

    public AddDevice() {
        super();
    }

    List<NotesPlaces> noteslist;
    String id;
    EditText name, type, model, details, ip, port, username, password, email, emailpass;
    Button bt;
    TextView olacename;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_adddevice);

        name = (EditText) findViewById(R.id.deivceName);
        type = (EditText) findViewById(R.id.deivceType);
        model = (EditText) findViewById(R.id.deivceModel);
        details = (EditText) findViewById(R.id.deivceDetails);
        ip = (EditText) findViewById(R.id.deivceIp);
        port = (EditText) findViewById(R.id.deivcePort);
        username = (EditText) findViewById(R.id.deivceUsername);
        password = (EditText) findViewById(R.id.deivcePassword);
        email = (EditText) findViewById(R.id.deivceEmail);
        emailpass = (EditText) findViewById(R.id.deivceEmailPassword);
        olacename = (TextView) findViewById(R.id.olacename);
        bt = (Button) findViewById(R.id.bt);
        bt.setEnabled(false);
        String api = "https://www.app.acapy-trade.com/getNotes.php";
        final NotesResponser update = new NotesResponser();
        update.setFinish(false);
        update.execute(api);
        final Handler handler = new Handler();
        Runnable runnableCode = new Runnable() {
            @SuppressLint("NewApi")
            @Override
            public void run() {
                if (update.isFinish()) {
                    TokenList = update.getUserId();
                    noteslist = extractFeuterFromJason(TokenList);
                    Log.e("as", String.valueOf(noteslist.size()));
                    for (int i = 0; i < noteslist.size(); i++) {
                        usersList.add(noteslist.get(i).getPalaceName());
                    }
                    bt.setEnabled(true);
                } else {
                    handler.postDelayed(this, 100);
                }

            }
        };
        handler.post(runnableCode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    AddResponser update;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (!bt.isEnabled()) {
                    new StyleableToast.Builder(this)
                            .text("غير قادر على تحميل الاماكن تحقق من اتصال الانترنت")
                            .textColor(getResources().getColor(android.R.color.white))
                            .backgroundColor(getResources().getColor(android.R.color.holo_red_dark))
                            .iconStart(R.drawable.falied)
                            .show();
                } else {
                    if (id == null) {
                        new StyleableToast.Builder(this)
                                .text("اختار المكان اولا")
                                .textColor(getResources().getColor(android.R.color.white))
                                .backgroundColor(getResources().getColor(android.R.color.holo_red_light))
                                .iconStart(R.drawable.falied)
                                .show();
                    } else {
                        update = new AddResponser();
                        update.setFinish(false);
                        update.execute(getApi());
                        final Handler handler = new Handler();
                        Runnable runnableCode = new Runnable() {
                            @Override
                            public void run() {
                                if (update.isFinish()) {
                                    String res = update.getUserId();
                                    if (!res.equals("0")) {
                                        new StyleableToast.Builder(AddDevice.this).text("Saved").iconStart(R.drawable.ic_save).backgroundColor(getResources().getColor(android.R.color.holo_green_light)).show();
                                        onBackPressed();
                                    } else {
                                        new StyleableToast.Builder(AddDevice.this).text("Not Saved!").iconStart(R.drawable.falied).backgroundColor(getResources().getColor(android.R.color.holo_red_dark)).show();
                                        onBackPressed();
                                    }
                                } else {
                                    handler.postDelayed(this, 100);
                                }
                            }
                        };
                        handler.post(runnableCode);
                    }
                }
        }
        return true;
    }

    private String getApi() {
        String Api = "https://www.app.acapy-trade.com/addNotesDetails.php?id=" + id
                + "&name=" + name.getText().toString() + "&type=" + type.getText().toString()
                + "&model=" + model.getText().toString() + "&details=" + details.getText().toString()
                + "&ip=" + ip.getText().toString() + "&username=" + username.getText().toString()
                + "&port=" + port.getText().toString() + "&email=" + email.getText().toString()
                + "&demailpass=" + emailpass.getText().toString() + "&password=" + password.getText().toString();
        return Api;
    }

    public List<NotesPlaces> extractFeuterFromJason(String jason) {
        List<NotesPlaces> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jason);
            JSONArray sa = jsonObject.names();
            for (int i = 0; i < sa.length(); i++) {
                JSONObject jsonArrayId = jsonObject.getJSONObject(sa.get(i).toString());
                NotesPlaces notesPlaces;

                notesPlaces = new NotesPlaces(jsonArrayId.getString("id"),
                        jsonArrayId.getString("name")
                );
                list.add(notesPlaces);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    List<String> usersList = new ArrayList<>();
    boolean[] checkedColors;

    public void setUsersList(View view) {
        olacename.setText("");
        AlertDialog.Builder builder = new AlertDialog.Builder(AddDevice.this);

        checkedColors = new boolean[usersList.size()];

        String[] colors = new String[usersList.size()];
        for (int x = 0; x < usersList.size(); x++) {
            colors[x] = usersList.get(x);
            checkedColors[0] = false;
        }

        builder.setMultiChoiceItems(colors, checkedColors, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                checkedColors[which] = isChecked;

                String currentItem = usersList.get(which);
            }
        });

        // Specify the dialog is not cancelable
        builder.setCancelable(false);

        // Set a title for alert dialog
        builder.setTitle("Order For");

        // Set the positive/yes button click listener
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                for (int i = 0; i < checkedColors.length; i++) {
                    boolean checked = checkedColors[i];
                    if (checked) {
                        olacename.setText(olacename.getText() + " - " + usersList.get(i));
                        id = noteslist.get(i).getPlaceId();
                    }
                }
            }
        });


        // Set the neutral/cancel button click listener
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when click the neutral button
            }
        });

        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();
    }

}
