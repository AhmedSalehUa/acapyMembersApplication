package com.acpay.acapymembers.bottomNavigationFragement.Store;

import static com.acpay.acapymembers.MainActivity.getAPIHEADER;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.acpay.acapymembers.R;
import com.acpay.acapymembers.bottomNavigationFragement.Costs.TransitionDetailsAdapter;
import com.acpay.acapymembers.bottomNavigationFragement.Costs.Transitions;
import com.acpay.acapymembers.bottomNavigationFragement.Costs.TransitionsDetails;
import com.acpay.acapymembers.sendNotification.Data;
import com.acpay.acapymembers.sendNotification.SendNotification;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StoreExitFragment extends Fragment {
    public StoreExitFragment(BottomNavigationView bottomNav) {
        super();
    }

    ListView list;
    ProgressBar progressBar;
    TextView emptyList;
    StoreAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_store_exit, container, false);
        list = rootView.findViewById(R.id.store_exit_list);
        progressBar = rootView.findViewById(R.id.exit_progress);
        emptyList = rootView.findViewById(R.id.empty_exit);
        emptyList.setText("جارى التحميل");
        adapter = new StoreAdapter(getContext(), new ArrayList<>(), "details");
        list.setAdapter(adapter);
        list.setEmptyView(emptyList);
        String api = getAPIHEADER(getContext()) + "/storeExitShow.php?status=false&user=" + FirebaseAuth.getInstance().getCurrentUser().getUid();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<storeItems> list = extractTransitionsfromapi(response);
                        adapter.addAll(list);
                        adapter.notifyDataSetChanged();

                        for (int i = 0; i < adapter.getCount(); i++) {
                            final int postion = i;

                            adapter.getItem(postion).setEditeBtn(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    storeItems storeItems=  adapter.getItem(postion);
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setMessage("هل انت متاكد من حذف البيانات !؟")
                                            .setCancelable(false)
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                                    String api = getAPIHEADER(getContext()) + "/storeExitDelete.php?id=" +  storeItems.getId();
                                                    RequestQueue queue = Volley.newRequestQueue(getContext());
                                                    Log.e("NotesBtn", api);
                                                    StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                                                            new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String response) {
                                                                    Log.e("NotesBtn", response);
                                                                    Toast.makeText(getContext(), "done", Toast.LENGTH_SHORT).show();
                                                                     adapter.remove(storeItems);
                                                                     adapter.notifyDataSetChanged();
                                                                }
                                                            }, new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {

                                                        }
                                                    });
                                                    stringRequest.setShouldCache(false);
                                                    stringRequest.setShouldRetryConnectionErrors(true);
                                                    stringRequest.setShouldRetryServerErrors(true);
                                                    queue.add(stringRequest);

                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                                    dialog.cancel();
                                                }
                                            });
                                    final AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            });}
                        progressBar.setVisibility(View.GONE);
                        emptyList.setText("لايوجد انتقالات");
                        Log.e("as", list.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onResponse", error.toString());
                progressBar.setVisibility(View.GONE);
                emptyList.setText("لايوجد بيانات");
            }
        });
        stringRequest.setShouldCache(false);
        stringRequest.setShouldRetryConnectionErrors(true);
        stringRequest.setShouldRetryServerErrors(true);
        queue.add(stringRequest);
        FloatingActionButton addExit = rootView.findViewById(R.id.addExitItems);
        addExit.setOnClickListener(view -> startActivity(new Intent(getContext(), AddExitItems.class)));
        return rootView;
    }

    private List<storeItems> extractTransitionsfromapi(String userId) {
        final List<storeItems> list = new ArrayList<>();
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
                        transitions.add(new Transitions(transitionsjsonArrayId.getString("amount"), transitionsjsonArrayId.getString("product")));
                    }
                }

                storeItems transitionsDetails = new storeItems(jsonArrayId.getInt("id"),
                        jsonArrayId.getString("date"),
                        jsonArrayId.getString("details") , transitions);
                list.add(transitionsDetails);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

}
