package com.acpay.acapymembers.bottomNavigationFragement.Costs;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.acpay.acapymembers.sendNotification.Data;
import com.acpay.acapymembers.sendNotification.SendNotification;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
                    List<TransitionsDetails> list = extractTransitionsfromapi(update.getUserId());
                    adapter = new TransitionDetailsAdapter(getContext(), list, "details");
                    listView.setAdapter(adapter);
                    listView.setEmptyView(emptyList);
                    progressBar.setVisibility(View.GONE);
                    emptyList.setText("لايوجد انتقالات");

                    Log.e("as", list.toString());
                } else {
                    handler.postDelayed(this, 100);
                }
            }


        };
        handler.post(runnableCode);
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
        menu.clear();
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

}
