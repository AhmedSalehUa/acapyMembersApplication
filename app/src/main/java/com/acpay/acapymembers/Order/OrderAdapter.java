package com.acpay.acapymembers.Order;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acpay.acapymembers.R;
import com.acpay.acapymembers.floadingCell.FoldingCell;

import java.util.HashSet;
import java.util.List;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class OrderAdapter extends ArrayAdapter<Order> {
    TextView orderNum;
    TextView userName;
    TextView date;
    TextView time;
    TextView place;
    TextView location;
    TextView dliverCost;
    TextView fixType;
    TextView classMatter;


    public OrderAdapter(@NonNull Context context, List<Order> orders) {
        super(context, 0, orders);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rootview;
        if (convertView == null) {
            rootview = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.orders_activity_list_header, parent, false);
        } else {
            rootview = convertView;
        }
        Order item = getItem(position);
        orderNum = rootview.findViewById(R.id.title_orderNum);
        orderNum.setText(item.getOrderNum());

        time = rootview.findViewById(R.id.title_time_label);
        time.setText(item.getTime());

        date = rootview.findViewById(R.id.title_date_label);
        date.setText(item.getDate());

        place = rootview.findViewById(R.id.title_place);
        place.setText(item.getPlace());

        location = rootview.findViewById(R.id.title_location);
        location.setText(item.getLocation());

        dliverCost = rootview.findViewById(R.id.title_dliverCost);
        dliverCost.setText(item.getDliverCost());

        classMatter = rootview.findViewById(R.id.title_matter);
        classMatter.setText(item.getClassMatter());

        fixType = rootview.findViewById(R.id.title_fixType);
        fixType.setText(item.getFixType());

        return rootview;
    }
}
