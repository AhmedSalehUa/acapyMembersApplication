package com.acpay.acapymembers.bottomNavigationFragement.Store;

import static com.acpay.acapymembers.MainActivity.getAPIHEADER;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.acpay.acapymembers.Order.progress.costs;
import com.acpay.acapymembers.Order.progress.costsAdapter;
import com.acpay.acapymembers.R;
import com.acpay.acapymembers.bottomNavigationFragement.Costs.Transitions;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddReturnedItems extends AppCompatActivity {
    public AddReturnedItems() {
        super();
    }

    private int mYear, mMonth, mDay, mHour, mMinute;
    EditText client, date, productId, amount;
    Spinner order;
    StoreReturnedOrdersAdapter adapter;
    storeProductsAdapter storeProductsAdapter;
    com.acpay.acapymembers.Order.progress.costsAdapter costsAdapter;
    Spinner details;
    int selectedItem = -1;
    ListView costsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_return_add);


        productId = findViewById(R.id.cost_id_add);
        amount = findViewById(R.id.cost_amount_add);
        order = (Spinner) findViewById(R.id.cost_client);
        adapter = new StoreReturnedOrdersAdapter(AddReturnedItems.this,
                R.layout.spinner_layout,
                new ArrayList<>());
        adapter.setDropDownViewResource(R.layout.spinner_layout);
        order.setAdapter(adapter);

        details = (Spinner) findViewById(R.id.cost_details_Spinner);
        storeProductsAdapter = new storeProductsAdapter(AddReturnedItems.this,
                R.layout.spinner_layout,
                new ArrayList<>());
        storeProductsAdapter.setDropDownViewResource(R.layout.spinner_layout);
        details.setAdapter(storeProductsAdapter);
        details.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                storeProducts user = storeProductsAdapter.getItem(position);
                productId.setText(String.valueOf(user.getProductId()));
                amount.setText(String.valueOf(user.getAmount()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }

        });

        String api = /*getAPIHEADER(AddReturnedItems.this) +*/ "http://192.168.1.90/app.acapy-trade.com/storeReturnedShow.php?user=" + FirebaseAuth.getInstance().getCurrentUser().getUid();
        RequestQueue queue = Volley.newRequestQueue(AddReturnedItems.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<StoreReturnedOrders> list = extractFeuterFromJason(response);

                        adapter.addAll(list);
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("onResponse", error.toString());
            }
        });
        stringRequest.setShouldCache(false);
        stringRequest.setShouldRetryConnectionErrors(true);
        stringRequest.setShouldRetryServerErrors(true);
        queue.add(stringRequest);
        costsList = (ListView) findViewById(R.id.costList);
        costsAdapter = new costsAdapter(AddReturnedItems.this, new ArrayList<costs>());
        costsList.setAdapter(costsAdapter);
        costsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem = i;
                costs item = costsAdapter.getItem(i);
                for (int q = 0; q < storeProductsAdapter.getCount(); q++) {
                    final int postion = q;

                    storeProducts a = storeProductsAdapter.getItem(postion);
                    if (item.getDetails().equals(a.getName())) {
                        details.setSelection(postion);
                    }
                }
                amount.setText(item.getAmount());
                productId.setText(item.getDate());
            }
        });
        order.setSelection(-1);
        order.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                StoreReturnedOrders user = adapter.getItem(position);

                List<storeProducts> list = user.getList();
                for (storeProducts a : list) {
                    Log.e("storeProducts", a.getName());
                }
                storeProductsAdapter.clear();
                productId.setText("");
                amount.setText("");
                storeProductsAdapter.addAll(list);
                storeProductsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });
        date = findViewById(R.id.store_date);
        date.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        final ImageView datePicker = (ImageView) findViewById(R.id.store_add_date);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddReturnedItems.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });
    }

    public static List<StoreReturnedOrders> extractFeuterFromJason(String jason) {
        final List<StoreReturnedOrders> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jason);
            JSONArray sa = jsonObject.names();
            for (int i = 0; i < sa.length(); i++) {
                JSONObject jsonArrayId = jsonObject.getJSONObject(sa.get(i).toString());
                List<storeProducts> transitions = new ArrayList<>();
                if (jsonArrayId.has("list")) {
                    JSONObject transitionsjsonObject = new JSONObject(jsonArrayId.getString("list"));
                    JSONArray transitionssa = transitionsjsonObject.names();
                    for (int x = 0; x < transitionssa.length(); x++) {
                        JSONObject transitionsjsonArrayId = transitionsjsonObject.getJSONObject(transitionssa.get(x).toString());
                        transitions.add(new storeProducts(transitionsjsonArrayId.getInt("productid"), transitionsjsonArrayId.getString("product"), transitionsjsonArrayId.getString("amount"), transitionsjsonArrayId.getInt("id")));
//                        Log.e("b",String.valueOf(transitionsjsonArrayId.getInt("productid"))+"  "+ transitionsjsonArrayId.getString("product")+"  "+ transitionsjsonArrayId.getString("amount")+"  "+ String.valueOf(transitionsjsonArrayId.getInt("id")));
                    }
                }

                StoreReturnedOrders transitionsDetails = new StoreReturnedOrders(jsonArrayId.getInt("id"),
                        jsonArrayId.getString("date"),
                        jsonArrayId.getString("details"), transitions);
                list.add(transitionsDetails);

                for (StoreReturnedOrders a : list) {
                    Log.e("a", a.getDetails());
                    for (storeProducts b : a.getList()) {
                        Log.e("b", b.getName());

                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.costs, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                if (amount.getText().toString().length() == 0 || productId.getText().toString().length() == 0) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(AddReturnedItems.this);
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
                    storeProducts sele = (storeProducts) details.getSelectedItem();
                    costsAdapter.add(new costs(amount.getText().toString(), sele.getName(), productId.getText().toString()));
                    amount.setText("");
                    productId.setText("");
                    details.setSelection(-1);
                }
                return true;
            case R.id.action_delete:
                if (selectedItem == -1) {

                } else {
                    costsAdapter.remove(costsAdapter.getItem(selectedItem));
                    amount.setText("");
                    productId.setText("");
                    details.setSelection(-1);
                }
                return true;
            case R.id.action_save:

//                String api = getAPIHEADER(AddExitItems.this) + "/storeExit.php?client="
//                        + client.getText().toString()
//                        + "&date=" + date.getText().toString()
//                        + "&user=" + FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//
//                ArrayList<costs> list = new ArrayList<>();
//                for (int i = 0; i < costsAdapter.getCount(); i++) {
//                    costs val = costsAdapter.getItem(i);
//                    list.add(val);
//                }
//
//                for (int x = 0; x < list.size(); x++) {
//                    costs so = list.get(x);
//                    api += "&amount[]=" + so.getAmount() + "&details[]=" + so.getDetails() + "&ids[]=" + so.getDate();
//                }
//                RequestQueue queue = Volley.newRequestQueue(AddExitItems.this);
//
//                StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                Toast.makeText(AddExitItems.this, "Saved", Toast.LENGTH_LONG).show();
//                                onBackPressed();
//                            }
//                        }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                        Log.e("onResponse", error.toString());
//                    }
//                });
//                stringRequest.setShouldCache(false);
//                stringRequest.setShouldRetryConnectionErrors(true);
//                stringRequest.setShouldRetryServerErrors(true);
//                queue.add(stringRequest);

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
