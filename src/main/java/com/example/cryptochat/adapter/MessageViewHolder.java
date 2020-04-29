package com.example.cryptochat.adapter;

import android.view.View;
import android.widget.TextView;

import com.example.cryptochat.message.Message;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class MessageViewHolder extends RecyclerView.ViewHolder {

    private TextView messageText;
    private TextView messageTime;
    private TextView userName;


    MessageViewHolder(@NonNull View itemView, int messageTextId, int messageTimeId, int userNameId) {
        super(itemView);
        messageText = itemView.findViewById(messageTextId);
        messageTime = itemView.findViewById(messageTimeId);
        userName = itemView.findViewById(userNameId);
    }

    void bind(Message message) {
        DateFormat fmt = SimpleDateFormat.getTimeInstance(DateFormat.SHORT);
        messageText.setText(message.text);
        messageTime.setText(fmt.format(message.date));
        userName.setText(message.userName);
    }
}
