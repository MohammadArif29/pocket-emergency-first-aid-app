package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import com.google.android.material.card.MaterialCardView;

public class SOSActivity extends AppCompatActivity {
    private static final String TAG = "SOSActivity";
    private static final int LOCATION_TIMEOUT = 10000; // 10 seconds timeout

    private MaterialCardView cardSOS;
    private Button btnSendSOS, btnToggleFlashlight, btnToggleSiren;
    private TextView tvStatus;
    private boolean isFlashOn = false;
    private boolean isSirenOn = false;
    private CameraManager cameraManager;
    private String cameraId;
    private final int REQUEST_CODE = 101;
    private final String phoneNumber = "1234567890"; // Replace with actual number
    
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Handler timeoutHandler;
    private boolean isLocationReceived = false;
    private Animation pulseAnimation;
    private MediaPlayer sirenPlayer;
    private boolean isSirenActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        setupToolbar();
        initializeViews();
        requestPermissions();
        initializeServices();
        setupClickListeners();
        setupAnimations();
        initializeSiren();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void initializeViews() {
        cardSOS = findViewById(R.id.cardSOS);
        btnSendSOS = findViewById(R.id.btnSendSOS);
        btnToggleFlashlight = findViewById(R.id.btnFlashlight);
        btnToggleSiren = findViewById(R.id.btnSiren);
        tvStatus = findViewById(R.id.tvStatus);
    }

    private void initializeSiren() {
        try {
            Log.d(TAG, "Initializing siren MediaPlayer");
            sirenPlayer = MediaPlayer.create(this, R.raw.siren);
            if (sirenPlayer != null) {
                sirenPlayer.setLooping(true);
                sirenPlayer.setVolume(1.0f, 1.0f);
                sirenPlayer.setOnPreparedListener(mp -> Log.d(TAG, "Siren MediaPlayer prepared"));
                sirenPlayer.setOnErrorListener((mp, what, extra) -> {
                    Log.e(TAG, "Siren MediaPlayer error: what=" + what + ", extra=" + extra);
                    return false;
                });
                btnToggleSiren.setEnabled(true);
                Log.d(TAG, "Siren initialized successfully");
            } else {
                Log.e(TAG, "Failed to initialize siren MediaPlayer - player is null");
                btnToggleSiren.setEnabled(false);
                showStatus("Siren not available", false);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error initializing siren: " + e.getMessage(), e);
            btnToggleSiren.setEnabled(false);
            showStatus("Siren initialization failed", false);
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.SEND_SMS,
                Manifest.permission.CAMERA
        }, REQUEST_CODE);
    }

    private void initializeServices() {
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        timeoutHandler = new Handler();

        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            Log.e(TAG, "Error accessing camera: " + e.getMessage());
        }
    }

    private void setupClickListeners() {
        btnSendSOS.setOnClickListener(v -> {
            sendSOS();
        });
        
        btnToggleFlashlight.setOnClickListener(v -> {
            if (isFlashOn) {
                turnFlashlightOff();
            } else {
                turnFlashlightOn();
            }
        });

        btnToggleSiren.setOnClickListener(v -> {
            toggleSiren();
        });
    }

    private void setupAnimations() {
        // Removed pulsing animation
    }

    private void sendSOS() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showStatus("Please enable GPS", false);
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) 
                != PackageManager.PERMISSION_GRANTED) {
            showStatus("Location permission required", false);
            return;
        }

        btnSendSOS.setEnabled(false);
        btnSendSOS.setText("Getting Location...");
        showStatus("Getting your location...", true);
        isLocationReceived = false;

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (!isLocationReceived) {
                    isLocationReceived = true;
                    timeoutHandler.removeCallbacksAndMessages(null);
                    sendLocationMessage(location);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {
                showStatus("Location provider disabled", false);
                cleanupLocation();
            }
        };

        try {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0,
                locationListener
            );

            timeoutHandler.postDelayed(() -> {
                if (!isLocationReceived) {
                    showStatus("Location request timed out", false);
                    cleanupLocation();
                }
            }, LOCATION_TIMEOUT);

        } catch (Exception e) {
            Log.e(TAG, "Error requesting location: " + e.getMessage());
            showStatus("Error getting location", false);
            cleanupLocation();
        }
    }

    private void sendLocationMessage(Location location) {
        try {
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            String message = "Emergency! I need help. My location: https://maps.google.com/?q=" + lat + "," + lon;
            SmsManager.getDefault().sendTextMessage(phoneNumber, null, message, null, null);
            showStatus("SOS Sent Successfully!", true);
            cardSOS.setCardBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        } catch (Exception e) {
            Log.e(TAG, "Error sending SMS: " + e.getMessage());
            showStatus("Error sending SOS message", false);
        } finally {
            cleanupLocation();
        }
    }

    private void cleanupLocation() {
        if (locationListener != null) {
            locationManager.removeUpdates(locationListener);
            locationListener = null;
        }
        timeoutHandler.removeCallbacksAndMessages(null);
        btnSendSOS.setEnabled(true);
        btnSendSOS.setText("SOS");
    }

    private void turnFlashlightOn() {
        try {
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                cameraManager.setTorchMode(cameraId, true);
                isFlashOn = true;
                btnToggleFlashlight.setText("Turn Off Flashlight");
                btnToggleFlashlight.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                showStatus("Flashlight ON", true);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error turning on flashlight: " + e.getMessage());
            showStatus("Flashlight not available", false);
        }
    }

    private void turnFlashlightOff() {
        try {
            cameraManager.setTorchMode(cameraId, false);
            isFlashOn = false;
            btnToggleFlashlight.setText("Turn On Flashlight");
            btnToggleFlashlight.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
            showStatus("Flashlight OFF", true);
        } catch (Exception e) {
            Log.e(TAG, "Error turning off flashlight: " + e.getMessage());
        }
    }

    private void toggleSiren() {
        if (sirenPlayer != null) {
            try {
                if (isSirenActive) {
                    Log.d(TAG, "Stopping siren");
                    sirenPlayer.pause();
                    btnToggleSiren.setText("Turn On Siren");
                    isSirenActive = false;
                } else {
                    Log.d(TAG, "Starting siren");
                    sirenPlayer.start();
                    btnToggleSiren.setText("Turn Off Siren");
                    isSirenActive = true;
                }
            } catch (IllegalStateException e) {
                Log.e(TAG, "Error toggling siren: " + e.getMessage(), e);
                showStatus("Error toggling siren", false);
                // Try to recover by reinitializing
                initializeSiren();
            }
        } else {
            Log.e(TAG, "Siren player is null when trying to toggle");
            showStatus("Siren not available", false);
            // Try to reinitialize
            initializeSiren();
        }
    }

    private void showStatus(String message, boolean isSuccess) {
        tvStatus.setText(message);
        tvStatus.setTextColor(getResources().getColor(
            isSuccess ? android.R.color.white : android.R.color.holo_red_light
        ));
        tvStatus.setBackgroundColor(getResources().getColor(
            isSuccess ? android.R.color.holo_green_dark : android.R.color.darker_gray
        ));
        
        // Add a subtle animation to the status text
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        tvStatus.startAnimation(fadeIn);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cleanupLocation();
        if (isFlashOn) {
            turnFlashlightOff();
        }
        if (sirenPlayer != null) {
            try {
                if (sirenPlayer.isPlaying()) {
                    sirenPlayer.stop();
                }
                sirenPlayer.release();
                sirenPlayer = null;
                Log.d(TAG, "Siren MediaPlayer released");
            } catch (Exception e) {
                Log.e(TAG, "Error releasing siren MediaPlayer: " + e.getMessage());
            }
        }
        if (timeoutHandler != null) {
            timeoutHandler.removeCallbacksAndMessages(null);
        }
    }
}
