package com.acpay.acapymembers.oreder.progress;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acpay.acapymembers.R;

import java.util.List;

public class boxesAdapter extends ArrayAdapter<boxes> {

    private CheckBox.OnCheckedChangeListener defCheckListener;
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView =  ((Activity) getContext()).getLayoutInflater().inflate(R.layout.orders_activity_list_content_checkboxes, parent, false);

        }
        boxes items =getItem(position);

        TextView text=(TextView)convertView.findViewById(R.id.chechbox_text);
        text.setText(items.getName());

        TextView note=(TextView)convertView.findViewById(R.id.chechbox_note);
        note.setText(items.getNotes());

        CheckBox co=(CheckBox)convertView.findViewById(R.id.chechbox_box);
        co.setChecked(Boolean.parseBoolean(items.getValue()));

        if (items.getCheckListener() == null ){

        }
        if (items.getCheckListener() != null) {
            co.setOnCheckedChangeListener(items.getCheckListener());
        } else {
            co.setOnCheckedChangeListener(defCheckListener);
        }
        return convertView;
    }

    public boxesAdapter(@NonNull Context context, @NonNull List<boxes> list) {
        super(context, 0, list);
    }
}
