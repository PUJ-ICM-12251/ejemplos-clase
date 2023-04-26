package com.example.firebase.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.firebase.R;
import com.example.firebase.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MessageAdapter extends ArrayAdapter<Message> {
    private final static String MESSAGE_DATE_FORMAT = "dd MMMM yyyy h:ma";
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    public MessageAdapter(Context context, ArrayList<Message> messages) {
        super(context, 0 , messages);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Grab the message to render
        Message message = getItem(position);
        // Check if an existing view is being reused, otherwise inflat the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_adapter, parent, false);
        }
        // Get all the fields from the adapter
        ConstraintLayout layout = convertView.findViewById(R.id.messageContainer);
        TextView user = convertView.findViewById(R.id.messageUser);
        TextView messageContent = convertView.findViewById(R.id.messageContent);
        TextView date = convertView.findViewById(R.id.messageDate);
        //Format and Set the values in the view
        user.setText(String.format("%s says:", message.getUserId()));
        messageContent.setText(message.getText());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MESSAGE_DATE_FORMAT);
        date.setText(simpleDateFormat.format(message.getTimestamp()));
        if (mAuth.getCurrentUser().getEmail().equals(message.getUserId())) {
            layout.setBackgroundResource(R.drawable.mymessage_layout);
        } else {
            layout.setBackgroundResource(R.drawable.message_layout);
        }
        return convertView;
    }
}
