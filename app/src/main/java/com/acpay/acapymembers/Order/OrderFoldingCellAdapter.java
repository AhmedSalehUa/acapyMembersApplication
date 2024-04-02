package com.acpay.acapymembers.Order;

import static com.acpay.acapymembers.MainActivity.getAPIHEADER;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.acpay.acapymembers.Order.progress.boxes;
import com.acpay.acapymembers.Order.progress.boxesAdapter;
import com.acpay.acapymembers.R;
import com.acpay.acapymembers.sendNotification.Data;
import com.acpay.acapymembers.sendNotification.SendNotification;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "unused"})
public class OrderFoldingCellAdapter extends ArrayAdapter<Order> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultPendingBtnClickListener;
    private View.OnClickListener defaultaddNotesBtnClickListener;
    private View.OnClickListener defaultSaveProgressBtnClickListener;
    private View.OnClickListener defaultDoneBtnClickListener;
    private View.OnClickListener defaultTransitionsBtnClickListener;
    private View.OnClickListener defaulttogBtnClickListener;
    private View.OnClickListener toggle;

    private int PendingBtn;
    private int addNotes;
    private int SaveProgress;
    private int DoneBtn;
    private int TransitionsBtn;
    boxesAdapter adapter;
    ArrayList<boxes> list = new ArrayList<>();

    String manager1 = "Samaan";
    String manager2 = "Ireny";

    public OrderFoldingCellAdapter(Context context, List<Order> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // get item for selected view
        final Order item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell rootview = (FoldingCell) convertView;
        ViewHolder viewHolder;
        if (rootview == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            rootview = (FoldingCell) vi.inflate(R.layout.orders_activity_list, parent, false);

            viewHolder.orderNum = rootview.findViewById(R.id.title_orderNum);
            viewHolder.content_orderNum = rootview.findViewById(R.id.content_orderNum);
            viewHolder.UserName = (TextView) rootview.findViewById(R.id.title_username);
            viewHolder.time = rootview.findViewById(R.id.title_time_label);
            viewHolder.date = rootview.findViewById(R.id.title_date_label);
            viewHolder.place = rootview.findViewById(R.id.title_place);
            viewHolder.location = rootview.findViewById(R.id.title_location);
            viewHolder.dliverCost = rootview.findViewById(R.id.title_dliverCost);
            viewHolder.classMatter = rootview.findViewById(R.id.title_matter);
            viewHolder.fixType = rootview.findViewById(R.id.title_fixType);

            viewHolder.content_time = rootview.findViewById(R.id.content_time_label);
            viewHolder.content_date = rootview.findViewById(R.id.content_date_label);
            viewHolder.content_place = rootview.findViewById(R.id.content_place);
            viewHolder.content_location = rootview.findViewById(R.id.content_location);
            viewHolder.content_dliverCost = rootview.findViewById(R.id.content_dliverCost);
            viewHolder.content_classMatter = rootview.findViewById(R.id.content_matter);
            viewHolder.content_fixType = rootview.findViewById(R.id.content_fixType);
            viewHolder.content_notes = rootview.findViewById(R.id.content_notes);
            viewHolder.progressList = rootview.findViewById(R.id.checkBoxesList);
            viewHolder.emptyList = rootview.findViewById(R.id.checkBoxesListNoItems);
            viewHolder.listProg = rootview.findViewById(R.id.checkBoxesListProgress);

            viewHolder.addNote = rootview.findViewById(R.id.content_note_btn);
            viewHolder.Done = rootview.findViewById(R.id.content_done_btn);
            viewHolder.SaveProgress = rootview.findViewById(R.id.content_save_progress);
            viewHolder.Pending = rootview.findViewById(R.id.content_pending_btn);

            viewHolder.tog = rootview.findViewById(R.id.toggle);
            rootview.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                rootview.unfold(true);
            } else {
                rootview.fold(true);
            }
            viewHolder = (ViewHolder) rootview.getTag();
        }

        if (null == item)
            return rootview;

        // bind data from selected element to view through view holder
        viewHolder.orderNum.setText(item.getOrderNum());
        viewHolder.content_orderNum.setText(item.getUserName());
        viewHolder.UserName.setText(item.getUserName());
        viewHolder.time.setText(item.getTime());
        viewHolder.date.setText(item.getDate());
        viewHolder.place.setText(item.getPlace());
        viewHolder.location.setText(item.getLocation());
        viewHolder.dliverCost.setText(item.getDliverCost());
        viewHolder.classMatter.setText(item.getClassMatter());
        viewHolder.fixType.setText(item.getFixType());
        viewHolder.content_time.setText(item.getTime());
        viewHolder.content_date.setText(item.getDate());
        viewHolder.content_place.setText(item.getPlace());
        viewHolder.content_location.setText(item.getLocation());
        viewHolder.content_dliverCost.setText(item.getDliverCost());
        viewHolder.content_classMatter.setText(item.getClassMatter());
        viewHolder.content_fixType.setText(item.getFixType());
        viewHolder.content_notes.setText(item.getNotes());
        if (item.getProgressList().size() > 0) {
            adapter = new boxesAdapter(getContext(), item.getProgressList());
            viewHolder.progressList.setAdapter(adapter);
            viewHolder.listProg.setVisibility(View.GONE);
            viewHolder.emptyList.setVisibility(View.GONE);

            for (int i = 0; i < adapter.getCount(); i++) {
                final int postion = i;
                boxes s = adapter.getItem(postion);
                if (s.getValue().equals("no")) {

                    list.add(s);
                    Log.e("added", s.getName());
                }
                adapter.getItem(i).setCheckListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        boxes s = adapter.getItem(postion);
                        if (compoundButton.isChecked()) {
                            list.add(s);
                            Log.e("added", s.getName());
                        } else {
                            list.remove(s);
                            Log.e("remove", s.getName());
                        }
                    }
                });
            }
            setListViewHeightBasedOnChildren(viewHolder.progressList);
        } else {
            viewHolder.listProg.setVisibility(View.GONE);
            viewHolder.emptyList.setText("لا يوجد خطوات");
        }


        viewHolder.addNote.setVisibility(item.getAddNotes());
        viewHolder.Done.setVisibility(item.getDoneBtn());
          viewHolder.SaveProgress.setVisibility(item.getSaveProgressBtn());
        viewHolder.Pending.setVisibility(item.getPendingBtn());

        if (item.getAddNotes() != -1) {
            viewHolder.addNote.setVisibility(item.getAddNotes());
        } else {
            viewHolder.addNote.setVisibility(addNotes);
        }



        if (item.getDoneBtn() != -1) {
            viewHolder.Done.setVisibility(item.getDoneBtn());
        } else {
            viewHolder.Done.setVisibility(DoneBtn);
        }

        if (item.getSaveProgressBtn() != -1) {
            viewHolder.SaveProgress.setVisibility(item.getSaveProgressBtn());
        } else {
            viewHolder.SaveProgress.setVisibility(SaveProgress);
        }

        if (item.getPendingBtn() != -1) {
            viewHolder.Pending.setVisibility(item.getPendingBtn());
        } else {
            viewHolder.Pending.setVisibility(PendingBtn);
        }

        if (item.getAddNotesBtnClickListener() != null) {
            viewHolder.addNote.setOnClickListener(item.getAddNotesBtnClickListener());
        } else {
            viewHolder.addNote.setOnClickListener(defaultaddNotesBtnClickListener);
        }

        if (item.getDoneBtnClickListener() != null) {
            viewHolder.Done.setOnClickListener(item.getDoneBtnClickListener());
        } else {
            viewHolder.Done.setOnClickListener(defaultDoneBtnClickListener);
        }

        if (item.getSaveProgressBtnClickListener() != null) {
            viewHolder.SaveProgress.setOnClickListener(item.getSaveProgressBtnClickListener());
        } else {
            viewHolder.SaveProgress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.e("tabbed", "ok");
                    if (list.size() > 0) {
                        String api = getAPIHEADER(getContext())+"/updatenotesprog.php?order=" + item.getOrderNum();
                        for (int x = 0; x < list.size(); x++) {
                            boxes so = list.get(x);
                            api += "&progress[]=" + so.getName() + "&note[]=" + so.getNotes();
                        }
                        Log.w("api", api);
                        RequestQueue queue = Volley.newRequestQueue(getContext());

                        StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Toast.makeText(getContext(), "Saved", Toast.LENGTH_LONG).show();
                                        Data data = new Data(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), "تم تحديث الخطوات", "ordernotes");
                                        SendNotification send1 = new SendNotification(getContext(), manager1, data);
                                        SendNotification send2 = new SendNotification(getContext(), manager2, data);
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Log.e("onResponse", error.toString());
                            }
                        });
                        stringRequest.setShouldCache(false);
                        queue.add(stringRequest);

                    }
                }
            });
        }

        if (item.getPendingBtnClickListener() != null) {
            viewHolder.Pending.setOnClickListener(item.getPendingBtnClickListener());
        } else {
            viewHolder.Pending.setOnClickListener(defaultPendingBtnClickListener);
        }
        if (item.getTogBtnClickListener() != null) {
            viewHolder.tog.setOnClickListener(item.getTogBtnClickListener());
        } else {
            viewHolder.tog.setOnClickListener(defaulttogBtnClickListener);
        }

        return rootview;
    }
    public void setListViewHeightBasedOnChildren(ListView listView) {
        int numberOfItems = adapter.getCount();

        // Get total height of all items.
        int totalItemsHeight = 0;
        for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
            View item = adapter.getView(itemPos, null, listView);
            float px = 500 * (listView.getResources().getDisplayMetrics().density);
            item.measure(View.MeasureSpec.makeMeasureSpec((int) px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalItemsHeight += item.getMeasuredHeight();
        }

        // Get total height of all item dividers.
        int totalDividersHeight = listView.getDividerHeight() *
                (numberOfItems - 1);
        // Get padding
        int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();

        // Set list height.
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = numberOfItems * 200;
        Log.e("params.height", String.valueOf(params.height));
        Log.e("listView.height", String.valueOf(listView.getLayoutParams().height));
        listView.setLayoutParams(params);
        listView.requestLayout();Log.e("listView.requestLayout", String.valueOf(listView.getLayoutParams().height));
        //setDynamicHeight(listView);

//        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
//                View.MeasureSpec.UNSPECIFIED);
//        int totalHeight = 0;
//        View view = null;
//        for (int i = 0; i < adapter.getCount(); i++) {
//            view = adapter.getView(i, view, listView);
//            if (i == 0)
//                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,
//                        ViewGroup.LayoutParams.WRAP_CONTENT));
//
//            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
//            totalHeight += view.getMeasuredHeight();
//        }
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight
//                + (listView.getDividerHeight() * (adapter.getCount() - 1));
//        listView.setLayoutParams(params);
//        listView.requestLayout();
    }
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

    private static class ViewHolder {
        TextView orderNum;

        TextView content_orderNum;
        TextView UserName;

        TextView date;
        TextView content_date;

        TextView time;
        TextView content_time;

        TextView place;
        TextView content_place;

        TextView location;
        TextView content_location;

        TextView dliverCost;
        TextView content_dliverCost;

        TextView fixType;
        TextView content_fixType;

        TextView classMatter;
        TextView content_classMatter;
        TextView content_notes;
        ListView progressList;
        TextView emptyList;
        ProgressBar listProg;

        TextView addNote;
         TextView Done;
        TextView SaveProgress;
        TextView Pending;

        ImageView tog;
    }
}