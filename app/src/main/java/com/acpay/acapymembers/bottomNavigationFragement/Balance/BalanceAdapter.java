package com.acpay.acapymembers.bottomNavigationFragement.Balance;

import static com.acpay.acapymembers.MainActivity.getAPIHEADER;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acpay.acapymembers.R;
import com.acpay.acapymembers.bottomNavigationFragement.Costs.Transitions;
import com.acpay.acapymembers.bottomNavigationFragement.Costs.TransitionsAdapter;
import com.acpay.acapymembers.bottomNavigationFragement.Costs.TransitionsDetails;
import com.acpay.acapymembers.sendNotification.Data;
import com.acpay.acapymembers.sendNotification.SendNotification;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashSet;
import java.util.List;

public class BalanceAdapter extends ArrayAdapter<Balance> {

    private View.OnClickListener defaultRequestBtnClickListener;
    String target;

    public BalanceAdapter(Context context, List<Balance> objects, String target) {
        super(context, 0, objects);
        this.target=target;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Balance item = getItem(position);

        View cell=convertView;
        ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = vi.inflate(R.layout.balance_item, parent, false);

            viewHolder.place = cell.findViewById(R.id.tarnsition_content_date);

            viewHolder.ContenetorderNum = cell.findViewById(R.id.tarnsition_content_total);
            viewHolder.Contenettime = cell.findViewById(R.id.tarnsition_content_time);

            viewHolder.edite=cell.findViewById(R.id.tarnsition_content_delete);

            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)

            viewHolder = (ViewHolder) cell.getTag();
        }

        if (null == item) {
            return cell;
        }


        viewHolder.Contenettime.setText(item.getDate());

        viewHolder.place.setText(item.getDetails());

        viewHolder.ContenetorderNum.setText(item.getAmount());
        viewHolder.edite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("id", String.valueOf(item.getId()));
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("هل انت متاكد من حذف المبلغ!؟")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                String api = getAPIHEADER(getContext()) + "/delBalance.php?id=" + item.getId();
                                RequestQueue queue = Volley.newRequestQueue(getContext());
                                Log.e("NotesBtn", api);
                                StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                Log.e("delBalance", response);
                                                Toast.makeText(getContext(), "done", Toast.LENGTH_SHORT).show();
                                                remove(item);
                                                notifyDataSetChanged();
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
        TextView edite;
    }
}
