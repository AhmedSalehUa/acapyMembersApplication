package com.acpay.acapymembers.bottomNavigationFragement.Orders;

import static com.acpay.acapymembers.MainActivity.getAPIHEADER;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.acpay.acapymembers.MainActivity;
import com.acpay.acapymembers.Order.Order;
import com.acpay.acapymembers.Order.OrderFoldingCellAdapter;
import com.acpay.acapymembers.Order.OrderUtilies;
import com.acpay.acapymembers.R;
import com.acpay.acapymembers.bottomNavigationFragement.Notes.AddDevice;
import com.acpay.acapymembers.sendNotification.Data;
import com.acpay.acapymembers.sendNotification.SendNotification;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.List;

public class OrderFragement extends Fragment {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private String ApiUrl;
    OrderFoldingCellAdapter adapter;
    ListView theListView;
    LoaderManager loaderManager;
    private static final int ORDER_LOADER_ID = 1;
    ProgressBar progressBar;
    TextView emptyList;

    public OrderFragement() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.orders_activity, container, false);
        ApiUrl = getAPIHEADER(getContext()) + "/orders.php?type=ok" + "&uid=" + user.getUid();
        final SwipeRefreshLayout pullToRefresh = rootView.findViewById(R.id.pullToRefresh);
        progressBar = (ProgressBar) rootView.findViewById(R.id.listProgressOrder);
        emptyList = (TextView) rootView.findViewById(R.id.listEmptyOrder);
        emptyList.setText("لا يوجد اوردرات");
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressBar.setVisibility(View.VISIBLE);

                ConnectivityManager cm =
                        (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if (isConnected) {
                    performApiRequest();
                } else {
                    emptyList.setText("لا يوجد اتصال بالانترنت");
                }
                pullToRefresh.setRefreshing(false);
                Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
            }
        });

        theListView = rootView.findViewById(R.id.orderMainList);
        theListView.setEmptyView(emptyList);
        theListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition = (theListView == null || theListView.getChildCount() == 0) ? 0 : theListView.getChildAt(0).getTop();
                pullToRefresh.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);

            }
        });

        adapter = new OrderFoldingCellAdapter(getContext(), new ArrayList<Order>());


        theListView.setAdapter(adapter);
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                ((FoldingCell) view).toggle(false);

                adapter.registerToggle(pos);
            }
        });
        performApiRequest();
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("اوردرات");
        return rootView;
    }

    private void performApiRequest() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getAPIHEADER(getContext()),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("onResponse", response);
                        List<Order> orders = OrderUtilies.extractFeuterFromJason(response);
                        setupAdpater(orders);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("onResponse", error.toString());
            }
        });
        stringRequest.setShouldCache(false);
        stringRequest.setShouldRetryConnectionErrors(true);
        stringRequest.setShouldRetryServerErrors(true);
        queue.add(stringRequest);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.order, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                startActivity(new Intent(getContext(), AddDevice.class));
                break;

            case R.id.action_Ip:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("set Static Ip");
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
                input.setText(getAPIHEADER(getContext()));
                container.addView(input, lp);
                alertDialog.setView(container);
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (input.getText().toString().length() > 1) {
                            SharedPreferences.Editor sharedPreferences = getContext().getSharedPreferences("MainActivity", 0).edit();
                            sharedPreferences.putString("api", input.getText().toString());
                            sharedPreferences.commit();
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(getContext(), "canceled", Toast.LENGTH_SHORT).show();
                    }
                }).show();
                break;
        }
        return true;
    }

    String manager1 = "Samaan";
    String manager2 = "Ireny";
//
//    @NonNull
//    @Override
//    public Loader<List<Order>> onCreateLoader(int id, @Nullable Bundle args) {
//
//        return new OrderLoader(getContext(), ApiUrl);
//    }
//

    private void setupAdpater(List<Order> data) {
        progressBar.setVisibility(View.GONE);
        emptyList.setText("لا يوجد اوردرات");
        adapter.clear();
        adapter.addAll(data);

        for (int i = 0; i < adapter.getCount(); i++) {
            final int postion = i;

            adapter.getItem(postion).setTogBtnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapter.registerToggle(postion);
                    adapter.notifyDataSetChanged();
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
                    input.setGravity(Gravity.TOP | Gravity.LEFT);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    input.setLines(1);
                    input.setMaxLines(1);
                    input.setText("test");
                    container.addView(input, lp);
                    alertDialog.setView(container);
                    alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (input.getText().toString().length() > 1) {
                                String api = getAPIHEADER(getContext()) + "/updatenotes.php?order=" + adapter.getItem(postion).getOrderNum() + "&note=" + input.getText().toString();
                                RequestQueue queue = Volley.newRequestQueue(getContext());
                                Log.e("NotesBtn", api);
                                StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                Log.e("NotesBtn", response);
                                                Toast.makeText(getContext(), "done", Toast.LENGTH_SHORT).show();
                                                Data data = new Data(user.getDisplayName(), user.getDisplayName() + " اضاف ملاحظة على الاوردر : " + input.getText().toString(), "ordernotes");
                                                SendNotification send1 = new SendNotification(getContext(), manager1, data);
                                                SendNotification send2 = new SendNotification(getContext(), manager2, data);
                                                onBackPressed();
                                                performApiRequest();
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                });
                                stringRequest.setShouldCache(false);
                                stringRequest.setShouldRetryConnectionErrors(true);
                                stringRequest.setShouldRetryServerErrors(true);
                                queue.add(stringRequest);


                            }
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Toast.makeText(getContext(), "canceled", Toast.LENGTH_SHORT).show();
                        }
                    }).show();
                }
            });

            adapter.getItem(i).setTransitionsBtnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), OrderCostsFragment.class);
                    intent.putExtra("num", adapter.getItem(postion).getOrderNum());
                    intent.putExtra("date", adapter.getItem(postion).getDate());
                    startActivity(intent);
                }
            });
            adapter.getItem(i).setPendingBtnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    pendingOrder.php?order=" + order.getOrderNum()
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    alertDialog.setTitle("اضافة السبب");
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
                    input.setText("test");
                    container.addView(input, lp);
                    alertDialog.setView(container);
                    alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (input.getText().toString().length() > 1) {
                                String api = getAPIHEADER(getContext()) + "/updatenotes.php?order=" + adapter.getItem(postion).getOrderNum() + "&note=" + input.getText().toString();
                                RequestQueue queue = Volley.newRequestQueue(getContext());
                                Log.e("NotesBtn", api);
                                StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                String api = getAPIHEADER(getContext()) + "/pendingOrder.php?order=" + adapter.getItem(postion).getOrderNum();
                                                RequestQueue queue = Volley.newRequestQueue(getContext());
                                                Log.e("NotesBtn", api);
                                                StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                Log.e("NotesBtn", response);
                                                                Toast.makeText(getContext(), "done", Toast.LENGTH_SHORT).show();
                                                                Data data = new Data(user.getDisplayName(), user.getDisplayName() + " اجل الاوردر بسبب : " + input.getText().toString(), "ordernotes");
                                                                SendNotification send1 = new SendNotification(getContext(), manager1, data);
                                                                SendNotification send2 = new SendNotification(getContext(), manager2, data);
                                                                onBackPressed();
                                                                performApiRequest();
                                                            }
                                                        }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {

                                                    }
                                                });
                                                stringRequest.setShouldCache(false);
                                                stringRequest.setShouldRetryConnectionErrors(true);
                                                stringRequest.setShouldRetryServerErrors(true);
                                                queue.add(stringRequest);
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                });
                                stringRequest.setShouldCache(false);
                                stringRequest.setShouldRetryConnectionErrors(true);
                                stringRequest.setShouldRetryServerErrors(true);
                                queue.add(stringRequest);


                            }
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
                                    String api = getAPIHEADER(getContext()) + "/doneOrder.php?order=" + adapter.getItem(postion).getOrderNum();
                                    RequestQueue queue = Volley.newRequestQueue(getContext());
                                    Log.e("NotesBtn", api);
                                    StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    Log.e("NotesBtn", response);
                                                    Toast.makeText(getContext(), "done", Toast.LENGTH_SHORT).show();
                                                    Data data = new Data(user.getDisplayName(), user.getDisplayName() + " انهى الاوردر بنجاح : ", "ordernotes");
                                                    SendNotification send1 = new SendNotification(getContext(), manager1, data);
                                                    SendNotification send2 = new SendNotification(getContext(), manager2, data);
                                                    onBackPressed();
                                                    performApiRequest();
                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    });
                                    stringRequest.setShouldCache(false);
                                    stringRequest.setShouldRetryConnectionErrors(true);
                                    stringRequest.setShouldRetryServerErrors(true);
                                    queue.add(stringRequest);

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
    }

    private void onBackPressed() {
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrderFragement()).commit();
    }


}
