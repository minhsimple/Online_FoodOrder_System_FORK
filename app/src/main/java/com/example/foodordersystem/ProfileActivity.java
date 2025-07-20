package com.example.foodordersystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String email = prefs.getString("email", "");
        int userId = prefs.getInt("userId", -1);

        TextView tvEmail = findViewById(R.id.tvProfileEmail);
        TextView tvUserId = findViewById(R.id.tvProfileUserId);
        Button btnLogout = findViewById(R.id.btnLogout);

        tvEmail.setText(email);
        tvUserId.setText(String.valueOf(userId));

        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
