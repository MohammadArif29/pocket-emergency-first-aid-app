package com.example.myapplication;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {

    private TextView titleText, descriptionText;
    private TextView categoryChip;
    private RecyclerView stepsRecyclerView;
    private StepsAdapter stepsAdapter;
    private List<Step> steps;
    private Map<String, Integer> stepImageMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        initializeStepImages();

        // Initialize views
        titleText = findViewById(R.id.titleText);
        descriptionText = findViewById(R.id.descriptionText);
        categoryChip = findViewById(R.id.categoryChip);
        stepsRecyclerView = findViewById(R.id.stepsRecyclerView);
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Set up toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Get data from intent
        String title = getIntent().getStringExtra("title");
        String details = getIntent().getStringExtra("details");
        String category = getIntent().getStringExtra("category");

        // Set title and description
        titleText.setText(title);
        setAidDescription(title);
        categoryChip.setText(category);

        // Parse steps from details
        steps = parseSteps(details);

        // Set up RecyclerView
        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stepsAdapter = new StepsAdapter(steps);
        stepsRecyclerView.setAdapter(stepsAdapter);
    }

    private void initializeStepImages() {
        stepImageMap = new HashMap<>();
        
        // Burns
        stepImageMap.put("cool", R.drawable.ic_burn_treatment);
        stepImageMap.put("water", R.drawable.ic_burn_treatment);
        stepImageMap.put("jewelry", R.drawable.ic_remove_jewelry);
        stepImageMap.put("remove jewelry", R.drawable.ic_remove_jewelry);
        
        // Allergic Reactions
        stepImageMap.put("identify", R.drawable.ic_identify_allergen);
        stepImageMap.put("remove allergen", R.drawable.ic_remove_allergen);
        stepImageMap.put("antihistamine", R.drawable.ic_medicine);
        stepImageMap.put("administer", R.drawable.ic_medicine);
        
        // CPR Steps
        stepImageMap.put("chest compression", R.drawable.chest_compression);
        stepImageMap.put("compress", R.drawable.compression);
        stepImageMap.put("cpr", R.drawable.ic_cpr);
        stepImageMap.put("rescue breath", R.drawable.rescue_breath);
        stepImageMap.put("airway", R.drawable.airway);
        stepImageMap.put("breathing", R.drawable.ic_cpr);
        stepImageMap.put("circulation", R.drawable.circulation);
        stepImageMap.put("heart", R.drawable.heart);
        stepImageMap.put("pulse", R.drawable.pulse);
        stepImageMap.put("mouth-to-mouth", R.drawable.mouth_to_mouth);
        stepImageMap.put("recovery position", R.drawable.recovery_position);
        stepImageMap.put("aed", R.drawable.aed);
        stepImageMap.put("defibrillator", R.drawable.defibrillator);
        stepImageMap.put("shock", R.drawable.ic_emergency);
        stepImageMap.put("emergency", R.drawable.ic_emergency);
        stepImageMap.put("hospital", R.drawable.ic_emergency);
        stepImageMap.put("ambulance", R.drawable.ic_emergency);
        stepImageMap.put("medical", R.drawable.ic_medicine);
        stepImageMap.put("doctor", R.drawable.ic_medicine);
        stepImageMap.put("nurse", R.drawable.ic_medicine);
        stepImageMap.put("treatment", R.drawable.ic_medicine);
    }

    private List<Step> parseSteps(String details) {
        List<Step> stepsList = new ArrayList<>();
        String[] lines = details.split("\n");
        
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;
            
            // Remove step number if present (e.g., "1. ", "2. ", etc.)
            if (line.matches("^\\d+\\.\\s.*")) {
                line = line.substring(line.indexOf(".") + 1).trim();
            }
            
            // Get the appropriate image resource based on the step content
            int imageResId = getImageResourceForStep(line, i);
            
            stepsList.add(new Step(i + 1, line, imageResId));
        }
        
        return stepsList;
    }

    private int getImageResourceForStep(String step, int index) {
        String stepLower = step.toLowerCase();
        
        // Check each keyword in the step text
        for (Map.Entry<String, Integer> entry : stepImageMap.entrySet()) {
            if (stepLower.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        
        // Return default image if no match found
        return R.drawable.ic_step_default;
    }

    private void setAidDescription(String title) {
        String description;
        switch (title.toLowerCase()) {
            case "first degree burns":
                description = "First-degree burns affect only the outer layer of skin, causing redness, minor swelling and pain. These burns usually heal within a week.";
                break;
            case "second degree burns":
                description = "Second-degree burns affect both the outer and underlying layer of skin, causing severe pain, redness, swelling and blistering. These burns may take several weeks to heal.";
                break;
            case "third degree burns":
                description = "Third-degree burns affect all layers of skin and underlying tissue. The burn area may appear charred or white. Immediate medical attention is required.";
                break;
            case "allergic reactions":
                description = "An allergic reaction occurs when your immune system reacts to a foreign substance. Symptoms can range from mild to severe and potentially life-threatening.";
                break;
            case "cpr":
                description = "Cardiopulmonary resuscitation (CPR) is an emergency procedure that combines chest compressions and artificial ventilation to maintain blood flow and oxygenation during cardiac arrest.";
                break;
            case "choking":
                description = "Choking occurs when a foreign object becomes lodged in the throat or windpipe, blocking the flow of air. Quick action is essential to prevent loss of consciousness.";
                break;
            case "bleeding":
                description = "External bleeding can range from minor cuts to severe injuries. Proper first aid can prevent excessive blood loss and promote healing.";
                break;
            case "fractures":
                description = "A fracture is a broken bone. It requires immediate first aid to prevent further injury and should be properly immobilized before seeking medical attention.";
                break;
            case "heat stroke":
                description = "Heat stroke is a severe form of heat injury that occurs when the body overheats. It requires immediate cooling and medical attention.";
                break;
            case "hypothermia":
                description = "Hypothermia occurs when your body loses heat faster than it can produce it, causing a dangerously low body temperature. Immediate warming is essential.";
                break;
            case "seizures":
                description = "A seizure is a sudden, uncontrolled electrical disturbance in the brain. First aid focuses on keeping the person safe until the seizure stops.";
                break;
            case "snake bite":
                description = "Snake bites can be life-threatening. Quick, appropriate first aid and immediate medical attention are crucial for survival.";
                break;
            default:
                description = "Follow these steps carefully to provide appropriate first aid assistance.";
        }
        descriptionText.setText(description);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Step data class
    public static class Step {
        public final int number;
        public final String description;
        public final int imageResId;

        public Step(int number, String description, int imageResId) {
            this.number = number;
            this.description = description;
            this.imageResId = imageResId;
        }
    }
}
