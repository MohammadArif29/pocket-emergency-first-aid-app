package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.*;
import android.database.Cursor;
import android.content.ContentValues;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "FirstAid.db";
    private static final int DB_VERSION = 3; // Increment version for new table
    
    // First Aid table
    private static final String TABLE_NAME = "aid";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_DETAILS = "details";
    private static final String COL_CATEGORY = "category";
    private static final String COL_ICON = "icon";
    
    // Emergency Contacts table constants - made public for external access
    public static final String TABLE_EMERGENCY_CONTACTS = "emergency_contacts";
    public static final String COL_CONTACT_ID = "contact_id";
    public static final String COL_CONTACT_NAME = "name";
    public static final String COL_CONTACT_PHONE = "phone";
    public static final String COL_CONTACT_TYPE = "contact_type";
    public static final String COL_CONTACT_PRIORITY = "priority";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create First Aid table
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_TITLE + " TEXT," +
                COL_DETAILS + " TEXT," +
                COL_CATEGORY + " TEXT," +
                COL_ICON + " TEXT)");

        // Create Emergency Contacts table
        db.execSQL("CREATE TABLE " + TABLE_EMERGENCY_CONTACTS + " (" +
                COL_CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_CONTACT_NAME + " TEXT NOT NULL," +
                COL_CONTACT_PHONE + " TEXT NOT NULL," +
                COL_CONTACT_TYPE + " TEXT," +
                COL_CONTACT_PRIORITY + " INTEGER)");

        // Insert comprehensive first aid data
        insertFirstAidData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the old tables and recreate with new schema
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMERGENCY_CONTACTS);
        onCreate(db);
    }

    // Emergency Contacts Methods
    public long addEmergencyContact(String name, String phone, String type, int priority) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_CONTACT_NAME, name);
        values.put(COL_CONTACT_PHONE, phone);
        values.put(COL_CONTACT_TYPE, type);
        values.put(COL_CONTACT_PRIORITY, priority);
        return db.insert(TABLE_EMERGENCY_CONTACTS, null, values);
    }

    public Cursor getAllEmergencyContacts() {
        return this.getReadableDatabase().rawQuery(
            "SELECT * FROM " + TABLE_EMERGENCY_CONTACTS + " ORDER BY " + COL_CONTACT_PRIORITY, 
            null
        );
    }

    public boolean updateEmergencyContact(int id, String name, String phone, String type, int priority) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_CONTACT_NAME, name);
        values.put(COL_CONTACT_PHONE, phone);
        values.put(COL_CONTACT_TYPE, type);
        values.put(COL_CONTACT_PRIORITY, priority);
        return db.update(TABLE_EMERGENCY_CONTACTS, values, 
                        COL_CONTACT_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteEmergencyContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_EMERGENCY_CONTACTS, 
                        COL_CONTACT_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    private void insertFirstAidData(SQLiteDatabase db) {
        // Emergency Response
        db.execSQL("INSERT INTO " + TABLE_NAME + " (title, details, category, icon) VALUES " +
                "('Emergency Response', 'Call emergency services (911/112) immediately for life-threatening situations.', 'Emergency', 'emergency')");
        
        db.execSQL("INSERT INTO " + TABLE_NAME + " (title, details, category, icon) VALUES " +
                "('CPR for Adults', '1. Check responsiveness\n2. Call for help\n3. Check breathing\n4. Begin chest compressions (30 compressions)\n5. Give rescue breaths (2 breaths)\n6. Continue cycles of 30:2', 'Emergency', 'cpr')");
        
        db.execSQL("INSERT INTO " + TABLE_NAME + " (title, details, category, icon) VALUES " +
                "('CPR for Children', '1. Check responsiveness\n2. Call for help\n3. Check breathing\n4. Begin chest compressions (15 compressions)\n5. Give rescue breaths (2 breaths)\n6. Continue cycles of 15:2', 'Emergency', 'cpr')");
        
        db.execSQL("INSERT INTO " + TABLE_NAME + " (title, details, category, icon) VALUES " +
                "('Heimlich Maneuver', '1. Stand behind the person\n2. Wrap your arms around their waist\n3. Make a fist with one hand\n4. Place fist thumb-side against middle of abdomen\n5. Grasp fist with other hand\n6. Give quick, upward thrusts', 'Emergency', 'choking')");
        
        // Wounds and Bleeding
        db.execSQL("INSERT INTO " + TABLE_NAME + " (title, details, category, icon) VALUES " +
                "('Minor Cuts and Scrapes', '1. Clean with soap and water\n2. Apply antiseptic\n3. Cover with sterile bandage\n4. Change bandage daily', 'Wounds', 'cuts')");
        
        db.execSQL("INSERT INTO " + TABLE_NAME + " (title, details, category, icon) VALUES " +
                "('Severe Bleeding', '1. Apply direct pressure with clean cloth\n2. Elevate injured area if possible\n3. Apply pressure bandage\n4. Keep victim warm\n5. Call emergency services', 'Wounds', 'bleeding')");
        
        db.execSQL("INSERT INTO " + TABLE_NAME + " (title, details, category, icon) VALUES " +
                "('Nosebleed', '1. Sit upright and lean forward\n2. Pinch nose for 10 minutes\n3. Apply ice pack to bridge of nose\n4. Avoid blowing nose for several hours', 'Wounds', 'nosebleed')");
        
        // Burns
        db.execSQL("INSERT INTO " + TABLE_NAME + " (title, details, category, icon) VALUES " +
                "('First Degree Burns', '1. Cool with cool (not cold) water for 10-20 minutes\n2. Remove jewelry from burned area\n3. Cover with sterile gauze\n4. Take pain reliever if needed', 'Burns', 'burn')");
        
        db.execSQL("INSERT INTO " + TABLE_NAME + " (title, details, category, icon) VALUES " +
                "('Second Degree Burns', '1. Cool with cool water for 10-20 minutes\n2. Do not break blisters\n3. Cover with sterile gauze\n4. Seek medical attention\n5. Keep victim warm', 'Burns', 'burn')");
        
        db.execSQL("INSERT INTO " + TABLE_NAME + " (title, details, category, icon) VALUES " +
                "('Chemical Burns', '1. Remove contaminated clothing\n2. Rinse with large amounts of water for 20 minutes\n3. Cover with sterile gauze\n4. Seek immediate medical attention', 'Burns', 'chemical')");
        
        // Fractures and Sprains
        db.execSQL("INSERT INTO " + TABLE_NAME + " (title, details, category, icon) VALUES " +
                "('Fractures', '1. Stop any bleeding\n2. Apply ice wrapped in cloth\n3. Immobilize the area\n4. Do not try to realign bone\n5. Call emergency services', 'Injuries', 'fracture')");
        
        db.execSQL("INSERT INTO " + TABLE_NAME + " (title, details, category, icon) VALUES " +
                "('Sprains and Strains', '1. Rest the injured area\n2. Apply ice for 20 minutes\n3. Compress with elastic bandage\n4. Elevate injured area\n5. Seek medical attention if severe', 'Injuries', 'sprain')");
        
        // Allergic Reactions
        db.execSQL("INSERT INTO " + TABLE_NAME + " (title, details, category, icon) VALUES " +
                "('Allergic Reactions', '1. Identify and remove allergen if possible\n2. Administer antihistamine if prescribed\n3. Watch for difficulty breathing\n4. Call emergency services if severe\n5. Be prepared to perform CPR if needed', 'Allergies', 'allergy')");
        
        db.execSQL("INSERT INTO " + TABLE_NAME + " (title, details, category, icon) VALUES " +
                "('Anaphylaxis', '1. Call emergency services immediately\n2. Administer epinephrine auto-injector if prescribed\n3. Lie flat with legs elevated\n4. Do not allow person to stand or walk\n5. Be prepared to perform CPR', 'Allergies', 'anaphylaxis')");
        
        // Poisoning
        db.execSQL("INSERT INTO " + TABLE_NAME + " (title, details, category, icon) VALUES " +
                "('Poisoning', '1. Call poison control center\n2. Do not induce vomiting unless instructed\n3. If on skin, rinse with water for 15-20 minutes\n4. If in eyes, rinse with water for 15-20 minutes\n5. Call emergency services if severe', 'Poisoning', 'poison')");
        
        // Heat and Cold Emergencies
        db.execSQL("INSERT INTO " + TABLE_NAME + " (title, details, category, icon) VALUES " +
                "('Heat Exhaustion', '1. Move to cool place\n2. Loosen clothing\n3. Apply cool, wet cloths\n4. Sip water slowly\n5. Seek medical attention if symptoms worsen', 'Environment', 'heat')");
        
        db.execSQL("INSERT INTO " + TABLE_NAME + " (title, details, category, icon) VALUES " +
                "('Heat Stroke', '1. Call emergency services immediately\n2. Move to cool place\n3. Remove excess clothing\n4. Cool with water, ice packs, or wet cloths\n5. Do not give fluids if unconscious', 'Environment', 'heat_stroke')");
        
        db.execSQL("INSERT INTO " + TABLE_NAME + " (title, details, category, icon) VALUES " +
                "('Hypothermia', '1. Move to warm place\n2. Remove wet clothing\n3. Warm center of body first\n4. Give warm, non-alcoholic beverages\n5. Seek medical attention', 'Environment', 'cold')");
        
        // Medical Conditions
        db.execSQL("INSERT INTO " + TABLE_NAME + " (title, details, category, icon) VALUES " +
                "('Heart Attack', '1. Call emergency services immediately\n2. Have person rest\n3. Loosen tight clothing\n4. Be prepared to perform CPR\n5. Give aspirin if not allergic', 'Medical', 'heart')");
        
        db.execSQL("INSERT INTO " + TABLE_NAME + " (title, details, category, icon) VALUES " +
                "('Stroke', '1. Call emergency services immediately\n2. Note time symptoms started\n3. Keep person calm and comfortable\n4. Do not give food or drink\n5. Be prepared to perform CPR if needed', 'Medical', 'stroke')");
        
        db.execSQL("INSERT INTO " + TABLE_NAME + " (title, details, category, icon) VALUES " +
                "('Seizure', '1. Protect from injury\n2. Do not restrain\n3. Do not put anything in mouth\n4. Time the seizure\n5. Call emergency services if seizure lasts more than 5 minutes', 'Medical', 'seizure')");
        
        db.execSQL("INSERT INTO " + TABLE_NAME + " (title, details, category, icon) VALUES " +
                "('Diabetic Emergency', '1. If conscious, give sugar\n2. If unconscious, call emergency services\n3. Do not give insulin\n4. Be prepared to perform CPR if needed', 'Medical', 'diabetes')");
    }

    public Cursor getAllData() {
        return this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY category, title", null);
    }
    
    public Cursor getDataByCategory(String category) {
        return this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE category = ? ORDER BY title", new String[]{category});
    }
    
    public Cursor getCategories() {
        return this.getReadableDatabase().rawQuery("SELECT DISTINCT category FROM " + TABLE_NAME + " ORDER BY category", null);
    }
}
