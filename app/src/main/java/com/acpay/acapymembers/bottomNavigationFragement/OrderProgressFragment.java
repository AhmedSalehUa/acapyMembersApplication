package com.acpay.acapymembers.bottomNavigationFragement;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.acpay.acapymembers.JasonReponser;
import com.acpay.acapymembers.R;
import com.acpay.acapymembers.oreder.progress.boxes;
import com.acpay.acapymembers.oreder.progress.boxesAdapter;
import com.acpay.acapymembers.oreder.progress.costs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderProgressFragment extends AppCompatActivity {
    boxesAdapter boxsAdapter;

    ListView boxesList;


    String orderNum;
    String boxesJasonResponse;


    int selectedItem = -1;
    ArrayList<boxes> list;

    public OrderProgressFragment() {
        super();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders_activity_list_item_progress);
        orderNum = getIntent().getStringExtra("num");
        boxesList = (ListView) findViewById(R.id.checkBoxesList);
        list = new ArrayList<>();
        String boxesapi = "https://www.app.acapy-trade.com/progress.php?order=" + orderNum;
        final JasonReponser boxesJason = new JasonReponser();
        boxesJason.setFinish(false);
        boxesJason.execute(boxesapi);
        final Handler boxeshandler = new Handler();
        Runnable boxesrunnableCode = new Runnable() {
            @Override
            public void run() {
                if (boxesJason.isFinish()) {
                    boxesJasonResponse = boxesJason.getUserId();
                    boxsAdapter = new boxesAdapter(OrderProgressFragment.this, fetchBoxsJason(boxesJasonResponse));
                    boxesList.setAdapter(boxsAdapter);
                    for (int i = 0; i < boxsAdapter.getCount(); i++) {
                        final int postion = i;
                        boxsAdapter.getItem(i).setCheckListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                boxes s = boxsAdapter.getItem(postion);
                                if (compoundButton.isChecked()) {
                                    list.add(s);
                                } else {
                                    list.remove(s);
                                }
                            }
                        });
                    }
                } else {
                    boxeshandler.postDelayed(this, 100);
                }
            }
        };
        boxeshandler.post(boxesrunnableCode);
    }


    private List<boxes> fetchBoxsJason(String boxesJasonResponse) {

        final List<boxes> boxes = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(boxesJasonResponse);
            JSONArray sa = jsonObject.names();
            for (int i = 0; i < sa.length(); i++) {
                JSONObject jsonArrayId = jsonObject.getJSONObject(sa.get(i).toString());
                boxes items = new boxes(jsonArrayId.getString("progress_name"),
                        jsonArrayId.getString("statue"),
                        jsonArrayId.getString("notes"));
                boxes.add(items);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return boxes;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.progress, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (list.size() > 0) {
                    String api = "https://www.app.acapy-trade.com/updatenotesprog.php?order=" + orderNum;
                    for (int x = 0; x < list.size(); x++) {
                        boxes so = list.get(x);
                        api += "&progress[]=" + so.getName() + "&note[]=" + so.getNotes();
                    }
                    Log.w("api", api);
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
                                    Toast.makeText(OrderProgressFragment.this, "Saved", Toast.LENGTH_LONG).show();
                                    onBackPressed();
                                } else {
                                    Toast.makeText(OrderProgressFragment.this, "not saved", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                handlerProg.postDelayed(this, 100);
                            }
                        }
                    };
                    handlerProg.post(runnableCodeProg);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}