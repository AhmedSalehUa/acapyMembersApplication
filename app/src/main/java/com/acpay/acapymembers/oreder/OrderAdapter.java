package com.acpay.acapymembers.oreder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acpay.acapymembers.R;
import com.acpay.acapymembers.floadingCell.FoldingCell;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class OrderAdapter extends ArrayAdapter<Order> {
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();

    private View.OnClickListener defaultRequestBtnClickListener;
    private View.OnClickListener defaultAddNotesBtnClickListener;
    private View.OnClickListener defaultDoneBtnClickListener;
    private View.OnClickListener defaultSaveBtnClickListener;
    private View.OnClickListener costBtnClickListener;
    private AdapterView.OnItemClickListener  defaultListBtnClickListener;

    public OrderAdapter(Context context, List<Order> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Order item = getItem(position);

        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.orders_activity_list, parent, false);

            viewHolder.orderNum = cell.findViewById(R.id.title_orderNum);
            viewHolder.time = cell.findViewById(R.id.title_time_label);
            viewHolder.date = cell.findViewById(R.id.title_date_label);
            viewHolder.place = cell.findViewById(R.id.title_place);
            viewHolder.location = cell.findViewById(R.id.title_location);
            viewHolder.dliverCost = cell.findViewById(R.id.title_dliverCost);
            viewHolder.classMatter = cell.findViewById(R.id.title_matter);
            viewHolder.fixType = cell.findViewById(R.id.title_fixType);

            viewHolder.ContenetorderNum = cell.findViewById(R.id.content_orderNum);
            viewHolder.Contenettime = cell.findViewById(R.id.content_time_label);
            viewHolder.Contenetdate = cell.findViewById(R.id.content_date_label);
            viewHolder.Contenetplace = cell.findViewById(R.id.content_place);
            viewHolder.Contenetlocation = cell.findViewById(R.id.content_location);
            viewHolder.ContenetdliverCost = cell.findViewById(R.id.content_dliverCost);
            viewHolder.ContenetclassMatter = cell.findViewById(R.id.title_matter);
            viewHolder.ContenetfixType = cell.findViewById(R.id.content_fixType);
             viewHolder.wrap_list=cell.findViewById(R.id.wrap_list);

            viewHolder.notes = cell.findViewById(R.id.content_notes);
            viewHolder.file = cell.findViewById(R.id.content_files);

            viewHolder.pending = cell.findViewById(R.id.content_request_btn);
            viewHolder.AddNotes = cell.findViewById(R.id.content_note_btn);
            viewHolder.Done = cell.findViewById(R.id.content_done_btn);
            viewHolder.Save = cell.findViewById(R.id.content_save_btn);
            viewHolder.Cost = cell.findViewById(R.id.content_cost_btn);
            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        if (null == item) {
            return cell;
        }
        viewHolder.orderNum.setText(item.getOrderNum());
        viewHolder.ContenetorderNum.setText(item.getOrderNum());

        viewHolder.time.setText(item.getTime());
        viewHolder.Contenettime.setText(item.getTime());

        viewHolder.date.setText(item.getDate());
        viewHolder.Contenetdate.setText(item.getDate());

        viewHolder.place.setText(item.getPlace());
        viewHolder.Contenetplace.setText(item.getPlace());

        viewHolder.location.setText(item.getLocation());
        viewHolder.Contenetlocation.setText(item.getLocation());

        viewHolder.dliverCost.setText(item.getDliverCost());
        viewHolder.ContenetdliverCost.setText(item.getDliverCost());

        viewHolder.classMatter.setText(item.getClassMatter());
        viewHolder.ContenetclassMatter.setText(item.getClassMatter());

        viewHolder.fixType.setText(item.getFixType());
        viewHolder.ContenetfixType.setText(item.getFixType());

        viewHolder.notes.setText(item.getNotes());
        viewHolder.file.setText(item.getFile());
        // set custom btn handler for list item from that item
        if (item.getRequestBtnClickListener() != null) {
            viewHolder.pending.setOnClickListener(item.getRequestBtnClickListener());

        } else {
            // (optionally) add "default" handler if no handler found in item
            viewHolder.pending.setOnClickListener(defaultRequestBtnClickListener);

        }
        if (item.getAddNotesBtnClickListener() != null) {
            viewHolder.AddNotes.setOnClickListener(item.getAddNotesBtnClickListener());

        } else {
            // (optionally) add "default" handler if no handler found in item
            viewHolder.AddNotes.setOnClickListener(defaultAddNotesBtnClickListener);

        }
        if (item.getDoneBtnClickListener() != null) {
            viewHolder.Done.setOnClickListener(item.getDoneBtnClickListener());

        } else {
            // (optionally) add "default" handler if no handler found in item
            viewHolder.pending.setOnClickListener(defaultDoneBtnClickListener);

        }
        if (item.getSaveBtnClickListener() != null) {
            viewHolder.Save.setOnClickListener(item.getSaveBtnClickListener());

        } else {
              viewHolder.Save.setOnClickListener(defaultSaveBtnClickListener);

        }

        if (item.getCostBtnClickListener() != null) {
            viewHolder.Cost.setOnClickListener(item.getCostBtnClickListener());

        } else {
            viewHolder.Cost.setOnClickListener(costBtnClickListener);
        }

        return cell;
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;

    }

    private static class ViewHolder {
        TextView orderNum;
        TextView date;
        TextView time;
        TextView place;
        TextView location;
        TextView dliverCost;
        TextView fixType;
        TextView classMatter;
        TextView ContenetorderNum;
        TextView Contenetdate;
        TextView Contenettime;
        TextView Contenetplace;
        TextView Contenetlocation;
        TextView ContenetdliverCost;
        TextView ContenetfixType;
        TextView ContenetclassMatter;
        TextView notes;
        TextView file;
        ImageView wrap_list;

        TextView pending;
        TextView AddNotes;
        TextView Done;
        TextView Save;
        TextView Cost;
    }
}
