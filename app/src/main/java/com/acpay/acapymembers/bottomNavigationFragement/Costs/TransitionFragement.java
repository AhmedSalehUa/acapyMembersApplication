package com.acpay.acapymembers.bottomNavigationFragement.Costs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.acpay.acapymembers.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TransitionFragement extends Fragment {
    private BottomNavigationView bottomNav;
    FrameLayout frameLayout;

    public TransitionFragement(FrameLayout frameLayout) {
        super();
        this.frameLayout = frameLayout;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) frameLayout.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        frameLayout.setLayoutParams(params);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_transition, container, false);
        bottomNav = (BottomNavigationView) rootview.findViewById(R.id.transition_navigation);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_transitions_pay:
                        getFragmentManager().beginTransaction().replace(R.id.transition_container, new TransitionPendingFragment(bottomNav)).commit();
                        break;
                    case R.id.nav_transitions_paied:
                        getFragmentManager().beginTransaction().replace(R.id.transition_container, new TransitionDoneFragment(bottomNav)).commit();
                        break;
                }
                return true;
            }
        });

        getFragmentManager().beginTransaction().replace(R.id.transition_container, new TransitionPendingFragment(bottomNav)).commit();



        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("انتقالات");
        return rootview;
    }

    private void enableBottomNav() {
        for (int i = 0; i < bottomNav.getMenu().size(); i++) {
            bottomNav.getMenu().getItem(i).setEnabled(true);
        }
    }

    private void disableBottomNav() {
        for (int i = 0; i < bottomNav.getMenu().size(); i++) {
            bottomNav.getMenu().getItem(i).setEnabled(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
}
