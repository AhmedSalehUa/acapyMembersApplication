package com.acpay.acapymembers.Order;

import android.os.Handler;
import android.util.Log;

import com.acpay.acapymembers.JasonReponser;

public class OrderDone {

    String response;
    private static boolean finish = false;

    public OrderDone(Order item, String me, String notevalue, String progressname) {
        finish = false;
        updateNotesOfProgress(item, notevalue, progressname);
    }

    public static boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public OrderDone(Order order, String method) {

        switch (method) {
            case "pending":
                finish = false;
                Pending(order);
                break;
            case "done":
                finish = false;
                done(order);
                break;

        }

    }

    public OrderDone(Order order, String method, String value) {
        finish = false;
        updateNotes(order, value);


    }

    private void updateNotesOfProgress(Order order, String notevalue, String value) {
        finish = false;
        String api = "https://www.app.acapy-trade.com/updatenotesprog.php?order=" + order.getOrderNum() + "&note=" + notevalue + "&progress=" + value;
        Log.w("pen", api);
        final JasonReponser update = new JasonReponser();
        update.setFinish(false);
        update.execute(api);
        final Handler handler = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                if (update.isFinish()) {
                    response = update.getUserId();
                    finish = true;
                } else {
                    handler.postDelayed(this, 100);
                }
            }
        };
        handler.post(runnableCode);
    }


    private void done(Order order) {
        finish = false;
        String api = "https://www.app.acapy-trade.com/doneOrder.php?order=" + order.getOrderNum();
        Log.w("pen", api);
        final JasonReponser update = new JasonReponser();
        update.setFinish(false);
        update.execute(api);
        final Handler handler = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                if (update.isFinish()) {
                    response = update.getUserId();
                    finish = true;
                } else {
                    handler.postDelayed(this, 100);
                }
            }
        };
        handler.post(runnableCode);
    }

    private void updateNotes(Order order, String value) {
        finish = false;
        String api = "https://www.app.acapy-trade.com/updatenotes.php?order=" + order.getOrderNum() + "&note=" + value;
        Log.w("pen", api);
        final JasonReponser update = new JasonReponser();
        update.setFinish(false);
        update.execute(api);
        final Handler handler = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                if (update.isFinish()) {
                    response = update.getUserId();
                    finish = true;
                } else {
                    handler.postDelayed(this, 100);
                }
            }
        };
        handler.post(runnableCode);
    }

    public void Pending(Order order) {
        finish = false;
        String api = "https://www.app.acapy-trade.com/pendingOrder.php?order=" + order.getOrderNum();
        Log.w("pen", api);
        final JasonReponser update = new JasonReponser();
        update.setFinish(false);
        update.execute(api);
        final Handler handler = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                if (update.isFinish()) {
                    response = update.getUserId();
                    finish = true;
                } else {
                    handler.postDelayed(this, 100);
                }
            }
        };
        handler.post(runnableCode);
    }

    public String getResponse() {
        return response;
    }
}
