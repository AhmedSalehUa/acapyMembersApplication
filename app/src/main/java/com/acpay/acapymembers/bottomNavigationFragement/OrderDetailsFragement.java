package com.acpay.acapymembers.bottomNavigationFragement;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.acpay.acapymembers.Order.Order;
import com.acpay.acapymembers.Order.OrderDone;
import com.acpay.acapymembers.Order.progress.boxes;
import com.acpay.acapymembers.Order.progress.boxesAdapter;
import com.acpay.acapymembers.Order.progress.progressReponser;
import com.acpay.acapymembers.Order.saveProgressReponser;
import com.acpay.acapymembers.R;
import com.acpay.acapymembers.SendNotification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsFragement extends Fragment {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    TextView orderNum;
    TextView userName;
    TextView date;
    TextView time;
    TextView place;
    TextView location;
    TextView dliverCost;
    TextView fixType;
    TextView classMatter;
    TextView notes;

    TextView AddNotes;
    TextView SaveProgress;
    TextView SetTranportCost;
    TextView PendingOrder;
    TextView DoneOrder;


    boxesAdapter boxsAdapter;
    ListView boxesList;
    ProgressBar boxesProgressBar;
    TextView boxesListEmpty;
    ArrayList<boxes> list;

    String manager1 = "Samaan";
    String manager2 = "Ireny";
    Order order;
    String orderString;

    public OrderDetailsFragement(Order order) {
        this.order = order;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.orders_activity_view_details, container, false);
        boxesList = rootView.findViewById(R.id.checkBoxesList);
        boxsAdapter = new boxesAdapter(getContext(), new ArrayList<boxes>());

        boxesProgressBar = (ProgressBar) rootView.findViewById(R.id.checkBoxesListProgress);
        boxesListEmpty = (TextView) rootView.findViewById(R.id.checkBoxesListNoItems);
        list = new ArrayList<>();
        setBoxListDetails();

        setDetails(rootView);
        AddOnClickListenerEvents(rootView);

        return rootView;
    }

    private void setBoxListDetails() {
        boxsAdapter.clear();
        boxesList.setEmptyView(boxesListEmpty);
        String boxesapi = "https://www.app.acapy-trade.com/progress.php?order=" + order.getOrderNum();

        final progressReponser boxesJason = new progressReponser();
        boxesJason.setFinish(false);
        boxesJason.execute(boxesapi);
        final Handler boxeshandler = new Handler();
        Runnable boxesrunnableCode = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                if (boxesJason.isFinish()) {

                    orderString = boxesJason.getUserId();
                    boxsAdapter.addAll(fetchBoxsJason(orderString));
                    boxesProgressBar.setVisibility(View.GONE);
                    boxesList.setAdapter(boxsAdapter);
                    ListAdapter myListAdapter = boxesList.getAdapter();
                    if (myListAdapter == null) {

                        return;
                    }

                    int totalHeight = 0;
                    for (int size = 0; size < myListAdapter.getCount(); size++) {
                        View listItem = myListAdapter.getView(size, null, boxesList);
                        listItem.measure(0, 0);
                        totalHeight += listItem.getMeasuredHeight() + 85;
                    }

                    ViewGroup.LayoutParams params = boxesList.getLayoutParams();
                    params.height = totalHeight + (boxesList.getDividerHeight() * (myListAdapter.getCount() - 1));
                    boxesList.setLayoutParams(params);

                    for (int i = 0; i < boxsAdapter.getCount(); i++) {
                        final int postion = i;
                        boxsAdapter.getItem(i).setCheckListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                boxes s = boxsAdapter.getItem(postion);
                                if (compoundButton.isChecked()) {
                                    list.add(s);
                                } else {
                                    list.remove(s);
                                }
                            }
                        });
                    }
                } else {
                    boxeshandler.postDelayed(this, 100);
                }
            }
        };
        boxeshandler.post(boxesrunnableCode);
    }

    private void setDetails(View rootview) {

        orderNum = rootview.findViewById(R.id.content_orderNum);
        orderNum.setText(order.getOrderNum());

        time = rootview.findViewById(R.id.content_time_label);
        time.setText(order.getTime());

        date = rootview.findViewById(R.id.content_date_label);
        date.setText(order.getDate());

        place = rootview.findViewById(R.id.content_place);
        place.setText(order.getPlace());

        location = rootview.findViewById(R.id.content_location);
        location.setText(order.getLocation());

        dliverCost = rootview.findViewById(R.id.content_dliverCost);
        dliverCost.setText(order.getDliverCost());

        classMatter = rootview.findViewById(R.id.content_matter);
        classMatter.setText(order.getClassMatter());

        fixType = rootview.findViewById(R.id.content_fixType);
        fixType.setText(order.getFixType());

        notes = rootview.findViewById(R.id.content_notes);
        notes.setText(order.getNotes());

    }

    private void AddOnClickListenerEvents(View rootview) {
        AddNotes = (TextView) rootview.findViewById(R.id.content_note_btn);
        AddNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("اضافة للملاحظات");
                LinearLayout container = new LinearLayout(getContext());
                container.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(20, 20, 20, 20);
                final EditText input = new EditText(getContext());
                input.setLayoutParams(lp);
                input.setGravity(Gravity.TOP | Gravity.LEFT);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setLines(1);
                input.setMaxLines(1);
                input.setText(order.getNotes());
                container.addView(input, lp);

                alertDialog.setView(container);
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        final OrderDone orderDone = new OrderDone(order, "updateNotes", input.getText().toString());
                        final Handler handler = new Handler();
                        Runnable runnableCode = new Runnable() {
                            @Override
                            public void run() {
                                if (orderDone.isFinish()) {
                                    if (orderDone.getResponse().equals("1")) {
                                        Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                                        SendNotification send1 = new SendNotification(getContext(),manager1, user.getDisplayName(), "تم اضافة ملاحظة");
                                        SendNotification send2 = new SendNotification(getContext(),manager2, user.getDisplayName(), "تم اضافة ملاحظة");

                                        onBackPressed();
                                    } else {
                                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    handler.postDelayed(this, 100);
                                }
                            }
                        };
                        handler.post(runnableCode);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(getContext(), "canceled", Toast.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });

        SaveProgress = (TextView) rootview.findViewById(R.id.content_save_progress);
        SaveProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.size() > 0) {
                    String api = "https://www.app.acapy-trade.com/updatenotesprog.php?order=" + order.getOrderNum();
                    for (int x = 0; x < list.size(); x++) {
                        boxes so = list.get(x);
                        api += "&progress[]=" + so.getName() + "&note[]=" + so.getNotes();
                    }
                    Log.w("api", api);
                    final saveProgressReponser updateProgress = new saveProgressReponser();
                    updateProgress.setFinish(false);
                    updateProgress.execute(api);
                    final Handler handlerProg = new Handler();
                    Runnable runnableCodeProg = new Runnable() {
                        @Override
                        public void run() {
                            if (updateProgress.isFinish()) {
                                String res = updateProgress.getUserId();
                                if (!res.equals("0")) {
                                    Toast.makeText(getContext(), "Saved", Toast.LENGTH_LONG).show();
                                    SendNotification send1 = new SendNotification(getContext(),manager1, user.getDisplayName(), "تم تحديث الخطوات");
                                    SendNotification send2 = new SendNotification(getContext(),manager2, user.getDisplayName(), "تم تحديث الخطوات");

                                    setBoxListDetails();

                                } else {
                                    Toast.makeText(getContext(), "not saved", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                handlerProg.postDelayed(this, 100);
                            }
                        }
                    };
                    handlerProg.post(runnableCodeProg);
                }
            }
        });

        SetTranportCost = (TextView) rootview.findViewById(R.id.content_cost_btn);
        SetTranportCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), OrderCostsFragment.class);
                intent.putExtra("num", order.getOrderNum());
                startActivity(intent);
            }
        });

        PendingOrder = (TextView) rootview.findViewById(R.id.content_request_btn);
        PendingOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("يرجى اضافة السبب للملاحظات اولا").setTitle("هل تريد تاجيل الاوردر لوجود مشكلة")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                final OrderDone orderDone = new OrderDone(order, "pending");
                                final Handler handler = new Handler();
                                Runnable runnableCode = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (orderDone.isFinish()) {
                                            if (orderDone.getResponse().equals("1")) {
                                                Toast.makeText(getContext(), "Pended", Toast.LENGTH_SHORT).show();
                                                SendNotification send1 = new SendNotification(getContext(),manager1, user.getDisplayName(), "تم تاجيل الاوردر");
                                                SendNotification send2 = new SendNotification(getContext(),manager2, user.getDisplayName(), "تم تاجيل الاوردر");

                                                onBackPressed();
                                            } else {
                                                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            handler.postDelayed(this, 100);
                                        }
                                    }
                                };
                                handler.post(runnableCode);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();
            }
        });

        DoneOrder = (TextView) rootview.findViewById(R.id.content_done_btn);
        DoneOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("هل انت متاكد من ان الاوردر انتهى ؟!")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                final OrderDone orderDone = new OrderDone(order, "done");
                                final Handler handler = new Handler();
                                Runnable runnableCode = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (orderDone.isFinish()) {
                                            if (orderDone.getResponse().equals("1")) {
                                                Toast.makeText(getContext(), "تم الحفظ", Toast.LENGTH_SHORT).show();
                                                SendNotification send1 = new SendNotification(getContext(),manager1, user.getDisplayName(), "انتهى الاوردر");
                                                SendNotification send2 = new SendNotification(getContext(),manager2, user.getDisplayName(), "انتهى الاوردر");

                                                onBackPressed();
                                            } else {
                                                Toast.makeText(getContext(), "خطا", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            handler.postDelayed(this, 100);
                                        }
                                    }
                                };
                                handler.post(runnableCode);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    private void onBackPressed() {
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrderFragement()).commit();
    }

    private List<boxes> fetchBoxsJason(String boxesJasonResponse) {

        final List<boxes> boxes = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(boxesJasonResponse);
            JSONArray sa = jsonObject.names();
            Log.e("jsonObject", sa.length() + "");
            for (int i = 0; i < sa.length(); i++) {
                JSONObject jsonArrayId = jsonObject.getJSONObject(sa.get(i).toString());
                boxes items = new boxes(jsonArrayId.getString("progress_name"),
                        jsonArrayId.getString("statue"),
                        jsonArrayId.getString("notes"));
                boxsAdapter.add(items);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return boxes;
    }

}
