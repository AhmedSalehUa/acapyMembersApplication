package com.acpay.acapymembers.Order;

import com.acpay.acapymembers.Order.progress.boxes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderUtilies {
    private static final String LOG_TAG = OrderUtilies.class.getName();

    public OrderUtilies() {

    }


    public static List<Order> extractFeuterFromJason(String jason) {
        final List<Order> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jason);
            JSONArray sa = jsonObject.names();
            for (int i = 0; i < sa.length(); i++) {
                JSONObject jsonArrayId = jsonObject.getJSONObject(sa.get(i).toString());
                Order order;
                if (jsonArrayId.has("list")) {
                    JSONObject transitionsjsonObject = new JSONObject(jsonArrayId.getString("list"));
                    JSONArray transitionssa = transitionsjsonObject.names();
                    List<boxes> boxesList=new ArrayList<>();
                    for (int x = 0; x < transitionssa.length(); x++) {
                        JSONObject transitionsjsonArrayId = transitionsjsonObject.getJSONObject(transitionssa.get(x).toString());
                        boxesList.add(new boxes(transitionsjsonArrayId.getString("progress_name")
                                , transitionsjsonArrayId.getString("statue")
                                , transitionsjsonArrayId.getString("notes")));
                    }
                    order = new Order(jsonArrayId.getString("order_num"),
                            jsonArrayId.getString("date"),
                            jsonArrayId.getString("time"),
                            jsonArrayId.getString("place"),
                            jsonArrayId.getString("location"),
                            jsonArrayId.getString("fixType"),
                            jsonArrayId.getString("num_of_matter"),
                            jsonArrayId.getString("dliverCost"),
                            jsonArrayId.getString("notes"),
                            jsonArrayId.getString("files"), jsonArrayId.getString("username"), boxesList
                    );
                    list.add(order);
                } else {
                    order = new Order(jsonArrayId.getString("order_num"),
                            jsonArrayId.getString("date"),
                            jsonArrayId.getString("time"),
                            jsonArrayId.getString("place"),
                            jsonArrayId.getString("location"),
                            jsonArrayId.getString("fixType"),
                            jsonArrayId.getString("num_of_matter"),
                            jsonArrayId.getString("dliverCost"),
                            jsonArrayId.getString("notes"),
                            jsonArrayId.getString("files"), jsonArrayId.getString("username"), new ArrayList<boxes>()
                    );
                    list.add(order);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
