package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.firebase.adapters.MessageAdapter;
import com.example.firebase.model.DatabasePaths;
import com.example.firebase.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = ChatActivity.class.getName();
    // Varriables for Firebase Auth
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    // Vaiables for Firebase DB
    FirebaseDatabase database;
    DatabaseReference myRef;
    ValueEventListener valueEventListener;

    // Local Data
    MessageAdapter adapter;
    LottieAnimationView sendButton;
    EditText messageEdit;
    ArrayList<Message> messages = new ArrayList<>();
    ListView messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        // Initialize Firebase database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        // Initialize Adapter
        adapter = new MessageAdapter(ChatActivity.this, messages);
        messageList = findViewById(R.id.chatMessages);
        messageList.setAdapter(adapter);

        sendButton = findViewById(R.id.sendButton);
        messageEdit = findViewById(R.id.messageEdit);
    }


    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            logout();
        }
        loadMessages();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (valueEventListener != null) {
            myRef.removeEventListener(valueEventListener);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClicked = item.getItemId();
        if (itemClicked == R.id.menuLogOut) {
            logout();
        }
        return super.onOptionsItemSelected(item);
    }


    public void loadMessages() {
        myRef = database.getReference(DatabasePaths.CHAT);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messages.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Message tmpMsg = snapshot.getValue(Message.class);
                    messages.add(tmpMsg);
                }
                adapter.notifyDataSetChanged();
                messageList.post(new Runnable() {
                    @Override
                    public void run() {
                        messageList.setSelection(messages.size()-1);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Error in query", databaseError.toException());
            }
        });
    }


    public void sendMessage(View view) {
        String message = messageEdit.getText().toString();
        if (!message.isEmpty()) {
            messageEdit.getText().clear();
            String key = myRef.push().getKey();
            Message msgToSend = new Message(key, message, currentUser.getEmail(), new Date());
            myRef = database.getReference(DatabasePaths.CHAT + key);
            myRef.setValue(msgToSend);
            sendButton.playAnimation();
        } else {
            Toast.makeText(ChatActivity.this, "Message cannot be empty.", Toast.LENGTH_SHORT).show();
        }
    }


    private void logout(){
        mAuth.signOut();
        Intent intent = new Intent(ChatActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}