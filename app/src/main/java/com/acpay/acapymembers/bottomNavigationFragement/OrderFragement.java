package com.acpay.acapymembers.bottomNavigationFragement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.app.LoaderManager.LoaderCallbacks;
import androidx.loader.content.Loader;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.acpay.acapymembers.R;
import com.acpay.acapymembers.User;
import com.acpay.acapymembers.floadingCell.FoldingCell;
import com.acpay.acapymembers.oreder.Order;
import com.acpay.acapymembers.oreder.OrderAdapter;
import com.acpay.acapymembers.oreder.OrderDone;
import com.acpay.acapymembers.oreder.OrderLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class OrderFragement extends Fragment implements LoaderCallbacks<List<Order>> {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    User usera1 = new User(user.getDisplayName(), user.getUid());
    String uid = usera1.getUserId();
    private String ApiUrl;
    OrderAdapter adapter;
    ListView theListView;
    LoaderManager loaderManager;
    private static final int ORDER_LOADER_ID = 1;

    public OrderFragement() {
        ApiUrl = " https://www.app.acapy-trade.com/orders.php?type=ok" + "&uid=" + this.uid;
        Log.w("call order fragment ", ApiUrl);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.orders_activity, container, false);
        final SwipeRefreshLayout pullToRefresh = rootView.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loaderManager.restartLoader(ORDER_LOADER_ID, null, OrderFragement.this);
                loaderManager.initLoader(ORDER_LOADER_ID, null, OrderFragement.this);
                pullToRefresh.setRefreshing(false);
                Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
            }
        });

        theListView = rootView.findViewById(R.id.order_list);


        adapter = new OrderAdapter(getContext(), new ArrayList<Order>());


        theListView.setAdapter(adapter);
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Log.e("asas", "clicked");
                ((FoldingCell) view).toggle(false);

                adapter.registerToggle(pos);

            }
        });

        loaderManager = getLoaderManager();
        loaderManager.initLoader(ORDER_LOADER_ID, null, OrderFragement.this);
        return rootView;
    }

    @NonNull
    @Override
    public Loader<List<Order>> onCreateLoader(int id, @Nullable Bundle args) {

        return new OrderLoader(getContext(), ApiUrl);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Order>> loader, List<Order> data) {

        adapter.clear();

        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
            for (int i = 0; i < adapter.getCount(); i++) {
                final int postion = i;

                adapter.getItem(i).setRequestBtnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("يرجى اضافة السبب للملاحظات اولا").setTitle("هل تريد تاجيل الاوردر لوجود مشكلة")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                        final OrderDone orderDone = new OrderDone(adapter.getItem(postion), "pending");
                                        final Handler handler = new Handler();
                                        Runnable runnableCode = new Runnable() {
                                            @Override
                                            public void run() {
                                                if (orderDone.isFinish()) {
                                                    if (orderDone.getResponse().equals("1")) {
                                                        Toast.makeText(getContext(), "Pended", Toast.LENGTH_SHORT).show();
                                                        loaderManager.restartLoader(ORDER_LOADER_ID, null, OrderFragement.this);
                                                        loaderManager.initLoader(ORDER_LOADER_ID, null, OrderFragement.this);
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
                adapter.getItem(i).setAddNotesBtnClickListener(new View.OnClickListener() {
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
                        input.setGravity(android.view.Gravity.TOP | android.view.Gravity.LEFT);
                        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                        input.setLines(1);
                        input.setMaxLines(1);
                        input.setText(adapter.getItem(postion).getNotes());
                        container.addView(input, lp);

                        alertDialog.setView(container);
                        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                final OrderDone orderDone = new OrderDone(adapter.getItem(postion), "updateNotes",input.getText().toString());
                                final Handler handler = new Handler();
                                Runnable runnableCode = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (orderDone.isFinish()) {
                                            if (orderDone.getResponse().equals("1")) {
                                                Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                                                loaderManager.restartLoader(ORDER_LOADER_ID, null, OrderFragement.this);
                                                loaderManager.initLoader(ORDER_LOADER_ID, null, OrderFragement.this);
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
                adapter.getItem(i).setDoneBtnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("هل انت متاكد من ان الاوردر انتهى ؟!")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                        final OrderDone orderDone = new OrderDone(adapter.getItem(postion), "done");
                                        final Handler handler = new Handler();
                                        Runnable runnableCode = new Runnable() {
                                            @Override
                                            public void run() {
                                                if (orderDone.isFinish()) {
                                                    if (orderDone.getResponse().equals("1")) {
                                                        Toast.makeText(getContext(), "تم الحفظ", Toast.LENGTH_SHORT).show();
                                                        loaderManager.restartLoader(ORDER_LOADER_ID, null, OrderFragement.this);
                                                        loaderManager.initLoader(ORDER_LOADER_ID, null, OrderFragement.this);
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
                adapter.getItem(i).setSaveBtnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(),OrderProgressFragment.class);
                        intent.putExtra("num",adapter.getItem(postion).getOrderNum());
                        startActivity(intent);
                    }
                });
                adapter.getItem(i).setCostBtnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(),OrderCostsFragment.class);
                        intent.putExtra("num",adapter.getItem(postion).getOrderNum());
                        startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Order>> loader) {

        adapter.clear();
    }

}
