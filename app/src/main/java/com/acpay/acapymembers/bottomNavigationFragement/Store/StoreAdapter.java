package com.acpay.acapymembers.bottomNavigationFragement.Store;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acpay.acapymembers.R;
import com.acpay.acapymembers.bottomNavigationFragement.Costs.Transitions;
import com.acpay.acapymembers.bottomNavigationFragement.Costs.TransitionsAdapter;

import java.util.HashSet;
import java.util.List;

public class StoreAdapter extends ArrayAdapter<storeItems> {
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();

    private View.OnClickListener defaultediteBtnClickListener;
    private View.OnClickListener defaultunPayBtnClickListener;
    private View.OnClickListener defaultRequestBtnClickListener;
    String target;

    public StoreAdapter(Context context, List<storeItems> objects, String target) {
        super(context, 0, objects);
        this.target=target;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final storeItems item = getItem(position);

        View cell=convertView;
         ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new  ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = vi.inflate(R.layout.activity_store_items, parent, false);

            viewHolder.place = cell.findViewById(R.id.store_show_details);
            viewHolder.date = cell.findViewById(R.id.store_show_date);
            viewHolder.detailsList = cell.findViewById(R.id.list_of_products);
            viewHolder.edite=cell.findViewById(R.id.tarnsition_content_delete);

            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)

            viewHolder = ( ViewHolder) cell.getTag();
        }

        if (null == item) {
            return cell;
        }



        viewHolder.place.setText(item.getDetails());
        viewHolder.date.setText(item.getDate());
        List<Transitions> list = item.getList();
        TransitionsAdapter myListAdapter = new TransitionsAdapter(getContext(),list);
        viewHolder.detailsList.setAdapter(myListAdapter);

        double total =0;
        for (int y=0;y<list.size();y++){
            total +=   Double.parseDouble(list.get(y).getAmount());
        }
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, viewHolder.detailsList);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight()+50 ;
        }

        ViewGroup.LayoutParams params = viewHolder.detailsList.getLayoutParams();
        params.height = totalHeight + (viewHolder.detailsList.getDividerHeight() * (myListAdapter.getCount() - 1));
        viewHolder.detailsList.setLayoutParams(params);
        if (item.getEditeBtn() != null){
            viewHolder.edite.setOnClickListener(item.getEditeBtn());

        }else {
            viewHolder.edite.setOnClickListener(defaultediteBtnClickListener);

        }


        return cell;
    }


    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;

    }

    private static class ViewHolder {
        TextView place;
        TextView date;
        ListView detailsList;
        TextView edite;
    }
}
