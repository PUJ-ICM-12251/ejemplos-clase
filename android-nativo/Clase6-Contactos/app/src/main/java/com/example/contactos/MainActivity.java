package com.example.contactos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ListView;
import android.Manifest;
import android.widget.Toast;

import com.example.contactos.adapters.ContactsAdapter;
import com.example.contactos.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    public static final int CONTACTS_PERM_ID = 5;
    String permContacts = Manifest.permission.READ_CONTACTS;

    String[] mProjection;
    Cursor mCursor;
    ContactsAdapter mContactsAdapter;
    ListView mContactListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mContactListView = binding.contactlist;
        mProjection = new String[]{
                ContactsContract.Profile._ID,
                ContactsContract.Profile.DISPLAY_NAME_PRIMARY,
        };

        // El Cursor está en null hasta que el usuario acepte el permiso
        mContactsAdapter = new ContactsAdapter(this, null, 0);
        mContactListView.setAdapter(mContactsAdapter);

        requestPermission(this, permContacts, "Se requiere acceso a los contactos para desplegar la lsta.", CONTACTS_PERM_ID);
        initView();

    }

    private void initView() {
        if(ContextCompat.checkSelfPermission(this, permContacts) == PackageManager.PERMISSION_GRANTED) {
            mCursor = getContentResolver().query(
                    ContactsContract.Contacts.CONTENT_URI, mProjection, null, null, null);
                    mContactsAdapter.changeCursor(mCursor);
        }
    }

    private void requestPermission(Activity context, String permission, String justification, int id) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, permContacts)) {
                Toast.makeText(context, justification, Toast.LENGTH_SHORT).show();
            }
            // Request the perrmission
            ActivityCompat.requestPermissions(context, new String[]{permission}, id);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CONTACTS_PERM_ID) {
            initView();
        }
    }
}

























