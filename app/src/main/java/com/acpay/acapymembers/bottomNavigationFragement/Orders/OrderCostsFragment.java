package com.acpay.acapymembers.bottomNavigationFragement.Orders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
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

import com.acpay.acapymembers.JasonReponser;
import com.acpay.acapymembers.R;
import com.acpay.acapymembers.Order.progress.costs;
import com.acpay.acapymembers.Order.progress.costsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderCostsFragment extends AppCompatActivity {

    costsAdapter costsAdapter;

    ListView costsList;

    String orderNum;

    String costsJasonResponse;

    int selectedItem = -1;

    public OrderCostsFragment() {
        super();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders_activity_list_item_cost);
        orderNum = getIntent().getStringExtra("num");
        costsList = (ListView) findViewById(R.id.costList);

        String costsapi = "https://www.app.acapy-trade.com/getexpendeCost.php?order=" + orderNum;
        final JasonCostsReponser costsJason = new JasonCostsReponser();
        costsJason.setFinish(false);
        costsJason.execute(costsapi);
        final Handler costshandler = new Handler();
        Runnable costsrunnableCode = new Runnable() {
            @Override
            public void run() {
                if (costsJason.isFinish()) {
                    costsJasonResponse = costsJason.getUserId();
                    Log.e("a",costsJasonResponse);
                    costsAdapter = new costsAdapter(OrderCostsFragment.this, fetchCostsJason(costsJasonResponse));
                    costsList.setAdapter(costsAdapter);
                } else {
                    costshandler.postDelayed(this, 100);
                }
            }
        };
        costshandler.post(costsrunnableCode);

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
        co.setText(new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH).format(new Date()));
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                ArrayList<costs> list = new ArrayList<>();
                for (int i = 0; i < costsAdapter.getCount(); i++) {
                    costs val = costsAdapter.getItem(i);
                    list.add(val);
                }
                String api = "https://www.app.acapy-trade.com/setexpendeCost.php?order=" + orderNum;
                for (int x = 0; x < list.size(); x++) {
                    costs so = list.get(x);
                    api += "&amount[]=" + so.getAmount() + "&details[]=" + so.getDetails() + "&date[]=" + so.getDate();
                }
                Log.e("add api",api) ;
                final JasonReponser updateProgress = new JasonReponser();
                updateProgress.setFinish(false);
                updateProgress.execute(api);
                final Handler handlerProg = new Handler();
                Runnable runnableCodeProg = new Runnable() {
                    @Override
                    public void run() {
                        if (updateProgress.isFinish()) {
                            String res = updateProgress.getUserId();
                            if (!res.equals("0")) {
                                Toast.makeText(OrderCostsFragment.this, "Saved", Toast.LENGTH_LONG).show();
                                onBackPressed();
                            } else {
                                Toast.makeText(OrderCostsFragment.this, "not saved", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            handlerProg.postDelayed(this, 100);
                        }
                    }
                };
                handlerProg.post(runnableCodeProg);
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
                    co.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date()));
                }
                // Utility.setDynamicHeight(costsList);
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
}

