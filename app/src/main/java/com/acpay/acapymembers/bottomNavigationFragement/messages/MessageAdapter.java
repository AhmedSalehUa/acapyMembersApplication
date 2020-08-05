package com.acpay.acapymembers.bottomNavigationFragement.messages;

import android.app.Activity;
import android.content.Context;
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

    public MessageAdapter(Context context, int resource, List<Message> objects, String userName) {
        super(context, resource, objects);
        this.userName = userName;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.message_activity_item, parent, false);
        }

        ImageView photoImageView = (ImageView) convertView.findViewById(R.id.photoImageView);
        TextView messageTextView = (TextView) convertView.findViewById(R.id.messageTextView);
        TextView authorTextView = (TextView) convertView.findViewById(R.id.nameTextView);

        Message message = getItem(position);

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
        authorTextView.setText(message.getName());
        LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.massege_container);
        if (message.getName().equals(userName)) {
            linearLayout.setBackground(getContext().getResources().getDrawable(R.drawable.shape_bg_outgoing_bubble));
            authorTextView.setTextColor(getContext().getResources().getColor(R.color.massegeMeSender));
            linearLayout.setGravity(Gravity.RIGHT);

        } else {
            linearLayout.setBackground(getContext().getResources().getDrawable(R.drawable.shape_bg_incoming_bubble));
            authorTextView.setTextColor(getContext().getResources().getColor(R.color.massegeToSender));
            linearLayout.setGravity(Gravity.LEFT);

        }
        return convertView;
    }
}
