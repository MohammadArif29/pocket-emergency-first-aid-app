package com.example.myapplication;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class EmergencyContactsActivity extends AppCompatActivity {
    private static final String TAG = "EmergencyContactsActivity";
    private RecyclerView contactsRecyclerView;
    private EmergencyContactAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<EmergencyContact> contacts;
    private MaterialButton btnAddContact;
    private FloatingActionButton fabAddContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);

        try {
            // Initialize database helper
            dbHelper = new DatabaseHelper(this);
            
            // Initialize views
            initializeViews();
            
            // Set up toolbar
            setupToolbar();
            
            // Set up RecyclerView
            setupRecyclerView();
            
            // Set up click listeners
            setupClickListeners();
            
            // Add default emergency contacts if none exist
            addDefaultEmergencyContacts();
            
            // Load contacts
            loadContacts();
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage());
            Toast.makeText(this, "Error initializing contacts", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeViews() {
        contactsRecyclerView = findViewById(R.id.contactsRecyclerView);
        btnAddContact = findViewById(R.id.btnAddContact);
        fabAddContact = findViewById(R.id.fabAddContact);

        if (contactsRecyclerView == null || btnAddContact == null || fabAddContact == null) {
            throw new IllegalStateException("Required views not found");
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Emergency Contacts");
        }
    }

    private void setupRecyclerView() {
        contacts = new ArrayList<>();
        adapter = new EmergencyContactAdapter(this, contacts);
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactsRecyclerView.setAdapter(adapter);
        contactsRecyclerView.setHasFixedSize(true);
    }

    private void setupClickListeners() {
        View.OnClickListener addContactListener = v -> showAddContactDialog();
        btnAddContact.setOnClickListener(addContactListener);
        fabAddContact.setOnClickListener(addContactListener);
    }

    private void loadContacts() {
        try {
            contacts.clear();
            Cursor cursor = dbHelper.getAllEmergencyContacts();
            
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CONTACT_NAME));
                    String phone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CONTACT_PHONE));
                    String type = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CONTACT_TYPE));
                    int priority = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CONTACT_PRIORITY));
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CONTACT_ID));
                    
                    contacts.add(new EmergencyContact(id, name, phone, type, priority, R.drawable.ic_person));
                } while (cursor.moveToNext());
                cursor.close();
            }
            
            adapter.updateContacts(contacts);
            
            // Show/hide empty state
            if (contacts.isEmpty()) {
                Toast.makeText(this, "No emergency contacts added yet", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading contacts: " + e.getMessage());
            Toast.makeText(this, "Error loading contacts", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAddContactDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_contact, null);
        
        EditText nameInput = dialogView.findViewById(R.id.nameInput);
        EditText phoneInput = dialogView.findViewById(R.id.phoneInput);
        
        builder.setView(dialogView)
               .setTitle("Add Emergency Contact")
               .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int id) {
                       String name = nameInput.getText().toString().trim();
                       String phone = phoneInput.getText().toString().trim();
                       
                       if (!name.isEmpty() && !phone.isEmpty()) {
                           try {
                               long result = dbHelper.addEmergencyContact(name, phone, "personal", contacts.size() + 1);
                               if (result != -1) {
                                   loadContacts();
                                   Toast.makeText(EmergencyContactsActivity.this, 
                                       "Contact added successfully", Toast.LENGTH_SHORT).show();
                               } else {
                                   Toast.makeText(EmergencyContactsActivity.this, 
                                       "Failed to add contact", Toast.LENGTH_SHORT).show();
                               }
                           } catch (Exception e) {
                               Log.e(TAG, "Error adding contact: " + e.getMessage());
                               Toast.makeText(EmergencyContactsActivity.this, 
                                   "Error adding contact", Toast.LENGTH_SHORT).show();
                           }
                       } else {
                           Toast.makeText(EmergencyContactsActivity.this, 
                               "Please fill in all fields", Toast.LENGTH_SHORT).show();
                       }
                   }
               })
               .setNegativeButton("Cancel", null);
        
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addDefaultEmergencyContacts() {
        Cursor cursor = dbHelper.getAllEmergencyContacts();
        if (cursor != null && cursor.getCount() == 0) {
            // Add Andhra Pradesh emergency numbers
            dbHelper.addEmergencyContact("Police Emergency", "100", "emergency", 1);
            dbHelper.addEmergencyContact("Ambulance", "108", "emergency", 2);
            dbHelper.addEmergencyContact("Fire Emergency", "101", "emergency", 3);
            dbHelper.addEmergencyContact("Women Helpline", "181", "emergency", 4);
            dbHelper.addEmergencyContact("Child Helpline", "1098", "emergency", 5);
            dbHelper.addEmergencyContact("AP Emergency", "112", "emergency", 6);
            dbHelper.addEmergencyContact("Disaster Management", "1070", "emergency", 7);
            dbHelper.addEmergencyContact("Railway Protection", "182", "emergency", 8);
            dbHelper.addEmergencyContact("Senior Citizen Helpline", "14567", "emergency", 9);
            dbHelper.addEmergencyContact("COVID-19 Helpline AP", "0866-2410978", "emergency", 10);
            Toast.makeText(this, "Default emergency contacts added", Toast.LENGTH_SHORT).show();
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
} 