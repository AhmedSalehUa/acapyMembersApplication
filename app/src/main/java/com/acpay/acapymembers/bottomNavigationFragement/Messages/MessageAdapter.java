package com.acpay.acapymembers.bottomNavigationFragement.Messages;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
<<<<<<< HEAD:app/src/main/java/com/acpay/acapymembers/bottomNavigationFragement/messages/MessageAdapter.java
        TextView authorTextView = (TextView) convertView.findViewById(R.id.nameTextView);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.dateTextView);
        TextView timeTextView = (TextView) convertView.findViewById(R.id.timeTextView);
=======
>>>>>>> e657a51ca46385b1f80c980290f93f6b456eb68a:app/src/main/java/com/acpay/acapymembers/bottomNavigationFragement/Messages/MessageAdapter.java

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
<<<<<<< HEAD:app/src/main/java/com/acpay/acapymembers/bottomNavigationFragement/messages/MessageAdapter.java
        authorTextView.setText("Me");
        dateTextView.setText(message.getDate());
        timeTextView.setText(message.getTime());
        LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.massege_container);
        if (message.getName().equals(userName)) {
            linearLayout.setBackground(getContext().getResources().getDrawable(R.drawable.shape_bg_outgoing_bubble));
            authorTextView.setTextColor(getContext().getResources().getColor(R.color.massegeMeSender));
            dateTextView.setTextColor(getContext().getResources().getColor(R.color.massegeMeSender));
            timeTextView.setTextColor(getContext().getResources().getColor(R.color.massegeMeSender));
            linearLayout.setGravity(Gravity.RIGHT);

        } else {
            linearLayout.setBackground(getContext().getResources().getDrawable(R.drawable.shape_bg_incoming_bubble));
            authorTextView.setTextColor(getContext().getResources().getColor(R.color.massegeToSender));
            dateTextView.setTextColor(getContext().getResources().getColor(R.color.massegeToSender));
            timeTextView.setTextColor(getContext().getResources().getColor(R.color.massegeToSender));

            linearLayout.setGravity(Gravity.LEFT);
=======

        dateTextView.setText(message.getDate());
        timeTextView.setText(message.getTime());

>>>>>>> e657a51ca46385b1f80c980290f93f6b456eb68a:app/src/main/java/com/acpay/acapymembers/bottomNavigationFragement/Messages/MessageAdapter.java

        return convertView;
    }
}
