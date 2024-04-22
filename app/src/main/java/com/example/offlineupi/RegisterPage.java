package com.example.offlineupi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.offlineupi.databinding.ActivityRegisterPageBinding;

public class RegisterPage extends AppCompatActivity {

    private static final int REQUEST_PHONE_CALL = 1;
    ActivityRegisterPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterPageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
        }


        SharedPreferences prefs = getSharedPreferences("login_state", MODE_PRIVATE);
        boolean isLoggedIn  = prefs.getBoolean("isLogin", false );

        if(isLoggedIn){
            binding.textView.setText(R.string.edit_info);
        }

        binding.RegisterBtn.setOnClickListener(view1 -> {

            String myName = binding.myName.getText().toString().trim();
            String myPhone = binding.myPhone.getText().toString().trim();
            String myUPIid = binding.myUPIid.getText().toString().trim();

            if(myName.isEmpty() || myPhone.isEmpty()){
                Toast.makeText(RegisterPage.this, "Fill all the Mandatory Info..", Toast.LENGTH_SHORT).show();
            } else if (!myPhone.matches("\\d{10}")) {
                Toast.makeText(RegisterPage.this, "Phone number should be a 10-digit number", Toast.LENGTH_SHORT).show();
            } else if (!myUPIid.isEmpty() && (!myUPIid.contains("@") || myUPIid.startsWith("@") || myUPIid.endsWith("@"))) {
                Toast.makeText(RegisterPage.this, "Invalid UPI ID", Toast.LENGTH_SHORT).show();
            }else {
                SharedPreferences preferences = getSharedPreferences("UserData", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("myName", myName);
                editor.putString("myPhone", myPhone);
                editor.putString("myUPIid", myUPIid);
                editor.apply();

                // Move to next activity
                // For example:


                if (!isLoggedIn) {
                    makeCall();
                } else {
                    startActivity(new Intent(RegisterPage.this, Menu.class));
                }

            }


        });


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PHONE_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied. Phone functionality may not work.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void makeCall() {
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
//            Intent intent = new Intent(Intent.ACTION_CALL);
//            intent.setData(Uri.parse("tel:" + Uri.encode("*99#")));
//            startActivity(intent);
            startActivity(new Intent(RegisterPage.this, SetPinActivity.class));
        } else {
            // Permission not granted, request the permission
            Toast.makeText(this, "Permission denied. Phone functionality may not work.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Retrieve and populate EditT ext fields with saved data
        SharedPreferences preferences = getSharedPreferences("UserData", MODE_PRIVATE);
        binding.myName.setText(preferences.getString("myName", ""));
        binding.myPhone.setText(preferences.getString("myPhone", ""));
        binding.myUPIid.setText(preferences.getString("myUPIid", ""));
    }
}