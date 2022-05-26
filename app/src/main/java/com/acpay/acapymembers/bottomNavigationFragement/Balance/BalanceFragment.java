package com.acpay.acapymembers.bottomNavigationFragement.Balance;

import static com.acpay.acapymembers.MainActivity.getAPIHEADER;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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

public class BalanceFragment extends Fragment {
    private BottomNavigationView bottomNav;
    ListView list;
    TextView emptyText;
    ProgressBar listProgress;
    BalanceAdapter adapter;

    public BalanceFragment(BottomNavigationView bottomNav) {
        super();
        this.bottomNav = bottomNav;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.blance_activity, container, false);
        list = root.findViewById(R.id.balance_list);
        emptyText = root.findViewById(R.id.balance_empty);
        emptyText.setText("لايوجد مسحوبات");
        listProgress = root.findViewById(R.id.balance_progress);
        list.setEmptyView(emptyText);
        FloatingActionButton addBalane = root.findViewById(R.id.add_balance);
        addBalane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddBalance.class));
            }
        });
        adapter = new BalanceAdapter(getContext(), new ArrayList<>(), "details");
        list.setAdapter(adapter);
        String api = getAPIHEADER(getContext()) + "/getbalance.php?user=" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "&status=false";
        Log.e("onResponse", api);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("as", response);
                        List<Balance> list = extractTransitionsfromapi(response);
                        adapter.addAll(list);
                        adapter.notifyDataSetChanged();
                        listProgress.setVisibility(View.GONE);
                        Log.e("as", list.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"error",Toast.LENGTH_SHORT).show();
                Log.e("onResponse", error.toString());
            }
        });
        stringRequest.setShouldCache(false);
        stringRequest.setShouldRetryConnectionErrors(true);
        stringRequest.setShouldRetryServerErrors(true);
        queue.add(stringRequest);
        return root;
    }

    private List<Balance> extractTransitionsfromapi(String userId) {
        final List<Balance> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(userId);
            JSONArray sa = jsonObject.names();
            for (int i = 0; i < sa.length(); i++) {
                JSONObject jsonArrayId = jsonObject.getJSONObject(sa.get(i).toString());
                list.add(new Balance(jsonArrayId.getInt("id"),jsonArrayId.getString("amount"), jsonArrayId.getString("date")
                        , "true",jsonArrayId.getString("details")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
