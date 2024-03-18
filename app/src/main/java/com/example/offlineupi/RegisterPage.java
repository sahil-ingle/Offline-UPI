package com.example.offlineupi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.offlineupi.databinding.ActivityRegisterPageBinding;

public class RegisterPage extends AppCompatActivity {

    ActivityRegisterPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterPageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String myName = binding.myName.getText().toString().trim();
                String myPhone = binding.myPhone.getText().toString().trim();
                String myUPIid = binding.myUPIid.getText().toString().trim();

                if(myName.isEmpty() || myPhone.isEmpty()){
                    Toast.makeText(RegisterPage.this, "Fill all the Mandatory Info..", Toast.LENGTH_SHORT).show();
                }
                else{
                    SharedPreferences preferences = getSharedPreferences("UserData", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("myName", myName);
                    editor.putString("myPhone", myPhone);
                    editor.putString("myUPIid", myUPIid);
                    editor.apply();

                    // Move to next activity
                    // For example:
                    startActivity(new Intent(RegisterPage.this, MainActivity.class));

                }


            }
        });


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