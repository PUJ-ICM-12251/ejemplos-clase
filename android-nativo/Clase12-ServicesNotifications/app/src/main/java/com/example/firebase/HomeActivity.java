package com.example.firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebase.services.BasicIntentService;
import com.example.firebase.services.BasicJobIntentService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.regex.Matcher;

public class HomeActivity extends AppCompatActivity {
    public static final String TAG = HomeActivity.class.getName();
    private FirebaseAuth mAuth;
    TextView texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        texto = findViewById(R.id.greeting);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            texto.setText(String.format("Hola %s. estás autenticado.", user.getEmail()));
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Service launch from i.e. onStart
//        Intent intent = new Intent(HomeActivity.this, BasicIntentService.class);
//        startService(intent);
//        Intent intent = new Intent(HomeActivity.this, BasicJobIntentService.class);
//        intent.putExtra("milliSeconds", 5000);
//        BasicJobIntentService.enqueueWork(HomeActivity.this, intent);
//        Log.i(TAG, "After the call to the service");
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
            mAuth.signOut();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    public void goToPeopleActivity(View view) {
        Intent intent = new Intent(getBaseContext(), PeopleActivity.class);
        startActivity(intent);
    }


    public void goToChatActivity(View view) {
        Intent intent = new Intent(getBaseContext(), ChatActivity.class);
        startActivity(intent);
    }

}