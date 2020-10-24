package com.acpay.acapymembers.bottomNavigationFragement.Costs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.acpay.acapymembers.Order.Order;
import com.acpay.acapymembers.Order.progress.boxes;
import com.acpay.acapymembers.R;
import com.acpay.acapymembers.bottomNavigationFragement.Orders.JasonOrderNumsReponser;
import com.acpay.acapymembers.bottomNavigationFragement.Orders.OrderCostsFragment;
import com.acpay.acapymembers.sendNotification.Data;
import com.acpay.acapymembers.sendNotification.SendNotification;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TransitionPendingFragment extends Fragment {
    BottomNavigationView bottomNav;
    TransitionDetailsAdapter adapter;
    TransitionsApiUpdate updatea;
    TransitionsApiRespoding update;
    ProgressBar progressBar;
    TextView emptyList;

    public TransitionPendingFragment(BottomNavigationView bottomNav) {
        super();
        this.bottomNav = bottomNav;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_transitions_details, container, false);
        final ListView listView = rootview.findViewById(R.id.costList);
        progressBar = rootview.findViewById(R.id.costListProgress);
        emptyList = rootview.findViewById(R.id.costListText);
        emptyList.setText("جارى التحميل");
        String api = "https://www.app.acapy-trade.com/getTransitions.php?name=" + getName() + "&status=false";
        update = new TransitionsApiRespoding();
        update.setFinish(false);
        update.execute(api);
        final Handler handler = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                if (update.isFinish()) {
                    String apiResponsed = update.getUserId();
                    Log.e("apiResponsed",apiResponsed);
                    List<TransitionsDetails> list = extractTransitionsfromapi(apiResponsed);
                    adapter = new TransitionDetailsAdapter(getContext(), list, "details");
                    listView.setAdapter(adapter);
                    listView.setEmptyView(emptyList);
                    progressBar.setVisibility(View.GONE);
                    emptyList.setText("لايوجد انتقالات");
                    for (int i = 0; i < adapter.getCount(); i++) {
                        final int po = i;
                        adapter.getItem(i).setEditeBtn(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //  Toast.makeText(getContext(),adapter.getItem(po).getOrderNum(),Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getContext(), OrderCostsFragment.class);
                                intent.putExtra("num", adapter.getItem(po).getOrderNum());
                                startActivity(intent);
                            }
                        });
                    }
                    Log.e("as", list.toString());
                } else {
                    handler.postDelayed(this, 100);
                }
            }


        };
        handler.post(runnableCode);
        setHasOptionsMenu(true);
        return rootview;
    }

    private String getName() {
        String user = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        switch (user) {
            case "Ahmed Saleh":
                return "Ahmed";
            case "George Elgndy":
                return "George";
            case "Mohamed Hammad":
                return "Mohamed";
            case "Remon":
                return "Remon";
        }
        return "";
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_transition, menu);
    }

    private List<TransitionsDetails> extractTransitionsfromapi(String userId) {
        final List<TransitionsDetails> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(userId);
            JSONArray sa = jsonObject.names();
            for (int i = 0; i < sa.length(); i++) {
                JSONObject jsonArrayId = jsonObject.getJSONObject(sa.get(i).toString());
                List<Transitions> transitions = new ArrayList<>();
                if (jsonArrayId.has("list")) {
                    Log.e("jsonArrayId.has(\"list\")", "ok");
                    JSONObject transitionsjsonObject = new JSONObject(jsonArrayId.getString("list"));
                    JSONArray transitionssa = transitionsjsonObject.names();
                    Log.e("transitionssa", String.valueOf(transitionssa.length()));
                    for (int x = 0; x < transitionssa.length(); x++) {
                        JSONObject transitionsjsonArrayId = transitionsjsonObject.getJSONObject(transitionssa.get(x).toString());
                        transitions.add(new Transitions(transitionsjsonArrayId.getString("amount"), transitionsjsonArrayId.getString("details")));
                    }
                }

                TransitionsDetails transitionsDetails = new TransitionsDetails(jsonArrayId.getString("order_num"),
                        jsonArrayId.getString("date"),
                        jsonArrayId.getString("time"),
                        jsonArrayId.getString("place"),
                        jsonArrayId.getString("location"), transitions);

                list.add(transitionsDetails);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addNewTrans:
                Snackbar.make(getView(),"جارى التحميل", BaseTransientBottomBar.LENGTH_SHORT).show();
                String api = "https://www.app.acapy-trade.com/getOrderNums.php?id=" + FirebaseAuth.getInstance().getCurrentUser().getUid();
                final JasonOrderNumsReponser update = new JasonOrderNumsReponser();
                update.setFinish(false);
                update.execute(api);
                final Handler handler = new Handler();
                Runnable runnableCode = new Runnable() {
                    @Override
                    public void run() {
                        if (update.isFinish()) {
                            String res = update.getUserId();
                            List<String> list = extractFeuterFromJason(res);
                            AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
                            builderSingle.setIcon(R.drawable.ic_add);
                            builderSingle.setTitle("اختار الاوردر");
                            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_singlechoice);

                            arrayAdapter.addAll(list);


                            builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String strName = arrayAdapter.getItem(which);
                                    String orderNum = strName.split("-")[0];
                                    Intent intent = new Intent(getContext(), OrderCostsFragment.class);
                                    intent.putExtra("num", orderNum);
                                    startActivity(intent);
                                }
                            });
                            builderSingle.show();
                        } else {
                            handler.postDelayed(this, 100);
                        }
                    }


                };
                handler.post(runnableCode);

                break;
        }
        return true;
    }

    public static List<String> extractFeuterFromJason(String jason) {
        final List<String> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jason);
            JSONArray sa = jsonObject.names();
            for (int i = 0; i < sa.length(); i++) {
                JSONObject jsonArrayId = jsonObject.getJSONObject(sa.get(i).toString());

                list.add(jsonArrayId.getString("order_num") + " - "
                        + jsonArrayId.getString("date") + " - "
                        + jsonArrayId.getString("place") + " - "
                        + jsonArrayId.getString("location"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
