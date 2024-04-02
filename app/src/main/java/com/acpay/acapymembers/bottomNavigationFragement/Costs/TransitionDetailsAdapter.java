package com.acpay.acapymembers.bottomNavigationFragement.Costs;

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

import java.util.HashSet;
import java.util.List;

public class TransitionDetailsAdapter extends ArrayAdapter<TransitionsDetails> {
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();

    private View.OnClickListener defaultediteBtnClickListener;
    private View.OnClickListener defaultunPayBtnClickListener;
    private View.OnClickListener defaultRequestBtnClickListener;
    String target;

    public TransitionDetailsAdapter(Context context, List<TransitionsDetails> objects,String target) {
        super(context, 0, objects);
        this.target=target;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final TransitionsDetails item = getItem(position);

        View cell=convertView;
        ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = vi.inflate(R.layout.activity_transitions_details_items_content, parent, false);

            viewHolder.place = cell.findViewById(R.id.tarnsition_content_date);

            viewHolder.ContenetorderNum = cell.findViewById(R.id.tarnsition_content_total);
            viewHolder.Contenettime = cell.findViewById(R.id.tarnsition_content_time);

            viewHolder.detailsList = cell.findViewById(R.id.tarnsition_content_list);
            viewHolder.edite=cell.findViewById(R.id.tarnsition_content_edite);

            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)

            viewHolder = (ViewHolder) cell.getTag();
        }

        if (null == item) {
            return cell;
        }


        viewHolder.Contenettime.setText(item.getDate()+" " +item.getTime());

        viewHolder.place.setText(item.getPlace()+"-"+item.getLocation());
        List<Transitions> list = item.getList();
        TransitionsAdapter myListAdapter = new TransitionsAdapter(getContext(),list);
        Log.e("aasasa", String.valueOf(item.getList().size()));
        viewHolder.detailsList.setAdapter(myListAdapter);

        double total =0;
        for (int y=0;y<list.size();y++){
          total +=   Double.parseDouble(list.get(y).getAmount());
        }
        viewHolder.ContenetorderNum.setText(Double.toString(total));
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
        TextView ContenetorderNum;
        TextView Contenettime;
        ListView detailsList;
        TextView edite;
    }
}
