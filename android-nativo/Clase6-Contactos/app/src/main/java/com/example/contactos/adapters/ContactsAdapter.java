package com.example.contactos.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.contactos.databinding.ContactAdapterLayoutBinding;


public class ContactsAdapter extends CursorAdapter {
    private ContactAdapterLayoutBinding binding;
    private final LayoutInflater inflater;
    private  final static int CONTACT_ID_INDEX = 0;
    private  final static int CONTACT_NAME_INDEX = 1;

    public ContactsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        binding = ContactAdapterLayoutBinding.inflate(inflater, parent, false);
        return binding.getRoot();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int idCont = cursor.getInt(CONTACT_ID_INDEX);
        String nameCont = cursor.getString(CONTACT_NAME_INDEX);
        TextView idContactText = binding.idContact;
        TextView nameContactText = binding.nameContact;
        idContactText.setText(String.valueOf(idCont));
        nameContactText.setText(nameCont);
    }
}