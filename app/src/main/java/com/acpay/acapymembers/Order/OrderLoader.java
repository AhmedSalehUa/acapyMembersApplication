package com.acpay.acapymembers.Order;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class OrderLoader extends AsyncTaskLoader<List<Order>> {

    private static final String LOG_TAG = OrderLoader.class.getName();

    /** Query URL */
    private String mUrl;

    public OrderLoader(Context context, String url) {
        super(context);
        mUrl = url;
        Log.w("from loader " , mUrl);
    }
    @Nullable
    @Override
    public List<Order> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        Log.w("network",mUrl);
        List<Order> orders = OrderUtilies.fetchData(mUrl);
        return orders;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
