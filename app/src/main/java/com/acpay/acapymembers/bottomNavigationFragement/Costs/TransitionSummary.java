package com.acpay.acapymembers.bottomNavigationFragement.Costs;

import static com.acpay.acapymembers.MainActivity.getAPIHEADER;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.acpay.acapymembers.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TransitionSummary extends Fragment {
    BottomNavigationView bottomNav;
    TextView transitions, balance, rest;

    public TransitionSummary(BottomNavigationView bottomNav) {
        super();
        this.bottomNav = bottomNav;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootViw = inflater.inflate(R.layout.activity_transition_summary, container, false);
        transitions = rootViw.findViewById(R.id.transitions_summary_total);
        balance = rootViw.findViewById(R.id.transitions_summary_balance);
        rest = rootViw.findViewById(R.id.transitions_summary_rest);
        String api = getAPIHEADER(getContext()) + "/getSummary.php?user=" + FirebaseAuth.getInstance().getCurrentUser().getUid();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray sa = jsonObject.names();

                            JSONObject jsonArrayId = jsonObject.getJSONObject(sa.get(0).toString());
                            transitions.setText(jsonArrayId.getString("totalTransactions"));
                            balance.setText(jsonArrayId.getString("totalBalance"));
                            rest.setText(jsonArrayId.getString("rest"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                Log.e("onResponse", error.toString());
            }
        });
        stringRequest.setShouldCache(false);
        stringRequest.setShouldRetryConnectionErrors(true);
        stringRequest.setShouldRetryServerErrors(true);
        queue.add(stringRequest);
        return rootViw;
    }
}
