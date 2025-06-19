package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.ArrayList;

public class FirstAidActivity extends AppCompatActivity {
    private static final String TAG = "FirstAidActivity";

    RecyclerView recyclerView;
    ArrayList<String> titles, descriptions, categories, icons;
    DatabaseHelper dbHelper;
    EmergencyAdapter adapter;
    EditText searchEditText;
    ChipGroup categoryChipGroup;
    TextView noResultsText;
    String currentCategory = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_first_aid);

            // Set up toolbar
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }

            // Initialize views
            initializeViews();

            // Initialize data
            initializeData();

            // Set up RecyclerView
            setupRecyclerView();

            // Set up search functionality
            setupSearch();

        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage());
            Toast.makeText(this, "Error initializing First Aid Guide", Toast.LENGTH_SHORT).show();
            finish();
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

    private void initializeViews() {
        try {
            recyclerView = findViewById(R.id.recyclerView);
            searchEditText = findViewById(R.id.searchEditText);
            categoryChipGroup = findViewById(R.id.categoryChipGroup);
            noResultsText = findViewById(R.id.noResultsText);

            if (recyclerView == null || searchEditText == null || 
                categoryChipGroup == null || noResultsText == null) {
                throw new IllegalStateException("Required views not found");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views: " + e.getMessage());
            throw e;
        }
    }

    private void initializeData() {
        try {
            dbHelper = new DatabaseHelper(this);
            titles = new ArrayList<>();
            descriptions = new ArrayList<>();
            categories = new ArrayList<>();
            icons = new ArrayList<>();

            // Load categories
            loadCategories();
            
            // Load all data initially
            loadData(currentCategory);
        } catch (Exception e) {
            Log.e(TAG, "Error initializing data: " + e.getMessage());
            throw e;
        }
    }

    private void setupRecyclerView() {
        try {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new EmergencyAdapter(this, titles, descriptions, categories, icons);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            Log.e(TAG, "Error setting up RecyclerView: " + e.getMessage());
            throw e;
        }
    }

    private void setupSearch() {
        try {
            searchEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    filterData(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        } catch (Exception e) {
            Log.e(TAG, "Error setting up search: " + e.getMessage());
            throw e;
        }
    }
    
    private void loadCategories() {
        Cursor categoryCursor = null;
        try {
            categoryCursor = dbHelper.getCategories();
            if (categoryCursor != null && categoryCursor.moveToFirst()) {
                do {
                    String category = categoryCursor.getString(0);
                    if (category != null) {
                        addCategoryChip(category);
                    }
                } while (categoryCursor.moveToNext());
            }
            
            // Add "All" category chip
            addCategoryChip("All");
            
            // Set up chip selection listener
            categoryChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
                if (checkedId != View.NO_ID) {
                    Chip chip = findViewById(checkedId);
                    if (chip != null) {
                        currentCategory = chip.getText().toString();
                        loadData(currentCategory);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error loading categories: " + e.getMessage());
        } finally {
            if (categoryCursor != null) {
                categoryCursor.close();
            }
        }
    }
    
    private void addCategoryChip(String category) {
        try {
            Chip chip = new Chip(this);
            chip.setText(category);
            chip.setCheckable(true);
            chip.setClickable(true);
            
            // Set "All" as checked by default
            if (category.equals("All")) {
                chip.setChecked(true);
            }
            
            categoryChipGroup.addView(chip);
        } catch (Exception e) {
            Log.e(TAG, "Error adding category chip: " + e.getMessage());
        }
    }
    
    private void loadData(String category) {
        Cursor cursor = null;
        try {
            // Clear existing data
            titles.clear();
            descriptions.clear();
            categories.clear();
            icons.clear();
            
            if (category.equals("All")) {
                cursor = dbHelper.getAllData();
            } else {
                cursor = dbHelper.getDataByCategory(category);
            }
            
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    titles.add(cursor.getString(1)); // title
                    descriptions.add(cursor.getString(2)); // details
                    categories.add(cursor.getString(3)); // category
                    icons.add(cursor.getString(4)); // icon
                } while (cursor.moveToNext());
                
                noResultsText.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                noResultsText.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
            
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading data: " + e.getMessage());
            Toast.makeText(this, "Error loading first aid data", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    
    private void filterData(String query) {
        Cursor cursor = null;
        try {
            if (query.isEmpty()) {
                loadData(currentCategory);
                return;
            }
            
            // Clear existing data
            titles.clear();
            descriptions.clear();
            categories.clear();
            icons.clear();
            
            if (currentCategory.equals("All")) {
                cursor = dbHelper.getAllData();
            } else {
                cursor = dbHelper.getDataByCategory(currentCategory);
            }
            
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String title = cursor.getString(1);
                    String description = cursor.getString(2);
                    String category = cursor.getString(3);
                    String icon = cursor.getString(4);
                    
                    // Check if title or description contains the search query
                    if (title.toLowerCase().contains(query.toLowerCase()) || 
                        description.toLowerCase().contains(query.toLowerCase())) {
                        titles.add(title);
                        descriptions.add(description);
                        categories.add(category);
                        icons.add(icon);
                    }
                } while (cursor.moveToNext());
                
                updateUIVisibility(!titles.isEmpty());
            } else {
                updateUIVisibility(false);
            }
            
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error filtering data: " + e.getMessage());
            Toast.makeText(this, "Error filtering data", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void updateUIVisibility(boolean hasResults) {
        if (noResultsText != null && recyclerView != null) {
            noResultsText.setVisibility(hasResults ? View.GONE : View.VISIBLE);
            recyclerView.setVisibility(hasResults ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
