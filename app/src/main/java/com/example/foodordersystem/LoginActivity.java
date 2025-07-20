package com.example.foodordersystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodordersystem.data.dao.UserDao;
import com.example.foodordersystem.data.database.AppDatabase;
import com.example.foodordersystem.data.entity.User;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    TextView tvEmailError, tvPasswordError;
    Button btnLogin;
    Button btnBack;

    boolean isValid = true;

    private AppDatabase db;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etEmail = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPassword);
        tvEmailError = findViewById(R.id.tvLoginEmailError);
        tvPasswordError = findViewById(R.id.tvLoginPasswordError);
        btnLogin = findViewById(R.id.btnLogin);
        btnBack = findViewById(R.id.btnBack);


        db = AppDatabase.getInstance(getApplicationContext());
        userDao = db.userDao();

        btnLogin.setOnClickListener(v -> {
            clearErrors();
            if (validateInput()) {
                new Thread(() -> {
                    User user = userDao.getUserByEmail(etEmail.getText().toString().trim());
                    if (user == null|| !user.password.equals(etPassword.getText().toString().trim())) {
                        runOnUiThread(() -> {
                            tvPasswordError.setVisibility(View.VISIBLE);
                            tvPasswordError.setText("Sai email hoặc mật khẩu ");

                        });
                    } else {

                        runOnUiThread(() -> {
                            // Lưu vào SharedPreferences
                            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("email", user.email);
                            editor.putInt("userId", user.userId);
                            editor.apply();
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        });
                    }
                }).start();
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void clearErrors() {
            tvEmailError.setVisibility(View.GONE);
            tvPasswordError.setVisibility(View.GONE);
    }
    private boolean validateInput() {
        boolean isValid = true;


        if (etEmail.getText().toString().trim().isEmpty()){
            tvEmailError.setVisibility(View.VISIBLE);
            tvEmailError.setText("Email không được để trống");
            isValid = false;
        }

        if (etPassword.getText().toString().trim().isEmpty()){
            tvPasswordError.setVisibility(View.VISIBLE);
            tvPasswordError.setText("Mật khẩu không được để trống");
            isValid = false;
        }


        return isValid;
    }
}