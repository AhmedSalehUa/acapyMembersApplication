package com.acpay.acapymembers.bottomNavigationFragement.Notes;


import static com.acpay.acapymembers.MainActivity.getAPIHEADER;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
        String api =  getAPIHEADER(AddDevice.this)+"/getNotes.php";
        RequestQueue queue = Volley.newRequestQueue(AddDevice.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        noteslist = extractFeuterFromJason(response);

                        for (int i = 0; i < noteslist.size(); i++) {
                            usersList.add(noteslist.get(i).getPalaceName());
                        }
                        bt.setEnabled(true);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("onResponse", error.toString());
            }
        });
        stringRequest.setShouldCache(false);stringRequest.setShouldRetryConnectionErrors(true);
        stringRequest.setShouldRetryServerErrors(true);
        queue.add(stringRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

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
                        RequestQueue queue = Volley.newRequestQueue(AddDevice.this);

                        StringRequest stringRequest = new StringRequest(Request.Method.GET, getApi(),
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (!response.equals("0")) {
                                            new StyleableToast.Builder(AddDevice.this).text("Saved").iconStart(R.drawable.ic_save).backgroundColor(getResources().getColor(android.R.color.holo_green_light)).show();
                                            onBackPressed();
                                        } else {
                                            new StyleableToast.Builder(AddDevice.this).text("Not Saved!").iconStart(R.drawable.falied).backgroundColor(getResources().getColor(android.R.color.holo_red_dark)).show();
                                            onBackPressed();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Log.e("onResponse", error.toString());
                            }
                        });
                        stringRequest.setShouldCache(false);stringRequest.setShouldRetryConnectionErrors(true);
                        stringRequest.setShouldRetryServerErrors(true);
                        queue.add(stringRequest);

                    }
                }
        }
        return true;
    }

    private String getApi() {
        String Api =  getAPIHEADER(AddDevice.this)+"/addNotesDetails.php?id=" + id
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
