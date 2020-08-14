package com.acpay.acapymembers.bottomNavigationFragement.messages;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acpay.acapymembers.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {
    String userName;
    TextView authorTextView;
    ImageView seen;

    public MessageAdapter(Context context, int resource, List<Message> objects, String userName) {
        super(context, resource, objects);
        this.userName = userName;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = getItem(position);
        if (convertView == null) {
            if (message.getName().equals(userName)) {
                convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.message_activity_item_sender, parent, false);
                seen = (ImageView)convertView.findViewById(R.id.message_status);
                Log.e(message.getDate(),String.valueOf(message.isSeen()));
                if (message.isSeen()){
                    seen.setImageResource(R.drawable.seen2);
                }else {
                    seen.setImageResource(R.drawable.delivered);
                }
            } else {
                convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.message_activity_item_reciver, parent, false);
                authorTextView = (TextView) convertView.findViewById(R.id.nameTextView);
                authorTextView.setText(message.getName());
            }
        }

        ImageView photoImageView = (ImageView) convertView.findViewById(R.id.photoImageView);
        TextView messageTextView = (TextView) convertView.findViewById(R.id.messageTextView);

        TextView dateTextView = (TextView) convertView.findViewById(R.id.dateTextView);
        TextView timeTextView = (TextView) convertView.findViewById(R.id.timeTextView);



        boolean isPhoto = message.getPhotoUrl() != null;
        if (isPhoto) {
            messageTextView.setVisibility(View.GONE);
            photoImageView.setVisibility(View.VISIBLE);
            Glide.with(photoImageView.getContext())
                    .load(message.getPhotoUrl())
                    .into(photoImageView);
        } else {
            messageTextView.setVisibility(View.VISIBLE);
            photoImageView.setVisibility(View.GONE);
            messageTextView.setText(message.getText());
        }

        dateTextView.setText(message.getDate());
        timeTextView.setText(message.getTime());


        return convertView;
    }
}
