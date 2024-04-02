package com.acpay.acapymembers.bottomNavigationFragement.Costs;

import static com.acpay.acapymembers.MainActivity.getAPIHEADER;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.acpay.acapymembers.R;
import com.acpay.acapymembers.bottomNavigationFragement.Orders.OrderCostsFragment;
import com.acpay.acapymembers.bottomNavigationFragement.Store.AddExitItems;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class TransitionPendingFragment extends Fragment {
    BottomNavigationView bottomNav;
    TransitionDetailsAdapter adapter;
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
        FloatingActionButton addExit=rootview.findViewById(R.id.addTransitions);
        addExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(getView(), "جارى التحميل", BaseTransientBottomBar.LENGTH_SHORT).show();
                String api = getAPIHEADER(getContext()) + "/getOrderNums.php?id=" + FirebaseAuth.getInstance().getCurrentUser().getUid();
                RequestQueue queue = Volley.newRequestQueue(getContext());
                StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                List<String> list = extractFeuterFromJason(response);
                                List<String> lastFive = list.subList(list.size() - 5, list.size());
                                Collections.reverse(lastFive);
                                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
                                builderSingle.setIcon(R.drawable.ic_add);
                                builderSingle.setTitle("اختار الاوردر");
                                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_singlechoice);

                                arrayAdapter.addAll(lastFive);

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
                                        String dateOfOrder = strName.split("-")[1] + "-" + strName.split("-")[2] + "-" + strName.split("-")[3];
                                        String orderNum = strName.split("-")[0];
                                        Intent intent = new Intent(getContext(), OrderCostsFragment.class);
                                        intent.putExtra("num", orderNum);
                                        intent.putExtra("date", dateOfOrder);
                                        startActivity(intent);
                                    }
                                });
                                builderSingle.show();
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

            }
        });
        progressBar = rootview.findViewById(R.id.costListProgress);
        emptyList = rootview.findViewById(R.id.costListText);
        emptyList.setText("جارى التحميل");
        adapter = new TransitionDetailsAdapter(getContext(), new ArrayList<>(), "details");
        listView.setAdapter(adapter);
        listView.setEmptyView(emptyList);
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
        String api = getAPIHEADER(getContext()) + "/getTransitions.php?name=" + getName() + "&status=false";
        Log.e("aoi",api);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<TransitionsDetails> list = extractTransitionsfromapi(response);
                        adapter.addAll(list);
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        emptyList.setText("لايوجد انتقالات");
                        Log.e("as", list.toString());
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

        setHasOptionsMenu(true);
        return rootview;
    }

    private String getName() {
        String user = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        if (!user.isEmpty()) {
            switch (user) {
                case "Ahmed Saleh":
                    return "Ahmed";
                case "Barsom":
                    return "Barsom";
                case "Mohamed Hammad":
                    return "Mohamed";
                case "Remon":
                    return "Remon";
            }
        } else {
            return FirebaseAuth.getInstance().getCurrentUser().getUid();
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

            case R.id.addNewCost:
                final Intent intent = new Intent(getContext(), OrderCostsFragment.class);
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                intent.putExtra("date", year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                intent.putExtra("num", "daily");
                                startActivity(intent);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

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
