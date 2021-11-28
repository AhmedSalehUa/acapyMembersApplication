package com.acpay.acapymembers.bottomNavigationFragement.Orders;

import static com.acpay.acapymembers.MainActivity.getAPIHEADER;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.acpay.acapymembers.Order.progress.costs;
import com.acpay.acapymembers.Order.progress.costsAdapter;
import com.acpay.acapymembers.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderCostsFragment extends AppCompatActivity {

    costsAdapter costsAdapter;

    ListView costsList;

    String orderNum;

    String DailyCostDate;

    String Api;

    String costsJasonResponse;

    int selectedItem = -1;

    public OrderCostsFragment() {
        super();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders_activity_list_item_cost);
        costsList = (ListView) findViewById(R.id.costList);
        orderNum = getIntent().getStringExtra("num");
        Log.e("num", orderNum);
        if (orderNum.equals("daily")) {
            getSupportActionBar().setTitle("مصاريف يومية");
            TextView header =(TextView)findViewById(R.id.costsListText);
            header.setText("مصاريف يومية");
            costsAdapter = new costsAdapter(OrderCostsFragment.this, new ArrayList<costs>());
            costsList.setAdapter(costsAdapter);
            costsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedItem = i;
                    costs item = costsAdapter.getItem(i);
                    TextView text = (TextView) findViewById(R.id.cost_amount_add);
                    TextView note = (TextView) findViewById(R.id.cost_details_add);
                    TextView co = (TextView) findViewById(R.id.cost_date_add);
                    text.setText(item.getAmount());
                    note.setText(item.getDetails());
                    co.setText(item.getDate());
                }
            });
            TextView co = (TextView) findViewById(R.id.cost_date_add);
            DailyCostDate = getIntent().getStringExtra("date");
            co.setText(DailyCostDate);
        } else {
            getSupportActionBar().setTitle("انتقالات");
            TextView header =(TextView)findViewById(R.id.costsListText);
            header.setText("انتقالات");
            String costsapi =  getAPIHEADER(OrderCostsFragment.this)+"/getexpendeCost.php?order=" + orderNum;
            RequestQueue queue = Volley.newRequestQueue(OrderCostsFragment.this);

            Log.e("ApiUrl", costsapi);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, costsapi,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            costsAdapter = new costsAdapter(OrderCostsFragment.this, fetchCostsJason(response));
                            costsList.setAdapter(costsAdapter);
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
            costsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedItem = i;
                    costs item = costsAdapter.getItem(i);
                    TextView text = (TextView) findViewById(R.id.cost_amount_add);
                    TextView note = (TextView) findViewById(R.id.cost_details_add);
                    TextView co = (TextView) findViewById(R.id.cost_date_add);
                    text.setText(item.getAmount());
                    note.setText(item.getDetails());
                    co.setText(item.getDate());
                }
            });
            TextView co = (TextView) findViewById(R.id.cost_date_add);
            DailyCostDate = getIntent().getStringExtra("date");
            co.setText(DailyCostDate);
        }


    }

    private List<costs> fetchCostsJason(String costsJasonResponse) {
        ArrayList<costs> costs = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(costsJasonResponse);
            JSONArray sa = jsonObject.names();
            for (int i = 0; i < sa.length(); i++) {
                JSONObject jsonArrayId = jsonObject.getJSONObject(sa.get(i).toString());
                costs items = new costs(jsonArrayId.getString("amount"),
                        jsonArrayId.getString("details"),
                        jsonArrayId.getString("date"));
                costs.add(items);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return costs;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.costs, menu);
        return true;
    }

    String api = "";
    ArrayList<costs> list = new ArrayList<>();

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:

                for (int i = 0; i < costsAdapter.getCount(); i++) {
                    costs val = costsAdapter.getItem(i);
                    list.add(val);
                }

                if (orderNum.equals("daily")) {
                    setUpApi();
                    RequestQueue queue = Volley.newRequestQueue(OrderCostsFragment.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, Api,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    api =  getAPIHEADER(OrderCostsFragment.this)+"/setexpendeCost.php?order=" + response;
                                    Log.e("api",api); completeAdding();
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
                    Log.e("api",Api);

                } else {
                    api =  getAPIHEADER(OrderCostsFragment.this)+"/setexpendeCost.php?order=" + orderNum;
                    completeAdding();
                }

                return true;
            case R.id.action_add:

                TextView text = (TextView) findViewById(R.id.cost_amount_add);
                TextView note = (TextView) findViewById(R.id.cost_details_add);
                TextView co = (TextView) findViewById(R.id.cost_date_add);
                if (text.getText().toString().length() == 0 || note.getText().toString().length() == 0 || co.getText().toString().length() == 0) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(OrderCostsFragment.this);
                    builder.setMessage("لا يجب ترك خانات فارغة!!")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                    dialog.cancel();
                                }
                            });
                    final AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    costsAdapter.add(new costs(text.getText().toString(), note.getText().toString(), co.getText().toString()));
                    text.setText("");
                    note.setText("");
                    co.setText(DailyCostDate);
                }
                return true;
            case R.id.action_delete:
                if (selectedItem == -1) {

                } else {
                    costsAdapter.remove(costsAdapter.getItem(selectedItem));
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void completeAdding() {

        for (int x = 0; x < list.size(); x++) {
            costs so = list.get(x);
            api += "&amount[]=" + so.getAmount() + "&details[]=" + so.getDetails() + "&date[]=" + so.getDate();
        }
        Log.e("add api", api);
        RequestQueue queue = Volley.newRequestQueue(OrderCostsFragment.this);

        Log.e("ApiUrl", api);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(OrderCostsFragment.this, "Saved", Toast.LENGTH_LONG).show();
                        onBackPressed();
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

    private void setUpApi() {
        Api = getAPIHEADER(OrderCostsFragment.this)+"/addOrdersFake.php?"
                + "&date=" + DailyCostDate
                + "&username[]=" + FirebaseAuth.getInstance().getCurrentUser().getDisplayName()
                + "&uid[]=" + FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}

