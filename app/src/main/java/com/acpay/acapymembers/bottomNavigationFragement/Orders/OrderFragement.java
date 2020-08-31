package com.acpay.acapymembers.bottomNavigationFragement.Orders;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.app.LoaderManager.LoaderCallbacks;
import androidx.loader.content.Loader;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.acpay.acapymembers.R;
import com.acpay.acapymembers.Order.Order;
import com.acpay.acapymembers.Order.OrderAdapter;
import com.acpay.acapymembers.Order.OrderLoader;
import com.acpay.acapymembers.bottomNavigationFragement.Notes.AddDevice;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class OrderFragement extends Fragment implements LoaderCallbacks<List<Order>> {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private String ApiUrl;
    OrderAdapter adapter;
    ListView theListView;
    LoaderManager loaderManager;
    private static final int ORDER_LOADER_ID = 1;
    ProgressBar progressBar;
    TextView emptyList;

    public OrderFragement() {
        ApiUrl = " https://www.app.acapy-trade.com/orders.php?type=ok" + "&uid=" + user.getUid();
        Log.w("call order fragment ", ApiUrl);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.orders_activity, container, false);
        final SwipeRefreshLayout pullToRefresh = rootView.findViewById(R.id.pullToRefresh);
        progressBar = (ProgressBar) rootView.findViewById(R.id.listProgressOrder);
        emptyList = (TextView) rootView.findViewById(R.id.listEmptyOrder);
        emptyList.setText("لا يوجد اوردرات");
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressBar.setVisibility(View.VISIBLE);

                ConnectivityManager cm =
                        (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if (isConnected) {
                    loaderManager = getLoaderManager();
                    loaderManager.initLoader(ORDER_LOADER_ID, null, OrderFragement.this);
                } else {
                    emptyList.setText("لا يوجد اتصال بالانترنت");
                }
                pullToRefresh.setRefreshing(false);
                Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
            }
        });

        theListView = rootView.findViewById(R.id.orderMainList);
        theListView.setEmptyView(emptyList);

        adapter = new OrderAdapter(getContext(), new ArrayList<Order>());


        theListView.setAdapter(adapter);
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Order order = adapter.getItem(pos);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrderDetailsFragement(order)).commit();
            }
        });
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            loaderManager = getLoaderManager();
            loaderManager.initLoader(ORDER_LOADER_ID, null, OrderFragement.this);
        } else {
            emptyList.setText("لا يوجد اتصال بالانترنت");
        }
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("اوردرات");
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.order, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                startActivity(new Intent(getContext(), AddDevice.class));
                break;
        }
        return true;
    }

    @NonNull
    @Override
    public Loader<List<Order>> onCreateLoader(int id, @Nullable Bundle args) {

        return new OrderLoader(getContext(), ApiUrl);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Order>> loader, List<Order> data) {
        progressBar.setVisibility(View.GONE);
        emptyList.setText("لا يوجد اوردرات");
        adapter.clear();
        adapter.addAll(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Order>> loader) {

        adapter.clear();
    }

}
