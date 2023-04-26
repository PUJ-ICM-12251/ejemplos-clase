package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.firebase.adapters.PeopleAdapter;
import com.example.firebase.model.Address;
import com.example.firebase.model.DatabasePaths;
import com.example.firebase.model.People;
import com.github.javafaker.Faker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.cert.PolicyNode;
import java.util.ArrayList;

public class PeopleActivity extends AppCompatActivity {
    private static final String TAG = PeopleActivity.class.getName();
    private FirebaseAuth mAuth;

    // Auth user
    FirebaseUser currentUser;

    // Variables for Firebase DB
    FirebaseDatabase database;
    DatabaseReference myRef;
    ValueEventListener valueEventListener;

    //Local Data
    PeopleAdapter adapter;
    ArrayList<People> peopleLocal = new ArrayList<>();
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        // Initialize Firebase database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        // Initialize Adapter
        adapter = new PeopleAdapter(PeopleActivity.this, peopleLocal);
        listView = findViewById(R.id.peopleList);
        listView.setAdapter(adapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if(currentUser == null) {
            logout();
        }
        loadQueryPeople();
    }


    public void loadQueryPeople() {
        myRef = database.getReference(DatabasePaths.PEOPLE);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                peopleLocal.clear();
                for (DataSnapshot snapshot: datasnapshot.getChildren()) {
                    People ppl = snapshot.getValue(People.class);
                    peopleLocal.add(ppl);
                }
                adapter.notifyDataSetChanged();
                Log.i(TAG, "Data changed from realtime DB");
                listView.post(new Runnable() {
                    @Override
                    public void run() {
                        listView.setSelection(peopleLocal.size()-1);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Error en la consulta", databaseError.toException());
            }
        });
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
        if(itemClicked == R.id.menuLogOut) {
            logout();
        }
        return super.onOptionsItemSelected(item);
    }


    // Generate random people
    public void generatePeople(View view) {
        Faker faker = new Faker();
        People pl = new People();
        pl.setFirstName(faker.funnyName().name());
        pl.setLastName(faker.name().lastName());
        pl.setAge(faker.date().birthday().getYear());
        pl.setHeight(faker.number().randomDouble(1,1,2));
        pl.setWeight(faker.number().randomDouble(1,50,150));
        pl.setCurrentAddress(new Address());
        pl.getCurrentAddress().setCity(faker.address().fullAddress());
        pl.getCurrentAddress().setAddress(faker.address().fullAddress());

        String key = myRef.push().getKey();
        myRef = database.getReference(DatabasePaths.PEOPLE + key);
        myRef.setValue(pl);
    }


    private void logout() {
        mAuth.signOut();
        Intent intent = new Intent(PeopleActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}