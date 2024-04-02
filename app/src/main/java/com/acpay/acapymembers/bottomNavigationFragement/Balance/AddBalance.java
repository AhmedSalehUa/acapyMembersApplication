package com.acpay.acapymembers.bottomNavigationFragement.Balance;

import static com.acpay.acapymembers.MainActivity.getAPIHEADER;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.acpay.acapymembers.R;
import com.acpay.acapymembers.bottomNavigationFragement.Orders.OrderCostsFragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddBalance extends AppCompatActivity {
    private int mYear, mMonth, mDay, mHour, mMinute;
    EditText cost, date, details;
    String Api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balance_add_activity);


        cost = findViewById(R.id.balance_amount);
        date = findViewById(R.id.balance_date);
        date.setEnabled(false);
        date.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        details = findViewById(R.id.balance_details);
        final ImageView datePicker = (ImageView) findViewById(R.id.balance_add_date);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddBalance.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_nav, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (cost.getText().toString().isEmpty() || date.getText().toString().isEmpty() || details.getText().toString().isEmpty()) {
                    Toast.makeText(AddBalance.this, "هناك بيانات فارغة", Toast.LENGTH_SHORT).show();

                } else {

                    Api = getAPIHEADER(AddBalance.this) + "/addbalance.php?"
                            + "&amount=" + cost.getText().toString()
                            + "&date=" + date.getText().toString()
                            + "&user=" + FirebaseAuth.getInstance().getCurrentUser().getUid()
                            + "&details=" + details.getText().toString();
                    RequestQueue queue = Volley.newRequestQueue(AddBalance.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, Api,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(AddBalance.this, "done", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(AddBalance.this, "error", Toast.LENGTH_SHORT).show();
                            Log.e("onResponse", error.toString());
                        }
                    });
                    stringRequest.setShouldCache(false);
                    stringRequest.setShouldRetryConnectionErrors(true);
                    stringRequest.setShouldRetryServerErrors(true);
                    queue.add(stringRequest);
                    break;
                }
        }
        return super.onOptionsItemSelected(item);
    }
}
