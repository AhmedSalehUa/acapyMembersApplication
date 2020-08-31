package com.acpay.acapymembers.bottomNavigationFragement.Costs;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acpay.acapymembers.R;

import java.util.List;

public class TransitionsAdapter extends ArrayAdapter<Transitions> {
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView =  ((Activity) getContext()).getLayoutInflater().inflate(R.layout.activity_transitions_details_items_content_list, parent, false);

        }
        Transitions items =getItem(position);
        TextView name=(TextView)convertView.findViewById(R.id.activity_transitions_details_items_content_list_name);
        name.setText(items.getDetails());

        TextView amount=(TextView)convertView.findViewById(R.id.activity_transitions_details_items_content_list_amount);
        amount.setText(items.getAmount());

        return convertView;
    }

    public TransitionsAdapter(@NonNull Context context, @NonNull List<Transitions> list) {
        super(context, 0, list);
    }
}
