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

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword;
    private TextView tvUsernameError, tvEmailError, tvPasswordError;
    private Button btnRegister;
    private Button btnBack;
    private AppDatabase db;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);


        db = AppDatabase.getInstance(getApplicationContext());
        userDao = db.userDao();

        etUsername = findViewById(R.id.etRegisterUsername);
        etEmail = findViewById(R.id.etRegisterEmail);
        etPassword = findViewById(R.id.etRegisterPassword);

        tvUsernameError = findViewById(R.id.tvRegisterUsernameError);
        tvEmailError = findViewById(R.id.tvRegisterEmailError);
        tvPasswordError = findViewById(R.id.tvRegisterPasswordError);

        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBack);


        btnRegister.setOnClickListener(v -> {
            clearErrors();
            if (validateInput()) {
                new Thread(() -> {
                    User userByEmail = userDao.getUserByEmail(etEmail.getText().toString().trim());
                    if (userByEmail != null) {
                        runOnUiThread(() -> {
                            tvEmailError.setText("Email đã tồn tại");
                            tvEmailError.setVisibility(View.VISIBLE);
                        });
                    } else {
                        User newUser = new User(
                                etUsername.getText().toString().trim(),
                                etPassword.getText().toString().trim(),
                                etEmail.getText().toString().trim()
                        );
                        userDao.insertUser(newUser);
                        User insertedUser = userDao.getUserByEmail(etEmail.getText().toString().trim());
                        runOnUiThread(() -> {
                            // Lưu vào SharedPreferences
                            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("email", insertedUser.email);
                            editor.putInt("userId", insertedUser.userId);
                            editor.apply();

                            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

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
        tvUsernameError.setVisibility(View.GONE);
        tvEmailError.setVisibility(View.GONE);
        tvPasswordError.setVisibility(View.GONE);
    }

    private boolean validateInput() {
        boolean isValid = true;

        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            tvUsernameError.setText("Tên người dùng không được để trống");
            tvUsernameError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        if (TextUtils.isEmpty(email)) {
            tvEmailError.setText("Email không được để trống");
            tvEmailError.setVisibility(View.VISIBLE);
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tvEmailError.setText("Email không đúng định dạng");
            tvEmailError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z]).{8,}$")) {
            tvPasswordError.setText("Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa và chữ thường");
            tvPasswordError.setVisibility(View.VISIBLE);
            isValid = false;
        }


        return isValid;
    }
}