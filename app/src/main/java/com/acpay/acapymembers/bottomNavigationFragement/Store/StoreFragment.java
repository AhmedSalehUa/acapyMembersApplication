package com.acpay.acapymembers.bottomNavigationFragement.Store;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.acpay.acapymembers.R;
import com.acpay.acapymembers.bottomNavigationFragement.Balance.BalanceFragment;
import com.acpay.acapymembers.bottomNavigationFragement.Costs.TransitionDoneFragment;
import com.acpay.acapymembers.bottomNavigationFragement.Costs.TransitionPendingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StoreFragment extends Fragment {

    private BottomNavigationView bottomNav;

    FrameLayout frameLayout;
    public StoreFragment(FrameLayout frameLayout) {
        super();
        this.frameLayout = frameLayout;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) frameLayout.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        frameLayout.setLayoutParams(params);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_store,container,false);
        bottomNav = (BottomNavigationView) rootview.findViewById(R.id.store_navigation);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_store_exit:
                        getFragmentManager().beginTransaction().replace(R.id.store_container, new StoreExitFragment(bottomNav)).commit();
                        break;
                    case R.id.nav_store_returned:
                        getFragmentManager().beginTransaction().replace(R.id.store_container, new StoreReturnedFragment(bottomNav)).commit();
                        break;
                }
                return true;
            }
        });

        getFragmentManager().beginTransaction().replace(R.id.store_container, new StoreExitFragment(bottomNav)).commit();



        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("انتقالات");
        return rootview;
    }
}
